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

import com.kybelksties.general.ExtRandom;
import java.awt.Color;
import java.awt.MultipleGradientPaint;
import java.awt.Paint;
import java.awt.PaintContext;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import static java.awt.Transparency.OPAQUE;
import static java.awt.Transparency.TRANSLUCENT;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.min;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.TreeMap;
import java.util.logging.Logger;
import javax.swing.table.AbstractTableModel;
import org.openide.util.NbBundle;

/**
 * A collection of utilities for the manipulation of colors/paints,
 *
 * @author kybelksd
 */
public class ColorUtils
{

    private static final Class CLAZZ = ColorUtils.class;
    private static final String CLASS_NAME = CLAZZ.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    static Color2Name[] colName = new Color2Name[]
                {
                    new Color2Name(makeColor(255, 250, 250), "snow"),
                    new Color2Name(makeColor(255, 250, 240), "FloralWhite"),
                    // <editor-fold defaultstate="collapsed" desc="... of more X-Term Color names...">
                    new Color2Name(makeColor(255, 239, 213), "PapayaWhip"),
                    new Color2Name(makeColor(255, 235, 205), "BlanchedAlmond"),
                    new Color2Name(makeColor(255, 228, 196), "bisque"),
                    new Color2Name(makeColor(255, 218, 185), "PeachPuff"),
                    new Color2Name(makeColor(255, 222, 173), "NavajoWhite"),
                    new Color2Name(makeColor(255, 228, 181), "moccasin"),
                    new Color2Name(makeColor(255, 248, 220), "cornsilk"),
                    new Color2Name(makeColor(255, 255, 240), "ivory"),
                    new Color2Name(makeColor(255, 250, 205), "LemonChiffon"),
                    new Color2Name(makeColor(255, 245, 238), "seashell"),
                    new Color2Name(makeColor(255, 240, 245), "LavenderBlush"),
                    new Color2Name(makeColor(255, 228, 225), "MistyRose"),
                    new Color2Name(makeColor(255, 255, 255), "white"),
                    new Color2Name(makeColor(255, 255, 224), "LightYellow"),
                    new Color2Name(makeColor(255, 255, 0), "yellow"),
                    new Color2Name(makeColor(255, 215, 0), "gold"),
                    new Color2Name(makeColor(255, 160, 122), "LightSalmon"),
                    new Color2Name(makeColor(255, 165, 0), "orange"),
                    new Color2Name(makeColor(255, 140, 0), "DarkOrange"),
                    new Color2Name(makeColor(255, 127, 80), "coral"),
                    new Color2Name(makeColor(255, 99, 71), "tomato"),
                    new Color2Name(makeColor(255, 69, 0), "OrangeRed"),
                    new Color2Name(makeColor(255, 0, 0), "red"),
                    new Color2Name(makeColor(255, 105, 180), "HotPink"),
                    new Color2Name(makeColor(255, 20, 147), "DeepPink"),
                    new Color2Name(makeColor(255, 192, 203), "pink"),
                    new Color2Name(makeColor(255, 182, 193), "LightPink"),
                    new Color2Name(makeColor(255, 0, 255), "magenta"),
                    new Color2Name(makeColor(255, 250, 250), "snow1"),
                    new Color2Name(makeColor(255, 245, 238), "seashell1"),
                    new Color2Name(makeColor(255, 239, 219), "AntiqueWhite1"),
                    new Color2Name(makeColor(255, 228, 196), "bisque1"),
                    new Color2Name(makeColor(255, 218, 185), "PeachPuff1"),
                    new Color2Name(makeColor(255, 222, 173), "NavajoWhite1"),
                    new Color2Name(makeColor(255, 250, 205), "LemonChiffon1"),
                    new Color2Name(makeColor(255, 248, 220), "cornsilk1"),
                    new Color2Name(makeColor(255, 255, 240), "ivory1"),
                    new Color2Name(makeColor(255, 240, 245), "LavenderBlush1"),
                    new Color2Name(makeColor(255, 228, 225), "MistyRose1"),
                    new Color2Name(makeColor(255, 246, 143), "khaki1"),
                    new Color2Name(makeColor(255, 236, 139), "LightGoldenrod1"),
                    new Color2Name(makeColor(255, 255, 224), "LightYellow1"),
                    new Color2Name(makeColor(255, 255, 0), "yellow1"),
                    new Color2Name(makeColor(255, 215, 0), "gold1"),
                    new Color2Name(makeColor(255, 193, 37), "goldenrod1"),
                    new Color2Name(makeColor(255, 185, 15), "DarkGoldenrod1"),
                    new Color2Name(makeColor(255, 193, 193), "RosyBrown1"),
                    new Color2Name(makeColor(255, 106, 106), "IndianRed1"),
                    new Color2Name(makeColor(255, 130, 71), "sienna1"),
                    new Color2Name(makeColor(255, 211, 155), "burlywood1"),
                    new Color2Name(makeColor(255, 231, 186), "wheat1"),
                    new Color2Name(makeColor(255, 165, 79), "tan1"),
                    new Color2Name(makeColor(255, 127, 36), "chocolate1"),
                    new Color2Name(makeColor(255, 48, 48), "firebrick1"),
                    new Color2Name(makeColor(255, 64, 64), "brown1"),
                    new Color2Name(makeColor(255, 140, 105), "salmon1"),
                    new Color2Name(makeColor(255, 160, 122), "LightSalmon1"),
                    new Color2Name(makeColor(255, 165, 0), "orange1"),
                    new Color2Name(makeColor(255, 127, 0), "DarkOrange1"),
                    new Color2Name(makeColor(255, 114, 86), "coral1"),
                    new Color2Name(makeColor(255, 99, 71), "tomato1"),
                    new Color2Name(makeColor(255, 69, 0), "OrangeRed1"),
                    new Color2Name(makeColor(255, 0, 0), "red1"),
                    new Color2Name(makeColor(255, 20, 147), "DeepPink1"),
                    new Color2Name(makeColor(255, 110, 180), "HotPink1"),
                    new Color2Name(makeColor(255, 181, 197), "pink1"),
                    new Color2Name(makeColor(255, 174, 185), "LightPink1"),
                    new Color2Name(makeColor(255, 130, 171), "PaleVioletRed1"),
                    new Color2Name(makeColor(255, 52, 179), "maroon1"),
                    new Color2Name(makeColor(255, 62, 150), "VioletRed1"),
                    new Color2Name(makeColor(255, 0, 255), "magenta1"),
                    new Color2Name(makeColor(255, 131, 250), "orchid1"),
                    new Color2Name(makeColor(255, 187, 255), "plum1"),
                    new Color2Name(makeColor(255, 225, 255), "thistle1"),
                    new Color2Name(makeColor(255, 255, 255), "gray100"),
                    new Color2Name(makeColor(255, 255, 255), "grey100"),
                    new Color2Name(makeColor(253, 245, 230), "OldLace"),
                    new Color2Name(makeColor(252, 252, 252), "gray99"),
                    new Color2Name(makeColor(252, 252, 252), "grey99"),
                    new Color2Name(makeColor(250, 240, 230), "linen"),
                    new Color2Name(makeColor(250, 235, 215), "AntiqueWhite"),
                    new Color2Name(makeColor(250, 250, 210),
                                   "LightGoldenrodYellow"),
                    new Color2Name(makeColor(250, 128, 114), "salmon"),
                    new Color2Name(makeColor(250, 250, 250), "gray98"),
                    new Color2Name(makeColor(250, 250, 250), "grey98"),
                    new Color2Name(makeColor(248, 248, 255), "GhostWhite"),
                    new Color2Name(makeColor(247, 247, 247), "gray97"),
                    new Color2Name(makeColor(247, 247, 247), "grey97"),
                    new Color2Name(makeColor(245, 245, 245), "WhiteSmoke"),
                    new Color2Name(makeColor(245, 255, 250), "MintCream"),
                    new Color2Name(makeColor(245, 245, 220), "beige"),
                    new Color2Name(makeColor(245, 222, 179), "wheat"),
                    new Color2Name(makeColor(245, 245, 245), "gray96"),
                    new Color2Name(makeColor(245, 245, 245), "grey96"),
                    new Color2Name(makeColor(244, 164, 96), "SandyBrown"),
                    new Color2Name(makeColor(242, 242, 242), "gray95"),
                    new Color2Name(makeColor(242, 242, 242), "grey95"),
                    new Color2Name(makeColor(240, 255, 240), "honeydew"),
                    new Color2Name(makeColor(240, 255, 255), "azure"),
                    new Color2Name(makeColor(240, 248, 255), "AliceBlue"),
                    new Color2Name(makeColor(240, 230, 140), "khaki"),
                    new Color2Name(makeColor(240, 128, 128), "LightCoral"),
                    new Color2Name(makeColor(240, 255, 240), "honeydew1"),
                    new Color2Name(makeColor(240, 255, 255), "azure1"),
                    new Color2Name(makeColor(240, 240, 240), "gray94"),
                    new Color2Name(makeColor(240, 240, 240), "grey94"),
                    new Color2Name(makeColor(238, 232, 170), "PaleGoldenrod"),
                    new Color2Name(makeColor(238, 221, 130), "LightGoldenrod"),
                    new Color2Name(makeColor(238, 130, 238), "violet"),
                    new Color2Name(makeColor(238, 233, 233), "snow2"),
                    new Color2Name(makeColor(238, 229, 222), "seashell2"),
                    new Color2Name(makeColor(238, 223, 204), "AntiqueWhite2"),
                    new Color2Name(makeColor(238, 213, 183), "bisque2"),
                    new Color2Name(makeColor(238, 203, 173), "PeachPuff2"),
                    new Color2Name(makeColor(238, 207, 161), "NavajoWhite2"),
                    new Color2Name(makeColor(238, 233, 191), "LemonChiffon2"),
                    new Color2Name(makeColor(238, 232, 205), "cornsilk2"),
                    new Color2Name(makeColor(238, 238, 224), "ivory2"),
                    new Color2Name(makeColor(238, 224, 229), "LavenderBlush2"),
                    new Color2Name(makeColor(238, 213, 210), "MistyRose2"),
                    new Color2Name(makeColor(238, 230, 133), "khaki2"),
                    new Color2Name(makeColor(238, 220, 130), "LightGoldenrod2"),
                    new Color2Name(makeColor(238, 238, 209), "LightYellow2"),
                    new Color2Name(makeColor(238, 238, 0), "yellow2"),
                    new Color2Name(makeColor(238, 201, 0), "gold2"),
                    new Color2Name(makeColor(238, 180, 34), "goldenrod2"),
                    new Color2Name(makeColor(238, 173, 14), "DarkGoldenrod2"),
                    new Color2Name(makeColor(238, 180, 180), "RosyBrown2"),
                    new Color2Name(makeColor(238, 99, 99), "IndianRed2"),
                    new Color2Name(makeColor(238, 121, 66), "sienna2"),
                    new Color2Name(makeColor(238, 197, 145), "burlywood2"),
                    new Color2Name(makeColor(238, 216, 174), "wheat2"),
                    new Color2Name(makeColor(238, 154, 73), "tan2"),
                    new Color2Name(makeColor(238, 118, 33), "chocolate2"),
                    new Color2Name(makeColor(238, 44, 44), "firebrick2"),
                    new Color2Name(makeColor(238, 59, 59), "brown2"),
                    new Color2Name(makeColor(238, 130, 98), "salmon2"),
                    new Color2Name(makeColor(238, 149, 114), "LightSalmon2"),
                    new Color2Name(makeColor(238, 154, 0), "orange2"),
                    new Color2Name(makeColor(238, 118, 0), "DarkOrange2"),
                    new Color2Name(makeColor(238, 106, 80), "coral2"),
                    new Color2Name(makeColor(238, 92, 66), "tomato2"),
                    new Color2Name(makeColor(238, 64, 0), "OrangeRed2"),
                    new Color2Name(makeColor(238, 0, 0), "red2"),
                    new Color2Name(makeColor(238, 18, 137), "DeepPink2"),
                    new Color2Name(makeColor(238, 106, 167), "HotPink2"),
                    new Color2Name(makeColor(238, 169, 184), "pink2"),
                    new Color2Name(makeColor(238, 162, 173), "LightPink2"),
                    new Color2Name(makeColor(238, 121, 159), "PaleVioletRed2"),
                    new Color2Name(makeColor(238, 48, 167), "maroon2"),
                    new Color2Name(makeColor(238, 58, 140), "VioletRed2"),
                    new Color2Name(makeColor(238, 0, 238), "magenta2"),
                    new Color2Name(makeColor(238, 122, 233), "orchid2"),
                    new Color2Name(makeColor(238, 174, 238), "plum2"),
                    new Color2Name(makeColor(238, 210, 238), "thistle2"),
                    new Color2Name(makeColor(237, 237, 237), "gray93"),
                    new Color2Name(makeColor(237, 237, 237), "grey93"),
                    new Color2Name(makeColor(235, 235, 235), "gray92"),
                    new Color2Name(makeColor(235, 235, 235), "grey92"),
                    new Color2Name(makeColor(233, 150, 122), "DarkSalmon"),
                    new Color2Name(makeColor(232, 232, 232), "gray91"),
                    new Color2Name(makeColor(232, 232, 232), "grey91"),
                    new Color2Name(makeColor(230, 230, 250), "lavender"),
                    new Color2Name(makeColor(229, 229, 229), "gray90"),
                    new Color2Name(makeColor(229, 229, 229), "grey90"),
                    new Color2Name(makeColor(227, 227, 227), "gray89"),
                    new Color2Name(makeColor(227, 227, 227), "grey89"),
                    new Color2Name(makeColor(224, 255, 255), "LightCyan"),
                    new Color2Name(makeColor(224, 238, 224), "honeydew2"),
                    new Color2Name(makeColor(224, 238, 238), "azure2"),
                    new Color2Name(makeColor(224, 255, 255), "LightCyan1"),
                    new Color2Name(makeColor(224, 102, 255), "MediumOrchid1"),
                    new Color2Name(makeColor(224, 224, 224), "gray88"),
                    new Color2Name(makeColor(224, 224, 224), "grey88"),
                    new Color2Name(makeColor(222, 184, 135), "burlywood"),
                    new Color2Name(makeColor(222, 222, 222), "gray87"),
                    new Color2Name(makeColor(222, 222, 222), "grey87"),
                    new Color2Name(makeColor(221, 160, 221), "plum"),
                    new Color2Name(makeColor(220, 220, 220), "gainsboro"),
                    new Color2Name(makeColor(219, 112, 147), "PaleVioletRed"),
                    new Color2Name(makeColor(219, 219, 219), "gray86"),
                    new Color2Name(makeColor(219, 219, 219), "grey86"),
                    new Color2Name(makeColor(218, 165, 32), "goldenrod"),
                    new Color2Name(makeColor(218, 112, 214), "orchid"),
                    new Color2Name(makeColor(217, 217, 217), "gray85"),
                    new Color2Name(makeColor(217, 217, 217), "grey85"),
                    new Color2Name(makeColor(216, 191, 216), "thistle"),
                    new Color2Name(makeColor(214, 214, 214), "gray84"),
                    new Color2Name(makeColor(214, 214, 214), "grey84"),
                    new Color2Name(makeColor(212, 212, 212), "gray83"),
                    new Color2Name(makeColor(212, 212, 212), "grey83"),
                    new Color2Name(makeColor(211, 211, 211), "LightGrey"),
                    new Color2Name(makeColor(211, 211, 211), "LightGray"),
                    new Color2Name(makeColor(210, 180, 140), "tan"),
                    new Color2Name(makeColor(210, 105, 30), "chocolate"),
                    new Color2Name(makeColor(209, 238, 238), "LightCyan2"),
                    new Color2Name(makeColor(209, 95, 238), "MediumOrchid2"),
                    new Color2Name(makeColor(209, 209, 209), "gray82"),
                    new Color2Name(makeColor(209, 209, 209), "grey82"),
                    new Color2Name(makeColor(208, 32, 144), "VioletRed"),
                    new Color2Name(makeColor(207, 207, 207), "gray81"),
                    new Color2Name(makeColor(207, 207, 207), "grey81"),
                    new Color2Name(makeColor(205, 92, 92), "IndianRed"),
                    new Color2Name(makeColor(205, 133, 63), "peru"),
                    new Color2Name(makeColor(205, 201, 201), "snow3"),
                    new Color2Name(makeColor(205, 197, 191), "seashell3"),
                    new Color2Name(makeColor(205, 192, 176), "AntiqueWhite3"),
                    new Color2Name(makeColor(205, 183, 158), "bisque3"),
                    new Color2Name(makeColor(205, 175, 149), "PeachPuff3"),
                    new Color2Name(makeColor(205, 179, 139), "NavajoWhite3"),
                    new Color2Name(makeColor(205, 201, 165), "LemonChiffon3"),
                    new Color2Name(makeColor(205, 200, 177), "cornsilk3"),
                    new Color2Name(makeColor(205, 205, 193), "ivory3"),
                    new Color2Name(makeColor(205, 193, 197), "LavenderBlush3"),
                    new Color2Name(makeColor(205, 183, 181), "MistyRose3"),
                    new Color2Name(makeColor(205, 198, 115), "khaki3"),
                    new Color2Name(makeColor(205, 190, 112), "LightGoldenrod3"),
                    new Color2Name(makeColor(205, 205, 180), "LightYellow3"),
                    new Color2Name(makeColor(205, 205, 0), "yellow3"),
                    new Color2Name(makeColor(205, 173, 0), "gold3"),
                    new Color2Name(makeColor(205, 155, 29), "goldenrod3"),
                    new Color2Name(makeColor(205, 149, 12), "DarkGoldenrod3"),
                    new Color2Name(makeColor(205, 155, 155), "RosyBrown3"),
                    new Color2Name(makeColor(205, 85, 85), "IndianRed3"),
                    new Color2Name(makeColor(205, 104, 57), "sienna3"),
                    new Color2Name(makeColor(205, 170, 125), "burlywood3"),
                    new Color2Name(makeColor(205, 186, 150), "wheat3"),
                    new Color2Name(makeColor(205, 133, 63), "tan3"),
                    new Color2Name(makeColor(205, 102, 29), "chocolate3"),
                    new Color2Name(makeColor(205, 38, 38), "firebrick3"),
                    new Color2Name(makeColor(205, 51, 51), "brown3"),
                    new Color2Name(makeColor(205, 112, 84), "salmon3"),
                    new Color2Name(makeColor(205, 129, 98), "LightSalmon3"),
                    new Color2Name(makeColor(205, 133, 0), "orange3"),
                    new Color2Name(makeColor(205, 102, 0), "DarkOrange3"),
                    new Color2Name(makeColor(205, 91, 69), "coral3"),
                    new Color2Name(makeColor(205, 79, 57), "tomato3"),
                    new Color2Name(makeColor(205, 55, 0), "OrangeRed3"),
                    new Color2Name(makeColor(205, 0, 0), "red3"),
                    new Color2Name(makeColor(205, 16, 118), "DeepPink3"),
                    new Color2Name(makeColor(205, 96, 144), "HotPink3"),
                    new Color2Name(makeColor(205, 145, 158), "pink3"),
                    new Color2Name(makeColor(205, 140, 149), "LightPink3"),
                    new Color2Name(makeColor(205, 104, 137), "PaleVioletRed3"),
                    new Color2Name(makeColor(205, 41, 144), "maroon3"),
                    new Color2Name(makeColor(205, 50, 120), "VioletRed3"),
                    new Color2Name(makeColor(205, 0, 205), "magenta3"),
                    new Color2Name(makeColor(205, 105, 201), "orchid3"),
                    new Color2Name(makeColor(205, 150, 205), "plum3"),
                    new Color2Name(makeColor(205, 181, 205), "thistle3"),
                    new Color2Name(makeColor(204, 204, 204), "gray80"),
                    new Color2Name(makeColor(204, 204, 204), "grey80"),
                    new Color2Name(makeColor(202, 225, 255), "LightSteelBlue1"),
                    new Color2Name(makeColor(202, 255, 112), "DarkOliveGreen1"),
                    new Color2Name(makeColor(201, 201, 201), "gray79"),
                    new Color2Name(makeColor(201, 201, 201), "grey79"),
                    new Color2Name(makeColor(199, 21, 133), "MediumVioletRed"),
                    new Color2Name(makeColor(199, 199, 199), "gray78"),
                    new Color2Name(makeColor(199, 199, 199), "grey78"),
                    new Color2Name(makeColor(198, 226, 255), "SlateGray1"),
                    new Color2Name(makeColor(196, 196, 196), "gray77"),
                    new Color2Name(makeColor(196, 196, 196), "grey77"),
                    new Color2Name(makeColor(194, 194, 194), "gray76"),
                    new Color2Name(makeColor(194, 194, 194), "grey76"),
                    new Color2Name(makeColor(193, 205, 193), "honeydew3"),
                    new Color2Name(makeColor(193, 205, 205), "azure3"),
                    new Color2Name(makeColor(193, 255, 193), "DarkSeaGreen1"),
                    new Color2Name(makeColor(192, 255, 62), "OliveDrab1"),
                    new Color2Name(makeColor(191, 239, 255), "LightBlue1"),
                    new Color2Name(makeColor(191, 62, 255), "DarkOrchid1"),
                    new Color2Name(makeColor(191, 191, 191), "gray75"),
                    new Color2Name(makeColor(191, 191, 191), "grey75"),
                    new Color2Name(makeColor(190, 190, 190), "gray"),
                    new Color2Name(makeColor(190, 190, 190), "grey"),
                    new Color2Name(makeColor(189, 183, 107), "DarkKhaki"),
                    new Color2Name(makeColor(189, 189, 189), "gray74"),
                    new Color2Name(makeColor(189, 189, 189), "grey74"),
                    new Color2Name(makeColor(188, 143, 143), "RosyBrown"),
                    new Color2Name(makeColor(188, 210, 238), "LightSteelBlue2"),
                    new Color2Name(makeColor(188, 238, 104), "DarkOliveGreen2"),
                    new Color2Name(makeColor(187, 255, 255), "PaleTurquoise1"),
                    new Color2Name(makeColor(186, 85, 211), "MediumOrchid"),
                    new Color2Name(makeColor(186, 186, 186), "gray73"),
                    new Color2Name(makeColor(186, 186, 186), "grey73"),
                    new Color2Name(makeColor(185, 211, 238), "SlateGray2"),
                    new Color2Name(makeColor(184, 134, 11), "DarkGoldenrod"),
                    new Color2Name(makeColor(184, 184, 184), "gray72"),
                    new Color2Name(makeColor(184, 184, 184), "grey72"),
                    new Color2Name(makeColor(181, 181, 181), "gray71"),
                    new Color2Name(makeColor(181, 181, 181), "grey71"),
                    new Color2Name(makeColor(180, 205, 205), "LightCyan3"),
                    new Color2Name(makeColor(180, 238, 180), "DarkSeaGreen2"),
                    new Color2Name(makeColor(180, 82, 205), "MediumOrchid3"),
                    new Color2Name(makeColor(179, 238, 58), "OliveDrab2"),
                    new Color2Name(makeColor(179, 179, 179), "gray70"),
                    new Color2Name(makeColor(179, 179, 179), "grey70"),
                    new Color2Name(makeColor(178, 34, 34), "firebrick"),
                    new Color2Name(makeColor(178, 223, 238), "LightBlue2"),
                    new Color2Name(makeColor(178, 58, 238), "DarkOrchid2"),
                    new Color2Name(makeColor(176, 196, 222), "LightSteelBlue"),
                    new Color2Name(makeColor(176, 224, 230), "PowderBlue"),
                    new Color2Name(makeColor(176, 48, 96), "maroon"),
                    new Color2Name(makeColor(176, 226, 255), "LightSkyBlue1"),
                    new Color2Name(makeColor(176, 176, 176), "gray69"),
                    new Color2Name(makeColor(176, 176, 176), "grey69"),
                    new Color2Name(makeColor(175, 238, 238), "PaleTurquoise"),
                    new Color2Name(makeColor(174, 238, 238), "PaleTurquoise2"),
                    new Color2Name(makeColor(173, 216, 230), "LightBlue"),
                    new Color2Name(makeColor(173, 255, 47), "GreenYellow"),
                    new Color2Name(makeColor(173, 173, 173), "gray68"),
                    new Color2Name(makeColor(173, 173, 173), "grey68"),
                    new Color2Name(makeColor(171, 130, 255), "MediumPurple1"),
                    new Color2Name(makeColor(171, 171, 171), "gray67"),
                    new Color2Name(makeColor(171, 171, 171), "grey67"),
                    new Color2Name(makeColor(169, 169, 169), "DarkGrey"),
                    new Color2Name(makeColor(169, 169, 169), "DarkGray"),
                    new Color2Name(makeColor(168, 168, 168), "gray66"),
                    new Color2Name(makeColor(168, 168, 168), "grey66"),
                    new Color2Name(makeColor(166, 166, 166), "gray65"),
                    new Color2Name(makeColor(166, 166, 166), "grey65"),
                    new Color2Name(makeColor(165, 42, 42), "brown"),
                    new Color2Name(makeColor(164, 211, 238), "LightSkyBlue2"),
                    new Color2Name(makeColor(163, 163, 163), "gray64"),
                    new Color2Name(makeColor(163, 163, 163), "grey64"),
                    new Color2Name(makeColor(162, 181, 205), "LightSteelBlue3"),
                    new Color2Name(makeColor(162, 205, 90), "DarkOliveGreen3"),
                    new Color2Name(makeColor(161, 161, 161), "gray63"),
                    new Color2Name(makeColor(161, 161, 161), "grey63"),
                    new Color2Name(makeColor(160, 82, 45), "sienna"),
                    new Color2Name(makeColor(160, 32, 240), "purple"),
                    new Color2Name(makeColor(159, 182, 205), "SlateGray3"),
                    new Color2Name(makeColor(159, 121, 238), "MediumPurple2"),
                    new Color2Name(makeColor(158, 158, 158), "gray62"),
                    new Color2Name(makeColor(158, 158, 158), "grey62"),
                    new Color2Name(makeColor(156, 156, 156), "gray61"),
                    new Color2Name(makeColor(156, 156, 156), "grey61"),
                    new Color2Name(makeColor(155, 205, 155), "DarkSeaGreen3"),
                    new Color2Name(makeColor(155, 48, 255), "purple1"),
                    new Color2Name(makeColor(154, 205, 50), "YellowGreen"),
                    new Color2Name(makeColor(154, 192, 205), "LightBlue3"),
                    new Color2Name(makeColor(154, 255, 154), "PaleGreen1"),
                    new Color2Name(makeColor(154, 205, 50), "OliveDrab3"),
                    new Color2Name(makeColor(154, 50, 205), "DarkOrchid3"),
                    new Color2Name(makeColor(153, 50, 204), "DarkOrchid"),
                    new Color2Name(makeColor(153, 153, 153), "gray60"),
                    new Color2Name(makeColor(153, 153, 153), "grey60"),
                    new Color2Name(makeColor(152, 251, 152), "PaleGreen"),
                    new Color2Name(makeColor(152, 245, 255), "CadetBlue1"),
                    new Color2Name(makeColor(151, 255, 255), "DarkSlateGray1"),
                    new Color2Name(makeColor(150, 205, 205), "PaleTurquoise3"),
                    new Color2Name(makeColor(150, 150, 150), "gray59"),
                    new Color2Name(makeColor(150, 150, 150), "grey59"),
                    new Color2Name(makeColor(148, 0, 211), "DarkViolet"),
                    new Color2Name(makeColor(148, 148, 148), "gray58"),
                    new Color2Name(makeColor(148, 148, 148), "grey58"),
                    new Color2Name(makeColor(147, 112, 219), "MediumPurple"),
                    new Color2Name(makeColor(145, 44, 238), "purple2"),
                    new Color2Name(makeColor(145, 145, 145), "gray57"),
                    new Color2Name(makeColor(145, 145, 145), "grey57"),
                    new Color2Name(makeColor(144, 238, 144), "PaleGreen2"),
                    new Color2Name(makeColor(144, 238, 144), "LightGreen"),
                    new Color2Name(makeColor(143, 188, 143), "DarkSeaGreen"),
                    new Color2Name(makeColor(143, 143, 143), "gray56"),
                    new Color2Name(makeColor(143, 143, 143), "grey56"),
                    new Color2Name(makeColor(142, 229, 238), "CadetBlue2"),
                    new Color2Name(makeColor(141, 182, 205), "LightSkyBlue3"),
                    new Color2Name(makeColor(141, 238, 238), "DarkSlateGray2"),
                    new Color2Name(makeColor(140, 140, 140), "gray55"),
                    new Color2Name(makeColor(140, 140, 140), "grey55"),
                    new Color2Name(makeColor(139, 69, 19), "SaddleBrown"),
                    new Color2Name(makeColor(139, 137, 137), "snow4"),
                    new Color2Name(makeColor(139, 134, 130), "seashell4"),
                    new Color2Name(makeColor(139, 131, 120), "AntiqueWhite4"),
                    new Color2Name(makeColor(139, 125, 107), "bisque4"),
                    new Color2Name(makeColor(139, 119, 101), "PeachPuff4"),
                    new Color2Name(makeColor(139, 121, 94), "NavajoWhite4"),
                    new Color2Name(makeColor(139, 137, 112), "LemonChiffon4"),
                    new Color2Name(makeColor(139, 136, 120), "cornsilk4"),
                    new Color2Name(makeColor(139, 139, 131), "ivory4"),
                    new Color2Name(makeColor(139, 131, 134), "LavenderBlush4"),
                    new Color2Name(makeColor(139, 125, 123), "MistyRose4"),
                    new Color2Name(makeColor(139, 134, 78), "khaki4"),
                    new Color2Name(makeColor(139, 129, 76), "LightGoldenrod4"),
                    new Color2Name(makeColor(139, 139, 122), "LightYellow4"),
                    new Color2Name(makeColor(139, 139, 0), "yellow4"),
                    new Color2Name(makeColor(139, 117, 0), "gold4"),
                    new Color2Name(makeColor(139, 105, 20), "goldenrod4"),
                    new Color2Name(makeColor(139, 101, 8), "DarkGoldenrod4"),
                    new Color2Name(makeColor(139, 105, 105), "RosyBrown4"),
                    new Color2Name(makeColor(139, 58, 58), "IndianRed4"),
                    new Color2Name(makeColor(139, 71, 38), "sienna4"),
                    new Color2Name(makeColor(139, 115, 85), "burlywood4"),
                    new Color2Name(makeColor(139, 126, 102), "wheat4"),
                    new Color2Name(makeColor(139, 90, 43), "tan4"),
                    new Color2Name(makeColor(139, 69, 19), "chocolate4"),
                    new Color2Name(makeColor(139, 26, 26), "firebrick4"),
                    new Color2Name(makeColor(139, 35, 35), "brown4"),
                    new Color2Name(makeColor(139, 76, 57), "salmon4"),
                    new Color2Name(makeColor(139, 87, 66), "LightSalmon4"),
                    new Color2Name(makeColor(139, 90, 0), "orange4"),
                    new Color2Name(makeColor(139, 69, 0), "DarkOrange4"),
                    new Color2Name(makeColor(139, 62, 47), "coral4"),
                    new Color2Name(makeColor(139, 54, 38), "tomato4"),
                    new Color2Name(makeColor(139, 37, 0), "OrangeRed4"),
                    new Color2Name(makeColor(139, 0, 0), "red4"),
                    new Color2Name(makeColor(139, 10, 80), "DeepPink4"),
                    new Color2Name(makeColor(139, 58, 98), "HotPink4"),
                    new Color2Name(makeColor(139, 99, 108), "pink4"),
                    new Color2Name(makeColor(139, 95, 101), "LightPink4"),
                    new Color2Name(makeColor(139, 71, 93), "PaleVioletRed4"),
                    new Color2Name(makeColor(139, 28, 98), "maroon4"),
                    new Color2Name(makeColor(139, 34, 82), "VioletRed4"),
                    new Color2Name(makeColor(139, 0, 139), "magenta4"),
                    new Color2Name(makeColor(139, 71, 137), "orchid4"),
                    new Color2Name(makeColor(139, 102, 139), "plum4"),
                    new Color2Name(makeColor(139, 123, 139), "thistle4"),
                    new Color2Name(makeColor(139, 0, 139), "DarkMagenta"),
                    new Color2Name(makeColor(139, 0, 0), "DarkRed"),
                    new Color2Name(makeColor(138, 43, 226), "BlueViolet"),
                    new Color2Name(makeColor(138, 138, 138), "gray54"),
                    new Color2Name(makeColor(138, 138, 138), "grey54"),
                    new Color2Name(makeColor(137, 104, 205), "MediumPurple3"),
                    new Color2Name(makeColor(135, 206, 235), "SkyBlue"),
                    new Color2Name(makeColor(135, 206, 250), "LightSkyBlue"),
                    new Color2Name(makeColor(135, 206, 255), "SkyBlue1"),
                    new Color2Name(makeColor(135, 135, 135), "gray53"),
                    new Color2Name(makeColor(135, 135, 135), "grey53"),
                    new Color2Name(makeColor(133, 133, 133), "gray52"),
                    new Color2Name(makeColor(133, 133, 133), "grey52"),
                    new Color2Name(makeColor(132, 112, 255), "LightSlateBlue"),
                    new Color2Name(makeColor(131, 139, 131), "honeydew4"),
                    new Color2Name(makeColor(131, 139, 139), "azure4"),
                    new Color2Name(makeColor(131, 111, 255), "SlateBlue1"),
                    new Color2Name(makeColor(130, 130, 130), "gray51"),
                    new Color2Name(makeColor(130, 130, 130), "grey51"),
                    new Color2Name(makeColor(127, 255, 212), "aquamarine"),
                    new Color2Name(makeColor(127, 255, 0), "chartreuse"),
                    new Color2Name(makeColor(127, 255, 212), "aquamarine1"),
                    new Color2Name(makeColor(127, 255, 0), "chartreuse1"),
                    new Color2Name(makeColor(127, 127, 127), "gray50"),
                    new Color2Name(makeColor(127, 127, 127), "grey50"),
                    new Color2Name(makeColor(126, 192, 238), "SkyBlue2"),
                    new Color2Name(makeColor(125, 38, 205), "purple3"),
                    new Color2Name(makeColor(125, 125, 125), "gray49"),
                    new Color2Name(makeColor(125, 125, 125), "grey49"),
                    new Color2Name(makeColor(124, 252, 0), "LawnGreen"),
                    new Color2Name(makeColor(124, 205, 124), "PaleGreen3"),
                    new Color2Name(makeColor(123, 104, 238), "MediumSlateBlue"),
                    new Color2Name(makeColor(122, 103, 238), "SlateBlue2"),
                    new Color2Name(makeColor(122, 139, 139), "LightCyan4"),
                    new Color2Name(makeColor(122, 197, 205), "CadetBlue3"),
                    new Color2Name(makeColor(122, 55, 139), "MediumOrchid4"),
                    new Color2Name(makeColor(122, 122, 122), "gray48"),
                    new Color2Name(makeColor(122, 122, 122), "grey48"),
                    new Color2Name(makeColor(121, 205, 205), "DarkSlateGray3"),
                    new Color2Name(makeColor(120, 120, 120), "gray47"),
                    new Color2Name(makeColor(120, 120, 120), "grey47"),
                    new Color2Name(makeColor(119, 136, 153), "LightSlateGray"),
                    new Color2Name(makeColor(119, 136, 153), "LightSlateGrey"),
                    new Color2Name(makeColor(118, 238, 198), "aquamarine2"),
                    new Color2Name(makeColor(118, 238, 0), "chartreuse2"),
                    new Color2Name(makeColor(117, 117, 117), "gray46"),
                    new Color2Name(makeColor(117, 117, 117), "grey46"),
                    new Color2Name(makeColor(115, 115, 115), "gray45"),
                    new Color2Name(makeColor(115, 115, 115), "grey45"),
                    new Color2Name(makeColor(112, 128, 144), "SlateGray"),
                    new Color2Name(makeColor(112, 128, 144), "SlateGrey"),
                    new Color2Name(makeColor(112, 112, 112), "gray44"),
                    new Color2Name(makeColor(112, 112, 112), "grey44"),
                    new Color2Name(makeColor(110, 123, 139), "LightSteelBlue4"),
                    new Color2Name(makeColor(110, 139, 61), "DarkOliveGreen4"),
                    new Color2Name(makeColor(110, 110, 110), "gray43"),
                    new Color2Name(makeColor(110, 110, 110), "grey43"),
                    new Color2Name(makeColor(108, 166, 205), "SkyBlue3"),
                    new Color2Name(makeColor(108, 123, 139), "SlateGray4"),
                    new Color2Name(makeColor(107, 142, 35), "OliveDrab"),
                    new Color2Name(makeColor(107, 107, 107), "gray42"),
                    new Color2Name(makeColor(107, 107, 107), "grey42"),
                    new Color2Name(makeColor(106, 90, 205), "SlateBlue"),
                    new Color2Name(makeColor(105, 105, 105), "DimGray"),
                    new Color2Name(makeColor(105, 105, 105), "DimGrey"),
                    new Color2Name(makeColor(105, 89, 205), "SlateBlue3"),
                    new Color2Name(makeColor(105, 139, 105), "DarkSeaGreen4"),
                    new Color2Name(makeColor(105, 139, 34), "OliveDrab4"),
                    new Color2Name(makeColor(105, 105, 105), "gray41"),
                    new Color2Name(makeColor(105, 105, 105), "grey41"),
                    new Color2Name(makeColor(104, 131, 139), "LightBlue4"),
                    new Color2Name(makeColor(104, 34, 139), "DarkOrchid4"),
                    new Color2Name(makeColor(102, 205, 170), "MediumAquamarine"),
                    new Color2Name(makeColor(102, 139, 139), "PaleTurquoise4"),
                    new Color2Name(makeColor(102, 205, 170), "aquamarine3"),
                    new Color2Name(makeColor(102, 205, 0), "chartreuse3"),
                    new Color2Name(makeColor(102, 102, 102), "gray40"),
                    new Color2Name(makeColor(102, 102, 102), "grey40"),
                    new Color2Name(makeColor(100, 149, 237), "CornflowerBlue"),
                    new Color2Name(makeColor(99, 184, 255), "SteelBlue1"),
                    new Color2Name(makeColor(99, 99, 99), "gray39"),
                    new Color2Name(makeColor(99, 99, 99), "grey39"),
                    new Color2Name(makeColor(97, 97, 97), "gray38"),
                    new Color2Name(makeColor(97, 97, 97), "grey38"),
                    new Color2Name(makeColor(96, 123, 139), "LightSkyBlue4"),
                    new Color2Name(makeColor(95, 158, 160), "CadetBlue"),
                    new Color2Name(makeColor(94, 94, 94), "gray37"),
                    new Color2Name(makeColor(94, 94, 94), "grey37"),
                    new Color2Name(makeColor(93, 71, 139), "MediumPurple4"),
                    new Color2Name(makeColor(92, 172, 238), "SteelBlue2"),
                    new Color2Name(makeColor(92, 92, 92), "gray36"),
                    new Color2Name(makeColor(92, 92, 92), "grey36"),
                    new Color2Name(makeColor(89, 89, 89), "gray35"),
                    new Color2Name(makeColor(89, 89, 89), "grey35"),
                    new Color2Name(makeColor(87, 87, 87), "gray34"),
                    new Color2Name(makeColor(87, 87, 87), "grey34"),
                    new Color2Name(makeColor(85, 107, 47), "DarkOliveGreen"),
                    new Color2Name(makeColor(85, 26, 139), "purple4"),
                    new Color2Name(makeColor(84, 255, 159), "SeaGreen1"),
                    new Color2Name(makeColor(84, 139, 84), "PaleGreen4"),
                    new Color2Name(makeColor(84, 84, 84), "gray33"),
                    new Color2Name(makeColor(84, 84, 84), "grey33"),
                    new Color2Name(makeColor(83, 134, 139), "CadetBlue4"),
                    new Color2Name(makeColor(82, 139, 139), "DarkSlateGray4"),
                    new Color2Name(makeColor(82, 82, 82), "gray32"),
                    new Color2Name(makeColor(82, 82, 82), "grey32"),
                    new Color2Name(makeColor(79, 148, 205), "SteelBlue3"),
                    new Color2Name(makeColor(79, 79, 79), "gray31"),
                    new Color2Name(makeColor(79, 79, 79), "grey31"),
                    new Color2Name(makeColor(78, 238, 148), "SeaGreen2"),
                    new Color2Name(makeColor(77, 77, 77), "gray30"),
                    new Color2Name(makeColor(77, 77, 77), "grey30"),
                    new Color2Name(makeColor(74, 112, 139), "SkyBlue4"),
                    new Color2Name(makeColor(74, 74, 74), "gray29"),
                    new Color2Name(makeColor(74, 74, 74), "grey29"),
                    new Color2Name(makeColor(72, 61, 139), "DarkSlateBlue"),
                    new Color2Name(makeColor(72, 209, 204), "MediumTurquoise"),
                    new Color2Name(makeColor(72, 118, 255), "RoyalBlue1"),
                    new Color2Name(makeColor(71, 60, 139), "SlateBlue4"),
                    new Color2Name(makeColor(71, 71, 71), "gray28"),
                    new Color2Name(makeColor(71, 71, 71), "grey28"),
                    new Color2Name(makeColor(70, 130, 180), "SteelBlue"),
                    new Color2Name(makeColor(69, 139, 116), "aquamarine4"),
                    new Color2Name(makeColor(69, 139, 0), "chartreuse4"),
                    new Color2Name(makeColor(69, 69, 69), "gray27"),
                    new Color2Name(makeColor(69, 69, 69), "grey27"),
                    new Color2Name(makeColor(67, 110, 238), "RoyalBlue2"),
                    new Color2Name(makeColor(67, 205, 128), "SeaGreen3"),
                    new Color2Name(makeColor(66, 66, 66), "gray26"),
                    new Color2Name(makeColor(66, 66, 66), "grey26"),
                    new Color2Name(makeColor(65, 105, 225), "RoyalBlue"),
                    new Color2Name(makeColor(64, 224, 208), "turquoise"),
                    new Color2Name(makeColor(64, 64, 64), "gray25"),
                    new Color2Name(makeColor(64, 64, 64), "grey25"),
                    new Color2Name(makeColor(61, 61, 61), "gray24"),
                    new Color2Name(makeColor(61, 61, 61), "grey24"),
                    new Color2Name(makeColor(60, 179, 113), "MediumSeaGreen"),
                    new Color2Name(makeColor(59, 59, 59), "gray23"),
                    new Color2Name(makeColor(59, 59, 59), "grey23"),
                    new Color2Name(makeColor(58, 95, 205), "RoyalBlue3"),
                    new Color2Name(makeColor(56, 56, 56), "gray22"),
                    new Color2Name(makeColor(56, 56, 56), "grey22"),
                    new Color2Name(makeColor(54, 100, 139), "SteelBlue4"),
                    new Color2Name(makeColor(54, 54, 54), "gray21"),
                    new Color2Name(makeColor(54, 54, 54), "grey21"),
                    new Color2Name(makeColor(51, 51, 51), "gray20"),
                    new Color2Name(makeColor(51, 51, 51), "grey20"),
                    new Color2Name(makeColor(50, 205, 50), "LimeGreen"),
                    new Color2Name(makeColor(48, 48, 48), "gray19"),
                    new Color2Name(makeColor(48, 48, 48), "grey19"),
                    new Color2Name(makeColor(47, 79, 79), "DarkSlateGray"),
                    new Color2Name(makeColor(47, 79, 79), "DarkSlateGrey"),
                    new Color2Name(makeColor(46, 139, 87), "SeaGreen"),
                    new Color2Name(makeColor(46, 139, 87), "SeaGreen4"),
                    new Color2Name(makeColor(46, 46, 46), "gray18"),
                    new Color2Name(makeColor(46, 46, 46), "grey18"),
                    new Color2Name(makeColor(43, 43, 43), "gray17"),
                    new Color2Name(makeColor(43, 43, 43), "grey17"),
                    new Color2Name(makeColor(41, 41, 41), "gray16"),
                    new Color2Name(makeColor(41, 41, 41), "grey16"),
                    new Color2Name(makeColor(39, 64, 139), "RoyalBlue4"),
                    new Color2Name(makeColor(38, 38, 38), "gray15"),
                    new Color2Name(makeColor(38, 38, 38), "grey15"),
                    new Color2Name(makeColor(36, 36, 36), "gray14"),
                    new Color2Name(makeColor(36, 36, 36), "grey14"),
                    new Color2Name(makeColor(34, 139, 34), "ForestGreen"),
                    new Color2Name(makeColor(33, 33, 33), "gray13"),
                    new Color2Name(makeColor(33, 33, 33), "grey13"),
                    new Color2Name(makeColor(32, 178, 170), "LightSeaGreen"),
                    new Color2Name(makeColor(31, 31, 31), "gray12"),
                    new Color2Name(makeColor(31, 31, 31), "grey12"),
                    new Color2Name(makeColor(30, 144, 255), "DodgerBlue"),
                    new Color2Name(makeColor(30, 144, 255), "DodgerBlue1"),
                    new Color2Name(makeColor(28, 134, 238), "DodgerBlue2"),
                    new Color2Name(makeColor(28, 28, 28), "gray11"),
                    new Color2Name(makeColor(28, 28, 28), "grey11"),
                    new Color2Name(makeColor(26, 26, 26), "gray10"),
                    new Color2Name(makeColor(26, 26, 26), "grey10"),
                    new Color2Name(makeColor(25, 25, 112), "MidnightBlue"),
                    new Color2Name(makeColor(24, 116, 205), "DodgerBlue3"),
                    new Color2Name(makeColor(23, 23, 23), "gray9"),
                    new Color2Name(makeColor(23, 23, 23), "grey9"),
                    new Color2Name(makeColor(20, 20, 20), "gray8"),
                    new Color2Name(makeColor(20, 20, 20), "grey8"),
                    new Color2Name(makeColor(18, 18, 18), "gray7"),
                    new Color2Name(makeColor(18, 18, 18), "grey7"),
                    new Color2Name(makeColor(16, 78, 139), "DodgerBlue4"),
                    new Color2Name(makeColor(15, 15, 15), "gray6"),
                    new Color2Name(makeColor(15, 15, 15), "grey6"),
                    new Color2Name(makeColor(13, 13, 13), "gray5"),
                    new Color2Name(makeColor(13, 13, 13), "grey5"),
                    new Color2Name(makeColor(10, 10, 10), "gray4"),
                    new Color2Name(makeColor(10, 10, 10), "grey4"),
                    new Color2Name(makeColor(8, 8, 8), "gray3"),
                    new Color2Name(makeColor(8, 8, 8), "grey3"),
                    new Color2Name(makeColor(5, 5, 5), "gray2"),
                    new Color2Name(makeColor(5, 5, 5), "grey2"),
                    new Color2Name(makeColor(3, 3, 3), "gray1"),
                    new Color2Name(makeColor(3, 3, 3), "grey1"),
                    new Color2Name(makeColor(0, 0, 0), "black"),
                    new Color2Name(makeColor(0, 0, 128), "navy"),
                    new Color2Name(makeColor(0, 0, 128), "NavyBlue"),
                    new Color2Name(makeColor(0, 0, 205), "MediumBlue"),
                    new Color2Name(makeColor(0, 0, 255), "blue"),
                    new Color2Name(makeColor(0, 191, 255), "DeepSkyBlue"),
                    new Color2Name(makeColor(0, 206, 209), "DarkTurquoise"),
                    new Color2Name(makeColor(0, 255, 255), "cyan"),
                    new Color2Name(makeColor(0, 100, 0), "DarkGreen"),
                    new Color2Name(makeColor(0, 255, 127), "SpringGreen"),
                    new Color2Name(makeColor(0, 255, 0), "green"),
                    new Color2Name(makeColor(0, 250, 154), "MediumSpringGreen"),
                    new Color2Name(makeColor(0, 0, 255), "blue1"),
                    new Color2Name(makeColor(0, 0, 238), "blue2"),
                    new Color2Name(makeColor(0, 0, 205), "blue3"),
                    new Color2Name(makeColor(0, 0, 139), "blue4"),
                    new Color2Name(makeColor(0, 191, 255), "DeepSkyBlue1"),
                    new Color2Name(makeColor(0, 178, 238), "DeepSkyBlue2"),
                    new Color2Name(makeColor(0, 154, 205), "DeepSkyBlue3"),
                    new Color2Name(makeColor(0, 104, 139), "DeepSkyBlue4"),
                    new Color2Name(makeColor(0, 245, 255), "turquoise1"),
                    new Color2Name(makeColor(0, 229, 238), "turquoise2"),
                    new Color2Name(makeColor(0, 197, 205), "turquoise3"),
                    new Color2Name(makeColor(0, 134, 139), "turquoise4"),
                    new Color2Name(makeColor(0, 255, 255), "cyan1"),
                    new Color2Name(makeColor(0, 238, 238), "cyan2"),
                    new Color2Name(makeColor(0, 205, 205), "cyan3"),
                    new Color2Name(makeColor(0, 139, 139), "cyan4"),
                    new Color2Name(makeColor(0, 255, 127), "SpringGreen1"),
                    new Color2Name(makeColor(0, 238, 118), "SpringGreen2"),
                    new Color2Name(makeColor(0, 205, 102), "SpringGreen3"),
                    new Color2Name(makeColor(0, 139, 69), "SpringGreen4"),
                    new Color2Name(makeColor(0, 255, 0), "green1"),
                    new Color2Name(makeColor(0, 238, 0), "green2"),
                    new Color2Name(makeColor(0, 205, 0), "green3"),
                    new Color2Name(makeColor(0, 139, 0), "green4"),
                    new Color2Name(makeColor(0, 0, 0), "gray0"),
                    new Color2Name(makeColor(0, 0, 0), "grey0"),
                    new Color2Name(makeColor(0, 0, 139), "DarkBlue"),
                    new Color2Name(makeColor(0, 139, 139), "DarkCyan"),
                    // missing Java colors
                    new Color2Name(Color.BLACK, "black"),
                    new Color2Name(Color.BLUE, "blue"),
                    new Color2Name(Color.CYAN, "cyan"),
                    new Color2Name(Color.DARK_GRAY, "dark_gray"),
                    new Color2Name(Color.GRAY, "gray"),
                    new Color2Name(Color.GREEN, "green"),
                    new Color2Name(Color.LIGHT_GRAY, "light_grey")
                // </editor-fold>
    };
    private static HashMap<Color, String> xtermColors2Strings =
                                          initializeXtermColors();
    private static TreeMap<String, Color> xtermStrings2Colors =
                                          initializeXtermStrings();

