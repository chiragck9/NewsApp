package com.chk.newsapp.view.topics;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chk.newsapp.R;
import com.chk.newsapp.data.api.utils.Resource;
import com.chk.newsapp.data.datamodel.Category;
import com.chk.newsapp.data.datamodel.SourceEntity;
import com.chk.newsapp.data.datamodel.Topic;
import com.chk.newsapp.data.repository.DataRepository;
import com.chk.newsapp.databinding.FragmentTopicsBinding;
import com.chk.newsapp.view.BaseFragment;
import com.chk.newsapp.view.NavigationController;
import com.chk.newsapp.view.customcomponents.AutoClearedValue;
import com.chk.newsapp.view.customcomponents.FragmentDataBindingComponent;
import com.chk.newsapp.view.sources.SourceListFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;


/**
 * Created by chira on 15-08-2017.
 */

public class TopicsFragment extends BaseFragment {

    @Inject
    ViewModelProvider.Factory mViewModelFactory;
    @Inject
    NavigationController mNavigationController;
    private DataBindingComponent mDataBindingComponent = new FragmentDataBindingComponent(this);
    private AutoClearedValue<FragmentTopicsBinding> mBinding;
    private AutoClearedValue<TopicsListAdapter> mAdapter;
    private TopicsViewModel mTopicsViewModel;

    @Inject
    DataRepository mDataRepository;

    @Override
    protected void create(Bundle savedInstanceState) {
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentTopicsBinding fragmentTopicsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_topics, container, false, mDataBindingComponent);
        mBinding = new AutoClearedValue<FragmentTopicsBinding>(this, fragmentTopicsBinding);
        return fragmentTopicsBinding.getRoot();
    }

    @Override
    protected void activityCreated(Bundle savedInstanceState) {
        mTopicsViewModel = ViewModelProviders.of(this, mViewModelFactory).get(TopicsViewModel.class);
        mTopicsViewModel.getSources().observe(this, new Observer<Resource<List<SourceEntity>>>() {
            @Override
            public void onChanged(@Nullable Resource<List<SourceEntity>> listResource) {
                mBinding.get().setResource(listResource);
                mBinding.get().executePendingBindings();

            }
        });
        TopicsListAdapter topicsListAdapter = new TopicsListAdapter(mDataBindingComponent,
                topic -> mFragmentNavigation.pushFragment(SourceListFragment.Companion.create(topic)));
//        mNavigationController.navigateToSource(topic));
        this.mAdapter = new AutoClearedValue<>(this, topicsListAdapter);
        this.mBinding.get().topicsList.setAdapter(topicsListAdapter);

        mTopicsViewModel.setTopics(createTopics());
        mTopicsViewModel.getTopics().observe(this, topics -> {
            if (topics != null) {
                mAdapter.get().replace(topics);
            } else {
                mAdapter.get().replace(Collections.emptyList());
            }
        });
    }

    public static TopicsFragment create() {
        TopicsFragment topicsFragment = new TopicsFragment();
        return topicsFragment;
    }

    private List<Topic> createTopics() {
        List<Topic> topics = new ArrayList<>();

        Topic topic = new Topic();
        topic.setCategory(Category.BUSINESS);
        topic.setTitle(getString(R.string.topic_business));
        topic.setUrl("https://cdn.pixabay.com/photo/2014/01/30/18/26/singapore-river-255116_960_720.jpg");
        topics.add(topic);

        Topic topic2 = new Topic();
        topic2.setCategory(Category.ENTERTAINMENT);
        topic2.setTitle(getString(R.string.topic_entertainment));
        topic2.setUrl("https://cdn.pixabay.com/photo/2016/12/29/15/23/juggler-1938707_960_720.jpg");
        topics.add(topic2);

        Topic topic3 = new Topic();
        topic3.setCategory(Category.GAMING);
        topic3.setTitle(getString(R.string.topic_gaming));
        topic3.setUrl("https://cdn.pixabay.com/photo/2015/12/23/22/36/minecraft-1106252_960_720.jpg");
        topics.add(topic3);

        Topic topic4 = new Topic();
        topic4.setCategory(Category.GENERAL);
        topic4.setTitle(getString(R.string.topic_general));
        topic4.setUrl("https://cdn.pixabay.com/photo/2017/06/06/17/29/ball-2377876_960_720.jpg");
        topics.add(topic4);

        Topic topic10 = new Topic();
        topic10.setCategory(Category.HEALTH);
        topic10.setTitle(getString(R.string.topic_technology));
        topic10.setUrl("https://cdn.pixabay.com/photo/2016/11/02/16/49/orange-1792233_960_720.jpg");
        topics.add(topic10);

        Topic topic5 = new Topic();
        topic5.setCategory(Category.MUSIC);
        topic5.setTitle(getString(R.string.topic_music));
        topic5.setUrl("https://cdn.pixabay.com/photo/2014/07/31/23/50/acoustic-guitar-407214_960_720.jpg");
        topics.add(topic5);

        Topic topic6 = new Topic();
        topic6.setCategory(Category.POLITICS);
        topic6.setTitle(getString(R.string.topic_politics));
        topic6.setUrl("https://cdn.pixabay.com/photo/2016/08/02/15/11/mural-1563668_960_720.jpg");
        topics.add(topic6);

        Topic topic7 = new Topic();
        topic7.setCategory(Category.SCIENCE_AND_NATURE);
        topic7.setTitle(getString(R.string.topic_science_and_nature));
        topic7.setUrl("https://cdn.pixabay.com/photo/2016/10/12/22/44/mountains-1736209_960_720.jpg");
        topics.add(topic7);

        Topic topic8 = new Topic();
        topic8.setCategory(Category.SPORTS);
        topic8.setTitle(getString(R.string.topic_sports));
        topic8.setUrl("https://cdn.pixabay.com/photo/2014/10/14/20/24/the-ball-488716_960_720.jpg");
        topics.add(topic8);

        Topic topic9 = new Topic();
        topic9.setCategory(Category.TECHNOLOGY);
        topic9.setTitle(getString(R.string.topic_technology));
        topic9.setUrl("https://cdn.pixabay.com/photo/2016/12/01/18/17/mobile-phone-1875813_960_720.jpg");
        topics.add(topic9);

        return topics;
    }
}

