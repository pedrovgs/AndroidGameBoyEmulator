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

import static com.github.pedrovgs.androidgameboyemulator.core.gpu.GPUMode.HORIZONTAL_BLANK;
import static com.github.pedrovgs.androidgameboyemulator.core.gpu.GPUMode.SCANLINE_OAM;
import static com.github.pedrovgs.androidgameboyemulator.core.gpu.GPUMode.SCANLINE_VRAM;
import static com.github.pedrovgs.androidgameboyemulator.core.gpu.GPUMode.VERTICAL_BLANK;

public class GPU {

  private static final int LCD_GPU_CONTROL_ADDRESS = 0xFF40;
  private static final int BG_MAP_BIT_INDEX = 3;
  private static final int BG_TILE_BIT_INDEX = 4;
  private static final int SCROLL_Y_ADDRESS = 0xFF42;
  private static final int SCROLL_X_ADDRESS = 0xFF43;
  private static final int CURRENT_LINE_ADDRESS = 0xFF44;
  private static final int MAP_0_ADDRESS = 0x9800;
  private static final int MAP_1_ADDRESS = 0x9C00;
  private static final int NUMBER_OF_LINES = 144;
  private static final int TILE_SIZE_IN_PX = 8;
  private static final int TILE_SET_0_ADDRESS = 0x9000;
  private static final int TILE_SET_1_ADDRESS = 0x8000;
  private static final int MAP_SIZE_IN_TILES = 32;
  private static final int VERTICAL_BLANK_PERIOD_LINES = 10;

  public final MMU mmu;

  private GPUMode currentGPUMode;
  private int currentModeClock;

  private GPUListener listener;

  public GPU(MMU mmu) {
    this.mmu = mmu;
    reset();
  }

  public void setListener(GPUListener listener) {
    this.listener = listener;
  }

  public void reset() {
    this.currentGPUMode = HORIZONTAL_BLANK;
    this.currentModeClock = 0;
  }

  public int getColorAtPixel(int x, int y) {
    TileColor tileColor = TileColor.OFF;
    if (isLCDActive()) {
      tileColor = getTileColor(x, y);
    }
    return tileColor.getRGBA();
  }

  public void tick(int cyclesElapsed) {
    if (!isLCDActive()) {
      return;
    }
    this.currentModeClock += cyclesElapsed;
    switch (currentGPUMode) {
      case HORIZONTAL_BLANK:
        if (currentModeClock >= HORIZONTAL_BLANK.getClocks()) {
          resetCurrentModeClock();
          incrementCurrentLine();
          if (getCurrentLine() == NUMBER_OF_LINES - 1) {
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
          incrementCurrentLine();
          if (getCurrentLine() > NUMBER_OF_LINES + VERTICAL_BLANK_PERIOD_LINES - 1) {
            setGPUMode(SCANLINE_OAM);
            setCurrentLine(0);
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
        }
        break;
      default:
    }
  }

  public int getMapAddress() {
    return isUsingMap1() ? MAP_1_ADDRESS : MAP_0_ADDRESS;
  }

  public int getTileSetAddress() {
    return isUsingTileSet1() ? TILE_SET_1_ADDRESS : TILE_SET_0_ADDRESS;
  }

  public int getCurrentLine() {
    return mmu.readByte(CURRENT_LINE_ADDRESS) & 0xFF;
  }

  public int getScrollX() {
    return mmu.readByte(SCROLL_X_ADDRESS) & 0xFF;
  }

  public int getScrollY() {
    return mmu.readByte(SCROLL_Y_ADDRESS) & 0xFF;
  }

  private boolean isLCDActive() {
    byte lcdRegisters = mmu.readByte(LCD_GPU_CONTROL_ADDRESS);
    return (lcdRegisters & 0x80) == 0x80;
  }

  private TileColor getTileColor(int x, int y) {
    x += getScrollX();
    y += getScrollY();
    int tileId = getTileId(x, y);
    TileColor tileColor = getTileColorByTileId(tileId, x, y);
    return tileColor;
  }

  private int getTileId(int x, int y) {
    int tileIndexX = x / TILE_SIZE_IN_PX;
    int tileIndexY = y / TILE_SIZE_IN_PX;
    int mapAddress = getMapAddress();
    mapAddress += tileIndexX + (tileIndexY * MAP_SIZE_IN_TILES);
    return mmu.readByte(mapAddress) & 0xFF;
  }

  private boolean isUsingMap1() {
    return getBackgroundMap() == 1;
  }

  private boolean isUsingTileSet1() {
    return getBackgroundTile() == 1;
  }

  private TileColor getTileColorByTileId(int tileId, int x, int y) {
    int tileAddress = (tileId * 16) + getTileSetAddress();
    tileAddress += (y % 8) * 2;
    x = 7 - x % 8;
    int firstValue = ((mmu.readByte(tileAddress) & 0xFF) >> x & 1) != 0 ? 1 : 0;
    int secondValue = ((mmu.readByte(tileAddress + 1) & 0xFF) >> x & 1) != 0 ? 2 : 0;
    int ordinalTileColor = firstValue + secondValue;
    return getTileColor(ordinalTileColor);
  }

  private TileColor getTileColor(int ordinalTileColor) {
    TileColor tileColor;
    switch (ordinalTileColor) {
      case 0:
        tileColor = TileColor.OFF;
        break;
      case 1:
        tileColor = TileColor.GRAY2;
        break;
      case 2:
        tileColor = TileColor.GRAY1;
        break;
      default:
        tileColor = TileColor.ON;
    }
    return tileColor;
  }

  private void setGPUMode(GPUMode currentGPUMode) {
    this.currentGPUMode = currentGPUMode;
  }

  private void incrementCurrentLine() {
    int currentLine = getCurrentLine();
    setCurrentLine(currentLine + 1);
  }

  private void setCurrentLine(int currentLine) {
    mmu.writeByte(CURRENT_LINE_ADDRESS, (byte) currentLine);
  }

  private int getBackgroundMap() {
    return (mmu.readByte(LCD_GPU_CONTROL_ADDRESS) & 0xFF) >> BG_MAP_BIT_INDEX & 0x1;
  }

  private int getBackgroundTile() {
    return (mmu.readByte(LCD_GPU_CONTROL_ADDRESS) & 0xFF) >> BG_TILE_BIT_INDEX & 0x1;
  }

  private void notifyListener() {
    if (listener != null) {
      listener.onGPUUpdated(this);
    }
  }

  private void resetCurrentModeClock() {
    currentModeClock = 0;
  }
}
