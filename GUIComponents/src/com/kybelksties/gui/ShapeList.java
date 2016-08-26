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
 * Enumeration of the possible shapes.
 *
 * @author kybelksd
 */
public enum ShapeList
{

    /**
     * Rectangular shapes.
     */
    RECTANGLE,
    /**
     * Rectangular shapes with 3-D look.
     */
    RECTANGLE3D, /**
     * Oval shapes.
     */
    ELLIPSE,
    /**
     * Rectangular shapes with rounded edges.
     */
    ROUNDED_RECTANGLE,
    /**
     * Regular star shapes.
     */
    REGULAR_STAR,
    /**
     * Regular polygon shapes.
     */
    REGULAR_POLYGON,
    /**
     * General polygon shapes.
     */
    POLYGON,
    /**
     * Images serving as shapes.
     */
    IMAGE;

    private static final Class CLAZZ = ShapeList.class;
    private static final String CLASS_NAME = CLAZZ.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    @Override
    public String toString()
    {

        return this == RECTANGLE ?
                    NbBundle.getMessage(CLAZZ, "ShapeList.rectangle") :
               this == RECTANGLE3D ?
                    NbBundle.getMessage(CLAZZ, "ShapeList.rectangle3D") :
               this == ELLIPSE ?
                    NbBundle.getMessage(CLAZZ, "ShapeList.ellipse") :
               this == ROUNDED_RECTANGLE ?
                    NbBundle.getMessage(CLAZZ, "ShapeList.roundedRectangle") :
               this == REGULAR_STAR ?
                    NbBundle.getMessage(CLAZZ, "ShapeList.regularStar") :
               this == REGULAR_POLYGON ?
                    NbBundle.getMessage(CLAZZ, "ShapeList.regularPolygon") :
               this == POLYGON ?
                    NbBundle.getMessage(CLAZZ, "ShapeList.polygon") :
               this == IMAGE ?
                    NbBundle.getMessage(CLAZZ, "ShapeList.image") :
                    NbBundle.getMessage(CLAZZ, "ShapeList.unknown") ;
    }
}
