/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.chk.newsapp.view.di;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;

import com.chk.newsapp.data.api.NewsApi;
import com.chk.newsapp.data.api.utils.LiveDataCallAdapterFactory;
import com.chk.newsapp.data.db.NewsAppDatabase;
import com.chk.newsapp.data.db.dao.HeadlinesDao;
import com.chk.newsapp.data.db.dao.SourceDao;
import com.chk.newsapp.data.executors.AppExecutor;
import com.chk.newsapp.data.executors.MainThreadExecutor;
import com.chk.newsapp.data.goose.GooseManager;
import com.chk.newsapp.data.repository.DataRepository;
import com.chk.newsapp.data.repository.DataRepositoryImp;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(includes = ViewModelModule.class)
class AppModule {

    private final String BASE_URL = "http://beta.newsapi.org/v2/";

    @Singleton
    @Provides
    NewsApi provideNewsService() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(NewsApi.class);
    }

    @Provides
    @Singleton
    Context providesContext(Application application) {
        return application.getApplicationContext();
    }

    @Singleton
    @Provides
    NewsAppDatabase provideDb(Application app) {
        return Room.databaseBuilder(app, NewsAppDatabase.class, "news-app.db").build();
    }

    @Provides
    @Singleton
    public AppExecutor provideThreadExecutor() {
        return new AppExecutor();
    }

    @Provides
    @Singleton
    public MainThreadExecutor providePostExecutionThread() {
        return new MainThreadExecutor();
    }


    @Provides
    public GooseManager provideGooseManager(Context context) {
        return new GooseManager(context);
    }

    @Provides
    public DataRepository provideDataRepository(AppExecutor appExecutor,
                                                MainThreadExecutor mainThreadExecutor,
                                                NewsApi newsApi,
                                                NewsAppDatabase newsAppDatabase,
                                                GooseManager gooseManager) {
        return new DataRepositoryImp(appExecutor, mainThreadExecutor, newsApi, newsAppDatabase, gooseManager);
    }

}
