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

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openide.util.Exceptions;

/**
 *
 * @author Dieter J Kybelksties
 */
public class ProtocolTest
{

    public ProtocolTest()
    {
    }

    @BeforeClass
    public static void setUpClass()
    {
    }

    @AfterClass
    public static void tearDownClass()
    {
    }

    @Before
    public void setUp()
    {
    }

    @After
    public void tearDown()
    {
    }

    /**
     * Test of addRule method, of class Protocol.
     */
    @Test
    public void testAddRule()
    {
        System.out.println("addRule");
        Protocol instance = new Protocol();
        try
        {
            instance.addRule(new Rule(new Actor("Server"),
                                      new Actor("Client"),
                                      new State("Initial"),
                                      ProcessMessage.Type.Ack,
                                      new State("Started")));

            System.out.println(instance.getRule(new Actor("Server"),
                                                new Actor("Client"),
                                                new State("Initial"),
                                                ProcessMessage.Type.Ack));
        }
        catch (Exception ex)
        {
            Exceptions.printStackTrace(ex);
        }
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

}
