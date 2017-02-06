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
 * @date Feb 01, 2017
 * @author Dieter J Kybelksties
 */
package com.kybelksties.gui;

import com.kybelksties.general.FileUtilities;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Dieter J Kybelksties
 */
public class MakeIcon
{

    private static final Class CLAZZ = MakeIcon.class;
    private static final String CLASS_NAME = CLAZZ.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    private static final String USAGE =
                                "Usage:\n" +
                                "java -jar \"/home/kybelksd/NetBeansProjects/MakeIcon/dist/MakeIcon.jar\" " +
                                "\n\t\t-f <format> -p <pictureFile> [-o <out-file>]" +
                                "\n\t\t[-a <alpha-file>] [-c <cap-number>] [-s <smugde-width>]" +
                                "\n\t\t[-j <jig-number>] [-?|-h|-help|--help]";
    private static final Random rand = new Random();
    private static int cap;
    private static int rem;

    private static int[] smudge(String src, int width, int jig, String pass)
    {
        int[] reval = new int[src.length() * width];
        int index = 0;
        int[] temp = new int[width];
        for (char c : src.toCharArray())
        {
            int avg = c / width;
            for (int t = 0; t < width; t++)
            {
                temp[t] = avg;
            }
            if (width * avg < c)
            {
                int corrector = c - width * avg;
                int corrInd = rand.nextInt(width);
                temp[corrInd] += corrector;
            }
            for (int t = 0; t < width; t++)
            {
                int jiggle = rand.nextInt(jig) - jig / 2;
                temp[t] += jiggle;
                temp[(t + 1) % width] -= jiggle;
                while (temp[(t + 1) % width] < 0 || temp[t] > cap)
                {
                    temp[t]--;
                    temp[(t + 1) % width]++;
                }
                while (temp[(t + 1) % width] > cap || temp[t] < 0)
                {
                    temp[t]++;
                    temp[(t + 1) % width]--;
                }
            }
            for (int t = 0; t < width; t++)
            {
                temp[t] = cap - temp[t];
            }
            for (int t = 0; t < width; t++)
            {
                reval[index++] = temp[t];
            }

        }
        if (!pass.isEmpty())
        {
            for (int i = 0; i < reval.length; i++)
            {
                reval[i] += (pass.charAt(i % pass.length()) % rem);
            }
        }
        return reval;
    }

    private static String unsmudge(int[] smudged, int width, String pass)
    {
        String reval = "";
        int asc = 0;
        for (int i = 0; i < smudged.length; i++)
        {

            asc += cap -
                   smudged[i] +
                   ((pass.isEmpty()) ? 0 : pass.charAt(i % pass.length()) % rem);
            if ((i % width) == (width - 1))
            {
                reval += (char) asc;
                asc = 0;
            }
        }

        return reval;
    }

    static BufferedImage shrink(BufferedImage srcImg, int numPixels)
    {
        if (srcImg == null)
        {
            return null;
        }
        int W = srcImg.getWidth();
        int H = srcImg.getHeight();
        double N = srcImg.getHeight() * srcImg.getWidth();
        double n = numPixels * 1.1; // extra 10%
        double h = H * Math.sqrt(n / N);
        double w = n / h;
        BufferedImage reval = new BufferedImage((int) w,
                                                (int) h,
                                                BufferedImage.TYPE_4BYTE_ABGR);
        double stepW = (double) srcImg.getWidth() / (double) reval.getWidth();
        double stepH = (double) srcImg.getHeight() / (double) reval.getHeight();
        double iH = 0.0;
        for (int ih = 0; ih < reval.getHeight(); ih++, iH += stepH)
        {
            double iW = 0.0;
            for (int iw = 0; iw < reval.getWidth(); iw++, iW += stepW)
            {
                reval.setRGB(iw, ih, srcImg.getRGB((int) iW, (int) iH));
            }
        }

        return reval;
    }

    private static void saveIcon(String fmt,
                                 String sourceFilename,
                                 String waterFilename,
                                 String dest,
                                 int width,
                                 int jig,
                                 String pass)
    {
        try
        {
            String destFilename = dest;
            if (dest.charAt(dest.length() - 1) == '/')
            {
                destFilename += sourceFilename.substring(
                sourceFilename.lastIndexOf('/') + 1);
            }
            if (destFilename.contains("."))
            {
                int dotPos = destFilename.lastIndexOf('.');
                destFilename = destFilename.substring(0, dotPos);
            }
            destFilename += fmt.equals("argb") ? ".png" : "." + fmt;

            FileUtilities.saveText(destFilename, "", true);
            File fileDest = new File(destFilename);
            String strWater = "";
            int[] waterArray;
            if (!waterFilename.isEmpty())
            {
                File waterDest = new File(waterFilename);
                String name = waterDest.getPath();
                strWater = FileUtilities.readText(waterFilename);
                int waterLength = strWater.length();
                strWater = name + "\n" + waterLength + "\n" + strWater;
                waterArray = smudge(strWater, width, jig, pass);
            }
            else
            {
                waterArray = new int[65536];
                for (int i = 0; i < waterArray.length; i++)
                {
                    waterArray[i] = cap + rand.nextInt(jig) - jig / 2;
                }
            }
            int waterIndex = 0;

            BufferedImage img = shrink(ImageIO.read(new File(sourceFilename)),
                                       waterArray.length);
            int imageType = fmt.equals("argb") ? BufferedImage.TYPE_4BYTE_ABGR :
                            fmt.equals("png") ? BufferedImage.TYPE_4BYTE_ABGR :
                            fmt.equals("wbmp") ? BufferedImage.TYPE_BYTE_BINARY :
                            BufferedImage.TYPE_3BYTE_BGR;
            BufferedImage newImage = new BufferedImage(img.getWidth(),
                                                       img.getHeight(),
                                                       imageType);
            for (int x = 0; x < img.getWidth(); x++)
            {
                for (int y = 0; y < img.getHeight(); y++)
                {
                    Color rgb = new Color(img.getRGB(x, y));
                    int water = waterArray.length > 0 ?
                                waterArray[waterIndex % waterArray.length] :
                                255;
                    if (waterIndex >= strWater.length() * width)
                    {
                        water += rand.nextInt(jig) - jig / 2;
                        while (water > 255)
                        {
                            water -= rand.nextInt(jig / 2);
                        }
                    }
                    waterIndex++;
                    Color c = new Color(rgb.getRed(),
                                        rgb.getGreen(),
                                        rgb.getBlue(),
                                        water);
                    newImage.setRGB(x, y, c.getRGB());
                }
            }
            if (!ImageIO.write(newImage,
                               fmt.equals("argb") ? "PNG" : fmt.toUpperCase(),
                               fileDest))
            {
                throw new Exception("Could not write image to '" +
                                    fileDest +
                                    "' in Format '" +
                                    fmt.toUpperCase() +
                                    "'. Probably no appropriate file-writer?");
            }

        }
        catch (Exception e)
        {
            LOGGER.log(Level.WARNING, e.toString());
        }
    }

