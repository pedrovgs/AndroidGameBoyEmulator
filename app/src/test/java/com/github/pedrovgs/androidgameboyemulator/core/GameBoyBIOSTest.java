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

    tickUntilPCEqualsTo(gameBoy, 0x34);
    tickUntilScrollProcessIsDone(gameBoy);

    assertVRAMContainsNintendoLogo();
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
    while (z80.getProgramCounter() < 0xE0) {
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

  private void assertVRAMContainsNintendoLogo() {
    assertEquals(0xf0, mmu.readByte(0x8010) & 0xFF);
    assertEquals(0xf0, mmu.readByte(0x8012) & 0xFF);
    assertEquals(0xfc, mmu.readByte(0x8014) & 0xFF);
    assertEquals(0xfc, mmu.readByte(0x8016) & 0xFF);
    assertEquals(0xfc, mmu.readByte(0x8018) & 0xFF);
    assertEquals(0xfc, mmu.readByte(0x801a) & 0xFF);
    assertEquals(0xf3, mmu.readByte(0x801c) & 0xFF);
    assertEquals(0xf3, mmu.readByte(0x801e) & 0xFF);
    assertEquals(0x3c, mmu.readByte(0x8020) & 0xFF);
    assertEquals(0x3c, mmu.readByte(0x8022) & 0xFF);
    assertEquals(0x3c, mmu.readByte(0x8024) & 0xFF);
    assertEquals(0x3c, mmu.readByte(0x8026) & 0xFF);
    assertEquals(0x3c, mmu.readByte(0x8028) & 0xFF);
    assertEquals(0x3c, mmu.readByte(0x802a) & 0xFF);
    assertEquals(0x3c, mmu.readByte(0x802c) & 0xFF);
    assertEquals(0x3c, mmu.readByte(0x802e) & 0xFF);
    assertEquals(0xf0, mmu.readByte(0x8030) & 0xFF);
    assertEquals(0xf0, mmu.readByte(0x8032) & 0xFF);
    assertEquals(0xf0, mmu.readByte(0x8034) & 0xFF);
    assertEquals(0xf0, mmu.readByte(0x8036) & 0xFF);
    assertEquals(0xf3, mmu.readByte(0x803c) & 0xFF);
    assertEquals(0xf3, mmu.readByte(0x803e) & 0xFF);
    assertEquals(0xcf, mmu.readByte(0x804c) & 0xFF);
    assertEquals(0xcf, mmu.readByte(0x804e) & 0xFF);
    assertEquals(0xf, mmu.readByte(0x8054) & 0xFF);
    assertEquals(0xf, mmu.readByte(0x8056) & 0xFF);
    assertEquals(0x3f, mmu.readByte(0x8058) & 0xFF);
    assertEquals(0x3f, mmu.readByte(0x805a) & 0xFF);
    assertEquals(0xf, mmu.readByte(0x805c) & 0xFF);
    assertEquals(0xf, mmu.readByte(0x805e) & 0xFF);
    assertEquals(0xc0, mmu.readByte(0x8068) & 0xFF);
    assertEquals(0xc0, mmu.readByte(0x806a) & 0xFF);
    assertEquals(0xf, mmu.readByte(0x806c) & 0xFF);
    assertEquals(0xf, mmu.readByte(0x806e) & 0xFF);
    assertEquals(0xf0, mmu.readByte(0x807c) & 0xFF);
    assertEquals(0xf0, mmu.readByte(0x807e) & 0xFF);
    assertEquals(0xf3, mmu.readByte(0x808c) & 0xFF);
    assertEquals(0xf3, mmu.readByte(0x808e) & 0xFF);
    assertEquals(0xc0, mmu.readByte(0x809c) & 0xFF);
    assertEquals(0xc0, mmu.readByte(0x809e) & 0xFF);
    assertEquals(0x3, mmu.readByte(0x80a0) & 0xFF);
    assertEquals(0x3, mmu.readByte(0x80a2) & 0xFF);
    assertEquals(0x3, mmu.readByte(0x80a4) & 0xFF);
    assertEquals(0x3, mmu.readByte(0x80a6) & 0xFF);
    assertEquals(0x3, mmu.readByte(0x80a8) & 0xFF);
    assertEquals(0x3, mmu.readByte(0x80aa) & 0xFF);
    assertEquals(0xff, mmu.readByte(0x80ac) & 0xFF);
    assertEquals(0xff, mmu.readByte(0x80ae) & 0xFF);
    assertEquals(0xc0, mmu.readByte(0x80b0) & 0xFF);
    assertEquals(0xc0, mmu.readByte(0x80b2) & 0xFF);
    assertEquals(0xc0, mmu.readByte(0x80b4) & 0xFF);
    assertEquals(0xc0, mmu.readByte(0x80b6) & 0xFF);
    assertEquals(0xc0, mmu.readByte(0x80b8) & 0xFF);
    assertEquals(0xc0, mmu.readByte(0x80ba) & 0xFF);
    assertEquals(0xc3, mmu.readByte(0x80bc) & 0xFF);
    assertEquals(0xc3, mmu.readByte(0x80be) & 0xFF);
    assertEquals(0xfc, mmu.readByte(0x80cc) & 0xFF);
    assertEquals(0xfc, mmu.readByte(0x80ce) & 0xFF);
    assertEquals(0xf3, mmu.readByte(0x80d0) & 0xFF);
    assertEquals(0xf3, mmu.readByte(0x80d2) & 0xFF);
    assertEquals(0xf0, mmu.readByte(0x80d4) & 0xFF);
    assertEquals(0xf0, mmu.readByte(0x80d6) & 0xFF);
    assertEquals(0xf0, mmu.readByte(0x80d8) & 0xFF);
    assertEquals(0xf0, mmu.readByte(0x80da) & 0xFF);
    assertEquals(0xf0, mmu.readByte(0x80dc) & 0xFF);
    assertEquals(0xf0, mmu.readByte(0x80de) & 0xFF);
    assertEquals(0x3c, mmu.readByte(0x80e0) & 0xFF);
    assertEquals(0x3c, mmu.readByte(0x80e2) & 0xFF);
    assertEquals(0xfc, mmu.readByte(0x80e4) & 0xFF);
    assertEquals(0xfc, mmu.readByte(0x80e6) & 0xFF);
    assertEquals(0xfc, mmu.readByte(0x80e8) & 0xFF);
    assertEquals(0xfc, mmu.readByte(0x80ea) & 0xFF);
    assertEquals(0x3c, mmu.readByte(0x80ec) & 0xFF);
    assertEquals(0x3c, mmu.readByte(0x80ee) & 0xFF);
    assertEquals(0xf3, mmu.readByte(0x80f0) & 0xFF);
    assertEquals(0xf3, mmu.readByte(0x80f2) & 0xFF);
    assertEquals(0xf3, mmu.readByte(0x80f4) & 0xFF);
    assertEquals(0xf3, mmu.readByte(0x80f6) & 0xFF);
    assertEquals(0xf3, mmu.readByte(0x80f8) & 0xFF);
    assertEquals(0xf3, mmu.readByte(0x80fa) & 0xFF);
    assertEquals(0xf3, mmu.readByte(0x80fc) & 0xFF);
    assertEquals(0xf3, mmu.readByte(0x80fe) & 0xFF);
    assertEquals(0xf3, mmu.readByte(0x8100) & 0xFF);
    assertEquals(0xf3, mmu.readByte(0x8102) & 0xFF);
    assertEquals(0xc3, mmu.readByte(0x8104) & 0xFF);
    assertEquals(0xc3, mmu.readByte(0x8106) & 0xFF);
    assertEquals(0xc3, mmu.readByte(0x8108) & 0xFF);
    assertEquals(0xc3, mmu.readByte(0x810a) & 0xFF);
    assertEquals(0xc3, mmu.readByte(0x810c) & 0xFF);
    assertEquals(0xc3, mmu.readByte(0x810e) & 0xFF);
    assertEquals(0xcf, mmu.readByte(0x8110) & 0xFF);
    assertEquals(0xcf, mmu.readByte(0x8112) & 0xFF);
    assertEquals(0xcf, mmu.readByte(0x8114) & 0xFF);
    assertEquals(0xcf, mmu.readByte(0x8116) & 0xFF);
    assertEquals(0xcf, mmu.readByte(0x8118) & 0xFF);
    assertEquals(0xcf, mmu.readByte(0x811a) & 0xFF);
    assertEquals(0xcf, mmu.readByte(0x811c) & 0xFF);
    assertEquals(0xcf, mmu.readByte(0x811e) & 0xFF);
    assertEquals(0x3c, mmu.readByte(0x8120) & 0xFF);
    assertEquals(0x3c, mmu.readByte(0x8122) & 0xFF);
    assertEquals(0x3f, mmu.readByte(0x8124) & 0xFF);
    assertEquals(0x3f, mmu.readByte(0x8126) & 0xFF);
    assertEquals(0x3c, mmu.readByte(0x8128) & 0xFF);
    assertEquals(0x3c, mmu.readByte(0x812a) & 0xFF);
    assertEquals(0xf, mmu.readByte(0x812c) & 0xFF);
    assertEquals(0xf, mmu.readByte(0x812e) & 0xFF);
    assertEquals(0x3c, mmu.readByte(0x8130) & 0xFF);
    assertEquals(0x3c, mmu.readByte(0x8132) & 0xFF);
    assertEquals(0xfc, mmu.readByte(0x8134) & 0xFF);
    assertEquals(0xfc, mmu.readByte(0x8136) & 0xFF);
    assertEquals(0xfc, mmu.readByte(0x813c) & 0xFF);
    assertEquals(0xfc, mmu.readByte(0x813e) & 0xFF);
    assertEquals(0xfc, mmu.readByte(0x8140) & 0xFF);
    assertEquals(0xfc, mmu.readByte(0x8142) & 0xFF);
    assertEquals(0xf0, mmu.readByte(0x8144) & 0xFF);
    assertEquals(0xf0, mmu.readByte(0x8146) & 0xFF);
    assertEquals(0xf0, mmu.readByte(0x8148) & 0xFF);
    assertEquals(0xf0, mmu.readByte(0x814a) & 0xFF);
    assertEquals(0xf0, mmu.readByte(0x814c) & 0xFF);
    assertEquals(0xf0, mmu.readByte(0x814e) & 0xFF);
    assertEquals(0xf3, mmu.readByte(0x8150) & 0xFF);
    assertEquals(0xf3, mmu.readByte(0x8152) & 0xFF);
    assertEquals(0xf3, mmu.readByte(0x8154) & 0xFF);
    assertEquals(0xf3, mmu.readByte(0x8156) & 0xFF);
    assertEquals(0xf3, mmu.readByte(0x8158) & 0xFF);
    assertEquals(0xf3, mmu.readByte(0x815a) & 0xFF);
    assertEquals(0xf0, mmu.readByte(0x815c) & 0xFF);
    assertEquals(0xf0, mmu.readByte(0x815e) & 0xFF);
    assertEquals(0xc3, mmu.readByte(0x8160) & 0xFF);
    assertEquals(0xc3, mmu.readByte(0x8162) & 0xFF);
    assertEquals(0xc3, mmu.readByte(0x8164) & 0xFF);
    assertEquals(0xc3, mmu.readByte(0x8166) & 0xFF);
    assertEquals(0xc3, mmu.readByte(0x8168) & 0xFF);
    assertEquals(0xc3, mmu.readByte(0x816a) & 0xFF);
    assertEquals(0xff, mmu.readByte(0x816c) & 0xFF);
    assertEquals(0xff, mmu.readByte(0x816e) & 0xFF);
    assertEquals(0xcf, mmu.readByte(0x8170) & 0xFF);
    assertEquals(0xcf, mmu.readByte(0x8172) & 0xFF);
    assertEquals(0xcf, mmu.readByte(0x8174) & 0xFF);
    assertEquals(0xcf, mmu.readByte(0x8176) & 0xFF);
    assertEquals(0xcf, mmu.readByte(0x8178) & 0xFF);
    assertEquals(0xcf, mmu.readByte(0x817a) & 0xFF);
    assertEquals(0xc3, mmu.readByte(0x817c) & 0xFF);
    assertEquals(0xc3, mmu.readByte(0x817e) & 0xFF);
    assertEquals(0xf, mmu.readByte(0x8180) & 0xFF);
    assertEquals(0xf, mmu.readByte(0x8182) & 0xFF);
    assertEquals(0xf, mmu.readByte(0x8184) & 0xFF);
    assertEquals(0xf, mmu.readByte(0x8186) & 0xFF);
    assertEquals(0xf, mmu.readByte(0x8188) & 0xFF);
    assertEquals(0xf, mmu.readByte(0x818a) & 0xFF);
    assertEquals(0xfc, mmu.readByte(0x818c) & 0xFF);
    assertEquals(0xfc, mmu.readByte(0x818e) & 0xFF);
    assertEquals(0x3c, mmu.readByte(0x8190) & 0xFF);
    assertEquals(0x42, mmu.readByte(0x8192) & 0xFF);
    assertEquals(0xb9, mmu.readByte(0x8194) & 0xFF);
    assertEquals(0xa5, mmu.readByte(0x8196) & 0xFF);
    assertEquals(0xb9, mmu.readByte(0x8198) & 0xFF);
    assertEquals(0xa5, mmu.readByte(0x819a) & 0xFF);
    assertEquals(0x42, mmu.readByte(0x819c) & 0xFF);
    assertEquals(0x4c, mmu.readByte(0x819e) & 0xFF);
    assertEquals(0x1, mmu.readByte(0x9904) & 0xFF);
    assertEquals(0x2, mmu.readByte(0x9905) & 0xFF);
    assertEquals(0x3, mmu.readByte(0x9906) & 0xFF);
    assertEquals(0x4, mmu.readByte(0x9907) & 0xFF);
    assertEquals(0x5, mmu.readByte(0x9908) & 0xFF);
    assertEquals(0x6, mmu.readByte(0x9909) & 0xFF);
    assertEquals(0x7, mmu.readByte(0x990a) & 0xFF);
    assertEquals(0x8, mmu.readByte(0x990b) & 0xFF);
    assertEquals(0x9, mmu.readByte(0x990c) & 0xFF);
    assertEquals(0xa, mmu.readByte(0x990d) & 0xFF);
    assertEquals(0xb, mmu.readByte(0x990e) & 0xFF);
    assertEquals(0xc, mmu.readByte(0x990f) & 0xFF);
    assertEquals(0x19, mmu.readByte(0x9910) & 0xFF);
    assertEquals(0xd, mmu.readByte(0x9924) & 0xFF);
    assertEquals(0xe, mmu.readByte(0x9925) & 0xFF);
    assertEquals(0xf, mmu.readByte(0x9926) & 0xFF);
    assertEquals(0x10, mmu.readByte(0x9927) & 0xFF);
    assertEquals(0x11, mmu.readByte(0x9928) & 0xFF);
    assertEquals(0x12, mmu.readByte(0x9929) & 0xFF);
    assertEquals(0x13, mmu.readByte(0x992a) & 0xFF);
    assertEquals(0x14, mmu.readByte(0x992b) & 0xFF);
    assertEquals(0x15, mmu.readByte(0x992c) & 0xFF);
    assertEquals(0x16, mmu.readByte(0x992d) & 0xFF);
    assertEquals(0x17, mmu.readByte(0x992e) & 0xFF);
    assertEquals(0x18, mmu.readByte(0x992f) & 0xFF);
  }
}
