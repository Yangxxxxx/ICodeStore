package com.example.administrator.sometest.mp4parser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.administrator.sometest.R
import java.io.IOException

class MP4ParserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mp4_parser)



        Thread(){
            kotlin.run {
                Log.e("MP4ParserActivity", "start trim")
                val srcPath = "/sdcard/2.mp4"
                val outPath = "/sdcard/result.mp4"
                val startTime: Long = 5000
                val endTime = 20000.toLong()

                try {
                    VideoClipUtils.clip(srcPath, outPath, startTime.toDouble(), endTime.toDouble())
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                Log.e("MP4ParserActivity", "end trim")

            }
        }.start()


    }
}
