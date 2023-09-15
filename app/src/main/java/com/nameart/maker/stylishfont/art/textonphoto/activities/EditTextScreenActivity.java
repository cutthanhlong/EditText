package com.nameart.maker.stylishfont.art.textonphoto.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.LinearGradient;
import android.graphics.MaskFilter;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.graphics.SweepGradient;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.ItemTouchHelper.Callback;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorChangedListener;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.slider.AlphaSlider;
import com.flask.colorpicker.slider.LightnessSlider;
import com.google.android.material.card.MaterialCardView;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.jaredrummler.materialspinner.MaterialSpinner.OnItemSelectedListener;
import com.nameart.maker.stylishfont.art.textonphoto.R;
import com.nameart.maker.stylishfont.art.textonphoto.adapter.FontsAdapter;
import com.nameart.maker.stylishfont.art.textonphoto.adapter.GradientsTextAdapter;
import com.nameart.maker.stylishfont.art.textonphoto.adapter.PatternsAdapter;
import com.nameart.maker.stylishfont.art.textonphoto.adapter.TabFontAdapter;
import com.nameart.maker.stylishfont.art.textonphoto.utils.AppController;
import com.nameart.maker.stylishfont.art.textonphoto.utils.DataHolderClass;
import com.nameart.maker.stylishfont.art.textonphoto.utils.SpHelper;
import com.nameart.maker.stylishfont.art.textonphoto.utils.SystemUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class EditTextScreenActivity extends AppCompatActivity implements OnClickListener {
    private Bitmap bmPattern;
    private EditText et_input;
    private String[] fonts;
    private String gradientTile = "Clamp";
    private String gradientType = "";
    private String[] gradients;
    private GridView gvColors;
    private GridView gvFonts;
    private GridView gvGradients;
    private GridView gvPatterns;
    private GridView gvShadow;
    private String imgPatternPath = "Patterns/text20.png";
    private String linearDirection = "Horizontal";
    private AppController mAppController;
    private FontsAdapter mFontsAdapter;
    private GradientsTextAdapter mGradientsTextAdapter;
    private PatternsAdapter mPatternsAdapter1;
    private List<View> mainViews = new ArrayList();
    private MaterialSpinner msgStyle;
    private MaterialSpinner msgTile;
    private MaterialSpinner msgType;
    private MaterialSpinner mspStyle;
    private MaterialSpinner mspTile;
    private TileMode patternTile1 = TileMode.REPEAT;
    private String[] patterns;
    private String selectedTab;
    private String[] separated;
    private int shadowColor;
    private int shadowRadius = 10;
    private int shadowXY = 8;
    private String strApply = "Color";
    private String style = "Normal";
    private TextView tv_input;
    private Typeface typeface;
    private ImageView imgBack, imgDelete, imgChecked;
    private TextView txtName;
    private CardView mcvFonts, mcvColors, mcvGradients, mcvPatterns, mcvShadow;
    private ImageView imgFonts, imgColors, imgGradients, imgPatterns, imgShadow;
    private CardView cvColorPicker;
    private LightnessSlider lightnessSlider;
    private ColorPickerView colorPicker;
    private AlphaSlider alphaSlider;
    private LightnessSlider lightnessSlider1;
    private ColorPickerView colorPicker1;
    private AlphaSlider alphaSlider1;
    private SeekBar sbBlur, sbDistance;
    private ConstraintLayout clColorPicker;
    private LinearLayout llSeekbarBlur, llSeekbarDistance;
    private PopupWindow popupWindow, popupWindow1;
    private View popupView, popupView1;
    private MaterialCardView mcvSpinner, mcvSpinner1;
    private TextView txtName1, txtName2, txtName0;
    private TextView txtName11, txtName21, txtName01;
    private RelativeLayout rlEditText;
    private MaterialCardView mcvOrientation;
    private ImageView imgOrientation;
    private int pauseResumeFlag = 0;
    private TabFontAdapter tabFontAdapter;
    private RecyclerView rvTabFont;
    private int posTabFont, posItemFont;
    private String colorChange;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SystemUtil.setLocale(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView((int) R.layout.activity_edit_text);

        gradientType = getResources().getString(R.string.Line);
        popupView = LayoutInflater.from(EditTextScreenActivity.this).inflate(R.layout.item_spinner_txt_texture, null);
        popupWindow = new PopupWindow(popupView, findViewById(R.id.mcvSpinner).getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);

        popupView1 = LayoutInflater.from(EditTextScreenActivity.this).inflate(R.layout.item_spinner_txt_gradient, null);
        popupWindow1 = new PopupWindow(popupView1, findViewById(R.id.mcvSpinner1).getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);

        findView();

        tv_input.setVisibility(View.INVISIBLE);
        if (SpHelper.getPosition(EditTextScreenActivity.this, SpHelper.SELECTED_FONT, -1) != -1 &&
                SpHelper.getPosition(EditTextScreenActivity.this, SpHelper.SELECTED_ITEM_FONT, -1) != -1) {
            posTabFont = SpHelper.getPosition(EditTextScreenActivity.this, SpHelper.SELECTED_FONT, -1);
            posItemFont = SpHelper.getPosition(EditTextScreenActivity.this, SpHelper.SELECTED_ITEM_FONT, -1);
            String[] menuTitles;
            menuTitles = getResources().getStringArray(R.array.Fonts_tabmenu);
            this.fonts = new String[0];
            try {
                this.fonts = getAssets().list("Fonts/" + menuTitles[posTabFont]);
                ArrayList<String> listFonts = new ArrayList(Arrays.asList(this.fonts));
                Collections.sort(listFonts, String.CASE_INSENSITIVE_ORDER);
                for (int i = 0; i < listFonts.size(); i++) {
                    this.fonts[i] = (String) listFonts.get(i);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            Typeface typeface1 = Typeface.createFromAsset(getAssets(), "Fonts/" + menuTitles[posTabFont] + "/" + fonts[posItemFont]);
            DataHolderClass.getInstance().setFont(typeface1);
            hideSoftInput();
            et_input.setTypeface(typeface1);
            tv_input.setTypeface(typeface1);
        } else {
            posTabFont = 0;
            posItemFont = -1;
        }


        if (SpHelper.getPosition(EditTextScreenActivity.this, SpHelper.SELECTED_ITEM_FONT, -1) == -1) {
            tv_input.setTypeface(ResourcesCompat.getFont(EditTextScreenActivity.this, R.font.urbanist_600));
            et_input.setTypeface(ResourcesCompat.getFont(EditTextScreenActivity.this, R.font.urbanist_600));
        }
        clickItem();
        visibleView();
        initView();
        clickToolBar();

        try {
            this.patterns = getAssets().list("Patterns");
        } catch (IOException e) {
            e.printStackTrace();
        }
        int pos = SpHelper.getPosition(EditTextScreenActivity.this, SpHelper.ITEM_PATTERN, -1);
        String posColor = SpHelper.getValueFromSharedprefrence(EditTextScreenActivity.this, SpHelper.COLOR_TEXT, "");
        if (pos != -1) {
            strApply = "Pattern";
            hideSoftInput();
            imgPatternPath = "Patterns/" + patterns[pos];
            applyPattern();
        }

        if (SpHelper.getBoolean(EditTextScreenActivity.this, SpHelper.IS_COLOR_TEXT, false)) {
            if (!posColor.equals("")) {
                et_input.setHintTextColor(Color.parseColor(posColor));
            }
        }
    }


    private void findView() {

        this.mAppController = new AppController(this);
        this.et_input = findViewById(R.id.et_input);
        this.tv_input = findViewById(R.id.tv_input);
        this.gvFonts = findViewById(R.id.gv_fonts);
        this.gvColors = findViewById(R.id.gv_colors);
        this.gvGradients = findViewById(R.id.gv_gradients);
        this.gvPatterns = findViewById(R.id.gv_patterns);
        this.gvShadow = findViewById(R.id.gv_shadow);

        this.msgType = findViewById(R.id.sp_gType);
        this.msgStyle = findViewById(R.id.sp_gStyle);
        this.msgTile = findViewById(R.id.sp_gTile);
        this.mspStyle = findViewById(R.id.sp_pStyle);
        this.mspTile = findViewById(R.id.sp_pTile);

        this.imgBack = findViewById(R.id.imgBack);
        this.imgDelete = findViewById(R.id.imgDelete);
        this.imgChecked = findViewById(R.id.imgChecked);
        this.txtName = findViewById(R.id.txtName);

        this.mcvFonts = findViewById(R.id.mcvFonts);
        this.mcvColors = findViewById(R.id.mcvColors);
        this.mcvGradients = findViewById(R.id.mcvGradients);
        this.mcvPatterns = findViewById(R.id.mcvPatterns);
        this.mcvShadow = findViewById(R.id.mcvShadow);

        this.imgFonts = findViewById(R.id.imgFonts);
        this.imgColors = findViewById(R.id.imgColors);
        this.imgGradients = findViewById(R.id.imgGradients);
        this.imgPatterns = findViewById(R.id.imgPatterns);
        this.imgShadow = findViewById(R.id.imgShadow);

        this.cvColorPicker = findViewById(R.id.cvColorPicker);

        this.alphaSlider = findViewById(R.id.v_alpha_slider);
        this.lightnessSlider = findViewById(R.id.v_lightness_slider);
        this.colorPicker = findViewById(R.id.colorPicker);
        this.alphaSlider1 = findViewById(R.id.v_alpha_slider1);
        this.lightnessSlider1 = findViewById(R.id.v_lightness_slider1);
        this.colorPicker1 = findViewById(R.id.colorPicker1);

        this.sbBlur = findViewById(R.id.sbBlur);
        this.sbDistance = findViewById(R.id.sbDistance);

        this.clColorPicker = findViewById(R.id.clColorPicker);
        this.llSeekbarBlur = findViewById(R.id.llSeekbarBlur);
        this.llSeekbarDistance = findViewById(R.id.llSeekbarDistance);

        this.txtName0 = findViewById(R.id.txtName0);
        this.mcvSpinner = findViewById(R.id.mcvSpinner);
        this.txtName1 = popupView.findViewById(R.id.txtName1);
        this.txtName2 = popupView.findViewById(R.id.txtName2);

        this.txtName01 = findViewById(R.id.txtName01);
        this.mcvSpinner1 = findViewById(R.id.mcvSpinner1);
        this.txtName11 = popupView1.findViewById(R.id.txtName11);
        this.txtName21 = popupView1.findViewById(R.id.txtName21);

        this.rlEditText = findViewById(R.id.rlEditText);
        this.mcvOrientation = findViewById(R.id.mcvOrientation);
        this.imgOrientation = findViewById(R.id.imgOrientation);
        this.rvTabFont = findViewById(R.id.rvFont);
    }

    private void clickItem() {
        findViewById(R.id.mcvFonts).setOnClickListener(this);
        findViewById(R.id.mcvColors).setOnClickListener(this);
        findViewById(R.id.mcvGradients).setOnClickListener(this);
        findViewById(R.id.mcvPatterns).setOnClickListener(this);
        findViewById(R.id.mcvShadow).setOnClickListener(this);
    }

    private void visibleView() {
        this.imgBack.setVisibility(View.GONE);
        this.imgDelete.setVisibility(View.VISIBLE);
        this.imgChecked.setVisibility(View.VISIBLE);
        this.txtName.setText(getResources().getString(R.string.Edit_Text));
    }

    private void initView() {
        this.mainViews.add(findViewById(R.id.ll_fonts));
        this.mainViews.add(this.gvColors);
        this.mainViews.add(findViewById(R.id.ll_gradients));
        this.mainViews.add(findViewById(R.id.ll_patterns));
        this.mainViews.add(findViewById(R.id.ll_shadow));
        this.msgType.setItems("Linear", "Radial", "Sweep");
        this.msgStyle.setItems("Normal", "Emboss", "Deboss");
        this.msgTile.setItems("Clamp", "Mirror", "Repeat");
        this.mspStyle.setItems("Normal", "Emboss", "Deboss");
        this.mspTile.setItems("Repeat", "Mirror");
//        setTablayoutMenu(R.id.tab_fonts);
        String[] menuTitles;
        menuTitles = getResources().getStringArray(R.array.Fonts_tabmenu);
        if (SpHelper.getPosition(EditTextScreenActivity.this, SpHelper.SELECTED_FONT, -1) == -1) {
            setFonts(menuTitles[0]);
        } else {
            setFonts(menuTitles[SpHelper.getPosition(EditTextScreenActivity.this, SpHelper.SELECTED_FONT, -1)]);
        }
        selectedTabBottomBar(1);
        tabFontAdapter = new TabFontAdapter(menuTitles, this, true,
                SpHelper.getPosition(EditTextScreenActivity.this, SpHelper.SELECTED_FONT, -1),
                new TabFontAdapter.OnClickItem() {
                    @Override
                    public void onClickItem(int pos) {
                        posTabFont = pos;
                        EditTextScreenActivity.this.setFonts(menuTitles[pos]);
                    }
                });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        rvTabFont.setLayoutManager(linearLayoutManager);
        if (SpHelper.getPosition(EditTextScreenActivity.this, SpHelper.SELECTED_FONT, -1) != -1) {
            rvTabFont.scrollToPosition(SpHelper.getPosition(EditTextScreenActivity.this, SpHelper.SELECTED_FONT, -1));
        }
        rvTabFont.setAdapter(tabFontAdapter);
        this.et_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tv_input.setText(et_input.getText().toString());
                int obj = -1;
                switch (strApply.hashCode()) {
                    case -1819712192:
                        if (strApply.equals("Shadow")) {
                            obj = 2;
                            break;
                        }
                        break;
                    case 154295120:
                        if (strApply.equals("Gradient")) {
                            obj = 0;
                            break;
                        }
                        break;
                    case 873562992:
                        if (strApply.equals("Pattern")) {
                            obj = 1;
                            break;
                        }
                        break;
                }
                switch (obj) {
                    case 0:
                        applyGradient();
                        return;
                    case 1:
                        applyPattern();
                        return;
                    default:
                        return;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        if (SpHelper.getPosition(EditTextScreenActivity.this, SpHelper.SELECTED_ITEM_FONT, -1) != -1) {
            if (DataHolderClass.getInstance().getFont() != null) {
                this.et_input.setTypeface(DataHolderClass.getInstance().getFont());
                this.tv_input.setTypeface(DataHolderClass.getInstance().getFont());
            }
            if (DataHolderClass.getInstance().getTvShader() != null) {
                this.tv_input.getPaint().setShader(DataHolderClass.getInstance().getTvShader());
            }
        }
    }

    private void setFonts(String tabSelected) {
        String[] menuTitles;
        menuTitles = getResources().getStringArray(R.array.Fonts_tabmenu);
        selectedTab = tabSelected;
        this.fonts = new String[0];
        try {
            this.fonts = getAssets().list("Fonts/" + selectedTab);
            ArrayList<String> listFonts = new ArrayList(Arrays.asList(this.fonts));
            Collections.sort(listFonts, String.CASE_INSENSITIVE_ORDER);
            for (int i = 0; i < listFonts.size(); i++) {
                this.fonts[i] = (String) listFonts.get(i);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (SpHelper.getPosition(EditTextScreenActivity.this, SpHelper.SELECTED_FONT, -1) != -1) {
            if (selectedTab.equals(menuTitles[SpHelper.getPosition(EditTextScreenActivity.this, SpHelper.SELECTED_FONT, -1)])) {
                this.mFontsAdapter = new FontsAdapter(this, this.selectedTab, this.fonts, true,
                        SpHelper.getPosition(EditTextScreenActivity.this, SpHelper.SELECTED_ITEM_FONT, -1),
                        SpHelper.getPosition(EditTextScreenActivity.this, SpHelper.SELECTED_FONT, -1),
                        new FontsAdapter.OnClickItem() {
                            @Override
                            public void onClickItem(int pos) {
                                posItemFont = pos;
                                typeface = Typeface.createFromAsset(getAssets(), "Fonts/" + selectedTab + "/" + fonts[pos]);
                                DataHolderClass.getInstance().setFont(typeface);
                                hideSoftInput();
                                et_input.setTypeface(typeface);
                                tv_input.setTypeface(typeface);
                            }
                        });
                if (SpHelper.getPosition(EditTextScreenActivity.this, SpHelper.SELECTED_ITEM_FONT, -1) != -1) {

                    int targetPosition = SpHelper.getPosition(EditTextScreenActivity.this, SpHelper.SELECTED_ITEM_FONT, -1);
                    int numColumns = gvFonts.getNumColumns();

                    int targetRow = targetPosition / numColumns;
                    int targetColumn = targetPosition % numColumns;

                    gvFonts.smoothScrollToPositionFromTop(targetRow, targetColumn, 300);
                }
                this.gvFonts.setAdapter(this.mFontsAdapter);
            } else {
                this.mFontsAdapter = new FontsAdapter(this, this.selectedTab, this.fonts, true,
                        -1,
                        -1,
                        new FontsAdapter.OnClickItem() {
                            @Override
                            public void onClickItem(int pos) {
                                posItemFont = pos;
                                typeface = Typeface.createFromAsset(getAssets(), "Fonts/" + selectedTab + "/" + fonts[pos]);
                                DataHolderClass.getInstance().setFont(typeface);
                                hideSoftInput();
                                et_input.setTypeface(typeface);
                                tv_input.setTypeface(typeface);
                            }
                        });
                this.gvFonts.setAdapter(this.mFontsAdapter);
            }
        } else {
            this.mFontsAdapter = new FontsAdapter(this, this.selectedTab, this.fonts, true,
                    -1,
                    -1,
                    new FontsAdapter.OnClickItem() {
                        @Override
                        public void onClickItem(int pos) {
                            posItemFont = pos;
                            typeface = Typeface.createFromAsset(getAssets(), "Fonts/" + selectedTab + "/" + fonts[pos]);
                            DataHolderClass.getInstance().setFont(typeface);
                            hideSoftInput();
                            et_input.setTypeface(typeface);
                            tv_input.setTypeface(typeface);
                        }
                    });
            this.gvFonts.setAdapter(this.mFontsAdapter);
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    private void setGradients() {
        rlEditText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow1.dismiss();
            }
        });

        tv_input.setOnTouchListener((view, motionEvent) -> {
            popupWindow1.dismiss();
            return false;
        });

        et_input.setOnTouchListener((view, motionEvent) -> {
            popupWindow1.dismiss();
            return false;
        });

        gvPatterns.setOnTouchListener((view, motionEvent) -> {
            popupWindow1.dismiss();
            return false;
        });

        int num = dpToPx(126, EditTextScreenActivity.this);
        popupWindow1 = new PopupWindow(popupView1, num, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.gradients = new String[0];
        this.gradients = getResources().getStringArray(R.array.Gradients);
        this.mGradientsTextAdapter = new GradientsTextAdapter(this,
                this.gradients,
                this.gradientTile,
                this.gradientType,
                this.linearDirection, true,
                SpHelper.getPosition(EditTextScreenActivity.this, SpHelper.ITEM_GRADIENT_TEXT, -1),
                new GradientsTextAdapter.OnClickItem() {
                    @Override
                    public void onClickItem(int position) {
                        SpHelper.setString(EditTextScreenActivity.this, SpHelper.TYPE_GRADIENT_TEXT, gradientType);
                        SpHelper.setPosition(EditTextScreenActivity.this, SpHelper.ITEM_GRADIENT_TEXT, position);
                        SpHelper.setPosition(EditTextScreenActivity.this, SpHelper.ITEM_PATTERN, -1);
                        SpHelper.putValueInSharedpreference(EditTextScreenActivity.this, SpHelper.COLOR_TEXT, "#FFFFFFFF");
                        SpHelper.setBoolean(EditTextScreenActivity.this, SpHelper.IS_COLOR_TEXT, false);
                        popupWindow1.dismiss();
                        strApply = "Gradient";
                        hideSoftInput();
                        separated = gradients[position].split(" ");
                        applyGradient();
                    }
                });
        this.gvGradients.setAdapter(this.mGradientsTextAdapter);

        this.msgType.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                gradientType = view.getText().toString();
                mGradientsTextAdapter = new GradientsTextAdapter(EditTextScreenActivity.this,
                        gradients,
                        gradientTile,
                        gradientType,
                        linearDirection, true,
                        SpHelper.getPosition(EditTextScreenActivity.this, SpHelper.ITEM_GRADIENT_TEXT, -1), new GradientsTextAdapter.OnClickItem() {
                    @Override
                    public void onClickItem(int position) {
                        SpHelper.setString(EditTextScreenActivity.this, SpHelper.TYPE_GRADIENT_TEXT, gradientType);
                        SpHelper.setPosition(EditTextScreenActivity.this, SpHelper.ITEM_PATTERN, -1);
                        SpHelper.putValueInSharedpreference(EditTextScreenActivity.this, SpHelper.COLOR_TEXT, "#FFFFFFFF");
                        SpHelper.setBoolean(EditTextScreenActivity.this, SpHelper.IS_COLOR_TEXT, false);
                    }
                });
                gvGradients.setAdapter(mGradientsTextAdapter);
            }
        });

        this.msgStyle.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                style = view.getText().toString();
                applyGradient();
            }
        });

        this.msgTile.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                gradientTile = view.getText().toString();
                mGradientsTextAdapter = new GradientsTextAdapter(EditTextScreenActivity.this,
                        gradients,
                        gradientTile,
                        gradientType,
                        linearDirection, true,
                        SpHelper.getPosition(EditTextScreenActivity.this, SpHelper.ITEM_GRADIENT_TEXT, -1),
                        new GradientsTextAdapter.OnClickItem() {
                            @Override
                            public void onClickItem(int position) {
                                SpHelper.setString(EditTextScreenActivity.this, SpHelper.TYPE_GRADIENT_TEXT, gradientType);
                                SpHelper.setPosition(EditTextScreenActivity.this, SpHelper.ITEM_PATTERN, -1);
                                SpHelper.putValueInSharedpreference(EditTextScreenActivity.this, SpHelper.COLOR_TEXT, "#FFFFFFFF");
                                SpHelper.setBoolean(EditTextScreenActivity.this, SpHelper.IS_COLOR_TEXT, false);
                            }
                        });
                gvGradients.setAdapter(mGradientsTextAdapter);
            }
        });

        txtName11.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                gradientType = txtName11.getText().toString();
                gradientTile = "Clamp";
                if (gradientType.equals(getResources().getString(R.string.Radial)) || gradientType.equals(getResources().getString(R.string.Sweep)))
                    findViewById(R.id.mcvOrientation).setVisibility(View.INVISIBLE);
                else findViewById(R.id.mcvOrientation).setVisibility(View.VISIBLE);
                mGradientsTextAdapter = new GradientsTextAdapter(EditTextScreenActivity.this,
                        gradients,
                        gradientTile,
                        gradientType,
                        linearDirection, true,
                        SpHelper.getPosition(EditTextScreenActivity.this, SpHelper.ITEM_GRADIENT_TEXT, -1),
                        new GradientsTextAdapter.OnClickItem() {
                            @Override
                            public void onClickItem(int position) {
                                SpHelper.setString(EditTextScreenActivity.this, SpHelper.TYPE_GRADIENT_TEXT, gradientType);
                                SpHelper.setPosition(EditTextScreenActivity.this, SpHelper.ITEM_PATTERN, -1);
                                SpHelper.putValueInSharedpreference(EditTextScreenActivity.this, SpHelper.COLOR_TEXT, "#FFFFFFFF");
                                SpHelper.setBoolean(EditTextScreenActivity.this, SpHelper.IS_COLOR_TEXT, false);
                                strApply = "Gradient";
                                hideSoftInput();
                                separated = gradients[position].split(" ");
                                SpHelper.setPosition(EditTextScreenActivity.this, SpHelper.ITEM_GRADIENT_TEXT, position);
                                applyGradient();
                            }
                        });
                gvGradients.setAdapter(mGradientsTextAdapter);
                String tmp = txtName01.getText().toString();
                txtName01.setText(gradientType);
                txtName11.setText(tmp);
                popupWindow1.dismiss();
            }
        });

        txtName21.setOnClickListener(view -> {
            gradientTile = "Clamp";
            gradientType = txtName21.getText().toString();
            if (gradientType.equals(getResources().getString(R.string.Radial)) || gradientType.equals(getResources().getString(R.string.Sweep)))
                findViewById(R.id.mcvOrientation).setVisibility(View.INVISIBLE);
            else findViewById(R.id.mcvOrientation).setVisibility(View.VISIBLE);
            mGradientsTextAdapter = new GradientsTextAdapter(EditTextScreenActivity.this,
                    gradients,
                    gradientTile,
                    gradientType,
                    linearDirection, true,
                    SpHelper.getPosition(EditTextScreenActivity.this, SpHelper.ITEM_GRADIENT_TEXT, -1),
                    new GradientsTextAdapter.OnClickItem() {
                        @Override
                        public void onClickItem(int position) {
                            SpHelper.setString(EditTextScreenActivity.this, SpHelper.TYPE_GRADIENT_TEXT, gradientType);
                            SpHelper.setPosition(EditTextScreenActivity.this, SpHelper.ITEM_PATTERN, -1);
                            SpHelper.putValueInSharedpreference(EditTextScreenActivity.this, SpHelper.COLOR_TEXT, "#FFFFFFFF");
                            SpHelper.setBoolean(EditTextScreenActivity.this, SpHelper.IS_COLOR_TEXT, false);
                            strApply = "Gradient";
                            hideSoftInput();
                            separated = gradients[position].split(" ");
                            applyGradient();
                            SpHelper.setPosition(EditTextScreenActivity.this, SpHelper.ITEM_GRADIENT_TEXT, position);
                        }
                    });
            gvGradients.setAdapter(mGradientsTextAdapter);
            String tmp = txtName01.getText().toString();
            txtName01.setText(gradientType);
            txtName21.setText(tmp);
            popupWindow1.dismiss();
        });

        findViewById(R.id.mcvSpinner1).setOnClickListener(view -> {
            hideSoftInput();
            if (popupWindow1.isShowing()) {
                popupWindow1.dismiss();
            } else {
                popupWindow1.showAsDropDown(mcvSpinner1, 0, dpToPx(4, EditTextScreenActivity.this));
            }
        });

        mcvOrientation.setOnClickListener(view -> {
            popupWindow.dismiss();
            popupWindow1.dismiss();
            if (pauseResumeFlag == 0) {
                pauseResumeFlag = 1;
                imgOrientation.setImageResource(R.drawable.ic_vertical);
                linearDirection = "Vertical";
                mGradientsTextAdapter = new GradientsTextAdapter(EditTextScreenActivity.this,
                        gradients,
                        gradientTile,
                        gradientType,
                        linearDirection, true,
                        SpHelper.getPosition(EditTextScreenActivity.this, SpHelper.ITEM_GRADIENT_TEXT, -1),
                        new GradientsTextAdapter.OnClickItem() {
                            @Override
                            public void onClickItem(int position) {
                                SpHelper.setString(EditTextScreenActivity.this, SpHelper.TYPE_GRADIENT_TEXT, gradientType);
                                SpHelper.setPosition(EditTextScreenActivity.this, SpHelper.ITEM_PATTERN, -1);
                                SpHelper.putValueInSharedpreference(EditTextScreenActivity.this, SpHelper.COLOR_TEXT, "#FFFFFFFF");
                                SpHelper.setBoolean(EditTextScreenActivity.this, SpHelper.IS_COLOR_TEXT, false);
                                strApply = "Gradient";
                                hideSoftInput();
                                separated = gradients[position].split(" ");
                                applyGradient();
                                SpHelper.setPosition(EditTextScreenActivity.this, SpHelper.ITEM_GRADIENT_TEXT, position);
                            }
                        });
                gvGradients.setAdapter(mGradientsTextAdapter);
            } else {
                pauseResumeFlag = 0;
                imgOrientation.setImageResource(R.drawable.ic_horizontal);
                linearDirection = "Horizontal";
                mGradientsTextAdapter = new GradientsTextAdapter(EditTextScreenActivity.this,
                        gradients,
                        gradientTile,
                        gradientType,
                        linearDirection, true,
                        SpHelper.getPosition(EditTextScreenActivity.this, SpHelper.ITEM_GRADIENT_TEXT, -1),
                        new GradientsTextAdapter.OnClickItem() {
                            @Override
                            public void onClickItem(int position) {
                                SpHelper.setString(EditTextScreenActivity.this, SpHelper.TYPE_GRADIENT_TEXT, gradientType);
                                SpHelper.setPosition(EditTextScreenActivity.this, SpHelper.ITEM_PATTERN, -1);
                                SpHelper.putValueInSharedpreference(EditTextScreenActivity.this, SpHelper.COLOR_TEXT, "#FFFFFFFF");
                                SpHelper.setBoolean(EditTextScreenActivity.this, SpHelper.IS_COLOR_TEXT, false);
                                strApply = "Gradient";
                                hideSoftInput();
                                separated = gradients[position].split(" ");
                                applyGradient();
                                SpHelper.setPosition(EditTextScreenActivity.this, SpHelper.ITEM_GRADIENT_TEXT, position);
                            }
                        });
                gvGradients.setAdapter(mGradientsTextAdapter);
            }
        });
    }

    private void applyGradient() {
        if (this.separated != null) {
            this.tv_input.getPaint().clearShadowLayer();
            Shader shader = null;
            TileMode tileMode = TileMode.MIRROR;
            int[] colors = new int[this.separated.length];
            for (int i = 0; i < this.separated.length; i++) {
                colors[i] = Color.parseColor(this.separated[i]);
            }
            if (this.gradientType == getResources().getString(R.string.Line)) {
                if (this.linearDirection == "Horizontal") {
                    shader = new LinearGradient(0.0f, 0.0f, (float) this.tv_input.getMeasuredWidth(), 0.0f, colors, null, tileMode);
                } else if (this.linearDirection == "Vertical") {
                    shader = new LinearGradient(0.0f, 0.0f, 0.0f, (float) this.tv_input.getHeight(), colors, null, tileMode);
                }
            } else if (this.gradientType == getResources().getString(R.string.Radial)) {
                shader = new RadialGradient((float) (this.tv_input.getWidth() / 2), (float) (this.tv_input.getHeight() / 2), (float) this.tv_input.getWidth(), colors, null, tileMode);
            } else if (this.gradientType == getResources().getString(R.string.Sweep)) {
                shader = new SweepGradient((float) (this.tv_input.getWidth() / 2), (float) (this.tv_input.getHeight() / 2), colors, null);
            }
            this.tv_input.getPaint().setMaskFilter(getMaskFilter(this.style));
            this.et_input.getPaint().setMaskFilter(getMaskFilter(this.style));
            this.tv_input.getPaint().setShader(shader);
            this.et_input.getPaint().setShader(shader);
            et_input.setHint(getResources().getString(R.string.Type_here));
            this.tv_input.setText(this.et_input.getText().toString());
            return;
        }
        Toast.makeText(this, getResources().getString(R.string.Please_choose_gradient_text_first), Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setPatterns() {
        try {
            this.patterns = getAssets().list("Patterns");
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.mPatternsAdapter1 = new PatternsAdapter(this, this.patterns, "Patterns",
                true,
                new PatternsAdapter.OnClickItem() {
                    @Override
                    public void onClickItem(int pos) {
                        SpHelper.setString(EditTextScreenActivity.this, SpHelper.TYPE_GRADIENT_TEXT, "");
                        SpHelper.setPosition(EditTextScreenActivity.this, SpHelper.ITEM_GRADIENT_TEXT, -1);
                        SpHelper.setPosition(EditTextScreenActivity.this, SpHelper.ITEM_PATTERN, pos);
                        SpHelper.putValueInSharedpreference(EditTextScreenActivity.this, SpHelper.COLOR_TEXT, "#FFFFFFFF");
                        SpHelper.setBoolean(EditTextScreenActivity.this, SpHelper.IS_COLOR_TEXT, false);
                        popupWindow.dismiss();
                        strApply = "Pattern";
                        hideSoftInput();
                        imgPatternPath = "Patterns/" + patterns[pos];
                        applyPattern();
                    }
                });

        this.gvPatterns.setAdapter(this.mPatternsAdapter1);
        this.mspStyle.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                style = view.getText().toString();
                applyPattern();
            }
        });
        int num = dpToPx(126, EditTextScreenActivity.this);
        popupWindow = new PopupWindow(popupView, num, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.mcvSpinner.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftInput();
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                } else {
                    popupWindow.showAsDropDown(mcvSpinner, 0, dpToPx(4, EditTextScreenActivity.this));
                }
            }
        });
        rlEditText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        tv_input.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                popupWindow.dismiss();
                return false;
            }
        });
        et_input.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                popupWindow.dismiss();
                return false;
            }
        });
        gvPatterns.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                popupWindow.dismiss();
                return false;
            }
        });
        txtName1.setOnClickListener(view -> {
            style = txtName1.getText().toString();
            applyPattern();
            String tmp = txtName0.getText().toString();
            txtName0.setText(style);
            txtName1.setText(tmp);
            popupWindow.dismiss();
        });
        txtName2.setOnClickListener(view -> {
            style = txtName2.getText().toString();
            applyPattern();
            String tmp = txtName0.getText().toString();
            txtName0.setText(style);
            txtName2.setText(tmp);
            popupWindow.dismiss();
        });
        this.mspTile.setOnItemSelectedListener((view, position, id, item) -> {
            String charSequence = view.getText().toString();
            int obj = -1;
            switch (charSequence.hashCode()) {
                case -1990043681:
                    if (charSequence.equals("Mirror")) {
                        obj = 1;
                        break;
                    }
                    break;
                case -1850664517:
                    if (charSequence.equals("Repeat")) {
                        obj = 2;
                        break;
                    }
                    break;
                case 65190043:
                    if (charSequence.equals("Clamp")) {
                        obj = 0;
                        break;
                    }
                    break;
            }
            switch (obj) {
                case 0:
                    patternTile1 = TileMode.CLAMP;
                    break;
                case 1:
                    patternTile1 = TileMode.MIRROR;
                    break;
                case 2:
                    patternTile1 = TileMode.REPEAT;
                    break;
            }
            applyPattern();
        });
    }

    public static int dpToPx(int dp, Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        int px = Math.round(dp * density);
        return px;
    }

    private void applyPattern() {
        if (this.imgPatternPath != "") {
            this.tv_input.getPaint().clearShadowLayer();
            this.et_input.getPaint().clearShadowLayer();
            this.bmPattern = Bitmap.createScaledBitmap(
                    this.mAppController.getBitmapFromAsset(this.imgPatternPath),
                    Callback.DEFAULT_SWIPE_ANIMATION_DURATION,
                    Callback.DEFAULT_SWIPE_ANIMATION_DURATION,
                    false);
            this.mAppController.setPattern(this.tv_input, this.bmPattern,
                    this.patternTile1, TileMode.MIRROR, getMaskFilter(this.style));
            this.mAppController.setPattern(this.et_input, this.bmPattern,
                    this.patternTile1, TileMode.MIRROR, getMaskFilter(this.style));
            this.et_input.setHint(getResources().getString(R.string.Type_here));
            this.tv_input.setText(this.et_input.getText().toString());
            return;
        }
        Toast.makeText(this, getResources().getString(R.string.Please_choose_pattern_first), Toast.LENGTH_SHORT).show();
    }

    private void setShadow() {
        lightnessSlider1.setColorPicker(colorPicker1);
        colorPicker1.addOnColorSelectedListener(new OnColorSelectedListener() {
            @Override
            public void onColorSelected(int selectedColor) {
                lightnessSlider1.setColor(selectedColor);
                if (!(strApply == "Color" && strApply == "Shadow")) {
                    tv_input.getPaint().setShader(null);
                    et_input.getPaint().setShader(null);
                }
                strApply = "Shadow";
                hideSoftInput();
                shadowColor = selectedColor;
                tv_input.setText(et_input.getText().toString());
                mAppController.setShadow(tv_input, (float) shadowRadius, (float) shadowXY, (float) shadowXY, shadowColor);
                mAppController.setShadow(et_input, (float) shadowRadius, (float) shadowXY, (float) shadowXY, shadowColor);
                tv_input.setText(et_input.getText().toString());
            }
        });
        colorPicker1.addOnColorChangedListener(selectedColor -> {
            lightnessSlider1.setColor(selectedColor);
            if (!(strApply == "Color" && strApply == "Shadow")) {
                tv_input.getPaint().setShader(null);
                et_input.getPaint().setShader(null);
            }
            strApply = "Shadow";
            hideSoftInput();
            shadowColor = selectedColor;
            tv_input.setText(et_input.getText().toString());
            mAppController.setShadow(tv_input, (float) shadowRadius, (float) shadowXY, (float) shadowXY, shadowColor);
            mAppController.setShadow(et_input, (float) shadowRadius, (float) shadowXY, (float) shadowXY, shadowColor);
            tv_input.setText(et_input.getText().toString());
        });

        this.sbBlur.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tv_input.getPaint().setShader(null);
                et_input.getPaint().setShader(null);
                shadowRadius = (progress / 5) + 5;
                mAppController.setShadow(tv_input, (float) shadowRadius, (float) shadowXY, (float) shadowXY, shadowColor);
                mAppController.setShadow(et_input, (float) shadowRadius, (float) shadowXY, (float) shadowXY, shadowColor);
                tv_input.setText(et_input.getText().toString());
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        this.sbDistance.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tv_input.getPaint().setShader(null);
                et_input.getPaint().setShader(null);
                shadowXY = progress / 10;
                mAppController.setShadow(tv_input, (float) shadowRadius, (float) shadowXY, (float) shadowXY, shadowColor);
                mAppController.setShadow(et_input, (float) shadowRadius, (float) shadowXY, (float) shadowXY, shadowColor);
                tv_input.setText(et_input.getText().toString());
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void holdShadow(boolean hold) {
        if (hold) {
            DataHolderClass.getInstance().setShadowXY(this.shadowXY);
            DataHolderClass.getInstance().setShadowRadius(this.shadowRadius);
            DataHolderClass.getInstance().setShadowColor(this.shadowColor);
            return;
        }
        DataHolderClass.getInstance().setShadowXY(0);
        DataHolderClass.getInstance().setShadowRadius(0);
        DataHolderClass.getInstance().setShadowColor(0);
    }

    private void hideSoftInput() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private MaskFilter getMaskFilter(String style) {
        EmbossMaskFilter embossFilter = null;
        if (style == "Emboss") {
            embossFilter = new EmbossMaskFilter(new float[]{1.0f, 5.0f, 1.0f}, 0.8f, 8.0f, 7.0f);
        } else if (style == "Deboss") {
            embossFilter = new EmbossMaskFilter(new float[]{0.0f, -1.0f, 0.5f}, 0.8f, 13.0f, 7.0f);
        }
        DataHolderClass.getInstance().setMaskFilter(embossFilter);
        return embossFilter;
    }

    private void clickToolBar() {
        imgDelete.setOnClickListener(view -> onBackPressed());

        //Hạn chế hai view đè lên nhau.
        imgChecked.setOnClickListener(view -> {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    tv_input.setVisibility(View.VISIBLE);
                }
            }, 1000);
            if (EditTextScreenActivity.this.et_input.getText().toString().isEmpty()) {
                Toast.makeText(EditTextScreenActivity.this, getResources().getString(R.string.You_should_add_text_first), Toast.LENGTH_SHORT).show();
                return;
            }
            DataHolderClass.getInstance().setTvShader(EditTextScreenActivity.this.tv_input.getPaint().getShader());
            DataHolderClass.getInstance().setText(EditTextScreenActivity.this.tv_input.getText().toString().trim());
//            if (SpHelper.getPosition(EditTextScreenActivity.this, SpHelper.SELECTED_ITEM_FONT, -1) == -1){
//                DataHolderClass.getInstance().setFont(ResourcesCompat.getFont(EditTextScreenActivity.this, R.font.urbanist_600));
//            }
            if (EditTextScreenActivity.this.strApply == "Shadow") {
                holdShadow(true);
            } else {
                holdShadow(false);
            }
            SpHelper.setPosition(EditTextScreenActivity.this, SpHelper.SELECTED_FONT, posTabFont);
            SpHelper.setPosition(EditTextScreenActivity.this, SpHelper.SELECTED_ITEM_FONT, posItemFont);
            finish();
            overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom);
        });
    }

    protected void onStart() {
        super.onStart();
        if (DataHolderClass.getInstance().isEditTextStickerView()) {
            this.et_input.setText(DataHolderClass.getInstance().getText());
            this.tv_input.setText(DataHolderClass.getInstance().getText());
        }
    }

    protected void onResume() {
        if (DataHolderClass.getInstance().isRewarded()) {
            Toast.makeText(this, getResources().getString(R.string.thanks_for_watching), Toast.LENGTH_SHORT).show();
            this.mFontsAdapter.updateList();
            DataHolderClass.getInstance().setRewarded(false);
        }
        super.onResume();
    }

    public void onBackPressed() {
        super.onBackPressed();
        popupWindow.dismiss();
        popupWindow1.dismiss();
        if (!DataHolderClass.getInstance().isEditTextStickerView()) {
            DataHolderClass.getInstance().setText(null);
        }
        finish();
        overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom);
    }

    private void selectedTabBottomBar(int i) {
        if (i == 1) {
            mcvFonts.setCardBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.xanh_dam)));
            imgFonts.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.neutral_trang)));
            mcvColors.setCardBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.neutral_trang)));
            imgColors.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.xanh_dam)));
            mcvGradients.setCardBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.neutral_trang)));
            imgGradients.setImageResource(R.drawable.ic_gradien_bottom);
            mcvPatterns.setCardBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.neutral_trang)));
            imgPatterns.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.xanh_dam)));
            mcvShadow.setCardBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.neutral_trang)));
            imgShadow.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.xanh_dam)));
        } else if (i == 2) {
            mcvFonts.setCardBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.neutral_trang)));
            mcvColors.setCardBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.xanh_dam)));
            mcvGradients.setCardBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.neutral_trang)));
            mcvPatterns.setCardBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.neutral_trang)));
            mcvShadow.setCardBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.neutral_trang)));
            imgFonts.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.xanh_dam)));
            imgColors.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.neutral_trang)));
            imgGradients.setImageResource(R.drawable.ic_gradien_bottom);
            imgPatterns.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.xanh_dam)));
            imgShadow.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.xanh_dam)));
        } else if (i == 3) {
            mcvFonts.setCardBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.neutral_trang)));
            mcvColors.setCardBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.neutral_trang)));
            mcvGradients.setCardBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.xanh_dam)));
            mcvPatterns.setCardBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.neutral_trang)));
            mcvShadow.setCardBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.neutral_trang)));
            imgFonts.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.xanh_dam)));
            imgColors.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.xanh_dam)));
            imgGradients.setImageResource(R.drawable.ic_grdient_text_checked);
            imgPatterns.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.xanh_dam)));
            imgShadow.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.xanh_dam)));
        } else if (i == 4) {
            mcvFonts.setCardBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.neutral_trang)));
            mcvColors.setCardBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.neutral_trang)));
            mcvGradients.setCardBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.neutral_trang)));
            mcvPatterns.setCardBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.xanh_dam)));
            mcvShadow.setCardBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.neutral_trang)));
            imgFonts.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.xanh_dam)));
            imgColors.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.xanh_dam)));
            imgGradients.setImageResource(R.drawable.ic_gradien_bottom);
            imgPatterns.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.neutral_trang)));
            imgShadow.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.xanh_dam)));
        } else if (i == 5) {
            mcvFonts.setCardBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.neutral_trang)));
            mcvColors.setCardBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.neutral_trang)));
            mcvGradients.setCardBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.neutral_trang)));
            mcvPatterns.setCardBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.neutral_trang)));
            mcvShadow.setCardBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.xanh_dam)));
            imgFonts.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.xanh_dam)));
            imgColors.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.xanh_dam)));
            imgGradients.setImageResource(R.drawable.ic_gradien_bottom);
            imgPatterns.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.xanh_dam)));
            imgShadow.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.neutral_trang)));
        }
    }

    @Override
    public void onClick(View view) {
        String[] menuTitles = new String[0];
        switch (view.getId()) {
            case R.id.mcvFonts:
                popupWindow.dismiss();
                popupWindow1.dismiss();
                clColorPicker.setVisibility(View.GONE);
                llSeekbarBlur.setVisibility(View.GONE);
                llSeekbarDistance.setVisibility(View.GONE);
                cvColorPicker.setVisibility(View.GONE);
                gvFonts.setVisibility(View.VISIBLE);
                findViewById(R.id.ll_fonts).setVisibility(View.VISIBLE);
                selectedTabBottomBar(1);
                menuTitles = getResources().getStringArray(R.array.Fonts_tabmenu);
                this.selectedTab = menuTitles[0];
                if (this.selectedTab.equals("عربي") && this.et_input.getHint().toString().equals("Sample")) {
                    this.et_input.setHint("نموذج");
                }
                String[] finalMenuTitles = menuTitles;
                tabFontAdapter = new TabFontAdapter(menuTitles, this, true,
                        SpHelper.getPosition(EditTextScreenActivity.this, SpHelper.SELECTED_FONT, -1),
                        new TabFontAdapter.OnClickItem() {
                            @Override
                            public void onClickItem(int pos) {
                                SpHelper.setPosition(EditTextScreenActivity.this, SpHelper.SELECTED_FONT, pos);
                                setFonts(finalMenuTitles[pos]);
                            }
                        });
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
                rvTabFont.setLayoutManager(linearLayoutManager);
                rvTabFont.setAdapter(tabFontAdapter);
                this.mAppController.updateVisibilite(this.mainViews, findViewById(R.id.ll_fonts));
                break;
            case R.id.mcvColors:
                popupWindow.dismiss();
                popupWindow1.dismiss();
                selectedTabBottomBar(2);
                clColorPicker.setVisibility(View.GONE);
                llSeekbarBlur.setVisibility(View.GONE);
                llSeekbarDistance.setVisibility(View.GONE);
                gvColors.setVisibility(View.GONE);
                gvGradients.setVisibility(View.GONE);
                findViewById(R.id.ll_gradients).setVisibility(View.GONE);
                gvFonts.setVisibility(View.GONE);
                findViewById(R.id.ll_fonts).setVisibility(View.GONE);
                gvPatterns.setVisibility(View.GONE);
                findViewById(R.id.ll_patterns).setVisibility(View.GONE);
                gvShadow.setVisibility(View.GONE);
                findViewById(R.id.ll_shadow).setVisibility(View.GONE);
                cvColorPicker.setVisibility(View.VISIBLE);
                String cl = SpHelper.getValueFromSharedprefrence(EditTextScreenActivity.this, SpHelper.COLOR_TEXT, "#FFFFFFFF");
                colorPicker.setInitialColor(Color.parseColor(SpHelper.getValueFromSharedprefrence(EditTextScreenActivity.this, SpHelper.COLOR_TEXT, "#FFFFFFFF")), true);
                lightnessSlider.setColorPicker(colorPicker);
                alphaSlider.setColorPicker(colorPicker);
                if (SpHelper.getBoolean(EditTextScreenActivity.this, SpHelper.IS_COLOR_TEXT, false)) {
                    et_input.setTextColor(Color.parseColor(cl));
                    tv_input.setTextColor(Color.parseColor(cl));
                } else {
                    et_input.setTextColor(Color.parseColor("#222B36"));
                    tv_input.setTextColor(Color.parseColor("#222B36"));
                }
                lightnessSlider.setColor(Color.parseColor(cl));
                alphaSlider.setColor(Color.parseColor(cl));
                colorPicker.addOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {
                        SpHelper.setString(EditTextScreenActivity.this, SpHelper.TYPE_GRADIENT_TEXT, "");
                        SpHelper.setPosition(EditTextScreenActivity.this, SpHelper.ITEM_GRADIENT_TEXT, -1);
                        SpHelper.setPosition(EditTextScreenActivity.this, SpHelper.ITEM_PATTERN, -1);
                        SpHelper.putValueInSharedpreference(EditTextScreenActivity.this, SpHelper.COLOR_TEXT, colorChange);
                        SpHelper.setBoolean(EditTextScreenActivity.this, SpHelper.IS_COLOR_TEXT, true);
                        lightnessSlider.setColor(selectedColor);
                        alphaSlider.setColor(selectedColor);
                        strApply = "Color";
                        hideSoftInput();
                        tv_input.getPaint().setShader(null);
                        et_input.getPaint().setShader(null);
                        String colorString = String.format("#%08X", tv_input.getCurrentTextColor());
                        tv_input.setTextColor(Color.parseColor(colorString));
                        et_input.setTextColor(Color.parseColor(colorString));
                        et_input.setHintTextColor(Color.parseColor(colorString));
                        DataHolderClass.getInstance().setColor(colorString);
                        tv_input.setText(et_input.getText().toString());
                    }
                });
                colorPicker.addOnColorChangedListener(new OnColorChangedListener() {
                    @Override
                    public void onColorChanged(int selectedColor) {
                        SpHelper.setString(EditTextScreenActivity.this, SpHelper.TYPE_GRADIENT_TEXT, "");
                        SpHelper.setPosition(EditTextScreenActivity.this, SpHelper.ITEM_GRADIENT_TEXT, -1);
                        SpHelper.setPosition(EditTextScreenActivity.this, SpHelper.ITEM_PATTERN, -1);
                        SpHelper.putValueInSharedpreference(EditTextScreenActivity.this, SpHelper.COLOR_TEXT, colorChange);
                        SpHelper.setBoolean(EditTextScreenActivity.this, SpHelper.IS_COLOR_TEXT, true);
                        lightnessSlider.setColor(selectedColor);
                        alphaSlider.setColor(selectedColor);
                        strApply = "Color";
                        hideSoftInput();
                        tv_input.getPaint().setShader(null);
                        et_input.getPaint().setShader(null);
                        String colorString = String.format("#%08X", selectedColor);
                        colorChange = colorString;
                        tv_input.setText(et_input.getText().toString());
                        DataHolderClass.getInstance().setColor(colorString);
                        tv_input.setTextColor(Color.parseColor(colorString));
                        et_input.setTextColor(Color.parseColor(colorString));
                        et_input.setHintTextColor(Color.parseColor(colorString));
                        tv_input.setText(et_input.getText().toString());
                    }
                });
                break;
            case R.id.mcvGradients:
                popupWindow.dismiss();
                clColorPicker.setVisibility(View.GONE);
                llSeekbarBlur.setVisibility(View.GONE);
                llSeekbarDistance.setVisibility(View.GONE);
                cvColorPicker.setVisibility(View.GONE);
                gvGradients.setVisibility(View.VISIBLE);
                findViewById(R.id.ll_gradients).setVisibility(View.VISIBLE);
                selectedTabBottomBar(3);
                setGradients();
                this.mAppController.updateVisibilite(this.mainViews, findViewById(R.id.ll_gradients));
                break;
            case R.id.mcvPatterns:
                try {
                    this.patterns = getAssets().list("Patterns");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int pos = SpHelper.getPosition(EditTextScreenActivity.this, SpHelper.ITEM_PATTERN, -1);
                if (pos != -1) {
                    strApply = "Pattern";
                    hideSoftInput();
                    imgPatternPath = "Patterns/" + patterns[pos];
                    applyPattern();
                }
                popupWindow1.dismiss();
                clColorPicker.setVisibility(View.GONE);
                llSeekbarBlur.setVisibility(View.GONE);
                llSeekbarDistance.setVisibility(View.GONE);
                gvPatterns.setVisibility(View.VISIBLE);
                findViewById(R.id.ll_patterns).setVisibility(View.VISIBLE);
                cvColorPicker.setVisibility(View.GONE);
                selectedTabBottomBar(4);
                setPatterns();
                this.mAppController.updateVisibilite(this.mainViews, findViewById(R.id.ll_patterns));
                break;
            case R.id.mcvShadow:
                popupWindow.dismiss();
                popupWindow1.dismiss();
                clColorPicker.setVisibility(View.VISIBLE);
                llSeekbarBlur.setVisibility(View.VISIBLE);
                llSeekbarDistance.setVisibility(View.VISIBLE);
                cvColorPicker.setVisibility(View.GONE);
                gvColors.setVisibility(View.GONE);
                gvGradients.setVisibility(View.GONE);
                findViewById(R.id.ll_gradients).setVisibility(View.GONE);
                gvFonts.setVisibility(View.GONE);
                findViewById(R.id.ll_fonts).setVisibility(View.GONE);
                gvPatterns.setVisibility(View.GONE);
                findViewById(R.id.ll_patterns).setVisibility(View.GONE);
                gvShadow.setVisibility(View.GONE);
                findViewById(R.id.ll_shadow).setVisibility(View.GONE);
                selectedTabBottomBar(5);
                this.sbBlur.setProgress((this.shadowXY * 100) / 16);
                this.sbDistance.setProgress((this.shadowRadius * 100) / 20);
                setShadow();
                break;
        }
    }
}
