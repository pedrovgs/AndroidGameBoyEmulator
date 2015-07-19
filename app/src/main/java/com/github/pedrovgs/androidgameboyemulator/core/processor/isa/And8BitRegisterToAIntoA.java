package com.github.pedrovgs.androidgameboyemulator.core.processor.isa;

import com.github.pedrovgs.androidgameboyemulator.core.mmu.MMU;
import com.github.pedrovgs.androidgameboyemulator.core.processor.GBZ80;
import com.github.pedrovgs.androidgameboyemulator.core.processor.Register;

public class And8BitRegisterToAIntoA extends And8Bit {

  private final Register sourceRegister;

  public And8BitRegisterToAIntoA(GBZ80 z80, MMU mmu, Register sourceRegister) {
    super(z80, mmu);
    this.sourceRegister = sourceRegister;
  }

  @Override protected byte getByte() {
    return z80.get8BitRegisterValue(sourceRegister);
  }

  @Override protected int getLastInstructionExecutionTime() {
    return 1;
  }
}
