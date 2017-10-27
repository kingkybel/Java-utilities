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
 * @date: 2017-03-03
 * @author: Dieter J Kybelksties
 */
package com.kybelksties.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.MultipleGradientPaint;
import java.awt.MultipleGradientPaint.CycleMethod;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Logger;
import javax.swing.table.AbstractTableModel;
import org.openide.util.Exceptions;

/**
 * A list of colors. Derived from List, hence implements the methods of List.
 * The class is intended to reduce the number of members variables when defining
 * (multi-color) paints. Also extends AbstractTableModel in order to be able to
 * create the list using a table.
 *
 * @author Dieter J Kybelksties
 */
public final class ColorList
        extends AbstractTableModel
        implements List<Color>
{

    private static final Class CLAZZ = ColorList.class;
    private static final String CLASS_NAME = CLAZZ.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    private GradientType gradientType = GradientType.UNIFORM;
    private float[] ratios = null;
    private CycleMethod cycleMethod = null;
    private Dimension dimension = null;
    ArrayList<Color> colors = null;

    /**
     * Exception derivative to be thrown when preconditions for different
     * gradient types are not met.
     */
    public class Exception extends java.lang.Exception
    {

        /**
         * Default constructor.
         */
        public Exception()
        {
        }

        /**
         * Construct with a message.
         *
         * @param message error message describing the exception
         */
        public Exception(String message)
        {
            super(message);
        }

    }

    /**
     * Default constructor.
     *
     * @throws com.kybelksties.gui.ColorList.Exception
     */
    public ColorList() throws Exception
    {
        this(GradientType.UNIFORM, null, null);
    }

    /**
     * Default constructor.
     *
     * @param other the color list to copy from.
     * @throws com.kybelksties.gui.ColorList.Exception
     */
    public ColorList(ColorList other) throws Exception
    {
        this.gradientType = other.gradientType;
        this.cycleMethod = other.cycleMethod;
        this.dimension = other.dimension;
        System.arraycopy(other.ratios, 0, this.ratios, 0, other.ratios.length);
    }

    /**
     * Construct with one or more colors.
     *
     * @param color  First color in the list
     * @param colors variable list of colors
     * @throws com.kybelksties.gui.ColorList.Exception
     */
    public ColorList(Color color, Color... colors) throws Exception
    {
        this(GradientType.UNIFORM, null, null, colors);
    }

    /**
     * Construct color list of specific gradientType.
     *
     * @param gradientType type of gradient
     * @param cycleMethod
     * @param dimension
     * @param colors       variable list of colors, that will be defaulted if
     *                     necessary
     * @throws com.kybelksties.gui.ColorList.Exception
     */
    public ColorList(GradientType gradientType,
                     CycleMethod cycleMethod,
                     Dimension dimension,
                     Color... colors)
            throws Exception
    {
        this.gradientType = gradientType == null ?
                            GradientType.UNIFORM :
                            gradientType;

        if (colors != null)
        {
            addAll(Arrays.asList(colors));
        }
        setConsistentData();
        setCycleMethod(CycleMethod.NO_CYCLE);
    }

    private void setConsistentData()
    {
        int minNum = gradientType == GradientType.UNIFORM ? 1 :
                     gradientType == GradientType.UNIFORM_2_COLOR ? 2 :
                     gradientType == GradientType.TOP_TO_BOTTOM ? 2 :
                     gradientType == GradientType.LEFT_TO_RIGHT ? 2 :
                     gradientType ==
                     GradientType.DIAGONAL_LEFT_TOP_TO_RIGHT_BOTTOM ? 2 :
                     gradientType ==
                     GradientType.DIAGONAL_RIGHT_TOP_TO_LEFT_BOTTOM ? 2 :
                     gradientType == GradientType.CIRCULAR ?
                     2 :
                     gradientType ==
                     GradientType.FOUR_COLOR_RECTANGULAR ? 4 :
                     gradientType ==
                     GradientType.RANDOM_RASTER ? 5 :
                     1;
        if (size() < minNum)
        {
            int start = size();
            for (int i = start; i < minNum; i++)
            {
                add(ColorUtils.randomColor());
            }
        }
        try
        {
            setRatios();
        }
        catch (Exception ex)
        {
            Exceptions.printStackTrace(ex);
        }
    }

    /**
     * Constructor for a gradient with non-equidistant ratios.
     *
     * @param gradientType type of gradient
     * @param colors       array list of colors, that will be defaulted if
     *                     necessary
     * @param ratios       distance ratios for gradients
     * @throws com.kybelksties.gui.ColorList.Exception
     */
    public ColorList(GradientType gradientType,
                     ArrayList<Color> colors,
                     ArrayList<Float> ratios) throws Exception
    {
        this(gradientType,
             null,
             null,
             (Color[]) (colors != null ? colors.toArray() : null));
        if (ratios != null)
        {
            float[] ratioArray = new float[ratios.size()];
            for (int i = 0; i < ratios.size(); i++)
            {
                ratioArray[i] = ratios.get(i);
            }
            setRatios(ratioArray);
        }
    }

    @Override
    public String getColumnName(int column)
    {
        if (gradientType.equals(GradientType.CIRCULAR) ||
            gradientType.equals(GradientType.DIAGONAL_LEFT_TOP_TO_RIGHT_BOTTOM) ||
            gradientType.equals(GradientType.DIAGONAL_RIGHT_TOP_TO_LEFT_BOTTOM) ||
            gradientType.equals(GradientType.LEFT_TO_RIGHT) ||
            gradientType.equals(GradientType.TOP_TO_BOTTOM))
        {
            return column == 0 ? "Color" : "Ratio";
        }
        else
        {
            return "Color";
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return true;
    }

    /**
     * Set the distance ratios for a gradient.
     *
     * @param gradientType type of gradient
     * @param ratios       new distance ratios for the gradient
     * @throws com.kybelksties.gui.ColorList.Exception the number of colors in
     *                                                 the list is 0 or
     *                                                 gradientType is not a
     *                                                 gradient
     */
    final void setRatios(float... ratios) throws Exception
    {
        setRatiosByAccumulation(ratios);
    }

    /**
     * Set the transparency of all colors in the list to the alpha value.
     *
     * @param alpha a value between 0 and 1, will be defaulted if outside the
     *              range
     * @return an identical list with alpha values changed to alpha
     * @throws com.kybelksties.gui.ColorList.Exception
     */
    public ColorList setTransparency(float alpha) throws Exception
    {
        ColorList reval = new ColorList(this);
        if (alpha < 0.0F || alpha > 1.0F)
        {
            alpha = 1.0F;
        }
        for (int i = 0; i < size(); i++)
        {
            float rgb[] = get(i).getColorComponents(null);
            set(i, new Color(rgb[0], rgb[1], rgb[2], alpha));
        }
        return reval;
    }

    /**
     * Get the color for a uniform "gradient".
     *
     * @return the color
     * @throws com.kybelksties.gui.ColorList.Exception if gradientType is not a
     *                                                 uniform color
     */
    public Color getColor() throws ColorList.Exception
    {
        if (!gradientType.equals(GradientType.UNIFORM))
        {
            throw new Exception(
                    "Single color only defined for uniform color list.");
        }
        return get(0);
    }

    /**
     * Retrieve the gradient gradientType.
     *
     * @return the gradient gradientType
     */
    public GradientType getType()
    {
        return gradientType;
    }

    /**
     * Set the gradient gradientType.
     *
     * @param type the new gradient gradientType
     */
    public void setType(GradientType type)
    {
        if (type == null)
        {
            return;
        }
        if (type != gradientType)
        {

            gradientType = type;
            if (colors == null)
            {
                colors = new ArrayList<>();
            }

            setConsistentData();
        }
        if (gradientType.equals(GradientType.UNIFORM) ||
            gradientType.equals(GradientType.UNIFORM_2_COLOR) ||
            gradientType.equals(GradientType.FOUR_COLOR_RECTANGULAR) ||
            gradientType.equals(GradientType.RANDOM_RASTER))
        {
            ratios = null;
        }
        setConsistentData();
    }

    /**
     * Retrieve the ratios.
     *
     * @return the ratios as float array
     */
    public float[] getRatios()
    {
        return ratios;
    }

    static boolean floatsAreStrictlyMonotone(float[] values)
    {
        // values need to be strictly monotone
        if (values == null || values.length == 0 || values.length == 1)
        {
            return true;
        }
        for (int i = 1; i < values.length; i++)
        {
            if (values[i] < values[i - 1])
            {
                return false;
            }
        }
        return true;
    }

    static boolean floatsAreStrictlyPositive(float[] values)
    {
        // values need to be strictly monotone
        if (values == null || values.length == 0)
        {
            return true;
        }
        for (int i = 0; i < values.length; i++)
        {
            if (values[i] <= 0.0F)
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Set the ratios. Depending on number of values the list might be defaulted
     * or truncated . Then it will be fit into the interval [0.0 .. 1.0].
     *
     * @param ratios the new ratios as float array. Only restriction is that all
     *               values are all > 0
     * @throws com.kybelksties.gui.ColorList.Exception if gradientType is not a
     *                                                 gradient
     */
    public void setRatiosByAccumulation(float[] ratios) throws Exception
    {
        if (gradientType == GradientType.TOP_TO_BOTTOM ||
            gradientType == GradientType.LEFT_TO_RIGHT ||
            gradientType == GradientType.DIAGONAL_LEFT_TOP_TO_RIGHT_BOTTOM ||
            gradientType == GradientType.DIAGONAL_RIGHT_TOP_TO_LEFT_BOTTOM ||
            gradientType == GradientType.CIRCULAR)
        {
            if (size() == 0)
            {
                throw new Exception(
                        "Cannot set ratios if number of colors is 0.");
            }
            // if the ratios array is null then make equidistant
            if (ratios == null)
            {
                this.ratios = new float[size()];
                this.ratios[0] = 1.0F / ((float) this.ratios.length);
                for (int i = 1; i < this.ratios.length - 1; i++)
                {
                    this.ratios[i] = this.ratios[i - 1] +
                                     1.0F / ((float) this.ratios.length);
                }
            }
            else
            {
                if (!floatsAreStrictlyPositive(ratios))
                {
                    throw new Exception(
                            "Can only create ratios with strictly posive values.");
                }
                // make an array of floats matching the number of colors
                this.ratios = new float[size()];
                float sum = 0.0F;
                for (int i = 0; i < size(); i++)
                {
                    // use the given ratio value if possible or default if not
                    this.ratios[i] = sum +
                                     ((i < ratios.length) ?
                                      ratios[i] :
                                      sum / (float) i);
                    sum += this.ratios[i];
                }

                if (this.ratios[this.ratios.length - 1] > 1.0F)
                {
                    // normalise the ratio-values to be monotone from 0.0 to 1.0
                    for (int i = 0; i < size(); i++)
                    {
                        this.ratios[i] /= this.ratios[this.ratios.length - 1];
                    }
                }

            }
            fireTableDataChanged();
        }
    }

    /**
     * Set the ratios. Depending on number of values the list might be defaulted
     * or truncated . Then it will be fit into the interval [0.0 .. 1.0].
     *
     * @param ratios the new ratios as float array. Only restriction is that all
     *               values are all > 0
     * @throws com.kybelksties.gui.ColorList.Exception if gradientType is not a
     *                                                 gradient
     */
    public void setRatiosFromMonotoneList(float[] ratios) throws Exception
    {
        if (gradientType == GradientType.TOP_TO_BOTTOM ||
            gradientType == GradientType.LEFT_TO_RIGHT ||
            gradientType == GradientType.DIAGONAL_LEFT_TOP_TO_RIGHT_BOTTOM ||
            gradientType == GradientType.DIAGONAL_RIGHT_TOP_TO_LEFT_BOTTOM ||
            gradientType == GradientType.CIRCULAR)
        {
            if (size() == 0)
            {
                throw new Exception(
                        "Cannot set ratios if number of colors is 0.");
            }
            // if the ratios array is null then make equidistant
            if (ratios == null)
            {
                this.ratios = new float[size()];
                this.ratios[0] = 1.0F / ((float) this.ratios.length);
                for (int i = 1; i < this.ratios.length - 1; i++)
                {
                    this.ratios[i] = this.ratios[i - 1] +
                                     1.0F / ((float) this.ratios.length);
                }
            }
            else
            {
                if (!floatsAreStrictlyPositive(ratios))
                {
                    throw new Exception(
                            "Can only create ratios with strictly posive values.");
                }
                // make an array of floats matching the number of colors
                this.ratios = new float[size()];
                float sum = 0.0F;
                if (!floatsAreStrictlyMonotone(ratios))
                {
                    throw new Exception(
                            "Can only create ratios from monotone list if the " +
                            "given list is strictly monotone.");
                }
                for (int i = 0; i < size(); i++)
                {
                    // use the given ratio value if possible or default if not
                    this.ratios[i] = ((i < ratios.length) ?
                                      ratios[i] :
                                      sum / (float) i);
                    sum += this.ratios[i];
                }

                if (this.ratios[this.ratios.length - 1] > 1.0F)
                {
                    // normalise the ratio-values to be monotone from 0.0 to 1.0
                    for (int i = 0; i < size(); i++)
                    {
                        this.ratios[i] /= this.ratios[this.ratios.length - 1];
                    }
                }

            }
            fireTableDataChanged();
        }
        else
        {
            throw new Exception("Cannot define ratios for non-gradients.");
        }
    }

    /**
     * Retrieve the cycle-method of gradients.
     *
     * @return the cycle-method
     */
    public MultipleGradientPaint.CycleMethod getCycleMethod()
    {
        return cycleMethod;
    }

    /**
     * Set the cycle-method of gradients.
     *
     * @param cycleMethod the new cycle-method
     */
    public final void setCycleMethod(
            MultipleGradientPaint.CycleMethod cycleMethod)
    {
        this.cycleMethod = cycleMethod;
    }

    // overrides AbstractTableModel
    @Override
    public int getRowCount()
    {
        return gradientType == null ? 0 :
               (gradientType == GradientType.UNIFORM ||
                gradientType == GradientType.UNIFORM_2_COLOR) ? 1 :
               colors.size();
    }

    // overrides AbstractTableModel
    @Override
    public int getColumnCount()
    {
        return gradientType == null ? 0 :
               (gradientType == GradientType.UNIFORM ||
                gradientType == GradientType.UNIFORM_2_COLOR) ? 1 :
               2;
    }

    // overrides AbstractTableModel
    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        if (rowIndex < 0 ||
            rowIndex >= getRowCount() ||
            columnIndex < 0 ||
            columnIndex >= getColumnCount())
        {
            return null;
        }
        switch (gradientType)
        {
            case UNIFORM:
                return colors.get(0);
            case UNIFORM_2_COLOR:
                return colors.get(rowIndex);
            case DIAGONAL_LEFT_TOP_TO_RIGHT_BOTTOM:
            case DIAGONAL_RIGHT_TOP_TO_LEFT_BOTTOM:
            case TOP_TO_BOTTOM:
            case LEFT_TO_RIGHT:
            case CIRCULAR:
                return columnIndex == 0 ?
                       colors.get(rowIndex) :
                       ratios[rowIndex];
            case FOUR_COLOR_RECTANGULAR:
                return colors.get(rowIndex * 2 + columnIndex);
        }
        return null;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex)
    {
        if (rowIndex < 0 ||
            rowIndex >= getRowCount() ||
            columnIndex < 0 ||
            columnIndex >= getColumnCount())
        {
            return;
        }
        switch (gradientType)
        {
            case UNIFORM:
                colors.set(0, (Color) aValue);
            case UNIFORM_2_COLOR:
                colors.set(rowIndex, (Color) aValue);
            case DIAGONAL_LEFT_TOP_TO_RIGHT_BOTTOM:
            case DIAGONAL_RIGHT_TOP_TO_LEFT_BOTTOM:
            case TOP_TO_BOTTOM:
            case LEFT_TO_RIGHT:
            case CIRCULAR:
                if (columnIndex == 0)
                {
                    colors.set(rowIndex, (Color) aValue);
                }
                else
                {
                    ratios[rowIndex] = (float) aValue;
                }
            case FOUR_COLOR_RECTANGULAR:
                colors.set(rowIndex * 2 + columnIndex, (Color) aValue);
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    // implements List
    @Override
    public int size()
    {
        return colors == null ? 0 : colors.size();
    }

    // implements List
    @Override
    public boolean isEmpty()
    {
        return size() == 0;
    }

    // implements List
    @Override
    public boolean contains(Object o)
    {
        return colors.contains(o);
    }

    // implements List
    @Override
    public Iterator<Color> iterator()
    {
        return colors.iterator();
    }

    // implements List
    @Override
    public Object[] toArray()
    {
        return colors != null ? colors.toArray() : null;
    }

    // implements List
    @Override
    public <T> T[] toArray(T[] a)
    {
        return colors != null ? colors.toArray(a) : null;
    }

    // implements List
    @Override
    public boolean add(Color e)
    {
        if (colors == null)
        {
            colors = new ArrayList<>();
        }
        if (ratios == null)
        {
            ratios = new float[0];
        }
        boolean reval = colors.add(e);
        float[] newRatios = new float[colors.size()];
        System.arraycopy(ratios, 0, newRatios, 0, ratios.length);
        ratios = newRatios;
        fireTableDataChanged();
        return reval;
    }

    // implements List
    @Override
    public boolean remove(Object o)
    {
        boolean reval = colors.remove(o);
        float[] newRatios = new float[colors.size()];
        System.arraycopy(ratios, 0, newRatios, 0, newRatios.length);
        ratios = newRatios;
        fireTableDataChanged();
        return reval;
    }

    // implements List
    @Override
    public boolean containsAll(Collection<?> c)
    {
        return colors.containsAll(c);
    }

    // implements List
    @Override
    public boolean addAll(Collection<? extends Color> c)
    {
        boolean reval = colors.addAll(c);
        float[] newRatios = new float[colors.size()];
        System.arraycopy(ratios, 0, newRatios, 0, ratios.length);
        ratios = newRatios;
        fireTableDataChanged();
        return reval;
    }

    // implements List
    @Override
    public boolean addAll(int index, Collection<? extends Color> c)
    {

        boolean reval = colors.addAll(index, c);
        float[] newRatios = new float[colors.size()];
        System.arraycopy(ratios, 0, newRatios, 0, ratios.length);
        ratios = newRatios;
        fireTableDataChanged();
        return reval;
    }

    // implements List
    @Override
    public boolean removeAll(Collection<?> c)
    {
        boolean reval = colors.removeAll(c);
        float[] newRatios = new float[colors.size()];
        System.arraycopy(ratios, 0, newRatios, 0, newRatios.length);
        ratios = newRatios;
        fireTableDataChanged();
        return reval;
    }

    // implements List
    @Override
    public boolean retainAll(Collection<?> c)
    {

        boolean reval = colors.retainAll(c);
        float[] newRatios = new float[colors.size()];
        System.arraycopy(ratios, 0, newRatios, 0, newRatios.length);
        ratios = newRatios;
        fireTableDataChanged();
        return reval;
    }

    // implements List
    @Override
    public void clear()
    {
        ratios = new float[0];
        colors.clear();
        fireTableDataChanged();
    }

    @Override
    public Color get(int index)
    {

        return colors.get(index);
    }

    // implements List
    @Override
    public Color set(int index, Color element)
    {
        Color reval = colors.set(index, element);
        fireTableDataChanged();
        return reval;
    }

    // implements List
    @Override
    public void add(int index, Color element)
    {
        colors.add(index, element);
        fireTableDataChanged();
    }

    // implements List
    @Override
    public Color remove(int index)
    {
        Color reval = colors.remove(index);
        float[] newRatios = new float[colors.size()];
        System.arraycopy(ratios, 0, newRatios, 0, newRatios.length);
        ratios = newRatios;
        fireTableDataChanged();
        return reval;
    }

    // implements List
    @Override
    public int indexOf(Object o)
    {
        return colors.indexOf(o);
    }

    // implements List
    @Override
    public int lastIndexOf(Object o)
    {
        return colors.lastIndexOf(o);
    }

    // implements List
    @Override
    public ListIterator<Color> listIterator()
    {
        return colors.listIterator();
    }

    // implements List
    @Override
    public ListIterator<Color> listIterator(int index)
    {
        return colors.listIterator(index);
    }

    // implements List
    @Override
    public List<Color> subList(int fromIndex, int toIndex)
    {
        return colors.subList(fromIndex, toIndex);
    }

}
