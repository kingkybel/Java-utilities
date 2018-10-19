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
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.AbstractTableModel;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementWrapper;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;

/**
 * Aggregate to keep environment variables and their values separate by
 * user-defined categories.
 *
 * @author kybelksd
 */
@XmlAccessorType(XmlAccessType.FIELD)
public final class EnvironmentVarModel
        extends AbstractTableModel
        implements Serializable
{

    private static final Class CLAZZ = EnvironmentVarModel.class;
    private static final String CLASS_NAME = CLAZZ.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);
    private static final long serialVersionUID = -8940196742313991701L;

    private static final String NEW_VAR = NbBundle.getMessage(
                                CLAZZ,
                                "EnvironmentVarModel.newVarBaseName");

    @XmlElementWrapper(name = "environmentVariables")
    private ArrayList<EnvironmentVar> allEnvVars = new ArrayList<>();
    private HashMap<String, Integer> varName2index = new HashMap<>();
    private TreeMap<String, ArrayList<Integer>> category2indexList =
                                                new TreeMap<>();

    private String modelCategory = EnvironmentVar.ALL_CATEGORIES;

    /**
     * Default construct.
     */
    public EnvironmentVarModel()
    {
        addIndexToCategory(EnvironmentVar.ALL_CATEGORIES, -1);
    }

    /**
     * Copy construct.
     *
     * @param rhs right hand side.
     */
    public EnvironmentVarModel(EnvironmentVarModel rhs)
    {
        this();
        for (EnvironmentVar rhsVar : rhs.allEnvVars)
        {
            add(new EnvironmentVar(rhsVar));
        }
    }

    /**
     * Construct by reading values from file.
     *
     * @param filename file to read from
     */
    public EnvironmentVarModel(String filename)
    {
        this();
        try
        {
            initialiseVarsFromFile(filename);
        }
        catch (Exception ex)
        {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    @Override // overriding Object
    public String toString()
    {
        String reval = "";
        for (EnvironmentVar v : allEnvVars)
        {
            reval += v.toString() + StringUtils.NEWLINE;
        }

        return reval;
    }

    /**
     * Add an environment variable to the environment. Also update the category
     * set.
     *
     * @param var variable to add
     */
    public void add(EnvironmentVar var)
    {
        if (var != null)
        {
            if (var.getCategory() == null ||
                var.getCategory().equals(EnvironmentVar.ALL_CATEGORIES))
            {
                var.setCategory(EnvironmentVar.NULL_CATEGORY);
            }
            if (varName2index.containsKey(var.getName()))
            {
                final Integer currentIndex = varName2index.get(var.getName());
                allEnvVars.set(currentIndex, var);
                fireTableRowsUpdated(currentIndex, currentIndex);
            }
            else if (allEnvVars.add(var))
            {
                int newIndex = allEnvVars.size() - 1;
                addIndexToCategory(EnvironmentVar.ALL_CATEGORIES, newIndex);
                addIndexToCategory(var.getCategory(), newIndex);
                varName2index.put(var.getName(), newIndex);

                fireTableDataChanged();
            }
        }
    }

    /**
     * Retrieve a category specific Table-model for example for the use in a
     * JTable.
     *
     * @param category
     * @return the table model relating to this category
     */
    public EnvironmentVarModel viewCategory(String category)
    {
        setModelCategory(category);
        return this;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return true;
    }

    /**
     * Initialize all variables so that they exist in the model by reading file
     * as a string and reusing initialiseVarsFromString().
     *
     * @param fileName
     * @throws Exception if the file does not exists or cannot be read from
     */
    public void initialiseVarsFromFile(String fileName) throws Exception
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
     * @throws java.lang.Exception
     */
    public void initialiseVarsFromString(String str) throws Exception
    {
        allEnvVars = new ArrayList<>();
        category2indexList = new TreeMap<>();
        try
        {
            str = str.replaceAll("\\\\" + StringUtils.NEWLINE, "");
            String[] lines = str.split(StringUtils.NEWLINE);
            for (String line : lines)
            {
                EnvironmentVar var = EnvironmentVar.fromString(line);
                if (var != null)
                {
                    add(var);
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.log(Level.WARNING, e.toString());
        }
    }

    /**
     * Prime all variables of from the shell-environment. Update the values of
     * the EnvironmentVar wrapper objects using the values that were set in the
     * shell environment by other means (like a previously run shell-script).
     *
     * @throws java.lang.Exception
     */
    public void primeVarsFromOS() throws Exception
    {
        for (EnvironmentVar var : allEnvVars)
        {
            primeVarFromOS(var);
        }
    }

    /**
     * Prime all variables of from the shell-environment. Update the values of
     * the EnvironmentVar wrapper objects using the values that were set in the
     * shell environment by other means (like a previously run shell-script).
     *
     * @param var
     * @throws java.lang.Exception
     */
    public void primeVarFromOS(EnvironmentVar var) throws Exception
    {
        if (var == null)
        {
            return;
        }

        ProcessBuilder builder = new ProcessBuilder();
        Map systemEnv = builder.environment();
        EnvironmentVar.StereoType valueType = var.getStereoType();
        String varName = var.getName();
        boolean definedInSysEnv = systemEnv.containsKey(varName);
        if (definedInSysEnv)
        {
            String valueInSysEnv = (String) systemEnv.get(varName);
            PodVariant pod = null;
            switch (valueType.getPodVariantType())
            {
                case BOOLEAN:
                {
                    pod = new PodVariant(
                    Boolean.parseBoolean(valueInSysEnv));
                    break;
                }
                case INTEGER:
                {
                    pod = new PodVariant(
                    Integer.parseInt(valueInSysEnv));
                    break;
                }
                case STRING:
                {
                    String theVal = valueInSysEnv;
                    if (theVal.contains("%USER%"))
                    {
                        theVal = theVal.replace("%USER%",
                                                System.getenv("USER"));
                    }
                    pod = new PodVariant(theVal);
                    break;
                }
                case DOUBLE:
                {
                    pod = new PodVariant(
                    Double.parseDouble((String) systemEnv.get(varName)));
                    break;
                }
            }
            var.setValue(pod);
        }
    }

    /**
     * Retrieve the value of the given variable as String.
     *
     * @param varName
     * @return null if unsuccessful
     */
    public String getString(String varName)
    {
        if (varName != null && varName2index.containsKey(varName))
        {
            final Integer index = varName2index.get(varName);
            return allEnvVars.get(index).getValue().getStringValue();
        }

        return "";
    }

    /**
     * Retrieve the value of the given variable as Boolean.
     *
     * @param varName
     * @return null if unsuccessful
     */
    public Boolean getBoolean(String varName)
    {
        if (varName != null && varName2index.containsKey(varName))
        {
            final Integer index = varName2index.get(varName);
            return allEnvVars.get(index).getValue().getBooleanValue();
        }
        return false;

    }

    /**
     * Retrieve the value of the given variable as Integer.
     *
     * @param varName variable name
     * @return null if unsuccessful
     */
    public Integer getInteger(String varName)
    {
        if (varName != null && varName2index.containsKey(varName))
        {
            final Integer index = varName2index.get(varName);
            return allEnvVars.get(index).getValue().getIntegerValue();
        }

        return 0;
    }

    /**
     * Retrieve the value of the given variable as Double.
     *
     * @param varName variable name
     * @return null if unsuccessful
     */
    public Double getDouble(String varName)
    {
        if (varName != null && varName2index.containsKey(varName))
        {
            final Integer index = varName2index.get(varName);
            return allEnvVars.get(index).getValue().getDoubleValue();
        }

        return 0.0;
    }

    /**
     * Set/Modify a variable.
     *
     * @param varName variable name
     * @param newCat  new category name
     * @param val     new value
     * @throws java.lang.Exception
     */
    public void setValue(String varName, String newCat, PodVariant val)
            throws Exception
    {
        if (varName == null || varName.isEmpty())
        {
            varName = generateNewVarName();
        }

        Integer index = varName2index.get(varName);
        if (index != null)
        {
            EnvironmentVar var = allEnvVars.get(index);
            if (var.getName().equals(varName))
            {
                var.setValue(val);
                // only set the category if the newCat != null
                if (newCat != null)
                {
                    var.setCategory(newCat);
                }
            }
        }
        else
        {
            // variable not found so we need to add it
            EnvironmentVar var = new EnvironmentVar(varName, newCat, val);
            add(var);
        }
        fireTableDataChanged();
    }

    /**
     * Set/Modify a variable.
     *
     * @param environmentVar variable name
     * @param val            value
     * @throws java.lang.Exception
     */
    public void setValue(String environmentVar, Object val) throws Exception
    {
        setValue(environmentVar, null, new PodVariant(val));
    }

    /**
     * Retrieve the set of category names.
     *
     * @return the categories
     */
    public Set<String> getCategoryNameSet()
    {
        return category2indexList.keySet();
    }

    /**
     * Create a String that defining all the defined environment variables in
     * ksh-syntax.
     *
     * @param addExport if true than export all the variables
     * @return the string
     */
    public String createShellVariableString(boolean addExport)
    {
        String reval = "";

        for (EnvironmentVar var : allEnvVars)
        {
            reval += StringUtils.NEWLINE +
                     "# " +
                     var.getCategory() +
                     ", type:" +
                     var.getStereoType() +
                     StringUtils.NEWLINE;
            reval += ((var.getDefined()) ? "" : "#") +
                     (addExport ? "export " : "") +
                     var.getName() +
                     "=\"" +
                     var.getValue() +
                     "\"" +
                     StringUtils.NEWLINE;
        }

        return reval;

    }

    /**
     * Create a String that defining all the defined environment variables in
     * ksh-syntax.
     *
     * @return the string
     */
    public String createShellVariableString()
    {
        return EnvironmentVarModel.this.createShellVariableString(false);
    }

    /**
     * Remove a category of environment variables including all the variables
     * themselves.
     *
     * @param category the category to be removed identified by its name
     */
    public void removeCategory(String category)
    {
        if (category != null &&
            !category.isEmpty() &&
            category2indexList.keySet().contains(category))
        {
            ArrayList<EnvironmentVar> copiedAllEnvVars =
                                      (ArrayList<EnvironmentVar>) allEnvVars.
                                      clone();
            allEnvVars = new ArrayList<>();
            category2indexList = new TreeMap<>();
            varName2index = new HashMap<>();
            for (EnvironmentVar var : copiedAllEnvVars)
            {
                if (!var.getCategory().equals(category))
                {
                    add(var);
                }
            }
        }
        fireTableDataChanged();
    }

    /**
     *
     * @param category
     * @param dataIndex
     */
    public void addIndexToCategory(String category, int dataIndex)
    {
        if (!category2indexList.keySet().contains(category))
        {
            category2indexList.put(category, new ArrayList<Integer>());
        }
        if (dataIndex > -1)
        {
            // remove the reference of the data index from any other category
            for (String cat : category2indexList.keySet())
            {
                if (!cat.equals(EnvironmentVar.ALL_CATEGORIES) &&
                    !cat.equals(category) &&
                    category2indexList.get(cat).contains(dataIndex))
                {
                    category2indexList.get(cat).remove(
                            category2indexList.get(cat).indexOf(dataIndex));
                }
            }
            category2indexList.get(category).add(dataIndex);
        }
    }

    /**
     * Retrieve a set of all variable names as set.
     *
     * @return a set of all variable names as set
     */
    public Set<String> allVariableNames()
    {
        return varName2index.keySet();
    }

    /**
     * Undefine all environment variables.
     */
    public void undefineAll()
    {
        for (Integer index : category2indexList.get(modelCategory))
        {
            allEnvVars.get(index).setDefined(false);
        }
        fireTableDataChanged();
    }

    /**
     * Make all environment variables "defined".
     */
    public void defineAll()
    {
        for (Integer index : category2indexList.get(modelCategory))
        {
            allEnvVars.get(index).setDefined(true);
        }
        fireTableDataChanged();
    }

    /**
     * Sets all boolean variables to the same value.
     *
     * @param on true or false
     */
    public void setBooleans(boolean on)
    {
        for (Integer index : category2indexList.get(modelCategory))
        {
            allEnvVars.get(index).getValue().setBooleanValue(on);
        }
        fireTableDataChanged();
    }

    private String generateNewVarName()
    {
        String reval = NEW_VAR;
        int num = 0;
        do
        {
            reval = NEW_VAR + num++;
        }
        while (allVariableNames().contains(reval));

        return reval;
    }

    /**
     * Remove the variable in row from the category.
     *
     * @param category
     * @param row
     */
    public void removeRowFromCategory(String category, int row)
    {
        if (category2indexList.keySet().contains(category))
        {
            ArrayList<Integer> indices = category2indexList.get(category);
            if (row < indices.size())
            {
                int dataIndex = indices.get(row);
                ArrayList<EnvironmentVar> copiedAllEnvVars =
                                          (ArrayList<EnvironmentVar>) allEnvVars.
                                          clone();
                allEnvVars = new ArrayList<>();
                category2indexList = new TreeMap<>();
                varName2index = new HashMap<>();
                for (int i = 0; i < copiedAllEnvVars.size(); i++)
                {
                    if (i != dataIndex)
                    {
                        add(copiedAllEnvVars.get(i));
                    }
                }
            }
        }
        fireTableDataChanged();
    }

    @Override // overriding AbstractTableModel
    public String getColumnName(int col)
    {
        return Columns.getEnum(
                (modelCategory.equals(EnvironmentVar.ALL_CATEGORIES)) ?
                col : col + 1).toString();
    }

    @Override // overriding AbstractTableModel
    public int getRowCount()
    {
        return category2indexList.get(modelCategory).size();
    }

    @Override // overriding AbstractTableModel
    public int getColumnCount()
    {
        return Columns.values().length -
               (modelCategory.equals(EnvironmentVar.ALL_CATEGORIES) ? 0 : 1);
    }

    /**
     * Retrieve the environment variable in specified row.
     *
     * @param row the row index
     * @return the variable
     */
    public EnvironmentVar get(int row)
    {
        final ArrayList<Integer> modelRows = category2indexList.get(
                                 modelCategory);
        final Integer index = modelRows.get(row);
        return allEnvVars.get(index);
    }

    @Override // overriding AbstractTableModel
    public synchronized Object getValueAt(int row, int col)
    {
        int modelCol = (modelCategory == null ||
                        modelCategory.equals(EnvironmentVar.ALL_CATEGORIES)) ?
                       col : col + 1;
        if (modelCol > getColumnCount())
        {
            return null;
        }
        final EnvironmentVar var = get(row);

        // EnvironmentVarModel.header.category
        // EnvironmentVarModel.header.defUndef"
        // EnvironmentVarModel.header.variable
        // EnvironmentVarModel.header.type
        // EnvironmentVarModel.header.value
        // Cat1,true,ENV_VAR1,GeneralString,strVal1
        return var == null ? null :
               (modelCol == 0) ? var.getCategory() :
               (modelCol == 1) ? var.getDefined() :
               (modelCol == 2) ? var.getName() :
               (modelCol == 3) ? var.getStereoType() :
               (modelCol == 4) ? (var.getValue() == null ?
                                  null :
                                  var.getValue().getValue()) :
               null;
    }

    @Override // overriding AbstractTableModel
    public void setValueAt(Object value, int row, int col)
    {
        int modelCol = (modelCategory == null ||
                        modelCategory.equals(EnvironmentVar.ALL_CATEGORIES)) ?
                       col : col + 1;
        if (row >= category2indexList.get(modelCategory).size())
        {
            return;
        }
        EnvironmentVar currentInfo = get(row);

        // EnvironmentVarModel.header.category
        // EnvironmentVarModel.header.defUndef"
        // EnvironmentVarModel.header.variable
        // EnvironmentVarModel.header.type
        // EnvironmentVarModel.header.value
        // Cat1,true,ENV_VAR1,GeneralString,strVal1
        switch (modelCol)
        {
            case 0:
                String cat = (String) value;
                if (cat == null || cat.isEmpty())
                {
                    cat = EnvironmentVar.NULL_CATEGORY;
                }
                if (!category2indexList.containsKey(cat))
                {
                    addIndexToCategory(cat, -1);
                }
                currentInfo.setCategory(cat);
                break;
            case 1:
                currentInfo.setDefined((Boolean) value);
                break;
            case 2:
                try
                {
                    String cleanedName = ((String) value).replaceAll(
                           "[^a-zA-Z_0-9]",
                           "");
                    currentInfo.setName(cleanedName);
                }
                catch (Exception ex)
                {
                    Exceptions.printStackTrace(ex);
                }
                break;
            case 3:
                EnvironmentVar.StereoType stereoType =
                                          (EnvironmentVar.StereoType) value;
                currentInfo.setStereoType(stereoType);
                break;
            case 4:
                currentInfo.setValue(new PodVariant(value));
                break;
        }
        allEnvVars.set(category2indexList.get(modelCategory).get(row),
                       currentInfo);
        fireTableDataChanged();
    }

    /**
     * Retrieve all variables of a certain category.
     *
     * @param category the category to search for
     * @return the list of matching environment variables
     */
    public ArrayList<EnvironmentVar> getCategoryVariables(String category)
    {
        if (category == null || category.equals(EnvironmentVar.ALL_CATEGORIES))
        {
            return allEnvVars;
        }
        ArrayList<Integer> indices = category2indexList.get(category);
        if (indices == null)
        {
            return null;
        }
        ArrayList<EnvironmentVar> reval = new ArrayList<>();
        for (Integer index : indices)
        {
            reval.add(allEnvVars.get(index));
        }
        return reval;
    }

    /**
     * Adds a new (empty) variable to a category.
     *
     * @param category the category to add to
     */
    public void addNewVariable(String category)
    {
        try
        {
            EnvironmentVar var = new EnvironmentVar(generateNewVarName(),
                                                    category,
                                                    null);
            add(var);
        }
        catch (Exception ex)
        {
            LOGGER.log(Level.SEVERE, ex.toString());
        }
    }

    /**
     * Retrieve the current category of this model.
     *
     * @return the current category
     */
    public String getModelCategory()
    {
        return modelCategory;
    }

    /**
     * Set the current category of this model.
     *
     * @param modelCategory the new category
     */
    public void setModelCategory(String modelCategory)
    {
        this.modelCategory = modelCategory;
    }

    /**
     * Check whether a variable is contained in the underlying data.
     *
     * @param name variable name
     * @return true if so, false otherwise
     */
    public boolean containsVariableName(String name)
    {
        return varName2index.keySet().contains(name);
    }

    /**
     * Reset the table model by clearing all the values and informing listeners.
     */
    public void reset()
    {
        allEnvVars.clear();
        varName2index.clear();
        category2indexList.clear();
        fireTableDataChanged();
    }

    /**
     * Enumeration of the columns per Environment-variable.
     */
    public enum Columns
    {

        /**
         * User defined category of an environment variable. Used to organise
         * big set of variables.
         */
        Category,
        /**
         * Is the variable defined or not defined. This can be used to make a
         * variable "defined in principle".
         */
        DefUndef,
        /**
         * Name of the variable.
         */
        Variable,
        /**
         * "type" of a variable. All environment variables are strings but to
         * add some type-safety one can change the stereotype to restrict what
         * can be assigned. Also used for"nice" editing.
         */
        StereoType,
        /**
         * The value of the environment variable.
         */
        Value;

        /**
         * Retrieve the enum value given its ordinal.
         *
         * @param ordinal ordinal of the value
         * @return the enum value
         */
        public static Columns getEnum(int ordinal)
        {
            return values()[(ordinal < 0 || ordinal > values().length) ?
                            0 :
                            ordinal];
        }

        @Override
        public String toString()
        {
            return this == Category ?
                   NbBundle.getMessage(CLAZZ,
                                       "EnvironmentVarModel.header.category") :
                   this == DefUndef ?
                   NbBundle.getMessage(CLAZZ,
                                       "EnvironmentVarModel.header.defUndef") :
                   this == Variable ?
                   NbBundle.getMessage(CLAZZ,
                                       "EnvironmentVarModel.header.variable") :
                   this == StereoType ?
                   NbBundle.getMessage(CLAZZ, "EnvironmentVarModel.header.type") :
                   this == Value ?
                   NbBundle.
                   getMessage(CLAZZ, "EnvironmentVarModel.header.value") :
                   "";
        }
    }

}
