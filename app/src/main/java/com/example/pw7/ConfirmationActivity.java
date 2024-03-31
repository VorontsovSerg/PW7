package com.example.pw7;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ConfirmationActivity extends AppCompatActivity {

    private TextView nameLabel;
    private EditText nameEditText;
    private Button confirmButton;
    private String defaultName = "Almir Listo"; // Устанавливаем имя по умолчанию

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        nameLabel = findViewById(R.id.name_label);
        nameEditText = findViewById(R.id.name_edit_text);
        confirmButton = findViewById(R.id.confirm_button);

        // Устанавливаем имя по умолчанию
        nameLabel.setText("Ваше имя: " + defaultName);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = nameEditText.getText().toString().trim();
                if (!newName.isEmpty()) {
                    // Показываем AlertDialog с новым именем
                    showConfirmationDialog(newName);
                }
            }
        });
    }

    private void showConfirmationDialog(String newName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Подтверждение")
                .setMessage("Вы уверены, что хотите изменить имя на " + newName + "?")
                .setPositiveButton("Да", (dialog, which) -> {
                    // Если пользователь нажал "Да", меняем имя
                    nameLabel.setText("Ваше имя: " + newName);
                })
                .setNegativeButton("Отмена", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
