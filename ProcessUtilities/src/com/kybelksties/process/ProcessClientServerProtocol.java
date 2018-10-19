package com.kybelksties.process;

import com.kybelksties.protocol.Actor;
import com.kybelksties.protocol.Protocol;
import java.util.logging.Logger;
import org.openide.util.Exceptions;

/*
 * Copyright (C) 2018 Dieter J Kybelksties
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
 * @date: 2018-03-05
 * @author: Dieter J Kybelksties
 */
/**
 *
 * @author Dieter J Kybelksties
 */
public class ProcessClientServerProtocol extends Protocol
{

    private static final Class CLAZZ = ProcessClientServerProtocol.class;
    private static final String CLASS_NAME = CLAZZ.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    final static public Actor SERVER_INITIAL = new Actor("Server",
                                                         ProcessState.initial());
    final static public Actor SERVER_READY = new Actor("Server",
                                                       ProcessState.ready());
    final static public Actor SERVER_SENDING_ACK = new Actor(
                              "Server",
                              ProcessState.sendingAck());
    final static public Actor SERVER_ERROR = new Actor("Server",
                                                       ProcessState.error());
    final static public Actor CLIENT_INITIAL = new Actor("Client",
                                                         ProcessState.initial());
    final static public Actor CLIENT_READY = new Actor("Client",
                                                       ProcessState.ready());
    final static public Actor CLIENT_ERROR = new Actor("Client",
                                                       ProcessState.error());
    final static public Actor CLIENT_SENDING_ACK = new Actor(
                              "Client",
                              ProcessState.sendingAck());
    static private ProcessClientServerProtocol instance = null;

    private ProcessClientServerProtocol()
    {
    }

    static public ProcessClientServerProtocol get()
    {
        if (instance == null)
        {
            instance = new ProcessClientServerProtocol();
            try
            {
                instance.addRule(CLIENT_INITIAL,
                                 ExeMessageType.bootstrap(),
                                 ProcessState.ready(),
                                 CLIENT_INITIAL);
                instance.addRule(CLIENT_INITIAL,
                                 ExeMessageType.bootstrap(),
                                 ProcessState.error(),
                                 CLIENT_INITIAL);
                instance.addRule(CLIENT_INITIAL,
                                 ExeMessageType.bootstrap(),
                                 ProcessState.error(),
                                 CLIENT_ERROR);
                instance.addRule(CLIENT_ERROR,
                                 ExeMessageType.bootstrap(),
                                 ProcessState.initial(),
                                 CLIENT_ERROR);
                instance.addRule(CLIENT_SENDING_ACK,
                                 ExeMessageType.acknowledge(),
                                 ProcessState.ready(),
                                 CLIENT_READY);
                instance.addRule(CLIENT_READY,
                                 ExeMessageType.startProcess(),
                                 ProcessState.executing(),
                                 Actor.ANY_ACTOR);
                instance.addRule(SERVER_INITIAL,
                                 ExeMessageType.bootstrap(),
                                 ProcessState.ready(),
                                 SERVER_READY);
                instance.addRule(SERVER_READY,
                                 ExeMessageType.acknowledge(),
                                 ProcessState.ready(),
                                 CLIENT_READY);
                instance.addRule(CLIENT_READY,
                                 ExeMessageType.chitChat(),
                                 ProcessState.ready(),
                                 Actor.ANY_ACTOR);
            }
            catch (Exception ex)
            {
                Exceptions.printStackTrace(ex);
            }
        }
        return instance;
    }
}
