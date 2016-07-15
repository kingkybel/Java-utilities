/*
 * @author  Dieter J Kybelksties
 * @date Jul 14, 2016
 *
 */
package com.kybelksties.gui;

import java.awt.Color;
import java.awt.Font;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

/**
 *
 * @author Dieter J Kybelksties
 */
public class OutputPanel extends javax.swing.JPanel
{

    private static final String CLASS_NAME = OutputPanel.class.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    public static final String NEWLINE = System.getProperty("line.separator");

    public enum Styles
    {

        NORMAL, HIGHLIGHT, META, ERROR
    }
    StyleContext styleContext = new StyleContext();
    StyledDocument doc;
    TreeMap<Comparable, Style> styles = new TreeMap<>();
    private boolean verbose = true;
    private String defaultFontFamily = "courier";
    private int defaultFontSize = 10;
    private Color defaultForeground = Color.GREEN;
    private Color defaultBackground = Color.MAGENTA;
    private boolean defaultBold = false;
    private boolean defaultItalic = false;

    /**
     * Creates new form OutputPanel.
     */
    public OutputPanel()
    {
        initComponents();
        reset();
    }

    /**
     * Get the value of verbose
     *
     * @return the value of verbose
     */
    public boolean isVerbose()
    {
        return verbose;
    }

    /**
     * Set the value of verbose
     *
     * @param verbose new value of verbose
     */
    public void setVerbose(boolean verbose)
    {
        boolean old = this.verbose;
        this.verbose = verbose;
        if (old != this.verbose)
        {
            firePropertyChange("verbose", old, this.verbose);
        }
    }

    /**
     * Get the value of defaultForeground
     *
     * @return the value of defaultForeground
     */
    public Color getDefaultForeground()
    {
        return defaultForeground;
    }

    /**
     * Set the value of defaultForeground
     *
     * @param defaultForeground new value of defaultForeground
     */
    public void setDefaultForeground(Color defaultForeground)
    {
        Color old = this.defaultForeground;
        this.defaultForeground = defaultForeground;
        if ((old == null) != (defaultForeground == null) ||
            old != null && !old.equals(this.defaultForeground))
        {
            firePropertyChange("defaultForeground", old, this.defaultForeground);
        }
        textPane.setForeground(defaultForeground);
    }

    /**
     * Get the value of defaultBackground
     *
     * @return the value of defaultBackground
     */
    public Color getDefaultBackground()
    {
        return defaultBackground;
    }

    /**
     * Set the value of defaultBackground
     *
     * @param defaultBackground new value of defaultBackground
     */
    public void setDefaultBackground(Color defaultBackground)
    {
        Color old = this.defaultBackground;
        this.defaultBackground = defaultBackground;
        if ((old == null) != (defaultBackground == null) ||
            old != null && !old.equals(this.defaultBackground))
        {
            firePropertyChange("defaultBackground", old, this.defaultBackground);
        }
        textPane.setBackground(defaultBackground);
    }

    /**
     * Get the value of defaultFontSize
     *
     * @return the value of defaultFontSize
     */
    public Integer getDefaultFontSize()
    {
        return defaultFontSize;
    }

    /**
     * Set the value of defaultFontSize
     *
     * @param defaultFontSize new value of defaultFontSize
     */
    public void setDefaultFontSize(Integer defaultFontSize)
    {
        this.defaultFontSize = defaultFontSize;
    }

    /**
     * Get the value of defaultFontFamily
     *
     * @return the value of defaultFontFamily
     */
    public String getDefaultFontFamily()
    {
        return defaultFontFamily;
    }

    /**
     * Set the value of defaultFontFamily
     *
     * @param defaultFontFamily new value of defaultFontFamily
     */
    public void setDefaultFontFamily(String defaultFontFamily)
    {
        this.defaultFontFamily = defaultFontFamily;
    }

