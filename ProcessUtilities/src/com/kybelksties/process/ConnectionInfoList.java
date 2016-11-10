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
 * @date: 2016-11-02
 * @author: Dieter J Kybelksties
 */
package com.kybelksties.process;

import java.io.Serializable;
import java.util.Objects;
import java.util.TreeSet;
import java.util.logging.Logger;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Dieter J Kybelksties
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ConnectionInfoList implements Serializable
{

    private static final Class CLAZZ = ConnectionInfoList.class;
    private static final String CLASS_NAME = CLAZZ.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);
    private static final long serialVersionUID = -8940196742313991701L;

    private final TreeSet<ConnectionInfo> configurations = new TreeSet<>();

    public ConnectionInfoList()
    {
    }

    public void add(String serverIP, int port, String name)
    {
        configurations.add(new ConnectionInfo(serverIP, port, name));
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    static public class ConnectionInfo implements Comparable<Object>,
                                                  Serializable
    {

        @Override
        public int hashCode()
        {
            int hash = 7;
            hash = 41 * hash + Objects.hashCode(this.serverIP);
            hash = 41 * hash + this.port;
            return hash;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (obj == null)
            {
                return false;
            }
            if (getClass() != obj.getClass())
            {
                return false;
            }
            final ConnectionInfo other = (ConnectionInfo) obj;
            if (!Objects.equals(this.serverIP, other.serverIP))
            {
                return false;
            }
            return this.port == other.port;
        }

        private static final Class CLAZZ = ConnectionInfo.class;
        private static final String CLASS_NAME = CLAZZ.getName();
        private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

        @Override
        public String toString()
        {
            return name + " (" + serverIP + ":" + port + ")";
        }

        public ConnectionInfo(String serverIP, int port, String name)
        {
            this.serverIP = serverIP;
            this.port = port;
            this.name = name;
        }

        public ConnectionInfo(String serverIP, int port)
        {
            this(serverIP, port, serverIP + ":" + port);
        }

        public ConnectionInfo()
        {
            this("localhost", 0);
        }

        String serverIP = "localhost";
        int port = 0;
        String name = "";

        @Override
        public int compareTo(Object o)
        {
            if (o == null)
            {
                return 1;
            }
            if (!(o instanceof ConnectionInfo))
            {
                return 100;
            }
            ConnectionInfo other = (ConnectionInfo) o;

            if (serverIP.equals(other.serverIP))
            {
                return other.port - port;
            }
            return serverIP.compareToIgnoreCase(other.serverIP);
        }
    }

    public ComboBoxModel getComboBoxModel()
    {
        return new DefaultComboBoxModel(configurations.toArray());
    }
}
