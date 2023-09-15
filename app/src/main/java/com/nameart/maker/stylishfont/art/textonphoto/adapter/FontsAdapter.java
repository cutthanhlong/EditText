package com.nameart.maker.stylishfont.art.textonphoto.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.nameart.maker.stylishfont.art.textonphoto.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FontsAdapter extends BaseAdapter {
    private String fontType;
    private String[] fonts;
    private Context mContext;
    OnClickItem onClickItem;
    private Boolean setClick;
    private int index;
    private int indexTabFont;
    private int tabFont;

    public FontsAdapter(Context mContext, String fontType, String[] fonts, Boolean setClick, int index, int indexTabFont, OnClickItem onClickItem) {
        this.mContext = mContext;
        this.fontType = fontType;
        this.fonts = fonts;
        this.setClick = setClick;
        this.index = index;
        this.indexTabFont = indexTabFont;
        this.onClickItem = onClickItem;
    }

    public int getCount() {
        return this.fonts.length;
    }

    public Object getItem(int position) {
        return this.fonts[position];
    }

    public long getItemId(int position) {
        return (long) this.fonts[position].length();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(this.mContext, R.layout.item_fonts, null);
        TextView fontSample = (TextView) view.findViewById(R.id.font_sample);
        if (this.fontType.equals("عربي")) {
            fontSample.setText("نموذج");
        } else {
            fontSample.setText(mContext.getResources().getString(R.string.Sample));
        }
        fontSample.setTypeface(Typeface.createFromAsset(this.mContext.getAssets(), "Fonts/" + this.fontType + "/" + this.fonts[position]));
        MaterialCardView mcvItemFonts = view.findViewById(R.id.recipe);
        SharedPreferences prefs = this.mContext.getSharedPreferences("BlockedResources", 0);
        SimpleDateFormat df = new SimpleDateFormat("yyyy:MM:dd:HH:mm");
        String currentTime = df.format(Calendar.getInstance().getTime());
        String additionalTime = prefs.getString("fontsAddTime", currentTime);

        mcvItemFonts.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                tabFont = indexTabFont;
                if (setClick)
                    index = position;
                onClickItem.onClickItem(position);
                updateList();
            }
        });
        if (index == position) {
            mcvItemFonts.setStrokeColor(ColorStateList.valueOf(mContext.getResources().getColor(R.color.color_xanh_0C9CED)));
            mcvItemFonts.setCardBackgroundColor(ColorStateList.valueOf(mContext.getResources().getColor(R.color.color_F5FBFF)));
        } else {
            mcvItemFonts.setStrokeColor(ColorStateList.valueOf(mContext.getResources().getColor(R.color.color_DAD1C2)));
            mcvItemFonts.setCardBackgroundColor(ColorStateList.valueOf(mContext.getResources().getColor(R.color.color_FEF7F6)));
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
