/*
        Simple Snake Android Application
        Copyright (C) 2020  Anton "PoorSkill" Kesy

        This program is free software: you can redistribute it and/or modify
        it under the terms of the GNU General Public License as published by
        the Free Software Foundation, either version 3 of the License, or
        (at your option) any later version.

        This program is distributed in the hope that it will be useful,
        but WITHOUT ANY WARRANTY; without even the implied warranty of
        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
        GNU General Public License for more details.

        You should have received a copy of the GNU General Public License
        along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.asif.snake.game.util;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.asif.snake.game.helper.CustomColor;

public final class Painter {


    public static void drawCircle(int centerX, int centerY, int radius, CustomColor color, Canvas canvas, Paint paint) {
        paint.setColor(Color.argb(color.getAlpha(), color.getRed(), color.getGreen(), color.getBlue()));
        canvas.drawCircle(centerX, centerY, radius, paint);
    }

    public static void drawRect(int left, int top, int right, int bottom, CustomColor color, Canvas canvas, Paint paint) {
        paint.setColor(Color.argb(color.getAlpha(), color.getRed(), color.getGreen(), color.getBlue()));
        canvas.drawRect(left, top, right, bottom, paint);
    }


    public static void drawText(String text, int x, int y, float size, CustomColor color, Canvas canvas, Paint paint) {
        paint.setColor(Color.argb(color.getAlpha(), color.getRed(), color.getGreen(), color.getBlue()));
        paint.setTextSize(size);
        canvas.drawText(text, x, y, paint);
    }

    public static void drawColor(CustomColor color, Canvas canvas) {
        canvas.drawColor(Color.argb(color.getAlpha(), color.getRed(), color.getGreen(), color.getBlue()));
    }
}
