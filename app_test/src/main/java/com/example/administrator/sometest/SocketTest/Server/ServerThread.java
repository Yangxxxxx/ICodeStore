package com.example.administrator.sometest.SocketTest.Server;

import java.io.*;
import java.net.*;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;


// 负责处理每个线程通信的线程类
public class ServerThread implements Runnable {
    // 定义当前线程所处理的Socket
    Socket s = null;
    // 该线程所处理的Socket所对应的输入流
    BufferedReader br = null;

    DataOutputStream dataOutputStream;

    Rectangle screenRectangle;

    Robot robot;

    public ServerThread(Socket s) throws IOException {
        this.s = s;
        // 初始化该Socket对应的输入流
        br = new BufferedReader(new InputStreamReader(s.getInputStream()));
        dataOutputStream = new DataOutputStream(s.getOutputStream());

        try {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            screenRectangle = new Rectangle(screenSize);
            robot = new Robot();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        String content = null;
        // 采用循环不断从Socket中读取客户端发送过来的数据
        while ((content = readFromClient()) != null) {

            if(content.equals("end")){
                try {
                    s.close();
                }catch (Exception e){

                }
//                MyServer.socketList.remove(s); // ①
                System.out.println("disconnect to " + s.getRemoteSocketAddress().toString() + " " + "normal");
                return;
            }

            if (content.equals("s")) {
                captureScreen(s);

//                for (Socket s : MyServer.socketList) {
//                    if (s.isClosed() || !s.isConnected()) continue;
//                    captureScreen(s);
//                }
            }
        }
    }

    // 定义读取客户端数据的方法
    private String readFromClient() {
        try {
            return br.readLine();
        }
        // 如果捕捉到异常，表明该Socket对应的客户端已经关闭
        catch (Exception e) {
            // 删除该Socket。
//            MyServer.socketList.remove(s); // ①
            System.out.println("disconnect to " + s.getRemoteSocketAddress().toString() + " " + e.getMessage());
        }
        return null;
    }

    public void captureScreen(Socket socket) {
        try {
            BufferedImage image = robot.createScreenCapture(screenRectangle);


            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", byteArrayOutputStream);
            byte[] picData = byteArrayOutputStream.toByteArray();

            dataOutputStream.writeInt(picData.length);
            dataOutputStream.write(picData);
            dataOutputStream.flush();


        } catch (Exception e) {
            System.out.println("截图失败: " + e.getMessage());
        }
    }
}
