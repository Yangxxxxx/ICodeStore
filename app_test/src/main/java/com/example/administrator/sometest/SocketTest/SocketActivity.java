package com.example.administrator.sometest.SocketTest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.administrator.sometest.R;

public class SocketActivity extends AppCompatActivity {
    private MyClient myClient;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_socket);
        imageView = findViewById(R.id.imageview);

        new Thread(){
            @Override
            public void run() {
                super.run();
                myClient = new MyClient(new ImageThread.ReceiveDataListener() {
                    @Override
                    public void onDataReceive(byte[] data) {
                        Log.e("yang", "data size: " + data.length);
                        final Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                imageView.setImageBitmap(bitmap);
                                myClient.sendText("s");
                            }
                        });
                    }
                });

                myClient.sendText("s");

            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myClient.close();
    }
}
