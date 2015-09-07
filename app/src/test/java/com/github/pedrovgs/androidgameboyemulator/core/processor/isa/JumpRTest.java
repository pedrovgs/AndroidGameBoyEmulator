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

public class JumpRTest extends InstructionTest {

  private static final int FLAG_Z = 0x80;
  private static final int JUMP_NON_ZERO = 0;
  private static final int PROGRAM_COUNTER_INCREMENT = 5;

  @Test public void shouldIncrementProgramCounterOnceIfTheJumpConditionIsFalse() {
    z80.setProgramCounter(ANY_PROGRAM_COUNTER);
    when(mmu.readByte(ANY_PROGRAM_COUNTER)).thenReturn((byte) PROGRAM_COUNTER_INCREMENT);
    z80.enableFlagZ();
    Instruction instruction = new JumpR(z80, mmu, JUMP_NON_ZERO, FLAG_Z);

    instruction.execute();

    int expectedProgramCounter = ANY_PROGRAM_COUNTER + 1;
    assertEquals(expectedProgramCounter, z80.getProgramCounter());
  }

  @Test
  public void shouldIncrementPCWithTheMemoryPointedByTheProgramCounterPlus1IfTheConditionIsTrue() {
    z80.setProgramCounter(ANY_PROGRAM_COUNTER);
    when(mmu.readByte(ANY_PROGRAM_COUNTER)).thenReturn((byte) PROGRAM_COUNTER_INCREMENT);
    z80.disableFlagZ();
    Instruction instruction = new JumpR(z80, mmu, JUMP_NON_ZERO, FLAG_Z);

    instruction.execute();

    int expectedProgramCounter = ANY_PROGRAM_COUNTER + 1 + PROGRAM_COUNTER_INCREMENT;
    assertEquals(expectedProgramCounter, z80.getProgramCounter());
  }

  @Test public void shouldUseTwoCyclesAsLastInstructionExecutionTimeIfTheJumpIsNotPerformed() {
    z80.enableFlagZ();
    Instruction instruction = new JumpR(z80, mmu, JUMP_NON_ZERO, FLAG_Z);

    instruction.execute();

    assertEquals(2, z80.getLastInstructionExecutionTime());
  }
}