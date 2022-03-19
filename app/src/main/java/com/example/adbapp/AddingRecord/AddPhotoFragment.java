package com.example.adbapp.AddingRecord;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TimePicker;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;

import com.example.adbapp.ContentView;
import com.example.adbapp.R;

import java.util.Calendar;
import java.util.Date;

public class AddPhotoFragment extends AddRecordFragment{

    ImageView imageView;
    ImageButton buttonGallery;

    public AddPhotoFragment(int _type) {
        super(_type);
    }

    @Override
    protected void setStringAddContent() {
        stringAddContent = "Добавить новое фото";
    }

    @Override
    protected void initContainer() {
        imageView = viewAddidable.findViewById(R.id.imageView);
        buttonGallery = viewAddidable.findViewById(R.id.buttonGallery);
        setStringAddContent();
        saveButton.setText(stringAddContent);
        saveButton.setEnabled(false);
    }

    @Override
    protected void setListener() {
        Calendar c = Calendar.getInstance();
        Date date = c.getTime();
        foundList.ActionOfSearch(ContentView.getTimeForTimes(date));
        name_ToAdd = ContentView.getTimeForTimes(date);

        buttonGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                intent.setType("image/*");
                //intent.setAction(Intent.ACTION_GET_CONTENT);
                //startActivity(Intent.createChooser(intent,"Выберите фото"));
                mStartForResult.launch(intent);
            }
        });

    }

    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK && result.getData() != null){
                        Intent intent = result.getData();
                        Uri selImage = intent.getData();
                        imageView.setImageURI(selImage);

                        foundList.ActionOfSearch(String.valueOf(selImage));
                        name_ToAdd = String.valueOf(selImage);
                        saveButton.setEnabled(true);

                        Log.d(TAG, "onActivityResult: URI="+String.valueOf(selImage));
                    }else
                        saveButton.setEnabled(false);
                }
            });
}
