package com.barbos.loader;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private EditText etKey;
    private Button btnActivate, btnInject;
    private TextView tvStatus;
    private LicenseManager license;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        license = new LicenseManager(this);

        etKey = findViewById(R.id.etKey);
        btnActivate = findViewById(R.id.btnActivate);
        btnInject = findViewById(R.id.btnInject);
        tvStatus = findViewById(R.id.tvStatus);

        if (license.check()) {
            tvStatus.setText("✅ Лицензия активна");
            btnInject.setEnabled(true);
        }

        btnActivate.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                String key = etKey.getText().toString().trim();
                if (key.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Введите ключ", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (license.activate(key)) {
                    tvStatus.setText("✅ Активация успешна!");
                    btnInject.setEnabled(true);
                    Toast.makeText(MainActivity.this, "Ключ активирован!", Toast.LENGTH_SHORT).show();
                } else {
                    tvStatus.setText("❌ Неверный ключ");
                }
            }
        });

        btnInject.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (!license.check()) {
                    Toast.makeText(MainActivity.this, "Сначала активируйте ключ", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!Settings.canDrawOverlays(MainActivity.this)) {
                        startActivity(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION));
                        return;
                    }
                }
                startService(new Intent(MainActivity.this, OverlayService.class));
                tvStatus.setText("✅ Чит запущен");
                Toast.makeText(MainActivity.this, "Чит активирован!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
