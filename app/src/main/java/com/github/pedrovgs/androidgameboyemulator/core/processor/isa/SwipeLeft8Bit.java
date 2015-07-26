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

public abstract class SwipeLeft8Bit extends Instruction {

  public SwipeLeft8Bit(GBZ80 z80, MMU mmu) {
    super(z80, mmu);
  }

  @Override public void execute() {
    byte value = getValue();
    boolean shouldEnableCY = (value & 0x80) == 0x80;
    value <<= 1;
    storeValue(value);
    boolean wasZEnabled = z80.isFlagZEnabled();
    z80.resetFlagF();
    if (shouldEnableCY) {
      z80.enableFlagCY();
    }
    if (wasZEnabled) {
      z80.enableFlagZ();
    } else {
      z80.disableFlagZ();
    }
    if (value == 0) {
      z80.enableFlagZ();
    }
    setLastInstructionExecutionTime();
  }

  protected abstract byte getValue();

  protected abstract void storeValue(byte value);

  protected abstract void setLastInstructionExecutionTime();
}
