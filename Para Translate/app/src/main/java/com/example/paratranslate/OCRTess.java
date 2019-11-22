package com.example.paratranslate;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.util.Log;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Dictionary;
import java.util.Hashtable;

public class OCRTess {
    private String datapath;
    private TessBaseAPI mTess;
    Context context;
    Dictionary lang_dict = new Hashtable();
    public OCRTess(Context context,String language) {
        this.context = context;
        lang_dict.put("English","eng");
        lang_dict.put("Italian","ita");
        lang_dict.put("German","deu");
        lang_dict.put("Hindi","hin");
        lang_dict.put("Spanish","spa");
        lang_dict.put("Russian","rus");
        datapath = context.getExternalFilesDir(null) + "/ocrctz/";
        Log.d("mylog", language);
        File dir = new File(datapath + "/tessdata/");
        File file = new File(datapath + "/tessdata/" + lang_dict.get(language).toString()+".traineddata");
        if (!file.exists()) {
            Log.d("mylog", "in file doesn't exist");
            dir.mkdirs();
            copyFile(context,language);
        }
        mTess = new TessBaseAPI();
        String lang = lang_dict.get(language).toString();
        mTess.init(datapath, lang);
        //Auto only
        mTess.setPageSegMode(TessBaseAPI.PageSegMode.PSM_AUTO_ONLY);
    }
    public void stopRecognition() {
        mTess.stop();
    }

    public String getOCRResult(Bitmap bitmap) {
        if (bitmap == null) {
            Log.e("Mylog", "Bitmap must be non-null");
            return null;
        }
        else if (bitmap.getConfig() != Bitmap.Config.ARGB_8888) {
            Log.e("Mylog", "Bitmap config must be ARGB_8888");
            return null;
        }

        else if (bitmap != null) {
            Log.d("mylog", "NOT NULL");
            mTess.setImage(bitmap);
            String result = mTess.getUTF8Text();
            Log.d("mylog", result);
            return result;
        }
        else{
            Log.e("Mylog", "Failed to Decode");
            return null;
        }
    }

    public void onDestroy() {
        if (mTess != null)
            mTess.end();
    }

    /*private void copyFile(Context context, String language) {
        AssetManager assetManager = context.getAssets();
        try {

            InputStream in = assetManager.open(lang_dict.get(language).toString()+".traineddata");
            OutputStream out = new FileOutputStream(datapath + "/tessdata/" + lang_dict.get(language).toString()+".traineddata");
            byte[] buffer = new byte[1024];
            int read = in.read(buffer);
            while (read != -1) {
                out.write(buffer, 0, read);
                read = in.read(buffer);
            }
        } catch (Exception e) {
            Log.d("mylog", "couldn't copy with the following error : "+e.toString());
        }
    }*/
    private void copyFile(Context context, String language) {
        AssetManager assetManager = context.getAssets();
        try {
            String[] filelist = assetManager.list(lang_dict.get(language).toString());
            for (int i=0; i<filelist.length; i++) {
                InputStream in = assetManager.open(lang_dict.get(language).toString()+"/"+filelist[i]);
                OutputStream out = new FileOutputStream(datapath + "/tessdata/" + filelist[i]);
                byte[] buffer = new byte[1024];
                int read = in.read(buffer);
                while (read != -1) {
                    out.write(buffer, 0, read);
                    read = in.read(buffer);
                }
            }
        }
        catch (Exception e) {
            Log.d("mylog", "couldn't copy with the following error : "+e.toString());
        }
    }

}