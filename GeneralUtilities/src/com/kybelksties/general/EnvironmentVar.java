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

    /**
     * Stereotypes for environment variables. They are introduced to enable
     * enhanced format checks and diagnostics.
     */
    public enum StereoType
    {

        GeneralString,
        BooleanFlag,
        IntegralValue,
        FloatingPointValue,
        SearchPath,
        DirectoryString,
        FilePath,
        PortNumber,
        ServerDescriptionString,
        UserID,
        Password,
        DatabaseServer,
        Database,
        DatabaseUser,
        DatabasePassword;

        static TreeSet<String> synonyms(String bundleID)
        {
            TreeSet<String> reval = new TreeSet<>();
            reval.addAll(
                    Arrays.asList(
                            NbBundle.getMessage(CLAZZ, bundleID).split(",")));
            return reval;
        }
        public static final TreeSet<String> GENERAL_STRING_SYNONYMS =
                                            synonyms(
                                                    "EnvironmentVar.Type.GeneralStringSynonyms");
        public static final TreeSet<String> BOOLEAN_FLAG_SYNONYMS =
                                            synonyms(
                                                    "EnvironmentVar.Type.BooleanFlagSynonyms");
        public static final TreeSet<String> INTEGRAL_VALUE_SYNONYMS =
                                            synonyms(
                                                    "EnvironmentVar.Type.IntegralValueSynonyms");
        public static final TreeSet<String> FLOATING_POINT_VALUE_SYNONYMS =
                                            synonyms(
                                                    "EnvironmentVar.Type.FloatingPointValueSynonyms");
        public static final TreeSet<String> SEARCH_PATH_SYNONYMS =
                                            synonyms(
                                                    "EnvironmentVar.Type.SearchPathSynonyms");
        public static final TreeSet<String> DIRECTORY_STRING_SYNONYMS =
                                            synonyms(
                                                    "EnvironmentVar.Type.DirectoryStringSynonyms");
        public static final TreeSet<String> FILE_PATH_SYNONYMS =
                                            synonyms(
                                                    "EnvironmentVar.Type.FilePathSynonyms");
        public static final TreeSet<String> PORT_NUMBER_SYNONYMS =
                                            synonyms(
                                                    "EnvironmentVar.Type.PortNumberSynonyms");
        public static final TreeSet<String> SERVER_DESCRIPTION_STRING_SYNONYMS =
                                            synonyms(
                                                    "EnvironmentVar.Type.ServerDescriptionStringSynonyms");
        public static final TreeSet<String> USER_ID_SYNONYMS =
                                            synonyms(
                                                    "EnvironmentVar.Type.UserIDSynonyms");
        public static final TreeSet<String> PASSWORD_SYNONYMS =
                                            synonyms(
                                                    "EnvironmentVar.Type.PasswordSynonyms");
        public static final TreeSet<String> DATABASE_SERVER_SYNONYMS =
                                            synonyms(
                                                    "EnvironmentVar.Type.DatabaseServerSynonyms");
        public static final TreeSet<String> DATABASE_SYNONYMS =
                                            synonyms(
                                                    "EnvironmentVar.Type.DatabaseSynonyms");
        public static final TreeSet<String> DATABASE_USER_SYNONYMS =
                                            synonyms(
                                                    "EnvironmentVar.Type.DatabaseUserSynonyms");
        public static final TreeSet<String> DATABASE_PASSWORD_SYNONYMS =
                                            synonyms(
                                                    "EnvironmentVar.Type.DatabasePasswordSynonyms");

        public static StereoType fromString(String typeStr)
        {
            StereoType reval = GeneralString;
            String lowerTypeStr = typeStr == null ?
                                  "generalstring" :
                                  typeStr.toLowerCase().trim();
            if (GENERAL_STRING_SYNONYMS.contains(lowerTypeStr))
            {
                reval = StereoType.GeneralString;
            }
            else if (BOOLEAN_FLAG_SYNONYMS.contains(lowerTypeStr))
            {
                reval = StereoType.BooleanFlag;
            }
            else if (INTEGRAL_VALUE_SYNONYMS.contains(lowerTypeStr))
            {
                reval = StereoType.IntegralValue;
            }
            else if (FLOATING_POINT_VALUE_SYNONYMS.contains(lowerTypeStr))
            {
                reval = StereoType.FloatingPointValue;
            }
            else if (SEARCH_PATH_SYNONYMS.contains(lowerTypeStr))
            {
                reval = StereoType.SearchPath;
            }
            else if (DIRECTORY_STRING_SYNONYMS.contains(lowerTypeStr))
            {
                reval = StereoType.DirectoryString;
            }
            else if (FILE_PATH_SYNONYMS.contains(lowerTypeStr))
            {
                reval = StereoType.FilePath;
            }
            else if (PORT_NUMBER_SYNONYMS.contains(lowerTypeStr))
            {
                reval = StereoType.PortNumber;
            }
            else if (SERVER_DESCRIPTION_STRING_SYNONYMS.contains(lowerTypeStr))
            {
                reval = StereoType.ServerDescriptionString;
            }
            else if (USER_ID_SYNONYMS.contains(lowerTypeStr))
            {
                reval = StereoType.UserID;
            }
            else if (PASSWORD_SYNONYMS.contains(lowerTypeStr))
            {
                reval = StereoType.Password;
            }
            else if (DATABASE_SERVER_SYNONYMS.contains(lowerTypeStr))
            {
                reval = StereoType.DatabaseServer;
            }
            else if (DATABASE_SYNONYMS.contains(lowerTypeStr))
            {
                reval = StereoType.Database;
            }
            else if (DATABASE_USER_SYNONYMS.contains(lowerTypeStr))
            {
                reval = StereoType.DatabaseUser;
            }
            else if (DATABASE_PASSWORD_SYNONYMS.contains(lowerTypeStr))
            {
                reval = StereoType.DatabasePassword;
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
            return this.equals(GeneralString) ?
                   bundleString("EnvironmentVar.Type.GeneralString") :
                   this.equals(BooleanFlag) ?
                   bundleString("EnvironmentVar.Type.BooleanFlag") :
                   this.equals(IntegralValue) ?
                   bundleString("EnvironmentVar.Type.IntegralValue") :
                   this.equals(FloatingPointValue) ?
                   bundleString("EnvironmentVar.Type.FloatingPointValue") :
                   this.equals(SearchPath) ?
                   bundleString("EnvironmentVar.Type.SearchPath") :
                   this.equals(DirectoryString) ?
                   bundleString("EnvironmentVar.Type.DirectoryString") :
                   this.equals(FilePath) ?
                   bundleString("EnvironmentVar.Type.FilePath") :
                   this.equals(PortNumber) ?
                   bundleString("EnvironmentVar.Type.PortNumber") :
                   this.equals(ServerDescriptionString) ?
                   bundleString("EnvironmentVar.Type.ServerDescriptionString") :
                   this.equals(UserID) ?
                   bundleString("EnvironmentVar.Type.Password") :
                   this.equals(DatabaseServer) ?
                   bundleString("EnvironmentVar.Type.DatabaseServer") :
                   this.equals(Database) ?
                   bundleString("EnvironmentVar.Type.Database") :
                   this.equals(DatabaseUser) ?
                   bundleString("EnvironmentVar.Type.DatabaseUser") :
                   this.equals(DatabasePassword) ?
                   bundleString("EnvironmentVar.Type.DatabasePassword") :
                   bundleString("EnvironmentVar.Type.Unknown");
        }

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

        public boolean isCompatible(PodVariant podVar)
        {
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

    /**
     * Create an EnvironentVar from a String.
     *
     * @param definitionStr in format
     *                      stereotype,category,name,defined,value,default .
     *                      Need minimum of the first 4 fields. Everything after
     *                      and including the first appearance of the '#'
     *                      character is ignored as comment
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
     * @param podType  plain old data type
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
    public void setStereoType(StereoType stereoType)
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

}
