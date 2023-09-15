package com.nameart.maker.stylishfont.art.textonphoto.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nameart.maker.stylishfont.art.textonphoto.R;
import com.nameart.maker.stylishfont.art.textonphoto.adapter.LanguageAdapter;
import com.nameart.maker.stylishfont.art.textonphoto.model.Language;
import com.nameart.maker.stylishfont.art.textonphoto.utils.SpHelper;
import com.nameart.maker.stylishfont.art.textonphoto.utils.SystemUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LanguageScreenActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ImageView btn_back;
    List<Language> listLanguage;
    String codeLang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SystemUtil.setLocale(this);
        setContentView(R.layout.activity_language);

        recyclerView = findViewById(R.id.recyclerView);
        btn_back = findViewById(R.id.imgBack);
        codeLang = Locale.getDefault().getLanguage();

        initData();
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        LanguageAdapter languageAdapter = new LanguageAdapter(listLanguage, code -> codeLang = code, this);


        findViewById(R.id.imgDone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SpHelper.setString(LanguageScreenActivity.this, SpHelper.ITEM_LANGUAGE, codeLang);
                SystemUtil.saveLocale(getBaseContext(), codeLang);
                back();
            }
        });

        languageAdapter.setCheck(SystemUtil.getPreLanguage(getBaseContext()));


        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(languageAdapter);
        //   recyclerView.addItemDecoration(itemDecoration);
    }

    @Override
    protected void onResume() {
        super.onResume();
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
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void back() {
        startActivity(new Intent(LanguageScreenActivity.this, HomeScreenActivity.class));
        finishAffinity();
    }

}
