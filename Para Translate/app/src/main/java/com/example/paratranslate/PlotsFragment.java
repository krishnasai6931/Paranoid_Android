package com.example.paratranslate;

//Class to generate data visualisations

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.math.MathUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PlotsFragment extends Fragment {

    private DatabaseManager dbManager;
    private DatabaseHelper dbHelper;
    PieChart pieChart;
    Cursor cursor;
    String plotLang;
    List<String> words = new LinkedList<>();
    List<Integer> counts = new LinkedList<>();
    int[] counts_arr;
    int count = 1;
    Spinner plotLang_spinner;

    @Nullable
    @Override


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_plots,container,false);
        plotLang_spinner = rootView.findViewById(R.id.plotLangSpinner);
        pieChart = rootView.findViewById(R.id.piechart);
        //pieChart.setUsePercentValues(true);
        initspinnerfooter();


        return rootView;
    }

    //Get total count of each word

    public static int get_sum(List<Integer> list) {
        int sum = 0;
        for (int i: list) {
            sum += i;
        }
        return sum;
    }

    //View saved words in chosen language
    private void initspinnerfooter() {
        String[] items = new String[]{"English","Italian","German","Spanish","Hindi","Russian"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        plotLang_spinner.setAdapter(adapter);
        plotLang_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Log.v("item", (String) parent.getItemAtPosition(position);
                plotLang = (String) parent.getItemAtPosition(position);
                Log.d("PlotLanguage 1","Lang selected "+plotLang );
                generatePlot(plotLang);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                plotLang ="English";
                generatePlot(plotLang);

            }
        });
    }

    void generatePlot(String plotLang){
        pieChart.clear();

        List<PieEntry> value = new ArrayList<>();

        dbManager = new DatabaseManager(getActivity());
        dbManager.open();
        cursor = dbManager.fetch_bylang_limit(plotLang);
        Log.d("COUNT PLOT", ""+cursor.getCount());

        words.clear();
        counts.clear();
        for(int i=0; i<cursor.getCount(); i++){
            cursor.moveToPosition(i);
            Log.d("CURSOR POSITION PLOT", ""+cursor.getPosition());
            Log.d("cursor plot", ""+i);
            words.add(cursor.getString(cursor.getColumnIndex(dbHelper.WORD)));
            counts.add(cursor.getInt(cursor.getColumnIndex(dbHelper.COUNT)));

        }
        int counts_sum = get_sum(counts);
        Log.d("WORDS PLOT", ""+words);
        Log.d("WORDS PLOT", ""+counts);

        pieChart.setHoleRadius(10f);

        for (int i = 0; i < counts.size(); i++) {
            value.add(new PieEntry(counts.get(i), words.get(i)));
        }

        pieChart.getDescription().setEnabled(false);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setEntryLabelTextSize(15f);
        PieDataSet pieDataSet = new PieDataSet(value, "Words");
        pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);

        PieData pieData = new PieData(pieDataSet);
        pieData.setValueTextSize(15f);
        pieChart.setData(pieData);

        pieChart.animateXY(1500,1500);

    }
}