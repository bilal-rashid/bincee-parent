package com.bincee.parent.helper;

import android.content.Context;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bincee.parent.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
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

    public static void setImageUrlRoundedCorner(ImageView imageView, String url) {
        Context context = imageView.getContext();
        Glide.with(context).load(url)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.profile_avatar)
                        .transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners(18)))
                        .error(R.drawable.profile_avatar))
//                .apply(RequestOptions.bitmapTransform(new RoundedCorners(14)))

                .into(imageView);
    }

    //    @BindingAdapter("imageUrl")
    public static void setImageSS(ImageView imageView, String url) {
        Context context = imageView.getContext();
        Glide.with(context).load(url)
                .apply(new RequestOptions()
//                        .override(UiUtils.DptoPixel(imageView.getContext(),300),UiUtils.DptoPixel(imageView.getContext(),180))
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
//    public static void centerCorpRoundedCorner(ImageView imageView, String url) {
//        Context context = imageView.getContext();
//        Glide.with(context).load(url)
//                .apply(new RequestOptions()
//                        .transform()
//                        .placeholder(R.drawable.girl_profile_icon)
//                        .error(R.drawable.girl_profile_icon))
//                .into(imageView);
//    }
}
