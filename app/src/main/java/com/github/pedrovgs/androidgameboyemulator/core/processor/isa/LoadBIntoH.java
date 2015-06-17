package com.github.pedrovgs.androidgameboyemulator.core.processor.isa;

import com.github.pedrovgs.androidgameboyemulator.core.processor.GBZ80;

class LoadBIntoH extends Instruction {

  LoadBIntoH(GBZ80 z80) {
    super(z80);
  }

  @Override public void execute() {
    int registerB = z80.getRegisterB();
    z80.setRegisterH(registerB);
    setLastExecutionTime(1);
  }
}
