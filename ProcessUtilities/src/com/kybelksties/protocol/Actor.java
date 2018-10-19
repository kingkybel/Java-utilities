/*
 * Copyright (C) 2017 Dieter J Kybelksties
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
 * @date: 2017-10-23
 * @author: Dieter J Kybelksties
 */
package com.kybelksties.protocol;

import java.io.Serializable;
import java.util.Objects;
import java.util.logging.Logger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;

/**
 * Actor/agent in a protocol (definition).
 *
 * @author Dieter J Kybelksties
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Actor implements Serializable, Comparable<Object>
{

    private static final Class CLAZZ = Actor.class;
    private static final String CLASS_NAME = CLAZZ.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    /**
     * Any actor constant as a catch-all actor to indicate that the any actor is
     * matching.
     */
    public static Actor ANY_ACTOR = new Actor(
                        NbBundle.getMessage(CLAZZ, "Actor.ANY_ACTOR"));
    private static int index = 0;
    private final String name;
    private State state = null;
    private final int currentIndex = index++;
    private MessageProcessor messageProcessor = new DefaultMessageProcessor();

    /**
     * Default constructor.
     */
    public Actor()
    {
        this(null, null);
    }

    /**
     * Construct an actor with a name only.
     *
     * @param name describing the function of the actor, e.g. Serve, Client,
     *             Log-provider,...
     */
    public Actor(String name)
    {
        this(name, null);
    }

    /**
     * Construct an actor with a name and a state.
     *
     * @param name  describing the function of the actor, e.g. Serve, Client,
     *              Log-provider,...
     * @param state the initial state of the actor
     */
    public Actor(String name, State state)
    {
        this.name = name == null ?
                    NbBundle.getMessage(CLAZZ,
                                        "Actor.DefaultNameTemplate",
                                        currentIndex) :
                    name;
        this.state = state;
    }

    /**
     * Process the message according to the protocol, if possible. This includes
     * vetting the protocol and message, set a new state for this actor if a
     * matching rule is found in the protocol and if the messageProcessor is not
     * null then also execute it. If the messageProcessor is null then the
     * member processor is executed.
     *
     * @param protocol         non-null protocol object describing the rules
     * @param message          message this actor received
     * @param messageProcessor optional message-processor
     * @return any object the message processor might have returned
     * @throws com.kybelksties.protocol.ProtocolException if any of the required
     *                                                    parameters is missing
     *                                                    or invalid or anything
     *                                                    the messageProcessor
     *                                                    might throw
     */
    public Object processMessage(Protocol protocol,
                                 Message message,
                                 MessageProcessor messageProcessor)
            throws ProtocolException
    {
        Object reval = null;
        if (protocol == null)
        {
            throw new ProtocolException(
                    ProtocolException.Type.ProtocolInvalid,
                    protocol);
        }
        if (message == null)
        {
            throw new ProtocolException(
                    ProtocolException.Type.MessageInvalid,
                    message);
        }
        try
        {
            State newState = protocol.getResultState(this,
                                                     message.getMessageType(),
                                                     message.getFrom());
            setState(newState);
            reval = messageProcessor.processMessage(protocol, message);
        }
        catch (Exception ex)
        {
            Exceptions.printStackTrace(ex);
        }

        return reval;
    }

    @Override
    public int compareTo(Object o)
    {
        if (o == null)
        {
            return Integer.MIN_VALUE;
        }
        if (!(o instanceof Actor))
        {
            return Integer.MIN_VALUE + 1;
        }
        Actor other = (Actor) o;
        if (ANY_ACTOR.name.equals(name) ||
            ANY_ACTOR.name.equals(other.name))
        {
            return 0;
        }
        if (!name.equals(other.name))
        {
            return name.compareTo(((Actor) o).name);
        }
        if (state == null && other.state == null)
        {
            return 0;
        }
        else if (state == null || other.state == null)
        {
            return state == null ? Integer.MIN_VALUE + 2 : Integer.MAX_VALUE;
        }

        return state.compareTo(other.state);
    }

    @Override
    public String toString()
    {
        return NbBundle.getMessage(CLAZZ, "Actor.toString", name, state);
    }

    @Override
    public boolean equals(Object obj)
    {
        return compareTo(obj) == 0;
    }

    @Override
    public int hashCode()
    {
        int hash = 11;
        hash = 31 * hash + Objects.hashCode(this.name);
        hash = 31 * hash + Objects.hashCode(this.state);
        return hash;
    }

    /**
     * Retrieve the state the actor is in.
     *
     * @return the current state
     */
    public State getState()
    {
        return state;
    }

    /**
     * Change the state of the actor.
     *
     * @param state the new state - can be null
     */
    public void setState(State state)
    {
        this.state = state;
    }

    /**
     * Retrieve the name of the actor
     *
     * @return the name
     */
    final public String getName()
    {
        return name;
    }

    /**
     * Retrieve the current message processor.
     *
     * @return the set message processor
     */
    public MessageProcessor getMessageProcessor()
    {
        return messageProcessor;
    }

    /**
     * Set a new message processor.
     *
     * @param messageProcessor the new message-processor; can be null in which
     *                         case a dummy DefaultMessageProcessor is set
     */
    public void setMessageProcessor(MessageProcessor messageProcessor)
    {
        this.messageProcessor = messageProcessor == null ?
                                new DefaultMessageProcessor() :
                                messageProcessor;
    }

}
