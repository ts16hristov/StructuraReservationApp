package nbu.f104260.structurestudioreservationapp.Service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.io.IOException;

import retrofit2.http.Url;

public class BackgroundMusicService extends Service {
    private static MediaPlayer mediaPlayer;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String musicUrl = intent.getStringExtra("music_url");
        if (musicUrl != null) {
            playMusic(musicUrl);
        }
        return START_NOT_STICKY;
    }

    private void playMusic(String url) {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync(); // For network streams
            mediaPlayer.setOnPreparedListener(MediaPlayer::start);
            mediaPlayer.setLooping(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // Methods to control music from Settings
    public static void pauseMusic() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    public static void resumeMusic() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }
}
