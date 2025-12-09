package com.gerardnico.xml.cli;


import com.gerardnico.xml.Xmls;
import picocli.CommandLine;

import java.nio.file.Path;
import java.util.concurrent.Callable;

@CommandLine.Command(
        name = "extract",
        description = {
                "extract one or multiple nodes and create another xml"
        }
)
public class XmlExtract implements Callable<Integer> {

    @CommandLine.Parameters(
            description = "A Xml File Path",
            // at least 1
            arity = "1..1"
    )
    private Path fileUri;

    @CommandLine.Option(names = {
            "--xpath", "-xp"},
            description = "defines the Xpath Expression",
            required = true
    )
    String xpath;

    @Override
    public Integer call() {

        Xmls.xmlExtract(fileUri, xpath);
        return 0;

    }


}
