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

import com.kybelksties.general.LogFormatter;
import com.kybelksties.general.SystemProperties;
import com.kybelksties.general.ToString;
import com.kybelksties.protocol.ProcessMessage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.ConsoleHandler;
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
public class ProcessServer implements ConcreteProcess.StateEventListener
{

    private static final Class CLAZZ = ProcessServer.class;
    private static final String CLASS_NAME = CLAZZ.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    static boolean keepRunning = true;
    final static String HOSTNAME = (String) SystemProperties.get("HOSTNAME");
    static int port;
    static Map<Integer, ServerLoop> map = new HashMap<>();
    static Map<String, ConcreteProcess> monitoredProcesses = new TreeMap<>();
    static long processNumber = 0;

    static
    {

        LOGGER.setUseParentHandlers(false);
        LogFormatter formatter = new LogFormatter();
        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(formatter);
        LOGGER.addHandler(handler);
    }

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
        port = (args == null || args.length == 0) ?
               9898 :
               Integer.parseInt(args[0]);
        logInfo("Starting Server");
        ServerSocket listener = new ServerSocket(port);
        while (keepRunning)
        {
            // blocking here, waiting for incoming request, then accept
            Socket acceptedSocket = listener.accept();

            if (keepRunning)
            {
                ServerLoop s = new ServerLoop(acceptedSocket, clientNumber);
                map.put(clientNumber, s);
                s.start();
                clientNumber++;
            }
        }
        for (Integer s : map.keySet())
        {
            ServerLoop loop = map.remove(s);
            loop.interrupt();
            logInfo(loop.getName() + " interrupted and removed.");
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

        ObjectOutputStream out = null;
        ObjectInputStream in = null;
        private int clientNumber = 0;
        private Socket socket = null;
        private String clientServerAddress = "";
        private int clientPort = 0;

        public ServerLoop(Socket socket, int clientNumber) throws Exception
        {
            setName("'ServerLoop for client " + clientNumber + "'");
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());

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
                // Expecting an Identification from the client
                ProcessMessage idMsg = (ProcessMessage) in.readObject();
                logInfo(idMsg.toString());
                clientServerAddress = (String) idMsg.getObjects().get(0);
                clientPort = (Integer) idMsg.getObjects().get(1);

                // Send a welcome message to the client.
                ProcessMessage msg = ProcessMessage.makeChitChat(
                               "Hello client '" +
                               clientNumber +
                               "'. Received ID: " +
                               ToString.make(idMsg.getObjects()) +
                               ". You are connected to server at " +
                               HOSTNAME);
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
                ProcessMessage msg = new ProcessMessage(ProcessMessage.Type.Ack);

                while (keepRunning)
                {
                    logInfo("In While {0}", getName());
                    // Get message object from the client
                    Object readObj = in.readObject();
                    if (readObj == null)
                    {
                        logInfo("the read object is invalid!!!");
                        keepRunning = false;
                        break;
                    }

                    ProcessMessage rcvdMsg = (ProcessMessage) readObj;
                    ArrayList objs = rcvdMsg.getObjects();
//                    if (rcvdMsg.isInstruction())
                    {
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
                                msg = ProcessMessage.makeAcknowledge();
                                break;
                            case Identify:
                                logInfo("Connected to port {0} on {1}",
                                        objs.toArray());
                                break;
                            case StartProcess:
                                try
                                {
                                    ScheduledProcess sp =
                                                     (ScheduledProcess) objs.
                                                     get(0);

                                    if (HOSTNAME.equals(sp.getTargetMachine()))
                                    {
                                        String ID =
                                               "(" + processNumber++ + ") " +
                                               clientServerAddress +
                                               ":" + clientPort + "-" +
                                               sp.getExeDefinition().
                                               getName();

                                        ConcreteProcess ps = sp.start();
                                        ps.addStateChangeEventListener(
                                                (ConcreteProcess.StateEventListener) this);
                                        msg = ProcessMessage.makeAcknowledge(
                                        ps.state, "Server ID=" + ID);
                                        monitoredProcesses.put(ID,
                                                               sp.getProcess());
                                    }
                                }
                                catch (ClassCastException e)
                                {
                                    msg = ProcessMessage.makeInvalid(
                                    e.toString());
                                }
                                break;
                            case ProcessList:
                                logInfo("ProcessList:");
                                System.exit(0);
                                break;
                            case ListProcesses:
                                msg = new ProcessMessage(
                                ProcessMessage.Type.ProcessList,
                                (ArrayList<ProcessInfo>) list());
                                break;
                            case KillProcess:
                                System.exit(0);
                                break;
                            case RestartProcess:
                                System.exit(0);
                                break;
                        }
                        out.writeObject(msg);
                        out.flush();
                    }

                }
            }
            catch (IOException | ClassNotFoundException | ClassCastException e)
            {
                logError("Error handling client {0}: {1}",
                         clientNumber,
                         ToString.make(e.toString()));
            }
            finally
            {
                if (!keepRunning)
                {
                    interrupt();
                }
                try
                {
                    socket.shutdownInput();
                    socket.shutdownOutput();
                    socket.close();
                }
                catch (IOException e)
                {
                    logError("Couldn't close the socket: {0}",
                             e.toString());
                }
            }
        }

    }

    @Override
    public void processStateChanged(ConcreteProcess.StateEvent evt)
    {
        ConcreteProcess ps = (ConcreteProcess) evt.getSource();
//        ps.state
    }

    /**
     * Logs an info-style simple message.
     *
     * @param message message(-format): can have ordinal replacement
     *                place-holders "{0}, {1},..."
     * @param objs    possibly empty list of objects used to replace
     *                place-holders
     */
    static void logInfo(String message, Object... objs)
    {
        LOGGER.log(Level.INFO, message, objs);
    }

    /**
     * Logs an error-style simple message.
     *
     * @param message message(-format): can have ordinal replacement
     *                place-holders "{0}, {1},..."
     * @param objs    possibly empty list of objects used to replace
     *                place-holders
     */
    static void logError(String message, Object... objs)
    {
        LOGGER.log(Level.SEVERE, message, objs);
    }

}
