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
