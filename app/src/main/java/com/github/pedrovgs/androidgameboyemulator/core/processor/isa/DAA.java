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

class DAA extends Instruction {

  DAA(GBZ80 z80) {
    super(z80);
  }

  @Override public void execute() {
    byte flags = z80.get8BitRegisterValue(Register.F);
    int a = z80.get8BitRegisterValue(Register.A) & 0xff;

    if (!z80.isFlagNEnabled()) {
      if (z80.isFlagHEnabled() || (a & 0x0F) > 9) {
        a += 6;
      }

      if (z80.isFlagCYEnabled() || (a > 0x9F)) {
        a += 0x60;
      }
    } else {
      if (z80.isFlagHEnabled()) {
        a = (a - 6) & 0xff;
      }
      if (z80.isFlagCYEnabled()) {
        a -= 0x60;
      }
    }

    z80.set8BitRegisterValue(Register.F, (byte) (flags & ~(0x20 | 0x80)));
    if ((a & 0x100) == 0x100) {
      flags = z80.get8BitRegisterValue(Register.F);
      z80.set8BitRegisterValue(Register.F, (byte) (flags | 0x10));
    }
    z80.set8BitRegisterValue(Register.A, (byte) a);

    if (a == 0) {
      flags = z80.get8BitRegisterValue(Register.F);
      z80.set8BitRegisterValue(Register.F, (byte) (flags | 0x80));
    }
    z80.setLastInstructionExecutionTime(1);
  }
}
