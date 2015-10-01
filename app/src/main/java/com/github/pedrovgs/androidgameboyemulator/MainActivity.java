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
import android.util.Log;
import android.widget.Toast;
import com.github.pedrovgs.androidgameboyemulator.core.GameBoy;
import com.github.pedrovgs.androidgameboyemulator.core.gameloader.AndroidGameReader;
import com.github.pedrovgs.androidgameboyemulator.core.gameloader.GameLoader;
import com.github.pedrovgs.androidgameboyemulator.core.gameloader.GameReader;
import com.github.pedrovgs.androidgameboyemulator.core.gpu.GPU;
import com.github.pedrovgs.androidgameboyemulator.core.mmu.MMU;
import com.github.pedrovgs.androidgameboyemulator.core.processor.GBZ80;
import com.github.pedrovgs.androidgameboyemulator.lcd.LCD;
import java.io.IOException;

public class MainActivity extends Activity {

  private static final String LOGTAG = "AndroidGameBoyEmulator";
  private static final String TEST_ROM_URI = "/sdcard/Download/test.gb";

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main_activity);

    initializeGameBoy();
  }

  private void initializeGameBoy() {
    final LCD lcd = (LCD) findViewById(R.id.lcd);
    final MMU mmu = new MMU();
    final GBZ80 z80 = new GBZ80();
    final GPU gpu = new GPU(mmu);
    GameReader gameReader = new AndroidGameReader();
    GameLoader gameLoader = new GameLoader(gameReader);
    final GameBoy gameBoy = new GameBoy(z80, mmu, gpu, gameLoader);
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
}
