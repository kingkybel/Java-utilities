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
package com.kybelksties.protocol;

import com.kybelksties.general.ToString;
import com.kybelksties.process.ScheduledProcess;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import org.openide.util.NbBundle;

/**
 *
 * @author Dieter J Kybelksties
 * @param <T>
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso(
        {
            ScheduledProcess.class
        })
public class ProcessMessage<T extends Serializable>
        implements Serializable
{

    private static final Class CLAZZ = ProcessMessage.class;
    private static final String CLASS_NAME = CLAZZ.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    private static final long serialVersionUID = -8940196742313991701L;

    private final Type type;
    private ArrayList<T> objs;

    @XmlType(name = "ProcessMessageType")
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
        RestartProcess;

        private static final Class CLAZZ = Type.class;
        private static final String CLASS_NAME = CLAZZ.getName();
        private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);
        private static final long serialVersionUID = -8940196742313991701L;

        @Override
        public String toString()
        {
            return this == Invalid ?
                   NbBundle.getMessage(CLAZZ, "ProcessMessage.Type.Invalid") :
                   this == Ack ?
                   NbBundle.getMessage(CLAZZ, "ProcessMessage.Type.Ack") :
                   this == ChitChat ?
                   NbBundle.getMessage(CLAZZ, "ProcessMessage.Type.ChitChat") :
                   this == StopServer ?
                   NbBundle.getMessage(CLAZZ, "ProcessMessage.Type.StopServer") :
                   this == Identify ?
                   NbBundle.getMessage(CLAZZ, "ProcessMessage.Type.Identify") :
                   this == StartProcess ?
                   NbBundle.
                   getMessage(CLAZZ, "ProcessMessage.Type.StartProcess") :
                   this == ListProcesses ?
                   NbBundle.getMessage(CLAZZ,
                                       "ProcessMessage.Type.ListProcesses") :
                   this == KillProcess ?
                   NbBundle.getMessage(CLAZZ, "ProcessMessage.Type.KillProcess") :
                   this == RestartProcess ?
                   NbBundle.getMessage(CLAZZ,
                                       "ProcessMessage.Type.RestartProcess") :
                   NbBundle.getMessage(CLAZZ, "ProcessMessage.Type.Unknown");
        }

        public boolean isInstruction()
        {
            return this == Ack ||
                   this == ChitChat ||
                   this == StopServer ||
                   this == StartProcess ||
                   this == ListProcesses ||
                   this == KillProcess ||
                   this == RestartProcess;
        }

        public static Object[] instructions()
        {
            ArrayList<Type> revalList = new ArrayList<>();
            for (Type type : Type.values())
            {
                if (type.isInstruction())
                {
                    revalList.add(type);
                }
            }
            return revalList.toArray();
        }

    }

    public ProcessMessage()
    {
        this.type = Type.Invalid;
        this.objs = null;
    }

    public ProcessMessage(Type type, ArrayList<T> list)
    {
        this.type = type;
        this.objs = list;
    }

    public ProcessMessage(Type type, T... objs)
    {
        this.type = type;
        this.objs = new ArrayList<>();
        this.objs.addAll(Arrays.asList(objs));
    }

    static ProcessMessage makeInvalid()
    {
        return new ProcessMessage(Type.Invalid);
    }

    public static ProcessMessage makeInvalid(String str)
    {
        return new ProcessMessage(Type.Invalid, str);
    }

    public static ProcessMessage makeAcknowledge(Object... object)
    {
        ArrayList objects = new ArrayList();
        if (object != null)
        {
            objects.addAll(Arrays.asList(object));
        }
        return new ProcessMessage(Type.Ack, objects);
    }

    public static ProcessMessage makeStopServer()
    {
        return new ProcessMessage(Type.StopServer);
    }

    public static ProcessMessage makeChitChat(String message)
    {
        return new ProcessMessage(Type.ChitChat, message);
    }

    public static ProcessMessage makeIdentify(String hostname, int port)
    {
        return new ProcessMessage(Type.Identify, hostname, port);
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
        return Type.instructions();
    }

    void setAdditionalPrameters(T[] params)
    {
        objs = (ArrayList<T>) Arrays.asList(params);
    }

    public boolean isInvalid()
    {
        return type.equals(Type.Invalid);
    }

    public boolean isInstruction()
    {
        return type.isInstruction();
    }

    public boolean isAcknowledgement()
    {
        return type == Type.Ack;
    }

    @Override
    public String toString()
    {
        return type + " " + ToString.make(objs);
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
