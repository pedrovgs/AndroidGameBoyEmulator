package com.github.pedrovgs.androidgameboyemulator.core.processor.isa;

import com.github.pedrovgs.androidgameboyemulator.core.processor.GBZ80;
import com.github.pedrovgs.androidgameboyemulator.core.processor.Register;

public class Ret extends Instruction {

  private final int flag;
  private final int condition;

  public Ret(GBZ80 z80, int flag, int condition) {
    super(z80);
    this.flag = flag;
    this.condition = condition;
  }

  @Override public void execute() {
    if ((z80.get8BitRegisterValue(Register.F) & flag) == condition) {
      z80.setLastInstructionExecutionTime(popTwice());
      if (flag != JUMP && condition != JUMP) {
        z80.setLastInstructionExecutionTime(4);
      }
    }
    z80.setLastInstructionExecutionTime(2);
  }
}
