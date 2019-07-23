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
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.lang.ref.WeakReference;


public class VideoEditActivity extends AppCompatActivity {
    private static final String TAG = VideoEditActivity.class.getSimpleName();
    private LinearLayout seekBarLayout;
    private RangeSeekBar seekBar;
    private VideoView mVideoView;
    private RecyclerView mRecyclerView;
    private ImageView positionIcon;
    private TextView textViewInfo;

    private VideoEditAdapter videoEditAdapter;
    private String path;
    private long leftProgress;
    private long rightProgress;

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
        int mMaxWidth = UIUtil.getScreenWidth(this) - UIUtil.dip2px(this, 70);
        videoCutViewModel = new VideoCutViewModel(this, path, mMaxWidth);
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

        videoCutViewModel.getThumbInfo().observe(this, new Observer<VideoEditInfo>() {
            @Override
            public void onChanged(@Nullable VideoEditInfo videoEditInfo) {
                videoEditAdapter.addItemVideoInfo(videoEditInfo);
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
                (UIUtil.getScreenWidth(this) - UIUtil.dip2px(this, 70)) / VideoCutViewModel.MAX_COUNT_RANGE);
        mRecyclerView.setAdapter(videoEditAdapter);
        mRecyclerView.addOnScrollListener(mOnScrollListener);
    }


    private void initEditVideo() {
        mRecyclerView.addItemDecoration(new EditSpacingItemDecoration(UIUtil.dip2px(this, 35), videoCutViewModel.getThumbPicNum()));

        seekBar = new RangeSeekBar(this);
        seekBar.setMinDistanceRatio(1.0 * VideoCutViewModel.MIN_CUT_DURATION / VideoCutViewModel.MAX_CUT_DURATION);
        seekBar.setOnRangeSeekBarChangeListener(mOnRangeSeekBarChangeListener);
        seekBarLayout.addView(seekBar);

        videoCutViewModel.start();
    }


    private void initPlay() {
        mVideoView.setVideoPath(path);
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                    @Override
                    public void onSeekComplete(MediaPlayer mp) {
                        videoCutViewModel.onSeekComplete();
                    }
                });
            }
        });
    }


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

        int start = (int) (UIUtil.dip2px(this, 35) + seekBar.getLeftOffset());
        int end = (int) (UIUtil.dip2px(this, 35) + seekBar.getRightOffset());

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
        mRecyclerView.removeOnScrollListener(mOnScrollListener);
        handler.removeCallbacksAndMessages(null);
        videoCutViewModel.end();
    }

    private void showPosInfo(long leftPos, long rightProgress){
        textViewInfo.setText(leftPos + " : " + rightProgress);
    }
}
