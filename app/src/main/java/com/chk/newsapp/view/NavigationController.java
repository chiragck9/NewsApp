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

package com.chk.newsapp.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.chk.newsapp.R;
import com.chk.newsapp.data.api.model.Headlines;
import com.chk.newsapp.data.datamodel.Topic;
import com.chk.newsapp.view.headlines.HeadlinesFragment;
import com.chk.newsapp.view.headlines.HeadlinesPagerFragment;
import com.chk.newsapp.view.sources.SourceListFragment;
import com.chk.newsapp.view.topics.TopicsFragment;

import javax.inject.Inject;

/**
 * A utility class that handles navigation in {@link MainActivity}.
 */
public class NavigationController {
    private final int containerId;
    private final FragmentManager fragmentManager;

    @Inject
    public NavigationController(MainActivity mainActivity) {
        this.containerId = R.id.container;
        this.fragmentManager = mainActivity.getSupportFragmentManager();
    }

    public void popStackInclusive(String tag) {
        fragmentManager.popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    public void navigateToHeadlinesPager() {
        Fragment fragment = fragmentManager.findFragmentByTag(HeadlinesPagerFragment.class.getSimpleName());
        if (fragment == null) {
            fragment = HeadlinesPagerFragment.create();
        }
        fragmentManager.beginTransaction()
                .add(containerId, fragment, HeadlinesPagerFragment.class.getSimpleName())
                .commit();
    }

    public void navigateToHeadlines(int position) {
        fragmentManager.beginTransaction()
                .replace(containerId, HeadlinesFragment.create(position), HeadlinesFragment.class.getSimpleName())
                .commit();
    }

    public void navigateToTopics() {
        Fragment fragment = fragmentManager.findFragmentByTag(TopicsFragment.class.getSimpleName());
        if (fragment == null) {
            fragment = TopicsFragment.create();
        }
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .add(containerId, fragment, TopicsFragment.class.getSimpleName())
                .addToBackStack(TopicsFragment.class.getSimpleName())
                .commit();
    }

    public void navigateToSource(Topic category) {
        Fragment fragment = fragmentManager.findFragmentByTag(SourceListFragment.class.getSimpleName());
        if (fragment == null) {
            fragment = SourceListFragment.Companion.create(category);
        }
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .replace(containerId, fragment, SourceListFragment.class.getSimpleName())
                .addToBackStack(SourceListFragment.class.getSimpleName())
                .commit();
    }

}
