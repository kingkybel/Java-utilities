
/*
 * Copyright (C) 2015 Dieter J Kybelksties
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
 * @date: 2015-12-14
 * @author: Dieter J Kybelksties
 */
package com.kybelksties.process;

import java.util.EventObject;
import java.util.logging.Logger;

/**
 * An evenObject extension indicating an update on a Parameter-table.
 *
 * @author kybelksd
 */
public class ParamTableUpdatedEvent extends EventObject
{

    private static final String CLASS_NAME =
                                ParamTableUpdatedEvent.class.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    /**
     * Construct with the event source.
     *
     * @param source the event source object
     */
    public ParamTableUpdatedEvent(Object source)
    {
        super(source);
    }
}
