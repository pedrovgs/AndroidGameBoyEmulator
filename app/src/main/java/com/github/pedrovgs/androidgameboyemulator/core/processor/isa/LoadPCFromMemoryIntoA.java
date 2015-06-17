package com.github.pedrovgs.androidgameboyemulator.core.processor.isa;

import com.github.pedrovgs.androidgameboyemulator.core.mmu.MMU;
import com.github.pedrovgs.androidgameboyemulator.core.processor.GBZ80;

public class LoadPCFromMemoryIntoA extends Instruction {

  LoadPCFromMemoryIntoA(GBZ80 z80, MMU mmu) {
    super(z80, mmu);
  }

  @Override public void execute() {
    int memoryValue = mmu.readByte(z80.getProgramCounter());
    z80.setRegisterA(memoryValue);
    z80.incrementProgramCounter();
    z80.setLastInstructionExecutionTime(2);
  }
}
