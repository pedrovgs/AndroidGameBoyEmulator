package com.github.pedrovgs.androidgameboyemulator.core.processor.isa;

import com.github.pedrovgs.androidgameboyemulator.core.mmu.MMU;
import com.github.pedrovgs.androidgameboyemulator.core.processor.GBZ80;

public class LDAIOn extends Instruction {

  LDAIOn(GBZ80 z80, MMU mmu) {
    super(z80, mmu);
  }

  @Override public void execute() {
    int address = 0xFF00 + mmu.readByte(z80.getProgramCounter());
    int value = mmu.readByte(address);
    z80.setRegisterA(value);
    z80.incrementProgramCounter();
    z80.setLastInstructionExecutionTime(3);
  }
}
