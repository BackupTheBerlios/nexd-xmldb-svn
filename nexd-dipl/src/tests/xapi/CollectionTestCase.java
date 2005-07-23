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

/*
 * $Log: CollectionTestCase.java,v $
 * Revision 1.11  2005/05/11 17:31:40  nexd
 * Refactoring and extended test cases
 *
 * Revision 1.10  2005/05/08 11:59:32  nexd
 * restructuring
 *
 * Revision 1.9  2005/04/24 15:00:27  nexd
 * Bugfixes and many performance and coding improvements.
 *
 * Revision 1.8  2005/04/22 14:59:41  nexd
 * SOAP support and performance update.
 *
 * Revision 1.7  2005/03/31 12:08:39  nexd
 * Advanced VCL support
 *
 * Revision 1.6  2005/03/14 12:22:48  nexd
 * Heavy javadoc, checkstyle and eclipse todo-task session.
 *
 */
package tests.xapi;

import java.io.IOException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.TestCase;

import org.sixdml.SixdmlDatabase;
import org.sixdml.dbmanagement.SixdmlCollection;
import org.sixdml.exceptions.DocumentAlreadyExistsException;
import org.sixdml.exceptions.InvalidCollectionDocumentException;
import org.sixdml.exceptions.NonExistentDocumentException;
import org.sixdml.exceptions.NonWellFormedXMLException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.ErrorCodes;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.Service;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.BinaryResource;
import org.xmldb.api.modules.CollectionManagementService;
import org.xmldb.api.modules.XMLResource;

import de.xplib.nexd.api.VirtualCollection;
import de.xplib.nexd.engine.xapi.DatabaseImpl;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.11 $
 */
public class CollectionTestCase extends TestCase {
    
    
    /**
     * Comment for <code>db</code>
     */
    private SixdmlDatabase db = null;
    
    /**
     * Comment for <code>coll</code>
     */
    private SixdmlCollection coll =null;
    
    private VirtualCollection vcoll = null;
    
    /**
     * Comment for <code>dbColl</code>
     */
    private SixdmlCollection dbColl = null;
    
    /**
     * 
     */
    public CollectionTestCase() throws Exception {
        /*
        this.coll = (SixdmlCollection) this.db.getCollection(
                "nexd://localhost./db/docs", "sa", "");
        */
                 
    }

