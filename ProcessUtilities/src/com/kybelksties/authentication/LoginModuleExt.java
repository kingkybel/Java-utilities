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
import java.util.Map;
import java.util.logging.Logger;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

/**
 *
 * @author Dieter J Kybelksties
 */
public class LoginModuleExt implements LoginModule
{

    private static final Class CLAZZ = LoginModuleExt.class;
    private static final String CLASS_NAME = CLAZZ.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    private Subject subject;
    private CallbackHandler callbackHandler;
    private Map sharedState;
    private Map options;

    private boolean succeeded = false;

    public LoginModuleExt()
    {
        System.out.println("Login Module - constructor called");
    }

    @Override
    public boolean abort() throws LoginException
    {
        System.out.println("Login Module - abort called");
        return false;
    }

    @Override
    public boolean commit() throws LoginException
    {
        System.out.println("Login Module - commit called");
        return succeeded;
    }

    @Override
    public void initialize(Subject subject,
                           CallbackHandler callbackHandler,
                           Map<String, ?> sharedState,
                           Map<String, ?> options)
    {

        System.out.println("Login Module - initialize called");
        this.subject = subject;
        this.callbackHandler = callbackHandler;
        this.sharedState = sharedState;
        this.options = options;

        System.out.println("testOption value: " +
                           (String) options.get("testOption"));

        succeeded = false;
    }

    @Override
    public boolean login() throws LoginException
    {
        System.out.println("Login Module - login called");
        if (callbackHandler == null)
        {
            throw new LoginException("Oops, callbackHandler is null");
        }

        Callback[] callbacks = new Callback[2];
        callbacks[0] = new NameCallback("name:");
        callbacks[1] = new PasswordCallback("password:", false);

        try
        {
            callbackHandler.handle(callbacks);
        }
        catch (IOException e)
        {
            throw new LoginException(
                    "Oops, IOException calling handle on callbackHandler");
        }
        catch (UnsupportedCallbackException e)
        {
            throw new LoginException(
                    "Oops, UnsupportedCallbackException calling handle on callbackHandler");
        }

        NameCallback nameCallback = (NameCallback) callbacks[0];
        PasswordCallback passwordCallback = (PasswordCallback) callbacks[1];

        String name = nameCallback.getName();
        String password = new String(passwordCallback.getPassword());

        if ("myName".equals(name) && "myPassword".equals(password))
        {
            System.out.println("Success! You get to log in!");
            succeeded = true;
            return succeeded;
        }
        else
        {
            System.out.println("Failure! You don't get to log in");
            succeeded = false;
            throw new FailedLoginException("Sorry! No login for you.");
        }
    }

    @Override
    public boolean logout() throws LoginException
    {
        System.out.println("Login Module - logout called");
        return false;
    }
}
