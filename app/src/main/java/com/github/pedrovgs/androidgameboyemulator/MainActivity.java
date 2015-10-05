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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnTouch;
import com.github.pedrovgs.androidgameboyemulator.core.GameBoy;
import com.github.pedrovgs.androidgameboyemulator.core.gameloader.AndroidGameReader;
import com.github.pedrovgs.androidgameboyemulator.core.gameloader.GameLoader;
import com.github.pedrovgs.androidgameboyemulator.core.gameloader.GameReader;
import com.github.pedrovgs.androidgameboyemulator.core.gpu.GPU;
import com.github.pedrovgs.androidgameboyemulator.core.keypad.Key;
import com.github.pedrovgs.androidgameboyemulator.core.keypad.Keypad;
import com.github.pedrovgs.androidgameboyemulator.core.mmu.MMU;
import com.github.pedrovgs.androidgameboyemulator.core.processor.GBZ80;
import com.github.pedrovgs.androidgameboyemulator.lcd.LCD;
import java.io.IOException;

public class MainActivity extends Activity {

  private static final String LOGTAG = "AndroidGameBoyEmulator";
  private static final float LCD_WIDTH_SCREEN_RATIO = 0.55f;
  private static final String TEST_ROM_URI = "/sdcard/Download/test.gb";
  private static final float LCD_WIDTH = 160;
  private static final float LCD_HEIGHT = 144;
  private static final float LCD_ASPECT_RATIO = LCD_WIDTH / LCD_HEIGHT;

  @Bind(R.id.lcd) LCD lcd;

  private GameBoy gameBoy;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main_activity);
    ButterKnife.bind(this);
    adjustLCDSize();
    initializeGameBoy();
  }

  @OnTouch(R.id.bt_a) public boolean onButtonATouch(View view, MotionEvent event) {
    if (isActionDownMotionEvent(event)) {
      gameBoy.keyDown(Key.A);
    } else if (isActionUpMotionEvent(event)) {
      gameBoy.keyUp(Key.B);
    }
    return true;
  }

  @OnTouch(R.id.bt_b) public boolean onButtonBTouch(View view, MotionEvent event) {
    if (isActionDownMotionEvent(event)) {
      gameBoy.keyDown(Key.B);
    } else if (isActionUpMotionEvent(event)) {
      gameBoy.keyUp(Key.B);
    }
    return true;
  }

  @OnTouch(R.id.bt_up) public boolean onButtonUpTouch(View view, MotionEvent event) {
    if (isActionDownMotionEvent(event)) {
      gameBoy.keyDown(Key.UP);
    } else if (isActionUpMotionEvent(event)) {
      gameBoy.keyUp(Key.UP);
    }
    return true;
  }

  @OnTouch(R.id.bt_down) public boolean onButtonDownTouch(View view, MotionEvent event) {
    if (isActionDownMotionEvent(event)) {
      gameBoy.keyDown(Key.DOWN);
    } else if (isActionUpMotionEvent(event)) {
      gameBoy.keyUp(Key.DOWN);
    }
    return true;
  }

  @OnTouch(R.id.bt_left) public boolean onButtonLeftTouch(View view, MotionEvent event) {
    if (isActionDownMotionEvent(event)) {
      gameBoy.keyDown(Key.LEFT);
    } else if (isActionUpMotionEvent(event)) {
      gameBoy.keyUp(Key.LEFT);
    }
    return true;
  }

  @OnTouch(R.id.bt_right) public boolean onButtonRightTouch(View view, MotionEvent event) {
    if (isActionDownMotionEvent(event)) {
      gameBoy.keyDown(Key.RIGHT);
    } else if (isActionUpMotionEvent(event)) {
      gameBoy.keyUp(Key.RIGHT);
    }
    return true;
  }

  @OnTouch(R.id.bt_start) public boolean onButtonStartTouch(View view, MotionEvent event) {
    if (isActionDownMotionEvent(event)) {
      gameBoy.keyDown(Key.START);
    } else if (isActionUpMotionEvent(event)) {
      gameBoy.keyUp(Key.START);
    }
    return true;
  }

  @OnTouch(R.id.bt_select) public boolean onButtonSelectTouch(View view, MotionEvent event) {
    if (isActionDownMotionEvent(event)) {
      gameBoy.keyDown(Key.SELECT);
    } else if (isActionUpMotionEvent(event)) {
      gameBoy.keyUp(Key.SELECT);
    }
    return true;
  }

  private void adjustLCDSize() {
    int screenWidth = getScreenWidth();
    int lcdWidth = (int) (screenWidth * LCD_WIDTH_SCREEN_RATIO);
    int lcdHeight = (int) (lcdWidth / LCD_ASPECT_RATIO);
    ViewGroup.LayoutParams layoutParams = lcd.getLayoutParams();
    layoutParams.height = lcdHeight;
    layoutParams.width = lcdWidth;
  }

  private int getScreenWidth() {
    DisplayMetrics displayMetrics = new DisplayMetrics();
    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
    return displayMetrics.widthPixels;
  }

  private void initializeGameBoy() {
    final MMU mmu = new MMU();
    final GBZ80 z80 = new GBZ80();
    final GPU gpu = new GPU(mmu);
    GameReader gameReader = new AndroidGameReader();
    GameLoader gameLoader = new GameLoader(gameReader);
    Keypad keypad = new Keypad(mmu);
    gameBoy = new GameBoy(z80, mmu, gpu, gameLoader, keypad);
    gameBoy.setGPUListener(lcd);

    Thread gameBoyThread = new Thread() {
      @Override public void run() {
        super.run();
        try {
          gameBoy.loadGame(TEST_ROM_URI);
          while (true) {
            gameBoy.frame();
          }
          //fillMemoryWithTrash(mmu);
        } catch (IOException e) {
          runOnUiThread(new Runnable() {
            @Override public void run() {
              Toast.makeText(MainActivity.this, "Error loading the ROM", Toast.LENGTH_SHORT).show();
            }
          });
          Log.e(LOGTAG, "The ROM can't be loaded.", e);
        }
      }
    };
    gameBoyThread.start();
  }

  private boolean isActionDownMotionEvent(MotionEvent event) {
    return event.getAction() == MotionEvent.ACTION_DOWN;
  }

  private boolean isActionUpMotionEvent(MotionEvent event) {
    return event.getAction() == MotionEvent.ACTION_UP;
  }
}
