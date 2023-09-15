package com.nameart.maker.stylishfont.art.textonphoto.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.OnCompleteListener;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;
import com.nameart.maker.stylishfont.art.textonphoto.R;
import com.nameart.maker.stylishfont.art.textonphoto.activities.SettingScreenActivity;
import com.nameart.maker.stylishfont.art.textonphoto.dialog.DialogRating;


public class SomeThingApp {
    public static ReviewManager manager;
    public static ReviewInfo reviewInfo;

    public static void rateApp(Activity activity, int type) {
        DialogRating ratingDialog = new DialogRating(activity);
        ratingDialog.init(activity, new DialogRating.OnPress() {
            @Override
            public void send() {
                ratingDialog.dismiss();
                try{
                    SettingScreenActivity.llRate.setVisibility(View.GONE);
                    SettingScreenActivity.v1.setVisibility(View.GONE);
                }catch (Exception e){
                    e.printStackTrace();
                }
                String uriText = "mailto:" + SharePrefUtils.email +
                        "?subject=" + "Review for " + SharePrefUtils.subject +
                        "&body=" + SharePrefUtils.subject +
                        "\nRate : " + ratingDialog.getRating() + "\nContent: ";
                Uri uri = Uri.parse(uriText);
                Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
                sendIntent.setData(uri);
                try {
                    if (type == 1) {
                        activity.finishAffinity();
                    }
                    activity.startActivity(Intent.createChooser(sendIntent, activity.getString(R.string.Send_Email)));
                    SharePrefUtils.forceRated(activity);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(activity, activity.getString(R.string.There_is_no), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void rating() {
                manager = ReviewManagerFactory.create(activity);
                Task<ReviewInfo> request = manager.requestReviewFlow();
                request.addOnCompleteListener(new OnCompleteListener<ReviewInfo>() {
                    @Override
                    public void onComplete(@NonNull Task<ReviewInfo> task) {
                        try{
                            SettingScreenActivity.llRate.setVisibility(View.GONE);
                            SettingScreenActivity.v1.setVisibility(View.GONE);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        if (task.isSuccessful()) {
                            reviewInfo = task.getResult();
                            Log.e("ReviewInfo", "" + reviewInfo);
                            Task<Void> flow = manager.launchReviewFlow(activity, reviewInfo);
                            flow.addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    SharePrefUtils.forceRated(activity);
                                    ratingDialog.dismiss();
                                    if (type == 1) {
                                        activity.finishAffinity();
                                    }
                                }
                            });
                        } else {
                            ratingDialog.dismiss();
                        }
                    }
                });
            }

            @Override
            public void later() {
                ratingDialog.dismiss();
                if (type == 1) {
                    activity.finishAffinity();
                }
            }

        });
        try {
            ratingDialog.show();
        } catch (WindowManager.BadTokenException e) {
            e.printStackTrace();
        }
    }

    public static void shareApp(Activity activity) {
        Intent intentShare = new Intent(Intent.ACTION_SEND);
        intentShare.setType("text/plain");
        intentShare.putExtra(Intent.EXTRA_SUBJECT, activity.getString(R.string.app_name));
        intentShare.putExtra(Intent.EXTRA_TEXT, "Download application :"
                + "https://play.google.com/store/apps/details?id=" + activity.getPackageName());
        activity.startActivity(Intent.createChooser(intentShare, "Share with"));
    }

    public static void moreApp(Activity activity) {
//        activity.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("link")));
    }

//    public static void exitApp(Activity activity) {
//        Dialog dialog = new Dialog(activity, R.style.DialogTheme);
//        dialog.requestWindowFeature(1);
//        dialog.setContentView(R.layout.confirm_dialog);
//
//        (Objects.requireNonNull(dialog.getWindow())).setLayout(-1, -1);
//        (dialog.findViewById(R.id.rlYes)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//                activity.finishAffinity();
//
//            }
//        });
//        ((RelativeLayout) dialog.findViewById(R.id.rlNo)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });
//        dialog.show();
//    }

}
