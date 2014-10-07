package com.karumi.calendardatepicker;

import java.util.Date;

/**
 *
 */
class DayModel {
  private static final int NO_TYPE = -1;
  private boolean today = false;
  private boolean regionFirstDay = false;
  private boolean regionLastDay = false;
  private boolean regionMiddle = false;
  private boolean selected = false;
  private boolean isCurrentMont = false;

  private int customType = DayModel.NO_TYPE;
  private String day;
  private Date date;

  public boolean isToday() {
    return today;
  }

  public void setToday(boolean today) {
    this.today = today;
  }

  public boolean isRegionFirstDay() {
    return regionFirstDay;
  }

  public void setRegionFirstDay(boolean regionFirstDay) {
    this.regionFirstDay = regionFirstDay;
  }

  public boolean isRegionLastDay() {
    return regionLastDay;
  }

  public void setRegionLastDay(boolean regionLastDay) {
    this.regionLastDay = regionLastDay;
  }

  public boolean isRegionMiddle() {
    return regionMiddle;
  }

  public void setRegionMiddle(boolean regionMiddle) {
    this.regionMiddle = regionMiddle;
  }

  public boolean isSelected() {
    return selected;
  }

  public void setSelected(boolean selected) {
    this.selected = selected;
  }

  public boolean isCurrentMont() {
    return isCurrentMont;
  }

  public void setCurrentMont(boolean isCurrentMont) {
    this.isCurrentMont = isCurrentMont;
  }

  public int getCustomType() {
    return customType;
  }

  public void setCustomType(int customType) {
    this.customType = customType;
  }

  public String getDay() {
    return day;
  }

  public void setDay(String day) {
    this.day = day;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public Date getDate() {
    return date;
  }
}
