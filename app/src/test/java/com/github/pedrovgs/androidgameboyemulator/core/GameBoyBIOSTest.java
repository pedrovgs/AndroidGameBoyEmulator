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
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GameBoyBIOSTest {

  private static final int STACK_POINTER_INITIAL_VALUE = 0xFFFE;
  private static final int FIRST_STAGE_FINISH_PROGRAM_COUNTER = 12;
  private static final int INITIALIZE_AUDIO_STAGE_TICKS = 11;
  private static final int THIRD_STAGE_FIRST_PC_VALUE = 29;
  private static final String ANY_GAME_URI = "AnyGame.gb";
  private static final int VRAM_BOTTOM_ADDRESS = 0X8000;
  private static final int COLOR_PALETTE_ADDRESS = 0xFF47;
  private static final int NINTENDO_LOGO_ADDRESS_IN_CARTRIDGE = 0x104;
  private static final int UNLOCK_ROM_ADDRESS = 0xFF50;
  private static final int VRAM_TOP_ADDRESS = 0x9FFF;

  private GBZ80 z80;
  private MMU mmu;
  private GPU gpu;

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

    assertEquals(VRAM_BOTTOM_ADDRESS - 1, z80.get16BitRegisterValue(Register.HL));
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
    assertEquals(0xFC, mmu.readByte(COLOR_PALETTE_ADDRESS) & 0xFF);
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

  @Test public void shouldPutTheNintendoLogoIntoMemoryDuringTheThirdStage() throws IOException {
    GameBoy gameBoy = givenAGameBoy();

    tickUntilThirdStageFinished(gameBoy);

    assertNintendoLogoIsInTheCartridge();
  }

  @Test public void shouldPutTheNintendoRLogoIntoMemoryDuringTheThirdStage() throws IOException {
    GameBoy gameBoy = givenAGameBoy();

    tickUntilThirdStageFinished(gameBoy);
  }

  @Test public void shouldInitializeTileMapDuringTheFourthStage() throws IOException {
    GameBoy gameBoy = givenAGameBoy();

    tickUntilFourthStageFinished(gameBoy);
  }

  @Test public void shouldPerformNintendoLogoScrollDuringTheFifthStage() throws IOException {
    GameBoy gameBoy = givenAGameBoy();

    tickUntilFifthStageFinished(gameBoy);
  }

  @Test public void shouldExecuteTheCheckRoutineSuccessfullyDuringTheSixthStage()
      throws IOException {
    GameBoy gameBoy = givenAGameBoy();

    tickUntilChecksumFinished(gameBoy);

    assertEquals(0, z80.get8BitRegisterValue(Register.A));
  }

  @Test public void shouldIndicateTheBIOSHasBeenLoadedUnlockingTheRomMapping() throws IOException {
    GameBoy gameBoy = givenAGameBoy();

    tickUntilBIOSLoaded(gameBoy);

    assertEquals(1, mmu.readByte(UNLOCK_ROM_ADDRESS) & 0xFF);
  }

  @Test public void shouldFinishBIOSExecution() throws IOException {
    GameBoy gameBoy = givenAGameBoy();

    tickUntilBIOSLoaded(gameBoy);

    assertTrue(mmu.isSystemReady());
  }

  @Test public void shouldShowNintendoLogoOnBIOSLoaded() throws IOException {
    GameBoy gameBoy = givenAGameBoy();

    tickUntilScrollProcessIsDone(gameBoy);

    dumpScrollState();
    dumpTileAndMapAddress();
    dumpVRAMMemory();
    dumpMap0VRAM();
  }

  private void dumpMap0VRAM() {
    System.out.println("MAP 0 MEMORY DUMP");
    System.out.println("_________________");
    int baseAddress = 0x9800;
    for (int i = 0; i < 32; i++) {
      for (int j = 0; j < 32; j++) {
        String memoryValue = mmu.readByte(baseAddress) == 0 ? "·" : "#";
        System.out.print(memoryValue);
        baseAddress++;
      }
      System.out.println();
    }
    System.out.println("_________________");
    System.out.println("END OF THE MAP 0 MEMORY DUMP");
  }

  private void dumpScrollState() {
    System.out.println("Scroll X = " + gpu.getScrollX());
    System.out.println("Scroll Y = " + gpu.getScrollY());
  }

  private void dumpTileAndMapAddress() {
    String mapAddress = Integer.toHexString(gpu.getMapAddress());
    System.out.println("The map address used is: " + mapAddress);
    String tileAddress = Integer.toHexString(gpu.getTileSetAddress());
    System.out.println("The tile address used is: " + tileAddress);
  }

  private void tickUntilScrollProcessIsDone(GameBoy gameBoy) {
    while (gpu.getScrollY() != 100) {
      gameBoy.tick();
    }
  }

  private GameBoy givenAGameBoy() throws IOException {
    z80 = new GBZ80();
    mmu = new MMU();
    gpu = new GPU(mmu);
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
    for (int address = VRAM_TOP_ADDRESS; address >= VRAM_BOTTOM_ADDRESS; address--) {
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

  private void tickUntilPCEqualsTo(GameBoy gameBoy, int pcValue) {
    while (z80.getProgramCounter() != pcValue) {
      gameBoy.tick();
    }
  }

  private void assertNintendoLogoIsInTheCartridge() {
    List<Integer> nintendoLogo =
        Arrays.asList(0xce, 0xed, 0x66, 0x66, 0xcc, 0x0d, 0x00, 0x0b, 0x03, 0x73, 0x00, 0x83, 0x00,
            0x0c, 0x00, 0x0d, 0x00, 0x08, 0x11, 0x1f, 0x88, 0x89, 0x00, 0x0e, 0xdc, 0xcc, 0x6e,
            0xe6, 0xdd, 0xdd, 0xd9, 0x99, 0xbb, 0xbb, 0x67, 0x63, 0x6e, 0x0e, 0xec, 0xcc, 0xdd,
            0xdc, 0x99, 0x9f, 0xbb, 0xb9, 0x33, 0x3e);

    for (int i = NINTENDO_LOGO_ADDRESS_IN_CARTRIDGE, j = 0; j < nintendoLogo.size(); i++, j++) {
      assertEquals((int) nintendoLogo.get(j), mmu.readByte(i) & 0xFF);
    }
  }

  private void tickUntilThirdStageFinished(GameBoy gameBoy) {
    tickUntilPCEqualsTo(gameBoy, 0x40);
  }

  private void tickUntilFourthStageFinished(GameBoy gameBoy) {
    tickUntilPCEqualsTo(gameBoy, 0x55);
  }

  private void tickUntilFifthStageFinished(GameBoy gameBoy) {
    tickUntilFourthStageFinished(gameBoy);
    tickUntilPCEqualsTo(gameBoy, 0xE0);
  }

  private void tickUntilBIOSLoaded(GameBoy gameBoy) {
    tickUntilPCEqualsTo(gameBoy, 0x100);
  }

  private void tickUntilChecksumFinished(GameBoy gameBoy) {
    tickUntilPCEqualsTo(gameBoy, 0xE0);
  }

  private void dumpMemory() {
    for (int i = 0; i < 0xFFFF; i++) {
      int value = mmu.readByte(i) & 0xFF;
      if (value != 0) {
        String hexAddress = Integer.toHexString(i);
        String hexValue = Integer.toHexString(value);
        System.out.println(hexAddress + " --> " + hexValue);
      }
    }
  }

  private void dumpVRAMMemory() {
    for (int i = VRAM_BOTTOM_ADDRESS; i < VRAM_TOP_ADDRESS; i++) {
      int value = mmu.readByte(i) & 0xFF;
      if (value != 0) {
        String hexAddress = Integer.toHexString(i);
        String hexValue = Integer.toHexString(value);
        System.out.println(hexAddress + " --> " + hexValue);
      }
    }
  }

  private void dumpGPUScreenMemory() {
    for (int i = 0; i < 160; i++) {
      for (int j = 0; j < 144; j++) {
        int value = gpu.getRedChannelAtPixel(i, j);
        String color = value == 0 ? "#" : " ";
        System.out.print(color);
      }
      System.out.println();
    }
  }
}
