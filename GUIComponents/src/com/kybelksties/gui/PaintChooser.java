/*
 * @author  Dieter J Kybelksties
 * @date Mar 14, 2017
 *
 */
package com.kybelksties.gui;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Window;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JDialog;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;

/**
 *
 * @author Dieter J Kybelksties
 */
public class PaintChooser extends javax.swing.JPanel
{

    private static final Class CLAZZ = PaintChooser.class;
    private static final String CLASS_NAME = CLAZZ.getName();
    static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    ColorList colorList = null;

    /**
     * Creates new form PaintChooser
     */
    public PaintChooser()
    {
        initComponents();
        try
        {
            colorList = new ColorList(Color.BLUE);
        }
        catch (ColorList.Exception ex)
        {
            Exceptions.printStackTrace(ex);
        }
    }

    /**
     * Shows the control embedded in a Dialog.
     *
     * @param window parent
     * @param color  the color we try to find complements for
     * @param colors
     * @return a set of contrast colors
     */
    static public ColorList showDialog(Window window,
                                       Color color,
                                       Color... colors)
    {
        return showDialog(window, color, 128, false, colors);
    }

    /**
     * Show a dialog with this component.
     *
     * @param window                parent
     * @param color                 the color we try to find complements for
     * @param difference            shift value for shift complement
     * @param includeRGBcomplements if true then add complement to the contrasts
     * @param colors
     * @return a set of contrast colors
     */
    static public ColorList showDialog(Window window,
                                       Color color,
                                       int difference,
                                       boolean includeRGBcomplements,
                                       Color... colors)
    {
        final PaintChooser chsr = new PaintChooser();

//        chsr.shiftSpinner.setValue(difference);
        final JDialog dlg = new JDialog(window,
                                        Dialog.ModalityType.APPLICATION_MODAL);
        dlg.setTitle(NbBundle.getMessage(CLAZZ, "PaintChooser.title"));
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
                NbBundle.getMessage(CLAZZ, "PaintChooser.ok"));
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
        ColorList reval = chsr.getColorList();
        return chsr.setColorList(reval);

    }

    /**
     * Set a new color, calculate new contrasts and update the GUI.
     *
     * @param colorList the color we try to find complements for
     * @return a set of contrast colors
     */
    public ColorList setColorList(ColorList colorList)
    {

        updateComponents();

        return colorList;
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

        previewPanel = new javax.swing.JPanel();
        gradientTypeCombo = new javax.swing.JComboBox();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        previewPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout previewPanelLayout = new javax.swing.GroupLayout(previewPanel);
        previewPanel.setLayout(previewPanelLayout);
        previewPanelLayout.setHorizontalGroup(
            previewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 256, Short.MAX_VALUE)
        );
        previewPanelLayout.setVerticalGroup(
            previewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        add(previewPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 170, 260, -1));

        gradientTypeCombo.setModel(new javax.swing.DefaultComboBoxModel(GradientType.values()));
        gradientTypeCombo.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                gradientTypeComboActionPerformed(evt);
            }
        });
        add(gradientTypeCombo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 263, -1));

        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.LINE_AXIS));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String []
            {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jPanel1.add(jScrollPane1);

        add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, 263, 144));
    }// </editor-fold>//GEN-END:initComponents

    private void gradientTypeComboActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_gradientTypeComboActionPerformed
    {//GEN-HEADEREND:event_gradientTypeComboActionPerformed
        updateComponents();
    }//GEN-LAST:event_gradientTypeComboActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox gradientTypeCombo;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JPanel previewPanel;
    // End of variables declaration//GEN-END:variables

    private void updateComponents()
    {
        if (gradientTypeCombo.getSelectedItem() == null)
        {
            gradientTypeCombo.setSelectedItem(GradientType.UNIFORM);
        }
        GradientType selectedType =
                     (GradientType) this.gradientTypeCombo.getSelectedItem();
    }

    private ColorList getColorList()
    {
        return colorList;
    }
}
