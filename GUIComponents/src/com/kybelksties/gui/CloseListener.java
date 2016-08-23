
/*
 * @author  Dieter J Kybelksties
 * @date Jul 20, 2016
 *
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

    private static final Class<CloseListener> CLAZZ = CloseListener.class;
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
