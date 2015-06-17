package com.github.pedrovgs.androidgameboyemulator.core.processor.isa;

import com.github.pedrovgs.androidgameboyemulator.core.processor.GBZ80;

class LoadCIntoL extends Instruction {

  LoadCIntoL(GBZ80 z80) {
    super(z80);
  }

  @Override public void execute() {
    int registerC = z80.getRegisterC();
    z80.setRegisterL(registerC);
    setLastExecutionTime(1);
  }
}