    /**
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        
        String name = "de.xplib.nexd.engine.xapi.DatabaseImpl";
        Class clazz = Class.forName(name);
        
        this.db = (SixdmlDatabase) clazz.newInstance();
        DatabaseManager.registerDatabase(this.db);
        
        this.coll = (SixdmlCollection) this.db.getCollection(
                "nexd://localhost./db/docs", "sa", "");
        
        this.vcoll = (VirtualCollection) this.db.getCollection(
                "nexd://localhost./db/vcl-data/myvc", "sa", "");
        
        this.dbColl = (SixdmlCollection) this.db.getCollection(
                "nexd://localhost./db", "sa", ""); 
        
    }
    
    protected void tearDown() throws Exception {
        super.tearDown();
        
        ((DatabaseImpl) this.db).getEngine().close();
    }

    public final void testSetSchema() throws Exception {
        
        this.coll = (SixdmlCollection) this.db.getCollection(
                "nexd://localhost./db/articles", "sa", "");
        
        URL url = new URL("file:///home/devel/workspace/nexd/data/test/dtd/package.dtd");
        try {
            this.coll.setSchema(url);
            assertFalse(true);
        } catch (InvalidCollectionDocumentException e) {
            //e.printStackTrace();
            assertFalse(false);
        }
        
        try {
            this.coll.insertDocument(
                    "mypackage.xml", 
                    new URL("file:///home/devel/workspace/nexd/data/test/package.xml"));
            assertTrue(false);
        } catch (XMLDBException e) {
            assertTrue(true);
        } catch (DocumentAlreadyExistsException e) {
            assertTrue(true);
        }
        
        this.coll = (SixdmlCollection) this.db.getCollection(
                "nexd://localhost./db/docs/foo", "sa", ""); 
        // insert a new schema
        try {
            this.coll.setSchema(url);
            assertTrue(true);
        } catch (InvalidCollectionDocumentException e) {
            e.printStackTrace();
            assertTrue(false);
        }
        
        // update an existing schema
        try {
            this.coll.setSchema(url);
            assertTrue(true);
        } catch (InvalidCollectionDocumentException e) {
            assertTrue(false);
        }
    }

    public final void testShowSchema() throws Exception {
        
        this.coll = (SixdmlCollection) this.db.getCollection(
                "nexd://localhost./db/articles", "sa", "");
        
        assertNull(this.coll.showSchema());

        this.coll = (SixdmlCollection) this.db.getCollection(
                "nexd://localhost./db/docs/foo", "sa", "");
        this.coll.setSchema(new URL("file:///home/devel/workspace/nexd/data/test/dtd/package.dtd"));
        assertNotNull(this.coll.showSchema());
    }

    public final void testUnsetSchema() throws Exception {
        
        URL url = new URL("file:///home/devel/workspace/nexd/data/test/dtd/package.dtd");
        
        this.coll = (SixdmlCollection) this.db.getCollection(
                "nexd://localhost./db/docs/foo", "sa", ""); 
        try {
            this.coll.setSchema(url);
        } catch (InvalidCollectionDocumentException e) {
            assertTrue("No InvalidCollectionDocumentException expected.", false);
        }
        
        assertNotNull(this.coll.showSchema());
        
        this.coll.unsetSchema();
        assertNull(this.coll.showSchema());
    }

    /*
     * Class under test for void insertDocument(String, String)
     */
    public final void testInsertDocumentStringString() throws Exception {
        
        boolean ok = false;
        try {
            this.coll.insertDocument("foobar", "This is no XML, I expect an NonWellformedException");
        } catch (NonWellFormedXMLException e) {
            ok = true;
        }
        assertTrue(ok);
        
        ok = false;
        try {
            this.coll.insertDocument("document.xml", "<this_document_name_already_exists />");
        } catch (DocumentAlreadyExistsException e1) {
            ok = true;
        }
        assertTrue(ok);
        /*
        URL url = new URL("file:///home/manuel/workspace/nexd/data/test/package.xml");
        
        this.coll = (SixdmlCollection) this.db.getCollection(
                "nexd://localhost./db/articles", "sa", "");
        
        this.coll.insertDocument("package.xml", url);
        */
        
        //Nothing to insert see testStoreResource
    }

    /*
     * Class under test for void insertDocument(String, URL)
     */
    public final void testInsertDocumentStringURL() throws Exception {
        
        URL url = new URL("http://no/xml/file");
        
        boolean ok = false;
        try {
            this.coll.insertDocument("document.xml", url);
        } catch (IOException e) {
            ok = true;
        }
        assertTrue(ok);
    }

    public final void testRemoveDocument() throws Exception {
        
        boolean non = false;
        try {
            this.coll.removeDocument("foobar.xml.xml.xml");
        } catch (NonExistentDocumentException e) {
            non = true;
        }
        assertTrue(non);
        
        //Nothing to remove see testRemoveResource
    }

    public final void testAddIndex() {
    }

    public final void testRemoveIndex() {
    }

    public final void testGetIndices() {
    }

    public final void testGetParentCollection() throws Exception {
        
        Collection parent = this.coll.getParentCollection();
        assertNotNull(parent);
        assertEquals(parent.getName(), "db");
        
        parent = this.dbColl.getParentCollection();
        assertNull(parent);
        
        this.coll.close();
        int code = -1;
        try {
            this.coll.getParentCollection();
        } catch (XMLDBException e) {
            code = e.errorCode;
        }
        assertEquals(code, ErrorCodes.COLLECTION_CLOSED);
    }

    public final void testGetChildCollectionCount() throws Exception {
        
        String[] names = this.coll.listChildCollections();
        assertEquals(names.length, this.coll.getChildCollectionCount());
        
        names = this.dbColl.listChildCollections();
        assertEquals(names.length, this.dbColl.getChildCollectionCount());
        
        this.coll.close();
        
        int code = -1;
        try {
            this.coll.getChildCollectionCount();
        } catch (XMLDBException e) {
            code = e.errorCode;
        }
        assertEquals(code, ErrorCodes.COLLECTION_CLOSED);
    }

