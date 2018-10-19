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

import static com.kybelksties.protocol.ProtocolException.Type.StateNameInvalid;
import java.io.Serializable;
import java.util.Objects;
import java.util.logging.Logger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;

/**
 * This class is meant as a base class for states of actors in a protocol.
 *
 * @author Dieter J Kybelksties
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class State implements Serializable, Comparable<Object>
{

    private static final Class CLAZZ = State.class;
    private static final String CLASS_NAME = CLAZZ.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    private static int currentID = -1;

    /**
     * A special undefined State with id == -1.
     */
    public static final State UNDEFINED = setUndefinedName();

    static State setUndefinedName()
    {
        State reval = null;
        try
        {
            reval = new State(NbBundle.getMessage(CLAZZ, "State.UNDEFINED"));
        }
        catch (ProtocolException ex)
        {
            Exceptions.printStackTrace(ex);
        }
        return reval;
    }
    private final String name;
    private final int id = currentID++;

    /**
     * Construct by naming the state.
     *
     * @param name the name of the newly constructed state
     * @throws com.kybelksties.protocol.ProtocolException
     */
    public State(String name) throws ProtocolException
    {
        if (name == null || name.isEmpty())
        {
            throw new ProtocolException(StateNameInvalid);
        }
        this.name = name;
    }

    /**
     * Check whether this is the (unique) undefined state.
     *
     * @return true if so, false otherwise
     */
    public boolean isUndefined()
    {
        return id == -1;
    }

    @Override
    public int compareTo(Object o)
    {
        if (o == null)
        {
            return Integer.MIN_VALUE;
        }
        if (!(o instanceof State))
        {
            return Integer.MIN_VALUE + 1;
        }
        return name.compareTo(((State) o).name);
    }

    @Override
    public String toString()
    {
        return NbBundle.getMessage(CLAZZ, "State.toString", name);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        if (!(obj instanceof State))
        {
            return false;
        }
        return name.equals(((State) obj).name);
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 37 * hash + Objects.hashCode(this.name);
        return hash;
    }

}
