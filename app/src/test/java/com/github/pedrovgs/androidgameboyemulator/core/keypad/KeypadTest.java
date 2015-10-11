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

package com.github.pedrovgs.androidgameboyemulator.core.keypad;

import com.github.pedrovgs.androidgameboyemulator.UnitTest;
import com.github.pedrovgs.androidgameboyemulator.core.mmu.MMU;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class KeypadTest extends UnitTest {

  private static final int KEYPAD_ADDRESS = 0xFF00;

  private MMU mmu;

  @Test public void shouldEnableAButtonOnKeyButtonADown() {
    Keypad keypad = givenAKeypad();

    keypad.keyDown(Key.A);

    assertEquals(0, mmu.readByte(KEYPAD_ADDRESS) & 0x1);
  }

  @Test public void shouldResetAButtonOnKeyButtonAUp() {
    Keypad keypad = givenAKeypad();

    keypad.keyDown(Key.A);
    keypad.keyUp(Key.A);

    assertEquals(1, mmu.readByte(KEYPAD_ADDRESS) & 0x1);
  }

  @Test public void shouldEnableBButtonOnKeyButtonBDown() {
    Keypad keypad = givenAKeypad();

    keypad.keyDown(Key.B);

    assertEquals(0x0, mmu.readByte(KEYPAD_ADDRESS) & 0x2);
  }

  @Test public void shouldResetBButtonOnKeyButtonAUp() {
    Keypad keypad = givenAKeypad();

    keypad.keyDown(Key.B);
    keypad.keyUp(Key.B);

    assertEquals(2, mmu.readByte(KEYPAD_ADDRESS) & 0x2);
  }

  private Keypad givenAKeypad() {
    mmu = new MMU();
    return new Keypad(mmu);
  }
}