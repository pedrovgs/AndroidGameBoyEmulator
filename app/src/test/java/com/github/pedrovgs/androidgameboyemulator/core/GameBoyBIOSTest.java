package com.github.pedrovgs.androidgameboyemulator.core;

import com.github.pedrovgs.androidgameboyemulator.core.gameloader.FakeGameReader;
import com.github.pedrovgs.androidgameboyemulator.core.gameloader.GameLoader;
import com.github.pedrovgs.androidgameboyemulator.core.gpu.GPU;
import com.github.pedrovgs.androidgameboyemulator.core.mmu.MMU;
import com.github.pedrovgs.androidgameboyemulator.core.processor.GBZ80;
import java.io.IOException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GameBoyBIOSTest {

  private static final String ANY_GAME_URI = "AnyGame.gb";
  private static final int FIST_BIOS_STAGE_COMPLITED_PC = 3074;
  private GBZ80 z80;
  private MMU mmu;

  @Test public void shouldInitializeVideoMemoryWithZero() throws IOException {
    GameBoy gameBoy = givenAGameBoy();

    tickUntilFirstBiosStageFinished(gameBoy);

    assertVideoMemoryIsInitializedToZero();
  }

  private GameBoy givenAGameBoy() {
    z80 = new GBZ80();
    mmu = new MMU();
    GPU gpu = new GPU(mmu);
    GameLoader gameLoader = new GameLoader(new FakeGameReader());
    return new GameBoy(z80, mmu, gpu, gameLoader);
  }

  private void tickUntilFirstBiosStageFinished(GameBoy gameBoy) throws IOException {
    gameBoy.loadGame(ANY_GAME_URI);
    while (z80.getProgramCounter() <= FIST_BIOS_STAGE_COMPLITED_PC) {
      gameBoy.tick();
    }
  }

  private void assertVideoMemoryIsInitializedToZero() {
    for (int address = 0x8000; address < 0x9FFF; address++) {
      byte value = mmu.readByte(address);
      assertEquals("Address = " + address + " not initialized to 0", 0, value);
    }
  }
}