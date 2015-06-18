package com.github.pedrovgs.androidgameboyemulator.core.processor.isa;

import com.github.pedrovgs.androidgameboyemulator.core.mmu.MMU;
import com.github.pedrovgs.androidgameboyemulator.core.processor.GBZ80;

public class LDAIOC extends Instruction {

  LDAIOC(GBZ80 z80, MMU mmu) {
    super(z80, mmu);
  }

  @Override public void execute() {
    int address = 0xFF00 + z80.getProgramCounter();
    int memoryValue = mmu.readByte(address);
    z80.setRegisterA(memoryValue);
    z80.setLastInstructionExecutionTime(2);
  }
}
