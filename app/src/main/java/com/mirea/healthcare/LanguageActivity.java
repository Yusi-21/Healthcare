package com.mirea.healthcare;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;

import java.util.Locale;

public class LanguageActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_language);

        RadioGroup radioGroup = findViewById(R.id.languageRadioGroup);
        Button btnApply = findViewById(R.id.btnApplyLanguage);

        // Загружаем сохраненный язык (или берем системный)
        SharedPreferences prefs = getSharedPreferences("Settings", MODE_PRIVATE);
        String savedLang = prefs.getString("app_lang", Locale.getDefault().getLanguage());

        // Устанавливаем выбранную радио-кнопку
        switch (savedLang) {
            case "ru":
                radioGroup.check(R.id.radioRussian);
                break;
            case "zh":
                radioGroup.check(R.id.radioChinese);
                break;
            default:
                radioGroup.check(R.id.radioEnglish); // en по умолчанию
        }

        btnApply.setOnClickListener(v -> {
            String langCode = getLangCodeFromRadioGroup(radioGroup);
            saveLanguage(langCode);
            applySavedLanguage();

            Toast.makeText(this, getString(R.string.language_changed), Toast.LENGTH_SHORT).show();

            startActivity(new Intent(this, OthersActivity.class));
            finish();
        });
    }

    private String getLangCodeFromRadioGroup(RadioGroup radioGroup) {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        if (selectedId == R.id.radioRussian) return "ru";
        if (selectedId == R.id.radioChinese) return "zh";
        return "en";
    }
}