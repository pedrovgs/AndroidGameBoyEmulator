package com.github.pedrovgs.androidgameboyemulator.core.processor.isa;

import com.github.pedrovgs.androidgameboyemulator.core.processor.GBZ80;

class LoadCIntoD extends Instruction {

  LoadCIntoD(GBZ80 z80) {
    super(z80);
  }

  @Override public void execute() {
    int registerC = z80.getRegisterC();
    z80.setRegisterD(registerC);
    setLastExecutionTime(1);
  }
}
