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

public class RotateRightCarry8BitRegisterATest extends InstructionTest {

  @Test public void shouldUseOneCycleAsLastInstructionExecutionTime() {
    Instruction instruction = new RotateRightCarry8BitRegisterA(z80);

    instruction.execute();

    assertEquals(1, z80.getLastInstructionExecutionTime());
  }

  @Test public void shouldDisableFlagsExceptTheCYOne() {
    Instruction instruction = new RotateRightCarry8BitRegisterA(z80);

    instruction.execute();

    assertFalse(z80.isFlagHEnabled());
    assertFalse(z80.isFlagNEnabled());
    assertFalse(z80.isFlagZEnabled());
  }

  @Test public void shouldRotateRegisterAValueWithoutUpdateCarryAndStoreTheResultIntoRegisterA() {
    z80.set8BitRegisterValue(Register.A, (byte) 2);
    Instruction instruction = new RotateRightCarry8BitRegisterA(z80);

    instruction.execute();

    assertEquals(1, z80.get8BitRegisterValue(Register.A));
    assertFalse(z80.isFlagCYEnabled());
  }

  @Test public void shouldRotateRegisterAValueAndUpdateTheCarryValue() {
    z80.set8BitRegisterValue(Register.A, (byte) 3);
    Instruction instruction = new RotateRightCarry8BitRegisterA(z80);

    instruction.execute();

    assertEquals((byte) 0x81, z80.get8BitRegisterValue(Register.A));
    assertTrue(z80.isFlagCYEnabled());
  }
}