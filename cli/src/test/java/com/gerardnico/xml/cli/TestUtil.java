package com.gerardnico.xml.cli;

import java.nio.charset.StandardCharsets;

public class TestUtil {


    public static String captureStdout(String[] args) {
        java.io.ByteArrayOutputStream byteArrayOutputStream = new java.io.ByteArrayOutputStream();
        java.io.PrintStream original = System.out;
        System.setOut(new java.io.PrintStream(byteArrayOutputStream));
        try {
            Xmli.main(args);
        } finally {
            System.setOut(original);
        }
        return byteArrayOutputStream.toString(StandardCharsets.UTF_8);
    }

}
