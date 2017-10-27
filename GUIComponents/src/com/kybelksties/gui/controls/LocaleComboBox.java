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
 * @date: 2016-04-08
 * @author: Dieter J Kybelksties
 */
package com.kybelksties.gui.controls;

import java.awt.Component;
import java.awt.Font;
import java.util.Locale;
import java.util.TreeMap;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 * A combo-box extension specialised for locales.
 *
 * @author Dieter J Kybelksties
 */
public class LocaleComboBox extends JComboBox
{
    
    private static final Class CLAZZ = LocaleComboBox.class;
    private static final String CLASS_NAME = CLAZZ.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    private static final Locale[] allLocales = Locale.getAvailableLocales();
    private final TreeMap<String, Locale> mapString2Locale = new TreeMap<>();

    /**
     * Default construct.
     */
    public LocaleComboBox()
    {
        for (Locale locale : allLocales)
        {
            addLocaleToMap(locale);
        }
        for (String displayName : mapString2Locale.keySet())
        {
            addItem(displayName);
        }
        setRenderer(new ComboBoxRenderer());
        setSelectedItem(Locale.getDefault().getDisplayName());

    }

    @Override
    public final void setSelectedItem(Object o)
    {
        String localeString = (String) o;
        super.setSelectedItem(localeString);
    }

    @Override
    public Object getSelectedItem()
    {
        String name = (String) super.getSelectedItem();
        return mapString2Locale.get(name);
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
     * Add an item to the string-locale map.
     *
     * @param locale the locale to add
     */
    private void addLocaleToMap(Locale locale)
    {
        mapString2Locale.put(locale.getDisplayName(locale), locale);
    }

    /**
     * Retrieve an array of descriptive strings of the combo-box choices.
     *
     * @return a string array
     */
    public String[] getChoicesAsStrings()
    {
        return (String[]) mapString2Locale.keySet().toArray();

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

            String localeString = value != null ?
                                  value.toString() :
                                  getSelectedItem().toString();
            setText(localeString);

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
