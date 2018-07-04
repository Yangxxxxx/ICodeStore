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

import com.example.jtnote.Constants;
import com.example.jtnote.R;
import com.example.jtnote.UsageInterface.InoteService;
import com.example.jtnote.bean.NoteItem;
import com.example.jtnote.service.NoteService;

public class TimePickerFragment extends Fragment{
    private NoteItem noteItem;
    private InoteService inoteService;

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
        view.findViewById(R.id.tv_alarm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noteItem.setAlarmTime(System.currentTimeMillis() + 1000*20);
                inoteService.newAlarmtask(noteItem);
            }
        });
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
