package com.chk.newsapp.view.topics;

import android.annotation.SuppressLint;
import android.arch.core.util.Function;
import android.arch.lifecycle.ComputableLiveData;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;

import com.chk.newsapp.BuildConfig;
import com.chk.newsapp.data.api.utils.Resource;
import com.chk.newsapp.data.datamodel.ArticleDetailEntity;
import com.chk.newsapp.data.datamodel.Category;
import com.chk.newsapp.data.datamodel.SourceEntity;
import com.chk.newsapp.data.datamodel.Topic;
import com.chk.newsapp.data.repository.DataRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by chira on 16-08-2017.
 */

public class TopicsViewModel extends ViewModel {

    private final LiveData<Resource<List<SourceEntity>>> sources;
    private MutableLiveData<List<Topic>> topics = new MutableLiveData<>();

    @Inject
    public TopicsViewModel(DataRepository dataRepository) {
        sources = dataRepository.getSources("", "en", "", BuildConfig.NEWS_API_KEY);
    }

    public LiveData<Resource<List<SourceEntity>>> getSources() {
        return sources;
    }

    public void setTopics(List<Topic> topicList) {
        topics.setValue(topicList);
    }

    public MutableLiveData<List<Topic>> getTopics() {
        return topics;
    }
}
