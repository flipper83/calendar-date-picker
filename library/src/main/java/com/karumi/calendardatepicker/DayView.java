package com.karumi.calendardatepicker;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.widget.TextView;
import java.util.Date;

/**
 *
 */
public class DayView extends TextView{

  DayModel dayModel;

  public DayView(Context context) {
    super(context);
  }

  public DayView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public DayView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  public void setDayModel(DayModel dayModel) {
    this.dayModel = dayModel;
    refreshDay();
  }

  public DayModel getDayModel() {
    return dayModel;
  }

  private void refreshDay() {
    setDayBackground();
    setDay();
  }

  private void setDayBackground() {
    //TODO ADD SPECIAL DAYS
    if(dayModel.isRegionFirstDay()){
      setBackgroundColor(getResources().getColor(android.R.color.white));
      setTextColor(getResources().getColor(android.R.color.black));
    }else if(dayModel.isRegionLastDay()){
      setBackgroundColor(getResources().getColor(android.R.color.white));
      setTextColor(getResources().getColor(android.R.color.black));
    }else if(dayModel.isRegionMiddle()){
      setBackgroundColor(getResources().getColor(android.R.color.white));
      setTextColor(getResources().getColor(android.R.color.black));
    }else if(dayModel.isSelected()){
      setBackgroundColor(getResources().getColor(android.R.color.white));
      setTextColor(getResources().getColor(android.R.color.black));
    }else if(dayModel.isToday()){
      setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
      setTextColor(getResources().getColor(android.R.color.black));
    }else {
      setBackgroundColor(getResources().getColor(android.R.color.black));

      if(!dayModel.isCurrentMont()){
        setTextColor(getResources().getColor(android.R.color.darker_gray));
      }else {
        //current month
        setTextColor(getResources().getColor(android.R.color.white));
      }
    }

  }

  private void setDay(){
    String day = dayModel.getDay();
    setText(day);
  }
}
