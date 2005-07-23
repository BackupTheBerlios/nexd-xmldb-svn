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
 * $Log: UpdateServiceTestCase.java,v $
 * Revision 1.7  2005/05/11 17:31:40  nexd
 * Refactoring and extended test cases
 *
 * Revision 1.6  2005/05/08 11:59:32  nexd
 * restructuring
 *
 * Revision 1.5  2005/04/22 14:59:41  nexd
 * SOAP support and performance update.
 *
 * Revision 1.4  2005/03/31 12:08:39  nexd
 * Advanced VCL support
 *
 * Revision 1.3  2005/03/14 12:22:48  nexd
 * Heavy javadoc, checkstyle and eclipse todo-Task session.
 *
 */
package tests.xapi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.TestCase;

import org.apache.xerces.dom.DOMImplementationImpl;
import org.sixdml.SixdmlDatabase;
import org.sixdml.dbmanagement.SixdmlCollection;
import org.sixdml.dbmanagement.SixdmlResource;
import org.sixdml.exceptions.InvalidQueryException;
import org.sixdml.exceptions.NonWellFormedXMLException;
import org.sixdml.exceptions.UpdateTypeMismatchException;
import org.sixdml.update.SixdmlUpdateService;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.ErrorCodes;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;

import de.xplib.nexd.engine.xapi.DatabaseImpl;
import de.xplib.nexd.engine.xml.dom.AbstractNode;
import de.xplib.nexd.engine.xml.dom.DocumentImpl;
import de.xplib.nexd.engine.xml.dom.ElementImpl;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.7 $
 */
public class UpdateServiceTestCase extends TestCase {
        
    /**
     * Comment for <code>db</code>
     */
    private SixdmlDatabase db = null;
    
    /**
     * Comment for <code>coll</code>
     */
    private SixdmlCollection coll =null;
    
    private SixdmlUpdateService sus = null;
    
    private SixdmlResource res = null;