    static final int MINCONTRAST = 150;
    static final int MEDIUMINTENSITY = (256 + 256 + 256) / 2;

    /**
     * Create a color with RGB values and by ensuring they are in legal range
     * [0..255].
     *
     * @param r red
     * @param g green
     * @param b blue
     * @return the color object
     */
    public static Color makeColor(int r, int g, int b)
    {
        r = (r < 0) ? 0 : r;
        r = (r > 255) ? 255 : r;
        g = (g < 0) ? 0 : g;
        g = (g > 255) ? 255 : g;
        b = (b < 0) ? 0 : b;
        b = (b > 255) ? 255 : b;
        return new Color(r, g, b);
    }

    /**
     * Create a color with RGB values and by ensuring they are in legal range
     * [0..255].
     *
     * @param r red
     * @param g green
     * @param b blue
     * @param a transparency
     * @return the color object
     */
    public static Color makeColor(float r, float g, float b, float a)
    {
        r = (r < 0.0F) ? 0.0F : r;
        r = (r > 1.0F) ? 1.0F : r;
        g = (g < 0.0F) ? 0.0F : g;
        g = (g > 1.0F) ? 1.0F : g;
        b = (b < 0.0F) ? 0.0F : b;
        b = (b > 1.0F) ? 255 : b;
        a = (a < 0.0F) ? 0.0F : a;
        a = (a > 1.0F) ? 255 : a;
        return new Color(r, g, b, a);
    }

