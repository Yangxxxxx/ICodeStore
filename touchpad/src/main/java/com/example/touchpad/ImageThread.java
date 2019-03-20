package com.example.touchpad;

import android.util.Log;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ImageThread implements Runnable {
    // 该线程负责处理的Socket
    private Socket s;
    private ReceiveDataListener receiveDataListener;

    public ImageThread(Socket s, ReceiveDataListener receiveDataListener) throws IOException {
        this.s = s;
        this.receiveDataListener = receiveDataListener;
    }

    public void run() {
        try {
            DataInputStream dataInputStream = new DataInputStream(s.getInputStream());
            byte[] unitContent = new byte[1024*50];
            byte[] allContent = new byte[1024*1024];

            while (true){
                int totalLen = 0;
                int len = 0;
                final int DATA_LEN = dataInputStream.readInt();
                Log.e("yang", "filter total len: " + DATA_LEN);
                while ((len = dataInputStream.read(unitContent)) > 0){
                    System.arraycopy(unitContent, 0, allContent, totalLen, len);
                    totalLen += len;

                    Log.e("yang", "read len: " + len);
                    if(totalLen == DATA_LEN) break;
                }

                Log.e("yang", "read doen");
                if(receiveDataListener != null){
                    receiveDataListener.onDataReceive(allContent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface ReceiveDataListener{
        void onDataReceive(byte[] data);
    }
}
