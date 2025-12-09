package com.gerardnico.xml.cli;


import com.gerardnico.xml.cli.util.Utils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;

class XmlQueryTest {

    @Test
    void queryTest() throws FileNotFoundException {

        Path pomXml = Utils.closest(Paths.get(""), ".git").getParent().resolve("pom.xml");

        String output = TestUtil.captureStdout(new String[]{"query", "-xp", "/project/version/text()", pomXml.toString()});

        String regex = "^\\d+\\.\\d+\\.\\d+(?:-SNAPSHOT)?$";
        Pattern p = Pattern.compile(regex);
        String trimmedOutput = output.trim(); // there is a EOL
        boolean matches = p.matcher(trimmedOutput).matches();
        Assertions.assertTrue(matches, "The value " + trimmedOutput + "  does not match the regex ");

    }

}