    /**
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        
        if (false && System.getProperty("done") == null) {
            
            Class.forName("org.hsqldb.jdbcDriver");
            Connection c = DriverManager.getConnection("jdbc:hsqldb:storage/nexd.db", "sa", "");
            
            Statement stmt = c.createStatement();
            
            stmt.executeUpdate("delete from nexd_resource where id=5;");
            stmt.executeUpdate("update nexd_sequence SET id=4 where name='nexd_resource';");
            stmt.executeUpdate("update nexd_sequence set id=5 where name='nexd_collection';");
            stmt.executeUpdate("update nexd_sequence set id=715 where name='nexd_dom_node';");
            
            stmt.close();
            c.close();
        }
        
        String name = "de.xplib.nexd.engine.xapi.DatabaseImpl";
        Class clazz = Class.forName(name);
        
        this.db = (SixdmlDatabase) clazz.newInstance();
        DatabaseManager.registerDatabase(this.db);
        
        this.coll = (SixdmlCollection) this.db.getCollection(
                "nexd://localhost./db/updates", "sa", "");
        
        this.sus = (SixdmlUpdateService) this.coll.getService(
                "SixdmlUpdateService", "1.0");
        
        // insert test data;
        if (true && System.getProperty("done") == null) {

            //DriverManager.getConnection("jdbc:hsqldb:storage/nexd.db", "sa", "");

            Resource r = this.coll.getResource("test1.xml");
            if (r != null) {
                this.coll.removeResource(r);
            }
            
            XMLResource res1 = (XMLResource) this.coll.createResource(
                "test1.xml", XMLResource.RESOURCE_TYPE);
                
        	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        	DocumentBuilder dob = dbf.newDocumentBuilder();
        
        	Document dom = dob.parse("data/test/namespace4.xml");
        
        	res1.setContentAsDOM(dom);
        	
        	dom = (Document) res1.getContentAsDOM();

        	try {
        	    this.coll.storeResource(res1);
        	} catch (XMLDBException e) {
        	    ((DatabaseImpl) this.db).getEngine().close();
        	} finally {
        	    System.setProperty("done", "done");
        	}
        	
        	System.setProperty("done", "done");
        }
        
        this.res = (SixdmlResource) this.coll.getResource("test1.xml");
    }
    
    private boolean done = false;
    
    protected void tearDown() throws Exception {
        super.tearDown();
        
        ((DatabaseImpl) this.db).getEngine().close();
    }

    /*
     * Class under test for int insertSibling(String, String, SixdmlResource, boolean)
     */
    public final void testInsertSiblingStringStringSixdmlResourceboolean() throws Exception {
        
        DocumentImpl doc = (DocumentImpl) this.res.getContentAsDOM();
        /*
        NodeList l = doc.getDocumentElement().getChildNodes();
        for (int i = 0; i < l.getLength(); i++) {
            System.out.println(l.item(i).getNodeName());
        }
        */
        //System.err.println(doc.getElementsByTagName("property").item(0).getPreviousSibling().getNodeName());
        
        
        //if (true) return;
        String   iid = ((AbstractNode) doc.getElementsByTagName("property").item(0)).getInternalId().export();
        
        String xpath = "/entity:definition/property";
        String frag  = "<param test='attr' />";
        
        this.sus.insertSibling(xpath, frag, this.res, false);
        
        assertEquals("param", 
                doc.getElementsByTagName("property").item(0)
                .getNextSibling().getNodeName());
        
        frag  = "<foobar><foo id='bar' /><bar id='foo' /></foobar>";
        this.sus.insertSibling(xpath, frag, this.res, true);
        
        doc = (DocumentImpl) this.res.getContentAsDOM();
        /*
        NodeList l = doc.getDocumentElement().getChildNodes();
        for (int i = 0; i < l.getLength(); i++) {
            System.out.println(l.item(i).getNodeName());
        }
        */
        assertEquals("foobar",
                doc.getElementsByTagName("property").item(0)
                .getPreviousSibling().getNodeName());
        assertEquals(2, 
                doc.getElementsByTagName("property").item(0)
                .getPreviousSibling().getChildNodes().getLength());
        
        assertFalse(iid.equals(
                ((AbstractNode) doc.getElementsByTagName("property").item(0))
                .getInternalId()));
        
        this.sus.delete("/entity:definition/param", this.res);
        this.sus.delete("/entity:definition/foobar", this.res);
        
        doc = (DocumentImpl) this.res.getContentAsDOM();
        
        String niid   = ((AbstractNode) doc.getElementsByTagName("property").item(0)).getInternalId().export();
        //String niid = ((AbstractNode) doc.getElementsByTagName("property").item(0))
        //              .getInternalId();        
        assertTrue("Expected id " + iid + " but was " + niid, iid.equals(niid));
        
        try {
            this.sus.insertSibling("/entity:definition", frag, this.res, false);
            assertTrue(false);
        } catch (UpdateTypeMismatchException e1) {}
        
        try {
            this.sus.insertSibling("/[]", frag, this.res, false);
            assertTrue(false);
        } catch (InvalidQueryException e1) {}
        
        try {
            Document d = new DOMImplementationImpl().createDocument(null, null, null);
            d.appendChild(d.createElement("root"));
            this.res.setContentAsDOM(d);
            this.sus.insertSibling(xpath, frag, this.res, true);
            assertTrue(false);
        } catch (XMLDBException e) {
            assertEquals(ErrorCodes.VENDOR_ERROR, e.errorCode);
        }
    }

    /*
     * Class under test for int insertChild(String, String, SixdmlResource)
     */
    public final void testInsertChildStringStringSixdmlResource() throws Exception{
        
        Document doc = (Document) this.res.getContentAsDOM();
        
        String iid = ((AbstractNode) doc.getElementsByTagName("param").item(0)).getInternalId().export();
        
        this.sus.insertChild(
                "/entity:definition//param", "<subelem />", this.res);
        
        doc = (Document) this.res.getContentAsDOM();
        
        String niid = ((AbstractNode) doc.getElementsByTagName("param").item(0)).getInternalId().export();
        assertFalse(iid.equals(niid));
        assertEquals(2, doc.getElementsByTagName("param").item(0).getChildNodes().getLength());
        
        int code = -1;
        try {
            this.sus.insertChild("/entity:definition/@version", "<subelem />", this.res);
        } catch (UpdateTypeMismatchException e) {
            code = 1;
        }
        assertEquals(1, code);
        
        this.sus.delete("/entity:definition//param/subelem", this.res);
        
        doc = (Document) this.res.getContentAsDOM();
        niid = this.getIntId("param");
        assertEquals(iid, niid);
        
        try {
            sus.insertChild("/()", "<subelem />", this.res);
            assertTrue(false);
        } catch (InvalidQueryException e1) {}
        
        try {
            Document d = new DOMImplementationImpl().createDocument(null, null, null);
            d.appendChild(d.createElement("root"));
            this.res.setContentAsDOM(d);
            sus.insertChild("/entity:definition//param", "<subelem />", this.res);
            assertTrue(false);
        } catch (XMLDBException e1) {}
    }
    
