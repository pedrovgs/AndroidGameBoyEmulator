package com.github.pedrovgs.androidgameboyemulator.core.processor.isa;

import com.github.pedrovgs.androidgameboyemulator.InstructionTest;
import com.github.pedrovgs.androidgameboyemulator.core.processor.Register;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class Adc8BitImmPCPlusAIntoATest extends InstructionTest {

  @Test public void shouldUseTwoCycleAsLastInstructionExecutionTime() {
    Instruction instruction = new Adc8BitImmPCPlusAIntoA(z80, mmu);

    instruction.execute();

    assertEquals(2, z80.getLastInstructionExecutionTime());
  }

  @Test public void shouldLoadTheContentOfTheImmPCAddressPlusCarryPlusAIntoA() {
    z80.enableFlagCY();
    z80.setProgramCounter(ANY_16BIT_REGISTER_VALUE);
    when(mmu.readByte(ANY_16BIT_REGISTER_VALUE)).thenReturn((byte) 0);
    z80.set8BitRegisterValue(Register.A, (byte) -2);
    Instruction instruction = new Adc8BitImmPCPlusAIntoA(z80, mmu);

    instruction.execute();

    assertEquals(-1, z80.get8BitRegisterValue(Register.A));
  }

  @Test public void shouldEnableFlagZIfTheContentIsZero() {
    z80.enableFlagCY();
    z80.setProgramCounter(ANY_16BIT_REGISTER_VALUE);
    when(mmu.readByte(ANY_16BIT_REGISTER_VALUE)).thenReturn((byte) 1);
    z80.set8BitRegisterValue(Register.A, (byte) -2);
    Instruction instruction = new Adc8BitImmPCPlusAIntoA(z80, mmu);

    instruction.execute();

    assertTrue(z80.isFlagZEnabled());
  }

  @Test public void shouldIncrementProgramCounter() {
    z80.setProgramCounter(ANY_16BIT_REGISTER_VALUE);
    Instruction instruction = new Adc8BitImmPCPlusAIntoA(z80, mmu);

    instruction.execute();

    assertEquals(ANY_16BIT_REGISTER_VALUE + 1, z80.getProgramCounter());
  }
}
