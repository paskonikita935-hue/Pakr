package com.barbos.loader;

import android.content.Context;
import android.content.SharedPreferences;

public class LicenseManager {
    private SharedPreferences prefs;
    private boolean isValid = false;

    public LicenseManager(Context ctx) {
        prefs = ctx.getSharedPreferences("barbos_license", Context.MODE_PRIVATE);
        load();
    }

    public boolean activate(String key) {
        if (key.startsWith("Barbos-7day-") && key.length() >= 20) {
            isValid = true;
            save(key);
            return true;
        }
        return false;
    }

    public boolean check() {
        return isValid;
    }

    private void save(String key) {
        prefs.edit().putString("key", key).putBoolean("active", true).apply();
    }

    private void load() {
        isValid = prefs.getBoolean("active", false);
    }
}
