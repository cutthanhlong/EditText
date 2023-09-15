package com.nameart.maker.stylishfont.art.textonphoto.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.nameart.maker.stylishfont.art.textonphoto.R;

public class StickerAdapter extends BaseAdapter {
    private String[] bgImages;
    private String folderName;
    private String[] images;
    private Context mContext;
    OnClickItem onClickItem;

    public StickerAdapter(Context mContext, String[] images, String folderName, OnClickItem onClickItem) {
        this.mContext = mContext;
        this.images = images;
        this.folderName = folderName;
        this.onClickItem = onClickItem;
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
        @SuppressLint("ViewHolder") View view = View.inflate(this.mContext, R.layout.item_sticker, null);
        ImageView image = view.findViewById(R.id.imgSticker);
        MaterialCardView mcvImage = view.findViewById(R.id.mcvSticker);
        if (this.folderName.contains("Stickers")) {
            Glide.with(this.mContext).load(Uri.parse("file:///android_asset/" + this.folderName + "/" + this.images[position])).into(image);
        }
        mcvImage.setOnClickListener(view1 -> onClickItem.onClickItem(position));
        return view;
    }

    public interface OnClickItem {
        void onClickItem(int pos);
    }
}
