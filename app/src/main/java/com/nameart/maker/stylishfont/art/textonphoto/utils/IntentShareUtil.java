package com.nameart.maker.stylishfont.art.textonphoto.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import androidx.appcompat.app.AlertDialog;

import com.nameart.maker.stylishfont.art.textonphoto.R;

public class IntentShareUtil {
    private final Context mContext;

    public IntentShareUtil(Context mContext) {
        this.mContext = mContext;
    }

    public void shareOnWhatsapp(String packageName, String textBody, Uri fileUri) {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("text/plain");
        intent.setPackage(packageName);
        String str = "android.intent.extra.TEXT";
        if (TextUtils.isEmpty(textBody)) {
            textBody = "";
        }
        intent.putExtra(str, textBody);
        if (fileUri != null) {
            intent.putExtra("android.intent.extra.STREAM", fileUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setType("image/*");
        }
        try {
            mContext.startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            ex.printStackTrace();
            showWarningDialog(mContext, mContext.getString(R.string.App_not_found));
        }
    }

    private void showWarningDialog(Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);

        builder.setMessage(message);
        builder.setNegativeButton(context.getString(R.string.Close), null);
        builder.show();
    }
}
