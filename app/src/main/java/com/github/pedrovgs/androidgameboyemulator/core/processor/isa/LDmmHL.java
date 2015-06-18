package com.github.pedrovgs.androidgameboyemulator.core.processor.isa;

import com.github.pedrovgs.androidgameboyemulator.core.mmu.MMU;
import com.github.pedrovgs.androidgameboyemulator.core.processor.GBZ80;

public class LDmmHL extends Instruction {

  LDmmHL(GBZ80 z80, MMU mmu) {
    super(z80, mmu);
  }

  @Override public void execute() {
    int programCounter = z80.getProgramCounter();
    int memoryValue = mmu.readWord(programCounter);
    int value = (z80.getRegisterH() << 8) + z80.getRegisterL();
    mmu.writeWord(memoryValue, value);
    z80.incrementProgramCounterTwice();
    z80.setLastInstructionExecutionTime(5);
  }
}
