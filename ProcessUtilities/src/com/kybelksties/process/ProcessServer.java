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
 * @date: 2016-10-05
 * @author: Dieter J Kybelksties
 */
package com.kybelksties.process;

import com.kybelksties.general.SystemProperties;
import com.kybelksties.general.ToString;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jutils.jprocesses.JProcesses;
import org.jutils.jprocesses.model.ProcessInfo;

/**
 * A server program which accepts requests from clients to handle (remote)
 * processes. When clients connect, a new thread is started to handle an
 * interactive dialog in which the client sends in a command and the server
 * thread sends back the a response.
 *
 * @author Dieter J Kybelksties
 */
public class ProcessServer
{

    private static final Class CLAZZ = ProcessServer.class;
    private static final String CLASS_NAME = CLAZZ.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    static ObjectOutputStream out = null;
    static ObjectInputStream in = null;
    static boolean keepRunning = true;
    final static String HOSTNAME = (String) SystemProperties.get("HOSTNAME");

    /**
     * Application method to run the server runs in an infinite (interuptable)
     * loop listening on a given port. When a connection is requested, it spawns
     * a new thread to do the servicing and immediately returns to listening.
     * The server keeps a unique client number for each client that connects
     * just to show interesting logging messages. It is certainly not necessary
     * to do this.
     *
     * @param args first argument is the port
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception
    {
        int clientNumber = 0;
        int port = (args == null || args.length == 0) ?
                   9898 :
                   Integer.parseInt(args[0]);
        logInfo("Starting Server");
        ServerSocket listener = new ServerSocket(port);
        while (keepRunning)
        {
            Socket acceptedSocket = listener.accept();
            out = new ObjectOutputStream(acceptedSocket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(acceptedSocket.getInputStream());

            // accept is blocking until incoming request received
            new ServerLoop(acceptedSocket, clientNumber++).start();
        }
        logInfo("Finished Server");
    }

    public static List<ProcessInfo> list()
    {
        List<ProcessInfo> processesList = JProcesses.getProcessList();
        logInfo("in list: {0} processes running.", processesList.size());
        return processesList;
    }

    /**
     * A private thread to handle the client requests on a particular socket.
     * The client terminates the dialogue by sending a STOP-command.
     */
    private static class ServerLoop extends Thread
    {

        private int clientNumber = 0;
        private Socket socket = null;

        public ServerLoop(Socket socket, int clientNumber) throws Exception
        {
            try
            {
                if (socket == null)
                {
                    throw new Exception("Socket cannot be null");
                }
                if (socket.isClosed())
                {
                    throw new Exception("Socket should not be closed but is!!");
                }
                this.socket = socket;
                this.clientNumber = clientNumber;
                // Send a welcome message to the client.
                ProcessMessage msg = ProcessMessage.makeChitChat(
                               "Hello client '" +
                               clientNumber +
                               "'! You are connected to server at " +
                               this.socket.getInetAddress().
                               getCanonicalHostName());
                out.writeObject(msg);
                out.flush();
            }
            catch (IOException ex)
            {
                logError("in ServerSocket: " + ex.toString());
            }
        }

        /**
         * Services this thread's client by first sending the client a welcome
         * message then repeatedly reading commands and sending back the
         * results.
         */
        @Override
        public void run()
        {
            try
            {

                // Get message object from the client
                while (keepRunning)
                {
                    Object readObj = in.readObject();
                    ProcessMessage rcvdMsg = (ProcessMessage) readObj;
                    if (readObj == null)
                    {
                        logInfo("the read object is invalid!!!");
                        break;
                    }

                    ArrayList objs = rcvdMsg.getObjects();
                    switch (rcvdMsg.getType())
                    {
                        case Ack:
                            break;
                        case ChitChat:
                            logInfo(ToString.make(objs));
                            break;
                        case StopServer:
                            logInfo("Bye, bye!");
                            keepRunning = false;
                            break;
                        case Identify:
                            logInfo("Connected to port {0} on {1}",
                                    objs.toArray());
                            break;
                        case StartProcess:
                            ScheduledProcess sp = (ScheduledProcess) objs.get(
                                             0);
                            if (HOSTNAME.equals(sp.getTargetMachine()))
                            {
                                ConcreteProcess process = sp.start();
                            }
                            break;
                        case ProcessList:
                            logInfo("ProcessList:");
                            System.exit(0);
                            break;
                        case ListProcesses:
                            ProcessMessage msg = new ProcessMessage(
                                           ProcessMessage.Type.ProcessList,
                                           (ArrayList<ProcessInfo>) list());
                            out.writeObject(msg);
                            out.flush();
                            logInfo("After flush");
                            break;
                        case KillProcess:
                            System.exit(0);
                            break;
                        case RestartProcess:
                            System.exit(0);
                            break;
                    }

                }
            }
            catch (IOException | ClassNotFoundException e)
            {
                logError("Error handling client {0}: {1}",
                         clientNumber,
                         ToString.make(e.toString()));
            }
            finally
            {
                try
                {
                    logInfo("trying to close socket");
                    socket.shutdownInput();
                    socket.shutdownOutput();
                    socket.close();
                }
                catch (IOException e)
                {
                    logError("Couldn't close the socket: {0}",
                             e.getLocalizedMessage());
                }
                logInfo("Connection with client {0} closed", clientNumber);
            }
        }

    }

    /**
     * Logs a simple message. In this case we just write the message to the
     * server applications standard output.
     */
    static void logInfo(String message, Object... objs)
    {
        LOGGER.log(Level.INFO, message, objs);
    }

    /**
     * Logs a simple message. In this case we just write the message to the
     * server applications standard output.
     */
    static void logError(String message, Object... objs)
    {
        LOGGER.log(Level.SEVERE, message, objs);
    }
}
