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

import java.io.Serializable;
import java.util.logging.Logger;
import org.openide.util.NbBundle;

/**
 * Window mode in which the process will be executed.
 *
 * @author kybelksd
 */
public enum WindowMode implements Serializable
{

    /**
     * Direct output to an xterm.
     */
    XTERM,
    /**
     * Direct output to a GUI frame.
     */
    GUIFRAME,
    /**
     * Discard output and run in background.
     */
    BACKGROUND,
    /**
     * Write output to file whilst running in background.
     */
    BACKGROUND_FILE;

    private static final Class CLAZZ = WindowMode.class;
    private static final String CLASS_NAME = CLAZZ.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);
    private static final long serialVersionUID = -8940196742313991701L;

    @Override
    public String toString()
    {
        return this.equals(XTERM) ?
               NbBundle.getMessage(CLAZZ, "WindowMode.XTERM") :
               this.equals(GUIFRAME) ?
               NbBundle.getMessage(CLAZZ, "WindowMode.GUIFRAME") :
               this.equals(BACKGROUND) ?
               NbBundle.getMessage(CLAZZ, "WindowMode.BACKGROUND") :
               this.equals(BACKGROUND_FILE) ?
               NbBundle.getMessage(CLAZZ, "WindowMode.BACKGROUND_FILE") :
               NbBundle.getMessage(CLAZZ, "WindowMode.UNKNOWN");
    }
}
