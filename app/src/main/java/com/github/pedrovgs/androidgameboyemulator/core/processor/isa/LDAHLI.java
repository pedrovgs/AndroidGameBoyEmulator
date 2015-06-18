package com.github.pedrovgs.androidgameboyemulator.core.processor.isa;

import com.github.pedrovgs.androidgameboyemulator.core.mmu.MMU;
import com.github.pedrovgs.androidgameboyemulator.core.processor.GBZ80;

public class LDAHLI extends Instruction {

  LDAHLI(GBZ80 z80, MMU mmu) {
    super(z80, mmu);
  }

  @Override public void execute() {
    int address = (z80.getRegisterH() << 8) + z80.getRegisterL();
    int memoryValue = mmu.readByte(address);
    z80.setRegisterA(memoryValue);
    int registerValue = (z80.getRegisterL() & 255) + 1;
    z80.setRegisterL(registerValue);
    if (z80.getRegisterL() == 0) {
      int registerHValue = (z80.getRegisterH() + 1) & 255;
    }
    z80.setLastInstructionExecutionTime(2);
  }
}
