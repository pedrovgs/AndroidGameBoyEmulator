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

import com.github.pedrovgs.androidgameboyemulator.core.mmu.MMU;
import com.github.pedrovgs.androidgameboyemulator.core.mmu.MMUListener;

import static com.github.pedrovgs.androidgameboyemulator.core.gpu.GPUMode.HORIZONTAL_BLANK;
import static com.github.pedrovgs.androidgameboyemulator.core.gpu.GPUMode.SCANLINE_OAM;
import static com.github.pedrovgs.androidgameboyemulator.core.gpu.GPUMode.SCANLINE_VRAM;
import static com.github.pedrovgs.androidgameboyemulator.core.gpu.GPUMode.VERTICAL_BLANK;

public class GPU implements MMUListener {

  private static final int SCREEN_PIXELS_RGBA = 92160;
  private static final byte PIXEL_CHANNEL_INITIAL_VALUE = (byte) 0xFF;
  private static final int SCREEN_WIDTH = 144;
  private static final int NUMBER_OF_TILES = 384;
  private static final int PIXELS_PER_TILE = 8;
  public static final int BASE_ADDRESS_MASK = 0x1FFE;

  public final MMU mmu;
  private final byte[] screenData;
  private final TileColor[][] tiles;

  private GPUMode currentGPUMode;
  private int currentModeClock;

  //TODO: Replace this registers with values in memory and getter methods.
  private int currentLine;
  private int scrollX;
  private int scrollY;
  private boolean backgroundMap;
  private int backgroundTile;

  private GPUListener listener;

  public GPU(MMU mmu) {
    this.mmu = mmu;
    this.screenData = new byte[SCREEN_PIXELS_RGBA];
    this.tiles = new TileColor[NUMBER_OF_TILES][PIXELS_PER_TILE];
    reset();
  }

  public void setListener(GPUListener listener) {
    this.listener = listener;
  }

  public void reset() {
    this.currentGPUMode = HORIZONTAL_BLANK;
    this.currentModeClock = 0;
    this.currentLine = 0;
    this.scrollX = 0;
    this.scrollY = 0;
    this.backgroundMap = true;
    this.backgroundTile = 0;
    for (int i = 0; i < SCREEN_PIXELS_RGBA; i++) {
      screenData[i] = PIXEL_CHANNEL_INITIAL_VALUE;
    }
    for (int i = 0; i < NUMBER_OF_TILES; i++) {
      for (int j = 0; j < PIXELS_PER_TILE; j++) {
        tiles[i][j] = TileColor.OFF;
      }
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
            notifyListener();
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
      default:
    }
  }

  @Override public void onVRAMUpdated(int address) {
    address &= BASE_ADDRESS_MASK;

    int tile = (address >> 4) & 511;
    int y = (address >> 1) & 7;

    int bitIndex;
    for (int x = 0; x < 8; x++) {
      bitIndex = 1 << (7 - x);
      int firstBitValue = mmu.readByte(address) & bitIndex;
      int secondBitValue = mmu.readByte(address) & bitIndex;
      int ordinalColor = secondBitValue << 1 + firstBitValue;
      tiles[tile][x] = TileColor.values()[ordinalColor];
    }
  }

  private void setGPUMode(GPUMode currentGPUMode) {
    this.currentGPUMode = currentGPUMode;
  }

  private void scanLine() {
    int mapOffset = backgroundMap ? 0x1C00 : 0x1800;
    mapOffset += ((currentLine + scrollY) & 255) >> 3;
    int lineOffset = (scrollX >> 3);
    int y = (currentLine + scrollY) & 7;
    int x = scrollX & 7;
    int canvasOffset = currentLine * 160 * 4;

    byte tile = mmu.readByte(mapOffset + lineOffset);
    if (backgroundTile == 1 && tile < 128) {
      tile += 256;
    }
    for (int i = 0; i < 160; i++) {
      TileColor tileColor = tiles[tile][y];
      screenData[canvasOffset + 0] = (byte) tileColor.getRed();
      screenData[canvasOffset + 1] = (byte) tileColor.getGreen();
      screenData[canvasOffset + 2] = (byte) tileColor.getBlue();
      screenData[canvasOffset + 3] = (byte) tileColor.getAlpha();
      canvasOffset += 4;

      x++;
      if (x == 8) {
        x = 0;
        lineOffset = (lineOffset + 1) & 31;
        tile = mmu.readByte(mapOffset + lineOffset);
        if (backgroundTile == 1 && tile < 128) {
          tile += 256;
        }
      }
    }
  }

  private void notifyListener() {
    if (listener != null) {
      listener.onGPUUpdated(this);
    }
  }

  private int getPixelIndex(int x, int y) {
    return y * 160 + x;
  }

  private void resetCurrentModeClock() {
    currentModeClock = 0;
  }
}
