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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openide.util.Exceptions;

/**
 * Test the public interface of the FileUtilities class.
 *
 * @author kybelksd
 */
public class FileUtilitiesTest
{

    private static final Class CLAZZ = FileUtilitiesTest.class;
    private static final String CLASS_NAME = CLAZZ.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    final static String testDirectory = FileUtilities.FILE_SEPARATOR +
                                        "tmp" +
                                        FileUtilities.FILE_SEPARATOR +
                                        "fileUtilTest";

    /**
     * Set up class statics.
     */
    @BeforeClass
    public static void setUpClass()
    {
    }

    /**
     * Tidy up class statics.
     */
    @AfterClass
    public static void tearDownClass()
    {
    }

    /**
     * Default construct.
     */
    public FileUtilitiesTest()
    {
    }

    /**
     * Set-up before any test.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception
    {
        if (new File(testDirectory).exists())
        {
            throw new Exception("Directory '" +
                                testDirectory +
                                "' exists. Please remove.");
        }
    }

    /**
     * Clean-up after any test.
     */
    @After
    public void tearDown()
    {
        FileUtilities.deleteRecursive(testDirectory);
    }

    /**
     * Test of createFolders method, of class FileUtilities.
     */
    @Test
    public void testCreateDeleteFolders()
    {
        LOGGER.log(Level.INFO, "create and delete folders");
        String[] dirs = new String[]
         {
             "1",
             "2",
             "3",
             "A" + FileUtilities.FILE_SEPARATOR + "a",
             "A" + FileUtilities.FILE_SEPARATOR + "b",
             "B" +
             FileUtilities.FILE_SEPARATOR +
             "x" +
             FileUtilities.FILE_SEPARATOR +
             "y"
        };
        String[] resultDirs = new String[]
         {
             testDirectory,
             testDirectory + FileUtilities.FILE_SEPARATOR + "2",
             testDirectory + FileUtilities.FILE_SEPARATOR + "3",
             testDirectory + FileUtilities.FILE_SEPARATOR + "B",
             testDirectory + FileUtilities.FILE_SEPARATOR + "B" +
             FileUtilities.FILE_SEPARATOR + "x",
             testDirectory + FileUtilities.FILE_SEPARATOR + "B" +
             FileUtilities.FILE_SEPARATOR + "x" +
             FileUtilities.FILE_SEPARATOR + "y",
             testDirectory + FileUtilities.FILE_SEPARATOR + "1",
             testDirectory + FileUtilities.FILE_SEPARATOR + "A",
             testDirectory + FileUtilities.FILE_SEPARATOR + "A" +
             FileUtilities.FILE_SEPARATOR + "b",
             testDirectory + FileUtilities.FILE_SEPARATOR + "A" +
             FileUtilities.FILE_SEPARATOR + "a"
        };

        boolean expResult = true;
        for (String resultDir : resultDirs)
        {
            if (new File(resultDir).exists())
            {
                fail("Directory " +
                     resultDir +
                     " is already present. Please remove before starting test.");
            }
        }
        boolean result = FileUtilities.createFolders(testDirectory,
                                                     Arrays.asList(dirs));
        assertEquals("createFolders", expResult, result);
        for (String resultDir : resultDirs)
        {
            if (!new File(resultDir).exists())
            {
                fail("Directory " + resultDir + " has not been created.");
            }
        }
        FileUtilities.deleteRecursive(testDirectory);
        for (String resultDir : resultDirs)
        {
            if (new File(resultDir).exists())
            {
                fail("Directory " + resultDir + " could not be removed.");
            }
        }
    }

