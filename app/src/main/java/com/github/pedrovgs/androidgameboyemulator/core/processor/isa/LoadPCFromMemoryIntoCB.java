package com.github.pedrovgs.androidgameboyemulator.core.processor.isa;

import com.github.pedrovgs.androidgameboyemulator.core.mmu.MMU;
import com.github.pedrovgs.androidgameboyemulator.core.processor.GBZ80;

class LoadPCFromMemoryIntoCB extends Instruction {

  LoadPCFromMemoryIntoCB(GBZ80 z80, MMU mmu) {
    super(z80, mmu);
  }

  @Override public void execute() {
    int programCounter = z80.getProgramCounter();
    int memoryValue = mmu.readByte(programCounter);
    z80.setRegisterC(memoryValue);
    z80.setRegisterB(programCounter + 1);
    z80.incrementProgramCounterTwice();
    z80.setLastInstructionExecutionTime(3);
  }
}
