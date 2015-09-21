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

class Adc8BitRegisterPlusAIntoA extends Instruction {

  private final Register sourceRegister;

  Adc8BitRegisterPlusAIntoA(GBZ80 z80, Register sourceRegister) {
    super(z80);
    this.sourceRegister = sourceRegister;
  }

  @Override public void execute() {
    byte registerValue = z80.get8BitRegisterValue(sourceRegister);
    byte registerAValue = z80.get8BitRegisterValue(Register.A);
    int carry = z80.isFlagCYEnabled() ? 1 : 0;
    byte sum = (byte) (registerAValue + registerValue + carry);
    z80.set8BitRegisterValue(Register.A, sum);
    z80.setLastInstructionExecutionTime(1);
    registerAValue = z80.get8BitRegisterValue(Register.A);

    z80.resetFlagF();
    if (((registerAValue & 0xF) + (registerValue & 0xF) + carry) > 0xF) {
      z80.enableFlagH();
    }

    if (((registerAValue & 0xff) + (registerValue & 0xff) + carry) > 0xFF) {
      z80.enableFlagCY();
    }

    if (registerAValue == 0) {
      z80.enableFlagZ();
    }
  }
}
