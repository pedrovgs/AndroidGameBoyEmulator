package com.github.pedrovgs.androidgameboyemulator.core.processor.isa;

import com.github.pedrovgs.androidgameboyemulator.core.mmu.MMU;
import com.github.pedrovgs.androidgameboyemulator.core.processor.GBZ80;

public class LDHLSPn extends Instruction {

  LDHLSPn(GBZ80 z80, MMU mmu) {
    super(z80, mmu);
  }

  @Override public void execute() {
    int programCounter = z80.getProgramCounter();
    int memoryValue = mmu.readByte(programCounter);
    if (memoryValue > 127) {
      memoryValue = -((~memoryValue + 1) & 255);
    }
    z80.incrementProgramCounter();
    memoryValue += z80.getStackPointer();
    z80.setRegisterH((memoryValue >> 8) & 255);
    z80.setRegisterL(memoryValue & 255);
    setLastExecutionTime(3);
  }
}
