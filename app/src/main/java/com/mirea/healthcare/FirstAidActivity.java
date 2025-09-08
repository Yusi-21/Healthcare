package com.mirea.healthcare;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;

public class FirstAidActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_first_aid);

        Button btnNearestHospital = findViewById(R.id.btnNearestHospital);
        btnNearestHospital.setOnClickListener(v -> openYandexMaps());

        TextView emergencyNumber = findViewById(R.id.emergencyNumber);
        emergencyNumber.setOnClickListener(v -> callEmergency());
    }

    private void openYandexMaps() {
        // Координаты пользователя (Москва для примера)
        double latitude = 55.751244;
        double longitude = 37.618423;

        // URI для Яндекс.Карт
        Uri yandexUri = Uri.parse("yandexmaps://maps.yandex.ru/?text=Больницы&ll=" +
                longitude + "," + latitude + "&z=14");

        // Создаем Intent для Яндекс.Карт
        Intent yandexIntent = new Intent(Intent.ACTION_VIEW, yandexUri);
        yandexIntent.setPackage("ru.yandex.yandexmaps"); // Правильный пакет для Яндекс.Карт

        // Проверяем, установлены ли Яндекс.Карты
        if (yandexIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(yandexIntent);
        } else {
            // Если Яндекс.Карт нет, пробуем через браузер
            try {
                Uri webUri = Uri.parse("https://yandex.ru/maps/?text=Больницы&ll=" +
                        longitude + "," + latitude + "&z=14"); // &z=14- Оптимальный масштаб для просмотра ближайших больниц
                Intent webIntent = new Intent(Intent.ACTION_VIEW, webUri);
                startActivity(webIntent);
            } catch (Exception e) {
                Toast.makeText(this, "Не удалось открыть карты", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void callEmergency() {
        try {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:103"));
            startActivity(callIntent);
        } catch (Exception e) {
            Toast.makeText(this, "Не удалось открыть номер", Toast.LENGTH_SHORT).show();
        }
    }
}