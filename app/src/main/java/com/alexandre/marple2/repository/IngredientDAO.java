package com.alexandre.marple2.repository;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.alexandre.marple2.model.Ingredient;

import java.util.List;

@Dao
public interface IngredientDAO {
    @Query("SELECT * FROM ingredient")
    List<Ingredient> getAll();

    @Query(value = "SELECT i.name FROM ingredient i " +
            "INNER JOIN restriction_has_ingredients ri ON i.id=ri.ingredientId " +
            "INNER JOIN restriction r ON r.id=ri.restrictionId " +
            "WHERE r.enable=1")
            List<String> getAllEnabled();

    @Query("SELECT * FROM ingredient WHERE id IN (:ingredientIds)")
    List<Ingredient> loadAllByIds(int[] ingredientIds);

    @Query("SELECT * FROM ingredient WHERE name LIKE :name LIMIT 1")
    Ingredient findByName(String name);

    @Query("DELETE FROM ingredient")
    int clean();

    @Insert
    void insertAll(Ingredient... ingredients);

    @Delete
    void delete(Ingredient ingredients);
}