    static int colorDifference(Color c1, Color c2)
    {
        if (c1 == null && c2 == null)
        {
            return 0;
        }
        else if (c1 == null || c2 == null)
        {
            return -1;
        }
        int r1 = c1.getRed();
        int g1 = c1.getGreen();
        int b1 = c1.getBlue();
        int r2 = c2.getRed();
        int g2 = c2.getGreen();
        int b2 = c2.getBlue();

        return abs(r1 - r2) + abs(g1 - g2) + abs(b1 - b2);
    }

    static int colorIntensity(Color c)
    {
        if (c == null)
        {
            return -1;
        }
        return c.getRed() + c.getGreen() + c.getBlue();
    }

    static int colorIntensityDifference(Color c1, Color c2)
    {
        if (c1 == null && c2 == null)
        {
            return 0;
        }
        else if (c1 == null || c2 == null)
        {
            return -1;
        }
        return abs(colorIntensity(c2) - colorIntensity(c1));
    }

    /**
     * Create a contrasting RGB color to a color by modifying the complement
     * color so that if used as foreground/background they are different enough
     * to be readable.
     *
     * @param color
     * @return the contrast color
     */
    public static Color contrastColorByComplement(Color color)
    {
        if (color == null)
        {
            color = Color.BLACK;
        }

        int r1 = color.getRed();
        int g1 = color.getGreen();
        int b1 = color.getBlue();
        int r2 = 255 - r1;
        int g2 = 255 - g1;
        int b2 = 255 - b1;
        Color reval = makeColor(r2, g2, b2);
        if (colorDifference(color, reval) < 150)
        {
            r2 = (r2 + (r2 < 128 ? -70 : 70)) % 256;
            g2 = (g2 + (g2 < 128 ? -70 : 70)) % 256;
            b2 = (b2 + (b2 < 128 ? -70 : 70)) % 256;
            reval = makeColor(r2, g2, b2);
        }
        int colIntensDiff = colorIntensityDifference(color, reval);
        if (colIntensDiff < MINCONTRAST)
        {
            int intensityToDistribute = MINCONTRAST - colIntensDiff;
            if (colorIntensity(color) < MEDIUMINTENSITY)
            {
                while (intensityToDistribute > 0)
                {
                    if (r2 < 255 && r2 > r1)
                    {
                        r2++;
                        intensityToDistribute--;
                    }
                    else if (g2 < 255 && g2 > g1)
                    {
                        g2++;
                        intensityToDistribute--;
                    }
                    else if (b2 < 255 && b2 > b1)
                    {
                        b2++;
                        intensityToDistribute--;
                    }
                    else
                    {
                        intensityToDistribute = 0;
                    }
                }
                reval = makeColor(r2, g2, b2);
            }
            else
            {
                while (intensityToDistribute > 0)
                {
                    if (r2 > 0 && r2 < r1)
                    {
                        r2--;
                        intensityToDistribute--;
                    }
                    else if (g2 > 0 && g2 < g1)
                    {
                        g2--;
                        intensityToDistribute--;
                    }
                    else if (b2 > 0 && b2 < b1)
                    {
                        b2--;
                        intensityToDistribute--;
                    }
                    else
                    {
                        intensityToDistribute = 0;
                    }
                }
                reval = makeColor(r2, g2, b2);
            }
        }
        return reval;
    }

