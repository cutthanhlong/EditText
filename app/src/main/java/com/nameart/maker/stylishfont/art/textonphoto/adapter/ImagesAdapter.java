package com.nameart.maker.stylishfont.art.textonphoto.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.nameart.maker.stylishfont.art.textonphoto.R;
import com.nameart.maker.stylishfont.art.textonphoto.utils.SpHelper;

public class ImagesAdapter extends BaseAdapter {
    private String[] bgImages;
    private String folderName;
    private String[] images;
    private Context mContext;
    private int clickPos;
    private boolean setClick;
    OnClickItem onClickItem;

    public ImagesAdapter(Context mContext, String[] images, String folderName, boolean setClick, int clickPos, OnClickItem onClickItem) {
        this.mContext = mContext;
        this.images = images;
        this.folderName = folderName;
        this.setClick = setClick;
        this.onClickItem = onClickItem;
        this.clickPos = clickPos;
        if (folderName.contains("Backgrounds")) {
            this.bgImages = new String[(images.length + 2)];
            this.bgImages[0] = "";
            this.bgImages[1] = "";
            for (int i = 0; i < images.length; i++) {
                this.bgImages[i + 2] = images[i];
            }
        }
    }

    public int getCount() {
        return this.folderName.contains("Backgrounds") ? this.bgImages.length : this.images.length;
    }

    public Object getItem(int position) {
        return this.folderName.contains("Backgrounds") ? this.bgImages[position] : this.images[position];
    }

    public long getItemId(int position) {
        return this.folderName.contains("Backgrounds") ? (long) this.bgImages[position].length() : (long) this.images[position].length();
    }

    public View getView(int position, View convertView, ViewGroup viewGroup) {
        @SuppressLint("ViewHolder") View view = View.inflate(this.mContext, R.layout.item_images, null);
        ImageView image = (ImageView) view.findViewById(R.id.iv_image);
        CardView mcvImage = view.findViewById(R.id.mcvImage);
        ImageView imgChecked = view.findViewById(R.id.imgSelected);
        if (this.folderName.contains("Patterns")) {
            Glide.with(this.mContext).load(Uri.parse("file:///android_asset/" + this.folderName + "/" + this.images[position])).into(image);
        }
        if (this.folderName.contains("Backgrounds")) {
            if (position == 0) {
                Glide.with(this.mContext).load(Integer.valueOf(R.drawable.ic_bg_picture)).into(image);
            } else if (position == 1) {
                Glide.with(this.mContext).load(Integer.valueOf(R.drawable.ic_bg_none)).into(image);
            } else {
                Glide.with(this.mContext).load(Uri.parse("file:///android_asset/" + this.folderName + "/" + this.bgImages[position])).into(image);
            }
        }
        mcvImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (setClick) {
                    clickPos = position; 
                }
                onClickItem.onClickItem(position);
                notifyDataSetChanged();
            }
        });
        if (clickPos == position && position != 0 && position != 1) {
            imgChecked.setVisibility(View.VISIBLE);
        } else {
            if (position == 0) {
                if (SpHelper.getPosition(mContext, SpHelper.ITEM_BACKGROUND, -1) == -1) {
                    imgChecked.setVisibility(View.GONE);
                } else {
                    clickPos = SpHelper.getPosition(mContext, SpHelper.ITEM_BACKGROUND, -1);
                }
            }
            imgChecked.setVisibility(View.GONE);
        }
        return view;
    }

    public void updateList() {
        notifyDataSetChanged();
    }

    public interface OnClickItem {
        void onClickItem(int pos);
    }
}
