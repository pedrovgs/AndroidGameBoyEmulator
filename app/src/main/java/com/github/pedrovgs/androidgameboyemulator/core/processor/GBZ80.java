package com.github.pedrovgs.androidgameboyemulator.core.processor;

public class GBZ80 {
  private static final int MASK_16_BITS = 65535;

  private final Clock clock;

  private int programCounter;
  private int stackPointer;

  private int lastInstructionClockM;
  private int lastInstructionClockT;

  public GBZ80() {
    this.clock = new Clock();
  }

  public void execute(int instruction) {

  }

  public void updateProgramCounter() {
    this.programCounter &= MASK_16_BITS;
  }

  public void updateClock() {
    clock.incrementClockM(lastInstructionClockM, lastInstructionClockT);
  }

  public int getProgramCounter() {
    return programCounter;
  }

  public int getLastInstructionClockM() {
    return lastInstructionClockM;
  }

  public int getLastInstructionClockT() {
    return lastInstructionClockT;
  }
}
