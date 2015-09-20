package com.github.pedrovgs.androidgameboyemulator.core.processor.isa;

import com.github.pedrovgs.androidgameboyemulator.InstructionTest;
import com.github.pedrovgs.androidgameboyemulator.core.processor.Register;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

public class Load8BitRegisterIntoHLAddressTest extends InstructionTest {

  @Test(expected = IllegalArgumentException.class)
  public void shouldNotAccept16BitRegisterAsSourceRegister() {
    Instruction instruction = new Load8BitRegisterIntoHLAddress(z80, mmu, Register.DE);

    instruction.execute();
  }

  @Test public void shouldUseTwoCyclesAsLastInstructionExecutionTime() {
    Instruction instruction = new Load8BitRegisterIntoHLAddress(z80, mmu, ANY_8BIT_SOURCE_REGISTER);

    instruction.execute();

    assertEquals(2, z80.getLastInstructionExecutionTime());
  }

  @Test
  public void shouldPutTheValueOfTheSourceRegisterIntoTheMemoryAddressPointedByTheRegisterHL() {
    z80.set16BitRegisterValue(Register.HL, ANY_16BIT_REGISTER_VALUE);
    z80.set8BitRegisterValue(ANY_8BIT_SOURCE_REGISTER, ANY_8BIT_REGISTER_VALUE);
    Instruction instruction = new Load8BitRegisterIntoHLAddress(z80, mmu, ANY_8BIT_SOURCE_REGISTER);

    instruction.execute();

    verify(mmu).writeByte(ANY_16BIT_REGISTER_VALUE, ANY_8BIT_REGISTER_VALUE);
  }
}