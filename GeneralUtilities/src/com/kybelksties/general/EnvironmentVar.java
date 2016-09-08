/*
 * Copyright (C) 2015 Dieter J Kybelksties
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
 * @date: 2015-12-16
 * @author: Dieter J Kybelksties
 */
package com.kybelksties.general;

import java.util.Objects;
import java.util.logging.Logger;
import org.openide.util.NbBundle;

/**
 * Class to attach a category, type and default to environment variables.
 *
 * @author kybelksd
 */
public class EnvironmentVar implements Comparable
{

    private static final Class CLAZZ = EnvironmentVar.class;
    private static final String CLASS_NAME = CLAZZ.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    static final String NULL_CATEGORY = NbBundle.getMessage(
                        CLAZZ,
                        "EnvironmentVar.uncategorized");

    /**
     * Create an EnvironentVar from a String.
     *
     * @param definitionStr in format type,category,name,defined,value,default.
     *                      need minimum of the first 4 fields
     * @return an EnvironmentVar represented by the String
     */
    public static EnvironmentVar fromString(String definitionStr)
    {
        EnvironmentVar reval = new EnvironmentVar();
        definitionStr = definitionStr.replaceAll("\\" + StringUtils.NEWLINE, "");
        if (definitionStr.contains("#"))
        {
            definitionStr = definitionStr.substring(0, definitionStr.
                                                    indexOf('#'));
        }
        String[] columns = definitionStr.split(",");
        for (int i = 0; i < columns.length; i++)
        {
            columns[i] = columns[i].trim();
        }
        if (columns.length < 4)
        {
            return null;
        }
        String typeStr = columns[0];
        reval.setValue(PodVariant.fromString(typeStr));
        reval.setCategory(columns[1]);
        reval.setName(columns[2]);
        reval.setDefined(StringUtils.scanBoolString(columns[3]));

        if (columns.length > 4)
        {
            reval.setValue(PodVariant.fromString(typeStr, columns[4]));
        }
        return reval;
    }

    private String name = "";
    private boolean defined = false;
    private PodVariant value = new PodVariant();
    private String category = "";

    /**
     * Default construct.
     */
    public EnvironmentVar()
    {
    }

    /**
     * Construct.
     *
     * @param podType  plain old type
     * @param category a category name - any unique string
     * @param name     name of the variable
     * @param defined  flag to indicate whether the variable is defined or not
     * @param val      value of the variable
     */
    public EnvironmentVar(PodVariant.Type podType,
                          String category,
                          String name,
                          boolean defined,
                          PodVariant... val)
    {
        setType(podType);
        setCategory(category);
        setName(name);
        setDefined(defined);
        if (val.length > 0)
        {
            setValue(val[0]);
        }
    }

    /**
     * Copy construct.
     *
     * @param rhs right hand side
     */
    public EnvironmentVar(EnvironmentVar rhs)
    {
        name = rhs.name;
        defined = rhs.defined;
        value = new PodVariant(rhs.getValue());
        category = rhs.category;
    }

    /**
     * Construct.
     *
     * @param name     name of the variable
     * @param category a category name - any unique string
     * @param value    value of the variable
     */
    public EnvironmentVar(String name, String category, PodVariant value)
    {
        this(value.getType(), category, name, true, value);
    }

    @Override
    public String toString()
    {
        String reval = getType() +
                       "," + getCategory() +
                       "," + getName() +
                       "," + getDefined();
        if (value != null)
        {
            reval += "," + getValue();
        }
        return reval;
    }

    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 11 * hash + Objects.hashCode(this.name);
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
        final EnvironmentVar other = (EnvironmentVar) obj;
        return Objects.equals(this.name, other.name);
    }

    @Override
    public int compareTo(Object o)
    {
        if (o == null)
        {
            return 1;
        }
        if (o.getClass() != EnvironmentVar.class)
        {
            return -1;
        }
        EnvironmentVar other = (EnvironmentVar) o;
        return name == null && other.name == null ? 0 :
               name == null ? -1 :
               other.name == null ? 1 :
               name.compareTo(other.name);

    }

    /**
     * Retrieve the POD type of the variable.
     *
     * @return the type
     */
    public PodVariant.Type getType()
    {
        return value.getType();
    }

    private void setType(PodVariant.Type podType)
    {
        if (podType == null)
        {
            podType = PodVariant.Type.UNDEFINED;
        }
        value = new PodVariant(podType);
    }

    /**
     * Retrieve the name.
     *
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Set the name of the variable.
     *
     * @param name the name to set
     */
    public final void setName(String name)
    {
        if (name == null)
        {
            name = "";
        }
        this.name = name;
    }

    /**
     * Is the variable defined, i.e: is it a placeholder or not.
     *
     * @return true if so, false otherwise
     */
    public boolean getDefined()
    {
        return defined;
    }

    /**
     * Set the variable as defined, i.e. is it a placeholder or not.
     *
     * @param defined true or false (placeholder variable)
     */
    public final void setDefined(boolean defined)
    {
        this.defined = defined;
    }

    /**
     * Retrieve the value of the variable.
     *
     * @return the value an POD
     */
    public PodVariant getValue()
    {
        return value;
    }

    /**
     * Set the value of the variable.
     *
     * @param value new value as POD
     */
    public final void setValue(PodVariant value)
    {
        this.value = value;
    }

    /**
     * Retrieve the category.
     *
     * @return a string name
     */
    public String getCategory()
    {
        return category;
    }

    /**
     * Set the category.
     *
     * @param category string name
     */
    public final void setCategory(String category)
    {
        if (category == null)
        {
            category = NULL_CATEGORY;
        }
        this.category = category;
    }
}
