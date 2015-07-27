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
import com.github.pedrovgs.androidgameboyemulator.core.processor.Register;

public class Sbc8BitImmPCToAIntoA extends Instruction {

  Sbc8BitImmPCToAIntoA(GBZ80 z80, MMU mmu) {
    super(z80, mmu);
  }

  @Override public void execute() {
    int address = z80.getProgramCounter();
    byte memoryValue = mmu.readByte(address);
    byte registerAValue = z80.get8BitRegisterValue(Register.A);
    int carry = z80.isFlagCYEnabled() ? 1 : 0;
    byte result = (byte) (registerAValue - memoryValue - carry);
    z80.set8BitRegisterValue(Register.A, result);
    z80.setLastInstructionExecutionTime(2);

    z80.set8BitRegisterValue(Register.F, (byte) 0);
    registerAValue = z80.get8BitRegisterValue(Register.A);
    if (((registerAValue & 0xF) + (memoryValue & 0xF) + carry) > 0xF) {
      z80.enableFlagH();
    }

    if (((registerAValue & 0xff) + (memoryValue & 0xff) + carry) > 0xFF) {
      z80.enableFlagCY();
    }

    if (registerAValue == 0) {
      z80.enableFlagZ();
    }
  }
}
