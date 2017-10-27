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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Window;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.colorchooser.ColorSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;
import org.openide.util.NbBundle;

/**
 * Dialog to chose colors for gradients.
 *
 * @author kybelksd
 */
public class GradientColorChooser extends javax.swing.JPanel
{

    private static final Class CLAZZ = GradientColorChooser.class;
    private static final String CLASS_NAME = CLAZZ.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    JPopupMenu colorTablePopup;

    /**
     * Creates new form ForegroundBackgroundColorChooser.
     */
    public GradientColorChooser()
    {
        initComponents();

        // Set the eventhandler for the color-chooser widget
        ColorSelectionModel model = colorChooser.getSelectionModel();
        ChangeListener changeListener = new ChangeListener()
        {

            @Override
            public void stateChanged(ChangeEvent e)
            {
                updateComponents();
            }
        };
        model.addChangeListener(changeListener);

        JPanel j = new JPanel();
        j.setLayout(new BorderLayout());
        colorChooser.setPreviewPanel(j);
        colorChooser.getPreviewPanel().setLayout(new BorderLayout());

        updateComponents();
    }

    /**
     * Update the GUI.
     */
    public final void updateComponents()
    {
        validate();
        repaint();
    }

    /**
     * Show a dialog with this component.
     *
     * @param window parent window
     * @param color1 first color of the gradient
     * @param color2 second color of the gradient
     * @return a gradient paint
     */
    static public Paint showDialog(Window window, Color color1, Color color2)
    {

        Paint reval = null;
        final GradientColorChooser chsr =
                                   new GradientColorChooser();

        final JDialog dlg = new JDialog(window,
                                        Dialog.ModalityType.APPLICATION_MODAL);
        dlg.setTitle(NbBundle.getMessage(
                CLAZZ,
                "GradientColorChooser.title"));
        dlg.getContentPane().setLayout(new AbsoluteLayout());
        Dimension chsrDim = chsr.getPreferredSize();
        dlg.getContentPane().setSize(chsrDim);
        AbsoluteConstraints constraints =
                            new AbsoluteConstraints(2,
                                                    2,
                                                    chsrDim.width,
                                                    chsrDim.height);
        dlg.add(chsr, constraints);
        JButton okButton = new JButton(NbBundle.getMessage(
                CLAZZ,
                "GradientColorChooser.ok"));
        okButton.addActionListener(new java.awt.event.ActionListener()
        {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                chsr.updateComponents();
                dlg.setVisible(false);
            }
        });

        // where to put the OK button
        Dimension okDim = okButton.getPreferredSize();
        constraints = new AbsoluteConstraints(chsrDim.width - okDim.width * 2,
                                              chsrDim.height + 4,
                                              okDim.width,
                                              okDim.height);
        dlg.add(okButton, constraints);

        Dimension dialogDim = new Dimension(chsrDim.width + 4,
                                            chsrDim.height + okDim.height + 40);
        dlg.setMinimumSize(dialogDim);
        dlg.setMaximumSize(dialogDim);
        dlg.setSize(dialogDim);
        Point p1 = window.getLocation();
        Dimension d1 = window.getSize();
        Dimension d2 = dlg.getSize();

        int x = p1.x + (d1.width - d2.width) / 2;
        int y = p1.y + (d1.height - d2.height) / 2;

        if (x < 0)
        {
            x = 0;
        }
        if (y < 0)
        {
            y = 0;
        }

        dlg.setLocation(x, y);

        dlg.setVisible(true);

        return reval;

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        colorChooser = new javax.swing.JColorChooser();

        setMaximumSize(new java.awt.Dimension(630, 350));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        colorChooser.setMaximumSize(new java.awt.Dimension(612, 305));
        add(colorChooser, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 650, 330));
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JColorChooser colorChooser;
    // End of variables declaration//GEN-END:variables
}
