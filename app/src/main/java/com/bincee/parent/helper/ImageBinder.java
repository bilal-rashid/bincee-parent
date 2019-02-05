package com.bincee.parent.helper;

import android.content.Context;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bincee.parent.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public final class ImageBinder {
    private ImageBinder() {
    }

    @BindingAdapter("imageUrl")
    public static void setImageUrl(ImageView imageView, String url) {
        Context context = imageView.getContext();
        Glide.with(context).load(url)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.profile_avatar)
                        .error(R.drawable.profile_avatar))
                .into(imageView);
    }

    //    @BindingAdapter("imageUrl")
    public static void setImageSS(ImageView imageView, String url) {
        Context context = imageView.getContext();
        Glide.with(context).load(url)
                .apply(new RequestOptions()
                        .fitCenter()
                        .placeholder(R.drawable.girl_profile_icon)
                        .error(R.drawable.girl_profile_icon))
                .into(imageView);
    }

    //    @BindingAdapter("imageUrl")
    public static void setImageCenterCorp(ImageView imageView, String url) {
        Context context = imageView.getContext();
        Glide.with(context).load(url)
                .apply(new RequestOptions()
                        .centerCrop()
                        .placeholder(R.drawable.girl_profile_icon)
                        .error(R.drawable.girl_profile_icon))
                .into(imageView);
    }
}