    /**
     * Get the value of defaultBold
     *
     * @return the value of defaultBold
     */
    public Boolean isDefaultBold()
    {
        return defaultBold;
    }

    /**
     * Set the value of defaultBold
     *
     * @param defaultBold new value of defaultBold
     */
    public void setDefaultBold(Boolean defaultBold)
    {
        this.defaultBold = defaultBold;
    }

    /**
     * Get the value of defaultItalic
     *
     * @return the value of defaultItalic
     */
    public Boolean isDefaultItalic()
    {
        return defaultItalic;
    }

    /**
     * Set the value of defaultItalic
     *
     * @param defaultItalic new value of defaultItalic
     */
    public void setDefaultItalic(Boolean defaultItalic)
    {
        this.defaultItalic = defaultItalic;
    }

    public StyledDocument getStyledDocument()
    {
        return textPane.getStyledDocument();
    }

    /**
     * Add a new style to be used on this panel.
     *
     * @param key        identifying key to retrieve the style
     * @param fgColor    foreground color
     * @param bgColor    background color
     * @param fontFamily string describing the family
     * @param fontSize   the size of the font
     * @param bold       bold weight if true, normal otherwise
     * @param italic     italic if true, upright (roman) otherwise
     * @return the new style
     * @throws java.lang.Exception thrown if the key is null
     */
    public Style addStyle(Comparable key,
                          Color fgColor,
                          Color bgColor,
                          String fontFamily,
                          Integer fontSize,
                          Boolean bold,
                          Boolean italic) throws Exception
    {
        if (key == null)
        {
            throw new Exception(
                    "Cannot add a style without key to later identify it");
        }
        if (fgColor == null)
        {
            fgColor = getDefaultForeground();
        }
        if (bgColor == null)
        {
            bgColor = getDefaultBackground();
        }
        if (fontFamily == null)
        {
            fontFamily = getDefaultFontFamily();
        }
        if (fontSize == null)
        {
            fontSize = getDefaultFontSize();
        }
        if (bold == null)
        {
            bold = isDefaultBold();
        }
        if (italic == null)
        {
            italic = isDefaultItalic();
        }
        String stylename = fgColor + "_" +
                           bgColor + "_" +
                           fontFamily + "_" +
                           fontSize.toString() + "_" +
                           (bold ? "bold" : "normal") + "_" +
                           (italic ? "italic" : "roman");
        Style newStyle = styleContext.addStyle(stylename, null);
        newStyle.addAttribute(StyleConstants.Foreground, fgColor);
        newStyle.addAttribute(StyleConstants.Background, bgColor);
        newStyle.addAttribute(StyleConstants.FontSize, fontSize);
        newStyle.addAttribute(StyleConstants.FontFamily, fontFamily);
        newStyle.addAttribute(StyleConstants.Bold, bold);
        newStyle.addAttribute(StyleConstants.Italic, italic);
        styles.put(key, newStyle);

        return newStyle;
    }

    public final void reset()
    {
        reset(null, null);
    }

    public final void reset(String fontFamily)
    {
        reset(fontFamily, null);
    }

