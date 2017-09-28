package com.chk.newsapp.data.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.chk.newsapp.data.datamodel.HeadlinesEntity;

import java.util.List;

/**
 * Created by chira on 16-08-2017.
 */
@Dao
public interface HeadlinesDao {

    @Query("SELECT * FROM news_entity")
    LiveData<List<HeadlinesEntity>> getHeadlines();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertHeadlines(HeadlinesEntity headlinesEntities);

    @Query("DELETE FROM news_entity")
    int deleteHeadlines();

}
