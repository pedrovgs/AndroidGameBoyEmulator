package com.github.pedrovgs.androidgameboyemulator.core.processor.isa;

import com.github.pedrovgs.androidgameboyemulator.core.processor.GBZ80;

class LoadLIntoC extends Instruction {

  LoadLIntoC(GBZ80 z80) {
    super(z80);
  }

  @Override public void execute() {
    int registerL = z80.getRegisterL();
    z80.setRegisterC(registerL);
    setLastExecutionTime(1);
  }
}
