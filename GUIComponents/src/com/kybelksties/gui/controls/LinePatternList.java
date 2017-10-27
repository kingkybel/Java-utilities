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
package com.kybelksties.gui.controls;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;

/**
 *
 * @author kybelksd
 */
public class LinePatternList extends ArrayList<float[]>
{
    
    private static final Class CLAZZ = LinePatternList.class;
    private static final String CLASS_NAME = CLAZZ.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    static float[][] defaultPatterns = new float[][]
             {
                 new float[]
                 {
                     1.0f, 0.0f
                 },
                 new float[]
                 {
                     20.0f
                 },
                 new float[]
                 {
                     10.0f
                 },
                 new float[]
                 {
                     5.0f
                 },
                 new float[]
                 {
                     2.0f
                 },
                 new float[]
                 {
                     1.0f
                 },
                 new float[]
                 {
                     20.0f, 10.0f
                 },
                 new float[]
                 {
                     10.0f, 5.0f
                 },
                 new float[]
                 {
                     5.0f, 2.5f
                 },
                 new float[]
                 {
                     2.0f, 0.5f
                 },
                 new float[]
                 {
                     20.0f, 5.0f
                 },
                 new float[]
                 {
                     10.0f, 2.5f
                 },
                 new float[]
                 {
                     5.0f, 1.25f
                 },
                 new float[]
                 {
                     20.0f, 2.0f, 1.0f, 2.0f
                 },
                 new float[]
                 {
                     10.0f, 2.0f, 1.0f, 2.0f
                 },
                 new float[]
                 {
                     5.0f, 2.0f, 1.0f, 2.0f
                 },
                 new float[]
                 {
                     2.0f, 1.0f, 1.0f, 1.0f
                 },
                 new float[]
                 {
                     10.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f,
                     1.0f, 1.0f
                 },
    };

    /**
     * Default construct.
     */
    public LinePatternList()
    {
        includeDefaultPatterns();
    }

    /**
     * Construct as a possibly empty list.
     *
     * @param includeDefaults include the default patterns
     */
    public LinePatternList(boolean includeDefaults)
    {
        this();
        if (!includeDefaults)
        {
            clear();
        }
    }

    /**
     * Construct with the possibility of default patterns included.
     *
     * @param includeDefaults include the default patterns
     * @param patterns        include the given patterns as well
     */
    public LinePatternList(boolean includeDefaults, float[][] patterns)
    {
        this(includeDefaults);
        if (patterns != null)
        {
            addAll(Arrays.asList(patterns));
        }
    }

    /**
     * Include the defaults in this list.
     */
    public final void includeDefaultPatterns()
    {
        addAll(Arrays.asList(defaultPatterns));
    }

    @Override
    public void add(int index, float[] element)
    {
        if (!contains(element))
        {
            super.add(index, element);
        }
    }

    @Override
    public boolean add(float[] e)
    {
        if (!contains(e))
        {
            return super.add(e);
        }
        else
        {
            return false;
        }
    }

}
