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

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Logger;
import org.openide.util.NbBundle;

/**
 * Class to attach a category, type and default to environment variables.
 *
 * @author kybelksd
 */
public class EnvironmentVar implements Comparable, Serializable
{

    private static final Class CLAZZ = EnvironmentVar.class;
    private static final String CLASS_NAME = CLAZZ.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    static final String NULL_CATEGORY = NbBundle.getMessage(
                        CLAZZ,
                        "EnvironmentVar.uncategorized");
    static final String ALL_CATEGORIES = NbBundle.getMessage(
                        CLAZZ,
                        "EnvironmentVar.allCategories");

    /**
     * Create an EnvironentVar from a String.
     *
     * @param definitionStr in format
     *                      stereotype,category,name,defined,value,default .
     *                      Need minimum of the first 4 fields. Everything after
     *                      and including the first appearance of the '#'
     *                      character is ignored as comment
     * @return an EnvironmentVar represented by the String
     * @throws java.lang.Exception
     */
    public static EnvironmentVar fromString(String definitionStr)
            throws Exception
    {
        EnvironmentVar reval = new EnvironmentVar();
        definitionStr = definitionStr.replaceAll("\\" + StringUtils.NEWLINE, "");
        if (definitionStr.contains("#"))
        {
            definitionStr = definitionStr.substring(0, definitionStr.
                                                    indexOf('#'));
        }
        String[] columns = definitionStr.split(",");
        if (columns.length < 4)
        {
            return null;
        }
        for (int i = 0; i < columns.length; i++)
        {
            columns[i] = columns[i].trim();
        }
        String stereoTypeStr = columns[0];
        reval.setStereoType(StereoType.fromString(stereoTypeStr));
        PodVariant.Type podType = reval.getStereoType().getPodVariantType();
        reval.setValue(new PodVariant(podType));
        reval.setCategory(columns[1]);
        reval.setName(columns[2]);
        reval.setDefined(StringUtils.scanBoolString(columns[3]));

        if (columns.length > 4)
        {
            reval.setValue(PodVariant.fromString(podType, columns[4]));
        }
        return reval;
    }

    private String name = "";
    private boolean defined = false;
    private PodVariant value = new PodVariant();
    private String category = "";
    private StereoType stereoType = StereoType.GeneralString;

    /**
     * Default construct.
     */
    public EnvironmentVar()
    {
    }

