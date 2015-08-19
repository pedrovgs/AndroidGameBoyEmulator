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

package com.github.pedrovgs.androidgameboyemulator;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import com.github.pedrovgs.androidgameboyemulator.core.gpu.GPU;
import com.github.pedrovgs.androidgameboyemulator.lcd.LCD;

public class MainActivity extends Activity {

  private Handler handler;
  private FakeGPU fakeGPU;
  private LCD lcd;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main_activity);
    lcd = (LCD) findViewById(R.id.lcd);
    fakeGPU = new FakeGPU();
    handler = new Handler();
    handler.postDelayed(updateLCD, 30);
  }

  private Runnable updateLCD = new Runnable() {
    @Override public void run() {
      fakeGPU.increment();
      lcd.onGPUUpdated(fakeGPU);
      handler.postDelayed(updateLCD, 30);
    }
  };

  private class FakeGPU extends GPU {

    private boolean increase = true;
    private int color = 0;

    private FakeGPU() {
      super(mmu);
    }

    public void increment() {
      if (increase) {
        color++;
      } else {
        color--;
      }

      if (color == 255 && increase) {
        increase = false;
      } else if (color == 0 && !increase) {
        increase = true;
      }
    }

    @Override public byte getAlphaChannelAtPixel(int x, int y) {
      return (byte) 255;
    }

    public byte getRedChannelAtPixel(int x, int y) {
      if ((x <= 9 || x >= 149) || (y <= 9 || y >= 133)) {
        return 0;
      }
      return (byte) color;
    }

    public byte getGreenChannelAtPixel(int x, int y) {
      if ((x <= 9 || x >= 149) || (y <= 9 || y >= 133)) {
        return 0;
      }
      return (byte) color;
    }

    public byte getBlueChannelAtPixel(int x, int y) {
      if ((x <= 9 || x >= 149) || (y <= 9 || y >= 133)) {
        return 0;
      }
      return (byte) color;
    }
  }
}
