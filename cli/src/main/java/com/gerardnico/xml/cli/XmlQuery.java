package com.gerardnico.xml.cli;


import com.gerardnico.xml.Xmls;
import picocli.CommandLine;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Callable;

@CommandLine.Command(
        name = "query",
        description = {
                "return the value of a node or from an attribute"
        }
)
public class XmlQuery implements Callable<Integer> {

    @CommandLine.Parameters(
            description = "A Xml file Uri  (file.xml)",
            // at least 1
            arity = "1..1"
    )
    private Path filePath;

    @CommandLine.Option(names = {
            "--xpath", "-xp"},
            description = "defines the Xpath Expression.",
            required = true
    )
    private String xpath;

    @Override
    public Integer call() {


        try {

            InputStream inputStream = Files.newInputStream(filePath);
            System.out.println(Xmls.get(inputStream, xpath));
            return 0;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }


}
