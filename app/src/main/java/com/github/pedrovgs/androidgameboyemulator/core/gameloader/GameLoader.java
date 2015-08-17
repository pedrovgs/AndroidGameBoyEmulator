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

import com.github.pedrovgs.androidgameboyemulator.core.mmu.MMU;
import java.io.IOException;

public class GameLoader {

  private static final int ROM_MEMORY_ADDRESS = 0x1000;

  private final GameReader gameReader;

  public GameLoader(GameReader gameReader) {
    this.gameReader = gameReader;
  }

  public void load(String uri, MMU mmu) throws IOException {

    int address = ROM_MEMORY_ADDRESS;
    try {
      gameReader.load(uri);
      int gameByte = gameReader.getByte();
      while (gameByte != -1) {
        mmu.writeByte(address, (byte) gameByte);
        gameByte = gameReader.getByte();
        address++;
      }
    } finally {
      gameReader.closeGame();
    }
  }
}
