package com.gerardnico.xml.cli;


import com.combostrap.type.Strings;
import com.gerardnico.xml.XmlDoc;
import com.gerardnico.xml.XmlDomTree;
import org.w3c.dom.Document;
import picocli.CommandLine;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.nio.file.Path;
import java.util.concurrent.Callable;


@CommandLine.Command(
        name = "print",
        description = {
                "print the XML tree as seen by the DOM",
                "Inspired by the DOM echo example program of Edwin Goei"
        }
)
public class XmlPrint implements Callable<Integer> {

    /**
     * Constants used for JAXP 1.2
     */
    static final String JAXP_SCHEMA_LANGUAGE =
            "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
    static final String W3C_XML_SCHEMA =
            "http://www.w3.org/2001/XMLSchema";
    static final String JAXP_SCHEMA_SOURCE =
            "http://java.sun.com/xml/jaxp/properties/schemaSource";

    @CommandLine.Parameters(
            description = "A Xml file Uri  (file.xml)",
            // at least 1
            arity = "1..*"
    )
    private Path filename;

    @CommandLine.Option(names = {
            "--dtd"},
            description = "DTD validation",
            defaultValue = "false"
    )
    private boolean dtdValidate;

    @CommandLine.Option(names = {
            "--xsdss"},
            description = "A xsd file Uri <file.xsd> = W3C XML Schema validation using xsi: hints in instance document or schema source <file.xsd>"
    )
    private String schemaSource;

    @CommandLine.Option(names = {
            "--comment", "-co"},
            description = "Ignore comment nodes",
            // default value is when no flag is present
            // if the flag is in a negated form (no), the value is negated if present
            // https://github.com/remkop/picocli/issues/813#issuecomment-532423733
            defaultValue = "true"
    )
    private boolean ignoreComments;

    @CommandLine.Option(names = {
            "-ws"},
            description = "Create element content whitespace nodes",
            // default value is when no flag is present
            // if the flag is in a negated form (no), the value is negated if present
            // https://github.com/remkop/picocli/issues/813#issuecomment-532423733
            defaultValue = "true"
    )
    private Boolean ignoreWhitespace;

    @CommandLine.Option(names = {
            "--cdata", "-cd"},
            description = "put CDATA into Text nodes",
            // default value is when no flag is present
            // if the flag is in a negated form (no), the value is negated if present
            // https://github.com/remkop/picocli/issues/813#issuecomment-532423733
            defaultValue = "false"
    )
    private boolean putCDATAIntoText;

    @CommandLine.Option(names = {
            "--entity-ref", "-e"},
            description = "create EntityReference nodes",
            // default value is when no flag is present
            // if the flag is in a negated form (no), the value is negated if present
            // https://github.com/remkop/picocli/issues/813#issuecomment-532423733
            defaultValue = "false"
    )
    private boolean createEntityRefs;


    @Override
    public Integer call() {


        boolean xsdValidate = schemaSource != null;


        // Create a DocumentBuilderFactory and configure it
        DocumentBuilderFactory documentBuilderFactory =
                DocumentBuilderFactory.newInstance();

        // Set namespaceAware to true to of a DOM Level 2 tree with nodes
        // containing namespace information.  This is necessary because the
        // default value from JAXP 1.0 was defined to be false.
        documentBuilderFactory.setNamespaceAware(true);

        // Set the validation mode to either: no validation, DTD
        // validation, or XSD validation
        documentBuilderFactory.setValidating(dtdValidate || xsdValidate);
        if (xsdValidate) {
            try {
                documentBuilderFactory.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
            } catch (IllegalArgumentException x) {
                // This can happen if the parser does not support JAXP 1.2
                throw new RuntimeException(Strings.createMultiLineFromStrings(
                        "Error: JAXP DocumentBuilderFactory attribute not recognized: " + JAXP_SCHEMA_LANGUAGE,
                        "Check to see if parser conforms to JAXP 1.2 spec.").toString());

            }
        }

        // Set the schema source, if any.  See the JAXP 1.2 maintenance
        // updateText specification for more complex usages of this feature.
        if (schemaSource != null) {
            documentBuilderFactory.setAttribute(JAXP_SCHEMA_SOURCE, new File(schemaSource));
        }

        // Optional: set various configuration options
        documentBuilderFactory.setIgnoringComments(ignoreComments);
        documentBuilderFactory.setIgnoringElementContentWhitespace(ignoreWhitespace);
        documentBuilderFactory.setCoalescing(putCDATAIntoText);
        // The opposite of creating entity ref nodes is expanding them inline
        documentBuilderFactory.setExpandEntityReferences(!createEntityRefs);


        Document doc = XmlDoc.of(documentBuilderFactory, filename);

        // Print out the DOM tree
        XmlDomTree.of().echo(doc);

        return 0;
    }
}
