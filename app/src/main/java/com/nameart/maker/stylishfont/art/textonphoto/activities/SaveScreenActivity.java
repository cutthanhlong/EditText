package com.nameart.maker.stylishfont.art.textonphoto.activities;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.nameart.maker.stylishfont.art.textonphoto.BuildConfig;
import com.nameart.maker.stylishfont.art.textonphoto.R;
import com.nameart.maker.stylishfont.art.textonphoto.utils.DataHolderClass;
import com.nameart.maker.stylishfont.art.textonphoto.utils.IntentShareUtil;
import com.nameart.maker.stylishfont.art.textonphoto.utils.SpHelper;
import com.nameart.maker.stylishfont.art.textonphoto.utils.SystemUtil;

import java.io.File;

public class SaveScreenActivity extends AppCompatActivity {
    private Uri fileUri;
    private ImageView image;
    private String shortUrl;
    private IntentShareUtil intentShareUtil;
    private ImageView imgHome, imgShare, imgEdit, imgBack;
    private boolean isShareClicked = true;
    Uri phototUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SystemUtil.setLocale(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);

        imgHome = findViewById(R.id.imgHome);
        imgShare = findViewById(R.id.imgShare);
        imgEdit = findViewById(R.id.imgEdit);
        imgBack = findViewById(R.id.imgBack);

        shortUrl = "   " + "https://play.google.com/store/apps/details?id=" + getPackageName();

        setImage();
        File photoFile = DataHolderClass.getInstance().getmImagePath();
        phototUri = Uri.fromFile(photoFile);

        if (Build.VERSION.SDK_INT >= 24) {
            fileUri = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".fileprovider", photoFile);
        } else {
            try {
                fileUri = Uri.fromFile(photoFile);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        intentShareUtil = new IntentShareUtil(this);
        onClickItem();
    }

    private void onClickItem() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SpHelper.setBoolean(SaveScreenActivity.this, SpHelper.IS_COLOR_TEXT, false);
                SpHelper.setPosition(SaveScreenActivity.this, SpHelper.SELECTED_FONT, 0);
                SpHelper.setPosition(SaveScreenActivity.this, SpHelper.SELECTED_ITEM_FONT, -1);
                SpHelper.putValueInSharedpreference(SaveScreenActivity.this, SpHelper.COLOR_TEXT, "#FFFFFFFF");

                SpHelper.setPosition(SaveScreenActivity.this, SpHelper.ITEM_BACKGROUND, -1);
                SpHelper.setPosition(SaveScreenActivity.this, SpHelper.ITEM_GRADIENT, -1);
                SpHelper.setPosition(SaveScreenActivity.this, SpHelper.ITEM_COLOR_BG, -1);

                SpHelper.setPosition(SaveScreenActivity.this, SpHelper.ITEM_PATTERN, -1);

                SpHelper.setString(SaveScreenActivity.this, SpHelper.TYPE_GRADIENT_TEXT, "");
                SpHelper.setPosition(SaveScreenActivity.this, SpHelper.ITEM_GRADIENT_TEXT, -1);
                startActivity(new Intent(SaveScreenActivity.this, CreateNewScreenActivity.class));
                finish();
            }
        });
        imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataHolderClass.getInstance().getmImagePath().delete();
                finish();
            }
        });
        imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isShareClicked) {
                    shareFileImage(phototUri.toString());
                    isShareClicked = false;
                }
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void setImage() {
        this.image = (ImageView) findViewById(R.id.image);
        if (DataHolderClass.getInstance().getmImagePath() != null) {
            File imgFile = DataHolderClass.getInstance().getmImagePath();
            if (imgFile.exists()) {
                this.image.setImageBitmap(BitmapFactory.decodeFile(imgFile.getAbsolutePath()));
            }
        }
    }

    String uri1 = "";

    private void shareFileImage(String path) {
        Log.d("TAG", "shareFileVideo: " + path);
        File file;
        if (path.contains("content://com."))
            intentFile(new File(Uri.parse(path).getPath()));
        else if (path.contains("content://")) {
            uri1 = "file://" + path;
            file = new File(Uri.parse(uri1).getPath());
            intentFile(file);
        } else {
            uri1 = "file://" + path;
            file = new File(Uri.parse(uri1).getPath());
            intentFile(file);
        }
    }

    private void intentFile(File file) {
        String packageName = getApplicationContext().getPackageName();

        if (file.exists()) {
            Uri _uri = FileProvider.getUriForFile(this,
                    packageName + ".fileprovider", file);
            Intent intent2 = new Intent(Intent.ACTION_SEND);
            intent2.putExtra("android.intent.extra.SUBJECT", getResources().getString(R.string.app_name));
            intent2.setType("video/*");
            intent2.putExtra("android.intent.extra.STREAM", _uri);
            intent2.putExtra("android.intent.extra.TEXT", "Image");
            startActivity(Intent.createChooser(intent2, "Where to Share?"));
        }
    }

    public void onBackPressed() {
        if (DataHolderClass.getInstance().isFromCreation()) {
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            DataHolderClass.getInstance().setFromCreation(false);
        } else {
            Intent intent = new Intent(this, HomeScreenActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            SpHelper.setPosition(SaveScreenActivity.this, SpHelper.SELECTED_FONT, 0);
            SpHelper.setPosition(SaveScreenActivity.this, SpHelper.SELECTED_ITEM_FONT, -1);
            SpHelper.putValueInSharedpreference(SaveScreenActivity.this, SpHelper.COLOR_TEXT, "#FFFFFFFF");
            SpHelper.setBoolean(SaveScreenActivity.this, SpHelper.IS_COLOR_TEXT, false);

            SpHelper.setPosition(SaveScreenActivity.this, SpHelper.ITEM_BACKGROUND, -1);
            SpHelper.setPosition(SaveScreenActivity.this, SpHelper.ITEM_GRADIENT, -1);
            SpHelper.setPosition(SaveScreenActivity.this, SpHelper.ITEM_COLOR_BG, -1);

            SpHelper.setPosition(SaveScreenActivity.this, SpHelper.ITEM_PATTERN, -1);

            SpHelper.setString(SaveScreenActivity.this, SpHelper.TYPE_GRADIENT_TEXT, "");
            SpHelper.setPosition(SaveScreenActivity.this, SpHelper.ITEM_GRADIENT_TEXT, -1);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            DataHolderClass.getInstance().setText(null);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!isShareClicked) {
            isShareClicked = true;
        }
    }
}
