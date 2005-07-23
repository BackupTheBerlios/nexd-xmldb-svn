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
 * $Log: XPathQueryServiceTestCase.java,v $
 * Revision 1.6  2005/05/11 17:31:40  nexd
 * Refactoring and extended test cases
 *
 * Revision 1.5  2005/05/08 11:59:32  nexd
 * restructuring
 *
 * Revision 1.4  2005/04/22 14:59:41  nexd
 * SOAP support and performance update.
 *
 * Revision 1.3  2005/03/31 12:08:39  nexd
 * Advanced VCL support
 *
 * Revision 1.2  2005/03/14 12:22:48  nexd
 * Heavy javadoc, checkstyle and eclipse todo-Task session.
 *
 */
package tests.xapi;

import junit.framework.TestCase;

import org.sixdml.SixdmlDatabase;
import org.sixdml.dbmanagement.SixdmlCollection;
import org.sixdml.dbmanagement.SixdmlResource;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.ErrorCodes;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XPathQueryService;

import de.xplib.nexd.engine.store.XPathSQLBuilder;
import de.xplib.nexd.engine.xapi.DatabaseImpl;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.6 $
 */
public class XPathQueryServiceTestCase extends TestCase {
    
    /**
     * Comment for <code>db</code>
     */
    private SixdmlDatabase db = null;
    
    /**
     * Comment for <code>coll</code>
     */
    private SixdmlCollection coll = null;
    
    private SixdmlCollection docs = null;
    
    private SixdmlResource res = null;
    
    private XPathQueryService service = null;
    
    public XPathQueryServiceTestCase() throws Exception {
        super("XPathQueryServiceTestCase");
    }

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        
        String name = "de.xplib.nexd.engine.xapi.DatabaseImpl";
        Class clazz = Class.forName(name);
        
        this.db = (SixdmlDatabase) clazz.newInstance();
        DatabaseManager.registerDatabase(this.db);
        
        this.coll = (SixdmlCollection) this.db.getCollection(
                "nexd://localhost./db/docs/foo", "sa", "");
        
        this.docs = (SixdmlCollection) this.db.getCollection(
                "nexd://localhost./db/docs", "sa", "");
        
