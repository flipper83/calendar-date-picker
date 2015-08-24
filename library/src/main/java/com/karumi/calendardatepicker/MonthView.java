package com.karumi.calendardatepicker;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.Scroller;
import java.util.Calendar;
import java.util.Date;

/**
 *
 */
public class MonthView extends LinearLayout {

  private static final int ANIMATION_TIME = 250;
  private ScrollRunnable scrollRunnable;
  private int widthMeasureSpec;
  private CalendarAdapter calendarAdapter = new BaseCalendarAdapter();
  private CalendarDatePickerModel calendarDatePickerModel;
  private AnimationMonthListener animationListener;

  public MonthView(Context context) {
    super(context);
    init(context);
  }

  public MonthView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  @TargetApi(Build.VERSION_CODES.HONEYCOMB) public MonthView(Context context, AttributeSet attrs,
      int defStyle) {
    super(context, attrs, defStyle);
    init(context);
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    this.widthMeasureSpec = widthMeasureSpec;
  }

  public void setCalendarDatePickerModel(CalendarDatePickerModel calendarDatePickerModel) {
    this.calendarDatePickerModel = calendarDatePickerModel;
    calendarAdapter.setCalendarDAtePickerModel(calendarDatePickerModel);
  }

  private void init(Context context) {
    setOrientation(VERTICAL);
    for (int i = 0; i < CalendarAdapter.MAX_WEEKS; i++) {
      View rowDays = inflate(context, R.layout.cdp_row_with_days, null);
      this.addView(rowDays);
    }

  }



  public void smoothScrollNextMonth(AnimationMonthListener animationNextListener) {
    this.animationListener = animationNextListener;

    calendarDatePickerModel.resetToCurrentDay();

    int offset = calculateOffsetPostionNextMonth(calendarDatePickerModel.getCalendar());

    setCalendarOnFirstWeek();

    calendarDatePickerModel.addCalendarDate(Calendar.DAY_OF_MONTH, CalendarAdapter.MAX_WEEKS * 7);
    smoothScrollTo(offset);
  }

  public void smoothScrollPreviousMonth(AnimationMonthListener animationPreviousListener) {
    animationListener = animationPreviousListener;

    calendarDatePickerModel.resetToCurrentDay();
    int offset = calculateOffsetPostionPreviousMonth();

    calendarDatePickerModel.resetToCurrentDay();
    setCalendarOnFirstWeek();
    smoothScrollTo(offset);
  }

  private int calculateOffsetPostionPreviousMonth() {
    Calendar calendar = calendarDatePickerModel.getCalendar();

    Calendar calendarForOperations = Calendar.getInstance();
    calendarForOperations.setTime(calendar.getTime());
    calendarForOperations.add(Calendar.MONTH, -1);
    int previousMonth = calendarForOperations.get(Calendar.MONTH);
    int previousYear = calendarForOperations.get(Calendar.YEAR);

    setCalendarOnFirstWeek();
    for (int i = 0; i < CalendarAdapter.MAX_WEEKS; i++) {
      calendar.add(Calendar.WEEK_OF_MONTH, -1);
      int monthWeek = calendar.get(Calendar.MONTH);
      int dayFirstDayWeek = calendar.get(Calendar.DAY_OF_MONTH);
      int yearWeek = calendar.get(Calendar.YEAR);

      if ((yearWeek < previousYear) || (monthWeek < previousMonth) || ((monthWeek == previousMonth)
          && (dayFirstDayWeek == 1))) {
        int sizeBlock = getChildAt(0).getHeight();
        return sizeBlock * (i + 1);
      }
    }

    return 0;
  }

  private int calculateOffsetPostionNextMonth(Calendar calendar) {
    Calendar calendarForOperations = Calendar.getInstance();
    calendarForOperations.setTime(calendar.getTime());
    calendarForOperations.add(Calendar.MONTH, 1);
    int nextMonth = calendarForOperations.get(Calendar.MONTH);

    for (int i = 0; i < getChildCount(); i++) {
      LinearLayout weekLayout = (LinearLayout) getChildAt(i);
      for (int day = 0; day < weekLayout.getChildCount(); day++) {
        DayView dayView = (DayView) weekLayout.getChildAt(day);
        Date dayDate = dayView.getDayModel().getDate();
        calendarForOperations.setTime(dayDate);
        int dayMonth = calendarForOperations.get(Calendar.MONTH);
        if (dayMonth == nextMonth) {
          return (-1) * weekLayout.getTop();
        }
      }
    }
    return 0;
  }

  private void smoothScrollTo(int distance) {
    if (scrollRunnable == null) {
      scrollRunnable = new ScrollRunnable(getContext());
    }
    scrollRunnable.start(distance);
  }

  public void refresh() {

    setCalendarOnFirstWeek();

    for (int i = 0; i < getChildCount(); i++) {
      LinearLayout weekLayout = (LinearLayout) getChildAt(i);
      for (int day = 0; day < weekLayout.getChildCount(); day++) {
        fillDayInfo(weekLayout, day);
        calendarDatePickerModel.addCalendarDate(Calendar.DAY_OF_MONTH, 1);
      }
    }

    calendarDatePickerModel.resetToCurrentDay();
  }

