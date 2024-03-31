package com.example.pw7;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TestMusicService musicService;
    private boolean isBound = false;

    private TextView dateTextView;
    private TextView timeTextView;

    // Переменные для хранения текущей даты и времени
    private String currentDate;
    private String currentTime;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            TestMusicService.LocalBinder binder = (TestMusicService.LocalBinder) service;
            musicService = binder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            isBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Привязываемся к сервису музыки
        Intent intent = new Intent(this, TestMusicService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

        // Находим кнопки управления музыкой
        Button playButton = findViewById(R.id.play_button);
        Button pauseButton = findViewById(R.id.pause_button);

        // Назначаем обработчики событий для кнопок управления музыкой
        playButton.setOnClickListener(v -> {
            if (isBound) {
                musicService.playMusic();
            }
        });

        pauseButton.setOnClickListener(v -> {
            if (isBound) {
                musicService.pauseMusic();
            }
        });

        // Находим текстовые поля для отображения даты и времени
        dateTextView = findViewById(R.id.date_text_view);
        timeTextView = findViewById(R.id.time_text_view);

        // Устанавливаем текущую дату и время в соответствующие текстовые поля
        setCurrentDateTime();

        // Находим и назначаем обработчики событий для кнопок диалоговых окон
        Button datePickerDialogButton = findViewById(R.id.date_picker_dialog_button);
        datePickerDialogButton.setOnClickListener(v -> showDatePickerDialog());

        Button timePickerDialogButton = findViewById(R.id.time_picker_dialog_button);
        timePickerDialogButton.setOnClickListener(v -> showTimePickerDialog());

        // Находим кнопку перехода на вторую активность
        Button goToConfirmationActivityButton = findViewById(R.id.go_to_second_activity_button);
        goToConfirmationActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Запускаем переход на вторую активность
                Intent intent = new Intent(MainActivity.this, ConfirmationActivity.class);
                startActivity(intent);
            }
        });
    }

    // Метод для установки текущей даты и времени в соответствующие текстовые поля
    private void setCurrentDateTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        currentDate = dateFormat.format(calendar.getTime());
        currentTime = timeFormat.format(calendar.getTime());

        dateTextView.setText("Дата: " + currentDate);
        timeTextView.setText("Время: " + currentTime);
    }

    // Метод для отображения DatePickerDialog
    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDayOfMonth) -> {
            // Обработка выбора даты
            updateDate(selectedYear, selectedMonth, selectedDayOfMonth);
        }, year, month, dayOfMonth);
        datePickerDialog.show();
    }

    // Метод для обновления текстового поля с датой
    private void updateDate(int year, int month, int dayOfMonth) {
        currentDate = String.format(Locale.getDefault(), "%02d/%02d/%d", dayOfMonth, month + 1, year);
        dateTextView.setText("Дата: " + currentDate);
    }

    // Метод для отображения TimePickerDialog
    private void showTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, selectedHourOfDay, selectedMinute) -> {
            // Обработка выбора времени
            updateTime(selectedHourOfDay, selectedMinute);
        }, hourOfDay, minute, true);
        timePickerDialog.show();
    }

    // Метод для обновления текстового поля с временем
    private void updateTime(int hourOfDay, int minute) {
        currentTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
        timeTextView.setText("Время: " + currentTime);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBound) {
            unbindService(serviceConnection);
            isBound = false;
        }
    }
}