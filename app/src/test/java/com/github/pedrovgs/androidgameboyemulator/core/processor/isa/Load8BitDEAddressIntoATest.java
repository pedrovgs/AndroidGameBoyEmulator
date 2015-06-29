package com.github.pedrovgs.androidgameboyemulator.core.processor.isa;

import com.github.pedrovgs.androidgameboyemulator.InstructionTest;
import com.github.pedrovgs.androidgameboyemulator.core.processor.Register;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.when;

public class Load8BitDEAddressIntoATest extends InstructionTest {

  @Test public void shouldLoadTheByteOfMemoryPointedByDERegisterIntoTheRegisterA() {
    when(mmu.readByte(anyInt())).thenReturn(ANY_MEMORY_BYTE_VALUE);
    Instruction instruction = new Load8BitDEAddressIntoA(z80, mmu);

    instruction.execute();

    assertEquals(ANY_MEMORY_BYTE_VALUE, z80.get8BitRegisterValue(Register.A));
  }

  @Test public void shouldUseTwoCyclesAsLastExecutionTime() {
    Instruction instruction = new Load8BitDEAddressIntoA(z80, mmu);

    instruction.execute();

    assertEquals(2, z80.getLastInstructionExecutionTime());
  }
}