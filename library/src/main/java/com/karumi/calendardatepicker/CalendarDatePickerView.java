package com.karumi.calendardatepicker;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 *
 */
public class CalendarDatePickerView extends LinearLayout {

  private LinearLayout dayOfWeekLayout;
  private MonthView monthView;

  CalendarDatePickerModel calendarDatePickerModel;
  private TextView monthTitleView;
  private ImageView nextButton;
  private ImageView prevButton;

  public CalendarDatePickerView(Context context) {
    super(context);
    init(context);
  }

  public CalendarDatePickerView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  @TargetApi(Build.VERSION_CODES.HONEYCOMB) public CalendarDatePickerView(Context context,
      AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init(context);
  }

  private void init(Context context) {
    View rootView = inflate(context, R.layout.cdp_calendar_view, this);

    mapGui(rootView);

    Locale locale = Locale.getDefault();
    Calendar today = Calendar.getInstance(locale);

    calendarDatePickerModel = new CalendarDatePickerModel(getContext(), locale, today);
    monthView.setCalendarDatePickerModel(calendarDatePickerModel);
    setDaysOfMonth(today.getTime());
    initDayOfWeek();
  }

  private void mapGui(View rootView) {
    dayOfWeekLayout = (LinearLayout) rootView.findViewById(R.id.cdp_ll_day_of_week);
    monthView = (MonthView) rootView.findViewById(R.id.cdp_ll_day_of_month);
    monthTitleView = (TextView) rootView.findViewById(R.id.cdp_tv_month);
    nextButton = (ImageView) rootView.findViewById(R.id.cdp_bt_next);
    prevButton = (ImageView) rootView.findViewById(R.id.cdp_bt_prev);

    nextButton.setOnClickListener(onClickNext);
    prevButton.setOnClickListener(onClickPrev);

    addDaysListener();
  }

  private void addDaysListener() {

  }

  private void setDaysOfMonth(Date time) {
    calendarDatePickerModel.setToday(time);
    refreshDays();
  }

  private void refreshDays() {
    String currentMonth = calendarDatePickerModel.getCurrentMonth();

    monthTitleView.setText(currentMonth);

    calendarDatePickerModel.firstOfMonth();

    monthView.refresh();

  }

  private void initDayOfWeek() {
    for (int i = 0; i < dayOfWeekLayout.getChildCount(); i++) {
      TextView dayOfWeekView = (TextView) dayOfWeekLayout.getChildAt(i);
      String dayOfWeek = calendarDatePickerModel.getDayOfWeek(i);
      dayOfWeekView.setText(dayOfWeek);
    }
  }

  private OnClickListener onClickNext = new OnClickListener() {
    @Override public void onClick(View v) {
      animateNext(animationNextListener);
    }
  };

  private void animateNext(AnimationMonthListener animationNextListener) {
    monthView.smoothScrollNextMonth(animationNextListener);
  }

  private OnClickListener onClickPrev = new OnClickListener() {
    @Override public void onClick(View v) {
      monthView.smoothScrollPreviousMonth(animationPreviousListener);
    }
  };

  AnimationMonthListener animationNextListener = new AnimationMonthListener() {
    @Override public void animationFinish() {
      calendarDatePickerModel.nextMonth();
      refreshDays();
    }
  };

  AnimationMonthListener animationPreviousListener = new AnimationMonthListener() {
    @Override public void animationFinish() {
      calendarDatePickerModel.previousMonth();
      refreshDays();
    }
  };

}