    public final void testInsertChildStringStringSixdmlCollection() throws Exception{
        sus.insertChild("/entity:definition", "<dummy />", coll);
        
        sus.delete("/entity:definition/dummy", coll);
    }

    /*
     * Class under test for int insertAttribute(String, SixdmlResource, String, String)
     */
    public final void testInsertAttributeStringSixdmlResourceStringString() throws Exception {
        
        String iid = this.getIntId("id");

        this.sus.insertAttribute(
                "//*[@type='BIGINT']", this.res, "foo", "bar");
        
        DocumentImpl doc = (DocumentImpl) this.res.getContentAsDOM();
        ElementImpl elem = (ElementImpl) doc.getElementsByTagName("id").item(0);
        assertNotNull(elem.getAttribute("foo"));
        assertEquals("bar", elem.getAttribute("foo"));
        
        String niid = this.getIntId("id");
        assertFalse(iid.equals(niid));
                
        this.sus.delete("//*[@type='BIGINT']/@foo", this.res);
        
        niid = this.getIntId("id");
        assertEquals(iid, niid);
        

        this.sus.insertAttribute(
                "//*[@type='BIGINT']", this.res, "foo", "bar", "http://");
        
        doc = (DocumentImpl) this.res.getContentAsDOM();
        elem = (ElementImpl) doc.getElementsByTagName("id").item(0);
        assertNotNull(elem.getAttribute("foo"));
        assertEquals("bar", elem.getAttribute("foo"));
        
        niid = this.getIntId("id");
        assertFalse(iid.equals(niid));
                
        this.sus.delete("//*[@type='BIGINT']/@foo", this.res);
        
        niid = this.getIntId("id");
        assertEquals(iid, niid);
        

        
        try {
            this.sus.insertAttribute(
                    "/*()", this.res, "foo", "bar");
            assertTrue(false);
        } catch (InvalidQueryException e1) {}
        
        try {
            this.sus.insertAttribute(
                    "//*[@type='BIGINT']/@type", this.res, "foo", "bar");
            assertTrue(false);
        } catch (UpdateTypeMismatchException e1) {}
        
        try {
            Document d = new DOMImplementationImpl().createDocument(null, null, null);
            d.appendChild(d.createElement("root"));
            this.res.setContentAsDOM(d);
            this.sus.insertAttribute(
                    "//*[@type='BIGINT']", this.res, "foo", "bar");
            assertTrue(false);
        } catch (XMLDBException e1) {}
    }

    /*
     * Class under test for int delete(String, SixdmlResource)
     */
    public final void testDeleteStringSixdmlResource() throws Exception {
        
        String iid = this.getIntId("id");
        
        assertEquals(2, this.getNode("id").getAttributes().getLength());
        
        this.sus.delete("//*[@type='BIGINT']/@type", this.res);
        
        assertFalse(iid.equals(this.getIntId("id")));
        assertEquals(1, this.getNode("id").getAttributes().getLength());
        
        this.sus.insertAttribute("//id", this.res, "type", "BIGINT");
        
        assertEquals(2, this.getNode("id").getAttributes().getLength());
        assertEquals(iid, this.getIntId("id"));
        
        try {
            this.sus.delete("/@type='BIGINT']", res);
        } catch (InvalidQueryException e) {}

        try {
            Document d = new DOMImplementationImpl().createDocument(null, null, null);
            d.appendChild(d.createElement("root"));
            this.res.setContentAsDOM(d);
            this.sus.delete("//*[@type='BIGINT']", res);
            assertTrue(false);
        } catch (XMLDBException e1) {}
    }

