package com.github.pedrovgs.androidgameboyemulator.core.gameloader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

public class FakeGameReader implements GameReader {

  private BufferedReader bufferedReader;

  @Override public void load(String gameUri) throws FileNotFoundException {
    File file = getFileFromPath(this, "res/" + gameUri);
    bufferedReader = new BufferedReader(new FileReader(file));
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

  private File getFileFromPath(Object obj, String fileName) {
    ClassLoader classLoader = obj.getClass().getClassLoader();
    URL resource = classLoader.getResource(fileName);
    return new File(resource.getPath());
  }
}
