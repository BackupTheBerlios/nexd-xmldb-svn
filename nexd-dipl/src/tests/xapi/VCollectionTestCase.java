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
package tests.xapi;

import java.io.File;
import java.net.URL;
import java.util.Iterator;

import junit.framework.TestCase;

import org.sixdml.SixdmlDatabase;
import org.sixdml.dbmanagement.SixdmlCollection;
import org.sixdml.dbmanagement.SixdmlResource;
import org.sixdml.dbmanagement.SixdmlTransactionService;
import org.sixdml.query.SixdmlQueryResultsMap;
import org.sixdml.query.SixdmlQueryService;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.ErrorCodes;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.Service;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.TransactionService;
import org.xmldb.api.modules.XMLResource;
import org.xmldb.api.modules.XPathQueryService;

import de.xplib.nexd.api.VirtualCollection;
import de.xplib.nexd.api.VirtualCollectionManagementService;
import de.xplib.nexd.api.VirtualResource;
import de.xplib.nexd.api.pcvr.PCVResource;
import de.xplib.nexd.api.vcl.VCLSchema;
import de.xplib.nexd.comm.NEXDEngineI;
import de.xplib.nexd.engine.xapi.DatabaseImpl;
import de.xplib.nexd.engine.xapi.VirtualCollectionImpl;
import de.xplib.nexd.engine.xml.jaxp.DocumentBuilderFactoryImpl;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.6 $
 */
public class VCollectionTestCase extends TestCase {

    public static final String EXPECTED_EXCEPTION = "Expected an Exception and not ";

    public static final String INVALID_COLL_MSG = "Expected errorCode was ErrorCodes.INVALID_COLLECTION but is was ";
    
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
    
    public final void testVCLSchemaVisitor() throws Exception {
        
        if (true) return;
        
        Collection vcl = this.dbColl.getChildCollection("vcl-data");
        
        VirtualCollectionManagementService vcms = 
            (VirtualCollectionManagementService) 
                vcl.getService("VirtualCollectionManagementService", "1.0");
        
        File f  = new File("data/vcl-schema/author-articles.vcs");
        URL url = new URL("file://" + f.getAbsolutePath());
        
        long start = System.currentTimeMillis();
        vcms.createVirtualCollection("myvc", url);
        long end = System.currentTimeMillis();

        System.out.println("Time: " + (end - start) + " msec");
    }
    
    public void testGetChildCollectionCountSuccess() throws Exception {
        VirtualCollection vc = this.getMyVC();
        assertEquals(0, vc.getChildCollectionCount());
    }
    
    public void testGetChildCollectionNullSuccess() throws Exception {
        VirtualCollection vc = this.getMyVC();
        assertNull(vc.getChildCollection("$vcl-schema"));
    }
    
    public void testSetStylesheetClosedFail() throws Exception {
        VirtualCollection vc = this.getMyVC();
        vc.close();
        
        try {
            URL url = null;
            vc.setStylesheet(url);
            assertTrue(false);
        } catch (XMLDBException e) {
            assertEquals(ErrorCodes.COLLECTION_CLOSED, e.errorCode);
        }
        
    }
    
    public void testUnsetStylesheetClosedFail() throws Exception {
        this.getMyVC();
        VirtualCollection vc = this.getMyVC();
        vc.close();
        try {
            vc.unsetStylesheet();
            assertTrue(false);
        } catch (XMLDBException e) {
            assertEquals(ErrorCodes.COLLECTION_CLOSED, e.errorCode);
        }
    }
    
    public void testShowStylesheetClosedFail() throws Exception {
        this.getMyVC();
        VirtualCollection vc = this.getMyVC();
        vc.close();
        try {
            vc.showStylesheet();
            assertTrue(false);
        } catch (XMLDBException e) {
            assertEquals(ErrorCodes.COLLECTION_CLOSED, e.errorCode);
        }
    }
    
