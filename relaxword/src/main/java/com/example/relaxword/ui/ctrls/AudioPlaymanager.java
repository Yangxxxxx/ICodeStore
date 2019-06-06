package com.example.relaxword.ui.ctrls;

import android.media.MediaPlayer;
import android.text.TextUtils;

public class AudioPlaymanager {
    private MediaPlayer mediaPlayer;
    private String preAudioPath;

    private static AudioPlaymanager audioPlaymanager = new AudioPlaymanager();

    private AudioPlaymanager(){
        init();
    }

    public static AudioPlaymanager getInstance(){
        return audioPlaymanager;
    }

    private void init(){
        if(mediaPlayer == null){
            mediaPlayer = new MediaPlayer();
        }
    }

    public void stop(){
        mediaPlayer.stop();
        mediaPlayer.release();
    }

    public void playSound(String audioPath, final PlayListener playListener){
        if(TextUtils.isEmpty(audioPath)) return;
        if(audioPath.equals(preAudioPath)){
            mediaPlayer.start();
            return;
        }
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                if(playListener != null) {
                    playListener.onPlayEnd();
                }
            }
        });
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                if(playListener != null) {
                    playListener.onPlayEnd();
                }
                return false;
            }
        });
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(audioPath);
            mediaPlayer.prepareAsync();// 进行缓冲\
            preAudioPath = audioPath;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface PlayListener{
        void onPlayEnd();
    }
}
