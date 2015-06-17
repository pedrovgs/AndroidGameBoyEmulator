package com.github.pedrovgs.androidgameboyemulator.core.processor.isa;

import com.github.pedrovgs.androidgameboyemulator.core.mmu.MMU;
import com.github.pedrovgs.androidgameboyemulator.core.processor.GBZ80;

public class LoadPCFromMemoryMemoryIntoA extends Instruction {

  LoadPCFromMemoryMemoryIntoA(GBZ80 z80, MMU mmu) {
    super(z80, mmu);
  }

  @Override public void execute() {
    int programCounter = z80.getProgramCounter();
    int pcMemory = mmu.readWord(programCounter);
    int memoryValue = mmu.readByte(pcMemory);
    z80.setRegisterA(memoryValue);
    z80.incrementProgramCounterTwice();
    z80.setLastInstructionExecutionTime(4);
  }
}
