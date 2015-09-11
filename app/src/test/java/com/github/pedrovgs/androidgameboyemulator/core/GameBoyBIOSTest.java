package com.github.pedrovgs.androidgameboyemulator.core;

import com.github.pedrovgs.androidgameboyemulator.core.gameloader.FakeGameReader;
import com.github.pedrovgs.androidgameboyemulator.core.gameloader.GameLoader;
import com.github.pedrovgs.androidgameboyemulator.core.gpu.GPU;
import com.github.pedrovgs.androidgameboyemulator.core.mmu.MMU;
import com.github.pedrovgs.androidgameboyemulator.core.processor.GBZ80;
import com.github.pedrovgs.androidgameboyemulator.core.processor.Register;
import java.io.IOException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class GameBoyBIOSTest {

  private static final int STACK_POINTER_INITIAL_VALUE = 0xFFFE;
  private static final int FIRST_STAGE_FINISH_PROGRAM_COUNTER = 12;
  private static final int INITIALIZE_AUDIO_STAGE_TICKS = 11;

  private GBZ80 z80;
  private MMU mmu;

  @Test public void shouldInitializeStackPointerToTheDefaultValueInTheFirstTick()
      throws IOException {
    GameBoy gameBoy = givenAGameBoy();

    tickGameBoy(gameBoy, 1);

    assertEquals(STACK_POINTER_INITIAL_VALUE, z80.getStackPointer());
  }

  @Test public void shouldInitializeVideoMemoryWithZero() throws IOException {
    GameBoy gameBoy = givenAGameBoy();
    fillVRAMWithNoise();

    tickUntilFirstBiosStageFinished(gameBoy);

    assertVideoMemoryIsInitializedToZero();
  }

  @Test public void shouldFinisBIOSFirstStageWithAConcreteHLValue() throws IOException {
    GameBoy gameBoy = givenAGameBoy();

    tickUntilFirstBiosStageFinished(gameBoy);

    assertEquals(0X8000 - 1, z80.get16BitRegisterValue(Register.HL));
  }

  @Test public void shouldFinishFirstStageWithTheNextStageProgramCounter() throws IOException {
    GameBoy gameBoy = givenAGameBoy();

    tickUntilFirstBiosStageFinished(gameBoy);

    assertEquals(FIRST_STAGE_FINISH_PROGRAM_COUNTER, z80.getProgramCounter());
  }

  @Test public void shouldNotFinishBIOSExecutionAtTheEndOfTheFirstStage() throws IOException {
    GameBoy gameBoy = givenAGameBoy();

    tickUntilFirstBiosStageFinished(gameBoy);

    assertFalse(mmu.isSystemReady());
  }

  @Test public void shouldInitializeAudioDevice() throws IOException {
    GameBoy gameBoy = givenAGameBoy();

    tickUntilSecondBiosStageFinished(gameBoy);

    assertEquals(18, z80.get8BitRegisterValue(Register.C) & 0xFF);
    assertEquals(0x77, z80.get8BitRegisterValue(Register.A) & 0xFF);
    assertEquals(65316, z80.get16BitRegisterValue(Register.HL));
  }

  private GameBoy givenAGameBoy() {
    z80 = new GBZ80();
    mmu = new MMU();
    GPU gpu = new GPU(mmu);
    GameLoader gameLoader = new GameLoader(new FakeGameReader());
    return new GameBoy(z80, mmu, gpu, gameLoader);
  }

  private void tickGameBoy(GameBoy gameBoy, int numberOfTicks) throws IOException {
    for (int i = 0; i < numberOfTicks; i++) {
      gameBoy.tick();
    }
  }

  private void tickUntilFirstBiosStageFinished(GameBoy gameBoy) throws IOException {
    while (z80.getProgramCounter() <= 0x0A) {
      gameBoy.tick();
    }
  }

  private void tickUntilSecondBiosStageFinished(GameBoy gameBoy) throws IOException {
    tickUntilFirstBiosStageFinished(gameBoy);
    tickGameBoy(gameBoy, INITIALIZE_AUDIO_STAGE_TICKS);
  }

  private void fillVRAMWithNoise() {
    for (int address = 0x8000; address < 0x9FFF; address++) {
      mmu.writeByte(address, (byte) 1);
    }
  }

  private void assertVideoMemoryIsInitializedToZero() {
    for (int address = 0x9FFF; address >= 0x8000; address--) {
      byte value = mmu.readByte(address);
      assertEquals("Address = " + address + " not initialized to 0", 0, value);
    }
  }
}