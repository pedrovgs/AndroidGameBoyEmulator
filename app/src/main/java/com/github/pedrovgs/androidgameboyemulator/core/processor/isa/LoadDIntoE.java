package com.github.pedrovgs.androidgameboyemulator.core.processor.isa;

import com.github.pedrovgs.androidgameboyemulator.core.processor.GBZ80;

class LoadDIntoE extends Instruction {

  LoadDIntoE(GBZ80 z80) {
    super(z80);
  }

  @Override public void execute() {
    int registerD = z80.getRegisterD();
    z80.setRegisterE(registerD);
    setLastExecutionTime(1);
  }
}