    public final void testListChildCollections() throws Exception {
        
        String[] colls = this.coll.listChildCollections();
        
        assertEquals(colls.length, 1);
        assertEquals(colls[0], "foo");
        
        colls = this.dbColl.listChildCollections();
        
        int cnt = 0;
        
        assertEquals(this.dbColl.getChildCollectionCount(), colls.length);
        for (int i = 0; i < colls.length; i++) {
            
            cnt += (colls[i].equals("docs") || colls[i].equals("vcl-data")) ? 1 : 0;
        }
        
        assertEquals("Expected the two collections 'docs' and 'vcl-data'", 2, cnt);
        
        this.dbColl.close();
        int code = -1;
        try {
            this.dbColl.listChildCollections();
        } catch (XMLDBException e) {
            code = e.errorCode;
        }
        assertEquals(code, ErrorCodes.COLLECTION_CLOSED);
    }

    public final void testGetChildCollection() throws Exception {
        
        Collection child = this.coll.getChildCollection("foo");
        assertNotNull(child);
        assertEquals(child.getName(), "foo");
        
        child = this.coll.getChildCollection("bar");
        assertNull(child);
        
        this.coll.close();
        int code = -1;
        try {
            this.coll.getChildCollection("foo");
        } catch (XMLDBException e) {
            code = e.errorCode;
        }
        assertEquals(code, ErrorCodes.COLLECTION_CLOSED);
    }

    public final void testGetResourceCount() throws Exception {
        
        int count = this.coll.getResourceCount();
        assertEquals(this.coll.listResources().length, count);
        
        count = this.dbColl.getResourceCount();
        assertEquals(this.dbColl.listResources().length, count);
    }

    public final void testListResources() throws Exception {
        
        String[] ress = this.coll.listResources();
        
        assertEquals(this.coll.getResourceCount(), ress.length);
        
        boolean ok = false;
        for (int i = 0; i < ress.length; i++) {
            if (ress[i].equals("document.xml")) {
                ok = true;
                break;
            }
        }
        assertTrue(ok);
    }

    public final void testCreateResource() throws Exception {
        
        Resource res = this.coll.createResource("foo", XMLResource.RESOURCE_TYPE);
        
        assertEquals(res.getResourceType(), XMLResource.RESOURCE_TYPE);
        assertEquals(res.getId(), "foo");
        assertTrue(res instanceof XMLResource);
        
        
        res = this.coll.createResource("foo", BinaryResource.RESOURCE_TYPE);
        
        assertEquals(res.getResourceType(), BinaryResource.RESOURCE_TYPE);
        assertEquals(res.getId(), "foo");
        assertTrue(res instanceof BinaryResource);
    }

    public final void testRemoveResource() throws Exception {
        
        String id = "delete.xml";
        
        XMLResource res = (XMLResource) this.coll.createResource(
                id, XMLResource.RESOURCE_TYPE);
                
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder dob = dbf.newDocumentBuilder();
        
        Document dom = dob.parse("data/test/conf.xml");
        
        res.setContentAsDOM(dom);
        /* TODO: Yust if necessary, blows up the database 
        this.coll.storeResource(res);
        
        assertEquals(this.coll.getResourceCount(), 4);
        assertNotNull(this.coll.getResource(id));
        
        this.coll.removeResource(res);
        
        assertEquals(this.coll.getResourceCount(), 3);
        assertNull(this.coll.getResource(id));
        */
    }

    public final void testStoreResource() throws Exception {
        String id = this.coll.createId();
        
        XMLResource res = (XMLResource) this.coll.createResource(
                id, XMLResource.RESOURCE_TYPE);
                
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder dob = dbf.newDocumentBuilder();
        
        Document dom = dob.parse("data/test/package.xml");
        
        res.setContentAsDOM(dom);
        
        //TODO: Blows up the database 
        //this.coll.storeResource(res);
    }
    
    public final void testStoreDocument() throws Exception {
        
        int cnt = coll.getResourceCount();
        
        coll.insertDocument("MyDocId.xml", "<document />");
        assertEquals(cnt + 1, coll.getResourceCount());
        
        coll.removeDocument("MyDocId.xml");
        assertEquals(cnt, coll.getResourceCount());
    }
    
