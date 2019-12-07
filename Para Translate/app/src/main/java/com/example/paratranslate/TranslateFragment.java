package com.example.paratranslate;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class TranslateFragment extends Fragment {
    Spinner spinner_from,spinner_to;
    String lang_from,lang_to;
    public static final int CAMERA_REQUEST=9999;
    public static final int GALLERY_REQUEST=8888;
    public EditText editText;
    OCRTess mOCRTess;
    String mCurrentPhotoPath;
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

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                        ex.printStackTrace();
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(getContext(), "com.example.android.paratranslate", photoFile);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(intent, CAMERA_REQUEST);
                    }
                }
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




        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==CAMERA_REQUEST ) {
            String myurl = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)+"/myimage1.jpg";
            Log.e("Transfrag",mCurrentPhotoPath);
            Uri contentURI =Uri.fromFile(new File(mCurrentPhotoPath));
            try {
                ImageDecoder.Source source = ImageDecoder.createSource(getActivity().getApplicationContext().getContentResolver(), contentURI);
                Bitmap bitmap = ImageDecoder.decodeBitmap(source);
                Bitmap converted = bitmap.copy(Bitmap.Config.ARGB_8888, false);
                mOCRTess = new OCRTess(getActivity().getApplicationContext(), lang_from);
                String OcrData = mOCRTess.getOCRResult(converted);
                Log.e("Fragactivity", OcrData);
                editText.setText(OcrData);
            }
            catch (IOException e) {
                e.printStackTrace();
            }

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
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );


        //File image = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES) ,"myimage1.jpg");


        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
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
