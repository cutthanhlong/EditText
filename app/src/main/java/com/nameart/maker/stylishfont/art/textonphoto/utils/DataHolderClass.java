package com.nameart.maker.stylishfont.art.textonphoto.utils;

import android.graphics.Color;
import android.graphics.MaskFilter;
import android.graphics.Shader;
import android.graphics.Typeface;
import java.io.File;
import java.util.ArrayList;

public class DataHolderClass {
    private static DataHolderClass dataobject = null;
    private boolean addText;
    private String color = "#000000";
    private int color1 = Color.parseColor("#F6CB60");
    private boolean editTextStickerView;
    private Typeface font;
    private boolean fromCreation;
    private int layout_x;
    private int layout_y;
    private File mImagePath;
    private float mRotateAngle;
    private float mScale;
    private MaskFilter maskFilter = null;
    private boolean rewarded;
    private ArrayList<String> rndNumbers = new ArrayList();
    private int shadowColor;
    private int shadowRadius;
    private int shadowXY;
    private String text;
    private Shader tvShader;

    private DataHolderClass() {
    }

    public static DataHolderClass getInstance() {
        if (dataobject == null) {
            dataobject = new DataHolderClass();
        }
        return dataobject;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isAddText() {
        return this.addText;
    }

    public void setAddText(boolean addText) {
        this.addText = addText;
    }

    public Typeface getFont() {
        return this.font;
    }

    public void setFont(Typeface font) {
        this.font = font;
    }

    public String getColor() {
        return this.color;
    }
    public int getColor1() {
        return this.color1;
    }

    public void setColor(String color) {
        this.color = color;
    }
    public  void setColor1(int color1){
        this.color1 = color1;
    }

    public int getShadowXY() {
        return this.shadowXY;
    }

    public void setShadowXY(int shadowXY) {
        this.shadowXY = shadowXY;
    }

    public int getShadowRadius() {
        return this.shadowRadius;
    }

    public void setShadowRadius(int shadowRadius) {
        this.shadowRadius = shadowRadius;
    }

    public int getShadowColor() {
        return this.shadowColor;
    }

    public void setShadowColor(int shadowColor) {
        this.shadowColor = shadowColor;
    }

    public int getLayout_x() {
        return this.layout_x;
    }

    public void setLayout_x(int layout_x) {
        this.layout_x = layout_x;
    }

    public int getLayout_y() {
        return this.layout_y;
    }

    public void setLayout_y(int layout_y) {
        this.layout_y = layout_y;
    }

    public float getmRotateAngle() {
        return this.mRotateAngle;
    }

    public void setmRotateAngle(float mRotateAngle) {
        this.mRotateAngle = mRotateAngle;
    }

    public float getmScale() {
        return this.mScale;
    }

    public void setmScale(float mScale) {
        this.mScale = mScale;
    }

    public Shader getTvShader() {
        return this.tvShader;
    }

    public void setTvShader(Shader tvShader) {
        this.tvShader = tvShader;
    }

    public MaskFilter getMaskFilter() {
        return this.maskFilter;
    }

    public void setMaskFilter(MaskFilter maskFilter) {
        this.maskFilter = maskFilter;
    }

    public boolean isEditTextStickerView() {
        return this.editTextStickerView;
    }

    public void setEditTextStickerView(boolean editTextStickerView) {
        this.editTextStickerView = editTextStickerView;
    }

    public File getmImagePath() {
        return this.mImagePath;
    }

    public void setmImagePath(File mImagePath) {
        this.mImagePath = mImagePath;
    }

    public boolean isFromCreation() {
        return this.fromCreation;
    }

    public void setFromCreation(boolean fromCreation) {
        this.fromCreation = fromCreation;
    }

    public ArrayList<String> getRndNumbers() {
        return this.rndNumbers;
    }

    public void setRndNumbers(ArrayList<String> rndNumbers) {
        this.rndNumbers = rndNumbers;
    }

    public boolean isRewarded() {
        return this.rewarded;
    }

    public void setRewarded(boolean rewarded) {
        this.rewarded = rewarded;
    }
}
