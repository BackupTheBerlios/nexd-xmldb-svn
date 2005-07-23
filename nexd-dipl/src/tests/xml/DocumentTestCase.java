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
 * $Log: DocumentTestCase.java,v $
 * Revision 1.4  2005/05/11 17:31:39  nexd
 * Refactoring and extended test cases
 *
 * Revision 1.3  2005/03/31 12:08:40  nexd
 * Advanced VCL support
 *
 * Revision 1.2  2005/03/14 12:22:49  nexd
 * Heavy javadoc, checkstyle and eclipse todo-Task session.
 *
 */
package tests.xml;

import java.io.ByteArrayInputStream;

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
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.xplib.nexd.comm.NEXDEngineI;
import de.xplib.nexd.engine.NEXDEnginePool;
import de.xplib.nexd.engine.xml.dom.DocumentImpl;
import de.xplib.nexd.engine.xml.jaxp.DocumentBuilderFactoryImpl;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.4 $
 */
public class DocumentTestCase extends TestCase {
    
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
    
    public final void testCacheDelete() {
        
        Element elem = doc.createElement("manuel");
        doc.getDocumentElement().appendChild(elem);
        assertEquals(1, ((DocumentImpl) doc).getCache().getInserted().getLength());
        assertEquals(0, ((DocumentImpl) doc).getCache().getChanged().getLength());
        assertEquals(0, ((DocumentImpl) doc).getCache().getDeleted().getLength());
        
        doc.getDocumentElement().removeChild(elem);
        assertEquals(0, ((DocumentImpl) doc).getCache().getInserted().getLength());
        assertEquals(0, ((DocumentImpl) doc).getCache().getChanged().getLength());
        assertEquals(0, ((DocumentImpl) doc).getCache().getDeleted().getLength());
        
        doc.removeChild(doc.getDocumentElement());
        assertEquals(0, ((DocumentImpl) doc).getCache().getInserted().getLength());
        assertEquals(0, ((DocumentImpl) doc).getCache().getChanged().getLength());
        assertEquals(1, ((DocumentImpl) doc).getCache().getDeleted().getLength());
    }
    
    public final void testCacheChange() {
        
        Element elem = doc.createElement("manuel");
        doc.getDocumentElement().appendChild(elem);
        assertEquals(1, ((DocumentImpl) doc).getCache().getInserted().getLength());
        assertEquals(0, ((DocumentImpl) doc).getCache().getChanged().getLength());
        assertEquals(0, ((DocumentImpl) doc).getCache().getDeleted().getLength());
        
        Element next = doc.createElement("pichler");
        
        doc.getDocumentElement().replaceChild(next, elem);
        assertEquals(2, ((DocumentImpl) doc).getCache().getInserted().getLength());
        assertEquals(0, ((DocumentImpl) doc).getCache().getChanged().getLength());
        assertEquals(0, ((DocumentImpl) doc).getCache().getDeleted().getLength());
        
        doc.replaceChild(elem, doc.getDocumentElement());
        assertEquals(3, ((DocumentImpl) doc).getCache().getInserted().getLength());
        assertEquals(0, ((DocumentImpl) doc).getCache().getChanged().getLength());
        assertEquals(2, ((DocumentImpl) doc).getCache().getDeleted().getLength());
    }

    public final void testAcceptNode() {
    }

    public final void testCloneNode() {
    }

    public final void testGetDoctype() {
    }

    public final void testGetImplementation() {
    }

    public final void testGetDocumentElement() {
    }

    public final void testCreateElement() {
    }

    public final void testCreateDocumentFragment() {
        doc.createDocumentFragment();
        System.err.println("Implement DocumentFragment");
    }

    public final void testCreateTextNode() {
    }

    public final void testCreateComment() {
    }

    public final void testCreateCDATASection() {
    }

    public final void testCreateProcessingInstruction() {
    }

    public final void testCreateAttribute() {
    }

    public final void testCreateEntityReference() {
    }

    public final void testGetElementsByTagNameNonDocElem() throws Exception {
        
        Document doc = DocumentBuilderFactoryImpl.newInstance().newDocumentBuilder().newDocument();
        NodeList nl  = doc.getElementsByTagName("*");
        
        assertNotNull(nl);
        assertTrue(nl instanceof NodeList);
        assertEquals(0, nl.getLength());
    }
    
    public final void testGetElementsByTagnameAnyElement() throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        
        Document doc = db.parse("file:///home/devel/workspace/nexd/data/test/package.xml");

