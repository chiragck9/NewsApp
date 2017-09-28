package com.chk.newsapp.view.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.chk.newsapp.view.headlines.HeadlinesViewModel;
import com.chk.newsapp.view.sources.SourcesViewModel;
import com.chk.newsapp.view.topics.TopicsViewModel;
import com.chk.newsapp.view.viewmodels.HeadlinesPagerBackPressModel;
import com.chk.newsapp.view.viewmodels.SelectedSourceViewModel;
import com.chk.newsapp.view.viewmodels.ViewModelFactory;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(TopicsViewModel.class)
    abstract ViewModel bindTopicsViewModel(TopicsViewModel userViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SourcesViewModel.class)
    abstract ViewModel bindSourcesViewModel(SourcesViewModel sourcesViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(HeadlinesViewModel.class)
    abstract ViewModel bindHeadlinesViewModel(HeadlinesViewModel headlinesViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(HeadlinesPagerBackPressModel.class)
    abstract ViewModel bindBackPressViewModel(HeadlinesPagerBackPressModel headlinesViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SelectedSourceViewModel.class)
    abstract ViewModel bindSelectedSourceViewModel(SelectedSourceViewModel selectedSourceViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);
}
