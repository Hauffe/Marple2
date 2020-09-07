package com.alexandre.marple2.repository.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.alexandre.marple2.model.Ingredient;
import com.alexandre.marple2.model.Restriction;
import com.alexandre.marple2.model.RestrictionWithIngredients;
import com.alexandre.marple2.repository.IngredientDAO;
import com.alexandre.marple2.repository.RestrictionWithIngredientsDAO;
import com.alexandre.marple2.repository.RestrictionsDAO;


@Database(entities = {
        Ingredient.class,
        Restriction.class,
        RestrictionWithIngredients.class,
    }, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract IngredientDAO ingredientDAO();
    public abstract RestrictionsDAO restrictionDAO();
    public abstract RestrictionWithIngredientsDAO restrictionWithIngredientsDAO();

    private static AppDatabase appDb;

    public static AppDatabase getInstance(Context context) {
        if(appDb == null)  appDb = buildDatabaseInstance(context);
        return appDb;
    }

    private static AppDatabase buildDatabaseInstance(Context context) {
        return Room.databaseBuilder(context,
                AppDatabase.class,
                "appdb.db")
                .allowMainThreadQueries().build();
    }

    public void cleanUp(){
        appDb = null;
    }

}
