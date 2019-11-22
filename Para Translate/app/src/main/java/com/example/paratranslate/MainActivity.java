package com.example.paratranslate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.material.navigation.NavigationView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    //private EditText editText;
    public static final int CAMERA_REQUEST=9999;
    public static final int GALLERY_REQUEST=8888;

    OCRTess mOCRTess;
    public TranslateFragment mTransFrag = new TranslateFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout_1);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        assert drawer != null;
        drawer.addDrawerListener(toggle);
        toggle.syncState();



        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,mTransFrag).commit();
        /*Spinner spinner_from = findViewById(R.id.lang_from);
        Spinner spinner_to = findViewById(R.id.lang_to);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.lang_labels, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);
        if (spinner_from != null) {
            spinner_from.setAdapter(adapter);
        }
        if (spinner_to != null) {
            spinner_to.setAdapter(adapter);
        }*/


    }

    @Override
    public void onBackPressed()
    {
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public void openCamera(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,CAMERA_REQUEST);
        String from_lang = mTransFrag.lang_from;
        Log.v("language","Lang selected "+from_lang );
    }
    public void openGallery(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(intent, GALLERY_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==CAMERA_REQUEST) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            Bitmap converted = bitmap.copy(Bitmap.Config.ARGB_8888, false);
            String from_lang = mTransFrag.lang_from;
            mOCRTess = new OCRTess(MainActivity.this,from_lang);
            String OcrData = mOCRTess.getOCRResult(converted);
            Log.e("mainactivity", OcrData);
            //editText.setText(OcrData);

        }
        else if (requestCode==GALLERY_REQUEST && resultCode == RESULT_OK && null != data) {
            Uri contentURI = data.getData();
            try {

                ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), contentURI);
                Bitmap bitmap = ImageDecoder.decodeBitmap(source);
                Bitmap converted = bitmap.copy(Bitmap.Config.ARGB_8888, false);
                String from_lang = mTransFrag.lang_from;
                mOCRTess = new OCRTess(MainActivity.this,from_lang);
                String OcrData = mOCRTess.getOCRResult(converted);
                Log.e("mainactivity", OcrData);
                //editText.setText("HEllo");

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,mTransFrag).commit();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
