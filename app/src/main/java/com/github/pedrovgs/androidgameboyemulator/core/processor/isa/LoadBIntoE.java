package com.github.pedrovgs.androidgameboyemulator.core.processor.isa;

import com.github.pedrovgs.androidgameboyemulator.core.processor.GBZ80;

class LoadBIntoE extends Instruction {

  LoadBIntoE(GBZ80 z80) {
    super(z80);
  }

  @Override public void execute() {
    int registerB = z80.getRegisterB();
    z80.setRegisterE(registerB);
    setLastExecutionTime(1);
  }
}
