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
import com.github.pedrovgs.androidgameboyemulator.core.mmu.MMU;
import com.github.pedrovgs.androidgameboyemulator.core.processor.GBZ80;
import com.github.pedrovgs.androidgameboyemulator.core.processor.isa.Instruction;
import com.github.pedrovgs.androidgameboyemulator.core.processor.isa.InstructionsPool;
import java.io.IOException;

public class GameBoy {

  private static final int BIOS_LIMIT = 0x0100;
  private static final int EXTENDED_OPERATION_CODE = 0xCB;
  private static final int CYCLES_PER_FRAME = 70224;

  private final GBZ80 z80;
  private final MMU mmu;
  private final GPU gpu;
  private final GameLoader gameLoader;
  private final InstructionsPool instructionsPool;

  private String loadedGameUri;
  private int tickCounter;

  public GameBoy(GBZ80 z80, MMU mmu, GPU gpu, GameLoader gameLoader) {
    this.z80 = z80;
    this.mmu = mmu;
    this.gpu = gpu;
    this.gameLoader = gameLoader;
    this.instructionsPool = new InstructionsPool(z80, mmu);
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
  }

  public void frame() {
    for (int i = 0; i < CYCLES_PER_FRAME; i++) {
      tick();
    }
  }

  private void incrementTickCounter() {
    tickCounter++;
  }

  private boolean isExtendedInstruction(int instructionCode) {
    return instructionCode == EXTENDED_OPERATION_CODE;
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
}
