/* <blockquote><small> Copyright (C) 2002 Hewlett-Packard Company.
 * This file is part of Sparta, an XML Parser, DOM, and XPath library.
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the <a href="doc-files/LGPL.txt">GNU
 * Lesser General Public License</a> as published by the Free Software
 * Foundation; either version 2.1 of the License, or (at your option)
 * any later version.  This library is distributed in the hope that it
 * will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. </small></blockquote>
 * 
 * @author Eamonn O'Brien-Strain
 */
package de.xplib.nexd.xml.spartadom;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;


/**
 * Sparta ParseLog wrapper around w3c ErrorHandler.
 * 
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 * @link http://sparta.sourceforge.net
 */
public class LogWrapper implements ParseLog {

    /**
     * Comment for <code>handler</code>
     */
    private final ErrorHandler handler;

    /**
     * @param handlerIn ..
     */
    public LogWrapper(final ErrorHandler handlerIn) {
        handler = handlerIn;
    }

    /**
     * <Some description here>
     * 
     * @param message ..
     * @param systemId ..
     * @param lineNum ..
     * @see de.xplib.nexd.xml.spartadom.ParseLog#error(
     * 		java.lang.String, java.lang.String, int)
     */
    public final void error(final String message, 
                            final String systemId,
                            final int lineNum) {
        message(message, systemId, lineNum);
    }

    /**
     * @param message ..
     * @param systemId ..
     * @param lineNum ..
     */
    private void message(final String message, 
                         final String systemId, 
                         final int lineNum) {
        try {
            handler.error(new SAXParseException(message, "", systemId,
                    lineNum, 0));
        } catch (SAXException e) {
            throw new Error(
                    "Assertion violation: error handler error method "
                    + "should not throw exception");
        }
    }

    /**
     * <Some description here>
     * 
     * @param message ..
     * @param systemId ..
     * @param lineNum ..
     * @see de.xplib.nexd.xml.spartadom.ParseLog#note(
     * 		java.lang.String, java.lang.String, int)
     */
    public final void note(final String message, 
                           final String systemId, 
                           final int lineNum) {
        message(message, systemId, lineNum);
    }

    /**
     * <Some description here>
     * 
     * @param message ..
     * @param systemId ..
     * @param lineNum ..
     * @see de.xplib.nexd.xml.spartadom.ParseLog#warning(
     * 		java.lang.String, java.lang.String, int)
     */
    public final void warning(final String message, 
                              final String systemId, 
                              final int lineNum) {
        message(message, systemId, lineNum);
    }
}

// $Log: LogWrapper.java,v $
// Revision 1.2 2002/11/06 02:59:55 eobrain
// Organize imputs to removed unused imports. Remove some unused local
// variables.
//
// Revision 1.1.1.1 2002/08/19 05:04:16 eobrain
// import from HP Labs internal CVS
//
// Revision 1.5 2002/08/18 05:45:48 eob
// Add copyright and other formatting and commenting in preparation for
// release to SourceForge.
//
// Revision 1.4 2002/08/05 20:04:32 sermarti
//
// Revision 1.3 2002/01/09 00:55:02 eob
// Add warning.
//
// Revision 1.2 2002/01/08 19:59:37 eob
// Distinguish error from note.
//
// Revision 1.1 2002/01/04 00:41:51 eob
// initial
