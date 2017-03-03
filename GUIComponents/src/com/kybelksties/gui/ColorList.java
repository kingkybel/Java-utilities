/*
 * Copyright (C) 2017 Dieter J Kybelksties
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
 * @date: 2017-03-03
 * @author: Dieter J Kybelksties
 */
package com.kybelksties.gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;

/**
 *
 * @author Dieter J Kybelksties
 */
public class ColorList extends ArrayList<Color>
{

    private static final Class CLAZZ = ColorList.class;
    private static final String CLASS_NAME = CLAZZ.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    GradientType type = GradientType.UNIFORM;
    float[] ratios = null;

    public class Exception extends java.lang.Exception
    {

        public Exception()
        {
        }

        public Exception(String message)
        {
            super(message);
        }

    }

    /**
     * Default constructor.
     *
     * @throws com.kybelksties.gui.ColorList.Exception
     */
    public ColorList() throws Exception
    {
        this(GradientType.UNIFORM);
    }

    /**
     * Construct with one or more colors.
     *
     * @param color  First color in the list
     * @param colors variable list of colors
     * @throws com.kybelksties.gui.ColorList.Exception
     */
    public ColorList(Color color, Color... colors) throws Exception
    {
        this(GradientType.UNIFORM, colors);
    }

    /**
     * Construct color list of specific type.
     *
     * @param type   gradient type
     * @param colors variable list of colors, that will be defaulted if
     *               necessary
     * @throws com.kybelksties.gui.ColorList.Exception
     */
    public ColorList(GradientType type, Color... colors) throws Exception
    {
        this.type = type == null ? GradientType.UNIFORM : type;
        int minNum = type == GradientType.UNIFORM ? 1 :
                     type == GradientType.TOP_TO_BOTTOM ? 2 :
                     type == GradientType.LEFT_TO_RIGHT ? 2 :
                     type == GradientType.DIAGONAL_LEFT_TOP_TO_RIGHT_BOTTOM ? 2 :
                     type == GradientType.DIAGONAL_RIGHT_TOP_TO_LEFT_BOTTOM ? 2 :
                     type == GradientType.CIRCULAR ? 2 :
                     type == GradientType.FOUR_COLOR_RECTANGULAR ? 4 :
                     type == GradientType.RANDOM_RASTER ? 5 : 1;

        if (colors != null)
        {
            addAll(Arrays.asList(colors));
        }
        if (size() < minNum)
        {
            int start = size();
            for (int i = start; i < minNum; i++)
            {
                add(ColorUtils.randomColor());
            }
        }
        setRatios(type);
    }

    private void setRatios(GradientType type, float... ratios) throws Exception
    {
        if (type == GradientType.TOP_TO_BOTTOM ||
            type == GradientType.LEFT_TO_RIGHT ||
            type == GradientType.DIAGONAL_LEFT_TOP_TO_RIGHT_BOTTOM ||
            type == GradientType.DIAGONAL_RIGHT_TOP_TO_LEFT_BOTTOM ||
            type == GradientType.CIRCULAR)
        {
            if (size() == 0)
            {
                throw new Exception(
                        "Cannot set ratios if number of colors is 0.");
            }
            if (ratios == null)
            {
                this.ratios = new float[size()];
                for (int i = 0; i < this.ratios.length; i++)
                {
                    this.ratios[i] = 1.0F / ((float) this.ratios.length);
                }
            }
            else
            {
                this.ratios = new float[size()];
                float sum = 0.0F;
                for (int i = 0; i < Math.min(size(), ratios.length); i++)
                {
                    this.ratios[i] = Math.abs(ratios[i]);
                    sum += this.ratios[i];
                }
                if (sum == 0.0F)
                {
                    for (int i = 0; i < size(); i++)
                    {
                        this.ratios[i] = 1.0f / ((float) size());
                    }
                }
                else
                {
                    for (int i = 0; i < size(); i++)
                    {
                        this.ratios[i] /= sum;
                    }
                }

            }
        }
    }

    public ColorList(GradientType type,
                     ArrayList<Color> colors,
                     ArrayList<Float> ratios) throws Exception
    {
        this(type, (Color[]) colors.toArray());
    }

    /**
     *
     * @return @throws com.kybelksties.gui.ColorList.Exception
     */
    public Color getColor() throws ColorList.Exception
    {
        if (!type.equals(GradientType.UNIFORM))
        {
            throw new Exception(
                    "Single color only defined for uniform color list.");
        }
        return get(0);
    }

    /**
     *
     * @return @throws com.kybelksties.gui.ColorList.Exception
     */
    public Color getAlternateColor() throws Exception
    {
        if (!type.equals(GradientType.UNIFORM))
        {
            throw new Exception(
                    "Alternate color only defined for uniform color list.");
        }
        return size() > 1 ?
               get(1) :
               ColorUtils.contrastColorByComplement(get(0));
    }

}
