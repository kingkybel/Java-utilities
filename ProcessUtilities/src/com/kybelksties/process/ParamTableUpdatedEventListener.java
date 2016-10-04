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

import java.util.EventListener;

/**
 * Interface that listeners to parameter table events have to implement.
 *
 * @author kybelksd
 */
public interface ParamTableUpdatedEventListener extends EventListener
{

    /**
     * The implementation of this method allows the listener to handle the
     * event.
     *
     * @param evt the triggering event
     */
    public void tableUpdatedEventOccurred(ParamTableUpdatedEvent evt);
}
