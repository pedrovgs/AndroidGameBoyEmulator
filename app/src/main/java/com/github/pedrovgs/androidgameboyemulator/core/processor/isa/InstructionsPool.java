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

  private static final int FLAG_Z = 0x80;
  private static final int FLAG_C = 0x10;
  private static final int JMP_NZ = 0;
  private static final int JMP_Z = 0x80;
  private static final int JMP_NC = 0;
  private static final int JMP_C = 0x10;

  private final Instruction[] normalInstructions;
  private final Instruction[] extendedInstructions;

  public InstructionsPool(GBZ80 z80, MMU mmu) {
    normalInstructions = new Instruction[256];
    extendedInstructions = new Instruction[256];
    generateNormalInstructions(z80, mmu);
    generateExtendedInstructions(z80, mmu);
  }

  public Instruction getExtendedInstruction(int operationCode) {
    Instruction instruction = extendedInstructions[operationCode];
    boolean undefinedInstruction = instruction == null;
    if (undefinedInstruction) {
      instruction = new UndefinedInstruction();
    }
    return instruction;
  }

  public Instruction getNormalInstruction(int operationCode) {
    Instruction instruction = normalInstructions[operationCode];
    boolean undefinedInstruction = instruction == null;
    if (undefinedInstruction) {
      instruction = new UndefinedInstruction();
    }
    return instruction;
  }

  private void generateNormalInstructions(GBZ80 z80, MMU mmu) {
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
    normalInstructions[0x0A] = new Load8BitBCAddressIntoA(z80, mmu);
    normalInstructions[0x1A] = new Load8BitDEAddressIntoA(z80, mmu);
    normalInstructions[0x7E] = new Load8BitHLAddressInto8BitRegister(z80, mmu, Register.A);
    normalInstructions[0xFA] = new Load16BitImmPCIntoA(z80, mmu);
    normalInstructions[0x3E] = new Load8BitImmPCInto8BitRegister(z80, mmu, Register.A);
    normalInstructions[0x47] = new Load8BitRegisterInto8BitRegister(z80, Register.B, Register.A);
    normalInstructions[0x4F] = new Load8BitRegisterInto8BitRegister(z80, Register.C, Register.A);
    normalInstructions[0x57] = new Load8BitRegisterInto8BitRegister(z80, Register.D, Register.A);
    normalInstructions[0x5F] = new Load8BitRegisterInto8BitRegister(z80, Register.E, Register.A);
    normalInstructions[0x67] = new Load8BitRegisterInto8BitRegister(z80, Register.H, Register.A);
    normalInstructions[0x6F] = new Load8BitRegisterInto8BitRegister(z80, Register.L, Register.A);
    normalInstructions[0x02] = new Load8BitRegisterAIntoBCAddress(z80, mmu);
    normalInstructions[0x12] = new Load8BitRegisterAIntoDEAddress(z80, mmu);
    normalInstructions[0x77] = new Load8BitRegisterAIntoHLAddress(z80, mmu);
    normalInstructions[0xEA] = new Load8BitRegisterAIntoTheImmPCAddress(z80, mmu);
    normalInstructions[0xF2] = new Load8BitPortRegisterOfCAddressIntoA(z80, mmu);
    normalInstructions[0xE2] = new Load8BitRegisterAIntoAddressPortRegisterOfC(z80, mmu);
    normalInstructions[0x3A] = new LoadHLAddressIntoAAndDecrementHL(z80, mmu);
    normalInstructions[0x32] = new Load8BitRegisterAIntoHLAddressAndDecrementHL(z80, mmu);
    normalInstructions[0x2A] = new LoadHLAddressIntoAAndIncrementHL(z80, mmu);
    normalInstructions[0x22] = new Load8BitRegisterAIntoHLAddressAndIncrementHL(z80, mmu);
    normalInstructions[0xE0] = new Load8BitRegisterAIntoAddressPortRegisterOfC(z80, mmu);
    normalInstructions[0xF0] = new Load8BitPortRegisterOfCAddressIntoA(z80, mmu);
    normalInstructions[0x01] = new Load16BitImmPcInto16BitRegister(z80, mmu, Register.BC);
    normalInstructions[0x11] = new Load16BitImmPcInto16BitRegister(z80, mmu, Register.DE);
    normalInstructions[0x21] = new Load16BitImmPcInto16BitRegister(z80, mmu, Register.HL);
    normalInstructions[0x31] = new Load16BitImmPcIntoStackPointer(z80, mmu);
    normalInstructions[0xF9] = new Load16BitRegisterHLIntoSP(z80);
    normalInstructions[0xF8] = new Load8BitImmPCPlusSPIntoHL(z80, mmu);
    normalInstructions[0x08] = new Load16BitImmPcIntoStackPointer(z80, mmu);
    normalInstructions[0xF5] = new Push16BitRegister(z80, mmu, Register.AF);
    normalInstructions[0xC5] = new Push16BitRegister(z80, mmu, Register.BC);
    normalInstructions[0xD5] = new Push16BitRegister(z80, mmu, Register.DE);
    normalInstructions[0xE5] = new Push16BitRegister(z80, mmu, Register.HL);
    normalInstructions[0xF1] = new Pop16BitRegister(z80, mmu, Register.AF);
    normalInstructions[0xC1] = new Pop16BitRegister(z80, mmu, Register.BC);
    normalInstructions[0xD1] = new Pop16BitRegister(z80, mmu, Register.DE);
    normalInstructions[0xE1] = new Pop16BitRegister(z80, mmu, Register.HL);
    normalInstructions[0x87] = new Add8BitRegisterPlusAIntoA(z80, Register.A);
    normalInstructions[0x80] = new Add8BitRegisterPlusAIntoA(z80, Register.B);
    normalInstructions[0x81] = new Add8BitRegisterPlusAIntoA(z80, Register.C);
    normalInstructions[0x82] = new Add8BitRegisterPlusAIntoA(z80, Register.D);
    normalInstructions[0x83] = new Add8BitRegisterPlusAIntoA(z80, Register.E);
    normalInstructions[0x84] = new Add8BitRegisterPlusAIntoA(z80, Register.H);
    normalInstructions[0x85] = new Add8BitRegisterPlusAIntoA(z80, Register.L);
    normalInstructions[0x86] = new Add8BitAddressHLPlusAIntoA(z80, mmu);
    normalInstructions[0xC6] = new Add8BitImmPCPlusAIntoA(z80, mmu);
    normalInstructions[0x8F] = new Adc8BitRegisterPlusAIntoA(z80, Register.A);
    normalInstructions[0x88] = new Adc8BitRegisterPlusAIntoA(z80, Register.B);
    normalInstructions[0x89] = new Adc8BitRegisterPlusAIntoA(z80, Register.C);
    normalInstructions[0x8A] = new Adc8BitRegisterPlusAIntoA(z80, Register.D);
    normalInstructions[0x8B] = new Adc8BitRegisterPlusAIntoA(z80, Register.E);
    normalInstructions[0x8C] = new Adc8BitRegisterPlusAIntoA(z80, Register.H);
    normalInstructions[0x8D] = new Adc8BitRegisterPlusAIntoA(z80, Register.L);
    normalInstructions[0x8E] = new Adc8BitHLAddressPlusAIntoA(z80, mmu);
    normalInstructions[0xCE] = new Adc8BitImmPCPlusAIntoA(z80, mmu);
    normalInstructions[0x97] = new Sub8BitRegisterToAIntoA(z80, Register.A);
    normalInstructions[0x90] = new Sub8BitRegisterToAIntoA(z80, Register.B);
    normalInstructions[0x91] = new Sub8BitRegisterToAIntoA(z80, Register.C);
    normalInstructions[0x92] = new Sub8BitRegisterToAIntoA(z80, Register.D);
    normalInstructions[0x93] = new Sub8BitRegisterToAIntoA(z80, Register.E);
    normalInstructions[0x94] = new Sub8BitRegisterToAIntoA(z80, Register.H);
    normalInstructions[0x95] = new Sub8BitRegisterToAIntoA(z80, Register.L);
    normalInstructions[0x96] = new Sub8BitAddressHLToAIntoA(z80, mmu);
    normalInstructions[0xD6] = new Sub8BitImmPCToAIntoA(z80, mmu);
    normalInstructions[0x9F] = new Sbc8BitRegisterToAIntoA(z80, Register.A);
    normalInstructions[0x98] = new Sbc8BitRegisterToAIntoA(z80, Register.B);
    normalInstructions[0x99] = new Sbc8BitRegisterToAIntoA(z80, Register.C);
    normalInstructions[0x9A] = new Sbc8BitRegisterToAIntoA(z80, Register.D);
    normalInstructions[0x9B] = new Sbc8BitRegisterToAIntoA(z80, Register.E);
    normalInstructions[0x9C] = new Sbc8BitRegisterToAIntoA(z80, Register.H);
    normalInstructions[0x9D] = new Sbc8BitRegisterToAIntoA(z80, Register.L);
    normalInstructions[0x9E] = new Sbc8BitAddressHLToAIntoA(z80, mmu);
    normalInstructions[0xDE] = new Sbc8BitImmPCToAIntoA(z80, mmu);
    normalInstructions[0xA7] = new And8BitRegisterToAIntoA(z80, Register.A);
    normalInstructions[0xA0] = new And8BitRegisterToAIntoA(z80, Register.B);
    normalInstructions[0xA1] = new And8BitRegisterToAIntoA(z80, Register.C);
    normalInstructions[0xA2] = new And8BitRegisterToAIntoA(z80, Register.D);
    normalInstructions[0xA3] = new And8BitRegisterToAIntoA(z80, Register.E);
    normalInstructions[0xA4] = new And8BitRegisterToAIntoA(z80, Register.H);
    normalInstructions[0xA5] = new And8BitRegisterToAIntoA(z80, Register.L);
    normalInstructions[0xA6] = new And8BitHLAddressToAIntoA(z80, mmu);
    normalInstructions[0xE6] = new And8BitImmPCToAIntoA(z80, mmu);
    normalInstructions[0xB7] = new Or8BitRegisterToAIntoA(z80, Register.A);
    normalInstructions[0xB0] = new Or8BitRegisterToAIntoA(z80, Register.B);
    normalInstructions[0xB1] = new Or8BitRegisterToAIntoA(z80, Register.C);
    normalInstructions[0xB2] = new Or8BitRegisterToAIntoA(z80, Register.D);
    normalInstructions[0xB3] = new Or8BitRegisterToAIntoA(z80, Register.E);
    normalInstructions[0xB4] = new Or8BitRegisterToAIntoA(z80, Register.H);
    normalInstructions[0xB5] = new Or8BitRegisterToAIntoA(z80, Register.L);
    normalInstructions[0xB6] = new Or8BitHLAddressToAIntoA(z80, mmu);
    normalInstructions[0xF6] = new Or8BitImmPCToAIntoA(z80, mmu);
    normalInstructions[0xAF] = new Xor8BitRegisterToAIntoA(z80, Register.A);
    normalInstructions[0xA8] = new Xor8BitRegisterToAIntoA(z80, Register.B);
    normalInstructions[0xA9] = new Xor8BitRegisterToAIntoA(z80, Register.C);
    normalInstructions[0xAA] = new Xor8BitRegisterToAIntoA(z80, Register.D);
    normalInstructions[0xAB] = new Xor8BitRegisterToAIntoA(z80, Register.E);
    normalInstructions[0xAC] = new Xor8BitRegisterToAIntoA(z80, Register.H);
    normalInstructions[0xAD] = new Xor8BitRegisterToAIntoA(z80, Register.L);
    normalInstructions[0xAE] = new Xor8BitHLAddressToAIntoA(z80, mmu);
    normalInstructions[0xEE] = new Xor8BitImmPCToAIntoA(z80, mmu);
    normalInstructions[0xBF] = new Cp8BitRegisterWithA(z80, Register.A);
    normalInstructions[0xB8] = new Cp8BitRegisterWithA(z80, Register.B);
    normalInstructions[0xB9] = new Cp8BitRegisterWithA(z80, Register.C);
    normalInstructions[0xBA] = new Cp8BitRegisterWithA(z80, Register.D);
    normalInstructions[0xBB] = new Cp8BitRegisterWithA(z80, Register.E);
    normalInstructions[0xBC] = new Cp8BitRegisterWithA(z80, Register.H);
    normalInstructions[0xBD] = new Cp8BitRegisterWithA(z80, Register.L);
    normalInstructions[0xBE] = new Cp8BitHLAddressWithA(z80, mmu);
    normalInstructions[0xFE] = new Cp8BitImmPCWithA(z80, mmu);
    normalInstructions[0x3C] = new Inc8BitRegister(z80, Register.A);
    normalInstructions[0x04] = new Inc8BitRegister(z80, Register.B);
    normalInstructions[0x0C] = new Inc8BitRegister(z80, Register.C);
    normalInstructions[0x14] = new Inc8BitRegister(z80, Register.D);
    normalInstructions[0x1C] = new Inc8BitRegister(z80, Register.E);
    normalInstructions[0x24] = new Inc8BitRegister(z80, Register.H);
    normalInstructions[0x2C] = new Inc8BitRegister(z80, Register.L);
    normalInstructions[0x34] = new IncHLAddress(z80, mmu);
    normalInstructions[0x3D] = new Dec8BitRegister(z80, Register.A);
    normalInstructions[0x05] = new Dec8BitRegister(z80, Register.B);
    normalInstructions[0x0D] = new Dec8BitRegister(z80, Register.C);
    normalInstructions[0x15] = new Dec8BitRegister(z80, Register.D);
    normalInstructions[0x1D] = new Dec8BitRegister(z80, Register.E);
    normalInstructions[0x25] = new Dec8BitRegister(z80, Register.L);
    normalInstructions[0x2D] = new Dec8BitRegister(z80, Register.L);
    normalInstructions[0x35] = new DecHLAddress(z80, mmu);
    normalInstructions[0x09] = new Add16BitRegisterToHLIntoHL(z80, Register.BC);
    normalInstructions[0x19] = new Add16BitRegisterToHLIntoHL(z80, Register.DE);
    normalInstructions[0x29] = new Add16BitRegisterToHLIntoHL(z80, Register.HL);
    normalInstructions[0x39] = new Add16BitStackPointerToHLIntoHL(z80);
    normalInstructions[0xE8] = new Add16BitRegisterSPPlus8BitImmPCIntoSP(z80, mmu);
    normalInstructions[0x03] = new Inc16BitRegister(z80, Register.BC);
    normalInstructions[0x13] = new Inc16BitRegister(z80, Register.DE);
    normalInstructions[0x23] = new Inc16BitRegister(z80, Register.HL);
    normalInstructions[0x33] = new Inc16BitStackPointer(z80);
    normalInstructions[0x0B] = new Dec16BitRegister(z80, Register.BC);
    normalInstructions[0x1B] = new Dec16BitRegister(z80, Register.DE);
    normalInstructions[0x2B] = new Dec16BitRegister(z80, Register.HL);
    normalInstructions[0x3B] = new Dec16BitStackPointer(z80);
    normalInstructions[0x27] = new DAA(z80);
    normalInstructions[0x2F] = new CPL(z80);
    normalInstructions[0x3F] = new CCF(z80);
    normalInstructions[0x37] = new SCF(z80);
    normalInstructions[0x00] = new NOP(z80);
    normalInstructions[0x76] = new HALT(z80);
    normalInstructions[0x10] = new STOP(z80);
    normalInstructions[0xF3] = new DI(z80);
    normalInstructions[0xFB] = new EI(z80);
    normalInstructions[0x07] = new RotateLeftCarry8BitRegisterA(z80);
    normalInstructions[0x17] = new RotateLeft8BitRegisterA(z80);
    normalInstructions[0x0F] = new RotateRightCarry8BitRegisterA(z80);
    normalInstructions[0x1F] = new RotateRight8BitRegisterA(z80);
    normalInstructions[0xC3] = new Jump(z80, mmu, GBZ80.JUMP, GBZ80.JUMP);
    normalInstructions[0xC2] = new Jump(z80, mmu, FLAG_Z, JMP_NZ);
    normalInstructions[0xCA] = new Jump(z80, mmu, FLAG_Z, JMP_Z);
    normalInstructions[0xD2] = new Jump(z80, mmu, FLAG_C, JMP_NC);
    normalInstructions[0xDA] = new Jump(z80, mmu, FLAG_C, JMP_C);
    normalInstructions[0xE9] = new JumpToHLAddress(z80, mmu);
    normalInstructions[0x18] = new JumpR(z80, mmu, GBZ80.JUMP, GBZ80.JUMP);
    normalInstructions[0x20] = new JumpR(z80, mmu, FLAG_Z, JMP_NZ);
    normalInstructions[0x28] = new JumpR(z80, mmu, FLAG_Z, JMP_Z);
    normalInstructions[0x30] = new JumpR(z80, mmu, FLAG_C, JMP_NC);
    normalInstructions[0x38] = new JumpR(z80, mmu, FLAG_C, JMP_C);
    normalInstructions[0xCD] = new Call(z80, mmu, GBZ80.JUMP, GBZ80.JUMP);
    normalInstructions[0xC4] = new Call(z80, mmu, FLAG_Z, JMP_NZ);
    normalInstructions[0xCC] = new Call(z80, mmu, FLAG_Z, JMP_Z);
    normalInstructions[0xD4] = new Call(z80, mmu, FLAG_C, JMP_NC);
    normalInstructions[0xDC] = new Call(z80, mmu, FLAG_C, JMP_C);
    normalInstructions[0xC7] = new Rst(z80, mmu, 0x00);
    normalInstructions[0xCF] = new Rst(z80, mmu, 0x08);
    normalInstructions[0xD7] = new Rst(z80, mmu, 0x10);
    normalInstructions[0xDF] = new Rst(z80, mmu, 0x18);
    normalInstructions[0xE7] = new Rst(z80, mmu, 0x20);
    normalInstructions[0xEF] = new Rst(z80, mmu, 0x28);
    normalInstructions[0xF7] = new Rst(z80, mmu, 0x30);
    normalInstructions[0xFF] = new Rst(z80, mmu, 0x38);
    normalInstructions[0xC9] = new Ret(z80, mmu, GBZ80.JUMP, GBZ80.JUMP);
    normalInstructions[0xC0] = new Ret(z80, mmu, FLAG_Z, JMP_NZ);
    normalInstructions[0xC8] = new Ret(z80, mmu, FLAG_Z, JMP_Z);
    normalInstructions[0xD0] = new Ret(z80, mmu, FLAG_C, JMP_NC);
    normalInstructions[0xD8] = new Ret(z80, mmu, FLAG_C, JMP_C);
    normalInstructions[0xD9] = new Reti(z80, mmu);
  }

  private void generateExtendedInstructions(GBZ80 z80, MMU mmu) {
    extendedInstructions[0x37] = new Swap8BitRegister(z80, Register.A);
    extendedInstructions[0x30] = new Swap8BitRegister(z80, Register.B);
    extendedInstructions[0x31] = new Swap8BitRegister(z80, Register.C);
    extendedInstructions[0x32] = new Swap8BitRegister(z80, Register.D);
    extendedInstructions[0x33] = new Swap8BitRegister(z80, Register.E);
    extendedInstructions[0x34] = new Swap8BitRegister(z80, Register.H);
    extendedInstructions[0x35] = new Swap8BitRegister(z80, Register.L);
    extendedInstructions[0x36] = new Swap8BitHLAddress(z80, mmu);
    extendedInstructions[0x07] = new RotateLeft8BitRegisterA(z80);
    extendedInstructions[0x00] = new RotateLeftCarry8BitRegister(z80, Register.B);
    extendedInstructions[0x01] = new RotateLeftCarry8BitRegister(z80, Register.C);
    extendedInstructions[0x02] = new RotateLeftCarry8BitRegister(z80, Register.D);
    extendedInstructions[0x03] = new RotateLeftCarry8BitRegister(z80, Register.E);
    extendedInstructions[0x04] = new RotateLeftCarry8BitRegister(z80, Register.H);
    extendedInstructions[0x05] = new RotateLeftCarry8BitRegister(z80, Register.L);
    extendedInstructions[0x06] = new RotateLeftCarry8BitHLAddress(z80, mmu);
    extendedInstructions[0x17] = new RotateLeft8BitRegisterA(z80);
    extendedInstructions[0x10] = new RotateLeft8BitRegister(z80, Register.B);
    extendedInstructions[0x11] = new RotateLeft8BitRegister(z80, Register.C);
    extendedInstructions[0x12] = new RotateLeft8BitRegister(z80, Register.D);
    extendedInstructions[0x13] = new RotateLeft8BitRegister(z80, Register.E);
    extendedInstructions[0x14] = new RotateLeft8BitRegister(z80, Register.H);
    extendedInstructions[0x15] = new RotateLeft8BitRegister(z80, Register.L);
    extendedInstructions[0x16] = new RotateLeft8BitHLAddress(z80, mmu);
    extendedInstructions[0x0F] = new RotateRightCarry8BitRegisterA(z80);
    extendedInstructions[0x08] = new RotateRightCarry8BitRegister(z80, Register.B);
    extendedInstructions[0x09] = new RotateRightCarry8BitRegister(z80, Register.C);
    extendedInstructions[0x0A] = new RotateRightCarry8BitRegister(z80, Register.D);
    extendedInstructions[0x0B] = new RotateRightCarry8BitRegister(z80, Register.E);
    extendedInstructions[0x0C] = new RotateRightCarry8BitRegister(z80, Register.H);
    extendedInstructions[0x0D] = new RotateRightCarry8BitRegister(z80, Register.L);
    extendedInstructions[0x0E] = new RotateRightCarry8BitHLAddress(z80, mmu);
    extendedInstructions[0x1F] = new RotateRight8BitRegisterA(z80);
    extendedInstructions[0x18] = new RotateRight8BitRegister(z80, Register.B);
    extendedInstructions[0x19] = new RotateRight8BitRegister(z80, Register.C);
    extendedInstructions[0x1A] = new RotateRight8BitRegister(z80, Register.D);
    extendedInstructions[0x1B] = new RotateRight8BitRegister(z80, Register.E);
    extendedInstructions[0x1C] = new RotateRight8BitRegister(z80, Register.H);
    extendedInstructions[0x1D] = new RotateRight8BitRegister(z80, Register.L);
    extendedInstructions[0x1E] = new RotateRight8BitHLAddress(z80, mmu);
    extendedInstructions[0x27] = new SwipeLeft8BitRegister(z80, Register.A);
    extendedInstructions[0x20] = new SwipeLeft8BitRegister(z80, Register.B);
    extendedInstructions[0x21] = new SwipeLeft8BitRegister(z80, Register.C);
    extendedInstructions[0x22] = new SwipeLeft8BitRegister(z80, Register.D);
    extendedInstructions[0x23] = new SwipeLeft8BitRegister(z80, Register.E);
    extendedInstructions[0x24] = new SwipeLeft8BitRegister(z80, Register.H);
    extendedInstructions[0x25] = new SwipeLeft8BitRegister(z80, Register.L);
    extendedInstructions[0x26] = new SwipeLeft8BitHLAddress(z80, mmu);
    extendedInstructions[0x2F] = new SwipeRightA8BitRegister(z80, Register.A);
    extendedInstructions[0x28] = new SwipeRightA8BitRegister(z80, Register.B);
    extendedInstructions[0x29] = new SwipeRightA8BitRegister(z80, Register.C);
    extendedInstructions[0x2A] = new SwipeRightA8BitRegister(z80, Register.D);
    extendedInstructions[0x2B] = new SwipeRightA8BitRegister(z80, Register.E);
    extendedInstructions[0x2C] = new SwipeRightA8BitRegister(z80, Register.H);
    extendedInstructions[0x2D] = new SwipeRightA8BitRegister(z80, Register.L);
    extendedInstructions[0x2E] = new SwipeRightA8BitHLAddress(z80, mmu);
    extendedInstructions[0x3F] = new SwipeRightL8BitRegister(z80, Register.A);
    extendedInstructions[0x38] = new SwipeRightL8BitRegister(z80, Register.B);
    extendedInstructions[0x39] = new SwipeRightL8BitRegister(z80, Register.C);
    extendedInstructions[0x3A] = new SwipeRightL8BitRegister(z80, Register.D);
    extendedInstructions[0x3B] = new SwipeRightL8BitRegister(z80, Register.E);
    extendedInstructions[0x3C] = new SwipeRightL8BitRegister(z80, Register.H);
    extendedInstructions[0x3D] = new SwipeRightL8BitRegister(z80, Register.L);
    extendedInstructions[0x3E] = new SwipeRightL8BitHLAddress(z80, mmu);
    for (int i = 0; i < 4; i++) {
      int bit = 0 + 2 * i;
      extendedInstructions[0x40 + 0x10 * i] = new Bit8BitRegister(z80, bit, Register.B);
      extendedInstructions[0x41 + 0x10 * i] = new Bit8BitRegister(z80, bit, Register.C);
      extendedInstructions[0x42 + 0x10 * i] = new Bit8BitRegister(z80, bit, Register.D);
      extendedInstructions[0x43 + 0x10 * i] = new Bit8BitRegister(z80, bit, Register.E);
      extendedInstructions[0x44 + 0x10 * i] = new Bit8BitRegister(z80, bit, Register.H);
      extendedInstructions[0x45 + 0x10 * i] = new Bit8BitRegister(z80, bit, Register.L);
      extendedInstructions[0x46 + 0x10 * i] = new Bit8BitHLAddress(z80, mmu, bit);
      extendedInstructions[0x47 + 0x10 * i] = new Bit8BitRegister(z80, bit, Register.A);
    }
    for (int i = 0; i < 4; i++) {
      int bit = 1 + 2 * i;
      extendedInstructions[0x48 + 0x10 * i] = new Bit8BitRegister(z80, bit, Register.B);
      extendedInstructions[0x49 + 0x10 * i] = new Bit8BitRegister(z80, bit, Register.C);
      extendedInstructions[0x4A + 0x10 * i] = new Bit8BitRegister(z80, bit, Register.D);
      extendedInstructions[0x4B + 0x10 * i] = new Bit8BitRegister(z80, bit, Register.E);
      extendedInstructions[0x4C + 0x10 * i] = new Bit8BitRegister(z80, bit, Register.H);
      extendedInstructions[0x4D + 0x10 * i] = new Bit8BitRegister(z80, bit, Register.L);
      extendedInstructions[0x4E + 0x10 * i] = new Bit8BitHLAddress(z80, mmu, bit);
      extendedInstructions[0x4F + 0x10 * i] = new Bit8BitRegister(z80, bit, Register.A);
    }
    for (int i = 0; i < 4; i++) {
      int bit = 0 + 2 * i;
      extendedInstructions[0x80 + 0x10 * i] = new Res8BitRegister(z80, bit, Register.B);
      extendedInstructions[0x81 + 0x10 * i] = new Res8BitRegister(z80, bit, Register.C);
      extendedInstructions[0x82 + 0x10 * i] = new Res8BitRegister(z80, bit, Register.D);
      extendedInstructions[0x83 + 0x10 * i] = new Res8BitRegister(z80, bit, Register.E);
      extendedInstructions[0x84 + 0x10 * i] = new Res8BitRegister(z80, bit, Register.H);
      extendedInstructions[0x85 + 0x10 * i] = new Res8BitRegister(z80, bit, Register.L);
      extendedInstructions[0x86 + 0x10 * i] = new Res8BitHLAddress(z80, mmu, bit);
      extendedInstructions[0x87 + 0x10 * i] = new Res8BitRegister(z80, bit, Register.A);
    }
    for (int i = 0; i < 4; i++) {
      int bit = 1 + 2 * i;
      extendedInstructions[0x88 + 0x10 * i] = new Res8BitRegister(z80, bit, Register.B);
      extendedInstructions[0x89 + 0x10 * i] = new Res8BitRegister(z80, bit, Register.C);
      extendedInstructions[0x8A + 0x10 * i] = new Res8BitRegister(z80, bit, Register.D);
      extendedInstructions[0x8B + 0x10 * i] = new Res8BitRegister(z80, bit, Register.E);
      extendedInstructions[0x8C + 0x10 * i] = new Res8BitRegister(z80, bit, Register.H);
      extendedInstructions[0x8D + 0x10 * i] = new Res8BitRegister(z80, bit, Register.L);
      extendedInstructions[0x8E + 0x10 * i] = new Res8BitHLAddress(z80, mmu, bit);
      extendedInstructions[0x8F + 0x10 * i] = new Res8BitRegister(z80, bit, Register.A);
    }
    for (int i = 0; i < 4; i++) {
      int bit = 0 + 2 * i;
      extendedInstructions[0xC0 + 0x10 * i] = new Set8BitRegister(z80, bit, Register.B);
      extendedInstructions[0xC1 + 0x10 * i] = new Set8BitRegister(z80, bit, Register.C);
      extendedInstructions[0xC2 + 0x10 * i] = new Set8BitRegister(z80, bit, Register.D);
      extendedInstructions[0xC3 + 0x10 * i] = new Set8BitRegister(z80, bit, Register.E);
      extendedInstructions[0xC4 + 0x10 * i] = new Set8BitRegister(z80, bit, Register.H);
      extendedInstructions[0xC5 + 0x10 * i] = new Set8BitRegister(z80, bit, Register.L);
      extendedInstructions[0xC6 + 0x10 * i] = new Set8BitHLAddress(z80, mmu, bit);
      extendedInstructions[0xC7 + 0x10 * i] = new Set8BitRegister(z80, bit, Register.A);
    }
    for (int i = 0; i < 4; i++) {
      int bit = 1 + 2 * i;
      extendedInstructions[0xC8 + 0x10 * i] = new Set8BitRegister(z80, bit, Register.B);
      extendedInstructions[0xC9 + 0x10 * i] = new Set8BitRegister(z80, bit, Register.C);
      extendedInstructions[0xCA + 0x10 * i] = new Set8BitRegister(z80, bit, Register.D);
      extendedInstructions[0xCB + 0x10 * i] = new Set8BitRegister(z80, bit, Register.E);
      extendedInstructions[0xCC + 0x10 * i] = new Set8BitRegister(z80, bit, Register.H);
      extendedInstructions[0xCD + 0x10 * i] = new Set8BitRegister(z80, bit, Register.L);
      extendedInstructions[0xCE + 0x10 * i] = new Set8BitHLAddress(z80, mmu, bit);
      extendedInstructions[0xCF + 0x10 * i] = new Set8BitRegister(z80, bit, Register.A);
    }
  }
}
