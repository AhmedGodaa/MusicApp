package com.examplez.musicapp.activities;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.examplez.musicapp.R;

public class Godaa extends AppCompatActivity {
    public static void implementTheme(Context context) {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            context.setTheme(R.style.Theme_Night);
        } else {
            context.setTheme(R.style.Theme_Light);
        }
    }

    public static void openActivity(Context context, Class<?> activity) {
        Intent intent = new Intent(context, activity);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }



}
