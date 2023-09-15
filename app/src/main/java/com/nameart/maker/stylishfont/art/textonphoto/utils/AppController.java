package com.nameart.maker.stylishfont.art.textonphoto.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.MaskFilter;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AppController {
    private Context mContext;

    public AppController(Context mContext) {
        this.mContext = mContext;
    }

    public ArrayList<Integer> getScreenDisplay(Activity activity) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        int height = displaymetrics.heightPixels;
        ArrayList<Integer> display = new ArrayList();
        display.add(Integer.valueOf(width));
        display.add(Integer.valueOf(height));
        return display;
    }

    public int dpToPx(int dp) {
        return Math.round(((float) dp) * this.mContext.getApplicationContext().getResources().getDisplayMetrics().density);
    }

    public void updateVisibilite(List<View> mainViews, View visibleView) {
        for (int i = 0; i < mainViews.size(); i++) {
            if (mainViews.get(i) == visibleView) {
                visibleView.setVisibility(View.VISIBLE);
            } else {
                ((View) mainViews.get(i)).setVisibility(View.GONE);
            }
        }
    }

    public Bitmap getBitmapFromAsset(String filePath) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(this.mContext.getAssets().open(filePath));
        } catch (IOException e) {
        }
        return bitmap;
    }

    public void setGradientBG(View view, Orientation orientation, int colorStart, int colorEnd) {
        view.setBackgroundDrawable(new GradientDrawable(orientation, new int[]{colorStart, colorEnd}));
    }

    public void setPattern(TextView textView, Bitmap bitmap, TileMode tileX, TileMode tiley, MaskFilter maskFilter) {
        Shader shader = new BitmapShader(bitmap, tileX, tiley);
        textView.getPaint().setMaskFilter(maskFilter);
        textView.getPaint().setShader(shader);
    }

    public void setShadow(TextView textView, float radius, float dx, float dy, int color) {
        textView.setShadowLayer(radius, dx, dy, color);
    }
}
