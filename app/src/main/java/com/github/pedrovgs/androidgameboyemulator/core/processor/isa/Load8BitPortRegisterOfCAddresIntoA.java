package com.github.pedrovgs.androidgameboyemulator.core.processor.isa;

import com.github.pedrovgs.androidgameboyemulator.core.mmu.MMU;
import com.github.pedrovgs.androidgameboyemulator.core.processor.GBZ80;
import com.github.pedrovgs.androidgameboyemulator.core.processor.Register;

public class Load8BitPortRegisterOfCAddresIntoA extends Instruction {

  Load8BitPortRegisterOfCAddresIntoA(GBZ80 z80, MMU mmu) {
    super(z80, mmu);
  }

  @Override public void execute() {
    int address = (z80.get8BitRegisterValue(Register.C) & 0xFF) + 0xFF00;
    byte memoryValue = mmu.readByte(address);
    z80.set8BitRegisterValue(Register.A, memoryValue);
    z80.setLastInstructionExecutionTime(2);
  }
}
