package com.github.pedrovgs.androidgameboyemulator.core.processor.isa;

import com.github.pedrovgs.androidgameboyemulator.core.processor.GBZ80;

class LoadEIntoH extends Instruction {

  LoadEIntoH(GBZ80 z80) {
    super(z80);
  }

  @Override public void execute() {
    int registerE = z80.getRegisterE();
    z80.setRegisterH(registerE);
    setLastExecutionTime(1);
  }
}
