package com.example.paratranslate;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;


import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class StatsFragment extends Fragment {

    private DatabaseManager dbManager;
    private ListView listView;

    private DatabaseHelper dbHelper;

    private ArrayList<String> arrayList;

    Cursor cursor;

    private String translatedText;
    private boolean connected;
    Translate translate;

    public String primaryLang;


    String statsLang;
    Spinner statsLang_spinner;

    final String[] from = new String[]{dbHelper._ID, dbHelper.WORD, dbHelper.COUNT};
    final int[] to = new int[]{R.id.id, R.id.listWord, R.id.listCount};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.stats_activity,container,false);
        Log.d("STATS", "s");
        dbManager = new DatabaseManager(getActivity());
        dbManager.open();
        statsLang_spinner = rootView.findViewById(R.id.statsLangSpinner);
        listView = (ListView) rootView.findViewById(R.id.myListView);
        initspinnerfooter();

        Context context = getActivity().getApplicationContext();


        /*
        String[] items = new String[]{"English","Italian","German","Spanish","Hindi","Russian"};
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, items);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        statsLang_spinner.setAdapter(dataAdapter);*/




        //Get native language of user from Shared preferences

        primaryLang = getActivity().getApplicationContext().getSharedPreferences("LANGUAGE", Context.MODE_PRIVATE).getString("Native", "English");
        //Log.d("Native_Frag", "Native:" + primaryLang);


        return rootView;

    }

    private void initspinnerfooter() {
        String[] items = new String[]{"English","Italian","German","Spanish","Hindi","Russian"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statsLang_spinner.setAdapter(adapter);
        statsLang_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Log.v("item", (String) parent.getItemAtPosition(position);
                statsLang = (String) parent.getItemAtPosition(position);
                Log.d("StatsLanguage 1","Lang selected "+statsLang );
                SimpleCursorAdapter adapter;
                cursor = dbManager.fetch_bylang(statsLang);
                adapter = new SimpleCursorAdapter(getContext(), R.layout.adapter, cursor, from, to, 0);
                listView.setAdapter(adapter);
                clickTrans();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                statsLang ="English"; //Default Language
                SimpleCursorAdapter adapter;
                cursor = dbManager.fetch_bylang(statsLang);
                adapter = new SimpleCursorAdapter(getContext(), R.layout.adapter, cursor, from, to, 0);
                listView.setAdapter(adapter);
                clickTrans();
            }
        });
    }
    void clickTrans(){

        //Used in "Your statistics", translate clicked word to native language

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item text from ListView
                Log.d("CURSOR POS", Integer.toString(cursor.getPosition()));
                int Pos = cursor.getPosition();
                boolean check = cursor.moveToPosition(Pos);
                String result;
                String Val;
                if(check){
                    Val = cursor.getString(cursor.getColumnIndex(dbHelper.WORD));
                    if (checkInternetConnection()) {

                        //If there is internet connection, get translate service and start translation:
                        getTranslateService();
                        result = translate(Val.toLowerCase(),primaryLang);

                    } else {

                        //If not, display "no connection" warning:
                        Toast.makeText(getActivity(),"No Internet Connection",Toast.LENGTH_LONG).show();
                        result="Nooooooooo!!!!!";
                    }
                }
                else{
                    Val ="noooooo!!!!!!!!!!!!"; //Lame cursor Warning
                    result=Val;
                }
                Log.d("CURSOR VAL",Val);

                //String selectedItem = (String) parent.getItemAtPosition(position);

                // Display the selected item text on TextView
                Toast.makeText(getActivity(),result,Toast.LENGTH_LONG).show();
            }
        });
    }

    //Code for translation as is also used in TranslateFragment

    public void getTranslateService() {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try (InputStream is = getResources().openRawResource(R.raw.credentials)) {

            //Get credentials:
            final GoogleCredentials myCredentials = GoogleCredentials.fromStream(is);

            //Set credentials and get translate service:
            TranslateOptions translateOptions = TranslateOptions.newBuilder().setCredentials(myCredentials).build();
            translate = translateOptions.getService();


        } catch (IOException ioe) {
            ioe.printStackTrace();

        }
    }
    public String translate(String s, String target) {

        switch(target){

            case "Italian":
                target = "it";
                break;


            case "German":
                target = "de";
                break;


            case "Hindi":
                target = "hi";
                break;


            case "Spanish":
                target = "es";
                break;


            case "Russian":
                target = "ru";
                break;


            case "English":
                target = "en";
                break;



        }


        //Get input text to be translated:
        //originalText = inputToTranslate.getText().toString();
        Translation translation = translate.translate(s, Translate.TranslateOption.targetLanguage(target), Translate.TranslateOption.model("base"));
        translatedText = translation.getTranslatedText();

        //Translated text and original text are set to TextViews:
        return translatedText;

    }
    public boolean checkInternetConnection() {

        //Check internet connection:
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        //Means that we are connected to a network (mobile or wi-fi)
        connected = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;

        return connected;
    }

}
