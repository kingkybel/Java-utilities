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

/**
 *
 * @author Dieter J Kybelksties
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Rule implements Serializable, Comparable<Object>
{

    private static final Class CLAZZ = Rule.class;
    private static final String CLASS_NAME = CLAZZ.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    private final Actor fromActor;
    private final Actor toActor;
    private final State state;
    private final State newState;
    private final Integer timeout;
    private final ProcessMessage.Type messageType;

    public Rule(Actor fromActor,
                Actor toActor,
                State currentState,
                ProcessMessage.Type messageType,
                State newState) throws Exception
    {
        this(fromActor, toActor, currentState, messageType, newState, null);
    }

    public Rule(Actor fromActor,
                Actor toActor,
                State currentState,
                ProcessMessage.Type messageType,
                State newState,
                Integer timeout) throws Exception
    {
        if (fromActor == null ||
            toActor == null ||
            currentState == null ||
            messageType == null)
        {
            throw new Exception("All fields in a Rule are mandatory");
        }
        this.fromActor = fromActor;
        this.toActor = toActor;
        this.state = currentState;
        this.newState = newState;
        this.timeout = timeout == null ? -1 : timeout;
        this.messageType = messageType;
    }

    @Override
    public int compareTo(Object o)
    {
        if (o == null)
        {
            return Integer.MIN_VALUE;
        }
        if (!(o instanceof Rule))
        {
            return Integer.MIN_VALUE + 1;
        }
        Rule other = (Rule) o;
        if (toActor.compareTo(other.toActor) != 0)
        {
            return toActor.compareTo(other.toActor);
        }
        if (state.compareTo(other.state) != 0)
        {
            return state.compareTo(other.state);
        }
        /*
         * Leave out newState from comparison as it won't be known when looking
         * up a rule. It's the result.
         */
        if (messageType.compareTo(other.messageType) != 0)
        {
            return messageType.compareTo(other.messageType);
        }

        return timeout.compareTo(other.timeout);
    }

    @Override
    public String toString()
    {
        return "rule(when " +
               toActor +
               " in " +
               state +
               " receives " +
               messageType +
               " from " +
               fromActor +
               " assume " +
               newState;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final Rule other = (Rule) obj;
        if (!Objects.equals(this.fromActor, other.fromActor))
        {
            return false;
        }
        if (!Objects.equals(this.toActor, other.toActor))
        {
            return false;
        }
        if (!Objects.equals(this.state, other.state))
        {
            return false;
        }
//        if (!Objects.equals(this.newState, other.newState))
//        {
//            return false;
//        }
//        if (!Objects.equals(this.timeout, other.timeout))
//        {
//            return false;
//        }
        return Objects.equals(this.messageType, other.messageType);
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.fromActor);
        hash = 23 * hash + Objects.hashCode(this.toActor);
        hash = 23 * hash + Objects.hashCode(this.state);
        hash = 23 * hash + Objects.hashCode(this.newState);
        hash = 23 * hash + Objects.hashCode(this.timeout);
        hash = 23 * hash + Objects.hashCode(this.messageType);
        return hash;
    }

    public Actor getFromActor()
    {
        return fromActor;
    }

    public Actor getToActor()
    {
        return toActor;
    }

    public State getState()
    {
        return state;
    }

    public State getNewState()
    {
        return newState;
    }

    public Integer getTimeout()
    {
        return timeout;
    }

    public ProcessMessage.Type getMessageType()
    {
        return messageType;
    }

}
