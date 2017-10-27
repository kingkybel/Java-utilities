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

import java.awt.Component;
import java.awt.event.ActionListener;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import org.openide.explorer.propertysheet.ExPropertyEditor;
import org.openide.explorer.propertysheet.InplaceEditor;
import org.openide.explorer.propertysheet.PropertyEnv;
import org.openide.explorer.propertysheet.PropertyModel;

/**
 * Inplace editor for the selection of locales.
 *
 * @author kybelksd
 */
public class LocaleEditor
        extends PropertyEditorSupport
        implements ExPropertyEditor,
                   InplaceEditor.Factory
{
    
    private static final Class CLAZZ = LocaleEditor.class;
    private static final String CLASS_NAME = CLAZZ.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);
    
    private InplaceEditor ed = null;

    @Override
    public String[] getTags()
    {
        if (ed != null && ed.getComponent() != null)
        {
            return ((LocaleComboBox) ed.getComponent()).
                    getChoicesAsStrings();
        }
        return null;
    }

    @Override
    public boolean isPaintable()
    {
        return true;
    }

    @Override
    public void paintValue(java.awt.Graphics gfx, java.awt.Rectangle box)
    {
//        LocaleComboBox cb = (LocaleComboBox) ed.getComponent();
//        if (cb != null)
//        {
//            String value = (String) cb.getSelectedItem();
//            if (value == null)
//            {
//                cb.setSelectedIndex(0);
//                value = (String) cb.getSelectedItem();
//            }
//            cb.getIcon(value).paintIcon(null, gfx, 1, 1);
//    }
    }

    /**
     * Attach a property environment to this editor.
     *
     * @param pe the property environment
     */
    @Override
    public void attachEnv(PropertyEnv pe)
    {
        pe.registerInplaceEditorFactory(this);
    }

    /**
     * Retrieve the inplace editor.
     *
     * @return the editor
     */
    @Override
    public InplaceEditor getInplaceEditor()
    {
        if (ed == null)
        {
            ed = new Inplace();
        }
        return ed;
    }

    @Override
    public Component getCustomEditor()
    {
        return getInplaceEditor().getComponent();
    }

    @Override
    public boolean supportsCustomEditor()
    {
        return true;
    }

    @Override
    public String getAsText()
    {
        return ((LocaleComboBox) getCustomEditor()).getSelectedItem().
                toString();
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException
    {
        ((LocaleComboBox) getCustomEditor()).selectString(text);

    }

    private static class Inplace implements InplaceEditor
    {

        LocaleComboBox localeComboBox = new LocaleComboBox();
        private PropertyEditor editor = null;
        private PropertyModel model;

        @Override
        public void connect(PropertyEditor propertyEditor, PropertyEnv env)
        {
            editor = propertyEditor;
            reset();
        }

        @Override
        public JComponent getComponent()
        {
            return localeComboBox;
        }

        @Override
        public void clear()
        {
            // avoid memory leaks
            editor = null;
            model = null;
        }

        @Override
        public Object getValue()
        {
            return localeComboBox.getSelectedItem();
        }

        @Override
        public void setValue(Object object)
        {
            localeComboBox.setSelectedItem(object);
        }

        @Override
        public boolean supportsTextEntry()
        {
            return true;
        }

        @Override
        public void reset()
        {
            String value = editor.getValue().toString();
            if (value != null)
            {
                localeComboBox.setSelectedItem(value);
            }
        }

        @Override
        public KeyStroke[] getKeyStrokes()
        {
            return new KeyStroke[0];
        }

        @Override
        public PropertyEditor getPropertyEditor()
        {
            return editor;
        }

        @Override
        public PropertyModel getPropertyModel()
        {
            return model;
        }

        @Override
        public void setPropertyModel(PropertyModel propertyModel)
        {
            this.model = propertyModel;
        }

        @Override
        public boolean isKnownComponent(Component component)
        {
            return component == localeComboBox ||
                   localeComboBox.isAncestorOf(component);
        }

        @Override
        public void addActionListener(ActionListener actionListener)
        {
            // do nothing - not needed for this component
        }

        @Override
        public void removeActionListener(ActionListener actionListener)
        {
            // do nothing - not needed for this component
        }
    }

}
