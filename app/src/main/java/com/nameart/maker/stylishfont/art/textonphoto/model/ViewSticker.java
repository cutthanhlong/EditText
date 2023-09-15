package com.nameart.maker.stylishfont.art.textonphoto.model;

import android.widget.TextView;

import androidx.cardview.widget.CardView;

public class ViewSticker {
    private CardView cardView;
    private TextView textView;

    public ViewSticker(CardView cardView, TextView textView) {
        this.cardView = cardView;
        this.textView = textView;
    }

    public CardView getCardView() {
        return cardView;
    }

    public void setCardView(CardView cardView) {
        this.cardView = cardView;
    }

    public TextView getTextView() {
        return textView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }
}
