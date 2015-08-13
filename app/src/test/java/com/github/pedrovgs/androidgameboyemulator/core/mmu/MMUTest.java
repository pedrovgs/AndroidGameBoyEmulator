/*
 * Copyright (C) 2015 Pedro Vicente Gomez Sanchez.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.pedrovgs.androidgameboyemulator.core.mmu;

import com.github.pedrovgs.androidgameboyemulator.UnitTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MMUTest extends UnitTest {

  public static final int MMU_SIZE = 65536;
  private static final int ANY_ADDRESS = 11;
  private static final byte ANY_BYTE_VALUE = 0x11;
  private static final int ANY_WORD = 17;

  @Test public void shouldInitializeMMUFullOfZeros() {
    MMU mmu = givenAMMU();

    assertMMUIsFullOfZeros(mmu);
  }

  @Test public void shouldFillTheMMUWithZerosOnReset() {
    MMU mmu = givenAMMU();
    mmu.writeByte(ANY_ADDRESS, ANY_BYTE_VALUE);

    mmu.reset();

    assertMMUIsFullOfZeros(mmu);
  }

  @Test public void shouldWriteByte() {
    MMU mmu = givenAMMU();
    mmu.writeByte(ANY_ADDRESS, ANY_BYTE_VALUE);

    byte byteWritten = mmu.readByte(ANY_ADDRESS);

    assertEquals(ANY_BYTE_VALUE, byteWritten);
  }

  @Test public void shouldWriteWord() {
    MMU mmu = givenAMMU();
    mmu.writeWord(ANY_ADDRESS, ANY_WORD);

    int word = mmu.readWord(ANY_ADDRESS);

    assertEquals(ANY_WORD, word);
  }

  private void assertMMUIsFullOfZeros(MMU mmu) {
    for (int i = 0; i < MMU_SIZE; i++) {
      assertEquals(0, mmu.readByte(i));
    }
  }

  private MMU givenAMMU() {
    return new MMU();
  }
}