package com.github.pedrovgs.androidgameboyemulator.core.processor;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class BGZ80Test {

  private static final Register ANY_8BIT_REGISTER = Register.A;
  public static final Register ANY_16BIT_REGISTER = Register.HL;
  private static final byte ANY_8BIT_VALUE = 8;
  private static final int ANY_16BIT_VALUE = 16;
  private static final int INITIAL_PROGRAM_COUNTER_VALUE = 0x100;
  private static final int INITIAL_STACK_POINTER_VALUE = 0xFFFE;

  @Test public void shouldUpdate8BitRegisterValue() {
    GBZ80 z80 = new GBZ80();

    z80.set8BitRegisterValue(ANY_8BIT_REGISTER, ANY_8BIT_VALUE);

    assertEquals(ANY_8BIT_VALUE, z80.get8BitRegisterValue(Register.A));
  }

  @Test public void shouldUpdate16BitRegisterValue() {
    GBZ80 z80 = new GBZ80();

    z80.set16BitRegisterValue(ANY_16BIT_REGISTER, ANY_16BIT_VALUE);

    assertEquals(ANY_16BIT_VALUE, z80.get16BitRegisterValue(Register.HL));
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldNotAccept16BitRegistersToSetA8BitRegisterValue() {
    GBZ80 z80 = new GBZ80();
    z80.set8BitRegisterValue(ANY_16BIT_REGISTER, ANY_8BIT_VALUE);
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldNotAccept16BitRegistersToGetA8BitRegisterValue() {
    GBZ80 z80 = new GBZ80();
    z80.get8BitRegisterValue(ANY_16BIT_REGISTER);
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldNotAccept8BitRegistersToSetA16BitRegisterValue() {
    GBZ80 z80 = new GBZ80();
    z80.set16BitRegisterValue(ANY_8BIT_REGISTER, ANY_16BIT_VALUE);
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldNotAccept8BitRegistersToGetA16BitRegisterValue() {
    GBZ80 z80 = new GBZ80();
    z80.get16BitRegisterValue(ANY_8BIT_REGISTER);
  }

  @Test public void shouldInitializeProgramCounterToTheInitialPCValue() {
    GBZ80 z80 = new GBZ80();

    int programCounter = z80.getProgramCounter();

    assertEquals(INITIAL_PROGRAM_COUNTER_VALUE, programCounter);
  }

  @Test public void shouldInitializeStackPointerToTheInitialSPValue() {
    GBZ80 z80 = new GBZ80();

    int programCounter = z80.getStackPointer();

    assertEquals(INITIAL_STACK_POINTER_VALUE, programCounter);
  }
}