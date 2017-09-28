package com.chk.newsapp.data.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.chk.newsapp.data.datamodel.HeadlinesEntity;
import com.chk.newsapp.data.datamodel.SourceEntity;

import java.util.List;

/**
 * Created by chira on 16-08-2017.
 */
@Dao
public interface SourceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insert(List<SourceEntity> sourceEntities);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(SourceEntity sourceEntities);

    @Query("SELECT * FROM source")
    LiveData<List<SourceEntity>> getSources();

    @Query("SELECT * FROM source where subscribed = 1")
    LiveData<List<SourceEntity>> getSubscribedSources();

    @Query("SELECT * FROM source where category LIKE :category")
    LiveData<List<SourceEntity>> getSourcesByCategory(String category);


    @Query("SELECT * FROM source where id LIKE :id")
    LiveData<SourceEntity> getSource(String id);

    @Query("UPDATE source SET subscribed = :subscribed WHERE id = :id")
    long setSubscribed(String id, boolean subscribed);

}
