package com.gerardnico.xml.cli;

import com.combostrap.fs.Fs;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;

class XmlPrintTest {

    @Test
    void printTest() throws FileNotFoundException {
        Path pomXml = Fs.closest(Paths.get(""), ".git").getParent().resolve("pom.xml");
        String output = TestUtil.captureStdout(new String[]{"print", pomXml.toString()});
        Assertions.assertFalse(output.isEmpty(),"Not empty");
        Assertions.assertEquals("DOC", output.substring(0, 3));
        //System.out.println(output);
    }

}
