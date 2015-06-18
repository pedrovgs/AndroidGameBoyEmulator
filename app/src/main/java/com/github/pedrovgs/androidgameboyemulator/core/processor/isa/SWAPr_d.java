package com.github.pedrovgs.androidgameboyemulator.core.processor.isa;

import com.github.pedrovgs.androidgameboyemulator.core.mmu.MMU;
import com.github.pedrovgs.androidgameboyemulator.core.processor.GBZ80;

public class SWAPr_d extends Instruction {

  SWAPr_d(GBZ80 z80, MMU mmu) {
    super(z80, mmu);
  }

  @Override public void execute() {
    int registerValue = z80.getRegisterD();
    int newRegisterValue = ((registerValue & 0xF) << 4) | ((registerValue & 0xF0) >> 4);
    z80.setRegisterD(newRegisterValue);
    z80.setRegisterF(z80.getRegisterD() != 0 ? 0 : 0x80);
    setLastExecutionTime(1);
  }
}
