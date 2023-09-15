package com.nameart.maker.stylishfont.art.textonphoto.Views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.nameart.maker.stylishfont.art.textonphoto.R;
import com.nameart.maker.stylishfont.art.textonphoto.activities.EditTextScreenActivity;
import com.nameart.maker.stylishfont.art.textonphoto.utils.DataHolderClass;
import com.nameart.maker.stylishfont.art.textonphoto.utils.RectUtil;


public class TextStickerView extends View {
    private static final int DELETE_MODE = 5;
    private static final int EDIT_MODE = 6;
    private static final int FLIP_MODE = 7;
    private static final int IDLE_MODE = 2;
    private static final int MOVE_MODE = 3;
    private static final int ROTATE_MODE = 4;
    public final int PADDING = 5;
    public int STICKER_BTN_HALF_SIZE = 30;
    public int TEXT_SIZE_DEFAULT = 120;
    private long currentTime;
    private Paint debugPaint = new Paint();
    private float dx;
    private float dy;
    private boolean isInEdit = true;
    private boolean isInitLayout = true;
    public static boolean isShowHelpBox = true;
    public static boolean isShowHelpBox1 = true;
    private float last_x = 0.0f;
    private float last_y = 0.0f;
    public int layout_x = DataHolderClass.getInstance().getLayout_x();
    public int layout_y = DataHolderClass.getInstance().getLayout_y();
    private int mAlpha = 255;
    private Context mContext;
    private int mCurrentMode = 2;
    private Bitmap mDeleteBitmap;
    private Bitmap mFlipBitmap;
    private RectF mDeleteDstRect = new RectF();
    private RectF mFlipDstRect = new RectF();
    private Rect mDeleteRect = new Rect();
    private Rect mFlipRect = new Rect();
    private Bitmap mEditBitmap;
    private RectF mEditDstRect = new RectF();
    private Rect mEditRect = new Rect();
    private RectF mHelpBoxRect = new RectF();
    private Paint mHelpPaint = new Paint();
    private TextPaint mPaint = new TextPaint();
    public float mRotateAngle = DataHolderClass.getInstance().getmRotateAngle();
    private Bitmap mRotateBitmap;
    private RectF mRotateDstRect = new RectF();
    private Rect mRotateRect = new Rect();
    public float mScale = DataHolderClass.getInstance().getmScale();
    private String mText;
    private Rect mTextRect = new Rect();
    private OperationListener operationListener;
    float shaderScaleX = 1.0f;
    float shaderScaleY = 1.0f;
    private Typeface type;
    private boolean isHorizonMirror = false;

    public interface OperationListener {
        void onFlipClick();

        void onDeleteClick();

        void onEdit(TextStickerView textStickerView);

        void onTop(TextStickerView textStickerView);
    }

    public TextStickerView(Context context, Typeface type) {
        super(context);
        this.mContext = context;
        initPrimitiveData();
        initView(context, type);
    }

    public TextStickerView(Context context, AttributeSet attrs, Typeface type) {
        super(context, attrs);
        initView(context, type);
    }

    public TextStickerView(Context context, AttributeSet attrs, int defStyleAttr, Typeface type) {
        super(context, attrs, defStyleAttr);
        initView(context, type);
    }

    public void initPrimitiveData() {
        this.layout_x = DataHolderClass.getInstance().getLayout_x();
        this.layout_y = DataHolderClass.getInstance().getLayout_y();
        this.mRotateAngle = DataHolderClass.getInstance().getmRotateAngle();
        this.mScale = DataHolderClass.getInstance().getmScale();
    }

