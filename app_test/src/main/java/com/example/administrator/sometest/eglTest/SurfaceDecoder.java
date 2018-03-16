package com.example.administrator.sometest.eglTest;

import android.graphics.SurfaceTexture;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Surface;

import com.example.administrator.sometest.eglTest.VideoPlayer.DecoderEncoderSynCtrl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by guoheng on 2016/9/1.
 */
public class SurfaceDecoder {

    private static final String TAG = "EncodeDecodeSurface";
    private static final boolean VERBOSE = false;           // lots of logging

    int saveWidth = 1920;
    int saveHeight = 1080;

    MediaCodec decoder = null;

    MediaExtractor extractor = null;

    public int DecodetrackIndex;

    private MediaCodec.BufferInfo mBufferInfo = new MediaCodec.BufferInfo();

    // where to find files (note: requires WRITE_EXTERNAL_STORAGE permission)
    private static final File FILES_DIR = Environment.getExternalStorageDirectory();
    private static final String INPUT_FILE = "1.mp4";

    private Runnable afterDecodeOneFrameTask;

    private boolean mIsStopRequested;

    private boolean isLoop;

    private DecodeLoopListener decodeLoopListener;

    private FramePresentationListener framePresentationListener;

    private SpeedControl speedControl = new SpeedControl();

    public void setDoLoop(boolean doLoop){
        this.isLoop = doLoop;
    }

    public SurfaceDecoder setTask(Runnable runnable){
        afterDecodeOneFrameTask = runnable;
        return this;
    }


