package com.example.administrator.sometest.SocketTest;

import android.util.Log;

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
	private  PrintStream printStream;

	public MyClient(ImageThread.ReceiveDataListener receiveDataListener){
		try {
			socket = new Socket("192.168.0.108" , 30000);
			// 客户端启动ClientThread线程不断读取来自服务器的数据
			new Thread(new ImageThread(socket, receiveDataListener)).start();   // ①
			// 获取该Socket对应的输出流
			printStream = new PrintStream(socket.getOutputStream());
		}catch (Exception e){
			Log.e("yang", "new socket error");
			e.printStackTrace();
		}

	}

	public void sendText(String text){
		printStream.println(text);
	}

	public void close(){
		try {
			sendText("end");
			printStream.close();
			socket.close();
		}catch (Exception e){
			e.printStackTrace();
		}

	}
}
