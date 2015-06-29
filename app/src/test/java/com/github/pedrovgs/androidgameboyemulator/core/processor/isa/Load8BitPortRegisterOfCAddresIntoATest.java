package com.github.pedrovgs.androidgameboyemulator.core.processor.isa;

import com.github.pedrovgs.androidgameboyemulator.InstructionTest;
import com.github.pedrovgs.androidgameboyemulator.core.processor.Register;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class Load8BitPortRegisterOfCAddresIntoATest extends InstructionTest {

  @Test public void shouldLoad8BitPortRegisterOfCAddressIntoA() {
    when(z80.get8BitRegisterValue(Register.C)).thenReturn(ANY_8BIT_REGISTER_VALUE);
    int address = 0XFF00 + (ANY_8BIT_REGISTER_VALUE & 0Xff);
    when(mmu.readByte(address)).thenReturn(ANY_MEMORY_BYTE_VALUE);
    Instruction instruction = new Load8BitPortRegisterOfCAddresIntoA(z80, mmu);

    instruction.execute();

    assertEquals(ANY_MEMORY_BYTE_VALUE, z80.get8BitRegisterValue(Register.A));
  }

  @Test public void shouldUseTwoCyclesAsLastExecutionTime() {
    Instruction instruction = new Load8BitPortRegisterOfCAddresIntoA(z80, mmu);

    instruction.execute();

    assertEquals(2, z80.getLastInstructionExecutionTime());
  }
}