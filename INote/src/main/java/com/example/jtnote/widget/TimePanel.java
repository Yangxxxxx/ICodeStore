package com.example.jtnote.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import java.util.Calendar;

/**
 * Created by Administrator on 2018/8/27 0027.
 */

public class TimePanel extends NumberPanel implements NumberPanel.NumberSelectListener{
    private final int MONTH_PANEL = 1;
    private final int DAY_PANEL = 2;
    private final int HOUR_PANEL = 3;
    private final int MINUTE_PANEL = 4;

    private int panelMode;
    private Calendar calendar;
    private CalenderChangeListener calenderChangeListener;

    public TimePanel(Context context) {
        this(context, null);
    }

    public TimePanel(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimePanel(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setNumberSelectListener(this);
    }

    public void attachCalendar(Calendar calendar){
        this.calendar = calendar;
    }

    public void setCalenderChangeListener(CalenderChangeListener calenderChangeListener){
        this.calenderChangeListener = calenderChangeListener;
    }

    public void turnMonthPanel(){
        if(calendar == null) return;
        panelMode = MONTH_PANEL;
        int minNum = getRangeMinNum(calendar, Calendar.MONTH) + 1;
        int maxNum = calendar.getActualMaximum(Calendar.MONTH) + 1;
        setRange(minNum, maxNum);
        setSelectNumberIndex(calendar.get(Calendar.MONTH) + 1);
    }

    public void turnDayPanel(){
        if(calendar == null) return;
        panelMode = DAY_PANEL;
        int minNum = getRangeMinNum(calendar, Calendar.DAY_OF_MONTH);//calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
        int maxNum = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        setRange(minNum, maxNum);
        setSelectNumberIndex(calendar.get(Calendar.DAY_OF_MONTH));
    }

    public void turnHourPanel(){
        if(calendar == null) return;
        panelMode = HOUR_PANEL;
        int minNum = getRangeMinNum(calendar, Calendar.HOUR_OF_DAY);//calendar.getActualMinimum(Calendar.HOUR_OF_DAY);
        int maxNum = calendar.getActualMaximum(Calendar.HOUR_OF_DAY);
        setRange(minNum, maxNum);
        setSelectNumberIndex(calendar.get(Calendar.HOUR_OF_DAY));
    }

    public void turnMinutePanel(){
        if(calendar == null) return;
        panelMode = MINUTE_PANEL;
        int minNum = getRangeMinNum(calendar, Calendar.MINUTE);//calendar.getActualMinimum(Calendar.MINUTE)
        int maxNum = calendar.getActualMaximum(Calendar.MINUTE);
        setRange(minNum, maxNum);
        setSelectNumberIndex(calendar.get(Calendar.MINUTE));
    }

    @Override
    public void OnNumberSelect(int num) {
        if(calendar == null) return;
        if(calenderChangeListener == null) return;
        switch (panelMode){
            case MONTH_PANEL:
                calendar.set(Calendar.MONTH, num - 1);
                break;
            case DAY_PANEL:
                calendar.set(Calendar.DAY_OF_MONTH, num);
                break;
            case HOUR_PANEL:
                calendar.set(Calendar.HOUR_OF_DAY, num);
                break;
            case MINUTE_PANEL:
                calendar.set(Calendar.MINUTE, num);
                break;
        }
        calenderChangeListener.onCalenderChanged(calendar);
    }


    private int getRangeMinNum(Calendar calendar, int field){
        Calendar calendarNow = Calendar.getInstance();
        boolean sameMonth = calendarNow.get(Calendar.MONTH) == calendar.get(Calendar.MONTH);
        boolean sameDay = calendarNow.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH);
        boolean sameHour = calendarNow.get(Calendar.HOUR_OF_DAY) == calendar.get(Calendar.HOUR_OF_DAY);

        int minNum = calendar.getActualMinimum(field);
        int currNum = calendarNow.get(field);

        if(field == Calendar.MONTH){
            return currNum;
        }else if(field == Calendar.DAY_OF_MONTH){
            return sameMonth ? currNum : minNum;
        }else if(field == Calendar.HOUR_OF_DAY){
            return sameMonth && sameDay ? currNum : minNum;
        }else if(field == Calendar.MINUTE){
            return sameMonth && sameDay && sameHour ? currNum : minNum;
        }
        return minNum;
    }

    public interface CalenderChangeListener{
        void  onCalenderChanged(Calendar calendar);
    }
}
