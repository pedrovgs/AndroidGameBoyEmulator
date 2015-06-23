package com.github.pedrovgs.androidgameboyemulator.core.processor.isa;

import com.github.pedrovgs.androidgameboyemulator.core.mmu.MMU;
import com.github.pedrovgs.androidgameboyemulator.core.processor.GBZ80;
import com.github.pedrovgs.androidgameboyemulator.core.processor.Register;

public class Load8BitImmPCInto8BitRegister extends Instruction {

  private final Register destinyRegister;

  Load8BitImmPCInto8BitRegister(GBZ80 z80, MMU mmu, Register destinyRegister) {
    super(z80, mmu);
    this.destinyRegister = destinyRegister;
  }

  @Override public void execute() {
    int address = z80.getProgramCounter() + 1;
    byte registerValue = mmu.readByte(address);
    z80.set8BitRegisterValue(destinyRegister,registerValue);
    z80.incrementProgramCounter();
    z80.setLastInstructionExecutionTime(2);
  }
}
