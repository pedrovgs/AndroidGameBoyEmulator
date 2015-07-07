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

public class Add8BitRegisterPlusAIntoA extends Instruction {

  private final Register sourceRegister;

  public Add8BitRegisterPlusAIntoA(GBZ80 z80, Register sourceRegister) {
    super(z80);
    this.sourceRegister = sourceRegister;
  }

  @Override public void execute() {
    byte registerAValue = z80.get8BitRegisterValue(Register.A);
    byte sourceRegisterValue = z80.get8BitRegisterValue(sourceRegister);
    byte sum = (byte) (registerAValue + sourceRegisterValue);
    z80.set8BitRegisterValue(Register.A, sum);
    z80.setLastInstructionExecutionTime(1);

    z80.disableFlagN();
    if (sum == 0) {
      z80.enableFlagZ();
    } else {
      z80.disableFlagZ();
    }
    if ((sum & 0x100) == 0x100) {
      z80.enableFlagCY();
    } else {
      z80.disableFlagCY();
    }
    if ((sum & 0x10) == 0x10) {
      z80.enableFlagH();
    } else {
      z80.disableFlagH();
    }
  }
}
