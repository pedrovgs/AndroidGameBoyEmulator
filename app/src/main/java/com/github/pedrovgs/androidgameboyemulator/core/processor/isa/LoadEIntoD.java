package com.github.pedrovgs.androidgameboyemulator.core.processor.isa;

import com.github.pedrovgs.androidgameboyemulator.core.processor.GBZ80;

class LoadEIntoD extends Instruction {

  LoadEIntoD(GBZ80 z80) {
    super(z80);
  }

  @Override public void execute() {
    int registerE = z80.getRegisterE();
    z80.setRegisterD(registerE);
    setLastExecutionTime(1);
  }
}
