package com.github.pedrovgs.androidgameboyemulator.core.processor.isa;

import com.github.pedrovgs.androidgameboyemulator.InstructionTest;
import com.github.pedrovgs.androidgameboyemulator.core.processor.Register;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class Load8BitImmIntoHLAddressTest extends InstructionTest {

  @Test public void shouldStoreProgramCounterAddressMemoryValueIntoHlAddress() {
    z80.setProgramCounter(ANY_16BIT_REGISTER_VALUE);
    when(mmu.readByte(ANY_16BIT_REGISTER_VALUE + 1)).thenReturn(ANY_MEMORY_BYTE_VALUE);
    Instruction instruction = new Load8BitImmIntoHLAddress(z80, mmu);

    instruction.execute();

    verify(mmu).writeByte(z80.get16BitRegisterValue(Register.HL), ANY_MEMORY_BYTE_VALUE);
  }

  @Test public void shouldIncrementProgramCounter() {
    int originalProgramCounter = z80.getProgramCounter();
    when(mmu.readByte(anyInt())).thenReturn(ANY_MEMORY_BYTE_VALUE);
    Instruction instruction = new Load8BitImmIntoHLAddress(z80, mmu);

    instruction.execute();

    assertEquals(originalProgramCounter + 1, z80.getProgramCounter());
  }

  @Test public void shouldUseThreeCyclesAsLastExecutionTime() {
    Instruction instruction = new Load8BitImmIntoHLAddress(z80, mmu);

    instruction.execute();

    assertEquals(3, z80.getLastInstructionExecutionTime());
  }
}
