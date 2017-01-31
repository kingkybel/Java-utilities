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
package com.kybelksties.gui;

import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import org.openide.util.NbBundle;

/**
 *
 * @author Dieter J Kybelksties
 */
public class OutputPanel extends javax.swing.JPanel
{

    private static final Class CLAZZ = OutputPanel.class;
    private static final String CLASS_NAME = CLAZZ.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    public static final String NEWLINE = System.getProperty("line.separator");

    public enum Styles
    {

        NORMAL, HIGHLIGHT, META, ERROR;

        @Override
        public String toString()
        {
            return this == NORMAL ?
                   NbBundle.getMessage(CLAZZ, "OutputPanel.Styles.normal") :
                   this == HIGHLIGHT ?
                   NbBundle.getMessage(CLAZZ, "OutputPanel.Styles.highlight") :
                   this == META ?
                   NbBundle.getMessage(CLAZZ, "OutputPanel.Styles.meta") :
                   this == ERROR ?
                   NbBundle.getMessage(CLAZZ, "OutputPanel.Styles.error") :
                   NbBundle.getMessage(CLAZZ, "OutputPanel.Styles.undefined");
        }
    }
    private StyleContext styleContext = new StyleContext();
    private StyledDocument doc;
    private boolean verbose = true;
    private String defaultFontFamily = (String) fontFamilyModel.getElementAt(0);
    private Integer defaultFontSize = 10;
    private Color defaultForeground = Color.BLACK;
    private Color defaultBackground = Color.WHITE;
    private boolean defaultBold = false;
    private boolean defaultItalic = false;

    static DefaultComboBoxModel fontFamilyModel = new DefaultComboBoxModel(
                                GraphicsEnvironment.
                                getLocalGraphicsEnvironment().
                                getAvailableFontFamilyNames());

    /**
     * Creates new form OutputPanel.
     */
    public OutputPanel()
    {
        initComponents();
        reset();
        resetDocument();
        textScrollPane.getVerticalScrollBar().addAdjustmentListener(
                new AdjustmentListener()
                {
                    int prevMax = 0;

                    @Override
                    public void adjustmentValueChanged(AdjustmentEvent e)
                    {
                        if (e.getAdjustable().getMaximum() != prevMax)
                        {
                            e.getAdjustable().setValue(
                                    e.getAdjustable().getMaximum());
                            prevMax = e.getAdjustable().getMaximum();
                        }
                    }
                });
        DefaultCaret caret = (DefaultCaret) textPane.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
    }

    /**
     * Get the value of verbose.
     *
     * @return the value of verbose
     */
    public boolean isVerbose()
    {
        return verbose;
    }

    /**
     * Set the value of verbose.
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
        reset();
    }

    /**
     * Get the value of defaultForeground.
     *
     * @return the value of defaultForeground
     */
    public Color getDefaultForeground()
    {
        return defaultForeground;
    }

    /**
     * Set the value of defaultForeground.
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
        reset();
    }

    /**
     * Get the value of defaultBackground.
     *
     * @return the value of defaultBackground
     */
    public Color getDefaultBackground()
    {
        return defaultBackground;
    }

    /**
     * Set the value of defaultBackground.
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
        reset();
    }

    /**
     * Get the value of defaultFontSize.
     *
     * @return the value of defaultFontSize
     */
    public Integer getDefaultFontSize()
    {
        return defaultFontSize;
    }

    /**
     * Set the value of defaultFontSize.
     *
     * @param defaultFontSize new value of defaultFontSize
     */
    public void setDefaultFontSize(Integer defaultFontSize)
    {
        Integer old = this.defaultFontSize;
        this.defaultFontSize = defaultFontSize;
        if ((old == null) != (defaultFontSize == null) ||
            old != null && !old.equals(this.defaultFontSize))
        {
            firePropertyChange("defaultFontSize", old, this.defaultFontSize);
        }
        reset();
    }

    /**
     * Get the value of defaultFontFamily.
     *
     * @return the value of defaultFontFamily
     */
    public String getDefaultFontFamily()
    {
        return defaultFontFamily;
    }

