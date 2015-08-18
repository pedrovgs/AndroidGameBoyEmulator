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

package com.github.pedrovgs.androidgameboyemulator.core.gpu;

public class GPU {

  private static final int SCREEN_PIXELS_RGBA = 92160;

  private final int[] screenData;

  public GPU() {
    screenData = new int[SCREEN_PIXELS_RGBA];
    reset();
  }

  public void reset() {
    for (int i = 0; i < SCREEN_PIXELS_RGBA; i++) {
      screenData[i] = 255;
    }
  }
}
