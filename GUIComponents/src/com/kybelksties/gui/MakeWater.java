/*
 * @author  Dieter J Kybelksties
 * @date Jan 24, 2017
 *
 */
package com.kybelksties.gui;

import com.kybelksties.general.FileUtilities;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.openide.util.Exceptions;

/**
 *
 * @author Dieter J Kybelksties
 */
public class MakeWater
{

    private static final Class CLAZZ = MakeWater.class;
    private static final String CLASS_NAME = CLAZZ.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

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
                reval[i] += pass.charAt(i % pass.length()) % rem;
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
            asc += cap - smudged[i];
            if (!pass.isEmpty())
            {
                asc += pass.charAt(i % pass.length()) % rem;
            }
            if (i % width == width - 1)
            {
                reval += (char) asc;
                asc = 0;
            }
        }

        return reval;
    }

    private static void writePng(String sourceFilename,
                                 String waterFilename,
                                 String dest,
                                 int width,
                                 int jig,
                                 String pass)
    {
        try
        {
            BufferedImage img = ImageIO.read(new File(sourceFilename));
            BufferedImage newImage = new BufferedImage(img.getWidth(),
                                                       img.getHeight(),
                                                       BufferedImage.TYPE_4BYTE_ABGR);
            File fileDest = new File(dest);
            String strWater = "";
            if (!waterFilename.isEmpty())
            {
                File waterDest = new File(waterFilename);
                String name = waterDest.getPath();
                strWater = FileUtilities.readText(waterFilename);
                int waterLength = strWater.length();
                strWater = name + "\n" + waterLength + "\n" + strWater;
            }
            int waterIndex = 0;
            int[] waterArray = smudge(strWater, width, jig, pass);
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
            ImageIO.write(newImage, "PNG", fileDest);
        }
        catch (Exception e)
        {
            // ignore
        }
    }

    private static void saveWater(BufferedImage img,
                                  int width,
                                  String pass,
                                  String dir)
    {
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
        String cmd = args.length > 0 ? args[0] : "";
        String pass = args.length > 0 ? cmd.substring(1) : "";
        String picFile = args.length > 1 ? args[1] : "";
        String capStr = args.length > 2 ? args[2] : "";
        String outFile = args.length > 3 ? args[3] : picFile;
        String waterFile = args.length > 4 ? args[4] : "";
        String widthStr = args.length > 5 ? args[5] : "";
        String jigStr = args.length > 6 ? args[6] : "";

        try
        {
            int width;
            int jig;
            try
            {
                cap = Integer.parseInt(capStr);
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
            try
            {
                width = Integer.parseInt(widthStr);
            }
            catch (NumberFormatException e)
            {
                width = 7;
            }
            if (cmd.startsWith("m"))
            {
                try
                {
                    jig = Integer.parseInt(jigStr);
                }
                catch (NumberFormatException e)
                {
                    jig = 7;
                }
                writePng(picFile, waterFile, outFile, width, jig, pass);
            }
            else
            {
                BufferedImage img = ImageIO.read(new File(picFile));
                saveWater(img, width, pass, outFile);
            }
        }
        catch (IOException ex)
        {
            Exceptions.printStackTrace(ex);
        }
    }

}
