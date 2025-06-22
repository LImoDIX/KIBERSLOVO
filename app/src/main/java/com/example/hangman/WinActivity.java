package com.example.hangman;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class WinActivity extends AppCompatActivity {

    private SoundManager soundManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win);

        soundManager = SoundManager.getInstance(this);

        String word = getIntent().getStringExtra("WORD");

        TextView tvWinWord = findViewById(R.id.tvWinWord);
        tvWinWord.setText(word);

        Button btnPlayAgain = findViewById(R.id.btnPlayAgain);
        Button btnMainMenu = findViewById(R.id.btnMainMenu);

        btnPlayAgain.setOnClickListener(v -> {
            soundManager.playButtonClick();
            Intent intent = new Intent(WinActivity.this, EnterWordActivity.class);
            startActivity(intent);
            finish();
        });

        btnMainMenu.setOnClickListener(v -> {
            soundManager.playButtonClick();
            Intent intent = new Intent(WinActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        soundManager.resumeBackgroundMusic();
    }

    @Override
    protected void onPause() {
        super.onPause();
        soundManager.pauseBackgroundMusic();
    }
}
