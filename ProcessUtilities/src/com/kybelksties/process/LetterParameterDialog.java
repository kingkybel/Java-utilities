
/*
 * Copyright (C) 2015 Dieter J Kybelksties
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
 * @date: 2015-12-14
 * @author: Dieter J Kybelksties
 */
package com.kybelksties.process;

import java.util.ArrayList;
import java.util.logging.Logger;
import javax.swing.JToggleButton;

/**
 * A dialog to customize which letters constitute legal parameters.
 *
 * @author kybelksd
 */
public class LetterParameterDialog extends javax.swing.JDialog
{

    private static final String CLASS_NAME = LetterParameterDialog.class.
                                getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    static final String alpha = "abcdefghijklmnopqrstuvwxyz";
    static final String ALPHA = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    static final String num = "0123456789";
    static final String all = alpha + ALPHA + num;

    ArrayList<JToggleButton> btn = new ArrayList<>();
    ParameterList theParameters;

    /**
     * Creates new form ParameterSelector.
     *
     * @param parent
     * @param modal
     */
    public LetterParameterDialog(javax.swing.JDialog parent, boolean modal)
    {
        super(parent, modal);
        initComponents();

        for (int i = 0; i < all.length(); i++)
        {
            String theLetter = all.substring(i, i + 1);
            btn.add(new JToggleButton(theLetter));
            btn.get(i).setEnabled(true);
            buttonSelectionPane.add(btn.get(i));
            btn.get(i).addActionListener(new java.awt.event.ActionListener()
            {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt)
                {
                    LetterParameterDialog.this.toggle(
                            (JToggleButton) evt.getSource());
                }
            });
            btn.get(i).setVisible(true);
        }
    }

    void selectParameters(ParameterList theParameters)
    {
        if (theParameters == null)
        {
            theParameters = new ParameterList();
        }
        this.theParameters = theParameters;

        for (int i = 0; i < all.length(); i++)
        {
            String theLetter = all.substring(i, i + 1);
            btn.get(i).setSelected(theParameters.contains(theLetter));
            buttonSelectionPane.add(btn.get(i));
        }
        setVisible(true);
    }

    private void toggle(JToggleButton btn)
    {
        String text = btn.getText();
        if (btn.isSelected())
        {
            theParameters.add(new LetterParameter(text.charAt(0)));
        }
        else
        {
            theParameters.remove(text.substring(0, 1));
        }
    }

    /**
     * Get all the letters of the toggled buttons.
     *
     * @return the chosen parameter letters
     */
    public ParameterList getSelection()
    {
        return theParameters;
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

        mainSplit = new javax.swing.JSplitPane();
        buttonSelectionPane = new javax.swing.JPanel();
        controlPane = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();

        setTitle("Parameter Selection");
        setBounds(new java.awt.Rectangle(200, 200, 0, 0));
        setMinimumSize(new java.awt.Dimension(230, 400));
        setName("parameterSelectorFrame"); // NOI18N
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.LINE_AXIS));

        mainSplit.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        buttonSelectionPane.setMinimumSize(new java.awt.Dimension(230, 400));
        buttonSelectionPane.setName("buttonSelectionPane"); // NOI18N
        buttonSelectionPane.setLayout(new java.awt.GridLayout(10, 4));
        mainSplit.setLeftComponent(buttonSelectionPane);

        controlPane.setMaximumSize(new java.awt.Dimension(2147483647, 25));
        controlPane.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                okButtonActionPerformed(evt);
            }
        });
        controlPane.add(okButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 0, -1, -1));

        mainSplit.setRightComponent(controlPane);

        getContentPane().add(mainSplit);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_okButtonActionPerformed
    {//GEN-HEADEREND:event_okButtonActionPerformed
        setVisible(false);
    }//GEN-LAST:event_okButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel buttonSelectionPane;
    private javax.swing.JPanel controlPane;
    private javax.swing.JSplitPane mainSplit;
    private javax.swing.JButton okButton;
    // End of variables declaration//GEN-END:variables

}