    public void testGetServiceClosedFail() throws Exception {
        this.getMyVC();
        VirtualCollection vc = this.getMyVC();
        vc.close();
        try {
            vc.getService("SixdmlStatementService", "1.0");
            assertTrue(false);
        } catch (XMLDBException e) {
            assertEquals(ErrorCodes.COLLECTION_CLOSED, e.errorCode);
        }
    }
    
    public void testGetServicesClosedFail() throws Exception {
        this.getMyVC();
        VirtualCollection vc = this.getMyVC();
        vc.close();
        try {
            vc.getServices();
            assertTrue(false);
        } catch (XMLDBException e) {
            assertEquals(ErrorCodes.COLLECTION_CLOSED, e.errorCode);
        }
    }
    
    /*
    public final void testDropResourceRoot() throws Exception {
        
        Collection author = this.dbColl.getChildCollection("vcl-data/author");
        
        author.removeResource(author.getResource("author.3.hjb.xml"));
    }
    
    public final void testDropResourceSub() throws Exception {
        
        Collection article = this.dbColl.getChildCollection("vcl-data/article");
        
        article.removeResource(article.getResource("article.patent.1.xml"));
    }
    */
    public final void testStoreResourceRoot() throws Exception {
        
        Collection author = this.dbColl.getChildCollection("vcl-data/author");
        Collection vcoll  = this.dbColl.getChildCollection("vcl-data/myvc");
                
        int size = vcoll.getResourceCount();
        
        SixdmlResource res = (SixdmlResource) author.createResource(
                "author.4.manuel.xml", SixdmlResource.RESOURCE_TYPE);
        res.setContentAsDOM(DocumentBuilderFactoryImpl.newInstance().newDocumentBuilder().parse("data/vcl-data/author.4.manuel.xml"));
                
        author.storeResource(res);
        
    }
    

    
    public final void testQueryResource1() throws Exception {
        
        VirtualCollection vc = (VirtualCollection) this.dbColl.getChildCollection("vcl-data/myvc");
        
        VirtualResource vr = (VirtualResource) 
                vc.getResource("virtual_autoren3.vxml");
                
        Document doc = (Document) vr.getContentAsDOM();
        Element articles = (Element) doc.getElementsByTagName("articles").item(0);
        
        assertEquals(0, articles.getElementsByTagName("*").getLength());
        
    }
    
    public final void testStoreResourceSubset() throws Exception {
        
        Collection article = this.dbColl.getChildCollection("vcl-data/article");
        Collection vcoll  = this.dbColl.getChildCollection("vcl-data/myvc");
        
        int size = vcoll.getResourceCount();
        
        SixdmlResource res = (SixdmlResource) article.createResource(
                "article.from.manuel.xml", SixdmlResource.RESOURCE_TYPE);
        res.setContentAsDOM(DocumentBuilderFactoryImpl.newInstance().newDocumentBuilder().parse("data/vcl-data/article.from.manuel.xml"));
        
        article.storeResource(res);
        
        assertEquals(size, vcoll.getResourceCount());
    }
    
    public final void testQueryResource2() throws Exception {
        VirtualCollection vc = (VirtualCollection) this.dbColl.getChildCollection("vcl-data/myvc");
        
        VirtualResource vr = (VirtualResource) 
                vc.getResource("virtual_autoren3.vxml");
        
        Document doc = (Document) vr.getContentAsDOM();
        Element articles = (Element) doc.getElementsByTagName("articles").item(0);
        
        assertEquals(4, articles.getElementsByTagName("*").getLength());
    }
    
    public final void testDropResourceSubset() throws Exception {
        
        Collection article = this.dbColl.getChildCollection("vcl-data/article");
        
        int size = article.getResourceCount();
        
        Resource r = article.getResource("article.from.manuel.xml");
        
        // Just do it if the resource exists.
        if (r != null) {
            article.removeResource(r);
            assertEquals(size - 1, article.getResourceCount());
        }
    }
    
