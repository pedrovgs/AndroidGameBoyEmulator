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

  private final byte[] memory = new byte[65536];

  public byte readByte(int address) {
    return memory[address];
  }

  public int readWord(int address) {
    byte firstByte = readByte(address);
    int secondByte = readByte(address + 1) << 8;
    return firstByte + secondByte;
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
    writeByte(0xFF05, (byte) 0x00);
    writeByte(0xFF06, (byte) 0x00);
    writeByte(0xFF07, (byte) 0x00);
    writeByte(0xFF10, (byte) 0x80);
    writeByte(0xFF11, (byte) 0xBF);
    writeByte(0xFF12, (byte) 0xF3);
    writeByte(0xFF14, (byte) 0xBF);
    writeByte(0xFF16, (byte) 0x3F);
    writeByte(0xFF17, (byte) 0x00);
    writeByte(0xFF19, (byte) 0xBF);
    writeByte(0xFF1A, (byte) 0x7F);
    writeByte(0xFF1B, (byte) 0xFF);
    writeByte(0xFF1C, (byte) 0x9F);
    writeByte(0xFF1E, (byte) 0xBF);
    writeByte(0xFF20, (byte) 0xFF);
    writeByte(0xFF21, (byte) 0x00);
    writeByte(0xFF22, (byte) 0x00);
    writeByte(0xFF23, (byte) 0xBF);
    writeByte(0xFF24, (byte) 0x77);
    writeByte(0xFF25, (byte) 0xF3);
    writeByte(0xFF26, (byte) 0xF1);
    writeByte(0xFF40, (byte) 0x91);
    writeByte(0xFF43, (byte) 0x00);
    writeByte(0xFF42, (byte) 0x00);
    writeByte(0xFF45, (byte) 0x00);
    writeByte(0xFF47, (byte) 0xFC);
    writeByte(0xFF48, (byte) 0xFF);
    writeByte(0xFF49, (byte) 0xFF);
    writeByte(0xFF4A, (byte) 0x00);
    writeByte(0xFF4B, (byte) 0x00);
    writeByte(0xFFFF, (byte) 0x00);
  }
}
