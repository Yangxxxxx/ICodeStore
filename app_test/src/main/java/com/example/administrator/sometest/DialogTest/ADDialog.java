package com.example.administrator.sometest.DialogTest;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Administrator on 2017/3/28 0028.
 */

public class ADDialog extends Dialog {

    private int adType = -1;

    private OnCloseListener onCloseListener;

    public ADDialog(Context context) {
        super(context);
//        this(context, android.R.style.Theme_NoTitleBar_Fullscreen/*R.style.ad_dialog*/);
    }

    public ADDialog(Context context, int themeResId) {
        super(context, themeResId);
    }


    public ADDialog setADType(int type){
        adType = type;
//        if(isLoadedAD(LOCATION_ADMOD)) adType = LOCATION_ADMOD;

//        initAD();
        return this;
    }

    public ADDialog enableBackPress(boolean enable){
        setCancelable(enable);
        return this;
    }

    public ADDialog setOnCloseListener(OnCloseListener onCloseListener){
        this.onCloseListener = onCloseListener;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCanceledOnTouchOutside(false);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        setCancelable(false);//点击返回键不让推出广告
//        setContentView(R.layout.dialog_ad);
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Log.e("yang", "ADDialog onDismiss1");
                if (adType != -1) {
//                    mAdController.detachAd(adType);
                }
            }
        });

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Log.e("yang", "ADDialog onDismiss2");
                if(onCloseListener != null){
                    onCloseListener.onClose();
                }
            }
        });

//        findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//            }
//        });
//        initAD();
    }

    @Override
    public void show() {
        if(isLoadedAD(adType)) {
            super.show();
        }
    }

    private void initAD(){
        if (adType != -1) {
//            mAdController.requestAd(adType);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
//        if (adType != -1) {
//            try {
//                mAdController.attachAd(adType, (ViewGroup) findViewById(R.id.ad_container));
//            } catch (NotRegistedException e) {
//                e.printStackTrace();
//            }
//        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (adType != -1) {
//            mAdController.detachAd(adType);
        }
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
//        if (adType != -1) {
//            try {
//                mAdController.attachAd(adType, (ViewGroup) findViewById(R.id.ad_container));
//            } catch (NotRegistedException e) {
//                e.printStackTrace();
//            }
//        }
    }

  public  interface OnCloseListener{
        void onClose();
    }


    public boolean isLoadedAD(int location){
//        try {
//            return mAdController.isLoaded(location);
//        } catch (NotRegistedException e) {
////            e.printStackTrace();
//            Log.e("Ad", "location have not registed");
//            return false;
//        }
        return true;
    }

}
