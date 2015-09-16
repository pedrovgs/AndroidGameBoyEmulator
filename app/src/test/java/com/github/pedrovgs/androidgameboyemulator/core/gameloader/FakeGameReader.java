package com.github.pedrovgs.androidgameboyemulator.core.gameloader;

import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class FakeGameReader implements GameReader {

  @Override public byte[] readGame(String uri) throws IOException {
    File file = getFileFromPath(this, "res/" + uri);
    byte[] gameInBytes = Files.toByteArray(file);
    return gameInBytes;
  }

  private File getFileFromPath(Object obj, String fileName) {
    ClassLoader classLoader = obj.getClass().getClassLoader();
    URL resource = classLoader.getResource(fileName);
    return new File(resource.getPath());
  }
}