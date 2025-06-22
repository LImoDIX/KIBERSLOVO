package com.example.hangman;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class EnterWordActivity extends AppCompatActivity {

    private EditText etWord;
    private SoundManager soundManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_word);

        soundManager = SoundManager.getInstance(this);

        etWord = findViewById(R.id.etWord);
        Button btnReady = findViewById(R.id.btnReady);
        ImageButton btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> {
            soundManager.playButtonClick();
            finish();
        });

        btnReady.setOnClickListener(v -> {
            soundManager.playButtonClick();

            String word = etWord.getText().toString().trim().toUpperCase();

            if (word.isEmpty()) {
                Toast.makeText(this, getString(R.string.enter_word_please), Toast.LENGTH_SHORT).show();
                return;
            }

            if (word.length() < 3) {
                Toast.makeText(this, getString(R.string.word_min_length), Toast.LENGTH_SHORT).show();
                return;
            }

            // Проверяем, что слово содержит только русские или английские буквы
            if (!word.matches("[А-Я]+") && !word.matches("[A-Z]+")) {
                Toast.makeText(this, getString(R.string.use_letters_only), Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(EnterWordActivity.this, GameActivity.class);
            intent.putExtra("WORD", word);
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

