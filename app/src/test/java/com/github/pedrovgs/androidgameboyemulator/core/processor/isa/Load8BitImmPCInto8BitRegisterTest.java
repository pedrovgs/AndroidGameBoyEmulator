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

public class Load8BitImmPCInto8BitRegisterTest extends InstructionTest {

  @Test(expected = IllegalArgumentException.class)
  public void shouldNotAccept16BitRegistersAsDestiny() {
    Instruction load8BitImm =
        new Load8BitImmPCInto8BitRegister(z80, mmu, ANY_16BIT_DESTINY_REGISTER);

    load8BitImm.execute();
  }

  @Test public void shouldLoadProgramCounterAddressIntoTheDestinyRegister() {
    int programCounter = z80.getProgramCounter();
    when(mmu.readByte(programCounter)).thenReturn(ANY_MEMORY_BYTE_VALUE);
    Instruction load8BitImm =
        new Load8BitImmPCInto8BitRegister(z80, mmu, ANY_8BIT_DESTINY_REGISTER);

    load8BitImm.execute();

    byte destinyRegisterValue = z80.get8BitRegisterValue(ANY_8BIT_DESTINY_REGISTER);
    assertEquals(ANY_MEMORY_BYTE_VALUE, destinyRegisterValue);
  }

  @Test public void shouldIncrementProgramCounterOnceAfterTheInstructionExecution() {
    int originalProgramCounter = z80.getProgramCounter();
    Instruction load8BitImm =
        new Load8BitImmPCInto8BitRegister(z80, mmu, ANY_8BIT_DESTINY_REGISTER);

    load8BitImm.execute();

    assertEquals(originalProgramCounter + 1, z80.getProgramCounter());
  }

  @Test public void shouldUseTwoCyclesAsExecutionTime() {
    Instruction load8BitImm =
        new Load8BitImmPCInto8BitRegister(z80, mmu, ANY_8BIT_DESTINY_REGISTER);

    load8BitImm.execute();

    assertEquals(2, z80.getLastInstructionExecutionTime());
  }
}
