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

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Window;
import static java.awt.image.ImageObserver.WIDTH;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;
import org.openide.util.NbBundle;

/**
 * Custom component to choose a font.
 *
 * @author kybelksd
 */
public class FontChooser extends javax.swing.JPanel
{

    private static final Class CLAZZ = FontChooser.class;
    private static final String CLASS_NAME = CLAZZ.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    String[] sizesArray = NbBundle.getMessage(
             CLAZZ,
             "FontChooser.fontSizes").split(",");

    private Font configuredFont = null;

    /**
     * Get the value of configuredFont
     *
     * @return the value of configuredFont
     */
    public Font getConfiguredFont()
    {
        return configuredFont;
    }

    /**
     * Set the value of configuredFont.
     *
     * @param newConfiguredFont new value of configuredFont
     */
    public void setConfiguredFont(Font newConfiguredFont)
    {
        if (newConfiguredFont == null ||
            !newConfiguredFont.equals(configuredFont))
        {
            propertyChangeSupport.firePropertyChange(
                    "configuredFont",
                    configuredFont,
                    newConfiguredFont);

        }

        this.configuredFont = newConfiguredFont;
    }

    private transient final PropertyChangeSupport propertyChangeSupport =
                                                  new PropertyChangeSupport(
                                                          this);

    /**
     * Add PropertyChangeListener.
     *
     * @param listener
     */
    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener)
    {
        if (propertyChangeSupport != null)
        {
            propertyChangeSupport.addPropertyChangeListener(listener);
        }
    }

    /**
     * Remove PropertyChangeListener.
     *
     * @param listener the listener to be removed
     */
    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener)
    {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    private boolean showSample = true;

    /**
     * Get the value of showSample.
     *
     * @return the value of showSample
     */
    public boolean getShowSample()
    {
        return showSample;
    }

    /**
     * Set the value of showSample.
     *
     * @param showSample new value of showSample
     */
    public void setShowSample(boolean showSample)
    {
        this.showSample = showSample;
        updateComponents();
    }

    /**
     * Creates new form FontChooser.
     *
     * @param font initial font, if null use default
     */
    public FontChooser(Font font)
    {
        initComponents();
        fontComboBox.setModel(
                new DefaultComboBoxModel(
                        GraphicsEnvironment.getLocalGraphicsEnvironment().
                        getAvailableFontFamilyNames()));
        sizeComboBox.setModel(new DefaultComboBoxModel(sizesArray));
        sampleTextField.setSize(sampleTextField.getHeight(), 70);

        if (font != null)
        {
            configuredFont = font;
        }

        if (configuredFont == null)
        {
            fontComboBox.setSelectedIndex(0);
            sizeComboBox.setSelectedItem(NbBundle.getMessage(
                    CLAZZ,
                    "FontChooser.selectedFontSize"));
        }
        setConfiguredFont();
        this.setSize(WIDTH, WIDTH);
        updateComponents();
    }

    /**
     * Default construct.
     */
    public FontChooser()
    {
        this(null);
    }

    /**
     * Update the GUI.
     */
    public final void updateComponents()
    {
        sampleTextField.setVisible(showSample);
        validate();
        repaint();
    }

    private void setConfiguredFont()
    {
        int style = boldButton.isSelected() ? Font.BOLD : Font.PLAIN;
        style |= italicButton.isSelected() ? Font.ITALIC : 0;
        int size = Integer.parseInt(sizeComboBox.getSelectedItem().toString());
        setConfiguredFont(new Font(fontComboBox.getSelectedItem().toString(),
                                   style,
                                   size));
        sampleTextField.setFont(configuredFont);
        sampleTextField.invalidate();
    }

    /**
     * Show a dialog with this component.
     *
     * @param window parent
     * @param font   an initial font
     * @return the newly configured
     */
    static public Font showDialog(Window window, Font font)
    {
        final FontChooser chsr = new FontChooser(font);

        final JDialog dlg = new JDialog(window,
                                        Dialog.ModalityType.APPLICATION_MODAL);
        dlg.setTitle(NbBundle.getMessage(CLAZZ, "FontChooser.title"));
        dlg.getContentPane().setLayout(new AbsoluteLayout());
        Dimension chsrDim = chsr.getPreferredSize();
        dlg.getContentPane().setSize(chsrDim);
        AbsoluteConstraints constraints =
                            new AbsoluteConstraints(2,
                                                    2,
                                                    chsrDim.width,
                                                    chsrDim.height);
        dlg.add(chsr, constraints);
        JButton okButton = new JButton(
                NbBundle.getMessage(CLAZZ, "FontChooser.ok"));
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

        return chsr.getConfiguredFont();

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

        boldButton = new javax.swing.JToggleButton();
        italicButton = new javax.swing.JToggleButton();
        fontComboBox = new javax.swing.JComboBox();
        sizeComboBox = new javax.swing.JComboBox();
        sampleTextField = new javax.swing.JTextField();

        setMinimumSize(new java.awt.Dimension(0, 0));
        setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING, 0, 0));

        boldButton.setFont(new java.awt.Font("Century Schoolbook L", 1, 12)); // NOI18N
        boldButton.setText(org.openide.util.NbBundle.getMessage(FontChooser.class, "FontChooser.boldButton.text")); // NOI18N
        boldButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                boldButtonActionPerformed(evt);
            }
        });
        add(boldButton);

        italicButton.setFont(new java.awt.Font("Century Schoolbook L", 2, 12)); // NOI18N
        italicButton.setText(org.openide.util.NbBundle.getMessage(FontChooser.class, "FontChooser.italicButton.text")); // NOI18N
        italicButton.setMaximumSize(new java.awt.Dimension(44, 23));
        italicButton.setMinimumSize(new java.awt.Dimension(44, 23));
        italicButton.setPreferredSize(new java.awt.Dimension(44, 23));
        italicButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                italicButtonActionPerformed(evt);
            }
        });
        add(italicButton);

        fontComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        fontComboBox.setMaximumSize(new java.awt.Dimension(32767, 24));
        fontComboBox.setName(""); // NOI18N
        fontComboBox.setOpaque(false);
        fontComboBox.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                fontComboBoxActionPerformed(evt);
            }
        });
        add(fontComboBox);

        sizeComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        sizeComboBox.setMaximumSize(new java.awt.Dimension(32767, 24));
        sizeComboBox.setName(""); // NOI18N
        sizeComboBox.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                sizeComboBoxActionPerformed(evt);
            }
        });
        add(sizeComboBox);

        sampleTextField.setEditable(false);
        sampleTextField.setText(org.openide.util.NbBundle.getMessage(FontChooser.class, "FontChooser.sampleTextField.text")); // NOI18N
        sampleTextField.setMaximumSize(new java.awt.Dimension(2147483647, 24));
        sampleTextField.setMinimumSize(new java.awt.Dimension(4, 24));
        sampleTextField.setPreferredSize(new java.awt.Dimension(46, 24));
        add(sampleTextField);
    }// </editor-fold>//GEN-END:initComponents

    private void italicButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_italicButtonActionPerformed
    {//GEN-HEADEREND:event_italicButtonActionPerformed
        setConfiguredFont();
    }//GEN-LAST:event_italicButtonActionPerformed

    private void boldButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_boldButtonActionPerformed
    {//GEN-HEADEREND:event_boldButtonActionPerformed
        setConfiguredFont();
    }//GEN-LAST:event_boldButtonActionPerformed

    private void fontComboBoxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_fontComboBoxActionPerformed
    {//GEN-HEADEREND:event_fontComboBoxActionPerformed
        setConfiguredFont();
    }//GEN-LAST:event_fontComboBoxActionPerformed

    private void sizeComboBoxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_sizeComboBoxActionPerformed
    {//GEN-HEADEREND:event_sizeComboBoxActionPerformed
        setConfiguredFont();
    }//GEN-LAST:event_sizeComboBoxActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton boldButton;
    private javax.swing.JComboBox fontComboBox;
    private javax.swing.JToggleButton italicButton;
    private javax.swing.JTextField sampleTextField;
    private javax.swing.JComboBox sizeComboBox;
    // End of variables declaration//GEN-END:variables
}
