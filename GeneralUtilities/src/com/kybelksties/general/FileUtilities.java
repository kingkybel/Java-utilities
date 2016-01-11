
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 * A collection of file manipulation utilities.
 *
 * @author kybelksd
 */
public class FileUtilities
{

    private static final String CLASS_NAME = FileUtilities.class.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    /**
     * Check whether a file exists.
     *
     * @param path the whole file path
     * @return true, if the file exists, false otherwise
     */
    public static boolean fileExists(File path)
    {
        return path.exists() && !path.isDirectory();
    }

    /**
     * Check whether a file exists.
     *
     * @param pathString the whole file path as string
     * @return true, if the file exists, false otherwise
     */
    public static boolean fileExists(String pathString)
    {
        File path = new File(pathString);
        return path.exists() && !path.isDirectory();
    }

    /**
     * Check whether a directory exists.
     *
     * @param path the whole directory path
     * @return true, if the directory exists, false otherwise
     */
    public static boolean directoryExists(File path)
    {
        return path.exists() && path.isDirectory();
    }

    /**
     * Check whether a directory exists.
     *
     * @param pathString the whole directory path as string
     * @return true, if the directory exists, false otherwise
     */
    public static boolean directoryExists(String pathString)
    {
        File path = new File(pathString);
        return path.exists() && path.isDirectory();
    }

    /**
     * Check whether a path (file or directory) exists.
     *
     * @param path the whole path
     * @return true, if the path exists, false otherwise
     */
    public static boolean exists(File path)
    {
        return path.exists();
    }

    /**
     * Check whether a path (file or directory) exists.
     *
     * @param pathString the whole path as string
     * @return true, if the path exists, false otherwise
     */
    public static boolean exists(String pathString)
    {
        File path = new File(pathString);
        return path.exists();
    }

    /**
     * Remove a file/directory. In case of directory recursively remove all
     * contained files and directories.
     *
     * @param path root path of the directory tree to delete
     * @return true on success, false otherwise
     */
    public static boolean deleteRecursive(File path)
    {
        boolean deleteSuccessful = false;
        if (!path.exists())
        {
            return deleteSuccessful;
        }
        boolean ret = true;
        if (path.isDirectory())
        {
            for (File f : path.listFiles())
            {
                ret = ret && deleteRecursive(f);
            }
            deleteSuccessful = path.delete();
        }
        else
        {
            deleteSuccessful = path.delete();
        }
        return ret && deleteSuccessful;
    }

    /**
     * Recursively delete all files/directories in the path-string.
     *
     * @param pathString root path of the directory tree to delete as string
     * @return true on success, false otherwise
     */
    public static boolean deleteRecursive(String pathString)
    {
        return deleteRecursive(new File(pathString));
    }

    /**
     * Recursively list all files/directories in the path.
     *
     * @param path root path of the directory tree to list
     * @return List of all files in the directory listed recursively
     */
    public static List<File> listRecursive(File path)
    {
        List<File> reval = new ArrayList<>();
        if (!path.exists())
        {
            return null;
        }
        if (path.isDirectory())
        {
            for (File f : path.listFiles())
            {
                List<File> sub = listRecursive(f);
                if (sub != null)
                {
                    reval.addAll(sub);
                }
            }
        }
        reval.add(path);

        return reval;
    }

    /**
     * List delete all files/directories in the path-string.
     *
     * @param pathString root path as string
     * @return a list of all the files
     */
    public static List<File> listRecursive(String pathString)
    {
        return listRecursive(new File(pathString));
    }

    /**
     * Listing files with a common start string and/or a common end string.
     *
     * @param rootPath    configured root path for the TE logs
     * @param startString any sequence of valid filename characters excluding
     *                    separators
     * @param extension   any sequence of valid filename characters excluding
     *                    separators; the '.' for the extension needs to be
     *                    explicitly included in the string.
     * @return map of node-name to list of corresponding log-files in
     *         subdirectories
     */
    public static List<File> findFilesStartingWith(String rootPath,
                                                   final String startString,
                                                   final String extension)
    {
        File root = new File(rootPath);
        List<File> allFiles = listRecursive(root);
        ArrayList<File> reval = new ArrayList<>();
        if (allFiles != null)
        {
            for (File f : allFiles)
            {
                if ((startString == null ||
                     f.getName().startsWith(startString)) &&
                    (extension == null ||
                     f.getName().endsWith(extension)))
                {
                    reval.add(f);
                }
            }
        }

        return reval;
    }

    /**
     * Scan a directory recursively for filenames matching a list.
     *
     * @param rootPath configured root path for the TE logs
     * @param names
     * @return list of files that match one of the names
     */
    public static List<File> findFilesNamed(String rootPath,
                                            final String... names)
    {
        File root = new File(rootPath);

        Set<String> nameSet = new HashSet<>();
        nameSet.addAll(Arrays.asList(names));

        List<File> allFiles = listRecursive(root);
        ArrayList<File> reval = new ArrayList<>();
        if (allFiles != null)
        {
            for (File f : allFiles)
            {
                String fileName = f.getName();
                if (names == null || nameSet.contains(fileName))
                {
                    reval.add(f);
                }
            }
        }
        return reval;
    }

    /**
     * Create all folders in a path.
     *
     * @param pathElements
     * @return true, if successful, false otherwise
     */
    public static boolean createFolders(String... pathElements)
    {
        String completePath = "";
        for (String el : pathElements)
        {
            completePath += el + "/";
        }
        boolean result = (new File(completePath)).mkdirs();
        return result;
    }

