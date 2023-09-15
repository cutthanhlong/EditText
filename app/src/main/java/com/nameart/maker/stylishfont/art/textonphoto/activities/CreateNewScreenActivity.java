package com.nameart.maker.stylishfont.art.textonphoto.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.graphics.SweepGradient;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.ShapeDrawable.ShaderFactory;
import android.graphics.drawable.shapes.RectShape;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorChangedListener;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.flask.colorpicker.slider.AlphaSlider;
import com.flask.colorpicker.slider.LightnessSlider;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.tabs.TabLayout;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.nameart.maker.stylishfont.art.textonphoto.R;
import com.nameart.maker.stylishfont.art.textonphoto.Views.DrawingView;
import com.nameart.maker.stylishfont.art.textonphoto.Views.MagicDrawingView;
import com.nameart.maker.stylishfont.art.textonphoto.Views.StickerView;
import com.nameart.maker.stylishfont.art.textonphoto.Views.TextStickerView;
import com.nameart.maker.stylishfont.art.textonphoto.Views.TextStickerView.OperationListener;
import com.nameart.maker.stylishfont.art.textonphoto.adapter.GradientsAdapter;
import com.nameart.maker.stylishfont.art.textonphoto.adapter.ImagesAdapter;
import com.nameart.maker.stylishfont.art.textonphoto.adapter.PaintAdapter;
import com.nameart.maker.stylishfont.art.textonphoto.adapter.StickerAdapter;
import com.nameart.maker.stylishfont.art.textonphoto.model.ViewSticker;
import com.nameart.maker.stylishfont.art.textonphoto.utils.AppController;
import com.nameart.maker.stylishfont.art.textonphoto.utils.DataHolderClass;
import com.nameart.maker.stylishfont.art.textonphoto.utils.SpHelper;
import com.nameart.maker.stylishfont.art.textonphoto.utils.SystemUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import io.techery.progresshint.addition.widget.SeekBar;

public class CreateNewScreenActivity extends AppCompatActivity implements OnClickListener {
    private static final String TAG = "EditorActivity";
    private String[] backgrounds;
    private String[] colors;
    private DrawingView drawingView;
    private int[] gradientColors;
    private final String gradientTile = "Clamp";
    private String gradientType = "";
    private String[] gradients;
    private GridView gvStickers;
    private String[] icons;
    public static ImageView ivBackground;
    private String linearDirection = "Horizontal";
    private LinearLayout llWidgets;
    private final AppController mAppController = new AppController(this);
    private StickerView mCurrentImgView;
    private TextStickerView mCurrentTextView;
    private GradientsAdapter mGradientsAdapter;
    private ImagesAdapter mImagesAdapter;
    private StickerAdapter mStikerAdapter;
    private StickerView mImgStickerView;
    private PaintAdapter mPaintAdapter;
    private final List<TextStickerView> mTextStickerList = new ArrayList();
    private TextStickerView mTextStickerView;
    private final ArrayList<View> mViews = new ArrayList();
    private MagicDrawingView magicDrawingView;
    private String mpImage = "Paint-1.png";
    private int mpIndex = 71;
    private MaterialSpinner msgType;
    private String pColor = "#CC0000";
    private int pIndex = 0;
    private Animation pushDownIn;
    private Animation pushDownOut;
    private Animation pushUpIn;
    private Animation pushUpOut;
    public static RelativeLayout rlColor;
    private RelativeLayout rlContainer;
    private RelativeLayout rlSticker;
    private RecyclerView rvMPaintImage;
    private RecyclerView rvPaintColors;
    private SeekBar sbmpBrushOpacity;
    private SeekBar sbmpBrushSize;
    private SeekBar sbpBrushOpacity;
    private SeekBar sbpBrushSize;
    private int screenHeight;
    private int screenWidth;
    private String[] separated;
    private String[] stickers;
    private TabLayout tlBackgrounds;
    private TabLayout tlStickers;
    private ImageView imgBack, imgRefresh;
    private MaterialCardView mcvExport;
    private ImageView imgSelect1, imgSelect2, imgSelect3, imgSelect4, imgSelect5;
    private ImageView imgText, imgBackgrounds, imgStickers, imgMagicPaint, imgPaint;
    private TextView txtText, txtBackgrounds, txtStickers, txtMagicPaint, txtPaint;
    private ImageView imgPaintMp, imgPenMp;
    private CardView cvPaintMp, cvPenMp;
    private ImageView imgPaintMp1, imgPenMp1;
    private CardView cvPaintMp1, cvPenMp1;
    SharedPreferences sharedPreferences;
    private ImageView imgBackP, imgBackMp;
    private TextView txtEmojiS, txtAlphabetS, txtPlannerS, txtLoveS, txtFoodS, txtHolidayS, txtAnimalS;
    private CardView cvEmojiS, cvAlphabetS, cvPlannerS, cvLoveS, cvFoodS, cvHolidayS, cvAnimalS;
    private ArrayList<ViewSticker> mViewSticker;
    private TextView txtName1, txtName2;
    private PopupWindow popupWindow;
    private View popupView;
    public static Dialog dialog;
    ImageView imgClose1;
    CardView cvImageBg1, cvGradientBg1, cvColorBg1;
    GridView gvBackgrounds1;
    TextView txtImageBg1, txtGradientBg1, txtColorBg1;
    private TextView txtName0Dialog;
    private MaterialCardView mcvOrientationDialog;
    private ImageView imgOrientationDialog;
    private ColorPickerView colorPickerDialog;
    private AlphaSlider alphaSliderDialog;
    private LightnessSlider lightnessSliderDialog;
    private ImageView imgDoneDialog;
    private int indexBg = 0;
    private int indexSticker = 0;
    private int pauseResumeFlag = 0;
    private boolean checkClickText = true;
    private boolean checkClickBg = true;
    private boolean isShowingDialog = false;

    protected void onCreate(Bundle savedInstanceState) {
        SystemUtil.setLocale(this);
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_create_new);

        gradientType = getResources().getString(R.string.Line);
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        popupView = LayoutInflater.from(CreateNewScreenActivity.this).inflate(R.layout.item_spinner_bg_gradient, null);
        popupWindow = new PopupWindow(popupView, findViewById(R.id.mcvSpinner).getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);

