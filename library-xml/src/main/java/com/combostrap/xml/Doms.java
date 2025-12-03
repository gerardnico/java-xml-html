package com.combostrap.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

import static com.combostrap.xml.XmlDomTree.printlnCommon;
import static org.w3c.dom.Node.TEXT_NODE;

/**
 * Utility Static Functions
 */
public class Doms {

    /**
     * All output will use this encoding
     */
    public static final String outputEncoding = "UTF-8";

    public static final Logger LOGGER = Logger.getLogger(Thread.currentThread().getStackTrace()[0].getClassName());

    /**
     * Get a node list from an xPath
     */
    public static NodeList getNodeList(Document document, String xPath) {

        NodeList nodeList;
        XPath xpath = XPathFactory.newInstance().newXPath();

        XPathExpression xPathExpression;
        try {

            xPathExpression = xpath.compile(xPath);
            nodeList = (NodeList) xPathExpression.evaluate(document, XPathConstants.NODESET);

        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
        return nodeList;
    }

    /**
     * Return the text that a node contains. This routine:
     * <ul>
     *  <li>Ignores comments and processing instructions.
     *  <li>Concatenates TEXT nodes, CDATA nodes, and the results of
     * recursively processing EntityRef nodes.
     *  <li>Ignores any element nodes in the sublist.
     *  (Other possible options are to recurse into element sublist or throw an exception.)
     * </ul>
     *
     * @param node a DOM node
     * @return a string representing its contents
     * Duplicate of {@link Node#getTextContent()}
     */
    public static String getText(Node node) {
        StringBuilder result = new StringBuilder();
        if (!node.hasChildNodes()) return node.getNodeValue();

        NodeList list = node.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            Node subnode = list.item(i);
            if (subnode.getNodeType() == Node.TEXT_NODE) {
                result.append(subnode.getNodeValue());
            } else if (subnode.getNodeType() ==
                    Node.CDATA_SECTION_NODE) {
                result.append(subnode.getNodeValue());
            } else if (subnode.getNodeType() ==
                    Node.ENTITY_REFERENCE_NODE) {
                // Recurse into the subtree for text
                // (and ignore comments)
                result.append(getText(subnode));
            }
        }
        return result.toString();
    }


    /**
     * Return a DOM from an input stream
     */
    public static Document getDom(InputStream xmlFile) {

        // Step 1: create a DocumentBuilderFactory and configure it
        DocumentBuilderFactory builder = DocumentBuilderFactory.newInstance();

        // Important: Disable namespace-awareness for the simple XPath to work
        // If you enable it, you must configure a NamespaceContext for the XPath
        builder.setNamespaceAware(false);

        // Set the validation mode to no validation (ie no DTD
        // validation, or XSD validation)
        builder.setValidating(false);

        // Optional: set various configuration options
        builder.setIgnoringComments(false);
        builder.setIgnoringElementContentWhitespace(false);
        builder.setCoalescing(true);
        // The opposite of creating entity ref nodes is expanding them inline
        builder.setExpandEntityReferences(false);

        // Create a DocumentBuilder that satisfies the constraints
        // specified by the DocumentBuilderFactory
        DocumentBuilder documentBuilder;

        // A big try to catch all JAXP and Trax exception
        Document doc;
        try {

            documentBuilder = builder.newDocumentBuilder();

            // Set an LocalErrorHandler before parsing
            OutputStreamWriter errorWriter;

            errorWriter = new OutputStreamWriter(System.err, outputEncoding);

            documentBuilder.setErrorHandler(new LocalErrorHandler(new PrintWriter(errorWriter, true)));

            // Parse the input file
            doc = documentBuilder.parse(xmlFile);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return doc;
    }

    /**
     * Update the text node of the nodeList with the value
     */
    public static void updateText(NodeList nodeList, String value) {

        // Modification of the value
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node childElement = nodeList.item(i);
            // If it's an element
            if (childElement.hasChildNodes()) {
                NodeList firstChildNodes = childElement.getChildNodes();
                for (int j = 0; j < firstChildNodes.getLength(); j++) {
                    Node firstChildNode = firstChildNodes.item(j);
                    if (firstChildNode.getNodeType() == TEXT_NODE) {
                        firstChildNode.setNodeValue(value);
                    }
                }
            }
        }

    }

    /**
     * Print a DOM to a stream
     */
    public static void toStream(Document document, OutputStream outputStream) {

        try {

            // Use a Transformer for output
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(outputStream);
            transformer.transform(source, result);

        } catch (TransformerConfigurationException tce) {
            // Error generated by the parser
            System.out.println("\n** Transformer Factory error");
            System.out.println("   " + tce.getMessage());

            // Use the contained exception, if any
            Throwable x = tce;

            if (tce.getException() != null) {
                x = tce.getException();
            }

            x.printStackTrace();
        } catch (TransformerException te) {
            // Error generated by the parser
            System.out.println("\n** Transformation error");
            System.out.println("   " + te.getMessage());

            // Use the contained exception, if any
            Throwable x = te;

            if (te.getException() != null) {
                x = te.getException();
            }

            x.printStackTrace();
        }
    }

    /**
     * * A wrapper functionm of {@link #toStream(Document, OutputStream)}
     * that prints to standard out
     */
    protected static void toConsole(Document dom) {
        toStream(dom, System.out);
    }

    /**
     * A wrapper function of {@link #toStream(Document, OutputStream)}
     * that prints to a file
     */
    protected static void toFile(Document dom, Path path) {

        try {
            toStream(dom, Files.newOutputStream(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getNodeInfo(Node n) {
        StringBuilder stringBuilder = new StringBuilder();
        int type = n.getNodeType();
        switch (type) {
            case Node.ATTRIBUTE_NODE:
                stringBuilder.append("ATTR:");
                break;
            case Node.CDATA_SECTION_NODE:
                stringBuilder.append("CDATA:");
                break;
            case Node.COMMENT_NODE:
                stringBuilder.append("COMM:");
                break;
            case Node.DOCUMENT_FRAGMENT_NODE:
                stringBuilder.append("DOC_FRAG:");
                break;
            case Node.DOCUMENT_NODE:
                stringBuilder.append("DOC:");
                break;
            case Node.DOCUMENT_TYPE_NODE:
                stringBuilder.append("DOC_TYPE:");
                break;
            case Node.ELEMENT_NODE:
                stringBuilder.append("ELEM:");
                break;
            case Node.ENTITY_NODE:
                stringBuilder.append("ENT:");
                break;
            case Node.ENTITY_REFERENCE_NODE:
                stringBuilder.append("ENT_REF:");
                break;
            case Node.NOTATION_NODE:
                stringBuilder.append("NOTATION:");
                break;
            case Node.PROCESSING_INSTRUCTION_NODE:
                stringBuilder.append("PROC_INST:");
                break;
            case Node.TEXT_NODE:
                stringBuilder.append("TEXT:");
                break;
            default:
                stringBuilder.append("UNSUPPORTED NODE: ").append(type);
                break;
        }
        stringBuilder.append(printlnCommon(n));
        return stringBuilder.toString();
    }

}
