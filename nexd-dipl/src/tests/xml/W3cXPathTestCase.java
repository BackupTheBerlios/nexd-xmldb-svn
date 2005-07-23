package tests.xml;

import java.io.CharArrayReader;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * 
 * Stuff common to FatPath and Xalan test cases. <blockquote><small>Copyright
 * (C) 2002 Hewlett-Packard Company. This file is part of Sparta, an XML Parser,
 * DOM, and XPath library. This library is free software; you can redistribute
 * it and/or modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1 of the
 * License, or (at your option) any later version. This library is distributed
 * in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * </small> </blockquote>
 * 
 * @version $Date: 2005/05/08 11:59:32 $ $Revision: 1.2 $
 * @author Eamonn O'Brien-Strain
 */

public abstract class W3cXPathTestCase extends XPathTestCase {

    /**
     * @param name ..
     * @throws SAXException ..
     * @throws IOException ..
     * @throws ParserConfigurationException ..
     * @throws NoSuchElementException ..
     */
    protected W3cXPathTestCase(final String name) 
    throws SAXException, IOException,
            ParserConfigurationException, NoSuchElementException {
        super(name);
        
        System.setProperty("javax.xml.parsers.DocumentBuilderFactory",
                "de.xplib.nexd.engine.xml.jaxp.DocumentBuilderFactoryImpl");

        for (int i = 0; i < XML.length; ++i) {
            DocumentBuilder parser = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder();
            //DocumentBuilder parser = new DocumentBuilderFactoryImpl()
            //		.newDocumentBuilder();
                       
            ErrorHandler eh = new EchoErrorHandler();
            parser.setErrorHandler(eh);
            InputSource src = new InputSource(new CharArrayReader(XML[i]
                    .toCharArray()));
            document[i] = parser.parse(src);
        }
        
        System.out.println(document[0].getClass());
        
        chapterOne = firstChildElement(document[0].getDocumentElement());
        sectionOne = firstChildElement(chapterOne);
        sectionTwo = nextSiblingElement(sectionOne);
    }

    /**
     * Comment for <code>document</code>
     */
    protected final Document[] document = new Document[XML.length];

    /**
     * Comment for <code>sectionTwo</code>
     */
    protected final Element chapterOne, sectionOne, sectionTwo;

    //////////////////////////////////////////////////////////////////////

    /**
     * @param parent ..
     * @return ..
     * @throws NoSuchElementException ..
     */
    protected static Element firstChildElement(final Element parent)
            throws NoSuchElementException {
        for (Node n = parent.getFirstChild(); n != null; 
        		n = n.getNextSibling()) {
            if (n instanceof Element) {
                return (Element) n;
            }
        }
        throw new NoSuchElementException();
    }

    /**
     * @param prev ..
     * @return ..
     * @throws NoSuchElementException ..
     */
    protected static Element nextSiblingElement(final Element prev)
            throws NoSuchElementException {
        for (Node n = prev.getNextSibling(); n != null;
        		n = n.getNextSibling()) {
            if (n instanceof Element) {
                return (Element) n;
            }
        }
        throw new NoSuchElementException();
    }

}