        findView();
        initView();
        invisibleView();
        initClickView();
        clickToolbar();
    }

    private void invisibleView() {
        imgSelect2.setVisibility(View.INVISIBLE);
        imgSelect3.setVisibility(View.INVISIBLE);
        imgSelect4.setVisibility(View.INVISIBLE);
        imgSelect1.setVisibility(View.INVISIBLE);
        imgSelect5.setVisibility(View.INVISIBLE);

        txtText.setTextColor(Color.parseColor("#8394AF"));
        txtBackgrounds.setTextColor(Color.parseColor("#8394AF"));
        txtStickers.setTextColor(Color.parseColor("#8394AF"));
        txtMagicPaint.setTextColor(Color.parseColor("#8394AF"));
        txtPaint.setTextColor(Color.parseColor("#8394AF"));

        imgText.setImageResource(R.drawable.ic_text);
        imgBackgrounds.setImageResource(R.drawable.ic_background);
        imgStickers.setImageResource(R.drawable.ic_sticker);
        imgMagicPaint.setImageResource(R.drawable.ic_magic_paint);
        imgPaint.setImageResource(R.drawable.ic_paint);
    }

    private void findView() {
        this.llWidgets = findViewById(R.id.ll_widgets);
        this.tlBackgrounds = findViewById(R.id.tl_backgrounds);
        this.rlContainer = findViewById(R.id.rl_text_container);
        this.rlSticker = findViewById(R.id.rl_sticker_container);
        this.tlStickers = findViewById(R.id.tl_stickers);
        ivBackground = findViewById(R.id.iv_background);
        this.gvStickers = findViewById(R.id.gv_stickers);
        rlColor = findViewById(R.id.rl_color);
        this.msgType = findViewById(R.id.sp_gType);
        this.drawingView = findViewById(R.id.drawingView);
        this.magicDrawingView = findViewById(R.id.magicDrawingView);
        this.rvPaintColors = findViewById(R.id.rv_pColor);
        this.rvMPaintImage = findViewById(R.id.rv_mpImage);
        this.sbpBrushSize = findViewById(R.id.sb_pBrushSize);
        this.sbpBrushOpacity = findViewById(R.id.sb_pBrushOpacity);
        this.sbmpBrushSize = findViewById(R.id.sb_mpBrushSize);
        this.sbmpBrushOpacity = findViewById(R.id.sb_mpBrushOpacity);
        this.imgBack = findViewById(R.id.imgBack);
        this.imgRefresh = findViewById(R.id.imgRefresh);
        this.mcvExport = findViewById(R.id.mcvExport);
        this.imgSelect1 = findViewById(R.id.imgSelect1);
        this.imgSelect2 = findViewById(R.id.imgSelect2);
        this.imgSelect3 = findViewById(R.id.imgSelect3);
        this.imgSelect4 = findViewById(R.id.imgSelect4);
        this.imgSelect5 = findViewById(R.id.imgSelect5);
        this.txtText = findViewById(R.id.txtText);
        this.txtBackgrounds = findViewById(R.id.txtBackgrounds);
        this.txtStickers = findViewById(R.id.txtStickers);
        this.txtMagicPaint = findViewById(R.id.txtMagicPaint);
        this.txtPaint = findViewById(R.id.txtPaint);
        this.imgText = findViewById(R.id.imgText);
        this.imgBackgrounds = findViewById(R.id.imgBackgrounds);
        this.imgStickers = findViewById(R.id.imgStickers);
        this.imgMagicPaint = findViewById(R.id.imgMagicPaint);
        this.imgPaint = findViewById(R.id.imgPaint);
        this.imgPaintMp = findViewById(R.id.ic_color);
        this.imgPenMp = findViewById(R.id.ic_brush);
        this.cvPaintMp = findViewById(R.id.cvPaintMp);
        this.cvPenMp = findViewById(R.id.cvPenMp);
        this.imgPaintMp1 = findViewById(R.id.ic_mpImage);
        this.imgPenMp1 = findViewById(R.id.ic_mpBrush);
        this.cvPaintMp1 = findViewById(R.id.cvPaintMp1);
        this.cvPenMp1 = findViewById(R.id.cvPenMp1);
        this.imgBackP = findViewById(R.id.ic_pClose);
        this.imgBackMp = findViewById(R.id.ic_mpClose);
        this.txtEmojiS = findViewById(R.id.txtEmojiS);
        this.txtAlphabetS = findViewById(R.id.txtAlphabetS);
        this.txtPlannerS = findViewById(R.id.txtPlannerS);
        this.txtLoveS = findViewById(R.id.txtLoveS);
        this.txtFoodS = findViewById(R.id.txtFoodS);
        this.txtHolidayS = findViewById(R.id.txtHolidayS);
        this.txtAnimalS = findViewById(R.id.txtAnimalS);
        this.cvEmojiS = findViewById(R.id.cvEmojiS);
        this.cvAlphabetS = findViewById(R.id.cvAlphabetS);
        this.cvPlannerS = findViewById(R.id.cvPlannerS);
        this.cvLoveS = findViewById(R.id.cvLoveS);
        this.cvFoodS = findViewById(R.id.cvFoodS);
        this.cvHolidayS = findViewById(R.id.cvHolidayS);
        this.cvAnimalS = findViewById(R.id.cvAnimalS);
        this.txtName1 = popupView.findViewById(R.id.txtName1);
        this.txtName2 = popupView.findViewById(R.id.txtName2);

        mViewSticker = new ArrayList<>();
        mViewSticker.add(new ViewSticker(cvEmojiS, txtEmojiS));
        mViewSticker.add(new ViewSticker(cvAlphabetS, txtAlphabetS));
        mViewSticker.add(new ViewSticker(cvPlannerS, txtPlannerS));
        mViewSticker.add(new ViewSticker(cvLoveS, txtLoveS));
        mViewSticker.add(new ViewSticker(cvFoodS, txtFoodS));
        mViewSticker.add(new ViewSticker(cvHolidayS, txtHolidayS));
        mViewSticker.add(new ViewSticker(cvAnimalS, txtAnimalS));

    }

    private void initView() {
        this.screenWidth = this.mAppController.getScreenDisplay(this).get(0);
        this.screenHeight = this.mAppController.getScreenDisplay(this).get(1);
        this.llWidgets.getLayoutParams().height = this.mAppController.getScreenDisplay(this).get(1) / 3 + this.mAppController.getScreenDisplay(this).get(1) / 8;
        this.pushUpIn = AnimationUtils.loadAnimation(this, R.anim.push_up_in);
        this.pushUpOut = AnimationUtils.loadAnimation(this, R.anim.push_up_out);
        this.pushDownIn = AnimationUtils.loadAnimation(this, R.anim.push_down_in);
        this.pushDownOut = AnimationUtils.loadAnimation(this, R.anim.push_down_out);
        rlColor.setBackgroundColor(-1);
        ivBackground.setImageBitmap(null);
        this.msgType.setItems("Linear", "Radial", "Sweep");

        this.rlContainer.setOnClickListener(view -> {
            setCurrentTextEdit2(mTextStickerView);
            setCurrentImgEdit2(mImgStickerView);
        });

        this.drawingView.setOnClickListener(view -> {
            setCurrentTextEdit2(mTextStickerView);
            setCurrentImgEdit2(mImgStickerView);
        });

        this.colors = getResources().getStringArray(R.array.Colors);
        try {
            this.icons = getAssets().list("MagicPaint");
            ArrayList<String> list = new ArrayList(Arrays.asList(this.icons));
            Collections.sort(list, String.CASE_INSENSITIVE_ORDER);
            for (int i = 0; i < list.size(); i++) {
                this.icons[i] = list.get(i);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            this.magicDrawingView.init(BitmapFactory.decodeStream(getAssets().open("MagicPaint/" + this.mpImage)));
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    private void initClickView() {
        findViewById(R.id.ll_text).setOnClickListener(this);
        findViewById(R.id.ll_backgrounds).setOnClickListener(this);
        findViewById(R.id.ll_stickers).setOnClickListener(this);
        findViewById(R.id.ll_magic_paint).setOnClickListener(this);
        findViewById(R.id.ll_paint).setOnClickListener(this);
        findViewById(R.id.ic_bgClose).setOnClickListener(this);
        findViewById(R.id.ic_stClose).setOnClickListener(this);
        findViewById(R.id.ic_pClose).setOnClickListener(this);
        findViewById(R.id.ic_mpClose).setOnClickListener(this);
        findViewById(R.id.ll_p_widget).setOnClickListener(this);
        findViewById(R.id.ll_mp_widget).setOnClickListener(this);
        this.drawingView.setOnClickListener(this);
        this.magicDrawingView.setOnClickListener(this);
    }

    private void clickToolbar() {
        mcvExport.setOnClickListener(view -> {
            if (!isStoragePermissionGranted()) {
                return;
            }
            if (CreateNewScreenActivity.this.mViews.isEmpty() && CreateNewScreenActivity.this.mTextStickerList.isEmpty()) {
                Toast.makeText(CreateNewScreenActivity.this, getResources().getString(R.string.Blank_image), Toast.LENGTH_SHORT).show();
                return;
            }
            Bitmap bitmap;

            RelativeLayout container = findViewById(R.id.container);
            setCurrentTextEdit2(CreateNewScreenActivity.this.mTextStickerView);
            setCurrentImgEdit2(CreateNewScreenActivity.this.mImgStickerView);
            File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "NameArtMaker");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            @SuppressLint("SimpleDateFormat") String fname = "Image-" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".png";
            File file = new File(dir, fname);
            if (file.exists()) {
                file.delete();
            }

            container.setDrawingCacheEnabled(true);
            container.buildDrawingCache();
            if (CreateNewScreenActivity.this.mAppController.getScreenDisplay(CreateNewScreenActivity.this).get(0) < 720) {
                bitmap = Bitmap.createScaledBitmap(container.getDrawingCache(), 720, 720, false);
            } else {
                bitmap = container.getDrawingCache();
            }
            try {
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            ContentValues values = new ContentValues();
            values.put("title", fname);
            values.put("description", "Name Art Maker Application Android");
            values.put("datetaken", System.currentTimeMillis());
            values.put("bucket_id", file.toString().toLowerCase(Locale.US).hashCode());
            values.put("bucket_display_name", file.getName().toLowerCase(Locale.US));
            values.put("_data", file.getAbsolutePath());
            getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, values);
            container.setDrawingCacheEnabled(false);
            container.destroyDrawingCache();
            DataHolderClass.getInstance().setmImagePath(file);
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath())));
            final Dialog dialog = new Dialog(CreateNewScreenActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_loading);
            Window window = dialog.getWindow();
            window.setGravity(Gravity.CENTER);
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(false);
            CountDownTimer countDownTimer = new CountDownTimer(3000, 1000) {
                @Override
                public void onTick(long l) {
                    dialog.show();
                }

                @Override
                public void onFinish() {
                    dialog.dismiss();
                    DataHolderClass.getInstance().setColor("#222B36");
                    DataHolderClass.getInstance().setFont(ResourcesCompat.getFont(CreateNewScreenActivity.this, R.font.urbanist_600));
                    startActivity(new Intent(CreateNewScreenActivity.this, SaveScreenActivity.class));
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
            };
            countDownTimer.start();
        });

        imgBack.setOnClickListener(view -> cancelDialog());

        imgRefresh.setOnClickListener(view -> eraseDialog());
    }

    @SuppressLint({"NonConstantResourceId", "UseCompatLoadingForDrawables", "ClickableViewAccessibility"})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ic_bgClose:
                popupWindow.dismiss();
                this.llWidgets.setVisibility(View.GONE);
                this.llWidgets.startAnimation(this.pushUpOut);
                return;

            case R.id.ic_stClose:
                this.llWidgets.setVisibility(View.GONE);
                this.llWidgets.startAnimation(this.pushUpOut);
                return;

            case R.id.ll_backgrounds:
                if (isShowingDialog) {
                    return;
                }
                isShowingDialog = true;
                findViewById(R.id.ll_mpSeekBars).setVisibility(View.GONE);
                findViewById(R.id.ll_pSeekBars).setVisibility(View.GONE);
                this.imgSelect2.setVisibility(View.VISIBLE);
                this.txtBackgrounds.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.xanh_dam)));
                this.imgBackgrounds.setImageResource(R.drawable.ic_background_selected);
                if (imgSelect1.getVisibility() == View.VISIBLE || imgSelect3.getVisibility() == View.VISIBLE || imgSelect4.getVisibility() == View.VISIBLE || imgSelect5.getVisibility() == View.VISIBLE) {
                    imgSelect1.setVisibility(View.INVISIBLE);
                    imgSelect3.setVisibility(View.INVISIBLE);
                    imgSelect4.setVisibility(View.INVISIBLE);
                    imgSelect5.setVisibility(View.INVISIBLE);
                    visibleBackgrounds();
                }
                this.rlContainer.setClickable(true);
                if (checkClickBg) {

                    checkClickBg = false;
                }
                dialog = new Dialog(CreateNewScreenActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_background);
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                dialog.getWindow().setGravity(Gravity.BOTTOM);
                dialog.setCancelable(false);

                //findViewDialog
                imgClose1 = dialog.findViewById(R.id.imgClose);
                cvImageBg1 = dialog.findViewById(R.id.cvImageBg);
                cvGradientBg1 = dialog.findViewById(R.id.cvGradientBg);
                cvColorBg1 = dialog.findViewById(R.id.cvColorBg);
                gvBackgrounds1 = dialog.findViewById(R.id.gv_backgrounds);
                txtImageBg1 = dialog.findViewById(R.id.txtImageBg);
                txtGradientBg1 = dialog.findViewById(R.id.txtGradientBg);
                txtColorBg1 = dialog.findViewById(R.id.txtColorBg);
                txtName0Dialog = dialog.findViewById(R.id.txtName0);
                mcvOrientationDialog = dialog.findViewById(R.id.mcvOrientation);
                imgOrientationDialog = dialog.findViewById(R.id.imgOrientation);
                colorPickerDialog = dialog.findViewById(R.id.colorPicker);
                alphaSliderDialog = dialog.findViewById(R.id.v_alpha_slider);
                lightnessSliderDialog = dialog.findViewById(R.id.v_lightness_slider);
                imgDoneDialog = dialog.findViewById(R.id.imgDone);

                if (indexBg == 1) {
                    txtName0Dialog.setText(gradientType);
                }

                imgClose1.setOnClickListener(view -> {
                    dialog.dismiss();
                    popupWindow.dismiss();
                });

                dialog.findViewById(R.id.cPicker).setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        popupWindow.dismiss();
                        return false;
                    }
                });

                dialog.findViewById(R.id.clTab).setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        popupWindow.dismiss();
                        return false;
                    }
                });

                gvBackgrounds1.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        popupWindow.dismiss();
                        return false;
                    }
                });

                dialog.findViewById(R.id.ll_gradients_bar).setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        popupWindow.dismiss();
                        return false;
                    }
                });

                imgDoneDialog.setOnClickListener(view -> {
                    popupWindow.dismiss();
                    if (SpHelper.getPosition(CreateNewScreenActivity.this, SpHelper.ITEM_BACKGROUND, -1) == -1 &&
                            SpHelper.getPosition(CreateNewScreenActivity.this, SpHelper.ITEM_GRADIENT, -1) == -1 &&
                            SpHelper.getPosition(CreateNewScreenActivity.this, SpHelper.ITEM_COLOR_BG, -1) == -1) {
                        Toast.makeText(this, getResources().getString(R.string.Not_change_background), Toast.LENGTH_SHORT).show();
                    } else {
                        if (SpHelper.getPosition(CreateNewScreenActivity.this, SpHelper.ITEM_GRADIENT, -1) == -1) {

                        } else {
                            separated = gradients[SpHelper.getPosition(CreateNewScreenActivity.this, SpHelper.ITEM_GRADIENT, -1)].split(" ");
                            applyGradient();
                            ivBackground.setImageBitmap(null);
                            Toast.makeText(CreateNewScreenActivity.this, getResources().getString(R.string.Change_background_gradient_success), Toast.LENGTH_SHORT).show();
                        }

                        if (SpHelper.getPosition(CreateNewScreenActivity.this, SpHelper.ITEM_COLOR_BG, -1) == -1) {

                        } else {
                            rlColor.setBackgroundColor(SpHelper.getPosition(CreateNewScreenActivity.this, SpHelper.ITEM_COLOR_BG, -1));
                            Toast.makeText(CreateNewScreenActivity.this, getResources().getString(R.string.Change_background_color_success), Toast.LENGTH_SHORT).show();
                            ivBackground.setImageBitmap(null);
                        }
                        if (SpHelper.getPosition(CreateNewScreenActivity.this, SpHelper.ITEM_BACKGROUND, -1) == -1 ||
                                SpHelper.getPosition(CreateNewScreenActivity.this, SpHelper.ITEM_BACKGROUND, -1) == 0) {

                        } else {
                            Drawable drawable = null;
                            int pos = SpHelper.getPosition(CreateNewScreenActivity.this, SpHelper.ITEM_BACKGROUND, -1);
                            try {
                                drawable = Drawable.createFromStream(getAssets().open("Backgrounds/" + backgrounds[pos - 2]), null);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(CreateNewScreenActivity.this, getResources().getString(R.string.Change_image_success), Toast.LENGTH_SHORT).show();
                            ivBackground.setImageDrawable(drawable);
                            rlColor.setBackgroundColor(-1);
                        }
                    }
                    dialog.dismiss();
                });

                dialog.setOnCancelListener(dialogInterface -> popupWindow.dismiss());

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        isShowingDialog = false;
                    }
                });

                if (this.tlBackgrounds.getChildCount() == 1) {
                    setBackgrounds2();
                    findViewById(R.id.rv_mpImage).setVisibility(View.GONE);
                    dialog.findViewById(R.id.ll_gradients_bar).setVisibility(View.GONE);
                    if (indexBg == 0) {
                        cvImageBg1.setCardBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.color_F6CB60)));
                        txtImageBg1.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.color_464646)));
                        try {
                            CreateNewScreenActivity.this.backgrounds = getAssets().list("Backgrounds");
                            ArrayList<String> list = new ArrayList(Arrays.asList(CreateNewScreenActivity.this.backgrounds));
                            Collections.sort(list, String.CASE_INSENSITIVE_ORDER);
                            for (int i2 = 0; i2 < list.size(); i2++) {
                                CreateNewScreenActivity.this.backgrounds[i2] = list.get(i2);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        CreateNewScreenActivity.this.mImagesAdapter = new ImagesAdapter(CreateNewScreenActivity.this, CreateNewScreenActivity.this.backgrounds, "Backgrounds",
                                true,
                                SpHelper.getPosition(CreateNewScreenActivity.this, SpHelper.ITEM_BACKGROUND, -1),
                                new ImagesAdapter.OnClickItem() {
                                    @Override
                                    public void onClickItem(int pos) {
                                        if (pos == 0) {
                                            startActivity(new Intent(CreateNewScreenActivity.this, LoadImageScreenActivity.class));
                                        } else if (pos == 1) {
                                            SpHelper.setPosition(CreateNewScreenActivity.this, SpHelper.ITEM_BACKGROUND, -1);
                                            SpHelper.setPosition(CreateNewScreenActivity.this, SpHelper.ITEM_COLOR_BG, -1);
                                            SpHelper.setPosition(CreateNewScreenActivity.this, SpHelper.ITEM_GRADIENT, -1);
                                            ivBackground.setImageDrawable(null);
                                            rlColor.setBackgroundColor(-1);
                                        } else {
                                            SpHelper.setPosition(CreateNewScreenActivity.this, SpHelper.ITEM_BACKGROUND, pos);
                                            SpHelper.setPosition(CreateNewScreenActivity.this, SpHelper.ITEM_COLOR_BG, -1);
                                            SpHelper.setPosition(CreateNewScreenActivity.this, SpHelper.ITEM_GRADIENT, -1);
                                            SpHelper.setPosition(CreateNewScreenActivity.this, SpHelper.ITEM_IMAGE, pos);
                                        }
                                        if (pos == 0) {
                                            return;
                                        } else {
                                            SpHelper.setPosition(CreateNewScreenActivity.this, SpHelper.BACKGROUND, 2);
                                        }
                                    }
                                });
                        gvBackgrounds1.setNumColumns(4);
                        gvBackgrounds1.setAdapter(CreateNewScreenActivity.this.mImagesAdapter);
                    } else if (indexBg == 1) {
                        dialog.findViewById(R.id.ll_gradients_bar).setVisibility(View.VISIBLE);
                        int num = dpToPx(126, CreateNewScreenActivity.this);
                        popupWindow = new PopupWindow(popupView, num, ViewGroup.LayoutParams.WRAP_CONTENT);

                        cvGradientBg1.setCardBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.color_F6CB60)));
                        txtGradientBg1.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.color_464646)));
                        cvImageBg1.setCardBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.color_DADADA)));
                        txtImageBg1.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.color_636363)));
                        cvColorBg1.setCardBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.color_DADADA)));
                        txtColorBg1.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.color_636363)));
                        gvBackgrounds1.setVisibility(View.VISIBLE);
                        dialog.findViewById(R.id.cPicker).setVisibility(View.INVISIBLE);
                        dialog.findViewById(R.id.ll_gradients_bar).setVisibility(View.VISIBLE);

                        CreateNewScreenActivity.this.gradients = new String[0];
                        CreateNewScreenActivity.this.gradients = getResources().getStringArray(R.array.Gradients);
                        CreateNewScreenActivity.this.mGradientsAdapter = new GradientsAdapter(CreateNewScreenActivity.this,
                                CreateNewScreenActivity.this.gradients, CreateNewScreenActivity.this.gradientTile,
                                CreateNewScreenActivity.this.gradientType, CreateNewScreenActivity.this.linearDirection,
                                true,
                                SpHelper.getPosition(CreateNewScreenActivity.this, SpHelper.ITEM_GRADIENT, -1),
                                new GradientsAdapter.OnClickItem() {
                                    @Override
                                    public void onClickItem(int position) {
                                        popupWindow.dismiss();
                                        SpHelper.setString(CreateNewScreenActivity.this, SpHelper.TYPE_GRADIENT, gradientType);
                                        SpHelper.setPosition(CreateNewScreenActivity.this, SpHelper.ITEM_GRADIENT, position);
                                        SpHelper.setPosition(CreateNewScreenActivity.this, SpHelper.ITEM_COLOR_BG, -1);
                                        SpHelper.setPosition(CreateNewScreenActivity.this, SpHelper.ITEM_BACKGROUND, -1);
                                    }
                                });
                        gvBackgrounds1.setNumColumns(4);
                        gvBackgrounds1.setAdapter(CreateNewScreenActivity.this.mGradientsAdapter);
                        dialog.findViewById(R.id.mcvSpinner).setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (popupWindow.isShowing()) {
                                    popupWindow.dismiss();
                                } else {
                                    popupWindow.showAsDropDown(dialog.findViewById(R.id.mcvSpinner), 0, dpToPx(4, CreateNewScreenActivity.this));
                                }
                            }
                        });

                        txtName1.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                gradientType = txtName1.getText().toString();
                                if (gradientType.equals(getResources().getString(R.string.Radial)) || gradientType.equals(getResources().getString(R.string.Sweep)))
                                    mcvOrientationDialog.setVisibility(View.INVISIBLE);
                                else mcvOrientationDialog.setVisibility(View.VISIBLE);
                                mGradientsAdapter = new GradientsAdapter(CreateNewScreenActivity.this, gradients, gradientTile, gradientType, linearDirection, true,
                                        SpHelper.getPosition(CreateNewScreenActivity.this, SpHelper.ITEM_GRADIENT, -1),
                                        new GradientsAdapter.OnClickItem() {
                                            @Override
                                            public void onClickItem(int position) {
                                                popupWindow.dismiss();
                                                SpHelper.setString(CreateNewScreenActivity.this, SpHelper.TYPE_GRADIENT, gradientType);
                                                SpHelper.setPosition(CreateNewScreenActivity.this, SpHelper.ITEM_GRADIENT, position);
                                                SpHelper.setPosition(CreateNewScreenActivity.this, SpHelper.ITEM_COLOR_BG, -1);
                                                SpHelper.setPosition(CreateNewScreenActivity.this, SpHelper.ITEM_BACKGROUND, -1);
                                            }
                                        });
                                gvBackgrounds1.setAdapter(mGradientsAdapter);
                                String tmp = txtName0Dialog.getText().toString();
                                txtName0Dialog.setText(gradientType);
                                txtName1.setText(tmp);
                                popupWindow.dismiss();
                            }
                        });

                        txtName2.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                gradientType = txtName2.getText().toString();
                                if (gradientType.equals(getResources().getString(R.string.Radial)) || gradientType.equals(getResources().getString(R.string.Sweep)))
                                    mcvOrientationDialog.setVisibility(View.INVISIBLE);
                                else mcvOrientationDialog.setVisibility(View.VISIBLE);
                                mGradientsAdapter = new GradientsAdapter(CreateNewScreenActivity.this, gradients, gradientTile, gradientType, linearDirection, true,
                                        SpHelper.getPosition(CreateNewScreenActivity.this, SpHelper.ITEM_GRADIENT, -1),
                                        new GradientsAdapter.OnClickItem() {
                                            @Override
                                            public void onClickItem(int position) {
                                                popupWindow.dismiss();
                                                SpHelper.setString(CreateNewScreenActivity.this, SpHelper.TYPE_GRADIENT, gradientType);
                                                SpHelper.setPosition(CreateNewScreenActivity.this, SpHelper.ITEM_GRADIENT, position);
                                                SpHelper.setPosition(CreateNewScreenActivity.this, SpHelper.ITEM_COLOR_BG, -1);
                                                SpHelper.setPosition(CreateNewScreenActivity.this, SpHelper.ITEM_BACKGROUND, -1);
                                            }
                                        });
                                gvBackgrounds1.setAdapter(mGradientsAdapter);
                                String tmp = txtName0Dialog.getText().toString();
                                txtName0Dialog.setText(gradientType);
                                txtName2.setText(tmp);
                                popupWindow.dismiss();
                            }
                        });

                        mcvOrientationDialog.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                popupWindow.dismiss();
                                if (pauseResumeFlag == 0) {
                                    pauseResumeFlag = 1;
                                    imgOrientationDialog.setImageResource(R.drawable.ic_vertical);
                                    linearDirection = "Vertical";
                                    mGradientsAdapter = new GradientsAdapter(CreateNewScreenActivity.this, gradients, gradientTile, gradientType, linearDirection, true,
                                            SpHelper.getPosition(CreateNewScreenActivity.this, SpHelper.ITEM_GRADIENT, -1),
                                            new GradientsAdapter.OnClickItem() {
                                                @Override
                                                public void onClickItem(int position) {
                                                    SpHelper.setPosition(CreateNewScreenActivity.this, SpHelper.ITEM_GRADIENT, position);
                                                    SpHelper.setPosition(CreateNewScreenActivity.this, SpHelper.ITEM_COLOR_BG, -1);
                                                    SpHelper.setPosition(CreateNewScreenActivity.this, SpHelper.ITEM_BACKGROUND, -1);
                                                }
                                            });
                                    gvBackgrounds1.setAdapter(mGradientsAdapter);
                                } else {
                                    pauseResumeFlag = 0;
                                    imgOrientationDialog.setImageResource(R.drawable.ic_horizontal);
                                    linearDirection = "Horizontal";
                                    mGradientsAdapter = new GradientsAdapter(CreateNewScreenActivity.this, gradients, gradientTile, gradientType, linearDirection, true,
                                            SpHelper.getPosition(CreateNewScreenActivity.this, SpHelper.ITEM_GRADIENT, -1),
                                            new GradientsAdapter.OnClickItem() {
                                                @Override
                                                public void onClickItem(int position) {
                                                    SpHelper.setString(CreateNewScreenActivity.this, SpHelper.TYPE_GRADIENT, gradientType);
                                                    SpHelper.setPosition(CreateNewScreenActivity.this, SpHelper.ITEM_GRADIENT, position);
                                                    SpHelper.setPosition(CreateNewScreenActivity.this, SpHelper.ITEM_COLOR_BG, -1);
                                                    SpHelper.setPosition(CreateNewScreenActivity.this, SpHelper.ITEM_BACKGROUND, -1);
                                                }
                                            });
                                    gvBackgrounds1.setAdapter(mGradientsAdapter);
                                }
                            }
                        });


                    } else {
                        dialog.findViewById(R.id.ll_gradients_bar).setVisibility(View.GONE);
                        popupWindow.dismiss();
                        cvColorBg1.setCardBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.color_F6CB60)));
                        txtColorBg1.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.color_464646)));
                        cvImageBg1.setCardBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.color_DADADA)));
                        txtImageBg1.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.color_636363)));
                        cvGradientBg1.setCardBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.color_DADADA)));
                        txtGradientBg1.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.color_636363)));

                        gvBackgrounds1.setVisibility(View.GONE);
                        dialog.findViewById(R.id.cPicker).setVisibility(View.VISIBLE);
                        int colorSave = SpHelper.getPosition(CreateNewScreenActivity.this, SpHelper.ITEM_COLOR_BG, -1);

                        if (colorSave == -1) {
                            colorPickerDialog.setInitialColor(Color.parseColor("#FFFFFFFF"), true);
                            colorPickerDialog.setAlpha(255);
                            lightnessSliderDialog.setColor(Color.parseColor("#FFFFFFFF"));
                            alphaSliderDialog.setColor(Color.parseColor("#FFFFFFFF"));
                        } else {
                            colorPickerDialog.setInitialColor(colorSave, false);
                            lightnessSliderDialog.setColor(colorSave);
                            alphaSliderDialog.setColor(colorSave);
                        }
                        lightnessSliderDialog.setColorPicker(colorPickerDialog);

                        colorPickerDialog.addOnColorSelectedListener(new OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(int selectedColor) {
                                alphaSliderDialog.setColor(selectedColor);
                                lightnessSliderDialog.setColor(selectedColor);
                                SpHelper.setPosition(CreateNewScreenActivity.this, SpHelper.ITEM_GRADIENT, -1);
                                SpHelper.setPosition(CreateNewScreenActivity.this, SpHelper.ITEM_BACKGROUND, -1);
                                SpHelper.setPosition(CreateNewScreenActivity.this, SpHelper.ITEM_COLOR_BG, selectedColor);
                            }
                        });

                        colorPickerDialog.addOnColorChangedListener(new OnColorChangedListener() {
                            @Override
                            public void onColorChanged(int selectedColor) {
                                alphaSliderDialog.setColor(selectedColor);
                                lightnessSliderDialog.setColor(selectedColor);
                                SpHelper.setPosition(CreateNewScreenActivity.this, SpHelper.ITEM_BACKGROUND, -1);
                                SpHelper.setPosition(CreateNewScreenActivity.this, SpHelper.ITEM_GRADIENT, -1);
                                SpHelper.setPosition(CreateNewScreenActivity.this, SpHelper.ITEM_COLOR_BG, selectedColor);
                            }
                        });
                        dialog.findViewById(R.id.ll_gradients_bar).setVisibility(View.GONE);
                    }
                }
                if (findViewById(R.id.ll_p_widget).getVisibility() == View.VISIBLE) {
                    findViewById(R.id.ll_p_widget).setVisibility(View.GONE);
                }
                if (findViewById(R.id.ll_mp_widget).getVisibility() == View.VISIBLE) {
                    findViewById(R.id.ll_mp_widget).setVisibility(View.GONE);
                }
                findViewById(R.id.ll_st_widget).setVisibility(View.GONE);
                findViewById(R.id.ll_bg_widget).setVisibility(View.VISIBLE);

                dialog.show();
                return;

            case R.id.ll_magic_paint:
                findViewById(R.id.ll_pSeekBars).setVisibility(View.GONE);
                findViewById(R.id.ll_mpSeekBars).setVisibility(View.GONE);
                imgBackMp.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_464646)));
                cvPaintMp1.setBackground(getResources().getDrawable(R.drawable.gradient_magic_paint));
                imgPaintMp1.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.neutral_trang)));
                cvPenMp1.setBackgroundColor(getResources().getColor(R.color.neutral_trang));
                imgPenMp1.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_3D87F8)));
                this.imgSelect4.setVisibility(View.VISIBLE);
                this.txtMagicPaint.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.xanh_dam)));
                this.imgMagicPaint.setImageResource(R.drawable.ic_magic_paint_selected);
                if (imgSelect2.getVisibility() == View.VISIBLE || imgSelect3.getVisibility() == View.VISIBLE || imgSelect1.getVisibility() == View.VISIBLE || imgSelect5.getVisibility() == View.VISIBLE) {
                    imgSelect2.setVisibility(View.INVISIBLE);
                    imgSelect3.setVisibility(View.INVISIBLE);
                    imgSelect1.setVisibility(View.INVISIBLE);
                    imgSelect5.setVisibility(View.INVISIBLE);
                    visibleMagicPaint();
                }
                if (findViewById(R.id.ll_mp_widget).getVisibility() == View.GONE) {
                    this.drawingView.setClickable(false);
                    this.rlContainer.setClickable(false);
                    setCurrentTextEdit2(this.mTextStickerView);
                    setCurrentImgEdit2(this.mImgStickerView);
                    setMagicPaint();
                    setMPaintImages();
                    if (findViewById(R.id.ll_p_widget).getVisibility() == View.VISIBLE) {
                        findViewById(R.id.ll_p_widget).setVisibility(View.GONE);
                    }
                    findViewById(R.id.ll_mp_widget).setVisibility(View.VISIBLE);
                    findViewById(R.id.ll_mp_widget).startAnimation(this.pushDownIn);
                    findViewById(R.id.rv_mpImage).setVisibility(View.VISIBLE);
                    findViewById(R.id.rv_mpImage).startAnimation(this.pushDownIn);
                    return;
                }
                return;

            case R.id.ll_paint:
                class BrushSize implements OnSeekBarChangeListener {
                    BrushSize() {
                    }

                    public void onProgressChanged(android.widget.SeekBar seekBar, int progress, boolean fromUser) {
                        drawingView.setBrushSize((float) progress);
                    }

                    public void onStartTrackingTouch(android.widget.SeekBar seekBar) {
                    }

                    public void onStopTrackingTouch(android.widget.SeekBar seekBar) {
                    }
                }

                class BrushOpacity implements OnSeekBarChangeListener {
                    BrushOpacity() {
                    }

                    public void onProgressChanged(android.widget.SeekBar seekBar, int progress, boolean fromUser) {
                        sbpBrushOpacity.setProgress(progress);
                        drawingView.setBrushOpacity((float) progress);
                    }

                    public void onStartTrackingTouch(android.widget.SeekBar seekBar) {
                    }

                    public void onStopTrackingTouch(android.widget.SeekBar seekBar) {
                    }
                }
                sbpBrushSize.setOnSeekBarChangeListener(new BrushSize());
                sbpBrushOpacity.setOnSeekBarChangeListener(new BrushOpacity());
                findViewById(R.id.ll_mpSeekBars).setVisibility(View.GONE);
                findViewById(R.id.rv_mpImage).setVisibility(View.GONE);
                imgBackP.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_464646)));
                cvPenMp.setBackground(getResources().getDrawable(R.drawable.gradient_magic_paint));
                imgPenMp.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.neutral_trang)));
                cvPaintMp.setBackgroundColor(getResources().getColor(R.color.neutral_trang));
                drawingView.setColor(sharedPreferences.getString("paint_color_string", "#CC0000"));
                this.imgSelect5.setVisibility(View.VISIBLE);
                this.txtPaint.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.xanh_dam)));
                this.imgPaint.setImageResource(R.drawable.ic_paint_selected);
                if (imgSelect2.getVisibility() == View.VISIBLE || imgSelect3.getVisibility() == View.VISIBLE || imgSelect4.getVisibility() == View.VISIBLE || imgSelect1.getVisibility() == View.VISIBLE) {
                    imgSelect2.setVisibility(View.INVISIBLE);
                    imgSelect3.setVisibility(View.INVISIBLE);
                    imgSelect4.setVisibility(View.INVISIBLE);
                    imgSelect1.setVisibility(View.INVISIBLE);
                    visiblePaint();
                }
                if (findViewById(R.id.ll_p_widget).getVisibility() == View.GONE) {
                    this.drawingView.setClickable(true);
                    this.rlContainer.setClickable(false);
                    setCurrentTextEdit2(this.mTextStickerView);
                    setCurrentImgEdit2(this.mImgStickerView);
                    setPaint();
                    if (findViewById(R.id.ll_mp_widget).getVisibility() == View.VISIBLE) {
                        findViewById(R.id.ll_mp_widget).setVisibility(View.GONE);
                    }
                    findViewById(R.id.ll_p_widget).setVisibility(View.VISIBLE);
                    findViewById(R.id.ll_p_widget).startAnimation(this.pushDownIn);
                    findViewById(R.id.ll_pSeekBars).setVisibility(View.VISIBLE);
                    findViewById(R.id.ll_pSeekBars).startAnimation(this.pushDownIn);
                    return;
                }
                return;

            case R.id.ll_stickers:
                findViewById(R.id.rv_mpImage).setVisibility(View.GONE);
                findViewById(R.id.ll_pSeekBars).setVisibility(View.GONE);
                findViewById(R.id.ll_mpSeekBars).setVisibility(View.GONE);
                this.imgSelect3.setVisibility(View.VISIBLE);
                this.txtStickers.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.xanh_dam)));
                this.imgStickers.setImageResource(R.drawable.ic_sticker_selected);
                gvStickers.setOnTouchListener((view, motionEvent) -> false);
                if (imgSelect2.getVisibility() == View.VISIBLE || imgSelect1.getVisibility() == View.VISIBLE || imgSelect4.getVisibility() == View.VISIBLE || imgSelect5.getVisibility() == View.VISIBLE) {
                    imgSelect2.setVisibility(View.INVISIBLE);
                    imgSelect1.setVisibility(View.INVISIBLE);
                    imgSelect4.setVisibility(View.INVISIBLE);
                    imgSelect5.setVisibility(View.INVISIBLE);
                    visibleStickers();
                }
                this.rlContainer.setClickable(true);
                if (this.tlStickers.getChildCount() == 1) {
                    if (indexSticker == 0) {
                        setColorTabSticker(cvEmojiS, txtEmojiS);
                        for (int i = 0; i < mViewSticker.size(); i++) {
                            Log.e("AAAA", mViewSticker.get(i).getCardView() + "");
                            if (mViewSticker.get(i).getCardView() != cvEmojiS)
                                setDefaultColorTabSticker(mViewSticker.get(i).getCardView(), mViewSticker.get(i).getTextView());
                        }
                        setStickers("Emoji");
                    }

                    this.txtEmojiS.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            indexSticker = 1;
                            setColorTabSticker(cvEmojiS, txtEmojiS);
                            for (int i = 0; i < mViewSticker.size(); i++) {
                                if (mViewSticker.get(i).getCardView() != cvEmojiS)
                                    setDefaultColorTabSticker(mViewSticker.get(i).getCardView(), mViewSticker.get(i).getTextView());
                            }
                            setStickers("Emoji");
                        }
                    });

                    this.txtAlphabetS.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            indexSticker = 2;
                            setColorTabSticker(cvAlphabetS, txtAlphabetS);
                            for (int i = 0; i < mViewSticker.size(); i++) {
                                if (mViewSticker.get(i).getCardView() != cvAlphabetS)
                                    setDefaultColorTabSticker(mViewSticker.get(i).getCardView(), mViewSticker.get(i).getTextView());
                            }
                            setStickers("Alphabet");
                        }
                    });

                    this.txtAnimalS.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            indexSticker = 3;
                            setColorTabSticker(cvAnimalS, txtAnimalS);
                            for (int i = 0; i < mViewSticker.size(); i++) {
                                if (mViewSticker.get(i).getCardView() != cvAnimalS)
                                    setDefaultColorTabSticker(mViewSticker.get(i).getCardView(), mViewSticker.get(i).getTextView());
                            }
                            setStickers("Animal");
                        }
                    });

                    this.txtPlannerS.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            indexSticker = 4;
                            setColorTabSticker(cvPlannerS, txtPlannerS);
                            for (int i = 0; i < mViewSticker.size(); i++) {
                                if (mViewSticker.get(i).getCardView() != cvPlannerS)
                                    setDefaultColorTabSticker(mViewSticker.get(i).getCardView(), mViewSticker.get(i).getTextView());
                            }
                            setStickers("Planner");
                        }
                    });

                    this.txtLoveS.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            indexSticker = 5;
                            setColorTabSticker(cvLoveS, txtLoveS);
                            for (int i = 0; i < mViewSticker.size(); i++) {
                                if (mViewSticker.get(i).getCardView() != cvLoveS)
                                    setDefaultColorTabSticker(mViewSticker.get(i).getCardView(), mViewSticker.get(i).getTextView());
                            }
                            setStickers("Love");
                        }
                    });

                    this.txtFoodS.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            indexSticker = 6;
                            setColorTabSticker(cvFoodS, txtFoodS);
                            for (int i = 0; i < mViewSticker.size(); i++) {
                                if (mViewSticker.get(i).getCardView() != cvFoodS)
                                    setDefaultColorTabSticker(mViewSticker.get(i).getCardView(), mViewSticker.get(i).getTextView());
                            }
                            setStickers("Food");
                        }
                    });

                    this.txtHolidayS.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            indexSticker = 7;
                            setColorTabSticker(cvHolidayS, txtHolidayS);
                            for (int i = 0; i < mViewSticker.size(); i++) {
                                if (mViewSticker.get(i).getCardView() != cvHolidayS)
                                    setDefaultColorTabSticker(mViewSticker.get(i).getCardView(), mViewSticker.get(i).getTextView());
                            }
                            setStickers("Holiday");
                        }
                    });
                }
                if (findViewById(R.id.ll_p_widget).getVisibility() == View.VISIBLE) {
                    findViewById(R.id.ll_p_widget).setVisibility(View.GONE);
                }
                if (findViewById(R.id.ll_mp_widget).getVisibility() == View.VISIBLE) {
                    findViewById(R.id.ll_mp_widget).setVisibility(View.GONE);
                }
                findViewById(R.id.ll_bg_widget).setVisibility(View.GONE);
                findViewById(R.id.ll_st_widget).setVisibility(View.VISIBLE);
                this.llWidgets.setVisibility(View.VISIBLE);
                this.llWidgets.startAnimation(this.pushUpIn);
                return;

            case R.id.ll_text:
                if (checkClickText) {
                    checkClickText = false;
                    findViewById(R.id.ll_mpSeekBars).setVisibility(View.GONE);
                    findViewById(R.id.rv_mpImage).setVisibility(View.GONE);
                    findViewById(R.id.ll_pSeekBars).setVisibility(View.GONE);
                    this.imgSelect1.setVisibility(View.VISIBLE);
                    this.txtText.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.xanh_dam)));
                    this.imgText.setImageResource(R.drawable.ic_text_selected);
                    if (imgSelect2.getVisibility() == View.VISIBLE || imgSelect3.getVisibility() == View.VISIBLE || imgSelect4.getVisibility() == View.VISIBLE || imgSelect5.getVisibility() == View.VISIBLE) {
                        imgSelect2.setVisibility(View.INVISIBLE);
                        imgSelect3.setVisibility(View.INVISIBLE);
                        imgSelect4.setVisibility(View.INVISIBLE);
                        imgSelect5.setVisibility(View.INVISIBLE);
                        visibleText();
                    }
                    this.rlContainer.setClickable(true);
                    DataHolderClass.getInstance().setLayout_x(this.screenWidth / 2);
                    DataHolderClass.getInstance().setLayout_y(this.screenHeight / 3);
                    DataHolderClass.getInstance().setmRotateAngle(0.0f);
                    DataHolderClass.getInstance().setmScale(1.0f);
                    startActivity(new Intent(this, EditTextScreenActivity.class));
                    if (this.mCurrentTextView != null) {
                        this.mCurrentTextView.setInEdit(false);
                        TextStickerView.isShowHelpBox1 = false;
                    }
                    overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom);
                    if (findViewById(R.id.ll_p_widget).getVisibility() == View.VISIBLE) {
                        findViewById(R.id.ll_p_widget).setVisibility(View.GONE);
                    }
                    if (findViewById(R.id.ll_mp_widget).getVisibility() == View.VISIBLE) {
                        findViewById(R.id.ll_mp_widget).setVisibility(View.GONE);
                        return;
                    }
                    return;
                }
            default:
                return;
        }
    }

    private void visibleText() {
        txtBackgrounds.setTextColor(Color.parseColor("#8394AF"));
        txtStickers.setTextColor(Color.parseColor("#8394AF"));
        txtMagicPaint.setTextColor(Color.parseColor("#8394AF"));
        txtPaint.setTextColor(Color.parseColor("#8394AF"));

        imgBackgrounds.setImageResource(R.drawable.ic_background);
        imgStickers.setImageResource(R.drawable.ic_sticker);
        imgMagicPaint.setImageResource(R.drawable.ic_magic_paint);
        imgPaint.setImageResource(R.drawable.ic_paint);
    }

    private void visibleBackgrounds() {
        txtText.setTextColor(Color.parseColor("#8394AF"));
        txtStickers.setTextColor(Color.parseColor("#8394AF"));
        txtMagicPaint.setTextColor(Color.parseColor("#8394AF"));
        txtPaint.setTextColor(Color.parseColor("#8394AF"));

        imgText.setImageResource(R.drawable.ic_text);
        imgStickers.setImageResource(R.drawable.ic_sticker);
        imgMagicPaint.setImageResource(R.drawable.ic_magic_paint);
        imgPaint.setImageResource(R.drawable.ic_paint);
    }

    private void visibleStickers() {
        txtText.setTextColor(Color.parseColor("#8394AF"));
        txtBackgrounds.setTextColor(Color.parseColor("#8394AF"));
        txtMagicPaint.setTextColor(Color.parseColor("#8394AF"));
        txtPaint.setTextColor(Color.parseColor("#8394AF"));

        imgText.setImageResource(R.drawable.ic_text);
        imgBackgrounds.setImageResource(R.drawable.ic_background);
        imgMagicPaint.setImageResource(R.drawable.ic_magic_paint);
        imgPaint.setImageResource(R.drawable.ic_paint);
    }

    private void visibleMagicPaint() {
        txtText.setTextColor(Color.parseColor("#8394AF"));
        txtBackgrounds.setTextColor(Color.parseColor("#8394AF"));
        txtStickers.setTextColor(Color.parseColor("#8394AF"));
        txtPaint.setTextColor(Color.parseColor("#8394AF"));

        imgText.setImageResource(R.drawable.ic_text);
        imgBackgrounds.setImageResource(R.drawable.ic_background);
        imgStickers.setImageResource(R.drawable.ic_sticker);
        imgPaint.setImageResource(R.drawable.ic_paint);
    }

    private void visiblePaint() {
        txtText.setTextColor(Color.parseColor("#8394AF"));
        txtBackgrounds.setTextColor(Color.parseColor("#8394AF"));
        txtStickers.setTextColor(Color.parseColor("#8394AF"));
        txtMagicPaint.setTextColor(Color.parseColor("#8394AF"));

        imgText.setImageResource(R.drawable.ic_text);
        imgBackgrounds.setImageResource(R.drawable.ic_background);
        imgStickers.setImageResource(R.drawable.ic_sticker);
        imgMagicPaint.setImageResource(R.drawable.ic_magic_paint);
    }

    private void createTextStickView() {
        this.mTextStickerView = new TextStickerView(this, null);
        LayoutParams layoutparams = new LayoutParams(-2, -2);
        layoutparams.addRule(10);
        this.mTextStickerView.setLayoutParams(layoutparams);
        this.mTextStickerView.setText(DataHolderClass.getInstance().getText());
        this.mTextStickerView.setType(DataHolderClass.getInstance().getFont());
        this.mTextStickerView.setOperationListener(new OperationListener() {
            @Override
            public void onFlipClick() {
            }

            @Override
            public void onDeleteClick() {
                Log.d(CreateNewScreenActivity.TAG, "gradient " + mCurrentTextView.getTag());
                mTextStickerList.remove(mCurrentTextView);
                rlContainer.removeView(mCurrentTextView);
            }

            @Override
            public void onEdit(TextStickerView textStickerView) {
                mCurrentTextView.setInEdit(false);
                TextStickerView.isShowHelpBox1 = false;
                mCurrentTextView = textStickerView;
                mCurrentTextView.setInEdit(true);
                TextStickerView.isShowHelpBox1 = true;
            }

            @Override
            public void onTop(TextStickerView textStickerView) {

            }
        });
        this.mTextStickerList.add(this.mTextStickerView);
        this.rlContainer.addView(this.mTextStickerView);
        setCurrentTextEdit(this.mTextStickerView);
    }

    private void setCurrentTextEdit(TextStickerView textStickerView) {
        if (this.mCurrentTextView != null) {
            this.mCurrentTextView.setInEdit(false);
            TextStickerView.isShowHelpBox1 = false;
        }
        this.mCurrentTextView = textStickerView;
        textStickerView.setInEdit(true);
        TextStickerView.isShowHelpBox1 = true;
    }

    private void setCurrentTextEdit2(TextStickerView textStickerView) {
        if (this.mCurrentTextView != null) {
            this.mCurrentTextView.setInEdit(false);
            TextStickerView.isShowHelpBox1 = false;
        }
        this.mCurrentTextView = textStickerView;
        if (textStickerView != null) {
            textStickerView.setInEdit(false);
            TextStickerView.isShowHelpBox1 = false;
        }
    }

    private void setBackgrounds2() {
        cvImageBg1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                indexBg = 0;
                cvImageBg1.setCardBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.color_F6CB60)));
                txtImageBg1.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.color_464646)));
                cvGradientBg1.setCardBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.color_DADADA)));
                txtGradientBg1.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.color_636363)));
                cvColorBg1.setCardBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.color_DADADA)));
                txtColorBg1.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.color_636363)));
                gvBackgrounds1.setVisibility(View.VISIBLE);
                dialog.findViewById(R.id.cPicker).setVisibility(View.INVISIBLE);
                dialog.findViewById(R.id.ll_gradients_bar).setVisibility(View.GONE);
                try {
                    CreateNewScreenActivity.this.backgrounds = getAssets().list("Backgrounds");
                    ArrayList<String> list = new ArrayList(Arrays.asList(CreateNewScreenActivity.this.backgrounds));
                    Collections.sort(list, String.CASE_INSENSITIVE_ORDER);
                    for (int i2 = 0; i2 < list.size(); i2++) {
                        CreateNewScreenActivity.this.backgrounds[i2] = list.get(i2);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                CreateNewScreenActivity.this.mImagesAdapter = new ImagesAdapter(CreateNewScreenActivity.this, CreateNewScreenActivity.this.backgrounds, "Backgrounds",
                        true,
                        SpHelper.getPosition(CreateNewScreenActivity.this, SpHelper.ITEM_BACKGROUND, -1),
                        new ImagesAdapter.OnClickItem() {
                            @Override
                            public void onClickItem(int pos) {
                                if (pos == 0) {
                                    startActivity(new Intent(CreateNewScreenActivity.this, LoadImageScreenActivity.class));
                                } else if (pos == 1) {
                                    SpHelper.setPosition(CreateNewScreenActivity.this, SpHelper.ITEM_BACKGROUND, -1);
                                    SpHelper.setPosition(CreateNewScreenActivity.this, SpHelper.ITEM_COLOR_BG, -1);
                                    SpHelper.setPosition(CreateNewScreenActivity.this, SpHelper.ITEM_GRADIENT, -1);
                                    ivBackground.setImageDrawable(null);
                                    rlColor.setBackgroundColor(-1);
                                } else {
                                    SpHelper.setPosition(CreateNewScreenActivity.this, SpHelper.ITEM_BACKGROUND, pos);
                                    SpHelper.setPosition(CreateNewScreenActivity.this, SpHelper.ITEM_COLOR_BG, -1);
                                    SpHelper.setPosition(CreateNewScreenActivity.this, SpHelper.ITEM_GRADIENT, -1);
                                }
                                if (pos == 0) {
                                    return;
                                } else {
                                }
                            }
                        });
                gvBackgrounds1.setNumColumns(4);
                gvBackgrounds1.setAdapter(CreateNewScreenActivity.this.mImagesAdapter);
            }
        });

        cvGradientBg1.setOnClickListener(new OnClickListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                indexBg = 1;
                int num = dpToPx(126, CreateNewScreenActivity.this);
                popupWindow = new PopupWindow(popupView, num, ViewGroup.LayoutParams.WRAP_CONTENT);

                cvGradientBg1.setCardBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.color_F6CB60)));
                txtGradientBg1.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.color_464646)));
                cvImageBg1.setCardBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.color_DADADA)));
                txtImageBg1.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.color_636363)));
                cvColorBg1.setCardBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.color_DADADA)));
                txtColorBg1.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.color_636363)));
                gvBackgrounds1.setVisibility(View.VISIBLE);
                dialog.findViewById(R.id.cPicker).setVisibility(View.INVISIBLE);
                dialog.findViewById(R.id.ll_gradients_bar).setVisibility(View.VISIBLE);

                CreateNewScreenActivity.this.gradients = new String[0];
                CreateNewScreenActivity.this.gradients = getResources().getStringArray(R.array.Gradients);
                CreateNewScreenActivity.this.mGradientsAdapter = new GradientsAdapter(CreateNewScreenActivity.this,
                        CreateNewScreenActivity.this.gradients, CreateNewScreenActivity.this.gradientTile,
                        CreateNewScreenActivity.this.gradientType, CreateNewScreenActivity.this.linearDirection,
                        true,
                        SpHelper.getPosition(CreateNewScreenActivity.this, SpHelper.ITEM_GRADIENT, -1),
                        new GradientsAdapter.OnClickItem() {
                            @Override
                            public void onClickItem(int position) {
                                popupWindow.dismiss();
                                SpHelper.setString(CreateNewScreenActivity.this, SpHelper.TYPE_GRADIENT, gradientType);
                                SpHelper.setPosition(CreateNewScreenActivity.this, SpHelper.ITEM_GRADIENT, position);
                                SpHelper.setPosition(CreateNewScreenActivity.this, SpHelper.ITEM_COLOR_BG, -1);
                                SpHelper.setPosition(CreateNewScreenActivity.this, SpHelper.ITEM_BACKGROUND, -1);
                            }
                        });
                gvBackgrounds1.setNumColumns(4);
                gvBackgrounds1.setAdapter(CreateNewScreenActivity.this.mGradientsAdapter);
                dialog.findViewById(R.id.mcvSpinner).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (popupWindow.isShowing()) {
                            popupWindow.dismiss();
                        } else {
                            popupWindow.showAsDropDown(dialog.findViewById(R.id.mcvSpinner), 0, dpToPx(4, CreateNewScreenActivity.this));
                        }
                    }
                });

                txtName1.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        gradientType = txtName1.getText().toString();
                        if (gradientType.equals(getResources().getString(R.string.Radial)) || gradientType.equals(getResources().getString(R.string.Sweep)))
                            mcvOrientationDialog.setVisibility(View.INVISIBLE);
                        else mcvOrientationDialog.setVisibility(View.VISIBLE);
                        mGradientsAdapter = new GradientsAdapter(CreateNewScreenActivity.this, gradients, gradientTile, gradientType, linearDirection, true,
                                SpHelper.getPosition(CreateNewScreenActivity.this, SpHelper.ITEM_GRADIENT, -1),
                                new GradientsAdapter.OnClickItem() {
                                    @Override
                                    public void onClickItem(int position) {
                                        popupWindow.dismiss();
                                        SpHelper.setString(CreateNewScreenActivity.this, SpHelper.TYPE_GRADIENT, gradientType);
                                        SpHelper.setPosition(CreateNewScreenActivity.this, SpHelper.ITEM_GRADIENT, position);
                                        SpHelper.setPosition(CreateNewScreenActivity.this, SpHelper.ITEM_COLOR_BG, -1);
                                        SpHelper.setPosition(CreateNewScreenActivity.this, SpHelper.ITEM_BACKGROUND, -1);
                                    }
                                });
                        gvBackgrounds1.setAdapter(mGradientsAdapter);
                        String tmp = txtName0Dialog.getText().toString();
                        txtName0Dialog.setText(gradientType);
                        txtName1.setText(tmp);
                        popupWindow.dismiss();
                    }
                });

                txtName2.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        gradientType = txtName2.getText().toString();
                        if (gradientType.equals(getResources().getString(R.string.Radial)) || gradientType.equals(getResources().getString(R.string.Sweep)))
                            mcvOrientationDialog.setVisibility(View.INVISIBLE);
                        else mcvOrientationDialog.setVisibility(View.VISIBLE);
                        mGradientsAdapter = new GradientsAdapter(CreateNewScreenActivity.this, gradients, gradientTile, gradientType, linearDirection, true,
                                SpHelper.getPosition(CreateNewScreenActivity.this, SpHelper.ITEM_GRADIENT, -1),
                                new GradientsAdapter.OnClickItem() {
                                    @Override
                                    public void onClickItem(int position) {
                                        popupWindow.dismiss();
                                        SpHelper.setString(CreateNewScreenActivity.this, SpHelper.TYPE_GRADIENT, gradientType);
                                        SpHelper.setPosition(CreateNewScreenActivity.this, SpHelper.ITEM_GRADIENT, position);
                                        SpHelper.setPosition(CreateNewScreenActivity.this, SpHelper.ITEM_COLOR_BG, -1);
                                        SpHelper.setPosition(CreateNewScreenActivity.this, SpHelper.ITEM_BACKGROUND, -1);
                                    }
                                });
                        gvBackgrounds1.setAdapter(mGradientsAdapter);
                        String tmp = txtName0Dialog.getText().toString();
                        txtName0Dialog.setText(gradientType);
                        txtName2.setText(tmp);
                        popupWindow.dismiss();
                    }
                });

                mcvOrientationDialog.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupWindow.dismiss();
                        if (pauseResumeFlag == 0) {
                            pauseResumeFlag = 1;
                            imgOrientationDialog.setImageResource(R.drawable.ic_vertical);
                            linearDirection = "Vertical";
                            mGradientsAdapter = new GradientsAdapter(CreateNewScreenActivity.this, gradients, gradientTile, gradientType, linearDirection, true,
                                    SpHelper.getPosition(CreateNewScreenActivity.this, SpHelper.ITEM_GRADIENT, -1),
                                    new GradientsAdapter.OnClickItem() {
                                        @Override
                                        public void onClickItem(int position) {
                                            SpHelper.setString(CreateNewScreenActivity.this, SpHelper.TYPE_GRADIENT, gradientType);
                                            SpHelper.setPosition(CreateNewScreenActivity.this, SpHelper.ITEM_GRADIENT, position);
                                            SpHelper.setPosition(CreateNewScreenActivity.this, SpHelper.ITEM_COLOR_BG, -1);
                                            SpHelper.setPosition(CreateNewScreenActivity.this, SpHelper.ITEM_BACKGROUND, -1);
                                        }
                                    });
                            gvBackgrounds1.setAdapter(mGradientsAdapter);
                        } else {
                            pauseResumeFlag = 0;
                            imgOrientationDialog.setImageResource(R.drawable.ic_horizontal);
                            linearDirection = "Horizontal";
                            mGradientsAdapter = new GradientsAdapter(CreateNewScreenActivity.this, gradients, gradientTile, gradientType, linearDirection, true,
                                    SpHelper.getPosition(CreateNewScreenActivity.this, SpHelper.ITEM_GRADIENT, -1),
                                    new GradientsAdapter.OnClickItem() {
                                        @Override
                                        public void onClickItem(int position) {
                                            SpHelper.setString(CreateNewScreenActivity.this, SpHelper.TYPE_GRADIENT, gradientType);
                                            SpHelper.setPosition(CreateNewScreenActivity.this, SpHelper.ITEM_GRADIENT, position);
                                            SpHelper.setPosition(CreateNewScreenActivity.this, SpHelper.ITEM_COLOR_BG, -1);
                                            SpHelper.setPosition(CreateNewScreenActivity.this, SpHelper.ITEM_BACKGROUND, -1);
                                        }
                                    });
                            gvBackgrounds1.setAdapter(mGradientsAdapter);
                        }
                    }
                });
            }
        });

        cvColorBg1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                indexBg = 2;
                popupWindow.dismiss();
                cvColorBg1.setCardBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.color_F6CB60)));
                txtColorBg1.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.color_464646)));
                cvImageBg1.setCardBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.color_DADADA)));
                txtImageBg1.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.color_636363)));
                cvGradientBg1.setCardBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.color_DADADA)));
                txtGradientBg1.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.color_636363)));

                gvBackgrounds1.setVisibility(View.GONE);
                dialog.findViewById(R.id.cPicker).setVisibility(View.VISIBLE);
                int colorSave = SpHelper.getPosition(CreateNewScreenActivity.this, SpHelper.ITEM_COLOR_BG, -1);
                String colorString = String.format("#%06X", 0xFFFFFF & colorSave);
                if (colorSave == -1) {
                    lightnessSliderDialog.setColor(Color.parseColor("#FFFFFFFF"));
                    alphaSliderDialog.setColor(Color.parseColor("#FFFFFFFF"));
                } else {
                    lightnessSliderDialog.setColor(colorSave);
                    alphaSliderDialog.setColor(colorSave);
                }
                if (colorSave == -1) {
                    colorPickerDialog.setInitialColor(Color.parseColor("#FFFFFFFF"), true);
                    colorPickerDialog.setAlpha(255);
                } else {
                    colorPickerDialog.setInitialColor(colorSave, false);
                }
                lightnessSliderDialog.setColorPicker(colorPickerDialog);
                colorPickerDialog.addOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {
                        lightnessSliderDialog.setColor(selectedColor);
                        alphaSliderDialog.setColor(selectedColor);
                        SpHelper.setPosition(CreateNewScreenActivity.this, SpHelper.ITEM_BACKGROUND, -1);
                        SpHelper.setPosition(CreateNewScreenActivity.this, SpHelper.ITEM_GRADIENT, -1);
                        SpHelper.setPosition(CreateNewScreenActivity.this, SpHelper.ITEM_COLOR_BG, selectedColor);
                    }
                });

                colorPickerDialog.addOnColorChangedListener(new OnColorChangedListener() {
                    @Override
                    public void onColorChanged(int selectedColor) {
                        lightnessSliderDialog.setColor(selectedColor);
                        alphaSliderDialog.setColor(selectedColor);
                        SpHelper.setPosition(CreateNewScreenActivity.this, SpHelper.ITEM_BACKGROUND, -1);
                        SpHelper.setPosition(CreateNewScreenActivity.this, SpHelper.ITEM_GRADIENT, -1);
                        SpHelper.setPosition(CreateNewScreenActivity.this, SpHelper.ITEM_COLOR_BG, selectedColor);
                    }
                });
                dialog.findViewById(R.id.ll_gradients_bar).setVisibility(View.GONE);
            }
        });
    }

    private void setColorTabSticker(CardView cardView, TextView textView) {
        cardView.setCardBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.color_F6CB60)));
        textView.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.color_464646)));
    }

    private void setDefaultColorTabSticker(CardView cardView, TextView textView) {
        cardView.setCardBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.color_DADADA)));
        textView.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.color_636363)));
    }

    private void applyGradient() {
        if (this.separated.length > 0) {
            final TileMode tileMode = TileMode.MIRROR;
            ShaderFactory sf = new ShaderFactory() {
                public Shader resize(int width, int height) {
                    gradientColors = new int[separated.length];
                    for (int i = 0; i < separated.length; i++) {
                        gradientColors[i] = Color.parseColor(separated[i]);
                    }
                    if (gradientType == getResources().getString(R.string.Line)) {
                        if (linearDirection == "Horizontal") {
                            return new LinearGradient(0.0f, 0.0f, (float) rlColor.getWidth(), 0.0f, gradientColors, null, tileMode);
                        } else if (linearDirection != "Vertical") {
                            return null;
                        } else {
                            return new LinearGradient(0.0f, 0.0f, 0.0f, (float) rlColor.getHeight(), gradientColors, null, tileMode);
                        }
                    } else if (gradientType == getResources().getString(R.string.Radial)) {
                        return new RadialGradient((float) (rlColor.getWidth() / 2), (float) (rlColor.getHeight() / 2), (float) rlColor.getWidth(), gradientColors, null, tileMode);
                    } else {
                        if (gradientType == getResources().getString(R.string.Sweep)) {
                            return new SweepGradient((float) (rlColor.getWidth() / 2), (float) (rlColor.getHeight() / 2), gradientColors, null);
                        }
                        return null;
                    }
                }
            };
            PaintDrawable p = new PaintDrawable();
            p.setShape(new RectShape());
            p.setShaderFactory(sf);
            rlColor.setBackgroundDrawable(p);
            return;
        }
        Toast.makeText(this, getResources().getString(R.string.Please_choose_gradient_first), Toast.LENGTH_SHORT).show();
    }

    private void setStickers(String str) {
        try {
            this.stickers = getAssets().list("Stickers/" + str);
            ArrayList<String> list = new ArrayList(Arrays.asList(this.stickers));
            Collections.sort(list, String.CASE_INSENSITIVE_ORDER);
            for (int i = 0; i < list.size(); i++) {
                this.stickers[i] = list.get(i);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.mStikerAdapter = new StickerAdapter(this, this.stickers, "Stickers/" + str, new StickerAdapter.OnClickItem() {
            @Override
            public void onClickItem(int pos) {
                addImgStickerView("Stickers/" + str + "/" + stickers[pos]);
            }
        });
        this.gvStickers.setAdapter(this.mStikerAdapter);

    }

    private void addImgStickerView(String stickerPath) {
        this.mImgStickerView = new StickerView(this);
        InputStream inputstream = null;
        try {
            inputstream = getAssets().open(stickerPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.mImgStickerView.setBitmap(BitmapFactory.decodeStream(inputstream));
        this.mImgStickerView.setOperationListener(new StickerView.OperationListener() {
            public void onDeleteClick() {
                mViews.remove(mCurrentImgView);
                rlSticker.removeView(mCurrentImgView);
            }

            public void onEdit(StickerView stickerView) {
                mCurrentImgView.setInEdit(false);
                TextStickerView.isShowHelpBox1 = false;
                mCurrentImgView = stickerView;
                mCurrentImgView.setInEdit(true);
                TextStickerView.isShowHelpBox1 = true;
            }

            public void onTop(StickerView stickerView) {
                int position = mViews.indexOf(stickerView);
                if (position != mViews.size() - 1) {
                    mViews.add(mViews.size(), mViews.remove(position));
                }
            }
        });
        this.rlSticker.addView(this.mImgStickerView, new LayoutParams(-1, -1));
        this.mViews.add(this.mImgStickerView);
        setCurrentImgEdit(this.mImgStickerView);
    }

    private void setCurrentImgEdit(StickerView stickerView) {
        if (this.mCurrentImgView != null) {
            this.mCurrentImgView.setInEdit(false);
            TextStickerView.isShowHelpBox1 = false;
        }
        this.mCurrentImgView = stickerView;
        stickerView.setInEdit(true);
        TextStickerView.isShowHelpBox1 = true;
    }

    private void setCurrentImgEdit2(StickerView stickerView) {
        if (this.mCurrentImgView != null) {
            this.mCurrentImgView.setInEdit(false);
            TextStickerView.isShowHelpBox1 = false;
        }
        this.mCurrentImgView = stickerView;
        if (stickerView != null) {
            stickerView.setInEdit(false);
            TextStickerView.isShowHelpBox1 = true;
        }
    }

    private void setPaint() {
        drawingView.setBrushOpacity(sbpBrushOpacity.getProgress());
        findViewById(R.id.ic_pClose).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                rlContainer.setClickable(true);
                findViewById(R.id.ll_pSeekBars).setVisibility(View.GONE);
                findViewById(R.id.ll_p_widget).setVisibility(View.GONE);
            }
        });
        imgPaintMp.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                findViewById(R.id.ll_pSeekBars).setVisibility(View.GONE);
                cvPaintMp.setBackground(getResources().getDrawable(R.drawable.gradient_magic_paint));
                imgPaintMp.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.neutral_trang)));
                cvPenMp.setBackgroundColor(getResources().getColor(R.color.neutral_trang));
                imgPenMp.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_3D87F8)));

                ColorPickerDialogBuilder.with(CreateNewScreenActivity.this, R.style.DialogColorPicker).setTitle(R.string.Choose_Paint_Color).wheelType(ColorPickerView.WHEEL_TYPE.FLOWER).density(12).initialColor(sharedPreferences.getInt("paint_color", Color.parseColor("#CC0000"))).setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {

                    }
                }).setPositiveButton("" + getText(R.string.Ok), new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("paint_color", selectedColor);
                        editor.apply();

                        String hexColor = String.format("#%06X", (0xFFFFFF & selectedColor));
                        drawingView.setColor(hexColor);
                        SharedPreferences.Editor editor1 = sharedPreferences.edit();
                        editor.putString("paint_color_string", hexColor);
                        editor.apply();
                        cvPaintMp.setBackgroundColor(getResources().getColor(R.color.neutral_trang));
                        imgPaintMp.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_3D87F8)));
                        cvPenMp.setBackground(getResources().getDrawable(R.drawable.gradient_magic_paint));
                        imgPenMp.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.neutral_trang)));
                        findViewById(R.id.ll_pSeekBars).setVisibility(View.VISIBLE);
                    }
                }).setNegativeButton("" + getText(R.string.Cancle), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cvPaintMp.setBackgroundColor(getResources().getColor(R.color.neutral_trang));
                        imgPaintMp.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_3D87F8)));
                        cvPenMp.setBackground(getResources().getDrawable(R.drawable.gradient_magic_paint));
                        imgPenMp.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.neutral_trang)));
                        findViewById(R.id.ll_pSeekBars).setVisibility(View.VISIBLE);
                    }
                }).build().show();
                drawingView.drawMode();
            }
        });

        imgPenMp.setOnClickListener(new OnClickListener() {

            class BrushSize implements OnSeekBarChangeListener {
                BrushSize() {
                }

                public void onProgressChanged(android.widget.SeekBar seekBar, int progress, boolean fromUser) {
                    drawingView.setBrushSize((float) progress);
                }

                public void onStartTrackingTouch(android.widget.SeekBar seekBar) {
                }

                public void onStopTrackingTouch(android.widget.SeekBar seekBar) {
                }
            }

            class BrushOpacity implements OnSeekBarChangeListener {
                BrushOpacity() {
                }

                public void onProgressChanged(android.widget.SeekBar seekBar, int progress, boolean fromUser) {
                    drawingView.setBrushOpacity((float) progress);
                }

                public void onStartTrackingTouch(android.widget.SeekBar seekBar) {
                }

                public void onStopTrackingTouch(android.widget.SeekBar seekBar) {
                }
            }

            public void onClick(View v) {
                cvPaintMp.setBackgroundColor(getResources().getColor(R.color.neutral_trang));
                imgPaintMp.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_3D87F8)));
                cvPenMp.setBackground(getResources().getDrawable(R.drawable.gradient_magic_paint));
                imgPenMp.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.neutral_trang)));
                rvPaintColors.setVisibility(View.GONE);
                findViewById(R.id.ll_pSeekBars).setVisibility(View.VISIBLE);
                findViewById(R.id.ll_mpSeekBars).setVisibility(View.GONE);
                sbpBrushSize.setOnSeekBarChangeListener(new BrushSize());
                sbpBrushOpacity.setOnSeekBarChangeListener(new BrushOpacity());
            }
        });

        findViewById(R.id.mcvUndoMagicPaint).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                drawingView.onClickUndo();
            }
        });

        findViewById(R.id.mcvRedoMagicPaint).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                drawingView.onClickRedo();
            }
        });

        findViewById(R.id.mcvRestoreMagicPaint).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                deleteDialog("Paint");
            }
        });
    }

    private void setMagicPaint() {
        magicDrawingView.setBrushOpacity(sbmpBrushOpacity.getProgress());
        imgBackMp.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                rlContainer.setClickable(true);
                findViewById(R.id.ll_mp_widget).setVisibility(View.GONE);
                findViewById(R.id.rv_mpImage).setVisibility(View.GONE);
                findViewById(R.id.ll_mpSeekBars).setVisibility(View.GONE);
            }
        });

        cvPaintMp1.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                rvMPaintImage.setVisibility(View.VISIBLE);
                cvPaintMp1.setBackground(getResources().getDrawable(R.drawable.gradient_magic_paint));
                imgPaintMp1.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.neutral_trang)));
                cvPenMp1.setBackgroundColor(getResources().getColor(R.color.neutral_trang));
                imgPenMp1.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_3D87F8)));
                findViewById(R.id.ll_mpSeekBars).setVisibility(View.GONE);
                for (int i = 0; i < icons.length; i++) {
                    if (icons[i].equals(mpImage)) {
                        mpIndex = i;
                        setMPaintImages();
                        break;
                    }
                }
                drawingView.drawMode();
            }
        });

        findViewById(R.id.ic_mpBrush).setOnClickListener(new OnClickListener() {

            class BrushSize implements OnSeekBarChangeListener {
                BrushSize() {
                }

                public void onProgressChanged(android.widget.SeekBar seekBar, int progress, boolean fromUser) {
                    magicDrawingView.setBrushSize(progress);
                }

                public void onStartTrackingTouch(android.widget.SeekBar seekBar) {
                }

                public void onStopTrackingTouch(android.widget.SeekBar seekBar) {
                }
            }

            class BrushOpacity implements OnSeekBarChangeListener {
                BrushOpacity() {
                }

                public void onProgressChanged(android.widget.SeekBar seekBar, int progress, boolean fromUser) {
                    magicDrawingView.setBrushOpacity((float) progress);
                }

                public void onStartTrackingTouch(android.widget.SeekBar seekBar) {
                }

                public void onStopTrackingTouch(android.widget.SeekBar seekBar) {
                }
            }

            public void onClick(View v) {
                findViewById(R.id.rv_mpImage).setVisibility(View.GONE);
                cvPenMp1.setBackground(getResources().getDrawable(R.drawable.gradient_magic_paint));
                imgPenMp1.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.neutral_trang)));
                cvPaintMp1.setBackgroundColor(getResources().getColor(R.color.neutral_trang));
                imgPaintMp1.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_3D87F8)));
                findViewById(R.id.ll_mpSeekBars).setVisibility(View.VISIBLE);
                sbmpBrushSize.setOnSeekBarChangeListener(new BrushSize());
                sbmpBrushOpacity.setOnSeekBarChangeListener(new BrushOpacity());
            }
        });

        findViewById(R.id.mcvUndoMagicPaint1).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                magicDrawingView.onClickUndo();
            }
        });

        findViewById(R.id.mcvRedoMagicPaint1).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                magicDrawingView.onClickRedo();
            }
        });

        findViewById(R.id.mcvRestoreMagicPaint1).setOnClickListener(new OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            public void onClick(View v) {
                deleteDialog("Magic Paint");
            }
        });
    }

    private void setPaintColors() {
        this.mPaintAdapter = new PaintAdapter(this, this.colors, this.pColor, "Paint");
        this.rvPaintColors.setAdapter(this.mPaintAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        manager.scrollToPositionWithOffset(this.pIndex, (this.mAppController.getScreenDisplay(this).get(0) / 2) - this.mAppController.dpToPx(21));
        this.rvPaintColors.setLayoutManager(manager);
    }

    private void setMPaintImages() {
        this.mPaintAdapter = new PaintAdapter(this, this.icons, this.mpImage, "Magic Paint");
        this.rvMPaintImage.setAdapter(this.mPaintAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        manager.scrollToPositionWithOffset(this.mpIndex, (this.mAppController.getScreenDisplay(this).get(0) / 2) - this.mAppController.dpToPx(21));
        this.rvMPaintImage.setLayoutManager(manager);
    }

    public void updatePaintValues(String paintType, String value) {
        int obj = -1;
        switch (paintType.hashCode()) {
            case 76875838:
                if (paintType.equals("Paint")) {
                    obj = 0;
                    break;
                }
                break;
            case 1270527979:
                if (paintType.equals("Magic Paint")) {
                    obj = 1;
                    break;
                }
                break;
        }
        int i;
        switch (obj) {
            case 0:
                this.pColor = value;
                for (i = 0; i < this.colors.length; i++) {
                    if (this.colors[i].equals(this.pColor)) {
                        this.pIndex = i;
                        setPaintColors();
                        this.drawingView.setColor(this.pColor);
                        return;
                    }
                }
                return;
            case 1:
                this.mpImage = value;
                for (i = 0; i < this.icons.length; i++) {
                    if (this.icons[i].equals(this.mpImage)) {
                        this.mpIndex = i;
                        setPaintColors();
                        try {
                            this.magicDrawingView.init(BitmapFactory.decodeStream(getAssets().open("MagicPaint/" + value)));
                            return;
                        } catch (IOException e) {
                            e.printStackTrace();
                            return;
                        }
                    }
                }
                return;
            default:
                return;
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void deleteDialog(final String paintType) {
        int ckeck = 0;
        MaterialCardView mcvRestore = findViewById(R.id.mcvRestoreMagicPaint);
        ImageView imgClear = findViewById(R.id.ic_clear);
        MaterialCardView mcvRestoreMp = findViewById(R.id.mcvRestoreMagicPaint1);
        ImageView imgClearMp = findViewById(R.id.ic_mpClear);

        if (paintType == "Paint") {
            mcvRestore.setBackground(getResources().getDrawable(R.drawable.gradient_magic_paint));
            imgClear.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.neutral_trang)));
            cvPenMp.setBackgroundColor(getResources().getColor(R.color.neutral_trang));
            imgPenMp.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_3D87F8)));
        } else if (paintType == "Magic Paint") {
            if (cvPenMp1.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.gradient_magic_paint).getConstantState())) {
                mcvRestoreMp.setBackground(getResources().getDrawable(R.drawable.gradient_magic_paint));
                imgClearMp.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.neutral_trang)));
                cvPenMp1.setBackgroundColor(getResources().getColor(R.color.neutral_trang));
                imgPenMp1.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_3D87F8)));
                ckeck = 1;
            }
            if (cvPaintMp1.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.gradient_magic_paint).getConstantState())) {
                mcvRestoreMp.setBackground(getResources().getDrawable(R.drawable.gradient_magic_paint));
                imgClearMp.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.neutral_trang)));
                cvPaintMp1.setBackgroundColor(getResources().getColor(R.color.neutral_trang));
                imgPaintMp1.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_3D87F8)));
                ckeck = 2;
            }
        }
        TextView tv_cancel, tv_delete, txtDelete, txtAreYou;
        final Dialog dialog = new Dialog(CreateNewScreenActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_delete);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        tv_cancel = dialog.findViewById(R.id.tv_cancel);
        tv_delete = dialog.findViewById(R.id.tv_delete);
        txtDelete = dialog.findViewById(R.id.txtDelete);
        txtAreYou = dialog.findViewById(R.id.txtAreYou);
        tv_delete.setText(getResources().getString(R.string.Reset));
        txtDelete.setText(getResources().getString(R.string.Reset));
        txtAreYou.setText(getResources().getString(R.string.Are_you));
        int finalCkeck = ckeck;

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (paintType == "Paint") {
                    cvPenMp.setBackground(getResources().getDrawable(R.drawable.gradient_magic_paint));
                    imgPenMp.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.neutral_trang)));
                    mcvRestore.setBackgroundColor(getResources().getColor(R.color.neutral_trang));
                    imgClear.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_3D87F8)));
                } else if (paintType == "Magic Paint") {
                    if (finalCkeck == 1) {
                        cvPenMp1.setBackground(getResources().getDrawable(R.drawable.gradient_magic_paint));
                        imgPenMp1.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.neutral_trang)));
                        mcvRestoreMp.setBackgroundColor(getResources().getColor(R.color.neutral_trang));
                        imgClearMp.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_3D87F8)));
                    } else if (finalCkeck == 2) {
                        cvPaintMp1.setBackground(getResources().getDrawable(R.drawable.gradient_magic_paint));
                        imgPaintMp1.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.neutral_trang)));
                        mcvRestoreMp.setBackgroundColor(getResources().getColor(R.color.neutral_trang));
                        imgClearMp.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_3D87F8)));
                    }
                }
            }
        });

        tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (paintType == "Paint") {
                    drawingView.eraseAll();
                    cvPenMp.setBackground(getResources().getDrawable(R.drawable.gradient_magic_paint));
                    imgPenMp.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.neutral_trang)));
                    mcvRestore.setBackgroundColor(getResources().getColor(R.color.neutral_trang));
                    imgClear.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_3D87F8)));
                } else if (paintType == "Magic Paint") {
                    magicDrawingView.eraseAll();
                    if (finalCkeck == 1) {
                        cvPenMp1.setBackground(getResources().getDrawable(R.drawable.gradient_magic_paint));
                        imgPenMp1.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.neutral_trang)));
                        mcvRestoreMp.setBackgroundColor(getResources().getColor(R.color.neutral_trang));
                        imgClearMp.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_3D87F8)));
                    } else if (finalCkeck == 2) {
                        cvPaintMp1.setBackground(getResources().getDrawable(R.drawable.gradient_magic_paint));
                        imgPaintMp1.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.neutral_trang)));
                        mcvRestoreMp.setBackgroundColor(getResources().getColor(R.color.neutral_trang));
                        imgClearMp.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_3D87F8)));
                    }
                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
                int writeExtStorage = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
                return writeExtStorage == PackageManager.PERMISSION_GRANTED;
            } else {
                return true;
            }
        }
        return true;
    }

    private void textStickerVisibility(List<TextStickerView> views, int visibility) {
        for (View view : views) {
            view.setVisibility(visibility);
        }
    }

    private void imageStickerVisibility(ArrayList<View> views, int visibility) {
        Iterator it = views.iterator();
        while (it.hasNext()) {
            ((View) it.next()).setVisibility(visibility);
        }
    }

    public static int dpToPx(int dp, Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        int px = Math.round(dp * density);
        return px;
    }

    @SuppressLint("SetTextI18n")
    private void eraseDialog() {
        TextView tv_cancel, tv_delete, txtDelete, txtAreYou;
        final Dialog dialog = new Dialog(CreateNewScreenActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_delete);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        tv_cancel = dialog.findViewById(R.id.tv_cancel);
        tv_delete = dialog.findViewById(R.id.tv_delete);
        txtDelete = dialog.findViewById(R.id.txtDelete);
        txtAreYou = dialog.findViewById(R.id.txtAreYou);
        tv_delete.setText(getResources().getString(R.string.Reset));
        txtDelete.setText(getResources().getString(R.string.Reset));

        txtAreYou.setText(getResources().getString(R.string.Are_you));
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textStickerVisibility(mTextStickerList, 4);
                imageStickerVisibility(mViews, 4);
                if (ivBackground.getDrawable() != null) {
                    ivBackground.setImageDrawable(null);
                    rlColor.setBackgroundColor(-1);
                }
                drawingView.eraseAll();
                magicDrawingView.eraseAll();
                txtText.setTextColor(Color.parseColor("#8394AF"));
                txtStickers.setTextColor(Color.parseColor("#8394AF"));
                txtMagicPaint.setTextColor(Color.parseColor("#8394AF"));
                txtPaint.setTextColor(Color.parseColor("#8394AF"));
                txtBackgrounds.setTextColor(Color.parseColor("#8394AF"));

                imgText.setImageResource(R.drawable.ic_text);
                imgStickers.setImageResource(R.drawable.ic_sticker);
                imgMagicPaint.setImageResource(R.drawable.ic_magic_paint);
                imgPaint.setImageResource(R.drawable.ic_paint);
                imgBackgrounds.setImageResource(R.drawable.ic_background);

                imgSelect2.setVisibility(View.INVISIBLE);
                imgSelect1.setVisibility(View.INVISIBLE);
                imgSelect3.setVisibility(View.INVISIBLE);
                imgSelect4.setVisibility(View.INVISIBLE);
                imgSelect5.setVisibility(View.INVISIBLE);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void cancelDialog() {
        TextView tv_cancel, tv_delete, txtDelete, txtAreYou;
        final Dialog dialog = new Dialog(CreateNewScreenActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_delete);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        tv_cancel = dialog.findViewById(R.id.tv_cancel);
        tv_delete = dialog.findViewById(R.id.tv_delete);
        txtDelete = dialog.findViewById(R.id.txtDelete);
        txtAreYou = dialog.findViewById(R.id.txtAreYou);
        tv_delete.setText(getResources().getString(R.string.Yes));
        txtDelete.setText(getResources().getString(R.string.Back_home));
        txtAreYou.setText(getResources().getString(R.string.cancel_msg));

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SpHelper.setPosition(CreateNewScreenActivity.this, SpHelper.SELECTED_FONT, 0);
                SpHelper.setPosition(CreateNewScreenActivity.this, SpHelper.SELECTED_ITEM_FONT, -1);
                finish();
            }
        });

        dialog.show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    protected void onStart() {
        if (DataHolderClass.getInstance().getText() != null) {
            createTextStickView();
            DataHolderClass.getInstance().setText(null);
            DataHolderClass.getInstance().setEditTextStickerView(false);
        }
        super.onStart();
    }

    protected void onResume() {
        if (DataHolderClass.getInstance().isRewarded()) {
            Toast.makeText(this, getResources().getString(R.string.thanks_for_watching), Toast.LENGTH_SHORT).show();
            this.mImagesAdapter.updateList();
            DataHolderClass.getInstance().setRewarded(false);
        }
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!checkClickText) {
            checkClickText = true;
        }

        if (!checkClickBg) {
            checkClickBg = true;
        }
    }

    public void onBackPressed() {
        popupWindow.dismiss();
        if (this.llWidgets.getVisibility() == View.VISIBLE) {
            this.llWidgets.setVisibility(View.GONE);
            this.llWidgets.startAnimation(this.pushUpOut);
            Log.d(TAG, "onBackPressed bg");
        } else if (findViewById(R.id.ll_mp_widget).getVisibility() == View.VISIBLE) {
            findViewById(R.id.ll_mp_widget).setVisibility(View.GONE);
        } else if (findViewById(R.id.ll_p_widget).getVisibility() == View.VISIBLE) {
            findViewById(R.id.ll_p_widget).setVisibility(View.GONE);
        } else {
            cancelDialog();
        }
    }
}
