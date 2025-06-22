package com.example.hangman;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class PauseMenuActivity extends AppCompatActivity {

    private SoundManager soundManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pause_menu);

        soundManager = SoundManager.getInstance(this);

        Button btnContinue = findViewById(R.id.btnContinue);
        Button btnSettings = findViewById(R.id.btnSettings);
        Button btnMainMenu = findViewById(R.id.btnMainMenu);

        btnContinue.setOnClickListener(v -> {
            soundManager.playButtonClick();
            finish(); // Возвращаемся к игре
        });

        btnSettings.setOnClickListener(v -> {
            soundManager.playButtonClick();
            Intent intent = new Intent(PauseMenuActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        btnMainMenu.setOnClickListener(v -> {
            soundManager.playButtonClick();
            Intent intent = new Intent(PauseMenuActivity.this, MainActivity.class);
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

    @Override
    public void onBackPressed() {
        // При нажатии кнопки "Назад" возвращаемся к игре
        super.onBackPressed();
    }
}