    public final void testQueryResource3() throws Exception {
        
        VirtualCollection vc = (VirtualCollection) this.dbColl.getChildCollection("vcl-data/myvc");
        
        VirtualResource vr = (VirtualResource) 
                vc.getResource("virtual_autoren3.vxml");
        
        Document doc = (Document) vr.getContentAsDOM();
        Element articles = (Element) doc.getElementsByTagName("articles").item(0);
        
        assertEquals(0, articles.getElementsByTagName("*").getLength());
        
    }
    
    public final void testDropResourceRoot() throws Exception {
        
        Collection author = this.dbColl.getChildCollection("vcl-data/author");
        Collection vcoll  = this.dbColl.getChildCollection("vcl-data/myvc");
        
        int size = vcoll.getResourceCount();
        
        Resource res = author.getResource("author.4.manuel.xml");
        
        author.removeResource(res);
        
        assertEquals(size - 1,vcoll.getResourceCount());
    }
    
    
    public final void testDropVirtualResourceFail() throws Exception {
        
        Collection vcoll = this.dbColl.getChildCollection("vcl-data/myvc");
        
        String[] names = vcoll.listResources();
        for (int i = 0; i < names.length; i++) {
            Resource res = vcoll.getResource(names[i]);
            
            assertTrue(
                    "Expected instance of VirtualResource and not " + res.getClass(),
                    res instanceof VirtualResource);
            
            try {
                vcoll.removeResource(res);
                assertTrue("Cannot delete a VirtualResource.", false);
            } catch (XMLDBException e) {
                assertTrue(
                        "Expected errorCode.INVALID_COLLECTION",
                        e.errorCode == ErrorCodes.INVALID_COLLECTION);
            }
        }
    }
    
    
    public final void testCreateIdFail() throws Exception {
        
        VirtualCollection vcoll = this.getMyVC();
        
        try {
            vcoll.createId();
            assertTrue(EXPECTED_EXCEPTION + " an id.", false);
        } catch (XMLDBException e) {
            assertTrue(INVALID_COLL_MSG + e.errorCode, e.errorCode == ErrorCodes.INVALID_COLLECTION);
        }
        
    }
    
    
    public final void testCreateResourceFail() throws Exception {
        
        VirtualCollection vcoll = this.getMyVC();
        try {
            vcoll.createResource("", XMLResource.RESOURCE_TYPE);
            assertTrue(EXPECTED_EXCEPTION + " a resource.", false);
        } catch (XMLDBException e) {
            assertTrue(INVALID_COLL_MSG + e.errorCode, isInvalidColl(e.errorCode));
        }
        
    }
    
    public final void testGetResourceSuccess() throws Exception {
        
        VirtualCollection vcoll = this.getMyVC();
        
        String[] names = vcoll.listResources();
        assertTrue("Expected resource names and not an empty array.", 0 != names.length);
        
        Resource res = vcoll.getResource(names[0]);
        assertTrue(
                "Expected instance of VirtualResource and not " + res.getClass(),
                res instanceof VirtualResource);
    }
    
    public final void testGetPCVResourceSuccess() throws Exception {
        VirtualCollection vcoll = this.getMyVC();
        
        String[] names = vcoll.listResources();
        assertTrue("Expected resource names and not an empty array.", 0 != names.length);
        
        Resource res = vcoll.getResource(names[0]);
        assertTrue(
                "Expected instance of VirtualResource and not " + res.getClass(),
                res instanceof VirtualResource);
        
        VirtualResource vres = (VirtualResource) res;
        
        PCVResource pres = vres.getPreCompiledResource();
        assertNotNull("Expected pres to be not null", pres);
        
        assertTrue(
                "Expected instance of PCVResource and not " + res.getClass(),
                pres instanceof PCVResource);
        
        assertEquals(vres.getId(), pres.getId());
    }
    
