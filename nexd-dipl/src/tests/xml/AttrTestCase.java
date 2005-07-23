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
 * $Log: AttrTestCase.java,v $
 * Revision 1.1  2005/05/11 17:31:39  nexd
 * Refactoring and extended test cases
 *
 */
package tests.xml;

import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.TestCase;

import org.sixdml.dbmanagement.SixdmlCollection;
import org.sixdml.dbmanagement.SixdmlResource;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import de.xplib.nexd.comm.NEXDEngineI;
import de.xplib.nexd.engine.NEXDEnginePool;
import de.xplib.nexd.engine.xml.dom.AttrImpl;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class AttrTestCase extends TestCase {
    
    private NEXDEngineI engine;
    
    private Document doc;
    
    private Document docNs;

    private Element elem;
    
    private Element elemNs;

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        
        engine = NEXDEnginePool.getInstance().getEngine();
        engine.open("sa", "");
        
        SixdmlCollection coll = engine.queryCollection("/db/docs");
        
        doc = (Document) ((SixdmlResource) engine.queryResource(coll, "document.xml")).getContentAsDOM();
        elem = doc.getDocumentElement();
        
        docNs = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse("data/test/conf.xml");
        elemNs = docNs.getDocumentElement();
        
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        
        engine.close();
    }
    
    public final void testAttrNSImplInvalidQNameFail() {
        try {
            docNs.createAttributeNS("http://", "ma:nu:el");
            assertTrue(false);
        } catch (DOMException e) {
            assertEquals(DOMException.INVALID_CHARACTER_ERR, e.code);
        }
        
        try {
            docNs.createAttributeNS(null, "ma:nu:el");
            assertTrue(false);
        } catch (DOMException e) {
            assertEquals(DOMException.INVALID_CHARACTER_ERR, e.code);
        }
        try {
            docNs.createAttributeNS(null, "man:uel");
            assertTrue(false);
        } catch (DOMException e) {
            assertEquals(DOMException.NAMESPACE_ERR, e.code);
        }
    }

    public final void testGetNodeName() throws Exception {
        Attr attr = elemNs.getAttributeNode("version");
        assertEquals("version", attr.getNodeName());
        
        attr = docNs.createAttributeNS("http://xplib.de", "man:uel");
        assertEquals("man:uel", attr.getNodeName());
    }

    public final void testGetNamespaceURI() {
        Attr attr = elemNs.getAttributeNode("version");
        assertNull(attr.getNamespaceURI());
        
        attr = docNs.createAttributeNS("http://xplib.de", "man:uel");
        assertEquals("http://xplib.de", attr.getNamespaceURI());
    }

    public final void testGetPrefix() {
        Attr attr = elemNs.getAttributeNode("version");
        assertNull(attr.getPrefix());
        
        attr = docNs.createAttributeNS("http://xplib.de", "man:uel");
        assertEquals("man", attr.getPrefix());
    }

    public final void testSetPrefix() {
        Attr attr = elemNs.getAttributeNode("version");
        attr.setPrefix("foo");
        assertNull(attr.getPrefix());
        
        attr = docNs.createAttributeNS("http://xplib.de", "man:uel");
        attr.setPrefix("foo");
        assertEquals("foo", attr.getPrefix());
    }

    public final void testGetLocalName() {
        Attr attr = elemNs.getAttributeNode("version");
        assertNull(attr.getLocalName());
        
        attr = docNs.createAttributeNS("http://xplib.de", "man:uel");
        assertEquals("uel", attr.getLocalName());
    }

    public final void testCloneNode() {
        Attr attr = elemNs.getAttributeNode("version");
        Node node = attr.cloneNode(true); 
        assertEquals(attr.getNodeName(), node.getNodeName());
        assertEquals(attr.getNodeType(), node.getNodeType());
        assertEquals(attr.getNodeValue(), node.getNodeValue());
        
        attr = docNs.createAttributeNS("http://xplib.de", "man:uel");
        node = attr.cloneNode(true); 
        assertEquals(attr.getNodeName(), node.getNodeName());
        assertEquals(attr.getNodeType(), node.getNodeType());
        assertEquals(attr.getNodeValue(), node.getNodeValue());
        assertEquals(attr.getLocalName(), node.getLocalName());
        assertEquals(attr.getNamespaceURI(), node.getNamespaceURI());
    }

    public final void testGetNodeValue() {
        assertEquals("2.0", elemNs.getAttributeNode("version").getNodeValue());
    }

    public final void testSetNodeValue() {
        
        Attr attr = elemNs.getAttributeNode("version");
        attr.setNodeValue("3.0");
        
        assertEquals("3.0", attr.getNodeValue());
    }

    public final void testGetName() {
        Attr attr = elemNs.getAttributeNode("version");
        assertEquals("version", attr.getName());
        assertEquals(attr.getName(), attr.getNodeName());
        
        attr = docNs.createAttributeNS("http://xplib.de", "man:uel");
        assertEquals("man:uel", attr.getName());
        assertEquals(attr.getName(), attr.getNodeName());
    }

    public final void testGetSpecified() {
        Attr attr = elemNs.getAttributeNode("version");
        assertTrue(attr.getSpecified());
        
        attr = docNs.createAttributeNS("http://xplib.de", "man:uel");
        assertTrue(attr.getSpecified());
    }

    public final void testGetValue() {
        Attr attr = elemNs.getAttributeNode("version");
        assertEquals("2.0", attr.getValue());
        assertEquals(attr.getValue(), attr.getNodeValue());
    }

    public final void testSetValue() {
        Attr attr = elemNs.getAttributeNode("version");
        attr.setValue("3.0");
        assertEquals("3.0", attr.getValue());
        assertEquals(attr.getValue(), attr.getNodeValue());
    }

    public final void testGetOwnerElement() {
        Attr attr = elemNs.getAttributeNode("version");
        assertSame(docNs.getDocumentElement(), attr.getOwnerElement());
        
        attr = doc.createAttributeNS("http://xplib.de", "manu:el");
        assertNull(attr.getOwnerElement());
        
        doc.getDocumentElement().setAttributeNode(attr);
        assertSame(doc.getDocumentElement(), attr.getOwnerElement());
    }

    public final void testGetTextContent() {
        AttrImpl attr = (AttrImpl) elemNs.getAttributeNode("version");
        
        
        assertEquals("2.0", attr.getTextContent());
        attr.setValue("3.0");
        assertEquals("3.0", attr.getTextContent());
    }

    public final void testIsId() {
        AttrImpl attr = (AttrImpl) elemNs.getAttributeNode("version");
        assertFalse(attr.isId());
        
        attr = (AttrImpl) elemNs.getAttributeNode("id");
        assertTrue(attr.isId());
    }

}
