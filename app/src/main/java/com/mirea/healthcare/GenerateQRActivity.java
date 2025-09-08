package com.mirea.healthcare;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class GenerateQRActivity extends BaseActivity {
    private ImageView ivQRCode;
    private TextView tvUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_generate_qr);

        ivQRCode = findViewById(R.id.iv_qr_code);
        tvUserName = findViewById(R.id.tv_user_name_qr);

        AuthManager authManager = new AuthManager(this);
        AppDatabase db = AppDatabase.getDatabase(this);
        int userId = authManager.getCurrentUserId();

        new Thread(() -> {
            User user = db.userDao().getUserById(userId);
            runOnUiThread(() -> {
                if (user != null) {
                    tvUserName.setText(user.getUsername());
                }
            });
        }).start();

        String appointmentData = "{\"patientId\":123,\"doctorId\":456,\"date\":\"2025-05-10\",\"time\":\"10:00\"}";

        try {
            BitMatrix bitMatrix = new QRCodeWriter().encode(
                    appointmentData,
                    BarcodeFormat.QR_CODE,
                    500, 500
            );
            Bitmap bitmap = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888);
            for (int x = 0; x < 500; x++) {
                for (int y = 0; y < 500; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            ivQRCode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}