    public final void testGetServiceCollectionManagementServiceFail() throws Exception {
        
        Service s = this.getService("CollectionManagementService");
        assertNull("Expected null and not CollectionManagementService, because a VC has no child collections.", s);
        
        s = this.getService("SixdmlCollectionManagementService");
        assertNull("Expected null and not SixdmlCollectionManagementService, because a VC has no child collections.", s);
    }
    
    public final void testGetServiceXPathQueryServiceSuccess() throws Exception {
        
        Service s = this.getService("XPathQueryService");
        assertNotNull("Expected an instance of XPathQueryService and not null.", s);
        
        assertTrue("Expected an instance of XPathQueryService.", s instanceof XPathQueryService);
    }
    
    public final void testXPathQueryServiceSuccess() throws Exception {
        
        XPathQueryService s = (XPathQueryService) this.getService("XPathQueryService");
        
        ResourceSet rs1 = s.query("//author/@id");
        assertTrue("Expected a non empty ResourceSet.", rs1.getSize() > 0);
        
        ResourceSet rs2 = s.query("//foo/@bar");
        assertTrue("Expected a non empty ResourceSet.", rs2.getSize() > 0);
        
        int id1[] = new int[(int) rs1.getSize()];
        for (int i = 0; i < id1.length; i++) {
            Resource res = rs1.getResource(i);
            assertTrue(
                    "Expected instance of VirtualResource and not " + res.getClass(),
                    res instanceof VirtualResource);
            
            Document doc = (Document) ((VirtualResource) res).getContentAsDOM();
            Element elem = doc.getDocumentElement();
            
            assertEquals(
                    "Expected " + NEXDEngineI.QNAME_QUERY_RESULT 
                    + " but it is " + elem.getTagName(), 
                    NEXDEngineI.QNAME_QUERY_RESULT, elem.getTagName());
            
            NamedNodeMap nnm = elem.getAttributes();
            
            String value = elem.getAttribute("id");
            id1[i] = Integer.parseInt(value);
        }
        
        
        int id2[] = new int[(int) rs2.getSize()];
        for (int i = 0; i < id2.length; i++) {
            Resource res = rs2.getResource(i);
            assertTrue(
                    "Expected instance of VirtualResource and not " + res.getClass(),
                    res instanceof VirtualResource);
            
            Document doc = (Document) ((VirtualResource) res).getContentAsDOM();
            Element elem = doc.getDocumentElement();
            
            assertEquals(
                    "Expected " + NEXDEngineI.QNAME_QUERY_RESULT 
                    + " but it is " + elem.getTagName(), 
                    NEXDEngineI.QNAME_QUERY_RESULT, elem.getTagName());
            
            String value = elem.getAttribute("bar");
            id2[i] = Integer.parseInt(value);
        }
        
        
        for (int i = 0; i < id1.length; i++) {
            int id = id1[i];
            
            boolean in1 = false;
            
            for (int j = 0; j < id2.length; j++) {
                
                if (id == id2[j]) {
                    in1 = true;
                }
                
                boolean in2 = false;
                for (int k = 0; k < id1.length; k++) {
                    if (id2[j] == id1[k]) {
                        in2 = true;
                    }
                }
                assertTrue("Expected id=" + id2[j] + " is in the first array.", in2);
            }
            assertTrue("Expected id=" + id + " is in the second array.", in1);
            
        }
    }
    
    public final void testGetServiceTransactionServiceSuccess() throws Exception {
        
        Service s = this.getService("TransactionService");
        assertNotNull("Expected an instance of TransactionService and not null.", s);
        assertTrue("Expected an instance of TransactionService instead of " + s.getClass(), s instanceof TransactionService);
    }
    
    public final void testGetServiceSixdmlTransactionServiceSuccess() throws Exception {
        
        Service s = this.getService("SixdmlTransactionService");
        assertNotNull("Expected an instance of SixdmlTransactionService and not null.", s);
        assertTrue("Expected an instance of SixdmlTransactionService instead of " + s.getClass(), s instanceof SixdmlTransactionService);
    }
    
