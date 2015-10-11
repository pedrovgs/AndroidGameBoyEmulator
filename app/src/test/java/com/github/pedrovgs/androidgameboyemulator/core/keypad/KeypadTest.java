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

  @Test public void shouldEnableRightButtonOnKeyButtonADown() {
    Keypad keypad = givenAKeypad();

    keypad.keyDown(Key.RIGHT);

    assertEquals(0, mmu.readByte(KEYPAD_ADDRESS) & 0x1);
  }

  @Test public void shouldResetRightButtonOnKeyButtonAUp() {
    Keypad keypad = givenAKeypad();

    keypad.keyDown(Key.RIGHT);
    keypad.keyUp(Key.RIGHT);

    assertEquals(1, mmu.readByte(KEYPAD_ADDRESS) & 0x1);
  }

  @Test public void shouldEnableLeftButtonOnKeyButtonBDown() {
    Keypad keypad = givenAKeypad();

    keypad.keyDown(Key.LEFT);

    assertEquals(0x0, mmu.readByte(KEYPAD_ADDRESS) & 0x2);
  }

  @Test public void shouldResetLeftButtonOnKeyButtonAUp() {
    Keypad keypad = givenAKeypad();

    keypad.keyDown(Key.LEFT);
    keypad.keyUp(Key.LEFT);

    assertEquals(2, mmu.readByte(KEYPAD_ADDRESS) & 0x2);
  }

  @Test public void shouldEnableSelectButtonOnKeyButtonADown() {
    Keypad keypad = givenAKeypad();

    keypad.keyDown(Key.SELECT);

    assertEquals(0, mmu.readByte(KEYPAD_ADDRESS) & 0x4);
  }

  @Test public void shouldResetSelectButtonOnKeyButtonAUp() {
    Keypad keypad = givenAKeypad();

    keypad.keyDown(Key.SELECT);
    keypad.keyUp(Key.SELECT);

    assertEquals(4, mmu.readByte(KEYPAD_ADDRESS) & 0x4);
  }

  @Test public void shouldEnableUpButtonOnKeyButtonBDown() {
    Keypad keypad = givenAKeypad();

    keypad.keyDown(Key.UP);

    assertEquals(0x0, mmu.readByte(KEYPAD_ADDRESS) & 0x4);
  }

  @Test public void shouldResetUpButtonOnKeyButtonAUp() {
    Keypad keypad = givenAKeypad();

    keypad.keyDown(Key.UP);
    keypad.keyUp(Key.UP);

    assertEquals(4, mmu.readByte(KEYPAD_ADDRESS) & 0x4);
  }

  //
  @Test public void shouldEnableStartButtonOnKeyButtonADown() {
    Keypad keypad = givenAKeypad();

    keypad.keyDown(Key.START);

    assertEquals(0, mmu.readByte(KEYPAD_ADDRESS) & 0x8);
  }

  @Test public void shouldResetStartButtonOnKeyButtonAUp() {
    Keypad keypad = givenAKeypad();

    keypad.keyDown(Key.START);
    keypad.keyUp(Key.START);

    assertEquals(8, mmu.readByte(KEYPAD_ADDRESS) & 0x8);
  }

  @Test public void shouldEnableDownButtonOnKeyButtonBDown() {
    Keypad keypad = givenAKeypad();

    keypad.keyDown(Key.DOWN);

    assertEquals(0x0, mmu.readByte(KEYPAD_ADDRESS) & 0x8);
  }

  @Test public void shouldResetDownButtonOnKeyButtonAUp() {
    Keypad keypad = givenAKeypad();

    keypad.keyDown(Key.DOWN);
    keypad.keyUp(Key.DOWN);

    assertEquals(8, mmu.readByte(KEYPAD_ADDRESS) & 0x8);
  }

  @Test public void shouldEnableAllTheBitsRelatedToTheKeypad() {
    Keypad keypad = givenAKeypad();
    pressAllKeysDown(keypad);

    keypad.keyUp(Key.DOWN);
    keypad.keyUp(Key.RIGHT);

    assertEquals(8, mmu.readByte(KEYPAD_ADDRESS) & 0x8);
    assertEquals(1, mmu.readByte(KEYPAD_ADDRESS) & 0x1);
  }

  @Test public void shouldEnableFirstColumnIfAFirstColumnKeyIsDown() {
    Keypad keypad = givenAKeypad();

    keypad.keyDown(Key.A);

    assertEquals(0x10, mmu.readByte(KEYPAD_ADDRESS) & 0x10);
  }

  @Test public void shouldEnableSecondColumnIfAFirstColumnKeyIsDown() {
    Keypad keypad = givenAKeypad();

    keypad.keyDown(Key.LEFT);

    assertEquals(0x20, mmu.readByte(KEYPAD_ADDRESS) & 0x20);
  }

  private void pressAllKeysDown(Keypad keypad) {
    keypad.keyDown(Key.LEFT);
    keypad.keyDown(Key.RIGHT);
    keypad.keyDown(Key.UP);
    keypad.keyDown(Key.DOWN);
    keypad.keyDown(Key.START);
    keypad.keyDown(Key.SELECT);
    keypad.keyDown(Key.B);
    keypad.keyDown(Key.A);
  }

  private Keypad givenAKeypad() {
    mmu = new MMU();
    return new Keypad(mmu);
  }
}