    private static void extractAlpha(String picFile,
                                     int width,
                                     String pass,
                                     String dir)
    {
        BufferedImage img;
        try
        {
            img = ImageIO.read(new File(picFile));
        }
        catch (IOException ex)
        {
            return;
        }

        int[] asc = new int[img.getWidth() * img.getWidth()];
        int waterInd = 0;
        for (int x = 0; x < img.getWidth(); x++)
        {

            for (int y = 0; y < img.getHeight(); y++)
            {
                Color c = new Color(img.getRGB(x, y), true);
                asc[waterInd] = c.getAlpha();
                waterInd++;
            }
        }
        String all = unsmudge(asc, width, pass);

        try
        {
            String p = all.substring(0, all.indexOf("\n"));
            int pLen = p.length();
            String lenStr = all.substring(pLen + 1, all.indexOf("\n", pLen + 1));
            int len = Integer.parseInt(lenStr);
            String txt = all.substring(all.indexOf("\n", pLen + 1) + 1,
                                       len + pLen + lenStr.length() + 1);

            FileUtilities.saveText((dir == null ? "" : dir + "/") + p,
                                   txt,
                                   true);
        }
        catch (Exception e)
        {
            // ignore
        }
    }

    /**
     * Main entry point to MakeIcon.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        String fmt = "";
        String pass = "";
        String picFile = "";
        String topStr = "";
        String outFile = "";
        String alphaFile = "";
        String smudgeWidthStr = "";
        String jigStr = "";
        for (int c = 0; c < args.length; c++)
        {
            switch (args[c])
            {
                case "-f":
                    if (c < args.length - 1)
                    {
                        fmt = args[c + 1].substring(0, 1).toLowerCase();
                        fmt = "j".equals(fmt) ? "jpg" :
                              "b".equals(fmt) ? "bmp" :
                              "g".equals(fmt) ? "gif" :
                              "p".equals(fmt) ? "png" :
                              "w".equals(fmt) ? "wbmp" :
                              "a".equals(fmt) ? "argb" :
                              "";
                        pass = args[c + 1].substring(1);
                        c++;
                    }
                    break;
                case "-p":
                    if (c < args.length - 1)
                    {
                        picFile = args[c + 1];
                        c++;
                    }
                    break;
                case "-o":
                    if (c < args.length - 1)
                    {
                        outFile = args[c + 1];
                        c++;
                    }
                    break;
                case "-a":
                    if (c < args.length - 1)
                    {
                        alphaFile = args[c + 1];
                        c++;
                    }
                    break;
                case "-c":
                    if (c < args.length - 1)
                    {
                        topStr = args[c + 1];
                        c++;
                    }
                    break;
                case "-s":
                    if (c < args.length - 1)
                    {
                        smudgeWidthStr = args[c + 1];
                        c++;
                    }
                    break;
                case "-j":
                    if (c < args.length - 1)
                    {
                        jigStr = args[c + 1];
                        c++;
                    }
                    break;
                case "-?":
                case "-h":
                case "-help":
                case "--help":
                    System.out.println(USAGE);
                    System.exit(0);
                    break;
                default:
                    System.out.println(
                            "unknown parameter '" + args[c] + "'!!\n" +
                            USAGE);
                    System.exit(-1);
                    break;
            }
        }

        setCap(topStr);
        int width = getSmudgeWidth(smudgeWidthStr);
        if (fmt.isEmpty())
        {
            extractAlpha(picFile, width, pass, outFile);
        }
        else
        {
            saveIcon(fmt,
                     picFile,
                     alphaFile,
                     outFile,
                     width,
                     getJig(jigStr),
                     pass);
        }
    }

    private static void setCap(String topStr)
    {
        try
        {
            cap = Integer.parseInt(topStr);
        }
        catch (NumberFormatException e)
        {
            cap = 245;
        }
        if (cap < 0 || cap > 255)
        {
            cap = 245;
        }
        rem = 255 - cap;
    }

    private static int getSmudgeWidth(String smudgeWidthStr)
    {
        int width;
        try
        {
            width = Integer.parseInt(smudgeWidthStr);
        }
        catch (NumberFormatException e)
        {
            width = 7;
        }
        return width;
    }

    private static int getJig(String jigStr)
    {
        int jig;
        try
        {
            jig = Integer.parseInt(jigStr);
        }
        catch (NumberFormatException e)
        {
            jig = 7;
        }
        return jig;
    }

}