  private void setCalendarOnFirstWeek() {
    calendarDatePickerModel.firstOfMonth();

    int firstDayOnMonth = calendarDatePickerModel.getCalendarValue(Calendar.DAY_OF_WEEK);

    int offsetWeek = firstDayOnMonth - calendarDatePickerModel.getFirstDayOfWeek();

    //this case is european calendar, firstDay is 2 'Monday' and the day is 1 sunday≤ we need move
    //a complete week
    if (offsetWeek < 0) {
      offsetWeek = 6;
    }

    Log.d("borrar", "offset " + offsetWeek);

    calendarDatePickerModel.addCalendarDate(Calendar.DAY_OF_MONTH, (-1) * offsetWeek);
  }

  private void fillDayInfo(LinearLayout weekLayout, int dayPos) {
    DayView dayView = (DayView) weekLayout.getChildAt(dayPos);
    DayModel dayModel = calendarDatePickerModel.getDay();
    dayView.setDayModel(dayModel);
    dayView.setOnClickListener(clickDay);
  }

  private OnClickListener clickDay = new OnClickListener() {
    @Override public void onClick(View v) {
      DayView dayView = (DayView) v;
      DayModel dayModel = dayView.getDayModel();
      calendarDatePickerModel.daySelected(dayModel.getDate());
      refresh();
    }
  };

  private class ScrollRunnable implements Runnable {
    Scroller scroller;
    int lastValue = 0;
    private boolean moveUp;

    private ScrollRunnable(Context context) {
      scroller = new Scroller(context, new LinearInterpolator());
    }

    void start(int distance) {
      if (distance < 0) {
        moveUp = true;
      } else {
        moveUp = false;
      }
      lastValue = 0;
      scroller.startScroll(0, 0, 0, distance, ANIMATION_TIME);
      postOnAnimation(this);
    }

    @Override public void run() {
      boolean more = scroller.computeScrollOffset();
      if (!scroller.isFinished()) {
        int dy = scroller.getCurrY() - lastValue;
        lastValue = scroller.getCurrY();

        for (int i = 0; i < getChildCount(); i++) {
          View dayView = getChildAt(i);
          dayView.offsetTopAndBottom(dy);

          //remove this view from the layout
          if (moveUp) {
            if (dayView.getTop() + dayView.getMeasuredHeight() <= 0) {
              removeViewInLayout(dayView);
            }
          } else {
            if (dayView.getTop() >= getHeight()) {
              removeViewInLayout(dayView);
            }
          }
        }

        if (moveUp) {
          addNewOnBottom();
        } else {
          addNewOnTop();
        }

        if (more) {
          postOnAnimation(this);
        } else {
          finishAnim();
        }
      } else {
        finishAnim();
      }
    }

    private void finishAnim() {
      if (getChildCount() > CalendarAdapter.MAX_WEEKS) {
        int diff = getChildCount() - CalendarAdapter.MAX_WEEKS;
        removeViews(getChildCount() - 1 - diff,diff);
      }
      calendarDatePickerModel.resetToCurrentDay();
      animationListener.animationFinish();
    }

    private void addNewOnTop() {
      View lastView = getChildAt(0);
      int lastTop = lastView.getTop();

      if (lastTop > 0) {

        calendarDatePickerModel.addCalendarDate(Calendar.WEEK_OF_MONTH, -1);
        View row = createMonthRow();
        calendarDatePickerModel.addCalendarDate(Calendar.WEEK_OF_MONTH, -1);

        ViewGroup.LayoutParams p = row.getLayoutParams();

        if (p == null) {
          p = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
              ViewGroup.LayoutParams.WRAP_CONTENT, 0.0f);
        }

        addViewInLayout(row, 0, p, true);

        setupChild(row, lastTop - lastView.getMeasuredHeight(), p);
      }
    }

    private void addNewOnBottom() {
      int height = getHeight();

      View lastView = getChildAt(getChildCount() - 1);
      int lastTop = lastView.getTop();
      int lastH = lastView.getMeasuredHeight();

      if (lastTop + lastH < height) {

        View row = createMonthRow();

        ViewGroup.LayoutParams p = row.getLayoutParams();

        if (p == null) {
          p = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
              ViewGroup.LayoutParams.WRAP_CONTENT, 0.0f);
        }

        addViewInLayout(row, getChildCount(), p, true);

        setupChild(row, lastTop + lastH, p);
      }
    }

  }

  private void setupChild(View row, int childTop, ViewGroup.LayoutParams p) {
    int childWidthSpec =
        ViewGroup.getChildMeasureSpec(widthMeasureSpec, getPaddingLeft() + getPaddingRight(),
            p.width);
    int lpHeight = p.height;
    int childHeightSpec;
    if (lpHeight > 0) {
      childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
    } else {
      childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
    }
    row.measure(childWidthSpec, childHeightSpec);

    final int w = row.getMeasuredWidth();
    final int h = row.getMeasuredHeight();

    final int childRight = 0 + w;
    final int childBottom = childTop + h;
    row.layout(0, childTop, childRight, childBottom);
  }

  private View createMonthRow() {

    LinearLayout row = (LinearLayout) LayoutInflater.from(getContext())
        .inflate(R.layout.cdp_row_with_days, MonthView.this, false);

    for (int day = 0; day < row.getChildCount(); day++) {
      fillDayInfo(row, day);
      calendarDatePickerModel.addCalendarDate(Calendar.DAY_OF_MONTH, 1);
    }
    return row;
  }
}
