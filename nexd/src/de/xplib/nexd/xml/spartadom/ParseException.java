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


/**
 * Thrown when error parsing XML or XPath.
 * 
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 * @link http://sparta.sourceforge.net
 */
public class ParseException extends Exception {

    /**
     * @param ch ..
     * @return ..
     */
    static String charRepr(final int ch) {
        return (ch == -1) ? "EOF" : ("" + (char) ch);
    }

    //////////////////////////////////////////////////////////////////

    /*
     * static private String toMessage(ParseCharStream source, String msg){
     * return toMessage( source.getSystemId(), source.getLineNumber(),
     * source.getLastCharRead(), source.getHistory(), msg ); }
     */

    /**
     * @param systemId ..
     * @param lineNumber ..
     * @param lastCharRead ..
     * @param history ..
     * @param msg ..
     * @return ..
     */
    private static String toMessage(final String systemId, 
                                    final int lineNumber,
                                    final int lastCharRead, 
                                    final String history, 
                                    final String msg) {
        return systemId + "(" + lineNumber + "): \n" + history
                + "\nLast character read was \'" + charRepr(lastCharRead)
                + "\'\n" + msg;
    }

    /**
     * @param chars ..
     * @return ..
     */
    private static String toString(final char[] chars) {
        StringBuffer result = new StringBuffer();
        result.append(chars[0]);
        for (int i = 1; i < chars.length; ++i) {
            result.append("or " + chars[i]);
        }
        return result.toString();
    }

    /**
     * Comment for <code>cause</code>
     */
    private Throwable cause = null;

    /**
     * Comment for <code>lineNumber</code>
     */
    private int lineNumber = -1;

    /**
     * @param source ..
     * @param actual ..
     * @param expected ..
     */
    public ParseException(final ParseCharStream source, 
                          final char actual, 
                          final char expected) {
        this(source, "got \'" + actual + "\' instead of expected \'" + expected
                + "\'");
    }

    /**
     * Precondition: expected.length > 0
     * 
     * @param source ..
     * @param actual ..
     * @param expected ..
     */
    public ParseException(final ParseCharStream source, 
            			  final char actual, 
            			  final char[] expected) {
        this(source, "got \'" + actual + "\' instead of " + toString(expected));
    }

    /**
     * @param source ..
     * @param actual ..
     * @param expected ..
     */
    public ParseException(final ParseCharStream source, 
                          final char actual, 
                          final String expected) {
        this(source, "got \'" + actual + "\' instead of " + expected
                + " as expected");
    }

    /**
     * @param source ..
     * @param msg ..
     */
    public ParseException(final ParseCharStream source, final String msg) {
        this(source.getLog(), source.getSystemId(), source.getLineNumber(),
                source.getLastCharRead(), source.getHistory(), msg);
    }

    /**
     * @param source ..
     * @param actual ..
     * @param expected ..
     */
    public ParseException(final ParseCharStream source, 
                          final String actual, 
                          final char[] expected) {
        this(source, actual, new String(expected));
    }

    /**
     * @param source ..
     * @param actual ..
     * @param expected ..
     */
    public ParseException(final ParseCharStream source, 
                          final String actual, 
                          final String expected) {
        this(source, "got \"" + actual + "\" instead of \"" + expected
                + "\" as expected");
    }

    /**
     * @param log ..
     * @param systemId ..
     * @param lineNumberIn ..
     * @param lastCharRead ..
     * @param history ..
     * @param msg ..
     */
    public ParseException(final ParseLog log, 
                          final String systemId, 
                          final int lineNumberIn,
                          final int lastCharRead, 
                          final String history, 
                          final String msg) {
        this(systemId, lineNumberIn, lastCharRead, history, msg);
        log.error(msg, systemId, lineNumberIn);
    }

    /**
     * @param msg ..
     */
    public ParseException(final String msg) {
        super(msg);
    }

    /**
     * Using systemID
     * 
     * @param systemId ..
     * @param lineNumberIn ..
     * @param lastCharRead ..
     * @param history ..
     * @param msg ..
     */
    public ParseException(final String systemId, 
                          final int lineNumberIn, 
                          final int lastCharRead,
                          final String history, 
                          final String msg) {
        super(toMessage(systemId, lineNumberIn, lastCharRead, history, msg));
        lineNumber = lineNumberIn;
    }

    /**
     * For use by handlers
     * 
     * @param msg ..
     * @param causeIn ..
     */
    public ParseException(final String msg, final Throwable causeIn) {
        super(msg + " " + causeIn);
        this.cause = causeIn;
    }

    /**
     * <Some description here>
     * 
     * @return ..
     * @see java.lang.Throwable#getCause()
     */
    public final Throwable getCause() {
        return cause;
    }

    /**
     * @return ..
     */
    public final int getLineNumber() {
        return lineNumber;
    }

}

// $Log: ParseException.java,v $
// Revision 1.1.1.1 2002/08/19 05:03:58 eobrain
// import from HP Labs internal CVS
//
// Revision 1.13 2002/08/18 04:37:56 eob
// Add copyright and other formatting and commenting in preparation for
// release to SourceForge.
//
// Revision 1.12 2002/08/09 22:36:49 sermarti
//
// Revision 1.11 2002/08/05 20:04:32 sermarti
//
// Revision 1.10 2002/07/25 21:10:15 sermarti
// Adding files that mysteriously weren't added from Sparta before.
//
// Revision 1.9 2002/05/23 21:29:32 eob
// Tweaks.
//
// Revision 1.8 2002/05/09 20:58:30 eob
// Add protected constructor to allow sub-classes without supplying all
// other info.
//
// Revision 1.7 2002/05/09 16:50:21 eob
// Add history for better error reporting.
//
// Revision 1.6 2002/01/08 19:51:02 eob
// Factored out constructors for more flexibilty.
//
// Revision 1.5 2002/01/04 00:38:40 eob
// Improve logging.
//
// Revision 1.4 2002/01/04 16:52:40 eob
// Comment change only.
//
// Revision 1.3 2002/01/04 16:52:10 eob
// Add constructors.
//
// Revision 1.2 2001/12/20 20:07:49 eob
// Added constructor that takes 2 strings. Add getLineNumber method.
//
// Revision 1.1 2001/12/19 05:52:38 eob
// initial