    public void reset(String fontFamily, Integer fontSize)
    {
        try
        {
            Font font = getFont();
            setDefaultFontFamily(font.getFamily());
            setDefaultFontSize(font.getSize());
            setDefaultBold(font.isBold());
            setDefaultItalic(font.isItalic());
//
//            textPane.setBackground(getDefaultBackground());
//            textPane.setForeground(getDefaultForeground());

            doc = textPane.getStyledDocument();
            doc.remove(0, doc.getLength());

            SimpleAttributeSet attrs = new SimpleAttributeSet();
            StyleConstants.setForeground(attrs, getDefaultForeground());
            StyleConstants.setBackground(attrs, getDefaultBackground());
            doc.setParagraphAttributes(0, doc.getLength(), attrs, false);
            UIDefaults defaults = new UIDefaults();
            defaults.put("TextPane[Enabled].backgroundPainter",
                         getDefaultBackground());
            textPane.putClientProperty("Nimbus.Overrides", defaults);
            textPane.putClientProperty("Nimbus.Overrides.InheritDefaults", true);
            textPane.setBackground(getDefaultBackground());
            textPane.setBackground(getDefaultForeground());

            SwingUtilities.updateComponentTreeUI(this);
            textPane.setDocument(doc);
            if (fontFamily == null)
            {
                fontFamily = defaultFontFamily;
            }
            if (fontSize == null)
            {
                fontSize = defaultFontSize;
            }

            doc = textPane.getStyledDocument();
            doc.remove(0, doc.getLength());

            styles = new TreeMap<>();
            styleContext = new StyleContext();

            addStyle(Styles.NORMAL,
                     defaultForeground,
                     defaultBackground,
                     fontFamily,
                     fontSize,
                     false,
                     false);
            addStyle(Styles.HIGHLIGHT,
                     defaultBackground,
                     defaultForeground,
                     fontFamily,
                     fontSize,
                     true,
                     false);
            addStyle(Styles.META,
                     Color.GRAY,
                     defaultBackground,
                     fontFamily,
                     fontSize,
                     false,
                     true);
            addStyle(Styles.ERROR,
                     Color.YELLOW,
                     Color.RED,
                     fontFamily,
                     fontSize,
                     true,
                     false);

        }
        catch (Exception ex)
        {
            LOGGER.log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Place text in the text area.
     *
     * @param text
     */
    void appendToDocument(Style style, String line)
    {
        try
        {
            doc.insertString(doc.getEndPosition().getOffset(), line, style);
        }
        catch (BadLocationException ex)
        {
            LOGGER.log(Level.INFO, ex.toString());
        }
    }

    /**
     *
     * @param message
     */
    public final void writelnMeta(String message)
    {
        if (isVerbose())
        {
            appendToDocument(styles.get(Styles.META), "// " + message + NEWLINE);
        }
    }

    /**
     *
     * @param style
     * @param message
     */
    public final void write(Style style, String message)
    {
        appendToDocument(style, message);
    }

    /**
     *
     * @param style
     * @param message
     */
    public final void writeln(Style style, String message)
    {
        appendToDocument(styles.get(Styles.NORMAL), message + NEWLINE);
    }

    /**
     *
     * @param styleKey
     * @param message
     */
    public final void write(Comparable styleKey, String message)
    {
        appendToDocument(styles.get(styleKey), message);
    }

    /**
     *
     * @param styleKey
     * @param message
     */
    public final void writeln(Comparable styleKey, String message)
    {
        appendToDocument(styles.get(styleKey), message + NEWLINE);
    }

    /**
     *
     * @param message
     */
    public final void write(String message)
    {
        appendToDocument(styles.get(Styles.NORMAL), message);
    }

    /**
     *
     * @param message
     */
    public final void writeln(String message)
    {
        appendToDocument(styles.get(Styles.NORMAL), message + NEWLINE);
    }

    /**
     *
     * @param message
     */
    public final void writeHighlight(String message)
    {
        appendToDocument(styles.get(Styles.HIGHLIGHT), message);
    }

    /**
     *
     * @param message
     */
    public final void writelnHighlight(String message)
    {
        appendToDocument(styles.get(Styles.HIGHLIGHT), message + NEWLINE);
    }

    /**
     *
     * @param message
     */
    public final void writelnError(String message)
    {
        appendToDocument(styles.get(Styles.ERROR),
                         "!!! " + message + "!!!" + NEWLINE);
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

        textScrollPane = new javax.swing.JScrollPane();
        textPane = new javax.swing.JTextPane();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));

        textScrollPane.setViewportView(textPane);

        add(textScrollPane);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextPane textPane;
    private javax.swing.JScrollPane textScrollPane;
    // End of variables declaration//GEN-END:variables
}
