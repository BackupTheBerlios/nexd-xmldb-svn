/*
 * Project: nexd 
 * Copyright (C) 2004  Manuel Pichler <manuel.pichler@xplib.de>
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
package tests.xml;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.TestCase;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.xplib.nexd.xml.dom.CommentImpl;
import de.xplib.nexd.xml.dom.DOMImplementationImpl;
import de.xplib.nexd.xml.dom.DocumentImpl;
import de.xplib.nexd.xml.dom.ElementNSImpl;
import de.xplib.nexd.xml.dom.ProcessingInstructionImpl;
import de.xplib.nexd.xml.jaxp.DocumentBuilderFactoryImpl;
import de.xplib.nexd.xml.jaxp.DocumentBuilderImpl;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 */
public class DocumentBuilderTestCase extends TestCase {

    /**
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        
        System.setProperty("javax.xml.parsers.DocumentBuilderFactory",
                "de.xplib.nexd.xml.jaxp.DocumentBuilderFactoryImpl");
    }
    
    
    /**
     * 
     */
    public void testNewDocumentBuilderFactory() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        assertSame(dbf.getClass(), DocumentBuilderFactoryImpl.class);
    }
    
    /**
     * @throws Exception ..
     */
    public void testNewDocumentBuilder() throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        
        assertSame(db.getClass(), DocumentBuilderImpl.class);
    }
    
    
    /**
     * @throws Exception ,..
     */
    public void testNewDocument() throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        
        Document doc = db.newDocument();
        
        assertSame(doc.getClass(), DocumentImpl.class);
    }
    
    
    /**
     * @throws Exception ..
     */
    public void testDOMImplementation() throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        
        DOMImplementation impl = db.getDOMImplementation();
        
        assertSame(impl.getClass(), DOMImplementationImpl.class);
    }
    
    
    /**
     * @throws Exception ..
     */
    public void testParse() throws Exception {
        
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        
        File f = new File("data/test/conf.xml");
        //File f = new File("data/test/entities.xml");
        Document doc = db.parse(f.toURL().openStream());
        
        assertSame(doc.getFirstChild().getClass(), CommentImpl.class);
        assertSame(doc.getDocumentElement().getClass(), ElementNSImpl.class);
        
        Element elem = doc.getDocumentElement();
        
        Element elem1 = (Element) elem.getChildNodes().item(5);
        
        assertEquals(
                elem1.getAttribute("name"), "bc_test_ds",
                elem1.getAttribute("name"));
        
        assertSame(
                elem1.getFirstChild()
                	.getNextSibling()
                	.getNextSibling()
                	.getNextSibling()
                	.getClass(), 
                ProcessingInstructionImpl.class);
        
/*        
        System.setProperty("javax.xml.parsers.DocumentBuilderFactory", 
        "com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl");
        
        dbf = DocumentBuilderFactory.newInstance();
        db = dbf.newDocumentBuilder();
        
        //File f = new File("data/test/entities.xml");
        doc = db.parse(f.toURL().openStream());
        
        //assertSame(doc.getFirstChild().getClass(), CommentImpl.class);
        //assertSame(doc.getDocumentElement().getClass(), ElementNSImpl.class);
        
        elem = doc.getDocumentElement();
        
        Element elem11 = (Element) elem.getChildNodes().item(5);
        assertEquals(
                elem11.getAttribute("name"), "bc_test_ds",
                elem11.getAttribute("name"));
                */
    }

}
