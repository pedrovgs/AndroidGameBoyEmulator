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

import com.github.pedrovgs.androidgameboyemulator.core.mmu.MMU;
import com.github.pedrovgs.androidgameboyemulator.core.processor.GBZ80;

public abstract class Instruction {

  protected final GBZ80 z80;
  protected final MMU mmu;

  Instruction(GBZ80 z80) {
    this(z80, null);
  }

  Instruction(GBZ80 z80, MMU mmu) {
    this.z80 = z80;
    this.mmu = mmu;
  }

  public abstract void execute();

  protected void setLastExecutionTime(int duration) {
    z80.setLastInstructionExecutionTime(1);
  }

  protected void pushTwice(int data) {
    push((byte) ((data >> 8) & 0xff));
    push((byte) (data & 0xff));
  }

  protected int popTwice() {
    int memoryValue = mmu.readByte(z80.getStackPointer()) & 0xff;
    z80.setStackPointer((z80.getStackPointer() + 1) & 0xffff);
    memoryValue |= ((mmu.readByte(z80.getStackPointer()) & 0xff) << 8);
    z80.setStackPointer((z80.getStackPointer() + 1) & 0xffff);
    return memoryValue;
  }

  private void push(byte value) {
    z80.setStackPointer((z80.getStackPointer() - 1) & 0xffff);
    mmu.writeByte(z80.getStackPointer(), value);
  }
}
