package com.karumi.calendardatepicker;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 *
 */
public abstract class CalendarAdapter extends BaseAdapter {

  public static final int MAX_WEEKS = 6;
  public CalendarDatePickerModel calendarDatePickerModel;


  protected CalendarAdapter() {
  }

  @Override public int getCount() {
    return MAX_WEEKS * 7;
  }

  @Override public DayView getItem(int position) {
    return null;
  }

  @Override public long getItemId(int position) {
    return position;
  }

  @Override public View getView(int position, View convertView, ViewGroup parent) {
    return null;
  }

  public void setCalendarDAtePickerModel(CalendarDatePickerModel calendarDatePickerModel) {
    this.calendarDatePickerModel = calendarDatePickerModel;
  }
}
