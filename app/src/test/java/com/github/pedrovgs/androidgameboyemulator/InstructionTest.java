package com.github.pedrovgs.androidgameboyemulator;

import com.github.pedrovgs.androidgameboyemulator.core.mmu.MMU;
import com.github.pedrovgs.androidgameboyemulator.core.processor.GBZ80;
import com.github.pedrovgs.androidgameboyemulator.core.processor.Register;
import org.junit.Before;
import org.mockito.Mock;

public class InstructionTest extends UnitTest {

  protected static final byte ANY_MEMORY_BYTE_VALUE = 11;
  protected static final Register ANY_8BIT_DESTINY_REGISTER = Register.E;
  protected static final Register ANY_8BIT_SOURCE_REGISTER = Register.B;
  protected static final byte ANY_8BIT_REGISTER_VALUE = (byte) 0xFF;
  protected static final Register ANY_16BIT_DESTINY_REGISTER = Register.HL;
  protected static final Register ANY_16BIT_SOURCE_REGISTER = Register.HL;
  protected static final int ANY_16BIT_REGISTER_VALUE = 23;

  protected GBZ80 z80;
  @Mock protected MMU mmu;

  @Before public void setUpGBZ80Processor() {
    this.z80 = new GBZ80();
  }
}
