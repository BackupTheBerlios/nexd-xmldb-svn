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
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 * @link http://sparta.sourceforge.net
 */
class DefaultLog implements ParseLog {

    /**
     * <Some description here>
     * 
     * @param msg ..
     * @param systemId ..
     * @param line ..
     * @see de.xplib.nexd.xml.spartadom.ParseLog#error(
     * 		java.lang.String, java.lang.String, int)
     */
    public void error(final String msg, final String systemId, final int line) {
        System.err.println(systemId + "(" + line + "): " + msg + " (ERROR)");
    }

    /**
     * <Some description here>
     * 
     * @param msg ..
     * @param systemId ..
     * @param line ..
     * @see de.xplib.nexd.xml.spartadom.ParseLog#note(
     * 		java.lang.String, java.lang.String, int)
     */
    public void note(final String msg, final String systemId, final int line) {
        System.out.println(systemId + "(" + line + "): " + msg + " (NOTE)");
    }

    /**
     * <Some description here>
     * 
     * @param msg ..
     * @param systemId ..
     * @param line ..
     * @see de.xplib.nexd.xml.spartadom.ParseLog#warning(
     * 		java.lang.String, java.lang.String, int)
     */
    public void warning(final String msg, 
            			final String systemId, 
            			final int line) {
        System.out.println(systemId + "(" + line + "): " + msg + " (WARNING)");
    }

}

/**
 * A source of XML that has been parsed.
 * 
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 * @link http://sparta.sourceforge.net
 */
public interface ParseSource {

    /**
     * The InputStream passed into the constructor must support mark with this
     * amount of lookahead. new BufferedInputStream(in,ParseSource.MAXLOOKAHEAD)
     * Max charset name is 40 according to
     * http://www.iana.org/assignments/character-sets
     */
    int MAXLOOKAHEAD = "<?xml version=\"1.0\" encoding=\"\"".length() + 40;

    /**
     * Comment for <code>DEFAULT_LOG</code>
     */
    ParseLog DEFAULT_LOG = new DefaultLog();

    /**
     * Last line number read by parser.
     * 
     * @return ..
     */
    int getLineNumber();

    /**
     * @return ...
     */
    String getSystemId();

    /**
     * @return ..
     */
    String toString();
}

// $Log: ParseSource.java,v $
// Revision 1.2 2003/07/17 23:58:40 eobrain
// Make compatiblie with J2ME. For example do not use "new"
// java.util classes.
//
// Revision 1.1.1.1 2002/08/19 05:03:57 eobrain
// import from HP Labs internal CVS
//
// Revision 1.10 2002/08/18 04:41:48 eob
// Add copyright and other formatting and commenting in preparation for
// release to SourceForge.
//
// Revision 1.9 2002/08/05 20:04:32 sermarti
//
// Revision 1.8 2002/07/25 21:10:15 sermarti
// Adding files that mysteriously weren't added from Sparta before.
//
// Revision 1.7 2002/02/01 21:58:04 eob
// To workaround javadoc bug, do not use anonymous class.
//
// Revision 1.6 2002/01/09 00:53:40 eob
// Add warning to default log.
//
// Revision 1.5 2002/01/08 19:52:34 eob
// Factored out ParseSource functionality into ParseCharStream and
// ParseByteStream.
//
// Revision 1.4 2002/01/05 21:29:15 eob
// Use PeekReader.
//
// Revision 1.3 2002/01/05 07:54:29 eob
// Proper handling of text interspersed between elements.
//
// Revision 1.2 2002/01/04 00:44:09 eob
// Improve logging.
//
// Revision 1.1 2002/01/04 17:09:14 eob
// initial
