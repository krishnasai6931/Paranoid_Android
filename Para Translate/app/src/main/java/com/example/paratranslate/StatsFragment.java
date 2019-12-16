package com.example.paratranslate;

import android.database.Cursor;
import android.os.Bundle;
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


import java.util.ArrayList;

public class StatsFragment extends Fragment {

    private DatabaseManager dbManager;
    private ListView listView;

    private DatabaseHelper dbHelper;

    private String Lang = "German";
    private ArrayList<String> arrayList;

    Cursor cursor;


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


        /*
        String[] items = new String[]{"English","Italian","German","Spanish","Hindi","Russian"};
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, items);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        statsLang_spinner.setAdapter(dataAdapter);*/




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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                statsLang ="English";
                SimpleCursorAdapter adapter;
                cursor = dbManager.fetch_bylang(statsLang);
                adapter = new SimpleCursorAdapter(getContext(), R.layout.adapter, cursor, from, to, 0);
                listView.setAdapter(adapter);
            }
        });
    }
}
