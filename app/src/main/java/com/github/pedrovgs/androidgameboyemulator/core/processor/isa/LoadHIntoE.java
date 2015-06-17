package com.github.pedrovgs.androidgameboyemulator.core.processor.isa;

import com.github.pedrovgs.androidgameboyemulator.core.processor.GBZ80;

class LoadHIntoE extends Instruction {

  LoadHIntoE(GBZ80 z80) {
    super(z80);
  }

  @Override public void execute() {
    int registerH = z80.getRegisterE();
    z80.setRegisterE(registerH);
    setLastExecutionTime(1);
  }
}
