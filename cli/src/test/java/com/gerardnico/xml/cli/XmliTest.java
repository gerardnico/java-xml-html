package com.gerardnico.xml.cli;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

/**
 * Test the cli
 */
class XmliTest {

    /**
     * Comment
     */
    public XmliTest() {
    }

    /**
     * Test the help flag
     */
    @Test
    void helpTest() {
        String[] args = {"-h"};
        String output = TestUtil.captureStdout(args);
        //System.out.println(output);
        Assertions.assertEquals("Usage", output.substring(0, "Usage".length()));
    }

    /**
     * Test the version flag
     */
    @Test
    void versionTest() {
        String[] args = {"-V"};
        String output = TestUtil.captureStdout(args);
        String regex = "^\\d+\\.\\d+\\.\\d+(?:-SNAPSHOT)?$";
        Pattern p = Pattern.compile(regex);
        String trimmedOutput = output.trim();
        boolean matches = p.matcher(trimmedOutput).matches();
        Assertions.assertTrue(matches, "The value " + trimmedOutput + "  does not match the regex");
    }
}
