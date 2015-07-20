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

import static org.junit.Assert.assertEquals;

public class Inc16BitRegisterTest extends InstructionTest {

  @Test public void shouldUseTwoCyclesAsLastInstructionExecutionTime() {
    Instruction instruction = new Inc16BitRegister(z80, ANY_16BIT_DESTINY_REGISTER);

    instruction.execute();

    assertEquals(2, z80.getLastInstructionExecutionTime());
  }

  @Test public void shouldIncrementTheValueOfTheRegisterPassedAsArgument() {
    z80.set16BitRegisterValue(ANY_16BIT_DESTINY_REGISTER, ANY_16BIT_REGISTER_VALUE);
    Instruction instruction = new Inc16BitRegister(z80, ANY_16BIT_DESTINY_REGISTER);

    instruction.execute();

    assertEquals(ANY_16BIT_REGISTER_VALUE + 1,
        z80.get16BitRegisterValue(ANY_16BIT_DESTINY_REGISTER));
  }
}