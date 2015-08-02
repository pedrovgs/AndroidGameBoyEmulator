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

public abstract class RotateRightCarry8Bit extends Instruction {

  RotateRightCarry8Bit(GBZ80 z80) {
    super(z80);
  }

  RotateRightCarry8Bit(GBZ80 z80, MMU mmu) {
    super(z80, mmu);
  }

  @Override public void execute() {
    byte registerAValue = loadValue();
    boolean firstBit = (registerAValue & 0x1) == 0x1;
    registerAValue = (byte) (registerAValue >> 1);
    if (firstBit) {
      z80.enableFlagCY();
      registerAValue |= 0x80;
    } else {
      z80.disableFlagCY();
    }
    storeValue(registerAValue);
    z80.disableFlagH();
    z80.disableFlagN();
    z80.disableFlagZ();
    setLastInstructionExecutionTime();
  }

  protected abstract byte loadValue();

  protected abstract void storeValue(byte value);

  protected abstract void setLastInstructionExecutionTime();
}
