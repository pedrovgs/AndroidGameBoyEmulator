package com.github.pedrovgs.androidgameboyemulator.core;

import com.github.pedrovgs.androidgameboyemulator.core.gameloader.FakeGameReader;
import com.github.pedrovgs.androidgameboyemulator.core.gameloader.GameLoader;
import com.github.pedrovgs.androidgameboyemulator.core.gpu.GPU;
import com.github.pedrovgs.androidgameboyemulator.core.mmu.MMU;
import com.github.pedrovgs.androidgameboyemulator.core.processor.GBZ80;
import com.github.pedrovgs.androidgameboyemulator.core.processor.Register;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class GameBoyBIOSTest {

  private static final int STACK_POINTER_INITIAL_VALUE = 0xFFFE;
  private static final int FIRST_STAGE_FINISH_PROGRAM_COUNTER = 12;
  private static final int INITIALIZE_AUDIO_STAGE_TICKS = 11;
  private static final int THIRD_STAGE_FIRST_PC_VALUE = 29;
  private static final String ANY_GAME_URI = "AnyGame.gb";

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

  @Test public void shouldFollowTheSecondStageProgramCounterSequence() throws IOException {
    GameBoy gameBoy = givenAGameBoy();

    tickUntilFirstBiosStageFinished(gameBoy);

    List<Integer> secondStageSequence =
        Arrays.asList(0xC, 0xF, 0x11, 0x13, 0x14, 0x15, 0x16, 0x18, 0x19, 0x1A, 0x1C);
    assertFollowsPCSequence(gameBoy, secondStageSequence);
  }

  @Test public void shouldStartThirdBIOSStageWith29AsProgramCounter() throws IOException {
    GameBoy gameBoy = givenAGameBoy();

    tickUntilSecondBiosStageFinished(gameBoy);

    assertEquals(THIRD_STAGE_FIRST_PC_VALUE, z80.getProgramCounter());
  }

  @Test public void shouldInitializeColorPaletteDuringTheThirdStage() throws IOException {
    GameBoy gameBoy = givenAGameBoy();

    tickUntilSecondBiosStageFinished(gameBoy);
    tickGameBoy(gameBoy, 2);

    assertEquals(0xFC, z80.get8BitRegisterValue(Register.A) & 0xFF);
    assertEquals(0xFC, mmu.readByte(0xFF20) & 0xFF);
  }

  @Test public void shouldPointDERegisterToTheNintendoLogoDuringTheThirdStage() throws IOException {
    GameBoy gameBoy = givenAGameBoy();

    tickUntilSecondBiosStageFinished(gameBoy);
    tickGameBoy(gameBoy, 3);

    assertEquals(0x104, z80.get16BitRegisterValue(Register.DE));
  }

  @Test public void shouldPointHLRegisterToAPortionOfTheVRAMDuringTheThirdStage()
      throws IOException {
    GameBoy gameBoy = givenAGameBoy();

    tickUntilSecondBiosStageFinished(gameBoy);
    tickGameBoy(gameBoy, 4);

    assertEquals(0x8010, z80.get16BitRegisterValue(Register.HL));
  }

  @Test public void shouldLoadAWithTheMemoryValuePointedByDERegisterDuringTheThirdStage()
      throws IOException {
    GameBoy gameBoy = givenAGameBoy();

    tickUntilSecondBiosStageFinished(gameBoy);
    tickGameBoy(gameBoy, 5);

    byte expectedMemoryValue = mmu.readByte(0x104);
    assertEquals(expectedMemoryValue, z80.get8BitRegisterValue(Register.A));
  }

  @Test public void shouldExecuteTwoCallInstructionsDuringTheThirdStage() throws IOException {
    GameBoy gameBoy = givenAGameBoy();

    tickUntilSecondBiosStageFinished(gameBoy);
    tickGameBoy(gameBoy, 5);

    List<Integer> callSequence = Arrays.asList(0x28, 0x2B);
    assertFollowsPCSequence(gameBoy, callSequence);
  }

  @Test public void shouldPrepareTheNintendoLogoLoopDuringTheThirdStage() throws IOException {
    GameBoy gameBoy = givenAGameBoy();

    tickUntilSecondBiosStageFinished(gameBoy);
    tickGameBoy(gameBoy, 7);

    List<Integer> afterCallSequence = Arrays.asList(0x2E, 0x2F, 0x30, 0x32);
    assertFollowsPCSequence(gameBoy, afterCallSequence);
  }

  @Ignore("Ignored until the bios unlocked") @Test public void shouldPutTheNintendoLogoIntoMemory()
      throws IOException {
    GameBoy gameBoy = givenAGameBoy();

    tickUntilBIOSIsLoaded(gameBoy);

    assertNintendoLogoIsLoadedIntoMemory();
  }

  private GameBoy givenAGameBoy() throws IOException {
    z80 = new GBZ80();
    mmu = new MMU();
    GPU gpu = new GPU(mmu);
    GameLoader gameLoader = new GameLoader(new FakeGameReader());
    GameBoy gameBoy = new GameBoy(z80, mmu, gpu, gameLoader);
    gameBoy.loadGame(ANY_GAME_URI);
    return gameBoy;
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

  private void assertFollowsPCSequence(GameBoy gameBoy, List<Integer> pcSequence) {
    for (Integer expectedPC : pcSequence) {
      assertEquals((int) expectedPC, z80.getProgramCounter());
      gameBoy.tick();
    }
  }

  private void tickUntilBIOSIsLoaded(GameBoy gameBoy) {
    while (!mmu.isSystemReady()) {
      gameBoy.tick();
    }
  }

  private void assertNintendoLogoIsLoadedIntoMemory() {
    List<Integer> nintendoLogo =
        Arrays.asList(0xce, 0xed, 0x66, 0x66, 0xcc, 0x0d, 0x00, 0x0b, 0x03, 0x73, 0x00, 0x83, 0x00,
            0x0c, 0x00, 0x0d, 0x00, 0x08, 0x11, 0x1f, 0x88, 0x89, 0x00, 0x0e, 0xdc, 0xcc, 0x6e,
            0xe6, 0xdd, 0xdd, 0xd9, 0x99, 0xbb, 0xbb, 0x67, 0x63, 0x6e, 0x0e, 0xec, 0xcc, 0xdd,
            0xdc, 0x99, 0x9f, 0xbb, 0xb9, 0x33, 0x3e);
    for (int i = 0x104; i < 0x133; i++) {
      assertEquals(nintendoLogo, mmu.readByte(i) & 0xFF);
    }
  }
}
