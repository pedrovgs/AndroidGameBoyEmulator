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
import static org.mockito.Mockito.verify;

public class Push16BitRegisterTest extends InstructionTest {

  @Test
  public void shouldPushTheContentOfThe16BitRegisterValueIntoTheAddressPointedByTheMemoryStack() {
    z80.set16BitRegisterValue(ANY_16BIT_SOURCE_REGISTER, ANY_16BIT_REGISTER_VALUE);
    Instruction instruction = new Push16BitRegister(z80, mmu, ANY_16BIT_SOURCE_REGISTER);

    instruction.execute();
  }

  @Test public void shouldUseFourCyclesAsLastInstructionExecutionTime() {
    Instruction instruction = new Push16BitRegister(z80, mmu, ANY_16BIT_SOURCE_REGISTER);

    instruction.execute();

    assertEquals(4, z80.getLastInstructionExecutionTime());
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldNotAccept8BitRegistersAsSourceRegister() {
    Instruction instruction = new Push16BitRegister(z80, mmu, ANY_8BIT_SOURCE_REGISTER);

    instruction.execute();
  }

  @Test public void shouldDecrementStackPointerByTwo() {
    z80.setStackPointer(ANY_16BIT_REGISTER_VALUE);
    Instruction instruction = new Push16BitRegister(z80, mmu, ANY_16BIT_SOURCE_REGISTER);

    instruction.execute();

    assertEquals(ANY_16BIT_REGISTER_VALUE - 2, z80.getStackPointer());
  }

  @Test public void shouldPushTheValueOfTheSourceRegisterIntoTheMemoryPointedByTheStackPointer() {
    z80.set16BitRegisterValue(ANY_16BIT_SOURCE_REGISTER, ANY_16BIT_REGISTER_VALUE);
    z80.setStackPointer(ANY_STACK_POINTER_VALUE);
    Instruction instruction = new Push16BitRegister(z80, mmu, ANY_16BIT_SOURCE_REGISTER);

    instruction.execute();

    byte firstPart = (ANY_16BIT_REGISTER_VALUE >> 8) & 0xFF;
    verify(mmu).writeByte(ANY_STACK_POINTER_VALUE - 1, firstPart);
    byte secondPart = ANY_16BIT_REGISTER_VALUE & 0xFF;
    verify(mmu).writeByte(ANY_STACK_POINTER_VALUE - 2, secondPart);
  }
}
