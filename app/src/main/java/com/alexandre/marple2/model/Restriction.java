package com.alexandre.marple2.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "restriction")
public class Restriction {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "enable")
    private boolean enable;

    @Ignore
    private List<Ingredient> ingredients;

    public Restriction(Long id, String name, boolean enable) {
        this.id = id;
        this.name = name;
        this.enable = enable;
        ingredients = new ArrayList<>();
    }

    @Ignore
    public Restriction() {
    }

    @Ignore
    public Restriction(String name) {
        this.name = name;
        ingredients = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

}
