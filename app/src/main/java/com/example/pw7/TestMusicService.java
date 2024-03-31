package com.example.pw7;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

public class TestMusicService extends Service {
    private MediaPlayer mediaPlayer;
    private final IBinder binder = new LocalBinder();
    private boolean isPlaying = false;

    public class LocalBinder extends Binder {
        TestMusicService getService() {
            return TestMusicService.this;
        }
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = MediaPlayer.create(this, R.raw.test_audio);
    }
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
    public void playMusic() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            isPlaying = true;
        }
    }
    public void pauseMusic() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            isPlaying = false;
        }
    }
    public boolean isPlaying() {
        return isPlaying;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
    }
}
