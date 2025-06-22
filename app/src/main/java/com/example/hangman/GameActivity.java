package com.example.hangman;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameActivity extends AppCompatActivity {

    private String wordToGuess;
    private StringBuilder currentGuess;
    private List<Character> wrongLetters;
    private int wrongCount;
    private final int MAX_WRONG = 6;

    private TextView tvWord;
    private TextView tvWrongLetters;
    private ImageView ivHangman;
    private SoundManager soundManager;

    private Map<Character, Button> letterButtons;

    private int[] hangmanImages = {
            R.drawable.hangman_0,
            R.drawable.hangman_1,
            R.drawable.hangman_2,
            R.drawable.hangman_3,
            R.drawable.hangman_4,
            R.drawable.hangman_5,
            R.drawable.hangman_6
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        soundManager = SoundManager.getInstance(this);

        // Получаем слово из предыдущей активности
        wordToGuess = getIntent().getStringExtra("WORD");
        if (wordToGuess == null) {
            finish();
            return;
        }

        initializeViews();
        initializeLetterButtons();
        initializeGame();
    }

    private void initializeViews() {
        tvWord = findViewById(R.id.tvWord);
        tvWrongLetters = findViewById(R.id.tvWrongLetters);
        ivHangman = findViewById(R.id.ivHangman);

        ImageButton btnPause = findViewById(R.id.btnPause);
        btnPause.setOnClickListener(v -> {
            soundManager.playButtonClick();
            Intent intent = new Intent(GameActivity.this, PauseMenuActivity.class);
            startActivity(intent);
        });
    }

    private void initializeLetterButtons() {
        letterButtons = new HashMap<>();

        // Определяем, какой алфавит использовать
        boolean isRussian = wordToGuess.matches("[А-Я]+");

        if (isRussian) {
            initializeRussianButtons();
        } else {
            initializeEnglishButtons();
        }
    }

    private void initializeRussianButtons() {
        // Инициализируем все кнопки-буквы для русского алфавита
        letterButtons.put('А', findViewById(R.id.btnА));
        letterButtons.put('Б', findViewById(R.id.btnБ));
        letterButtons.put('В', findViewById(R.id.btnВ));
        letterButtons.put('Г', findViewById(R.id.btnГ));
        letterButtons.put('Д', findViewById(R.id.btnД));
        letterButtons.put('Е', findViewById(R.id.btnЕ));
        letterButtons.put('Ж', findViewById(R.id.btnЖ));
        letterButtons.put('З', findViewById(R.id.btnЗ));
        letterButtons.put('И', findViewById(R.id.btnИ));
        letterButtons.put('Й', findViewById(R.id.btnЙ));
        letterButtons.put('К', findViewById(R.id.btnК));
        letterButtons.put('Л', findViewById(R.id.btnЛ));
        letterButtons.put('М', findViewById(R.id.btnМ));
        letterButtons.put('Н', findViewById(R.id.btnН));
        letterButtons.put('О', findViewById(R.id.btnО));
        letterButtons.put('П', findViewById(R.id.btnП));
        letterButtons.put('Р', findViewById(R.id.btnР));
        letterButtons.put('С', findViewById(R.id.btnС));
        letterButtons.put('Т', findViewById(R.id.btnТ));
        letterButtons.put('У', findViewById(R.id.btnУ));
        letterButtons.put('Ф', findViewById(R.id.btnФ));
        letterButtons.put('Х', findViewById(R.id.btnХ));
        letterButtons.put('Ц', findViewById(R.id.btnЦ));
        letterButtons.put('Ч', findViewById(R.id.btnЧ));
        letterButtons.put('Ш', findViewById(R.id.btnШ));
        letterButtons.put('Щ', findViewById(R.id.btnЩ));
        letterButtons.put('Ь', findViewById(R.id.btnЬ));
        letterButtons.put('Ы', findViewById(R.id.btnЫ));
        letterButtons.put('Ъ', findViewById(R.id.btnЪ));
        letterButtons.put('Э', findViewById(R.id.btnЭ));
        letterButtons.put('Ю', findViewById(R.id.btnЮ));
        letterButtons.put('Я', findViewById(R.id.btnЯ));

        setupButtonListeners();
    }

    private void initializeEnglishButtons() {
        // Для английского алфавита меняем текст кнопок
        Button[] buttons = {
                findViewById(R.id.btnА), findViewById(R.id.btnБ), findViewById(R.id.btnВ), findViewById(R.id.btnГ),
                findViewById(R.id.btnД), findViewById(R.id.btnЕ), findViewById(R.id.btnЖ), findViewById(R.id.btnЗ),
                findViewById(R.id.btnИ), findViewById(R.id.btnЙ), findViewById(R.id.btnК), findViewById(R.id.btnЛ),
                findViewById(R.id.btnМ), findViewById(R.id.btnН), findViewById(R.id.btnО), findViewById(R.id.btnП),
                findViewById(R.id.btnР), findViewById(R.id.btnС), findViewById(R.id.btnТ), findViewById(R.id.btnУ),
                findViewById(R.id.btnФ), findViewById(R.id.btnХ), findViewById(R.id.btnЦ), findViewById(R.id.btnЧ),
                findViewById(R.id.btnШ), findViewById(R.id.btnЩ)
        };

        String[] englishLetters = {
                "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
                "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
        };

        for (int i = 0; i < Math.min(buttons.length, englishLetters.length); i++) {
            if (buttons[i] != null) {
                buttons[i].setText(englishLetters[i]);
                letterButtons.put(englishLetters[i].charAt(0), buttons[i]);
            }
        }

        // Скрываем лишние кнопки для английского алфавита
        findViewById(R.id.btnЬ).setVisibility(View.GONE);
        findViewById(R.id.btnЫ).setVisibility(View.GONE);
        findViewById(R.id.btnЪ).setVisibility(View.GONE);
        findViewById(R.id.btnЭ).setVisibility(View.GONE);
        findViewById(R.id.btnЮ).setVisibility(View.GONE);
        findViewById(R.id.btnЯ).setVisibility(View.GONE);

        setupButtonListeners();
    }

    private void setupButtonListeners() {
        // Устанавливаем обработчики нажатий
        for (Map.Entry<Character, Button> entry : letterButtons.entrySet()) {
            final char letter = entry.getKey();
            Button button = entry.getValue();
            if (button != null) {
                button.setOnClickListener(v -> makeGuess(letter, button));
            }
        }
    }

    private void initializeGame() {
        wrongLetters = new ArrayList<>();
        wrongCount = 0;
        currentGuess = new StringBuilder();

        // Инициализируем текущую догадку подчеркиваниями
        for (int i = 0; i < wordToGuess.length(); i++) {
            currentGuess.append("_");
        }

        updateDisplay();
    }

    private void makeGuess(char letter, Button button) {
        soundManager.playButtonClick();

        // Отключаем кнопку
        button.setEnabled(false);

        // Проверяем, есть ли буква в слове
        boolean found = false;
        for (int i = 0; i < wordToGuess.length(); i++) {
            if (wordToGuess.charAt(i) == letter) {
                currentGuess.setCharAt(i, letter);
                found = true;
            }
        }

        if (found) {
            soundManager.playCorrectSound();
            // Помечаем кнопку как правильную (зеленая)
            button.setActivated(true);
        } else {
            wrongLetters.add(letter);
            wrongCount++;
            soundManager.playWrongSound();
            soundManager.vibrate();
            // Помечаем кнопку как неправильную (красная)
            button.setSelected(true);
        }

        updateDisplay();
        checkGameEnd();
    }

    private void updateDisplay() {
        // Обновляем отображение слова
        StringBuilder displayWord = new StringBuilder();
        for (int i = 0; i < currentGuess.length(); i++) {
            displayWord.append(currentGuess.charAt(i));
            if (i < currentGuess.length() - 1) {
                displayWord.append(" ");
            }
        }
        tvWord.setText(displayWord.toString());

        // Обновляем неправильные буквы
        StringBuilder wrongLettersText = new StringBuilder();
        for (int i = 0; i < wrongLetters.size(); i++) {
            wrongLettersText.append(wrongLetters.get(i));
            if (i < wrongLetters.size() - 1) {
                wrongLettersText.append(", ");
            }
        }
        tvWrongLetters.setText(wrongLettersText.toString());

        // Обновляем изображение виселицы
        ivHangman.setImageResource(hangmanImages[wrongCount]);
    }

    private void checkGameEnd() {
        // Проверяем победу
        if (currentGuess.toString().equals(wordToGuess)) {
            soundManager.playWinSound();
            Intent intent = new Intent(GameActivity.this, WinActivity.class);
            intent.putExtra("WORD", wordToGuess);
            startActivity(intent);
            finish();
            return;
        }

        // Проверяем поражение
        if (wrongCount >= MAX_WRONG) {
            soundManager.playLoseSound();
            soundManager.vibrate();
            Intent intent = new Intent(GameActivity.this, LoseActivity.class);
            intent.putExtra("WORD", wordToGuess);
            startActivity(intent);
            finish();
        }
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
