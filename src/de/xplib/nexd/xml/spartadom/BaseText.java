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

import java.io.IOException;
import java.io.Writer;
import java.util.Enumeration;

/**
 * A section of data in an element. In XML this can be either CDATA or not: the
 * DOM model does not distinguish between the two encodings.
 * 
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 * @link http://sparta.sourceforge.net
 * @see org.w3c.dom.Text
 */

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 */
public class BaseText extends BaseNode {

    /**
     * Comment for <code>data</code>
     */
    private StringBuilder data;

    /**
     * Create with an initial character as its data. This can be added to later.
     * 
     * @param ch ..
     */
    public BaseText(final char ch) {
        data = new StringBuilder();
        data.append(ch);
    }

    /**
     * Create with an initial character as its data. This can be added to later.
     * 
     * @param dataIn ..
     */
    public BaseText(final String dataIn) {
        data = new StringBuilder(dataIn);
    }

    /**
     * @param ch ..
     */
    public final void appendData(final char ch) {
        data.append(ch);
        notifyObservers();
    }

    /**
     * @param cbuf
     *            characters, some of which are to be appended to the data data
     * @param offset
     *            the first index in cbuf to copy
     * @param len
     *            the number of characters to copy
     */
    public final void appendData(final char[] cbuf, 
            		             final int offset, 
            		             final int len) {
        data.append(cbuf, offset, len);
        notifyObservers();
    }

    /**
     * @param s ..
     */
    public final void appendData(final String s) {
        data.append(s);
        notifyObservers();
    }

    
    /**
     * Deep clone: returns Text node with copy of this ones data.
     * 
     * @return ..
     * @see java.lang.Object#clone()
     */
    public final Object clone() {
        return new BaseText(data.toString());
    }

    /**
     * Called whenever cached version of hashCode needs to be regenerated.
     * 
     * @return ..
     * @see de.xplib.nexd.xml.spartadom.BaseNode#computeHashCode()
     */
    protected final int computeHashCode() {
        return data.toString().hashCode();
    }

    /**
     * Text nodes can be equal even if they are in different documents,
     * different parents, different siblings, or different annotations. They are
     * equal IFF their string data is equal
     * 
     * @param thatO ..
     * @return ..
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public final boolean equals(final Object thatO) {

        //Do cheap tests first
        if (this == thatO) {
            return true;
        }
        if (!(thatO instanceof BaseText)) {
            return false;
        }
        BaseText that = (BaseText) thatO;
        return this.data.toString().equals(that.data.toString());
    }
    
    /**
     * <Some description here>
     * 
     * @return ..
     * @see java.lang.Object#hashCode()
     */
    public final int hashCode() {
        return super.hashCode();
    }

    /**
     * @return ...
     */
    public final String getData() {
        return data.toString();
    }

    /**
     * @param dataIn ..
     */
    public final void setData(final String dataIn) {
        data = new StringBuilder(dataIn);
        notifyObservers();
    }

    /**
     * <Some description here>
     * 
     * @param writer ..
     * @throws IOException ..
     * @see de.xplib.nexd.xml.spartadom.BaseNode#toString(java.io.Writer)
     */
    final void toString(final Writer writer) throws IOException {
        writer.write(data.toString());
    }

    /**
     * <Some description here>
     * 
     * @param writer ..
     * @throws IOException ..
     * @see de.xplib.nexd.xml.spartadom.BaseNode#toXml(java.io.Writer)
     */
    final void toXml(final Writer writer) throws IOException {
        //System.out.println("Text.toXml "+text_.toString());
        String s = data.toString();
        if (s.length() < 50) {
            //short
            htmlEncode(writer, s);
        } else {
            //long
            writer.write("<![CDATA[");
            writer.write(s);
            writer.write("]]>");
        }
    }

    /**
     * Not implemented.
     * 
     * @param xpath ..
     * @return ..
     * @see de.xplib.nexd.xml.spartadom.BaseNode#xpathSelectElement(
     * 		java.lang.String)
     */
    public final BaseElement xpathSelectElement(final String xpath) {
        throw new Error("Sorry, not implemented");
    }

    /**
     * Not implemented.
     * 
     * @param xpath ..
     * @return ..
     * @see de.xplib.nexd.xml.spartadom.BaseNode#xpathSelectElements(
     * 		java.lang.String)
     */
    public final Enumeration xpathSelectElements(final String xpath) {
        throw new Error("Sorry, not implemented");
    }

    /**
     * Not implemented.
     * 
     * @param xpath ..
     * @return ..
     * @see de.xplib.nexd.xml.spartadom.BaseNode#xpathSelectString(
     * 		java.lang.String)
     */
    public final String xpathSelectString(final String xpath) {
        throw new Error("Sorry, not implemented");
    }

    /**
     * Not implemented.
     * 
     * @param xpath ..
     * @return ..
     * @see de.xplib.nexd.xml.spartadom.BaseNode#xpathSelectStrings(
     * 		java.lang.String)
     */
    public final Enumeration xpathSelectStrings(final String xpath) {
        throw new Error("Sorry, not implemented");
    }
}

// $Log: Text.java,v $
// Revision 1.5 2003/07/17 23:58:40 eobrain
// Make compatiblie with J2ME. For example do not use "new"
// java.util classes.
//
// Revision 1.4 2003/06/19 20:20:46 eobrain
// hash code optimization
//
// Revision 1.3 2002/12/13 23:09:24 eobrain
// Fix javadoc.
//
// Revision 1.2 2002/11/06 02:57:59 eobrain
// Organize imputs to removed unused imports. Remove some unused local
// variables.
//
// Revision 1.1.1.1 2002/08/19 05:03:53 eobrain
// import from HP Labs internal CVS
//
// Revision 1.11 2002/08/18 04:22:24 eob
// Sparta no longer throws XPathException -- it throws ParseException
// instead. Remove deprecated constructors.
//
// Revision 1.10 2002/08/15 21:23:00 eob
// Constructor no longer needs document.
//
// Revision 1.9 2002/08/15 05:09:22 eob
// Notify observers. CDATA
//
// Revision 1.8 2002/08/01 23:29:17 sermarti
// Much faster Sparta parsing.
// Has debug features enabled by default. Currently toggled
// in ParseCharStream.java and recompiled.
//
// Revision 1.7 2002/06/14 19:37:51 eob
// Make toString of Node do the same as in XSLT -- recursive
// concatenation of all data in data nodes.
//
// Revision 1.6 2002/05/23 21:32:05 eob
// Add appendData method for efficiency. This optimization was done
// because of what performance profiling showed.
//
// Revision 1.5 2002/05/11 00:10:53 eob
// Add stubs for xpathSelect* methods.
//
// Revision 1.4 2002/05/10 21:38:06 eob
// equals added
//
// Revision 1.3 2002/03/28 01:23:18 jrowson
// fixed bugs related to client side caching
//
// Revision 1.2 2002/02/23 02:07:11 eob
// Add clone method. Tweak toXml API.
//
// Revision 1.1 2002/01/05 07:31:50 eob
// initial
