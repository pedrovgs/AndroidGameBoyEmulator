package com.github.pedrovgs.androidgameboyemulator.core.processor;

public enum Register {
  A, B, C, D, E, F, H, L, AF(0), BC(2), DE(4), HL(6);

  private final int registerIndex;

  Register() {
    this.registerIndex = ordinal();
  }

  Register(int registerIndex) {
    this.registerIndex = registerIndex;
  }

  public int getRegisterIndex() {
    return registerIndex;
  }
}
