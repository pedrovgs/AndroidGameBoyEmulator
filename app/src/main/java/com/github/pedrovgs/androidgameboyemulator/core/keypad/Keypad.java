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

import com.github.pedrovgs.androidgameboyemulator.core.mmu.MMU;

public class Keypad {

  private static final int KEYPAD_ADDRESS = 0xFF00;
  private static final int MAX_REPRESENTABLE_KEY_INDEX = 4;
  private static final int FIRST_COLUMN_MASK = 0x10;
  private static final int SECOND_COLUMN_MASK = 0x20;

  private final MMU mmu;
  private final boolean[] pressedKeys;

  public Keypad(MMU mmu) {
    this.mmu = mmu;
    this.pressedKeys = new boolean[8];
    reset();
  }

  public void reset() {
    mmu.writeByte(KEYPAD_ADDRESS, (byte) 0x0F);
  }

  public void keyUp(Key key) {
    pressedKeys[key.ordinal()] = false;
    updateMMU();
  }

  public void keyDown(Key key) {
    pressedKeys[key.ordinal()] = true;
    updateMMU();
  }

  public boolean isFirstColumnEnabled() {
    return (mmu.readByte(KEYPAD_ADDRESS) & FIRST_COLUMN_MASK) == 0;
  }

  public boolean isSecondColumnEnabled() {
    return (mmu.readByte(KEYPAD_ADDRESS) & SECOND_COLUMN_MASK) == 0;
  }

  private void updateMMU() {
    byte keypadValue = mmu.readByte(KEYPAD_ADDRESS);
    for (int i = 0; i < MAX_REPRESENTABLE_KEY_INDEX; i++) {
      if (shouldEnableButtonAt(i)) {
        keypadValue &= ~(0x1 << i);
      } else if (shouldDisableButtonAt(i)) {
        keypadValue |= (0x1 << i);
      }
    }
    mmu.writeByte(KEYPAD_ADDRESS, keypadValue);
  }

  private boolean shouldEnableButtonAt(int keypadBitIndex) {
    return pressedKeys[keypadBitIndex] || pressedKeys[keypadBitIndex + MAX_REPRESENTABLE_KEY_INDEX];
  }

  private boolean shouldDisableButtonAt(int keypadBitIndex) {
    return !pressedKeys[keypadBitIndex] & !pressedKeys[keypadBitIndex
        + MAX_REPRESENTABLE_KEY_INDEX];
  }
}
