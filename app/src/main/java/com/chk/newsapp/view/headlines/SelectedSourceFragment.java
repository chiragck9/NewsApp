package com.chk.newsapp.view.headlines;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.chk.newsapp.R;
import com.chk.newsapp.data.api.utils.Resource;
import com.chk.newsapp.data.datamodel.SourceEntity;
import com.chk.newsapp.data.db.PreferencesHelper;
import com.chk.newsapp.data.utils.Constants;
import com.chk.newsapp.view.viewmodels.SelectedSourceViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.chk.newsapp.data.db.PreferencesHelper.SELECTED_SOURCE;
import static com.chk.newsapp.data.utils.Constants.COVER_STORY;

/**
 * Created by chira on 04-09-2017.
 */

public class SelectedSourceFragment extends BottomSheetDialogFragment {

    private static final String LIST = "list";

    private ArrayList<SelectedSourceItem> list;

    public static SelectedSourceFragment create(ArrayList<SelectedSourceItem> selectedSourceItems) {
        SelectedSourceFragment selectedSourceFragment = new SelectedSourceFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(LIST, selectedSourceItems);
        selectedSourceFragment.setArguments(bundle);
        return selectedSourceFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.list = getArguments().getParcelableArrayList(LIST);
        }
    }

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }

        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };


    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.fragment_selected_source, null);

        RecyclerView selectedSourceList = contentView.findViewById(R.id.selected_source_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(contentView.getContext());
        selectedSourceList.setLayoutManager(linearLayoutManager);

        SelectedSourceViewModel selectedSourceViewModel = ViewModelProviders.of(getActivity()).get(SelectedSourceViewModel.class);
        SelectedSourceAdapter selectedSourceAdapter = new SelectedSourceAdapter(sourceEntity -> {
            PreferencesHelper.getInstance(getContext()).write(SELECTED_SOURCE, sourceEntity.getId());
            selectedSourceViewModel.setSelectedSource(sourceEntity.getId());
            dismiss();
        });
        selectedSourceAdapter.replace(this.list);
        selectedSourceList.setAdapter(selectedSourceAdapter);
        dialog.setContentView(contentView);

        View view = (View) contentView.getParent();
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) view.getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
            WindowManager wm = (WindowManager) contentView.getContext().getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics metrics = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(metrics);
            ((BottomSheetBehavior) behavior).setPeekHeight((int) (metrics.heightPixels * 0.55));
        }

    }

}
