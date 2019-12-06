package com.example.paratranslate;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

import static android.app.Activity.RESULT_OK;

public class TranslateFragment extends Fragment {
    Spinner spinner_from,spinner_to;
    String lang_from,lang_to;
    public static final int CAMERA_REQUEST=9999;
    public static final int GALLERY_REQUEST=8888;
    public EditText editText;
    public TextView textView;
    public Button translateButton;
    OCRTess mOCRTess;
    Dictionary lang_dict = new Hashtable();

    private final String API_KEY = "KEY";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_main,container,false);
        spinner_from = rootView.findViewById(R.id.lang_from);
        spinner_to = rootView.findViewById(R.id.lang_to);
        initspinnerfooter();

        ImageView camera = (ImageView) rootView.findViewById(R.id.camera);
        ImageView gallery = (ImageView) rootView.findViewById(R.id.gallery);
        editText =(EditText) rootView.findViewById(R.id.text_from);
        translateButton =(Button) rootView.findViewById(R.id.translate);
        textView =(TextView) rootView.findViewById(R.id.text_to);


        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,CAMERA_REQUEST);
            }
        });
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, GALLERY_REQUEST);
            }
        });

        translateButton.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {

                final Handler textViewHandler = new Handler();
                final String transtext = editText.getText().toString();

                lang_dict.put("English","en");
                lang_dict.put("Italian","it");
                lang_dict.put("German","de");
                lang_dict.put("Hindi","hi");
                lang_dict.put("Spanish","es");
                lang_dict.put("Russian","ru");
                ConnectivityManager connMgr = (ConnectivityManager)
                        getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = null;
                if (connMgr != null) {
                    networkInfo = connMgr.getActiveNetworkInfo();
                }

                if (networkInfo != null && networkInfo.isConnected())
                {
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            TranslateOptions options = TranslateOptions.newBuilder()
                                    .setApiKey(API_KEY)
                                    .build();
                            Translate translate = options.getService();
                            final Translation translation =
                                    translate.translate(transtext,
                                            //Translate.TranslateOption.sourceLanguage(lang_dict.get(lang_from).toString()),
                                            Translate.TranslateOption.targetLanguage(lang_dict.get(lang_to).toString()));
                            textViewHandler.post(new Runnable() {
                                @Override
                                public void run() {

                                    textView.setText(translation.getTranslatedText());

                                }
                            });
                            return null;
                        }
                    }.execute();
                }
                else
                {
                    Toast.makeText(getActivity(),"No Internet Connnection",Toast.LENGTH_LONG).show();
                    textView.setText("No Internet Connnection");
                }
                InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);


            }
        });


        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==CAMERA_REQUEST) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            Bitmap converted = bitmap.copy(Bitmap.Config.ARGB_8888, false);
            mOCRTess = new OCRTess(getActivity().getApplicationContext(),lang_from);
            String OcrData = mOCRTess.getOCRResult(converted);
            Log.e("Fragactivity", OcrData);
            editText.setText(OcrData);

        }
        else if (requestCode==GALLERY_REQUEST && resultCode == RESULT_OK && null != data) {
            Uri contentURI = data.getData();
            try {

                ImageDecoder.Source source = ImageDecoder.createSource(getActivity().getApplicationContext().getContentResolver(), contentURI);
                Bitmap bitmap = ImageDecoder.decodeBitmap(source);
                Bitmap converted = bitmap.copy(Bitmap.Config.ARGB_8888, false);
                mOCRTess = new OCRTess(getActivity().getApplicationContext(),lang_from);
                String OcrData = mOCRTess.getOCRResult(converted);
                Log.e("Fragactivity", OcrData);
                editText.setText(OcrData);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }




    private void initspinnerfooter() {
        String[] items = new String[]{"English","Italian","German","Spanish","Hindi","Russian"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_from.setAdapter(adapter);
        spinner_to.setAdapter(adapter);
        spinner_from.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Log.v("item", (String) parent.getItemAtPosition(position);
                lang_from = (String) parent.getItemAtPosition(position);
                Log.v("language","Lang selected "+lang_from );

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
                Log.v("language","Lang selected "+lang_from );
                lang_from = (String) parent.getItemAtPosition(0);

            }
        });
        spinner_to.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Log.v("item", (String) parent.getItemAtPosition(position));
                lang_to = (String) parent.getItemAtPosition(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
                lang_to = (String) parent.getItemAtPosition(0);
            }
        });
    }

}
