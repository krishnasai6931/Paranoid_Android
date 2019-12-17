package com.example.paratranslate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;


public class Welcome extends AppCompatActivity {

    Spinner input;
    public String native_lang;
    Button inputButton;




    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


        input = findViewById(R.id.input_to_db);
        initspinner();

        inputButton = findViewById(R.id.go);


    }


    private void initspinner() {
        String[] items = new String[]{"English", "Italian", "German", "Spanish", "Hindi", "Russian"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        input.setAdapter(adapter);

        input.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Log.v("item", (String) parent.getItemAtPosition(position);

                native_lang = (String) parent.getItemAtPosition(position);
                Log.v("welcome", "Lang selected " + native_lang);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
                native_lang = (String) parent.getItemAtPosition(0);
                Log.v("welcome", "Lang selected " + native_lang);


            }
        });
    }

    public void goToTranslate(View view) {

        Log.d("Welcome_PRIMARY_Button", "N" + native_lang);

        getSharedPreferences("LANGUAGE", MODE_PRIVATE).edit().putString("Native", native_lang).commit();

        Intent intent = new Intent(Welcome.this, MainActivity.class);
        startActivity(intent);
    }

}
