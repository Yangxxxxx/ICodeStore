package com.gxz.example.videoedit

import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.os.Handler
import android.support.v7.widget.RecyclerView
import android.view.MotionEvent

open class VideoCutViewModel(val context: Context, val videoPath: String,  val mMaxWidth: Int, val mUIHandler: Handler){
    private val TAG = "VideoCutViewModel"
    private val MIN_CUT_DURATION = 5 * 1000L// 最小剪辑时间3s
    private val MAX_CUT_DURATION = 15 * 1000L//视频最多剪切多长时间
    private val MAX_COUNT_RANGE = 10//seekBar的区域内一共有多少张图片

    var startProgress = MutableLiveData<Long>()
    var endProgress = MutableLiveData<Long>()

    var videoPauseEvent = SingleLiveEvent<Void>()
    var videoStartEvent = SingleLiveEvent<Void>()
    var videoSeekEvent = SingleLiveEvent<Int>()

    private var leftSeekProgress: Long = 0
    private var rightSeekProgress: Long = 0
    private var scrollProgress: Long = 0

    private var mExtractVideoInfoUtil: ExtractVideoInfoUtil? = null

    private var isSeeking: Boolean = false
    private var duration: Long = 0
    private var averageMsPx: Float = 0F//每毫秒所占的px
    private var OutPutFileDirPath: String? = null
    private var mExtractFrameWorkThread: ExtractFrameWorkThread? = null

    fun start(){
        mExtractVideoInfoUtil = ExtractVideoInfoUtil(videoPath)
        duration = mExtractVideoInfoUtil?.videoLength?.toLong() ?: 0

        val startPosition: Long = 0
        val endPosition = duration
        val thumbnailsCount: Int
        val rangeWidth: Int
        val isOver_60_s: Boolean
        if (endPosition <= MAX_CUT_DURATION) {
            isOver_60_s = false
            thumbnailsCount = MAX_COUNT_RANGE
            rangeWidth = mMaxWidth
        } else {
            isOver_60_s = true
            thumbnailsCount = Math.ceil((endPosition * 1.0f / (MAX_CUT_DURATION * 1.0f) * MAX_COUNT_RANGE).toDouble()).toInt()
            rangeWidth = mMaxWidth / MAX_COUNT_RANGE * thumbnailsCount
        }

        averageMsPx = duration * 1f / rangeWidth
        OutPutFileDirPath = PictureUtils.getSaveEditThumbnailDir(context)
        val extractW = (UIUtil.getScreenWidth(context) - UIUtil.dip2px(context, 70)) / MAX_COUNT_RANGE
        val extractH = UIUtil.dip2px(context, 55)
        mExtractFrameWorkThread = ExtractFrameWorkThread(extractW, extractH, mUIHandler, videoPath, OutPutFileDirPath, startPosition, endPosition, thumbnailsCount)
        mExtractFrameWorkThread?.start()

        leftSeekProgress = 0;
        if (isOver_60_s) {
            rightSeekProgress = MAX_CUT_DURATION
        } else {
            rightSeekProgress = endPosition
        }
        startProgress.value = leftSeekProgress
        endProgress.value = rightSeekProgress
        videoStartEvent.call()
    }

    fun getVideoDuration(): Long{
        return (endProgress.value ?: 0) - (startProgress.value ?: 0)
    }

    fun end(){
        mExtractFrameWorkThread?.stopExtract()
        mExtractVideoInfoUtil?.release()
    }

    fun onScrollStateChanged(newState: Int) {
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            isSeeking = false
        } else {
            isSeeking = true
            videoPauseEvent.call()
        }
    }

    private var preScrollX = 0;
    fun onScrolled(scrollX: Int, dx: Int, dy: Int) {
        if(dx == preScrollX) return //此时没有滚动
        videoPauseEvent.call()
        isSeeking = true
        scrollProgress = (averageMsPx * (UIUtil.dip2px(context, 35) + scrollX)).toLong()
        startProgress.value = leftSeekProgress + scrollProgress
        endProgress.value = rightSeekProgress + scrollProgress

        videoSeekEvent.value = startProgress.value?.toInt()
        preScrollX = dx
    }

    fun onSeekComplete(){
        if (!isSeeking) {
            videoStartEvent.call()
        }
    }

    fun onRangeSeekBarValuesChanged(offset: Float, totalLen: Float,  isLeft: Boolean, action: Int) {
        var seekProgess = (offset / totalLen * MAX_CUT_DURATION).toLong();
        if(isLeft){
            leftSeekProgress = seekProgess;
            startProgress.value = seekProgess + scrollProgress
        }else{
            rightSeekProgress = seekProgess;
            endProgress.value = seekProgess + scrollProgress
        }

        when (action) {
            MotionEvent.ACTION_DOWN -> {
                isSeeking = false
                videoPauseEvent.call()
            }
            MotionEvent.ACTION_MOVE -> {
                isSeeking = true
                videoSeekEvent.value = if (isLeft) startProgress.value?.toInt() else endProgress.value?.toInt()


            }
            MotionEvent.ACTION_UP -> {
                isSeeking = false
                videoSeekEvent.value = startProgress.value?.toInt()
            }
        }
    }
}