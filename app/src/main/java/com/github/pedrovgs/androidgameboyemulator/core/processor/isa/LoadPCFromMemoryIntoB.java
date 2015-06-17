package com.github.pedrovgs.androidgameboyemulator.core.processor.isa;

import com.github.pedrovgs.androidgameboyemulator.core.mmu.MMU;
import com.github.pedrovgs.androidgameboyemulator.core.processor.GBZ80;

public class LoadPCFromMemoryIntoB extends Instruction {

  LoadPCFromMemoryIntoB(GBZ80 z80, MMU mmu) {
    super(z80, mmu);
  }

  @Override public void execute() {
    int memoryValue = mmu.readByte(z80.getProgramCounter());
    z80.setRegisterB(memoryValue);
    z80.incrementProgramCounter();
    z80.setLastInstructionExecutionTime(2);
  }
}
