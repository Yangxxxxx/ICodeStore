package com.gxz.example.videoedit;

import android.animation.ValueAnimator;
import android.arch.lifecycle.Observer;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.lang.ref.WeakReference;

/**
 * ================================================
 * 作    者：顾修忠-guxiuzhong@youku.com/gfj19900401@163.com
 * 版    本：
 * 创建日期：2017/4/8-下午3:48
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class VideoEditActivity extends AppCompatActivity {
    private static final String TAG = VideoEditActivity.class.getSimpleName();
    private static final long MIN_CUT_DURATION = 5 * 1000L;// 最小剪辑时间3s
    private static final long MAX_CUT_DURATION = 15 * 1000L;//视频最多剪切多长时间
    private static final int MAX_COUNT_RANGE = 10;//seekBar的区域内一共有多少张图片
    private LinearLayout seekBarLayout;
    private ExtractVideoInfoUtil mExtractVideoInfoUtil;
    private int mMaxWidth;

    private long duration;
    private RangeSeekBar seekBar;
    private VideoView mVideoView;
    private RecyclerView mRecyclerView;
    private ImageView positionIcon;
    private VideoEditAdapter videoEditAdapter;
    private float averageMsPx;//每毫秒所占的px
    private float averagePxMs;//每px所占用的ms毫秒
    private String OutPutFileDirPath;
    private ExtractFrameWorkThread mExtractFrameWorkThread;
    private String path;
    private long leftProgress, rightProgress;
    private long scrollPos = 0;
    private int mScaledTouchSlop;
    private int lastScrollX;
    private boolean isSeeking;

    private TextView textViewInfo;

    private Handler UIHandler = new Handler(Looper.getMainLooper());
    private VideoCutViewModel videoCutViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_edit);
        initData();
        initView();

        initPlay();
        initEditVideo();
    }

    private void initData() {
        path = Environment.getExternalStorageDirectory() + "/2.mp4";
        //for video check
        if (!new File(path).exists()) {
            Toast.makeText(this, "视频文件不存在", Toast.LENGTH_LONG).show();
            finish();
        }
        mExtractVideoInfoUtil = new ExtractVideoInfoUtil(path);
        duration = Long.valueOf(mExtractVideoInfoUtil.getVideoLength());


        mMaxWidth = UIUtil.getScreenWidth(this) - UIUtil.dip2px(this, 70);
        mScaledTouchSlop = ViewConfiguration.get(this).getScaledTouchSlop();


        videoCutViewModel = new VideoCutViewModel(this, path, mMaxWidth, mUIHandler);

        videoCutViewModel.getVideoPauseEvent().observe(this, new Observer<Void>() {
            @Override
            public void onChanged(@Nullable Void aVoid) {
                videoPause();
            }
        });

        videoCutViewModel.getVideoStartEvent().observe(this, new Observer<Void>() {
            @Override
            public void onChanged(@Nullable Void aVoid) {
                videoStart();
            }
        });

        videoCutViewModel.getVideoSeekEvent().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                mVideoView.seekTo(integer);
            }
        });

        videoCutViewModel.getStartProgress().observe(this, new Observer<Long>() {
            @Override
            public void onChanged(@Nullable Long aLong) {
                leftProgress = aLong;
                showPosInfo(leftProgress, rightProgress);
            }
        });

        videoCutViewModel.getEndProgress().observe(this, new Observer<Long>() {
            @Override
            public void onChanged(@Nullable Long aLong) {
                rightProgress = aLong;
                showPosInfo(leftProgress, rightProgress);
            }
        });
    }

    private void initView() {
        textViewInfo = (TextView) findViewById(R.id.tv_debug);
        seekBarLayout = (LinearLayout) findViewById(R.id.id_seekBarLayout);
        mVideoView = (VideoView) findViewById(R.id.uVideoView);
        positionIcon = (ImageView) findViewById(R.id.positionIcon);
        mRecyclerView = (RecyclerView) findViewById(R.id.id_rv_id);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        videoEditAdapter = new VideoEditAdapter(this,
                (UIUtil.getScreenWidth(this) - UIUtil.dip2px(this, 70)) / MAX_COUNT_RANGE);
        mRecyclerView.setAdapter(videoEditAdapter);
        mRecyclerView.addOnScrollListener(mOnScrollListener);
    }


    private void initEditVideo() {
        //for video edit
        long startPosition = 0;
        long endPosition = duration;
        int thumbnailsCount;
        int rangeWidth;
        boolean isOver_60_s;
        if (endPosition <= MAX_CUT_DURATION) {
            isOver_60_s = false;
            thumbnailsCount = MAX_COUNT_RANGE;
            rangeWidth = mMaxWidth;
        } else {
            isOver_60_s = true;
            thumbnailsCount = (int)Math.ceil(endPosition * 1.0f / (MAX_CUT_DURATION * 1.0f) * MAX_COUNT_RANGE);
            rangeWidth = mMaxWidth / MAX_COUNT_RANGE * thumbnailsCount;
        }

        mRecyclerView.addItemDecoration(new EditSpacingItemDecoration(UIUtil.dip2px(this, 35), thumbnailsCount));

        //init seekBar
        seekBar = new RangeSeekBar(this);
        seekBar.setMinDistanceRatio(1.0 * MIN_CUT_DURATION / MAX_CUT_DURATION);
        seekBar.setOnRangeSeekBarChangeListener(mOnRangeSeekBarChangeListener);
        seekBarLayout.addView(seekBar);

//        Log.d(TAG, "-------thumbnailsCount--->>>>" + thumbnailsCount);
//        averageMsPx = duration * 1f / rangeWidth;
////        averageMsPx = Math.min(duration, MAX_CUT_DURATION) * 1f / mMaxWidth;
//        Log.d(TAG, "-------rangeWidth--->>>>" + rangeWidth);
//        Log.d(TAG, "-------localMedia.getDuration()--->>>>" + duration);
//        Log.d(TAG, "-------averageMsPx--->>>>" + averageMsPx);
//        OutPutFileDirPath = PictureUtils.getSaveEditThumbnailDir(this);
//        int extractW = (UIUtil.getScreenWidth(this) - UIUtil.dip2px(this, 70)) / MAX_COUNT_RANGE;
//        int extractH = UIUtil.dip2px(this, 55);
//        mExtractFrameWorkThread = new ExtractFrameWorkThread(extractW, extractH, mUIHandler, path, OutPutFileDirPath, startPosition, endPosition, thumbnailsCount);
//        mExtractFrameWorkThread.start();

        //init pos icon start
//        leftProgress = 0;
//        if (isOver_60_s) {
//            rightProgress = MAX_CUT_DURATION;
//        } else {
//            rightProgress = endPosition;
//        }
//        averagePxMs = (mMaxWidth * 1.0f / (rightProgress - leftProgress));
//        Log.d(TAG, "------averagePxMs----:>>>>>" + averagePxMs);
//
//        showPosInfo(leftProgress, rightProgress);

        videoCutViewModel.start();
    }


    private void initPlay() {
        mVideoView.setVideoPath(path);
        //设置videoview的OnPrepared监听
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                //设置MediaPlayer的OnSeekComplete监听
                mp.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                    @Override
                    public void onSeekComplete(MediaPlayer mp) {
//                        Log.d(TAG, "------ok----real---start-----");
//                        Log.d(TAG, "------isSeeking-----"+isSeeking);
//                        if (!isSeeking) {
//                            videoStart();
//                        }
                        videoCutViewModel.onSeekComplete();
                    }
                });
            }
        });
        //first
//        videoStart();
    }

    private boolean isOverScaledTouchSlop;

    private final RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            videoCutViewModel.onScrollStateChanged(newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int scrollX = getScrollXDistance();
            videoCutViewModel.onScrolled(scrollX, dx, dy);
        }
    };

    /**
     * 水平滑动了多少px
     *
     * @return int px
     */
    private int getScrollXDistance() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        int position = layoutManager.findFirstVisibleItemPosition();
        View firstVisibleChildView = layoutManager.findViewByPosition(position);
        int itemWidth = firstVisibleChildView.getWidth();
        return (position) * itemWidth - firstVisibleChildView.getLeft();
    }

    private ValueAnimator animator;

    private void anim() {
        Log.e(TAG, "anim start pos: " + mVideoView.getCurrentPosition());
        if (positionIcon.getVisibility() == View.GONE) {
            positionIcon.setVisibility(View.VISIBLE);
        }
        final FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) positionIcon.getLayoutParams();
        int start = (int) (UIUtil.dip2px(this, 35) + (leftProgress/*mVideoView.getCurrentPosition()*/ - scrollPos) * averagePxMs);
        int end = (int) (UIUtil.dip2px(this, 35) + (rightProgress - scrollPos) * averagePxMs);

        start = (int) (UIUtil.dip2px(this, 35) + seekBar.getLeftOffset());
        end = (int) (UIUtil.dip2px(this, 35) + seekBar.getRightOffset());

        animator = ValueAnimator
                .ofInt(start, end)
                .setDuration(videoCutViewModel.getVideoDuration());
        animator.setInterpolator(new LinearInterpolator());
        final int finalEnd = end;
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                params.leftMargin = (int) animation.getAnimatedValue();
                positionIcon.setLayoutParams(params);

                if(params.leftMargin == finalEnd){
                    Log.e(TAG, "anim end pos: " + mVideoView.getCurrentPosition());
                }
            }
        });
        animator.start();
    }

    private final MainHandler mUIHandler = new MainHandler(this);

    private static class MainHandler extends Handler {
        private final WeakReference<VideoEditActivity> mActivity;

        MainHandler(VideoEditActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            VideoEditActivity activity = mActivity.get();
            if (activity != null) {
                if (msg.what == ExtractFrameWorkThread.MSG_SAVE_SUCCESS) {
                    if (activity.videoEditAdapter != null) {
                        VideoEditInfo info = (VideoEditInfo) msg.obj;
                        activity.videoEditAdapter.addItemVideoInfo(info);
                    }
                }
            }
        }
    }

    private final RangeSeekBar.OnRangeSeekBarChangeListener mOnRangeSeekBarChangeListener = new RangeSeekBar.OnRangeSeekBarChangeListener() {
        @Override
        public void onRangeSeekBarValuesChanged(int action, boolean isLeftHandleMoving) {
            videoCutViewModel.onRangeSeekBarValuesChanged(isLeftHandleMoving ? seekBar.getLeftOffset() : seekBar.getRightOffset(),
                    seekBar.getWidth(), isLeftHandleMoving, action);
        }
    };


    private void videoStart() {
        UIHandler.post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "----videoStart----->>>>>>>");
                mVideoView.start();
                positionIcon.clearAnimation();
                if (animator != null && animator.isRunning()) {
                    animator.cancel();
                }
                anim();
                handler.removeCallbacks(ReplayTask);
                handler.postDelayed(ReplayTask, videoCutViewModel.getVideoDuration());
            }
        });
    }

    private boolean replayVideoIfNeed() {
        boolean needReplay = mVideoView.getCurrentPosition() >= rightProgress;
        if (needReplay) {
            Log.e(TAG, "check replay true pos: " + mVideoView.getCurrentPosition());
            mVideoView.pause(); //有必要，不然再次播放时，seek的指针位置有变化
            mVideoView.seekTo((int) leftProgress);
            positionIcon.clearAnimation();
            if (animator != null && animator.isRunning()) {
                animator.cancel();
            }
        }else {
            Log.e(TAG, "check replay false pos: " + mVideoView.getCurrentPosition());
        }

        return needReplay;
    }

    private void videoPause() {
        if(mVideoView == null || !mVideoView.isPlaying()) return;
        isSeeking = false;
        if (mVideoView != null && mVideoView.isPlaying()) {
            mVideoView.pause();
            handler.removeCallbacks(ReplayTask);
        }
        Log.d(TAG, "----videoPause----->>>>>>>");
        if (positionIcon.getVisibility() == View.VISIBLE) {
            positionIcon.setVisibility(View.GONE);
        }
        positionIcon.clearAnimation();
        if (animator != null && animator.isRunning()) {
            animator.cancel();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mVideoView != null) {
            mVideoView.seekTo((int) leftProgress);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mVideoView != null && mVideoView.isPlaying()) {
            videoPause();
        }
    }

    private Handler handler = new Handler();
    private Runnable ReplayTask = new Runnable() {

        @Override
        public void run() {
            boolean needReplay = replayVideoIfNeed();
            if(!needReplay) {
                long nextCheckTime = rightProgress - mVideoView.getCurrentPosition(); //下次检测是否播放完成的时间点
                handler.postDelayed(ReplayTask, nextCheckTime);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (animator != null) {
            animator.cancel();
        }
        if (mVideoView != null) {
            mVideoView.stopPlayback();
        }
        if (mExtractVideoInfoUtil != null) {
            mExtractVideoInfoUtil.release();
        }
        mRecyclerView.removeOnScrollListener(mOnScrollListener);
        if (mExtractFrameWorkThread != null) {
            mExtractFrameWorkThread.stopExtract();
        }
        mUIHandler.removeCallbacksAndMessages(null);
        handler.removeCallbacksAndMessages(null);
        if (!TextUtils.isEmpty(OutPutFileDirPath)) {
            PictureUtils.deleteFile(new File(OutPutFileDirPath));
        }
        videoCutViewModel.end();
    }

    private void showPosInfo(long leftPos, long rightProgress){
        textViewInfo.setText(leftPos + " : " + rightProgress);
    }

    private long getSeekBarLeftTime(){
        return (long) (1.0 * seekBar.getLeftOffset() / seekBar.getWidth() * MAX_CUT_DURATION);
    }

    private long getSeekBarRightTime(){
        return (long) (1.0 * seekBar.getRightOffset() / seekBar.getWidth() * MAX_CUT_DURATION);
    }
}
