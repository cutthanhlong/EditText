package com.nameart.maker.stylishfont.art.textonphoto.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.nameart.maker.stylishfont.art.textonphoto.R;


public class SlideAdapter extends PagerAdapter {

    int[] images = {R.drawable.img_intro1, R.drawable.img_intro2, R.drawable.img_intro3};
    LayoutInflater inflater;
    Context context;

    public SlideAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.intro_slide_layout, container, false);
        ImageView img = view.findViewById(R.id.im_LogoSlide);
        img.setBackgroundResource(images[position]);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
