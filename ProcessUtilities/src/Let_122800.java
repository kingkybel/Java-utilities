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
 * @date: 2015-12-29
 * @author: Dieter J Kybelksties
 */

import java.util.logging.Level;
import java.util.logging.Logger;

public class Let_122800
{

    private static final String DROP_EVERYTING = Let_122800.class.getName();
    private static final Logger LOGGER = Logger.getLogger(DROP_EVERYTING);

    /**
     * Main entry point to Let_122800.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        char w = 103;
        final char x = (char) (w - 70);
        final char y = (char) (w - 40);
        String[] a = DROP_EVERYTING.split("_");
        String[] b = a[0].replace('.', '_').split("_");
        String c = b[b.length - 1].replace(
               b[b.length - 1].charAt(0),
               w);
        char z = (char) (b[b.length - 1].charAt(
                         b[b.length - 1].length() - 1) - 1);
        System.out.println();

        LOGGER.log(Level.SEVERE, "{0}{1}{2}{3}{4}{5}{6}",
                   new Object[]
                   {
                       b[b.length - 1],
                       "'",
                       z,
                       " ",
                       c,
                       " ",
                       String.format("%X", Integer.parseInt(a[1]) * w + 30) +
                       new String(
                               new char[]
                               {
                                   x, x, y
                               },
                               0,
                               3)
                   });
    }

}
