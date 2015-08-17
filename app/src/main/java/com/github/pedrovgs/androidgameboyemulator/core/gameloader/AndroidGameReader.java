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

  @Override public byte getByte() throws IOException {
    int firstByte = (byte) (readHalfByte() << 4);
    int secondByte = readHalfByte();
    byte readByte = (byte) (firstByte + secondByte);
    if (firstByte == -1 || secondByte == -1) {
      return -1;
    }
    return readByte;
  }

  private int readHalfByte() throws IOException {
    return bufferedReader.read();
  }

  @Override public void closeGame() throws IOException {
    bufferedReader.close();
  }
}
