package com.gxz.example.videoedit

import android.animation.ValueAnimator
import androidx.lifecycle.Observer
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView

import java.io.File


class VideoEditActivity : AppCompatActivity() {
    private var seekBarLayout: LinearLayout? = null
    private lateinit var seekBar: VideoSeekBar
    private lateinit var mVideoView: VideoView
    private var mRecyclerView: androidx.recyclerview.widget.RecyclerView? = null
    private var positionIcon: ImageView? = null
    private var textViewInfo: TextView? = null

    private var videoEditAdapter: VideoEditAdapter? = null
    private var path: String? = null
    private var leftProgress: Long = 0
    private var rightProgress: Long = 0

    private val UIHandler = Handler(Looper.getMainLooper())
    private var videoCutViewModel: VideoCutViewModel? = null

    private var animator: ValueAnimator? = null
    private val handler = Handler()
    private val replayTask = object : Runnable {

        override fun run() {
            val needReplay = replayVideoIfNeed()
            if (!needReplay) {
                val nextCheckTime = rightProgress - mVideoView.currentPosition //下次检测是否播放完成的时间点
                handler.postDelayed(this, nextCheckTime)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_edit)
        initData()
        initView()

        initPlay()
        initEditVideo()
    }

    private fun initData() {
        path = Environment.getExternalStorageDirectory().toString() + "/2.mp4"
        //for video check
        if(!File(path).exists()){
            Toast.makeText(this, "视频文件不存在", Toast.LENGTH_LONG).show()
            finish()
        }
        val mMaxWidth = UIUtil.getScreenWidth(this) - UIUtil.dip2px(this, 70)
        videoCutViewModel = VideoCutViewModel(this, path, mMaxWidth)
        videoCutViewModel?.videoPauseEvent?.observe(this, Observer { videoPause() })

        videoCutViewModel?.videoStartEvent?.observe(this, Observer { videoStart() })

        videoCutViewModel?.videoSeekEvent?.observe(this, Observer { integer ->
            if (integer == 0) {
                videoStart()
            } else {
                mVideoView.seekTo(integer?:0)
            }
        })

        videoCutViewModel?.startProgress?.observe(this, Observer { aLong ->
            leftProgress = aLong ?: 0
            showPosInfo(leftProgress, rightProgress)
        })

        videoCutViewModel?.endProgress?.observe(this, Observer { aLong ->
            rightProgress = aLong ?: 0
            showPosInfo(leftProgress, rightProgress)
        })

        videoCutViewModel?.thumbInfo?.observe(this, Observer { videoEditInfo -> videoEditAdapter?.addItemVideoInfo(videoEditInfo) })

        videoCutViewModel?.let {
            lifecycle.addObserver(it)
        }
    }


    private val mOnScrollListener = object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: androidx.recyclerview.widget.RecyclerView?, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            videoCutViewModel?.onScrollStateChanged(newState)
        }

