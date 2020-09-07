package com.alexandre.marple2.Repository;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;


import com.alexandre.marple2.model.Restriction;

import java.util.List;


@Dao
public interface RestrictionsDAO {

    @Query("SELECT * FROM restriction")
    List<Restriction> getAll();

    @Query("SELECT * FROM restriction WHERE id IN (:restrictionIds)")
    List<Restriction> loadAllByIds(int[] restrictionIds);

    @Query("SELECT * FROM restriction WHERE name LIKE :name LIMIT 1")
    Restriction findByName(String name);

    @Query("DELETE FROM restriction")
    int clean();

    @Insert
    void insertAll(Restriction... restrictions);

    @Delete
    void delete(Restriction restrictions);
}
