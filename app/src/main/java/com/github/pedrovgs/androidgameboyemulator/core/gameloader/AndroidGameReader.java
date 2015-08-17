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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class AndroidGameReader implements GameReader {

  private BufferedReader bufferedReader;

  @Override public void load(String gameUri) throws FileNotFoundException {
    File game = new File(gameUri);
    bufferedReader = new BufferedReader(new FileReader(game));
  }

  @Override public int getByte() throws IOException {
    int firstPart = readHalfByte();
    int secondPart = readHalfByte();
    if (firstPart == -1 && secondPart == -1) {
      return -1;
    } else if (firstPart == -1 || secondPart == -1) {
      firstPart = firstPart == -1 ? 0 : firstPart;
      secondPart = secondPart == -1 ? 0 : secondPart;
    }
    firstPart = Character.getNumericValue(firstPart) << 4;
    secondPart = Character.getNumericValue(secondPart);
    return (firstPart + secondPart) & 0xFF;
  }

  private int readHalfByte() throws IOException {
    int character = bufferedReader.read();
    if (character == ' ' || character == '\n') {
      character = bufferedReader.read();
    }
    return character;
  }

  @Override public void closeGame() throws IOException {
    if (bufferedReader != null) {
      bufferedReader.close();
    }
  }
}
