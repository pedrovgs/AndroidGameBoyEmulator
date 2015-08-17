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

package com.github.pedrovgs.androidgameboyemulator.core.gameloader;

import com.github.pedrovgs.androidgameboyemulator.UnitTest;
import com.github.pedrovgs.androidgameboyemulator.core.mmu.MMU;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;

public class GameLoaderTest extends UnitTest {

  private static final String ANY_GAME_URI = "AnyGame.gb";

  private FakeGameReader fakeGameReader;
  private MMU mmu;

  @Before public void setUp() {
    fakeGameReader = new FakeGameReader();
    mmu = new MMU();
  }

  @Test public void shouldStartLoadingGameIntoTheMemoryRomBanks() throws IOException {
    GameLoader gameLoader = givenAGameLoader(fakeGameReader);

    gameLoader.load(ANY_GAME_URI, mmu);
  }

  private GameLoader givenAGameLoader(FakeGameReader fakeGameReader) {
    return new GameLoader(fakeGameReader);
  }
}