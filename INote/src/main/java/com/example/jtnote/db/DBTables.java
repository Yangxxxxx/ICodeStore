package com.example.jtnote.db;

import android.provider.BaseColumns;

/**
 * Created by Administrator on 2018/6/14 0014.
 */

public class DBTables {
    public class NoteItemTable implements BaseColumns{
        public static final String TABLE_NAME = "NoteItemTable";

        public static final String TEXT_CONTENT = "text_content";
        public static final String AUDIO_CONTENT = "audio_content";
        public static final String IMAGE_CONTENT = "image_content";
        public static final String VIDEO_CONTENT = "video_content";
        public static final String ALARM_TIME = "alarm_time";
        public static final String CREATE_TIME = "create_time";
    }
}
