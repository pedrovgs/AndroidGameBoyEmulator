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

package com.github.pedrovgs.androidgameboyemulator.core.gpu;

import static com.github.pedrovgs.androidgameboyemulator.core.gpu.GPUMode.HORIZONTAL_BLANK;
import static com.github.pedrovgs.androidgameboyemulator.core.gpu.GPUMode.SCANLINE_OAM;
import static com.github.pedrovgs.androidgameboyemulator.core.gpu.GPUMode.SCANLINE_VRAM;
import static com.github.pedrovgs.androidgameboyemulator.core.gpu.GPUMode.VERTICAL_BLANK;

public class GPU {

  private static final int SCREEN_PIXELS_RGBA = 92160;
  private static final byte PIXEL_CHANNEL_INITIAL_VALUE = (byte) 0xFF;
  public static final int SCREEN_WIDTH = 144;

  private final byte[] screenData;

  private GPUMode currentGPUMode;
  private int currentModeClock;
  private int currentLine;

  public GPU() {
    this.screenData = new byte[SCREEN_PIXELS_RGBA];
    this.currentGPUMode = HORIZONTAL_BLANK;
    this.currentModeClock = 0;
    this.currentLine = 0;
    reset();
  }

  public void reset() {
    currentGPUMode = HORIZONTAL_BLANK;
    currentModeClock = 0;
    currentLine = 0;
    for (int i = 0; i < SCREEN_PIXELS_RGBA; i++) {
      screenData[i] = PIXEL_CHANNEL_INITIAL_VALUE;
    }
  }

  public byte getRedChannelAtPixel(int x, int y) {
    int pixelIndex = getPixelIndex(x, y);
    return screenData[pixelIndex];
  }

  public byte getGreenChannelAtPixel(int x, int y) {
    int pixelIndex = getPixelIndex(x, y);
    return screenData[pixelIndex + 1];
  }

  public byte getBlueChannelAtPixel(int x, int y) {
    int pixelIndex = getPixelIndex(x, y);
    return screenData[pixelIndex + 2];
  }

  public byte getAlphaChannelAtPixel(int x, int y) {
    int pixelIndex = getPixelIndex(x, y);
    return screenData[pixelIndex + 3];
  }

  public void tick(int cyclesElapsed) {
    this.currentModeClock += cyclesElapsed;
    switch (currentGPUMode) {
      case HORIZONTAL_BLANK:
        if (currentModeClock < HORIZONTAL_BLANK.getClocks()) {
          resetCurrentModeClock();
          currentLine++;
          if (currentLine == SCREEN_WIDTH - 1) {
            setGPUMode(VERTICAL_BLANK);
            updateLCD();
          } else {
            setGPUMode(SCANLINE_VRAM);
          }
        }
        break;
      case VERTICAL_BLANK:
        if (currentModeClock >= VERTICAL_BLANK.getClocks()) {
          resetCurrentModeClock();
          currentLine++;
          if (currentLine > 153) {
            setGPUMode(SCANLINE_VRAM);
            currentLine = 0;
          }
        }
        break;
      case SCANLINE_OAM:
        if (currentModeClock >= SCANLINE_OAM.getClocks()) {
          currentModeClock = 0;
          setGPUMode(SCANLINE_VRAM);
        }
        break;
      case SCANLINE_VRAM:
        if (currentModeClock >= SCANLINE_VRAM.getClocks()) {
          resetCurrentModeClock();
          setGPUMode(HORIZONTAL_BLANK);
          scanLine();
        }
        break;
    }
  }

  private void setGPUMode(GPUMode currentGPUMode) {
    this.currentGPUMode = currentGPUMode;
  }

  private void scanLine() {

  }

  private void updateLCD() {
    //TODO Update the LCD here.
  }

  private int getPixelIndex(int x, int y) {
    return y * 160 + x;
  }

  private void resetCurrentModeClock() {
    currentModeClock = 0;
  }
}
