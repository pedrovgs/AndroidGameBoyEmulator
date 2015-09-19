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

public class MMU {

  private static final int BIOS_TOP_LIMIT_ADDRESS = 0x100;
  private static final int TOTAL_MEMORY_IN_BYTES = 65536;

  private final byte[] memory;
  private boolean systemReady;

  public MMU() {
    this.memory = new byte[TOTAL_MEMORY_IN_BYTES];
    reset();
  }

  public byte readByte(int address) {
    byte value;
    if (!systemReady && address < BIOS_TOP_LIMIT_ADDRESS) {
      int biosValue = BIOS.ROM[address];
      value = (byte) biosValue;
    } else {
      value = memory[address];
    }
    return value;
  }

  public int readWord(int address) {
    int firstByte = readByte(address) & 0xFF;
    int secondByte = readByte(address + 1) & 0xFF;
    secondByte = secondByte << 8;
    int value = firstByte + secondByte;
    return value;
  }

  public void writeByte(int address, byte value) {
    memory[address] = value;
  }

  public void writeWord(int address, int value) {
    byte firstByte = (byte) (value & 0xFF);
    writeByte(address, firstByte);
    byte secondByte = (byte) (value >> 8);
    writeByte(address + 1, secondByte);
  }

  public void reset() {
    for (int i = 0; i < memory.length; i++) {
      memory[i] = 0;
    }
    systemReady = false;
  }

  public boolean isSystemReady() {
    return systemReady;
  }

  public void setSystemReady(boolean systemReady) {
    this.systemReady = systemReady;
  }
}
