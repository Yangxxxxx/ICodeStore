package com.example.administrator.sometest.RoomTest;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.administrator.sometest.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class RoomTestActivity extends AppCompatActivity {
    PersonDatabase personDatabase;
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_test);

        personDatabase =
                Room.databaseBuilder(getApplication(), PersonDatabase.class, "person.db")
                        .allowMainThreadQueries()
                .build();
        personDatabase.getPersonDao().insert(new Person("aaa", 0));

        findViewById(R.id.rl_root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyDB2sdcard();
            }
        });

        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Person person = new Person("bbb", count++);
                person.setFirstWork(new Work("HX", "beijing"));

                personDatabase.getPersonDao().insert(person);
            }
        });
    }



    private void copyDB2sdcard(){
        String dbPath = getApplication().getDatabasePath("person") + ".db";
        Log.e("yang", "dbPath: " + dbPath);
        String sdcardPath = "/sdcard/livechat.db";
        copyFile(dbPath, sdcardPath, true);
    }


    public boolean copyFile(String srcFileName, String destFileName,
                                   boolean overlay) {
        File srcFile = new File(srcFileName);

        // 判断源文件是否存在
        if (!srcFile.exists() && !srcFile.isFile()) {
            return false;
        }

        // 判断目标文件是否存在
        File destFile = new File(destFileName);
        if (destFile.exists()) {
            // 如果目标文件存在并允许覆盖
            if (overlay) {
                // 删除已经存在的目标文件，无论目标文件是目录还是单个文件
                new File(destFileName).delete();
            }
        } else {
            // 如果目标文件所在目录不存在，则创建目录
            File parentFile = destFile.getAbsoluteFile().getParentFile();
            if (parentFile != null && !parentFile.exists()) {
                // 目标文件所在目录不存在
                if (!parentFile.mkdirs()) {
                    // 复制文件失败：创建目标文件所在目录失败
                    return false;
                }
            } else {
//                return false;
            }
        }

        // 复制文件
        int byteread = 0; // 读取的字节数
        InputStream in = null;
        OutputStream out = null;

        try {
            in = new FileInputStream(srcFile);
            out = new FileOutputStream(destFile);
            byte[] buffer = new byte[1024];

            while ((byteread = in.read(buffer)) != -1) {
                out.write(buffer, 0, byteread);
            }
            return true;
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        } finally {
            try {
                if (out != null)
                    out.close();
                if (in != null)
                    in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