    /**
     * Create a contrasting RGB color to a color by shifting the color (making
     * it brighter or darker) so that if used as foreground/background they are
     * different enough to be readable.
     *
     * @param color the color we want to create a contrast for
     * @param shift the shift value
     * @return the contrast color
     */
    public static Color contrastColorByShift(Color color, int shift)
    {
        if (color == null)
        {
            color = Color.BLACK;
        }

        if (shift < 1)
        {
            shift = 128;
        }

        int r1 = color.getRed();
        int g1 = color.getGreen();
        int b1 = color.getBlue();
        int r2 = (r1 + shift) % 256;
        int g2 = (g1 + shift) % 256;
        int b2 = (b1 + shift) % 256;

        Color reval = makeColor(r2, g2, b2);

        return reval;
    }

    /**
     * Create a contrasting RGB color to a color by creating a "GREY"
     * (RGB(c,c,c)) value so that if used as foreground/background they are
     * different enough (in brightness/intensity) to be readable.
     *
     * @param color the color to which to find a complement
     * @return the contrast color
     */
    public static Color contrastColorGrey(Color color)
    {
        if (color == null)
        {
            color = Color.BLACK;
        }

        int grey = ((colorIntensity(color) / 3) + 128) % 256;

        Color reval = makeColor(grey, grey, grey);

        return reval;
    }

