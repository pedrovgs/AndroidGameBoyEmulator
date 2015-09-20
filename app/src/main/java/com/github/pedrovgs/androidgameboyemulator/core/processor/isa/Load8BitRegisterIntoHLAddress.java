package com.github.pedrovgs.androidgameboyemulator.core.processor.isa;

import com.github.pedrovgs.androidgameboyemulator.core.mmu.MMU;
import com.github.pedrovgs.androidgameboyemulator.core.processor.GBZ80;
import com.github.pedrovgs.androidgameboyemulator.core.processor.Register;

class Load8BitRegisterIntoHLAddress extends Instruction {

  private final Register sourceRegister;

  Load8BitRegisterIntoHLAddress(GBZ80 z80, MMU mmu, Register sourceRegister) {
    super(z80, mmu);
    this.sourceRegister = sourceRegister;
  }

  @Override public void execute() {
    byte registerValue = z80.get8BitRegisterValue(sourceRegister);
    int address = z80.get16BitRegisterValue(Register.HL);
    mmu.writeByte(address, registerValue);
    setLastExecutionTime(2);
  }
}
