package com.chk.newsapp.view.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import javax.inject.Inject;

/**
 * Created by chira on 30-08-2017.
 */

public class HeadlinesPagerBackPressModel extends ViewModel {

    MutableLiveData<Integer> currentPosition = new MutableLiveData<>();

    @Inject
    public HeadlinesPagerBackPressModel() {
    }

    public void setCurrentPosition(Integer currentPosition) {
        if (this.currentPosition.getValue() != currentPosition) {
            this.currentPosition.setValue(currentPosition);
        }
    }

    public MutableLiveData<Integer> getCurrentPosition() {
        return currentPosition;
    }
}
