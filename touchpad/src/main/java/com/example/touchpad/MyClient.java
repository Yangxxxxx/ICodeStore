package com.example.touchpad;

import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

/**
 * Description:
 * <br/>网站: <a href="http://www.crazyit.org">疯狂Java联盟</a>
 * <br/>Copyright (C), 2001-2016, Yeeku.H.Lee
 * <br/>This program is protected by copyright laws.
 * <br/>Program Name:
 * <br/>Date:
 * @author Yeeku.H.Lee kongyeeku@163.com
 * @version 1.0
 */
public class MyClient
{
	Socket socket;
	DataOutputStream dataOutputStream;
	boolean isConnected;

	public MyClient(){
		new Thread(){
			@Override
			public void run() {
				super.run();
				try {
					socket = new Socket("192.168.3.96" , 30000);
					dataOutputStream = new DataOutputStream(socket.getOutputStream());
					isConnected = true;
				}catch (Exception e){
					Log.e("yang", "new socket error");
					e.printStackTrace();
				}
			}
		}.start();
	}

	public void writeInt(int value){
		if(!isConnected) return;
		try {
			dataOutputStream.writeInt(value);
			dataOutputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public void close(){
		try {
			dataOutputStream.close();
			socket.close();
			isConnected = false;
		}catch (Exception e){
			e.printStackTrace();
		}

	}
}
