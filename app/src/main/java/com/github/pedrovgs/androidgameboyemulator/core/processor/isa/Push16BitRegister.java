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

class Push16BitRegister extends Instruction {

  private final Register sourceRegister;

  Push16BitRegister(GBZ80 z80, MMU mmu, Register sourceRegister) {
    super(z80, mmu);
    this.sourceRegister = sourceRegister;
  }

  @Override public void execute() {
    int registerValue = z80.get16BitRegisterValue(sourceRegister);
    pushTwice(registerValue);
    z80.setLastInstructionExecutionTime(4);
  }
}
