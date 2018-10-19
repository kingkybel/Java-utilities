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
 * @date: 2018-02-22
 * @author: Dieter J Kybelksties
 */
package com.kybelksties.process;

import com.kybelksties.protocol.MessageType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.logging.Logger;
import javax.xml.bind.annotation.XmlType;
import org.openide.util.NbBundle;

/**
 *
 * @author Dieter J Kybelksties
 */
public class ExeMessageType implements MessageType
{

    private static final Class CLAZZ = ExeMessageType.class;
    private static final String CLASS_NAME = CLAZZ.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    static Object[] values()
    {
        ExeMessageType[] reval = new ExeMessageType[Type.values().length];
        for (int i = 0; i < Type.values().length; i++)
        {
            reval[i] = new ExeMessageType(Type.values()[i]);
        }
        return reval;
    }

    static Object[] instructions()
    {
        ExeMessageType[] reval = new ExeMessageType[Type.values().length];
        for (int i = 0; i < Type.values().length; i++)
        {
            if (Type.values()[i].isInstruction())
            {
                reval[i] = new ExeMessageType(Type.values()[i]);
            }
        }
        return reval;
    }

    /**
     *
     * @return
     */
    public static MessageType bootstrap()
    {
        return new ExeMessageType(Type.Bootstrap);
    }

    /**
     *
     * @return
     */
    public static ExeMessageType invalid()
    {
        return new ExeMessageType(Type.Invalid);
    }

    /**
     *
     * @return
     */
    public static ExeMessageType acknowledge()
    {
        return new ExeMessageType(Type.Ack);
    }

    /**
     *
     * @return
     */
    public static ExeMessageType chitChat()
    {
        return new ExeMessageType(Type.ChitChat);
    }

    /**
     *
     * @return
     */
    static public ExeMessageType stopServer()
    {
        return new ExeMessageType(Type.StopServer);
    }

    /**
     *
     * @return
     */
    static public ExeMessageType identify()
    {
        return new ExeMessageType(Type.Identify);
    }

    /**
     *
     * @return
     */
    static public ExeMessageType startProcess()
    {
        return new ExeMessageType(Type.StartProcess);
    }

    /**
     *
     * @return
     */
    static public ExeMessageType listProcesses()
    {
        return new ExeMessageType(Type.ListProcesses);
    }

    /**
     *
     * @return
     */
    static public ExeMessageType processList()
    {
        return new ExeMessageType(Type.ProcessList);
    }

    /**
     *
     * @return
     */
    static public ExeMessageType killProcess()
    {
        return new ExeMessageType(Type.KillProcess);
    }

    /**
     *
     * @return
     */
    static public ExeMessageType restartProcess()
    {
        return new ExeMessageType(Type.RestartProcess);
    }
    Type type = Type.Invalid;

    private ExeMessageType(Type type)
    {
        this.type = type;
    }

    @Override
    public String toString()
    {
        return type.toString();
    }

    boolean isInstruction()
    {
        return type == null ? false : type.isInstruction();
    }

    @Override // MessageType
    public MessageType getType()
    {
        return this;
    }

    @Override // MessageType
    public TreeSet<MessageType> getAllMessageTypes()
    {
        TreeSet<MessageType> reval = new TreeSet<>();
        for (Type typeVal : Type.values())
        {
            reval.add(new ExeMessageType(typeVal));
        }
        return reval;
    }

    @Override // MessageType
    public MessageType fromString(String type)
    {
        return new ExeMessageType(Type.fromSting(type));
    }

    /**
     *
     */
    @XmlType(name = "ProcessMessageType")
    public static enum Type implements Serializable
    {

        /**
         *
         */
        Bootstrap,
        /**
         *
         */
        Invalid,
        /**
         *
         */
        Ack,
        /**
         *
         */
        ChitChat,
        /**
         *
         */
        StopServer,
        /**
         *
         */
        Identify,
        /**
         *
         */
        StartProcess,
        /**
         *
         */
        ListProcesses,
        /**
         *
         */
        ProcessList,
        /**
         *
         */
        KillProcess,
        /**
         *
         */
        RestartProcess;

        private static final Class CLAZZ = Type.class;
        private static final String CLASS_NAME = CLAZZ.getName();
        private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);
        private static final long serialVersionUID = -8940196742313991701L;

        @Override
        public String toString()
        {
            return this == Bootstrap ?
                   NbBundle.getMessage(CLAZZ, "ProcessMessage.Type.Bootstrap") :
                   this == Invalid ?
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
                   NbBundle.getMessage(CLAZZ,
                                       "ProcessMessage.Type.StartProcess") :
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

        /**
         *
         * @return
         */
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

        /**
         *
         * @return
         */
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

        static TreeSet<String> asStringSet()
        {
            TreeSet<String> reval = new TreeSet<>();
            for (Type t : values())
            {
                reval.add(t.toString());
            }
            return reval;
        }

        static Type fromSting(String string)
        {
            return (string == null ||
                    string.isEmpty() ||
                    "Invalid".equalsIgnoreCase(string)) ? Invalid :
                   "Ack".equalsIgnoreCase(string) ? Ack :
                   "ChitChat".equalsIgnoreCase(string) ? ChitChat :
                   "Identify".equalsIgnoreCase(string) ? Identify :
                   "StartProcess".equalsIgnoreCase(string) ? StartProcess :
                   "ListProcesses".equalsIgnoreCase(string) ? ListProcesses :
                   "ProcessList".equalsIgnoreCase(string) ? ProcessList :
                   "KillProcess".equalsIgnoreCase(string) ? KillProcess :
                   "RestartProcess".equalsIgnoreCase(string) ? ProcessList :
                   Invalid;

        }
    }
}
