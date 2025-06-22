package com.example.hangman;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {

    private SoundManager soundManager;
    private SharedPreferences preferences;
    private Switch switchMusic, switchSounds, switchVibration;
    private Button btnLanguage;
    private boolean isEnglish = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        soundManager = SoundManager.getInstance(this);
        preferences = getSharedPreferences("HangmanSettings", MODE_PRIVATE);

        initializeViews();
        loadSettings();
        setupListeners();
    }

    private void initializeViews() {
        ImageButton btnBack = findViewById(R.id.btnBack);
        switchMusic = findViewById(R.id.switchMusic);
        switchSounds = findViewById(R.id.switchSounds);
        switchVibration = findViewById(R.id.switchVibration);
        btnLanguage = findViewById(R.id.btnLanguage);

        btnBack.setOnClickListener(v -> {
            soundManager.playButtonClick();
            finish();
        });
    }

    private void loadSettings() {
        // Загружаем настройки
        boolean musicEnabled = preferences.getBoolean("music_enabled", true);
        boolean soundsEnabled = preferences.getBoolean("sounds_enabled", true);
        boolean vibrationEnabled = preferences.getBoolean("vibration_enabled", true);
        isEnglish = preferences.getBoolean("is_english", false);

        switchMusic.setChecked(musicEnabled);
        switchSounds.setChecked(soundsEnabled);
        switchVibration.setChecked(vibrationEnabled);

        updateLanguageButton();
    }

    private void setupListeners() {
        switchMusic.setOnCheckedChangeListener((buttonView, isChecked) -> {
            soundManager.playButtonClick();
            soundManager.setMusicEnabled(isChecked);
            saveSettings();
        });

        switchSounds.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                soundManager.playButtonClick();
            }
            soundManager.setSoundEnabled(isChecked);
            saveSettings();
        });

        switchVibration.setOnCheckedChangeListener((buttonView, isChecked) -> {
            soundManager.playButtonClick();
            soundManager.setVibrationEnabled(isChecked);
            saveSettings();
        });

        btnLanguage.setOnClickListener(v -> {
            soundManager.playButtonClick();
            isEnglish = !isEnglish;
            changeLanguage();
            updateLanguageButton();
            saveSettings();
        });
    }

    private void changeLanguage() {
        String languageCode = isEnglish ? "en" : "ru";
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        // Перезапускаем активность для применения изменений
        recreate();
    }

    private void updateLanguageButton() {
        btnLanguage.setText(isEnglish ? "English" : "Русский");
    }

    private void saveSettings() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("music_enabled", switchMusic.isChecked());
        editor.putBoolean("sounds_enabled", switchSounds.isChecked());
        editor.putBoolean("vibration_enabled", switchVibration.isChecked());
        editor.putBoolean("is_english", isEnglish);
        editor.apply();
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
