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
package com.kybelksties.gui;

import java.util.logging.Logger;
import org.openide.util.NbBundle;

/**
 * Enumeration of the possible gradient types.
 *
 * @author kybelksd
 */
public enum GradientType
{

    /**
     * 2-color gradient that changes from top to bottom.
     */
    TOP_TO_BOTTOM,
    /**
     * 2-color gradient that changes from left to right.
     */
    LEFT_TO_RIGHT,
    /**
     * 2-color gradient that changes diagonally from the top left to the right
     * bottom.
     */
    DIAGONAL_LEFT_TOP_TO_RIGHT_BOTTOM,
    /**
     * 2-color gradient that changes diagonally from the top right to the left
     * bottom.
     */
    DIAGONAL_RIGHT_TOP_TO_LEFT_BOTTOM,
    /**
     * 2-color gradient changing radial from the center.
     */
    CIRCULAR,
    /**
     * 4-color gradient from the corner of a rectangle.
     */
    FOUR_COLOR_RECTANGULAR,
    /**
     * Random raster.
     */
    RANDOM_RASTER;

    private static final Class CLAZZ = GradientColorChooserBeanInfo.class;
    private static final String CLASS_NAME = CLAZZ.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    @Override
    public String toString()
    {

        return NbBundle.getMessage(CLAZZ,
                                   this == TOP_TO_BOTTOM ?
                                   "GradientType.topToBottom" :
                                   this == LEFT_TO_RIGHT ?
                                   "GradientType.leftToRight" :
                                   this == DIAGONAL_LEFT_TOP_TO_RIGHT_BOTTOM ?
                                   "GradientType.firstMainDiag" :
                                   this == DIAGONAL_RIGHT_TOP_TO_LEFT_BOTTOM ?
                                   "GradientType.secondMainDiag" :
                                   this == CIRCULAR ?
                                   "GradientType.circular" :
                                   this == FOUR_COLOR_RECTANGULAR ?
                                   "GradientType.fourColor" :
                                   this == RANDOM_RASTER ?
                                   "GradientType.randomRaster" :
                                   "GradientType.unknown");
    }
}
