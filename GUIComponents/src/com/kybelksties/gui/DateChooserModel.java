package com.kybelksties.gui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListDataListener;
import javax.swing.table.AbstractTableModel;

/**
 * This table-model maintains a list of day-of-month in a numDays x 7 grid. This
 * can be used to display months in a compact table for example. The table is
 * pre- and appended with previous and next month days to generate full weeks.
 * <blockquote>
 * <table border=0 cellspacing=3 cellpadding=0
 * summary="examples calendar">
 * <tr bgcolor="#ccccff">
 * <th align=left>Sun</th>
 * <th align=left>Mon</th>
 * <th align=left>Tue</th>
 * <th align=left>Wed</th>
 * <th align=left>Thu</th>
 * <th align=left>Fri</th>
 * <th align=left>Sat</th>
 * </tr> <tr>
 * <td></td><td></td><td></td><td></td><td></td><td>1</td><td>2</td>
 * </tr> <tr>
 * <td>3</td><td>4</td><td>5</td><td>6</td><td>7</td><td>8</td><td>9</td>
 * </tr> <tr>
 * <td>10</td><td>11</td><td>12</td><td>13</td><td>14</td><td>15</td><td>16</td>
 * </tr><tr>
 * <td>17</td><td>18</td><td>19</td><td>20</td><td>21</td><td>22</td><td>23<td>
 * </tr><tr>
 * <td>24</td><td>25</td><td>26</td><td>27</td><td>28</td><td>29</td><td>30<td>
 * </tr>
 * </table>
 * </blockquote>
 *
 * Also display lists like long- and short-month-names and weekday-names are
 * provided according to chosen locale.
 *
 * @author Dieter J Kybelksties
 */
