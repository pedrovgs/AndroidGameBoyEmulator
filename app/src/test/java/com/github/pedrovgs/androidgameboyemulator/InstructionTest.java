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

package com.github.pedrovgs.androidgameboyemulator;

import com.github.pedrovgs.androidgameboyemulator.core.mmu.MMU;
import com.github.pedrovgs.androidgameboyemulator.core.processor.GBZ80;
import com.github.pedrovgs.androidgameboyemulator.core.processor.Register;
import org.junit.Before;
import org.mockito.Mock;

public class InstructionTest extends UnitTest {

  protected static final byte ANY_MEMORY_BYTE_VALUE = 11;
  protected static final int ANY_MEMORY_WORD_VALUE = 22;
  protected static final Register ANY_8BIT_DESTINY_REGISTER = Register.E;
  protected static final Register ANY_8BIT_SOURCE_REGISTER = Register.B;
  protected static final byte ANY_8BIT_REGISTER_VALUE = (byte) 0x2F;
  protected static final Register ANY_16BIT_DESTINY_REGISTER = Register.AF;
  protected static final Register ANY_16BIT_SOURCE_REGISTER = Register.AF;
  protected static final int ANY_16BIT_REGISTER_VALUE = 23;
  protected static final int ANY_STACK_POINTER_VALUE = 44;
  protected static final int ANY_BIT_VALUE = 1;

  protected GBZ80 z80;
  @Mock protected MMU mmu;

  @Before public void setUpGBZ80Processor() {
    this.z80 = new GBZ80();
    z80.clearRegisters();
  }
}
