package com.nameart.maker.stylishfont.art.textonphoto.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.ViewPager;

import com.nameart.maker.stylishfont.art.textonphoto.R;
import com.nameart.maker.stylishfont.art.textonphoto.adapter.SlideAdapter;
import com.nameart.maker.stylishfont.art.textonphoto.utils.SystemUtil;

public class IntroScreenActivity extends AppCompatActivity {
    ImageView[] dots = null;
    int positionPage = 0;
    ViewPager viewPager;
    TextView tvTitle, tvContent;
    CardView btnStart, btnNext;
    String[] title;
    String[] content;
    LinearLayout ll_cricle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SystemUtil.setLocale(this);
        setContentView(R.layout.activity_intro);

        viewPager = findViewById(R.id.view_pager2);
        btnNext = findViewById(R.id.btnNext);
        btnStart = findViewById(R.id.btnStart);
        tvTitle = findViewById(R.id.tv_title);
        tvContent = findViewById(R.id.tv_content);
        ll_cricle = findViewById(R.id.ll_cricle);

        dots = new ImageView[]{findViewById(R.id.cricle_1), findViewById(R.id.cricle_2), findViewById(R.id.cricle_3)};
        title = new String[]{getResources().getString(R.string.Create_Text), getResources().getString(R.string.Multiple_Backgrounds), getResources().getString(R.string.Magic_Paint)};
        content = new String[]{getResources().getString(R.string.You_can_add_favorite), getResources().getString(R.string.You_can_choose_aesthetic), getResources().getString(R.string.You_can_use_magic)};

        SlideAdapter adapter = new SlideAdapter(this);
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                positionPage = position;
            }

            @Override
            public void onPageSelected(int position) {
                changeContentInit(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToHome();
            }
        });
    }

    private void changeContentInit(int position) {
        tvTitle.setText(title[position]);
        tvContent.setText(content[position]);
        for (int i = 0; i < 3; i++) {
            if (i == position)
                dots[i].setImageResource(R.drawable.ic_dot_selected);
            else
                dots[i].setImageResource(R.drawable.ic_dot_not_select);
        }

        switch (position) {
            case 0:
            case 1:
                findViewById(R.id.txtSkip).setVisibility(View.GONE);
                ll_cricle.setVisibility(View.VISIBLE);
                btnNext.setVisibility(View.VISIBLE);
                btnStart.setVisibility(View.GONE);
                break;
            case 2:
                ll_cricle.setVisibility(View.VISIBLE);
                btnNext.setVisibility(View.GONE);
                findViewById(R.id.txtSkip).setVisibility(View.GONE);
                btnStart.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    public void goToHome() {
        startActivity(new Intent(IntroScreenActivity.this, HomeScreenActivity.class));
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        changeContentInit(viewPager.getCurrentItem());
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}