    public final void testStoreDocumentInvalidFail() throws Exception {

        try {
            coll.insertDocument("MyDocId.xml", "</document>");
            assertTrue(false);
        } catch (XMLDBException e) {
        }
    }
    
    
    public final void testGetServices() throws Exception {
        Service[] all = coll.getServices();
        assertEquals(7, all.length);
    }
    
    public final void testGetServicesCloseFail() throws Exception {
        coll.close();
        
        try {
            coll.getServices();
            assertTrue(false);
        } catch (XMLDBException e) {
            assertEquals(ErrorCodes.COLLECTION_CLOSED, e.errorCode);
        }
    }
    
    public void testServiceSixdml() throws Exception {
        assertNotNull(coll.getService("SixdmlCollectionManagementService", "1.0"));
        assertNotNull(coll.getService("XUpdateQueryService", "1.0"));
    }
    
    public final void testGetServiceClosedFail() throws Exception {
        coll.close();
        
        try {
            coll.getService("XPathQueryService", "1.0");
            assertTrue(false);
        } catch (XMLDBException e) {
            assertEquals(ErrorCodes.COLLECTION_CLOSED, e.errorCode);
        }
    }
    
    
    public final void testCreateResourceClosedFail() throws Exception {
        
        coll.close();
        
        try {
            coll.createResource("doo", XMLResource.RESOURCE_TYPE);
            assertTrue(false);
        } catch (XMLDBException e) {
            assertEquals(ErrorCodes.COLLECTION_CLOSED, e.errorCode);
        }
    }
    
    public final void testCreateResourceUnknownTypeFail() throws Exception {
        try {
            coll.createResource("fop", "UnknownResource");
            assertTrue(false);
        } catch (XMLDBException e) {
            assertEquals(ErrorCodes.UNKNOWN_RESOURCE_TYPE, e.errorCode);
        }
    }
    
    public final void testRemoveResourceClosedFail() throws Exception {
        Resource res = coll.getResource("document.xml");
        coll.close();
        
        try {
            coll.removeResource(res);
            assertTrue(false);
        } catch (XMLDBException e) {
            assertEquals(ErrorCodes.COLLECTION_CLOSED, e.errorCode);
        }
    }
    
    public final void testGetResourceClosedFail() throws Exception {
        coll.close();
        
        try {
            coll.getResource("document.xml");
            assertTrue(false);
        } catch (XMLDBException e) {
            assertEquals(ErrorCodes.COLLECTION_CLOSED, e.errorCode);
        }
    }
    
    public void testStoreResourceClosedFail() throws Exception {
        Resource r = coll.createResource("Dummy", XMLResource.RESOURCE_TYPE);
        
        coll.close();
        
        try {
            coll.storeResource(r);
            assertTrue(false);
        } catch (XMLDBException e) {
            assertEquals(ErrorCodes.COLLECTION_CLOSED, e.errorCode);
        }
    }

    public void testGetChildCollectionCountClosedFail() throws Exception {
        coll.close();
        try {
            coll.getChildCollectionCount();
            assertTrue(false);
        } catch (XMLDBException e) {
            assertEquals(ErrorCodes.COLLECTION_CLOSED, e.errorCode);
        }        
        vcoll.close();
        try {
            vcoll.getChildCollectionCount();
            assertTrue(false);
        } catch (XMLDBException e) {
            assertEquals(ErrorCodes.COLLECTION_CLOSED, e.errorCode);
        }
    }
    
    public void testListChildCollectionsClosedFail() throws Exception {
        coll.close();
        try {
            coll.listChildCollections();
            assertTrue(false);
        } catch (XMLDBException e) {
            assertEquals(ErrorCodes.COLLECTION_CLOSED, e.errorCode);
        }        
        vcoll.close();
        try {
            vcoll.listChildCollections();
            assertTrue(false);
        } catch (XMLDBException e) {
            assertEquals(ErrorCodes.COLLECTION_CLOSED, e.errorCode);
        }
    }
    public void testGetChildCollectionClosedFail() throws Exception {
        coll.close();
        try {
            coll.getChildCollection("");
            assertTrue(false);
        } catch (XMLDBException e) {
            assertEquals(ErrorCodes.COLLECTION_CLOSED, e.errorCode);
        }        
        vcoll.close();
        try {
            vcoll.getChildCollection("");
            assertTrue(false);
        } catch (XMLDBException e) {
            assertEquals(ErrorCodes.COLLECTION_CLOSED, e.errorCode);
        }
    }
    