        NodeList nl = doc.getElementsByTagName("*");
        assertNotNull(nl);
        assertTrue(nl instanceof NodeList);
        assertTrue(nl.getLength() > 0);
        assertSame(doc.getDocumentElement(), nl.item(0));
    }

    public final void testImportNode() throws Exception {
        
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        
        Document doc = db.parse("file:///home/devel/workspace/nexd/data/test/package.xml");
        NodeList nl  = doc.getElementsByTagName("notes");
        
        for (int i = 0; i < nl.getLength(); i++) {
        //    System.out.println(nl.item(i).getFirstChild().getNextSibling().getNodeType());
        }
    }
    
    public final void testImportNodeNullNodeFail() throws Exception {
        
        DocumentBuilderFactory dbf = DocumentBuilderFactoryImpl.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        
        Document doc = db.parse("file:///home/devel/workspace/nexd/data/test/package.xml");
        
        try {
            doc.importNode(null, true);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }
    
    public final void testImportNodesSuccess() throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactoryImpl.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        
        Document imp = db.parse(new ByteArrayInputStream((
                "<!DOCTYPE doc [" +
                "  <!ENTITY my-entity \"Test my entity\">" +
                "]>" +
                "<doc>" +
                "  <![CDATA[My CDATA Section]]>" +
                "  <!-- My Comment -->" +
                "  <?my processing instruction ?>" +
                "  &my-entity;" +
                "</doc>"
                ).getBytes()));
        
        Document doc = db.parse("file:///home/devel/workspace/nexd/data/test/package.xml");
        
        NodeList nl = imp.getDocumentElement().getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node n = doc.importNode(nl.item(i), true);
            doc.getDocumentElement().appendChild(n);
        }
        
        Attr attr = imp.createAttributeNS("http://xplib.de", "xplib:attr");
        doc.getDocumentElement().setAttributeNode((Attr) doc.importNode(attr, true));
    }
    
    public final void testInportNodesWithDoctypeSuccess() throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        
        Document doc = db.parse("file:///home/devel/workspace/nexd/data/test/package.xml");
        
        Document d = db.newDocument();
        
        for (int i = 0; i < doc.getChildNodes().getLength(); i++) {
            Node n = d.importNode(doc.getChildNodes().item(i), true);
        }
    }

    public final void testCreateElementNS() {
    }

    public final void testCreateAttributeNS() {
    }

    public final void testGetElementsByTagNameNSEmptyRootSuccess() throws Exception {
        
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        NodeList nl  = doc.getElementsByTagNameNS("http://xplib.de", "manu");
        
        assertNotNull(nl);
        assertTrue(nl instanceof NodeList);
        assertEquals(0, nl.getLength());
    }
    
    public final void testGetElementsByTagNameNSAnyElementSuccess() throws Exception {
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(
                new ByteArrayInputStream((
                        "<foo>" +
                        "  <m:bar xmlns:m=\"http://xplib.de\">" +
                        "    <m:foobar />" +
                        "    <d:foobar xmlns:d=\"http://de.xyplib\" />" +
                        "    <foobar id=\"manuel\" />" +
                        "    <m:barfoo />" +
                        "  </m:bar>" +
                        "</foo>").getBytes()));
        
        NodeList nl = doc.getElementsByTagNameNS("*", "foobar");
        assertNotNull(nl);
        assertTrue(nl instanceof NodeList);
        assertEquals(2, nl.getLength());
        
        nl = doc.getElementsByTagNameNS("http://xplib.de", "*");
        assertNotNull(nl);
        assertTrue(nl instanceof NodeList);
        assertEquals(3, nl.getLength());
        
        nl = doc.getElementsByTagNameNS("*", "*");
        assertNotNull(nl);
        assertTrue(nl instanceof NodeList);
        assertTrue(nl.getLength() > 3);
    }

    public final void testGetElementByIdSuccess() throws Exception {
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(
                new ByteArrayInputStream((
                        "<foo>" +
                        "  <m:bar xmlns:m=\"http://xplib.de\">" +
                        "    <m:foobar />" +
                        "    <d:foobar xmlns:d=\"http://de.xyplib\" />" +
                        "    <foobar id=\"manuelpichler\" />" +
                        "    <m:barfoo />" +
                        "    <?pi pi ?>" +
                        "  </m:bar>" +
                        "</foo>").getBytes()));
        
        Element elem = doc.getElementById("manuelpichler");
        assertNotNull(elem);
        assertTrue(elem instanceof Element);
        assertEquals("manuelpichler", elem.getAttribute("id"));
        
        elem = doc.getElementById("manuelhesse");
        assertNull(elem);
    }
    
    public final void testGetElementByIdRootSuccess() throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        
        Document doc = db.parse("file:///home/devel/workspace/nexd/data/test/conf.xml");
        
        Element elem = doc.getElementById("datasources");
        assertSame(doc.getDocumentElement(), elem);
    }

    public final void testGetActualEncoding() {
    }

    public final void testSetActualEncoding() {
    }

    public final void testGetEncoding() {
    }

    public final void testSetEncoding() {
    }

    public final void testGetStandalone() {
    }

    public final void testSetStandalone() {
    }

    public final void testGetVersion() {
    }

    public final void testSetVersionGreater10Fail() throws Exception {
        DocumentImpl doc = (DocumentImpl) DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        
        try {
            doc.setVersion("1.1");
            assertTrue(false);
        } catch (DOMException e) {
            assertEquals(DOMException.NOT_SUPPORTED_ERR, e.code);
        }
    }
    
    public final void testSetVersionSuccess() throws Exception {
        DocumentImpl doc = (DocumentImpl) DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        doc.setVersion("1.0");
    }

    public final void testGetStrictErrorChecking() {
    }

    public final void testSetStrictErrorChecking() {
    }

    public final void testGetErrorHandler() {
    }

    public final void testSetErrorHandler() {
    }

    public final void testGetDocumentURI() throws Exception {
        
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        DocumentImpl doc = (DocumentImpl) db.parse("file:///home/devel/workspace/nexd/data/test/package.xml");
        
        assertTrue(doc.getDocumentURI().endsWith("package.xml"));
    }

    public final void testSetDocumentURI() {
    }

    public final void testAdoptNodeNotSupportedFail() throws Exception {
        
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        DocumentImpl doc1 = (DocumentImpl) db.parse("file:///home/devel/workspace/nexd/data/test/package.xml");
        
        DocumentImpl doc2 = (DocumentImpl) db.parse("file:///home/devel/workspace/nexd/data/test/package.xml");
        
        try {
            doc1.adoptNode(doc2.getDocumentElement());
            assertTrue(false);
        } catch (DOMException e) {
            assertEquals(DOMException.NOT_SUPPORTED_ERR, e.code);
        }
    }

    public final void testNormalizeDocument() throws Exception {
        
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        DocumentImpl doc1 = (DocumentImpl) db.parse("file:///home/devel/workspace/nexd/data/test/package.xml");
        
        doc1.normalizeDocument();
    }

    public final void testCanSetNormalizationFeature() throws Exception {
        
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        DocumentImpl doc1 = (DocumentImpl) db.parse("file:///home/devel/workspace/nexd/data/test/package.xml");
        
        assertFalse(doc1.canSetNormalizationFeature("foo", true));
    }

    public final void testSetNormalizationFeatureNotSupportedFail() throws Exception {
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        DocumentImpl doc1 = (DocumentImpl) db.parse("file:///home/devel/workspace/nexd/data/test/package.xml");
        
        try {
            doc1.setNormalizationFeature("text", true);
            assertTrue(false);
        } catch (DOMException e) {
            assertEquals(DOMException.NOT_SUPPORTED_ERR, e.code);
        }
    }

    public final void testGetNormalizationFeatureFalseSuccess() throws Exception {
        assertFalse(((DocumentImpl) DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument()).getNormalizationFeature("foo"));
    }

    public final void testRenameNodeElementSuccess() throws Exception {
        DocumentImpl doc = (DocumentImpl) DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(
                new ByteArrayInputStream((
                        "<foo>" +
                        "  <m:bar xmlns:m=\"http://xplib.de\">" +
                        "    <m:foobar />" +
                        "    <d:foobar xmlns:d=\"http://de.xyplib\" />" +
                        "    <foobar id=\"manuelpichler\" />" +
                        "    <m:barfoo />" +
                        "    <?pi pi ?>" +
                        "  </m:bar>" +
                        "</foo>").getBytes()));
        
        Node n = doc.getElementsByTagName("m:barfoo").item(0);
        n = doc.renameNode(n, null, "foobar");
        
        assertEquals("foobar", n.getNodeName());
        assertEquals(2, doc.getElementsByTagName("foobar").getLength());
        assertEquals(0, doc.getElementsByTagName("m:barfoo").getLength());
    }

    public final void testRenameNodeAttrSuccess() throws Exception {
        DocumentImpl doc = (DocumentImpl) DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(
                new ByteArrayInputStream((
                        "<foo>" +
                        "  <m:bar xmlns:m=\"http://xplib.de\">" +
                        "    <m:foobar />" +
                        "    <d:foobar xmlns:d=\"http://de.xyplib\" />" +
                        "    <foobar id=\"manuelpichler\" />" +
                        "    <m:barfoo />" +
                        "    <?pi pi ?>" +
                        "  </m:bar>" +
                        "</foo>").getBytes()));
        
        Node n = ((Element) doc.getElementsByTagName("foobar").item(0)).getAttributeNode("id");
        
        n = doc.renameNode(n, null, "foobar");
        assertNull(((Element) doc.getElementsByTagName("foobar").item(0)).getAttributeNode("id"));
        assertNotNull(((Element) doc.getElementsByTagName("foobar").item(0)).getAttributeNode("foobar"));
    }
    
    public final void testRenameNodeNotElemOrAttrFail() throws Exception {
        DocumentImpl doc = (DocumentImpl) DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(
                new ByteArrayInputStream((
                        "<foo>" +
                        "  <m:bar xmlns:m=\"http://xplib.de\">" +
                        "    <m:foobar />" +
                        "    <d:foobar xmlns:d=\"http://de.xyplib\" />" +
                        "    <foobar id=\"manuelpichler\" />" +
                        "    <m:barfoo />" +
                        "    <?pi pi ?>" +
                        "  </m:bar>" +
                        "</foo>").getBytes()));
        
        Node pi = doc.getElementsByTagName("m:barfoo").item(0).getNextSibling().getNextSibling();
        
        try {
            doc.renameNode(pi, null, "dontwork");
            assertTrue(false);
        } catch (DOMException e) {
            assertEquals(DOMException.NOT_SUPPORTED_ERR, e.code);
        }
    }
    
    public void testInsertSecondDTDFail() throws Exception {
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        
        Document doc = db.parse("file:///home/devel/workspace/nexd/data/test/package.xml");
        
        DocumentType dtd = db.getDOMImplementation().createDocumentType("package", "FOO", "file:///home/manuel/workspace/nexd/data/test/dtd/package.dtd");
        
        try {
            doc.appendChild(dtd);
            assertTrue(false);
        } catch (DOMException e) {
            assertEquals(DOMException.HIERARCHY_REQUEST_ERR, e.code);
        }
    }
    
    public final void testInsertSecondDocElemFail() throws Exception {
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        
        Document doc = db.parse("file:///home/devel/workspace/nexd/data/test/package.xml");
        Element elem = doc.createElement("failElem");
        
        try {
            doc.appendChild(elem);
            assertTrue(false);
        } catch (DOMException e) {
            assertEquals(DOMException.HIERARCHY_REQUEST_ERR, e.code);
        }
    }

    public final void testGetDomConfig() {
    }

    public final void testGetInputEncoding() {
    }

    public final void testGetXmlEncoding() {
    }

    public final void testGetXmlStandalone() {
    }

    public final void testGetXmlVersion() {
    }

    public final void testSetXmlStandalone() {
    }

    public final void testSetXmlVersion() {
    }

    public final void testGetResourceId() {
    }

    public final void testSetResourceId() {
    }

    public final void testGetResourceType() {
    }
    
    public final void testAppendWrongChildFail() throws Exception  {
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        
        Document doc = db.parse("file:///home/devel/workspace/nexd/data/test/package.xml");
        Attr attr = doc.createAttribute("failElem");
        
        try {
            doc.appendChild(attr);
            assertTrue(false);
        } catch (DOMException e) {
            assertEquals(DOMException.HIERARCHY_REQUEST_ERR, e.code);
        }
    }
    
    public final void testAppendWrongDocumentFail() throws Exception {
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        
        Document doc = db.parse("file:///home/devel/workspace/nexd/data/test/package.xml");
        Attr attr = doc.createAttribute("failElem");
        
        Document doc2 = db.newDocument();
        
        try {
            doc2.appendChild(attr);
            assertTrue(false);
        } catch (DOMException e) {
            assertEquals(DOMException.WRONG_DOCUMENT_ERR, e.code);
        }
    }
}
