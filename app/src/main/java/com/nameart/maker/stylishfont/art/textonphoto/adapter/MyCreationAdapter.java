package com.nameart.maker.stylishfont.art.textonphoto.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nameart.maker.stylishfont.art.textonphoto.R;
import com.nameart.maker.stylishfont.art.textonphoto.utils.SquareImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyCreationAdapter extends RecyclerView.Adapter<MyCreationAdapter.ViewHolderMyCreation> {
    List<Uri> listImageSaved = new ArrayList<>();
    Context context;
    OnClickImage onClickImage;

    boolean[] selected;
    private boolean isSelectionMode = false;
    private ArrayList<Integer> selectedPositions = new ArrayList<>();

    public MyCreationAdapter(Context context, List<Uri> listImageSaved, OnClickImage onClickImage) {
        this.context = context;
        this.listImageSaved = listImageSaved;
        this.onClickImage = onClickImage;
        selected = new boolean[listImageSaved.size()];
        Arrays.fill(selected, false);
    }

    @NonNull
    @Override
    public ViewHolderMyCreation onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_my_creation, parent, false);
        return new ViewHolderMyCreation(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderMyCreation holder, @SuppressLint("RecyclerView") int position) {
        Glide.with(this.context).load(listImageSaved.get(position).toString()).
                thumbnail(0.1f).dontAnimate().centerCrop().
                placeholder(R.drawable.img_loadding_image).
                error(R.drawable.img_loadding_image).into(holder.imgAlbum);

        if (selected[position]) {
            holder.itemView.setBackgroundColor(Color.GRAY);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickImage.onClickImage(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listImageSaved.size();
    }

    public class ViewHolderMyCreation extends RecyclerView.ViewHolder {
        SquareImageView imgAlbum;


        public ViewHolderMyCreation(@NonNull View itemView) {
            super(itemView);
            imgAlbum = itemView.findViewById(R.id.itemImg);
        }
    }

    public interface OnClickImage {
        void onClickImage(int pos);
    }
}
