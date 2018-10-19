/*
 * Copyright (C) 2017 Dieter J Kybelksties
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
 * @date: 2017-11-22
 * @author: Dieter J Kybelksties
 */
package com.kybelksties.general;

import java.io.IOException;
import java.util.Set;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openide.util.Exceptions;

/**
 *
 * @author kybelksd
 */
public class EnvironmentVarModelTest
{

    private static final Class CLAZZ = EnvironmentVarModelTest.class;
    private static final String CLASS_NAME = CLAZZ.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    /**
     * Setup before this is created.
     */
    @BeforeClass
    public static void setUpClass()
    {
    }

    /**
     * Run this after this has been deleted
     */
    @AfterClass
    public static void tearDownClass()
    {
    }
    private final String fileName1 = "EnvVarTest1.cfg";
    private final String fileContent1 =
                         "GeneralString,Cat1,ENV_VAR1,true,strVal1" +
                         StringUtils.NEWLINE +
                         "BooleanFlag,Cat1,ENV_VAR2,true,1" +
                         StringUtils.NEWLINE +
                         "FloatingPointValue,Cat2,ENV_VAR3,true,3.1415" +
                         StringUtils.NEWLINE +
                         "GeneralString,Cat3,ENV_VAR4,true,strVal2" +
                         StringUtils.NEWLINE +
                         "IntegralValue,Cat3,ENV_VAR5,true,666" +
                         StringUtils.NEWLINE;

    /**
     * Test class for environment variables.
     */
    public EnvironmentVarModelTest()
    {
    }

    /**
     * Set up a test.
     */
    @Before
    public void setUp()
    {
        try
        {
            FileUtilities.saveText(fileName1, fileContent1);
        }
        catch (IOException ex)
        {
            Exceptions.printStackTrace(ex);
        }
    }

    /**
     * Clean up after a test.
     */
    @After
    public void tearDown()
    {
        FileUtilities.deleteRecursive(fileName1);
    }

    /**
     * Test of toString method, of class EnvironmentVarModel.
     */
    @Test
    public void testToString()
    {
        System.out.println("toString");
        EnvironmentVarModel instance = new EnvironmentVarModel();
        String expResult = "";
        String result = instance.toString();
        assertEquals(expResult, result);

        expResult = "GeneralString,Cat1,NEW_VAR_0,true" +
                    StringUtils.NEWLINE +
                    "GeneralString,Cat2,NEW_VAR_1,true" +
                    StringUtils.NEWLINE +
                    "GeneralString,Cat1,NEW_VAR_2,true" +
                    StringUtils.NEWLINE +
                    "GeneralString,Cat2,NEW_VAR_3,true" +
                    StringUtils.NEWLINE;
        for (int i = 0; i < 2; i++)
        {
            instance.addNewVariable("Cat1");
            instance.addNewVariable("Cat2");
        }
        result = instance.toString();
        assertEquals(expResult, result);
    }

    /**
     * Test of add method, of class EnvironmentVarModel.
     */
    @Test
    public void testAdd()
    {
        System.out.println("add");
        EnvironmentVar var = null;
        EnvironmentVarModel instance = new EnvironmentVarModel();
        // adding null doesn't change the model
        instance.add(var);

        String expResult = "";
        String result = instance.toString();
        assertEquals(expResult, result);
        try
        {
            var = new EnvironmentVar("ENV_VAR", "Cat1", null);
            instance.add(var);
        }
        catch (Exception ex)
        {
            fail("Unexpected exception." + ex.toString());
        }
        expResult = "GeneralString,Cat1,ENV_VAR,true" + StringUtils.NEWLINE;
        result = instance.toString();
        assertEquals(expResult, result);
    }

