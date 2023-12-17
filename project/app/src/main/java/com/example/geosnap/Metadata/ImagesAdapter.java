package com.example.geosnap.Metadata;

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

public class ImagesAdapter<T> extends PagerAdapter {

    Context context;
    ArrayList<T> imageUris;
    LayoutInflater layoutInflater;

    public ImagesAdapter(Context context, ArrayList<T> imageUris) {
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
        ArrayList<Uri> uriList = new ArrayList<>();

        if (imageUris.get(position) instanceof String) {
            for (int i=0; i<imageUris.size(); i++) {
                uriList.add(Uri.parse(imageUris.get(i).toString()));
            }
        } else if (imageUris.get(position) instanceof Uri){
            uriList= (ArrayList<Uri>) imageUris;
        }

        imageView.setImageURI(uriList.get(position));
        Objects.requireNonNull(container).addView(view);
        /*
        Glide.with(context)
                .load((String) imageUris.get(position))
                .into(imageView);
        }
        */

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
