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

public class Channel implements Runnable {

    private DatagramSocket socket;
    private boolean running;

    public void bind(int port) throws SocketException {
        socket = new DatagramSocket(port);
    }

    public void start() {
        new Thread(this).start();
    }

    public void stop() {
        running = false;
        socket.close();
    }

    @Override
    public void run() {
        byte[] buffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        running = true;
        while (running) {
            try {
                socket.receive(packet);
                String message = new String(packet.getData(), 0, packet.getLength());
                System.out.println(message);
            } catch (IOException e) {
                System.out.print("Catch!");
                break;
            }
        }
    }

    public void sendMessage(String message, InetSocketAddress address) throws IOException {
        DatagramPacket packet = new DatagramPacket(message.getBytes(), message.getBytes().length);
        packet.setSocketAddress(address);
        socket.send(packet);
    }
}
