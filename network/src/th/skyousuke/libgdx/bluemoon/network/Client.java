/*
 * Copyright 2016 Surasek Nusati <surasek@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package th.skyousuke.libgdx.bluemoon.network;

import java.io.IOException;
import java.net.SocketException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author S.Kyousuke <surasek@gmail.com>
 */

public class Client extends Communicator {

    private static final int SERVER_TIMEOUT = 5; // Seconds

    private CountDownLatch acceptCountDown;
    private ScheduledExecutorService executor;
    private boolean online;
    private int waitingOkPacket;

    public Client(String name) throws SocketException {
        super(name);
    }

    private void sendPacketToServer(Command command, String data) throws IOException {
        sendPacket(command, data, ServerLauncher.address);
    }

    /**
     * @return true if successfully connected to server or already connected to server.
     */
    public boolean connectToServer() {
        if (online) return true;
        try {
            sendPacketToServer(Command.CONNECT, "");
            System.out.println("Waiting reply from server... ");
            acceptCountDown = new CountDownLatch(1);
            acceptCountDown.await(SERVER_TIMEOUT, TimeUnit.SECONDS);
            if (acceptCountDown.getCount() == 0) {
                System.out.println("Successfully connected to server.");
                online = true;
                startConnectionCheck();
                return true;
            }
            else {
                System.out.println("Connection timeout!");
                return false;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void startConnectionCheck() {
        waitingOkPacket = 0;
        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> {
            try {
                if (waitingOkPacket > 3) {
                    System.out.println("Lost connected to server!");
                    online = false;
                    stopConnectionCheck();
                    return;
                }
                sendPacketToServer(Command.CHECK, "");
                ++waitingOkPacket;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, SERVER_TIMEOUT, SERVER_TIMEOUT, TimeUnit.SECONDS);
    }

    private void stopConnectionCheck() {
        if (executor != null) executor.shutdown();
    }

    public void disconnectFromServer() {
        if (online) {
            try {
                sendPacketToServer(Command.DISCONNECT, "");
                stopConnectionCheck();
                online = false;
                System.out.println("Disconnected from server.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isOnline() {
        return online;
    }

    @Override
    protected void processReceivedPacket() throws IOException {
        if (receivedPacket != null) {
            switch (receivedPacket.getCommand()) {
                case ACCEPT:
                    if (acceptCountDown != null) {
                        acceptCountDown.countDown();
                    }
                    break;
                case OK:
                    waitingOkPacket = 0;
                    break;
                case MESSAGE:
                    System.out.println(receivedPacket.getName() + ": " + receivedPacket.getData());
                    break;
            }
        }
    }

    public void sendMessageToServer(String message) throws IOException {
        sendPacketToServer(Command.MESSAGE, message);
    }
}
