package com.nameart.maker.stylishfont.art.textonphoto.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nameart.maker.stylishfont.art.textonphoto.R;
import com.nameart.maker.stylishfont.art.textonphoto.adapter.LanguageStartAdapter;
import com.nameart.maker.stylishfont.art.textonphoto.interfaces.IClickItemLanguage;
import com.nameart.maker.stylishfont.art.textonphoto.model.Language;
import com.nameart.maker.stylishfont.art.textonphoto.utils.SharePrefUtils;
import com.nameart.maker.stylishfont.art.textonphoto.utils.SpHelper;
import com.nameart.maker.stylishfont.art.textonphoto.utils.SystemUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class LanguageStartScreenActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ImageView btn_done;
    List<Language> listLanguage;
    String codeLang;
    private boolean checkLanguage = false;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SystemUtil.setLocale(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_start);
        recyclerView = findViewById(R.id.recyclerView);
        btn_done = findViewById(R.id.imgDone);
        codeLang = Locale.getDefault().getLanguage();
        initData();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        LanguageStartAdapter languageAdapter = new LanguageStartAdapter(listLanguage, new IClickItemLanguage() {
            @Override
            public void onClickItemLanguage(String code) {
                codeLang = code;
            }
        }, this);

        // set checked default lang local
        String codeLangDefault = Locale.getDefault().getLanguage();
        String[] langDefault = {"hi", "zh", "es", "fr", "pt", "in", "de"};
        if (!Arrays.asList(langDefault).contains(codeLangDefault)) codeLang = "en";

        languageAdapter.setCheck(codeLang);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(languageAdapter);

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharePrefUtils.increaseCountFirstHelp(LanguageStartScreenActivity.this);
                SystemUtil.saveLocale(getBaseContext(), codeLang);
                SpHelper.setString(LanguageStartScreenActivity.this, SpHelper.ITEM_LANGUAGE, codeLang);
                checkLanguage = true;
                SpHelper.setBoolean(LanguageStartScreenActivity.this, SpHelper.CHECK_LANGUAGE, checkLanguage);
                startActivity(new Intent(LanguageStartScreenActivity.this, IntroScreenActivity.class).putExtra("INTRO_FROM_SPLASH", true));
                finish();
            }
        });
    }

    private void initData() {
        listLanguage = new ArrayList<>();
        String lang = Locale.getDefault().getLanguage();
        listLanguage.add(new Language("English", "en"));
        listLanguage.add(new Language("China", "zh"));
        listLanguage.add(new Language("French", "fr"));
        listLanguage.add(new Language("German", "de"));
        listLanguage.add(new Language("Hindi", "hi"));
        listLanguage.add(new Language("Indonesia", "in"));
        listLanguage.add(new Language("Portuguese", "pt"));
        listLanguage.add(new Language("Spanish", "es"));

        for (int i = 0; i < listLanguage.size(); i++) {
            if (listLanguage.get(i).getCode().equals(lang)) {
                listLanguage.add(0, listLanguage.get(i));
                listLanguage.remove(i + 1);
            }
        }
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}
