package com.github.pedrovgs.androidgameboyemulator.core.processor.isa;

import com.github.pedrovgs.androidgameboyemulator.core.processor.GBZ80;

class LoadLIntoH extends Instruction {

  LoadLIntoH(GBZ80 z80) {
    super(z80);
  }

  @Override public void execute() {
    int registerL = z80.getRegisterL();
    z80.setRegisterH(registerL);
    setLastExecutionTime(1);
  }
}
