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

import com.chk.newsapp.view.bookmarks.BookmarksFragment;
import com.chk.newsapp.view.headlines.HeadlinesFragment;
import com.chk.newsapp.view.headlines.HeadlinesPagerFragment;
import com.chk.newsapp.view.headlines.SelectedSourceFragment;
import com.chk.newsapp.view.search.SearchFragment;
import com.chk.newsapp.view.settings.SettingsFragment;
import com.chk.newsapp.view.sources.SourceListFragment;
import com.chk.newsapp.view.topics.TopicsFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract HeadlinesPagerFragment contributeNewsFragment();

    @ContributesAndroidInjector
    abstract HeadlinesFragment contributeHeadlinesFragment();

    @ContributesAndroidInjector
    abstract TopicsFragment contributeTopicsFragment();

    @ContributesAndroidInjector
    abstract SourceListFragment contributeSourceListFragment();

    @ContributesAndroidInjector
    abstract BookmarksFragment contributeBookmarksFragment();

    @ContributesAndroidInjector
    abstract SearchFragment contributeSearchFragment();

    @ContributesAndroidInjector
    abstract SettingsFragment contributeSettingsFragment();

    @ContributesAndroidInjector
    abstract SelectedSourceFragment contributeSelectedSourceFragment();

}
