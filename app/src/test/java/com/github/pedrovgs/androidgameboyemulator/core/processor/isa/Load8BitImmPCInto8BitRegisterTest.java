package com.github.pedrovgs.androidgameboyemulator.core.processor.isa;

import com.github.pedrovgs.androidgameboyemulator.core.mmu.MMU;
import com.github.pedrovgs.androidgameboyemulator.core.processor.GBZ80;
import com.github.pedrovgs.androidgameboyemulator.core.processor.Register;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class Load8BitImmPCInto8BitRegisterTest {

  private static final Register ANY_8BIT_DESTINY_REGISTER = Register.E;
  private static final Register ANY_16BIT_DESTINY_REGISTER = Register.HL;
  private static final byte ANY_MEMORY_VALUE = 11;

  private GBZ80 z80;
  private MMU mmu;

  @Before public void setUp() {
    this.z80 = new GBZ80();
    this.mmu = mock(MMU.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldNotAccept16BitRegistersAsDestiny() {
    Instruction load8BitImm =
        new Load8BitImmPCInto8BitRegister(z80, mmu, ANY_16BIT_DESTINY_REGISTER);

    load8BitImm.execute();
  }

  @Test public void shouldLoadProgramCounterPlusOneAddressByteIntoTheDestinyRegister() {
    int programCounter = z80.getProgramCounter();
    when(mmu.readByte(programCounter + 1)).thenReturn(ANY_MEMORY_VALUE);
    Instruction load8BitImm =
        new Load8BitImmPCInto8BitRegister(z80, mmu, ANY_8BIT_DESTINY_REGISTER);

    load8BitImm.execute();

    byte destinyRegisterValue = z80.get8BitRegisterValue(ANY_8BIT_DESTINY_REGISTER);
    assertEquals(ANY_MEMORY_VALUE, destinyRegisterValue);
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
