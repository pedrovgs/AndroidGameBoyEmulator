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

package com.github.pedrovgs.androidgameboyemulator.core;

import com.github.pedrovgs.androidgameboyemulator.core.gameloader.GameLoader;
import com.github.pedrovgs.androidgameboyemulator.core.gpu.GPU;
import com.github.pedrovgs.androidgameboyemulator.core.gpu.GPUListener;
import com.github.pedrovgs.androidgameboyemulator.core.gpu.VerticalBlankListener;
import com.github.pedrovgs.androidgameboyemulator.core.keypad.Key;
import com.github.pedrovgs.androidgameboyemulator.core.keypad.Keypad;
import com.github.pedrovgs.androidgameboyemulator.core.mmu.MMU;
import com.github.pedrovgs.androidgameboyemulator.core.processor.GBZ80;
import com.github.pedrovgs.androidgameboyemulator.core.processor.isa.Instruction;
import com.github.pedrovgs.androidgameboyemulator.core.processor.isa.InstructionsPool;
import java.io.IOException;

public class GameBoy {

  private static final int BIOS_LIMIT = 0x0100;
  private static final int EXTENDED_OPERATION_CODE = 0xCB;
  private static final int CYCLES_PER_FRAME = 70224;
  private static final int INTERRUPT_FLAG = 0xFF0F;
  private static final int INTERRUPT_ENABLE = 0xFFFF;
  private static final int VBLANK_INTERRUPT_PC = 0x0040;
  private static final int LCD_INTERRUPT_PC = 0x0048;
  private static final int TIMER_INTERRUPT_PC = 0x0050;
  private static final int SERIAL_INTERRUPT_PC = 0x0058;
  private static final int KEYPAD_INTERRUPT_PC = 0x0060;
  private static final byte VBLANK_INTERRUPT = 1;
  private static final byte LCD_INTERRUPT = 2;
  private static final byte TIMER_INTERRUPT = 4;
  private static final byte SERIAL_INTERRUPT = 8;
  private static final byte KEYPAD_INTERRUPT = 0x10;

  private final GBZ80 z80;
  private final MMU mmu;
  private final GPU gpu;
  private final GameLoader gameLoader;
  private final Keypad keypad;
  private final InstructionsPool instructionsPool;

  private String loadedGameUri;
  private int tickCounter;

  public GameBoy(GBZ80 z80, MMU mmu, GPU gpu, GameLoader gameLoader, Keypad keypad) {
    this.z80 = z80;
    this.mmu = mmu;
    this.gpu = gpu;
    this.gameLoader = gameLoader;
    this.keypad = keypad;
    this.instructionsPool = new InstructionsPool(z80, mmu);
    hookListeners();
  }

  public void loadGame(String uri) throws IOException {
    loadedGameUri = uri;
    gameLoader.load(uri, mmu);
  }

  public void start() {
    while (true) {
      tick();
    }
  }

  public void tick() {
    int programCounter = z80.getProgramCounter();
    int instructionCode = mmu.readByte(programCounter) & 0xFF;
    z80.incrementProgramCounter();
    Instruction instruction;
    if (isExtendedInstruction(instructionCode)) {
      int extendedInstructionCode = mmu.readByte(z80.getProgramCounter()) & 0xFF;
      z80.incrementProgramCounter();
      instruction = instructionsPool.getExtendedInstruction(extendedInstructionCode);
    } else {
      instruction = instructionsPool.getNormalInstruction(instructionCode);
    }
    instruction.execute();
    z80.adjustProgramCounter();
    int cyclesElapsed = z80.getLastInstructionExecutionTime();
    gpu.tick(cyclesElapsed);
    if (!mmu.isSystemReady() && z80.getProgramCounter() == BIOS_LIMIT) {
      mmu.setSystemReady(true);
    }
    incrementTickCounter();
    checkInterruptions();
  }

  public void frame() {
    for (int i = 0; i < CYCLES_PER_FRAME; i++) {
      tick();
    }
  }

  public void keyDown(Key key) {
    keypad.keyDown(key);
    sendKeypadInterrupt();
  }

  public void keyUp(Key key) {
    keypad.keyUp(key);
    sendKeypadInterrupt();
  }

  public void reset() throws IOException {
    tickCounter = 0;
    z80.reset();
    mmu.reset();
    gpu.reset();
    if (loadedGameUri != null) {
      loadGame(loadedGameUri);
    }
  }

  public void setGPUListener(GPUListener listener) {
    gpu.setListener(listener);
  }

  private void hookListeners() {
    gpu.setVerticalBlankListener(new VerticalBlankListener() {
      @Override public void onVerticalBlankFinished() {
        if (mmu.isSystemReady()) {
          z80.enableInterruptMasterFlag();
          mmu.writeByte(INTERRUPT_FLAG, VBLANK_INTERRUPT);
          mmu.writeByte(INTERRUPT_ENABLE, VBLANK_INTERRUPT);
        }
      }
    });
  }

  private void incrementTickCounter() {
    tickCounter++;
  }

