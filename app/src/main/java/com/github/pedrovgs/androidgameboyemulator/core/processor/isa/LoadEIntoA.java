package com.github.pedrovgs.androidgameboyemulator.core.processor.isa;

import com.github.pedrovgs.androidgameboyemulator.core.processor.GBZ80;

class LoadEIntoA extends Instruction {

  LoadEIntoA(GBZ80 z80) {
    super(z80);
  }

  @Override public void execute() {
    int registerE = z80.getRegisterE();
    z80.setRegisterA(registerE);
    setLastExecutionTime(1);
  }
}
