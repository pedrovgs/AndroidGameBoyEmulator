package com.github.pedrovgs.androidgameboyemulator.core.processor.isa;

import com.github.pedrovgs.androidgameboyemulator.InstructionTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class Load16BitImmPcIntoStackPointerTest extends InstructionTest {

  @Test public void shouldUseThreeCyclesAsLastInstructionExecutionTime() {
    Instruction instruction = new Load16BitImmPcIntoStackPointer(z80, mmu);

    instruction.execute();

    assertEquals(5, z80.getLastInstructionExecutionTime());
  }

  @Test public void shouldLoadTheContentOfTheMemoryPointedByPCIntoTheStackPointer() {
    z80.setProgramCounter(ANY_PROGRAM_COUNTER);
    when(mmu.readWord(ANY_PROGRAM_COUNTER)).thenReturn(ANY_16BIT_REGISTER_VALUE);
    Instruction instruction = new Load16BitImmPcIntoStackPointer(z80, mmu);

    instruction.execute();

    assertEquals(ANY_16BIT_REGISTER_VALUE, z80.getStackPointer());
  }
}