    public void testGetResourceCountClosedFail() throws Exception {
        coll.close();
        try {
            coll.getResourceCount();
            assertTrue(false);
        } catch (XMLDBException e) {
            assertEquals(ErrorCodes.COLLECTION_CLOSED, e.errorCode);
        }
    }
    
    public void testListResourcesClosedFail() throws Exception {
        coll.close();
        try {
            coll.listResources();
            assertTrue(false);
        } catch (XMLDBException e) {
            assertEquals(ErrorCodes.COLLECTION_CLOSED, e.errorCode);
        }
    }
    
    public void testStoreBinResourceNotImplementedFail() throws Exception {
        Resource r = coll.createResource("bin.res", BinaryResource.RESOURCE_TYPE);
            
        try {
            coll.storeResource(r);
            assertTrue(false);
        } catch (XMLDBException e) {
            assertEquals(ErrorCodes.NOT_IMPLEMENTED, e.errorCode);
        }
    }
    
    public void testStoreResourceInvalidTypeFail() throws Exception {
        
        Resource r = new Resource() {
            public String getId() { return "invalid"; }
            public void setContent(Object content) {}
            public Object getContent() {return null;}
            public String getResourceType() {return "InvalidResourceType";}
            public Collection getParentCollection() { return null; }
        };
        
        try {
            coll.storeResource(r);
            assertTrue(false);
        } catch (XMLDBException e) {
            assertEquals(ErrorCodes.INVALID_RESOURCE, e.errorCode);
        }
    }
    
    public void testStoreResourceNoSixdmlFail() throws Exception {
        Resource r = new Resource() {
            public String getId() { return "invalid"; }
            public void setContent(Object content) {}
            public Object getContent() {return null;}
            public String getResourceType() {return XMLResource.RESOURCE_TYPE;}
            public Collection getParentCollection() { return null; }
        };
        
        try {
            coll.storeResource(r);
            assertTrue(false);
        } catch (XMLDBException e) {
            assertEquals(ErrorCodes.INVALID_RESOURCE, e.errorCode);
        }
    }

    public final void testGetResource() throws Exception {
        
        Resource res = this.coll.getResource("binarycloud-entity.xml");
        
        Object o = res.getContent();
        assertTrue("Expected res.getContent() to be a String", o instanceof String);
        
        
        Document doc = (Document) ((XMLResource) res).getContentAsDOM();
        
        Element e = doc.getDocumentElement();
        assertEquals(e.getTagName(), "entity:definition");
        
        String[] attrs = {
                "version", "1.0", "id", "person", "classfile", 
                "app/newstorage/ent/person.php", "datasource", "bc_storage",
                "xmlns:entity", "http://www.binarycloud.com/ns/storage/entity",
                "xmlns", "http://www.binarycloud.com/ns/storage/entity"
        };
        
        for (int i = 0; i < attrs.length; i+=2) {
            assertEquals(e.getAttribute(attrs[i]), attrs[i + 1]);
            assertEquals(e.getAttributeNode(attrs[i]).getValue(), attrs[i + 1]);
            assertEquals(e.getAttributeNode(attrs[i]).getName(), attrs[i]);
        }
        
        NodeList nl = e.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            assertSame(nl.item(i).getParentNode(), e);
            
            if (i > 0) {
                assertSame(nl.item(i).getPreviousSibling(), nl.item(i - 1));
            }
            
            if (nl.item(i).getNodeName().equals("property")) {
                attrs = new String[] {
                        "name", "birthdate", "type", 
                        "date", "not-null", "true"
                };
                
                for (int j = 0; j < attrs.length; j+=2) {
                    assertEquals(((Element) nl.item(i)).getAttribute(attrs[j]), attrs[j + 1]);
                    assertEquals(((Element) nl.item(i)).getAttributeNode(attrs[j]).getValue(), attrs[j + 1]);
                    assertEquals(((Element) nl.item(i)).getAttributeNode(attrs[j]).getName(), attrs[j]);
                }
            }
        }
        
        this.coll = (SixdmlCollection) this.db.getCollection(
                "nexd://localhost./db/docs", "sa", "");
        
