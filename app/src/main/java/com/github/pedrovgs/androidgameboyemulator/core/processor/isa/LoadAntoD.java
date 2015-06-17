package com.github.pedrovgs.androidgameboyemulator.core.processor.isa;

import com.github.pedrovgs.androidgameboyemulator.core.processor.GBZ80;

class LoadAntoD extends Instruction {

  LoadAntoD(GBZ80 z80) {
    super(z80);
  }

  @Override public void execute() {
    int registerA = z80.getRegisterA();
    z80.setRegisterD(registerA);
    setLastExecutionTime(1);
  }
}
