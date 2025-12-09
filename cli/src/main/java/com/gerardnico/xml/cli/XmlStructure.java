package com.gerardnico.xml.cli;


import picocli.CommandLine;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Path;
import java.util.concurrent.Callable;


@CommandLine.Command(
        name = "describe",
        description = {
                "print the structure of a XML based on its content",
                "print a tree summary structure of an xml"
        }
)
public class XmlStructure implements Callable<Integer> {

    @CommandLine.Parameters(
            description = "A Xml file Uri  (file.xml)",
            // at least 1
            arity = "1..1"
    )
    private Path filePath;


    @Override
    public Integer call() {


        try (Reader reader = new InputStreamReader(new FileInputStream(filePath.toFile()))) {

            com.gerardnico.xml.XmlStructure.of(reader).printNodeNames();
            return 0;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


}
