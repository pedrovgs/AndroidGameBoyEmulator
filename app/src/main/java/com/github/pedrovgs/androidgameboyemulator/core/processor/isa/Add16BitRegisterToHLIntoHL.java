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

import com.github.pedrovgs.androidgameboyemulator.core.processor.GBZ80;
import com.github.pedrovgs.androidgameboyemulator.core.processor.Register;

class Add16BitRegisterToHLIntoHL extends Instruction {

  private final Register sourceRegister;

  Add16BitRegisterToHLIntoHL(GBZ80 z80, Register sourceRegister) {
    super(z80);
    this.sourceRegister = sourceRegister;
  }

  @Override public void execute() {
    int registerValue = z80.get16BitRegisterValue(sourceRegister);
    int registerHLValue = z80.get16BitRegisterValue(Register.HL);
    int sum = registerHLValue + registerValue;
    z80.set16BitRegisterValue(Register.HL, sum);
    z80.setLastInstructionExecutionTime(2);

    z80.disableFlagN();
    if ((registerValue & 0xFFF) + (registerHLValue & 0xFFF) > 0xFFF) {
      z80.enableFlagH();
    } else {
      z80.disableFlagH();
    }
    if (sum > 0xFFFF) {
      z80.enableFlagCY();
    } else {
      z80.disableFlagCY();
    }
  }
}