    /**
     * Create a contrasting RGB color to a color by giving a hint of preferred
     * color, which will be adjusted, if necessary so that if used as
     * foreground/background they are different enough (in brightness/intensity)
     * to be readable.
     *
     * @param color     the color to which to find a complement
     * @param hintColor hint to create a color close to this
     * @return the contrast color
     */
    public static Color contrastColorByHint(Color color, Color hintColor)
    {
        if (color == null)
        {
            color = Color.BLACK;
        }

        if (hintColor == null)
        {
            hintColor = contrastColorByComplement(color);
        }

        Color reval = hintColor;
        int colIntensDiff = colorIntensityDifference(color, hintColor);
        if (colIntensDiff < MINCONTRAST)
        {
            int intensityToDistribute = MINCONTRAST - colIntensDiff;

            int r1 = color.getRed();
            int g1 = color.getGreen();
            int b1 = color.getBlue();
            int r2 = hintColor.getRed();
            int g2 = hintColor.getGreen();
            int b2 = hintColor.getBlue();

            while (intensityToDistribute > 0)
            {
                if (r1 < r2)
                {
                    if (r2 < 255)
                    {
                        r2++;
                        intensityToDistribute--;
                    }
                }
                else
                {
                    if (r2 > 0)
                    {
                        r2--;
                        intensityToDistribute--;
                    }

                }

                if (g1 < g2)
                {
                    if (g2 < 255)
                    {
                        g2++;
                        intensityToDistribute--;
                    }
                }
                else
                {
                    if (g2 > 0)
                    {
                        g2--;
                        intensityToDistribute--;
                    }

                }

                if (b1 < b2)
                {
                    if (b2 < 255)
                    {
                        b2++;
                        intensityToDistribute--;
                    }
                }
                else
                {
                    if (b2 > 0)
                    {
                        b2--;
                        intensityToDistribute--;
                    }

                }
            }
            reval = makeColor(r2, g2, b2);

        }

        return reval;
    }

    /**
     * Retrieve a hexadecimal string representation of the given Color.
     *
     * @param color the color we want a hex-string for
     * @return hexadecimal representation of the RGB values
     */
    static public String hexColor(Color color)
    {
        String redStr = Integer.toHexString(color.getRed());
        String greenStr = Integer.toHexString(color.getGreen());
        String blueStr = Integer.toHexString(color.getBlue());
        redStr = (redStr.length() < 2 ? "0" : "") + redStr;
        greenStr = (greenStr.length() < 2 ? "0" : "") + greenStr;
        blueStr = (blueStr.length() < 2 ? "0" : "") + blueStr;
        return redStr + greenStr + blueStr;
    }

    /**
     * Retrieve a Color for a name if the name is defined, black otherwise. The
     * search is case-insensitive. so "red", "RED" and "rED" return the same
     * color.
     *
     * @param name the color name
     * @return the xterm-named color if defined, black otherwise
     */
    static public Color getXtermColor(String name)
    {
        return xtermStrings2Colors.containsKey(name) ?
               xtermStrings2Colors.get(name) :
               makeColor(0, 0, 0);
    }

    /**
     * Create a string description of a color. Use the x-term symbolic name if
     * possible (not all colors have one!).
     *
     * @param color          the Color value to translate to string
     * @param includeRGB     include the Red Green Blue values if true
     * @param exactMatchOnly if true then only include the xterm name on exact
     *                       matches, otherwise use the closest match
     * @return closest xterm-name of a RGB-color
     */
    public static String xtermColorString(Color color,
                                          boolean includeRGB,
                                          boolean exactMatchOnly)
    {
        if (color == null)
        {
            return "black" + (includeRGB ? " RGB(0,0,0)" : "");
        }
        String bestMatch = "RGB(" +
                           color.getRed() +
                           "," +
                           color.getGreen() +
                           "," +
                           color.getBlue() +
                           ")";
        int minDiff = 1000000;
        int diff;
        for (Color c : xtermColors2Strings.keySet())
        {
            diff = colorDifference(c, color);
            if (diff == 0)
            {
                String rgb = " RGB(" +
                             c.getRed() +
                             "," +
                             c.getGreen() +
                             "," +
                             c.getBlue() +
                             ")";
                return xtermColors2Strings.get(c) + (includeRGB ? rgb : "");
            }
            else if (!exactMatchOnly && minDiff > diff)
            {
                minDiff = diff;
                String rgb = " RGB(" +
                             c.getRed() +
                             "," +
                             c.getGreen() +
                             "," +
                             c.getBlue() +
                             ")";
                bestMatch = xtermColors2Strings.get(c) + (includeRGB ? rgb : "");
            }
        }

        return bestMatch;
    }

    /**
     * Create the closest string for the closest X-Term color string without
     * RGB-values.
     *
     * @param color the color
     * @return the color name
     */
    public static String xtermColorString(Color color)
    {
        return xtermColorString(color, false, false);
    }

    /**
     * Create a map between Color-objects and xterm names.
     *
     * @return the generated map
     */
    public static HashMap<Color, String> initializeXtermColors()
    {
        xtermColors2Strings = new HashMap<>();
        for (Color2Name c2n : colName)
        {
            xtermColors2Strings.put(c2n.color, c2n.name);
        }
        return xtermColors2Strings;
    }

    /**
     * Create a map between xterm names and Color-objects.
     *
     * @return the generated map
     */
    public static TreeMap<String, Color> initializeXtermStrings()
    {
        xtermStrings2Colors = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        for (Color2Name c2n : colName)
        {
            xtermStrings2Colors.put(c2n.name, c2n.color);
        }
        return xtermStrings2Colors;
    }

    private static class Color2Name
    {

        Color color;
        String name;

        Color2Name(Color color, String name)
        {
            this.color = color;
            this.name = name;
        }
    }

    /**
     * A set of colors each with a set of contrast colors. Extends the
     * AbstractTableModel to be able to use in JTable.
     */
    public static class ContrastColorSet extends AbstractTableModel
    {

        static final String headers[] = NbBundle.getMessage(
                              CLAZZ,
                              "ColorUtils.ContrastColorSet.tableHeaders").split(
                                      ",");

        private Color color = Color.BLACK;
        private ArrayList<Contrast> contrastColors = new ArrayList<>();

        /**
         * Construct using a specific background color.
         *
         * @param color the color for which we need contrast(s)
         */
        public ContrastColorSet(Color color)
        {
            reset(color);
        }

        /**
         * Default construct.
         */
        public ContrastColorSet()
        {
            this(Color.BLACK);
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex)
        {
            return true;
        }

        @Override
        public String getColumnName(int column)
        {
            return headers.length > column && headers.length > -1 ?
                   headers[column] : "";
        }

        /**
         * Remove a row from the mode.
         *
         * @param row index of the row to remove
         */
        public void remove(int row)
        {
            if (row > -1 && row < contrastColors.size())
            {
                contrastColors.remove(row);
            }
            fireTableDataChanged();
        }

        /**
         * Remove several rows from the mode.
         *
         * @param rows indices of the rows to remove
         */
        public void remove(int[] rows)
        {
            for (int i = rows.length - 1; i > -1; i--)
            {
                contrastColors.remove(rows[i]);
            }
        }

        /**
         * Recalculate the contrasts using the new background color.
         *
         * @param color
         */
        public final void reset(Color color)
        {
            this.color = color;
            ArrayList<Contrast> newContrastColors = new ArrayList<>();
            for (Contrast contrast : contrastColors)
            {
                newContrastColors.add(contrast.modify(color));
            }
            contrastColors = newContrastColors;
            fireTableDataChanged();
        }

        /**
         * Retrieve the color.
         *
         * @return the color
         */
        public Color getColor()
        {
            return color;
        }

        private boolean append(Contrast contrast)
        {
            boolean reval = false;
            if (!contrastColors.contains(contrast))
            {
                contrastColors.add(contrast);
                reval = true;
            }
            fireTableDataChanged();
            return reval;
        }

        /**
         * Add the complement of the current color to the list of
         * contrast-colors.
         *
         * @return success
         */
        public final boolean addComplement()
        {
            return append(Contrast.makeComplement(color));
        }

        /**
         * Add the grey-contrast of the current color to the list of
         * contrast-colors.
         *
         * @return success
         */
        public final boolean addGreyContrast()
        {
            return append(Contrast.makeGrey(color));
        }

        /**
         * Add the shift contrast of the current color to the list of
         * contrast-colors.
         *
         * @param difference the shift values
         *
         * @return success
         */
        public final boolean addShiftContrast(int difference)
        {
            return append(Contrast.makeShift(color, difference));
        }

        /**
         * Add the hint contrast of the current color to the list of
         * contrast-colors.
         *
         * @param preferred the color we would like (hint only)
         *
         * @return success
         */
        public final boolean addHintContrast(Color preferred)
        {
            return append(Contrast.makeHint(color, preferred));
        }

        /**
         * Add the exact contrast of the current color to the list of
         * contrast-colors.
         *
         * @param exact use this exact color as contrast (user override)
         * @return success
         */
        public final boolean addExactColorContrast(Color exact)
        {
            return append(Contrast.makeExact(color, exact));
        }

        /**
         * Retrieve the i'th contrast color.
         *
         * @param index ordinal in the set of contrast colors
         * @return the contrast color
         */
        public Color getContrastColor(int index)
        {
            if (index >= contrastColors.size())
            {
                if (!contrastColors.isEmpty())
                {
                    int lastColorIndex = contrastColors.size() - 1;
                    return contrastColors.get(lastColorIndex).electedContrast;
                }
                else
                {
                    return Color.WHITE;
                }
            }
            if (index < 0)
            {
                if (!contrastColors.isEmpty())
                {
                    return contrastColors.get(0).electedContrast;
                }
                else
                {
                    return Color.WHITE;
                }
            }
            return contrastColors.get(index).electedContrast;
        }

