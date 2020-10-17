package com.alexandre.marple2.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alexandre.marple2.R;
import com.alexandre.marple2.activities.adapters.RestrictionsAdapter;
import com.alexandre.marple2.model.Ingredient;
import com.alexandre.marple2.model.Restriction;
import com.alexandre.marple2.model.RestrictionWithIngredients;
import com.alexandre.marple2.repository.db.AppDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NewRestrictionActivity extends AppCompatActivity {

    private TextView restriction_name_txt;
    private TextView ingredients_text;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_restriction);

        db = AppDatabase.getInstance(this);

        restriction_name_txt = findViewById(R.id.restricionNameTxt);
        ingredients_text = findViewById(R.id.ingredientsText);


    }

    public void save_restriction(View view) {
        Restriction restriction = new Restriction(restriction_name_txt.getText().toString());
        restriction.setEnable(true);
        List<String> ingredient_names = Arrays.asList(ingredients_text.getText().toString().split(", "));
        List<Ingredient> ingredients = new ArrayList<>();
        if(!ingredient_names.isEmpty()){
            for(String ingName : ingredient_names){
                ingredients.add(new Ingredient(ingName, null));
            }
        }
        if(!restriction.getName().equals("")){
            restriction.setIngredients(ingredients);
            saveAllRestriction(restriction);

            Toast.makeText(this,
                    "Restrição salva",
                    Toast.LENGTH_SHORT).show();
        }

        Intent intent = new Intent(this, RestrictionActivity.class);
        startActivity(intent);
    }

    public void saveAllRestriction(Restriction restriction) {
        restriction = verifyRestriction(restriction);
        for (Ingredient ingredient : restriction.getIngredients()) {
            ingredient = verifyIngredients(ingredient);
            verifyRestrictionWithIngredient(restriction.getId(), ingredient.getId());
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
}