    /**
     * Construct.
     *
     * @param stereoType plain old data type
     * @param category   a category name - any unique string
     * @param name       name of the variable
     * @param defined    flag to indicate whether the variable is defined or not
     * @param val        value of the variable
     * @throws java.lang.Exception when new name is empty or contains invalid
     *                             characters
     */
    public EnvironmentVar(StereoType stereoType,
                          String category,
                          String name,
                          boolean defined,
                          PodVariant... val) throws Exception
    {
        setStereoType(stereoType);
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
     * @throws java.lang.Exception when new name is empty or contains invalid
     *                             characters
     */
    public EnvironmentVar(String name, String category, PodVariant value)
            throws Exception
    {
        this((value == null || value.getType() == PodVariant.Type.STRING) ?
             StereoType.GeneralString :
             value.getType() == PodVariant.Type.INTEGER ?
             StereoType.IntegralValue :
             value.getType() == PodVariant.Type.DOUBLE ?
             StereoType.FloatingPointValue :
             value.getType() == PodVariant.Type.BOOLEAN ? StereoType.BooleanFlag :
             StereoType.GeneralString,
             category,
             name,
             true,
             value);
    }

    @Override
    public String toString()
    {
        String reval = getStereoType().name() +
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
     * @throws java.lang.Exception when new name is empty or contains invalid
     *                             characters
     */
    public final void setName(String name) throws Exception
    {
        if (name == null)
        {
            name = "";
        }
        String regex = "^[a-zA-Z_]+[a-zA-Z_0-9]*$";
        if (!name.matches(regex))
        {
            throw new Exception(
                    NbBundle.getMessage(
                            CLAZZ,
                            "EnvironmenVar.nameFormatException",
                            name));
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
     * Set the variable as defined, ie is it a placeholder or not.
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

    /**
     * Retrieve the stereotype.
     *
     * @return the current stereotype
     */
    public StereoType getStereoType()
    {
        return stereoType;
    }

    /**
     * Set the stereotype of the variable.
     *
     * @param stereoType the new stereotype
     */
    public final void setStereoType(StereoType stereoType)
    {
        if (stereoType == null)
        {
            stereoType = StereoType.GeneralString;
        }
        this.stereoType = stereoType;
        if (!this.stereoType.isCompatible(value))
        {
            setValue(new PodVariant(stereoType.getPodVariantType()));
        }
    }

    /**
     * Stereotypes for environment variables. They are introduced to enable
     * enhanced format checks and diagnostics.
     */
    public enum StereoType
    {

        /**
         * General string value.
         */
        GeneralString,
        /**
         * The environment variable can assume only boolean values.
         */
        BooleanFlag,
        /**
         * The environment variable can assume only integral values.
         */
        IntegralValue,
        /**
         * The environment variable can assume only floating point values.
         */
        FloatingPointValue,
        /**
         * The environment variable can assume only search-path string values.
         */
        SearchPath,
        /**
         * The environment variable can assume only strings describing
         * directories.
         */
        DirectoryString,
        /**
         * The environment variable can assume only strings describing
         * filenames.
         */
        FilePath,
        /**
         * The environment variable can assume only strings describing
         * port-numbers.
         */
        PortNumber,
        /**
         * The environment variable can assume only strings describing
         * server-names/descriptions.
         */
        ServerDescriptionString,
        /**
         * The environment variable can assume only strings describing user-IDs.
         */
        UserID,
        /**
         * The environment variable can assume only strings describing
         * passwords.
         */
        Password,
        /**
         * The environment variable can assume only strings describing
         * database-servers.
         */
        DatabaseServer,
        /**
         * The environment variable can assume only strings describing
         * databases.
         */
        Database,
        /**
         * The environment variable can assume only strings describing
         * database-users.
         */
        DatabaseUser,
        /**
         * The environment variable can assume only strings describing
         * database-passwords.
         */
        DatabasePassword;

        static TreeSet<String> synonyms(String bundleID)
        {
            TreeSet<String> reval = new TreeSet<>();
            reval.addAll(
                    Arrays.asList(
                            NbBundle.getMessage(CLAZZ, bundleID).split(",")));
            return reval;
        }
        static TreeMap<StereoType, TreeSet<String>> SYNONYMS = init();

        static TreeMap<StereoType, TreeSet<String>> init()
        {
            TreeMap<StereoType, TreeSet<String>> reval = new TreeMap<>();
            for (StereoType stereoType : values())
            {
                String clsName = stereoType.getClass().getCanonicalName();
                String pkgName = stereoType.getClass().getPackage().getName() +
                                 ".";
                String bundleID = (clsName + "." + stereoType.name()).
                       replaceFirst(pkgName, "") + "Synonyms";
                reval.put(stereoType, synonyms(bundleID));

            }
            return reval;
        }

        /**
         * Create a StereoType from the given string.
         *
         * @param typeStr the string describing the Stereotype
         * @return the StereoType - enum-value, GeneralString if parsing fails
         */
        public static StereoType fromString(String typeStr)
        {
            StereoType reval = GeneralString;
            String lowerTypeStr = typeStr == null ?
                                  "generalstring" :
                                  typeStr.toLowerCase().trim();
            for (StereoType stereoType : values())
            {
                if (SYNONYMS.get(stereoType).contains(lowerTypeStr))
                {
                    return stereoType;
                }
            }
            return reval;
        }

        static String bundleString(String bundleID)
        {
            return NbBundle.getMessage(CLAZZ, bundleID);
        }

        @Override
        public String toString()
        {
            String bundleID = (getClass().getCanonicalName() +
                               "." +
                               this.name()).replaceFirst(
                   getClass().getPackage().getName() + ".", "");
            return NbBundle.getMessage(CLAZZ, bundleID);
        }

        /**
         * Maps the SteroType to the PodVariant-type needed to represent it.
         *
         * @return the PodVarian -type
         */
        public PodVariant.Type getPodVariantType()
        {
            return this == GeneralString ? PodVariant.Type.STRING :
                   this == BooleanFlag ? PodVariant.Type.BOOLEAN :
                   this == IntegralValue ? PodVariant.Type.INTEGER :
                   this == FloatingPointValue ? PodVariant.Type.DOUBLE :
                   this == SearchPath ? PodVariant.Type.STRING :
                   this == DirectoryString ? PodVariant.Type.STRING :
                   this == FilePath ? PodVariant.Type.STRING :
                   this == PortNumber ? PodVariant.Type.INTEGER :
                   this == ServerDescriptionString ? PodVariant.Type.STRING :
                   this == UserID ? PodVariant.Type.STRING :
                   this == Password ? PodVariant.Type.STRING :
                   this == DatabaseServer ? PodVariant.Type.STRING :
                   this == Database ? PodVariant.Type.STRING :
                   this == DatabaseUser ? PodVariant.Type.STRING :
                   this == DatabasePassword ? PodVariant.Type.STRING :
                   PodVariant.Type.UNDEFINED;
        }

        /**
         * Check whether two types are compatible
         *
         * @param podVar the other pod-variant
         * @return true if so, false otherwise
         */
        public boolean isCompatible(PodVariant podVar)
        {
            if (podVar == null)
            {
                return true;
            }
            PodVariant.Type myPodtType = getPodVariantType();
            if (myPodtType.equals(podVar.getType()))
            {
                return true;
            }

            if (myPodtType.equals(PodVariant.Type.STRING))
            {
                return true;
            }

            if (myPodtType.equals(PodVariant.Type.DOUBLE))
            {
                return podVar.isDouble() || podVar.isInteger();
            }

            return false;
        }
    }

}
