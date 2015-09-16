/*
 * Copyright (C) 2015 Pedro Vicente Gomez Sanchez.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.github.pedrovgs.androidgameboyemulator.core.processor;

public class GBZ80 {

  private static final int MAX_PC_VALUE = 65535;
  private static final int INITIAL_REGISTER_VALUE = 0x0;
  private static final int NUMBER_OF_8BIT_REGISTERS = 8;

  private final Clock clock;

  private final byte[] registers;

  private int programCounter;
  private int stackPointer;
  private int lastInstructionExecutionTime;

  private boolean interruptMasterFlag;
  private boolean halt;

  public GBZ80() {
    this.clock = new Clock();
    this.registers = new byte[NUMBER_OF_8BIT_REGISTERS];
    reset();
  }

  public void reset() {
    this.programCounter = INITIAL_REGISTER_VALUE;
    this.stackPointer = INITIAL_REGISTER_VALUE;
    for (int i = 0; i < registers.length; i++) {
      registers[i] = INITIAL_REGISTER_VALUE;
    }
  }

  public byte get8BitRegisterValue(Register register) {
    validate8BitRegister(register);

    return registers[register.getRegisterIndex()];
  }

  public void set8BitRegisterValue(Register register, byte value) {
    validate8BitRegister(register);

    registers[register.getRegisterIndex()] = value;
  }

  public int get16BitRegisterValue(Register register) {
    validate16BitRegister(register);

    int registerIndex = register.getRegisterIndex();
    int firstPart = registers[registerIndex] << 8 & 0xFF00;
    int secondPart = registers[registerIndex + 1] & 0x00FF;
    return firstPart + secondPart;
  }

  public void set16BitRegisterValue(Register register, int value) {
    validate16BitRegister(register);

    byte firstRegisterValue = (byte) (value >> 8 & 0xFF);
    byte secondRegisterValue = (byte) (value & 0xFF);
    registers[register.getRegisterIndex()] = firstRegisterValue;
    registers[register.getRegisterIndex() + 1] = secondRegisterValue;
  }

  public void updateClock() {
    clock.incrementClockM(lastInstructionExecutionTime);
  }

  public int getProgramCounter() {
    return programCounter;
  }

  public void incrementProgramCounter() {
    this.programCounter++;
  }

  public void incrementProgramCounterTwice() {
    this.programCounter = programCounter + 2;
  }

  public void setProgramCounter(int programCounter) {
    this.programCounter = programCounter;
  }

  public void adjustProgramCounter() {
    int programCounter = getProgramCounter();
    setProgramCounter(programCounter & MAX_PC_VALUE);
  }

  public int getStackPointer() {
    return stackPointer;
  }

  public void setStackPointer(int stackPointer) {
    this.stackPointer = stackPointer;
  }

  public void decrementStackPointer() {
    this.stackPointer--;
  }

  public void incrementStackPointer() {
    this.stackPointer++;
  }

  public void setLastInstructionExecutionTime(int lastInstructionExecutionTime) {
    this.lastInstructionExecutionTime = lastInstructionExecutionTime;
  }

  public int getLastInstructionExecutionTime() {
    return lastInstructionExecutionTime;
  }

  public void resetFlagF() {
    set8BitRegisterValue(Register.F, (byte) 0);
  }

  public void disableFlagZ() {
    byte registerF = get8BitRegisterValue(Register.F);
    registerF &= 0x7F;
    set8BitRegisterValue(Register.F, registerF);
  }

  public void disableFlagN() {
    byte registerF = get8BitRegisterValue(Register.F);
    registerF &= 0xBF;
    set8BitRegisterValue(Register.F, registerF);
  }

  public void disableFlagCY() {
    byte registerF = get8BitRegisterValue(Register.F);
    registerF &= 0xEF;
    set8BitRegisterValue(Register.F, registerF);
  }

  public void disableFlagH() {
    byte registerF = get8BitRegisterValue(Register.F);
    registerF &= 0xDF;
    set8BitRegisterValue(Register.F, registerF);
  }

  public void enableFlagZ() {
    byte registerF = get8BitRegisterValue(Register.F);
    registerF |= 0x80;
    set8BitRegisterValue(Register.F, registerF);
  }

  public void enableFlagN() {
    byte registerF = get8BitRegisterValue(Register.F);
    registerF |= 0x40;
    set8BitRegisterValue(Register.F, registerF);
  }

  public void enableFlagCY() {
    byte registerF = get8BitRegisterValue(Register.F);
    registerF |= 0x10;
    set8BitRegisterValue(Register.F, registerF);
  }

  public void enableFlagH() {
    byte registerF = get8BitRegisterValue(Register.F);
    registerF |= 0x20;
    set8BitRegisterValue(Register.F, registerF);
  }

  public boolean isFlagZEnabled() {
    byte flagF = get8BitRegisterValue(Register.F);
    return (flagF & 0x80) == 0x80;
  }

  public boolean isFlagNEnabled() {
    byte flagF = get8BitRegisterValue(Register.F);
    return (flagF & 0x40) == 0x40;
  }

  public boolean isFlagCYEnabled() {
    byte flagF = get8BitRegisterValue(Register.F);
    return (flagF & 0x10) == 0x10;
  }

  public boolean isFlagHEnabled() {
    byte flagF = get8BitRegisterValue(Register.F);
    return (flagF & 0x20) == 0x20;
  }

  public void enableInterruptMasterFlag() {
    this.interruptMasterFlag = true;
  }

  public void disableInterruptMasterFlag() {
    this.interruptMasterFlag = false;
  }

  public void enableHalt() {
    this.halt = true;
  }

  private void validate8BitRegister(Register register) {
    int registerOrdinal = register.ordinal();
    int first16BitRegister = registers.length;
    if (registerOrdinal >= first16BitRegister) {
      throw new IllegalArgumentException(
          "You can't access to a 8 bit register with the register key: " + register);
    }
  }

  private void validate16BitRegister(Register register) {
    int registerOrdinal = register.ordinal();
    int last8BitRegisterOrdinal = registers.length - 1;
    if (registerOrdinal <= last8BitRegisterOrdinal) {
      throw new IllegalArgumentException(
          "You can't access to a 16 bit register with the register key: " + register);
    }
  }
}
