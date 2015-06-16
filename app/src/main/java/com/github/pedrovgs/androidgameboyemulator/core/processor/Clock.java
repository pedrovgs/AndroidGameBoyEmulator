package com.github.pedrovgs.androidgameboyemulator.core.processor;

class Clock {

  private int clockM;
  private int clockT;

  public void incrementClockM(int clockM, int clockT) {
    this.clockM = clockM;
    this.clockT = clockT;
  }
}
