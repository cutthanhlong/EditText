package com.nameart.maker.stylishfont.art.textonphoto.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.nameart.maker.stylishfont.art.textonphoto.BuildConfig;
import com.nameart.maker.stylishfont.art.textonphoto.R;
import com.nameart.maker.stylishfont.art.textonphoto.databinding.ActivityAboutBinding;
import com.nameart.maker.stylishfont.art.textonphoto.utils.SystemUtil;

public class AboutScreenActivity extends AppCompatActivity {
    ActivityAboutBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        SystemUtil.setLocale(this);
        binding = ActivityAboutBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        initData();
        initClickView();
    }

    private void initData() {
        binding.txtName.setText(getResources().getString(R.string.app_name));
        binding.txtVersion.setText(getResources().getString(R.string.version) + " " + BuildConfig.VERSION_NAME);
    }

    private void initClickView() {
        binding.imgBack.setOnClickListener(view -> {
            onBackPressed();
        });

        binding.txtPrivatePolicy.setOnClickListener(view -> {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://firebasestorage.googleapis.com/v0/b/name-art---stylish-text.appspot.com/o/Privacy Policy.html?alt=media&token=153bd925-726f-4b2f-aa89-149c6f26713d")));
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
