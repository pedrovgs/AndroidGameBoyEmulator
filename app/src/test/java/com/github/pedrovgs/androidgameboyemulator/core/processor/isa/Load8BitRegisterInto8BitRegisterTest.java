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

package com.github.pedrovgs.androidgameboyemulator.core.processor.isa;

import com.github.pedrovgs.androidgameboyemulator.InstructionTest;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class Load8BitRegisterInto8BitRegisterTest extends InstructionTest {

  @Test(expected = IllegalArgumentException.class)
  public void shouldNotAccept16BitRegistersAsSource() {
    Instruction load8BitRegister =
        new Load8BitRegisterInto8BitRegister(z80, ANY_8BIT_DESTINY_REGISTER,
            ANY_16BIT_SOURCE_REGISTER);

    load8BitRegister.execute();
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldNotAccept16BitRegistersAsDestiny() {
    Instruction load8BitRegister =
        new Load8BitRegisterInto8BitRegister(z80, ANY_16BIT_DESTINY_REGISTER,
            ANY_8BIT_SOURCE_REGISTER);

    load8BitRegister.execute();
  }

  @Test public void shouldLoadSourceRegisterIntoDestinyRegister() {
    z80.set8BitRegisterValue(ANY_8BIT_SOURCE_REGISTER, ANY_8BIT_REGISTER_VALUE);
    Instruction load8BitRegister =
        new Load8BitRegisterInto8BitRegister(z80, ANY_8BIT_DESTINY_REGISTER,
            ANY_8BIT_SOURCE_REGISTER);

    load8BitRegister.execute();

    assertEquals(ANY_8BIT_REGISTER_VALUE, z80.get8BitRegisterValue(ANY_8BIT_DESTINY_REGISTER));
  }

  @Test public void shouldUseOneCycleAsExecutionTime() {
    Instruction load8BitRegister =
        new Load8BitRegisterInto8BitRegister(z80, ANY_8BIT_DESTINY_REGISTER,
            ANY_8BIT_SOURCE_REGISTER);

    load8BitRegister.execute();

    assertEquals(1, z80.getLastInstructionExecutionTime());
  }
}
