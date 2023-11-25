package com.example.geosnap.MetadataExtractor;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.geosnap.R;

import java.util.ArrayList;
import java.util.Objects;

public class ImagesAdapter extends PagerAdapter {

    Context context;
    ArrayList<Uri> imageUris;
    LayoutInflater layoutInflater;

    public ImagesAdapter(Context context, ArrayList<Uri> imageUris) {
        this.context = context;
        this.imageUris = imageUris;
        layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return imageUris.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view= layoutInflater.inflate(R.layout.images_single, container, false);
        ImageView imageView= (ImageView) view.findViewById(R.id.imageView);
        imageView.setImageURI(imageUris.get(position));
        Objects.requireNonNull(container).addView(view);
        return view;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (object);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
    }
}
