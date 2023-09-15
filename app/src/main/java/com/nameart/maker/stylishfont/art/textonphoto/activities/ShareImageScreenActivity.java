package com.nameart.maker.stylishfont.art.textonphoto.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.RecoverableSecurityException;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.nameart.maker.stylishfont.art.textonphoto.R;
import com.nameart.maker.stylishfont.art.textonphoto.utils.SystemUtil;

import java.io.File;
import java.util.ArrayList;

public class ShareImageScreenActivity extends Activity {
    ImageView back, btnShare, btnDelete, imageView;
    TextView txtName;
    Uri phototUri = null;
    File file;
    ArrayList<Uri> uris = new ArrayList<>();

    private boolean isShareClicked = false;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        SystemUtil.setLocale(this);
        setContentView(R.layout.activity_share_image);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initViews();
        initClickView();

        phototUri = Uri.parse(getIntent().getStringExtra("uri"));
        Glide.with(this).load(phototUri.toString()).format(DecodeFormat.PREFER_ARGB_8888).into(imageView);
    }

    public void initViews() {
        txtName = findViewById(R.id.txtName);
        back = findViewById(R.id.imgBack);
        btnShare = findViewById(R.id.btnShare);
        btnDelete = findViewById(R.id.btnDelete);
        imageView = findViewById(R.id.image);

        txtName.setText(getResources().getString(R.string.My_Gallery));
    }

    private void initClickView() {
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnShare.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isShareClicked) {
                    shareFileImage(phototUri.toString());
                    isShareClicked = true;
                }
            }
        });

        btnDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogDelete();
            }
        });
    }

    public void showDialogDelete() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_delete);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView tv_cancel = dialog.findViewById(R.id.tv_cancel);
        TextView tv_delete = dialog.findViewById(R.id.tv_delete);
        file = new File(phototUri.getPath());

        tv_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        tv_delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
                    file.delete();
                    Intent intent = new Intent("load_my_creation");
                    LocalBroadcastManager.getInstance(ShareImageScreenActivity.this).sendBroadcast(intent);
                    Toast.makeText(ShareImageScreenActivity.this, getString(R.string.delete_success), Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    //Delete file android 11
                    Long mediaID = getFilePathToMediaID(file.getAbsolutePath(), ShareImageScreenActivity.this);
                    Uri uri = ContentUris.withAppendedId(MediaStore.Images.Media.getContentUri("external"), mediaID);
                    uris.clear();
                    uris.add(uri);
                    requestDeletePermission(uris);
                }
            }
        });

        dialog.show();
    }

    public static long getFilePathToMediaID(String songPath, Context context) {
        long id = 0;
        ContentResolver cr = context.getContentResolver();

        Uri uri = MediaStore.Files.getContentUri("external");
        String selection = MediaStore.Images.Media.DATA;
        String[] selectionArgs = {songPath};
        String[] projection = {MediaStore.Images.Media._ID};

        Cursor cursor = cr.query(uri, projection, selection + "=?", selectionArgs, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int idIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                id = Long.parseLong(cursor.getString(idIndex));
            }
        }

        return id;
    }

    private void requestDeletePermission(ArrayList<Uri> uris) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            PendingIntent pi = MediaStore.createDeleteRequest(getContentResolver(), uris);
            try {
                startIntentSenderForResult(pi.getIntentSender(), 1111, null, 0, 0, PendingIntent.FLAG_IMMUTABLE);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1111 && resultCode != 0) {
            deleteFromGallery(phototUri.getPath());
        }
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
//                Toast.makeText(this, getString(R.string.delete_successfully), Toast.LENGTH_LONG).show();
                Intent intent = new Intent("load_my_creation");
                LocalBroadcastManager.getInstance(ShareImageScreenActivity.this).sendBroadcast(intent);
                finish();
            } catch (Exception e3) {
                e3.printStackTrace();
            }
        }
        query.close();
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
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isShareClicked = false;
    }
}
