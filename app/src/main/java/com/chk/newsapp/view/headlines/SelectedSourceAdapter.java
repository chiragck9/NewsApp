package com.chk.newsapp.view.headlines;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.chk.newsapp.R;
import com.chk.newsapp.data.datamodel.SourceEntity;
import com.chk.newsapp.data.utils.Objects;
import com.chk.newsapp.databinding.SelectedSourceItemBinding;
import com.chk.newsapp.view.customcomponents.DataBoundListAdapter;

/**
 * Created by chira on 04-09-2017.
 */

public class SelectedSourceAdapter extends DataBoundListAdapter<SelectedSourceItem, SelectedSourceItemBinding> {

    private OnItemClickListener mOnItemClickListener;

    public SelectedSourceAdapter(OnItemClickListener topicsClickCallback) {
        this.mOnItemClickListener = topicsClickCallback;
    }

    @Override
    protected SelectedSourceItemBinding createBinding(ViewGroup parent) {
        SelectedSourceItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.selected_source_item, parent, false);
        binding.getRoot().setOnClickListener(view -> {
            SelectedSourceItem sourceEntity = binding.getSource();
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onClick(sourceEntity);
            }

        });
        return binding;
    }


    @Override
    protected void bind(SelectedSourceItemBinding binding, SelectedSourceItem item) {
        binding.setSource(item);
    }

    @Override
    protected boolean areItemsTheSame(SelectedSourceItem oldItem, SelectedSourceItem newItem) {
        return Objects.equals(oldItem, newItem);
    }

    @Override
    protected boolean areContentsTheSame(SelectedSourceItem oldItem, SelectedSourceItem newItem) {
        return Objects.equals(oldItem, newItem);
    }


    public interface OnItemClickListener {
        void onClick(SelectedSourceItem sourceEntity);
    }
}