    private void initView(Context context, Typeface type) {
        this.debugPaint.setColor(Color.parseColor("#66ff0000"));
        this.mFlipBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_flip);
        this.mDeleteBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_delete1);
        this.mEditBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_edit);
        this.mRotateBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_resize);
        this.STICKER_BTN_HALF_SIZE = this.mDeleteBitmap.getWidth() / 7;
        this.mDeleteRect.set(0, 0, this.mDeleteBitmap.getWidth(), this.mDeleteBitmap.getHeight());
        this.mEditRect.set(0, 0, this.mEditBitmap.getWidth(), this.mEditBitmap.getHeight());
        this.mRotateRect.set(0, 0, this.mRotateBitmap.getWidth(), this.mRotateBitmap.getHeight());
        this.mFlipRect.set(0, 0, this.mFlipBitmap.getWidth(), this.mFlipBitmap.getHeight());
        this.mDeleteDstRect = new RectF(0.0f, 0.0f, (float) (this.STICKER_BTN_HALF_SIZE << 1), (float) (this.STICKER_BTN_HALF_SIZE << 1));
        this.mEditDstRect = new RectF(0.0f, 0.0f, (float) (this.STICKER_BTN_HALF_SIZE << 1), (float) (this.STICKER_BTN_HALF_SIZE << 1));
        this.mRotateDstRect = new RectF(0.0f, 0.0f, (float) (this.STICKER_BTN_HALF_SIZE << 1), (float) (this.STICKER_BTN_HALF_SIZE << 1));
        this.mFlipDstRect = new RectF(0.0f, 0.0f, (float) (this.STICKER_BTN_HALF_SIZE << 1), (float) (this.STICKER_BTN_HALF_SIZE << 1));
        this.mPaint.setColor(Color.parseColor(DataHolderClass.getInstance().getColor()));
//        this.mPaint.setColor(DataHolderClass.getInstance().getColor1());
//        this.mPaint.setColor(Color.parseColor("#71FF0000"));
        this.mPaint.setTextAlign(Align.CENTER);
        this.mPaint.setTextSize(50.0f * getContext().getResources().getDisplayMetrics().density);
        this.mPaint.setAntiAlias(true);
        Shader shader = DataHolderClass.getInstance().getTvShader();
        if (shader != null) {
            this.mPaint.setShader(shader);
        } else {
            this.mPaint.setShader(shader);
        }
        if (DataHolderClass.getInstance().getShadowColor() != 0) {
            this.mPaint.setShadowLayer((float) DataHolderClass.getInstance().getShadowRadius(), (float) DataHolderClass.getInstance().getShadowXY(), (float) DataHolderClass.getInstance().getShadowXY(), DataHolderClass.getInstance().getShadowColor());
        } else {
            this.mPaint.setMaskFilter(DataHolderClass.getInstance().getMaskFilter());
        }
