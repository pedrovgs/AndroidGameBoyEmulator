package com.github.pedrovgs.androidgameboyemulator.core.processor.isa;

import com.github.pedrovgs.androidgameboyemulator.core.processor.GBZ80;
import com.github.pedrovgs.androidgameboyemulator.core.processor.Register;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class Load8BitRegisterTest {

  private static final byte ANY_REGISTER_VALUE = 3;

  private GBZ80 z80;

  @Before public void setUp() {
    this.z80 = new GBZ80();
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldNotAccept16BitRegistersAsSource() {
    Instruction load8BitRegister = new Load8BitRegister(z80, Register.A, Register.HL);

    load8BitRegister.execute();
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldNotAccept16BitRegistersAsDestiny() {
    Instruction load8BitRegister = new Load8BitRegister(z80, Register.HL, Register.B);

    load8BitRegister.execute();
  }

  @Test public void shouldLoadSourceRegisterIntoDestinyRegister() {
    z80.set8BitRegisterValue(Register.B, ANY_REGISTER_VALUE);
    Instruction load8BitRegister = new Load8BitRegister(z80, Register.A, Register.B);

    load8BitRegister.execute();

    assertEquals(ANY_REGISTER_VALUE, z80.get8BitRegisterValue(Register.A));
  }

  @Test public void shouldUseOneCycleAsExecutionTime(){
    Instruction load8BitRegister = new Load8BitRegister(z80, Register.A, Register.B);

    load8BitRegister.execute();

    assertEquals(1,z80.getLastInstructionExecutionTime());
  }
}
