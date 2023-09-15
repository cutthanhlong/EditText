package com.nameart.maker.stylishfont.art.textonphoto.Views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.view.MotionEventCompat;

import com.nameart.maker.stylishfont.art.textonphoto.R;

public class StickerView extends AppCompatImageView {
    private static final String TAG = "StickerView";

    private Bitmap deleteBitmap;
    private Bitmap flipVBitmap;
    private Bitmap topBitmap;
    private Bitmap resizeBitmap;
    private Bitmap mBitmap;
    private Rect dst_delete;
    private Rect dst_resize;
    private Rect dst_flipV;
    private Rect dst_top;
    private int deleteBitmapWidth;
    private int deleteBitmapHeight;
    private int resizeBitmapWidth;
    private int resizeBitmapHeight;

    private int flipVBitmapWidth;
    private int flipVBitmapHeight;

    private int topBitmapWidth;
    private int topBitmapHeight;
    private Paint localPaint;
    private int mScreenwidth;
    private int mScreenheight;
    private static final float BITMAP_SCALE = 0.7f;
    private PointF mid = new PointF();
    private OperationListener operationListener;
    private float lastRotateDegree;


    private boolean isPointerDown = false;

    private final float pointerLimitDis = 20f;
    private final float pointerZoomCoeff = 0.09f;
    private float lastLength;
    private boolean isInResize = false;

    private Matrix matrix = new Matrix();
    private boolean isInSide;

    private float lastX, lastY;
    private boolean isInEdit = true;

    private float MIN_SCALE = 0.5f;

    private float MAX_SCALE = 1.2f;

    private double halfDiagonalLength;

    private float oringinWidth = 0;
    private float oldDis;

    private final long stickerId;

    private DisplayMetrics dm;
    private boolean isHorizonMirror = false;

