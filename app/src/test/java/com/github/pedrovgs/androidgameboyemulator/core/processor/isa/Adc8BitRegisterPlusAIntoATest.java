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
import static org.junit.Assert.assertTrue;

public class Adc8BitRegisterPlusAIntoATest extends InstructionTest {

  @Test(expected = IllegalArgumentException.class)
  public void shouldNotAccept16BitRegistersAsSourceRegister() {
    Instruction instruction = new Adc8BitRegisterPlusAIntoA(z80, ANY_16BIT_DESTINY_REGISTER);

    instruction.execute();
  }

  @Test public void shouldUseOneCycleAsLastInstructionExecutionTime() {
    Instruction instruction = new Adc8BitRegisterPlusAIntoA(z80, ANY_8BIT_SOURCE_REGISTER);

    instruction.execute();

    assertEquals(1, z80.getLastInstructionExecutionTime());
  }

  @Test public void shouldLoadTheContentOfTheSourceRegisterPlusCarryIntoA() {
    z80.enableFlagCY();
    z80.set8BitRegisterValue(ANY_8BIT_SOURCE_REGISTER, ANY_8BIT_REGISTER_VALUE);
    z80.set8BitRegisterValue(Register.A, ANY_8BIT_REGISTER_VALUE);
    Instruction instruction = new Adc8BitRegisterPlusAIntoA(z80, ANY_8BIT_SOURCE_REGISTER);

    instruction.execute();

    byte expectedSum = ANY_8BIT_REGISTER_VALUE + ANY_8BIT_REGISTER_VALUE + 1;
    assertEquals(expectedSum, z80.get8BitRegisterValue(Register.A));
  }

  @Test public void shouldEnableFlagZIfTheContentIsZero() {
    z80.enableFlagCY();
    z80.set8BitRegisterValue(ANY_8BIT_SOURCE_REGISTER, (byte) 1);
    z80.set8BitRegisterValue(Register.A, (byte) -2);
    Instruction instruction = new Adc8BitRegisterPlusAIntoA(z80, ANY_8BIT_SOURCE_REGISTER);

    instruction.execute();

    assertTrue(z80.isFlagZEnabled());
  }
}
