package com.alexandre.marple2.model;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "ingredient")
public class Ingredient {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "description")
    private String description;

    public Ingredient(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    @Ignore
    public Ingredient() {
    }

    @Ignore
    public Ingredient(String name, String description) {
        this.name = name;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return name;
    }
}
