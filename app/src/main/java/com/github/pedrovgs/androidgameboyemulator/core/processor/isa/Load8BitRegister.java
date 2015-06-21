package com.github.pedrovgs.androidgameboyemulator.core.processor.isa;

import com.github.pedrovgs.androidgameboyemulator.core.processor.GBZ80;
import com.github.pedrovgs.androidgameboyemulator.core.processor.Register;

public class Load8BitRegister extends Instruction {

  private final Register sourceRegister;
  private final Register destinyRegister;

  Load8BitRegister(GBZ80 z80, Register destinyRegister, Register sourceRegister) {
    super(z80);
    this.destinyRegister = destinyRegister;
    this.sourceRegister = sourceRegister;
  }

  @Override public void execute() {
    byte registerValue = z80.get8BitRegisterValue(sourceRegister);
    z80.set8BitRegisterValue(destinyRegister, registerValue);
    z80.setLastInstructionExecutionTime(1);
  }
}
