package com.github.pedrovgs.androidgameboyemulator.core.processor.isa;

import com.github.pedrovgs.androidgameboyemulator.InstructionTest;
import com.github.pedrovgs.androidgameboyemulator.core.processor.Register;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

public class Load8BitRegisterAIntoHLAddressTest extends InstructionTest {

  @Test public void shouldUseTwoCyclesAsLastInstructionExecutionTime() {
    Instruction instruction = new Load8BitRegisterAIntoHLAddress(z80, mmu);

    instruction.execute();

    assertEquals(2, z80.getLastInstructionExecutionTime());
  }

  @Test public void shouldLoadTheContentOfTheRegisterAIntoTheAddressPointedByTheRegisterHL() {
    z80.set8BitRegisterValue(Register.A, ANY_8BIT_REGISTER_VALUE);
    z80.set16BitRegisterValue(Register.HL, ANY_16BIT_REGISTER_VALUE);
    Instruction instruction = new Load8BitRegisterAIntoHLAddress(z80, mmu);

    instruction.execute();

    verify(mmu).writeByte(ANY_16BIT_REGISTER_VALUE, ANY_8BIT_REGISTER_VALUE);
  }
}