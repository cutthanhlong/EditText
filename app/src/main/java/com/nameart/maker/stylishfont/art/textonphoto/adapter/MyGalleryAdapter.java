package com.nameart.maker.stylishfont.art.textonphoto.adapter;

import static com.nameart.maker.stylishfont.art.textonphoto.activities.MyGalleryScreenActivity.back;
import static com.nameart.maker.stylishfont.art.textonphoto.activities.MyGalleryScreenActivity.cancel;
import static com.nameart.maker.stylishfont.art.textonphoto.activities.MyGalleryScreenActivity.selectall;
import static com.nameart.maker.stylishfont.art.textonphoto.activities.MyGalleryScreenActivity.tv_toolbar;
import static com.nameart.maker.stylishfont.art.textonphoto.activities.MyGalleryScreenActivity.unselect;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nameart.maker.stylishfont.art.textonphoto.R;
import com.nameart.maker.stylishfont.art.textonphoto.activities.MyGalleryScreenActivity;
import com.nameart.maker.stylishfont.art.textonphoto.utils.SquareImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class MyGalleryAdapter extends RecyclerView.Adapter<MyGalleryAdapter.ViewHolderMyCreation> {
    List<Uri> listImageSaved = new ArrayList<>();
    private int selectedCount = 0;
    Context mContext;
    OnClickImage onClickImage;

    boolean[] selected;
    public boolean isSelectionMode = false;

    public static String path = "";

    private HashSet<Uri> selectedUris = new HashSet<>();

    public MyGalleryAdapter(Context context, List<Uri> listImageSaved, OnClickImage onClickImage) {
        this.mContext = context;
        this.listImageSaved = listImageSaved;
        this.onClickImage = onClickImage;
        selected = new boolean[listImageSaved.size()];
        Arrays.fill(selected, false);
    }

    @NonNull
    @Override
    public ViewHolderMyCreation onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_my_creation, parent, false);
        return new ViewHolderMyCreation(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderMyCreation holder, @SuppressLint("RecyclerView") int position) {
        Uri uri = listImageSaved.get(position);
        if (selected[position]) {
            holder.imgSelected.setVisibility(View.VISIBLE);
            selectedCount++;
        } else {
            holder.imgSelected.setVisibility(View.GONE);
            selectedCount--;
        }
        Glide.with(this.mContext).load(uri.toString()).thumbnail(0.1f).dontAnimate().centerCrop().placeholder(R.drawable.img_loadding_image).error(R.drawable.img_loadding_image).into(holder.imgAlbum);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                toggleSelection(position);
                isSelectionMode = true;
                if (getSelectedCount() >= 1) {
                    ((MyGalleryScreenActivity) mContext).showSelectAll();
                    ((MyGalleryScreenActivity) mContext).showClearButton();
                    ((MyGalleryScreenActivity) mContext).showCancel();
                } else {
                    tv_toolbar.setVisibility(View.VISIBLE);
                    unselect.setVisibility(View.GONE);
                    cancel.setVisibility(View.GONE);
                    selectall.setVisibility(View.GONE);
                    ((MyGalleryScreenActivity) mContext).hideClearButton();
                    back.setVisibility(View.VISIBLE);
                }
                if (getSelectedCount() == listImageSaved.size()) {
                    unselect.setVisibility(View.VISIBLE);
                    selectall.setVisibility(View.GONE);
                    int color = Color.parseColor("#5C5C5C");
                    unselect.setTextColor(color);
                }

                return true;
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSelectionMode == true) {
                    toggleSelection(position);
                    if (getSelectedCount() >= 1) {
                        ((MyGalleryScreenActivity) mContext).showSelectAll();
                        ((MyGalleryScreenActivity) mContext).showClearButton();
                        ((MyGalleryScreenActivity) mContext).showCancel();
                        unselect.setVisibility(View.GONE);
                    } else {
                        tv_toolbar.setVisibility(View.VISIBLE);
                        cancel.setVisibility(View.GONE);
                        unselect.setVisibility(View.GONE);
                        selectall.setVisibility(View.GONE);
                        ((MyGalleryScreenActivity) mContext).hideClearButton();
                        back.setVisibility(View.VISIBLE);
                        isSelectionMode = false;
                    }
                    if (getSelectedCount() == listImageSaved.size()) {
                        unselect.setVisibility(View.VISIBLE);
                        selectall.setVisibility(View.GONE);
                        int color = Color.parseColor("#5C5C5C");
                        unselect.setTextColor(color);
                    }
                } else {
                    onClickImage.onClickImage(position);
                }
            }
        });

    }

    public void selectAll() {
        for (int i = 0; i < listImageSaved.size(); i++) {
            selected[i] = true;
            selectedUris.add(listImageSaved.get(i));
            notifyItemChanged(i);
            ;
        }
    }

    public void clearSelection() {
        for (int i = 0; i < listImageSaved.size(); i++) {
            selected[i] = false;
            selectedUris.clear();
            notifyItemChanged(i);
            isSelectionMode = false;
        }
    }

    public void clearSelection1() {
        for (int i = 0; i < listImageSaved.size(); i++) {
            selected[i] = false;
            selectedUris.clear();
            notifyItemChanged(i);
            isSelectionMode = true;
        }
    }

    public void toggleSelection(int position) {
        selected[position] = !selected[position];
        Uri uri = listImageSaved.get(position);
        if (selected[position]) {
            selectedUris.add(uri);
        } else {
            unselect.setVisibility(View.GONE);
            selectedUris.remove(uri);

        }
        notifyItemChanged(position);
    }

    public void removeSelectedItems() {
        listImageSaved.removeAll(selectedUris);
        notifyDataSetChanged();
        clearSelection();
        isSelectionMode = false;
        selectedUris.clear();
    }

    public int getSelectedCount() {
        return selectedUris.size();
    }

    public ArrayList<Uri> getSelectedUris() {
        ArrayList<Uri> selectedUris = new ArrayList<>(this.selectedUris);
        for (Uri uri : selectedUris) {
            path = uri.getPath();
        }
        return selectedUris;
    }

    @Override
    public int getItemCount() {
        return listImageSaved.size();
    }


    public class ViewHolderMyCreation extends RecyclerView.ViewHolder {
        SquareImageView imgAlbum;
        ImageView imgSelected;


        public ViewHolderMyCreation(@NonNull View itemView) {
            super(itemView);
            imgAlbum = itemView.findViewById(R.id.itemImg);
            imgSelected = itemView.findViewById(R.id.imgSelected);

        }
    }

    public interface OnClickImage {
        void onClickImage(int pos);
    }
}
