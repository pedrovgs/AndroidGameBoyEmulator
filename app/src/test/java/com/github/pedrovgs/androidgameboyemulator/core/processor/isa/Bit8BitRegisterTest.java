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

import com.github.pedrovgs.androidgameboyemulator.InstructionTest;
import com.github.pedrovgs.androidgameboyemulator.core.processor.Register;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class Bit8BitRegisterTest extends InstructionTest {

  @Test public void shouldUseTwoCyclesAsLastInstructionExecutionTime() {
    Instruction instruction = new Bit8BitRegister(z80, ANY_BIT_VALUE, ANY_8BIT_SOURCE_REGISTER);

    instruction.execute();

    assertEquals(2, z80.getLastInstructionExecutionTime());
  }

  @Test public void shouldEnableFlagH() {
    Instruction instruction = new Bit8BitRegister(z80, ANY_BIT_VALUE, ANY_8BIT_SOURCE_REGISTER);

    instruction.execute();

    assertTrue(z80.isFlagHEnabled());
  }

  @Test public void shouldEnableFlagZIfTheDisplacementOfTheBitIsZero() {
    z80.set8BitRegisterValue(Register.A, (byte) 0x01);
    Instruction instruction = new Bit8BitRegister(z80, 1, Register.A);

    instruction.execute();

    assertTrue(z80.isFlagHEnabled());
  }

  @Test public void shouldSetFlagZAsDisabledIfTheDisplacementOfTheBitIsNotZero() {
    z80.set8BitRegisterValue(Register.A, (byte) 0x02);
    Instruction instruction = new Bit8BitRegister(z80, 1, Register.A);

    instruction.execute();

    assertFalse(z80.isFlagZEnabled());
  }
}

