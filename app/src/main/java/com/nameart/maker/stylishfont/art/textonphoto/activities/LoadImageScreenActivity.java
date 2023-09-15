package com.nameart.maker.stylishfont.art.textonphoto.activities;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.nameart.maker.stylishfont.art.textonphoto.R;
import com.nameart.maker.stylishfont.art.textonphoto.adapter.MyCreationAdapter;
import com.nameart.maker.stylishfont.art.textonphoto.utils.SystemUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class LoadImageScreenActivity extends AppCompatActivity {

    List<Uri> imageList = new ArrayList<>();
    ArrayList<Uri> screenshotList = new ArrayList<>();
    List<Uri> listImage;
    ArrayList<Uri> listCamera = new ArrayList<>();

    MyCreationAdapter myCreationAdapter;
    RecyclerView rcvListImg, rcvListImg1;

    TextView txtName, txtAll, txtDownloads, txtScreenshorts, txtCamera;
    String[] extensions = new String[]{"jpg", "jpeg", "JPG", "JPEG", "png"};
    private boolean isselected = false;
    private boolean isselected1 = false;
    private boolean isselected2 = false;
    private boolean isselected3 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SystemUtil.setLocale(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_image);
        findView();

        txtName.setText(getResources().getString(R.string.Select_Image));
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if (ContextCompat.checkSelfPermission(LoadImageScreenActivity.this.getApplicationContext(), "android.permission.READ_EXTERNAL_STORAGE") != 0
                || ContextCompat.checkSelfPermission(LoadImageScreenActivity.this.getApplicationContext(), "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            if (Build.VERSION.SDK_INT >= 23) {
                LoadImageScreenActivity.this.requestPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"}, 922);
            }
        }

        onClick();
    }

    private void findView() {
        rcvListImg = findViewById(R.id.rcvListImg);
        rcvListImg1 = findViewById(R.id.rcvListImg1);
        txtAll = findViewById(R.id.txtAll);
        txtDownloads = findViewById(R.id.txtDownloads);
        txtScreenshorts = findViewById(R.id.txtScreenshorts);
        txtCamera = findViewById(R.id.txtCamera);
        txtName = findViewById(R.id.txtName);
    }

    private void onClick() {
        findViewById(R.id.imgBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        txtAll.setOnClickListener(view -> {
            refreshImage();
            changeColor("All");
        });
        txtDownloads.setOnClickListener(view -> {
            loadListDowload();
            changeColor("Downloads");
        });
        txtScreenshorts.setOnClickListener(view -> {
            changeColor("Screenshorts");

            myCreationAdapter = new MyCreationAdapter(LoadImageScreenActivity.this, getAllScreenshots1(), new MyCreationAdapter.OnClickImage() {
                @Override
                public void onClickImage(int pos) {
                    if (!isselected3) {
                        Intent intent = new Intent(LoadImageScreenActivity.this, CropScreenActivity.class);
                        intent.putExtra("uri", screenshotList.get(pos).getPath());
                        startActivityForResult(intent, 5);
                        isselected3 = true;
                    }
                }
            });
            rcvListImg.setAdapter(myCreationAdapter);
            if (screenshotList.size() == 0) {
                findViewById(R.id.layoutNoPics).setVisibility(View.VISIBLE);
            } else {
                findViewById(R.id.layoutNoPics).setVisibility(View.GONE);
            }
        });
        txtCamera.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                changeColor("Camera");
                myCreationAdapter = new MyCreationAdapter(LoadImageScreenActivity.this, getAllCamera(), new MyCreationAdapter.OnClickImage() {
                    @Override
                    public void onClickImage(int pos) {
                        if (!isselected2) {
                            Intent intent = new Intent(LoadImageScreenActivity.this, CropScreenActivity.class);
                            intent.putExtra("uri", listCamera.get(pos).getPath());
                            startActivityForResult(intent, 5);
                            isselected2 = true;
                        }
                    }
                });
                rcvListImg.setAdapter(myCreationAdapter);
                if (listCamera.size() == 0) {
                    findViewById(R.id.layoutNoPics).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.layoutNoPics).setVisibility(View.GONE);
                }
            }
        });
    }

    private void changeColor(String str) {
        if (str.equals("All")) {
            txtAll.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.color_464646)));
            txtCamera.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.color_636363)));
            txtScreenshorts.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.color_636363)));
            txtDownloads.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.color_636363)));

            txtAll.setBackgroundResource(R.drawable.bg_exit);
            txtCamera.setBackgroundResource(R.drawable.bg_folder);
            txtScreenshorts.setBackgroundResource(R.drawable.bg_folder);
            txtDownloads.setBackgroundResource(R.drawable.bg_folder);
        } else if (str.equals("Camera")) {
            txtAll.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.color_636363)));
            txtCamera.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.color_464646)));
            txtScreenshorts.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.color_636363)));
            txtDownloads.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.color_636363)));

            txtAll.setBackgroundResource(R.drawable.bg_folder);
            txtCamera.setBackgroundResource(R.drawable.bg_exit);
            txtScreenshorts.setBackgroundResource(R.drawable.bg_folder);
            txtDownloads.setBackgroundResource(R.drawable.bg_folder);
        } else if (str.equals("Screenshorts")) {
            txtAll.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.color_636363)));
            txtCamera.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.color_636363)));
            txtScreenshorts.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.color_464646)));
            txtDownloads.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.color_636363)));

            txtAll.setBackgroundResource(R.drawable.bg_folder);
            txtCamera.setBackgroundResource(R.drawable.bg_folder);
            txtScreenshorts.setBackgroundResource(R.drawable.bg_exit);
            txtDownloads.setBackgroundResource(R.drawable.bg_folder);
        } else {
            txtAll.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.color_636363)));
            txtCamera.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.color_636363)));
            txtScreenshorts.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.color_636363)));
            txtDownloads.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.color_464646)));

            txtAll.setBackgroundResource(R.drawable.bg_folder);
            txtCamera.setBackgroundResource(R.drawable.bg_folder);
            txtScreenshorts.setBackgroundResource(R.drawable.bg_folder);
            txtDownloads.setBackgroundResource(R.drawable.bg_exit);
        }
    }

    private void getImage() {
        // Lấy danh sách ảnh từ thư viện
        String[] projection = {MediaStore.Images.Media.DATA};
        String sortOrder = MediaStore.Images.Media.DATE_TAKEN + " DESC";
        Cursor cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                sortOrder
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                // Thêm Uri của ảnh vào danh sách
                @SuppressLint("Range")
                String imagePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                Uri imageUri = Uri.parse(imagePath);
                imageList.add(imageUri);
            }
            cursor.close();
        }

        // Hiển thị danh sách ảnh trên RecyclerView
        myCreationAdapter = new MyCreationAdapter(this, imageList, new MyCreationAdapter.OnClickImage() {
            @Override
            public void onClickImage(int pos) {
                if (!isselected1) {
                    Intent intent = new Intent(LoadImageScreenActivity.this, CropScreenActivity.class);
                    intent.putExtra("uri", imageList.get(pos).getPath());
                    startActivityForResult(intent, 5);
                    isselected1 = true;
                }
            }
        });
        Log.d("All", imageList.size() + "");
        rcvListImg.setAdapter(myCreationAdapter);
        if (imageList.size() == 0) {
            findViewById(R.id.layoutNoPics).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.layoutNoPics).setVisibility(View.GONE);
        }
    }

    private void refreshImage() {
        // Xóa danh sách ảnh cũ
        imageList.clear();

        // Load lại danh sách ảnh mới
        getImage();

        // Cập nhật lại adapter trên RecyclerView
        myCreationAdapter.notifyDataSetChanged();
    }


    public void loadListDowload() {
        listImage = loadAllImages(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString());
        myCreationAdapter = new MyCreationAdapter(this, listImage, new MyCreationAdapter.OnClickImage() {
            @Override
            public void onClickImage(int pos) {
                if (!isselected) {
                    Intent intent = new Intent(LoadImageScreenActivity.this, CropScreenActivity.class);
                    intent.putExtra("uri", listImage.get(pos).getPath());
                    startActivityForResult(intent, 5);
                    isselected = true;
                }
            }
        });
        rcvListImg.setAdapter(myCreationAdapter);
        if (listImage.size() == 0) {
            findViewById(R.id.layoutNoPics).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.layoutNoPics).setVisibility(View.GONE);
        }
    }

    private List<Uri> loadAllImages(String str) {
        int size;
        HashMap hashMap = new HashMap();
        File file = new File(str, "/");
        if (file.isDirectory()) {
            File[] listFiles = file.listFiles();
            if (listFiles != null && listFiles.length > 0) {
                for (File file2 : listFiles) {
                    if (!file2.isDirectory()) {
                        for (String endsWith : this.extensions) {
                            if (file2.getAbsolutePath().endsWith(endsWith)) {
                                hashMap.put(Long.valueOf(file2.lastModified()), Uri.fromFile(file2));
                            }
                        }
                    }
                }
            }
        }
        if (hashMap.size() == 0) {
            return new ArrayList();
        }
        ArrayList arrayList = new ArrayList(hashMap.keySet());
        Collections.sort(arrayList);
        size = arrayList.size();
        ArrayList arrayList2 = new ArrayList();
        for (size--; size >= 0; size--) {
            arrayList2.add(hashMap.get(arrayList.get(size)));
        }
        return arrayList2;
    }

    private ArrayList<Uri> getAllScreenshots1() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            File screenshotsDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/Screenshots/");
            if (screenshotsDirectory.exists()) {
                File[] files = screenshotsDirectory.listFiles();
                if (files != null) {
                    for (File file : files) {
                        Uri uri = Uri.fromFile(file);
                        if (!screenshotList.contains(uri)) { // Kiểm tra trước khi thêm vào danh sách
                            screenshotList.add(uri);
                        }
                    }
                }
            }
        } else {
            File screenshotsDirectory = new File(Environment.getExternalStorageDirectory() + "/Pictures/Screenshots/");
            if (screenshotsDirectory.exists()) {
                File[] files = screenshotsDirectory.listFiles();
                if (files != null) {
                    for (File file : files) {
                        Uri uri = Uri.fromFile(file);
                        if (!screenshotList.contains(uri)) { // Kiểm tra trước khi thêm vào danh sách
                            screenshotList.add(uri);
                        }
                    }
                }
            }
        }
        Log.e("Screen", "" + screenshotList.size());

        return screenshotList;
    }

    private ArrayList<Uri> getAllCamera() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            File screenshotsDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/Camera/");
            if (screenshotsDirectory.exists()) {
                File[] files = screenshotsDirectory.listFiles();
                if (files != null) {
                    for (File file : files) {
                        Uri uri = Uri.fromFile(file);
                        if (!listCamera.contains(uri)) {
                            listCamera.add(uri);
                        }
                    }
                }
            }
        } else {
            File screenshotsDirectory = new File(Environment.getExternalStorageDirectory() + "/Pictures/Camera");
            if (screenshotsDirectory.exists()) {
                File[] files = screenshotsDirectory.listFiles();
                if (files != null) {
                    for (File file : files) {
                        Uri uri = Uri.fromFile(file);
                        if (!listCamera.contains(uri)) { // Kiểm tra trước khi thêm vào danh sách
                            listCamera.add(uri);
                        }
                    }
                }
            }
        }

        Log.e("Camera", "" + listCamera.size());
        return listCamera;
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("ACTION_CLOSE_ACTIVITY_THU_HAI".equals(action)) {
                // Thực hiện các hành động cần thiết, ví dụ như đóng Activity thứ 2
                finish();
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 5 && resultCode == 5) {
            setResult(5);
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter("ACTION_CLOSE_ACTIVITY_THU_HAI");
        registerReceiver(broadcastReceiver, intentFilter);
        try {
            getImage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
        imageList.clear();
        changeColor("All");
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!isselected) {
            isselected = false;
        }
        if (!isselected1) {
            isselected1 = false;
        }
        if (!isselected2) {
            isselected2 = false;
        }
        if (!isselected3) {
            isselected3 = false;
        }
    }
}