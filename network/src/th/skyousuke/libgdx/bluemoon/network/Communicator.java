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
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

/**
 * @author S.Kyousuke <surasek@gmail.com>
 */

public abstract class Communicator implements Runnable{

    protected final String name;
    protected GamePacket receivedPacket;

    private final DatagramPacket receivedDatagramPacket;
    private DatagramSocket socket;

    public Communicator(String name) throws SocketException {
        this.name = name;
        byte[] buffer = new byte[1024];
        receivedDatagramPacket = new DatagramPacket(buffer, buffer.length);
    }

    protected void sendPacket(Command command, String data, InetSocketAddress address) throws IOException {
        GamePacket packet = new GamePacket(name, command, data, address);
        sendPacket(packet);
    }

    protected void sendPacket(GamePacket packet) throws IOException {
        socket.send(packet.getDatagramPacket());
    }

    public void start() {
        start(0);
    }

    public void start(int port) {
        try {
            socket = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        new Thread(this).start();
    }

    public void stop() {
        socket.close();
        socket = null;
    }

    @Override
    public void run() {
        while (true) {
            try {
                socket.receive(receivedDatagramPacket);
                receivedPacket = GamePacket.build(receivedDatagramPacket);
                processReceivedPacket();
            } catch (IOException e) {
                if (socket != null)
                    e.printStackTrace();
                break;
            }
        }
    }

    protected abstract void processReceivedPacket() throws IOException;

}
