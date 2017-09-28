package com.chk.newsapp.data.mappers;

import com.chk.newsapp.data.api.model.Article;
import com.chk.newsapp.data.api.model.Headlines;
import com.chk.newsapp.data.api.model.Source;
import com.chk.newsapp.data.api.model.Sources;
import com.chk.newsapp.data.datamodel.ArticleEntity;
import com.chk.newsapp.data.datamodel.HeadlinesEntity;
import com.chk.newsapp.data.datamodel.SourceEntity;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

/**
 * Created by chira on 16-08-2017.
 */

public class ModelMapper {

    @Nullable
    public static List<SourceEntity> transform(Sources item) {
        if (item != null) {
            List<Source> sources = item.getSources();
            List<SourceEntity> sourceEntities = new ArrayList<>();
            for (Source source : sources) {
                SourceEntity sourceEntity = new SourceEntity();
                sourceEntity.setId(source.getId());
                sourceEntity.setName(source.getName());
                sourceEntity.setDescription(source.getDescription());
                sourceEntity.setUrl(source.getUrl());
                sourceEntity.setCategory(source.getCategory());
                sourceEntity.setLanguage(source.getLanguage());
                sourceEntity.setCountry(source.getCountry());
                sourceEntities.add(sourceEntity);
            }
            return sourceEntities;
        }
        return null;
    }

    public static HeadlinesEntity transformHeadlines(Headlines headlines) {
        if (headlines != null) {
            HeadlinesEntity headlinesEntity = new HeadlinesEntity();
            headlinesEntity.setStatus(headlines.getStatus());
            headlinesEntity.setArticles(transformArticles(headlines.getArticles()));
            return headlinesEntity;
        }
        return null;
    }

    public static List<ArticleEntity> transformArticles(List<Article> items) {
        if (items != null) {
            List<ArticleEntity> articleEntities = new ArrayList<>();
            int size = items.size();
            for (int i = 0; i < size; i++) {
                Article article = items.get(i);
                ArticleEntity articleEntity = new ArticleEntity();
                articleEntity.setSourceName(article.getSource().getName());
                articleEntity.setSourceId(article.getSource().getId());
                articleEntity.setAuthor(article.getAuthor());
                articleEntity.setDescription(article.getDescription());
                articleEntity.setPublishedAt(article.getPublishedAt());
                articleEntity.setTitle(article.getTitle());
                articleEntity.setUrl(article.getUrl());
                articleEntity.setUrlToImage(article.getUrlToImage());
                articleEntities.add(articleEntity);
            }
            return articleEntities;
        }
        return null;
    }

    private static SourceEntity transformSource(Source source) {
        SourceEntity sourceEntity = new SourceEntity();
        source.setId(source.getId());
        sourceEntity.setName((source.getName()));
        return sourceEntity;
    }

}
