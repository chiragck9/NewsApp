package com.chk.newsapp.view.headlines;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chk.newsapp.R;
import com.chk.newsapp.data.api.utils.Status;
import com.chk.newsapp.data.datamodel.ArticleEntity;
import com.chk.newsapp.data.datamodel.HeadlinesEntity;
import com.chk.newsapp.data.db.PreferencesHelper;
import com.chk.newsapp.databinding.FragmentHeadlinesPagerBinding;
import com.chk.newsapp.view.BaseFragment;
import com.chk.newsapp.view.customcomponents.AutoClearedValue;
import com.chk.newsapp.view.customcomponents.FragmentDataBindingComponent;
import com.chk.newsapp.view.viewmodels.HeadlinesPagerBackPressModel;
import com.chk.newsapp.view.viewmodels.SelectedSourceViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import me.kaelaela.verticalviewpager.transforms.StackTransformer;
import timber.log.Timber;

import static com.chk.newsapp.data.db.PreferencesHelper.SELECTED_SOURCE;
import static com.chk.newsapp.data.utils.Constants.COVER_STORY;

/**
 * Created by chira on 15-08-2017.
 */

public class HeadlinesPagerFragment extends BaseFragment {


    @Inject
    ViewModelProvider.Factory mViewModelFactory;
    private android.databinding.DataBindingComponent mDataBindingComponent = new FragmentDataBindingComponent(this);
    private AutoClearedValue<FragmentHeadlinesPagerBinding> mBinding;
    private AutoClearedValue<HeadlinesPagerAdapter> mHeadlinesPagerAdapter;
    private HeadlinesViewModel mHeadlinesViewModel;
    private HeadlinesPagerBackPressModel mHeadlinesPagerBackPressModel;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private AutoClearedValue<List<HeadlinesEntity>> mHeadlinesEntities;
    private AutoClearedValue<List<ArticleEntity>> mSelectedSourceArticleEntities;
    private AutoClearedValue<String> mSelectedSource;

