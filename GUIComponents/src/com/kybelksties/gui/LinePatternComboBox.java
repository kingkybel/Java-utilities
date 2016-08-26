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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import org.openide.util.NbBundle;

/**
 * A combo-box extension specialised for line patterns.
 *
 * @author kybelksd
 */
public class LinePatternComboBox extends JComboBox
{
    private static final Class CLAZZ = LinePatternComboBox.class;
    private static final String CLASS_NAME = CLAZZ.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    static LinePatternList defaultPatterns = new LinePatternList();

    HashMap<float[], LineIcon> pattern2Icon = new HashMap<>();

    /**
     * Default construct with list of default choices.
     */
    public LinePatternComboBox()
    {
        this(null, true);
    }

    /**
     * Construct with a set of patterns.
     *
     * @param patterns given list of patterns (as array)
     */
    public LinePatternComboBox(float[][] patterns)
    {
        this(patterns, false);
    }

    /**
     * Construct with a set of patterns.
     *
     * @param patterns        given list of patterns (as array)
     * @param includeDefaults if true, then also include some default patterns
     */
    public LinePatternComboBox(float[][] patterns, boolean includeDefaults)
    {
        if (includeDefaults)
        {
            for (float[] pattern : defaultPatterns)
            {
                addPattern(pattern);
            }
        }
        if (patterns != null)
        {
            for (float[] pattern : patterns)
            {
                addPattern(pattern);
            }
        }
        setRenderer(new ComboBoxRenderer());
    }

    /**
     * Retrieve the combo choices as list of strings.
     *
     * @return the choices
     */
    public String[] getChoicesAsStrings()
    {
        ArrayList<String> reval = new ArrayList<>();
        for (int i = 0; i < getItemCount(); i++)
        {
            reval.add(getItemAt(i).toString());
        }

        return (String[]) reval.toArray();
    }

    void selectString(String text)
    {
        for (float[] pattern : defaultPatterns)
        {
            if (Arrays.toString(pattern).equals(text))
            {
                setSelectedItem(text);
            }
        }
    }

    /**
     * Add a line pattern described as an array of floats.
     *
     * @param pattern the pattern to add
     */
    public final void addPattern(float[] pattern)
    {
        LineIcon icon = new LineIcon(pattern);
        // only add each individual pattern once
        if (!pattern2Icon.containsKey(pattern))
        {
            pattern2Icon.put(pattern, icon);
            addItem(pattern);
        }
    }

    /**
     * Retrieve the icon associated with this.
     *
     * @param rect rectangle for the icon
     * @return the icon
     */
    public Icon getIcon(Rectangle rect)
    {
        if (getItemCount() > 0)
        {
            int width = rect != null ? rect.width : 80;
            int height = rect != null ? rect.height : 20;
            return new LineIcon(width, height, (float[]) getSelectedItem());
        }
        return null;
    }

    /**
     *
     */
    public static class LineIcon
            implements Icon
    {

        int width = 80;
        int height = 20;
        private final float[] pattern;

        /**
         * Construct an icon with the given pattern as array of floats.
         *
         * @param pattern the given patten
         */
        public LineIcon(float[] pattern)
        {
            this.pattern = pattern;
        }

        /**
         * Construct an icon with the given pattern as array of floats.
         *
         * @param width   width of the rectangle of the icon
         * @param height  height of the rectangle of the icon
         * @param pattern the given patten
         */
        public LineIcon(
                int width, int height, float[] pattern)
        {
            this.width = width;
            this.height = height;
            this.pattern = pattern;
        }

        @Override
        public int hashCode()
        {
            int hash = 5;
            hash = 73 * hash + Arrays.hashCode(this.pattern);
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
            final LineIcon other = (LineIcon) obj;
            return Arrays.equals(this.pattern, other.pattern);
        }

        @Override
        public int getIconWidth()
        {
            return width;
        }

        @Override
        public int getIconHeight()
        {
            return height;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y)
        {
            g.setColor(Color.black);
            final Stroke stroke = new BasicStroke(2.0f,
                                                  BasicStroke.CAP_BUTT,
                                                  BasicStroke.JOIN_MITER,
                                                  10.0f,
                                                  pattern,
                                                  0.0f);
            ((Graphics2D) g).setStroke(stroke);
            g.setColor(Color.BLACK);
            g.drawLine(1, getIconHeight() / 2, getIconWidth() - 1, 10);
        }
    }

    class ComboBoxRenderer extends JLabel implements ListCellRenderer
    {

        private Font noIconFont;

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

            float[] pattern = value != null ?
                              (float[]) value :
                              (float[]) getSelectedItem();
            Icon icon = pattern2Icon.get(pattern);
            String patternString = Arrays.toString(pattern);
            setIcon(icon);
            if (icon != null)
            {
                setText(patternString);
                setFont(list.getFont());
            }
            else
            {
                setNoIconText(NbBundle.getMessage(
                        CLAZZ,
                        "LinePatternComboBox.noImageAvailable",
                        patternString),
                              list.getFont());
            }

            return this;
        }

        /**
         * Set the font and text when no image was found.
         *
         * @param noIconText text displayed when there is no icon
         * @param normalFont the normal font (default)
         */
        protected void setNoIconText(String noIconText, Font normalFont)
        {
            if (noIconFont == null)
            {
                // lazily create this font
                noIconFont = normalFont.deriveFont(Font.ITALIC);
            }
            setFont(noIconFont);
            setText(noIconText);
        }
    }
}
