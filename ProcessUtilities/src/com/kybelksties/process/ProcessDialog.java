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

import com.kybelksties.general.DateUtils;
import com.kybelksties.general.FileUtilities;
import com.kybelksties.general.StringUtils;
import com.kybelksties.gui.OutputPanel;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;

/**
 * A dialog that show the standard output and error output of a process.
 *
 * @author kybelksd
 */
public class ProcessDialog extends javax.swing.JDialog
{

    private static final Class CLAZZ = ProcessDialog.class;
    private static final String CLASS_NAME = CLAZZ.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    static class StreamGobbler extends Thread
    {

        InputStream inStream;
        String fileName;
        InstreamType type;
        final ScheduledProcess schedProcess;
        DefaultCaret caret;
        StyledDocument doc;
        OutputPanel outputPanel;

        // reads everything from inStream until empty.
        StreamGobbler(OutputPanel outputPanel,
                      ScheduledProcess schedProcess,
                      InstreamType type,
                      StyleContext styleContext)
        {
            // No checks for null, as we are the only user of the class and
            // we do everything right!
            this.outputPanel = outputPanel;
            this.type = type;
            this.inStream = (this.type == null ||
                             this.type == InstreamType.InputStream) ?
                            schedProcess.getProcess().getInputStream() :
                            schedProcess.getProcess().getErrorStream();

            this.fileName = schedProcess.getLogFileName();
            this.schedProcess = schedProcess;
        }

        @Override
        public void run()
        {
            try
            {
                InputStreamReader isr = new InputStreamReader(inStream);
                BufferedReader br = new BufferedReader(isr);
                String line;
                while (isAlive() && schedProcess.isRunning())
                {
                    if ((line = br.readLine()) != null)
                    {
                        line = DateUtils.logTimestamp() + ": " + type + " :" +
                               line;
                        outputPanel.writeln(line);
                    }
                }
                outputPanel.writelnMeta(
                        NbBundle.getMessage(
                                CLAZZ,
                                "ProcessDialog.noMoreOutputOnStream",
                                type));
            }
            catch (IOException ioe)
            {
                outputPanel.writelnError(
                        NbBundle.getMessage(
                                CLAZZ,
                                "ProcessDialog.exceptionOnOutputOnStream",
                                DateUtils.logTimestamp(),
                                StringUtils.NEWLINE,
                                ioe));
            }
            try
            {
                if (schedProcess.getLogFileName() != null)
                {
                    FileUtilities.saveText(schedProcess.getLogFileName() +
                                           DateUtils.file_now() + ".log",
                                           outputPanel.getText());
                }
            }
            catch (IOException | BadLocationException ex)
            {
                LOGGER.log(Level.INFO, ex.toString());
            }
        }

        enum InstreamType
        {

            InputStream, ErrorStream
        }

    }

    ScheduledProcess schedProcess = null;
    StreamGobbler instreamGobbler = null;
    StreamGobbler errstreamGobbler = null;
    StyleContext styleContext = new StyleContext();
    final StyledDocument doc;

    static Style metaStyle;

    private ProcessDialog(java.awt.Frame parent, ScheduledProcess schedProcess)
    {
        super(parent, false);
        initComponents();
        setTitle(schedProcess.getExeDefinition().getName());

        this.schedProcess = schedProcess;
        doc = outputPanel.getStyledDocument();

        startProcess();
        // Define a default background color attribute
        Color backgroundColor = Color.red;
        SimpleAttributeSet background = new SimpleAttributeSet();
        StyleConstants.setBackground(background, backgroundColor);
        doc.setParagraphAttributes(0,
                                   doc.getLength(),
                                   background,
                                   false);

        outputPanel.writelnMeta(schedProcess.toString());
    }

    private void startProcess()
    {
        outputPanel.writelnMeta(
                NbBundle.getMessage(CLAZZ,
                                    "ProcessDialog.startProcess",
                                    StringUtils.NEWLINE,
                                    StringUtils.NEWLINE,
                                    StringUtils.NEWLINE));
        if (!schedProcess.isRunning())
        {
            schedProcess.getProcess().start();
            instreamGobbler = new StreamGobbler(outputPanel,
                                                this.schedProcess,
                                                StreamGobbler.InstreamType.InputStream,
                                                styleContext);
            errstreamGobbler = new StreamGobbler(outputPanel,
                                                 this.schedProcess,
                                                 StreamGobbler.InstreamType.ErrorStream,
                                                 styleContext);
            instreamGobbler.start();
            errstreamGobbler.start();
        }
        else
        {
            outputPanel.writelnMeta(
                    NbBundle.getMessage(CLAZZ,
                                        "ProcessDialog.alreadyRunning",
                                        StringUtils.NEWLINE,
                                        StringUtils.NEWLINE,
                                        StringUtils.NEWLINE));
        }
    }

