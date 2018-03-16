package com.example.administrator.sometest.TmpActivity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

/**
 * Created by Administrator on 2018/1/31 0031.
 */

public class DimenFileCreator {
    private static final String FILE_HEADER = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";
    private static final String TAG_RESOURCES_HEAD = "<resources>\n";
    private static final String TAG_RESOURCES_TAIL = "</resources>";
    private static final String DIMEN_DP_ITEM = "\t<dimen name=\"n%1$ddp\">%2$.1fdp</dimen>\n";
    private static final String DIMEN_SP_ITEM = "\t<dimen name=\"n%1$dsp\">%2$.1fsp</dimen>\n";
    private static final String FOLDER_NAME = "values-sw%1$ddp";

    private static final int DP_STANDARD = 360;
    private static final int MAX_DP_VALUE = 200;
    private static final int MAX_SP_VALUE = 50;

    public static void main(String[] args){
       for (String item: args){
           try {
               int screenWidth = Integer.parseInt(item);
               createFile(screenWidth);
           }catch (NumberFormatException e){

           }
       }
    }

    private static void createFile(int screenWidth) {
        String folderName = String.format(FOLDER_NAME, screenWidth);
        File folderFile = new File(folderName);
        folderFile.mkdirs();
        File dimenFile = new File(folderFile, "dimen.xml");

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(dimenFile);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fileOutputStream, "UTF-8"));
            writer.write(getFileContent(screenWidth));
            writer.close();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static String getFileContent(int screenWidth){
        String content = FILE_HEADER + TAG_RESOURCES_HEAD;
        float ratio = screenWidth*1f / DP_STANDARD;

        //dp list
        for(int i = 1; i <= MAX_DP_VALUE; i++){
            content += String.format(DIMEN_DP_ITEM, i, i*ratio);
        }

        //sp list
        for(int i = 1; i <= MAX_SP_VALUE; i++){
            content += String.format(DIMEN_SP_ITEM, i, i*ratio);
        }
        content += TAG_RESOURCES_TAIL;
        return content;
    }
}
