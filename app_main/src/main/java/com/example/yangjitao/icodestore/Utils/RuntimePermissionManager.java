package com.example.yangjitao.icodestore.Utils;

import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import static android.os.Build.VERSION_CODES.M;

/**
 * 运行时权限申请
 * 其中：setMainActivity、onRequestPermissionsResult、releasActivityReference需要在主activity中调用
 */

public class RuntimePermissionManager {
    private static final RuntimePermissionManager ourInstance = new RuntimePermissionManager();

    public  static RuntimePermissionManager getInstance() {
        return ourInstance;
    }

    private RuntimePermissionManager() {
    }
    //----------------------------------------------------------------------------------------------
    private final int REQUEST_CODE = 101;
    public static final int STATE_GRANT = 0;
    public static final int STATE_DENY = 1;
    public static final int STATE_NEVER_ASK = 2;
    private Activity mainActivity;
    private OnRequestListener onRequestListener;

    public void setMainActivity(Activity activity){
        mainActivity = activity;
    }

    /** 在activity中的onRequestPermissionsResult的调用，用于传回用户授权结果*/
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(requestCode != REQUEST_CODE) return;
        boolean isAllGranted = true;
        int[] myResult = new int[grantResults.length];
        for(int i = 0; i < grantResults.length; i++){
            int itemResult = grantResults[i];
            isAllGranted = isAllGranted && (itemResult == PackageManager.PERMISSION_GRANTED);
            myResult[i] = itemResult == PackageManager.PERMISSION_GRANTED ? STATE_GRANT : STATE_DENY;

            //用户勾选了 “不再提醒”
            if(myResult[i] == STATE_DENY && ActivityCompat.shouldShowRequestPermissionRationale(mainActivity, permissions[i])) {
                myResult[i] = STATE_NEVER_ASK;
            }
        }

        if(onRequestListener != null) onRequestListener.onRequest(isAllGranted, permissions, grantResults);
    }

    /** 申请权限*/
    public void requestPermission(String[] permissions, OnRequestListener onRequestListener){
        this.onRequestListener = onRequestListener;

        //找出还没有授权的权限
        List<String> needRequestList = new ArrayList<>();
        for (String item: permissions){
            boolean grant1 = ContextCompat.checkSelfPermission(mainActivity, item) == PackageManager.PERMISSION_GRANTED;
            boolean grant2 = checkOpsPermission(mainActivity, item);//小米系统需要这个来检测权限
            if(!grant1 || !grant2){
                needRequestList.add(item);
            }
        }

        //都已拥有授权
        if(needRequestList.size() == 0){
            int[] result = new int[permissions.length];
            for(int i = 0; i < result.length; i++){
                result[i] = PackageManager.PERMISSION_GRANTED;
            }
            if(onRequestListener != null) onRequestListener.onRequest(true, permissions, result);
            return;
        }

        String[]  needRequestArr = new String[needRequestList.size()];
        ActivityCompat.requestPermissions(mainActivity, needRequestList.toArray(needRequestArr), REQUEST_CODE);
    }

    public void releasActivityReference(){
        mainActivity = null;
    }

    /** 进入到app的详情页面*/
    public void startApplicationDetailPage() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", mainActivity.getPackageName(), null);
        intent.setData(uri);
        mainActivity.startActivityForResult(intent, 100);
    }

    private boolean checkOpsPermission(Context context, String permission) {
        if(Build.VERSION.SDK_INT < M) return true;
        try {
            AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            String opsName = AppOpsManager.permissionToOp(permission);
            if (opsName == null) {
                return true;
            }
            int opsMode = appOpsManager.checkOpNoThrow(opsName, android.os.Process.myUid(), context.getPackageName());
            return opsMode == AppOpsManager.MODE_ALLOWED;
        } catch (Exception ex) {
            return true;
        }
    }

    public interface OnRequestListener{
        void onRequest(boolean isAllGranted, String[] permissions, int[] results);
    }
}
