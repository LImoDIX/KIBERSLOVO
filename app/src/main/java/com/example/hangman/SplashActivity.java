package com.example.hangman;

import androidx.appcompat.app.AppCompatActivity;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    private ImageView ivLogo;
    private TextView tvTitle;
    private TextView tvSubtitle;
    private TextView tvLoading;
    private ProgressBar progressBar;

    private Handler handler = new Handler(Looper.getMainLooper());
    private int progressStatus = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Скрываем ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        initializeViews();
        startAnimations();
        simulateLoading();
    }

    private void initializeViews() {
        ivLogo = findViewById(R.id.ivLogo);
        tvTitle = findViewById(R.id.tvTitle);
        tvSubtitle = findViewById(R.id.tvSubtitle);
        tvLoading = findViewById(R.id.tvLoading);
        progressBar = findViewById(R.id.progressBar);
    }

    private void startAnimations() {
        // Анимация логотипа (появление с поворотом)
        ObjectAnimator logoFadeIn = ObjectAnimator.ofFloat(ivLogo, "alpha", 0f, 1f);
        ObjectAnimator logoRotation = ObjectAnimator.ofFloat(ivLogo, "rotation", -180f, 0f);
        ObjectAnimator logoScale = ObjectAnimator.ofFloat(ivLogo, "scaleX", 0.5f, 1f);
        ObjectAnimator logoScaleY = ObjectAnimator.ofFloat(ivLogo, "scaleY", 0.5f, 1f);

        AnimatorSet logoAnimSet = new AnimatorSet();
        logoAnimSet.playTogether(logoFadeIn, logoRotation, logoScale, logoScaleY);
        logoAnimSet.setDuration(1000);
        logoAnimSet.setInterpolator(new BounceInterpolator());

        // Анимация заголовка (появление снизу)
        ObjectAnimator titleFadeIn = ObjectAnimator.ofFloat(tvTitle, "alpha", 0f, 1f);
        ObjectAnimator titleSlideUp = ObjectAnimator.ofFloat(tvTitle, "translationY", 100f, 0f);

        AnimatorSet titleAnimSet = new AnimatorSet();
        titleAnimSet.playTogether(titleFadeIn, titleSlideUp);
        titleAnimSet.setDuration(800);
        titleAnimSet.setStartDelay(500);
        titleAnimSet.setInterpolator(new AccelerateDecelerateInterpolator());

        // Анимация подзаголовка
        ObjectAnimator subtitleFadeIn = ObjectAnimator.ofFloat(tvSubtitle, "alpha", 0f, 1f);
        ObjectAnimator subtitleSlideUp = ObjectAnimator.ofFloat(tvSubtitle, "translationY", 50f, 0f);

        AnimatorSet subtitleAnimSet = new AnimatorSet();
        subtitleAnimSet.playTogether(subtitleFadeIn, subtitleSlideUp);
        subtitleAnimSet.setDuration(600);
        subtitleAnimSet.setStartDelay(800);
        subtitleAnimSet.setInterpolator(new AccelerateDecelerateInterpolator());

        // Запуск анимаций
        logoAnimSet.start();
        titleAnimSet.start();
        subtitleAnimSet.start();
    }

    private void simulateLoading() {
        // Киберпанк тексты загрузки
        Thread loadingThread = new Thread(() -> {
            String[] loadingTexts = {
                    "Инициализация системы...",
                    "Загрузка нейросети...",
                    "Подключение к матрице...",
                    "Активация протоколов...",
                    "Запуск киберпространства...",
                    "Система готова к взлому..."
            };

            try {
                for (int i = 0; i <= 100; i += 2) {
                    Thread.sleep(50); // Задержка для реалистичности

                    final int progress = i;
                    final String loadingText = loadingTexts[Math.min(i / 17, loadingTexts.length - 1)];

                    handler.post(() -> {
                        progressBar.setProgress(progress);
                        tvLoading.setText(loadingText);

                        // Добавляем мигание точек
                        if (progress % 10 == 0) {
                            animateLoadingText();
                        }
                    });
                }

                // Задержка перед переходом
                Thread.sleep(500);

                handler.post(this::navigateToMainActivity);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        loadingThread.start();
    }

    private void animateLoadingText() {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(tvLoading, "scaleX", 1f, 1.1f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(tvLoading, "scaleY", 1f, 1.1f, 1f);

        AnimatorSet scaleAnimSet = new AnimatorSet();
        scaleAnimSet.playTogether(scaleX, scaleY);
        scaleAnimSet.setDuration(300);
        scaleAnimSet.start();
    }

    private void navigateToMainActivity() {
        // Анимация исчезновения
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(findViewById(android.R.id.content), "alpha", 1f, 0f);
        fadeOut.setDuration(500);
        fadeOut.start();

        // Переход к главной активности
        handler.postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();

            // Анимация перехода
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }, 500);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Очищаем все отложенные задачи при уничтожении активности
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }
}
