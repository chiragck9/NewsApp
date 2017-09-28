package com.chk.newsapp.view.headlines;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.chk.newsapp.BuildConfig;
import com.chk.newsapp.data.api.utils.Resource;
import com.chk.newsapp.data.datamodel.ArticleEntity;
import com.chk.newsapp.data.datamodel.HeadlinesEntity;
import com.chk.newsapp.data.datamodel.SourceEntity;
import com.chk.newsapp.data.repository.DataRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by chira on 19-08-2017.
 */

public class HeadlinesViewModel extends ViewModel {

    private MutableLiveData<List<ArticleEntity>> articleEntities = new MutableLiveData<>();
    private MutableLiveData<SourceEntity> sourceEntity = new MutableLiveData<>();
    private LiveData<Resource<List<HeadlinesEntity>>> allSavedArticles;
    private DataRepository mDataRepository;

    @Inject
    public HeadlinesViewModel(DataRepository dataRepository) {
        mDataRepository = dataRepository;
        allSavedArticles = dataRepository.getAllHeadlinesFromStorage();
    }

    public LiveData<Resource<List<SourceEntity>>> getSubscribedSources() {
        return mDataRepository.getSubscribedSources();
    }

    public MutableLiveData<List<ArticleEntity>> getArticleEntities() {
        return articleEntities;
    }

    public void setArticleEntities(List<ArticleEntity> articleEntities) {
        this.articleEntities.setValue(articleEntities);
    }

    public LiveData<Resource<List<HeadlinesEntity>>> getAllSavedArticles() {
        return allSavedArticles;
    }

    public Observable<HeadlinesEntity> getHeadlinesForSubscribedSources(List<SourceEntity> data) {
        return mDataRepository.getHeadlinesForMultipleSources(data, BuildConfig.NEWS_API_KEY);
    }

    public void setSubscribedSource(SourceEntity sourceEntity) {
        this.sourceEntity.setValue(sourceEntity);
    }

}
