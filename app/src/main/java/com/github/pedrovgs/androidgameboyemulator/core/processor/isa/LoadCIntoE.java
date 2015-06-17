package com.github.pedrovgs.androidgameboyemulator.core.processor.isa;

import com.github.pedrovgs.androidgameboyemulator.core.processor.GBZ80;

class LoadCIntoE extends Instruction {

  LoadCIntoE(GBZ80 z80) {
    super(z80);
  }

  @Override public void execute() {
    int registerC = z80.getRegisterC();
    z80.setRegisterE(registerC);
    setLastExecutionTime(1);
  }
}
