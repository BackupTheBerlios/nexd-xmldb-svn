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
package de.xplib.nexd.xml.xpath;

import java.io.IOException;

/**
 * Thrown when some problem parsing or executing an XPath expression.
 * 
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 * @link http://sparta.sourceforge.net
 */
public class XPathException extends Exception {

    /**
     * 
     * @param toks
     *            ---
     * @return ---
     */
    private static String tokenToString(final SimpleStreamTokenizer toks) {
        switch (toks.ttype) {
        case SimpleStreamTokenizer.TT_EOF:
            return "<end of expression>";
        case SimpleStreamTokenizer.TT_NUMBER:
            return toks.nval + "";
        case SimpleStreamTokenizer.TT_WORD:
            return toks.sval;
        default:
            return (char) toks.ttype + "";
        }
    }

    /**
     * 
     * @param toks
     *            ---
     * @return ---
     */
    private static String toString(final SimpleStreamTokenizer toks) {
        try {
            StringBuffer result = new StringBuffer();
            result.append(tokenToString(toks));
            if (toks.ttype != SimpleStreamTokenizer.TT_EOF) {
                toks.nextToken();
                result.append(tokenToString(toks));
                toks.pushBack();
            }
            return result.toString();
        } catch (IOException e) {
            return "(cannot get  info: " + e + ")";
        }
    }

    /**
     *  
     */
    private Throwable cause = null;

    /**
     * 
     * @param xpath
     *            ---
     * @param causeIn
     *            ---
     */
    XPathException(final XPath xpath, final Exception causeIn) {
        super(xpath + " " + causeIn);
        cause = causeIn;
    } // end XPathException(final XPath xpath, final Exception causeIn)

    /**
     * 
     * @param xpath
     *            ---
     * @param msg
     *            ---
     */
    public XPathException(final XPath xpath, final String msg) {
        super(xpath + " " + msg);
    } // end public XPathException(final XPath xpath, final String msg)

    /**
     * 
     * @param xpath
     *            ---
     * @param where
     *            ---
     * @param toks
     *            ---
     * @param expected
     *            ---
     */
    XPathException(final XPath xpath, final String where,
            final SimpleStreamTokenizer toks, final String expected) {

        this(xpath, where + " got \"" + toString(toks)
                + "\" instead of expected " + expected);
    } // end XPathException(final XPath xpath, ...)

    /**
     * <Some description here>
     * 
     * @return ---
     * @see java.lang.Throwable#getCause()
     */
    public final Throwable getCause() {
        return cause;
    } // end public final Throwable getCause()

}

// $Log: XPathException.java,v $
// Revision 1.3 2003/07/18 00:01:42 eobrain
// Make compatiblie with J2ME. For example do not use "new"
// java.util classes.
//
// Revision 1.2 2003/05/12 20:08:29 eobrain
// Inconsequential code change to avoid eclipse warning.
//
// Revision 1.1.1.1 2002/08/19 05:04:02 eobrain
// import from HP Labs internal CVS
//
// Revision 1.4 2002/08/18 23:39:32 eob
// Add copyright and other formatting and commenting in preparation for
// release to SourceForge.
//
// Revision 1.3 2002/08/15 05:11:57 eob
// add cause
//
// Revision 1.2 2002/05/23 21:07:07 eob
// Better error reporting.
//
// Revision 1.1 2002/01/24 22:11:35 eob
// initial
