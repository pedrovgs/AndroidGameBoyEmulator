package com.github.pedrovgs.androidgameboyemulator.core.processor.isa;

import com.github.pedrovgs.androidgameboyemulator.InstructionTest;
import com.github.pedrovgs.androidgameboyemulator.core.processor.Register;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class Load8BitHLAddressInto8BitRegisterTest extends InstructionTest {

  @Test(expected = IllegalArgumentException.class)
  public void shouldNotAccept16BitRegistersAsDestinyRegister() {
    Instruction instruction =
        new Load8BitHLAddressInto8BitRegsiter(z80, mmu, ANY_16BIT_DESTINY_REGISTER);

    instruction.execute();
  }

  @Test public void shouldStoreMemoryValuePointerByHLRegisterIntoTheDestinyRegister() {
    z80.set16BitRegisterValue(Register.HL, ANY_16BIT_REGISTER_VALUE);
    when(mmu.readByte(ANY_16BIT_REGISTER_VALUE)).thenReturn(ANY_MEMORY_BYTE_VALUE);
    Instruction instruction =
        new Load8BitHLAddressInto8BitRegsiter(z80, mmu, ANY_8BIT_DESTINY_REGISTER);

    instruction.execute();

    assertEquals(ANY_MEMORY_BYTE_VALUE, z80.get8BitRegisterValue(ANY_8BIT_DESTINY_REGISTER));
  }

  @Test public void shouldUseTwoCyclesAsExecutionTime() {
    Instruction instruction =
        new Load8BitHLAddressInto8BitRegsiter(z80, mmu, ANY_8BIT_DESTINY_REGISTER);

    instruction.execute();

    assertEquals(2, z80.getLastInstructionExecutionTime());
  }
}
