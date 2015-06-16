package com.github.pedrovgs.androidgameboyemulator.core;

import com.github.pedrovgs.androidgameboyemulator.core.mmu.MMU;
import com.github.pedrovgs.androidgameboyemulator.core.processor.GBZ80;

public class GameBoy {

  private final GBZ80 z80;
  private final MMU mmu;

  public GameBoy(GBZ80 z80, MMU mmu) {
    this.z80 = z80;
    this.mmu = mmu;
  }

  public void startEmulation() {
    while (true) {
      int programCounter = z80.getProgramCounter();
      int instruction = mmu.readByte(programCounter);
      z80.execute(instruction);
      z80.updateProgramCounter();
      z80.updateClock();
    }
  }
}