        this.res     = (SixdmlResource) this.docs.getResource("document.xml");
        this.service = (XPathQueryService) this.docs.getService(
                "XPathQueryService", "1.0");
    }
    
    protected void tearDown() throws Exception {
        super.tearDown();
        
        ((DatabaseImpl) this.db).getEngine().close();
    }
    
    public final void testCollectionGetService() throws Exception {
        
        XPathQueryService xqs = (XPathQueryService) this.coll.getService(
                "XPathQueryService", "1.0");
        
        assertNotNull(xqs);
    }
    
    public final void testCollectionQuerySimpleAbsolute() throws Exception {
        
        String xpath = "/entity:definition/id";
        XPathQueryService xqs = (XPathQueryService) this.docs.getService(
                "XPathQueryService", "1.0");
        
        assertEquals(2, xqs.query(xpath).getSize());
    }
    
    public final void testCollectionQueryRel() throws Exception {
        String xpath = "//id/generator";
        XPathQueryService xqs = (XPathQueryService) this.docs.getService(
                "XPathQueryService", "1.0");
        
        assertEquals(2, xqs.query(xpath).getSize());
    }
    
    public final void testCollectionQueryAttrParent() throws Exception {
        
        String xpath = "//id[@name='id' and @type='BIGINT']/generator/param";
        XPathQueryService xqs = (XPathQueryService) this.docs.getService(
                "XPathQueryService", "1.0");
        
        ResourceSet rs = xqs.query(xpath);
        assertEquals(1, rs.getSize());
        
        SixdmlResource r = (SixdmlResource) rs.getResource(0);
        assertEquals("param", r.getContentAsDOM().getFirstChild().getNodeName());
        
        xpath = "//id[@name='id' and @type='BIGINT']/generator/param/..";
        
        XPathSQLBuilder xsb = new XPathSQLBuilder(xpath, 2, true);
        //System.err.println(xsb.buildSQLQuery());
        
        rs = xqs.query(xpath);
        assertEquals(1, rs.getSize());
        
        r = (SixdmlResource) rs.getResource(0);
        assertEquals("generator", r.getContentAsDOM().getFirstChild().getNodeName());
    }
    
    public final void testQueryParent_AttrElementParent() throws Exception {
        String xpath = "//id[@name='id']/generator/param/../..";
        XPathQueryService xqs = (XPathQueryService) this.docs.getService(
                "XPathQueryService", "1.0");
        
        ResourceSet rs = xqs.query(xpath);
        assertEquals(2, rs.getSize());
        
        SixdmlResource sr = (SixdmlResource) rs.getResource(0);
        assertEquals("id", sr.getContentAsDOM().getFirstChild().getNodeName());
    }
    
    public final void testQueryAttr_AttrElementParent() throws Exception {
        String xpath = "//id[@name='id']//param/../../@type";
        XPathQueryService xqs = (XPathQueryService) this.docs.getService(
                "XPathQueryService", "1.0");
        
        XPathSQLBuilder xsb = new XPathSQLBuilder(xpath, 2, true);
//        System.err.println(xsb.buildSQLQuery());
        
        ResourceSet rs = xqs.query(xpath);
        assertEquals(2, rs.getSize());
        
        SixdmlResource sr = (SixdmlResource) rs.getResource(0);
        
        Node node = sr.getContentAsDOM().getFirstChild();
        assertEquals("nexd:result", node.getNodeName());
        
        Attr attr = ((Element) node).getAttributeNode("type");
        assertNotNull(attr);
        assertEquals("BIGINT", attr.getValue());
    }
    
    public final void testQueryAttr_Any() throws Exception {
        
        String xpath = "//*/@type";
        XPathQueryService xqs = (XPathQueryService) this.docs.getService(
                "XPathQueryService", "1.0");
        
        ResourceSet rs = xqs.query(xpath);
        assertEquals(8, rs.getSize());
        
        SixdmlResource sr = (SixdmlResource) rs.getResource(0);
        Element elem = (Element) sr.getContentAsDOM().getFirstChild();
        assertEquals("nexd:result", elem.getNodeName());
        
        assertEquals("boolean", elem.getAttribute("type"));
        
        sr = (SixdmlResource) rs.getResource(1);
        elem = (Element) sr.getContentAsDOM().getFirstChild();
        assertEquals("nexd:result", elem.getNodeName());
        
        assertEquals("boolean", elem.getAttribute("type"));
        
        sr = (SixdmlResource) rs.getResource(2);
        elem = (Element) sr.getContentAsDOM().getFirstChild();
        assertEquals("nexd:result", elem.getNodeName());
        
        assertEquals("raw", elem.getAttribute("type"));
        
        sr = (SixdmlResource) rs.getResource(3);
        elem = (Element) sr.getContentAsDOM().getFirstChild();
        assertEquals("nexd:result", elem.getNodeName());
        
        assertEquals("boolean", elem.getAttribute("type"));
    }
    
    public final void testQueryElement_AnyAttr() throws Exception {
        
        String xpath = "//*[@type='date']";
        XPathQueryService xqs = (XPathQueryService) this.docs.getService(
                "XPathQueryService", "1.0");
        
        ResourceSet rs = xqs.query(xpath);
        assertEquals(1, rs.getSize());
        
        SixdmlResource sr = (SixdmlResource) rs.getResource(0);
        
        Node node = sr.getContentAsDOM().getFirstChild();
        assertEquals("property", node.getNodeName());
    }
    
    public final void testQueryElement_AnyAttrParent() throws Exception {
        //String xpath = "/entitiy:definition//*[@type='date']/..";
        String xpath = "//*[@type='date']/..";

        //XPathSQLBuilder xsb = new XPathSQLBuilder(xpath, 2, true);
        //System.err.println(xsb.buildSQLQuery());
        
        ResourceSet rs = this.service.query(xpath);
        assertEquals(1, rs.getSize());
        
        SixdmlResource sr = (SixdmlResource) rs.getResource(0);
        
        Element elem = (Element) sr.getContentAsDOM().getFirstChild();
        assertEquals("entity:definition", elem.getTagName());
        
        xpath = "//*[@class='hilo']/..";
        
        //XPathSQLBuilder xsb = new XPathSQLBuilder(xpath, 2, true);
        //System.err.println(xsb.buildSQLQuery());
        
        rs = this.service.query(xpath);
        assertEquals(2, rs.getSize());
         
        sr = (SixdmlResource) rs.getResource(0);
        
        elem = (Element) sr.getContentAsDOM().getFirstChild();
        assertEquals("id", elem.getTagName());
        assertNotNull(elem.getAttribute("type"));
        assertEquals("BIGINT", elem.getAttribute("type"));
        
        
        sr = (SixdmlResource) rs.getResource(1);
        
        elem = (Element) sr.getContentAsDOM().getFirstChild();
        assertEquals("id", elem.getTagName());
        assertNotNull(elem.getAttribute("type"));
        assertEquals("SMALL", elem.getAttribute("type"));
    }
    
    public final void testQueryAny_ElementAll() throws Exception {
        String xpath = "/entity:definition/*";
        
        ResourceSet rs = this.service.query(xpath);
        
        assertEquals(4, rs.getSize());
        
        Resource r = rs.getMembersAsResource();
    }
    
    public final void testQueryText_ElementAttr() throws Exception {
        String xpath = "//id[@type='BIGINT']/generator/param/text()";
        
        //XPathSQLBuilder xsb = new XPathSQLBuilder(xpath, 2, true);
        //System.err.println(xsb.buildSQLQuery());
        
        ResourceSet rs = this.service.query(xpath);
        
        assertEquals(1, rs.getSize());
        
        SixdmlResource sr = (SixdmlResource) rs.getResource(0);
        assertEquals("person_id_seq", sr.getContentAsDOM().getFirstChild().getFirstChild().getNodeValue());
    }
    
    public final void testQueryTextRange_ElementAttr() throws Exception {
        String xpath = "//id[@type='BIGINT']/text()";
        
        //XPathSQLBuilder xsb = new XPathSQLBuilder(xpath, 2, true);
        //System.err.println(xsb.buildSQLQuery());
        
        ResourceSet rs = this.service.query(xpath);
        
        assertEquals(1, rs.getSize());
        
        SixdmlResource sr = (SixdmlResource) rs.getResource(0);
        
        String text = sr.getContentAsDOM().getFirstChild().getFirstChild().getNodeValue();
        assertFalse(0 == text.length());
        assertTrue(0 == text.trim().length());
        
        
        xpath = "//id[@type='BIGINT']//*/text()";
        
        //XPathSQLBuilder xsb = new XPathSQLBuilder(xpath, 2, true);
        //System.err.println(xsb.buildSQLQuery());
        
        rs = this.service.query(xpath);
        
        assertEquals(1, rs.getSize());
        
        sr = (SixdmlResource) rs.getResource(0);
                
        Node node = sr.getContentAsDOM().getFirstChild();
        assertEquals(3, node.getChildNodes().getLength());
        
        node = node.getFirstChild();
        assertEquals("", node.getNodeValue().trim());
        
        node = node.getNextSibling();
        assertEquals("person_id_seq", node.getNodeValue());
        
        node = node.getNextSibling();
        assertEquals("", node.getNodeValue().trim());
    }
    
    public final void testQueryElement_AnyAttrIndex() throws Exception {
        String xpath = "//*[@name and @not-null]";
        
        //XPathSQLBuilder xsb = new XPathSQLBuilder(xpath, 2, true);
        //System.err.println(xsb.buildSQLQuery());
        
        ResourceSet rs = this.service.query(xpath);
        assertEquals(2, rs.getSize());
        
        
        xpath = "//*[@name and @type]";
        
        //XPathSQLBuilder xsb = new XPathSQLBuilder(xpath, 2, true);
        //System.err.println(xsb.buildSQLQuery());
        
        rs = this.service.query(xpath);
        assertEquals(8, rs.getSize());
        
        
        xpath = "//*[@name]";
        
        //XPathSQLBuilder xsb = new XPathSQLBuilder(xpath, 2, true);
        //System.err.println(xsb.buildSQLQuery());
        
        rs = this.service.query(xpath);
        
        //assertEquals(6, rs.getSize());
        
    }
    
    public final void testElementQuery_AttrNotEqual() throws Exception {
        String xpath = "//id[@type!='BIGINT']";
        
        //XPathSQLBuilder xsb = new XPathSQLBuilder(xpath, 2, true);
        //System.err.println(xsb.buildSQLQuery());
        
        ResourceSet rs = this.service.query(xpath);
        assertEquals(1, rs.getSize());
        
        SixdmlResource sr = (SixdmlResource) rs.getResource(0);
        
        assertEquals("SMALL", ((Element) sr.getContentAsDOM().getFirstChild()).getAttribute("type"));
    }
    
    
    public final void testElementQuery_TextExists() throws Exception {
        String xpath = "//id/*/*[text()]";
        
        //XPathSQLBuilder xsb = new XPathSQLBuilder(xpath, 2, true);
        //System.err.println(xsb.buildSQLQuery());
        
        ResourceSet rs = this.service.query(xpath);
        assertEquals(2, rs.getSize());
        
        SixdmlResource sr = (SixdmlResource) rs.getResource(0);
        
        assertEquals("param", ((Element) sr.getContentAsDOM().getFirstChild()).getTagName());
        
        xpath = "//property[text()]";
        
        //XPathSQLBuilder xsb = new XPathSQLBuilder(xpath, 2, true);
        //System.err.println(xsb.buildSQLQuery());
        
        rs = this.service.query(xpath);
        assertEquals(36, rs.getSize());
    }
    
    public final void testElementQuery_TextEquals() throws Exception {
        String xpath = "//*[text() = 'person_id_seq']";
        
        //XPathSQLBuilder xsb = new XPathSQLBuilder(xpath, 2, true);
        //System.err.println(xsb.buildSQLQuery());
        
        ResourceSet rs = this.service.query(xpath);
        assertEquals(2, rs.getSize());
    }
    
    public final void testElementQuery_AttrLess() throws Exception {
        String xpath = "//*[@version < 2.0]";
        
        //XPathSQLBuilder xsb = new XPathSQLBuilder(xpath, 2, true);
        //System.err.println(xsb.buildSQLQuery());
        
        ResourceSet rs = this.service.query(xpath);
        assertEquals(2, rs.getSize());
    }
    

    public final void testSetNamespace() throws Exception {
        service.setNamespace("prefix", "http://xplib.de");
    }

    public final void testGetNamespace()  throws Exception {
        service.setNamespace("prefix", "http://xplib.de");
        assertEquals("http://xplib.de", service.getNamespace("prefix"));
    }

    public final void testRemoveNamespace() throws Exception {
        service.setNamespace("prefix", "http://xplib.de");
        assertEquals("http://xplib.de", service.getNamespace("prefix"));
        service.removeNamespace("prefix");
        assertNull(service.getNamespace("prefix"));
    }

    public final void testClearNamespaces() throws Exception {
        service.setNamespace("prefix1", "http://xplib.de");
        service.setNamespace("prefix2", "http://xplib.de");
        service.clearNamespaces();
        assertNull(service.getNamespace("prefix1"));
        assertNull(service.getNamespace("prefix2"));
    }

    public final void testQueryInvalidQueryFail() {
        try {
            service.query("../00000=======\\}]}}]}");
            assertTrue(false);
        } catch (XMLDBException e) {
            assertEquals(ErrorCodes.VENDOR_ERROR, e.errorCode);
        }
    }

    public final void testQueryResource() {
        try {
            service.queryResource("document.xml", "../00000=======\\}]}}]}");
            assertTrue(false);
        } catch (XMLDBException e) {
            assertEquals(ErrorCodes.VENDOR_ERROR, e.errorCode);
        }
    }
    
    public final void testGetName() throws Exception {
        assertEquals("XPathQueryService", service.getName());
    }
    
    public final void testGetVersion() throws Exception {
        assertEquals("1.0", service.getVersion());
    }
    
    public final void testSetCollectionNullFail() throws Exception {
        try {
            service.setCollection(null);
            assertTrue(false);
        } catch (XMLDBException e) {
            assertEquals(ErrorCodes.INVALID_COLLECTION, e.errorCode);
        }
    }
}
