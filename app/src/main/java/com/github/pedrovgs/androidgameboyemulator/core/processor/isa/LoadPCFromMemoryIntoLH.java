package com.github.pedrovgs.androidgameboyemulator.core.processor.isa;

import com.github.pedrovgs.androidgameboyemulator.core.mmu.MMU;
import com.github.pedrovgs.androidgameboyemulator.core.processor.GBZ80;

class LoadPCFromMemoryIntoLH extends Instruction {

  LoadPCFromMemoryIntoLH(GBZ80 z80, MMU mmu) {
    super(z80, mmu);
  }

  @Override public void execute() {
    int programCounter = z80.getProgramCounter();
    int memoryValue = mmu.readByte(programCounter);
    z80.setRegisterL(memoryValue);
    z80.setRegisterH(programCounter + 1);
    z80.incrementProgramCounterTwice();
    z80.setLastInstructionExecutionTime(3);
  }
}
