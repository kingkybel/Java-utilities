package com.kybelksties.gui;

/*
 * @author  Dieter J Kybelksties
 * @date May 5, 2016
 *
 */
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

    private static final String CLASS_NAME = DateChooserPopup.class.getName();
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