    /*
     * Class under test for int replace(String, String, SixdmlResource)
     */
    public final void testReplaceStringStringSixdmlResource() throws Exception {
        
        String iid = this.getIntId("property");
        
        assertFalse(this.getNode("property").hasChildNodes());
        
        this.sus.insertChild("//property", "<test/>", this.res);
        
        assertFalse(iid.equals(this.getIntId("property")));
        assertTrue(this.getNode("property").hasChildNodes());
        assertEquals(1, this.getNode("property").getChildNodes().getLength());
        assertEquals("test", this.getNode("property").getChildNodes().item(0).getNodeName());
                
        this.sus.replace("//property/test", "<dummy />", this.res);
        
        assertFalse(iid.equals(this.getIntId("property")));
        assertTrue(this.getNode("property").hasChildNodes());
        //assertEquals(1, this.getNode("property").getChildNodes().getLength());
        assertEquals("dummy", this.getNode("property").getChildNodes().item(0).getNodeName());
        
        this.sus.delete("//dummy", this.res);
        
        //assertEquals(iid, this.getIntId("property"));
        
        try {
            this.sus.replace("/entity:definition//@type", "<dummy />", res);
        } catch (UpdateTypeMismatchException e) {}
        
        try {
            this.sus.replace("/entity:definition", "Text", res);
        } catch (UpdateTypeMismatchException e) {}
        
        try {
            this.sus.replace("//-sajdhf", "Text", res);
        } catch (UpdateTypeMismatchException e) {}

        try {
            Document d = new DOMImplementationImpl().createDocument(null, null, null);
            d.appendChild(d.createElement("root"));
            this.res.setContentAsDOM(d);
            this.sus.replace("//property/test", "<dummy />", this.res);
            assertTrue(false);
        } catch (XMLDBException e1) {}
    }

    /*
     * Class under test for int rename(String, String, String, SixdmlResource)
     */
    public final void testRenameStringStringStringSixdmlResource() throws Exception {
        
        /*
        DocumentImpl doc = (DocumentImpl) this.res.getContentAsDOM();
        
        NodeList nl = doc.getElementsByTagName("id");
        
        assertEquals(1, nl.getLength());
        
        Node lft = nl.item(0).getPreviousSibling();
        Node rgt = nl.item(0).getNextSibling();
        Node par = nl.item(0).getParentNode();
        
        Node nn = doc.renameNode(nl.item(0), null, "manuel");
        
        assertSame(par, nn.getParentNode());
        assertSame(lft, nn.getPreviousSibling());
        assertSame(rgt, nn.getNextSibling());
        */
        

        String iid = this.getIntId("id");
        
        this.sus.rename("//id", "myid", "", this.res);
        assertNull(this.getNode("id"));
        assertNotNull(this.getNode("myid"));
        assertEquals(iid, this.getIntId("myid"));
        
        this.sus.rename("//myid", "id", "", this.res);
        assertNull(this.getNode("myid"));
        assertNotNull(this.getNode("id"));
        assertEquals(iid, this.getIntId("id"));
        
        
        try {
            sus.rename("//id/text()", "foo", "http://", res);
            assertTrue(false);
        } catch (UpdateTypeMismatchException e) {}
        
        try {
            this.sus.rename("//sd+fpsdf", "text", "http://", res);
            assertTrue(false);
        } catch (InvalidQueryException e) {}
        try {
            sus.rename("/.-sadr-df aüspdo apüsed /text()", "foo", "http://", res);
            assertTrue(false);
        } catch (InvalidQueryException e) {}
        
        try {
            Document d = new DOMImplementationImpl().createDocument(null, null, null);
            d.appendChild(d.createElement("root"));
            this.res.setContentAsDOM(d);
            this.sus.rename("//property", "text", "http://", res);
            assertTrue(false);
        } catch (XMLDBException e1) {}
        
        
        
    }

    /*
     * Class under test for int insertChild(String, NodeList, SixdmlCollection)
     */
    public final void testInsertChildStringNodeListSixdmlCollection() {
    }

    /*
     * Class under test for int insertAttribute(String, SixdmlCollection, String, String)
     */
    public final void testInsertAttributeStringSixdmlCollectionStringString() {
    }

