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

import com.github.pedrovgs.androidgameboyemulator.UnitTest;
import com.github.pedrovgs.androidgameboyemulator.core.mmu.MMU;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class GPUTest extends UnitTest {

  private static final int SCREEN_HEIGHT = 144;
  private static final int SCREEN_WIDTH = 160;

  private MMU mmu;

  @Test public void shouldInitializeGPUWithEveryPixelConfiguredTo255InEveryChannel() {
    GPU gpu = givenAGPU();

    assertAllPixelsChannelAre((byte) 0xFF, gpu);
  }

  @Test public void shouldRenderTheCopyRightImageCloseToTheOrigin() {
    GPU gpu = givenAGPU();
    configureTileZeroWithTheCopyrightTile();
    configureMapToUseAlwaysTileZero();

    assertPixelHasColor(gpu, 0, 0, false);
    assertPixelHasColor(gpu, 1, 0, false);
    assertPixelHasColor(gpu, 2, 0, true);
    assertPixelHasColor(gpu, 3, 0, true);
    assertPixelHasColor(gpu, 4, 0, true);
    assertPixelHasColor(gpu, 5, 0, true);
    assertPixelHasColor(gpu, 6, 0, false);
    assertPixelHasColor(gpu, 7, 0, false);

    assertPixelHasColor(gpu, 0, 6, false);
    assertPixelHasColor(gpu, 1, 6, true);
    assertPixelHasColor(gpu, 2, 6, false);
    assertPixelHasColor(gpu, 3, 6, false);
    assertPixelHasColor(gpu, 4, 6, false);
    assertPixelHasColor(gpu, 5, 6, false);
    assertPixelHasColor(gpu, 6, 6, true);
    assertPixelHasColor(gpu, 7, 6, false);
  }

  @Test public void shouldRenderTheCopyRightImageFartherThanTheOrigin() {
    GPU gpu = givenAGPU();
    configureTileZeroWithTheCopyrightTile();
    configureMapToUseAlwaysTileZero();

    assertPixelHasColor(gpu, 8, 0, false);
    assertPixelHasColor(gpu, 9, 0, false);
    assertPixelHasColor(gpu, 10, 0, true);
    assertPixelHasColor(gpu, 11, 0, true);
    assertPixelHasColor(gpu, 12, 0, true);
    assertPixelHasColor(gpu, 13, 0, true);
    assertPixelHasColor(gpu, 14, 0, false);
    assertPixelHasColor(gpu, 15, 0, false);

    assertPixelHasColor(gpu, 8, 6, false);
    assertPixelHasColor(gpu, 9, 6, true);
    assertPixelHasColor(gpu, 10, 6, false);
    assertPixelHasColor(gpu, 11, 6, false);
    assertPixelHasColor(gpu, 12, 6, false);
    assertPixelHasColor(gpu, 13, 6, false);
    assertPixelHasColor(gpu, 14, 6, true);
    assertPixelHasColor(gpu, 15, 6, false);
  }

  private void assertPixelHasColor(GPU gpu, int x, int y, boolean hasColor) {
    int channel = gpu.getBlueChannelAtPixel(x, y) & 0xFF;
    if (hasColor) {
      assertEquals(0, channel);
    } else {
      assertFalse(channel == 0);
    }
  }

  private void configureTileZeroWithTheCopyrightTile() {
    mmu.writeByte(0x9000, (byte) 0x3c);
    mmu.writeByte(0x9001, (byte) 0x3c);
    mmu.writeByte(0x9002, (byte) 0x42);
    mmu.writeByte(0x9003, (byte) 0x42);
    mmu.writeByte(0x9004, (byte) 0xb9);
    mmu.writeByte(0x9005, (byte) 0xb9);
    mmu.writeByte(0x9006, (byte) 0xa5);
    mmu.writeByte(0x9007, (byte) 0xa5);
    mmu.writeByte(0x9008, (byte) 0xb9);
    mmu.writeByte(0x9009, (byte) 0xb9);
    mmu.writeByte(0x900A, (byte) 0xa5);
    mmu.writeByte(0x900B, (byte) 0xa5);
    mmu.writeByte(0x900C, (byte) 0x42);
    mmu.writeByte(0x900D, (byte) 0x42);
    mmu.writeByte(0x900E, (byte) 0x3c);
    mmu.writeByte(0x900F, (byte) 0x3c);
  }

  private void configureMapToUseAlwaysTileZero() {
    for (int i = 0x9800; i < 0x9bff; i++) {
      mmu.writeByte(i, (byte) 0);
    }
  }

  private void assertAllPixelsChannelAre(byte channelValue, GPU gpu) {
    for (int x = 0; x < SCREEN_WIDTH; x++) {
      for (int y = 0; y < SCREEN_HEIGHT; y++) {
        assertEquals(channelValue, gpu.getRedChannelAtPixel(x, y));
        assertEquals(channelValue, gpu.getGreenChannelAtPixel(x, y));
        assertEquals(channelValue, gpu.getBlueChannelAtPixel(x, y));
        assertEquals(channelValue, gpu.getAlphaChannelAtPixel(x, y));
      }
    }
  }

  private GPU givenAGPU() {
    mmu = new MMU();
    return new GPU(mmu);
  }
}