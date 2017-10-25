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
public class Actor implements Serializable, Comparable<Object>
{

    private static final Class CLAZZ = Actor.class;
    private static final String CLASS_NAME = CLAZZ.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);
    private final String name;
    private State state = null;
    private static int index = 0;
    private final int currentIndex = index++;

    public Actor()
    {
        this(null, null);
    }

    public Actor(String name)
    {
        this(name, null);
    }

    public Actor(String name, State state)
    {
        this.name = name == null ? "Actor" + currentIndex : name;
        this.state = state == null ? State.UNDEFINED : state;
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
        return name.compareTo(((Actor) o).name);
    }

    @Override
    public String toString()
    {
        return "actor(" + name + ")";
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
        return name.equals(((Actor) obj).name);
    }

    @Override
    public int hashCode()
    {
        return super.hashCode();
    }

    public State apply(Rule rule)
    {
        State reval = state;
        if (rule != null && rule.getState().equals(state))
        {
            state = rule.getNewState();
        }
        return reval;
    }
}