    /**
     * Test of viewCategory method, of class EnvironmentVarModel.
     */
    //   @Test
    public void testGetTableModel()
    {
        System.out.println("getTableModel");
        String category = "";
        EnvironmentVarModel instance = new EnvironmentVarModel();
        EnvironmentVarModel expResult = null;
        EnvironmentVarModel result = instance.viewCategory(category);
        assertEquals(expResult, result);
        // TODO review the generated test code and removeCategory the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of initialiseVarsFromFile method, of class EnvironmentVarModel.
     */
    @Test
    public void testInitialiseVarsFromFile()
    {
        System.out.println("initialiseVarsFromFile");
        EnvironmentVarModel instance = new EnvironmentVarModel();
        try
        {
            instance.initialiseVarsFromFile(fileName1);
        }
        catch (Exception ex)
        {
            fail("Unexpected exception." + ex.toString());
        }
        assertEquals(fileContent1, instance.toString());

    }

    /**
     * Test of writeToFile method, of class EnvironmentVarModel.
     */
    @Test
    public void testWriteToFile()
    {
        try
        {
            System.out.println("writeToFile");
            EnvironmentVarModel instance = new EnvironmentVarModel();
            instance.initialiseVarsFromFile(fileName1);
            String orig = instance.toString();
            instance.writeToFile(fileName1 + ".2");
            EnvironmentVarModel instance2 = new EnvironmentVarModel();
            instance2.initialiseVarsFromFile(fileName1 + ".2");
            String reRead = instance2.toString();
            assertEquals(orig, reRead);
            FileUtilities.deleteRecursive(fileName1 + ".2");
        }
        catch (Exception ex)
        {
            fail("Unexpected exception." + ex.toString());
        }

    }

    /**
     * Test of getString method, of class EnvironmentVarModel.
     */
    @Test
    public void testGetValues()
    {
        System.out.println("get{Value}");
        EnvironmentVarModel instance = new EnvironmentVarModel();
        try
        {
            instance.initialiseVarsFromFile(fileName1);
        }
        catch (Exception ex)
        {
            fail("Unexpected exception." + ex.toString());
        }

        assertEquals("strVal1", instance.getString("ENV_VAR1"));
        assertEquals(true, instance.getBoolean("ENV_VAR2"));
        assertEquals((Double) 3.1415, instance.getDouble("ENV_VAR3"));
        assertEquals((Integer) 666, instance.getInteger("ENV_VAR5"));
    }

    /**
     * Test of setValue method, of class EnvironmentVarModel.
     */
    @Test
    public void testSetValue()
    {
        try
        {
            System.out.println("setValue");
            EnvironmentVarModel instance = new EnvironmentVarModel();
            instance.setValue("VARIABLE1",
                              "Category 1",
                              new PodVariant(Boolean.TRUE));
            assertEquals(true, instance.getBoolean("VARIABLE1"));
            assertEquals(null, instance.getInteger("VARIABLE1"));

            instance.setValue("VARIABLE2",
                              "Category 2",
                              new PodVariant(666));
            assertEquals((Integer) 666, instance.getInteger("VARIABLE2"));
            assertEquals(null, instance.getDouble("VARIABLE2"));
            instance.setValue("VARIABLE3",
                              "Category 1",
                              new PodVariant(3.1415));
            assertEquals((Double) 3.1415, instance.getDouble("VARIABLE3"));
            assertEquals(null, instance.getBoolean("VARIABLE3"));
        }
        catch (Exception ex)
        {
            fail("Unexpected exception." + ex.toString());
        }
    }

    /**
     * Test of getCategoryNameSet method, of class EnvironmentVarModel.
     */
    @Test
    public void testGetCategoryNameSet()
    {
        System.out.println("getCategoryNameSet");
        EnvironmentVarModel instance = new EnvironmentVarModel();
        try
        {
            instance.initialiseVarsFromFile(fileName1);
        }
        catch (Exception ex)
        {
            fail("Unexpected exception." + ex.toString());
        }

        Set<String> result = instance.getCategoryNameSet();
        if (result.size() > 4) // includes the "ALL_CATEGORIES"-dummy
        {
            fail("Too many categories in result set (expected 4): " +
                 result.size());
        }
        for (String cat : new String[]
        {
            "Cat1", "Cat2", "Cat3"
        })
        {
            if (!result.contains(cat))
            {
                fail("Category name set does not contain category '" + cat + "'");
            }
        }
    }

    /**
     * Test of createShellVariableString method, of class EnvironmentVarModel.
     */
    @Test
    public void testCreateShellVariableString()
    {
        System.out.println("createShellVariableString");
        EnvironmentVarModel instance = new EnvironmentVarModel();
        try
        {
            instance.initialiseVarsFromFile(fileName1);
        }
        catch (Exception ex)
        {
            fail("Unexpected exception." + ex.toString());
        }
        String expResult = "\n" +
                           "# Cat1, type:General String\n" +
                           "ENV_VAR1=\"strVal1\"\n" +
                           "\n" +
                           "# Cat1, type:Boolean Flag\n" +
                           "ENV_VAR2=\"1\"\n" +
                           "\n" +
                           "# Cat2, type:Floating Point Value\n" +
                           "ENV_VAR3=\"3.1415\"\n" +
                           "\n" +
                           "# Cat3, type:General String\n" +
                           "ENV_VAR4=\"strVal2\"\n" +
                           "\n" +
                           "# Cat3, type:General Integer\n" +
                           "ENV_VAR5=\"666\"\n" +
                           "";
        boolean addExport = false;
        String result = instance.createShellVariableString(addExport);
        assertEquals(expResult, result);

        result = instance.createShellVariableString();
        assertEquals(expResult, result);

        expResult = "\n" +
                    "# Cat1, type:General String\n" +
                    "export ENV_VAR1=\"strVal1\"\n" +
                    "\n" +
                    "# Cat1, type:Boolean Flag\n" +
                    "export ENV_VAR2=\"1\"\n" +
                    "\n" +
                    "# Cat2, type:Floating Point Value\n" +
                    "export ENV_VAR3=\"3.1415\"\n" +
                    "\n" +
                    "# Cat3, type:General String\n" +
                    "export ENV_VAR4=\"strVal2\"\n" +
                    "\n" +
                    "# Cat3, type:General Integer\n" +
                    "export ENV_VAR5=\"666\"\n" +
                    "";
        addExport = true;
        result = instance.createShellVariableString(addExport);
        assertEquals(expResult, result);
    }

    /**
     * Test of removeCategory method, of class EnvironmentVarModel.
     */
    @Test
    public void testRemove()
    {
        System.out.println("remove");
        EnvironmentVarModel instance = new EnvironmentVarModel();
        try
        {
            instance.initialiseVarsFromFile(fileName1);
        }
        catch (Exception ex)
        {
            fail("Unexpected exception." + ex.toString());
        }
        instance.removeCategory("Cat2");
        String expectedResult = "GeneralString,Cat1,ENV_VAR1,true,strVal1\n" +
                                "BooleanFlag,Cat1,ENV_VAR2,true,1\n" +
                                "GeneralString,Cat3,ENV_VAR4,true,strVal2\n" +
                                "IntegralValue,Cat3,ENV_VAR5,true,666\n" +
                                "";
        assertEquals(expectedResult, instance.toString());
        instance.removeCategory("Cat1");
        expectedResult = "GeneralString,Cat3,ENV_VAR4,true,strVal2\n" +
                         "IntegralValue,Cat3,ENV_VAR5,true,666\n" +
                         "";
        assertEquals(expectedResult, instance.toString());
        instance.removeCategory("Cat3");
        expectedResult = "";
        assertEquals(expectedResult, instance.toString());
    }

    /**
     * Test of removeRowFromCategory method, of class EnvironmentVarModel.
     */
    @Test
    public void testRemoveRowFromCategory()
    {
        System.out.println("removeRowFromCategory");
        EnvironmentVarModel instance = new EnvironmentVarModel();
        instance.removeRowFromCategory("Cat1", 5);
        try
        {
            instance.initialiseVarsFromFile(fileName1);
        }
        catch (Exception ex)
        {
            fail("Unexpected exception." + ex.toString());
        }
        instance.removeRowFromCategory("Cat2", 1);
        String expectedResult =
               "GeneralString,Cat1,ENV_VAR1,true,strVal1" +
               StringUtils.NEWLINE +
               "BooleanFlag,Cat1,ENV_VAR2,true,1" +
               StringUtils.NEWLINE +
               "FloatingPointValue,Cat2,ENV_VAR3,true,3.1415" +
               StringUtils.NEWLINE +
               "GeneralString,Cat3,ENV_VAR4,true,strVal2" +
               StringUtils.NEWLINE +
               "IntegralValue,Cat3,ENV_VAR5,true,666" +
               StringUtils.NEWLINE;
        assertEquals(expectedResult, instance.toString());

        instance.removeRowFromCategory("Cat2", 0);
        expectedResult =
        "GeneralString,Cat1,ENV_VAR1,true,strVal1" +
        StringUtils.NEWLINE +
        "BooleanFlag,Cat1,ENV_VAR2,true,1" +
        StringUtils.NEWLINE +
        "GeneralString,Cat3,ENV_VAR4,true,strVal2" +
        StringUtils.NEWLINE +
        "IntegralValue,Cat3,ENV_VAR5,true,666" +
        StringUtils.NEWLINE;
        assertEquals(expectedResult, instance.toString());

        instance.removeRowFromCategory("Cat3", 1);
        expectedResult =
        "GeneralString,Cat1,ENV_VAR1,true,strVal1" +
        StringUtils.NEWLINE +
        "BooleanFlag,Cat1,ENV_VAR2,true,1" +
        StringUtils.NEWLINE +
        "GeneralString,Cat3,ENV_VAR4,true,strVal2" +
        StringUtils.NEWLINE;
        assertEquals(expectedResult, instance.toString());

        instance.removeRowFromCategory("Cat3", 0);
        expectedResult =
        "GeneralString,Cat1,ENV_VAR1,true,strVal1" +
        StringUtils.NEWLINE +
        "BooleanFlag,Cat1,ENV_VAR2,true,1" +
        StringUtils.NEWLINE;
        assertEquals(expectedResult, instance.toString());

        instance.removeRowFromCategory("Cat1", 0);
        expectedResult =
        "BooleanFlag,Cat1,ENV_VAR2,true,1" +
        StringUtils.NEWLINE;
        assertEquals(expectedResult, instance.toString());

        instance.removeRowFromCategory("Cat1", 0);
        expectedResult = "";
        assertEquals(expectedResult, instance.toString());
    }

    /**
     * Test of addNewVariable method, of class EnvironmentVarModel.
     */
    @Test
    public void testAddNewVariable()
    {
        System.out.println("addNewVariable");
        EnvironmentVarModel instance = new EnvironmentVarModel();
        try
        {
            instance.initialiseVarsFromFile(fileName1);
        }
        catch (Exception ex)
        {
            fail("Unexpected exception." + ex.toString());
        }

        instance.addNewVariable("Cat2");
        String expectedResult =
               "GeneralString,Cat1,ENV_VAR1,true,strVal1" +
               StringUtils.NEWLINE +
               "BooleanFlag,Cat1,ENV_VAR2,true,1" +
               StringUtils.NEWLINE +
               "FloatingPointValue,Cat2,ENV_VAR3,true,3.1415" +
               StringUtils.NEWLINE +
               "GeneralString,Cat3,ENV_VAR4,true,strVal2" +
               StringUtils.NEWLINE +
               "IntegralValue,Cat3,ENV_VAR5,true,666" +
               StringUtils.NEWLINE +
               "GeneralString,Cat2,NEW_VAR_0,true" +
               StringUtils.NEWLINE;
        assertEquals(expectedResult, instance.toString());

        instance.addNewVariable("Cat1");
        expectedResult =
        "GeneralString,Cat1,ENV_VAR1,true,strVal1" +
        StringUtils.NEWLINE +
        "BooleanFlag,Cat1,ENV_VAR2,true,1" +
        StringUtils.NEWLINE +
        "FloatingPointValue,Cat2,ENV_VAR3,true,3.1415" +
        StringUtils.NEWLINE +
        "GeneralString,Cat3,ENV_VAR4,true,strVal2" +
        StringUtils.NEWLINE +
        "IntegralValue,Cat3,ENV_VAR5,true,666" +
        StringUtils.NEWLINE +
        "GeneralString,Cat2,NEW_VAR_0,true" +
        StringUtils.NEWLINE +
        "GeneralString,Cat1,NEW_VAR_1,true" +
        StringUtils.NEWLINE;
        assertEquals(expectedResult, instance.toString());
    }

    /**
     * Test of getValueAt method, of class EnvironmentVarModel.
     */
    @Test
    public void testGetAndSetValueAt()
    {
        System.out.println("get[set]ValueAt");
        EnvironmentVarModel instance = new EnvironmentVarModel();
        try
        {
            instance.initialiseVarsFromFile(fileName1);
        }
        catch (Exception ex)
        {
            fail("Unexpected exception." + ex.toString());
        }

        // Column-order in table:
        // Cat1,true,ENV_VAR1,GeneralString,strVal1
        Object result = instance.getValueAt(0, 0);
        assertEquals("Cat1", result);
        instance.setModelCategory("Cat1");
        result = instance.getValueAt(0, 0);
        assertEquals(true, result);
        instance.setModelCategory("Cat2");
        result = instance.getValueAt(0, 1);
        assertEquals("ENV_VAR3", result);
        instance.setModelCategory("Cat3");
        instance.setValueAt("Changed", 0, 3);
        result = instance.getValueAt(0, 3);
        assertEquals("Changed", result);
    }

}
