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
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

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


    static ProcessMessage makeInvalid()
    {
        return new ProcessMessage(ExeMessageType.invalid());
    }

    /**
     *
     * @param str
     * @return
     */
    public static ProcessMessage makeInvalid(String str)
    {
        return new ProcessMessage(ExeMessageType.invalid(), str);
    }

    /**
     *
     * @param object
     * @return
     */
    public static ProcessMessage makeAcknowledge(Object... object)
    {
        ArrayList objects = new ArrayList();
        if (object != null)
        {
            objects.addAll(Arrays.asList(object));
        }
        return new ProcessMessage(ExeMessageType.acknowledge(), objects);
    }

    /**
     *
     * @return
     */
    public static ProcessMessage makeStopServer()
    {
        return new ProcessMessage(ExeMessageType.stopServer());
    }

    /**
     *
     * @param message
     * @return
     */
    public static ProcessMessage makeChitChat(String message)
    {
        return new ProcessMessage(ExeMessageType.chitChat(), message);
    }

    /**
     *
     * @param hostname
     * @param port
     * @return
     */
    public static ProcessMessage makeIdentify(String hostname, int port)
    {
        return new ProcessMessage(ExeMessageType.identify(), hostname, port);
    }

    /**
     *
     * @param stringID
     * @return
     */
    public static ProcessMessage makeStartProcess(String stringID)
    {
        return new ProcessMessage(ExeMessageType.startProcess(), stringID);
    }

    /**
     *
     * @return
     */
    public static Object[] allMessageTypes()
    {
        return ExeMessageType.values();
    }

    /**
     *
     * @return
     */
    public static Object[] instructionMessageTypes()
    {
        return ExeMessageType.instructions();
    }
    private ExeMessageType type = ExeMessageType.invalid();
    private ArrayList<T> objs;

    /**
     *
     * @param type
     * @param list
     */
    public ProcessMessage(ExeMessageType type, ArrayList<T> list)
    {
        if (type != null)
        {
            this.type = type;
        }
        this.objs = list;
    }

    /**
     *
     * @param type
     * @param objs
     */
    public ProcessMessage(ExeMessageType type, T... objs)
    {
        if (type != null)
        {
            this.type = type;
        }
        this.objs = new ArrayList<>();
        this.objs.addAll(Arrays.asList(objs));
    }

    void setAdditionalPrameters(T[] params)
    {
        objs = (ArrayList<T>) Arrays.asList(params);
    }

    /**
     *
     * @return
     */
    public boolean isInvalid()
    {
        return type.equals(ExeMessageType.invalid());
    }

    /**
     *
     * @return
     */
    public boolean isInstruction()
    {
        return ((ExeMessageType) type).isInstruction();
    }

    /**
     *
     * @return
     */
    public boolean isAcknowledgement()
    {
        return type == ExeMessageType.acknowledge();
    }

    @Override
    public String toString()
    {
        return type + " " + ToString.make(objs);
    }

    /**
     *
     * @return
     */
    public boolean isStopCommand()
    {
        return type == null || type == ExeMessageType.stopServer();
    }

    /**
     *
     * @return
     */
    public ArrayList<T> getObjects()
    {
        return objs;
    }

    ExeMessageType getType()
    {
        return type;
    }
}