    public SurfaceDecoder SurfaceDecoderPrePare(Surface encodersurface, String filePath) {
        try {
            File inputFile = new File(FILES_DIR, INPUT_FILE);   // must be an absolute path

            if(!TextUtils.isEmpty(filePath)){
                inputFile = new File(filePath);
            }

            if (!inputFile.canRead()) {
                throw new FileNotFoundException("Unable to read " + inputFile);
            }
            extractor = new MediaExtractor();
            extractor.setDataSource(inputFile.toString());
            DecodetrackIndex = selectTrack(extractor);
            if (DecodetrackIndex < 0) {
                throw new RuntimeException("No video track found in " + inputFile);
            }
            extractor.selectTrack(DecodetrackIndex);

            MediaFormat format = extractor.getTrackFormat(DecodetrackIndex);
            if (VERBOSE) {
                Log.d(TAG, "Video size is " + format.getInteger(MediaFormat.KEY_WIDTH) + "x" +
                        format.getInteger(MediaFormat.KEY_HEIGHT));
            }

            String mime = format.getString(MediaFormat.KEY_MIME);
            decoder = MediaCodec.createDecoderByType(mime);
            decoder.configure(format, encodersurface, null, 0);
            decoder.start();

//            doExtract();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }


    int decodeIndex;

    public void doExtract() {
        Log.e("yang11", "enter decoder: " + Thread.currentThread().getName());
        decodeIndex = 0;
        int trackIndex = DecodetrackIndex;

        final int TIMEOUT_USEC = 10000;
        ByteBuffer[] decoderInputBuffers = decoder.getInputBuffers();
        int inputChunk = 0;
        long firstInputTimeNsec = -1;

        boolean outputDone = false;
        boolean inputDone = false;
        while (!outputDone) {
            if (VERBOSE) Log.d(TAG, "loop");
            if (mIsStopRequested) {
                Log.d(TAG, "Stop requested");
                break;
            }


            // Feed more data to the decoder.
            SurfaceTexture surfaceTexture=new SurfaceTexture(1);
            Surface surface=new Surface(surfaceTexture);
            if (!inputDone) {
                int inputBufIndex = decoder.dequeueInputBuffer(TIMEOUT_USEC);
                if (inputBufIndex >= 0) {
                    if (firstInputTimeNsec == -1) {
                        firstInputTimeNsec = System.nanoTime();
                    }
                    ByteBuffer inputBuf = decoderInputBuffers[inputBufIndex];
                    // Read the sample data into the ByteBuffer.  This neither respects nor
                    // updates inputBuf's position, limit, etc.
                    int chunkSize = extractor.readSampleData(inputBuf, 0);
                    if (chunkSize < 0) {
                        // End of stream -- send empty frame with EOS flag set.
                        decoder.queueInputBuffer(inputBufIndex, 0, 0, 0L,
                                MediaCodec.BUFFER_FLAG_END_OF_STREAM);
                        inputDone = true;
                        if (VERBOSE) Log.d(TAG, "sent input EOS");
                    } else {
                        if (extractor.getSampleTrackIndex() != trackIndex) {
                            Log.w(TAG, "WEIRD: got sample from track " +
                                    extractor.getSampleTrackIndex() + ", expected " + trackIndex);
                        }
                        long presentationTimeUs = extractor.getSampleTime();
                        decoder.queueInputBuffer(inputBufIndex, 0, chunkSize,
                                presentationTimeUs, 0 /*flags*/);
                        if (VERBOSE) {
                            Log.d(TAG, "submitted frame " + inputChunk + " to dec, size=" +
                                    chunkSize);
                        }
                        inputChunk++;
                        extractor.advance();
                    }
                } else {
                    if (VERBOSE) Log.d(TAG, "input buffer not available");
                }
            }

            boolean doLoop = false;
            if (!outputDone) {
                int decoderStatus = decoder.dequeueOutputBuffer(mBufferInfo, TIMEOUT_USEC);
                if (decoderStatus == MediaCodec.INFO_TRY_AGAIN_LATER) {
                    // no output available yet
                    if (VERBOSE) Log.d(TAG, "no output from decoder available");
                } else if (decoderStatus == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
                    // not important for us, since we're using Surface
                    if (VERBOSE) Log.d(TAG, "decoder output buffers changed");
                } else if (decoderStatus == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                    MediaFormat newFormat = decoder.getOutputFormat();
                    if (VERBOSE) Log.d(TAG, "decoder output format changed: " + newFormat);
                } else if (decoderStatus < 0) {
                    throw new RuntimeException(
                            "unexpected result from decoder.dequeueOutputBuffer: " +
                                    decoderStatus);
                } else { // decoderStatus >= 0
                    if (firstInputTimeNsec != 0) {
                        // Log the delay from the first buffer of input to the first buffer
                        // of output.
                        long nowNsec = System.nanoTime();
                        Log.d(TAG, "startup lag " + ((nowNsec-firstInputTimeNsec) / 1000000.0) + " ms");
                        firstInputTimeNsec = 0;
                    }
                    if (VERBOSE) Log.d(TAG, "surface decoder given buffer " + decoderStatus +
                            " (size=" + mBufferInfo.size + ")");
                    if ((mBufferInfo.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                        if (VERBOSE) Log.d(TAG, "output EOS");
                        if (isLoop) {
                            doLoop = true;
                        } else {
                            outputDone = true;
                        }
                    }


//                    speedControl.preRender(mBufferInfo.presentationTimeUs);
                    decodeIndex++;
                    if(framePresentationListener != null) framePresentationListener.onFramePresent(mBufferInfo.presentationTimeUs);
                    DecoderEncoderSynCtrl.getInstance().waitMinute();

//                    try {
//                        long sleepTime = mBufferInfo.presentationTimeUs/1000 - preTime;
//                        if(sleepTime < 0) sleepTime = 0;
//                        Thread.sleep(sleepTime);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    preTime = mBufferInfo.presentationTimeUs/1000;

                    boolean doRender = (mBufferInfo.size != 0);
                    decoder.releaseOutputBuffer(decoderStatus, doRender);

                    if(afterDecodeOneFrameTask != null ){
                        afterDecodeOneFrameTask.run();
                    }

                    if (doLoop) {
                        Log.d(TAG, "Reached EOS, looping");
                        extractor.seekTo(0, MediaExtractor.SEEK_TO_CLOSEST_SYNC);
                        inputDone = false;
                        decoder.flush();    // reset decoder state
//                        frameCallback.loopReset();
                    }

                    if(doLoop || outputDone){
                        if(decodeLoopListener != null) decodeLoopListener.onceLoopEnd();
                    }

                }
            }
        }

        Log.e("yang10", "decodeIndex:  " + decodeIndex);
        release();
                Log.e("yang11", "leave decoder: " + Thread.currentThread().getName());
    }


    private long preTime=0;

    public void setStop(boolean state){
        mIsStopRequested  = state;
    }


    private int selectTrack(MediaExtractor extractor) {
        // Select the first video track we find, ignore the rest.
        int numTracks = extractor.getTrackCount();
        for (int i = 0; i < numTracks; i++) {
            MediaFormat format = extractor.getTrackFormat(i);
            String mime = format.getString(MediaFormat.KEY_MIME);
            if (mime.startsWith("video/")) {
                if (VERBOSE) {
                    Log.d(TAG, "Extractor selected track " + i + " (" + mime + "): " + format);
                }
                return i;
            }
        }

        return -1;
    }


   public void release() {
        if (decoder != null) {
            decoder.stop();
            decoder.release();
            decoder = null;
        }
        if (extractor != null) {
            extractor.release();
            extractor = null;
        }

    }

    public void setDecodeLoopListener(DecodeLoopListener decodeLoopListener) {
        this.decodeLoopListener = decodeLoopListener;
    }

    public void setFramePresentationListener(FramePresentationListener framePresentationListener) {
        this.framePresentationListener = framePresentationListener;
    }

    public interface DecodeLoopListener{
        void onceLoopEnd();
    }

    public interface FramePresentationListener{
        void onFramePresent(long time);
    }


}
