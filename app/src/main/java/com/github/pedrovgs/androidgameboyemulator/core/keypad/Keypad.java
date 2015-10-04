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
import java.util.HashMap;
import java.util.Map;

public class Keypad {

  private static final int KEYPAD_ADDRESS = 0xFF00;

  private final Map<Key, Byte> keyUpValues = new HashMap<Key, Byte>();
  private final Map<Key, Byte> keyDownValues = new HashMap<Key, Byte>();

  private final MMU mmu;

  public Keypad(MMU mmu) {
    this.mmu = mmu;
    initializeKeypadValues();
  }

  public void keyUp(Key key) {
    byte keypadValue = getKeypadUpValue(key);
    mmu.writeByte(KEYPAD_ADDRESS, keypadValue);
  }

  public void keyDown(Key key) {
    byte keypadValue = getKeypadDownValue(key);
    mmu.writeByte(KEYPAD_ADDRESS, keypadValue);
  }

  private byte getKeypadUpValue(Key key) {
    return keyUpValues.get(key);
  }

  private byte getKeypadDownValue(Key key) {
    return keyDownValues.get(key);
  }

  private void initializeKeypadValues() {

  }
}
