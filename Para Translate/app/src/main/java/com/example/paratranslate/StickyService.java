package com.example.paratranslate;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

//Detecting App Close

public class StickyService extends Service {
    private String sharedPrefFile =
            "com.example.android.paratranslate";
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.e("StickyService","Removed");
        SharedPreferences settings = getApplicationContext().getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE);
        settings.edit().clear().commit();
    }
}
