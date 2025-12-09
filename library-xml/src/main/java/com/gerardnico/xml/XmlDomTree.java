package com.gerardnico.xml;

import org.w3c.dom.DocumentType;
import org.w3c.dom.Entity;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Inspired by the DOM echo example program of `Edwin GOEI`
 */
public class XmlDomTree {


    /**
     * Indent level
     */
    private int indent = 0;

    /**
     * Indentation will be in multiples of basicIndent
     */
    @SuppressWarnings("FieldCanBeLocal")
    private final String basicIndent = "  ";


    public XmlDomTree() {
    }

    /**
     * Echo common attributes of a DOM2 Node and terminate output with an
     * EOL character.
     */
    public static String printlnCommon(Node n) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" nodeName=\"").append(n.getNodeName()).append("\"");

        String val = n.getNamespaceURI();
        if (val != null) {
            stringBuilder.append(" uri=\"").append(val).append("\"");
        }

        val = n.getPrefix();
        if (val != null) {
            stringBuilder.append(" pre=\"").append(val).append("\"");
        }

        val = n.getLocalName();
        if (val != null) {
            stringBuilder.append(" local=\"").append(val).append("\"");
        }

        val = n.getNodeValue();
        if (val != null) {
            stringBuilder.append(" nodeValue=");
            if (val.trim().isEmpty()) {
                // Whitespace
                stringBuilder.append("[WS]");
            } else {
                stringBuilder.append("\"").append(n.getNodeValue()).append("\"");
            }
        }
        stringBuilder.append(System.lineSeparator());
        return stringBuilder.toString();
    }

    public static XmlDomTree of() {
        return new XmlDomTree();
    }

    public static void print() {
        System.out.println("print is called");
    }

    /**
     * Indent to the current level in multiples of basicIndent
     */
    private void outputIndentation() {
        for (int i = 0; i < indent; i++) {
            System.out.print(basicIndent);
        }
    }

    /**
     * Recursive routine to print out DOM tree nodes
     */
    public void echo(Node n) {

        // Indent to the current level before printing anything
        outputIndentation();

        int type = n.getNodeType();
        switch (type) {
            case Node.ATTRIBUTE_NODE:
                System.out.print("ATTR:");
                System.out.print(printlnCommon(n));
                break;
            case Node.CDATA_SECTION_NODE:
                System.out.print("CDATA:");
                System.out.print(printlnCommon(n));
                break;
            case Node.COMMENT_NODE:
                System.out.print("COMM:");
                System.out.print(printlnCommon(n));
                break;
            case Node.DOCUMENT_FRAGMENT_NODE:
                System.out.print("DOC_FRAG:");
                System.out.print(printlnCommon(n));
                break;
            case Node.DOCUMENT_NODE:
                System.out.print("DOC:");
                System.out.print(printlnCommon(n));
                break;
            case Node.DOCUMENT_TYPE_NODE:
                System.out.print("DOC_TYPE:");
                System.out.print(printlnCommon(n));

                // Print entities if any
                NamedNodeMap nodeMap = ((DocumentType) n).getEntities();
                indent += 2;
                for (int i = 0; i < nodeMap.getLength(); i++) {
                    Entity entity = (Entity) nodeMap.item(i);
                    echo(entity);
                }
                indent -= 2;
                break;
            case Node.ELEMENT_NODE:
                System.out.print("ELEM:");
                System.out.print(printlnCommon(n));

                // Print attributes if any.  Note: element attributes are not
                // children of ELEMENT_NODEs but are properties of their
                // associated ELEMENT_NODE.  For this reason, they are printed
                // with 2x the indent level to indicate this.
                NamedNodeMap atts = n.getAttributes();
                indent += 2;
                for (int i = 0; i < atts.getLength(); i++) {
                    Node att = atts.item(i);
                    echo(att);
                }
                indent -= 2;
                break;
            case Node.ENTITY_NODE:
                System.out.print("ENT:");
                System.out.print(printlnCommon(n));
                break;
            case Node.ENTITY_REFERENCE_NODE:
                System.out.print("ENT_REF:");
                System.out.print(printlnCommon(n));
                break;
            case Node.NOTATION_NODE:
                System.out.print("NOTATION:");
                System.out.print(printlnCommon(n));
                break;
            case Node.PROCESSING_INSTRUCTION_NODE:
                System.out.print("PROC_INST:");
                System.out.print(printlnCommon(n));
                break;
            case Node.TEXT_NODE:
                System.out.print("TEXT:");
                System.out.print(printlnCommon(n));
                break;
            default:
                System.out.print("UNSUPPORTED NODE: " + type);
                System.out.print(printlnCommon(n));
                break;
        }

        // Print children if any
        indent++;
        for (Node child = n.getFirstChild(); child != null;
             child = child.getNextSibling()) {
            echo(child);
        }
        indent--;
    }


    // Error handler to report errors and warnings
    private static class MyErrorHandler implements ErrorHandler {

        /**
         * Returns a string describing parse exception details
         */
        private String getParseExceptionInfo(SAXParseException spe) {
            String systemId = spe.getSystemId();
            if (systemId == null) {
                systemId = "null";
            }
            return "URI=" + systemId +
                    " Line=" + spe.getLineNumber() +
                    ": " + spe.getMessage();
        }

        // The following methods are standard SAX LocalErrorHandler methods.
        // See SAX documentation for more info.

        public void warning(SAXParseException spe) throws SAXException {
            System.out.println("Warning: " + getParseExceptionInfo(spe));
        }

        public void error(SAXParseException spe) throws SAXException {
            String message = "Error: " + getParseExceptionInfo(spe);
            throw new SAXException(message);
        }

        public void fatalError(SAXParseException spe) throws SAXException {
            String message = "Fatal Error: " + getParseExceptionInfo(spe);
            throw new SAXException(message);
        }
    }
}
