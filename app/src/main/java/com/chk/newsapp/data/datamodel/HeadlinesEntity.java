package com.chk.newsapp.data.datamodel;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.chk.newsapp.data.db.converters.ArticleConverter;

import java.util.List;

/**
 * Created by chira on 17-08-2017.
 */

@Entity(tableName = "news_entity")
@TypeConverters(ArticleConverter.class)
public class HeadlinesEntity {

    private String status;
    @PrimaryKey
    private List<ArticleEntity> articles;

    public HeadlinesEntity() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ArticleEntity> getArticles() {
        return articles;
    }

    public void setArticles(List<ArticleEntity> articles) {
        this.articles = articles;
    }
}
