package com.gerardnico.xml.cli.util;

public class NoManifestException extends Exception {

  public NoManifestException(Throwable cause) {
    super(cause);
  }

  public NoManifestException(String message, Throwable cause) {
    super(message, cause);
  }

  public NoManifestException() {
    super();
  }
}
