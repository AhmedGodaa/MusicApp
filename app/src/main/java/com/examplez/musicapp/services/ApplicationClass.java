package com.examplez.musicapp.services;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.examplez.musicapp.models.Constants;

public class ApplicationClass extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel firstChannel = new NotificationChannel(
                    Constants.CHANNEL_ID_1,
                    "First Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            firstChannel.setDescription("Channel 1 Desc..");

            NotificationChannel secondChannel = new NotificationChannel(
                    Constants.CHANNEL_ID_2,
                    "Second Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            secondChannel.setDescription("Channel 2 Desc..");
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(firstChannel);
            notificationManager.createNotificationChannel(secondChannel);

        }
    }
}
