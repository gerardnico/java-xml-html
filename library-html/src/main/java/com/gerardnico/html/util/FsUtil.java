package com.gerardnico.html.util;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class FsUtil {
    /**
     * @param s - the resource name starting with a /
     * @return the content as a string
     */
    public static String readStringFromResource(String s) {
        try {
            URI uri = Objects.requireNonNull(FsUtil.class.getResource(s)).toURI();
            return Files.readString(Paths.get(uri), StandardCharsets.UTF_8);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param path - the path
     * @return the string
     */
    public static String readString(Path path) {
        try {
            return Files.readString(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Error while reading the path (" + path + "). Error: " + e.getMessage(), e);
        }
    }

}
