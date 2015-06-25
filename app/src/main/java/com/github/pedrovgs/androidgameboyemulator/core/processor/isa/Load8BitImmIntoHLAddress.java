package com.github.pedrovgs.androidgameboyemulator.core.processor.isa;

import com.github.pedrovgs.androidgameboyemulator.core.mmu.MMU;
import com.github.pedrovgs.androidgameboyemulator.core.processor.GBZ80;
import com.github.pedrovgs.androidgameboyemulator.core.processor.Register;

public class Load8BitImmIntoHLAddress extends Instruction {

  public Load8BitImmIntoHLAddress(GBZ80 z80, MMU mmu) {
    super(z80, mmu);
  }

  @Override public void execute() {
    byte value = mmu.readByte(z80.getProgramCounter() + 1);
    int address = z80.get16BitRegisterValue(Register.HL);
    mmu.writeByte(address, value);
    z80.incrementProgramCounter();
    z80.setLastInstructionExecutionTime(3);
  }
}
