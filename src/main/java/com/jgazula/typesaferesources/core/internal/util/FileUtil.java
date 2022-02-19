package com.jgazula.typesaferesources.core.internal.util;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;

/**
 * A wrapper over the static File related methods. This makes testing easier as this class can be
 * injected wherever needed.
 */
public class FileUtil {

  /** Same as {@link Files#exists(Path, LinkOption...)}. */
  public boolean exists(Path path) {
    return Files.exists(path);
  }
}
