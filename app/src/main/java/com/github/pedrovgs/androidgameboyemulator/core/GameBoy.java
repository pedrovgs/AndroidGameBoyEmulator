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

package com.github.pedrovgs.androidgameboyemulator.core;

import com.github.pedrovgs.androidgameboyemulator.core.mmu.MMU;
import com.github.pedrovgs.androidgameboyemulator.core.processor.GBZ80;

public class GameBoy {

  private final GBZ80 z80;
  private final MMU mmu;

  public GameBoy(GBZ80 z80, MMU mmu) {
    this.z80 = z80;
    this.mmu = mmu;
  }

  public void startEmulation() {
    while (true) {
      int programCounter = z80.getProgramCounter();
      int rawInstruction = mmu.readByte(programCounter);
      z80.execute(rawInstruction, mmu);
      //z80.maskProgramCounter();
      z80.updateClock();
    }
  }
}
