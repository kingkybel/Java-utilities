/*
 * Copyright (C) 2016 Dieter J Kybelksties
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
 * @date: 2016-11-17
 * @author: Dieter J Kybelksties
 */
package com.kybelksties.authentication;

import java.io.IOException;
import java.util.logging.Logger;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

/**
 *
 * @author Dieter J Kybelksties
 */
public class LoginCallbackHandler implements CallbackHandler

{

    private static final Class CLAZZ = LoginCallbackHandler.class;
    private static final String CLASS_NAME = CLAZZ.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    String name;
    String password;

    public LoginCallbackHandler(String name, String password)
    {
        System.out.println("Callback Handler - constructor called");
        this.name = name;
        this.password = password;
    }

    @Override
    public void handle(Callback[] callbacks) throws IOException,
                                                    UnsupportedCallbackException
    {
        System.out.println("Callback Handler - handle called");
        for (Callback callback : callbacks)
        {
            if (callback instanceof NameCallback)
            {
                NameCallback nameCallback = (NameCallback) callback;
                nameCallback.setName(name);
            }
            else if (callback instanceof PasswordCallback)
            {
                PasswordCallback passwordCallback = (PasswordCallback) callback;
                passwordCallback.setPassword(password.toCharArray());
            }
            else
            {
                throw new UnsupportedCallbackException(callback,
                                                       "The submitted Callback is unsupported");
            }
        }
    }
}
