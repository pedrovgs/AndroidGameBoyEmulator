package com.github.pedrovgs.androidgameboyemulator.core.processor.isa;

import com.github.pedrovgs.androidgameboyemulator.core.mmu.MMU;
import com.github.pedrovgs.androidgameboyemulator.core.processor.GBZ80;

class CallNN extends Instruction {

  CallNN(GBZ80 z80, MMU mmu) {
    super(z80, mmu);
  }

  @Override public void execute() {
    z80.incrementProgramCounterTwice();
    pushTwice(z80.getProgramCounter());
    z80.setLastInstructionExecutionTime(5);
  }
}
