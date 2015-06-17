package com.github.pedrovgs.androidgameboyemulator.core.processor.isa;

import com.github.pedrovgs.androidgameboyemulator.core.mmu.MMU;
import com.github.pedrovgs.androidgameboyemulator.core.processor.GBZ80;

public class LoadBIntoHLMemory extends Instruction {

  LoadBIntoHLMemory(GBZ80 z80, MMU mmu) {
    super(z80, mmu);
  }

  @Override public void execute() {
    int address = (z80.getRegisterH() << 8) + z80.getRegisterL();
    int registerValue = z80.getRegisterB();
    mmu.writeByte(address, registerValue);
    z80.setLastInstructionExecutionTime(2);
  }
}
