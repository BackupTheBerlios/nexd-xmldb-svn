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
 * $Log: NodeTestCase.java,v $
 * Revision 1.1  2005/05/11 17:31:39  nexd
 * Refactoring and extended test cases
 *
 */
package tests.xml;

import junit.framework.TestCase;

import org.sixdml.dbmanagement.SixdmlCollection;
import org.sixdml.dbmanagement.SixdmlResource;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import de.xplib.nexd.comm.NEXDEngineI;
import de.xplib.nexd.engine.NEXDEnginePool;
import de.xplib.nexd.engine.store.NestedSetIId;
import de.xplib.nexd.engine.xml.dom.AbstractNode;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class NodeTestCase extends TestCase {
    
    private NEXDEngineI engine;
    
    private Document doc;
    
    private CharacterData charData;
    
    private Node node;
    
    private Comment comment;

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        
        engine = NEXDEnginePool.getInstance().getEngine();
        engine.open("sa", "");
        
        SixdmlCollection coll = engine.queryCollection("/db/docs");
        
        doc = (Document) ((SixdmlResource) engine.queryResource(coll, "document.xml")).getContentAsDOM();
        
        charData = (CharacterData) doc.getDocumentElement().getFirstChild().getNextSibling().getNextSibling().getNextSibling().getFirstChild().getNextSibling().getFirstChild();
        
        node = doc.getFirstChild();
        
        comment = (Comment) doc.getDocumentElement().getChildNodes().item(1);
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        
        engine.close();
    }

    public final void testGetNodeName() {
        assertEquals("#document", doc.getNodeName());
        assertEquals("#text", charData.getNodeName());
        assertEquals("#comment", node.getNodeName());
        assertEquals("#comment", comment.getNodeName());
    }

    public final void testGetNodeValue() {
        assertNull(doc.getNodeValue());
        assertEquals("MySQL", charData.getNodeValue());
        assertTrue(node.getNodeValue().length() > 0);
        assertEquals(" Data source for the Storage bc_storage ", comment.getNodeValue());
    }

    public final void testSetNodeValue() {
        doc.setNodeValue("FooBar");
        assertNull(doc.getNodeValue());
    }

    public final void testGetNodeType() {
        assertEquals(Node.DOCUMENT_NODE, doc.getNodeType());
        assertEquals(Node.TEXT_NODE, charData.getNodeType());
        assertEquals(Node.COMMENT_NODE, node.getNodeType());
        assertEquals(Node.COMMENT_NODE, comment.getNodeType());
    }

    public final void testGetParentNode() {
        assertSame(doc, node.getParentNode());
        assertSame(doc.getDocumentElement(), comment.getParentNode());
        assertNull(doc.getParentNode());
    }

    public final void testGetChildNodes() {
        assertSame(comment.getChildNodes(), node.getChildNodes());
        assertSame(node.getChildNodes(), charData.getChildNodes());
    }

    public final void testGetFirstChild() {
        assertNull(comment.getFirstChild());
        assertNull(node.getFirstChild());
        assertNull(charData.getFirstChild());
        assertNotNull(doc.getFirstChild());
    }

    public final void testGetLastChild() {
        assertNull(comment.getLastChild());
        assertNull(node.getLastChild());
        assertNull(charData.getLastChild());
        assertNotNull(doc.getLastChild());
    }

    public final void testGetPreviousSibling() {
        assertSame(doc.getDocumentElement().getPreviousSibling(), node);
        assertNull(node.getPreviousSibling());
        assertTrue(comment.getPreviousSibling() instanceof Text);
        assertNull(charData.getPreviousSibling());
        assertNull(doc.getPreviousSibling());
    }

    public final void testGetNextSibling() {
        assertSame(node.getNextSibling(), doc.getDocumentElement());
        assertTrue(comment.getNextSibling() instanceof Text);
        assertNull(charData.getNextSibling());
        assertNull(doc.getNextSibling());
    }

    public final void testGetAttributes() {
        assertNull(node.getAttributes());
        assertNull(comment.getAttributes());
        assertNull(charData.getAttributes());
        assertNull(doc.getAttributes());
    }

    public final void testGetOwnerDocument() {
        assertSame(doc, node.getOwnerDocument());
        assertSame(doc, comment.getOwnerDocument());
        assertSame(doc, charData.getOwnerDocument());
        assertNull(doc.getOwnerDocument());
    }

    public final void testInsertBefore() {
        try {
            node.insertBefore(doc.createTextNode("foo"), null);
            assertTrue(false);
        } catch (DOMException e) {
            assertEquals(DOMException.HIERARCHY_REQUEST_ERR, e.code);
        }
        try {
            comment.insertBefore(doc.createTextNode("foo"), null);
            assertTrue(false);
        } catch (DOMException e) {
            assertEquals(DOMException.HIERARCHY_REQUEST_ERR, e.code);
        }
        try {
            charData.insertBefore(doc.createTextNode("foo"), null);
            assertTrue(false);
        } catch (DOMException e) {
            assertEquals(DOMException.HIERARCHY_REQUEST_ERR, e.code);
        }
        
        doc.insertBefore(doc.createComment("Manuel Pichler"), node);
    }

    public final void testReplaceChild() {
        try {
            node.replaceChild(doc.createComment("Manuel Pichler"), null);
            assertTrue(false);
        } catch (DOMException e) {
            assertEquals(DOMException.HIERARCHY_REQUEST_ERR, e.code);
        }
        try {
            comment.replaceChild(doc.createComment("Manuel Pichler"), null);
            assertTrue(false);
        } catch (DOMException e) {
            assertEquals(DOMException.HIERARCHY_REQUEST_ERR, e.code);
        }
        try {
            charData.replaceChild(doc.createComment("Manuel Pichler"), null);
            assertTrue(false);
        } catch (DOMException e) {
            assertEquals(DOMException.HIERARCHY_REQUEST_ERR, e.code);
        }
        
        try {
            doc.replaceChild(doc.createComment("Manuel Pichler"), null);
            assertTrue(false);
        } catch (DOMException e) {
            assertEquals(DOMException.NOT_FOUND_ERR, e.code);
        }
        
        try {
            doc.replaceChild(doc.createComment("Manuel Pichler"), charData);
            assertTrue(false);
        } catch (DOMException e) {
            assertEquals(DOMException.NOT_FOUND_ERR, e.code);
        }
        
        doc.replaceChild(doc.createComment("Manuel Pichler"), node);
        
        Node parent = charData.getParentNode();
        
        parent.replaceChild(comment, charData);
        assertSame(parent, comment.getParentNode());
        assertNull(charData.getParentNode());
    }

    public final void testRemoveChild() {
        try {
            node.appendChild(doc.createComment("Manuel Pichler"));
            assertTrue(false);
        } catch (DOMException e) {
            assertEquals(DOMException.HIERARCHY_REQUEST_ERR, e.code);
        }
        try {
            comment.appendChild(doc.createComment("Manuel Pichler"));
            assertTrue(false);
        } catch (DOMException e) {
            assertEquals(DOMException.HIERARCHY_REQUEST_ERR, e.code);
        }
        try {
            charData.appendChild(doc.createComment("Manuel Pichler"));
            assertTrue(false);
        } catch (DOMException e) {
            assertEquals(DOMException.HIERARCHY_REQUEST_ERR, e.code);
        }
        
        doc.appendChild(doc.createComment("Manuel Pichler"));
    }

    public final void testAppendChild() {
        try {
            node.appendChild(doc.createComment("Manuel"));
            assertTrue(false);
        } catch (DOMException e) {
            assertEquals(DOMException.HIERARCHY_REQUEST_ERR, e.code);
        }
        try {
            comment.appendChild(doc.createComment("Manuel"));
            assertTrue(false);
        } catch (DOMException e) {
            assertEquals(DOMException.HIERARCHY_REQUEST_ERR, e.code);
        }
        try {
            charData.appendChild(doc.createComment("Manuel"));
            assertTrue(false);
        } catch (DOMException e) {
            assertEquals(DOMException.HIERARCHY_REQUEST_ERR, e.code);
        }
        
        doc.appendChild(doc.createComment("Manuel "));
        doc.appendChild(comment);
        
        assertSame(doc, comment.getParentNode());
    }

    public final void testHasChildNodes() {
        assertFalse(node.hasChildNodes());
        assertFalse(comment.hasChildNodes());
        assertFalse(charData.hasChildNodes());
        assertTrue(doc.hasChildNodes());
    }

    public final void testNormalize() {
       
        Node parent = charData.getParentNode();
        assertEquals(1, parent.getChildNodes().getLength());
        
        parent.appendChild(doc.createTextNode(" Database"));
        assertEquals(2, parent.getChildNodes().getLength());
        
        doc.normalize();
        assertEquals(1, parent.getChildNodes().getLength());
        assertEquals("MySQL Database", parent.getChildNodes().item(0).getNodeValue());
    }

    public final void testIsSupported() {
        assertFalse(node.isSupported("Foo", "bar"));
        assertFalse(comment.isSupported("Foo", "bar"));
        assertFalse(charData.isSupported("Foo", "bar"));
        assertFalse(doc.isSupported("Foo", "bar"));
    }

    public final void testGetNamespaceURI() {
        
        assertNull(node.getNamespaceURI());
        assertNull(comment.getNamespaceURI());
        assertNull(charData.getNamespaceURI());
        assertNull(doc.getNamespaceURI());
        
        System.err.println("Implement namespaces in the database");
        //assertNotNull(doc.getDocumentElement().getNamespaceURI());
    }

    public final void testGetPrefix() {
        assertNull(node.getPrefix());
        assertNull(comment.getPrefix());
        assertNull(charData.getPrefix());
        assertNull(doc.getPrefix());
    }

    public final void testSetPrefix() {
        node.setPrefix("prefix");
        assertNull(node.getPrefix());
        
        comment.setPrefix("prefix");
        assertNull(comment.getPrefix());
        
        charData.setPrefix("prefix");
        assertNull(charData.getPrefix());
        
        doc.setPrefix("prefix");
        assertNull(doc.getPrefix());
    }

    public final void testGetLocalName() {
        assertNull(node.getLocalName());
        assertNull(comment.getLocalName());
        assertNull(charData.getLocalName());
        assertNull(doc.getLocalName());
    }

    public final void testHasAttributes() {
        assertFalse(node.hasAttributes());
        assertFalse(comment.hasAttributes());
        assertFalse(charData.hasAttributes());
        assertFalse(doc.hasAttributes());
        assertTrue(doc.getDocumentElement().hasAttributes());
    }

    public final void testIsSameNode() {
        assertTrue(((AbstractNode) node).isSameNode(node));
        assertFalse(((AbstractNode) node).isSameNode(comment));
        
        assertTrue(((AbstractNode) comment).isSameNode(comment));
        assertFalse(((AbstractNode) comment).isSameNode(charData));
        
        assertTrue(((AbstractNode) charData).isSameNode(charData));
        assertFalse(((AbstractNode) charData).isSameNode(comment));
    }

    public final void testLookupNamespacePrefix() {
        
        assertNull(((AbstractNode) node).lookupNamespacePrefix("http://www.binarycloud.com/ns/conf"));
        assertNull(((AbstractNode) comment).lookupNamespacePrefix("http://www.binarycloud.com/ns/conf"));
        assertNull(((AbstractNode) charData).lookupNamespacePrefix("http://www.binarycloud.com/ns/conf"));
        assertNull(((AbstractNode) doc).lookupNamespacePrefix("http://www.binarycloud.com/ns/conf"));
    }

    public final void testLookupNamespaceURI() {
        assertNull(((AbstractNode) node).lookupNamespaceURI("conf"));
        assertNull(((AbstractNode) comment).lookupNamespaceURI("conf"));
        assertNull(((AbstractNode) charData).lookupNamespaceURI("conf"));
        assertNull(((AbstractNode) doc).lookupNamespaceURI("conf"));
    }

    public final void testGetInternalId() {
        assertTrue(((AbstractNode) node).getInternalId() instanceof NestedSetIId);
        assertTrue(((AbstractNode) comment).getInternalId() instanceof NestedSetIId);
        assertTrue(((AbstractNode) charData).getInternalId() instanceof NestedSetIId);
        assertTrue(((AbstractNode) doc).getInternalId() instanceof NestedSetIId);
        
        assertNull(((AbstractNode) doc.createElement("Manuel")).getInternalId());
    }

    public final void testSetInternalId() {
        
        NestedSetIId iid = new NestedSetIId(1, 2, 3);

        ((AbstractNode) node).setInternalId(iid);
        assertEquals(iid, ((AbstractNode) node).getInternalId());
        
        ((AbstractNode) comment).setInternalId(iid);
        assertEquals(iid, ((AbstractNode) comment).getInternalId());
        
        ((AbstractNode) charData).setInternalId(iid);
        assertEquals(iid, ((AbstractNode) charData).getInternalId());
        
        ((AbstractNode) doc).setInternalId(iid);
        assertEquals(iid, ((AbstractNode) doc).getInternalId());
    }

}
