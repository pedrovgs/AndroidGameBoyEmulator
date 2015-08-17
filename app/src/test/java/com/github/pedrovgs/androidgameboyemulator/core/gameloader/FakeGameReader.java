package com.github.pedrovgs.androidgameboyemulator.core.gameloader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

public class FakeGameReader implements GameReader {

  private BufferedReader bufferReader;

  @Override public void load(String gameUri) throws FileNotFoundException {
    File file = getFileFromPath(this, "res/test.json");
    bufferReader = new BufferedReader(new FileReader(file));
  }

  @Override public int getWord() throws IOException {
    return bufferReader.read();
  }

  @Override public void closeGame() throws IOException {
    bufferReader.close();
  }

  private File getFileFromPath(Object obj, String fileName) {
    ClassLoader classLoader = obj.getClass().getClassLoader();
    URL resource = classLoader.getResource(fileName);
    return new File(resource.getPath());
  }
}