    /**
     * Create a flock of folders rootPath/discrim[i].
     *
     * @param rootPath the path where the folder will be created
     * @param discrim  set of discriminators. May contain path separator '/' to
     *                 create subdirectories
     * @return true, if successful, false otherwise
     */
    public static boolean createFolders(String rootPath, Iterable discrim)
    {
        Iterator it = discrim.iterator();
        boolean result = true;
        while (it.hasNext())
        {
            result &= createFolders(rootPath,
                                    it.next().toString());
        }

        return result;
    }

    /**
     * Save the text in the String to a file.
     *
     * @param fileName
     * @param textToSave
     * @throws java.io.IOException
     */
    public static void saveText(String fileName, String textToSave) throws
            IOException
    {
        FileUtilities.saveText(fileName, textToSave, false);
    }

    /**
     * Save the text to a file, forcing the creation of the file if is does not
     * exist.
     *
     * @param fileName   Path of the (new) file as string
     * @param textToSave the new contents of the file
     * @param force      force the creation of path/file if set to true
     * @throws java.io.IOException
     */
    public static void saveText(String fileName,
                                String textToSave,
                                boolean force)
            throws IOException
    {
        BufferedWriter out = null;
        File file = new File(fileName);
        File path = new File(file.getParent());
        if (force && !path.exists())
        {
            (new File(path.getAbsolutePath())).mkdirs();
        }
        try
        {
            if (file.exists() && file.canWrite())
            {
                file.delete();
            }
            out = new BufferedWriter(new FileWriter(file));
            out.write(textToSave);
            out.close();
        }
        catch (IOException ex)
        {
            if (!path.exists())
            {
                String msg = "Directory " + path.getParent() +
                             " does not exist so cannot create file." +
                             ex.getMessage();
                throw new IOException(msg);
            }
            throw ex;
        }
        finally
        {
            try
            {
                if (out != null)
                {
                    out.close();
                }
            }
            catch (IOException ex)
            {
                String msg = "Cannot close outstream " +
                             fileName + ". " + ex.getMessage();
                throw new IOException(msg);
            }
        }
    }

    /**
     * Read the contents of a file.
     *
     * @param fileName path of the file to read
     * @return the contents as string.
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static String readText(String fileName) throws IOException
    {
        String reval;
        try (BufferedReader bufferedReader =
                            new BufferedReader(new FileReader(fileName)))
        {
            StringBuilder sb = new StringBuilder();
            int readChar;
            do
            {
                readChar = bufferedReader.read();
                if (readChar != -1)
                {
                    sb.append(Character.toChars(readChar));
                }
            }
            while (readChar != -1);
            reval = sb.toString();
        }

        return reval;
    }

    /**
     * Serialize an object to file.
     *
     * @param <T>      a serializable type
     * @param filename the file to which the serialization is written
     * @param obj      the serializable object
     */
    public static <T> void serialize(String filename, T obj)
    {
        try (FileOutputStream fileOut = new FileOutputStream(filename))
        {
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(obj);
            out.close();
        }
        catch (IOException ex)
        {
            LOGGER.log(Level.SEVERE, "Serialize-Error", ex);
        }

    }

    /**
     * Deserialize an object from a file.
     *
     * @param <T>      a serializable type
     * @param filename the file from which the serialization is read
     * @return the object
     */
    public static <T> T deserialize(String filename)
    {
        T reval = null;
        try (FileInputStream fileIn = new FileInputStream(filename))
        {
            ObjectInputStream in = new ObjectInputStream(fileIn);
            reval = (T) in.readObject();
            in.close();
        }
        catch (IOException | ClassNotFoundException ex)
        {
            LOGGER.log(Level.SEVERE, "Deserialize-Error", ex);
        }

        return reval;
    }

    /**
     * Serialize an object to file as XML.
     *
     * @param <T>      a XML serializable type
     * @param filename the file to which the serialization is written
     * @param obj      the serializable object
     */
    public static <T> void xmlSerialize(String filename, T obj)
    {
        try (FileOutputStream fileOut = new FileOutputStream(filename))
        {
            JAXBContext context = JAXBContext.newInstance(obj.getClass());

            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            m.marshal(obj, fileOut);
        }
        catch (IOException | JAXBException ex)
        {
            LOGGER.log(Level.SEVERE, "XmlSerialize-Error", ex);
        }

    }

    /**
     * Deserialize an object from file containing XML.
     *
     * @param <T>      a serializable type
     * @param filename the file from which the serialization is read
     * @param reval
     * @return the object
     */
    public static <T> T xmlDeserialize(String filename, T reval)
    {
        try
        {
            JAXBContext jc = JAXBContext.newInstance(reval.getClass());
            Unmarshaller u = jc.createUnmarshaller();

            File f = new File(filename);
            reval = (T) u.unmarshal(f);
        }
        catch (JAXBException ex)
        {
            LOGGER.log(Level.SEVERE, "XmlDeserialize-Error", ex);
        }

        return reval;
    }

    /**
     * Append a line to file.
     *
     * @param fileName path to file as string
     * @param line     the line to append
     * @throws IOException
     */
    static public void appendLine(String fileName, String line)
            throws IOException
    {
        BufferedWriter out = null;
        File file = new File(fileName);
        File path = new File(file.getParent());
        if (!path.exists())
        {
            (new File(path.getAbsolutePath())).mkdirs();
        }
        try
        {
            out = new BufferedWriter(new FileWriter(file));
            out.append(line);
            out.close();
        }
        catch (IOException ex)
        {
            if (!path.exists())
            {
                String msg = "Directory " + path.getParent() +
                             " does not exist so cannot create file." +
                             ex.getMessage();
                throw new IOException(msg);
            }
            throw ex;
        }
        finally
        {
            try
            {
                if (out != null)
                {
                    out.close();
                }
            }
            catch (IOException ex)
            {
                String msg = "Cannot close outstream " +
                             fileName + ". " + ex.getMessage();
                throw new IOException(msg);
            }
        }
    }
}