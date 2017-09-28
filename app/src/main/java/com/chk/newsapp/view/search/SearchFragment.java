package com.chk.newsapp.view.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chk.newsapp.view.BaseFragment;

/**
 * Created by chira on 05-09-2017.
 */

public class SearchFragment extends BaseFragment {
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

    public static SearchFragment create() {
        SearchFragment searchFragment = new SearchFragment();
        return searchFragment;
    }
}
