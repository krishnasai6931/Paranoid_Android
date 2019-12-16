package com.example.paratranslate;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DatabaseManager {
    private DatabaseHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;
    private Cursor cursor;
    private int count;

    public DatabaseManager(Context c) {
        this.context = c;
    }

    public DatabaseManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(String sentence, String language) {
        String[] words = sentence.split("\\s+");
        for (int i = 0; i < words.length; i++) {
            // You may want to check for a non-word character before blindly
            // performing a replacement
            // It may also be necessary to adjust the character class
            words[i] = words[i].replaceAll("[^\\w]", "");
            words[i] = words[i];
        }

        for (int i=0; i<words.length; i++){
            cursor = database.rawQuery("SELECT * FROM " + dbHelper.TABLE_NAME+ " WHERE " + dbHelper.WORD + " = '"+words[i]+"' and " + dbHelper.LANG + " = '" + language + "'", null);
            Log.d("Cursor", String.valueOf(cursor));
            if(cursor!=null && cursor.getCount()>0){
                Log.d("Count-Before", String.valueOf(count));
                cursor.moveToFirst();
                count = cursor.getInt(cursor.getColumnIndex(dbHelper.COUNT)) +1;
                Log.d("Count-After", String.valueOf(count));
                ContentValues contentValues = new ContentValues();
                contentValues.put(dbHelper.WORD, words[i]);
                contentValues.put(dbHelper.LANG, cursor.getString(cursor.getColumnIndex(dbHelper.LANG)));
                contentValues.put(dbHelper.COUNT, Integer.toString(count));
                database.update(dbHelper.TABLE_NAME, contentValues, dbHelper._ID + " = "+ cursor.getInt(cursor.getColumnIndex(dbHelper._ID)), null);
            }
            else{
                ContentValues contentValues = new ContentValues();
                contentValues.put(dbHelper.WORD, words[i]);
                contentValues.put(dbHelper.LANG, language);
                contentValues.put(dbHelper.COUNT, 1);
                database.insert(dbHelper.TABLE_NAME, null, contentValues);
            }
        }
    }

    public void update(int id, String word, String lang, int count) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(dbHelper.WORD, word);
        contentValues.put(dbHelper.LANG, lang);
        contentValues.put(dbHelper.COUNT, count);
        database.update(dbHelper.TABLE_NAME, contentValues, dbHelper._ID + " = " + id, null);
    }

    public void delete(int id) {
        database.delete(dbHelper.TABLE_NAME,dbHelper._ID + " ='" + id + "'",null);
    }

    public Cursor fetch() {
        String[] columns = new String[]{dbHelper._ID, dbHelper.WORD, dbHelper.LANG, dbHelper.COUNT};
        Cursor cursor = database.query(dbHelper.TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor fetch_bylang(String lang){
        Cursor cursor = database.rawQuery("SELECT * FROM " + dbHelper.TABLE_NAME + " WHERE " + dbHelper.LANG + " = '" + lang + "'" + " ORDER BY " + dbHelper.COUNT, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }


}
