package com.chk.newsapp.data.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.chk.newsapp.data.api.NewsApi;
import com.chk.newsapp.data.api.utils.ApiResponse;
import com.chk.newsapp.data.api.utils.NetworkBoundResource;
import com.chk.newsapp.data.api.utils.Resource;
import com.chk.newsapp.data.datamodel.Category;
import com.chk.newsapp.data.db.NewsAppDatabase;
import com.chk.newsapp.data.datamodel.ArticleDetailEntity;
import com.chk.newsapp.data.datamodel.HeadlinesEntity;
import com.chk.newsapp.data.datamodel.SourceEntity;
import com.chk.newsapp.data.executors.AppExecutor;
import com.chk.newsapp.data.executors.MainThreadExecutor;
import com.chk.newsapp.data.api.model.Headlines;
import com.chk.newsapp.data.api.model.Sources;
import com.chk.newsapp.data.goose.GooseManager;
import com.chk.newsapp.data.mappers.ModelMapper;
import com.chk.newsapp.data.utils.AbsentLiveData;
import com.chk.newsapp.data.utils.CollectionsUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import me.angrybyte.goose.Article;

/**
 * Created by chira on 15-08-2017.
 */

@Singleton
public class DataRepositoryImp implements DataRepository {

    private AppExecutor mAppExecutor;
    private MainThreadExecutor mMainThreadExecutor;
    private NewsApi mNewsApi;
    private NewsAppDatabase mNewsAppDatabase;
    private GooseManager mGooseManager;

    @Inject
    public DataRepositoryImp(AppExecutor appExecutor,
                             MainThreadExecutor mainThreadExecutor,
                             NewsApi newsApi,
                             NewsAppDatabase newsAppDatabase,
                             GooseManager gooseManager) {
        this.mAppExecutor = appExecutor;
        this.mMainThreadExecutor = mainThreadExecutor;
        this.mNewsApi = newsApi;
        this.mNewsAppDatabase = newsAppDatabase;
        this.mGooseManager = gooseManager;
    }


    @NotNull
    @Override
    public LiveData<Resource<List<HeadlinesEntity>>> getAllHeadlinesFromStorage() {
        return new NetworkBoundResource<List<HeadlinesEntity>, List<HeadlinesEntity>>(mAppExecutor, mMainThreadExecutor) {
            @Override
            protected void saveCallResult(List<HeadlinesEntity> item) {
            }

            @Override
            protected boolean shouldFetch(@Nullable List<HeadlinesEntity> data) {
                return false;
            }

            @NotNull
            @Override
            protected LiveData<List<HeadlinesEntity>> loadFromDb() {
                return mNewsAppDatabase.headlinesDao().getHeadlines();
            }

            @NotNull
            @Override
            protected LiveData<ApiResponse<List<HeadlinesEntity>>> createCall() {
                return null;
            }
        }.getAsLiveData();
    }

    @NotNull
    @Override
    public LiveData<Resource<List<SourceEntity>>> getSources(@NotNull String category, @NotNull String language, @NotNull String country, String apiKey) {
        return new NetworkBoundResource<List<SourceEntity>, Sources>(mAppExecutor, mMainThreadExecutor) {

            @NotNull
            @Override
            protected LiveData<ApiResponse<Sources>> createCall() {
                return mNewsApi.getSources(category, language, country, apiKey);
            }

            @NotNull
            @Override
            protected LiveData<List<SourceEntity>> loadFromDb() {
                return mNewsAppDatabase.sourceDao().getSources();
            }

            @Override
            protected boolean shouldFetch(@Nullable List<SourceEntity> data) {
                return CollectionsUtils.isEmpty(data);
            }

            @Override
            protected void saveCallResult(Sources item) {
                mNewsAppDatabase.sourceDao().insert(ModelMapper.transform(item));
            }
        }.getAsLiveData();
    }

    @NotNull
    @Override
    public LiveData<Resource<List<SourceEntity>>> getSubscribedSources() {
        return new NetworkBoundResource<List<SourceEntity>, Sources>(mAppExecutor, mMainThreadExecutor) {

            @NotNull
            @Override
            protected LiveData<ApiResponse<Sources>> createCall() {
                return AbsentLiveData.create();
            }

            @NotNull
            @Override
            protected LiveData<List<SourceEntity>> loadFromDb() {
                return mNewsAppDatabase.sourceDao().getSubscribedSources();
            }

            @Override
            protected boolean shouldFetch(@Nullable List<SourceEntity> data) {
                return false;
            }

            @Override
            protected void saveCallResult(Sources item) {

            }
        }.getAsLiveData();
    }

