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

enum TileColor {

  OFF(255, 255, 255, 255), GRAY1(255, 192, 192, 192), GRAY2(255, 96, 96, 96), ON(255, 0, 0, 0);

  private final int alpha;
  private final int red;
  private final int green;
  private final int blue;
  private final int rgba;

  TileColor(int alpha, int red, int green, int blue) {
    this.alpha = alpha;
    this.red = red;
    this.green = green;
    this.blue = blue;
    this.rgba = (alpha << 24) | (red << 16) | (green << 8) | blue;
  }

  public int getAlpha() {
    return alpha;
  }

  public int getRed() {
    return red;
  }

  public int getGreen() {
    return green;
  }

  public int getBlue() {
    return blue;
  }

  public int getRGBA() {
    return rgba;
  }
}
