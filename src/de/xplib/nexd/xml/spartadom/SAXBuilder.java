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

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;


/**
 * Use to build a Sparta DOM from a SAX parser. Inspired by <a
 * href="http://www.jdom.org/docs/apidocs/org/jdom/input/SAXBuilder.html"
 * > class in JDOM </a>.
 * 
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 * @link http://sparta.sourceforge.net
 */
public class SAXBuilder extends DefaultHandler {

    /**
     * Comment for <code>doc</code>
     */
    private final BaseDocument doc = new BaseDocument();

    /**
     * Comment for <code>parent</code>
     */
    private BaseElement parent = null;

    /**
     * Comment for <code>text</code>
     */
    private BaseText text = null;

    /**
     * <Some description here>
     * 
     * @param ch ..
     * @param start ..
     * @param length ..
     * @see org.xml.sax.ContentHandler#characters(char[], int, int)
     */
    public final void characters(final char[] ch, 
                                 final int start, 
                                 final int length) {
        if (text == null) {
            text = new BaseText("");
            parent.appendChild(text);
        }
        text.appendData(ch, start, length);
    }

    /**
     * <Some description here>
     * 
     * @param uri ..
     * @param localName ..
     * @param qName ..
     * @see org.xml.sax.ContentHandler#endElement(
     * 		java.lang.String, java.lang.String, java.lang.String)
     */
    public final void endElement(final String uri, 
                                 final String localName, 
                                 final String qName) {
        parent = parent.getParentNode();
    }

    /**
     * @return ..
     */
    public final BaseDocument getParsedDocument() {
        return doc;
    }

    /**
     * <Some description here>
     * 
     * @param ch ..
     * @param start ..
     * @param length ..
     * @see org.xml.sax.ContentHandler#ignorableWhitespace(char[], int, int)
     */
    public final void ignorableWhitespace(final char[] ch, 
                                          final int start, 
                                          final int length) {
        characters(ch, start, length);
    }

    /**
     * <Some description here>
     * 
     * @param uri ..
     * @param localName ..
     * @param qName ..
     * @param attrs ..
     * @see org.xml.sax.ContentHandler#startElement(
     * 		java.lang.String, java.lang.String, java.lang.String, 
     * 		org.xml.sax.Attributes)
     */
    public final void startElement(final String uri, 
                                   final String localName, 
                                   final String qName,
                                   final Attributes attrs) {

        text = null;
        BaseElement elem = new BaseElement(qName);
        int n = attrs.getLength();
        for (int i = 0; i < n; ++i) {
            elem.setAttribute(attrs.getQName(i), attrs.getValue(i));
        }
        if (parent == null) {
            doc.setDocumentElement(elem);
        } else {
            parent.appendChild(elem);
        }
        parent = elem;
    }

}

// $Log: SAXBuilder.java,v $
// Revision 1.2 2003/06/26 03:27:18 eobrain
// Add missing copyright notice.
//
// Revision 1.1 2003/06/19 20:20:04 eobrain
// Use to build a Sparta DOM from a SAX parser.
//