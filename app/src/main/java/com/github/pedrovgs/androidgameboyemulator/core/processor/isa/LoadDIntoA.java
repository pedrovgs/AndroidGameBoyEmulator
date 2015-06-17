package com.github.pedrovgs.androidgameboyemulator.core.processor.isa;

import com.github.pedrovgs.androidgameboyemulator.core.processor.GBZ80;

class LoadDIntoA extends Instruction {

  LoadDIntoA(GBZ80 z80) {
    super(z80);
  }

  @Override public void execute() {
    int registerD = z80.getRegisterD();
    z80.setRegisterA(registerD);
    setLastExecutionTime(1);
  }
}
