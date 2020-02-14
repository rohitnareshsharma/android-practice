package com.practice.data.repositories;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

/**
 * Component responsible for image loading in app.
 */
public class ImageRepository {

    @BindingAdapter(value={"imageUrl", "placeholder"}, requireAll=false)
    public static void setImageUrl(ImageView imageView, String url,
                                   Drawable placeHolder) {
        if (url == null) {
            imageView.setImageDrawable(placeHolder);
        } else {
            Glide.with(imageView.getContext().getApplicationContext())
                    .load(url)
                    .centerCrop()
                    .apply(RequestOptions.circleCropTransform())
                    .placeholder(placeHolder)
                    .into(imageView);
        }
    }
}
