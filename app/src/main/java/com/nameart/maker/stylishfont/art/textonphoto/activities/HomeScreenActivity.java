package com.nameart.maker.stylishfont.art.textonphoto.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.nameart.maker.stylishfont.art.textonphoto.R;
import com.nameart.maker.stylishfont.art.textonphoto.utils.SharePrefUtils;
import com.nameart.maker.stylishfont.art.textonphoto.utils.SomeThingApp;
import com.nameart.maker.stylishfont.art.textonphoto.utils.SpHelper;
import com.nameart.maker.stylishfont.art.textonphoto.utils.SystemUtil;

import java.util.ArrayList;
import java.util.Arrays;

public class HomeScreenActivity extends Activity implements View.OnClickListener {
    MaterialCardView mcvCreateNew, mcvMyGallery;
    ImageView imgSetting;
    private boolean checkClickCreateNew = true;
    private boolean checkClickMyGallery = true;
    private boolean checkClickSetting = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SystemUtil.setLocale(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mapping();
        clickItem();
        saveShared();
    }

    private void mapping() {
        mcvCreateNew = findViewById(R.id.mcvCreateNew);
        mcvMyGallery = findViewById(R.id.mcvMyGallery);
        imgSetting = findViewById(R.id.imgSetting);
    }

    private void clickItem() {
        mcvCreateNew.setOnClickListener(this);
        mcvMyGallery.setOnClickListener(this);
        imgSetting.setOnClickListener(this);
    }

    private void saveShared() {
        SpHelper.setPosition(HomeScreenActivity.this, SpHelper.SELECTED_FONT, 0);
        SpHelper.setPosition(HomeScreenActivity.this, SpHelper.SELECTED_ITEM_FONT, -1);

        SpHelper.setPosition(HomeScreenActivity.this, SpHelper.ITEM_BACKGROUND, -1);
        SpHelper.setPosition(HomeScreenActivity.this, SpHelper.ITEM_GRADIENT, -1);
        SpHelper.setPosition(HomeScreenActivity.this, SpHelper.ITEM_COLOR_BG, -1);

        SpHelper.putValueInSharedpreference(HomeScreenActivity.this, SpHelper.COLOR_TEXT, "#FFFFFFFF");
        SpHelper.setBoolean(HomeScreenActivity.this, SpHelper.IS_COLOR_TEXT, false);

        SpHelper.setPosition(HomeScreenActivity.this, SpHelper.ITEM_PATTERN, -1);

        SpHelper.setString(HomeScreenActivity.this, SpHelper.TYPE_GRADIENT_TEXT, "");
        SpHelper.setPosition(HomeScreenActivity.this, SpHelper.ITEM_GRADIENT_TEXT, -1);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mcvCreateNew:
                if (checkClickCreateNew) {
                    startActivity(new Intent(this, CreateNewScreenActivity.class));
                    checkClickCreateNew = false;
                }
                break;

            case R.id.mcvMyGallery:
                if (checkClickMyGallery) {
                    startActivity(new Intent(this, MyGalleryScreenActivity.class));
                    checkClickMyGallery = false;
                }
                break;

            case R.id.imgSetting:
                if (checkClickSetting) {
                    startActivity(new Intent(this, SettingScreenActivity.class));
                    checkClickSetting = false;
                }
                break;
        }
    }

    private void showExitDialog() {
        TextView txtCancel, txtQuit;
        final Dialog dialog = new Dialog(HomeScreenActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_quit_app);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        txtCancel = dialog.findViewById(R.id.tv_cancel);
        txtQuit = dialog.findViewById(R.id.txtQuit);

        txtCancel.setOnClickListener(view -> dialog.dismiss());

        txtQuit.setOnClickListener(view -> {
            dialog.dismiss();
            finish();
        });

        dialog.show();
    }

    ArrayList<String> remoteRate = new ArrayList<String>(Arrays.asList("2", "4", "6", "8", "10"));

    @Override
    public void onBackPressed() {
        if (!SharePrefUtils.isRated(this)) {
            String cout = String.valueOf(SharePrefUtils.getCountOpenApp(this));
            Log.e("abcdg", "Cout: " + cout);
            if (remoteRate.contains(cout)) {
                SomeThingApp.rateApp(HomeScreenActivity.this, 1);
            } else {
                showExitDialog();
            }
        } else {
            showExitDialog();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!checkClickCreateNew) {
            checkClickCreateNew = true;
        }

        if (!checkClickMyGallery) {
            checkClickMyGallery = true;
        }

        if (!checkClickSetting) {
            checkClickSetting = true;
        }
    }
}