        /**
         * Retrieve the (first) contrast color.
         *
         * @return the contrast
         */
        public Color getContrastColor()
        {
            return getContrastColor(0);
        }

        /**
         * Retrieve all contrast colors.
         *
         * @return an array of colors
         */
        public Color[] getContrastColors()
        {
            return (Color[]) contrastColors.toArray();
        }

        /**
         * Make a string describing the color and its i'th contrast color.
         *
         * @param i index of the set of contrasts
         * @return the string
         */
        public String toString(int i)
        {
            return xtermColorString(color, true, true) +
                   "fg(" +
                   xtermColorString(getContrastColor(i), true, true) +
                   ") ";
        }

        @Override
        public int getRowCount()
        {
            return contrastColors.size();
        }

        @Override
        public int getColumnCount()
        {
            return headers.length;
        }

        @Override
        public void setValueAt(Object aValue, int row, int col)
        {
            Contrast contrast = contrastColors.get(row);

            switch (col)
            {
                case 0:
                    contrast.type = (Contrast.Type) aValue;
                    break;
                case 1:
                    break;
                case 2:
                    contrast.hintColor = (Color) aValue;
                    break;
                case 3:
                    contrast.shift = Integer.parseInt(aValue.toString());
                    break;
            }
            contrastColors.set(row, contrast);
            reset(color);
        }

        @Override
        public Object getValueAt(int row, int col)
        {
            if (row < 0 ||
                col < 0 ||
                row > contrastColors.size() - 1 ||
                col > headers.length - 1)
            {
                return "";
            }
            Contrast contrast = contrastColors.get(row);
            return col == 0 ? contrast.type :
                   col == 1 ? toString(row) :
                   col == 2 ? (contrast.hintColor == null ? "" :
                               contrast.hintColor.toString()) :
                   col == 3 ? contrast.shift : "";
        }

        boolean up(int row)
        {
            if (row < 1)
            {
                return false;
            }
            Contrast tmp = contrastColors.get(row);
            contrastColors.set(row, contrastColors.get(row - 1));
            contrastColors.set(row - 1, tmp);
            fireTableDataChanged();

            return true;
        }

        boolean down(int row)
        {
            if (row > contrastColors.size() - 2)
            {
                return false;
            }
            Contrast tmp = contrastColors.get(row);
            contrastColors.set(row, contrastColors.get(row + 1));
            contrastColors.set(row + 1, tmp);
            fireTableDataChanged();

            return true;
        }

        void up(int[] rows)
        {
            for (int i = 0; i < rows.length; i++)
            {
                up(rows[i]);
            }
        }

        void down(int[] rows)
        {
            for (int i = rows.length - 1; i > -1; i--)
            {
                down(rows[i]);
            }
        }

        /**
         *
         */
        public static class Contrast implements Comparable<Contrast>
        {

            /**
             * Create a complement contrast color.
             *
             * @param bg background color
             * @return the contrast
             */
            public static Contrast makeComplement(Color bg)
            {
                return new Contrast(bg, Type.COMPLEMENT);
            }

            /**
             * Create a grey-value contrast color.
             *
             * @param bg background color
             * @return the contrast
             */
            public static Contrast makeGrey(Color bg)
            {
                return new Contrast(bg, Type.GREY);
            }

            /**
             * Create a shift contrast color.
             *
             * @param bg    background color
             * @param shift shift up or down by adding/subtracting this value
             *              from R,G and B
             * @return the contrast
             */
            public static Contrast makeShift(Color bg, Integer shift)
            {
                return new Contrast(bg, Type.SHIFT, shift);
            }

            /**
             * Create a shift contrast color.
             *
             * @param bg        background color
             * @param hintColor we want a color that is as similar as possible
             *                  to this hint-color with still having enough
             *                  contrast
             * @return the contrast
             */
            public static Contrast makeHint(Color bg, Color hintColor)
            {
                return new Contrast(bg, Type.HINT, hintColor);
            }

            /**
             * Create a shift contrast color.
             *
             * @param bg         background color
             * @param exactColor take this as the contrast
             * @return the contrast
             */
            public static Contrast makeExact(Color bg, Color exactColor)
            {
                return new Contrast(bg, Type.EXACT, exactColor);
            }
            Type type = null;
            Color electedContrast = null;
            Color hintColor = null;
            Integer shift = -1;

            private Contrast(Color bg,
                             Type type, Object... params)
            {
                this.type = type;
                if (type == Type.COMPLEMENT)
                {
                    electedContrast = contrastColorByComplement(bg);
                }
                else if (type == Type.GREY)
                {
                    electedContrast = contrastColorGrey(bg);
                }
                else if (type == Type.SHIFT)
                {
                    shift = params != null ? (int) params[0] : 128;
                    electedContrast = contrastColorByShift(bg, shift);
                }
                else if (type == Type.HINT || type == Type.EXACT)
                {
                    hintColor = (params == null) ?
                                Color.BLACK :
                                (Color) params[0];
                    if (type == Type.EXACT)
                    {
                        electedContrast = hintColor;

                    }
                    else if (type == Type.HINT)
                    {
                        electedContrast = contrastColorByHint(bg,
                                                              hintColor);

                    }
                }
            }

            @Override
            public String toString()
            {
                return "Contrast{" + "type=" + type +
                       ", electedContrast=" + electedContrast +
                       ", hintColor=" + hintColor +
                       ", shift=" + shift +
                       '}';
            }

            @Override
            public int hashCode()
            {
                int hash = 5;
                hash = 17 * hash + Objects.hashCode(this.type);
                hash = 17 * hash + Objects.hashCode(this.electedContrast);
                return hash;
            }

            @Override
            public boolean equals(Object obj)
            {
                if (obj == null)
                {
                    return false;
                }
                if (getClass() != obj.getClass())
                {
                    return false;
                }
                final Contrast other = (Contrast) obj;
                if (this.type != other.type)
                {
                    return false;
                }
                return Objects.equals(this.electedContrast,
                                      other.electedContrast);
            }

            @Override
            public int compareTo(Contrast o)
            {
                Color thisColor = electedContrast;
                Color otherColor = o.electedContrast;
                int rDiff = thisColor.getRed() - otherColor.getRed();
                int gDiff = thisColor.getGreen() - otherColor.getGreen();
                int bDiff = thisColor.getBlue() - otherColor.getBlue();
                int typeDiff = type.ordinal() - o.type.ordinal();

                return rDiff != 0 ? rDiff :
                       gDiff != 0 ? gDiff :
                       bDiff != 0 ? bDiff :
                       typeDiff;
            }

            private Contrast modify(Color newBg)
            {
                return type == Type.COMPLEMENT ? makeComplement(newBg) :
                       type == Type.GREY ? makeGrey(newBg) :
                       type == Type.SHIFT ? makeShift(newBg, shift) :
                       type == Type.GREY ? makeHint(newBg, hintColor) :
                       makeExact(newBg, hintColor);
            }

            /**
             * Type of how the contrast was obtained.
             */
            public static enum Type
            {

                /**
                 * Complements in terms of RGB values: each R, G and B is
                 * subtracted from 255 to create a complement. works well for
                 * colors where R, G, B are sufficiently different from 128.
                 */
                COMPLEMENT, /**
                 * Give a hint color and slowly change it until contrast is big
                 * enough.
                 */
                HINT, /**
                 * Add or deduct values from RGB values to achieve desired
                 * contrast.
                 */
                SHIFT, /**
                 * Get a grey value with big enough contrast.
                 */
                GREY, /**
                 * Force a user-defined contrast.
                 */
                EXACT
            }
        }
    }

    private static int fourCornerAverage(int topLeft,
                                         int topRight,
                                         int bottomLeft,
                                         int bottomRight,
                                         double ratio_w,
                                         double ratio_h)
    {
        double top = topLeft * ratio_w + topRight * (1.0 - ratio_w);
        double bottom = bottomLeft * ratio_w + bottomRight * (1.0 - ratio_w);
        return (int) (top * ratio_h + bottom * (1.0 - ratio_h));
    }

    /**
     * A 4-colour gradient class.
     */
    public static class FourColorGradientPaint implements Paint
    {

        Point2D topLeft;
        Point2D bottomRight;
        Color topLeftColor;
        Color topRightColor;
        Color bottomLeftColor;
        Color bottomRightColor;
        int colorDistanceX;
        int colorDistanceY;
        MultipleGradientPaint.CycleMethod cycleMethod =
                                          MultipleGradientPaint.CycleMethod.REPEAT;

        /**
         * Constructor taking x- and y- coordinates of bottom right corner of a
         * rectangle and colors for each corner. The top-left corner is assumed
         * to be (0,0).
         *
         * @param x                x-coordinate of bottom right corner
         * @param y                y-coordinate of bottom right corner
         * @param topLeftColor     color in the top left corner of the rectangle
         * @param topRightColor    color in the top right corner of the
         *                         rectangle
         * @param bottomLeftColor  color in the bottom left corner of the
         *                         rectangle
         * @param bottomRightColor color in the bottom right corner of the
         *                         rectangle
         */
        public FourColorGradientPaint(int x,
                                      int y,
                                      Color topLeftColor,
                                      Color topRightColor,
                                      Color bottomLeftColor,
                                      Color bottomRightColor)
        {
            this(new Point(x, y),
                 topLeftColor,
                 topRightColor,
                 bottomLeftColor,
                 bottomRightColor);
        }

        /**
         * Constructor taking x- and y- coordinates of bottom right corner of a
         * rectangle and colors for each corner. The top-left corner is assumed
         * to be (0,0).
         *
         * @param x                x-coordinate of bottom right corner
         * @param y                y-coordinate of bottom right corner
         * @param topLeftColor     color in the top left corner of the rectangle
         * @param topRightColor    color in the top right corner of the
         *                         rectangle
         * @param bottomLeftColor  color in the bottom left corner of the
         *                         rectangle
         * @param bottomRightColor color in the bottom right corner of the
         *                         rectangle
         * @param cycleMethod      how to extend the gradient outside the
         *                         defining rectangle
         */
        public FourColorGradientPaint(int x,
                                      int y,
                                      Color topLeftColor,
                                      Color topRightColor,
                                      Color bottomLeftColor,
                                      Color bottomRightColor,
                                      MultipleGradientPaint.CycleMethod cycleMethod)
        {
            this(new Point(x, y),
                 topLeftColor,
                 topRightColor,
                 bottomLeftColor,
                 bottomRightColor,
                 cycleMethod);
        }

        /**
         * Constructor taking x- and y- coordinates for top left and bottom
         * right corner of a rectangle and colors for each corner.
         *
         * @param x1               x-coordinate of top left corner
         * @param y1               y-coordinate of top left corner
         * @param x2               x-coordinate of bottom right corner
         * @param y2               y-coordinate of bottom right corner
         * @param topLeftColor     color in the top left corner of the rectangle
         * @param topRightColor    color in the top right corner of the
         *                         rectangle
         * @param bottomLeftColor  color in the bottom left corner of the
         *                         rectangle
         * @param bottomRightColor color in the bottom right corner of the
         *                         rectangle
         */
        public FourColorGradientPaint(int x1,
                                      int y1,
                                      int x2,
                                      int y2,
                                      Color topLeftColor,
                                      Color topRightColor,
                                      Color bottomLeftColor,
                                      Color bottomRightColor)
        {
            this(new Point(x1, y1),
                 new Point(x2, y2),
                 topLeftColor,
                 topRightColor,
                 bottomLeftColor,
                 bottomRightColor,
                 MultipleGradientPaint.CycleMethod.NO_CYCLE);
        }

        /**
         * Constructor taking x- and y- coordinates for top left and bottom
         * right corner of a rectangle and colors for each corner.
         *
         * @param x1               x-coordinate of top left corner
         * @param y1               y-coordinate of top left corner
         * @param x2               x-coordinate of bottom right corner
         * @param y2               y-coordinate of bottom right corner
         * @param topLeftColor     color in the top left corner of the rectangle
         * @param topRightColor    color in the top right corner of the
         *                         rectangle
         * @param bottomLeftColor  color in the bottom left corner of the
         *                         rectangle
         * @param bottomRightColor color in the bottom right corner of the
         *                         rectangle
         * @param cycleMethod      how to extend the gradient outside the
         *                         defining rectangle
         */
        public FourColorGradientPaint(int x1,
                                      int y1,
                                      int x2,
                                      int y2,
                                      Color topLeftColor,
                                      Color topRightColor,
                                      Color bottomLeftColor,
                                      Color bottomRightColor,
                                      MultipleGradientPaint.CycleMethod cycleMethod)
        {
            this(new Point(x1, y1),
                 new Point(x2, y2),
                 topLeftColor,
                 topRightColor,
                 bottomLeftColor,
                 bottomRightColor,
                 cycleMethod);
        }

        /**
         * Constructor taking the bottom right corner point of a rectangle and
         * colors for each corner. The top-left corner is assumed to be (0,0).
         *
         * @param point            bottom right corner point
         * @param topLeftColor     color in the top left corner of the rectangle
         * @param topRightColor    color in the top right corner of the
         *                         rectangle
         * @param bottomLeftColor  color in the bottom left corner of the
         *                         rectangle
         * @param bottomRightColor color in the bottom right corner of the
         *                         rectangle
         */
        public FourColorGradientPaint(Point2D point,
                                      Color topLeftColor,
                                      Color topRightColor,
                                      Color bottomLeftColor,
                                      Color bottomRightColor)
        {
            this(new Point(0, 0),
                 new Point((int) point.getX(), (int) point.getY()),
                 topLeftColor,
                 topRightColor,
                 bottomLeftColor,
                 bottomRightColor,
                 MultipleGradientPaint.CycleMethod.NO_CYCLE
            );
        }

