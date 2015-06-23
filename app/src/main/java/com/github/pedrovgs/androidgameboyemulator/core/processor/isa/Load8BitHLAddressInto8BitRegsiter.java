package com.github.pedrovgs.androidgameboyemulator.core.processor.isa;

import com.github.pedrovgs.androidgameboyemulator.core.mmu.MMU;
import com.github.pedrovgs.androidgameboyemulator.core.processor.GBZ80;
import com.github.pedrovgs.androidgameboyemulator.core.processor.Register;

public class Load8BitHLAddressInto8BitRegsiter extends Instruction {

  private final Register destinyRegister;

  Load8BitHLAddressInto8BitRegsiter(GBZ80 z80, MMU mmu, Register destinyRegister) {
    super(z80, mmu);
    this.destinyRegister = destinyRegister;
  }

  @Override public void execute() {
    int address = z80.get16BitRegisterValue(Register.HL);
    byte memoryValue = mmu.readByte(address);
    z80.set8BitRegisterValue(destinyRegister, memoryValue);
    z80.setLastInstructionExecutionTime(2);
  }
}
