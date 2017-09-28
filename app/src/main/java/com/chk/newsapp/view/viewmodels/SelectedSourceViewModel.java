package com.chk.newsapp.view.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.SharedPreferences;

import com.chk.newsapp.R;

import javax.inject.Inject;

/**
 * Created by chira on 04-09-2017.
 */

public class SelectedSourceViewModel extends ViewModel {


    private MutableLiveData<String> selectedSource = new MutableLiveData<>();

    @Inject
    public SelectedSourceViewModel() {
    }

    public MutableLiveData<String> getSelectedSource() {
        return selectedSource;
    }

    public void setSelectedSource(String selectedSource) {
        this.selectedSource.setValue(selectedSource);
    }
}
