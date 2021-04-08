package com.example.administrator.sometest.EditTextTest;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.example.administrator.sometest.R;

public class EditTextActivity extends AppCompatActivity implements TextView.OnEditorActionListener{
    private EditText mActionDoneEditText;
    private EditText mActionGoEditText;
    private EditText mActionNextEditText;
    private EditText mActionNoneEditText;
    private EditText mActionPreviousEditText;
    private EditText mActionSearchEditText;
    private EditText mActionSendEditText;
    private EditText mActionUnspecifiedEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_text);
        initView();
    }

    private void initView() {
        mActionDoneEditText = (EditText) findViewById(R.id.actionDoneEditText);
        mActionGoEditText = (EditText) findViewById(R.id.actionGoEditText);
        mActionNextEditText = (EditText) findViewById(R.id.actionNextEditText);
        mActionNoneEditText = (EditText) findViewById(R.id.actionNoneEditText);
        mActionPreviousEditText = (EditText) findViewById(R.id.actionPreviousEditText);
        mActionSearchEditText = (EditText) findViewById(R.id.actionSearchEditText);
        mActionSendEditText = (EditText) findViewById(R.id.actionSendEditText);
        mActionUnspecifiedEditText = (EditText) findViewById(R.id.actionUnspecifiedEditText);

        mActionDoneEditText.setOnEditorActionListener(this);
        mActionGoEditText.setOnEditorActionListener(this);
        mActionNextEditText.setOnEditorActionListener(this);
        mActionNoneEditText.setOnEditorActionListener(this);
        mActionPreviousEditText.setOnEditorActionListener(this);
        mActionSearchEditText.setOnEditorActionListener(this);
        mActionSendEditText.setOnEditorActionListener(this);
        mActionUnspecifiedEditText.setOnEditorActionListener(this);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        doWhichOperation(actionId);
        Log.e("BALLACK", "event: " + event);
        Log.e("BALLACK", "v.getImeActionId(): " + v.getImeActionId());
        Log.e("BALLACK", "v.getImeOptions(): " + v.getImeOptions());
        Log.e("BALLACK", "----------------------------------------------");
        return true;
    }

    private void doWhichOperation(int actionId) {
        switch (actionId) {
            case EditorInfo.IME_ACTION_DONE:
                Log.e("BALLACK", "IME_ACTION_DONE");
                break;
            case EditorInfo.IME_ACTION_GO:
                Log.e("BALLACK", "IME_ACTION_GO");
                break;
            case EditorInfo.IME_ACTION_NEXT:
                Log.e("BALLACK", "IME_ACTION_NEXT");
                break;
            case EditorInfo.IME_ACTION_NONE:
                Log.e("BALLACK", "IME_ACTION_NONE");
                break;
            case EditorInfo.IME_ACTION_PREVIOUS:
                Log.e("BALLACK", "IME_ACTION_PREVIOUS");
                break;
            case EditorInfo.IME_ACTION_SEARCH:
                Log.e("BALLACK", "IME_ACTION_SEARCH");
                break;
            case EditorInfo.IME_ACTION_SEND:
                Log.e("BALLACK", "IME_ACTION_SEND");
                break;
            case EditorInfo.IME_ACTION_UNSPECIFIED:
                Log.e("BALLACK", "IME_ACTION_UNSPECIFIED");
                break;
            default:
                break;
        }
    }
}
