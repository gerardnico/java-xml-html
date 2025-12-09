package com.gerardnico.xml.cli.util;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Utils {


    public static boolean isJUnitTest() {

        for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
            if (element.getClassName().startsWith("org.junit.")) {
                return true;
            }
        }

        return false;

    }

    /**
     * @param name - the name of the closest path
     * @return the closest path
     */
    public static Path closest(Path path, String name) throws FileNotFoundException {

        Path resolved;
        Path actual = path;
        if (!Files.isDirectory(path)) {
            actual = path.getParent();
        }
        // toAbsolute is needed otherwise the loop
        // will not stop at the root of the file system
        // but at the root of the relative path
        actual = actual.toAbsolutePath();
        while (actual != null) {
            resolved = actual.resolve(name);
            if (Files.exists(resolved)) {
                return resolved;
            }
            actual = actual.getParent();
        }
        throw new FileNotFoundException("No closest file was found");

    }


}
