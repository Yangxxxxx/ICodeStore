package cn.bingoogolapple.qrcode.zxing;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.bingoogolapple.qrcode.core.QRCodeView;

public class ZXingView extends QRCodeView {
    private MultiFormatReader mMultiFormatReader;

    public ZXingView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ZXingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initMultiFormatReader();
    }

    private void initMultiFormatReader() {
        mMultiFormatReader = new MultiFormatReader();
        mMultiFormatReader.setHints(QRCodeDecoder.HINTS);
    }

    @Override
    public String processData(byte[] data, int width, int height, boolean isRetry) {
        String result = null;
        Result rawResult = null;
        Rect rect = mScanBoxView.getScanBoxAreaRect(height);

        try {
            PlanarYUVLuminanceSource source = null;
            if (rect != null) {
                source = new PlanarYUVLuminanceSource(data, width, height, rect.left, rect.top, rect.width(), rect.height(), false);
            } else {
                source = new PlanarYUVLuminanceSource(data, width, height, 0, 0, width, height, false);
            }

            long preTime = System.currentTimeMillis();
//            Log.e("yang", "BGA right sepnd time: " + preTime +" "+ width + " " + height + " " + rect);
            rawResult = mMultiFormatReader.decodeWithState(new BinaryBitmap(new HybridBinarizer(source)));
            Log.e("yang", "BGA right sepnd time: " + (System.currentTimeMillis() - preTime) +" "+ width + " " + height + " " + rect);
        } catch (Exception e) {
            e.printStackTrace();
//            Log.e("yang", "BGA wrong sepnd time: " + (System.currentTimeMillis() - preTime) + " "+ width + " " + height + " " + rect);
        } finally {
            mMultiFormatReader.reset();
        }

        getFileFromBytes(data, "/sdcard/yuv2/" + saveCount++);

        if (rawResult != null) {
            result = rawResult.getText();
        }
        return result;
    }

    int saveCount = 0;

    public static File getFileFromBytes(byte[] b, String outputFile) {
        File ret = null;
        BufferedOutputStream stream = null;
        try {
            ret = new File(outputFile);
            FileOutputStream fstream = new FileOutputStream(ret);
            stream = new BufferedOutputStream(fstream);
            stream.write(b);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return ret;
    }
}