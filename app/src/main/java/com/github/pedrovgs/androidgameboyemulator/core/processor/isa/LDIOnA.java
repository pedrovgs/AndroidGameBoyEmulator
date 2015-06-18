package com.github.pedrovgs.androidgameboyemulator.core.processor.isa;

import com.github.pedrovgs.androidgameboyemulator.core.mmu.MMU;
import com.github.pedrovgs.androidgameboyemulator.core.processor.GBZ80;

public class LDIOnA extends Instruction {

  LDIOnA(GBZ80 z80, MMU mmu) {
    super(z80, mmu);
  }

  @Override public void execute() {
    int value = z80.getRegisterA();
    int address = z80.getProgramCounter();
    mmu.writeByte(address, value);
    z80.incrementProgramCounter();
    z80.setLastInstructionExecutionTime(3);
  }
}
