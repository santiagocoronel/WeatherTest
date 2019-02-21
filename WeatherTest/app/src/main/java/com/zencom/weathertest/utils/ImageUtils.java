package com.zencom.weathertest.utils;

import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;

public class ImageUtils {

    public static void loadImage(File file, ImageView imageView) {
        Glide.with(imageView.getContext())
                .load(file)
                .apply(new RequestOptions()
                        .skipMemoryCache(false)
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .fitCenter()
                )
                .into(imageView);
    }

    public static void loadImage(Uri uri, ImageView imageView) {
        Glide.with(imageView.getContext())
                .load(uri)
                .apply(new RequestOptions()
                        .skipMemoryCache(false)
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .fitCenter()
                )
                .into(imageView);
    }

    public static void loadImageCircle(File file, ImageView imageView) {
        Glide.with(imageView.getContext())
                .load(file)
                .apply(new RequestOptions()
                        .skipMemoryCache(false)
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .fitCenter()
                        .circleCrop()
                )
                .into(imageView);
    }

    public static void loadImageCircle(Uri uri, ImageView imageView) {
        Glide.with(imageView.getContext())
                .load(uri)
                .apply(new RequestOptions()
                        .skipMemoryCache(false)
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .fitCenter()
                        .circleCrop()
                )
                .into(imageView);
    }

}
