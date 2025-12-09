package com.gerardnico.xml.cli;

import com.gerardnico.xml.Doms;
import com.gerardnico.xml.Xmls;
import picocli.CommandLine;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Callable;


@CommandLine.Command(
        name = "check",
        description = {
                "verify the value of a node or from an attribute",
                "",
                "\nExample to check that the value of the attribute \"dataSource\" is \"NewValue\" in the element \"ConnectionPool\" with the attribute \"name\" equal to \"myConnectionPool\" of the file \"inputFile\", you would call it as:\n" +
                        "command" + " -xp \"//Repository/DECLARE/ConnectionPool[@name=\\\"myConnectionPool\\\"]/@dataSource\" -val newDNS -in inputFile.xml \n"
        },
        footer = {
                "This application will check if some node (element or attribute) have the correct value\n"
        }
)
public class XmlCheck implements Callable<Integer> {


    @CommandLine.Option(names = {
            "--xpath", "-xp"},
            description = "defines the Xpath Expression. It may be also given in the CSV file parameter."
    )
    private String xpath;

    @CommandLine.Option(names = {
            "--xml-file", "-xf"},
            description = "defines the Xml File Input"
    )
    private String inputFilePath;

    @CommandLine.Option(names = {
            "--csv-file", "-cf"},
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
            System.out.println("The xpath parameter or the CSV file parameter is mandatory. None of this parameters were found");
            return 1;
        }


        // Output
        OutputStream outputStream = System.out;
        OutputStreamWriter outputStreamWriter = null;
        try {
            outputStreamWriter = new OutputStreamWriter(outputStream, Doms.outputEncoding);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        PrintWriter printWriter = new PrintWriter(outputStreamWriter, true);

        InputStream inputStream;

        try {
            inputStream = Files.newInputStream(Paths.get(inputFilePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        int nbError = Xmls.check(inputStream, xpath, value, printWriter, csvPath);
        printWriter.close();
        return nbError;

    }


}
