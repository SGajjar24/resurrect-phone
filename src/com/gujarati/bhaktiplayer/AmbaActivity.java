package com.gujarati.bhaktiplayer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class AmbaActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.EXTRA_AUTO_PLAY, BhaktiService.SONG_AMBA);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
}
