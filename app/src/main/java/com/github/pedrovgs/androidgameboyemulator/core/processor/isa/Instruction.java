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
package com.github.pedrovgs.androidgameboyemulator.core.processor.isa;

import com.github.pedrovgs.androidgameboyemulator.core.mmu.MMU;
import com.github.pedrovgs.androidgameboyemulator.core.processor.GBZ80;

public abstract class Instruction {

  protected final GBZ80 z80;
  protected final MMU mmu;

  Instruction(GBZ80 z80) {
    this(z80, null);
  }

  Instruction(GBZ80 z80, MMU mmu) {
    this.z80 = z80;
    this.mmu = mmu;
  }

  public abstract void execute();

  protected void setLastExecutionTime(int duration) {
    z80.setLastInstructionExecutionTime(1);
  }
}
