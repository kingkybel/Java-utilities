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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
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
 * Combo-box inplace editor for Cap/Join styles for lines.
 *
 * @author kybelksd
 */
public class CapJoinEditor
        extends PropertyEditorSupport
        implements ExPropertyEditor,
                   InplaceEditor.Factory
{

    private static final Class CLAZZ = CapJoinEditor.class;
    private static final String CLASS_NAME = CLAZZ.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);
    
    private InplaceEditor ed = null;

    @Override
    public void paintValue(Graphics gfx, Rectangle box)
    {
        float[] pattern = (float[]) ((CapJoinComboBox) ed.getComponent()).
                getSelectedItem();
        gfx.setColor(Color.black);
        final Stroke stroke = new BasicStroke(2.0f,
                                              BasicStroke.CAP_BUTT,
                                              BasicStroke.JOIN_MITER,
                                              10.0f,
                                              pattern,
                                              0.0f);
        ((Graphics2D) gfx).setStroke(stroke);
        gfx.setColor(Color.BLACK);
        gfx.drawLine(1, box.height / 2, box.height - 1, 10);
    }

    @Override
    public boolean isPaintable()
    {
        return true;
    }

    @Override
    public String[] getTags()
    {
        if (ed != null && ed.getComponent() != null)
        {
            return ((CapJoinComboBox) ed.getComponent()).getChoicesAsStrings();
        }
        return null;
    }

//    @Override
//    public void paintValue(java.awt.Graphics gfx, java.awt.Rectangle box)
//    {
//        Color oldColor = gfx.getColor();
//        gfx.setColor(Color.black);
//        gfx.drawRect(box.x, box.y, box.width - 3, box.height - 3);
//        gfx.setColor(Color.BLUE);
//        gfx.fillRect(box.x + 1, box.y + 1, box.width - 4, box.height - 4);
//        gfx.setColor(oldColor);
//    }
//
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
        return ((CapJoinComboBox) getCustomEditor()).getSelectedItem().
                toString();
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException
    {
        ((CapJoinComboBox) getCustomEditor()).selectString(text);
    }

    private static class Inplace implements InplaceEditor
    {

        CapJoinComboBox capJoinComboBox = new CapJoinComboBox();
        private PropertyEditor editor = null;
        private PropertyModel model = null;

        @Override
        public void connect(PropertyEditor propertyEditor, PropertyEnv env)
        {
            editor = propertyEditor;
            reset();
        }

        @Override
        public JComponent getComponent()
        {
            return capJoinComboBox;
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
            return capJoinComboBox.getSelectedItem();
        }

        @Override
        public void setValue(Object object)
        {
            capJoinComboBox.setSelectedItem(object);
        }

        @Override
        public boolean supportsTextEntry()
        {
            return true;
        }

        @Override
        public void reset()
        {
            Object value = editor.getValue();
            if (value != null)
            {
                capJoinComboBox.setSelectedItem(value);
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
            return component == capJoinComboBox ||
                   capJoinComboBox.isAncestorOf(component);
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
