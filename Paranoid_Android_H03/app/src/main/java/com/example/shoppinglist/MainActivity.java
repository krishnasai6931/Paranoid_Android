package com.example.shoppinglist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG =
            MainActivity.class.getSimpleName();
    public static final int TEXT_REQUEST = 1;
    private int count = 0;
    private TextView txt1,txt2,txt3,txt4,txt5,txt6,txt7,txt8,txt9,txt10;
    private TextView text[] = new TextView [10];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txt1 = findViewById(R.id.item1);
        txt2 = findViewById(R.id.item2);
        txt3 = findViewById(R.id.item3);
        txt4 = findViewById(R.id.item4);
        txt5 = findViewById(R.id.item5);
        txt6 = findViewById(R.id.item6);
        txt7 = findViewById(R.id.item7);
        txt8 = findViewById(R.id.item8);
        txt9 = findViewById(R.id.item9);
        txt10 = findViewById(R.id.item10);

        if (savedInstanceState != null) {
            txt1.setText(savedInstanceState.getString("reply1"));
            txt2.setText(savedInstanceState.getString("reply2"));
            txt3.setText(savedInstanceState.getString("reply3"));
            txt4.setText(savedInstanceState.getString("reply4"));
            txt5.setText(savedInstanceState.getString("reply5"));
            txt6.setText(savedInstanceState.getString("reply6"));
            txt7.setText(savedInstanceState.getString("reply7"));
            txt8.setText(savedInstanceState.getString("reply8"));
            txt9.setText(savedInstanceState.getString("reply9"));
            txt10.setText(savedInstanceState.getString("reply10"));

        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("reply1", txt1.getText().toString());
        outState.putString("reply2", txt2.getText().toString());
        outState.putString("reply3", txt3.getText().toString());
        outState.putString("reply4", txt4.getText().toString());
        outState.putString("reply5", txt5.getText().toString());
        outState.putString("reply6", txt6.getText().toString());
        outState.putString("reply7", txt7.getText().toString());
        outState.putString("reply8", txt8.getText().toString());
        outState.putString("reply9", txt9.getText().toString());
        outState.putString("reply10", txt10.getText().toString());

    }
    public void launchItems(View view) {
        Log.d(LOG_TAG, "Button clicked!");
        Intent intent = new Intent(this, Items.class);
        startActivityForResult(intent, TEXT_REQUEST);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TEXT_REQUEST) {
            if (resultCode == RESULT_OK) {
                String reply = data.getStringExtra(Items.EXTRA_REPLY);
                //TextView textView = new TextView(this);
                //textView.setText(reply);
                switch (count) {
                    case 0:
                        txt1.setText(reply);
                        count++;
                        break;
                    case 1:
                        txt2.setText(reply);
                        count++;
                        break;
                    case 2:
                        txt3.setText(reply);
                        count++;
                        break;
                    case 3:
                        txt4.setText(reply);
                        count++;
                        break;
                    case 4:
                        txt5.setText(reply);
                        count++;
                        break;
                    case 5:
                        txt6.setText(reply);
                        count++;
                        break;
                    case 6:
                        txt7.setText(reply);
                        count++;
                        break;
                    case 7:
                        txt8.setText(reply);
                        count++;
                        break;
                    case 8:
                        txt9.setText(reply);
                        count++;
                        break;
                    case 9:
                        txt10.setText(reply);
                        count++;
                        break;
                    default:
                        break;
                }





            }
        }
    }
}
