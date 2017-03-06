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
package com.kybelksties.jung.graphproperties;

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
 * Inplace editor for visual properties of graphs.
 *
 * @author Dieter J Kybelksties
 */
public class VisualGraphPropertiesEditor
        extends PropertyEditorSupport
        implements ExPropertyEditor,
                   InplaceEditor.Factory
{

    private static final Class CLAZZ = VisualGraphPropertiesEditor.class;
    private static final String CLASSNAME = CLAZZ.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASSNAME);

    private InplaceEditor ed = null;

    @Override
    public boolean supportsCustomEditor()
    {
        return true;
    }

    @Override
    public Component getCustomEditor()
    {
        return getInplaceEditor().getComponent();
    }

    @Override
    public boolean isPaintable()
    {
        return true;
    }

    /**
     * Attach this editor with the given a property environment.
     *
     * @param pe property environment
     */
    @Override
    public void attachEnv(PropertyEnv pe)
    {
        pe.registerInplaceEditorFactory(this);
    }

    /**
     * Retrieve the in-place editor.
     *
     * @return the in-place editor
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

    private static class Inplace implements InplaceEditor
    {

        private final VisualGraphPropertiesPanel vgpPanel =
                                                 new VisualGraphPropertiesPanel();
        private PropertyEditor editor = null;
        private PropertyModel model = null;

        @Override
        public void connect(PropertyEditor propertyEditor, PropertyEnv pe1)
        {
            editor = propertyEditor;
            reset();
        }

        @Override
        public JComponent getComponent()
        {
            return vgpPanel;
        }

        @Override
        public void clear()
        {
            // avoid memory leaks:
            editor = null;
            model = null;
        }

        @Override
        public Object getValue()
        {
            return vgpPanel.getVisualGraphProperties();
        }

        @Override
        public void setValue(Object o)
        {
            vgpPanel.setVisualGraphProperties((VisualGraphProperties) o);
        }

        @Override
        public boolean supportsTextEntry()
        {
            return false;
        }

        @Override
        public void reset()
        {
//            Object value = editor.getValue();
        }

        @Override
        public void addActionListener(ActionListener al)
        {
            // do nothing - not needed for this component
        }

        @Override
        public void removeActionListener(ActionListener al)
        {
            // do nothing - not needed for this component
        }

        @Override
        public KeyStroke[] getKeyStrokes()
        {
            return new KeyStroke[1];
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
            return component == vgpPanel || vgpPanel.isAncestorOf(component);
        }
    }

}
