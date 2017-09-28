package com.chk.newsapp.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chk.newsapp.R;
import com.chk.newsapp.data.api.utils.Resource;
import com.chk.newsapp.data.datamodel.SourceEntity;
import com.chk.newsapp.data.utils.Constants;
import com.chk.newsapp.databinding.ActivityMainBinding;
import com.chk.newsapp.view.bookmarks.BookmarksFragment;
import com.chk.newsapp.view.headlines.HeadlinesPagerFragment;
import com.chk.newsapp.view.headlines.HeadlinesViewModel;
import com.chk.newsapp.view.headlines.SelectedSourceAdapter;
import com.chk.newsapp.view.headlines.SelectedSourceFragment;
import com.chk.newsapp.view.headlines.SelectedSourceItem;
import com.chk.newsapp.view.search.SearchFragment;
import com.chk.newsapp.view.settings.SettingsFragment;
import com.chk.newsapp.view.topics.TopicsFragment;
import com.chk.newsapp.view.viewmodels.HeadlinesPagerBackPressModel;
import com.chk.newsapp.view.viewmodels.SelectedSourceViewModel;
import com.ncapdevi.fragnav.FragNavController;
import com.ncapdevi.fragnav.FragNavTransactionOptions;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

/**
 * Created by chira on 17-08-2017.
 */

