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
 * @date: 2017-03-30
 * @author: Dieter J Kybelksties
 */
package com.kybelksties.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import org.openide.util.NbBundle;

/**
 *
 * @author Dieter J Kybelksties
 */
public class ColorButton extends JButton
{

    private static final Class CLAZZ = ColorButton.class;
    private static final String CLASS_NAME = CLAZZ.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    private static final String COLORDIALOG_HEADER = NbBundle.getMessage(
                                CLAZZ,
                                "ColorButton.colorButton.dialogHeader");
    private Color chosenColor;

    public ColorButton()
    {
        initializeHandler(NbBundle.getMessage(CLAZZ,
                                              "ColorButton.colorButton.text"));
    }

    @Override
    final public void addActionListener(ActionListener l)
    {
        if (l == null)
        {
            l = new ActionListener()
            {
                @Override
                final public void actionPerformed(ActionEvent e)
                {
                    setChosenColor(JColorChooser.showDialog(getParent(),
                                                            COLORDIALOG_HEADER,
                                                            chosenColor));
                }
            };
            super.addActionListener(l);
        }
    }

    private void initializeHandler(String text)
    {
        addActionListener(null);
        setText(text);
    }

    public ColorButton(Icon icon)
    {
        super(icon);
        initializeHandler(NbBundle.getMessage(CLAZZ,
                                              "ColorButton.colorButton.text"));
    }

    public ColorButton(String text)
    {
        super(text);
        initializeHandler(NbBundle.getMessage(CLAZZ,
                                              "ColorButton.colorButton.text"));
    }

    public ColorButton(Action a)
    {
        super(a);
        initializeHandler(NbBundle.getMessage(CLAZZ,
                                              "ColorButton.colorButton.text"));
    }

    public ColorButton(String text, Icon icon)
    {
        super(text, icon);
        initializeHandler(text);
    }

    /**
     * Get the value of chosenColor.
     *
     * @return the value of chosenColor
     */
    public Color getChosenColor()
    {
        return chosenColor;
    }

    /**
     * Set the value of chosenColor.
     *
     * @param chosenColor new value of chosenColor
     */
    public void setChosenColor(Color chosenColor)
    {
        this.chosenColor = chosenColor;
        setBackground(chosenColor);
    }

}
