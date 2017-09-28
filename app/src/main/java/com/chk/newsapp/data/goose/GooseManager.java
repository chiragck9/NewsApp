package com.chk.newsapp.data.goose;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ComputableLiveData;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import com.chk.newsapp.data.api.utils.ApiResponse;

import javax.inject.Inject;
import javax.inject.Singleton;

import me.angrybyte.goose.Article;
import me.angrybyte.goose.Configuration;
import me.angrybyte.goose.ContentExtractor;
import retrofit2.Response;

/**
 * Created by chira on 17-08-2017.
 */
@Singleton
public class GooseManager {

    private final ContentExtractor mExtractor;

    @Inject
    public GooseManager(Context context) {
        Configuration config = new Configuration(context.getFilesDir().getAbsolutePath());
        mExtractor = new ContentExtractor(config);
    }

    @SuppressLint("RestrictedApi")
    public LiveData<ApiResponse<Article>> getArticle(String url) {
        return new ComputableLiveData<ApiResponse<Article>>() {

            @Override
            protected ApiResponse<Article> compute() {
                Article article = mExtractor.extractContent(url);
                if (article == null) {
                    return new ApiResponse<Article>(null, new Throwable("Aricle not found by Goose."));
                } else {
                    return new ApiResponse<Article>(Response.success(article), null);
                }
            }
        }.getLiveData();
    }

    public void releaseResources() {
        mExtractor.releaseResources();
    }

}
