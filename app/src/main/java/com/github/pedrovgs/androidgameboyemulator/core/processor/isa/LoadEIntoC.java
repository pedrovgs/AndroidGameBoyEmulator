package com.github.pedrovgs.androidgameboyemulator.core.processor.isa;

import com.github.pedrovgs.androidgameboyemulator.core.processor.GBZ80;

class LoadEIntoC extends Instruction {

  LoadEIntoC(GBZ80 z80) {
    super(z80);
  }

  @Override public void execute() {
    int registerE = z80.getRegisterE();
    z80.setRegisterC(registerE);
    setLastExecutionTime(1);
  }
}
