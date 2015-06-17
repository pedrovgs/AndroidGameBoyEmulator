package com.github.pedrovgs.androidgameboyemulator.core.processor.isa;

import com.github.pedrovgs.androidgameboyemulator.core.processor.GBZ80;

class LoadHIntoL extends Instruction {

  LoadHIntoL(GBZ80 z80) {
    super(z80);
  }

  @Override public void execute() {
    int registerH = z80.getRegisterH();
    z80.setRegisterL(registerH);
    setLastExecutionTime(1);
  }
}
