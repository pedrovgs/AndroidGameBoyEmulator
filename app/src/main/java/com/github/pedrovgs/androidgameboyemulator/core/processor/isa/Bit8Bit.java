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

package com.github.pedrovgs.androidgameboyemulator.core.processor.isa;

import com.github.pedrovgs.androidgameboyemulator.core.mmu.MMU;
import com.github.pedrovgs.androidgameboyemulator.core.processor.GBZ80;

public abstract class Bit8Bit extends Instruction {

  private final int bit;

  Bit8Bit(GBZ80 z80, int bit) {
    super(z80);
    this.bit = bit;
  }

  Bit8Bit(GBZ80 z80, MMU mmu, int bit) {
    super(z80, mmu);
    this.bit = bit;
  }

  @Override public void execute() {
    byte n = loadValue();
    z80.enableFlagH();
    if ((n >> bit & 1) == 0) {
      z80.enableFlagZ();
    }
    setLastInstructionExecutionTime();
  }

  protected abstract byte loadValue();

  protected abstract void setLastInstructionExecutionTime();
}
