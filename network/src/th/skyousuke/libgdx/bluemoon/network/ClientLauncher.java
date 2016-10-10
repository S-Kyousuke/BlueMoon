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
import java.util.Scanner;

/**
 * @author S.Kyousuke <surasek@gmail.com>
 */

public class ClientLauncher {


    public static void main(String[] arg) throws IOException {

        Scanner scanner = new Scanner(System.in);
        System.out.print("Name: ");
        String name = scanner.nextLine();

        Client client = new Client(name);
        client.start();
        client.connectToServer();

        while (client.isOnline()) {
            String message = scanner.nextLine();
            if (message.isEmpty())
                break;
            client.sendMessageToServer(message);
        }

        client.disconnectFromServer();
        client.stop();
        scanner.close();
    }
}
