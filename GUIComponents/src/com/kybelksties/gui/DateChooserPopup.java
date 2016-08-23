/*
 * Copyright (C) 2016 Dieter J Kybelksties
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
 * @date: 2016-08-23
 * @author: Dieter J Kybelksties
 */
package com.kybelksties.gui;

import java.awt.Frame;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.JWindow;
import javax.swing.Popup;

/**
 *
 * @author Dieter J Kybelksties
 */
public class DateChooserPopup
        extends Popup
        implements WindowFocusListener
{

    private static final Class<DateChooserPopup> CLAZZ = DateChooserPopup.class;
    private static final String CLASS_NAME = CLAZZ.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);
    DateChooser dateChooser;
    private final JWindow dialog;

    public DateChooserPopup(Frame base, int x, int y)
    {
        super();
        dateChooser = new DateChooser();
        dialog = new JWindow(base);
        dialog.setFocusable(true);
        dialog.setLocation(x, y);
        dialog.setContentPane(dateChooser);
        dateChooser.setBorder(new JPopupMenu().getBorder());
        dialog.setSize(dateChooser.getPreferredSize());
        dialog.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
                {
                    dialog.setVisible(false);
                }
            }
        });
    }

    public DateChooserPopup(Frame owner,
                            JLabel contents,
                            int x,
                            int y,
                            Locale locale,
                            Date date)
    {
        super(owner, contents, x, y);
        dateChooser = new DateChooser(locale, date);
        dialog = new JWindow(owner);
        dialog.setFocusable(true);
        dialog.setLocation(x, y);
        dialog.setContentPane(dateChooser);
        dateChooser.setBorder(new JPopupMenu().getBorder());
        dialog.setSize(dateChooser.getPreferredSize());
        dialog.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
                {
                    dialog.setVisible(false);
                }
            }
        });
    }

    @Override
    public void show()
    {
        dialog.addWindowFocusListener(this);
        dialog.setVisible(true);
    }

    @Override
    public void hide()
    {
        dialog.setVisible(false);
        dialog.removeWindowFocusListener(this);
    }

    @Override
    public void windowGainedFocus(WindowEvent e)
    {
        // NO-OP
    }

    @Override
    public void windowLostFocus(WindowEvent e)
    {
        hide();
    }

}
