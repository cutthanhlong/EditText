package com.nameart.maker.stylishfont.art.textonphoto.activities;

import static com.nameart.maker.stylishfont.art.textonphoto.activities.ShareImageScreenActivity.getFilePathToMediaID;

import android.app.Dialog;
import android.app.RecoverableSecurityException;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nameart.maker.stylishfont.art.textonphoto.R;
import com.nameart.maker.stylishfont.art.textonphoto.adapter.MyGalleryAdapter;
import com.nameart.maker.stylishfont.art.textonphoto.utils.SystemUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MyGalleryScreenActivity extends AppCompatActivity implements MyGalleryAdapter.OnClickImage {
    RecyclerView rcvListImg;
    List<Uri> listImage = new ArrayList<>();
    MyGalleryAdapter myCreationAdapter;
    public static ImageView back;
    public static TextView selectall, cancel, unselect;
    public static TextView tv_toolbar;
    ImageView clear, clear_select;
    LinearLayout ll_delete;
    int cout = 0;
    String path = "";
    private boolean isClick = false;

    ActivityResultLauncher<IntentSenderRequest> deleteIntent = registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            for (Uri con : myCreationAdapter.getSelectedUris()) {
                if (cout == myCreationAdapter.getSelectedCount()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        deleteFromGallery(con.getPath());
                    }
                }
            }

            loadListCreation();
        }
    });
    String[] extensions = new String[]{"jpg", "jpeg", "JPG", "JPEG", "png"};


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        SystemUtil.setLocale(this);
        setContentView(R.layout.activity_my_gallery);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        checkPermiss();
        mapping();
        viewListener();
        onClickItem();
    }

    private void mapping() {
        unselect = findViewById(R.id.done_text1);
        rcvListImg = findViewById(R.id.rcvListImg);
        ll_delete = findViewById(R.id.ll_delete);
        back = findViewById(R.id.back);
        selectall = findViewById(R.id.done_text);
        tv_toolbar = findViewById(R.id.tv_toolbar);
        cancel = findViewById(R.id.cancel);
        clear = findViewById(R.id.clear);
        clear_select = findViewById(R.id.clear_select);
    }

    private void onClickItem() {
        selectall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myCreationAdapter.selectAll();
                selectall.setVisibility(View.GONE);
                unselect.setVisibility(View.VISIBLE);
                showCancel();
                clear_select.setVisibility(View.GONE);
                clear.setVisibility(View.VISIBLE);
                ll_delete.setVisibility(View.VISIBLE);

            }
        });

        unselect.setVisibility(View.GONE);
        unselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myCreationAdapter.clearSelection1();
                selectall.setVisibility(View.VISIBLE);
                unselect.setVisibility(View.GONE);
                ll_delete.setVisibility(View.GONE);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myCreationAdapter.clearSelection();
                hideClearButton();
                tv_toolbar.setVisibility(View.VISIBLE);
                cancel.setVisibility(View.GONE);
                selectall.setVisibility(View.GONE);
                unselect.setVisibility(View.GONE);
                back.setVisibility(View.VISIBLE);
                ll_delete.setVisibility(View.GONE);

            }
        });

        clear_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView tv_cancel, tv_delete;
                final Dialog dialog = new Dialog(MyGalleryScreenActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_delete);
                Window window = dialog.getWindow();
                window.setGravity(Gravity.CENTER);
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCancelable(false);
                tv_cancel = dialog.findViewById(R.id.tv_cancel);
                tv_delete = dialog.findViewById(R.id.tv_delete);

                tv_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                tv_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ArrayList<Uri> selectedUris = myCreationAdapter.getSelectedUris();

                        for (Uri uri : selectedUris) {
                            path = uri.getPath();
                        }
                        if (Build.VERSION.SDK_INT >= 30) {
                            for (Uri con : selectedUris) {
                                deleteImageAPI30(con.getPath());
                            }
                        } else {
                            Timer timer = new Timer();
                            timer.scheduleAtFixedRate(new TimerTask() {
                                @RequiresApi(api = Build.VERSION_CODES.R)
                                @Override
                                public void run() {
                                    if (cout > myCreationAdapter.getSelectedUris().size()) {
                                        timer.cancel();
                                        cout = 0;
                                        runOnUiThread(() -> {
                                            loadListCreation();
                                        });
                                    } else {
                                        for (Uri con : selectedUris) {
                                            if (cout == selectedUris.indexOf(con)) {
                                                deleteFromGallery(con.getPath());
                                            }
                                        }
                                        cout += 1;
                                    }
                                }
                            }, 0, 200);
                        }

                        hideClearButton();
                        tv_toolbar.setVisibility(View.VISIBLE);
                        cancel.setVisibility(View.GONE);
                        selectall.setVisibility(View.GONE);
                        ll_delete.setVisibility(View.GONE);
                        back.setVisibility(View.VISIBLE);
                        unselect.setVisibility(View.GONE);
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView tv_cancel, tv_delete;
                final Dialog dialog = new Dialog(MyGalleryScreenActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_delete);
                Window window = dialog.getWindow();
                window.setGravity(Gravity.CENTER);
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCancelable(false);
                tv_cancel = dialog.findViewById(R.id.tv_cancel);
                tv_delete = dialog.findViewById(R.id.tv_delete);
                tv_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                tv_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ArrayList<Uri> selectedUris = myCreationAdapter.getSelectedUris();

                        for (Uri uri : selectedUris) {
                            path = uri.getPath();
                        }
                        if (Build.VERSION.SDK_INT >= 30) {
                            for (Uri con : selectedUris) {
                                deleteImageAPI30(con.getPath());
                            }
                        } else {
                            Timer timer = new Timer();
                            timer.scheduleAtFixedRate(new TimerTask() {
                                @RequiresApi(api = Build.VERSION_CODES.R)
                                @Override
                                public void run() {
                                    if (cout > myCreationAdapter.getSelectedUris().size()) {
                                        timer.cancel();
                                        cout = 0;
                                        runOnUiThread(() -> {
                                            loadListCreation();
                                        });
                                    } else {
                                        for (Uri con : selectedUris) {
                                            if (cout == selectedUris.indexOf(con)) {
                                                deleteFromGallery(con.getPath());
                                            }
                                        }
                                        cout += 1;
                                    }
                                }
                            }, 0, 200);
                        }
                        hideClearButton();
                        tv_toolbar.setVisibility(View.VISIBLE);
                        cancel.setVisibility(View.GONE);
                        selectall.setVisibility(View.GONE);
                        ll_delete.setVisibility(View.GONE);
                        back.setVisibility(View.VISIBLE);
                        unselect.setVisibility(View.GONE);
                        myCreationAdapter.notifyDataSetChanged();
                        if (listImage.size() == 0) {
                            findViewById(R.id.layoutNoPics).setVisibility(View.VISIBLE);
                        } else {
                            findViewById(R.id.layoutNoPics).setVisibility(View.GONE);
                        }
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void deleteImageAPI30(String path) {
        ContentResolver contentResolver = this.getContentResolver();
        ArrayList<Uri> arrayList2 = new ArrayList();
        Long mediaID = getFilePathToMediaID(path, this);
        Uri urii = ContentUris.withAppendedId(MediaStore.Images.Media.getContentUri("external"), mediaID);
        arrayList2.add(urii); // You need to use the Uri you got using ContentUris.withAppendedId() method
        Collections.addAll(arrayList2);
        IntentSender intentSender = MediaStore.createDeleteRequest(contentResolver, arrayList2).getIntentSender();
        IntentSenderRequest senderRequest = new IntentSenderRequest.Builder(intentSender)
                .setFillInIntent(null)
                .setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION, 0)
                .build();
        deleteIntent.launch(senderRequest);
    }

    public void showSelectAll() {
        tv_toolbar.setVisibility(View.GONE);
        selectall.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.neutral_trang)));
        selectall.setVisibility(View.VISIBLE);
    }

    public void showClearButton() {
        tv_toolbar.setVisibility(View.GONE);
        ll_delete.setVisibility(View.VISIBLE);
        clear_select.setVisibility(View.VISIBLE);
    }

    public void showCancel() {
        tv_toolbar.setVisibility(View.GONE);
        cancel.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.neutral_trang)));
        cancel.setVisibility(View.VISIBLE);
        back.setVisibility(View.INVISIBLE);
    }

    public void onResume() {
        super.onResume();
        try {
            loadListCreation();
        } catch (Exception e) {
            e.printStackTrace();
        }
        isClick = false;
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void viewListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        tv_toolbar.setText(getText(R.string.My_Gallery));
    }

    public void hideClearButton() {
        ll_delete.setVisibility(View.GONE);
    }

    public void loadListCreation() {
        listImage = loadAllImages(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString());
        myCreationAdapter = new MyGalleryAdapter(this, listImage, new MyGalleryAdapter.OnClickImage() {
            @Override
            public void onClickImage(int pos) {
                if (!isClick) {
                    myCreationAdapter.clearSelection();
                    hideClearButton();
                    cancel.setVisibility(View.GONE);
                    back.setVisibility(View.VISIBLE);
                    selectall.setVisibility(View.GONE);
                    Intent intent = new Intent(MyGalleryScreenActivity.this, ShareImageScreenActivity.class);
                    intent.putExtra("uri", listImage.get(pos).getPath());
                    startActivity(intent);
                    isClick = true;
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
        File file = new File(str, "/NameArtMaker");
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

    @Override
    public void onClickImage(int pos) {
    }

    public void deleteFromGallery(String str) {
        String[] strArr = {"_id"};
        String[] strArr2 = {str};
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = getContentResolver();
        Cursor query = contentResolver.query(uri, strArr, "_data = ?", strArr2, null);
        if (query.moveToFirst()) {
            try {
                contentResolver.delete(ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, query.getLong(query.getColumnIndexOrThrow("_id"))), null, null);
            } catch (Exception securityException) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    RecoverableSecurityException recoverableSecurityException;
                    if (securityException instanceof RecoverableSecurityException) {
                        recoverableSecurityException =
                                (RecoverableSecurityException) securityException;
                    } else {
                        throw new RuntimeException(
                                securityException.getMessage(), securityException);
                    }
                    IntentSender intentSender = recoverableSecurityException.getUserAction()
                            .getActionIntent().getIntentSender();
                    try {
                        startIntentSenderForResult(intentSender, 1003,
                                null, 0, 0, 0, null);
                    } catch (IntentSender.SendIntentException sendIntentException) {
                        sendIntentException.printStackTrace();
                    }
                } else {
                    throw new RuntimeException(
                            securityException.getMessage(), securityException);
                }
            }
        } else {
            try {
                new File(str).delete();
                Intent intent = new Intent("load_my_creation");
                LocalBroadcastManager.getInstance(MyGalleryScreenActivity.this).sendBroadcast(intent);
                finish();
            } catch (Exception e3) {
                e3.printStackTrace();
            }
        }
        query.close();
    }

    private void checkPermiss() {
        if (ContextCompat.checkSelfPermission(MyGalleryScreenActivity.this.getApplicationContext(), "android.permission.READ_EXTERNAL_STORAGE") != 0
                || ContextCompat.checkSelfPermission(MyGalleryScreenActivity.this.getApplicationContext(), "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            if (Build.VERSION.SDK_INT >= 23) {
                MyGalleryScreenActivity.this.requestPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"}, 922);
            }
        }
    }
}