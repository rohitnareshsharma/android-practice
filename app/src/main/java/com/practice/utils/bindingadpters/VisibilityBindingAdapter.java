package com.practice.utils.bindingadpters;

import android.view.View;

import androidx.databinding.BindingAdapter;

public class VisibilityBindingAdapter {

    @BindingAdapter(value="visibleOrGone")
    public static void visibleOrGone(View view, boolean isVisible) {
        view.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    @BindingAdapter(value="visibleOrInvisible")
    public static void visibleOrInvisible(View view, boolean isVisible) {
        view.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
    }
}
