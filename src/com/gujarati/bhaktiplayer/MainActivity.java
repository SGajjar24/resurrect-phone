package com.gujarati.bhaktiplayer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener, Runnable {
    public static final String EXTRA_AUTO_PLAY = "extra_auto_play";
    private static MainActivity currentInstance;

    private TextView txtStatus;
    private ImageView imgStatusIcon;
    private Button btnPause;
    private Button btnStop;
    private Button btnVolDown;
    private Button btnVolUp;
    private Button btnSleepTimer;

    // Devotional Cards
    private View cardHanuman;
    private View cardGanesh;
    private View cardShiv;
    private View cardKrishna;
    private View cardAmba;
    private View cardDada;
    private View cardJagdish;
    private View cardNewPipe;
    private View cardFmRadio;

    // Card Action Icons
    private ImageView icActionHanuman;
    private ImageView icActionGanesh;
    private ImageView icActionShiv;
    private ImageView icActionKrishna;
    private ImageView icActionAmba;
    private ImageView icActionDada;
    private ImageView icActionJagdish;

    public static void notifyStateChanged() {
        if (currentInstance != null) {
            currentInstance.runOnUiThread(currentInstance);
        }
    }

    @Override
    public void run() {
        updateUi();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentInstance = this;
        setContentView(R.layout.activity_main);

        txtStatus = (TextView) findViewById(R.id.txtStatus);
        imgStatusIcon = (ImageView) findViewById(R.id.imgStatusIcon);
        btnPause = (Button) findViewById(R.id.btnPause);
        btnStop = (Button) findViewById(R.id.btnStop);
        btnVolDown = (Button) findViewById(R.id.btnVolDown);
        btnVolUp = (Button) findViewById(R.id.btnVolUp);
        btnSleepTimer = (Button) findViewById(R.id.btnSleepTimer);

        cardHanuman = findViewById(R.id.cardHanuman);
        cardGanesh = findViewById(R.id.cardGanesh);
        cardShiv = findViewById(R.id.cardShiv);
        cardKrishna = findViewById(R.id.cardKrishna);
        cardAmba = findViewById(R.id.cardAmba);
        cardDada = findViewById(R.id.cardDada);
        cardJagdish = findViewById(R.id.cardJagdish);
        cardNewPipe = findViewById(R.id.cardNewPipe);
        cardFmRadio = findViewById(R.id.cardFmRadio);

        icActionHanuman = (ImageView) findViewById(R.id.icActionHanuman);
        icActionGanesh = (ImageView) findViewById(R.id.icActionGanesh);
        icActionShiv = (ImageView) findViewById(R.id.icActionShiv);
        icActionKrishna = (ImageView) findViewById(R.id.icActionKrishna);
        icActionAmba = (ImageView) findViewById(R.id.icActionAmba);
        icActionDada = (ImageView) findViewById(R.id.icActionDada);
        icActionJagdish = (ImageView) findViewById(R.id.icActionJagdish);

        cardHanuman.setOnClickListener(this);
        cardGanesh.setOnClickListener(this);
        cardShiv.setOnClickListener(this);
        cardKrishna.setOnClickListener(this);
        cardAmba.setOnClickListener(this);
        cardDada.setOnClickListener(this);
        cardJagdish.setOnClickListener(this);

        cardNewPipe.setOnClickListener(this);
        cardFmRadio.setOnClickListener(this);

        btnPause.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        btnVolDown.setOnClickListener(this);
        btnVolUp.setOnClickListener(this);
        if (btnSleepTimer != null) btnSleepTimer.setOnClickListener(this);

        Intent svc = new Intent(this, BhaktiService.class);
        startService(svc);

        checkAutoPlayIntent(getIntent());
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.cardHanuman) {
            play(BhaktiService.SONG_HANUMAN_CHALISA);
        } else if (id == R.id.cardGanesh) {
            play(BhaktiService.SONG_GANESH);
        } else if (id == R.id.cardShiv) {
            play(BhaktiService.SONG_SHIV);
        } else if (id == R.id.cardKrishna) {
            play(BhaktiService.SONG_KRISHNA);
        } else if (id == R.id.cardAmba) {
            play(BhaktiService.SONG_AMBA);
        } else if (id == R.id.cardDada) {
            play(BhaktiService.SONG_DADA);
        } else if (id == R.id.cardJagdish) {
            play(BhaktiService.SONG_JAGDISH);
        } else if (id == R.id.cardNewPipe) {
            launchNewPipeBhajanSearch();
        } else if (id == R.id.cardFmRadio) {
            BhaktiService s = BhaktiService.getInstance();
            if (s != null && s.isPlaying()) {
                s.pausePlayback();
            }
            launchApp("com.motorola.fmplayer", "FM Radio");
        } else if (id == R.id.btnPause) {
            BhaktiService s = BhaktiService.getInstance();
            if (s != null) {
                s.togglePlayPause();
            } else {
                play(BhaktiService.SONG_HANUMAN_CHALISA);
            }
        } else if (id == R.id.btnStop) {
            BhaktiService s = BhaktiService.getInstance();
            if (s != null) {
                s.stopPlayback();
            }
        } else if (id == R.id.btnVolDown) {
            BhaktiService s = BhaktiService.getInstance();
            if (s != null) {
                s.adjustVolume(-1);
            }
        } else if (id == R.id.btnVolUp) {
            BhaktiService s = BhaktiService.getInstance();
            if (s != null) {
                s.adjustVolume(1);
            }
        } else if (id == R.id.btnSleepTimer) {
            BhaktiService s = BhaktiService.getInstance();
            if (s != null) {
                s.cycleSleepTimer();
                int mins = s.getSleepTimerMinutes();
                if (mins > 0) {
                    Toast.makeText(this, "⏱️ સ્લીપ ટાઈમર: " + mins + " મિનિટ પછી આપમેળે બંધ થશે", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "⏱️ સ્લીપ ટાઈમર બંધ કરેલ છે", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void launchNewPipeBhajanSearch() {
        try {
            BhaktiService s = BhaktiService.getInstance();
            if (s != null && s.isPlaying()) {
                s.pausePlayback();
            }
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setClassName("org.schabi.newpipe", "org.schabi.newpipe.RouterActivity");
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, "gujarati bhajan");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (Exception e) {
            launchApp("org.schabi.newpipe", "NewPipe");
        }
    }

    private void launchApp(String packageName, String label) {
        try {
            Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);
            if (intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                Toast.makeText(this, label + " મળ્યું નથી", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "ભૂલ: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void play(int songId) {
        BhaktiService s = BhaktiService.getInstance();
        if (s != null) {
            s.playSong(songId);
        } else {
            Intent svc = new Intent(this, BhaktiService.class);
            svc.putExtra("ACTION_PLAY_SONG", songId);
            startService(svc);
        }
    }

    private void checkAutoPlayIntent(Intent intent) {
        if (intent != null && intent.hasExtra(EXTRA_AUTO_PLAY)) {
            int songId = intent.getIntExtra(EXTRA_AUTO_PLAY, -1);
            if (songId >= 0) {
                play(songId);
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        checkAutoPlayIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        currentInstance = this;
        hideSystemUI();
        updateUi();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    /**
     * Immersive Sticky Kiosk Mode:
     * Hides the navigation bar and status bar so elderly users cannot
     * accidentally exit, misclick Back, or get trapped in system menus.
     */
    private void hideSystemUI() {
        try {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            );
        } catch (Exception ignored) {}
    }

    @Override
    public void onBackPressed() {
        // Keep user on the home screen
    }

    @Override
    protected void onDestroy() {
        if (currentInstance == this) {
            currentInstance = null;
        }
        super.onDestroy();
    }

    private void updateCardState(View card, ImageView actionIcon, boolean isThisSong, boolean isPlaying) {
        if (card == null || actionIcon == null) return;
        if (isThisSong) {
            card.setBackgroundResource(R.drawable.card_bg_playing);
            actionIcon.setImageResource(isPlaying ? R.drawable.ic_sound_wave : R.drawable.ic_ctrl_pause);
        } else {
            card.setBackgroundResource(R.drawable.card_selector);
            actionIcon.setImageResource(R.drawable.ic_ctrl_play);
        }
    }

    private void updateUi() {
        if (txtStatus == null) return;

        BhaktiService s = BhaktiService.getInstance();
        int curId = (s != null) ? s.getCurrentSongId() : BhaktiService.SONG_NONE;
        boolean playing = (s != null && s.isPlaying());

        // Update each devotional card
        updateCardState(cardHanuman, icActionHanuman, curId == BhaktiService.SONG_HANUMAN_CHALISA, playing);
        updateCardState(cardGanesh, icActionGanesh, curId == BhaktiService.SONG_GANESH, playing);
        updateCardState(cardShiv, icActionShiv, curId == BhaktiService.SONG_SHIV, playing);
        updateCardState(cardKrishna, icActionKrishna, curId == BhaktiService.SONG_KRISHNA, playing);
        updateCardState(cardAmba, icActionAmba, curId == BhaktiService.SONG_AMBA, playing);
        updateCardState(cardDada, icActionDada, curId == BhaktiService.SONG_DADA, playing);
        updateCardState(cardJagdish, icActionJagdish, curId == BhaktiService.SONG_JAGDISH, playing);

        // Update Dock Status & Controls
        if (s != null) {
            String title = s.getCurrentSongTitle();
            if (playing) {
                txtStatus.setText(getString(R.string.now_playing) + " " + title);
                btnPause.setText(getString(R.string.pause_playback));
                if (imgStatusIcon != null) imgStatusIcon.setImageResource(R.drawable.ic_sound_wave);
            } else if (curId != BhaktiService.SONG_NONE) {
                txtStatus.setText(getString(R.string.now_paused) + " " + title);
                btnPause.setText(getString(R.string.play_playback));
                if (imgStatusIcon != null) imgStatusIcon.setImageResource(R.drawable.ic_diya);
            } else {
                txtStatus.setText(getString(R.string.stopped));
                btnPause.setText(getString(R.string.play_playback));
                if (imgStatusIcon != null) imgStatusIcon.setImageResource(R.drawable.ic_diya);
            }
        } else {
            txtStatus.setText(getString(R.string.stopped));
            btnPause.setText(getString(R.string.play_playback));
            if (imgStatusIcon != null) imgStatusIcon.setImageResource(R.drawable.ic_diya);
        }

        // Update Sleep Timer Button
        if (btnSleepTimer != null) {
            if (s != null) {
                btnSleepTimer.setText(s.getSleepTimerLabel());
                if (s.getSleepTimerMinutes() > 0) {
                    btnSleepTimer.setTextColor(0xFFFF7043); // bright saffron when active
                } else {
                    btnSleepTimer.setTextColor(0xFFFFB300); // temple gold
                }
            } else {
                btnSleepTimer.setText(getString(R.string.timer_off));
                btnSleepTimer.setTextColor(0xFFFFB300);
            }
        }
    }
}
