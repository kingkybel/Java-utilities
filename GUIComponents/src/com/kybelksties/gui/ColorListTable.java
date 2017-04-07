/*
 * @author  Dieter J Kybelksties
 * @date Mar 31, 2017
 *
 */
package com.kybelksties.gui;

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
        try
        {
            setModel(colorList == null ? new ColorList() : colorList);
        }
        catch (ColorList.Exception ex)
        {
            Exceptions.printStackTrace(ex);
        }

        ice = new IndividualCellEditor(this);
        ice.insertButtonEditor("ColorEditor", new ColorButton());
        ice.insertSpinnerEditor("RatioEditor", 1.0, 0.001, 100.0, 1.0);
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

    @Override
    public final void setModel(TableModel dataModel)
    {
        // only allow color-list table models
        super.setModel((dataModel instanceof ColorList) ? dataModel : null);
        initialize((ColorList) ((dataModel instanceof ColorList) ?
                                dataModel :
                                null));
    }

}
