package com.kybelksties.gui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.table.AbstractTableModel;

/**
 * This table-model maintains a list of day-of-month in a x*7 grid. This can be
 * used to display months in a compact table for example.
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
 * </tr>
 * <tr> <td>  </td><td>   </td><td>    </td><td>    </td><td>    </td><td> 1  </td><td> 2
 * </td></tr>
 * <tr> <td>3 </td><td> 4 </td><td> 5  </td><td> 6  </td><td> 7  </td><td> 8
 * </td><td> 9  </td></tr>
 * <tr> <td>10</td><td> 11</td><td> 12 </td><td> 13 </td><td> 14 </td><td> 15
 * </td><td> 16 </td></tr>
 * <tr> <td>17</td><td> 18</td><td> 19 </td><td> 20 </td><td> 21 </td><td> 22
 * </td><td> 23 <td></tr>
 * <tr> <td>24</td><td> 25</td><td> 26 </td><td> 27 </td><td> 28 </td><td> 29
 * </td><td> 30 <td></tr>
 * </table>
 * </blockquote>
 *
 * Also display lists like long and short month-names and weekday names are
 * provided according to chosen locale.
 *
 * @author dieter
 */
public class CalendarModel extends AbstractTableModel
{

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

    final void initializeNameLists(Locale locale)
    {
        this.locale = locale;
        weekDaysLong = makeLocalNameArray(Calendar.DAY_OF_WEEK,
                                          Calendar.LONG,
                                          locale);
        weekDaysShort = makeLocalNameArray(Calendar.DAY_OF_WEEK,
                                           Calendar.SHORT,
                                           locale);
        monthsLong = makeLocalNameArray(Calendar.MONTH, Calendar.LONG, locale);
        monthsShort = makeLocalNameArray(Calendar.MONTH, Calendar.SHORT, locale);
    }

    private static ArrayList<String> makeLocalNameArray(int type,
                                                        int style,
                                                        Locale locale)
    {
        ArrayList<String> reval = new ArrayList<>();
        Map<Integer, String> sorted = new TreeMap<>();
        Map<String, Integer> calendarMap =
                             Calendar.getInstance().getDisplayNames(
                                     type,
                                     style,
                                     locale);
        for (String s : calendarMap.keySet())
        {
            sorted.put(calendarMap.get(s), s);
        }
        for (Integer i : sorted.keySet())
        {
            reval.add(sorted.get(i));
        }
        return reval;
    }

    public CalendarModel()
    {
        this(Locale.getDefault());
    }

    public CalendarModel(Locale locale)
    {
        this(locale, Calendar.getInstance().getTime());
    }

    public CalendarModel(Locale locale, Date newDate)
    {
        initializeNameLists(locale);
        updateModel(newDate);
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
        if (daysTableData != null ||
            row >= daysTableData.length ||
            col >= daysTableData[0].length)
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

    public ArrayList<String> getWeekDaysLong()
    {
        return weekDaysLong;
    }

    public ArrayList<String> getWeekDaysShort()
    {
        return weekDaysShort;
    }

    public ArrayList<String> getMonthsLong()
    {
        return monthsLong;
    }

    public ArrayList<String> getMonthsShort()
    {
        return monthsShort;
    }

    public String getMonth()
    {
        return calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, locale);
    }

    public void setMonth(int monthIndex)
    {
        calendar.set(Calendar.MONTH, monthIndex);
        updateModel(calendar.getTime());
    }

    public String getDay()
    {
        return calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, locale);
    }

    public void setDay(int day)
    {
        calendar.set(Calendar.DAY_OF_MONTH, day);
        updateModel(calendar.getTime());
    }

    int getYear()
    {
        return calendar.get(Calendar.YEAR);
    }

    public void setYear(int year)
    {
        calendar.set(Calendar.YEAR, year);
        updateModel(calendar.getTime());
    }

    public Date getDate()
    {
        return calendar.getTime();
    }

    public void setDate(Date newDate)
    {
        calendar.setTime(newDate);
        updateModel(calendar.getTime());
    }

    public int getDayInMonth()
    {
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public String getDateAsString()
    {
        Date date = calendar.getTime();
        return (new SimpleDateFormat()).format(date);
    }

}
