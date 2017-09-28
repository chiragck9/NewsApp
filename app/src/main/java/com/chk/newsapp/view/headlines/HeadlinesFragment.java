package com.chk.newsapp.view.headlines;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chk.newsapp.R;
import com.chk.newsapp.data.api.utils.Resource;
import com.chk.newsapp.data.datamodel.ArticleEntity;
import com.chk.newsapp.data.datamodel.HeadlinesEntity;
import com.chk.newsapp.data.datamodel.SourceEntity;
import com.chk.newsapp.data.utils.DatePattern;
import com.chk.newsapp.databinding.FragmentHeadlinesBinding;
import com.chk.newsapp.databinding.FragmentHeadlinesPagerBinding;
import com.chk.newsapp.view.BaseFragment;
import com.chk.newsapp.view.customcomponents.AutoClearedValue;
import com.chk.newsapp.view.customcomponents.FragmentDataBindingComponent;
import com.chk.newsapp.view.headlines.HeadlinesViewModel;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by chira on 15-08-2017.
 */

public class HeadlinesFragment extends BaseFragment {


    private static final String POSITION = "position";

    @Inject
    ViewModelProvider.Factory mViewModelFactory;
    private android.databinding.DataBindingComponent mDataBindingComponent = new FragmentDataBindingComponent(this);
    private AutoClearedValue<FragmentHeadlinesBinding> mBinding;
    private HeadlinesViewModel mHeadlinesViewModel;

    @Override
    protected void create(Bundle savedInstanceState) {
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentHeadlinesBinding fragmentTopicsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_headlines, container, false, mDataBindingComponent);
        mBinding = new AutoClearedValue<>(this, fragmentTopicsBinding);
        return fragmentTopicsBinding.getRoot();
    }

    @Override
    protected void activityCreated(Bundle savedInstanceState) {
        mHeadlinesViewModel = ViewModelProviders.of(getActivity(), mViewModelFactory).get(HeadlinesViewModel.class);

        if (getArguments() != null) {
            int position = getArguments().getInt(POSITION);
            mHeadlinesViewModel.getArticleEntities().observe(this, articleEntities -> {
                AutoClearedValue<ArticleEntity> article = new AutoClearedValue<ArticleEntity>(this, articleEntities.get(position));
                mBinding.get().setArticle(article.get());

                String publishedAt = parsePublishedDate(article.get().getPublishedAt());
                String author = article.get().getAuthor();

                StringBuilder result = new StringBuilder();
                if (author != null) {
                    result.append(author);
                    if (publishedAt != null) {
                        result.append(" | ").append(publishedAt);
                    }
                    mBinding.get().authorAndTime.setVisibility(View.VISIBLE);
                    mBinding.get().authorAndTime.setText(result.toString());
                } else if (publishedAt != null) {
                    result.append(publishedAt);
                    mBinding.get().authorAndTime.setVisibility(View.VISIBLE);
                    mBinding.get().authorAndTime.setText(result.toString());
                } else {
                    mBinding.get().authorAndTime.setVisibility(View.GONE);
                }

            });
        }
    }

    private String parsePublishedDate(String publishedAt) {
        try {
            Date now = new Date();
            Date published = DatePattern.PUBLISHED.format(publishedAt);
            long millis = now.getTime() - published.getTime();
            long minutes = (millis / 1000) / 60;
            if (minutes >= 60) {
                long hours = minutes / 60;
                if (hours >= 24) {
                    long days = hours / 24;
                    return days + "d";
                }
                return hours + "h";
            } else {
                return minutes + "h";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static HeadlinesFragment create(int position) {
        HeadlinesFragment headlinesFragment = new HeadlinesFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(POSITION, position);
        headlinesFragment.setArguments(bundle);
        return headlinesFragment;
    }
}
