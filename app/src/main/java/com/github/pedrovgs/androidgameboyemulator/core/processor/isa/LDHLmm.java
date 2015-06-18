package com.github.pedrovgs.androidgameboyemulator.core.processor.isa;

import com.github.pedrovgs.androidgameboyemulator.core.mmu.MMU;
import com.github.pedrovgs.androidgameboyemulator.core.processor.GBZ80;

public class LDHLmm extends Instruction {

  LDHLmm(GBZ80 z80, MMU mmu) {
    super(z80, mmu);
  }

  @Override public void execute() {
    int programCounter = z80.getProgramCounter();
    int memoryValue = mmu.readWord(programCounter);
    z80.setRegisterL(memoryValue);
    z80.setRegisterB(memoryValue);
    int secondMemoryValue = mmu.readByte(memoryValue + 1);
    z80.setRegisterH(secondMemoryValue);
    z80.incrementProgramCounterTwice();
    z80.setLastInstructionExecutionTime(5);
  }
}