    /**
     * Set the value of defaultFontFamily.
     *
     * @param defaultFontFamily new value of defaultFontFamily
     */
    public void setDefaultFontFamily(String defaultFontFamily)
    {
        if (fontFamilyModel.getIndexOf(defaultFontFamily) == -1)
        {
            LOGGER.log(Level.INFO,
                       NbBundle.
                       getMessage(CLAZZ, "OutputPanel.noSuchFontFamily"),
                       defaultFontFamily);
            boolean found = false;
            for (int i = 0; i < fontFamilyModel.getSize() && !found; i++)
            {
                String localFF = defaultFontFamily.replaceAll(" ", "").
                       toLowerCase();
                String ff_i = ((String) fontFamilyModel.getElementAt(i)).
                       toLowerCase().replaceAll(" ", "");
                if (localFF.equals(ff_i))
                {
                    defaultFontFamily = (String) fontFamilyModel.getElementAt(i);
                    LOGGER.log(Level.INFO,
                               NbBundle.getMessage(CLAZZ,
                                                   "OutputPanel.fontFamilySetTo",
                                                   defaultFontFamily)
                    );
                    found = true;
                }
            }
            if (!found)
            {
                defaultFontFamily = (String) fontFamilyModel.getElementAt(0);
                LOGGER.log(Level.INFO,
                           NbBundle.getMessage(CLAZZ,
                                               "OutputPanel.fontFamilySetTo",
                                               defaultFontFamily)
                );
            }

        }
        this.defaultFontFamily = defaultFontFamily;
        reset();
    }

    /**
     * Retrieve the text on this panel as string.
     *
     * @return the text
     */
    public String getText() throws BadLocationException
    {
        String reval = "";
        Document doc = textPane.getDocument();
        if (doc != null)
        {
            reval = doc.getText(0, doc.getLength());
        }
        return reval;
    }

    /**
     * Get the value of defaultBold.
     *
     * @return the value of defaultBold
     */
    public Boolean isDefaultBold()
    {
        return defaultBold;
    }

    /**
     * Set the value of defaultBold.
     *
     * @param defaultBold new value of defaultBold
     */
    public void setDefaultBold(Boolean defaultBold)
    {
        this.defaultBold = defaultBold;
        reset();
    }

    /**
     * Get the value of defaultItalic.
     *
     * @return the value of defaultItalic
     */
    public Boolean isDefaultItalic()
    {
        return defaultItalic;
    }

    /**
     * Set the value of defaultItalic.
     *
     * @param defaultItalic new value of defaultItalic
     */
    public void setDefaultItalic(Boolean defaultItalic)
    {
        this.defaultItalic = defaultItalic;
        reset();
    }

