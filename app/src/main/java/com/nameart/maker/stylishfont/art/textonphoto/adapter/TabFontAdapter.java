package com.nameart.maker.stylishfont.art.textonphoto.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nameart.maker.stylishfont.art.textonphoto.databinding.ItemTabBarBinding;

public class TabFontAdapter extends RecyclerView.Adapter<TabFontAdapter.TabFontViewHolder> {
    private String[] tabFont;
    private Context context;
    private int posTabFont;
    OnClickItem onClickItem;
    private boolean setClick;

    public TabFontAdapter(String[] tabFont, Context context, Boolean setClick, int posTabFont, OnClickItem onClickItem) {
        this.tabFont = tabFont;
        this.context = context;
        this.setClick = setClick;
        this.posTabFont = posTabFont;
        this.onClickItem = onClickItem;
    }

    @NonNull
    @Override
    public TabFontViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTabBarBinding binding = ItemTabBarBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new TabFontViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TabFontViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.binding.txtName.setText(tabFont[position]);
        if (posTabFont == position) {
            holder.binding.cvName0.setCardBackgroundColor(Color.parseColor("#F6CB60"));
            holder.binding.txtName.setTextColor(Color.parseColor("#464646"));
        } else {
            holder.binding.cvName0.setCardBackgroundColor(Color.parseColor("#DADADA"));
            holder.binding.txtName.setTextColor(Color.parseColor("#636363"));
        }
        holder.binding.cvName0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (setClick) {
                    posTabFont = position;
                }
                onClickItem.onClickItem(posTabFont);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (tabFont != null) {
            return tabFont.length;
        }
        return 0;
    }

    public class TabFontViewHolder extends RecyclerView.ViewHolder {
        ItemTabBarBinding binding;

        public TabFontViewHolder(@NonNull ItemTabBarBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface OnClickItem {
        void onClickItem(int pos);
    }
}
