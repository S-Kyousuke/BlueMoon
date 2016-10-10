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

import java.net.DatagramPacket;
import java.net.InetSocketAddress;

/**
 * @author S.Kyousuke <surasek@gmail.com>
 */

public class GamePacket {

    private final String name;
    private final Command command;
    private final String data;

    private final InetSocketAddress address;

    public static GamePacket build(DatagramPacket datagramPacket) {
        final String packetData = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
        final StringBuilder sb = new StringBuilder();
        final int length = packetData.length();
        String name = null;
        Command command = null;
        int i = 0;
        for (; i< length; ++i) {
            final char c = packetData.charAt(i);
            if (c != '#') sb.append(c);  // # is delimiter.
            else {
                if (name == null) name = sb.toString();
                else command = Command.getCommand(sb.toString());
                sb.setLength(0);
            }
        }
        if (name != null && command != null)
            return new GamePacket(name, command, sb.toString(), (InetSocketAddress) datagramPacket.getSocketAddress());

        System.out.println("GamePacket build(): Wrong datagram packet: " + packetData);
        return null;
    }

    public GamePacket(String name, Command command, String data, InetSocketAddress address) {
        if (data.isEmpty()) data += ' ';
        this.name = name;
        this.command = command;
        this.data = data;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public Command getCommand() {
        return command;
    }

    public String getData() {
        return data;
    }

    public InetSocketAddress getAddress() {
        return address;
    }

    public DatagramPacket getDatagramPacket() {
        final byte[] packetDataBytes = getPacketData().getBytes();
        return new DatagramPacket(packetDataBytes, packetDataBytes.length, address);
    }

    public String getPacketData() {
        return name + '#' + command.getText() + '#' + data;
    }

    @Override
    public String toString() {
        return getPacketData() + ' ' + address.getAddress().getHostAddress();
    }


}
