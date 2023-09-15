package com.nameart.maker.stylishfont.art.textonphoto.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SpHelper {
    public static final String PREFERENCE = "ColorCallScreen2021";
    public static String CHECK_LANGUAGE = "language";
    public static String SELECTED_FONT = "font position: ";
    public static String SELECTED_ITEM_FONT = "item font position: ";
    public static String COLOR_TEXT = "COLOR: ";
    public static String IS_COLOR_TEXT = "IS_COLOR: ";
    public static String BACKGROUND = "BACKGROUND: ";
    public static String ITEM_IMAGE = "ITEM_IMAGE: ";
    public static String ITEM_GRADIENT = "ITEM_GRADIENT: ";
    public static String ITEM_GRADIENT_SAVE = "ITEM_GRADIENT_SAVE: ";
    public static String ITEM_PATTERN = "ITEM_PATTERN: ";
    public static String TYPE_GRADIENT = "TYPE_GRADIENT: ";
    public static String ITEM_GRADIENT_TEXT = "ITEM_GRADIENT_TEXT: ";
    public static String TYPE_GRADIENT_TEXT = "TYPE_GRADIENT_TEXT: ";
    public static String ITEM_COLOR_BG = "ITEM_COLOR_BG: ";
    public static String ITEM_COLOR_BG_SAVE = "ITEM_COLOR_BG_SAVE: ";
    public static String ITEM_BACKGROUND = "ITEM_BACKGROUND: ";
    public static String ITEM_BACKGROUND_SAVE = "ITEM_BACKGROUND_SAVE: ";
    public static String ITEM_LANGUAGE = "ITEM_LANGUAGE: ";

    public static void putValueInSharedpreference(Context context, String str, String str2) {
        SharedPreferences.Editor edit = context.getSharedPreferences(PREFERENCE, 0).edit();
        edit.putString(str, str2);
        edit.apply();
    }

    public static String getValueFromSharedprefrence(Context context, String str, String str2) {
        return context.getSharedPreferences(PREFERENCE, 0).getString(str, str2);
    }

    public static void setString(Context context, String str, String str2) {
        SharedPreferences.Editor edit = context.getSharedPreferences(PREFERENCE, 0).edit();
        edit.putString(str, str2);
        edit.apply();
    }

    public static String getString(Context context, String str, String str2) {
        return context.getSharedPreferences(PREFERENCE, 0).getString(str, str2);
    }

    public static void setLong(Context context, String str, long i) {
        SharedPreferences.Editor edit = context.getSharedPreferences(PREFERENCE, 0).edit();
        edit.putLong(str, i);
        edit.apply();
    }

    public static long getLong(Context context, String str, long i) {
        return context.getSharedPreferences(PREFERENCE, 0).getLong(str, i);
    }

    public static void setPosition(Context context, String str, int i) {
        SharedPreferences.Editor edit = context.getSharedPreferences(PREFERENCE, 0).edit();
        edit.putInt(str, i);
        edit.apply();
    }

    public static int getPosition(Context context, String str, int i) {
        return context.getSharedPreferences(PREFERENCE, 0).getInt(str, i);
    }

    public static void setBoolean(Context context, String str, boolean b){
        SharedPreferences.Editor edit = context.getSharedPreferences(PREFERENCE, 0).edit();
        edit.putBoolean(str, b);
        edit.apply();
    }

    public static boolean getBoolean(Context context, String str, boolean b){
        return context.getSharedPreferences(PREFERENCE, 0).getBoolean(str, b);
    }
}
