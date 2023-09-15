package com.nameart.maker.stylishfont.art.textonphoto.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.nameart.maker.stylishfont.art.textonphoto.R;
import com.nameart.maker.stylishfont.art.textonphoto.model.Language;
import com.nameart.maker.stylishfont.art.textonphoto.utils.SharePrefUtils;
import com.nameart.maker.stylishfont.art.textonphoto.utils.SomeThingApp;
import com.nameart.maker.stylishfont.art.textonphoto.utils.SpHelper;
import com.nameart.maker.stylishfont.art.textonphoto.utils.SystemUtil;

import java.util.ArrayList;
import java.util.List;

public class SettingScreenActivity extends AppCompatActivity {
    private ImageView imgBack;
    private LinearLayout llAbout, llMoreApp, llLanguage, llShare;
    public static LinearLayout llRate;
    public static View v1;
    private View v3;
    TextView txtLanguage;
    List<Language> listLanguage;
    private boolean checkClickAbout = true;
    private boolean checkClickLg = true;
    public static boolean checkClickRate = true;
    public static boolean checkClickShare = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        SystemUtil.setLocale(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mapping();
        initData();
        goneView();
        click();
    }

    private void mapping() {
        imgBack = findViewById(R.id.imgBack);
        llAbout = findViewById(R.id.llAbout);
        llMoreApp = findViewById(R.id.llMoreApp);
        llLanguage = findViewById(R.id.llLanguage);
        txtLanguage = findViewById(R.id.tv_text_language);
        llRate = findViewById(R.id.llRate);
        llShare = findViewById(R.id.llShare);
        v3 = findViewById(R.id.v3);
        v1 = findViewById(R.id.v1);
    }

    private void goneView() {
        llMoreApp.setVisibility(View.GONE);
        v3.setVisibility(View.GONE);

        if (SharePrefUtils.isRated(this)) {
            llRate.setVisibility(View.GONE);
            v1.setVisibility(View.GONE);
        }
    }

    private void initData() {
        listLanguage = new ArrayList<>();
        listLanguage.add(new Language("English", "en"));
        listLanguage.add(new Language("Portuguese", "pt"));
        listLanguage.add(new Language("Spanish", "es"));
        listLanguage.add(new Language("German", "de"));
        listLanguage.add(new Language("French", "fr"));
        listLanguage.add(new Language("China", "zh"));
        listLanguage.add(new Language("Hindi", "hi"));
        listLanguage.add(new Language("Indonesia", "in"));
        for (int i = 0; i < listLanguage.size(); i++) {
            if (SpHelper.getString(SettingScreenActivity.this, SpHelper.ITEM_LANGUAGE, "en").equals(listLanguage.get(i).getCode())) {
                txtLanguage.setText(listLanguage.get(i).getName());
            }
        }
    }

    private void click() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        llAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkClickAbout) {
                    startActivity(new Intent(SettingScreenActivity.this, AboutScreenActivity.class));
                    checkClickAbout = false;
                }
            }
        });

        llLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkClickLg) {
                    startActivity(new Intent(SettingScreenActivity.this, LanguageScreenActivity.class));
                    checkClickLg = false;
                }
            }
        });

        llRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkClickRate) {
                    if (!SharePrefUtils.isRated(SettingScreenActivity.this)) {
                        SomeThingApp.rateApp(SettingScreenActivity.this, 0);
                    }
                    checkClickRate = false;
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        checkClickRate = true;
                    }
                }, 3000);
            }
        });

        llShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkClickShare) {
                    SomeThingApp.shareApp(SettingScreenActivity.this);
                    checkClickShare = false;
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        checkClickShare = true;
                    }
                }, 1000);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!checkClickAbout) {
            checkClickAbout = true;
        }

        if (!checkClickLg) {
            checkClickLg = true;
        }
    }
}
