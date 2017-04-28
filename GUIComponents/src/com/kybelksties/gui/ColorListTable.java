/*
 * Copyright (C) 2017 Dieter J Kybelksties
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
 * @date: 2017-03-31
 * @author: Dieter J Kybelksties
 */
package com.kybelksties.gui;

import java.awt.Color;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import org.openide.util.Exceptions;

/**
 *
 * @author Dieter J Kybelksties
 */
public class ColorListTable extends JTable
{

    private static final Class CLAZZ = ColorListTable.class;
    private static final String CLASS_NAME = CLAZZ.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    private IndividualCellEditor ice = null;

    public ColorListTable()
    {
        initialize(null);
    }

    private void initialize(ColorList colorList)
    {
        ice = new IndividualCellEditor(this);
//        ice.insertButtonEditor("ColorEditor", new ColorButton());
        ice.insertComboEditor("ColorEditor", new Color[]
                      {
                          Color.BLUE, Color.CYAN
        });
        ice.insertSpinnerEditor("RatioEditor", 1.0, 0.001, 100.0, 1.0);
        try
        {
            setModel(colorList == null ? new ColorList() : colorList);
            setCellEditor(ice);
            setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);

            setRowHeight(20);
        }
        catch (ColorList.Exception ex)
        {
            Exceptions.printStackTrace(ex);
        }

    }

    public ColorListTable(ColorList colorList)
    {
        super(colorList);
        initialize(colorList);
    }

    public ColorListTable(ColorList colorList, TableColumnModel columnModel)
    {
        super(colorList, columnModel);
        initialize(colorList);
    }

    public ColorListTable(ColorList colorList,
                          TableColumnModel columnModel,
                          ListSelectionModel slelectionModel)
    {
        super(colorList, columnModel, slelectionModel);
        initialize(colorList);
    }

    public ColorListTable(int numRows, int numColumns)
    {
        super(numRows, numColumns);
        initialize(null);
    }

    /**
     * Get the value of gradientType
     *
     * @return the value of gradientType
     */
    public GradientType getGradientType()
    {
        TableModel model = getModel();
        return model != null && (model instanceof ColorList) ?
               ((ColorList) model).getType() :
               GradientType.UNIFORM;
    }

    /**
     * Set the value of gradientType
     *
     * @param gradientType new value of gradientType
     */
    public void setGradientType(GradientType gradientType)
    {
        TableModel model = getModel();
        if (model == null || !(model instanceof ColorList))
        {
            try
            {
                model = new ColorList();
            }
            catch (ColorList.Exception ex)
            {
                Exceptions.printStackTrace(ex);
            }
        }
        if (model != null)
        {
            ((ColorList) model).setType(gradientType);
            setModel(model);
        }
    }

    @Override
    public final void setModel(TableModel dataModel)
    {
        if (dataModel == null || !(dataModel instanceof ColorList))
        {
            return;
        }
        // only allow color-list table models
        super.setModel(dataModel);
        if (dataModel instanceof ColorList)
        {
            ColorList cl = (ColorList) dataModel;
            switch (cl.getType())
            {
                case DIAGONAL_LEFT_TOP_TO_RIGHT_BOTTOM:
                case DIAGONAL_RIGHT_TOP_TO_LEFT_BOTTOM:
                case CIRCULAR:
                case LEFT_TO_RIGHT:
                case TOP_TO_BOTTOM:
                    ice.setColumnEditor(0, "ColorEditor");
                    ice.setColumnEditor(1, "RatioEditor");
                    break;
                default:
                    for (int col = 0; col < getColumnCount(); col++)
                    {
                        ice.setColumnEditor(col, "ColorEditor");
                    }
            }
            setCellEditor(ice);
            setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);

            setRowHeight(20);
        }
    }

}