    /*
     * Class under test for int delete(String, SixdmlCollection)
     */
    public final void testDeleteStringSixdmlCollection() {
    }

    /*
     * Class under test for int replace(String, NodeList, SixdmlCollection)
     */
    public final void testReplaceStringNodeListSixdmlCollection() throws Exception {
        

        NodeList nl = new NodeList() {
            public int getLength() {
                return 0;
            }
            public Node item(int index) {
                return new de.xplib.nexd.engine.xml.dom.DOMImplementationImpl().createDocument(null, null, null).createElement("foobar");
            }
        };
        
        sus.insertChild("/entity:definition", "<dummy />", coll);
        sus.replace("/entity:definition/dummy", nl, coll);
        
        //assertEquals(1, ((Document) res.getContentAsDOM()).getElementsByTagName("foobar").getLength());
        
        sus.delete("/entity:definition/foobar", coll);
    }

    /*
     * Class under test for int replace(String, String, SixdmlCollection)
     */
    public final void testReplaceStringStringSixdmlCollection() throws Exception {
        
        sus.insertChild("/entity:definition", "<dummy />", coll);
        sus.replace("/entity:definition/dummy", "<foobar />", coll);
        
        sus.delete("/entity:definition/foobar", coll);
        
        try {
            sus.replace("/x,fgmölsdkf sdf ---][", "<foobar />", coll);
            assertTrue(false);
        } catch (InvalidQueryException e) {}
    }

    /*
     * Class under test for int rename(String, String, String, SixdmlCollection)
     */
    public final void testRenameStringStringStringSixdmlCollection() throws Exception {
        

        try {
            this.sus.rename("//sd+fpsdf", "text", "http://", coll);
            assertTrue(false);
        } catch (InvalidQueryException e) {}
        
    }

    /*
     * Class under test for int insertChild(String, String, String, SixdmlCollection)
     */
    public final void testInsertChildStringStringStringSixdmlCollection() throws Exception {
        sus.insertChild("/entity:definition", "predicate", "<subelem />", coll);
        sus.delete("/entity:definition/subelem", coll);
    }

    /*
     * Class under test for int insertChild(String, String, NodeList, SixdmlCollection)
     */
    public final void testInsertChildStringStringNodeListSixdmlCollection() throws Exception {
        
        NodeList nl = new NodeList() {
            public int getLength() {
                return 1;
            }
            public Node item(int index) {
                return new de.xplib.nexd.engine.xml.dom.DOMImplementationImpl().createDocument(null, null, null).createElement("foobar");
            }
        };
        
        sus.insertChild("/entity:definition", "predicate", nl, coll);
        sus.delete("/entity:definition/foobar", coll);
    }

    /*
     * Class under test for int insertSibling(String, String, NodeList, SixdmlCollection, boolean)
     */
    public final void testInsertSiblingStringNodeListSixdmlCollectionboolean() throws Exception {
        sus.insertChild("/entity:definition", "<dummy />", coll);
        
        NodeList nl = new NodeList() {
            public int getLength() {
                return 1;
            }
            public Node item(int index) {
                return new de.xplib.nexd.engine.xml.dom.DOMImplementationImpl().createDocument(null, null, null).createElement("foobar");
            }
        };
        
        sus.insertSibling("/entity:definition/dummy", nl, coll, true);
        sus.delete("/entity:definition/foobar", coll);
        sus.delete("/entity:definition/dummy", coll);
    }

    /*
     * Class under test for int insertSibling(String, String, String, SixdmlCollection, boolean)
     */
    public final void testInsertSiblingStringStringSixdmlCollectionboolean() throws Exception {
        sus.insertChild("/entity:definition", "<dummy />", coll);
        sus.insertSibling("/entity:definition/dummy", "<foobar />", coll, true);
        sus.delete("/entity:definition/foobar", coll);
        sus.delete("/entity:definition/dummy", coll);
        
        
    }
    
