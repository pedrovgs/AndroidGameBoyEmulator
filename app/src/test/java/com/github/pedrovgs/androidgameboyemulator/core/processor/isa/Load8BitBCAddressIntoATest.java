package com.github.pedrovgs.androidgameboyemulator.core.processor.isa;

import com.github.pedrovgs.androidgameboyemulator.InstructionTest;
import com.github.pedrovgs.androidgameboyemulator.core.processor.Register;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class Load8BitBCAddressIntoATest extends InstructionTest {

  @Test public void shouldLoadTheByteOfMemoryPointedByBCRegisterIntoTheRegisterA() {
    z80.set16BitRegisterValue(Register.BC, ANY_16BIT_REGISTER_VALUE);
    when(mmu.readByte(ANY_16BIT_REGISTER_VALUE)).thenReturn(ANY_MEMORY_BYTE_VALUE);
    Instruction instruction = new Load8BitBCAddressIntoA(z80, mmu);

    instruction.execute();

    assertEquals(ANY_MEMORY_BYTE_VALUE, z80.get8BitRegisterValue(Register.A));
  }

  @Test public void shouldUseTwoCyclesAsLastExecutionTime() {
    Instruction instruction = new Load8BitBCAddressIntoA(z80, mmu);

    instruction.execute();

    assertEquals(2, z80.getLastInstructionExecutionTime());
  }
}
