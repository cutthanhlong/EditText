package com.nameart.maker.stylishfont.art.textonphoto.Views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MagicDrawingView extends View {
    private float TOUCH_TOLERANCE = 40.0f;
    private Bitmap bitmap;
    private ArrayList<Bitmap> bitmapArrayList = new ArrayList();
    private Canvas bitmapCanvas;
    private int brushOpacity = 255;
    private int brushSize = 120;
    private Bitmap mBitmapBrush;
    private Vector2 mBitmapBrushDimensions;
    private List<Vector2> mPositions = new ArrayList(100);
    private float mX;
    private float mY;
    private Paint paintLine = new Paint();
    private HashMap<Integer, Path> pathMap = new HashMap();
    private HashMap<Integer, Point> previousPointMap = new HashMap();
    private ArrayList<Bitmap> undoBitmapArrayList = new ArrayList();
    private List<Vector2> undoPositions = new ArrayList();

    private static final class Vector2 {
        public final float f12x;
        public final float f13y;

        public Vector2(float x, float y) {
            this.f12x = x;
            this.f13y = y;
        }
    }

    public void init(Bitmap bitmap) {
        this.mBitmapBrush = Bitmap.createScaledBitmap(bitmap, this.brushSize, this.brushSize, false);
        BitmapShader shader = new BitmapShader(this.mBitmapBrush, TileMode.MIRROR, TileMode.MIRROR);
        this.paintLine.setAlpha(this.brushOpacity);
        this.paintLine.setShader(shader);
        this.mBitmapBrushDimensions = new Vector2((float) this.mBitmapBrush.getWidth(), (float) this.mBitmapBrush.getHeight());
        this.TOUCH_TOLERANCE = (float) (((double) this.mBitmapBrush.getWidth()) / 1.5d);
    }

    @SuppressLint({"UseSparseArrays"})
    public MagicDrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
    }

    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        this.bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Config.ARGB_8888);
        this.bitmapCanvas = new Canvas(this.bitmap);
    }

    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(this.bitmap, 0.0f, 0.0f, null);
        for (int i = 0; i < this.mPositions.size(); i++) {
            canvas.drawBitmap((Bitmap) this.bitmapArrayList.get(i), ((Vector2) this.mPositions.get(i)).f12x, ((Vector2) this.mPositions.get(i)).f13y, this.paintLine);
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        int actionIndex = event.getActionIndex();
        switch (event.getAction()) {
            case 0:
                touchStarted(event.getX(actionIndex), event.getY(actionIndex), event.getPointerId(actionIndex));
                break;
            case 1:
                touchEnded(event.getPointerId(actionIndex));
                break;
            case 2:
                touchMoved(event);
                break;
            default:
                return false;
        }
        invalidate();
        return true;
    }

    private void touchStarted(float x, float y, int lineID) {
        this.pathMap.put(Integer.valueOf(lineID), new Path());
        this.previousPointMap.put(Integer.valueOf(lineID), new Point());
        Path path = new Path();
        Point point = new Point();
        path.moveTo(x, y);
        point.x = (int) x;
        point.y = (int) y;
        this.mX = x;
        this.mY = y;
        this.undoPositions.clear();
        this.undoBitmapArrayList.clear();
        this.mPositions.add(new Vector2(x - (this.mBitmapBrushDimensions.f12x / 2.0f), y - (this.mBitmapBrushDimensions.f13y / 2.0f)));
        this.bitmapArrayList.add(this.mBitmapBrush);
    }

    private void touchMoved(MotionEvent event) {
        for (int i = 0; i < event.getPointerCount(); i++) {
            float posX = event.getX();
            float posY = event.getY();
            float dx = Math.abs(posX - this.mX);
            float dy = Math.abs(posY - this.mY);
            if (dx >= this.TOUCH_TOLERANCE || dy >= this.TOUCH_TOLERANCE) {
                this.mPositions.add(new Vector2(posX - (this.mBitmapBrushDimensions.f12x / 2.0f), posY - (this.mBitmapBrushDimensions.f13y / 2.0f)));
                this.bitmapArrayList.add(this.mBitmapBrush);
                this.mX = posX;
                this.mY = posY;
            }
        }
        invalidate();
    }

    private void touchEnded(int lineID) {
        Path path = (Path) this.pathMap.get(Integer.valueOf(lineID));
        if (path != null) {
            path.reset();
        }
    }

    public void onClickUndo() {
        if (this.mPositions.size() > 0) {
            this.undoPositions.add(this.mPositions.remove(this.mPositions.size() - 1));
            invalidate();
        }
        if (this.bitmapArrayList.size() > 0) {
            this.undoBitmapArrayList.add(this.bitmapArrayList.remove(this.bitmapArrayList.size() - 1));
            invalidate();
        }
    }

    public void onClickRedo() {
        if (this.undoPositions.size() > 0) {
            this.mPositions.add(this.undoPositions.remove(this.undoPositions.size() - 1));
            invalidate();
        }
        if (this.undoBitmapArrayList.size() > 0) {
            this.bitmapArrayList.add(this.undoBitmapArrayList.remove(this.undoBitmapArrayList.size() - 1));
            invalidate();
        }
    }

    public void eraseAll() {
        this.undoPositions.clear();
        this.mPositions.clear();
        this.bitmapArrayList.clear();
        this.undoBitmapArrayList.clear();
        invalidate();
    }

    public void setBrushSize(int size) {
        this.brushSize = size + 50;
        init(this.mBitmapBrush);
    }

    public void setBrushOpacity(float opacity) {
        this.brushOpacity = (int) (55.0f + ((200.0f * opacity) / 100.0f));
        init(this.mBitmapBrush);
    }
}
