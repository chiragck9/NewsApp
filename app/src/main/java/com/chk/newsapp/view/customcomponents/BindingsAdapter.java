package com.chk.newsapp.view.customcomponents;

import android.databinding.BindingAdapter;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.chk.newsapp.R;

/**
 * Created by chira on 19-08-2017.
 */

public class BindingsAdapter {
    @BindingAdapter("visiblity")
    public static void setVisibility(View view, boolean show) {
        view.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @BindingAdapter("selectedColor")
    public static void setColor(TextView view, boolean selected) {
        if (selected) {
            view.setTextColor(ContextCompat.getColor(view.getContext(), R.color.colorPrimary));
        } else {
            view.setTextColor(ContextCompat.getColor(view.getContext(), R.color.colorBlack));
        }
    }
}