    public StickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        stickerId = 0;
        init();
    }

    public StickerView(Context context) {
        super(context);
        stickerId = 0;
        init();
    }

    public StickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        stickerId = 0;
        init();
    }

    private void init() {

        dst_delete = new Rect();
        dst_resize = new Rect();
        dst_flipV = new Rect();
        dst_top = new Rect();
        localPaint = new Paint();
        localPaint.setColor(getResources().getColor(R.color.black));
        localPaint.setAntiAlias(true);
        localPaint.setDither(true);
        localPaint.setStyle(Paint.Style.STROKE);
        localPaint.setStrokeWidth(3.0f);
        dm = getResources().getDisplayMetrics();
        mScreenwidth = dm.widthPixels;
        mScreenheight = dm.heightPixels;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mBitmap != null) {
            float[] arrayOfFloat = new float[9];
            matrix.getValues(arrayOfFloat);
            float f1 = 0.0F * arrayOfFloat[0] + 0.0F * arrayOfFloat[1] + arrayOfFloat[2];
            float f2 = 0.0F * arrayOfFloat[3] + 0.0F * arrayOfFloat[4] + arrayOfFloat[5];
            float f3 = arrayOfFloat[0] * this.mBitmap.getWidth() + 0.0F * arrayOfFloat[1] + arrayOfFloat[2];
            float f4 = arrayOfFloat[3] * this.mBitmap.getWidth() + 0.0F * arrayOfFloat[4] + arrayOfFloat[5];
            float f5 = 0.0F * arrayOfFloat[0] + arrayOfFloat[1] * this.mBitmap.getHeight() + arrayOfFloat[2];
            float f6 = 0.0F * arrayOfFloat[3] + arrayOfFloat[4] * this.mBitmap.getHeight() + arrayOfFloat[5];
            float f7 = arrayOfFloat[0] * this.mBitmap.getWidth() + arrayOfFloat[1] * this.mBitmap.getHeight() + arrayOfFloat[2];
            float f8 = arrayOfFloat[3] * this.mBitmap.getWidth() + arrayOfFloat[4] * this.mBitmap.getHeight() + arrayOfFloat[5];

            canvas.save();
            canvas.drawBitmap(mBitmap, matrix, null);
            //删除在右上角
            this.dst_delete.left = (int) (((double) f1) - (((double) this.deleteBitmapWidth) / 4d));
            this.dst_delete.right = (int) (((double) f1) + (((double) this.deleteBitmapWidth) / 4d));
            this.dst_delete.top = (int) (((double) f2) - (((double) this.deleteBitmapHeight) / 4d));
            this.dst_delete.bottom = (int) (((double) f2) + (((double) this.deleteBitmapHeight) / 4d));

            this.dst_resize.left = (int) (((double) f7) - (((double) this.resizeBitmapWidth) / 4d));
            this.dst_resize.right = (int) (((double) f7) + (((double) this.resizeBitmapWidth) / 4d));
            this.dst_resize.top = (int) (((double) f8) - (((double) this.resizeBitmapHeight) / 4d));
            this.dst_resize.bottom = (int) (((double) f8) + (((double) this.resizeBitmapHeight) / 4d));

            this.dst_top.left = (int) (((double) f5) - (((double) this.flipVBitmapWidth) / 10d));
            this.dst_top.right = (int) (((double) f5) + (((double) this.flipVBitmapWidth) / 10d));
            this.dst_top.top = (int) (((double) f6) - (((double) this.flipVBitmapHeight) / 10d));
            this.dst_top.bottom = (int) (((double) f6) + (((double) this.flipVBitmapHeight) / 10d));

            this.dst_flipV.left = (int) (((double) f3) - (((double) this.topBitmapWidth) / 5d));
            this.dst_flipV.right = (int) (((double) f3) + (((double) this.topBitmapWidth) / 5d));
            this.dst_flipV.top = (int) (((double) f4) - (((double) this.topBitmapHeight) / 5d));
            this.dst_flipV.bottom = (int) (((double) f4) + (((double) this.topBitmapHeight) / 5d));

            if (isInEdit) {

                canvas.drawLine(f1, f2, f3, f4, localPaint);
                canvas.drawLine(f3, f4, f7, f8, localPaint);
                canvas.drawLine(f5, f6, f7, f8, localPaint);
                canvas.drawLine(f5, f6, f1, f2, localPaint);

                canvas.drawBitmap(deleteBitmap, null, dst_delete, null);
                canvas.drawBitmap(resizeBitmap, null, dst_resize, null);
                canvas.drawBitmap(flipVBitmap, null, dst_flipV, null);
//                canvas.drawBitmap(topBitmap, null, dst_top, null);
            }

            canvas.restore();
        }
    }

    @Override
    public void setImageResource(int resId) {
        setBitmap(BitmapFactory.decodeResource(getResources(), resId));
    }

    public void setBitmap(Bitmap bitmap) {
        this.matrix.reset();
        this.mBitmap = bitmap;
        setDiagonalLength();
        initBitmaps();
        int w = this.mBitmap.getWidth();
        int h = this.mBitmap.getHeight();
        this.oringinWidth = (float) w;
        this.matrix.postScale(1.2f, 1.2f, (float) (w / 2), (float) (h / 2));
        this.matrix.postTranslate((float) ((this.mScreenwidth / 2) - (w / 2)), (float) ((this.mScreenwidth / 2) - (h / 2)));
        invalidate();
    }


    private void setDiagonalLength() {
        halfDiagonalLength = Math.hypot(mBitmap.getWidth(), mBitmap.getHeight()) / 2;
    }

    private void initBitmaps() {
        if (mBitmap.getWidth() >= mBitmap.getHeight()) {
            float minWidth = mScreenwidth / 8;
            if (mBitmap.getWidth() < minWidth) {
                MIN_SCALE = 1f;
            } else {
                MIN_SCALE = 1.0f * minWidth / mBitmap.getWidth();
            }

            if (mBitmap.getWidth() > mScreenwidth) {
                MAX_SCALE = 1;
            } else {
                MAX_SCALE = 1.0f * mScreenwidth / mBitmap.getWidth();
            }
        } else {
            float minHeight = mScreenwidth / 8;
            if (mBitmap.getHeight() < minHeight) {
                MIN_SCALE = 1f;
            } else {
                MIN_SCALE = 1.0f * minHeight / mBitmap.getHeight();
            }

            if (mBitmap.getHeight() > mScreenwidth) {
                MAX_SCALE = 1;
            } else {
                MAX_SCALE = 1.0f * mScreenwidth / mBitmap.getHeight();
            }
        }

        topBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_flip_sticker);
        deleteBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_delete_sticker);
        flipVBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_flip_sticker);
        resizeBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_resize_sticker);

        deleteBitmapWidth = (int) (deleteBitmap.getWidth() * BITMAP_SCALE);
        deleteBitmapHeight = (int) (deleteBitmap.getHeight() * BITMAP_SCALE);

        resizeBitmapWidth = (int) (resizeBitmap.getWidth() * BITMAP_SCALE);
        resizeBitmapHeight = (int) (resizeBitmap.getHeight() * BITMAP_SCALE);

        flipVBitmapWidth = (int) (flipVBitmap.getWidth() * BITMAP_SCALE);
        flipVBitmapHeight = (int) (flipVBitmap.getHeight() * BITMAP_SCALE);

        topBitmapWidth = (int) (topBitmap.getWidth() * BITMAP_SCALE);
        topBitmapHeight = (int) (topBitmap.getHeight() * BITMAP_SCALE);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = MotionEventCompat.getActionMasked(event);
        boolean handled = true;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (isInButton(event, dst_delete)) {
                    if (operationListener != null) {
                        operationListener.onDeleteClick();
                    }
                } else if (isInResize(event)) {
                    isInResize = true;
                    lastRotateDegree = rotationToStartPoint(event);
                    midPointToStartPoint(event);
                    lastLength = diagonalLength(event);
                } else if (isInButton(event, dst_flipV)) {
                    PointF localPointF = new PointF();
                    midDiagonalPoint(localPointF);
                    matrix.postScale(-1.0F, 1.0F, localPointF.x, localPointF.y);
                    isHorizonMirror = !isHorizonMirror;
                    invalidate();
                } else if (isInButton(event, dst_top)) {
                    bringToFront();
                   /* if (operationListener != null) {
                        operationListener.onTop(this);
                    }*/
                } else if (isInBitmap(event)) {
                    isInSide = true;
                    lastX = event.getX(0);
                    lastY = event.getY(0);
                } else {
                    handled = false;
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                if (spacing(event) > pointerLimitDis) {
                    oldDis = spacing(event);
                    isPointerDown = true;
                    midPointToStartPoint(event);
                } else {
                    isPointerDown = false;
                }
                isInSide = false;
                isInResize = false;
                break;
            case MotionEvent.ACTION_MOVE:
                if (isPointerDown) {
                    float scale;
                    float disNew = spacing(event);
                    if (disNew == 0 || disNew < pointerLimitDis) {
                        scale = 1;
                    } else {
                        scale = disNew / oldDis;
                        scale = (scale - 1) * pointerZoomCoeff + 1;
                    }
                    float scaleTemp = (scale * Math.abs(dst_flipV.left - dst_resize.left)) / oringinWidth;
                    if (((scaleTemp <= MIN_SCALE)) && scale < 1 ||
                            (scaleTemp >= MAX_SCALE) && scale > 1) {
                        scale = 1;
                    } else {
                        lastLength = diagonalLength(event);
                    }
                    matrix.postScale(scale, scale, mid.x, mid.y);
                    invalidate();
                } else if (isInResize) {

                    matrix.postRotate((rotationToStartPoint(event) - lastRotateDegree) * 2, mid.x, mid.y);
                    lastRotateDegree = rotationToStartPoint(event);

                    float scale = diagonalLength(event) / lastLength;

                    if (((diagonalLength(event) / halfDiagonalLength <= MIN_SCALE)) && scale < 1 ||
                            (diagonalLength(event) / halfDiagonalLength >= MAX_SCALE) && scale > 1) {
                        scale = 1;
                        if (!isInResize(event)) {
                            isInResize = false;
                        }
                    } else {
                        lastLength = diagonalLength(event);
                    }
                    matrix.postScale(scale, scale, mid.x, mid.y);

                    invalidate();
                } else if (isInSide) {
                    float x = event.getX(0);
                    float y = event.getY(0);
                    //TODO 移动区域判断 不能超出屏幕
                    matrix.postTranslate(x - lastX, y - lastY);
                    lastX = x;
                    lastY = y;
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                isInResize = false;
                isInSide = false;
                isPointerDown = false;
                break;

        }
        if (handled && operationListener != null) {
            operationListener.onEdit(this);
        }
        return handled;
    }

    private boolean isInBitmap(MotionEvent event) {
        float[] arrayOfFloat1 = new float[9];
        this.matrix.getValues(arrayOfFloat1);

        float f1 = 0.0F * arrayOfFloat1[0] + 0.0F * arrayOfFloat1[1] + arrayOfFloat1[2];
        float f2 = 0.0F * arrayOfFloat1[3] + 0.0F * arrayOfFloat1[4] + arrayOfFloat1[5];

        float f3, f4, f5, f6, f7, f8;
        f3 = f4 = f5 = f6 = f7 = f8 = 0;
        if (mBitmap != null && mBitmap.getWidth() != 0 && mBitmap.getHeight() != 0) {
            f3 = arrayOfFloat1[0] * this.mBitmap.getWidth() + 0.0F * arrayOfFloat1[1] + arrayOfFloat1[2];
            f4 = arrayOfFloat1[3] * this.mBitmap.getWidth() + 0.0F * arrayOfFloat1[4] + arrayOfFloat1[5];

            f5 = 0.0F * arrayOfFloat1[0] + arrayOfFloat1[1] * this.mBitmap.getHeight() + arrayOfFloat1[2];
            f6 = 0.0F * arrayOfFloat1[3] + arrayOfFloat1[4] * this.mBitmap.getHeight() + arrayOfFloat1[5];

            f7 = arrayOfFloat1[0] * this.mBitmap.getWidth() + arrayOfFloat1[1] * this.mBitmap.getHeight() + arrayOfFloat1[2];
            f8 = arrayOfFloat1[3] * this.mBitmap.getWidth() + arrayOfFloat1[4] * this.mBitmap.getHeight() + arrayOfFloat1[5];
        }
        float[] arrayOfFloat2 = new float[4];
        float[] arrayOfFloat3 = new float[4];

        arrayOfFloat2[0] = f1;
        arrayOfFloat2[1] = f3;
        arrayOfFloat2[2] = f7;
        arrayOfFloat2[3] = f5;

        arrayOfFloat3[0] = f2;
        arrayOfFloat3[1] = f4;
        arrayOfFloat3[2] = f8;
        arrayOfFloat3[3] = f6;
        return pointInRect(arrayOfFloat2, arrayOfFloat3, event.getX(0), event.getY(0));
    }

    private boolean pointInRect(float[] xRange, float[] yRange, float x, float y) {

        double a1 = Math.hypot(xRange[0] - xRange[1], yRange[0] - yRange[1]);
        double a2 = Math.hypot(xRange[1] - xRange[2], yRange[1] - yRange[2]);
        double a3 = Math.hypot(xRange[3] - xRange[2], yRange[3] - yRange[2]);
        double a4 = Math.hypot(xRange[0] - xRange[3], yRange[0] - yRange[3]);

        double b1 = Math.hypot(x - xRange[0], y - yRange[0]);
        double b2 = Math.hypot(x - xRange[1], y - yRange[1]);
        double b3 = Math.hypot(x - xRange[2], y - yRange[2]);
        double b4 = Math.hypot(x - xRange[3], y - yRange[3]);

        double u1 = (a1 + b1 + b2) / 2;
        double u2 = (a2 + b2 + b3) / 2;
        double u3 = (a3 + b3 + b4) / 2;
        double u4 = (a4 + b4 + b1) / 2;

        double s = a1 * a2;

        double ss = Math.sqrt(u1 * (u1 - a1) * (u1 - b1) * (u1 - b2))
                + Math.sqrt(u2 * (u2 - a2) * (u2 - b2) * (u2 - b3))
                + Math.sqrt(u3 * (u3 - a3) * (u3 - b3) * (u3 - b4))
                + Math.sqrt(u4 * (u4 - a4) * (u4 - b4) * (u4 - b1));
        return Math.abs(s - ss) < 0.5;
    }


    private boolean isInButton(MotionEvent event, Rect rect) {
        int left = rect.left;
        int right = rect.right;
        int top = rect.top;
        int bottom = rect.bottom;
        return event.getX(0) >= left && event.getX(0) <= right && event.getY(0) >= top && event.getY(0) <= bottom;
    }

    private boolean isInResize(MotionEvent event) {
        int left = -20 + this.dst_resize.left;
        int top = -20 + this.dst_resize.top;
        int right = 20 + this.dst_resize.right;
        int bottom = 20 + this.dst_resize.bottom;
        return event.getX(0) >= left && event.getX(0) <= right && event.getY(0) >= top && event.getY(0) <= bottom;
    }

    private void midPointToStartPoint(MotionEvent event) {
        float[] arrayOfFloat = new float[9];
        matrix.getValues(arrayOfFloat);
        float f1 = 0.0f * arrayOfFloat[0] + 0.0f * arrayOfFloat[1] + arrayOfFloat[2];
        float f2 = 0.0f * arrayOfFloat[3] + 0.0f * arrayOfFloat[4] + arrayOfFloat[5];
        float f3 = f1 + event.getX(0);
        float f4 = f2 + event.getY(0);
        mid.set(f3 / 2, f4 / 2);
    }

    private void midDiagonalPoint(PointF paramPointF) {
        float[] arrayOfFloat = new float[9];
        this.matrix.getValues(arrayOfFloat);
        float f1 = 0.0F * arrayOfFloat[0] + 0.0F * arrayOfFloat[1] + arrayOfFloat[2];
        float f2 = 0.0F * arrayOfFloat[3] + 0.0F * arrayOfFloat[4] + arrayOfFloat[5];
        float f3 = arrayOfFloat[0] * this.mBitmap.getWidth() + arrayOfFloat[1] * this.mBitmap.getHeight() + arrayOfFloat[2];
        float f4 = arrayOfFloat[3] * this.mBitmap.getWidth() + arrayOfFloat[4] * this.mBitmap.getHeight() + arrayOfFloat[5];
        float f5 = f1 + f3;
        float f6 = f2 + f4;
        paramPointF.set(f5 / 2.0F, f6 / 2.0F);
    }

    private float rotationToStartPoint(MotionEvent event) {

        float[] arrayOfFloat = new float[9];
        matrix.getValues(arrayOfFloat);
        float x = 0.0f * arrayOfFloat[0] + 0.0f * arrayOfFloat[1] + arrayOfFloat[2];
        float y = 0.0f * arrayOfFloat[3] + 0.0f * arrayOfFloat[4] + arrayOfFloat[5];
        double arc = Math.atan2(event.getY(0) - y, event.getX(0) - x);
        return (float) Math.toDegrees(arc);
    }

    private float diagonalLength(MotionEvent event) {
        float diagonalLength = (float) Math.hypot(event.getX(0) - mid.x, event.getY(0) - mid.y);
        return diagonalLength;
    }

    private float spacing(MotionEvent event) {
        if (event.getPointerCount() == 2) {
            float x = event.getX(0) - event.getX(1);
            float y = event.getY(0) - event.getY(1);
            return (float) Math.sqrt(x * x + y * y);
        } else {
            return 0;
        }
    }

    public interface OperationListener {
        void onDeleteClick();

        void onEdit(StickerView stickerView);

        void onTop(StickerView stickerView);
    }

    public void setOperationListener(OperationListener operationListener) {
        this.operationListener = operationListener;
    }

    public void setInEdit(boolean isInEdit) {
        this.isInEdit = isInEdit;
        invalidate();
    }
}
