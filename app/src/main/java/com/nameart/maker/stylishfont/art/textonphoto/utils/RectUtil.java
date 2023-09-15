package com.nameart.maker.stylishfont.art.textonphoto.utils;

import android.graphics.RectF;

public class RectUtil {
    public static void scaleRect(RectF rect, float scale) {
        float w = rect.width();
        float h = rect.height();
        float dx = ((scale * w) - w) / 2.0f;
        float dy = ((scale * h) - h) / 2.0f;
        rect.left -= dx;
        rect.top -= dy;
        rect.right += dx;
        rect.bottom += dy;
    }

    public static void rotateRect(RectF rect, float center_x, float center_y, float roatetAngle) {
        float x = rect.centerX();
        float y = rect.centerY();
        float sinA = (float) Math.sin(Math.toRadians((double) roatetAngle));
        float cosA = (float) Math.cos(Math.toRadians((double) roatetAngle));
        rect.offset(((((x - center_x) * cosA) + center_x) - ((y - center_y) * sinA)) - x, ((((y - center_y) * cosA) + center_y) + ((x - center_x) * sinA)) - y);
    }
}
