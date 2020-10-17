package com.alexandre.marple2.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import com.alexandre.marple2.R;
import com.alexandre.marple2.model.Ingredient;
import com.alexandre.marple2.repository.db.AppDatabase;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CameraActivity extends AppCompatActivity {

    private AppDatabase db;
    List<String> allIngredients;
    SurfaceView mCameraView;
    TextView text_view;
    TextView text_unapproved;
    CameraSource mCameraSource;
    List<String> sentences;

    private static final int requestPermissionID = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        db = AppDatabase.getInstance(this);
        mCameraView = findViewById(R.id.surfaceView);
        text_view = findViewById(R.id.text_view);
        text_unapproved = findViewById(R.id.text_unapproved);

        startCameraSource();
        allIngredients = getIngredients();
        sentences = new ArrayList<>();
        sentences.add("pode conter ");
        sentences.add("sem ");
        sentences.add("não possui ");
        Log.d("ingredients", String.valueOf(allIngredients));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != requestPermissionID) {
            Log.d(getString(R.string.CameraActivity), getString(R.string.unexpectedResult)+ requestCode);
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            try {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mCameraSource.start(mCameraView.getHolder());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<String> getIngredients() {
        return db.ingredientDAO().getAllEnabled();
    }


    private void startCameraSource() {

        //Create the TextRecognizer
        final TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();

        if (!textRecognizer.isOperational()) {
            Log.w(getString(R.string.CameraActivity), "Detector dependencies not loaded yet");
        } else {

            //Initialize camerasource to use high resolution and set Autofocus on.
            mCameraSource = new CameraSource.Builder(getApplicationContext(), textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(768, 768)
                    .setAutoFocusEnabled(true)
                    .setRequestedFps(2.0f)
                    .build();

            /**
             * Add call back to SurfaceView and check if camera permission is granted.
             * If permission is granted we can start our cameraSource and pass it to surfaceView
             */
            mCameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    try {

                        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(CameraActivity.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    requestPermissionID);
                            return;
                        }
                        mCameraSource.start(mCameraView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    mCameraSource.stop();
                }
            });

            //Set the TextRecognizer's Processor.
            textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
                @Override
                public void release() {
                }

                /**
                 * Detect all the text from camera using TextBlock and the values into a stringBuilder
                 * which will then be set to the textView.
                 * */
                @Override
                public void receiveDetections(Detector.Detections<TextBlock> detections) {
                    final SparseArray<TextBlock> items = detections.getDetectedItems();
                    if (items.size() != 0 ){

                        text_view.post(new Runnable() {
                            @Override
                            public void run() {
                                StringBuilder text_view_str = new StringBuilder();
                                StringBuilder text_unapproved_str = new StringBuilder();
                                for(int i=0;i<items.size();i++){
                                    TextBlock item = items.valueAt(i);
                                    String textBlock = item.getValue().toLowerCase();
                                    if(!allIngredients.isEmpty()) {
                                        for (String restr : allIngredients) {
                                            String restrLower = restr.toLowerCase();
                                            if (textBlock.contains(restrLower)) {
                                                for (String sentence : sentences)
                                                    if (!textBlock.contains(sentence + restrLower)) {
                                                        text_unapproved_str.append(restrLower);
                                                        break;
                                                    }
                                            }
                                        }
                                    }
                                    text_view_str.append(item.getValue());
                                    text_view_str.append("\n");
                                }
                                text_view.setText(text_view_str.toString());
                                text_unapproved.setText(text_unapproved_str.toString());
                            }
                        });
                    }
                }
            });
        }
    }
}