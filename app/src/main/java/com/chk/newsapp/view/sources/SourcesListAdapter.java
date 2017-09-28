package com.chk.newsapp.view.sources;

import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.chk.newsapp.R;
import com.chk.newsapp.data.datamodel.SourceEntity;
import com.chk.newsapp.databinding.SourcesItemBinding;
import com.chk.newsapp.data.utils.Objects;
import com.chk.newsapp.view.customcomponents.DataBoundListAdapter;

/**
 * Created by chira on 19-08-2017.
 */

public class SourcesListAdapter extends DataBoundListAdapter<SourceEntity, SourcesItemBinding> {

    private DataBindingComponent mDataBindingComponent;
    private SourceSubscribedCallback mSourceSubscribedCallback;

    public SourcesListAdapter(DataBindingComponent dataBindingComponent, SourceSubscribedCallback topicsClickCallback) {
        this.mDataBindingComponent = dataBindingComponent;
        this.mSourceSubscribedCallback = topicsClickCallback;
    }

    @Override
    protected SourcesItemBinding createBinding(ViewGroup parent) {
        SourcesItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.sources_item, parent, false,
                        mDataBindingComponent);

        binding.subscription.setOnClickListener(v -> {
            SourceEntity source = binding.getSource();
            source.setSubscribed(!source.isSubscribed());
            binding.setSource(source);
            if (mSourceSubscribedCallback != null) {
                mSourceSubscribedCallback.onSourceSubscription(source);
            }
        });
        return binding;
    }

    @Override
    protected void bind(SourcesItemBinding binding, SourceEntity item) {
        binding.setSource(item);
    }

    @Override
    protected boolean areItemsTheSame(SourceEntity oldItem, SourceEntity newItem) {
        return Objects.equals(oldItem, newItem);
    }

    @Override
    protected boolean areContentsTheSame(SourceEntity oldItem, SourceEntity newItem) {
        return Objects.equals(oldItem, newItem);
    }


    public interface SourceSubscribedCallback {
        void onSourceSubscription(SourceEntity sourceEntity);
    }

}