    public final void testTransactionServiceImpl() throws Exception {
        // TODO Implement this test.
    }
    
    public final void testGetServiceSixdmlUpdateServiceFail() throws Exception {
        
        Service s = this.getService("SixdmlUpdateService");
        assertNull("Expected getService(SixdmlUpdateService) to be null, because a VirtualResource cannot be updated ", s);
    }
    
    public final void testGetServiceSixdmlQueryServiceSuccess() throws Exception {
        
        Service s = this.getService("SixdmlQueryService");
        assertNotNull("Expected getService(SixdmlQueryService) to be not null", s);
        assertTrue("Expected an instance of SixdmlQueryService instead of " + s.getClass(), s instanceof SixdmlQueryService);
    }
    
    public final void testSixdmlQueryServiceSuccess() throws Exception {
        
        VirtualCollectionImpl vcoll = (VirtualCollectionImpl) this.getMyVC();
        SixdmlQueryService s = (SixdmlQueryService) this.getService("SixdmlQueryService");
        
        SixdmlQueryResultsMap map = s.executeQuery("//author/info", vcoll);
        
        
        assertEquals("//author/info", map.getQuery());
        assertEquals("myvc", map.getCollectionName());
        
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            String data = it.next().toString();
                        
            int index = data.indexOf('=');
            
            String name = data.substring(0, index).trim();
            String xml  = data.substring(index + 1).trim();
            
            assertTrue("Name ends with '.vxml'", name.endsWith(".vxml"));
            assertTrue("Xml starts with '<info>", xml.startsWith("<info>"));
            assertTrue("Xml ends with '</info>", xml.endsWith("</info>"));
        }
    }
    
    public final void testGetServiceVirtualCollectionManagementServiceFail() throws Exception {
        
        Service s = this.getService("VirtualCollectionManagementService");
        assertNull("Expected null and not an instance of VirtualCollectionManagementService, because VirtualCollection cannot have a child.", s);
    }
    
    //public final void testCreateVirtualCollection
    
    public final void testGetServiceXUpdateQueryServiceFail() throws Exception {
        Service s = this.getService("XUpdateQueryService");
        assertNull("Expected null and not an instance of XUpdateQueryService, because a VirtualResource cannot be updated", s);
    }
    
    public final void testGetServicesSuccess() throws Exception {
        
        Service[] services = this.getMyVC().getServices();
        assertEquals(3, services.length);
    }
    
    public final void testRemoveResourceFail() throws Exception {
        
        VirtualCollection vcoll = this.getMyVC();
        String[] names = vcoll.listResources();
        
        for (int i = 0; i < names.length; i++) {
            Resource r = vcoll.getResource(names[i]);
            try {
                vcoll.removeResource(r);
                assertTrue(EXPECTED_EXCEPTION, false);
            } catch (XMLDBException e) {
                assertTrue(INVALID_COLL_MSG + e.errorCode, this.isInvalidColl(e.errorCode));
            }
        }
    }
    
    public final void testStoreResourceFail() throws Exception {
        
        Resource r = this.dbColl.createResource(
                "foobar", XMLResource.RESOURCE_TYPE);
        
        try {
            this.getMyVC().storeResource(r);
            assertTrue(EXPECTED_EXCEPTION, false);
        } catch (XMLDBException e) {
            assertTrue(INVALID_COLL_MSG + e.errorCode, this.isInvalidColl(e.errorCode));
        }
    }
    
    public final void testInsertDocumentStringStringFail() throws Exception {
        
        String xml = "<foo><bar/></foo>";
        String id  = "failtest.xml";
        
        VirtualCollectionImpl vc = this.getMyVC();
        try {
            vc.insertDocument(id, xml);
            assertTrue(EXPECTED_EXCEPTION, false);
        } catch (XMLDBException e) {
            assertTrue(INVALID_COLL_MSG + e.errorCode, this.isInvalidColl(e.errorCode));
        }
    }
    
    public final void testInsertDocumentStringURLFail() throws Exception {
        
        String id  = "failtest.xml";
        URL url = new File("data/test/conf.xml").toURL();
        
        VirtualCollectionImpl vc = this.getMyVC();
        
        try {
            vc.insertDocument(id, url);
            assertTrue(EXPECTED_EXCEPTION, false);
        } catch (XMLDBException e) {
            assertTrue(INVALID_COLL_MSG + e.errorCode, this.isInvalidColl(e.errorCode));
        }
    }
    
    public final void testRemoveDocumentFail() throws Exception {
        
        VirtualCollectionImpl vc = this.getMyVC();
        
        try {
            vc.removeDocument("foobar.xml");
            assertTrue(EXPECTED_EXCEPTION, false);
        } catch (XMLDBException e) {
            assertTrue(INVALID_COLL_MSG + e.errorCode, this.isInvalidColl(e.errorCode));
        }
    }
    
    public final void testCreateVirtualCollectionSuccess() throws Exception {
        Collection testColl = this.db.getCollection("/db/vc-colls", "sa", "");
        
        VirtualCollectionManagementService vcms =
            (VirtualCollectionManagementService) 
                testColl.getService("VirtualCollectionManagementService", "1.0");
        
        int size = testColl.getChildCollectionCount();
        
        VirtualCollection vc = vcms.createVirtualCollection("test-vc", new File(
                "data/vcl-schema/author-articles.no.dtd.vcs").toURL());
        
        assertNotNull("Expected that an instance of VirtualCollection is returned and not null", vc);
        assertTrue("Expected the returns instance is an instance of VirtualCollection and not " + vc.getClass(), vc instanceof VirtualCollection);
        
        assertEquals("Why is the virtual collection not inserted but returned?", size + 1, testColl.getChildCollectionCount());
    }
    
    public final void testDropVirtualCollectionSuccess() throws Exception {
        Collection testColl = this.db.getCollection("/db/vc-colls", "sa", "");
        
        VirtualCollectionManagementService vcms =
            (VirtualCollectionManagementService) 
                testColl.getService("VirtualCollectionManagementService", "1.0");
        
        int size = testColl.getChildCollectionCount();
        
        vcms.removeVirtualCollection("test-vc");
        
        assertEquals("Why is the virtual collection not removed?", size - 1, testColl.getChildCollectionCount());
    }
    
    public final void testGetVCLSchemaSuccess() throws Exception {
        
        VirtualCollectionImpl vc = this.getMyVC();
        
        VCLSchema vcl = vc.getVCLSchema();
        assertNotNull("Expected was an instance of VCLSchema and not null.", vcl);
        assertTrue("Expected was an instance of VCLSchema and not " + vcl.getClass(), vcl instanceof VCLSchema);
        
        assertNotNull(vcl.getCollectionReference());
            
    }
    
    protected Service getService(String name) throws Exception {
        VirtualCollection vcoll = this.getMyVC();
        return vcoll.getService(name, "1.0");
    }
    
    protected boolean isInvalidColl(int code) {
        return code == ErrorCodes.INVALID_COLLECTION;
    }
    
    protected VirtualCollectionImpl getMyVC() throws Exception {
        return (VirtualCollectionImpl) this.dbColl.getChildCollection("vcl-data/myvc");
    }
    
    public static void main(String[] args) throws Exception {
        
        VCollectionTestCase test = new VCollectionTestCase();
        
        test.setUp();
        for (int i = 0; i < 1; i++) {
            System.out.print(i + ": ");
            test.testDropResourceSubset();
            test.testStoreResourceSubset();
            test.testDropResourceSubset();
        }
        
        test.tearDown();
        
        //while (true) { System.out.println("loop"); Thread.sleep(2000); }
    }

}
