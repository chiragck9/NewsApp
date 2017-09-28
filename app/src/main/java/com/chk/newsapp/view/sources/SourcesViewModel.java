package com.chk.newsapp.view.sources;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.VisibleForTesting;

import com.chk.newsapp.BuildConfig;
import com.chk.newsapp.data.api.utils.Resource;
import com.chk.newsapp.data.datamodel.Category;
import com.chk.newsapp.data.datamodel.SourceEntity;
import com.chk.newsapp.data.repository.DataRepository;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by chira on 19-08-2017.
 */

public class SourcesViewModel extends ViewModel {

    private final LiveData<Resource<List<SourceEntity>>> sources;
    private final DataRepository mDataRepository;
    private MutableLiveData<Category> category = new MutableLiveData<>();

    @Inject
    public SourcesViewModel(DataRepository dataRepository) {
        mDataRepository = dataRepository;
        sources = Transformations.switchMap(category, source -> {
            return dataRepository.getSourcesByCategory(source, BuildConfig.NEWS_API_KEY);
        });
    }

    public void setCategory(Category category) {
        if (category == this.category.getValue()) {
            return;
        }
        this.category.setValue(category);
    }

    public LiveData<Resource<List<SourceEntity>>> getSources() {
        return sources;
    }

    public Observable<SourceEntity> setSubscriprionDetails(SourceEntity source) {
        return mDataRepository.setSourceSubscribed(source);
    }
}