        /**
         * Constructor taking the bottom right corner point of a rectangle and
         * colors for each corner. The top-left corner is assumed to be (0,0).
         *
         * @param point            bottom right corner point
         * @param topLeftColor     color in the top left corner of the rectangle
         * @param topRightColor    color in the top right corner of the
         *                         rectangle
         * @param bottomLeftColor  color in the bottom left corner of the
         *                         rectangle
         * @param bottomRightColor color in the bottom right corner of the
         *                         rectangle
         * @param cycleMethod      how to extend the gradient outside the
         *                         defining rectangle
         */
        public FourColorGradientPaint(Point2D point,
                                      Color topLeftColor,
                                      Color topRightColor,
                                      Color bottomLeftColor,
                                      Color bottomRightColor,
                                      MultipleGradientPaint.CycleMethod cycleMethod)
        {
            this(new Point(0, 0),
                 new Point((int) point.getX(), (int) point.getY()),
                 topLeftColor,
                 topRightColor,
                 bottomLeftColor,
                 bottomRightColor,
                 MultipleGradientPaint.CycleMethod.NO_CYCLE
            );
        }

        /**
         * Constructor taking the top left and bottom right corner points of a
         * rectangle and colors for each corner. The top-left corner is assumed
         * to be (0,0).
         *
         * @param topLeft          top left corner point
         * @param bottomRight      bottom right corner point
         * @param topLeftColor     color in the top left corner of the rectangle
         * @param topRightColor    color in the top right corner of the
         *                         rectangle
         * @param bottomLeftColor  color in the bottom left corner of the
         *                         rectangle
         * @param bottomRightColor color in the bottom right corner of the
         *                         rectangle
         */
        public FourColorGradientPaint(Point2D topLeft,
                                      Point2D bottomRight,
                                      Color topLeftColor,
                                      Color topRightColor,
                                      Color bottomLeftColor,
                                      Color bottomRightColor)
        {
            this(topLeft,
                 bottomRight,
                 topLeftColor,
                 topRightColor,
                 bottomLeftColor,
                 bottomRightColor,
                 MultipleGradientPaint.CycleMethod.NO_CYCLE
            );
        }

        /**
         * Constructor taking the top left and bottom right corner points of a
         * rectangle and colors for each corner. The top-left corner is assumed
         * to be (0,0).
         *
         * @param topLeft          top left corner point
         * @param bottomRight      bottom right corner point
         * @param topLeftColor     color in the top left corner of the rectangle
         * @param topRightColor    color in the top right corner of the
         *                         rectangle
         * @param bottomLeftColor  color in the bottom left corner of the
         *                         rectangle
         * @param bottomRightColor color in the bottom right corner of the
         *                         rectangle
         * @param cycleMethod      how to extend the gradient outside the
         *                         defining rectangle
         */
        public FourColorGradientPaint(Point2D topLeft,
                                      Point2D bottomRight,
                                      Color topLeftColor,
                                      Color topRightColor,
                                      Color bottomLeftColor,
                                      Color bottomRightColor,
                                      MultipleGradientPaint.CycleMethod cycleMethod)
        {
            if (topLeft == null ||
                bottomRight == null ||
                topLeftColor == null ||
                topRightColor == null ||
                bottomLeftColor == null ||
                bottomRightColor == null ||
                topLeft.equals(bottomRight))
            {
                throw new IllegalArgumentException(
                        NbBundle.getMessage(
                                CLAZZ,
                                "ColorUtils.FourColorGradientPaint.illegalArgument"));
            }
            int minX = min((int) topLeft.getX(), (int) bottomRight.getX());
            int maxX = max((int) topLeft.getX(), (int) bottomRight.getX());
            int minY = min((int) topLeft.getY(), (int) bottomRight.getY());
            int maxY = max((int) topLeft.getY(), (int) bottomRight.getY());
            this.topLeft = new Point(minX, minY);
            this.bottomRight = new Point(maxX, maxY);
            this.topLeftColor = topLeftColor;
            this.topRightColor = topRightColor;
            this.bottomLeftColor = bottomLeftColor;
            this.bottomRightColor = bottomRightColor;
            this.colorDistanceX = maxX - minX;
            this.colorDistanceY = maxY - minY;
            this.cycleMethod = cycleMethod == null ?
                               MultipleGradientPaint.CycleMethod.NO_CYCLE :
                               cycleMethod;
        }

        @Override
        public PaintContext createContext(ColorModel cm,
                                          Rectangle deviceBounds,
                                          Rectangle2D userBounds,
                                          AffineTransform xform,
                                          RenderingHints hints)
        {
            Point2D xFormedTopLeft = xform.transform(topLeft, null);
            Point2D xFormedBottomRight = xform.transform(bottomRight, null);
            return new FourColorGradientPaintContext(xFormedTopLeft,
                                                     xFormedBottomRight);
        }

        @Override
        public int getTransparency()
        {
            int a1 = topLeftColor.getAlpha();
            int a2 = topRightColor.getAlpha();
            int a3 = bottomLeftColor.getAlpha();
            int a4 = bottomRightColor.getAlpha();
            return (((a1 & a2 & a3 & a4) == 0xff) ? OPAQUE : TRANSLUCENT);
        }

        /**
         * Find a color that uses the defining member-colors and averages them
         * using x- and y- ratios.
         *
         *
         * @param topLeftColor     color in the top left corner of the rectangle
         * @param topRightColor    color in the top right corner of the
         *                         rectangle
         * @param bottomLeftColor  color in the bottom left corner of the
         *                         rectangle
         * @param bottomRightColor color in the bottom right corner of the
         *                         rectangle
         * @param ratio_x          ratio in x- direction
         * @param ratio_y          ratio in y-direction
         * @param cycleMethod
         * @return
         */
        public static Color ratioAvgColor(
                Color topLeftColor,
                Color topRightColor,
                Color bottomLeftColor,
                Color bottomRightColor,
                final double ratio_x,
                final double ratio_y,
                MultipleGradientPaint.CycleMethod cycleMethod)
        {
            // create local copies of the parameters. We cannot change the
            // final parameters.
            Color topLeftColorLoc = topLeftColor;
            Color topRightColorLoc = topRightColor;
            Color bottomLeftColorLoc = bottomLeftColor;
            Color bottomRightColorLoc = bottomRightColor;
            double ratio_xLoc = ratio_x;
            double ratio_yLoc = ratio_y;

            // if any ratio is outside [0.0, 1.0] use the ratios to determine
            // how to extend beyond the corners of the original rectangle
            if (ratio_y < 0.0 ||
                ratio_y > 1.0 ||
                ratio_x < 0.0 ||
                ratio_x > 1.0)
            {
                switch (cycleMethod)
                {
                    case NO_CYCLE:
                        // X|_|_
                        // _|_|_
                        //  | |
                        if (ratio_x < 0.0 && ratio_y < 0.0)
                        {
                            topLeftColorLoc = bottomRightColorLoc;
                            topRightColorLoc = bottomRightColorLoc;
                            bottomLeftColorLoc = bottomRightColorLoc;
                            ratio_xLoc = 1.0;
                            ratio_yLoc = 1.0;
                        }
                        // _|_|_
                        // X|_|_
                        //  | |
                        else if (ratio_x < 0.0 &&
                                 ratio_y >= 0.0 &&
                                 ratio_y <= 1.0)
                        {
                            topLeftColorLoc = topRightColor;
                            bottomLeftColorLoc = bottomRightColor;
                            ratio_xLoc = 1.0;
                        }
                        // _|_|_
                        // _|_|_
                        // X| |
                        if (ratio_y > 1.0 && ratio_x < 0.0)
                        {
                            topLeftColorLoc = topRightColor;
                            bottomLeftColorLoc = topRightColor;
                            bottomRightColorLoc = topRightColor;
                            ratio_yLoc = 1.0;
                            ratio_xLoc = 1.0;
                        }
                        // _|X|_
                        // _|_|_
                        //  | |
                        else if (ratio_y < 0.0 &&
                                 ratio_x >= 0.0 &&
                                 ratio_x <= 1.0)
                        {
                            topLeftColorLoc = bottomLeftColor;
                            topRightColorLoc = bottomRightColor;
                            ratio_yLoc = 1.0;
                        }
                        // _|_|_
                        // _|_|_
                        //  |X|
                        else if (ratio_y > 1.0 &&
                                 ratio_x >= 0.0 &&
                                 ratio_x <= 1.0)
                        {
                            bottomLeftColorLoc = topLeftColor;
                            bottomRightColorLoc = topRightColor;
                            ratio_yLoc = 1.0;
                        }
                        // _|_|X
                        // _|_|_
                        //  | |
                        else if (ratio_y < 0.0 && ratio_x > 1.0)
                        {
                            topLeftColorLoc = bottomLeftColor;
                            topRightColorLoc = bottomLeftColor;
                            bottomRightColorLoc = bottomLeftColor;
                            ratio_yLoc = 1.0;
                            ratio_xLoc = 1.0;
                        }
                        // _|_|_

                        // _|_|X
                        //  | |
                        else if (ratio_x > 1.0 &&
                                 ratio_y >= 0.0 &&
                                 ratio_y <= 1.0)
                        {
                            topRightColorLoc = topLeftColor;
                            bottomRightColorLoc = bottomLeftColor;
                            ratio_yLoc = 1.0;
                            ratio_xLoc = 1.0;
                        }
                        // _|_|_
                        // _|_|_
                        //  | |X
                        else if (ratio_y > 1.0 && ratio_x > 1.0)
                        {
                            topRightColorLoc = topLeftColor;
                            bottomLeftColorLoc = topLeftColor;
                            bottomRightColorLoc = topLeftColor;
                            ratio_yLoc = 1.0;
                            ratio_xLoc = 1.0;
                        }
                        break;
                    case REPEAT:

                        if (ratio_x < 0.0)
                        {
                            ratio_xLoc = 1.0 + (ratio_x - (int) ratio_x);
                        }
                        else if (ratio_x > 1.0)
                        {
                            ratio_xLoc = ratio_x - (int) ratio_x;
                        }
                        if (ratio_y < 0.0)
                        {
                            ratio_xLoc = 1.0 + (ratio_y - (int) ratio_y);
                        }
                        else if (ratio_y > 1.0)
                        {
                            ratio_xLoc = (ratio_y - (int) ratio_y);
                        }
                        break;
                    case REFLECT:
                        if (ratio_x < 0.0)
                        {
                            topRightColorLoc = topLeftColor;
                            bottomRightColorLoc = bottomLeftColor;
                            ratio_xLoc = 1.0;
                        }
                        else if (ratio_x > 1.0)
                        {
                            topLeftColorLoc = topRightColor;
                            bottomLeftColorLoc = bottomRightColor;
                            ratio_xLoc = 1.0;
                        }
                        if (ratio_y < 0.0)
                        {
                            bottomLeftColorLoc = topLeftColor;
                            bottomRightColorLoc = topRightColor;
                            ratio_xLoc = 1.0;
                        }
                        else if (ratio_y > 1.0)
                        {
                            topLeftColorLoc = bottomLeftColor;
                            topRightColorLoc = bottomRightColor;
                            ratio_xLoc = 1.0;
                        }
                        break;
                }
            }

            float r = fourCornerAverage(topLeftColorLoc.getRed(),
                                        topRightColorLoc.getRed(),
                                        bottomLeftColorLoc.getRed(),
                                        bottomRightColorLoc.getRed(),
                                        ratio_xLoc,
                                        ratio_yLoc) / 255.0F;
            float g = fourCornerAverage(topLeftColorLoc.getGreen(),
                                        topRightColorLoc.getGreen(),
                                        bottomLeftColorLoc.getGreen(),
                                        bottomRightColorLoc.getGreen(),
                                        ratio_xLoc,
                                        ratio_yLoc) / 255.0F;
            float b = fourCornerAverage(topLeftColorLoc.getBlue(),
                                        topRightColorLoc.getBlue(),
                                        bottomLeftColorLoc.getBlue(),
                                        bottomRightColorLoc.getBlue(),
                                        ratio_xLoc,
                                        ratio_yLoc) / 255.0F;
            float a = fourCornerAverage(topLeftColorLoc.getAlpha(),
                                        topRightColorLoc.getAlpha(),
                                        bottomLeftColorLoc.getAlpha(),
                                        bottomRightColorLoc.getAlpha(),
                                        ratio_xLoc,
                                        ratio_yLoc) / 255.0F;
            r = (r < 0.0F) ? 0.0F : r > 1.0F ? 1.0F : r;
            g = (g < 0.0F) ? 0.0F : g > 1.0F ? 1.0F : g;
            b = (b < 0.0F) ? 0.0F : b > 1.0F ? 1.0F : b;
            a = (a < 0.0F) ? 0.0F : a > 1.0F ? 1.0F : a;

            return new Color(r, g, b, a);
        }

