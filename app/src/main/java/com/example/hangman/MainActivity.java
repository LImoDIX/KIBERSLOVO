package com.example.hangman;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private SoundManager soundManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Загружаем язык перед созданием активности
        loadLanguageSettings();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        soundManager = SoundManager.getInstance(this);
        soundManager.loadSettings(this);
        soundManager.startBackgroundMusic();

        Button btnStartGame = findViewById(R.id.btnStartGame);
        ImageButton btnSettings = findViewById(R.id.btnSettings);

        btnStartGame.setOnClickListener(v -> {
            soundManager.playButtonClick();
            Intent intent = new Intent(MainActivity.this, EnterWordActivity.class);
            startActivity(intent);
        });

        btnSettings.setOnClickListener(v -> {
            soundManager.playButtonClick();
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });
    }

    private void loadLanguageSettings() {
        SharedPreferences preferences = getSharedPreferences("HangmanSettings", MODE_PRIVATE);
        boolean isEnglish = preferences.getBoolean("is_english", false);

        String languageCode = isEnglish ? "en" : "ru";
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
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
    protected void onDestroy() {
        super.onDestroy();
        soundManager.stopBackgroundMusic();
    }
}
