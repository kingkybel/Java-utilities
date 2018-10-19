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
 * @date: 2018-03-02
 * @author: Dieter J Kybelksties
 */
package com.kybelksties.protocol;

import java.util.logging.Logger;
import org.openide.util.NbBundle;

/**
 * Exception derivative for protocol errors.
 *
 * @author Dieter J Kybelksties
 */
public class ProtocolException extends Exception
{

    private static final Class CLAZZ = ProtocolException.class;
    private static final String CLASS_NAME = CLAZZ.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    public enum Type
    {

        MessageSendingActorInvalid,
        MessageReceivingActorInvalid,
        MessageTypeInvalid,
        NoSuchRule,
        ResultStateInvalid,
        StateNameInvalid,
        ProtocolInvalid,
        MessageInvalid
    }

    public ProtocolException(Type type, Object... params)
    {
        super(NbBundle.getMessage(CLAZZ,
                                  "ProtocolException.Type." + type.name(),
                                  params));
    }
}
