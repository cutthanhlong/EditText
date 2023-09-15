package com.nameart.maker.stylishfont.art.textonphoto.Views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DrawingView extends View {
    private float TOUCH_TOLERANCE = 4.0f;
    private Map<Path, Integer> brushOpacitiesMap = new HashMap();
    private int brushOpacity = 255;
    private float brushSize = 20.0f;
    private Map<Path, Float> brushSizesMap = new HashMap();
    private Bitmap canvasBitmap;
    private Paint canvasPaint;
    private Map<Path, Integer> colorsMap = new HashMap();
    private Canvas drawCanvas;
    private Paint drawPaint;
    private Path drawPath;
    private boolean eraserMode;
    private boolean isTouchable = true;
    private float mX;
    private float mY;
    private String paintColor = "#CC0000";
    private ArrayList<Path> paths = new ArrayList();
    private Paint transparentPaint;
    private ArrayList<Path> undonePaths = new ArrayList();

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
    }

    public void init() {
        this.drawPath = new Path();
        this.drawPaint = new Paint();
        this.drawPaint.setAntiAlias(true);
        this.drawPaint.setColor(Color.parseColor(this.paintColor));
        this.drawPaint.setStrokeWidth(this.brushSize);
        this.drawPaint.setStyle(Style.STROKE);
        this.drawPaint.setStrokeJoin(Join.ROUND);
        this.drawPaint.setStrokeCap(Cap.ROUND);
        this.drawPaint.setAlpha(this.brushOpacity);
        this.canvasPaint = new Paint(4);
    }

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    protected void onDraw(Canvas canvas) {
        Iterator it = this.paths.iterator();
        while (it.hasNext()) {
            Path p = (Path) it.next();
            try {
                this.drawPaint.setColor(((Integer) this.colorsMap.get(p)).intValue());
                this.drawPaint.setStrokeWidth(((Float) this.brushSizesMap.get(p)).floatValue());
                this.drawPaint.setAlpha(((Integer) this.brushOpacitiesMap.get(p)).intValue());
            } catch (NullPointerException e) {
            }
            canvas.drawPath(p, this.drawPaint);
        }
        this.drawPaint.setColor(Color.parseColor(this.paintColor));
        this.drawPaint.setStrokeWidth(this.brushSize);
        this.drawPaint.setAlpha(this.brushOpacity);
        canvas.drawPath(this.drawPath, this.drawPaint);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.canvasBitmap = Bitmap.createBitmap(w, h, Config.ARGB_8888);
        this.drawCanvas = new Canvas(this.canvasBitmap);
    }

    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();
        switch (event.getAction()) {
            case 0:
                touch_start(touchX, touchY);
                invalidate();
                break;
            case 1:
                this.colorsMap.put(this.drawPath, Integer.valueOf(Color.parseColor(this.paintColor)));
                this.brushOpacitiesMap.put(this.drawPath, Integer.valueOf(this.brushOpacity));
                this.brushSizesMap.put(this.drawPath, Float.valueOf(this.brushSize));
                touch_up();
                invalidate();
                break;
            case 2:
                touch_move(touchX, touchY);
                invalidate();
                break;
            default:
                return false;
        }
        return super.onTouchEvent(event);
    }

    private void touch_start(float x, float y) {
        this.undonePaths.clear();
        this.drawPath.reset();
        this.drawPath.moveTo(x, y);
        this.mX = x;
        this.mY = y;
    }

    private void touch_move(float x, float y) {
        float dx = Math.abs(x - this.mX);
        float dy = Math.abs(y - this.mY);
        if (dx >= this.TOUCH_TOLERANCE || dy >= this.TOUCH_TOLERANCE) {
            this.drawPath.quadTo(this.mX, this.mY, (this.mX + x) / 2.0f, (this.mY + y) / 2.0f);
            this.mX = x;
            this.mY = y;
        }
    }

    private void touch_up() {
        this.drawPath.lineTo(this.mX, this.mY);
        this.drawCanvas.drawPath(this.drawPath, this.drawPaint);
        this.paths.add(this.drawPath);
        this.drawPath = new Path();
    }

    public void onClickUndo() {
        if (this.paths.size() > 0) {
            this.undonePaths.add(this.paths.remove(this.paths.size() - 1));
            invalidate();
        }
    }

    public void onClickRedo() {
        if (this.undonePaths.size() > 0) {
            this.paths.add(this.undonePaths.remove(this.undonePaths.size() - 1));
            invalidate();
        }
    }

    public void eraseAll() {
        this.undonePaths.clear();
        this.paths.clear();
        invalidate();
    }

    public void eraserMode() {
        this.eraserMode = true;
    }

    public void drawMode() {
        this.eraserMode = false;
    }

    public void setColor(String color) {
        this.paintColor = color;
    }

    public void setBrushSize(float size) {
        this.brushSize = size;
    }

    public void setBrushOpacity(float opacity) {
        this.brushOpacity = (int) (55.0f + ((200.0f * opacity) / 100.0f));
    }
}
