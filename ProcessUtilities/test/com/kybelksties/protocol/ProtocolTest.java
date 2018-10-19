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

import java.util.Arrays;
import java.util.Objects;
import java.util.TreeSet;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openide.util.Exceptions;

/**
 *
 * @author kybelksd
 */
public class ProtocolTest
{

    private static final Logger LOG =
                                Logger.getLogger(ProtocolTest.class.getName());

    /**
     *
     */
    @BeforeClass
    public static void setUpClass()
    {
    }

    /**
     *
     */
    @AfterClass
    public static void tearDownClass()
    {
    }

    /**
     *
     */
    public ProtocolTest()
    {
    }

    /**
     *
     */
    @Before
    public void setUp()
    {
    }

    /**
     *
     */
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
        System.out.println("Testing addRule() ...");
        try
        {
            Protocol protocol = new Protocol();
            protocol.addRule(new Actor("Client", new State("Initial")),
                             TestMessageType.getType("startTheClient"),
                             new State("Started"),
                             new Actor("Server"));
            final State resultState = protocol.getResultState(
                        new Actor("Client", new State("Initial")),
                        TestMessageType.getType("startTheClient"),
                        new Actor("Server"));

            assertEquals(new State("Started"), resultState);
        }
        catch (Exception ex)
        {
            Exceptions.printStackTrace(ex);
        }
        try
        {
            Protocol protocol = new Protocol();
            protocol.addRule(new Actor("Client", new State("Initial")),
                             EnumTestMessageType.StartClient,
                             new State("Started"),
                             new Actor("Server"));
            final State resultState = protocol.getResultState(
                        new Actor("Client", new State("Initial")),
                        EnumTestMessageType.StartClient,
                        new Actor("Server"));

            assertEquals(new State("Started"), resultState);
        }
        catch (Exception ex)
        {
            Exceptions.printStackTrace(ex);
        }
        System.out.println("... testing addRule() done.");
    }

    enum EnumTestMessageType implements MessageType
    {

        StartClient, StopClient, RestartClient, InterruptClient;

        @Override
        public MessageType getType()
        {
            return this;
        }

        @Override
        public TreeSet<MessageType> getAllMessageTypes()
        {
            TreeSet<MessageType> reval = new TreeSet<>();
            reval.addAll(Arrays.asList(values()));
            return reval;
        }

        @Override
        public MessageType fromString(String type)
        {
            return type == null || type.isEmpty() ? null :
                   type.equalsIgnoreCase("StartClient") ? StartClient :
                   type.equalsIgnoreCase("StopClient") ? StopClient :
                   type.equalsIgnoreCase("RestartClient") ? RestartClient :
                   type.equalsIgnoreCase("InterruptClient") ? InterruptClient :
                   null;
        }
    }

    static class TestMessageType implements MessageType, Comparable<Object>
    {

        static TreeSet<TestMessageType> types = initMessgeTypes();

        static TreeSet<TestMessageType> initMessgeTypes()
        {
            TreeSet<TestMessageType> reval = new TreeSet<>();
            reval.add(new TestMessageType(("startTheClient")));
            reval.add(new TestMessageType(("stopTheClient")));
            reval.add(new TestMessageType(("restartTheClient")));
            reval.add(new TestMessageType(("interruptTheClient")));

            return reval;
        }

        static MessageType getType(String type)
        {
            TestMessageType testMessageType = new TestMessageType(
                            type == null ? "startTheClient" : type);
            return types.floor(testMessageType);
        }
        private final String type;

        TestMessageType(String type)
        {
            this.type = type;
        }

        @Override
        public MessageType getType()
        {
            return this;
        }

        @Override
        public TreeSet<MessageType> getAllMessageTypes()
        {
            TreeSet<MessageType> reval = new TreeSet<>();
            reval.addAll(types);

            return reval;
        }

        @Override
        public MessageType fromString(String type)
        {
            return getType(type);
        }

        @Override
        public String toString()
        {
            return "message(" + type + ")";
        }

        @Override
        public boolean equals(Object obj)
        {
            if (obj == null)
            {
                return false;
            }
            if (!(obj.getClass().equals(TestMessageType.class)))
            {
                return false;
            }
            return type.compareTo(((TestMessageType) obj).type) == 0;
        }

        @Override
        public int hashCode()
        {
            int hash = 7;
            hash = 23 * hash + Objects.hashCode(this.type);
            return hash;
        }

        @Override
        public int compareTo(Object o)
        {
            if (o == null)
            {
                return Integer.MIN_VALUE;
            }
            if (o.getClass() != TestMessageType.class)
            {
                return Integer.MIN_VALUE + 1;
            }
            TestMessageType other = (TestMessageType) o;
            return type.compareTo(other.type);
        }
    }

}