    public final void testInsertSiblingStringStringNodeListSixdmlCollectionboolean() throws Exception {

        NodeList nl = new NodeList() {
            public int getLength() {
                return 1;
            }
            public Node item(int index) {
                return new de.xplib.nexd.engine.xml.dom.DOMImplementationImpl().createDocument(null, null, null).createElement("foobar");
            }
        };
        
        sus.insertChild("/entity:definition", "<dummy />", coll);
        sus.insertSibling("/entity:definition/dummy", "predicate", nl, coll, true);
        sus.delete("/entity:definition/foobar", coll);
        sus.delete("/entity:definition/dummy", coll);
    }
    
    public final void testInsertSiblingStringStringStringSixdmlCollectionboolean() throws Exception {
        sus.insertChild("/entity:definition", "<dummy />", coll);
        sus.insertSibling("/entity:definition/dummy", "predicate", "<foobar />", coll, true);
        sus.delete("/entity:definition/foobar", coll);
        sus.delete("/entity:definition/dummy", coll);
    }

    /*
     * Class under test for int insertAttribute(String, SixdmlCollection, String, String)
     */
    public final void testInsertAttributeSixdmlCollectionStringString() throws Exception {
        sus.insertChild("/entity:definition", "<dummy />", coll);
        sus.insertAttribute("/entity:definition/dummy", coll, "manuel", "pichler");
        sus.delete("/entity:definition/dummy", coll);
    }

    /*
     * Class under test for int insertAttribute(String, String, SixdmlCollection, String, String, String)
     */
    public final void testInsertAttributeStringSixdmlCollectionStringStringString() throws Exception {
        sus.insertChild("/entity:definition", "<dummy />", coll);
        sus.insertAttribute("/entity:definition/dummy", coll, "manuel", "pichler", "http://xplib.de");
        sus.delete("/entity:definition/dummy", coll);
        
    }
    
    /*
     * Class under test for int insertAttribute(String, String, SixdmlCollection, String, String, String)
     */
    public final void testInsertAttributeStringStringSixdmlCollectionStringStringString() throws Exception {
        sus.insertChild("/entity:definition", "<dummy />", coll);
        sus.insertAttribute("/entity:definition/dummy", "predicate", coll, "manuel", "pichler", "http://xplib.de");
        sus.delete("/entity:definition/dummy", coll);
    }

    /*
     * Class under test for int insertAttribute(String, String, SixdmlCollection, String, String, String)
     */
    public final void testInsertAttributeStringStringSixdmlCollectionStringString() throws Exception {
        sus.insertChild("/entity:definition", "<dummy />", coll);
        sus.insertAttribute("/entity:definition/dummy", "predicate", coll, "manuel", "pichler");
        sus.delete("/entity:definition/dummy", coll);
        
    }

    /*
     * Class under test for int delete(String, String, SixdmlCollection)
     */
    public final void testDeleteStringStringSixdmlCollection() throws Exception {
        sus.insertChild("/entity:definition", "<dummy />", coll);
        sus.delete("/entity:definition/dummy", "predicate", coll);
    }

    /*
     * Class under test for int replace(String, String, String, SixdmlCollection)
     */
    public final void testReplaceStringStringStringSixdmlCollection() throws Exception {
        sus.insertChild("/entity:definition", "<dummy />", coll);
        sus.replace("/entity:definition/dummy", "predicate", "<foobar />", coll);
        
        sus.delete("/entity:definition/foobar", coll);
    }

    /*
     * Class under test for int rename(String, String, String, String, SixdmlCollection)
     */
    public final void testRenameStringStringStringStringSixdmlCollection() throws Exception {
        sus.insertChild("/entity:definition", "<dummy />", coll);
        sus.rename("/entity:definition/dummy", "predicate", "foobar", "http://xplib.de", coll);
        
        sus.delete("/entity:definition/foobar", coll);
    }
    
    public void testNonWellFormedXML() throws Exception {
        try {
            sus.insertChild("/entity:definition", "><dummy /><", coll);
            assertTrue(false);
        } catch (NonWellFormedXMLException e) {}
    }
    
    
    protected String getIntId(String name) throws Exception {
        return this.getNode(name).getInternalId().export();
    }
    
    protected AbstractNode getNode(String name) throws Exception {
        Document doc = (Document) this.res.getContentAsDOM();
        return ((AbstractNode) doc.getElementsByTagName(name).item(0));
    }

}
