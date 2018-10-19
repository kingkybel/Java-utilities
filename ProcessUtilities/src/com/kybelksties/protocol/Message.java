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
 * @date: 2018-02-27
 * @author: Dieter J Kybelksties
 */
package com.kybelksties.protocol;

import java.io.Serializable;
import java.util.Objects;
import java.util.logging.Logger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import org.openide.util.NbBundle;

/**
 * Message class used to pass information between actors of a protocol.
 *
 * @author Dieter J Kybelksties
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Message implements Serializable, Comparable<Object>
{

    private static final Class CLAZZ = Message.class;
    private static final String CLASS_NAME = CLAZZ.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    final private Actor from;
    final private Actor to;
    final private MessageType messageType;
    private Object[] payload = null;
    private boolean isTemplate = false;

    /**
     * Construct a Message.
     *
     * @param isTemplate  indicates whether this is a template or a fully
     *                    fledged message
     * @param messageType type of message
     * @param from        sender of the message
     * @param to          receiver of the message
     * @param payload     a possibly empty array of message information
     * @throws Exception thrown when sender, recipient or message type is not
     *                   supplied
     */
    public Message(boolean isTemplate,
                   MessageType messageType,
                   Actor to,
                   Actor from,
                   Object... payload) throws Exception
    {
        this.isTemplate = isTemplate;
        if (messageType == null || to == null)
        {
            throw new Exception(
                    NbBundle.getMessage(CLAZZ,
                                        "Message.ReceivingActorCompulsory"));
        }
        this.messageType = messageType;
        this.from = isTemplate ? new Actor(from.getName(), null) : from;
        this.to = to;
        if (payload != null && payload.length > 0 && !isTemplate)
        {
            this.payload = new Object[payload.length];
            System.arraycopy(payload, 0, this.payload, 0, payload.length);
        }

    }

    /**
     * Normal constructor.
     *
     * @param messageType type of message
     * @param from        sender of the message
     * @param to          receiver of the message
     * @param payload     a possibly empty array of message information
     * @throws Exception thrown when sender, recipient or message type is not
     *                   supplied
     */
    public Message(MessageType messageType,
                   Actor from,
                   Actor to,
                   Object... payload)
            throws Exception
    {
        this(false, messageType, to, from, payload);
    }

    @Override
    public String toString()
    {
        return NbBundle.getMessage(CLAZZ,
                                   "Message.toString",
                                   (from == null ? Actor.ANY_ACTOR : from),
                                   messageType,
                                   to);
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.from);
        hash = 29 * hash + Objects.hashCode(this.to);
        hash = 29 * hash + Objects.hashCode(this.messageType);
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        return compareTo(obj) == 0;
    }

    /**
     * Retrieve the message payload as Object array.
     *
     * @return
     */
    public Object[] getPayload()
    {
        return payload;
    }

    /**
     * Change/set/reset the message payload.
     *
     * @param payload the new payload, may be null (reset)
     */
    public void setPayload(Object[] payload)
    {
        this.payload = payload;
    }

    /**
     * Retrieve the sending actor.
     *
     * @return the sending actor
     */
    public Actor getFrom()
    {
        return from;
    }

    /**
     * Retrieve the receiving actor.
     *
     * @return the receiving actor
     */
    public Actor getTo()
    {
        return to;
    }

    /**
     * Retrieve the type of this message.
     *
     * @return the type
     */
    public MessageType getMessageType()
    {
        return messageType;
    }

    @Override
    public int compareTo(Object o)
    {
        if (o == null)
        {
            return Integer.MIN_VALUE;
        }
        if (!(o instanceof Message))
        {
            return Integer.MIN_VALUE + 1;
        }

        Message other = (Message) o;
        if (from.compareTo(other.from) != 0)
        {
            return from.compareTo(other.from);
        }
        if (to.compareTo(other.to) != 0)
        {
            return 10 * to.compareTo(other.to);
        }

        return messageType.hashCode() - other.messageType.hashCode();
    }

}
