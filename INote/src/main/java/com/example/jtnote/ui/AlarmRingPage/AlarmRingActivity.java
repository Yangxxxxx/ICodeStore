package com.example.jtnote.ui.AlarmRingPage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.jtnote.Constants;
import com.example.jtnote.Model;
import com.example.jtnote.R;
import com.example.jtnote.bean.NoteItem;

public class AlarmRingActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView contentView;

    private NoteItem noteItem;

    Vibrator vibrator;

    public static void start(Context context, NoteItem noteItem){
        Intent intent = new Intent(context, AlarmRingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Constants.KEY_NOTEITEM_PARAM, noteItem);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_ring);

        Intent intent = getIntent();
        if(intent != null){
            noteItem = (NoteItem)intent.getSerializableExtra(Constants.KEY_NOTEITEM_PARAM);
        }

        contentView = findViewById(R.id.tv_content);
        contentView.setText(noteItem.getTextContent());
        findViewById(R.id.tv_close).setOnClickListener(this);

        vibrationHint();
    }

    @Override
    protected void onPause() {
        if(vibrator != null){
            vibrator.cancel();
        }
        super.onPause();
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
