
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

import com.kybelksties.general.EnvironmentVar;
import com.kybelksties.general.EnvironmentVarDialog;
import com.kybelksties.general.EnvironmentVarSets;
import com.kybelksties.general.EnvironmentVarTable;
import com.kybelksties.general.EnvironmentVarTableModel;
import com.kybelksties.gui.ColorUtils;
import com.kybelksties.gui.ForegroundBackgroundColorChooser;
import java.awt.Color;
import java.io.File;
import java.util.HashMap;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * A dialog to define the schedule of a process.
 *
 * @author kybelksd
 */
public class ScheduleDefinitionDialog extends javax.swing.JDialog
{

    private static final String CLASS_NAME =
                                ScheduleDefinitionDialog.class.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    ScheduledProcess theScheduledProcess;
    static HashMap<String, JScrollPane> envTabs = new HashMap<>();
    Color newBackColor;
    Color newForeColor;
    String newBackColorName;
    String newForeColorName;

    private ScheduleDefinitionDialog(java.awt.Frame parent,
                                     boolean modal,
                                     ScheduledProcess scheduledProcess)
    {
        // leave the initialization first
        super(parent, modal);
        initComponents();

        theScheduledProcess = scheduledProcess;
        ConcreteProcess process = theScheduledProcess.getProcess();

        makeEnvironmentTabs(process);

        // Set the color of the Background color" button
        newBackColorName = scheduledProcess.getWindowBackground();
        if (newBackColorName == null)
        {
            newBackColorName = "black";
        }
        newBackColor = ColorUtils.getXtermColor(newBackColorName);

        newForeColorName = scheduledProcess.getWindowForeground();
        if (newForeColorName == null)
        {
            newForeColor = ColorUtils.contrastColorByComplement(newBackColor);
            newForeColorName = ColorUtils.xtermColorString(newForeColor,
                                                           false,
                                                           false);
        }
        newForeColor = ColorUtils.getXtermColor(newForeColorName);

        changeColorButton.setBackground(newBackColor);
        changeColorButton.setForeground(newForeColor);

        ExeDefinition exeDef = scheduledProcess.getExeDefinition();
        exePathLbl.setText(exeDef.getExecutableWithPath());
        windowModeComboBox.setModel(
                new DefaultComboBoxModel<>(WindowMode.values()));
        windowModeComboBox.setSelectedItem(scheduledProcess.getWindowMode());
        nameLbl.setText(exeDef.getName());
        setParameterCellRenderer(exeDef);
        sleepSpinner.addChangeListener(new ChangeListener()
        {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                theScheduledProcess.setNoopTime(
                        (Long) ((JSpinner) e.getSource()).getValue());
            }
        });
        parametersTbl.setModel(exeDef.getFilledParameterModel());
        startInDirInput.setText(theScheduledProcess.getStartInDirectory());
    }

    /**
     * Show a dialog to schedule a process and return the scheduled process
     * object.
     *
     * @param parent           parent object
     * @param scheduledProcess
     * @return the scheduled process object
     */
    public static ScheduledProcess showDialog(
            java.awt.Frame parent,
            ScheduledProcess scheduledProcess)
    {
        ScheduleDefinitionDialog dlg = new ScheduleDefinitionDialog(parent,
                                                                    true,
                                                                    scheduledProcess);
        dlg.setVisible(true);
        return dlg.getProcessDefinition();
    }

    /**
     * Create the environment value category-tabs.
     *
     * @param process the process for which we want to configure the environment
     */
    public final void makeEnvironmentTabs(ConcreteProcess process)
    {
        envVarsTabsPane.removeAll();
        EnvironmentVarSets ce = process.getCategorisedEnvironment();
        for (String category : ce.getCategoryNameSet())
        {
            JPanel panel = new JPanel();
            JScrollPane scrollPane = new JScrollPane();
            panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
            EnvironmentVarTableModel model = ce.getTableModel(category);
            EnvironmentVarTable table = new EnvironmentVarTable(model);
            scrollPane.setViewportView(table);
            panel.add(scrollPane);
            envVarsTabsPane.addTab(category, scrollPane);

            envTabs.put(category, scrollPane);
            scrollPane.setVisible(true);
            table.setVisible(true);
        }
        pack();
    }

    /**
     * A cell renderer for the process's parameters.
     */
    public class ParameterCellRenderer extends DefaultTableCellRenderer
    {

        @Override
        public java.awt.Component getTableCellRendererComponent(
                javax.swing.JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column)
        {
            java.awt.Component cellComponent =
                               super.getTableCellRendererComponent(
                                       table, value, isSelected, hasFocus, row,
                                       column);
            ParameterList params = (ParameterList) table.getModel();
            if (params.requiresValue(row))
            {
                cellComponent.setBackground(java.awt.Color.PINK);
            }
            else if (params.allowsValue(row))
            {
                cellComponent.setBackground(java.awt.Color.WHITE);
            }
            else if (!params.isUsed(row) || (params.isUsed(row) &&
                                             params.hasArgument(row)))
            {
                cellComponent.setBackground(java.awt.Color.lightGray);
            }
            else
            {
                cellComponent.setBackground(java.awt.Color.WHITE);
            }
            return cellComponent;
        }
    }

    private void setParameterCellRenderer(ExeDefinition exeDefinition)
    {
        ParameterCellRenderer pcr = new ParameterCellRenderer();
        parametersTbl.setDefaultRenderer(String.class, pcr);
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
        detailsPane = new javax.swing.JPanel();
        doneBtn = new javax.swing.JButton();
        cancelBtn = new javax.swing.JButton();
        sleepSpinner = new javax.swing.JSpinner();
        sleepLabel = new javax.swing.JLabel();
        changeColorButton = new javax.swing.JButton();
        startInButton = new javax.swing.JButton();
        startInDirInput = new javax.swing.JTextField();
        reinitEnvButton = new javax.swing.JButton();
        addEnvButton = new javax.swing.JButton();
        windowModeComboBox = new javax.swing.JComboBox();
        exePathLbl = new javax.swing.JTextField();
        nameLbl = new javax.swing.JTextField();
        tablesPane = new javax.swing.JTabbedPane();
        parametersPane = new javax.swing.JScrollPane();
        parametersTbl = new javax.swing.JTable();
        envVarsPane = new javax.swing.JPanel();
        envVarsTabsPane = new javax.swing.JTabbedPane();

        setTitle("Schedule Process");
        setAlwaysOnTop(true);
        setBounds(new java.awt.Rectangle(200, 200, 0, 0));
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.LINE_AXIS));

        mainSplit.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        detailsPane.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        doneBtn.setText("Done");
        doneBtn.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                doneBtnActionPerformed(evt);
            }
        });
        detailsPane.add(doneBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 260, -1, -1));

        cancelBtn.setText("Cancel");
        cancelBtn.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cancelBtnActionPerformed(evt);
            }
        });
        detailsPane.add(cancelBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 260, 90, -1));

        sleepSpinner.setModel(new javax.swing.SpinnerNumberModel(Long.valueOf(0L), Long.valueOf(0L), Long.valueOf(3600L), Long.valueOf(1L)));
        detailsPane.add(sleepSpinner, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 100, 70, -1));

        sleepLabel.setText("seconds sleep");
        detailsPane.add(sleepLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 100, -1, -1));

        changeColorButton.setText("Change Window Background");
        changeColorButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                changeColorButtonActionPerformed(evt);
            }
        });
        detailsPane.add(changeColorButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 160, 260, -1));

        startInButton.setText("Start in...");
        startInButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                startInButtonActionPerformed(evt);
            }
        });
        detailsPane.add(startInButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, -1, -1));

        startInDirInput.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                startInDirInputActionPerformed(evt);
            }
        });
        startInDirInput.addFocusListener(new java.awt.event.FocusAdapter()
        {
            public void focusLost(java.awt.event.FocusEvent evt)
            {
                startInDirInputFocusLost(evt);
            }
        });
        detailsPane.add(startInDirInput, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 70, 370, -1));

        reinitEnvButton.setText("Re-initialise environment variables");
        reinitEnvButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                reinitEnvButtonActionPerformed(evt);
            }
        });
        detailsPane.add(reinitEnvButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 190, 260, -1));

        addEnvButton.setText("Add environmentvariable ...");
        addEnvButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                addEnvButtonActionPerformed(evt);
            }
        });
        detailsPane.add(addEnvButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 220, 260, -1));

        windowModeComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        windowModeComboBox.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                windowModeComboBoxActionPerformed(evt);
            }
        });
        detailsPane.add(windowModeComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 130, 150, -1));

        exePathLbl.setEditable(false);
        exePathLbl.setText("jTextField1");
        detailsPane.add(exePathLbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, 470, -1));

        nameLbl.setEditable(false);
        nameLbl.setText("jTextField1");
        detailsPane.add(nameLbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 470, -1));

        mainSplit.setTopComponent(detailsPane);

        parametersTbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {

            }
        ));
        parametersPane.setViewportView(parametersTbl);

        tablesPane.addTab("Parameters", parametersPane);

        envVarsPane.setLayout(new javax.swing.BoxLayout(envVarsPane, javax.swing.BoxLayout.LINE_AXIS));
        envVarsPane.add(envVarsTabsPane);

        tablesPane.addTab("Environment variables", envVarsPane);

        mainSplit.setRightComponent(tablesPane);

        getContentPane().add(mainSplit);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void doneBtnActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_doneBtnActionPerformed
    {//GEN-HEADEREND:event_doneBtnActionPerformed
        if (theScheduledProcess.allParametetersDefined())
        {
            setVisible(false);
        }
        else
        {
            String error = "Not all mandatory parameters are defined:" +
                           theScheduledProcess.getMissingParameters();
            JOptionPane.showMessageDialog(this, error);
        }
    }//GEN-LAST:event_doneBtnActionPerformed

    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cancelBtnActionPerformed
    {//GEN-HEADEREND:event_cancelBtnActionPerformed
        theScheduledProcess = null;
        setVisible(false);
    }//GEN-LAST:event_cancelBtnActionPerformed

    private void changeColorButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_changeColorButtonActionPerformed
    {//GEN-HEADEREND:event_changeColorButtonActionPerformed
        ColorUtils.ContrastColorSet cols =
                                    ForegroundBackgroundColorChooser.showDialog(
                                            this,
                                            newBackColor,
                                            newForeColor);
        newBackColor = cols.getColor();
        newBackColorName = ColorUtils.xtermColorString(newBackColor);
        changeColorButton.setBackground(newBackColor);

        newForeColor = cols.getContrastColor();
        newForeColorName = ColorUtils.xtermColorString(newForeColor);
        changeColorButton.setForeground(newForeColor);

        theScheduledProcess.setWindowBackground(newBackColorName);
        theScheduledProcess.setWindowForeground(newForeColorName);
    }//GEN-LAST:event_changeColorButtonActionPerformed

    private void startInButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_startInButtonActionPerformed
    {//GEN-HEADEREND:event_startInButtonActionPerformed
        String startInDirectory = theScheduledProcess.getStartInDirectory();
        final JFileChooser fc = new JFileChooser(startInDirectory);
        fc.setSelectedFile(new File(startInDirectory));

        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int reval = fc.showOpenDialog(this);
        if (reval == JFileChooser.APPROVE_OPTION)
        {
            File file = fc.getSelectedFile();
            theScheduledProcess.setStartInDirectory(file.getAbsolutePath());
            startInDirInput.setText(file.getAbsolutePath());
        }
    }//GEN-LAST:event_startInButtonActionPerformed

    private void startInDirInputActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_startInDirInputActionPerformed
    {//GEN-HEADEREND:event_startInDirInputActionPerformed
        if (startInDirInput != null)
        {
            theScheduledProcess.setStartInDirectory(startInDirInput.getText());
        }
    }//GEN-LAST:event_startInDirInputActionPerformed

    private void startInDirInputFocusLost(java.awt.event.FocusEvent evt)//GEN-FIRST:event_startInDirInputFocusLost
    {//GEN-HEADEREND:event_startInDirInputFocusLost
        theScheduledProcess.setStartInDirectory(startInDirInput.getText());
    }//GEN-LAST:event_startInDirInputFocusLost

    private void reinitEnvButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_reinitEnvButtonActionPerformed
    {//GEN-HEADEREND:event_reinitEnvButtonActionPerformed
        final JFileChooser fc = new JFileChooser("config");
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int reval = fc.showOpenDialog(this);
        if (reval == JFileChooser.APPROVE_OPTION)
        {
            File file = fc.getSelectedFile();
            theScheduledProcess.initEnvironment(file.getAbsolutePath());
            makeEnvironmentTabs(theScheduledProcess.getProcess());
        }

    }//GEN-LAST:event_reinitEnvButtonActionPerformed

    private void addEnvButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_addEnvButtonActionPerformed
    {//GEN-HEADEREND:event_addEnvButtonActionPerformed
        int i = 0;
        tablesPane.setSelectedComponent(envVarsPane);
        EnvironmentVarSets ce =
                           theScheduledProcess.getProcess().
                           getCategorisedEnvironment();
        EnvironmentVarDialog dlg =
                             new EnvironmentVarDialog(this, true, ce.
                                                      getCategoryNameSet().
                                                      toArray());
        EnvironmentVar val = dlg.getNewValue();
        if (val != null)
        {
            theScheduledProcess.getProcess().getCategorisedEnvironment().
                    setValue(val.getName(),
                             val.getCategory(),
                             val.getValue());
            makeEnvironmentTabs(theScheduledProcess.getProcess());
        }
    }//GEN-LAST:event_addEnvButtonActionPerformed

    private void windowModeComboBoxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_windowModeComboBoxActionPerformed
    {//GEN-HEADEREND:event_windowModeComboBoxActionPerformed
        theScheduledProcess.setUsesXterm(
                (WindowMode) windowModeComboBox.getSelectedItem());
    }//GEN-LAST:event_windowModeComboBoxActionPerformed

    /**
     * Retrieve the process definition object.
     *
     * @return the process definition
     */
    public ScheduledProcess getProcessDefinition()
    {
        return theScheduledProcess;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addEnvButton;
    private javax.swing.JButton cancelBtn;
    private javax.swing.JButton changeColorButton;
    private javax.swing.JPanel detailsPane;
    private javax.swing.JButton doneBtn;
    private javax.swing.JPanel envVarsPane;
    private javax.swing.JTabbedPane envVarsTabsPane;
    private javax.swing.JTextField exePathLbl;
    private javax.swing.JSplitPane mainSplit;
    private javax.swing.JTextField nameLbl;
    private javax.swing.JScrollPane parametersPane;
    private javax.swing.JTable parametersTbl;
    private javax.swing.JButton reinitEnvButton;
    private javax.swing.JLabel sleepLabel;
    private javax.swing.JSpinner sleepSpinner;
    private javax.swing.JButton startInButton;
    private javax.swing.JTextField startInDirInput;
    private javax.swing.JTabbedPane tablesPane;
    private javax.swing.JComboBox windowModeComboBox;
    // End of variables declaration//GEN-END:variables
}
