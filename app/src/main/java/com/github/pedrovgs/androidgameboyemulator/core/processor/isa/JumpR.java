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

class JumpR extends Instruction {

  private final int condition;
  private final int flag;

  JumpR(GBZ80 z80, MMU mmu, int condition, int flag) {
    super(z80, mmu);
    this.condition = condition;
    this.flag = flag;
  }

  @Override public void execute() {
    int programCounter = z80.getProgramCounter();
    byte value = mmu.readByte(programCounter);
    z80.incrementProgramCounter();
    if ((z80.get8BitRegisterValue(Register.F) & flag) == condition) {
      int newProgramCounterValue = z80.getProgramCounter() + value;
      z80.setProgramCounter(newProgramCounterValue);
      if (flag != JUMP && condition != JUMP) {
        z80.setLastInstructionExecutionTime(3);
      }
    }
    z80.setLastInstructionExecutionTime(2);
  }
}