    /**
     * Retrieve the document from the panel.
     *
     * @return the styled document
     */
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
                    NbBundle.getMessage(CLAZZ, "OutputPanel.noKeyForStyle"));
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
        String stylename = key.toString();
        Style newStyle = styleContext.addStyle(stylename, null);
        newStyle.addAttribute(StyleConstants.Foreground, fgColor);
        newStyle.addAttribute(StyleConstants.Background, bgColor);
        newStyle.addAttribute(StyleConstants.FontSize, fontSize);
        newStyle.addAttribute(StyleConstants.FontFamily, fontFamily);
        newStyle.addAttribute(StyleConstants.Bold, bold);
        newStyle.addAttribute(StyleConstants.Italic, italic);

        return newStyle;
    }

    public final void resetDocument()
    {
        doc = textPane.getStyledDocument();
        try
        {
            doc.remove(0, doc.getLength());
        }
        catch (BadLocationException ex)
        {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Reset the styles of the document using new basic characteristics.
     */
    public final void reset(/*String fontFamily, Integer fontSize*/)
    {
        try
        {
            doc = textPane.getStyledDocument();

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
            textPane.setForeground(getDefaultForeground());

            SwingUtilities.updateComponentTreeUI(this);
            textPane.setDocument(doc);
            styleContext = new StyleContext();

            addStyle(Styles.NORMAL,
                     getDefaultForeground(),
                     getDefaultBackground(),
                     defaultFontFamily,
                     defaultFontSize,
                     false,
                     false);
            addStyle(Styles.HIGHLIGHT,
                     getDefaultBackground(),
                     getDefaultForeground(),
                     defaultFontFamily,
                     defaultFontSize,
                     true,
                     false);
            addStyle(Styles.META,
                     Color.GRAY,
                     getDefaultBackground(),
                     defaultFontFamily,
                     defaultFontSize,
                     false,
                     true);
            addStyle(Styles.ERROR,
                     Color.YELLOW,
                     Color.RED,
                     defaultFontFamily,
                     defaultFontSize,
                     true,
                     false);

        }
        catch (Exception ex)
        {
            LOGGER.log(Level.SEVERE, null, ex);
        }

    }

    private void appendToDocument(Styles styleKey, String text)
    {
        appendToDocument(styleContext.getStyle(styleKey.toString()), text);
    }

    /**
     * Place text in the text area.
     *
     * @param text the text to append
     */
    void appendToDocument(Style style, String text)
    {
        try
        {
            doc.insertString(doc.getEndPosition().getOffset(), text, style);
        }
        catch (BadLocationException ex)
        {
            LOGGER.log(Level.INFO, ex.toString());
        }
    }

    /**
     * Append the text to the end of the document in the meta/comment style. Do
     * add a line-break at the end.
     *
     * @param text the text to append
     */
    public final void writelnMeta(String text)
    {
        if (isVerbose())
        {
            appendToDocument(Styles.META, "// " + text + NEWLINE);
        }
    }

    /**
     * Append the text to the end of the document using the given style. Do NOT
     * add a line-break at the end.
     *
     * @param style the text style
     * @param text  the text to append
     */
    public final void write(Style style, String text)
    {
        appendToDocument(style, text);
    }

    /**
     * Append the text to the end of the document using the given style. Do add
     * a line-break at the end.
     *
     * @param style the text style
     * @param text  the text to append
     */
    public final void writeln(Style style, String text)
    {
        appendToDocument(style, text + NEWLINE);
    }

    /**
     * Append the text to the end of the document using the style identified by
     * its key. Do NOT add a line-break at the end.
     *
     * @param styleKey key identifying the text style
     * @param text     the text to append
     */
    public final void write(Comparable styleKey, String text)
    {
        appendToDocument(styleContext.getStyle(styleKey.toString()), text);
    }

    /**
     * Append the text to the end of the document using the style identified by
     * its key. Do add a line-break at the end.
     *
     * @param styleKey key identifying the text style
     * @param text     the text to append
     */
    public final void writeln(Comparable styleKey, String text)
    {
        appendToDocument(styleContext.getStyle(styleKey.toString()),
                         text + NEWLINE);
    }

    /**
     * Append the text to the end of the document in the default style. Do NOT
     * add a line-break at the end.
     *
     * @param text the text to append
     */
    public final void write(String text)
    {
        appendToDocument(styleContext.getStyle(Styles.NORMAL.toString()), text);
    }

    /**
     * Append the text to the end of the document in the default style. Add a
     * line-break at the end.
     *
     * @param text the text to append
     */
    public final void writeln(String text)
    {
        appendToDocument(styleContext.getStyle(Styles.NORMAL.toString()),
                         text + NEWLINE);
    }

    /**
     * Append the text to the end of the document in the highlight style. Do NOT
     * add a line-break at the end.
     *
     * @param text the text to append
     */
    public final void writeHighlight(String text)
    {
        appendToDocument(styleContext.getStyle(Styles.HIGHLIGHT.toString()),
                         text);
    }

    /**
     * Append the text to the end of the document in the highlight style. Do add
     * a line-break at the end.
     *
     * @param text the text to append
     */
    public final void writelnHighlight(String text)
    {
        appendToDocument(styleContext.getStyle(Styles.HIGHLIGHT.toString()),
                         text + NEWLINE);
    }

    /**
     * Append the text to the end of the document in the error style. Do add a
     * line-break at the end.
     *
     * @param text the text to append
     */
    public final void writelnError(String text)
    {
        appendToDocument(styleContext.getStyle(Styles.ERROR.toString()),
                         "!!! " + text + "!!!" + NEWLINE);
    }

    void showStyleSamples()
    {
        final Enumeration<?> styleNames = styleContext.getStyleNames();

        while (styleNames.hasMoreElements())
        {
            String name = (String) styleNames.nextElement();

            String sampleString = "";
            final Enumeration<?> attributeNames =
                                 styleContext.getStyle(name).getAttributeNames();

            while (attributeNames.hasMoreElements())
            {
                Object attrName = attributeNames.nextElement();
                sampleString += attrName.toString() +
                                "=" +
                                styleContext.getStyle(name).getAttribute(
                                        attrName) +
                                " ";
            }
            writeln(name, sampleString);

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

        textScrollPane = new javax.swing.JScrollPane();
        textPane = new javax.swing.JTextPane();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));

        textPane.setEditable(false);
        textScrollPane.setViewportView(textPane);

        add(textScrollPane);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextPane textPane;
    private javax.swing.JScrollPane textScrollPane;
    // End of variables declaration//GEN-END:variables
}