    private void stopProcess()
    {
        outputPanel.writelnMeta(
                NbBundle.getMessage(CLAZZ,
                                    "ProcessDialog.stoppingProcess",
                                    StringUtils.NEWLINE,
                                    StringUtils.NEWLINE,
                                    StringUtils.NEWLINE));
        schedProcess.getProcess().destroy();
    }

    /**
     * Creates new form ProcessDialog.
     *
     * @param parent       parent frame
     * @param schedProcess the scheduled process
     * @return the new ProcessDialog
     */
    static public ProcessDialog makeProcessDialog(java.awt.Frame parent,
                                                  ScheduledProcess schedProcess)
    {
        ProcessDialog dlg = new ProcessDialog(parent, schedProcess);

        dlg.setVisible(true);

        return dlg;
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

        outputPane = new javax.swing.JTabbedPane();
        outputPanel = new com.kybelksties.gui.OutputPanel();
        jPanel2 = new javax.swing.JPanel();
        menyBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        saveMenuItem = new javax.swing.JMenuItem();
        processMenu = new javax.swing.JMenu();
        stopMenuItem = new javax.swing.JMenuItem();
        startMenuItem = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.LINE_AXIS));

        outputPane.addTab(org.openide.util.NbBundle.getMessage(ProcessDialog.class, "ProcessDialog.outputPanel.TabConstraints.tabTitle"), outputPanel); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 395, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 273, Short.MAX_VALUE)
        );

        outputPane.addTab(org.openide.util.NbBundle.getMessage(ProcessDialog.class, "ProcessDialog.jPanel2.TabConstraints.tabTitle"), jPanel2); // NOI18N

        getContentPane().add(outputPane);

        fileMenu.setText(org.openide.util.NbBundle.getMessage(ProcessDialog.class, "ProcessDialog.fileMenu.text")); // NOI18N

        saveMenuItem.setText(org.openide.util.NbBundle.getMessage(ProcessDialog.class, "ProcessDialog.saveMenuItem.text")); // NOI18N
        saveMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                saveMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(saveMenuItem);

        processMenu.setText(org.openide.util.NbBundle.getMessage(ProcessDialog.class, "ProcessDialog.processMenu.text")); // NOI18N

        stopMenuItem.setText(org.openide.util.NbBundle.getMessage(ProcessDialog.class, "ProcessDialog.stopMenuItem.text")); // NOI18N
        stopMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                stopMenuItemActionPerformed(evt);
            }
        });
        processMenu.add(stopMenuItem);

        startMenuItem.setText(org.openide.util.NbBundle.getMessage(ProcessDialog.class, "ProcessDialog.startMenuItem.text")); // NOI18N
        startMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                startMenuItemActionPerformed(evt);
            }
        });
        processMenu.add(startMenuItem);

        fileMenu.add(processMenu);

        menyBar.add(fileMenu);

        editMenu.setText(org.openide.util.NbBundle.getMessage(ProcessDialog.class, "ProcessDialog.editMenu.text")); // NOI18N
        menyBar.add(editMenu);

        setJMenuBar(menyBar);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void saveMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_saveMenuItemActionPerformed
    {//GEN-HEADEREND:event_saveMenuItemActionPerformed
        final JFileChooser fc = new JFileChooser("/tmp");
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.setFileFilter(new FileNameExtensionFilter("*.log", "log"));
        int reval = fc.showSaveDialog(this);
        if (reval == JFileChooser.APPROVE_OPTION)
        {
            File file = fc.getSelectedFile();
            String logFileName = file.getAbsolutePath() +
                                 "/" +
                                 getTitle() +
                                 "_" +
                                 DateUtils.file_now() +
                                 ".log";
            try
            {
                int last = doc.getEndPosition().getOffset();
                String str = doc.getText(0, last);
                FileUtilities.saveText(logFileName, str);
            }
            catch (IOException | BadLocationException ex)
            {
                Exceptions.printStackTrace(ex);
            }
        }
    }//GEN-LAST:event_saveMenuItemActionPerformed

    private void stopMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_stopMenuItemActionPerformed
    {//GEN-HEADEREND:event_stopMenuItemActionPerformed
        stopProcess();
    }//GEN-LAST:event_stopMenuItemActionPerformed

    private void startMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_startMenuItemActionPerformed
    {//GEN-HEADEREND:event_startMenuItemActionPerformed
        startProcess();
    }//GEN-LAST:event_startMenuItemActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu editMenu;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JMenuBar menyBar;
    private javax.swing.JTabbedPane outputPane;
    private com.kybelksties.gui.OutputPanel outputPanel;
    private javax.swing.JMenu processMenu;
    private javax.swing.JMenuItem saveMenuItem;
    private javax.swing.JMenuItem startMenuItem;
    private javax.swing.JMenuItem stopMenuItem;
    // End of variables declaration//GEN-END:variables
}
