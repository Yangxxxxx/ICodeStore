package com.example.jtnote.ui.TimePickerPage;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jtnote.Constants;
import com.example.jtnote.Model;
import com.example.jtnote.R;
import com.example.jtnote.UsageInterface.InoteService;
import com.example.jtnote.bean.NoteItem;
import com.example.jtnote.service.NoteService;
import com.example.jtnote.ui.MainPage.MainPageContract;
import com.example.jtnote.utils.CommonUtils;
import com.example.jtnote.widget.TimePanel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TimePickerFragment extends Fragment implements View.OnClickListener, TimePanel.CalenderChangeListener{
    private NoteItem noteItem;
    private InoteService inoteService;
    private Calendar calendar = Calendar.getInstance();
    private List<TextView> timeViewList = new ArrayList<>();

    private TimePanel numberPanel;
    private TextView yearView;
    private TextView monthView;
    private TextView dayView;
    private TextView hourView;
    private TextView minuteView;
    private TextView hintView;
    private ImageButton resetButton;

    public static TimePickerFragment newInstance(NoteItem noteItem) {
        Bundle args = new Bundle();
        args.putSerializable(Constants.KEY_NOTEITEM_PARAM, noteItem);
        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null){
            noteItem = (NoteItem) bundle.getSerializable(Constants.KEY_NOTEITEM_PARAM);
        }
        calendar.set(Calendar.SECOND, 0);
        if(noteItem.getAlarmTime() != 0){
            calendar.setTimeInMillis(noteItem.getAlarmTime());
        }
        getContext().bindService(new Intent(getContext(), NoteService.class), serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_time_picker, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.tv_reset:
                removeAlarm();
                break;
            case R.id.tv_month:
                numberPanel.turnMonthPanel();
                break;
            case R.id.tv_day:
                numberPanel.turnDayPanel();
                break;
            case R.id.tv_hour:
                numberPanel.turnHourPanel();
                break;
            case R.id.tv_minute:
                numberPanel.turnMinutePanel();
                break;
        }
        hightlightSelectedView(v);
    }

    @Override
    public void onCalenderChanged(Calendar calendar) {
        alarmNewTime(calendar.getTimeInMillis());
        showSelectedTime(calendar);
        showHintInfo();
        showRemoveButton();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContext().unbindService(serviceConnection);
    }

    private void initView(View view){
        resetButton = view.findViewById(R.id.tv_reset);
        hintView = view.findViewById(R.id.tv_hint);
        yearView = view.findViewById(R.id.tv_year);
        monthView = view.findViewById(R.id.tv_month);
        dayView = view.findViewById(R.id.tv_day);
        hourView = view.findViewById(R.id.tv_hour);
        minuteView = view.findViewById(R.id.tv_minute);
        yearView.setOnClickListener(this);
        monthView.setOnClickListener(this);
        dayView.setOnClickListener(this);
        hourView.setOnClickListener(this);
        minuteView.setOnClickListener(this);
        resetButton.setOnClickListener(this);
        timeViewList.add(monthView);
        timeViewList.add(dayView);
        timeViewList.add(hourView);
        timeViewList.add(minuteView);

        numberPanel = view.findViewById(R.id.time_panel);
        numberPanel.setCalenderChangeListener(this);
        numberPanel.attachCalendar(calendar);
        numberPanel.setRange(-1, -1);
        showSelectedTime(calendar);
        showHintInfo();
        showRemoveButton();
    }

    private void showSelectedTime(Calendar calendar){
        yearView.setText(String.valueOf(calendar.get(Calendar.YEAR)));
        monthView.setText(String.format("%02d", calendar.get(Calendar.MONTH) + 1));
        dayView.setText(String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH)));
        hourView.setText(String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)));
        minuteView.setText(String.format("%02d", calendar.get(Calendar.MINUTE)));
    }

    private void showHintInfo(){
        if(noteItem.getAlarmTime() == 0){
            hintView.setText("点击选择日期");
            return;
        }

        long timeInterval = calendar.getTimeInMillis() - System.currentTimeMillis();
        if(timeInterval < 0){
            hintView.setText("闹钟已过期");
        }else {
            hintView.setText(CommonUtils.getDuration(timeInterval) + " 后提示");
        }
    }

    private void showRemoveButton(){
        resetButton.setVisibility(noteItem.getAlarmTime() == 0 ? View.INVISIBLE : View.VISIBLE);
    }


    private void alarmNewTime(long time){
        long alarmTime = time - System.currentTimeMillis();
        if(alarmTime >= 0) {
            noteItem.setAlarmTime(time);
            inoteService.newAlarmtask(noteItem);
            Model.getInstance().updateNote(noteItem);
        }else {
            Toast.makeText(getContext(), "uncorrect time", Toast.LENGTH_SHORT).show();
        }
    }

    private void cancleAlarm(){
        noteItem.removeAlarm();
        inoteService.cancleAlarmtask(noteItem);
        Model.getInstance().updateNote(noteItem);
    }

    private void removeAlarm(){
        cancleAlarm();
        showHintInfo();
        calendar.setTimeInMillis(System.currentTimeMillis());
        showSelectedTime(calendar);
        numberPanel.setRange(-1, -1);
        showRemoveButton();
    }

    private void hightlightSelectedView(View timeView){
        for (TextView  view: timeViewList){
            boolean isSelectedView = view == timeView;
            view.setSelected(isSelectedView);
        }
    }


    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            inoteService = (InoteService) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };
}


/****************************************************************************************************/

//    private void showSelectedTime(long time){
//        if(time == 0){
//            resetButton.setText("---- -- -- -- --");
//        }else {
//            resetButton.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(time));
//        }
//    }

//        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
//            @Override
//            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
//                Log.e("yang", "onselectedDay");
//                calendar.set(year, month, dayOfMonth);
//
//                showSelectedTime(calendar.getTimeInMillis());
//                alarmNewTime(calendar.getTimeInMillis());
//            }
//        });

//        timePicker.setIs24HourView(true);
//        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
//            @Override
//            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
//                Log.e("yang", "onTimeChange");
//                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
//                calendar.set(Calendar.MINUTE, minute);
//
//                showSelectedTime(calendar.getTimeInMillis());
//                alarmNewTime(calendar.getTimeInMillis());
//            }
//        });