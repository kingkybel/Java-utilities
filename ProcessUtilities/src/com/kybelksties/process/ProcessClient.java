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

import com.kybelksties.protocol.ProcessMessage;
import com.kybelksties.general.SystemProperties;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
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
    private static final String LOCALHOST = localHostName();
    String serverAddress = null;
    int port = 9898;
    private boolean connected = false;

    static private String localHostName()
    {
        String reval = (String) SystemProperties.get("HOSTNAME");
        return reval == null || reval.isEmpty() ? "localhost" : reval;
    }

    /**
     * Creates new ProcessClient.
     *
     * @param serverAddress TCP-string representation of a server name.
     * @param port          port number
     * @throws java.io.IOException when the server address is null or empty
     */
    public ProcessClient(String serverAddress, int port)
            throws IOException
    {
        this.serverAddress = serverAddress;
        this.port = port;
        if (this.serverAddress == null || this.serverAddress.isEmpty())
        {
            throw new IOException("Serveraddress cannot be empty string or null");
        }
    }

    /**
     * Implements the connection logic by prompting the end user for the
     * server's IP address, connecting, setting up streams, and consuming the
     * welcome messages from the server.
     *
     * @return a (possibly empty, but not null) list of ProcessMessage s -
     *         welcome from the server
     * @throws IOException            thrown if message cannot be read or the
     *                                response message is garbled
     * @throws ClassNotFoundException thrown when the read-operation from the
     *                                socket's input-stream cannot be
     *                                de-serialised
     */
    public ArrayList<ProcessMessage> connectToServer()
            throws IOException,
                   ClassNotFoundException
    {
        ArrayList<ProcessMessage> reval = new ArrayList<>();
        // Make connection and initialize streams
        socket = new Socket(serverAddress, port);
        out = new ObjectOutputStream(socket.getOutputStream());
        out.flush();
        in = new ObjectInputStream(socket.getInputStream());

        out.writeObject(ProcessMessage.makeIdentify(LOCALHOST, port));
        ProcessMessage msg;

        // read the welcome messages from the server
        msg = (ProcessMessage) in.readObject();
        reval.add(msg);
        connected = true;
        return reval;
    }

    /**
     * Send the given message serialised over the socket's output-stream.
     *
     * @param sendMsg the message to be sent
     * @return response message from the server
     * @throws IOException            thrown if message cannot be sent or the
     *                                response message is garbled
     * @throws ClassNotFoundException thrown when the read-operation from the
     *                                socket's input-stream cannot be
     *                                de-serialised
     */
    public ProcessMessage sendMessage(ProcessMessage sendMsg)
            throws IOException,
                   ClassNotFoundException
    {
        Object response;
        ProcessMessage rcvdMsg = null;
        if (!connected)
        {
            connectToServer();
        }
        out.writeObject(sendMsg);
        out.flush();
        try
        {
            response = in.readObject();
            if (response == null || !(response instanceof ProcessMessage))
            {
                throw new IOException("Not a Process Command!");
            }
            rcvdMsg = (ProcessMessage) response;
        }
        catch (IOException | ClassNotFoundException ex)
        {
            rcvdMsg = ProcessMessage.makeInvalid(ex.toString());
        }

        return rcvdMsg;
    }

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
