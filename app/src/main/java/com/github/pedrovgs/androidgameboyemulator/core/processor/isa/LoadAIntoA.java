package com.github.pedrovgs.androidgameboyemulator.core.processor.isa;

import com.github.pedrovgs.androidgameboyemulator.core.processor.GBZ80;

class LoadAIntoA extends Instruction {

  LoadAIntoA(GBZ80 z80) {
    super(z80);
  }

  @Override public void execute() {
    int registerA = z80.getRegisterA();
    z80.setRegisterA(registerA);
    setLastExecutionTime(1);
  }
}
