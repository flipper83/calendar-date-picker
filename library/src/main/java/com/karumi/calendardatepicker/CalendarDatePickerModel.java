package com.karumi.calendardatepicker;

import android.content.Context;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

/**
 *
 */
class CalendarDatePickerModel {

  private static final int MODE_SINGLE_SELECTION = 0;
  private static final int MODE_MULTIPLE_SELECTION = 1;
  private static final int MODE_RANGE_SELECTION = 2;

  private final Calendar calendar;
  private final SimpleDateFormat monthNameFormat;
  private final Locale locale;
  private final Calendar today;
  private SimpleDateFormat weekdayNameFormat;
  private Date currentDate;

  private List<Calendar> selectedDays = new ArrayList<Calendar>();
  private Calendar currentMonth;
  private int selectedMode = MODE_RANGE_SELECTION;

  private Calendar startRange;
  private Calendar endRange;

  CalendarDatePickerModel(Context context, Locale locale, Calendar today) {
    this.today = today;
    this.calendar = Calendar.getInstance(locale);
    this.currentMonth = Calendar.getInstance(locale);
    calendar.setTime(today.getTime());
    currentMonth.setTime(today.getTime());
    this.locale = locale;

    weekdayNameFormat =
        new SimpleDateFormat(context.getString(R.string.cdp_dayofweek_format), locale);
    monthNameFormat = new SimpleDateFormat(context.getString(R.string.cdp_month_format), locale);
  }

  public String getDayOfWeek(int position) {
    Calendar calendarDays = Calendar.getInstance(locale);
    int day = calendarDays.getFirstDayOfWeek() + position;
    calendar.set(Calendar.DAY_OF_WEEK, day);
    return weekdayNameFormat.format(calendar.getTime());
  }

  public void setToday(Date date) {
    currentDate = date;
    today.setTime(date);
    currentMonth.setTime(date);
    calendar.setTime(date);
  }

  public String getCurrentMonth() {
    return monthNameFormat.format(currentDate);
  }


  public Calendar getCalendar() {
    return calendar;
  }

  public void resetToCurrentDay() {
    calendar.setTime(currentDate);
  }

  public void nextMonth() {
    calendar.add(Calendar.MONTH, 1);
    currentDate = calendar.getTime();
    currentMonth.setTime(currentDate);
  }

  public void previousMonth() {
    calendar.add(Calendar.MONTH, -1);
    currentDate = calendar.getTime();
    currentMonth.setTime(currentDate);
  }

  public void firstOfMonth() {
    calendar.set(Calendar.DAY_OF_MONTH, 1);
  }

  public int getCalendarValue(int field) {
    return calendar.get(field);
  }

  public int getFirstDayOfWeek() {
    return calendar.getFirstDayOfWeek();
  }

  public void addCalendarDate(int field, int value) {
    calendar.add(field, value);
  }

  public DayModel getDay() {

    DayModel day = new DayModel();

    day.setDate(calendar.getTime());
    day.setDay(Integer.toString(calendar.get(Calendar.DAY_OF_MONTH)));

    if (selectedMode != MODE_RANGE_SELECTION) {
      if (containsSelectedDay(calendar)) {
        day.setSelected(true);
      }
    } else {
      if (sameDate(calendar, startRange)) {
        day.setRegionFirstDay(true);
      } else if (sameDate(calendar, endRange)) {
        day.setRegionLastDay(true);
      } else if ((startRange != null)
          && (endRange != null)
          && (calendar.after(startRange))
          && (calendar.before(endRange))) {
        day.setRegionMiddle(true);
      }
    }

    if (sameMonth(calendar, currentMonth)) {
      day.setCurrentMont(true);
    } else {
      day.setCurrentMont(false);
    }

    if (sameDate(calendar, today)) {
      day.setToday(true);
    }

    return day;
  }

  private boolean sameMonth(Calendar calendar, Calendar selectedDate) {
    return calendar.get(MONTH) == selectedDate.get(MONTH) && calendar.get(YEAR) == selectedDate.get(
        YEAR);
  }

  private boolean sameDate(Calendar calendar, Calendar selectedDate) {
    if (selectedDate == null || calendar == null) {
      return false;
    }
    return calendar.get(MONTH) == selectedDate.get(MONTH) && calendar.get(YEAR) == selectedDate.get(
        YEAR) && calendar.get(DAY_OF_MONTH) == selectedDate.get(DAY_OF_MONTH);
  }

  public void daySelected(Date date) {
    Calendar calendarSelected = Calendar.getInstance();
    calendarSelected.setTime(date);

    if (selectedMode == MODE_SINGLE_SELECTION) {
      selectedSingleSelection(calendarSelected);
    } else if (selectedMode == MODE_MULTIPLE_SELECTION) {
      selectedMultipleSelection(calendarSelected);
    } else if (selectedMode == MODE_RANGE_SELECTION) {
      selectedRange(calendarSelected);
    }
  }

  private void selectedRange(Calendar calendarSelected) {
    if((startRange != null) && (endRange != null)){
      startRange = calendarSelected;
      endRange = null;
    }else{
      if ((startRange == null) || (calendarSelected.before(startRange))) {
        startRange = calendarSelected;
      } else {
        endRange = calendarSelected;
      }
    }

  }

  private void selectedSingleSelection(Calendar calendarSelected) {
    selectedDays.clear();
    selectedDays.add(calendarSelected);
  }

  private void selectedMultipleSelection(Calendar calendarSelected) {
    if (!containsSelectedDay(calendarSelected)) {
      selectedDays.add(calendarSelected);
    } else {
      int position = getSelectedDayIndex(calendarSelected);
      selectedDays.remove(position);
    }
  }

  private int getSelectedDayIndex(Calendar calendarSelected) {
    int postion = 0;
    for (Calendar selectedDay : selectedDays) {
      if (sameDate(calendarSelected, selectedDay)) {
        return postion;
      }
      postion++;
    }
    return -1;
  }

  private boolean containsSelectedDay(Calendar calendarSelected) {
    for (Calendar selectedDay : selectedDays) {
      if (sameDate(calendarSelected, selectedDay)) {
        return true;
      }
    }
    return false;
  }
}
