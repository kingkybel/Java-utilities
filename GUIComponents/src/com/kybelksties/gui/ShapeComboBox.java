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

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import org.openide.util.NbBundle;

/**
 * A combo-box extension specialised for some basic geometric shapes.
 *
 * @author kybelksd
 */
public class ShapeComboBox extends JComboBox
{
    
    private static final Class CLAZZ = ShapeComboBox.class;
    private static final String CLASS_NAME = CLAZZ.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    HashMap<String, ShapeIcon> name2Icon = new HashMap<>();

    /**
     * Default construct.
     */
    public ShapeComboBox()
    {
        addShapeItem(ShapeList.RECTANGLE);
        addShapeItem(ShapeList.RECTANGLE3D);
        addShapeItem(ShapeList.ROUNDED_RECTANGLE);
        addShapeItem(ShapeList.ELLIPSE);
        addShapeItem(ShapeList.REGULAR_STAR);
        addShapeItem(ShapeList.REGULAR_POLYGON);
        addShapeItem(ShapeList.POLYGON);
        setRenderer(new ComboBoxRenderer());
    }

    ShapeIcon getIcon(String iconName)
    {
        return name2Icon.get(iconName);
    }

    void selectString(String text)
    {
        for (int i = 0; i < getItemCount(); i++)
        {
            if (getItemAt(i).toString().equals(text))
            {
                setSelectedItem(text);
            }
        }
    }

    /**
     * Add an item to the drop-down.
     *
     * @param shapeType
     */
    public final void addShapeItem(ShapeList shapeType)
    {
        try
        {
            // add the  regular shapes
            if (shapeType == ShapeList.REGULAR_POLYGON ||
                shapeType == ShapeList.REGULAR_STAR)
            {
                ShapeIcon icon = new ShapeIcon(shapeType, 5);
                name2Icon.put(shapeType.toString(), icon);
                addItem(shapeType);
            }
            else
            {
                ShapeIcon icon = new ShapeIcon(shapeType);
                name2Icon.put(shapeType.toString(), icon);
                addItem(shapeType);
            }
        }
        catch (Exception ex)
        {
            // Don't add
            //Exceptions.printStackTrace(ex);
        }
    }

    /**
     * Retrieve an array of descriptive strings of the combo-box choices.
     *
     * @return a string array
     */
    public String[] getChoicesAsStrings()
    {
        ArrayList<String> strList = new ArrayList<>();
        for (int i = 0; i < this.getItemCount(); i++)
        {
            strList.add(getItemAt(i).toString());
        }
        return (String[]) strList.toArray();

    }