//        this.mPaint.setAlpha(this.mAlpha);
        this.mHelpPaint.setColor(Color.parseColor("#4A8CF8"));
        this.mHelpPaint.setStyle(Style.STROKE);
        this.mHelpPaint.setAntiAlias(true);
        this.mHelpPaint.setStrokeWidth(2.0f);
    }

    public void setText(String text) {
        this.mText = text;
        invalidate();
    }

    public String getmText() {
        return this.mText;
    }

    public void setTextColor(int newColor) {
        this.mPaint.setColor(newColor);
        invalidate();
    }

    public Typeface getType() {
        return this.type;
    }

    public void setType(Typeface type) {
        this.type = type;
        this.mPaint.setTypeface(type);
        invalidate();
    }

    public int getmAlpha() {
        return this.mAlpha;
    }

    public void setmAlpha(int mAlpha) {
        this.mAlpha = mAlpha;
        this.mPaint.setAlpha(mAlpha);
        invalidate();
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (this.isInitLayout && !DataHolderClass.getInstance().isEditTextStickerView()) {
            this.isInitLayout = false;
        }
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!TextUtils.isEmpty(this.mText)) {
            drawContent(canvas);
        }
    }

    private void drawContent(Canvas canvas) {
        drawText(canvas);
        int offsetValue = ((int) this.mDeleteDstRect.width()) >> 1;
        this.mDeleteDstRect.offsetTo(this.mHelpBoxRect.left - ((float) offsetValue), this.mHelpBoxRect.top - ((float) offsetValue));
        this.mEditDstRect.offsetTo(this.mHelpBoxRect.left - ((float) offsetValue), this.mHelpBoxRect.bottom - ((float) offsetValue));
        this.mRotateDstRect.offsetTo(this.mHelpBoxRect.right - ((float) offsetValue), this.mHelpBoxRect.bottom - ((float) offsetValue));
        this.mFlipDstRect.offsetTo(this.mHelpBoxRect.right - ((float) offsetValue), this.mHelpBoxRect.top - ((float) offsetValue));
        RectUtil.rotateRect(this.mDeleteDstRect, this.mHelpBoxRect.centerX(), this.mHelpBoxRect.centerY(), this.mRotateAngle);
        RectUtil.rotateRect(this.mEditDstRect, this.mHelpBoxRect.centerX(), this.mHelpBoxRect.centerY(), this.mRotateAngle);
        RectUtil.rotateRect(this.mRotateDstRect, this.mHelpBoxRect.centerX(), this.mHelpBoxRect.centerY(), this.mRotateAngle);
        RectUtil.rotateRect(this.mFlipDstRect, this.mHelpBoxRect.centerX(), this.mHelpBoxRect.centerY(), this.mRotateAngle);
        if (this.isShowHelpBox && this.isInEdit) {
            canvas.save();
            canvas.rotate(this.mRotateAngle, this.mHelpBoxRect.centerX(), this.mHelpBoxRect.centerY());
            canvas.drawRoundRect(this.mHelpBoxRect, 10.0f, 10.0f, this.mHelpPaint);
            canvas.restore();
            canvas.drawBitmap(this.mDeleteBitmap, this.mDeleteRect, this.mDeleteDstRect, null);
            canvas.drawBitmap(this.mEditBitmap, this.mEditRect, this.mEditDstRect, null);
            canvas.drawBitmap(this.mRotateBitmap, this.mRotateRect, this.mRotateDstRect, null);
            canvas.drawBitmap(this.mFlipBitmap, this.mFlipRect, this.mFlipDstRect, null);
        }
    }

    private void drawText(Canvas canvas) {
        drawText(canvas, this.layout_x, this.layout_y, this.mScale, this.mRotateAngle);
    }

    public void drawText(Canvas canvas, int _x, int _y, float scale, float rotate) {
        if (!TextUtils.isEmpty(this.mText)) {
            int x = _x;
            int y = _y;
            this.mPaint.getTextBounds(this.mText, 0, this.mText.length(), this.mTextRect);
            this.mTextRect.offset(x - (this.mTextRect.width() >> 1), y);
            this.mHelpBoxRect.set((float) (this.mTextRect.left - 5), (float) (this.mTextRect.top - 5), (float) (this.mTextRect.right + 5), (float) (this.mTextRect.bottom + 5));
            RectUtil.scaleRect(this.mHelpBoxRect, scale);
            canvas.save();
            canvas.scale(scale, scale, this.mHelpBoxRect.centerX(), this.mHelpBoxRect.centerY());
            canvas.rotate(rotate, this.mHelpBoxRect.centerX(), this.mHelpBoxRect.centerY());
            canvas.drawText(this.mText, (float) x, (float) y, this.mPaint);
            canvas.restore();
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        boolean ret = super.onTouchEvent(event);
        int action = event.getAction();
        float x = event.getX();
        float y = event.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (this.mDeleteDstRect.contains(x, y)) {
                    this.isShowHelpBox = true;
                    if (this.operationListener != null) {
                        this.operationListener.onDeleteClick();
                    }
                } else if (this.mEditDstRect.contains(x, y)) {
                    this.isShowHelpBox = true;
                    this.mCurrentMode = 6;
                    if (getmText() != null) {
                        DataHolderClass.getInstance().setText(getmText());
                    }
                    DataHolderClass.getInstance().setFont(getType());
                    DataHolderClass.getInstance().setTvShader(this.mPaint.getShader());
                    DataHolderClass.getInstance().setMaskFilter(this.mPaint.getMaskFilter());
                    DataHolderClass.getInstance().setEditTextStickerView(true);
                    this.mContext.startActivity(new Intent(this.mContext, EditTextScreenActivity.class));
                    clearTextContent();
                    invalidate();
                } else if (this.mRotateDstRect.contains(x, y)) {
                    this.isShowHelpBox = true;
                    this.mCurrentMode = 4;
                    this.last_x = this.mRotateDstRect.centerX();
                    this.last_y = this.mRotateDstRect.centerY();
                    ret = true;
                } else if (this.mFlipDstRect.contains(x, y)) {
                    this.isShowHelpBox = true;
                    this.mCurrentMode = 7;
                    if (this.operationListener != null) {
                        this.operationListener.onFlipClick();
                    }
                    float flipX = Math.abs(x - last_x);
                    float flipY = Math.abs(y - last_y);
                    float originalScaleX = getScaleX();
                    float originalScaleY = getScaleY();
                    if (flipX > flipY) {
                        setScaleX(originalScaleX * -1);
                    } else {
                        setScaleX(originalScaleX);
                        setScaleY(originalScaleY * -1);
                    }
                } else if (this.mHelpBoxRect.contains(x, y)) {
                    this.isShowHelpBox = true;
//                    this.isShowHelpBox1 = true;
                    this.mCurrentMode = 3;
                    this.last_x = x;
                    this.last_y = y;
                    ret = true;
                    this.currentTime = System.currentTimeMillis();
                } else {
                    this.isShowHelpBox = false;
//                    this.isShowHelpBox1 = false;
                    invalidate();
                }
                if (this.mCurrentMode == 5) {
                    this.mCurrentMode = 2;
                    clearTextContent();
                    invalidate();
                    break;
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
            case 3:
                if (System.currentTimeMillis() - this.currentTime > 10 || (this.dx > 20.0f && this.dy > 20.0f)) {
                    ret = false;
                    this.mCurrentMode = 2;
                    DataHolderClass.getInstance().setLayout_x(this.layout_x);
                    DataHolderClass.getInstance().setLayout_y(this.layout_y);
                    DataHolderClass.getInstance().setmRotateAngle(this.mRotateAngle);
                    DataHolderClass.getInstance().setmScale(this.mScale);
                    Log.d("tv", "x = " + this.layout_x + " y = " + this.layout_y + " scale = " + this.mScale + " rotation = " + this.mRotateAngle);
                    break;
                }
                this.isShowHelpBox = false;
//                this.isShowHelpBox1 = false;
                invalidate();
                return true;

            case 2:
                ret = true;
                if (this.mCurrentMode != 3) {
                    if (this.mCurrentMode == 4) {
                        this.mCurrentMode = 4;
                        this.dx = x - this.last_x;
                        this.dy = y - this.last_y;
                        updateRotateAndScale(this.dx, this.dy);
                        invalidate();
                        this.last_x = x;
                        this.last_y = y;
                        break;
                    }
                }
                this.mCurrentMode = 3;
                this.dx = x - this.last_x;
                this.dy = y - this.last_y;
                this.layout_x = (int) (((float) this.layout_x) + this.dx);
                this.layout_y = (int) (((float) this.layout_y) + this.dy);
                invalidate();
                this.last_x = x;
                this.last_y = y;
                break;

        }
        if (ret && this.operationListener != null) {
            this.operationListener.onEdit(this);
        }
        return ret;
    }

    public void clearTextContent() {
        setText(null);
    }

    public void updateRotateAndScale(float dx, float dy) {
        float c_x = this.mHelpBoxRect.centerX();
        float c_y = this.mHelpBoxRect.centerY();
        float x = this.mRotateDstRect.centerX();
        float y = this.mRotateDstRect.centerY();
        float xa = x - c_x;
        float ya = y - c_y;
        float xb = (x + dx) - c_x;
        float yb = (y + dy) - c_y;
        float srcLen = (float) Math.sqrt((double) ((xa * xa) + (ya * ya)));
        float curLen = (float) Math.sqrt((double) ((xb * xb) + (yb * yb)));
        float scale = curLen / srcLen;
        this.mScale *= scale;
        if (this.mHelpBoxRect.width() * this.mScale < 70.0f) {
            this.mScale /= scale;
            return;
        }
        double cos = (double) (((xa * xb) + (ya * yb)) / (srcLen * curLen));
        if (cos <= 1.0d && cos >= -1.0d) {
            this.mRotateAngle += ((float) Math.toDegrees(Math.acos(cos))) * ((float) ((xa * yb) - (xb * ya) > 0.0f ? 1 : -1));
        }
    }

    public void resetView() {
        this.layout_x = getMeasuredWidth() / 2;
        this.layout_y = getMeasuredHeight() / 3;
        this.mRotateAngle = 0.0f;
        this.mScale = 1.0f;
    }

    public boolean isShowHelpBox() {
        return this.isShowHelpBox;
    }

    public void setShowHelpBox(boolean showHelpBox) {
        this.isShowHelpBox = showHelpBox;
        invalidate();
    }

    public void setOperationListener(OperationListener operationListener) {
        this.operationListener = operationListener;
    }

    public void setInEdit(boolean isInEdit) {
        this.isInEdit = isInEdit;
        invalidate();
    }
}