public class MainActivity extends BaseActivity implements HasSupportFragmentInjector,
        BaseFragment.FragmentNavigation,
        FragNavController.TransactionListener,
        FragNavController.RootFragmentListener {

    private final int INDEX_HOME = FragNavController.TAB1;
    private final int INDEX_TOPICS = FragNavController.TAB2;
    private final int INDEX_SEARCH = FragNavController.TAB3;
    private final int INDEX_BOOKMARKS = FragNavController.TAB4;
    private final int INDEX_SETTINGS = FragNavController.TAB5;
    private static final int TOTAL_TABS = 5;

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
    @Inject
    NavigationController mNavigationController;
    @Inject
    ViewModelProvider.Factory mViewModelFactory;
    private HeadlinesPagerBackPressModel mHeadlinesPagerBackPressModel;
    private int mPosition;
    private ActivityMainBinding mBinding;
    private FragNavController mNavController;
    private FragNavTransactionOptions.Builder mFragNavBuilder;
    private String mSelectedSource;
    private ArrayList<SelectedSourceItem> mSubscribedSourceList;
    private BottomSheetDialog mBottomSheetDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mBinding.navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mBinding.navigation.setOnNavigationItemReselectedListener(mOnNavigationItemReselectedListener);
        mBinding.navigation.enableAnimation(true);
        mBinding.navigation.enableShiftingMode(false);
        mBinding.navigation.enableItemShiftingMode(false);
        mBinding.navigation.setTextVisibility(false);

        mHeadlinesPagerBackPressModel = ViewModelProviders.of(this, mViewModelFactory).get(HeadlinesPagerBackPressModel.class);
        mHeadlinesPagerBackPressModel.getCurrentPosition().observe(this, position -> {
            mPosition = position;
        });

        mNavController = FragNavController.newBuilder(savedInstanceState, getSupportFragmentManager(), R.id.container)
                .transactionListener(this)
                .rootFragmentListener(this, TOTAL_TABS)
                .build();

        mFragNavBuilder = FragNavTransactionOptions.newBuilder().customAnimations(R.anim.slide_in_right, R.anim.slide_out_left, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        mNavController.switchTab(INDEX_HOME);

        SelectedSourceViewModel selectedSourceViewModel = ViewModelProviders.of(this, mViewModelFactory).get(SelectedSourceViewModel.class);
        selectedSourceViewModel.getSelectedSource().observe(this, selectedSource -> {
            mSelectedSource = selectedSource;
        });

        HeadlinesViewModel headlinesViewModel = ViewModelProviders.of(this, mViewModelFactory).get(HeadlinesViewModel.class);
        headlinesViewModel.getSubscribedSources().observe(this, new Observer<Resource<List<SourceEntity>>>() {
            @Override
            public void onChanged(@Nullable Resource<List<SourceEntity>> listResource) {
                mSubscribedSourceList = convert(listResource.getData(), mSelectedSource);
            }
        });

        mBinding.toolbarContainer.filter.setOnClickListener(view -> {
            BottomSheetDialogFragment selectedSourceFragment = SelectedSourceFragment.create(mSubscribedSourceList);
            selectedSourceFragment.show(getSupportFragmentManager(), SelectedSourceFragment.class.getSimpleName());
//            showBottomSheetDialog();
        });
    }

    @Override
    public void onBackPressed() {
        if (!mNavController.isRootFragment()) {
            mNavController.popFragment();
        } else {
            if (mPosition != 0) {
                mHeadlinesPagerBackPressModel.setCurrentPosition(0);
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mNavController != null) {
            mNavController.onSaveInstanceState(outState);
        }
    }


    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

    @Override
    public void pushFragment(Fragment fragment) {
        if (mNavController != null) {
            mNavController.pushFragment(fragment, mFragNavBuilder.build());
        }
    }

    @Override
    public void onTabTransaction(Fragment fragment, int index) {
        // If we have a backstack, show the back button
        if (fragment instanceof HeadlinesPagerFragment) {
            mBinding.toolbarContainer.filter.setVisibility(View.VISIBLE);
            mBinding.toolbarContainer.toolbarTitle.setText(getString(R.string.app_name));
        } else if (fragment instanceof TopicsFragment) {
            mBinding.toolbarContainer.filter.setVisibility(View.GONE);
            mBinding.toolbarContainer.toolbarTitle.setText(getString(R.string.title_topics));
        } else if (fragment instanceof SearchFragment) {
            mBinding.toolbarContainer.filter.setVisibility(View.GONE);
            mBinding.toolbarContainer.toolbarTitle.setText(getString(R.string.title_search));
        } else if (fragment instanceof BookmarksFragment) {
            mBinding.toolbarContainer.filter.setVisibility(View.GONE);
            mBinding.toolbarContainer.toolbarTitle.setText(getString(R.string.title_bookmarks));
        } else if (fragment instanceof SettingsFragment) {
            mBinding.toolbarContainer.filter.setVisibility(View.GONE);
            mBinding.toolbarContainer.toolbarTitle.setText(getString(R.string.title_settings));
        } else {
            mBinding.toolbarContainer.filter.setVisibility(View.GONE);
        }
    }


    @Override
    public void onFragmentTransaction(Fragment fragment, FragNavController.TransactionType transactionType) {
    }

    @Override
    public Fragment getRootFragment(int index) {
        switch (index) {
            case INDEX_HOME:
                return HeadlinesPagerFragment.create();
            case INDEX_TOPICS:
                return TopicsFragment.create();
            case INDEX_SEARCH:
                return SearchFragment.create();
            case INDEX_BOOKMARKS:
                return BookmarksFragment.create();
            case INDEX_SETTINGS:
                return SettingsFragment.create();
        }
        throw new IllegalStateException("Need to send an index that we know");
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = item -> {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                mNavController.switchTab(INDEX_HOME);
                break;
            case R.id.navigation_topics:
                mNavController.switchTab(INDEX_TOPICS, mFragNavBuilder.build());
                break;
            case R.id.navigation_search:
                mNavController.switchTab(INDEX_SEARCH, mFragNavBuilder.build());
                break;
            case R.id.navigation_bookmarks:
                mNavController.switchTab(INDEX_BOOKMARKS, mFragNavBuilder.build());
                break;
            case R.id.navigation_settings:
                mNavController.switchTab(INDEX_BOOKMARKS, mFragNavBuilder.build());
                break;
        }
        return true;
    };
    private BottomNavigationView.OnNavigationItemReselectedListener mOnNavigationItemReselectedListener = item -> {
        if (mNavController.isRootFragment()) {
            mHeadlinesPagerBackPressModel.setCurrentPosition(0);
        } else {
            mNavController.clearStack();
        }
    };


    private ArrayList<SelectedSourceItem> convert(List<SourceEntity> data, String selectedSource) {
        if (data != null) {
            ArrayList<SelectedSourceItem> selectedSourceItems = new ArrayList<>();
            SelectedSourceItem selectedSourceItem = new SelectedSourceItem();
            selectedSourceItem.setName(Constants.COVER_STORY);
            selectedSourceItem.setSelected(Constants.COVER_STORY.equals(selectedSource));
            selectedSourceItems.add(selectedSourceItem);

            for (SourceEntity sourceEntity : data) {
                SelectedSourceItem item = new SelectedSourceItem();
                item.setId(sourceEntity.getId());
                item.setName(sourceEntity.getName());
                item.setSelected(sourceEntity.getName().equals(selectedSource));
                selectedSourceItems.add(item);
            }
            return selectedSourceItems;
        }
        return null;
    }



    private void showBottomSheetDialog() {

        mBottomSheetDialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.fragment_selected_source, null);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.selected_source_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        SelectedSourceAdapter selectedSourceAdapter = new SelectedSourceAdapter(sourceEntity -> {
//            PreferencesHelper.getInstance(getContext()).write(SELECTED_SOURCE, sourceEntity.getId());
//            selectedSourceViewModel.setSelectedSource(sourceEntity.getId());
        });
        selectedSourceAdapter.replace(mSubscribedSourceList);
        recyclerView.setAdapter(selectedSourceAdapter);

        mBottomSheetDialog.setContentView(view);
        mBottomSheetDialog.show();
        mBottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mBottomSheetDialog = null;
            }
        });
    }

}
