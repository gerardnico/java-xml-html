package com.gerardnico.xml.cli;


import com.combostrap.java.JavaEnvs;
import picocli.CommandLine;

import java.nio.file.Path;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;


@CommandLine.Command(
        name = "xmli",
        mixinStandardHelpOptions = true,
        versionProvider = XmlVersionProvider.class,
        description = {
                "To of the text value of the node defined by an Xpath expression, you would type:\nof --xpath \"//Repository/DECLARE/Database[@name=\\\"databaseName\\\"]/\" -in inputFile.xml \n"
        },
        subcommands = {
                XmlCheck.class,
                XmlCsv.class,
                XmlExtract.class,
                XmlQuery.class,
                XmlPrint.class,
                XmlUpdate.class,
        }
)
public class Xmli implements Callable<Integer> {

    private static final Logger logger = Logger.getLogger(Xmli.class.getName());

    @Override
    public Integer call() {

        // Show help when no subcommand is provided
        CommandLine.usage(this, System.out);
        return 1;

    }


    public static void main(String[] args) {
        CommandLine commandLine = getCommandLine();

        int exitCode = commandLine.execute(args);
        if (JavaEnvs.isJUnitTest()) {
            if (exitCode != 0) {
                throw new RuntimeException("Exit code (" + exitCode + ") is not zero. Errors has been seen.");
            }
            return;
        }
        System.exit(exitCode);
    }

    /**
     * A command line with converter
     * For now, there is no to configure it,
     * but it could be used in test if it was possible
     */
    protected static CommandLine getCommandLine() {


        CommandLine commandLine = new CommandLine(new Xmli())
                .registerConverter(Path.class, new PicocliPathConverter());

        /*
          Picocli catch the exception by default
          We overwrite it here
         */
        commandLine.setExecutionExceptionHandler((ex, commandLine1, parseResult) -> {

            logger.log(Level.SEVERE, "Command execution failed", ex);
            return 1;
        });

        return commandLine;
    }
}
