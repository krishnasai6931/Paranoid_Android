package com.example.shoppinglist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class Items extends AppCompatActivity {
    private static final String LOG_TAG =
            Items.class.getSimpleName();
    public static final String EXTRA_REPLY =
            "com.example.shoppinglist.extra.REPLY";
    String reply;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);
    }

    public void launchMainActivity(View view) {
        Log.d(LOG_TAG, "Button clicked!");
        //Intent intent = new Intent(this, MainActivity.class);
        reply = ((Button) view).getText().toString();
        //Log.d(LOG_TAG, reply);
        Intent replyIntent = new Intent();
        replyIntent.putExtra(EXTRA_REPLY, reply);
        setResult(RESULT_OK, replyIntent);
        Log.d(LOG_TAG,"Item selected"+reply);
        finish();
    }
}
