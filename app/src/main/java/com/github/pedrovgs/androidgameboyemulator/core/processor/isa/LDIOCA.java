package com.github.pedrovgs.androidgameboyemulator.core.processor.isa;

import com.github.pedrovgs.androidgameboyemulator.core.mmu.MMU;
import com.github.pedrovgs.androidgameboyemulator.core.processor.GBZ80;

public class LDIOCA extends Instruction {

  LDIOCA(GBZ80 z80, MMU mmu) {
    super(z80, mmu);
  }

  @Override public void execute() {
    int address = 0xFF00 + z80.getRegisterC();
    int value = z80.getRegisterA();
    mmu.writeByte(address, value);
    z80.setLastInstructionExecutionTime(2);
  }
}
