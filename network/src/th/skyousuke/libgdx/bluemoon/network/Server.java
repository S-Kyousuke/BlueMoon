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

import com.badlogic.gdx.utils.ObjectMap;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;

/**
 * @author S.Kyousuke <surasek@gmail.com>
 */

public class Server extends Communicator {

    private ObjectMap<String, InetSocketAddress> clientAddresses;

    public Server() throws SocketException {
        super("server");
        clientAddresses = new ObjectMap<>();
    }

    @Override
    protected void processReceivedPacket() throws IOException {
        if (receivedPacket != null) {
            switch (receivedPacket.getCommand()) {
                case CONNECT:
                    addClient(receivedPacket);
                    break;
                case DISCONNECT:
                    removeClient(receivedPacket);
                    break;
                case MESSAGE:
                    System.out.println(receivedPacket.getName() + ": " + receivedPacket.getData());
                    break;
                case CHECK:
                    confirmClient(receivedPacket);
                    break;
            }
        }
    }

    public void broadcast(String message) throws IOException {
        for (InetSocketAddress address : clientAddresses.values()) {
            GamePacket gamePacket = new GamePacket(name, Command.MESSAGE, message, address);
            sendPacket(gamePacket);
        }
        System.out.println("Broadcast: " + message);
    }

    public void showClientList() {
        System.out.println("Current client List:");
        int i = 1;
        for (ObjectMap.Entry entry : clientAddresses.entries()) {
            System.out.println(i + ". " + String.valueOf(entry));
            ++i;
        }
    }

    /**
     * @return true if the client was successfully added.
     */
    public boolean addClient(GamePacket clientPacket) throws IOException {
        if (!clientAddresses.containsKey(clientPacket.getName())) {
            clientAddresses.put(clientPacket.getName(), clientPacket.getAddress());
            System.out.println('\"' + clientPacket.getName() + "\" is connected to server.");
            sendPacket(Command.ACCEPT, "", clientPacket.getAddress());
            return true;
        }
        return false;
    }

    public void removeClient(GamePacket clientPacket) {
        clientAddresses.remove(clientPacket.getName());
        System.out.println('\"' + clientPacket.getName() + "\" is disconnected from server.");
    }

    private void confirmClient(GamePacket clientPacket) throws IOException {
        sendPacket(Command.OK, "", clientPacket.getAddress());
    }

}
