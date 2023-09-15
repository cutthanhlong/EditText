package com.nameart.maker.stylishfont.art.textonphoto.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.nameart.maker.stylishfont.art.textonphoto.R;
import com.nameart.maker.stylishfont.art.textonphoto.utils.SharePrefUtils;
import com.nameart.maker.stylishfont.art.textonphoto.utils.SpHelper;
import com.nameart.maker.stylishfont.art.textonphoto.utils.SystemUtil;


public class SplashScreenActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        SystemUtil.setLocale(this);
        setContentView((int) R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (SpHelper.getBoolean(SplashScreenActivity.this, SpHelper.CHECK_LANGUAGE, false)) {
                    SplashScreenActivity.this.startActivity(new Intent(SplashScreenActivity.this, IntroScreenActivity.class));
                    SplashScreenActivity.this.finish();
                } else {
                    SplashScreenActivity.this.startActivity(new Intent(SplashScreenActivity.this, LanguageStartScreenActivity.class));
                    //Langua
                    SplashScreenActivity.this.finish();
                }
            }
        }, 3000);

        SharePrefUtils.increaseCountOpenApp(SplashScreenActivity.this);
    }

    @Override
    public void onBackPressed() {
    }
}