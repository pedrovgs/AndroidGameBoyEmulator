package com.github.pedrovgs.androidgameboyemulator.core.processor.isa;

import com.github.pedrovgs.androidgameboyemulator.core.mmu.MMU;
import com.github.pedrovgs.androidgameboyemulator.core.processor.GBZ80;
import com.github.pedrovgs.androidgameboyemulator.core.processor.Register;

public class Load8BitRegisterAddresIntoHLAddress extends Instruction {

  private final Register sourceRegister;

  Load8BitRegisterAddresIntoHLAddress(GBZ80 z80, MMU mmu, Register sourceRegister) {
    super(z80, mmu);
    this.sourceRegister = sourceRegister;
  }

  @Override public void execute() {
    int registerValue = z80.get8BitRegisterValue(sourceRegister);
    byte memoryValue = mmu.readByte(registerValue);
    int address = z80.get16BitRegisterValue(Register.HL);
    mmu.writeByte(address, memoryValue);
    z80.setLastInstructionExecutionTime(2);
  }
}
