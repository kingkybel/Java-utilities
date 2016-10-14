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
 * @date: 2016-10-05
 * @author: Dieter J Kybelksties
 */
package com.kybelksties.process;

import com.kybelksties.general.ToString;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;

/**
 *
 * @author Dieter J Kybelksties
 * @param <T>
 */
public class ProcessMessage<T extends Serializable> implements Serializable
{

    @Override
    public String toString()
    {
        return type + " " + ToString.make(objs);
    }

    private static final Class CLAZZ = ProcessMessage.class;
    private static final String CLASS_NAME = CLAZZ.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    ProcessMessage(Type type, ArrayList<T> list)
    {
        this.type = type;
        this.objs = list;
    }

    static ProcessMessage makeInvalid()
    {
        return new ProcessMessage(Type.Invalid);
    }

    public static ProcessMessage makeStopServer()
    {
        return new ProcessMessage(Type.StopServer);
    }

    public static ProcessMessage makeChitChat(String message)
    {
        return new ProcessMessage(Type.ChitChat, message);
    }

    public static ProcessMessage makeIdentify(int port, String serverIP)
    {
        return new ProcessMessage(Type.Identify, port, serverIP);
    }

    public static ProcessMessage makeStartProcess(String stringID)
    {
        return new ProcessMessage(Type.StartProcess, stringID);
    }

    static public Object[] allMessageTypes()
    {
        return Type.values();
    }

    static public Object[] instructionMessageTypes()
    {
        return Type.values();
    }

    void setAdditionalPrameters(T[] params)
    {
        objs = (ArrayList<T>) Arrays.asList(params);
    }

    boolean isInvalid()
    {
        return type.equals(Type.Invalid);
    }

    public static enum Type implements Serializable
    {

        Invalid,
        Ack,
        ChitChat,
        StopServer,
        Identify,
        StartProcess,
        ListProcesses,
        ProcessList,
        KillProcess,
        RestartProcess
    }

    private final Type type;
    private ArrayList<T> objs;

    public ProcessMessage(Type type, T... objs)
    {
        this.type = type;
        this.objs = new ArrayList<>();
        this.objs.addAll(Arrays.asList(objs));
    }

    public boolean isStopCommand()
    {
        return type == null || type == Type.StopServer;
    }

    public Type getType()
    {
        return type;
    }

    public ArrayList<T> getObjects()
    {
        return objs;
    }
}
