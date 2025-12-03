package com.combostrap.xml;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.io.PrintWriter;

/**
 * Error handler to report errors and warnings
 * <p>
 * The name has the Local prefix because sax has also a ErrorHandler
 */
public class LocalErrorHandler implements org.xml.sax.ErrorHandler {

    /**
     * Error handler output goes here
     */
    private PrintWriter out;

    /**
     * The constructor
     *
     * @param out - the writer
     */
    public LocalErrorHandler(PrintWriter out) {
        this.out = out;
    }

    /**
     * Returns a string describing parse exception details
     *
     * @param saxParseException - the exception
     * @return the info
     */
    private String getParseExceptionInfo(SAXParseException saxParseException) {
        String systemId = saxParseException.getSystemId();
        if (systemId == null) {
            systemId = "null";
        }
        return "URI=" + systemId +
                " Line=" + saxParseException.getLineNumber() +
                ": " + saxParseException.getMessage();
    }

    // The following methods are standard SAX LocalErrorHandler methods.
    // See SAX documentation for more info.

    public void warning(SAXParseException spe) throws SAXException {
        out.println("Warning: " + getParseExceptionInfo(spe));
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
