package com.alexandre.marple2.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.alexandre.marple2.R;
import com.alexandre.marple2.activities.adapters.RestrictionsAdapter;
import com.alexandre.marple2.model.Ingredient;
import com.alexandre.marple2.model.Restriction;
import com.alexandre.marple2.model.RestrictionWithIngredients;
import com.alexandre.marple2.repository.db.AppDatabase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RestrictionActivity extends AppCompatActivity {

    private RestrictionsAdapter adapter;
    private RecyclerView recyclerView;
    private List<Restriction> restrictions = new ArrayList<>();
    private AppDatabase db;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restriction);

        db = AppDatabase.getInstance(this);
        restrictions = populateRestrictions();

        initFirebase();
        generateDataList(restrictions);
    }

    private void generateDataList(List<Restriction> restrictions){
        recyclerView = findViewById(R.id.lstRestrictions);
        adapter = new RestrictionsAdapter(this, restrictions);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
    }

    public void download_all(View view) {
        databaseReference.child("Restriction").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                restrictions.clear();
                for(DataSnapshot objSnapshot: dataSnapshot.getChildren()){
                    Restriction restriction_fb = objSnapshot.getValue(Restriction.class);
                    restrictions.add(restriction_fb);
                    for(Restriction restriction : restrictions){
                        restriction.setId(null);
                        for(Ingredient ingredient : restriction.getIngredients()){
                            ingredient.setId(null);
                        }
                    }
                    saveAllRestriction(restrictions);
                    generateDataList(populateRestrictions());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void initFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    public void saveAllRestriction(List<Restriction> restrictions){
        for(Restriction restriction : restrictions){
            restriction = verifyRestriction(restriction);
            for(Ingredient ingredient: restriction.getIngredients()){
                ingredient = verifyIngredients(ingredient);
                verifyRestrictionWithIngredient(restriction.getId(), ingredient.getId());
            }
        }
    }

    public Restriction verifyRestriction(Restriction restriction){
        if(db.restrictionDAO().findByName(restriction.getName()) == null){
            db.restrictionDAO().insertAll(restriction);
            restriction.setId(db.restrictionDAO().findByName(restriction.getName()).getId());
            return restriction;
        }else
            return db.restrictionDAO().findByName(restriction.getName());
    }

    public Ingredient verifyIngredients(Ingredient ingredient) {
        if (db.ingredientDAO().findByName(ingredient.getName()) == null) {
            db.ingredientDAO().insertAll(ingredient);
            ingredient.setId(db.ingredientDAO().findByName(ingredient.getName()).getId());
            return ingredient;
        } else
            return db.ingredientDAO().findByName(ingredient.getName());
    }

    public void verifyRestrictionWithIngredient(Long restrictionId, Long ingredientId){
        boolean flag = true;
        List<Restriction> relation = db.restrictionWithIngredientsDAO().getRestrictionsForIngredients(ingredientId);
        for(Restriction restriction : relation){
            if(restriction.getId() == restrictionId){
                flag = false;
                break;
            }
        }
        if(flag){
            db.restrictionWithIngredientsDAO()
                    .insert(new RestrictionWithIngredients(restrictionId, ingredientId));
        }
    }

    public List<Restriction> populateRestrictions(){
        List<Restriction> restrictions = db.restrictionDAO().getAll();
        for(Restriction restriction : restrictions){
            List<Ingredient> ingredients = db.restrictionWithIngredientsDAO()
                    .getIngredientsForRestrictions(restriction.getId());
            restriction.setIngredients(ingredients);
        }
        return restrictions;
    }
}