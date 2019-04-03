package com.example.yangjitao.icodestore.Utils;

import com.example.yangjitao.icodestore.LocalPhotoExhibition.Album;

/**
 * Created by Administrator on 2017/3/27 0027.
 */

public class GlobalData {
    private static GlobalData globalData = new GlobalData();
    private GlobalData(){}
    public static GlobalData getInstance(){
        return globalData;
    }
    /***************************************************************/

    public Album selectedAlubm;
}
