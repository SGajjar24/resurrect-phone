package com.gujarati.bhaktiplayer;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.PowerManager;
import android.util.Log;
import java.io.File;

public class BhaktiService extends Service implements MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, Runnable {
    private static final String TAG = "BhaktiService";
    private static final int NOTIF_ID = 1001;

    public static final int SONG_NONE = -1;
    public static final int SONG_HANUMAN_CHALISA = 0;
    public static final int SONG_GANESH = 1;
    public static final int SONG_SHIV = 2;
    public static final int SONG_KRISHNA = 3;
    public static final int SONG_AMBA = 4;
    public static final int SONG_DADA = 5;
    public static final int SONG_JAGDISH = 6;

    public static final String[] SONG_NAMES = {
        "હનુમાન ચાલીસા",
        "ગણેશજી આરતી",
        "શિવજી આરતી",
        "કૃષ્ણ ભજન (અચ્યુતમ કેશવમ)",
        "અંબા મા આરતી",
        "દાદા ભગવાન આરતી",
        "વિષ્ણુ આરતી (જગદીશ હરે)"
    };

    private static final String[] FILE_NAMES = {
        "Hanuman_Chalisa.m4a",
        "Ganesh_Aarti.m4a",
        "Shiv_Aarti.m4a",
        "Krishna_Bhajan.m4a",
        "Amba_Ma_Aarti.m4a",
        "Dada_Bhagwan_Aarti.m4a",
        "Jagdish_Aarti.m4a"
    };

    public static BhaktiService instance;

    private MediaPlayer mediaPlayer;
    private int currentSongId = SONG_NONE;
    private AudioManager audioManager;
    private boolean isLoopMode = false;

    // Sleep Timer (auto-shutoff for prayer/bedtime)
    private int sleepTimerMinutes = 0;
    private Handler sleepHandler = new Handler(Looper.getMainLooper());

    @Override
    public void run() {
        Log.i(TAG, "Sleep timer expired. Gracefully stopping playback.");
        sleepTimerMinutes = 0;
        stopPlayback();
        MainActivity.notifyStateChanged();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        instance = this;
        if (intent != null && intent.hasExtra("ACTION_PLAY_SONG")) {
            int songId = intent.getIntExtra("ACTION_PLAY_SONG", -1);
            if (songId >= 0) {
                playSong(songId);
            }
        }
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static BhaktiService getInstance() {
        return instance;
    }

    public int getCurrentSongId() {
        return currentSongId;
    }

    public boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }

    public String getCurrentSongTitle() {
        if (currentSongId >= 0 && currentSongId < SONG_NAMES.length) {
            return SONG_NAMES[currentSongId];
        }
        return "કંઈ વાગતું નથી";
    }

    public void cycleSleepTimer() {
        sleepHandler.removeCallbacks(this);
        if (sleepTimerMinutes == 0) {
            sleepTimerMinutes = 15;
        } else if (sleepTimerMinutes == 15) {
            sleepTimerMinutes = 30;
        } else if (sleepTimerMinutes == 30) {
            sleepTimerMinutes = 60;
        } else {
            sleepTimerMinutes = 0;
        }

        if (sleepTimerMinutes > 0) {
            sleepHandler.postDelayed(this, sleepTimerMinutes * 60 * 1000L);
            Log.i(TAG, "Sleep timer scheduled: " + sleepTimerMinutes + " min");
        } else {
            Log.i(TAG, "Sleep timer disabled");
        }
        MainActivity.notifyStateChanged();
    }

    public int getSleepTimerMinutes() {
        return sleepTimerMinutes;
    }

    public String getSleepTimerLabel() {
        if (sleepTimerMinutes == 15) return "⏱️ ૧૫ મિ.";
        if (sleepTimerMinutes == 30) return "⏱️ ૩૦ મિ.";
        if (sleepTimerMinutes == 60) return "⏱️ ૬૦ મિ.";
        return "⏱️ બંધ";
    }

    public boolean isLooping() {
        return isLoopMode;
    }

    public void toggleLoop() {
        isLoopMode = !isLoopMode;
        if (mediaPlayer != null) {
            mediaPlayer.setLooping(isLoopMode);
        }
        MainActivity.notifyStateChanged();
    }

    public void playSong(int songId) {
        if (songId < 0 || songId >= FILE_NAMES.length) return;

        stopPlayback();
        currentSongId = songId;

        File bhajansDir = new File(Environment.getExternalStorageDirectory(), "Bhajans");
        File audioFile = new File(bhajansDir, FILE_NAMES[songId]);

        if (!audioFile.exists()) {
            Log.e(TAG, "File not found: " + audioFile.getAbsolutePath());
            MainActivity.notifyStateChanged();
            return;
        }

        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
            mediaPlayer.setLooping(isLoopMode);
            mediaPlayer.setDataSource(getApplicationContext(), Uri.fromFile(audioFile));
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.setOnErrorListener(this);
            mediaPlayer.prepare();
            mediaPlayer.start();

            startForegroundNotification(SONG_NAMES[songId]);
        } catch (Exception e) {
            Log.e(TAG, "Error playing audio", e);
            stopPlayback();
        }

        MainActivity.notifyStateChanged();
    }

    public void togglePlayPause() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            } else {
                mediaPlayer.start();
            }
            MainActivity.notifyStateChanged();
        } else if (currentSongId != SONG_NONE) {
            playSong(currentSongId);
        } else {
            playSong(SONG_HANUMAN_CHALISA);
        }
    }

    public void pausePlayback() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            MainActivity.notifyStateChanged();
        }
    }

    public void stopPlayback() {
        if (mediaPlayer != null) {
            try {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                mediaPlayer.release();
            } catch (Exception ignored) {}
            mediaPlayer = null;
        }
        currentSongId = SONG_NONE;
        stopForeground(true);
        MainActivity.notifyStateChanged();
    }

    public void adjustVolume(int direction) {
        if (audioManager != null) {
            audioManager.adjustStreamVolume(
                AudioManager.STREAM_MUSIC,
                direction > 0 ? AudioManager.ADJUST_RAISE : AudioManager.ADJUST_LOWER,
                AudioManager.FLAG_SHOW_UI
            );
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (!isLoopMode) {
            stopPlayback();
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.e(TAG, "MediaPlayer error: " + what + ", " + extra);
        stopPlayback();
        return true;
    }

    private void startForegroundNotification(String title) {
        Intent notifIntent = new Intent(this, MainActivity.class);
        notifIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pi = PendingIntent.getActivity(this, 0, notifIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(this)
            .setContentTitle("ભક્તિ સંગીત વાગી રહ્યું છે")
            .setContentText(title)
            .setSmallIcon(R.drawable.ic_bhakti)
            .setContentIntent(pi)
            .setOngoing(true);

        startForeground(NOTIF_ID, builder.build());
    }

    @Override
    public void onDestroy() {
        if (sleepHandler != null) {
            sleepHandler.removeCallbacks(this);
        }
        stopPlayback();
        instance = null;
        super.onDestroy();
    }
}
