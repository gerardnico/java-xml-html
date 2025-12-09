package com.gerardnico.xml.cli;


import com.gerardnico.xml.Xmls;
import picocli.CommandLine;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.Callable;

@CommandLine.Command(
        name = "csv",
        description = {
                "create from an XML file a CSV file",
                "",
                "Example",
                " --xpath \"//Repository/DECLARE/Database[@name=\\\"databaseName\\\"]/\" inputFile.xml \n"
        }
)
public class XmlCsv implements Callable<Integer> {

    @CommandLine.Option(names = {
            "--xpath", "-xp"},
            description = "defines the Xpath Expression",
            required = true
    )
    private String xpath;

    @CommandLine.Parameters(
            description = "defines the Xml File Input",
            // at least 1
            arity = "1..1"
    )
    private String inputFilePath;


    @Override
    public Integer call() {

        InputStream inputStream;
        try {
            inputStream = Files.newInputStream(Paths.get(inputFilePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Xmls.xml2Csv(inputStream, xpath);
        return 0;

    }


}
