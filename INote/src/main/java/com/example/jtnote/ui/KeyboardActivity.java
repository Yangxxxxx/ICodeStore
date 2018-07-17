package com.example.jtnote.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.jtnote.Constants;
import com.example.jtnote.R;
import com.example.jtnote.widget.InsetableRelativeLayout;

public class KeyboardActivity extends AppCompatActivity implements View.OnClickListener, InsetableRelativeLayout.OnSystemWindowsChangeListener{
    private EditText editText;
    private InsetableRelativeLayout rootView;
    private LinearLayout bottomAreaLayout;

    private boolean showingKeyboard;

    public static void start(Activity activity, int requestCode){
        Intent intent = new Intent(activity, KeyboardActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyboard);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        bottomAreaLayout = findViewById(R.id.ll_bottom_area);
        bottomAreaLayout.setAlpha(0);
        editText = findViewById(R.id.et_input);
        rootView = findViewById(R.id.rl_root);
        rootView.setOnSystemWindowsChangeListener(this);
        findViewById(R.id.tv_confirm).setOnClickListener(this);
        findViewById(R.id.tv_more).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        String inputStr = editText.getText().toString();
        if(TextUtils.isEmpty(inputStr)) return;
        Intent intent = new Intent();
        intent.putExtra(Constants.KEY_INPUT_CONTENT, inputStr);
        if(id == R.id.tv_more){
            intent.putExtra(Constants.KEY_GO_MORE_PAGE, true);
        }
        setResult(RESULT_OK, intent);
        onBackPressed();
    }

    @Override
    public void fitSystemWindows(Rect insets) {
        boolean iskeyboardHide = insets.bottom == 0 && showingKeyboard;
        if(iskeyboardHide){
            finish();
        }else {
            showingKeyboard = insets.bottom > 0;
            if(showingKeyboard){
                changeBottomLayoutPos(insets);
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    private void changeBottomLayoutPos(Rect insets){
//        bottomAreaLayout.setAlpha(1);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) bottomAreaLayout.getLayoutParams();
        layoutParams.bottomMargin = insets.bottom;
        bottomAreaLayout.setLayoutParams(layoutParams);
        bottomAreaLayout.animate().alpha(1).setDuration(500).start();
    }
}
