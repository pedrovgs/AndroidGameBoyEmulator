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

  private final GameReader gameReader;

  public GameLoader(GameReader gameReader) {
    this.gameReader = gameReader;
  }

  public void load(String uri, MMU mmu) throws IOException {
    gameReader.load(uri);
    int address = 0;
    while (!gameReader.isGameRead()) {
      int gameWord = gameReader.getWord();
      mmu.writeWord(address, gameWord);
    }
  }
}