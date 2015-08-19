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

public class GPUTest extends UnitTest {

  private static final int SCREEN_HEIGHT = 144;
  private static final int SCREEN_WIDTH = 160;

  @Test public void shouldInitializeGPUWithEveryPixelConfiguredTo255InEveryChannel() {
    GPU gpu = givenAGPU();

    assertAllPixelsChannelAre((byte) 0xFF, gpu);
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
    MMU mmu = new MMU();
    return new GPU(mmu);
  }
}