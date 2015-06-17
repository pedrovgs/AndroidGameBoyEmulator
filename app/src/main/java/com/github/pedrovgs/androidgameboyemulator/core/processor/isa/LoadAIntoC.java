package com.github.pedrovgs.androidgameboyemulator.core.processor.isa;

import com.github.pedrovgs.androidgameboyemulator.core.processor.GBZ80;

class LoadAIntoC extends Instruction {

  LoadAIntoC(GBZ80 z80) {
    super(z80);
  }

  @Override public void execute() {
    int registerA = z80.getRegisterA();
    z80.setRegisterC(registerA);
    setLastExecutionTime(1);
  }
}