        /**
         * A 4-color gradient context class.
         */
        public class FourColorGradientPaintContext
                implements PaintContext
        {

            Point2D topLeft;
            Point2D bottomRight;

            /**
             * Constructor taking the top left and bottom right corner of the
             * defining rectangle in device coordinates.
             *
             * @param topLeft     top left corner point in device coordinates
             * @param bottomRight bottom right corner point in device
             *                    coordinates
             */
            public FourColorGradientPaintContext(Point2D topLeft,
                                                 Point2D bottomRight)
            {
                this.topLeft = topLeft;
                this.bottomRight = bottomRight;
            }

            @Override
            public void dispose()
            {
            }

            @Override
            public ColorModel getColorModel()
            {
                return ColorModel.getRGBdefault();
            }

            private Color ratioAvgColor(final double ratio_x,
                                        final double ratio_y)
            {
                return ColorUtils.FourColorGradientPaint.ratioAvgColor(
                        topLeftColor,
                        topRightColor,
                        bottomLeftColor,
                        bottomRightColor,
                        ratio_x,
                        ratio_y,
                        cycleMethod);
            }

            @Override
            public Raster getRaster(int x, int y, int w, int h)
            {
                WritableRaster raster =
                               getColorModel().createCompatibleWritableRaster(w,
                                                                              h);
                // get the colors of the four corners of this raster, which might be
                // a sub-grid of topLeft - bottomRight or (partly/fully) outside of
                // the given corner-points
                double ratio_w_l = 1.0 -
                                   (double) (x - topLeft.getX()) /
                                   (double) colorDistanceX;
                double ratio_w_r = 1.0 - (double) (x + w - topLeft.
                                                   getX()) /
                                         (double) colorDistanceX;
                double ratio_h_t = 1.0 -
                                   (double) (y - topLeft.getY()) /
                                   (double) colorDistanceY;
                double ratio_h_b = 1.0 - (double) (y + h - topLeft.
                                                   getY()) /
                                         (double) colorDistanceY;
                Color tl = ratioAvgColor(ratio_w_l, ratio_h_t);
                Color tr = ratioAvgColor(ratio_w_r, ratio_h_t);
                Color bl = ratioAvgColor(ratio_w_l, ratio_h_b);
                Color br = ratioAvgColor(ratio_w_r, ratio_h_b);

                int[] data = new int[w * h * 4];

                // calculate the step-size for each channel in h)eight direction
                double step_R_h_l =
                       (double) (bl.getRed() - tl.getRed()) / (double) h;
                double step_G_h_l =
                       (double) (bl.getGreen() - tl.getGreen()) / (double) h;
                double step_B_h_l =
                       (double) (bl.getBlue() - tl.getBlue()) / (double) h;
                double step_A_h_l =
                       (double) (bl.getAlpha() - tl.getAlpha()) / (double) h;
                double step_R_h_r =
                       (double) (br.getRed() - tr.getRed()) / (double) h;
                double step_G_h_r =
                       (double) (br.getGreen() - tr.getGreen()) / (double) h;
                double step_B_h_r =
                       (double) (br.getBlue() - tr.getBlue()) / (double) h;
                double step_A_h_r =
                       (double) (br.getAlpha() - tr.getAlpha()) / (double) h;

                for (int j = 0; j < h; j++)
                {
                    // set the channel values at the left pixel of the line
                    double curR = (double) tl.getRed() + j * step_R_h_l;
                    double curG = (double) tl.getGreen() + j * step_G_h_l;
                    double curB = (double) tl.getBlue() + j * step_B_h_l;
                    double curA = (double) tl.getAlpha() + j * step_A_h_l;

                    // calculate the stepsize for each channel in w)idth direction
                    double step_R_w =
                           ((double) tr.getRed() + j * step_R_h_r - curR) /
                           (double) w;
                    double step_G_w =
                           ((double) tr.getGreen() + j * step_G_h_r - curG) /
                           (double) w;
                    double step_B_w =
                           ((double) tr.getBlue() + j * step_B_h_r - curB) /
                           (double) w;
                    double step_A_w =
                           ((double) tr.getAlpha() + j * step_A_h_r - curA) /
                           (double) w;

                    for (int i = 0; i < w; i++)
                    {

                        int base = (j * w + i) * 4;

                        data[base + 0] = (int) curR;
                        data[base + 1] = (int) curG;
                        data[base + 2] = (int) curB;
                        data[base + 3] = (int) curA;
                        // increase each channel value by its step-size in
                        // width direction
                        curR += step_R_w;
                        curG += step_G_w;
                        curB += step_B_w;
                        curA += step_A_w;
                    }
                }
                raster.setPixels(0, 0, w, h, data);

                return raster;
            }

        }

    }

    private static ExtRandom random = new ExtRandom();

    /**
     * Seed the Number generator for random colors.
     *
     * @param seed
     */
    public static void seedRandom(long seed)
    {
        random.setSeed(seed);
    }

    public static void unSeedRandom()
    {
        random = new ExtRandom();
    }

    public static Color randomColor()
    {
        return makeColor(random.nextInt(0, 256),
                         random.nextInt(0, 256),
                         random.nextInt(0, 256));
    }

    public static Color randomColor(float rMin,
                                    float rMax,
                                    float gMin,
                                    float gMax,
                                    float bMin,
                                    float bMax,
                                    float aMin,
                                    float aMax)
    {
        return makeColor(random.nextFloat(rMin, rMax),
                         random.nextFloat(gMin, gMax),
                         random.nextFloat(bMin, bMax),
                         random.nextFloat(aMin, aMax));
    }

    public static Color randomColor(float rMax,
                                    float gMax,
                                    float bMax,
                                    float aMax)
    {
        return randomColor(0.0F, rMax, 0.0F, gMax, 0.0F, bMax, 0.0F, aMax);

    }

    public static class RandomGridGradientPaint implements Paint
    {

        Point2D topLeft;
        Point2D bottomRight;
        int distanceX;
        int distanceY;
        int numColors;

        /**
         *
         * @param topLeft
         * @param bottomRight
         * @param distanceX
         * @param distanceY
         * @param numColors
         */
        public RandomGridGradientPaint(Point topLeft,
                                       Point bottomRight,
                                       int distanceX,
                                       int distanceY,
                                       int numColors)
        {
            if (topLeft == null ||
                bottomRight == null ||
                topLeft.getX() > bottomRight.getX() ||
                topLeft.getY() > bottomRight.getY() ||
                distanceX < 1 ||
                distanceY < 1 ||
                numColors < 1)
            {
                throw new IllegalArgumentException(
                        NbBundle.getMessage(
                                CLAZZ,
                                "ColorUtils.RandomGridGradientPaint.illegalArgument"));
            }

            this.distanceX = distanceX;
            this.distanceY = distanceY;
            this.topLeft = topLeft;
            this.bottomRight = bottomRight;
            this.numColors = numColors;

        }

        @Override
        public PaintContext createContext(ColorModel cm,
                                          Rectangle deviceBounds,
                                          Rectangle2D userBounds,
                                          AffineTransform xform,
                                          RenderingHints hints)
        {
            Point2D xFormedTopLeft = xform.transform(topLeft, null);
            Point2D xFormedBottomRight = xform.transform(bottomRight, null);
            return new RandomGridGradientPaintContext(
                    xFormedTopLeft,
                    xFormedBottomRight,
                    distanceX,
                    distanceY,
                    numColors);
        }

        @Override
        public int getTransparency()
        {
            return TRANSLUCENT;
        }

    }

    /**
     * A 4-color gradient context class.
     */
    public static class RandomGridGradientPaintContext implements
            PaintContext
    {

        Point2D topLeft;
        Point2D bottomRight;
        int distanceX;
        int distanceY;
        Color[][] colors;

        private RandomGridGradientPaintContext(Point2D topLeft,
                                               Point2D bottomRight,
                                               int distanceX,
                                               int distanceY,
                                               int numColors)
        {
            this.distanceX = distanceX;
            this.distanceY = distanceY;
            this.topLeft = topLeft;
            this.bottomRight = bottomRight;

            int colorsX = (int) ((bottomRight.getX() - topLeft.getX()) /
                                 distanceX + 1);
            int colorsY = (int) ((bottomRight.getY() - topLeft.getY()) /
                                 distanceY + 1);
            colors = new Color[colorsX][colorsY];

            Color[] colorArr = new Color[numColors];

            for (int colorIndex = 0; colorIndex < numColors; colorIndex++)
            {
                colorArr[colorIndex] = randomColor();
            }
            for (int y = 0; y < colorsY; y++)
            {
                for (int x = 0; x < colorsX; x++)
                {

                    int i = (y * colorsX) + x;
                    colors[x][y] = (i % 7 == 0 ? Color.white :
                                    i % 7 == 1 ? Color.red :
                                    i % 7 == 2 ? Color.yellow :
                                    i % 7 == 3 ? Color.cyan :
                                    i % 7 == 4 ? Color.blue :
                                    i % 7 == 5 ? Color.green :
                                    Color.black);
                    System.out.print(
                            ColorUtils.xtermColorString(colors[x][y]) +
                            "(" + x + "," + y + ")\t");
                }
                System.out.println("");
            }
        }

        @Override
        public void dispose()
        {
        }

        @Override
        public ColorModel getColorModel()
        {
            return ColorModel.getRGBdefault();
        }

        @Override
        public Raster getRaster(int x, int y, int w, int h)
        {
            WritableRaster raster =
                           getColorModel().createCompatibleWritableRaster(w,
                                                                          h);

            int[] data = new int[w * h * 4];

            int colorIndexX;
            int colorIndexY;
            Color topLeftColor;
            Color topRightColor;
            Color bottomLeftColor;
            Color bottomRightColor;

            for (int hi = 0; hi < h; hi++)
            {
                for (int wi = 0; wi < w; wi++)
                {
                    colorIndexX = (int) ((x + wi - topLeft.getX()) /
                                         distanceX);
                    colorIndexY =
                    (int) ((y + hi - topLeft.getY()) / distanceY);
                    int leftBorderX = (int) (topLeft.getX() + colorIndexX *
                                                              distanceX);
                    int topBorderY = (int) (topLeft.getY() + colorIndexY *
                                                             distanceY);
                    double ratio_w = (double) (x + wi - leftBorderX) /
                                     (double) distanceX;
                    double ratio_h = (double) (y + hi - topBorderY) /
                                     (double) distanceY;

                    topLeftColor = getTopLeftColor(colorIndexX, colorIndexY);
                    topRightColor = getTopRightColor(hi, wi);
                    bottomLeftColor = getBottomLeftColor(hi, wi);
                    bottomRightColor = getBottomRightColor(hi, wi);
                    if (wi % distanceX == 0 && hi % distanceY == 0)
                    {
                        printColors(colorIndexX,
                                    colorIndexY,
                                    topLeftColor,
                                    topRightColor,
                                    bottomLeftColor,
                                    bottomRightColor);
                    }
                    if (ratio_w > 1.0)
                    {
                        ratio_w = 1.0;
                    }
                    if (ratio_h > 1.0)
                    {
                        ratio_h = 1.0;
                    }

                    int base = (hi * w + wi) * 4;

                    data[base + 0] =
                    fourCornerAverage(topLeftColor.getRed(),
                                      topRightColor.getRed(),
                                      bottomLeftColor.getRed(),
                                      bottomRightColor.getRed(),
                                      ratio_w,
                                      ratio_h);
                    data[base + 1] = fourCornerAverage(topLeftColor.
                    getGreen(),
                                                       topRightColor.
                                                       getGreen(),
                                                       bottomLeftColor.
                                                       getGreen(),
                                                       bottomRightColor.
                                                       getGreen(),
                                                       ratio_w,
                                                       ratio_h);
                    data[base + 2] = fourCornerAverage(topLeftColor.
                    getBlue(),
                                                       topRightColor.
                                                       getBlue(),
                                                       bottomLeftColor.
                                                       getBlue(),
                                                       bottomRightColor.
                                                       getBlue(),
                                                       ratio_w,
                                                       ratio_h);
                    data[base + 3] = fourCornerAverage(topLeftColor.
                    getAlpha(),
                                                       topRightColor.
                                                       getAlpha(),
                                                       bottomLeftColor.
                                                       getAlpha(),
                                                       bottomRightColor.
                                                       getAlpha(),
                                                       ratio_w,
                                                       ratio_h);
                }
            }
            raster.setPixels(0, 0, w, h, data);

            return raster;
        }

        private void printColors(int colorIndexX,
                                 int colorIndexY,
                                 Color topLeft,
                                 Color topRight,
                                 Color bottomLeft,
                                 Color bottomRight)
        {

            System.out.println(
                    "colorIndex=(" + colorIndexX + "," + colorIndexY + ")\n" +
                    ColorUtils.xtermColorString(topLeft) + "\t" +
                    ColorUtils.xtermColorString(topRight) + "\n" +
                    ColorUtils.xtermColorString(bottomLeft) + "\t" +
                    ColorUtils.xtermColorString(bottomRight) + "\n"
            );
        }

        private Color getTopLeftColor(int x, int y)
        {
            return colors[x % colors.length][y % colors[0].length];
        }

        private Color getTopRightColor(int x, int y)
        {
            return colors[(x + 1) % colors.length][y % colors[0].length];
        }

        private Color getBottomLeftColor(int x, int y)
        {
            return colors[x % colors.length][(y + 1) % colors[0].length];
        }

        private Color getBottomRightColor(int x, int y)
        {
            return colors[(x + 1) % colors.length][(y + 1) %
                                                   colors[0].length];
        }
    }

}
