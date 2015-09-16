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
    final LCD lcd = (LCD) findViewById(R.id.lcd);
    final MMU mmu = new MMU();
    GBZ80 z80 = new GBZ80();
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
          while (!mmu.isSystemReady()) {
            gameBoy.tick();
          }
          fillMemoryWithTrash(mmu);
          lcd.onGPUUpdated(gpu);
          byte color = gpu.getBlueChannelAtPixel(6, 6);
          color = color;
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

  private void fillMemoryWithTrash(MMU mmu) {
    //Configure tile0 in tile set 1
    mmu.writeByte(0x8000, (byte) 0x3c);
    mmu.writeByte(0x8001, (byte) 0x3c);
    mmu.writeByte(0x8002, (byte) 0x42);
    mmu.writeByte(0x8003, (byte) 0x42);
    mmu.writeByte(0x8004, (byte) 0xb9);
    mmu.writeByte(0x8005, (byte) 0xb9);
    mmu.writeByte(0x8006, (byte) 0xa5);
    mmu.writeByte(0x8007, (byte) 0xa5);
    mmu.writeByte(0x8008, (byte) 0xb9);
    mmu.writeByte(0x8009, (byte) 0xb9);
    mmu.writeByte(0x800A, (byte) 0xA2);
    mmu.writeByte(0x800B, (byte) 0xA2);
    mmu.writeByte(0x800C, (byte) 0x42);
    mmu.writeByte(0x800D, (byte) 0x42);
    mmu.writeByte(0x800E, (byte) 0x3c);
    mmu.writeByte(0x800F, (byte) 0x3c);
    for (int i = 0x9c00; i < 0x9fff; i++) {
      mmu.writeByte(i, (byte) 0);
    }
    //Scroll x & y = 0
    mmu.writeByte(0xff42, (byte) 0);
    mmu.writeByte(0xff43, (byte) 0);
  }
}
