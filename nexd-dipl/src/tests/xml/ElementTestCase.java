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
 * $Log: ElementTestCase.java,v $
 * Revision 1.1  2005/05/11 17:31:39  nexd
 * Refactoring and extended test cases
 *
 */
package tests.xml;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.TestCase;

import org.sixdml.dbmanagement.SixdmlCollection;
import org.sixdml.dbmanagement.SixdmlResource;
import org.w3c.dom.Attr;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.xplib.nexd.comm.NEXDEngineI;
import de.xplib.nexd.engine.NEXDEnginePool;
import de.xplib.nexd.engine.xml.dom.ElementImpl;
import de.xplib.nexd.engine.xml.jaxp.DocumentBuilderFactoryImpl;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class ElementTestCase extends TestCase {
    
    private NEXDEngineI engine;
    
    private Document doc;
    
    private Document docNs;
    
    private CharacterData charData;
    
    private Node node;
    
    private Comment comment;

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        
        System.setProperty("javax.xml.parsers.DocumentBuilderFactory",
        "de.xplib.nexd.engine.xml.jaxp.DocumentBuilderFactoryImpl");
        
        engine = NEXDEnginePool.getInstance().getEngine();
        engine.open("sa", "");
        
        SixdmlCollection coll = engine.queryCollection("/db/docs");
        
        doc = (Document) ((SixdmlResource) engine.queryResource(coll, "document.xml")).getContentAsDOM();
        
        charData = (CharacterData) doc.getDocumentElement().getFirstChild().getNextSibling().getNextSibling().getNextSibling().getFirstChild().getNextSibling().getFirstChild();
        
        node = doc.getFirstChild();
        
        comment = (Comment) doc.getDocumentElement().getChildNodes().item(1);
        
        DocumentBuilderFactory dbf = DocumentBuilderFactoryImpl.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        
        docNs  = db.parse("file:///home/devel/workspace/nexd/data/test/conf.xml");
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        
        engine.close();
    }

    public final void testGetNodeName() {
        
        assertEquals("conf:domain", doc.getDocumentElement().getNodeName());
        assertEquals("conf:domain", docNs.getDocumentElement().getNodeName());
    }

    public final void testGetNamespaceURI() {
        assertNull(doc.getDocumentElement().getNamespaceURI());
        assertEquals("http://www.binarycloud.com/ns/conf", docNs.getDocumentElement().getNamespaceURI());
    }

    public final void testGetPrefix() {
        assertNull(doc.getDocumentElement().getPrefix());
        assertEquals("conf", docNs.getDocumentElement().getPrefix());
    }

    public final void testSetPrefix() {
        doc.getDocumentElement().setPrefix("manuel");
        assertNull(doc.getDocumentElement().getPrefix());
        
        docNs.getDocumentElement().setPrefix("manuel");
        assertEquals("manuel", docNs.getDocumentElement().getPrefix());
    }

    public final void testGetLocalName() {
        assertNull(doc.getDocumentElement().getLocalName());
        assertEquals("domain", docNs.getDocumentElement().getLocalName());
    }

    public final void testElementNSImplFail() {
        
        try {
            doc.createElementNS("http://foo", "f:o:o");
            assertTrue(false);
        } catch (DOMException e) {
            assertEquals(DOMException.INVALID_CHARACTER_ERR, e.code);
        }
        try {
            doc.createElementNS("http://foo", "xml:foo");
            assertTrue(false);
        } catch (DOMException e) {
            assertEquals(DOMException.NAMESPACE_ERR, e.code);
        }
        try {
            doc.createElementNS("http://foo", "xmlns:foo");
            assertTrue(false);
        } catch (DOMException e) {
            assertEquals(DOMException.NAMESPACE_ERR, e.code);
        }
        try {
            doc.createElementNS(null, "xmlns:foo");
            assertTrue(false);
        } catch (DOMException e) {
            assertEquals(DOMException.NAMESPACE_ERR, e.code);
        }
        try {
            doc.createElementNS("http://www.w3.org/2000/xmlns/", "bar:foo");
            assertTrue(false);
        } catch (DOMException e) {
            assertEquals(DOMException.NAMESPACE_ERR, e.code);
        }
        
    }

    public final void testGetAttributes() {
        NamedNodeMap nnm = doc.getDocumentElement().getAttributes();
        assertNotNull(nnm);
        assertTrue(nnm instanceof NamedNodeMap);
        assertEquals(4, nnm.getLength());
    }

    public final void testHasAttributes() {
        assertTrue(doc.getDocumentElement().hasAttributes());
    }

    public final void testGetTagName() {
        assertEquals("conf:domain", doc.getDocumentElement().getTagName());
        assertEquals("conf:domain", docNs.getDocumentElement().getTagName());
    }

    public final void testGetAttribute() {
        String v = doc.getDocumentElement().getAttribute("version");
        assertNotNull(v);
        assertEquals("2.0", v);
        
        v = docNs.getDocumentElement().getAttribute("version");
        assertNotNull(v);
        assertEquals("2.0", v);
    }

    public final void testSetAttribute() {
        doc.getDocumentElement().setAttribute("manuel", "pichler");
        String v = doc.getDocumentElement().getAttribute("manuel");
        assertNotNull(v);
        assertEquals("pichler", v);
        
        docNs.getDocumentElement().setAttribute("manuel", "pichler");
        v = docNs.getDocumentElement().getAttribute("manuel");
        assertNotNull(v);
        assertEquals("pichler", v);
    }

    public final void testRemoveAttribute() {
        doc.getDocumentElement().removeAttribute("version");
        NamedNodeMap nnm = doc.getDocumentElement().getAttributes();
        assertEquals(3, nnm.getLength());
        
        docNs.getDocumentElement().removeAttribute("version");
        nnm = docNs.getDocumentElement().getAttributes();
        assertEquals(2, nnm.getLength());
        /*
        try {
            System.err.println(doc.getDocumentElement().getClass());
            doc.getDocumentElement().removeAttribute("foobar");
            assertTrue(false);
        } catch (DOMException e) {
            assertEquals(DOMException.NOT_FOUND_ERR, e.code);
        }*/
    }

    public final void testGetAttributeNode() {
        Attr attr = doc.getDocumentElement().getAttributeNode("version");
        assertNotNull(attr);
        assertEquals("version", attr.getName());
        assertEquals("2.0", attr.getValue());
        
        attr = docNs.getDocumentElement().getAttributeNode("version");
        assertNotNull(attr);
        assertEquals("version", attr.getName());
        assertEquals("2.0", attr.getValue());
    }

    public final void testSetAttributeNode() {
        Attr attr = doc.createAttribute("manuel");
        attr.setValue("pichler");
        doc.getDocumentElement().setAttributeNode(attr);
        assertEquals(5, doc.getDocumentElement().getAttributes().getLength());
        
        attr = docNs.createAttribute("manuel");
        attr.setValue("pichler");
        docNs.getDocumentElement().setAttributeNode(attr);
        assertEquals(4, docNs.getDocumentElement().getAttributes().getLength());
    }

    public final void testRemoveAttributeNode() {
        
        Attr attr = doc.getDocumentElement().getAttributeNode("version");
        doc.getDocumentElement().removeAttributeNode(attr);
        assertEquals(3, doc.getDocumentElement().getAttributes().getLength());

        attr = docNs.getDocumentElement().getAttributeNode("version");
        docNs.getDocumentElement().removeAttributeNode(attr);
        assertEquals(2, docNs.getDocumentElement().getAttributes().getLength());
    }

    public final void testGetElementsByTagName() {
        NodeList nl1 = doc.getDocumentElement().getElementsByTagName("*");
        NodeList nl2 = docNs.getDocumentElement().getElementsByTagName("*");
        
        assertEquals(nl1.getLength(), nl2.getLength());
    }

    public final void testGetAttributeNS() {
        assertEquals("", doc.getDocumentElement().getAttributeNS("http://www.binarycloud.com/ns/conf", "version"));
        assertEquals("2.0", docNs.getDocumentElement().getAttributeNS("http://www.binarycloud.com/ns/conf", "version"));
    }

    public final void testSetAttributeNS() {
        doc.getDocumentElement().setAttributeNS("http://www.binarycloud.com/ns/conf", "manuel", "pichler");
        assertEquals("pichler", doc.getDocumentElement().getAttributeNS("http://www.binarycloud.com/ns/conf", "manuel"));
    }

    public final void testRemoveAttributeNS() {
        docNs.getDocumentElement().removeAttributeNS("http://www.binarycloud.com/ns/conf", "version");
        assertEquals("", docNs.getDocumentElement().getAttributeNS("http://www.binarycloud.com/ns/conf", "version"));

    }

    public final void testGetAttributeNodeNS() {
        Attr attr = docNs.getDocumentElement().getAttributeNodeNS("http://www.binarycloud.com/ns/conf", "version");
        assertNotNull(attr);
        assertEquals("version", attr.getName());
        assertEquals("2.0", attr.getValue());
    }

    public final void testSetAttributeNodeNS() {

        Attr attr = docNs.getDocumentElement().getAttributeNode("version");
        try {
            docNs.getDocumentElement().setAttributeNodeNS(attr);
            assertTrue(false);
        } catch (DOMException e) {
            assertEquals(DOMException.INUSE_ATTRIBUTE_ERR, e.code);
        }
        
        doc.getDocumentElement().setAttributeNodeNS(doc.createAttributeNS("http://foo", "foo:bar"));
    }

    public final void testGetElementsByTagNameNS() {
        
        NodeList nl = docNs.getDocumentElement().getElementsByTagNameNS("http://www.binarycloud.com/ns/co", "*");
        assertEquals(0, nl.getLength());
        
        nl = docNs.getDocumentElement().getElementsByTagNameNS("http://www.binarycloud.com/ns/conf", "*");
        assertTrue(nl.getLength() > 0);
    }

    public final void testHasAttribute() {
        
        assertTrue(docNs.getDocumentElement().hasAttribute("version"));
        assertFalse(docNs.getDocumentElement().hasAttribute("manuel"));
        
        docNs.getDocumentElement().setAttribute("manuel", "pichler");
        assertTrue(docNs.getDocumentElement().hasAttribute("manuel"));
    }

    public final void testHasAttributeNS() {
        assertFalse(docNs.getDocumentElement().hasAttributeNS("http://xplib.de", "manuel"));
        docNs.getDocumentElement().setAttributeNS("http://xplib.de", "manuel", "test");
        assertTrue(docNs.getDocumentElement().hasAttributeNS("http://xplib.de", "manuel"));
    }
    
    public final void testSetIdAttribute() {
        
        assertNull(docNs.getElementById("mid"));
        ((ElementImpl) docNs.getDocumentElement()).setIdAttribute("manuel", true);
        docNs.getDocumentElement().getAttributeNode("manuel").setValue("mid");
        assertNotNull(docNs.getElementById("mid"));
        assertSame(docNs.getDocumentElement(), docNs.getElementById("mid"));
    }

    public final void testSetIdAttributeNode() {
        assertNull(docNs.getElementById("mid"));
        ((ElementImpl) docNs.getDocumentElement()).setIdAttributeNode(docNs.createAttribute("manuel"), true);
        docNs.getDocumentElement().getAttributeNode("manuel").setValue("mid");
        assertNotNull(docNs.getElementById("mid"));
        assertSame(docNs.getDocumentElement(), docNs.getElementById("mid"));
    }

    public final void testSetIdAttributeNS() {
        assertNull(docNs.getElementById("mid"));
        ((ElementImpl) docNs.getDocumentElement()).setIdAttributeNS("http://xplib.de", "manuel", true);
        docNs.getDocumentElement().getAttributeNode("manuel").setValue("mid");
        assertNotNull(docNs.getElementById("mid"));
        assertSame(docNs.getDocumentElement(), docNs.getElementById("mid"));
    }

}
