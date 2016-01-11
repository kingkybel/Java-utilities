
/*
 * Copyright (C) 2015 Dieter J Kybelksties
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
 * @date: 2015-12-14
 * @author: Dieter J Kybelksties
 */
package com.kybelksties.process;

import java.io.Serializable;
import java.util.Objects;
import java.util.logging.Logger;
import javax.swing.table.AbstractTableModel;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * Class to encapsulate static executable information like path and name.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ExeDefinition implements Serializable, Comparable<Object>
{

    private static final String CLASS_NAME = ExeDefinition.class.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    static Integer creationCounter = 0;
    private String name = "";
    final private Integer creationIndex;
    private String executable;
    private String path;
    private ParameterList parameters;
    private Boolean active;

    /**
     * Default construct.
     */
    public ExeDefinition()
    {
        this.creationIndex = creationCounter;
        creationCounter++;
        this.executable = "";
        this.path = "";
        this.parameters = new ParameterList();
        active = false;
    }

    /**
     * Construct with a name (identifier).
     *
     * @param name
     */
    public ExeDefinition(String name)
    {
        this();
        this.name = name;
        this.active = true;
    }

    /**
     * Copy construct.
     *
     * @param rhs right hand side object
     */
    public ExeDefinition(ExeDefinition rhs)
    {
        this.creationIndex = creationCounter;
        creationCounter++;
        this.name = rhs.name;
        this.executable = rhs.executable;
        this.path = rhs.path;
        this.parameters = new ParameterList(rhs.parameters);
        this.active = rhs.active;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.name);
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
        final ExeDefinition other = (ExeDefinition) obj;
        return Objects.equals(this.name, other.name);
    }

    @Override
    public String toString()
    {
        return name == null || !active ? "" : name;
    }

    /**
     * Retrieve the name of the executable definition.
     *
     * @return a string, does not need to correspond to file or path-name
     */
    public String getName()
    {
        return name;
    }

    private void setName()
    {
        name = executable.toUpperCase() + "_" + creationIndex;
        active = true;
    }

    /**
     * Retrieve the executable. This is the file (system) name.
     *
     * @return the string denoting the executable in the file system
     */
    public String getExecutable()
    {
        return executable;
    }

    /**
     * Set the path to the executable.
     *
     * @param exePath the new path to the executable including the executable
     *                itself
     */
    public void setExecutable(String exePath)
    {
        int lastSlashPos = exePath.lastIndexOf('/');
        if (lastSlashPos != -1)
        {
            path = exePath.substring(0, lastSlashPos);
            executable = exePath.substring(lastSlashPos);
        }
        else
        {
            executable = exePath;
        }
        setName();
    }

    /**
     * Retrieve the path component of the executable.
     *
     * @return the path in the file system where the executable is located
     */
    public String getPath()
    {
        return path;
    }

    /**
     * Set the path component of the executable.
     *
     * @param path the new path in the file system where the executable is
     *             located including the executable itself
     *
     */
    public void setPath(String path)
    {
        if (path.endsWith("/"))
        {
            path = path.substring(0, path.length() - 1);
        }
        this.path = path;
    }

    /**
     * Retrieve the complete path to the executable.
     *
     * @return the path as string
     */
    public String getExecutableWithPath()
    {
        String reval = path == null ? "" : path;
        if (!reval.isEmpty())
        {
            reval += "/";
        }
        reval += getExecutable();
        return reval;
    }

    /**
     * Check whether the executable uses positional parameters (as opposed
     * parameters with letters).
     *
     * @return true if so, false otherwise
     */
    public boolean hasPositionalParameters()
    {
        return parameters.isPositional;
    }

    /**
     * Retrieve the list of parameters the executable uses.
     *
     * @return the defined parameters
     */
    public ParameterList getParameters()
    {
        return parameters;
    }

    /**
     * Get a table model that can be used in GUI components such as JTable to
     * edit the parameters.
     *
     * @return the model of editable parameters
     */
    public AbstractTableModel getEditParameterModel()
    {
        parameters.setModelMode(ParameterList.ModelMode.EDIT);
        return parameters;
    }

    private AbstractTableModel getPositionalParameterModel()
    {
        parameters.setModelMode(ParameterList.ModelMode.POSITIONAL_EVALUATED);
        return parameters;
    }

    private AbstractTableModel getLetterParameterModel()
    {
        parameters.setModelMode(ParameterList.ModelMode.PARAMLETTER_EVALUATED);
        return parameters;
    }

    /**
     * Retrieve the parameters that are currently set for an instance as table
     * model (for the use in GUI components such as JTable).
     *
     * @return the table model
     */
    public AbstractTableModel getFilledParameterModel()
    {
        return (hasPositionalParameters()) ?
               getPositionalParameterModel() : getLetterParameterModel();
    }

    /**
     * Set new parameters.
     *
     * @param parameters the new parameters
     */
    public void setParameters(ParameterList parameters)
    {
        this.parameters = parameters;
    }

    Boolean allParametersDefined()
    {
        for (AbstractParameter p : parameters)
        {
            if (p.isMandatory() && p.hasArgument() &&
                p.getDefaultedValue().isEmpty())
            {
                return false;
            }
        }

        return true;
    }

    String getMissingParameters()
    {
        String reval = "";
        Integer pos = 0;
        for (AbstractParameter p : parameters)
        {
            if (p.isMandatory() && p.hasArgument() &&
                p.getDefaultedValue().isEmpty())
            {
                if (p.getClass() == LetterParameter.class)
                {
                    reval += ((LetterParameter) p).getParameterString() + " ";
                }
                else
                {
                    reval += "[" + pos + "] ";
                }
            }
            pos++;
        }

        return reval;
    }

    @Override
    public int compareTo(Object rhs)
    {
        if (!rhs.getClass().equals(this.getClass()))
        {
            return getClass().getName().compareTo(rhs.getClass().getName());
        }
        return name.compareTo(((ExeDefinition) rhs).name);
    }

    /**
     * Retrieves whether this is a valid definition of an executable.
     *
     * @return true is do, false otherwise
     */
    public boolean isValid()
    {
        return name != null && !name.isEmpty() &&
               executable != null && !executable.isEmpty();
    }
}
