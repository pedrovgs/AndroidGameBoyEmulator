package com.github.pedrovgs.androidgameboyemulator.core.processor.isa;

import com.github.pedrovgs.androidgameboyemulator.core.mmu.MMU;
import com.github.pedrovgs.androidgameboyemulator.core.processor.GBZ80;
import com.github.pedrovgs.androidgameboyemulator.core.processor.Register;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class Load8BitHLAddressInto8BitRegisterTest {

  private static final Register ANY_16BIT_DESTINY_REGISTER = Register.AF;
  private static final Register ANY_8BIT_DESTINY_REGISTER = Register.A;
  private static final byte ANY_BYTE_VALUE = 11;

  private GBZ80 z80;
  private MMU mmu;

  @Before public void setUp() {
    this.z80 = new GBZ80();
    this.mmu = mock(MMU.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldNotAccept16BitRegistersAsDestinyRegister() {
    Instruction instruction =
        new Load8BitHLAddressInto8BitRegsiter(z80, mmu, ANY_16BIT_DESTINY_REGISTER);

    instruction.execute();
  }

  @Test public void shouldStoreMemoryValuePointerByHLRegisterIntoTheDestinyRegister() {
    when(mmu.readByte(anyInt())).thenReturn(ANY_BYTE_VALUE);
    Instruction instruction =
        new Load8BitHLAddressInto8BitRegsiter(z80, mmu, ANY_8BIT_DESTINY_REGISTER);

    instruction.execute();

    assertEquals(ANY_BYTE_VALUE, z80.get8BitRegisterValue(ANY_8BIT_DESTINY_REGISTER));
  }

  @Test public void shouldUseTwoCyclesAsExecutionTime() {
    Instruction instruction =
        new Load8BitHLAddressInto8BitRegsiter(z80, mmu, ANY_8BIT_DESTINY_REGISTER);

    instruction.execute();

    assertEquals(2, z80.getLastInstructionExecutionTime());
  }
}
