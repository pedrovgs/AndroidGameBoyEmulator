package com.github.pedrovgs.androidgameboyemulator.core.processor.isa;

import com.github.pedrovgs.androidgameboyemulator.InstructionTest;
import com.github.pedrovgs.androidgameboyemulator.core.processor.Register;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class Load8BitRegisterAddressIntoHLAddressTest extends InstructionTest {

  @Test(expected = IllegalArgumentException.class)
  public void shouldNotAccept16BitRegistersAsSourceRegister() {
    Instruction instruction =
        new Load8BitRegisterAddressIntoHLAddress(z80, mmu, ANY_16BIT_SOURCE_REGISTER);

    instruction.execute();
  }

  @Test public void shouldStoreTheContentOfTheSourceRegisterIntoTheMemoryPointedByHLRegister() {
    z80.set16BitRegisterValue(Register.HL, ANY_16BIT_REGISTER_VALUE);
    when(mmu.readByte(ANY_16BIT_REGISTER_VALUE)).thenReturn(ANY_MEMORY_BYTE_VALUE);
    int hlRegisterValue = z80.get16BitRegisterValue(Register.HL);
    Instruction instruction =
        new Load8BitRegisterAddressIntoHLAddress(z80, mmu, ANY_8BIT_SOURCE_REGISTER);

    instruction.execute();

    verify(mmu).writeByte(hlRegisterValue, ANY_MEMORY_BYTE_VALUE);
  }

  @Test public void shouldUseTwoCyclesAsExecutionTime() {
    Instruction instruction =
        new Load8BitRegisterAddressIntoHLAddress(z80, mmu, ANY_8BIT_SOURCE_REGISTER);

    instruction.execute();

    assertEquals(2, z80.getLastInstructionExecutionTime());
  }
}
