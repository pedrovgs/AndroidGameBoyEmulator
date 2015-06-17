package com.github.pedrovgs.androidgameboyemulator.core.processor.isa;

import com.github.pedrovgs.androidgameboyemulator.core.processor.GBZ80;

class LoadDIntoD extends Instruction {

  LoadDIntoD(GBZ80 z80) {
    super(z80);
  }

  @Override public void execute() {
    int registerD = z80.getRegisterD();
    z80.setRegisterD(registerD);
    setLastExecutionTime(1);
  }
}
