/*
 * Project: nexd 
 * Copyright (C) 2005  Manuel Pichler <manuel.pichler@xplib.de>
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

/*
 * $Log: XPathAPITestCase.java,v $
 * Revision 1.1  2005/05/11 17:31:39  nexd
 * Refactoring and extended test cases
 *
 */
package tests.xml;

import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.TestCase;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.xplib.nexd.engine.xml.dom.xpath.XPathAPI;
import de.xplib.nexd.engine.xml.jaxp.DocumentBuilderFactoryImpl;
import de.xplib.nexd.engine.xml.xpath.XPathExceptionExt;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class XPathAPITestCase extends TestCase {
    
    Document doc;
    
    protected void setUp() throws Exception {
        super.setUp();
        

        System.setProperty("javax.xml.parsers.DocumentBuilderFactory",
        "de.xplib.nexd.engine.xml.jaxp.DocumentBuilderFactoryImpl");
        
        DocumentBuilderFactory dbf = DocumentBuilderFactoryImpl.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        
        doc = db.parse("file:///home/devel/workspace/nexd/data/test/conf.xml");
    }

    public final void testEval() throws Exception {

        XPathAPI.eval(doc, "/*");
    }

    /*
     * Class under test for Iterator selectElementIterator(Document, String)
     */
    public final void testSelectElementIteratorDocumentString() throws Exception {
        
        XPathAPI.selectElementIterator(doc, "conf:domain");
        
        try {
            XPathAPI.selectElementIterator(doc, "/conf:domain/section/property/text()");
            assertTrue(false);
        } catch (XPathExceptionExt e) {
            
        }
        
        Iterator it = XPathAPI.selectElementIterator(doc, "//section//section/../property");
        assertTrue(it.hasNext());
        it.next();
        assertFalse(it.hasNext());
        
        it = XPathAPI.selectElementIterator(doc, "//section//section/property[1]");
        assertTrue(it.hasNext());
        it.next();
        assertFalse(it.hasNext());
        
        it = XPathAPI.selectElementIterator(doc, "//section[@name='configuration']/property[1]");
        assertTrue(it.hasNext());
        it.next();
        assertFalse(it.hasNext());
    }

    /*
     * Class under test for Iterator selectElementIterator(Element, String)
     */
    public final void testSelectElementIteratorElementString() throws Exception {
        
        Iterator it = XPathAPI.selectElementIterator(doc, "//property[@type]");
        assertTrue(it.hasNext());
        it.next();
        assertTrue(it.hasNext());
        it.next();
        assertTrue(it.hasNext());
        it.next();
        assertTrue(it.hasNext());
        it.next();
        assertFalse(it.hasNext());
        
        it = XPathAPI.selectElementIterator(doc.getDocumentElement(), ".//section/property[text() = 'creole_test']");
        assertTrue(it.hasNext());
        it.next();
        assertFalse(it.hasNext());
        
        
        try {
            it = XPathAPI.selectElementIterator(doc.getDocumentElement(), "//section/property[text() = 'creole_test']");
            assertTrue(false);
        } catch (XPathExceptionExt e) {}
    }

    /*
     * Class under test for Element selectSingleElement(Document, String)
     */
    public final void testSelectSingleElementDocumentString() throws Exception {

        Element elem = XPathAPI.selectSingleElement(doc, "//section/property[text() = 'creole_test']");
        assertNotNull(elem);
        assertEquals("property", elem.getNodeName());
        
        elem = XPathAPI.selectSingleElement(doc, "/*[@version > 1.0]");
        assertNotNull(elem);
        assertSame(doc.getDocumentElement(), elem);
        
        elem = XPathAPI.selectSingleElement(doc, "/*[@version >= 2.0]");
        assertNotNull(elem);
        assertSame(doc.getDocumentElement(), elem);
        
        elem = XPathAPI.selectSingleElement(doc, "/*[@version < 3.0]");
        assertNotNull(elem);
        assertSame(doc.getDocumentElement(), elem);
        
        elem = XPathAPI.selectSingleElement(doc, "/*[@version <= 2.0]");
        assertNotNull(elem);
        assertSame(doc.getDocumentElement(), elem);
        
        elem = XPathAPI.selectSingleElement(doc, "//property[@name != 'user']");
        assertNotNull(elem);
    }

    /*
     * Class under test for Element selectSingleElement(Element, String)
     */
    public final void testSelectSingleElementElementString() throws Exception {
        
        XPathAPI.selectSingleElement(doc.getDocumentElement(), ".//property");

        try {
            XPathAPI.selectSingleElement(doc.getDocumentElement(), "//section/property[text() = 'creole_test']");
            assertTrue(false);
        } catch (XPathExceptionExt e) {}
        
        try {
            XPathAPI.selectSingleElement(doc.getDocumentElement(), ".//property/text()");
            assertTrue(false);
        } catch (XPathExceptionExt e1) {}
    }

    /*
     * Class under test for String selectSingleString(Document, String)
     */
    public final void testSelectSingleStringDocumentString() throws Exception {
        XPathAPI.selectSingleString(doc, "//section/property[text() != 'creole_test']/text()");
        
        XPathAPI.selectSingleString(doc, "//section/property[text() != 'creole_test' and @name='phptype']/text()");
        XPathAPI.selectSingleString(doc, "//section/property[text() != 'creole_test' or @name='phptype']/text()");
        
        assertNotNull(XPathAPI.selectSingleString(doc, "//*[text()]/text()"));
        try {
            XPathAPI.selectSingleString(doc, "//section/property[text() != 'creole_test']");
            assertTrue(false);
        } catch (XPathExceptionExt e) {}
        
        try {
            XPathAPI.selectSingleString(doc, "//property[text( = 'manuel']/text()");
            assertTrue(false);
        } catch (XPathExceptionExt e1) {}
        
        try {
            XPathAPI.selectSingleString(doc, "//property[text) = 'manuel']/text()");
            assertTrue(false);
        } catch (XPathExceptionExt e1) {}

        try {
            XPathAPI.selectSingleString(doc, "//property[./text() = 'manuel']/text()");
            assertTrue(false);
        } catch (XPathExceptionExt e1) {}
        
        try {
            XPathAPI.selectSingleString(doc, "//property[text() = manuel]/text()");
            assertTrue(false);
        } catch (XPathExceptionExt e1) {}
        
        try {
            XPathAPI.selectSingleString(doc, "//property[@ = manuel]/text()");
            assertTrue(false);
        } catch (XPathExceptionExt e1) {}
        
        try {
            XPathAPI.selectSingleString(doc, "//property[@name !> 'manuel']/text()");
            assertTrue(false);
        } catch (XPathExceptionExt e1) {}
        
        try {
            XPathAPI.selectSingleString(doc, "//property[@name != manuel']/text()");
            assertTrue(false);
        } catch (XPathExceptionExt e1) {}
        try {
            XPathAPI.selectSingleString(doc, "//property[@name = manuel']/text()");
            assertTrue(false);
        } catch (XPathExceptionExt e1) {}
        try {
            XPathAPI.selectSingleString(doc, "//property[@name > manuel']/text()");
            assertTrue(false);
        } catch (XPathExceptionExt e1) {}
        try {
            XPathAPI.selectSingleString(doc, "//property[@name >- 'manuel']/text()");
            assertTrue(false);
        } catch (XPathExceptionExt e1) {}
        try {
            XPathAPI.selectSingleString(doc, "//property[@name < manuel']/text()");
            assertTrue(false);
        } catch (XPathExceptionExt e1) {}
        try {
            XPathAPI.selectSingleString(doc, "//property[@name <- 'manuel']/text()");
            assertTrue(false);
        } catch (XPathExceptionExt e1) {}
        
        try {
            XPathAPI.selectSingleString(doc, "//property[text() !> 'manuel']/text()");
            assertTrue(false);
        } catch (XPathExceptionExt e1) {}
        
        try {
            XPathAPI.selectSingleString(doc, "//property[text() != manuel]/text()");
            assertTrue(false);
        } catch (XPathExceptionExt e1) {}
        
        XPathAPI.selectSingleString(doc, "/conf:domain[@version <= 2.0]/text()");
        XPathAPI.selectSingleString(doc, "/conf:domain[@version >= 2.0]/text()");
    }

    /*
     * Class under test for String selectSingleString(Element, String)
     */
    public final void testSelectSingleStringElementString() throws Exception {
        String s = XPathAPI.selectSingleString(doc.getDocumentElement(), ".//section[@name='perm_ds']/property[text() = 'mysql']/@name");
        assertEquals("phptype", s);
    }

    /*
     * Class under test for Iterator selectStringIterator(Document, String)
     */
    public final void testSelectStringIteratorDocumentString() throws Exception {
        Iterator it = XPathAPI.selectStringIterator(doc, "//property[@type]/@type");
        assertTrue(it.hasNext());
        it.next();
        assertTrue(it.hasNext());
        it.next();
        assertTrue(it.hasNext());
        it.next();
        assertTrue(it.hasNext());
        it.next();
        assertFalse(it.hasNext());
        
        try {
            XPathAPI.selectStringIterator(doc, "//section[property/text() = 'manu']/text()");
            assertTrue(false);
        } catch (XPathExceptionExt e) {}
    }

    /*
     * Class under test for Iterator selectStringIterator(Element, String)
     */
    public final void testSelectStringIteratorElementString() throws Exception {
        Iterator it;
        try {
            it = XPathAPI.selectStringIterator(doc.getDocumentElement(), "//property[@type]/@type");
            assertTrue(false);
        } catch (XPathExceptionExt e) {}
        
        it = XPathAPI.selectStringIterator(doc.getDocumentElement(), ".//section[@name]/property[1]/text()");
        assertTrue(it.hasNext());

        try {
            it = XPathAPI.selectStringIterator(doc.getDocumentElement(), ".//property[@type]");
            assertTrue(false);
        } catch (XPathExceptionExt e) {}
    }

}
