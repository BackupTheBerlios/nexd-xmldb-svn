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
 * $Log: EngineTestCase.java,v $
 * Revision 1.1  2005/05/11 17:31:40  nexd
 * Refactoring and extended test cases
 *
 */
package tests.engine;

import java.io.File;
import java.net.URL;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamSource;

import junit.framework.TestCase;

import org.apache.xml.serialize.XMLSerializer;
import org.sixdml.dbmanagement.SixdmlCollection;
import org.sixdml.dbmanagement.SixdmlResource;
import org.sixdml.exceptions.InvalidQueryException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xmldb.api.base.ErrorCodes;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.BinaryResource;
import org.xmldb.api.modules.TransactionService;
import org.xmldb.api.modules.XMLResource;

import de.xplib.nexd.api.VirtualCollection;
import de.xplib.nexd.comm.NEXDEngineI;
import de.xplib.nexd.engine.NEXDEnginePool;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class EngineTestCase extends TestCase {
    
    NEXDEngineI engine;

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        
        engine = NEXDEnginePool.getInstance().getEngine();
        engine.open("sa", "");
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        
        engine.close();
    }

    public final void testOpen() {
        
        
    }

    public final void testClose() {
        //TODO Implement close().
    }

    public final void testBeginTransaction() {
        //TODO Implement beginTransaction().
    }

    public final void testCommitTransaction() {
        //TODO Implement commitTransaction().
    }

    public final void testContainsId() {
        //TODO Implement containsId().
    }

    /*
     * Class under test for SixdmlCollection storeCollection(SixdmlCollection, String)
     */
    public final void testStoreCollectionSixdmlCollectionString() {
        //TODO Implement storeCollection().
    }

    /*
     * Class under test for VirtualCollection storeCollection(SixdmlCollection, String, URL)
     */
    public final void testStoreCollectionInVirtualCollectionFails() throws Exception {
        
        SixdmlCollection coll = engine.queryCollection("/db/vcl-data/myvc");
        
        try {
            engine.storeCollection(coll, "test");
            assertTrue(false);
        } catch (XMLDBException e) {
            assertEquals(ErrorCodes.VENDOR_ERROR, e.errorCode);
        }
    }
    
    public final void testStorageCollectionNonCollectionImplFails() throws Exception {
        
        SixdmlCollection coll = new DummyCollection();
        
        try {
            engine.storeCollection(coll, "foo");
            assertTrue(false);
        } catch (XMLDBException e) {
            assertEquals(ErrorCodes.INVALID_COLLECTION, e.errorCode);
        }
    }
    
    public final void testStorageCollectionExistsFail() throws Exception {
        
        SixdmlCollection coll = this.engine.queryCollection("/db");
        try {
            engine.storeCollection(coll, "docs");
            assertTrue(false);
        } catch (XMLDBException e) {
            assertEquals(ErrorCodes.VENDOR_ERROR, e.errorCode);
        }
        
    }
    
    public final void testStorageCollectionExists() throws Exception {
        
        SixdmlCollection coll = this.engine.queryCollection("/db/vcl-data");
        String name = "myvc";
        URL vcs = new File("data/vcl-schema/author-articles.no.dtd.vcs").toURL();
        URL xsl = null;
        
        try {
            engine.storeCollection(coll, name, vcs, xsl);
            assertTrue(false);
        } catch (XMLDBException e) {
            assertEquals(ErrorCodes.VENDOR_ERROR, e.errorCode);
        }
        
    }
    
    
    public final void testQueryCollectionFailInvalidPath() throws Exception {
        String invalidPath = "/}(Sasd[]=!'\"dlfjsldfk";
        try {
            engine.queryCollection(invalidPath);
            assertTrue(false);
        } catch (XMLDBException e) {
            assertEquals(ErrorCodes.VENDOR_ERROR, e.errorCode);
        }
    }

    /*
     * Class under test for VirtualCollection storeCollection(SixdmlCollection, String, URL, URL)
     */
    public final void testStoreCollectionSixdmlCollectionStringURLURL() {
        //TODO Implement storeCollection().
    }

    public final void testDropCollectionNullParentFail() throws Exception {
        SixdmlCollection coll = null;
        
        try {
            engine.dropCollection(coll, "docs");
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }
    
    public final void testDropCollectionNullNameFail() throws Exception {
        
        SixdmlCollection coll = engine.queryCollection("/db");
        try {
            engine.dropCollection(coll, null);
            assertTrue(true);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }
    
    public final void testDropCollectionRootFail() throws Exception {
        SixdmlCollection coll = engine.queryCollection("/db");
        try {
            engine.dropCollection(coll, "/db");
            assertTrue(false);
        } catch (XMLDBException e) {
            assertEquals(ErrorCodes.INVALID_COLLECTION, e.errorCode);
        }
    }
    
    public final void testDropCollectionNotExistsFail() throws Exception  {
        
        SixdmlCollection coll = engine.queryCollection("/db");
        
        try {
            engine.dropCollection(coll, "a-not-existing-collection");
            assertTrue(false);
        } catch (XMLDBException e) {
            assertEquals(ErrorCodes.NO_SUCH_COLLECTION, e.errorCode);
        }
    }

    public final void testQueryCollectionNotExistingFail() throws Exception {
        
        assertNull(engine.queryCollection("/db/test123/456/789"));
        
    }
    
    public final void testQueryCollectionWithMagicCharFail() throws Exception {
        try {
            engine.queryCollection("/db/vcl-data/myvc/$xsl-stylesheet");
            assertTrue(false);
        } catch (XMLDBException e) {
            assertEquals(ErrorCodes.VENDOR_ERROR, e.errorCode);
        }
    }

    public final void testQueryChildCollectionSuccess() throws Exception {

        SixdmlCollection db = engine.queryCollection("/db");
        SixdmlCollection child = engine.queryChildCollection(db, "docs");
        
        assertNotNull(child);
        assertTrue(child instanceof SixdmlCollection);
        assertEquals("docs", child.getName());
    }
    
    public final void testQueryChildCollectionMagicCharFail() throws Exception {
        SixdmlCollection db = engine.queryCollection("/db");
        try {
            engine.queryChildCollection(db, "$docs");
            assertTrue(false);
        } catch (XMLDBException e) {
            assertEquals(ErrorCodes.VENDOR_ERROR, e.errorCode);
        }
    }
    
    public final void testQueryChildCollectionTransaction() throws Exception {
        
        SixdmlCollection coll = this.engine.queryCollection("/db/vcl-data");
        TransactionService ts = (TransactionService) coll.getService(
                "TransactionService", "1.0");
        
        ts.begin();
        
        assertNotNull(coll.getProperty(NEXDEngineI.TRANSACTION_ID_KEY));
        
        SixdmlCollection child = engine.queryChildCollection(coll, "article");
        
        assertEquals(coll.getProperty(NEXDEngineI.TRANSACTION_ID_KEY), child.getProperty(NEXDEngineI.TRANSACTION_ID_KEY));
        
        ts.commit();
        
        child = engine.queryChildCollection(coll, "article");
        assertNull(child.getProperty(NEXDEngineI.TRANSACTION_ID_KEY));
    }

    public final void testQueryParentCollection() {
        
        
        //TODO Implement queryParentCollection().
    }

    public final void testQueryCollectionCount() {
        //TODO Implement queryCollectionCount().
    }

    public final void testQueryCollectionsVirtualCollectionSuccess() throws Exception {
        
        SixdmlCollection coll = engine.queryCollection("/db/vcl-data/myvc");
        
        String[] names = engine.queryCollections(coll);
        assertEquals(0, names.length);
    }

    public final void testStoreCollectionSchemaNonXapiCollFail() throws Exception {
        
        URL url = new File("data/test/dtd/example.dtd").toURL();
        
        SixdmlCollection coll = new DummyCollection();
        
        try {
            engine.storeCollectionSchema(coll, url);
            assertTrue(false);
        } catch (XMLDBException e) {
            assertEquals(ErrorCodes.INVALID_COLLECTION, e.errorCode);
        }
    }

    public final void testDropCollectionSchema() {
        //TODO Implement dropCollectionSchema().
    }

    public final void testQueryCollectionSchema() {
        //TODO Implement queryCollectionSchema().
    }

    public final void testStoreCollectionStylesheetNullCollectionFail() throws Exception {
        URL url = new File("data/test/dtd/example.dtd").toURL();
        VirtualCollection coll = null;
        
        try {
            engine.storeCollectionStylesheet(coll, url);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
        //TODO Implement storeCollectionStylesheet().
    }
    
    public final void testStoreCollectionStylesheetNullURLFail() throws Exception {
        VirtualCollection coll = (VirtualCollection) engine.queryCollection("/db/vcl-data/myvc");
        URL url = null;
        
        try {
            engine.storeCollectionStylesheet(coll, url);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }
    
    public static void main(String[] args) throws Exception {
    	
    	Transformer t = TransformerFactory.newInstance().newTransformer(
    			new StreamSource(new File("data/xsl/author-articles.xsl").toURL().openStream()));
    	
    	StreamSource ss = new StreamSource(new File("data/vcl-data/article.from.manuel.xml").toURL().openStream());
    	DOMResult dr = new DOMResult();
    	
    	t.transform(ss, dr);
    	
    	XMLSerializer xser = new XMLSerializer();
    	xser.setOutputByteStream(System.out);
    	
    	xser.serialize((Document) dr.getNode());
    }
    
    public final void testStoreQueryDropCollectionStylesheetSuccess() throws Exception {
        VirtualCollection coll = (VirtualCollection) engine.queryCollection("/db/vcl-data/myvc");
        URL url = new File("data/xsl/author-articles.xsl").toURL();
        
        engine.storeCollectionStylesheet(coll, url);
        
        Node node = engine.queryCollectionStylesheet(coll);
        assertNotNull(node);
        
        engine.dropCollectionStylesheet(coll);
        
        node = engine.queryCollectionStylesheet(coll);
        assertNull(node);
    }
    
    public final void testQueryVResourceWithStylescheet() throws Exception {
    	VirtualCollection coll = (VirtualCollection) engine.queryCollection("/db/vcl-data/myvc");
        URL url = new File("data/xsl/author-articles.xsl").toURL();
        
        String[] names = coll.listResources();
        String name = names[0];
        
        Object obj1 = coll.getResource(name).getContent();
        
        engine.storeCollectionStylesheet(coll, url);
        
        Object obj2 = coll.getResource(name).getContent();
        
        assertFalse("Expected content to be different.", obj1.equals(obj2));
        
        engine.dropCollectionStylesheet(coll);
        
        obj2 = coll.getResource(name).getContent();
        
        assertEquals(obj1, obj2);
    	
    }

    public final void testStoreResourceNonXapiCollFail() throws Exception {
        SixdmlCollection org  = engine.queryCollection("/db/docs");
        SixdmlCollection coll = new DummyCollection();
        
        Resource r = org.createResource("mid", XMLResource.RESOURCE_TYPE);
        r.setContent("<root />");
        
        try {
            engine.storeResource(coll, (SixdmlResource) r);
            assertTrue(false);
        } catch (XMLDBException e) {
            assertEquals(ErrorCodes.INVALID_COLLECTION, e.errorCode);
        }
    }
    
    public final void testStoreResourceNullCollFails() throws Exception {
        SixdmlCollection org  = engine.queryCollection("/db/docs");
        
        Resource r = org.createResource("tid", XMLResource.RESOURCE_TYPE);
        r.setContent("<root />");
        
        try {
            engine.storeResource(null, (SixdmlResource) r);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }
    
    public final void testStoreResourceNullResFails() throws Exception {
        SixdmlCollection org  = engine.queryCollection("/db/docs");
        
        SixdmlResource r = null;
        
        try {
            engine.storeResource(org, r);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    public final void testUpdateResourceNonXapiCollFails() throws Exception {
        SixdmlCollection coll  = engine.queryCollection("/db/docs");
        SixdmlCollection dummy = new DummyCollection();
        
        String[] names = coll.listResources();
        
        SixdmlResource res = (SixdmlResource) engine.queryResource(coll, names[0]);
        
        try {
            engine.updateResource(dummy, res);
            assertTrue(false);
        } catch (XMLDBException e) {
            assertEquals(ErrorCodes.INVALID_COLLECTION, e.errorCode);
        }
    }
    
    public void testUpdateResourceNullCollFail() throws Exception {
        SixdmlCollection coll  = engine.queryCollection("/db/docs");
        SixdmlCollection dummy = null;
        
        SixdmlResource res = (SixdmlResource) engine.queryResource(coll, "mid");
        
        try {
            engine.updateResource(dummy, res);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }
    
    public void testUpdateResourceNullResFail() throws Exception {
        SixdmlCollection coll  = engine.queryCollection("/db/docs");
        
        SixdmlResource res = null;
        
        try {
            engine.updateResource(coll, res);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }
    
    public final void testUpdateResourceDoesNothingSuccess() throws Exception {
        SixdmlCollection coll  = engine.queryCollection("/db/docs");
        Resource res = coll.createResource("binid", BinaryResource.RESOURCE_TYPE);
        
        engine.updateResource(coll, res);
    }

    public final void testDropResourceNonXapiCollFail() throws Exception {
        SixdmlCollection coll  = engine.queryCollection("/db/docs");
        SixdmlCollection dummy = new DummyCollection();
        
        String[] names = coll.listResources();
        
        SixdmlResource res = (SixdmlResource) engine.queryResource(coll, names[0]);

        try {
            engine.dropResource(dummy, res);
            assertTrue(false);
        } catch (XMLDBException e) {
            assertEquals(ErrorCodes.INVALID_COLLECTION, e.errorCode);
        }
    }
    
    public final void testDropResourceVCollFails() throws Exception {
        SixdmlCollection coll  = engine.queryCollection("/db/vcl-data/myvc");
        
        String[] names = coll.listResources();
        Resource res = engine.queryResource(coll, names[0]);

        try {
            engine.dropResource(coll, res);
            assertTrue(false);
        } catch (XMLDBException e) {
            assertEquals(ErrorCodes.INVALID_COLLECTION, e.errorCode);
        }
    }
    
    public final void testDropResourceNullCollFail() throws Exception {
        SixdmlCollection coll  = engine.queryCollection("/db/docs");
        
        String[] names = coll.listResources();
        Resource res = engine.queryResource(coll, names[0]);

        try {
            engine.dropResource(null, res);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }
    
    public final void testDropResourceNullResFail() throws Exception {
        SixdmlCollection coll  = engine.queryCollection("/db/docs");
        Resource res = null;

        try {
            engine.dropResource(coll, res);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertTrue(true); 
        }
    }
    
    public final void testDropResourceBinNothingDoneSuccess() throws Exception {
        SixdmlCollection coll  = engine.queryCollection("/db/docs");
        Resource res = coll.createResource("binid", BinaryResource.RESOURCE_TYPE);

        engine.dropResource(coll, res);        
    }

    public final void testQueryResourceNonXapiCollFail() throws Exception {
        SixdmlCollection coll  = engine.queryCollection("/db/docs");
        SixdmlCollection dummy = new DummyCollection();
        
        String[] names = coll.listResources();
        try {
            engine.queryResource(dummy, names[0]);
            assertTrue(false);
        } catch (XMLDBException e) {
            assertEquals(ErrorCodes.INVALID_COLLECTION, e.errorCode);
        }
    }

    public final void testQueryResourceNullCollFail() throws Exception {
        SixdmlCollection coll  = engine.queryCollection("/db/docs");
        SixdmlCollection dummy = null;
        
        String[] names = coll.listResources();
        try {
            engine.queryResource(dummy, names[0]);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    public final void testQueryResourceNullResIdFail() throws Exception {
        SixdmlCollection coll  = engine.queryCollection("/db/docs");
        
        String[] names = coll.listResources();
        try {
            engine.queryResource(coll, null);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    public final void testQueryResourceUnknownResIdFail() throws Exception {
        SixdmlCollection coll  = engine.queryCollection("/db/docs");
        assertNull(engine.queryResource(coll, "this-is-an-unknow-res-id"));
    }

    public final void testQueryResourceCountNonXapiCollFail() throws Exception {
        
        try {
            engine.queryResourceCount(new DummyCollection());
            assertTrue(false);
        } catch (XMLDBException e) {
            assertEquals(ErrorCodes.INVALID_COLLECTION, e.errorCode);
        }
    }
    
    public final void testQueryResourceCountNullCollFail() throws Exception {
        
        try {
            engine.queryResourceCount(null);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    public final void testQueryResourcesNonXapiCollFail()  throws Exception {
        
        try {
            engine.queryResources(new DummyCollection());
            assertTrue(false);
        } catch (XMLDBException e) {
            assertEquals(ErrorCodes.INVALID_COLLECTION, e.errorCode);
        }
    }
    
    public final void testQueryResourcesNullCollFail() throws Exception {
        try {
            engine.queryResources(null);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    public final void testQueryResourcesByXPathNullCollectionSuccess() throws Exception {

        SixdmlCollection coll = null;
        
        try {
            ResourceSet set = this.engine.queryResourcesByXPath(coll, "//foo");
            assertTrue(false);
        } catch (XMLDBException e) {
            assertEquals(ErrorCodes.INVALID_COLLECTION, e.errorCode);
        }
    }

    public final void testQueryResourcesByXPathNullQuerySuccess() throws Exception {

        SixdmlCollection coll = this.engine.queryCollection("/db/docs");
        ResourceSet set = this.engine.queryResourcesByXPath(coll, null);
        
        assertNotNull(set);
        assertTrue(set instanceof ResourceSet);
        
        assertEquals(0, set.getSize());
    }
    
    public final void testQueryResourcesByXPathInvalidQuerySuccess() throws Exception {

        SixdmlCollection coll = this.engine.queryCollection("/db/docs");
        try {
            this.engine.queryResourcesByXPath(coll, "[/(sdlkfjsldkfj/söadlk]");
            assertTrue(false);
        } catch (InvalidQueryException e) {
            assertTrue(true);
        } catch (XMLDBException e) {
            assertTrue(false);
        }
    }

    public final void testQueryResourceByXPathNonXapiCollFail() throws Exception {
        
        try {
            engine.queryResourceByXPath(new DummyCollection(), "", "");
        } catch (XMLDBException e) {
            assertEquals(ErrorCodes.INVALID_COLLECTION, e.errorCode);
        }
    }
    
    //public final void testQueryResourceByXPathCachedSuccess() throws Exception {
    //}
    
    public final void testQueryResourceByXPathNullParamsFail() throws Exception {
        
        SixdmlCollection coll = engine.queryCollection("/db/docs");
        String names[] = coll.listResources();
        
        String name = names[0];
        
        try {
            engine.queryResourceByXPath(null, name, "//*");
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
        
        try {
            engine.queryResourceByXPath(coll, null, "//*");
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
        
        try {
            engine.queryResourceByXPath(coll, name, null);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }
    
    public final void testQueryResourceByXPathInvalidQueryFail() throws Exception {
        SixdmlCollection coll = engine.queryCollection("/db/docs");
        String names[] = coll.listResources();
        
        String name = names[0];
        
        try {
            engine.queryResourceByXPath(coll, name, "foo[(/\\");
            assertTrue(false);
        } catch (InvalidQueryException e) {
            assertTrue(true);
        }
    }
    
    public final void testQueryResourceByXPathCachedSuccess() throws Exception {
        
        SixdmlCollection coll = engine.queryCollection("/db/docs");
        String names[] = coll.listResources();
        
        String name = names[0];
        
        long start = System.currentTimeMillis();
        engine.queryResourceByXPath(coll, name, "//*");
        long end = System.currentTimeMillis();
        
        long time = end - start;
        
        for (int i = 0; i < 10; i++) {
            start = System.currentTimeMillis();
            engine.queryResourceByXPath(coll, name, "//*");
            end = System.currentTimeMillis();
            
            System.out.println(time + "   " + (end -start));
            //assertTrue(time > (end - start));
        }
    }

    public final void testQueryVCLSchemaNonXapiCollFail() throws Exception {

        VirtualCollection coll = new DummyVCollection();
        
        try {
            engine.queryVCLSchema(coll);
            assertTrue(false);
        } catch (XMLDBException e) {
            assertEquals(ErrorCodes.INVALID_COLLECTION, e.errorCode);
        }
    }
    
    public final void testQueryVCLSchemaNullCollFail() throws Exception {
        try {
            engine.queryVCLSchema(null);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    public final void testQueryPCVResourceNonXapiCollFail() throws Exception {
        
        SixdmlCollection coll  = engine.queryCollection("/db/vcl-data/myvc");
        VirtualCollection dummy = new DummyVCollection();
        
        String[] names = coll.listResources();

        try {
            engine.queryPCVResource(dummy, names[0]);
            assertTrue(false);
        } catch (XMLDBException e) {
            assertEquals(ErrorCodes.INVALID_COLLECTION, e.errorCode);
        }
    }
    
    public final void testQueryPCVResourceNullCollFail() throws Exception {

        SixdmlCollection coll  = engine.queryCollection("/db/vcl-data/myvc");
        VirtualCollection dummy = null;
        
        String[] names = coll.listResources();

        try {
            engine.queryPCVResource(dummy, names[0]);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

}
