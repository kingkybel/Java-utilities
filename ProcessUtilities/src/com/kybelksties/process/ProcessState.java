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
package com.kybelksties.process;

import com.kybelksties.protocol.ProtocolException;
import com.kybelksties.protocol.State;
import java.util.logging.Logger;

/**
 *
 * @author Dieter J Kybelksties
 */
public class ProcessState extends State
{

    private static final Class CLAZZ = ProcessState.class;
    private static final String CLASS_NAME = CLAZZ.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    enum States
    {

        Initial, Ready, SendingAck, SendingHeartbeat, Executing, Error;
    };

    private static State initial = initial();
    private static State ready = ready();
    private static State sendingAck = sendingAck();
    private static State sendingHeartbeat = sendingHeartbeat();
    private static State executing = executing();
    private static State error = error();

    private ProcessState(States state) throws ProtocolException
    {
        super(state.name());
    }

    public static State initial()
    {
        if (initial == null)
        {
            try
            {
                initial = new ProcessState(States.Initial);
            }
            catch (ProtocolException ex)
            {

            }
        }
        return initial;
    }

    public static State ready()
    {
        if (ready == null)
        {
            try
            {
                ready = new ProcessState(States.Ready);
            }
            catch (ProtocolException ex)
            {

            }
        }
        return ready;
    }

    static State error()
    {
        if (error == null)
        {
            try
            {
                error = new ProcessState(States.Error);
            }
            catch (ProtocolException ex)
            {

            }
        }
        return error;
    }

    public static State sendingAck()
    {
        if (sendingAck == null)
        {
            try
            {
                sendingAck = new ProcessState(States.SendingAck);
            }
            catch (ProtocolException ex)
            {

            }
        }
        return sendingAck;
    }

    public static State sendingHeartbeat()
    {
        if (sendingHeartbeat == null)
        {
            try
            {
                sendingHeartbeat = new ProcessState(States.SendingHeartbeat);
            }
            catch (ProtocolException ex)
            {

            }
        }
        return sendingHeartbeat;
    }

    public static State executing()
    {
        if (executing == null)
        {
            try
            {
                executing = new ProcessState(States.Executing);
            }
            catch (ProtocolException ex)
            {

            }
        }
        return executing;
    }

}