        override fun onScrolled(recyclerView: androidx.recyclerview.widget.RecyclerView?, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val scrollX = scrollXDistance + UIUtil.dip2px(this@VideoEditActivity, 35)
            videoCutViewModel?.onScrolled(scrollX, dx, dy)
        }
    }

    private val mOnRangeSeekBarChangeListener = VideoSeekBar.OnRangeSeekBarChangeListener { action, isLeftHandleMoving ->
        var offset = if (isLeftHandleMoving) seekBar.leftOffset else seekBar.rightOffset
        videoCutViewModel?.onRangeSeekBarValuesChanged(offset, seekBar.width.toFloat(), isLeftHandleMoving, action)
    }


    private fun initView() {
        textViewInfo = findViewById<View>(R.id.tv_debug) as TextView
        seekBarLayout = findViewById<View>(R.id.id_seekBarLayout) as LinearLayout
        mVideoView = findViewById<View>(R.id.uVideoView) as VideoView
        positionIcon = findViewById<View>(R.id.positionIcon) as ImageView
        mRecyclerView = findViewById<View>(R.id.id_rv_id) as androidx.recyclerview.widget.RecyclerView
        mRecyclerView?.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this, androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL, false)
        videoEditAdapter = VideoEditAdapter(this,
                (UIUtil.getScreenWidth(this) - UIUtil.dip2px(this, 70)) / VideoCutViewModel.MAX_COUNT_RANGE)
        mRecyclerView?.adapter = videoEditAdapter
        mRecyclerView?.addOnScrollListener(mOnScrollListener)
    }


    private fun initEditVideo() {
        mRecyclerView?.addItemDecoration(EditSpacingItemDecoration(UIUtil.dip2px(this, 35), videoCutViewModel?.getThumbPicNum() ?: 0))

        seekBar = VideoSeekBar(this)
        seekBar.setMinDistanceRatio(1.0 * VideoCutViewModel.MIN_CUT_DURATION / VideoCutViewModel.MAX_CUT_DURATION)
        seekBar.setOnRangeSeekBarChangeListener(mOnRangeSeekBarChangeListener)
        seekBarLayout?.addView(seekBar)
    }

    private fun initPlay() {
        mVideoView.setVideoPath(path)
        mVideoView.setOnPreparedListener { mp -> mp.setOnSeekCompleteListener { videoCutViewModel?.onSeekComplete() } }
    }

    private fun startAnim() {
        Log.e(TAG, "startAnim start pos: " + mVideoView.currentPosition)
        if (positionIcon?.visibility == View.GONE) {
            positionIcon?.visibility = View.VISIBLE
        }
        val params = positionIcon?.layoutParams as FrameLayout.LayoutParams

        val start = (UIUtil.dip2px(this, 35) + seekBar.leftOffset).toInt()
        val end = (UIUtil.dip2px(this, 35) + seekBar.rightOffset).toInt()

        videoCutViewModel?.let {
            animator = ValueAnimator
                    .ofInt(start, end)
                    .setDuration(it.getVideoDuration())
            animator?.interpolator = LinearInterpolator()
            animator?.addUpdateListener { animation ->
                params.leftMargin = animation.animatedValue as Int
                positionIcon?.layoutParams = params

                if (params.leftMargin == end) {
                    Log.e(TAG, "startAnim end pos: " + mVideoView.currentPosition)
                }
            }
            animator?.start()
        }
    }

    private fun stopAnim() {
        if (positionIcon?.visibility == View.VISIBLE) {
            positionIcon?.visibility = View.GONE
        }
        positionIcon?.clearAnimation()
        if (animator?.isRunning == true) {
            animator?.cancel()
        }
    }

    private fun videoStart() {
        videoCutViewModel?.let {
            UIHandler.post {
                Log.d(TAG, "----videoStart----->>>>>>>")
                mVideoView.start()
                stopAnim()
                startAnim()
                handler.removeCallbacks(replayTask)
                handler.postDelayed(replayTask, it.getVideoDuration())
            }
        }
    }

    private fun replayVideoIfNeed(): Boolean {
        val needReplay = mVideoView.currentPosition >= rightProgress
        if (needReplay) {
            Log.e(TAG, "check replay true pos: " + mVideoView.currentPosition)
            mVideoView.pause() //有必要，不然再次播放时，seek的指针位置有变化
            mVideoView.seekTo(leftProgress.toInt())
            stopAnim()
        } else {
            Log.e(TAG, "check replay false pos: " + mVideoView.currentPosition)
        }

        return needReplay
    }

    private fun videoPause() {
        if (mVideoView.isPlaying) {
            mVideoView.pause()
            handler.removeCallbacks(replayTask)
            Log.d(TAG, "----videoPause----->>>>>>>")
            stopAnim()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        animator?.cancel()
        mVideoView.stopPlayback()
        mRecyclerView?.removeOnScrollListener(mOnScrollListener)
        handler.removeCallbacksAndMessages(null)
        videoCutViewModel?.let {
            it.end()
            lifecycle.removeObserver(it)
        }
    }

    private fun showPosInfo(leftPos: Long, rightProgress: Long) {
        textViewInfo?.text = "$leftPos : $rightProgress"
    }


    private val scrollXDistance: Int
        get() {
            val layoutManager = mRecyclerView?.layoutManager as androidx.recyclerview.widget.LinearLayoutManager
            val position = layoutManager.findFirstVisibleItemPosition()
            val firstVisibleChildView = layoutManager.findViewByPosition(position)
            val itemWidth = firstVisibleChildView.width
            return position * itemWidth - firstVisibleChildView.left
        }

    companion object {
        private val TAG = VideoEditActivity::class.java.simpleName
    }
}