    /**
     * Test of createFolders method, of class FileUtilities.
     */
    @Test
    public void testListFoldersRecursivlely()
    {
        LOGGER.log(Level.INFO, "list files in folders recursively");
        String[] dirs = new String[]
         {
             "1", "2"
        };

        String[] resultDirs = new String[]
         {
             testDirectory + FileUtilities.FILE_SEPARATOR + "1",
             testDirectory + FileUtilities.FILE_SEPARATOR + "2",
        };

        String[] fileNames = new String[]
         {
             "fileName",
             "diffName",
             "fileName.txt",
             "diffName.txt",
             "fileName.csv",
             "diffName.csv",
        };

        boolean expResult = true;
        for (String resultDir : resultDirs)
        {
            if (new File(resultDir).exists())
            {
                fail("Directory " +
                     resultDir +
                     " is already present. Please remove before starting test.");
            }
        }
        boolean result = FileUtilities.createFolders(testDirectory,
                                                     Arrays.asList(dirs));
        assertEquals("createFolders", expResult, result);
        for (String resultDir : resultDirs)
        {
            if (!new File(resultDir).exists())
            {
                fail("Directory " + resultDir + " has not been created.");
            }
            else
            {
                for (String fileName : fileNames)
                {
                    try
                    {
                        FileUtilities.saveText(resultDir +
                                               FileUtilities.FILE_SEPARATOR +
                                               fileName,
                                               resultDir +
                                               FileUtilities.FILE_SEPARATOR +
                                               fileName);
                    }
                    catch (IOException ex)
                    {
                        LOGGER.log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        ////////////////////////////////////////////////////////////////////////
        List resultFiles = FileUtilities.listRecursive(testDirectory);
        LOGGER.log(Level.INFO, "All files:{0}", ToString.make(
                   resultFiles.toArray()));
        assertEquals("count of all files",
                     dirs.length * (1 + fileNames.length) + 1,
                     resultFiles.size());

        resultFiles = FileUtilities.findFilesNamed(testDirectory, "fileName");
        LOGGER.log(Level.INFO,
                   "All fileName files:{0}",
                   ToString.make(resultFiles.toArray()));
        assertEquals("count of files named 'fileName'",
                     dirs.length,
                     resultFiles.size());

        resultFiles = FileUtilities.findFilesNamed(testDirectory,
                                                   "fileName",
                                                   "diffName");
        LOGGER.log(Level.INFO,
                   "All fileName/diffName files:{0}",
                   ToString.make(resultFiles.toArray()));
        assertEquals("count of files named 'fileName' or 'diffName'",
                     dirs.length * 2,
                     resultFiles.size());

        resultFiles = FileUtilities.findFilesStartingWith(testDirectory,
                                                          "fileName",
                                                          null);
        LOGGER.log(Level.INFO,
                   "All starting with fileName files:{0}",
                   ToString.make(resultFiles.toArray()));
        assertEquals("count of files starting with fileName",
                     dirs.length * 3,
                     resultFiles.size());

        resultFiles = FileUtilities.findFilesStartingWith(testDirectory,
                                                          null,
                                                          ".txt");
        LOGGER.log(Level.INFO,
                   "All ending with .txt files:{0}",
                   ToString.make(resultFiles.toArray()));
        assertEquals("count of files ending with .txt",
                     dirs.length * 2,
                     resultFiles.size());

        resultFiles = FileUtilities.findFilesStartingWith(testDirectory,
                                                          "file",
                                                          ".txt");
        LOGGER.log(Level.INFO,
                   "All starting with ''file'' and ending with .txt files:{0}",
                   ToString.make(resultFiles.toArray()));
        assertEquals("count of files starting with 'file' and ending with .txt",
                     dirs.length * 1,
                     resultFiles.size());

        ////////////////////////////////////////////////////////////////////////
        FileUtilities.deleteRecursive(testDirectory);
        for (String resultDir : resultDirs)
        {
            if (new File(resultDir).exists())
            {
                fail("Directory " + resultDir + " could not be removed.");
            }
        }
    }

    /**
     * Test of saveText/Read text-files.
     */
    @Test
    public void testSaveAndReadText()
    {
        LOGGER.log(Level.INFO, "save and read text files");
        String fileName = testDirectory +
                          FileUtilities.FILE_SEPARATOR + "testfile.txt";

        try
        {
            FileUtilities.readText(fileName);
            fail("Should have thrown IOException ");
        }
        catch (IOException ex)
        {
            if (ex instanceof FileNotFoundException)
            {
                // expected
            }
            else
            {
                fail("unexpected exception " + ex);
            }
        }

        String fileContents = "Line1\nLine2\nLine3";
        try
        {
            FileUtilities.saveText(fileName, fileContents);
            fail("Should have thrown IOException ");
        }
        catch (IOException ex)
        {
            // expected
        }

        try
        {
            FileUtilities.saveText(fileName, fileContents, true);
        }
        catch (IOException ex)
        {
            fail("unexpected exception " + ex);
        }

        try
        {
            String readContents = FileUtilities.readText(fileName);
            assertEquals("check read string equals written one",
                         fileContents,
                         readContents);
        }
        catch (IOException ex)
        {
            fail("unexpected exception " + ex);
        }

    }

    /**
     * Test of appendText method, of class FileUtilities.
     */
    @Test
    public void testAppendLine()
    {
        LOGGER.log(Level.INFO, "Append line");
        String filename = "appended.txt";
        Object obj = null;
        try
        {
            String fileContents =
                   "Line 1" + StringUtils.NEWLINE +
                   "Line 2" + StringUtils.NEWLINE +
                   "Line 3" + StringUtils.NEWLINE +
                   "Line 4" + StringUtils.NEWLINE +
                   "Line 5" + StringUtils.NEWLINE;

            String[] lines = fileContents.split(StringUtils.NEWLINE);
            FileUtilities.deleteRecursive(filename);
            for (String line : lines)
            {
                FileUtilities.appendText(filename, line + StringUtils.NEWLINE);
            }
            String readContents = FileUtilities.readText(filename);
            assertEquals("check read string equals written one",
                         fileContents,
                         readContents);

        }
        catch (IOException ex)
        {
            Exceptions.printStackTrace(ex);
            fail("Unexpected exception: " + ex.toString());
        }
    }

    /**
     * Test of serialize method, of class FileUtilities.
     */
//    @Test
    public void testSerialize()
    {
        LOGGER.log(Level.INFO, "Serialize");
        String filename = "object.ser";
        Object obj = null;
        try
        {
            FileUtilities.serialize(filename, obj);
        }
        catch (IOException ex)
        {
            Exceptions.printStackTrace(ex);
        }
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of deserialize method, of class FileUtilities.
     */
//    @Test
    public void testDeserialize()
    {
        LOGGER.log(Level.INFO, "Deserialize");
        String filename = "";
        Object expResult = null;
        Object result = null;
        try
        {
            result = FileUtilities.deserialize(filename);
        }
        catch (IOException | ClassNotFoundException ex)
        {
            Exceptions.printStackTrace(ex);
        }
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of xmlSerialize method, of class FileUtilities.
     */
//    @Test
    public void testXmlSerialize()
    {
        LOGGER.log(Level.INFO, "XmlSerialize");
        String filename = "";
        Object obj = null;
        try
        {
            FileUtilities.xmlSerialize(filename, obj);
        }
        catch (JAXBException | FileNotFoundException ex)
        {
            Exceptions.printStackTrace(ex);
        }
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
