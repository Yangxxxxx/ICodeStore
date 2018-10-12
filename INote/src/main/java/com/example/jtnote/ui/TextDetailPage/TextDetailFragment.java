package com.example.jtnote.ui.TextDetailPage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jtnote.Constants;
import com.example.jtnote.Model;
import com.example.jtnote.R;
import com.example.jtnote.bean.NoteItem;
import com.example.jtnote.ui.KeyboardActivity;

import static android.app.Activity.RESULT_OK;

public class TextDetailFragment extends Fragment implements View.OnClickListener {
    private TextView textContentView;

    private NoteItem noteItem;

    public static TextDetailFragment newInstance(NoteItem noteItem) {
        Bundle args = new Bundle();
        args.putSerializable(Constants.KEY_NOTEITEM_PARAM, noteItem);
        TextDetailFragment fragment = new TextDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            noteItem = (NoteItem) bundle.getSerializable(Constants.KEY_NOTEITEM_PARAM);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_text_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.ib_edit).setOnClickListener(this);
        textContentView = view.findViewById(R.id.tv_content);
        textContentView.setText(noteItem.getTextContent());
        textContentView.setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_edit:
                KeyboardActivity.start(this, noteItem.getTextContent(), Constants.KEYBOARD_ACTIVITY_REQUESTCODE);
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Constants.KEYBOARD_ACTIVITY_REQUESTCODE && resultCode == RESULT_OK && data != null){
            String inputStr = data.getStringExtra(Constants.KEY_INPUT_CONTENT);
            textContentView.setText(inputStr);
            noteItem.setTextContent(inputStr);
            Model.getInstance().updateNote(noteItem);
        }
    }
}
