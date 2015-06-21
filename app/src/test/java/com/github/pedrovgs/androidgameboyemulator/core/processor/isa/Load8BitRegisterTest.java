package com.github.pedrovgs.androidgameboyemulator.core.processor.isa;

import com.github.pedrovgs.androidgameboyemulator.core.processor.GBZ80;
import com.github.pedrovgs.androidgameboyemulator.core.processor.Register;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class Load8BitRegisterTest {

  private static final byte ANY_REGISTER_VALUE = 3;
  private static final Register ANY_8BIT_DESTINY_REGISTER = Register.E;
  private static final Register ANY_8BIT_SOURCE_REGISTER = Register.B;
  private static final Register ANY_16BIT_DESTINY_REGISTER = Register.HL;
  private static final Register ANY_16BIT_SOURCE_REGISTER = Register.HL;

  private GBZ80 z80;

  @Before public void setUp() {
    this.z80 = new GBZ80();
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldNotAccept16BitRegistersAsSource() {
    Instruction load8BitRegister =
        new Load8BitRegister(z80, ANY_8BIT_DESTINY_REGISTER, ANY_16BIT_SOURCE_REGISTER);

    load8BitRegister.execute();
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldNotAccept16BitRegistersAsDestiny() {
    Instruction load8BitRegister =
        new Load8BitRegister(z80, ANY_16BIT_DESTINY_REGISTER, ANY_8BIT_SOURCE_REGISTER);

    load8BitRegister.execute();
  }

  @Test public void shouldLoadSourceRegisterIntoDestinyRegister() {
    z80.set8BitRegisterValue(ANY_8BIT_SOURCE_REGISTER, ANY_REGISTER_VALUE);
    Instruction load8BitRegister =
        new Load8BitRegister(z80, ANY_8BIT_DESTINY_REGISTER, ANY_8BIT_SOURCE_REGISTER);

    load8BitRegister.execute();

    assertEquals(ANY_REGISTER_VALUE, z80.get8BitRegisterValue(ANY_8BIT_DESTINY_REGISTER));
  }

  @Test public void shouldUseOneCycleAsExecutionTime() {
    Instruction load8BitRegister =
        new Load8BitRegister(z80, ANY_8BIT_DESTINY_REGISTER, ANY_8BIT_SOURCE_REGISTER);

    load8BitRegister.execute();

    assertEquals(1, z80.getLastInstructionExecutionTime());
  }
}
