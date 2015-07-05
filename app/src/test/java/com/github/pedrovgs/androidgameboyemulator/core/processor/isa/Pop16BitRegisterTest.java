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
import static org.mockito.Mockito.when;

public class Pop16BitRegisterTest extends InstructionTest {

  @Test(expected = IllegalArgumentException.class)
  public void shouldNotAccept8BitRegistersAsDestinyRegister() {
    Instruction instruction = new Pop16BitRegister(z80, mmu, ANY_8BIT_DESTINY_REGISTER);

    instruction.execute();
  }

  @Test public void shouldUse3CyclesAsLastInstructionExecutionTime() {
    Instruction instruction = new Pop16BitRegister(z80, mmu, ANY_16BIT_DESTINY_REGISTER);

    instruction.execute();

    assertEquals(3, z80.getLastInstructionExecutionTime());
  }

  @Test public void shouldIncrementStackPointerByTwo() {
    z80.setStackPointer(ANY_STACK_POINTER_VALUE);
    Instruction instruction = new Pop16BitRegister(z80, mmu, ANY_16BIT_DESTINY_REGISTER);

    instruction.execute();

    assertEquals(ANY_STACK_POINTER_VALUE + 2, z80.getStackPointer());
  }

  //This test fails because the set16BitRegisterValue is not working as should. Debug and you'll see
  // how the value arrives properly to the class but the method set16Bit breaks the final result
  @Test public void shouldPopTheContentOfTheMemoryPointedByTheStackPointerIntoTheDestinyRegister() {
    z80.setStackPointer(ANY_STACK_POINTER_VALUE);
    when(mmu.readByte(ANY_STACK_POINTER_VALUE)).thenReturn((byte) 0xF1);
    when(mmu.readByte(ANY_STACK_POINTER_VALUE + 1)).thenReturn((byte) 0xF2);
    Instruction instruction = new Pop16BitRegister(z80, mmu, ANY_16BIT_DESTINY_REGISTER);

    instruction.execute();

    assertEquals(0xF2F1, z80.get16BitRegisterValue(ANY_16BIT_DESTINY_REGISTER));
  }
}
