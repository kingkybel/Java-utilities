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

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 * Aggregate to keep environment variables and their values separate by
 * user-defined categories.
 *
 * @author kybelksd
 */
@XmlAccessorType(XmlAccessType.FIELD)
public final class EnvironmentVarSets implements Serializable
{

    private static final Class CLAZZ = EnvironmentVarSets.class;
    private static final String CLASS_NAME = CLAZZ.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);
    private static final long serialVersionUID = -8940196742313991701L;

    @XmlElementWrapper(name = "environmentVariables")
    ArrayList<EnvironmentVar> allEnvs = new ArrayList<>();

    private HashMap<String, EnvironmentVarTableModel> theVarTableModels =
                                                      new HashMap<>();

    /**
     * Default construct.
     */
    public EnvironmentVarSets()
    {
    }

    /**
     * Copy construct.
     *
     * @param rhs right hand side.
     */
    public EnvironmentVarSets(EnvironmentVarSets rhs)
    {
        allEnvs = new ArrayList<>();
        for (EnvironmentVar rhsVar : rhs.allEnvs)
        {
            add(new EnvironmentVar(rhsVar));
        }
    }

    /**
     * Construct by reading values from file.
     *
     * @param filename file to read from
     */
    public EnvironmentVarSets(String filename)
    {
        try
        {
            initialiseVarsFromFile(filename);
        }
        catch (IOException ex)
        {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String toString()
    {
        String reval = "";
        for (EnvironmentVar v : allEnvs)
        {
            reval += v.toString() + StringUtils.NEWLINE;
        }

        return reval;
    }

    /**
     * Add an environment variable to the environment. Also add to the all
     * environment collection.
     *
     * @param var variable to add
     */
    public void add(EnvironmentVar var)
    {
        if (var != null)
        {
            allEnvs.add(var);
            insertCategory(var.getCategory());
            theVarTableModels.get(var.getCategory()).add(var);
        }
    }

    /**
     * Add an environment variable to the environment.
     *
     * @param var
     */
    public void addToTableModel(EnvironmentVar var)
    {
        if (var != null)
        {
            insertCategory(var.getCategory());
            theVarTableModels.get(var.getCategory()).add(var);
        }
    }

    private void insertCategory(String category)
    {
        if (!theVarTableModels.containsKey(category))
        {
            theVarTableModels.put(category, new EnvironmentVarTableModel());
        }
    }

    /**
     * Retrieve a category specific Table-model for example for the use in a
     * JTable.
     *
     * @param category
     * @return the table model relating to this category
     */
    public EnvironmentVarTableModel getTableModel(String category)
    {
        return theVarTableModels.get(category);
    }

    /**
     * Initialize all variables so that they exist in the model by reading file
     * as a string and reusing initialiseVarsFromString().
     *
     * @param fileName
     * @throws IOException if the file does not exists or cannot be read from
     */
    public void initialiseVarsFromFile(String fileName) throws IOException
    {
        initialiseVarsFromString(FileUtilities.readText(fileName));
    }

    /**
     * Write the categorized environment to file in a format that late be read
     * in again.
     *
     * @param fileName
     * @throws IOException
     */
    public void writeToFile(String fileName) throws IOException
    {
        FileUtilities.saveText(fileName, toString());
    }

    /**
     * Initialize all variables so that they exist in the model.
     *
     * @param str comma delimited/line delimited lines
     *            [type],[category],[var-name],[default-value] if they are not
     *            defined in the O/S environment then they are still created but
     *            with a flag set to false to indicate that. This enables us to
     *            have all relevant variables at hand should we want to define
     *            them.
     */
    public void initialiseVarsFromString(String str)
    {
        Set<String> categories = new HashSet<>();
        allEnvs = new ArrayList<>();
        try
        {
            str = str.replaceAll("\\\\" + StringUtils.NEWLINE, "");
            String[] lines = str.split(StringUtils.NEWLINE);
            for (String line : lines)
            {
                EnvironmentVar var = EnvironmentVar.fromString(line);
                if (var != null)
                {
                    allEnvs.add(var);
                    categories.add(var.getCategory());
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.log(Level.WARNING, e.toString());
        }
        for (String category : categories)
        {
            initialiseVarsFromShell(category);
        }
    }

    /**
     * Initialize all variables of a category from the shell-environment.
     *
     * @param category
     */
    public void initialiseVarsFromShell(String category)
    {
        ProcessBuilder builder = new ProcessBuilder();
        Map systemEnv = builder.environment();

        for (EnvironmentVar var : allEnvs)
        {
            PodVariant.Type valueType = var.getType();
            String key = var.getName();
            String theCategory = var.getCategory();
            PodVariant value = var.getValue();
            boolean definedInSysEnv = systemEnv.containsKey(key);
            String valueInSysEnv =
                   definedInSysEnv ? (String) systemEnv.get(key) :
                   "";
            if (!theCategory.equals(category))
            {
                continue;
            }
            switch (valueType)
            {
                case BOOLEAN:
                {
                    boolean on = definedInSysEnv ?
                                 Boolean.parseBoolean(valueInSysEnv) :
                                 value.getBooleanValue();
                    addToTableModel(new EnvironmentVar(valueType,
                                                       theCategory,
                                                       key,
                                                       definedInSysEnv,
                                                       new PodVariant(on)));
                    break;
                }
                case INTEGER:
                {
                    Integer theVal = definedInSysEnv ?
                                     Integer.parseInt(valueInSysEnv) :
                                     value.getIntegerValue();
                    addToTableModel(new EnvironmentVar(valueType,
                                                       theCategory,
                                                       key,
                                                       definedInSysEnv,
                                                       new PodVariant(theVal)));
                    break;
                }
                case STRING:
                {
                    String theVal = definedInSysEnv ?
                                    valueInSysEnv :
                                    value.getStringValue();
                    if (theVal.contains("%USER%"))
                    {
                        theVal = theVal.replace("%USER%", System.getenv("USER"));
                    }
                    addToTableModel(new EnvironmentVar(valueType,
                                                       theCategory,
                                                       key,
                                                       definedInSysEnv,
                                                       new PodVariant(theVal)));
                    break;
                }
                case DOUBLE:
                {
                    Double theVal = definedInSysEnv ? Double.parseDouble(
                                    (String) systemEnv.get(key)) :
                                    value.getDoubleValue();
                    addToTableModel(new EnvironmentVar(valueType,
                                                       theCategory,
                                                       key,
                                                       definedInSysEnv,
                                                       new PodVariant(theVal)));
                    break;
                }
            }

        }
    }

    /**
     * Look for environmentVar in the whole set and attempt to retrieve as
     * String.
     *
     * @param environmentVar
     * @return null if unsuccessful
     */
    public String getString(String environmentVar)
    {
        for (EnvironmentVarTableModel model : theVarTableModels.values())
        {
            if (model.contains(environmentVar))
            {
                return model.get(environmentVar).getValue().getStringValue();
            }
        }

        return "";
    }

    /**
     * Look for environmentVar in the whole set and attempt to retrieve as
     * boolean.
     *
     * @param environmentVar
     * @return null if unsuccessful
     */
    public Boolean getBoolean(String environmentVar)
    {
        for (EnvironmentVarTableModel model : theVarTableModels.values())
        {
            if (model.contains(environmentVar))
            {
                return model.get(environmentVar).getValue().getBooleanValue();
            }
        }

        return false;
    }

    /**
     * Set/Modify a variable.
     *
     * @param varName variable name
     * @param newCat  new category name
     * @param val     new value
     */
    public void setValue(String varName, String newCat, PodVariant val)
    {
        if (varName == null || varName.isEmpty())
        {
            return;
        }
        if (newCat == null || newCat.isEmpty())
        {
            newCat = EnvironmentVar.NULL_CATEGORY;
        }

        for (EnvironmentVar var : allEnvs)
        {
            if (var.getName().equals(varName))
            {
                var.setValue(val);
                String oldCat = var.getCategory();
                if (newCat != null)
                {
                    var.setCategory(newCat);
                    if (!newCat.equals(oldCat))
                    {
                        initialiseVarsFromShell(newCat);
                        initialiseVarsFromShell(oldCat);
                    }
                }
                return;
            }
        }
        newCat = EnvironmentVar.NULL_CATEGORY;
        add(new EnvironmentVar(varName, newCat, val));
        initialiseVarsFromShell(newCat);
    }

    /**
     * Set/Modify a variable.
     *
     * @param environmentVar variable name
     * @param val            value
     */
    public void setValue(String environmentVar, Object val)
    {
        setValue(environmentVar, null, new PodVariant(val));
    }

    /**
     * Look for environmentVar in the whole set and attempt to retrieve as
     * Integer.
     *
     * @param environmentVar variable name
     * @return null if unsuccessful
     */
    public Integer getInteger(String environmentVar)
    {
        for (EnvironmentVarTableModel model : theVarTableModels.values())
        {
            if (model.contains(environmentVar))
            {
                return model.get(environmentVar).getValue().getIntegerValue();
            }
        }

        return 0;
    }

    /**
     * Look for environmentVar in the whole set and attempt to retrieve as
     * Double.
     *
     * @param environmentVar variable name
     * @return null if unsuccessful
     */
    public Double getDouble(String environmentVar)
    {
        for (EnvironmentVarTableModel model : theVarTableModels.values())
        {
            if (model.contains(environmentVar))
            {
                return model.get(environmentVar).getValue().getDoubleValue();
            }
        }

        return 0.0;
    }

    /**
     * Retrieve the set of category names.
     *
     * @return the categories
     */
    public Set<String> getCategoryNameSet()
    {
        return theVarTableModels.keySet();
    }

    /**
     * Create a String that exports all the defined environment variables.
     *
     * @return the string
     */
    public String createExportVariablesString()
    {
        String reval = "";
        Set<String> cats = getCategoryNameSet();
        for (String cat : cats)
        {
            reval += "# Catecory: " + cat + StringUtils.NEWLINE;
            EnvironmentVarTableModel catModel = getTableModel(cat);
            for (String key : catModel)
            {
                EnvironmentVar var = catModel.get(key);
                if (var.getDefined())
                {
                    reval += "export " +
                             key +
                             "=\"" +
                             var.getValue() +
                             "\"" +
                             StringUtils.NEWLINE;
                }
            }
        }
        return reval;

    }
}
