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

  @Test public void shouldLoadProgramCounterPlusOneAddressByteIntoTheDestinyRegister() {
    int programCounter = z80.getProgramCounter();
    when(mmu.readByte(programCounter + 1)).thenReturn(ANY_MEMORY_BYTE_VALUE);
    Instruction load8BitImm =
        new Load8BitImmPCInto8BitRegister(z80, mmu, ANY_8BIT_DESTINY_REGISTER);

    load8BitImm.execute();

    byte destinyRegisterValue = z80.get8BitRegisterValue(ANY_8BIT_DESTINY_REGISTER);
    assertEquals(ANY_MEMORY_BYTE_VALUE, destinyRegisterValue);
  }

  @Test public void shouldIncrementProgramCounterInOneAfterTheInstructionExecution() {
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
