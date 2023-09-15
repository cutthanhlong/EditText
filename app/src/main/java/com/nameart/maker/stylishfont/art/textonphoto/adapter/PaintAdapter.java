package com.nameart.maker.stylishfont.art.textonphoto.adapter;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.bumptech.glide.Glide;
import com.nameart.maker.stylishfont.art.textonphoto.R;
import com.nameart.maker.stylishfont.art.textonphoto.activities.CreateNewScreenActivity;
import com.nameart.maker.stylishfont.art.textonphoto.interfaces.ItemClickListener;

public class PaintAdapter extends Adapter<PaintAdapter.ViewHolderCollagePattern> {
    private String clicked_item_value;
    private Context mContext;
    private String paintType;
    private String[] resources;

    static class ViewHolderCollagePattern extends ViewHolder implements OnClickListener {
        ImageView imageView;
        ItemClickListener itemClickListener;
        RelativeLayout relativeLayout;
        View view;

        public ViewHolderCollagePattern(View itemView) {
            super(itemView);
            this.view = itemView.findViewById(R.id.view);
            this.relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relativeLayout);
            this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener ic) {
            this.itemClickListener = ic;
        }

        public void onClick(View v) {
            this.itemClickListener.onItemClick(v, getLayoutPosition());
        }
    }

    public PaintAdapter(Context mContext, String[] resources, String clicked_item_value, String paintType) {
        this.mContext = mContext;
        this.resources = resources;
        this.clicked_item_value = clicked_item_value;
        this.paintType = paintType;
    }

    public ViewHolderCollagePattern onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolderCollagePattern(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_view, parent, false));
    }

    public void onBindViewHolder(ViewHolderCollagePattern holder, int position) {
        String str = this.paintType;
        int i = -1;
        switch (str.hashCode()) {
            case 76875838:
                if (str.equals("Paint")) {
                    i = 0;
                    break;
                }
                break;
            case 1270527979:
                if (str.equals("Magic Paint")) {
                    i = 1;
                    break;
                }
                break;
        }
        switch (i) {
            case 0:
                holder.relativeLayout.setBackgroundColor(Color.parseColor(this.resources[position]));
                break;
            case 1:
                Glide.with(this.mContext).load(Uri.parse("file:///android_asset/MagicPaint/" + this.resources[position])).into(holder.imageView);
                break;
        }
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                clicked_item_value = resources[pos];
                if (mContext instanceof CreateNewScreenActivity) {
                    ((CreateNewScreenActivity) mContext).updatePaintValues(paintType, resources[pos]);
                }
                notifyDataSetChanged();
            }
        });
        if (this.resources[position].equals(this.clicked_item_value)) {
            holder.view.setVisibility(View.VISIBLE);
        } else {
            holder.view.setVisibility(View.GONE);
        }
    }

    public int getItemCount() {
        return this.resources != null ? this.resources.length : 0;
    }
}
