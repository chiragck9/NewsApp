package com.chk.newsapp.view.bookmarks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chk.newsapp.view.BaseFragment;

/**
 * Created by chira on 03-09-2017.
 */

public class BookmarksFragment extends BaseFragment {
    @Override
    protected void create(Bundle savedInstanceState) {

    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return null;
    }

    @Override
    protected void activityCreated(Bundle savedInstanceState) {

    }

    public static BookmarksFragment create(){
        return new BookmarksFragment();
    }
}
