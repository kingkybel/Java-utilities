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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import org.openide.util.NbBundle;

/**
 * Specialised combo box to select cap and join styles for line-drawings. All
 * possible combinations of cap and join are present and the icon show and
 * example of the style.
 *
 * @author kybelksd
 */
public final class CapJoinComboBox extends JComboBox
{

    private static final Class CLAZZ = CapJoinComboBox.class;
    private static final String CLASS_NAME = CLAZZ.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    static final String BUTT_BEVEL = NbBundle.getMessage(CLAZZ,
                                                         "CapJoinComboBox.buttBevel");
    static final String BUTT_MITRE = NbBundle.getMessage(CLAZZ,
                                                         "CapJoinComboBox.buttMitre");
    static final String BUTT_ROUND = NbBundle.getMessage(CLAZZ,
                                                         "CapJoinComboBox.buttRound");
    static final String ROUND_BEVEL = NbBundle.getMessage(CLAZZ,
                                                          "CapJoinComboBox.buttBevel");
    static final String ROUND_MITRE = NbBundle.getMessage(CLAZZ,
                                                          "CapJoinComboBox.buttMitre");
    static final String ROUND_ROUND = NbBundle.getMessage(CLAZZ,
                                                          "CapJoinComboBox.buttRound");
    static final String SQUARE_BEVEL = NbBundle.getMessage(CLAZZ,
                                                           "CapJoinComboBox.buttBevel");
    static final String SQUARE_MITRE = NbBundle.getMessage(CLAZZ,
                                                           "CapJoinComboBox.buttMitre");
    static final String SQUARE_ROUND = NbBundle.getMessage(CLAZZ,
                                                           "CapJoinComboBox.buttRound");
    static final String UNDEF = NbBundle.getMessage(CLAZZ,
                                                    "CapJoinComboBox.undefined");

    private final HashMap<CapJoinType, CapIcon> cap2Icon = new HashMap<>();

    /**
     * Default construct with list of default choices.
     */
    public CapJoinComboBox()
    {
        setRenderer(new ComboBoxRenderer());
        for (CapJoinType cap : CapJoinType.values())
        {
            addItem(cap);
            cap2Icon.put(cap, new CapIcon(cap));
        }
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
        for (int i = 0; i < getItemCount(); i++)
        {
            if (getItemAt(i).toString().equalsIgnoreCase(text))
            {
                setSelectedIndex(i);
            }
        }
    }

    /**
     * Enumeration of all combinations of Caps and Joins.
     */
    public static enum CapJoinType
    {

        /**
         * Butt-Cap and Bevel-Join.
         */
        Butt_Bevel,
        /**
         * Round-Cap and Bevel-Join.
         */
        Round_Bevel,
        /**
         * Square-Cap and Bevel-Join.
         */
        Square_Bevel,
        /**
         * Butt-Cap and Mitre-Join.
         */
        Butt_Mitre,
        /**
         * Round-Cap and Mitre-Join.
         */
        Round_Mitre,
        /**
         * Square-Cap and Mitre-Join.
         */
        Square_Mitre,
        /**
         * Butt-Cap and Round-Join.
         */
        Butt_Round,
        /**
         * Round-Cap and Round-Join.
         */
        Round_Round,
        /**
         * Square-Cap and Round-Join.
         */
        Square_Round;

        @Override
        public String toString()
        {
            return this == Butt_Bevel ? BUTT_BEVEL :
                   this == Butt_Mitre ? BUTT_MITRE :
                   this == Butt_Round ? BUTT_ROUND :
                   this == Round_Bevel ? ROUND_BEVEL :
                   this == Round_Mitre ? ROUND_MITRE :
                   this == Round_Round ? ROUND_ROUND :
                   this == Square_Bevel ? SQUARE_BEVEL :
                   this == Square_Mitre ? SQUARE_MITRE :
                   this == Square_Round ? SQUARE_ROUND :
                   UNDEF;
        }

        /**
         * Retrieve the Cap style as integer value.
         *
         * @return the Cap style
         */
        public int getCap()
        {
            return (this == CapJoinType.Butt_Bevel ||
                    this == CapJoinType.Butt_Mitre ||
                    this == CapJoinType.Butt_Round) ? BasicStroke.CAP_BUTT :
                   (this == CapJoinType.Round_Bevel ||
                    this == CapJoinType.Round_Mitre ||
                    this == CapJoinType.Round_Round) ? BasicStroke.CAP_ROUND :
                   BasicStroke.CAP_SQUARE;
        }

        /**
         * Retrieve the Join style as integer value.
         *
         * @return the Join style
         */
        public int getJoin()
        {
            return (this == CapJoinType.Butt_Bevel ||
                    this == CapJoinType.Round_Bevel ||
                    this == CapJoinType.Square_Bevel) ? BasicStroke.JOIN_BEVEL :
                   (this == CapJoinType.Butt_Mitre ||
                    this == CapJoinType.Round_Mitre ||
                    this == CapJoinType.Square_Mitre) ? BasicStroke.JOIN_MITER :
                   BasicStroke.JOIN_ROUND;
        }
    }

    /**
     * Icon showing an example of a Cap/Join-combination.
     */
    public static class CapIcon
            implements Icon
    {

        private final int width = 40;
        private final int height = 30;
        private final CapJoinType capType;

        /**
         * Construct an icon with the given pattern as array of floats.
         *
         * @param pattern the given patten
         */
        CapIcon(CapJoinType pattern)
        {
            this.capType = pattern;
        }

        @Override
        public int hashCode()
        {
            int hash = 3;
            hash = 17 * hash + Objects.hashCode(this.capType);
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
            final CapIcon other = (CapIcon) obj;
            return this.capType == other.capType;
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
            int cap = capType.getCap();
            int join = capType.getJoin();

            final Stroke stroke = new BasicStroke(6.0f,
                                                  cap,
                                                  join,
                                                  10.0f,
                                                  new float[]
                                                  {
                                                      1.0f, 0.0f
                                                  },
                                                  0.0f);
            ((Graphics2D) g).setStroke(stroke);
            g.setColor(Color.BLACK);
            int[] xPoints = new int[]
          {
              7, getIconWidth() - 7, 7
            };
            int[] yPoints = new int[]
          {
              7, getIconHeight() / 2, getIconHeight() - 7
            };
            g.drawPolyline(xPoints, yPoints, xPoints.length);
        }
    }

    private class ComboBoxRenderer extends JLabel implements ListCellRenderer
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

            CapJoinType cap = value != null ?
                              (CapJoinType) value :
                              (CapJoinType) getSelectedItem();
            Icon icon = cap2Icon.get(cap);
            setIcon(icon);
            if (icon != null)
            {
                setText(cap.toString());
                setFont(list.getFont());
            }
            else
            {
                setNoIconText(
                        NbBundle.getMessage(
                                CLAZZ,
                                "CapJoinComboBox.noImageAvailable",
                                cap.toString()),
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
