package com.chk.newsapp.data.executors;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by chira on 15-08-2017.
 */
@Singleton
public class MainThreadExecutor implements ThreadExecutor {

    @Inject
    public MainThreadExecutor() {
    }

    private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    @Override
    public void execute(@NonNull Runnable command) {
        mainThreadHandler.post(command);
    }
}