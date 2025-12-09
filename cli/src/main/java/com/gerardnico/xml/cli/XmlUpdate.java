package com.gerardnico.xml.cli;


import com.gerardnico.xml.Xmls;
import picocli.CommandLine;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Callable;


@CommandLine.Command(
        name = "update",
        description = {
                "update the value of a node or from an attribute",
                "",
                "Example to change the value of the attribute \"dataSource\" to \"NewValue\" in the element \"ConnectionPool\" with the attribute \"name\" equal to \"myConnectionPool\" of the file \"inputFile\", you would call it as:",
                "xmli update -xp \"//Repository/DECLARE/ConnectionPool[@name=\\\"myConnectionPool\\\"]/@dataSource\" -val newDNS -in inputFile.xml"
        }
)
public class XmlUpdate implements Callable<Integer> {


    @CommandLine.Option(names = {
            "--xpath", "-xp"},
            description = "defines the Xpath Expression. It may be also given in the CSV file parameter."
    )
    private String xpath;

    @CommandLine.Option(names = {
            "-in"},
            description = "defines the Xml File Input"
    )
    private Path inputFilePath;

    @CommandLine.Option(names = {
            "-out"},
            description = "defines the Xml File created (Default to a timestamped file)"
    )
    private Path outputFilePath;

    @CommandLine.Option(names = {
            "--csv"},
            description = "defines the Csv File that contains a list of row (xpath, value) to check the XML file in batch"
    )
    private Path csvPath;

    @CommandLine.Option(names = {
            "--value"},
            description = "defines the text value that must be found at the Xpath Expression. It may be also given in the CSV file parameter."
    )
    private String value;


    @Override
    public Integer call() {


        if (xpath == null && csvPath == null) {
            throw new IllegalArgumentException(
                    String.join(System.lineSeparator(),
                            "The xpath parameter or the CSV file parameter is mandatory",
                            "None of this parameters were found"
                    ));
        }


        if (outputFilePath == null) {
            String timeStamp = new SimpleDateFormat("yyyy.MM.dd_HH.mm.ss").format(new Date());
            outputFilePath = inputFilePath.getParent().resolve(inputFilePath.getFileName().toString() + "_" + timeStamp + ".xml");
        }


        InputStream inputStream;
        OutputStream outputStream;
        try {
            inputStream = Files.newInputStream(inputFilePath);
            outputStream = Files.newOutputStream(outputFilePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Xmls.update(inputStream, xpath, value, outputStream, csvPath);

        System.out.println("The xml input file was updated and written to " + outputFilePath);
        return 0;

    }


}
