/*
 * Copyright (C) 2015 Pedro Vicente Gomez Sanchez.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.github.pedrovgs.androidgameboyemulator.core.processor;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class GBZ80Test {

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

  @Test public void shouldIncrementProgramCounter() {
    GBZ80 z80 = new GBZ80();
    z80.setProgramCounter(ANY_16BIT_VALUE);

    z80.incrementProgramCounter();

    assertEquals(ANY_16BIT_VALUE + 1, z80.getProgramCounter());
  }

  @Test public void shouldIncrementProgramCounterTwice() {
    GBZ80 z80 = new GBZ80();
    z80.setProgramCounter(ANY_16BIT_VALUE);

    z80.incrementProgramCounterTwice();

    assertEquals(ANY_16BIT_VALUE + 2, z80.getProgramCounter());
  }
}
