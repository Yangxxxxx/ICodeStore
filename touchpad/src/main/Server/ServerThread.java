import java.io.*;
import java.net.*;
import java.awt.Point;
import java.awt.MouseInfo;
import java.awt.Robot;
import java.awt.event.KeyEvent;

import java.text.SimpleDateFormat;
import java.util.Date;


public class ServerThread implements Runnable {
    private final static int ACTION_DOWN = -1;
    private final static int ACTION_CLICK = -2;
    private final static int ACTION_UP = -3;

    private final static int POINTER_ONE = -4;
    private final static int POINTER_TWO = -5;
    private final static int POINTER_THREE = -6;

    private final static int MOUSE_WHEEL_UNIT = 5;
    private final static float MOVE_SCALE_FACTOR = 1.5f;

    Socket s = null;
    DataInputStream dataInputStream;
    Robot robot;

    int pointerCount = POINTER_ONE;
    int preCoorX = -1;
    int preCoorY = -1;
    int scrollDistanceTwoFinger;


    public ServerThread(Socket s) throws IOException {
        this.s = s;
        dataInputStream = new DataInputStream(s.getInputStream());
        try {
            robot = new Robot();
        }catch (Exception e){

        }
    }

    @Override
    public void run() {
        while (true){
            try {

                int coorValue = dataInputStream.readInt();

                if(coorValue == POINTER_ONE || coorValue == POINTER_TWO || coorValue == POINTER_THREE){
                    pointerCount = coorValue;

                    preCoorX = -1;
                    preCoorY = -1;

                    scrollDistanceTwoFinger = 0;
                }

                System.out.println("coorValue: " + coorValue);
//                if(coorValue == ACTION_DOWN){
//                    preCoorX = -1;
//                    preCoorY = -1;
//                    continue;
//                }

                if(coorValue < 0 && coorValue != POINTER_THREE/*coorValue == ACTION_UP || coorValue == POINTER_TWO || coorValue == POINTER_ONE*/){
                    robot.mouseRelease(KeyEvent.BUTTON1_MASK);
                }

                if(coorValue == ACTION_UP){
//                    scrollDistanceTwoFinger = 0;
//                    preCoorX = 0;
//                    preCoorY = 0;
                }

                if(coorValue == POINTER_THREE){
                    System.out.println("POINTER_THREE");
                    robot.mousePress(KeyEvent.BUTTON1_MASK);
                    continue;
                }

                if(coorValue == ACTION_CLICK){
                    System.out.println("click detected");
                    robot.mousePress(KeyEvent.BUTTON1_MASK);
                    robot.mouseRelease(KeyEvent.BUTTON1_MASK);

                    continue;
                }
                if(coorValue < 0){
                    continue;
                }

                int coorX = coorValue / 10000;
                int coorY = coorValue % 10000;


                if(preCoorX == -1 || preCoorY == -1){
                    preCoorX = coorX;
                    preCoorY = coorY;
                }

                int offsetX = coorX - preCoorX;
                int offsetY = coorY - preCoorY;

                Point mousepoint = MouseInfo.getPointerInfo().getLocation();

                if(pointerCount == POINTER_TWO){
                    scrollDistanceTwoFinger += offsetY;
                    if(Math.abs(scrollDistanceTwoFinger) / MOUSE_WHEEL_UNIT > 0){
                        robot.mouseWheel(scrollDistanceTwoFinger > 0 ? -1 : 1);
                        scrollDistanceTwoFinger = scrollDistanceTwoFinger % MOUSE_WHEEL_UNIT;
                    }
                }else {
                    robot.mouseMove((int)(mousepoint.getX() +  offsetX * MOVE_SCALE_FACTOR), (int)(mousepoint.getY() + offsetY * MOVE_SCALE_FACTOR));
                }

                preCoorX = coorX;
                preCoorY = coorY;

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("connect failed!");
                break;
            }
        }

        try {
            dataInputStream.close();
            s.close();
        }catch (IOException e){

        }
    }

}
