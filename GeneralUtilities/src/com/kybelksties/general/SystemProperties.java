/*
 * Copyright (C) 2016 Dieter J Kybelksties
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * @date: 2016-09-01
 * @author: Dieter J Kybelksties
 */
package com.kybelksties.general;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.TreeMap;
import java.util.logging.Logger;

/**
 *
 * @author Dieter J Kybelksties
 */
public class SystemProperties
{

    private static final Class CLAZZ = SystemProperties.class;
    private static final String CLASS_NAME = CLAZZ.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);


    private static TreeMap<PropKey, Object> map = makeMap();

    private static TreeMap<PropKey, Object> makeMap()
    {
        map = new TreeMap<>();
        Map<String, String> env = System.getenv();
        HashMap<String, ?> sys = new HashMap(System.getProperties());
        for (String key : sys.keySet())
        {
            map.put(new PropKey(key, Type.SystemProperty), sys.get(key));
        }
        for (String key : env.keySet())
        {
            map.put(new PropKey(key, Type.EnvironmentVariable), env.get(key));
        }
        return map;
    }

    /**
     * Retrieve the the property by it's key. First look in the environment
     * properties then in the system properties.
     *
     * @param key the key to the property
     * @return the property as object, or null if no such property exists in
     *         either set
     */
    public static Object get(String key)
    {
        if (System.getenv().containsKey(key))
        {
            return System.getenv().get(key);
        }
        HashMap<String, ?> sys = new HashMap(System.getProperties());
        if (sys.containsKey(key))
        {
            return sys.get(key);
        }
        return null;
    }

    /**
     * Print an ordered list of all properties to the given print-stream.
     *
     * @param printStream the print-stream to print to
     */
    public static void print(PrintStream printStream)
    {
        for (PropKey k : map.keySet())
        {
            printStream.println(k.toString() + ": '" + map.get(k) + "'");
        }
    }

    /**
     * Main entry point.
     *
     * @param args standard array of strings (parameters)
     */
    public static void main(String[] args)
    {
        print(System.out);
    }

    /**
     * Load properties from a file.
     *
     * @param fileName the filename to load from
     * @throws IOException           when file access is prohibited
     * @throws FileNotFoundException when the file is present
     */
    public synchronized void loadFile(String fileName) throws
            IOException,
                                                              FileNotFoundException
    {
        File file = new File(fileName);
        DataInputStream inpStream = new DataInputStream(
                new FileInputStream(file));
        
        Properties prop = new Properties();
        prop.load(inpStream);
    }
    
    private static enum Type
    {
        
        SystemProperty, EnvironmentVariable
    }
    
    private static class PropKey implements Comparable<PropKey>
    {

        private final String key;
        private final Type type;

        PropKey(String key, Type type)
        {
            this.key = key;
            this.type = type;
        }

        @Override
        public String toString()
        {
            return key + " (" + type + ")";
        }

        @Override
        public int hashCode()
        {
            int hash = 7;
            hash = 79 * hash + Objects.hashCode(this.key);
            hash = 79 * hash + Objects.hashCode(this.type);
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
            final PropKey other = (PropKey) obj;
            if (!Objects.equals(this.key, other.key))
            {
                return false;
            }
            return this.type == other.type;
        }

        @Override
        public int compareTo(PropKey o)
        {
            if (o == null)
            {
                return 1;
            }
            if (key == null && o.key == null)
            {
                return type == o.type ? 0 :
                       type == null ? -1 :
                       o.type == null ? 1 :
                       type.compareTo(o.type);
            }
            if (key == null)
            {
                return -1;
            }
            if (o.key == null)
            {
                return 1;
            }
            if (key.compareToIgnoreCase(o.key) == 0)
            {
                return type == o.type ? 0 :
                       type == null ? -1 :
                       o.type == null ? 1 :
                       type.compareTo(o.type);
            }

            return key.compareToIgnoreCase(o.key);
        }
    }
}
