/*
 * Copyright (C) 2016 Dieter J Kybelksties
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * @date: 2016-10-13
 * @author: Dieter J Kybelksties
 */
package com.kybelksties.process;

import com.kybelksties.general.SystemProperties;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Logger;

/**
 *
 * @author Dieter J Kybelksties
 */
public class ProcessClient
{

    private static final Class CLAZZ = ProcessClient.class;
    private static final String CLASS_NAME = CLAZZ.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    final static String HOSTNAME = (String) SystemProperties.get("HOSTNAME");

    /**
     * Implements the connection logic by prompting the end user for the
     * server's IP address, connecting, setting up streams, and consuming the
     * welcome messages from the server.
     *
     * @param serverAddress
     * @param port          port number on which to communicate
     * @return
     * @throws java.io.IOException
     * @throws java.lang.ClassNotFoundException
     */
    public ProcessMessage connectToServer(String serverAddress, int port)
            throws IOException,
                   ClassNotFoundException
    {

        if (serverAddress.isEmpty())
        {
            serverAddress = "localhost";
        }
        // Make connection and initialize streams
        socket = new Socket(serverAddress, port);
        out = new ObjectOutputStream(socket.getOutputStream());
        out.flush();
        in = new ObjectInputStream(socket.getInputStream());

        out.writeObject(ProcessMessage.makeIdentify(port, HOSTNAME));
        return readObject();
    }

    /**
     *
     * @param sendMsg
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void sendMessage(ProcessMessage sendMsg)
            throws IOException, ClassNotFoundException
    {
        out.writeObject(sendMsg);
        out.flush();
        while (in.available() == 0)
        {
            System.out.println("waiting for response...");
        }
        ProcessMessage rcvdMsg = readObject();
//            switch (cmd.getType())
//            {
//                case ChitChat:
//                    System.exit(0);
//                    break;
//                case StopServer:
//                    System.exit(0);
//                    break;
//                case Identify:
//                    System.exit(0);
//                    break;
//                case StartProcess:
//                    System.exit(0);
//                    break;
//                case ListProcesses:
//                    System.exit(0);
//                    break;
//                case ProcessList:
//                    System.exit(0);
//                    break;
//                case KillProcess:
//                    System.exit(0);
//                    break;
//                case RestartProcess:
//                    System.exit(0);
//                    break;
//            }

    }

    /**
     *
     * @return @throws IOException
     * @throws java.lang.ClassNotFoundException
     */
    public ProcessMessage readObject() throws IOException,
                                              ClassNotFoundException
    {
        return in.available() != 0 ?
               (ProcessMessage) in.readObject() :
               ProcessMessage.makeInvalid();
    }
}
