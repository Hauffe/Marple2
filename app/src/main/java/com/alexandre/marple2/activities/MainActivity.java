package com.alexandre.marple2.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.alexandre.marple2.R;
import com.alexandre.marple2.model.Ingredient;
import com.alexandre.marple2.model.Restriction;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.w(getString(R.string.MainActivity),"initiated");
        initFirebase();
    }

    private void initFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_checkProductCam:
                openCheckProductCam();
                return true;
            case R.id.menu_restrictions:
                openMenuRestrictions();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void openCheckProductCam() {
        Intent intent = new Intent(this, CameraActivity.class);
        startActivity(intent);
    }

    private void openMenuRestrictions() {
        Intent intent = new Intent(this, RestrictionActivity.class);
        startActivity(intent);
    }
}