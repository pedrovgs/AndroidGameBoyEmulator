package com.github.pedrovgs.androidgameboyemulator.core.processor.isa;

import com.github.pedrovgs.androidgameboyemulator.core.mmu.MMU;
import com.github.pedrovgs.androidgameboyemulator.core.processor.GBZ80;

public class LoadAIntoPCMemory extends Instruction {

  LoadAIntoPCMemory(GBZ80 z80, MMU mmu) {
    super(z80, mmu);
  }

  @Override public void execute() {
    int programCounter = z80.getProgramCounter();
    int address = mmu.readWord(programCounter);
    int registerValue = z80.getRegisterA();
    mmu.writeByte(address, registerValue);
    z80.incrementProgramCounterTwice();
    z80.setLastInstructionExecutionTime(4);
  }
}
