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

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

/**
 *
 * @author Dieter J Kybelksties
 */
public class AuthenticationTest
{

    private static final Class CLAZZ = AuthenticationTest.class;
    private static final String CLASS_NAME = CLAZZ.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    /**
     *
     * @param args
     */
    public static void main(String[] args)
    {
        System.setProperty("java.security.auth.login.config",
                           "/home/kybelksd/NetBeansProjects/utilities/ProcessUtilities/config/jaas.config");

        String name = "myName";
        String password = "myPassword";

        try
        {
            LoginContext lc = new LoginContext(
                         "Test",
                         new LoginCallbackHandler(name, password));
            lc.login();
        }
        catch (LoginException e)
        {
            LOGGER.log(Level.SEVERE, e.toString());
        }
    }
}
