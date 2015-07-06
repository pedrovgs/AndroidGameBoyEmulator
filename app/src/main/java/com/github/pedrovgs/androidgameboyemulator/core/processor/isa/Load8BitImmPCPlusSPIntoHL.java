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

public class Load8BitImmPCPlusSPIntoHL extends Instruction {

  public Load8BitImmPCPlusSPIntoHL(GBZ80 z80, MMU mmu) {
    super(z80, mmu);
  }

  @Override public void execute() {
    int programCounter = z80.getProgramCounter();
    byte memoryValue = mmu.readByte(programCounter);
    z80.incrementProgramCounter();
    int stackPointer = z80.getStackPointer();
    int registerValue = (stackPointer + memoryValue) & 0xFFFF;
    z80.set16BitRegisterValue(Register.HL, registerValue);

    int check = stackPointer ^ (memoryValue) ^ ((stackPointer + memoryValue) & 0xFFFF);
    z80.disableFlagZ();
    z80.disableFlagN();
    if ((check & 0x100) == 0x100) {
      z80.enableFlagCY();
    } else {
      z80.disableFlagCY();
    }
    if ((check & 0x10) == 0x10) {
      z80.enableFlagH();
    } else {
      z80.disableFlagH();
    }
    z80.setLastInstructionExecutionTime(3);
  }
}
