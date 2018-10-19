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
 * @date: 2018-02-22
 * @author: Dieter J Kybelksties
 */
package com.kybelksties.protocol;

import java.util.TreeSet;

/**
 * An interface for enumeration style types that serve as message types.
 *
 * @author Dieter J Kybelksties
 */
public interface MessageType
{

    /**
     * Get the value of this object.
     *
     * @return
     */
    MessageType getType();

    /**
     * Retrieve all the different types as tree-ordered set.
     *
     * @return all valid types
     */
    TreeSet<MessageType> getAllMessageTypes();

    /**
     * Convert the given string into a message type object.
     *
     * @param type the type-string
     * @return the value resulting from the string-conversion or null if no such
     *         conversion is possible
     */
    MessageType fromString(String type);

    @Override
    public boolean equals(Object obj);

    @Override
    public int hashCode();

}
