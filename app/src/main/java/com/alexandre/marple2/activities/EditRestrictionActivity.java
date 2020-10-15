package com.alexandre.marple2.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alexandre.marple2.R;
import com.alexandre.marple2.model.Ingredient;
import com.alexandre.marple2.model.Restriction;
import com.alexandre.marple2.model.RestrictionWithIngredients;
import com.alexandre.marple2.repository.db.AppDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EditRestrictionActivity extends AppCompatActivity {

    private Restriction restriction;
    private TextView restriction_name_txt;
    private TextView ingredients_text;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_restriction);

        db = AppDatabase.getInstance(this);

        restriction = (Restriction) getIntent().getSerializableExtra("restriction");

        restriction_name_txt = findViewById(R.id.restricionNameTxt);
        ingredients_text = findViewById(R.id.ingredientsText);

        restriction_name_txt.setText(restriction.getName());
        ingredients_text.setText(restriction.getIngredientsString());
    }

    public void delete_restriction(View view) {
        deleteRestriction(restriction);

        Toast.makeText(this,
                "Restrição deletada",
                Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, RestrictionActivity.class);
        startActivity(intent);
    }

    public void update_restriction(View view) {
        List<String> ingredient_names = Arrays.asList(ingredients_text.getText().toString().split(", "));
        List<Ingredient> ingredients = new ArrayList<>();
        for(String ingName : ingredient_names){
            ingredients.add(new Ingredient(ingName, null));
        }
        restriction.setIngredients(ingredients);
        restriction.setName(restriction_name_txt.getText().toString());
        updateAllRestriction(restriction);

        Toast.makeText(this,
                "Restrição atualizada",
                Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, RestrictionActivity.class);
        startActivity(intent);
    }

    public void updateAllRestriction(Restriction restriction) {
        restriction = updateRestriction(restriction);
        deleteRestrictionIngredients(restriction);
        for (Ingredient ingredient : restriction.getIngredients()) {
            ingredient = verifyIngredients(ingredient);
            verifyRestrictionWithIngredient(restriction.getId(), ingredient.getId());
        }
    }

    public void deleteRestriction(Restriction restriction){
            deleteRestrictionIngredients(restriction);
            db.restrictionDAO().delete(restriction);
    }

    public void deleteRestrictionIngredients(Restriction restriction){
        List<Ingredient> ingredients = db.restrictionWithIngredientsDAO().getIngredientsForRestrictions(restriction.getId());
        for(Ingredient ingredient : ingredients){
            db.restrictionWithIngredientsDAO().delete(new RestrictionWithIngredients(restriction.getId(), ingredient.getId()));
        }
    }

    public Restriction updateRestriction(Restriction restriction){
            db.restrictionDAO().update(restriction);
            return restriction;
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