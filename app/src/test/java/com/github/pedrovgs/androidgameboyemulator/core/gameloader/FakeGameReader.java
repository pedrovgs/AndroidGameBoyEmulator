package com.github.pedrovgs.androidgameboyemulator.core.gameloader;

import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

public class FakeGameReader implements GameReader {

  @Override public byte[] readGame(String uri) throws IOException {
    File file = getFileFromPath(this, "res/" + uri);
    String plainGame = Files.toString(file, Charset.defaultCharset());
    plainGame = plainGame.trim().replace(" ", "").replace("\n", "");
    return plainGame.getBytes();
  }

  private File getFileFromPath(Object obj, String fileName) {
    ClassLoader classLoader = obj.getClass().getClassLoader();
    URL resource = classLoader.getResource(fileName);
    return new File(resource.getPath());
  }
}