    @Override
    protected void create(Bundle savedInstanceState) {

    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentHeadlinesPagerBinding fragmentTopicsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_headlines_pager, container, false, mDataBindingComponent);
        mBinding = new AutoClearedValue<>(this, fragmentTopicsBinding);
        return fragmentTopicsBinding.getRoot();
    }

    @Override
    protected void activityCreated(Bundle savedInstanceState) {

        mHeadlinesViewModel = ViewModelProviders.of(getActivity(), mViewModelFactory).get(HeadlinesViewModel.class);

        // ----------------- Back press on non zerp position in pager.---------------------------------------------
        mHeadlinesPagerBackPressModel = ViewModelProviders.of(getActivity(), mViewModelFactory).get(HeadlinesPagerBackPressModel.class);
        mHeadlinesPagerBackPressModel.getCurrentPosition().observe(this, position -> {
            if (position == 0) {
                mBinding.get().headlinesPager.setCurrentItem(0);
            }
        });
        // ----------------- Back press on non zerp position in pager.---------------------------------------------

        // ----------------- Selected source in home screen---------------------------------------------
        SelectedSourceViewModel selectedSourceViewModel = ViewModelProviders.of(getActivity(), mViewModelFactory).get(SelectedSourceViewModel.class);
        selectedSourceViewModel.getSelectedSource().observe(getActivity(), selectedSource -> {
            mSelectedSource = new AutoClearedValue<>(this, selectedSource);
            if (mHeadlinesEntities!=null && mHeadlinesEntities.get().size() > 0) {
                mSelectedSourceArticleEntities =
                        new AutoClearedValue<>(HeadlinesPagerFragment.this,
                                extractArticlesForSource(mHeadlinesEntities.get(), mSelectedSource.get()));
                mHeadlinesViewModel.setArticleEntities(mSelectedSourceArticleEntities.get());
                mHeadlinesPagerAdapter.get().setArticleEntities(mSelectedSourceArticleEntities.get());
            }
        });
        String source = PreferencesHelper.getInstance(getContext()).read(SELECTED_SOURCE);
        if (source == null) {
            PreferencesHelper.getInstance(getContext()).write(SELECTED_SOURCE, COVER_STORY);
            selectedSourceViewModel.setSelectedSource(COVER_STORY);
        } else {
            selectedSourceViewModel.setSelectedSource(source);
        }
        // ----------------- Selected source in home screen---------------------------------------------

        //Get articles from DB
        getHeadlinesFromStorage();

        //Download new articles.
        getHeadlinesForSubscribedSources();
    }

    @Override
    public void onStop() {
        super.onStop();
        mCompositeDisposable.clear();
    }

    private void getHeadlinesForSubscribedSources() {
        mHeadlinesViewModel.getSubscribedSources().observe(getActivity(), subscribedSourcesResource -> {
            if (subscribedSourcesResource != null && subscribedSourcesResource.getStatus() == Status.SUCCESS) {
                if (subscribedSourcesResource.getData() != null && subscribedSourcesResource.getData().size() > 0) {
                    mCompositeDisposable.add(mHeadlinesViewModel.getHeadlinesForSubscribedSources(subscribedSourcesResource.getData()).subscribe(new Consumer<HeadlinesEntity>() {
                        @Override
                        public void accept(HeadlinesEntity articleEntities) throws Exception {
                            mHeadlinesEntities.get().add(articleEntities);
                        }


                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Timber.i("Articles not found. ");
                        }
                    }, new Action() {
                        @Override
                        public void run() throws Exception {
                            if (mHeadlinesEntities.get().size() > 0) {
                                mSelectedSourceArticleEntities =
                                        new AutoClearedValue<>(HeadlinesPagerFragment.this,
                                                extractArticlesForSource(mHeadlinesEntities.get(), mSelectedSource.get()));
                                mHeadlinesViewModel.setArticleEntities(mSelectedSourceArticleEntities.get());
                                mHeadlinesPagerAdapter.get().setArticleEntities(mSelectedSourceArticleEntities.get());
                            }
                        }
                    }));
                }
            }
        });
    }

    private void getHeadlinesFromStorage() {
        mHeadlinesViewModel.getAllSavedArticles().observe(this, headlinesEntityResource -> {
            if (headlinesEntityResource != null) {
                if (headlinesEntityResource.getStatus() == Status.LOADING) {
                    mBinding.get().setResource(headlinesEntityResource);
                } else if (headlinesEntityResource.getStatus() == Status.ERROR) {
                    mBinding.get().setResource(headlinesEntityResource);
                } else {
                    List<HeadlinesEntity> headlinesEntities = headlinesEntityResource.getData();
                    mHeadlinesEntities = new AutoClearedValue<>(this, headlinesEntities);
                    mSelectedSourceArticleEntities = new AutoClearedValue<>(this, extractArticlesForSource(mHeadlinesEntities.get(), mSelectedSource.get()));

                    mBinding.get().setResource(headlinesEntityResource);
                    mHeadlinesViewModel.setArticleEntities(mSelectedSourceArticleEntities.get());

                    mBinding.get().headlinesPager.setPageTransformer(true, new StackTransformer());
                    mHeadlinesPagerAdapter = new AutoClearedValue<HeadlinesPagerAdapter>(this, new HeadlinesPagerAdapter(getChildFragmentManager(), mSelectedSourceArticleEntities.get()));
                    mBinding.get().headlinesPager.setAdapter(mHeadlinesPagerAdapter.get());

                    mBinding.get().headlinesPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        }

                        @Override
                        public void onPageSelected(int position) {
                            mHeadlinesPagerBackPressModel.setCurrentPosition(position);
                            if (position == 0) {
                                mBinding.get().swipeToRefreshLayout.setEnabled(true);
                            } else {
                                mBinding.get().swipeToRefreshLayout.setEnabled(false);
                            }
                        }

                        @Override
                        public void onPageScrollStateChanged(int state) {

                        }
                    });
                    mBinding.get().swipeToRefreshLayout.setOnRefreshListener(() ->
                            getHeadlinesForSubscribedSources());
                    mBinding.get().swipeToRefreshLayout.setColorSchemeResources(
                            android.R.color.holo_green_light,
                            android.R.color.holo_blue_bright,
                            android.R.color.holo_orange_light,
                            android.R.color.holo_red_light);
                }
            }
        });
    }

    private static List<ArticleEntity> extractArticlesForSource(List<HeadlinesEntity> articles, String source) {
        List<ArticleEntity> articleEntities = new ArrayList<>();
        if (COVER_STORY.equals(source)) {
            for (HeadlinesEntity headlinesEntity : articles) {
                for (ArticleEntity articleEntity : headlinesEntity.getArticles()) {
                    articleEntities.add(articleEntity);
                }
            }
        } else {
            for (HeadlinesEntity headlinesEntity : articles) {
                for (ArticleEntity articleEntity : headlinesEntity.getArticles()) {
                    if (source.equals(articleEntity.getSourceId()))
                        articleEntities.add(articleEntity);
                }
            }
        }

        return articleEntities;
    }

    public static HeadlinesPagerFragment create() {
        return new HeadlinesPagerFragment();
    }

    private class HeadlinesPagerAdapter extends FragmentStatePagerAdapter {

        private List<ArticleEntity> mArticleEntities;

        public HeadlinesPagerAdapter(FragmentManager fm, List<ArticleEntity> articleEntities) {
            super(fm);
            this.mArticleEntities = articleEntities;
        }

        @Override
        public Fragment getItem(int position) {
            return HeadlinesFragment.create(position);
        }

        @Override
        public int getCount() {
            return (mArticleEntities != null) ? mArticleEntities.size() : 0;
        }


        public void setArticleEntities(List<ArticleEntity> articleEntity) {
            mArticleEntities = articleEntity;
            notifyDataSetChanged();
        }
    }

}
