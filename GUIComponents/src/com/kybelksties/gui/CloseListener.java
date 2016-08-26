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
 * @date Jul 20, 2016
 * @author: Dieter J Kybelksties
 */
package com.kybelksties.gui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.openide.util.NbBundle;

/**
 *
 * @author Dieter J Kybelksties
 */
public class CloseListener extends WindowAdapter
{

    private static final Class CLAZZ = CloseListener.class;
    private static final String CLASS_NAME = CLAZZ.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);
    
    String prompt = NbBundle.getMessage(CLAZZ, "CloseListener.prompt");
    String title = NbBundle.getMessage(CLAZZ, "CloseListener.title");

    public CloseListener()
    {
    }

    public CloseListener(String prompt, String title)
    {
        this.prompt = prompt;
        this.title = title;
    }

    @Override
    public void windowClosing(WindowEvent e)
    {
        JFrame frame = (JFrame) e.getSource();
        int result = JOptionPane.showConfirmDialog(
            frame,
            prompt,
            title,
            JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION)
        {
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
    }

}
