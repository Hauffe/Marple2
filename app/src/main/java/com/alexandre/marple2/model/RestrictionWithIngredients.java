package com.alexandre.marple2.model;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import java.io.Serializable;

@Entity(tableName = "restriction_has_ingredients",
        primaryKeys = { "restrictionId", "ingredientId" },
        foreignKeys = {
                @ForeignKey(entity = Restriction.class,
                        parentColumns = "id",
                        childColumns = "restrictionId"),
                @ForeignKey(entity = Ingredient.class,
                        parentColumns = "id",
                        childColumns = "ingredientId")
        })
public class RestrictionWithIngredients implements Serializable {

    @NonNull
    public final Long restrictionId;
    @NonNull
    public final Long ingredientId;

    public RestrictionWithIngredients(Long restrictionId, Long ingredientId) {
        this.restrictionId = restrictionId;
        this.ingredientId = ingredientId;
    }
}
