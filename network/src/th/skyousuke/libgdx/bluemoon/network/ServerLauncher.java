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
import java.net.InetSocketAddress;
import java.util.Scanner;

/**
 * @author S.Kyousuke <surasek@gmail.com>
 */

public class ServerLauncher {

    public static final InetSocketAddress address = new InetSocketAddress("127.0.0.1", 6612);

    public static void main(String[] arg) throws IOException {

        Scanner scanner = new Scanner(System.in);
        Server server = new Server();
        server.start(address.getPort());
        System.out.println("Server is running...");

        mainLoop: while (true) {
            String message = scanner.nextLine();
            switch (message) {
                case "online":
                    server.showClientList();
                    break;
                case "":
                    break mainLoop;
                default:
                    server.broadcast(message);
            }
        }
        server.stop();
        scanner.close();
    }

}
