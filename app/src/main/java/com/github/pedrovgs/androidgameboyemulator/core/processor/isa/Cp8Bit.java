package com.github.pedrovgs.androidgameboyemulator.core.processor.isa;

import com.github.pedrovgs.androidgameboyemulator.core.mmu.MMU;
import com.github.pedrovgs.androidgameboyemulator.core.processor.GBZ80;
import com.github.pedrovgs.androidgameboyemulator.core.processor.Register;

public abstract class Cp8Bit extends Instruction {

  public Cp8Bit(GBZ80 z80, MMU mmu) {
    super(z80, mmu);
  }

  @Override public void execute() {
    byte sourceValue = getValue();
    byte registerAValue = z80.get8BitRegisterValue(Register.A);
    boolean result = registerAValue == sourceValue;
    int lastInstructionExecutionTime = getLastInstructionExecutionTime();
    z80.setLastInstructionExecutionTime(lastInstructionExecutionTime);
    z80.resetFlagF();
    z80.enableFlagN();
    if (result) {
      z80.enableFlagZ();
    } else {
      z80.disableFlagZ();
    }
    if ((registerAValue & 0xF) < (sourceValue & 0xF)) {
      z80.enableFlagH();
    }
    if ((registerAValue & 0xff) < (sourceValue & 0xff)) {
      z80.enableFlagCY();
    }
  }

  protected abstract int getLastInstructionExecutionTime();

  protected abstract byte getValue();
}
