package com.example.jtnote.ui.AlarmRingPage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.jtnote.Constants;
import com.example.jtnote.INoteApplication;
import com.example.jtnote.Model;
import com.example.jtnote.R;
import com.example.jtnote.bean.NoteItem;
import com.example.jtnote.utils.CommonUtils;

public class AlarmRingActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView contentView;

    private NoteItem noteItem;

    Vibrator vibrator;

    public static void start(Context context, NoteItem noteItem){
        if(!CommonUtils.isScreenOn(context)){
            CommonUtils.turnOnScreen(context);
        }

        Intent intent = new Intent(context, AlarmRingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Constants.KEY_NOTEITEM_PARAM, noteItem);
        context.startActivity(intent);
    }

    public static Intent getIntent(NoteItem noteItem){
        Intent intent = new Intent(INoteApplication.getInstance(), AlarmRingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Constants.KEY_NOTEITEM_PARAM, noteItem);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_SECURE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_ring);

        Intent intent = getIntent();
        if(intent != null){
            noteItem = (NoteItem)intent.getSerializableExtra(Constants.KEY_NOTEITEM_PARAM);
        }

        contentView = findViewById(R.id.tv_content);
        contentView.setText(noteItem.getTextContent());
        findViewById(R.id.tv_close).setOnClickListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(vibrator != null){
            vibrator.cancel();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        vibrationHint();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.tv_close:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Model.getInstance().noteStateChanged();
        super.onBackPressed();
    }

    private void vibrationHint(){
        vibrator = (Vibrator)this.getSystemService(this.VIBRATOR_SERVICE);
        if(vibrator != null) {
            long[] patter = {1000, 1000, 1000, 1000, 1000, 1000};
            vibrator.vibrate(patter, -1);
        }
    }
}