    /**
     * Icons for drawing in the combo box.
     */
    public static class ShapeIcon
            implements Icon
    {

        ShapeList shapeType;
        int[] xPoints = null;
        int[] yPoints = null;
        int numPoints = 0;
        private Image image = null;

        ShapeIcon(ShapeList shapeType) throws Exception
        {
            if (shapeType.equals(ShapeList.RECTANGLE) ||
                shapeType.equals(ShapeList.RECTANGLE3D) ||
                shapeType.equals(ShapeList.ROUNDED_RECTANGLE) ||
                shapeType.equals(ShapeList.ELLIPSE))
            {
                this.shapeType = shapeType;
            }
            else
            {
                throw new Exception(NbBundle.getMessage(
                        CLAZZ,
                        "ShapeComboBox.cannotCreateShapeIcon",
                        shapeType));
            }
        }

        ShapeIcon(ShapeList shapeType, int degree) throws Exception
        {
            if (!shapeType.equals(ShapeList.REGULAR_POLYGON) &&
                !shapeType.equals(ShapeList.REGULAR_STAR))
            {
                throw new Exception(NbBundle.getMessage(
                        CLAZZ,
                        "ShapeComboBox.cannotCreateShapeIcon2",
                        shapeType,
                        ShapeList.REGULAR_POLYGON,
                        ShapeList.REGULAR_STAR)
                );
            }

            this.shapeType = shapeType;
            numPoints = shapeType.equals(ShapeList.REGULAR_POLYGON) ? 5 : 10;
            xPoints = new int[numPoints];
            yPoints = new int[numPoints];
            final double twoPi = Math.PI * 2.0;
            double divisor = numPoints;
            double step = twoPi / divisor;
            double scale = Math.min(getIconHeight(), getIconWidth()) / 2 - 1;

            if (shapeType.equals(ShapeList.REGULAR_POLYGON))
            {
                int index = 0;
                for (double alpha = 0.0; alpha < twoPi; alpha += step)
                {
                    Double sine = Math.sin(alpha);
                    Double cosine = Math.cos(alpha);
                    Double scaledSine = sine * scale + scale;
                    Double scaledCosine = cosine * scale + scale;
                    xPoints[index] = scaledSine.intValue();
                    yPoints[index] = scaledCosine.intValue();
                    index++;
                }
            }
            else
            {
                Double scale2 = scale / 2.0;
                int index = 0;
                for (double alpha = 0.0; alpha < twoPi; alpha += step)
                {

                    Double sine = Math.sin(alpha);
                    Double cosine = Math.cos(alpha);
                    Double scaledSine = sine *
                                        ((index % 2 == 0) ? scale : scale2) +
                                        scale;
                    Double scaledCosine = cosine *
                                          ((index % 2 == 0) ? scale : scale2) +
                                          scale;
                    xPoints[index] = scaledSine.intValue();
                    yPoints[index] = scaledCosine.intValue();
                    index++;
                }
            }
        }

        ShapeIcon(int[] xPoints, int[] yPoints, int numPoints)
        {
            this.shapeType = ShapeList.POLYGON;
            this.xPoints = xPoints;
            this.yPoints = yPoints;
            this.numPoints = numPoints;
        }

        ShapeIcon(Image image)
        {
            this.shapeType = ShapeList.IMAGE;
            this.image = image;
        }

        @Override
        public final int getIconWidth()
        {
            return 30;
        }

        @Override
        public final int getIconHeight()
        {
            return 20;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y)
        {
            g.setColor(Color.black);
            if (shapeType.equals(ShapeList.RECTANGLE))
            {
                g.drawRect(3, 3, getIconWidth() - 7, getIconHeight() - 7);
            }
            else if (shapeType.equals(ShapeList.RECTANGLE3D))
            {
                g.draw3DRect(3,
                             3,
                             getIconWidth() - 7,
                             getIconHeight() - 7,
                             true);
            }
            else if (shapeType.equals(ShapeList.ELLIPSE))
            {
                g.drawOval(3, 3, getIconWidth() - 7, getIconHeight() - 7);
            }
            else if (shapeType.equals(ShapeList.ROUNDED_RECTANGLE))
            {
                g.drawRoundRect(3,
                                3,
                                getIconWidth() - 7,
                                getIconHeight() - 7,
                                6,
                                4);
            }
            else if (shapeType.equals(ShapeList.POLYGON) ||
                     shapeType.equals(ShapeList.REGULAR_POLYGON) ||
                     shapeType.equals(ShapeList.REGULAR_STAR))
            {
                g.drawPolygon(xPoints, yPoints, xPoints.length);
            }
            else if (shapeType.equals(ShapeList.IMAGE))
            {
                g.drawImage(image, 1, 1, null);
            }
        }
    }

    class ComboBoxRenderer extends JLabel implements ListCellRenderer
    {

        private Font missingIconFont;

        ComboBoxRenderer()
        {
            setOpaque(true);
            setHorizontalAlignment(LEFT);
            setVerticalAlignment(CENTER);
        }

        /**
         * This method finds the image and text corresponding to the selected
         * value and returns the label, set up to display the text and image.
         */
        @Override
        public Component getListCellRendererComponent(
                JList list,
                Object value,
                int index,
                boolean isSelected,
                boolean cellHasFocus)
        {
            if (isSelected)
            {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            }
            else
            {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }

            String shapeString = value != null ?
                                 value.toString() :
                                 getSelectedItem().toString();
            Icon icon = name2Icon.get(shapeString);
            setIcon(icon);
            if (icon != null)
            {
                setText(shapeString);
                setFont(list.getFont());
            }
            else
            {
                setMissingIconText(NbBundle.getMessage(
                        CLAZZ,
                        "LinePatternComboBox.noImageAvailable",
                        shapeString),
                                   list.getFont());
            }

            return this;
        }

        /**
         * Set the font and text when no image was found.
         */
        protected void setMissingIconText(String missingIconText,
                                          Font normalFont)
        {
            if (missingIconFont == null)
            {
                //lazily create this font
                missingIconFont = normalFont.deriveFont(Font.ITALIC);
            }
            setFont(missingIconFont);
            setText(missingIconText);
        }
    }
}
