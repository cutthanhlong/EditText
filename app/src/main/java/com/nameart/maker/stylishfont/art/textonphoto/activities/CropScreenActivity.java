package com.nameart.maker.stylishfont.art.textonphoto.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.nameart.maker.stylishfont.art.textonphoto.R;
import com.nameart.maker.stylishfont.art.textonphoto.utils.AppConstant;
import com.nameart.maker.stylishfont.art.textonphoto.utils.SpHelper;
import com.nameart.maker.stylishfont.art.textonphoto.utils.SystemUtil;
import com.theartofdev.edmodo.cropper.CropImageView;

public class CropScreenActivity extends AppCompatActivity implements View.OnClickListener {
    public static CropImageView cropImageView;
    public static Bitmap croppedImage;
    public static Bitmap imagePick;
    Bitmap newp;
    private ImageView imgBackCrop;
    private CropImageView cropImageView2;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        SystemUtil.setLocale(this);
        setContentView((int) R.layout.activity_screen_crop);

        findView();
        initClickView();
        initData();
    }

    private void findView() {
        cropImageView2 = (CropImageView) findViewById(R.id.cropImageView);
        imgBackCrop = findViewById(R.id.imgBackCrop);
    }

    private void initData() {
        try {
            imagePick = BitmapFactory.decodeFile(getIntent().getStringExtra("uri"));
        } catch (Exception e) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img_loadding_image);
            imagePick = bitmap;
        }
        WindowManager windowManager = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            windowManager = (WindowManager) getSystemService(CropScreenActivity.this.WINDOW_SERVICE);
        }

        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);

        int screenWidth = displayMetrics.widthPixels;
        cropImageView = cropImageView2;
        cropImageView2.setAspectRatio(1, 1);
        cropImageView.setFixedAspectRatio(true);
        cropImageView.setShowCropOverlay(true);
        try {
            if (imagePick.getWidth() >= 1300 || imagePick.getHeight() >= 1500) {
                float min = Math.min(screenWidth / ((float) imagePick.getWidth()), 1280.0f / ((float) imagePick.getHeight()));
                Matrix matrix = new Matrix();
                matrix.postScale(min, min);
                this.newp = Bitmap.createBitmap(imagePick, 0, 0, imagePick.getWidth(), imagePick.getHeight(), matrix, true);
            } else {
                this.newp = imagePick;
            }
            cropImageView.setImageBitmap(this.newp);
        } catch (Exception e) {
            Toast.makeText(this, getResources().getString(R.string.Error), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initClickView() {
        imgBackCrop.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.neutral_trang)));
        imgBackCrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBackCrop:
                onBackPressed();
                return;

            case R.id.ivdone:
                AppConstant.idiom = 1;
                croppedImage = cropImageView.getCroppedImage();
                CreateNewScreenActivity.ivBackground.setImageBitmap(croppedImage);
                CreateNewScreenActivity.rlColor.setBackgroundColor(-1);
                if (CreateNewScreenActivity.dialog.isShowing())
                    CreateNewScreenActivity.dialog.dismiss();
                SpHelper.setPosition(CropScreenActivity.this, SpHelper.ITEM_BACKGROUND, -1);
                SpHelper.setPosition(CropScreenActivity.this, SpHelper.ITEM_COLOR_BG, -1);
                SpHelper.setPosition(CropScreenActivity.this, SpHelper.ITEM_GRADIENT, -1);
                setResult(5);
                sendBroadcastToActivityThuHai();
                finish();
                return;

            case R.id.lytLeftRotate:
                cropImageView.rotateImage(-90);
                return;

            case R.id.lytRightRotate:
                cropImageView.rotateImage(90);
                return;

            default:
                return;
        }
    }

    public void sendBroadcastToActivityThuHai() {
        Intent intent = new Intent(this, LoadImageScreenActivity.class);
        intent.setAction("ACTION_CLOSE_ACTIVITY_THU_HAI");
        sendBroadcast(intent); // Gửi Broadcast
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
