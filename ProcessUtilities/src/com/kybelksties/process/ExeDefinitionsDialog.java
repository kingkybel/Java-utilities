
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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.table.TableColumnModel;

/**
 * Dialog that enables the user to define an executable definition which
 * basically consists of the path to an executable and optionally some
 * parameters.
 *
 * @author kybelksd
 */
public class ExeDefinitionsDialog extends javax.swing.JDialog
{

    private static final String CLASS_NAME =
                                ExeDefinitionsDialog.class.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    ParameterList theParameters;
    ExeDefinition theExeDefinition;
    ExeDefinitions exeDefinitionList;
    String lastExeDirectory;
    LetterParameterDialog paramLetterSelector = new LetterParameterDialog(this,
                                                                          true);
    PositionalParameterDialog paramPositionSelector =
                              new PositionalParameterDialog(this, true);

    final void createDefinition()
    {
        theExeDefinition = new ExeDefinition();
        theParameters = theExeDefinition.getParameters();
        exeDefinitionList.add(theExeDefinition);
        updateDialogFromModel();
    }

    /**
     * Creates new form ExecutionDetailsDialog.
     *
     * @param parent           the parent frame
     * @param modal            indicates whether the window is used in modal or
     *                         non-modal mode
     * @param lastExeDirectory
     */
    private ExeDefinitionsDialog(java.awt.Frame parent,
                                 boolean modal,
                                 String lastExeDirectory,
                                 ExeDefinitions exeDefinitionList)
    {
        super(parent, modal);
        initComponents();

        this.exeDefinitionList = exeDefinitionList;

        if (exeDefinitionList != null)
        {
            if (exeDefinitionList.size() > 0)
            {
                theExeDefinition = exeDefinitionList.getDefinition(0);
            }
            else
            {
                theExeDefinition = new ExeDefinition();
            }
            theParameters = theExeDefinition.getParameters();
        }
        else
        {
            createDefinition();
        }
        if (exeDefinitionList != null)
        {
            exeNameCB.setModel(exeDefinitionList.getComboBoxModel());
        }
        this.lastExeDirectory = lastExeDirectory;
        setColumnWidths();
        JMenuItem menuItem;
        ActionListener action = new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String command = e.getActionCommand().toUpperCase();
                switch (command)
                {
                    case "MOVE UP":
                        ExeDefinitionsDialog.this.
                                upButtonActionPerformed(
                                        e);
                        break;
                    case "MOVE DOWN":
                        ExeDefinitionsDialog.this.
                                downButtonActionPerformed(
                                        e);
                        break;
                    case "DELETE":
                        ExeDefinitionsDialog.this.
                                deleteButtonActionPerformed(
                                        e);
                        break;
                }
            }
        };
        paramPopup = new JPopupMenu();

        menuItem = new JMenuItem("Move up");
        menuItem.addActionListener(action);
        paramPopup.add(menuItem);

        menuItem = new JMenuItem("Move Down");
        menuItem.addActionListener(action);
        paramPopup.add(menuItem);

        menuItem = new JMenuItem("Delete");
        menuItem.addActionListener(action);
        paramPopup.add(menuItem);
        updateDialogFromModel();
    }

    /**
     * Show a dialog and return the customised definitions of executables.
     *
     * @param parent
     * @param lastExeDirectory the last used directory (to save time)
     * @param exeDefinitions   currently d
     *
     * @return the customised definitions of executables
     */
    public static ExeDefinitions showDialog(java.awt.Frame parent,
                                            String lastExeDirectory,
                                            ExeDefinitions exeDefinitions)
    {
        ExeDefinitions currentDefinitions = new ExeDefinitions(exeDefinitions);
        if (exeDefinitions == null)
        {
            exeDefinitions = new ExeDefinitions();
//            exeDefinitions.add(new ExeDefinition());
        }
        ExeDefinitionsDialog dlg = new ExeDefinitionsDialog(parent,
                                                            true,
                                                            lastExeDirectory,
                                                            exeDefinitions);
        dlg.setVisible(true);
        if (dlg.getDefinitions() == null || !dlg.theExeDefinition.isValid())
        {
            return currentDefinitions;
        }
        return dlg.getDefinitions();
    }

    final void setColumnWidths()
    {
        if (paramTbl.isVisible())
        {
            TableColumnModel colmod = paramTbl.getColumnModel();
            if (colmod.getColumnCount() > 4)
            {
                colmod.getColumn(2).setPreferredWidth(250);
                colmod.getColumn(3).setPreferredWidth(250);
                colmod.getColumn(4).setPreferredWidth(250);
                if (paramPositionalCB.isSelected())
                {
                    colmod.getColumn(1).setMinWidth(0);
                    colmod.getColumn(0).setPreferredWidth(200);
                    colmod.getColumn(1).setPreferredWidth(0);
                    colmod.getColumn(1).setMaxWidth(0);
                }
                else
                {
                    colmod.getColumn(0).setMinWidth(0);
                    colmod.getColumn(0).setPreferredWidth(0);
                    colmod.getColumn(1).setPreferredWidth(200);
                    colmod.getColumn(0).setMaxWidth(0);
                }
            }
        }
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

        paramPopup = new javax.swing.JPopupMenu();
        exeDetailsSplit = new javax.swing.JSplitPane();
        executableDefinitionPanel = new javax.swing.JPanel();
        browseForExeBtn = new javax.swing.JButton();
        exePathInput = new javax.swing.JTextField();
        paramPositionalCB = new javax.swing.JCheckBox();
        paramPane = new javax.swing.JScrollPane();
        paramTbl = new javax.swing.JTable();
        addParamBtn = new javax.swing.JButton();
        closeBtn = new javax.swing.JButton();
        discardBtn = new javax.swing.JButton();
        exeDefinitionNameInput = new javax.swing.JTextField();
        exeNameCB = new javax.swing.JComboBox();
        newBtn = new javax.swing.JButton();
        commandLinePane = new javax.swing.JPanel();
        commandlineLabel = new javax.swing.JLabel();
        commandLineScrollPane = new javax.swing.JScrollPane();
        commandlineInput = new javax.swing.JTextArea();

        setTitle("Commandline and Environment Variables");
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.LINE_AXIS));

        exeDetailsSplit.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        executableDefinitionPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        browseForExeBtn.setText("Browse for Executable...");
        browseForExeBtn.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                browseForExeBtnActionPerformed(evt);
            }
        });
        executableDefinitionPanel.add(browseForExeBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, -1, -1));

        exePathInput.setEditable(false);
        exePathInput.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                exePathInputActionPerformed(evt);
            }
        });
        executableDefinitionPanel.add(exePathInput, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 30, 570, -1));

        paramPositionalCB.setText("Parameters are positional");
        paramPositionalCB.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                paramPositionalCBActionPerformed(evt);
            }
        });
        executableDefinitionPanel.add(paramPositionalCB, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 90, -1, -1));

        paramTbl.setAutoCreateRowSorter(true);
        paramTbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {

            }
        ));
        paramTbl.setColumnSelectionAllowed(true);
        paramTbl.setDragEnabled(true);
        paramTbl.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                paramTblMouseClicked(evt);
            }
        });
        paramPane.setViewportView(paramTbl);

        executableDefinitionPanel.add(paramPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 150, 760, 140));

        addParamBtn.setText("Add Parameter");
        addParamBtn.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                addParamBtnActionPerformed(evt);
            }
        });
        executableDefinitionPanel.add(addParamBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 120, -1, -1));

        closeBtn.setText("Accept and Close");
        closeBtn.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                closeBtnActionPerformed(evt);
            }
        });
        executableDefinitionPanel.add(closeBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 300, 150, -1));

        discardBtn.setText("Discard");
        discardBtn.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                discardBtnActionPerformed(evt);
            }
        });
        executableDefinitionPanel.add(discardBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 300, 100, -1));

        exeDefinitionNameInput.setEditable(false);
        executableDefinitionPanel.add(exeDefinitionNameInput, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 450, -1));

        exeNameCB.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                exeNameCBActionPerformed(evt);
            }
        });
        executableDefinitionPanel.add(exeNameCB, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 240, -1));

        newBtn.setText("New");
        newBtn.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                newBtnActionPerformed(evt);
            }
        });
        executableDefinitionPanel.add(newBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 300, -1, -1));

        exeDetailsSplit.setTopComponent(executableDefinitionPanel);

        commandLinePane.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        commandlineLabel.setText("Command line:");
        commandLinePane.add(commandlineLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        commandlineInput.setEditable(false);
        commandlineInput.setColumns(20);
        commandlineInput.setLineWrap(true);
        commandlineInput.setRows(5);
        commandlineInput.addInputMethodListener(new java.awt.event.InputMethodListener()
        {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt)
            {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt)
            {
                commandlineInputInputMethodTextChanged(evt);
            }
        });
        commandlineInput.addPropertyChangeListener(new java.beans.PropertyChangeListener()
        {
            public void propertyChange(java.beans.PropertyChangeEvent evt)
            {
                commandlineInputPropertyChange(evt);
            }
        });
        commandLineScrollPane.setViewportView(commandlineInput);

        commandLinePane.add(commandLineScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 20, 760, 60));

        exeDetailsSplit.setRightComponent(commandLinePane);

        getContentPane().add(exeDetailsSplit);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void updateDialogFromModel()
    {
        if (isVisible())
        {
            exePathInput.setText(theExeDefinition.getExecutableWithPath());
            String defName = theExeDefinition.getName();
            exeDefinitionNameInput.setText(defName);
            paramPositionalCB.setSelected(theParameters.isPositional);
            exeNameCB.setModel(exeDefinitionList.getComboBoxModel());
            exeNameCB.setSelectedItem(exeDefinitionList.getActiveDefinition());
            paramTbl.setModel(theExeDefinition.getEditParameterModel());
            theParameters.addTableUpdatedEventListener(
                    new ParamTableUpdatedEventListener()
                    {
                        @Override
                        public void tableUpdatedEventOccurred(
                                ParamTableUpdatedEvent evt)
                        {
                            commandlineInput.setText(updateCommandLine());
                                }
                    });
        }
    }

    private void browseForExeBtnActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_browseForExeBtnActionPerformed
    {//GEN-HEADEREND:event_browseForExeBtnActionPerformed
        final JFileChooser fc = new JFileChooser(lastExeDirectory);
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int reval = fc.showOpenDialog(this);
        if (reval == JFileChooser.APPROVE_OPTION)
        {
            File file = fc.getSelectedFile();
            exePathInput.setText(file.getAbsolutePath());
            theExeDefinition.setExecutable(file.getName());
            theExeDefinition.setPath(file.getParent());
            lastExeDirectory = theExeDefinition.getPath();
        }
        updateDialogFromModel();
    }//GEN-LAST:event_browseForExeBtnActionPerformed

    private void addParamBtnActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_addParamBtnActionPerformed
    {//GEN-HEADEREND:event_addParamBtnActionPerformed
        if (!paramPositionalCB.isSelected())
        {
            paramLetterSelector.selectParameters(theParameters);
            theParameters = paramLetterSelector.getSelection();
        }
        else
        {
            paramPositionSelector.selectPositions(theParameters);
            theParameters = paramPositionSelector.getSelection();
        }
        updateDialogFromModel();
    }//GEN-LAST:event_addParamBtnActionPerformed

    private void paramPositionalCBActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_paramPositionalCBActionPerformed
    {//GEN-HEADEREND:event_paramPositionalCBActionPerformed
        theParameters.makePositional(paramPositionalCB.isSelected());
        updateDialogFromModel();
    }//GEN-LAST:event_paramPositionalCBActionPerformed

    private void exePathInputActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_exePathInputActionPerformed
    {//GEN-HEADEREND:event_exePathInputActionPerformed
        updateDialogFromModel();
    }//GEN-LAST:event_exePathInputActionPerformed

    private void commandlineInputPropertyChange(java.beans.PropertyChangeEvent evt)//GEN-FIRST:event_commandlineInputPropertyChange
    {//GEN-HEADEREND:event_commandlineInputPropertyChange
        updateDialogFromModel();
    }//GEN-LAST:event_commandlineInputPropertyChange

    private void commandlineInputInputMethodTextChanged(java.awt.event.InputMethodEvent evt)//GEN-FIRST:event_commandlineInputInputMethodTextChanged
    {//GEN-HEADEREND:event_commandlineInputInputMethodTextChanged
        updateDialogFromModel();
    }//GEN-LAST:event_commandlineInputInputMethodTextChanged

    private void paramTblMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_paramTblMouseClicked
    {//GEN-HEADEREND:event_paramTblMouseClicked
        paramTbl.setComponentPopupMenu(paramPopup);
    }//GEN-LAST:event_paramTblMouseClicked

    private void closeBtnActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_closeBtnActionPerformed
    {//GEN-HEADEREND:event_closeBtnActionPerformed
        setVisible(false);
    }//GEN-LAST:event_closeBtnActionPerformed

    private void exeNameCBActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_exeNameCBActionPerformed
    {//GEN-HEADEREND:event_exeNameCBActionPerformed
        int selInd = exeNameCB.getSelectedIndex();
        if (selInd > -1)
        {
            exeDefinitionList.setActiveDefinition(selInd);
        }
        else
        {
            exeDefinitionList.add(new ExeDefinition());
        }
        theExeDefinition = exeDefinitionList.getActiveDefinition();
        theParameters = theExeDefinition.getParameters();
        updateDialogFromModel();
    }//GEN-LAST:event_exeNameCBActionPerformed

    private void newBtnActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_newBtnActionPerformed
    {//GEN-HEADEREND:event_newBtnActionPerformed
        createDefinition();
        updateDialogFromModel();
    }//GEN-LAST:event_newBtnActionPerformed

    private void discardBtnActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_discardBtnActionPerformed
    {//GEN-HEADEREND:event_discardBtnActionPerformed
        exeDefinitionList.remove(theExeDefinition);
        updateDialogFromModel();
        exeDefinitionList = null;
        setVisible(false);
    }//GEN-LAST:event_discardBtnActionPerformed

    ExeDefinitions getDefinitions()
    {
        return exeDefinitionList;
    }

    private String updateCommandLine()
    {
        if (this.isVisible())
        {
            String cmd = exePathInput.getText() + " " + theParameters.toString();
            commandlineInput.setText(cmd);
            return cmd;
        }
        else
        {
            return "";
        }
    }

    private void upButtonActionPerformed(ActionEvent e)
    {
        int row = paramTbl.getSelectedRow();
        boolean hasMoved = theParameters.up(row);
        if (hasMoved)
        {
            paramTbl.setRowSelectionInterval(row > 1 ? row - 1 : 0,
                                             row > 1 ? row - 1 : 0);
            paramTbl.setColumnSelectionInterval(0,
                                                paramTbl.getColumnCount() - 1);
        }
    }

    private void downButtonActionPerformed(ActionEvent e)
    {
        int row = paramTbl.getSelectedRow();
        boolean hasMoved = theParameters.down(row);
        if (hasMoved)
        {
            paramTbl.setRowSelectionInterval(row > 1 ? row - 1 : 0, row > 1 ?
                                                                    row - 1 : 0);
            paramTbl.
                    setColumnSelectionInterval(0, paramTbl.getColumnCount() - 1);
        }
    }

    private void deleteButtonActionPerformed(ActionEvent e)
    {
        theParameters.removeRows(paramTbl.getSelectedRows());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addParamBtn;
    private javax.swing.JButton browseForExeBtn;
    private javax.swing.JButton closeBtn;
    private javax.swing.JPanel commandLinePane;
    private javax.swing.JScrollPane commandLineScrollPane;
    private javax.swing.JTextArea commandlineInput;
    private javax.swing.JLabel commandlineLabel;
    private javax.swing.JButton discardBtn;
    private javax.swing.JTextField exeDefinitionNameInput;
    private javax.swing.JSplitPane exeDetailsSplit;
    private javax.swing.JComboBox exeNameCB;
    private javax.swing.JTextField exePathInput;
    private javax.swing.JPanel executableDefinitionPanel;
    private javax.swing.JButton newBtn;
    private javax.swing.JScrollPane paramPane;
    private javax.swing.JPopupMenu paramPopup;
    private javax.swing.JCheckBox paramPositionalCB;
    private javax.swing.JTable paramTbl;
    // End of variables declaration//GEN-END:variables

}
