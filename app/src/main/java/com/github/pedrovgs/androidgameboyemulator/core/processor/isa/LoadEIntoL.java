package com.github.pedrovgs.androidgameboyemulator.core.processor.isa;

import com.github.pedrovgs.androidgameboyemulator.core.processor.GBZ80;

class LoadEIntoL extends Instruction {

  LoadEIntoL(GBZ80 z80) {
    super(z80);
  }

  @Override public void execute() {
    int registerE = z80.getRegisterE();
    z80.setRegisterL(registerE);
    setLastExecutionTime(1);
  }
}
