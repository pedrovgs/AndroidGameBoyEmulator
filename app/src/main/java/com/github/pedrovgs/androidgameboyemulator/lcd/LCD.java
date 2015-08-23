/*
 * Copyright (C) 2015 Pedro Vicente Gomez Sanchez.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.github.pedrovgs.androidgameboyemulator.lcd;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import com.github.pedrovgs.androidgameboyemulator.core.gpu.GPU;
import com.github.pedrovgs.androidgameboyemulator.core.gpu.GPUListener;

public class LCD extends View implements GPUListener {

  private static final int LCD_HEIGHT = 144;
  private static final int LCD_WIDTH = 160;

  private GPU gpu;
  private Bitmap bitmap;
  private Paint paint;
  private Rect lcdSize;
  private Rect lcdScaledSize;

  public LCD(Context context) {
    this(context, null);
  }

  public LCD(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public LCD(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initializeLCD();
  }

  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    if (isGPUReady()) {
      for (int x = 0; x < LCD_WIDTH; x++) {
        for (int y = 0; y < LCD_HEIGHT; y++) {
          int pixelColor = getGPUPixelColor(x, y);
          bitmap.setPixel(x, y, pixelColor);
        }
      }
      canvas.save();
      canvas.drawBitmap(bitmap, lcdSize, lcdScaledSize, paint);
      canvas.restore();
    }
  }

  @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    lcdScaledSize = new Rect(0, 0, w, h);
  }

  @Override public void onGPUUpdated(GPU gpu) {
    this.gpu = gpu;
    post(new Runnable() {
      @Override public void run() {
        invalidate();
      }
    });
  }

  private void initializeLCD() {
    paint = new Paint();
    bitmap = Bitmap.createBitmap(LCD_WIDTH, LCD_HEIGHT, Bitmap.Config.ARGB_8888);
    lcdSize = new Rect(0, 0, LCD_WIDTH, LCD_HEIGHT);
    lcdScaledSize = new Rect(0, 0, LCD_WIDTH, LCD_HEIGHT);
  }

  private boolean isGPUReady() {
    return gpu != null;
  }

  private int getGPUPixelColor(int x, int y) {
    int alpha = gpu.getAlphaChannelAtPixel(x, y);
    int red = gpu.getRedChannelAtPixel(x, y);
    int green = gpu.getGreenChannelAtPixel(x, y);
    int blue = gpu.getBlueChannelAtPixel(x, y);
    int color = alpha << 24 | red << 16 | green << 8 | blue;
    return color;
  }
}
