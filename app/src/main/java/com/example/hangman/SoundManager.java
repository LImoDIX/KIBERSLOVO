package com.example.hangman;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.media.ToneGenerator;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;

public class SoundManager {
    private static SoundManager instance;
    private SoundPool soundPool;
    private MediaPlayer backgroundMusic;
    private Context context;
    private ToneGenerator toneGenerator;
    private Vibrator vibrator;

    // Sound IDs - будут -1 если звуки не загружены
    private int correctSound = -1;
    private int wrongSound = -1;
    private int winSound = -1;
    private int loseSound = -1;
    private int buttonClickSound = -1;

    private boolean soundEnabled = true;
    private boolean musicEnabled = true;
    private boolean vibrationEnabled = true;

    private SoundManager(Context context) {
        this.context = context.getApplicationContext();
        this.vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        initializeSoundPool();
        initializeToneGenerator();
        loadSounds();
    }

    public static synchronized SoundManager getInstance(Context context) {
        if (instance == null) {
            instance = new SoundManager(context);
        }
        return instance;
    }

    public void loadSettings(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("HangmanSettings", Context.MODE_PRIVATE);
        soundEnabled = preferences.getBoolean("sounds_enabled", true);
        musicEnabled = preferences.getBoolean("music_enabled", true);
        vibrationEnabled = preferences.getBoolean("vibration_enabled", true);
    }

    private void initializeSoundPool() {
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(5)
                .setAudioAttributes(audioAttributes)
                .build();
    }

    private void initializeToneGenerator() {
        try {
            toneGenerator = new ToneGenerator(AudioManager.STREAM_MUSIC, 50);
        } catch (RuntimeException e) {
            Log.e("SoundManager", "Error creating ToneGenerator: " + e.getMessage());
            toneGenerator = null;
        }
    }

    private void loadSounds() {
        try {
            // Раскомментируйте эти строки после добавления звуковых файлов:
            correctSound = soundPool.load(context, R.raw.correct_sound, 1);
            wrongSound = soundPool.load(context, R.raw.wrong_sound, 1);
            winSound = soundPool.load(context, R.raw.win_sound, 1);
            loseSound = soundPool.load(context, R.raw.lose_sound, 1);
            buttonClickSound = soundPool.load(context, R.raw.button_click, 1);

            Log.d("SoundManager", "Sound files loaded successfully");
        } catch (Exception e) {
            Log.e("SoundManager", "Error loading sounds: " + e.getMessage());
        }
    }

    public void vibrate() {
        if (vibrationEnabled && vibrator != null && vibrator.hasVibrator()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                vibrator.vibrate(200);
            }
        }
    }

    public void playCorrectSound() {
        if (soundEnabled) {
            if (correctSound != -1) {
                soundPool.play(correctSound, 0.7f, 0.7f, 1, 0, 1.0f);
            } else if (toneGenerator != null) {
                // Играем приятный тон для правильного ответа
                toneGenerator.startTone(ToneGenerator.TONE_PROP_ACK, 200);
            }
        }
    }

    public void playWrongSound() {
        if (soundEnabled) {
            if (wrongSound != -1) {
                soundPool.play(wrongSound, 0.8f, 0.8f, 1, 0, 0.8f);
            } else if (toneGenerator != null) {
                // Играем низкий тон для неправильного ответа
                toneGenerator.startTone(ToneGenerator.TONE_PROP_NACK, 300);
            }
        }
    }

    public void playWinSound() {
        if (soundEnabled) {
            if (winSound != -1) {
                soundPool.play(winSound, 1.0f, 1.0f, 1, 0, 1.0f);
            } else if (toneGenerator != null) {
                // Играем мелодию победы
                new Thread(() -> {
                    try {
                        toneGenerator.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 150);
                        Thread.sleep(200);
                        toneGenerator.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 150);
                        Thread.sleep(200);
                        toneGenerator.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 300);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }).start();
            }
        }
    }

    public void playLoseSound() {
        if (soundEnabled) {
            if (loseSound != -1) {
                soundPool.play(loseSound, 1.0f, 1.0f, 1, 0, 0.7f);
            } else if (toneGenerator != null) {
                // Играем грустный тон для поражения
                toneGenerator.startTone(ToneGenerator.TONE_CDMA_ABBR_ALERT, 500);
            }
        }
    }

    public void playButtonClick() {
        if (soundEnabled) {
            if (buttonClickSound != -1) {
                soundPool.play(buttonClickSound, 0.5f, 0.5f, 1, 0, 1.2f);
            } else if (toneGenerator != null) {
                // Играем короткий клик
                toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP, 50);
            }
        }
    }

    public void startBackgroundMusic() {
        if (musicEnabled && backgroundMusic == null) {
            try {
                // Раскомментируйте эти строки после добавления музыкального файла:
                backgroundMusic = MediaPlayer.create(context, R.raw.background_music);
                if (backgroundMusic != null) {
                    backgroundMusic.setLooping(true);
                    backgroundMusic.setVolume(0.3f, 0.3f);
                    backgroundMusic.start();
                }
                Log.d("SoundManager", "Background music started");
            } catch (Exception e) {
                Log.e("SoundManager", "Error starting background music: " + e.getMessage());
            }
        }
    }

    public void stopBackgroundMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.stop();
            backgroundMusic.release();
            backgroundMusic = null;
        }
    }

    public void pauseBackgroundMusic() {
        if (backgroundMusic != null && backgroundMusic.isPlaying()) {
            backgroundMusic.pause();
        }
    }

    public void resumeBackgroundMusic() {
        if (backgroundMusic != null && musicEnabled) {
            backgroundMusic.start();
        }
    }

    public void setSoundEnabled(boolean enabled) {
        this.soundEnabled = enabled;
    }

    public void setMusicEnabled(boolean enabled) {
        this.musicEnabled = enabled;
        if (!enabled) {
            stopBackgroundMusic();
        } else {
            startBackgroundMusic();
        }
    }

    public void setVibrationEnabled(boolean enabled) {
        this.vibrationEnabled = enabled;
    }

    public boolean isSoundEnabled() {
        return soundEnabled;
    }

    public boolean isMusicEnabled() {
        return musicEnabled;
    }

    public boolean isVibrationEnabled() {
        return vibrationEnabled;
    }

    public void release() {
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
        if (toneGenerator != null) {
            toneGenerator.release();
            toneGenerator = null;
        }
        stopBackgroundMusic();
    }
}
