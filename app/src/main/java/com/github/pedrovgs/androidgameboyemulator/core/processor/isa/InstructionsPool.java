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

package com.github.pedrovgs.androidgameboyemulator.core.processor.isa;

import com.github.pedrovgs.androidgameboyemulator.core.mmu.MMU;
import com.github.pedrovgs.androidgameboyemulator.core.processor.GBZ80;
import com.github.pedrovgs.androidgameboyemulator.core.processor.Register;

public class InstructionsPool {

  private final Instruction[] normalInstructions;
  private final Instruction[] extendedInstructions;

  public InstructionsPool(GBZ80 z80, MMU mmu) {
    normalInstructions = new Instruction[256];
    extendedInstructions = new Instruction[256];
    generateNormalInstructions(z80, mmu);
    generateExtendedInstructions(z80, mmu);
  }

  public Instruction get(int rawInstruction) {
    return null;
  }

  private void generateNormalInstructions(GBZ80 z80, MMU mmu) {

  }

  private void generateExtendedInstructions(GBZ80 z80, MMU mmu) {
    normalInstructions[0x06] = new Load8BitImmPCInto8BitRegister(z80, mmu, Register.B);
    normalInstructions[0x0E] = new Load8BitImmPCInto8BitRegister(z80, mmu, Register.C);
    normalInstructions[0x16] = new Load8BitImmPCInto8BitRegister(z80, mmu, Register.D);
    normalInstructions[0x1E] = new Load8BitImmPCInto8BitRegister(z80, mmu, Register.E);
    normalInstructions[0x26] = new Load8BitImmPCInto8BitRegister(z80, mmu, Register.H);
    normalInstructions[0x2E] = new Load8BitImmPCInto8BitRegister(z80, mmu, Register.L);
    normalInstructions[0x7F] = new Load8BitRegisterInto8BitRegister(z80, Register.A, Register.A);
    normalInstructions[0x78] = new Load8BitRegisterInto8BitRegister(z80, Register.A, Register.B);
    normalInstructions[0x79] = new Load8BitRegisterInto8BitRegister(z80, Register.A, Register.C);
    normalInstructions[0x7A] = new Load8BitRegisterInto8BitRegister(z80, Register.A, Register.D);
    normalInstructions[0x7B] = new Load8BitRegisterInto8BitRegister(z80, Register.A, Register.E);
    normalInstructions[0x7C] = new Load8BitRegisterInto8BitRegister(z80, Register.A, Register.H);
    normalInstructions[0x7D] = new Load8BitRegisterInto8BitRegister(z80, Register.A, Register.L);
    normalInstructions[0x7E] = new Load8BitHLAddressInto8BitRegister(z80, mmu, Register.A);
    normalInstructions[0x40] = new Load8BitRegisterInto8BitRegister(z80, Register.B, Register.B);
    normalInstructions[0x41] = new Load8BitRegisterInto8BitRegister(z80, Register.B, Register.C);
    normalInstructions[0x42] = new Load8BitRegisterInto8BitRegister(z80, Register.B, Register.D);
    normalInstructions[0x43] = new Load8BitRegisterInto8BitRegister(z80, Register.B, Register.E);
    normalInstructions[0x44] = new Load8BitRegisterInto8BitRegister(z80, Register.B, Register.H);
    normalInstructions[0x45] = new Load8BitRegisterInto8BitRegister(z80, Register.B, Register.L);
    normalInstructions[0x46] = new Load8BitHLAddressInto8BitRegister(z80, mmu, Register.B);
    normalInstructions[0x48] = new Load8BitRegisterInto8BitRegister(z80, Register.C, Register.B);
    normalInstructions[0x49] = new Load8BitRegisterInto8BitRegister(z80, Register.C, Register.C);
    normalInstructions[0x4A] = new Load8BitRegisterInto8BitRegister(z80, Register.C, Register.D);
    normalInstructions[0x4B] = new Load8BitRegisterInto8BitRegister(z80, Register.C, Register.E);
    normalInstructions[0x4C] = new Load8BitRegisterInto8BitRegister(z80, Register.C, Register.H);
    normalInstructions[0x4D] = new Load8BitRegisterInto8BitRegister(z80, Register.C, Register.L);
    normalInstructions[0x4E] = new Load8BitHLAddressInto8BitRegister(z80, mmu, Register.C);
    normalInstructions[0x50] = new Load8BitRegisterInto8BitRegister(z80, Register.D, Register.B);
    normalInstructions[0x51] = new Load8BitRegisterInto8BitRegister(z80, Register.D, Register.C);
    normalInstructions[0x52] = new Load8BitRegisterInto8BitRegister(z80, Register.D, Register.D);
    normalInstructions[0x53] = new Load8BitRegisterInto8BitRegister(z80, Register.D, Register.E);
    normalInstructions[0x54] = new Load8BitRegisterInto8BitRegister(z80, Register.D, Register.H);
    normalInstructions[0x55] = new Load8BitRegisterInto8BitRegister(z80, Register.D, Register.L);
    normalInstructions[0x56] = new Load8BitHLAddressInto8BitRegister(z80, mmu, Register.D);
    normalInstructions[0x58] = new Load8BitRegisterInto8BitRegister(z80, Register.E, Register.B);
    normalInstructions[0x59] = new Load8BitRegisterInto8BitRegister(z80, Register.E, Register.C);
    normalInstructions[0x5A] = new Load8BitRegisterInto8BitRegister(z80, Register.E, Register.E);
    normalInstructions[0x5B] = new Load8BitRegisterInto8BitRegister(z80, Register.E, Register.E);
    normalInstructions[0x5C] = new Load8BitRegisterInto8BitRegister(z80, Register.E, Register.H);
    normalInstructions[0x5D] = new Load8BitRegisterInto8BitRegister(z80, Register.E, Register.L);
    normalInstructions[0x5E] = new Load8BitHLAddressInto8BitRegister(z80, mmu, Register.E);
    normalInstructions[0x60] = new Load8BitRegisterInto8BitRegister(z80, Register.H, Register.B);
    normalInstructions[0x61] = new Load8BitRegisterInto8BitRegister(z80, Register.H, Register.C);
    normalInstructions[0x62] = new Load8BitRegisterInto8BitRegister(z80, Register.H, Register.D);
    normalInstructions[0x63] = new Load8BitRegisterInto8BitRegister(z80, Register.H, Register.E);
    normalInstructions[0x64] = new Load8BitRegisterInto8BitRegister(z80, Register.H, Register.H);
    normalInstructions[0x65] = new Load8BitRegisterInto8BitRegister(z80, Register.H, Register.L);
    normalInstructions[0x66] = new Load8BitHLAddressInto8BitRegister(z80, mmu, Register.H);
    normalInstructions[0x68] = new Load8BitRegisterInto8BitRegister(z80, Register.L, Register.B);
    normalInstructions[0x69] = new Load8BitRegisterInto8BitRegister(z80, Register.L, Register.C);
    normalInstructions[0x6A] = new Load8BitRegisterInto8BitRegister(z80, Register.L, Register.D);
    normalInstructions[0x6B] = new Load8BitRegisterInto8BitRegister(z80, Register.L, Register.E);
    normalInstructions[0x6C] = new Load8BitRegisterInto8BitRegister(z80, Register.L, Register.H);
    normalInstructions[0x6D] = new Load8BitRegisterInto8BitRegister(z80, Register.L, Register.L);
    normalInstructions[0x6E] = new Load8BitHLAddressInto8BitRegister(z80, mmu, Register.L);
    normalInstructions[0x70] = new Load8BitHLAddressInto8BitRegister(z80, mmu, Register.B);
    normalInstructions[0x71] = new Load8BitHLAddressInto8BitRegister(z80, mmu, Register.C);
    normalInstructions[0x72] = new Load8BitHLAddressInto8BitRegister(z80, mmu, Register.D);
    normalInstructions[0x73] = new Load8BitHLAddressInto8BitRegister(z80, mmu, Register.E);
    normalInstructions[0x74] = new Load8BitHLAddressInto8BitRegister(z80, mmu, Register.H);
    normalInstructions[0x75] = new Load8BitHLAddressInto8BitRegister(z80, mmu, Register.L);
    normalInstructions[0x36] = new Load8BitImmIntoHLAddress(z80, mmu);
  }
}
