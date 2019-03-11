package com.example.alumnot.mapps;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.text.TextPaint;
import android.widget.Toast;

public class Preferencias extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferencias);
    }

    @Override
    protected void finalize() {
        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(this);
        boolean usingPreferences = pref.getBoolean("usingPreferencies", true);
        super.onResume();
    }
}