        res = this.coll.getResource("namespace2.xml");
        doc = (Document) ((XMLResource) res).getContentAsDOM();
        
        e = doc.getDocumentElement();
        attrs = new String[]{
                "version", "2.0", "id", "main",  "path", 
                "app/formdemo/DemoForm.php", "xmlns", 
                "http://www.binarycloud.com/ns/node", "xmlns:node",
                "http://www.binarycloud.com/ns/node", "xmlns:form",
                "http://www.binarycloud.com/ns/form"
        };
        for (int i = 0; i < attrs.length; i+=2) {
            assertEquals(e.getAttribute(attrs[i]), attrs[i + 1]);
            assertEquals(e.getAttributeNode(attrs[i]).getValue(), attrs[i + 1]);
            assertEquals(e.getAttributeNode(attrs[i]).getName(), attrs[i]);
        }
        
        e = (Element) e.getFirstChild().getNextSibling();
        assertEquals(e.getTagName(), "contains");
        
        nl = e.getChildNodes();
        
        assertEquals(nl.item(5).getNodeType(), Node.COMMENT_NODE);
        assertEquals(nl.item(6).getNodeType(), Node.TEXT_NODE);
        assertEquals(nl.item(7).getNodeType(), Node.COMMENT_NODE);
        assertEquals(nl.item(8).getNodeType(), Node.TEXT_NODE);
        assertEquals(nl.item(9).getNodeName(), "node:definition");
        
        e = (Element) nl.item(9);
        
        attrs = new String[] {
                "id", "opinion", "path", 
                "binarycloud/form/nodes/OptionListFormInput.php"};
        
        for (int i = 0; i < attrs.length; i+=2) {
            assertEquals(e.getAttribute(attrs[i]), attrs[i + 1]);
            assertEquals(e.getAttributeNode(attrs[i]).getValue(), attrs[i + 1]);
            assertEquals(e.getAttributeNode(attrs[i]).getName(), attrs[i]);
        }
        
        nl = e.getChildNodes();
        
        assertEquals(nl.item(17).getNodeName(), "form:validator");
        
        e = (Element) ((Element) nl.item(17)).getChildNodes().item(1);
        
        assertEquals(e.getAttributes().getLength(), 1);
        assertEquals(e.getAttributes().getNamedItem("name").getNodeValue(), "error");
        assertEquals(e.getFirstChild().getNodeValue(), "You must specify your opinion");
    }

    public final void testCreateId() throws Exception {
        
        String id = this.coll.createId();
        
        assertNotNull(id);
        assertFalse(id.equals(""));
    }
    
    public void testCreateIdClosedFail() throws Exception {
        coll.close();
        try {
            coll.createId();
            assertTrue(false);
        } catch (XMLDBException e) {
            assertEquals(ErrorCodes.COLLECTION_CLOSED, e.errorCode);
        }
    }

    public final void testIsOpen() throws Exception {
        
        assertTrue(this.coll.isOpen());
        this.coll.close();
        assertFalse(this.coll.isOpen());
    }

    public final void testClose() throws Exception {
        assertTrue(this.coll.isOpen());
        this.coll.close();
        assertFalse(this.coll.isOpen());
    }

    public final void testGetService() {
    }

    public final void testGetName() throws Exception {
        assertEquals(this.coll.getName(), "docs");
    }
    
    public final void testDropCollectionWithVCRefFail() throws Exception {
        
        Collection article = this.dbColl.getChildCollection("vcl-data/article");
        assertNotNull("Where is the collection vcl-data/article?", article);
        
        Collection vclColl = this.dbColl.getChildCollection("vcl-data");
        
        CollectionManagementService cms = 
            (CollectionManagementService) vclColl.getService(
                    "CollectionManagementService", "1.0");
        
        try {
            cms.removeCollection("article");
            assertTrue("Expected an exception, because vcl-data/article is referenced by vcl-data/myvc", false);
        } catch (XMLDBException e) {
System.out.println(e.getMessage());
            assertEquals(-1, e.vendorErrorCode);
        }
        
        article = this.dbColl.getChildCollection("vcl-data/article");
        assertNotNull("Where is the collection vcl-data/article?", article);
    }

}
