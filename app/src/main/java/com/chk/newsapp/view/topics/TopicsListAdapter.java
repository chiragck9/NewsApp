package com.chk.newsapp.view.topics;

import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.chk.newsapp.R;
import com.chk.newsapp.data.datamodel.Topic;
import com.chk.newsapp.databinding.TopicsItemBinding;
import com.chk.newsapp.data.utils.Objects;
import com.chk.newsapp.view.customcomponents.DataBoundListAdapter;

/**
 * Created by chira on 19-08-2017.
 */

public class TopicsListAdapter extends DataBoundListAdapter<Topic, TopicsItemBinding> {

    private DataBindingComponent mDataBindingComponent;
    private TopicsClickCallback mTopicsClickCallback;

    public TopicsListAdapter(DataBindingComponent dataBindingComponent, TopicsClickCallback topicsClickCallback) {
        this.mDataBindingComponent = dataBindingComponent;
        this.mTopicsClickCallback = topicsClickCallback;
    }

    @Override
    protected TopicsItemBinding createBinding(ViewGroup parent) {
        TopicsItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.topics_item, parent, false,
                        mDataBindingComponent);

        binding.getRoot().setOnClickListener(v -> {
            Topic topic = binding.getTopic();
            if (mTopicsClickCallback != null) {
                mTopicsClickCallback.onClick(topic);
            }
        });
        return binding;
    }

    @Override
    protected void bind(TopicsItemBinding binding, Topic item) {
        binding.setTopic(item);
    }

    @Override
    protected boolean areItemsTheSame(Topic oldItem, Topic newItem) {
        return Objects.equals(oldItem,newItem);
    }

    @Override
    protected boolean areContentsTheSame(Topic oldItem, Topic newItem) {
        return Objects.equals(oldItem,newItem);
    }


    public interface TopicsClickCallback {
        void onClick(Topic topic);
    }

}
