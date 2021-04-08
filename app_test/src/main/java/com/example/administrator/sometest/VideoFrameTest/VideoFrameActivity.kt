package com.example.administrator.sometest.VideoFrameTest

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.media.MediaMetadataRetriever.OPTION_CLOSEST
import android.media.MediaMetadataRetriever.OPTION_CLOSEST_SYNC
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.administrator.sometest.R
import kotlinx.android.synthetic.main.activity_video_frame.*

class VideoFrameActivity : AppCompatActivity() {
    val TAG = "VideoFrameActivity"
    var mediaMetadataRetriever = MediaMetadataRetriever()

    var capPos = 0L
    var capInterval = 500

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_frame)

        video_view.setVideoPath("/sdcard/2.mp4")
        mediaMetadataRetriever.setDataSource("/sdcard/2.mp4")

        val videoLen = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)

        iv_frame.setOnClickListener {
            if(video_view.isPlaying){
                Log.e(TAG, "stat")
                video_view.pause()
                Log.e(TAG, "pause")
                val curPos = video_view.currentPosition * 1000

                Log.e(TAG, "currentPosition")
                val frame = mediaMetadataRetriever.getFrameAtTime(curPos.toLong(), OPTION_CLOSEST)

                Log.e(TAG, "getFrameAtTime " + frame)
                iv_frame.setImageBitmap(frame)
            }else{
                video_view.start()
            }
        }

        iv_frame2.setOnClickListener {
            val frame = mediaMetadataRetriever.getFrameAtTime(capPos * 1000, OPTION_CLOSEST_SYNC)

            tv_info.setText(capPos.toString())
            Log.e(TAG, "getFrameAtTime " + capPos + "  " +  frame)
            iv_frame2.setImageBitmap(frame)
            capPos += capInterval

            if(capPos > videoLen.toLong()) {
                capPos = videoLen.toLong()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaMetadataRetriever.release()
    }
}
