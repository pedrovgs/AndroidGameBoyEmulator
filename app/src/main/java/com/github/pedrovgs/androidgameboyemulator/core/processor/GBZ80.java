/*
 * Copyright (C) 2015 Pedro Vicente Gomez Sanchez.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.pedrovgs.androidgameboyemulator.core.processor;

import com.github.pedrovgs.androidgameboyemulator.core.mmu.MMU;
import com.github.pedrovgs.androidgameboyemulator.core.processor.isa.Instruction;

public class GBZ80 {

  public static final int ZERO_FLAG_Z = 0x80;
  public static final int SUBSTRACT_FLAG_N = 0x40;
  public static final int HALF_CARRY_FLAG_H = 0x20;
  public static final int CARRY_FLAG_C = 0x10;
  private static final int INITIAL_PROGRAM_COUNTER_VALUE = 0x100;
  private static final int INITIAL_STACK_POINTER_VALUE = 0xFFFE;

  private final Clock clock;
  private final InstructionsPool instructionsPool;

  private byte[] registers;
  private int programCounter;
  private int stackPointer;
  private int lastInstructionExecutionTime;

  public GBZ80() {
    this.clock = new Clock();
    this.instructionsPool = new InstructionsPool();
    this.registers = new byte[8];
    this.programCounter = INITIAL_PROGRAM_COUNTER_VALUE;
    this.stackPointer = INITIAL_STACK_POINTER_VALUE;
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
    int firstPart = registers[registerIndex] << 8 & 0xff00;
    int secondPart = registers[registerIndex + 1] & 0x00ff;
    return firstPart + secondPart;
  }

  public void set16BitRegisterValue(Register register, int value) {
    validate16BitRegister(register);

    byte firstRegisterValue = (byte) (value & 0xff00);
    byte secondRegisterValue = (byte) (value & 0x00ff);
    registers[register.getRegisterIndex()] = firstRegisterValue;
    registers[register.getRegisterIndex() + 1] = secondRegisterValue;
  }

  public void execute(int rawInstruction, MMU mmu) {
    Instruction instruction =
        instructionsPool.getInstructionFromRawValue(rawInstruction, this, mmu);
    instruction.execute();
  }

  public void updateClock() {
    clock.incrementClockM(lastInstructionExecutionTime);
  }

  public int getProgramCounter() {
    return programCounter;
  }

  public int getStackPointer() {
    return stackPointer;
  }

  public void setLastInstructionExecutionTime(int lastInstructionExecutionTime) {
    this.lastInstructionExecutionTime = lastInstructionExecutionTime;
  }

  public int getLastInstructionExecutionTime() {
    return lastInstructionExecutionTime;
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
