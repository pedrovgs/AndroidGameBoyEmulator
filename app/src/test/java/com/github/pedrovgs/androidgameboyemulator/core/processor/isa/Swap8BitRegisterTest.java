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
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class Swap8BitRegisterTest extends InstructionTest {

  @Test public void shouldUseTwoCyclesAsLastInstructionExecutionTime() {
    Instruction instruction = new Swap8BitRegister(z80, mmu, ANY_8BIT_DESTINY_REGISTER);

    instruction.execute();

    assertEquals(2, z80.getLastInstructionExecutionTime());
  }

  @Test public void shouldDisableFlagCYFlagNAndFlagH() {
    Instruction instruction = new Swap8BitRegister(z80, mmu, ANY_8BIT_DESTINY_REGISTER);

    instruction.execute();

    assertFalse(z80.isFlagCYEnabled());
    assertFalse(z80.isFlagHEnabled());
    assertFalse(z80.isFlagNEnabled());
  }

  @Test public void shouldEnableFlagZIfResultIsZero() {
    z80.set8BitRegisterValue(ANY_8BIT_DESTINY_REGISTER, (byte) 0x0);
    Instruction instruction = new Swap8BitRegister(z80, mmu, ANY_8BIT_DESTINY_REGISTER);

    instruction.execute();

    assertTrue(z80.isFlagZEnabled());
  }

  @Test public void shouldNotEnableFlagZIfResultIsZeroAndTheFlagWasNotPreviouslyEnabled() {
    z80.set8BitRegisterValue(ANY_8BIT_DESTINY_REGISTER, (byte) 0x1);
    z80.disableFlagZ();
    Instruction instruction = new Swap8BitRegister(z80, mmu, ANY_8BIT_DESTINY_REGISTER);

    instruction.execute();

    assertFalse(z80.isFlagZEnabled());
  }

  @Test public void shouldEnableFlagZIfResultIsNotZeroAndTheFlagWasNotPreviouslyEnabled() {
    z80.set8BitRegisterValue(ANY_8BIT_DESTINY_REGISTER, (byte) 0x0);
    z80.disableFlagZ();
    Instruction instruction = new Swap8BitRegister(z80, mmu, ANY_8BIT_DESTINY_REGISTER);

    instruction.execute();

    assertTrue(z80.isFlagZEnabled());
  }

  @Test public void shouldSwapRegisterValueAndStoreTheResultIntoTheRegister() {
    z80.set8BitRegisterValue(ANY_8BIT_DESTINY_REGISTER, (byte) 0x10);
    Instruction instruction = new Swap8BitRegister(z80, mmu, ANY_8BIT_DESTINY_REGISTER);

    instruction.execute();

    assertEquals((byte) 0x1, z80.get8BitRegisterValue(ANY_8BIT_DESTINY_REGISTER));
  }
}