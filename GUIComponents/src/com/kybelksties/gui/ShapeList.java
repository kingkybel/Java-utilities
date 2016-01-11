
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

    @Override
    public String toString()
    {

        return this == RECTANGLE ? "Rectangle" :
               this == RECTANGLE3D ? "Rectangle 3D" :
               this == ELLIPSE ? "Ellipse" :
               this == ROUNDED_RECTANGLE ? "Rounded Rectangle" :
               this == REGULAR_STAR ? "Regular Star" :
               this == REGULAR_POLYGON ? "Regular polygon" :
               this == POLYGON ? "Polygon" :
               this == IMAGE ? "Image" :
               "<UNKNOWN>";
    }
}