    @NotNull
    @Override
    public LiveData<Resource<ArticleDetailEntity>> getArticleDetail(@NotNull String url) {
        return new NetworkBoundResource<ArticleDetailEntity, Article>(mAppExecutor, mMainThreadExecutor) {
            @NotNull
            @Override
            protected LiveData<ApiResponse<Article>> createCall() {
                return mGooseManager.getArticle(url);
            }

            @NotNull
            @Override
            protected LiveData<ArticleDetailEntity> loadFromDb() {
                return AbsentLiveData.create();
            }

            @Override
            protected boolean shouldFetch(@Nullable ArticleDetailEntity data) {
                return true;
            }

            @Override
            protected void saveCallResult(Article item) {

            }
        }.getAsLiveData();
    }

    @NotNull
    @Override
    public LiveData<Resource<List<SourceEntity>>> getSourcesByCategory(@NotNull Category category, String apikey) {
        return new NetworkBoundResource<List<SourceEntity>, Sources>(mAppExecutor, mMainThreadExecutor) {

            @NotNull
            @Override
            protected LiveData<ApiResponse<Sources>> createCall() {
                return mNewsApi.getSources(category.getCategory(), "", "", apikey);
            }

            @NotNull
            @Override
            protected LiveData<List<SourceEntity>> loadFromDb() {
                return mNewsAppDatabase.sourceDao().getSourcesByCategory(category.getCategory());
            }

            @Override
            protected boolean shouldFetch(@Nullable List<SourceEntity> data) {
                return CollectionsUtils.isEmpty(data);
            }

            @Override
            protected void saveCallResult(Sources item) {
            }
        }.getAsLiveData();
    }

    @Override
    public Observable<SourceEntity> setSourceSubscribed(@NotNull SourceEntity source) {
        return Observable.create(new ObservableOnSubscribe<SourceEntity>() {
            @Override
            public void subscribe(ObservableEmitter<SourceEntity> e) throws Exception {
                long row = mNewsAppDatabase.sourceDao().setSubscribed(source.getId(), source.isSubscribed());
                if (row != -1) {
                    e.onNext(source);
                    e.onComplete();
                } else {
                    e.onError(new Throwable("Error setting subscription state to database."));
                }
            }
        }).subscribeOn(io.reactivex.schedulers.Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @NotNull
    @Override
    public Observable<Integer> deleteHeadlines() {
        return Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                int row = mNewsAppDatabase.headlinesDao().deleteHeadlines();
                if (row > 1) {
                    e.onNext(row);
                    e.onComplete();
                } else {
                    e.onError(new Throwable("Error deleting headlines from database."));
                }
            }
        }).subscribeOn(io.reactivex.schedulers.Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @NotNull
    @Override
    public Observable<HeadlinesEntity> getHeadlinesForMultipleSources(@NotNull List<? extends SourceEntity> subscribedSources, @NotNull String newsApiKey) {
        Collections.shuffle(subscribedSources);
        return Observable.just(subscribedSources).flatMapIterable(ids -> ids)
                .buffer(5)
                .flatMap(new Function<List<? extends SourceEntity>, ObservableSource<Headlines>>() {
                    @Override
                    public ObservableSource<Headlines> apply(List<? extends SourceEntity> sourceEntities) throws Exception {
                        String sources = getCommaSeparatedSources(sourceEntities);
                        return mNewsApi.getHeadlinesObservable(sources, newsApiKey);
                    }
                }).flatMap(new Function<Headlines, Observable<HeadlinesEntity>>() {
                    @Override
                    public Observable<HeadlinesEntity> apply(Headlines headlines) throws Exception {
                        if (headlines != null) {
                            HeadlinesEntity headlinesEntity = ModelMapper.transformHeadlines(headlines);
                            mNewsAppDatabase.headlinesDao().insertHeadlines(headlinesEntity);
                            return Observable.just(headlinesEntity);
                        }
                        return null;
                    }
                }).subscribeOn(io.reactivex.schedulers.Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @NonNull
    private String getCommaSeparatedSources(List<? extends SourceEntity> sourceEntities) {
        StringBuilder stringBuilder = new StringBuilder();
        int size = sourceEntities.size();
        for (int i = 0; i < size; i++) {
            stringBuilder.append(sourceEntities.get(i).getId());
            if (i != size - 1) {
                stringBuilder.append(",");
            }
        }
        return stringBuilder.toString();
    }
}