  private boolean isExtendedInstruction(int instructionCode) {
    return instructionCode == EXTENDED_OPERATION_CODE;
  }

  private void checkInterruptions() {
    if (z80.isInterruptMasterFlageEnabled()) {
      byte interruptFlag = mmu.readByte(INTERRUPT_FLAG);
      byte interruptEnabled = mmu.readByte(INTERRUPT_ENABLE);
      if (interruptFlag != 0) {
        if (isVBlankInterrupt(interruptFlag, interruptEnabled)) {
          handleVBlankInterruption(interruptFlag);
        }
        if (isLCDInterrupt(interruptFlag, interruptEnabled)) {
          handleLCDInterrupt(interruptFlag);
        }
        if (isTimerInterrupt(interruptFlag, interruptEnabled)) {
          handleTimerInterrupt(interruptFlag);
        }
        if (isSerialInterrupt(interruptFlag, interruptEnabled)) {
          handleSerialInterrupt(interruptFlag);
        }
        if (isKeypadInterrupt(interruptFlag, interruptEnabled)) {
          handleKeypadInterrupt(interruptFlag);
        }
      }
    }
  }

  private void handleKeypadInterrupt(byte interruptFlag) {
    z80.disableInterruptMasterFlag();
    pushTwice(z80.getProgramCounter());
    interruptFlag &= 0xEF;
    mmu.writeByte(INTERRUPT_FLAG, interruptFlag);
    z80.setProgramCounter(KEYPAD_INTERRUPT_PC);
  }

  private void handleSerialInterrupt(byte interruptFlag) {
    z80.disableInterruptMasterFlag();
    pushTwice(z80.getProgramCounter());
    interruptFlag &= 0xF7;
    mmu.writeByte(INTERRUPT_FLAG, interruptFlag);
    z80.setProgramCounter(SERIAL_INTERRUPT_PC);
  }

  private void handleTimerInterrupt(byte interruptFlag) {
    z80.disableInterruptMasterFlag();
    pushTwice(z80.getProgramCounter());
    interruptFlag &= 0xFB;
    mmu.writeByte(INTERRUPT_FLAG, interruptFlag);
    z80.setProgramCounter(TIMER_INTERRUPT_PC);
  }

  private void handleLCDInterrupt(byte interruptFlag) {
    z80.disableInterruptMasterFlag();
    pushTwice(z80.getProgramCounter());
    interruptFlag &= 0xFD;
    mmu.writeByte(INTERRUPT_FLAG, interruptFlag);
    z80.setProgramCounter(LCD_INTERRUPT_PC);
  }

  private void handleVBlankInterruption(byte interruptFlag) {
    z80.disableInterruptMasterFlag();
    pushTwice(z80.getProgramCounter());
    interruptFlag &= 0xFE;
    mmu.writeByte(INTERRUPT_FLAG, interruptFlag);
    z80.setProgramCounter(VBLANK_INTERRUPT_PC);
  }

  private boolean isVBlankInterrupt(byte interruptFlag, byte interruptEnabled) {
    return ((interruptEnabled & VBLANK_INTERRUPT) == VBLANK_INTERRUPT) && (
        (interruptFlag & VBLANK_INTERRUPT) == VBLANK_INTERRUPT);
  }

  private boolean isLCDInterrupt(byte interruptFlag, byte interruptEnabled) {
    return ((interruptEnabled & LCD_INTERRUPT) == LCD_INTERRUPT) && ((interruptFlag & LCD_INTERRUPT)
        == LCD_INTERRUPT);
  }

  private boolean isTimerInterrupt(byte interruptFlag, byte interruptEnabled) {
    return ((interruptEnabled & TIMER_INTERRUPT) == TIMER_INTERRUPT) && (
        (interruptFlag & TIMER_INTERRUPT) == TIMER_INTERRUPT);
  }

  private boolean isSerialInterrupt(byte interruptFlag, byte interruptEnabled) {
    return ((interruptEnabled & SERIAL_INTERRUPT) == SERIAL_INTERRUPT) && (
        (interruptFlag & SERIAL_INTERRUPT) == SERIAL_INTERRUPT);
  }

  private boolean isKeypadInterrupt(byte interruptFlag, byte interruptEnabled) {
    return ((interruptEnabled & KEYPAD_INTERRUPT) == KEYPAD_INTERRUPT) && (
        (interruptFlag & KEYPAD_INTERRUPT) == KEYPAD_INTERRUPT);
  }

  private void pushTwice(int data) {
    push((byte) ((data >> 8) & 0xff));
    push((byte) (data & 0xff));
  }

  private void push(byte value) {
    z80.setStackPointer((z80.getStackPointer() - 1) & 0xFFFF);
    mmu.writeByte(z80.getStackPointer(), value);
  }

  private void sendKeypadInterrupt() {
    z80.enableInterruptMasterFlag();
    mmu.writeByte(INTERRUPT_FLAG, KEYPAD_INTERRUPT);
    mmu.writeByte(INTERRUPT_ENABLE, KEYPAD_INTERRUPT);
  }
}
