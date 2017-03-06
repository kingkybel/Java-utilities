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
 * @date: 2016-09-29
 * @author: Dieter J Kybelksties
 */
package com.kybelksties.jung.graphproperties;

import com.google.common.base.Function;
import com.kybelksties.gui.ColorUtils;
import com.kybelksties.gui.GradientType;
import edu.uci.ics.jung.visualization.picking.PickedInfo;
import java.awt.Color;
import java.awt.Paint;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

/**
 *
 * @author Dieter J Kybelksties
 * @param <GO> Graph object like vertex/edge
 */
class GraphObjectPaintTransformer<GO>
        implements Function<GO, Paint>
{

    private static final Class CLAZZ = GraphObjectPaintTransformer.class;
    private static final String CLASS_NAME = CLAZZ.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    PickedInfo<GO> pickedInfo = null;
    Map<GO, Number> transparency = null;

    Color[] unpickedColors = null;
    float[] unpickedRatios = null;
    Color[] pickedColors = null;
    float[] pickedRatios = null;

    GradientType pickedType = GradientType.UNIFORM;
    GradientType unpickedType = GradientType.UNIFORM;

    GraphObjectPaintTransformer(PickedInfo<GO> pickedInfo,
                                Map<GO, Number> transparency,
                                ArrayList<Color> unpickedColors,
                                ArrayList<Float> unpickedRatios,
                                GradientType unpickedType,
                                ArrayList<Color> pickedColors,
                                ArrayList<Float> pickedRatios,
                                GradientType pickedType)
    {
        this.pickedInfo = pickedInfo;
        if (transparency != null && !transparency.isEmpty())
        {
            this.transparency = transparency;
        }
        else
        {
            this.transparency = new TreeMap<>();

        }

        if (unpickedColors == null || unpickedColors.isEmpty())
        {
            unpickedColors = new ArrayList<>();
            unpickedColors.add(Color.BLACK);
        }
        this.unpickedColors = (Color[]) unpickedColors.toArray(new Color[0]);
        if (unpickedType != null &&
            unpickedType != GradientType.UNIFORM &&
            unpickedType != GradientType.FOUR_COLOR_RECTANGULAR &&
            unpickedType != GradientType.RANDOM_RASTER)
        {
            this.unpickedRatios = unpickedRatios != null &&
                                  unpickedRatios.size() > 0 ?
                                  new float[unpickedRatios.size()] :
                                  new float[]
                                  {
                                      1.0F
                                  };

        }

        if (unpickedRatios != null && unpickedRatios.size() > 0)
        {
            for (int i = 0; i < unpickedRatios.size(); i++)
            {
                this.unpickedRatios[i] = unpickedRatios.get(i);
            }
        }

        this.pickedColors = (pickedColors != null &&
                             pickedColors.size() > 0) ?
                            (Color[]) pickedColors.toArray() :
                            new Color[]
                            {
                                Color.GRAY
                            };
        this.pickedRatios = pickedRatios != null &&
                            pickedRatios.size() > 0 ?
                            new float[pickedRatios.size()] :
                            new float[]
                            {
                                1.0F
                            };
        if (pickedRatios != null && pickedRatios.size() > 0)
        {
            for (int i = 0; i < pickedRatios.size(); i++)
            {
                this.pickedRatios[i] = pickedRatios.get(i);
            }
        }

        initialize();
    }

    GraphObjectPaintTransformer(PickedInfo<GO> pickedInfo)
    {
        this(pickedInfo,
             null,
             null,
             null,
             null,
             null,
             null,
             null);
    }

    private void initialize()
    {

        if (unpickedRatios != null &&
            unpickedColors.length != unpickedRatios.length)
        {
            if (unpickedColors.length < unpickedRatios.length)
            {

                Color[] newUnpickedColors = new Color[unpickedRatios.length];
                for (int i = 0; i < unpickedRatios.length; i++)
                {
                    newUnpickedColors[i] = i < unpickedColors.length ?
                                           unpickedColors[i] :
                                           newUnpickedColors[i - 1];
                }
                unpickedColors = newUnpickedColors;
            }
            else
            {
                float[] newUnpickedRatios = new float[unpickedColors.length];
                for (int i = 0; i < unpickedRatios.length; i++)
                {
                    newUnpickedRatios[i] = i < unpickedRatios.length ?
                                           unpickedRatios[i] :
                                           newUnpickedRatios[i - 1];
                }
                unpickedRatios = newUnpickedRatios;
            }
        }
        if (unpickedColors.length == 1)
        {
            unpickedType = GradientType.UNIFORM;
        }
        if (unpickedType == null)
        {
            unpickedType = GradientType.LEFT_TO_RIGHT;
        }

        ///
        if (pickedColors.length != pickedRatios.length)
        {
            if (pickedColors.length < pickedRatios.length)
            {

                Color[] newPickedColors = new Color[pickedRatios.length];
                for (int i = 0; i < pickedRatios.length; i++)
                {
                    newPickedColors[i] = i < pickedColors.length ?
                                         pickedColors[i] :
                                         newPickedColors[i - 1];
                }
                pickedColors = newPickedColors;
            }
            else
            {
                float[] newPickedRatios = new float[pickedColors.length];
                for (int i = 0; i < pickedRatios.length; i++)
                {
                    newPickedRatios[i] = i < pickedRatios.length ?
                                         pickedRatios[i] :
                                         newPickedRatios[i - 1];
                }
                pickedRatios = newPickedRatios;
            }
        }
        if (pickedColors.length == 1)
        {
            pickedType = GradientType.UNIFORM;
        }
        if (pickedType == null)
        {
            pickedType = GradientType.LEFT_TO_RIGHT;
        }

    }

    @Override
    public Paint apply(GO v)
    {
        return pickedInfo.isPicked(v) ?
               makePaint(pickedColors,
                         pickedRatios,
                         getAlpha(v),
                         pickedType) :
               makePaint(unpickedColors,
                         unpickedRatios,
                         getAlpha(v),
                         unpickedType);
    }

    private float getAlpha(GO go)
    {
        if (transparency == null ||
            transparency.isEmpty() ||
            !transparency.containsKey(go))
        {
            return 1.0F;
        }
        return transparency.get(go).floatValue();
    }

    private Paint makePaint(Color[] colors,
                            float[] ratios,
                            Float alpha,
                            GradientType type)
    {
        ArrayList<Color> alphaColors = new ArrayList<>();
        for (Color c : colors)
        {
            float[] rgb = c.getColorComponents(null);
            alphaColors.add(
                    ColorUtils.makeColor(rgb[0], rgb[1], rgb[2], alpha));
        }
        return /*type == GradientType.UNIFORM ? */ alphaColors.
                get(0);
        //type == GradientType.TOP_TO_BOTTOM ? new LinearGradientPaint(
    }
}
