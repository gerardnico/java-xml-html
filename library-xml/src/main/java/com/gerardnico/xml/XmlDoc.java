package com.gerardnico.xml;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.file.Path;

import static com.gerardnico.xml.Doms.outputEncoding;

/**
 * Static function that shows how to create a document
 */
public class XmlDoc {

    /**
     * @param builder  - the builder
     * @param filePath - the path
     * @return a document
     */
    public static Document of(DocumentBuilderFactory builder, Path filePath) {

        try {

            // Create a DocumentBuilder that satisfies the constraints specified by the DocumentBuilderFactory
            DocumentBuilder documentBuilder;

            documentBuilder = builder.newDocumentBuilder();


            // Set an LocalErrorHandler before parsing
            OutputStreamWriter errorWriter =
                    new OutputStreamWriter(System.err, outputEncoding);
            documentBuilder.setErrorHandler(new LocalErrorHandler(new PrintWriter(errorWriter, true)));

            // Step 3: parse the input file
            return documentBuilder.parse(filePath.toFile());

        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new RuntimeException(e);
        }
    }
}