public class DateChooserModel
        extends AbstractTableModel // for choosing day of month
        implements SpinnerModel, // for selection of year
                   ComboBoxModel // for choosin a month
{

    private static final String CLASS_NAME = DateChooserModel.class.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    private static ArrayList<String> nameArray(int type, int style,
                                               Locale locale)
    {
        ArrayList<String> reval = new ArrayList<>();
        Map<Integer, String> sorted = new TreeMap<>();
        Map<String, Integer> nameMap = Calendar.getInstance().getDisplayNames(
                             type,
                             style,
                             locale);
        for (String s : nameMap.keySet())
        {
            sorted.put(nameMap.get(s), s);
        }
        for (Integer i : sorted.keySet())
        {
            reval.add(sorted.get(i));
        }
        return reval;
    }

    private ArrayList<String> weekDaysLong;
    private ArrayList<String> weekDaysShort;
    private ArrayList<String> monthsLong;
    private ArrayList<String> monthsShort;

    private final Calendar calendar = Calendar.getInstance();
    private int dayOffset = 0;
    private int daysInMonth = 31;
    private int daysInPreviousMonth = 31;
    private int rows = 5;
    private int[][] daysTableData;
    private Locale locale;
    private SpinnerNumberModel yearSpinnerModel;
    private DefaultComboBoxModel monthComboBoxModel;

    /**
     * Default construct.
     */
    public DateChooserModel()
    {
        this(Locale.getDefault());
    }

    /**
     * Construct with a locale different from the default.
     *
     * @param locale the different locale
     */
    public DateChooserModel(Locale locale)
    {
        this(locale, Calendar.getInstance().getTime());
    }

    /**
     * Construct with a locale and date different from the default.
     *
     * @param locale  the different locale
     * @param newDate the date to create the model for
     */
    public DateChooserModel(Locale locale, Date newDate)
    {
        initializeNameLists(locale);
        yearSpinnerModel = new SpinnerNumberModel(calendar.get(Calendar.YEAR),
                                                  calendar.getActualMinimum(
                                                          Calendar.YEAR),
                                                  calendar.getActualMaximum(
                                                          Calendar.YEAR),
                                                  1);
        monthComboBoxModel = new DefaultComboBoxModel(monthsLong.toArray());
        updateModel(newDate);
    }

    final void initializeNameLists(Locale locale)
    {
        this.locale = locale;
        weekDaysLong = nameArray(Calendar.DAY_OF_WEEK, Calendar.LONG, locale);
        weekDaysShort = nameArray(Calendar.DAY_OF_WEEK, Calendar.SHORT, locale);
        monthsLong = nameArray(Calendar.MONTH, Calendar.LONG, locale);
        monthsShort = nameArray(Calendar.MONTH, Calendar.SHORT, locale);
    }

    private void updateModel(Date newDate)
    {
        if (newDate == null)
        {
            newDate = calendar.getTime();
        }

        // set the new date on the calendar for subsequent calculations
        calendar.setTime(newDate);

        calendar.set(Calendar.DAY_OF_MONTH, 1);
        dayOffset = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        int monthContainingDate = calendar.get(Calendar.MONTH);
        int previousMonth = monthContainingDate == 1 ?
                            12 :
                            monthContainingDate - 1;
        daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        rows = (daysInMonth + dayOffset + 1) / 7 + 1;
        calendar.set(Calendar.MONTH, previousMonth);
        daysInPreviousMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        daysTableData = new int[rows][7];
        if (dayOffset > 0)
        {
            for (int i = dayOffset; i > -1; i--)
            {
                daysTableData[0][dayOffset - i] = daysInPreviousMonth - i + 1;
            }
        }
        for (int i = dayOffset; i < rows * 7; i++)
        {
            daysTableData[i / 7][i % 7] = ((i - dayOffset) % daysInMonth) + 1;
        }

        // set the new date on the calendar now again as the selected date
        calendar.setTime(newDate);

        fireTableDataChanged();
        monthComboBoxModel.setSelectedItem(getMonth());
        yearSpinnerModel.setValue(getYear());
    }

    @Override
    public int getRowCount()
    {
        return rows;
    }

    @Override
    public int getColumnCount()
    {
        return 7;
    }

    @Override
    public Object getValueAt(int row, int col)
    {
        if (daysTableData != null &&
            daysTableData.length > 0 &&
            row < daysTableData.length &&
            daysTableData[0].length > 0 &&
            col < daysTableData[0].length)
        {
            return daysTableData[row][col];
        }
        return null;
    }

    @Override
    public String getColumnName(int col)
    {
        return weekDaysShort.get(col);
    }

    /**
     * Retrieve the list of weekday-names according to the locale.
     *
     * @return the list of weekday-names
     */
    public ArrayList<String> getWeekDaysLong()
    {
        return weekDaysLong;
    }

    /**
     * Retrieve the list of abbreviated weekday-names according to the locale.
     *
     * @return the list of abbreviated weekday-names
     */
    public ArrayList<String> getWeekDaysShort()
    {
        return weekDaysShort;
    }

    /**
     * Retrieve the list of month-names according to the locale.
     *
     * @return the list of month-names
     */
    public ArrayList<String> getMonthsLong()
    {
        return monthsLong;
    }

    /**
     * Retrieve the list of abbreviated month-names according to the locale.
     *
     * @return the list of abbreviated month-names
     */
    public ArrayList<String> getMonthsShort()
    {
        return monthsShort;
    }

    /**
     * Retrieve the month set in this model.
     *
     * @return the month (as name according to the locale)
     */
    public String getMonth()
    {
        return calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, locale);
    }

    /**
     * Retrieve the month set in this model as integer.
     *
     * @return the month-index
     */
    public int getMonthIndex()
    {
        return calendar.get(Calendar.MONTH);
    }

    /**
     * Set the month by index.
     *
     * @param monthIndex integer 1 to 12
     */
    public void setMonthByIndex(int monthIndex)
    {
        calendar.set(Calendar.MONTH, monthIndex);
        updateModel(calendar.getTime());
    }

    /**
     * Retrieve the set year.
     *
     * @return the year as integer
     */
    public int getYear()
    {
        return calendar.get(Calendar.YEAR);
    }

    /**
     * Set the year.
     *
     * @param year the new year as integer
     */
    public void setYear(int year)
    {
        calendar.set(Calendar.YEAR, year);
        updateModel(calendar.getTime());
    }

    /**
     * Retrieve the set date.
     *
     * @return the date
     */
    public Date getDate()
    {
        return calendar.getTime();
    }

    /**
     * Set a new date.
     *
     * @param newDate the new date
     */
    public void setDate(Date newDate)
    {
        calendar.setTime(newDate);
        updateModel(calendar.getTime());
    }

    /**
     * Retrieve the set locale.
     *
     * @return the locale
     */
    public Locale getLocale()
    {
        return locale;
    }

    /**
     * Set a new locale.
     *
     * @param locale the new locale
     */
    public void setLocale(Locale locale)
    {
        if (locale != null && this.locale != locale)
        {
            this.locale = locale;
            initializeNameLists(locale);
            updateModel(calendar.getTime());
        }
    }

    /**
     * Set the day of month.
     *
     * @param day the new day of month
     */
    public void setDayOfMonth(int day)
    {
        calendar.set(Calendar.DAY_OF_MONTH, day);
        updateModel(calendar.getTime());
    }

    /**
     * Retrieve the day of month.
     *
     * @return the day of month
     */
    public int getDayOfMonth()
    {
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Retrieve the maximal day in the month (28,29,30 or 31).
     *
     * @return the maximal day number
     */
    public int getMaxDayOfMonth()
    {
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * Retrieve the short weekday name for a weekday index.
     *
     * @param dayIndex
     * @return the short weekday name
     */
    public String getShortWeekdayName(int dayIndex)
    {
        return weekDaysShort.get(dayIndex);
    }

    /**
     * Retrieve the selected date as a string formated using the chosen locale.
     *
     * @return the date as String
     */
    public String getDateAsString()
    {
        Date date = calendar.getTime();
        return (new SimpleDateFormat("dd MMMM yyyy", locale)).format(date);
    }

    /**
     * Set the date to the previous month. If month is January then the previous
     * month will be the December of the previous year.
     */
    public void setPreviousMonth()
    {
        int monthContainingDate = calendar.get(Calendar.MONTH);
        int previousMonth = monthContainingDate == 1 ?
                            12 :
                            monthContainingDate - 1;
        calendar.set(Calendar.MONTH, previousMonth);
        if (previousMonth == 12) // need to also change the year
        {
            int currentYear = calendar.get(Calendar.YEAR);
            calendar.set(Calendar.YEAR, currentYear - 1);
        }
        updateModel(calendar.getTime());
    }

    /**
     * Set the date to the next month. If month is December then the next month
     * will be the January of the next year.
     */
    public void setNextMonth()
    {
        int monthContainingDate = calendar.get(Calendar.MONTH);
        int nextMonth = monthContainingDate == 12 ?
                        1 :
                        monthContainingDate + 1;
        calendar.set(Calendar.MONTH, nextMonth);
        if (nextMonth == 1) // need to also change the year
        {
            int currentYear = calendar.get(Calendar.YEAR);
            calendar.set(Calendar.YEAR, currentYear + 1);
        }
        updateModel(calendar.getTime());
    }

    @Override
    public Object getValue()
    {
        return getYear();
    }

    @Override
    public void setValue(Object value)
    {
        setYear((int) value);
    }

    @Override
    public Object getNextValue()
    {
        int year = getYear();
        if (year < calendar.getActualMaximum(Calendar.YEAR))
        {
            year++;
        }
        return year;
    }

    @Override
    public Object getPreviousValue()
    {
        int year = getYear();
        if (year > calendar.getActualMinimum(Calendar.YEAR))
        {
            year--;
        }
        return year;
    }

    @Override
    public void addChangeListener(ChangeListener l)
    {
        yearSpinnerModel.addChangeListener(l);
    }

    @Override
    public void removeChangeListener(ChangeListener l)
    {
        yearSpinnerModel.removeChangeListener(l);
    }

    @Override
    public void setSelectedItem(Object anItem)
    {
        monthComboBoxModel.setSelectedItem(anItem);
    }

    @Override
    public Object getSelectedItem()
    {
        return monthComboBoxModel.getSelectedItem();
    }

    @Override
    public int getSize()
    {
        return monthsLong.size();
    }

    @Override
    public Object getElementAt(int index)
    {
        return monthsLong.get(index);
    }

    @Override
    public void addListDataListener(ListDataListener l)
    {
        monthComboBoxModel.addListDataListener(l);
    }

    @Override
    public void removeListDataListener(ListDataListener l)
    {
        monthComboBoxModel.removeListDataListener(l);
    }

}
