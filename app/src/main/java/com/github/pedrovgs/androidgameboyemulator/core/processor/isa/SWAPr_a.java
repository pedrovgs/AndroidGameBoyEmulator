package com.github.pedrovgs.androidgameboyemulator.core.processor.isa;

import com.github.pedrovgs.androidgameboyemulator.core.mmu.MMU;
import com.github.pedrovgs.androidgameboyemulator.core.processor.GBZ80;

public class SWAPr_a extends Instruction {

  SWAPr_a(GBZ80 z80, MMU mmu) {
    super(z80, mmu);
  }

  @Override public void execute() {
    int registerValue = z80.getRegisterA();
    int newRegisterValue = ((registerValue & 0xF) << 4) | ((registerValue & 0xF0) >> 4);
    z80.setRegisterA(newRegisterValue);
    z80.setRegisterF(z80.getRegisterA() != 0 ? 0 : 0x80);
    setLastExecutionTime(1);
  }
}
