package com.example.whowroteit;

import android.content.Context;
import androidx.loader.content.AsyncTaskLoader;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

public class BookLoader extends AsyncTaskLoader<String> {

    private String mQueryString;

    public BookLoader(Context context, String queryString) {
        super(context);
        mQueryString = queryString;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Nullable
    @Override
    public String loadInBackground() {
        return NetworkUtils.getBookInfo(mQueryString);
    }
}

