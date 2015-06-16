package com.github.pedrovgs.androidgameboyemulator.core.processor.isa;

import com.github.pedrovgs.androidgameboyemulator.core.processor.GBZ80;

public abstract class Instruction {

  private final GBZ80 z80;

  public Instruction(GBZ80 z80) {
    this.z80 = z80;
  }

  public abstract void execute();
}
