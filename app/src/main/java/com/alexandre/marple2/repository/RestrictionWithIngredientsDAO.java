package com.alexandre.marple2.repository;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.alexandre.marple2.model.Ingredient;
import com.alexandre.marple2.model.Restriction;
import com.alexandre.marple2.model.RestrictionWithIngredients;

import java.util.List;


@Dao
public interface RestrictionWithIngredientsDAO {

    @Insert
    void insert(RestrictionWithIngredients restrictionWithIngredients);

    @Delete
    void delete(RestrictionWithIngredients restrictionWithIngredients);

    @Query("DELETE FROM restriction_has_ingredients")
    int clean();

    @Query("SELECT * FROM restriction " +
            "INNER JOIN restriction_has_ingredients " +
            "ON restriction.id=restriction_has_ingredients.restrictionId " +
            "WHERE restriction_has_ingredients.ingredientId=:ingredientId")
    List<Restriction> getRestrictionsForIngredients(final Long ingredientId);

    @Query("SELECT * FROM ingredient " +
            "INNER JOIN restriction_has_ingredients " +
            "ON ingredient.id=restriction_has_ingredients.ingredientId " +
            "WHERE restriction_has_ingredients.restrictionId=:restrictionId")
    List<Ingredient> getIngredientsForRestrictions(final Long restrictionId);

}
