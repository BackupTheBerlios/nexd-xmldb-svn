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
 * $Log: PCVResourceTestCase.java,v $
 * Revision 1.1  2005/05/11 17:31:40  nexd
 * Refactoring and extended test cases
 *
 */
package tests.xapi.vc;

import org.sixdml.SixdmlDatabase;
import org.sixdml.dbmanagement.SixdmlCollection;
import org.w3c.dom.Document;
import org.xml.sax.helpers.DefaultHandler;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;

import de.xplib.nexd.api.VirtualCollection;
import de.xplib.nexd.api.VirtualResource;
import de.xplib.nexd.api.pcvr.PCVResource;
import de.xplib.nexd.engine.xapi.DatabaseImpl;
import junit.framework.TestCase;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class PCVResourceTestCase extends TestCase {
    
    /**
     * Comment for <code>db</code>
     */
    private SixdmlDatabase db = null;
    
    /**
     * Comment for <code>vcoll</code>
     */
    private VirtualCollection vcoll =null;
    
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
        
        this.vcoll = (VirtualCollection) this.db.getCollection(
                "nexd://localhost./db/vcl-data/myvc", "sa", "");
        
        this.dbColl = (SixdmlCollection) this.db.getCollection(
                "nexd://localhost./db", "sa", ""); 
        
    }
    
    protected void tearDown() throws Exception {
        super.tearDown();
        
        ((DatabaseImpl) this.db).getEngine().close();
    }

    public final void testGetContentAsDOM() throws Exception {
        String names[] = this.vcoll.listResources();
        assertTrue("Expected more than 0 resources", names.length > 0);
        
        VirtualResource vres = (VirtualResource) this.vcoll.getResource(names[0]);
        
        PCVResource pres = vres.getPreCompiledResource();
        
        assertTrue(pres.getContentAsDOM() instanceof Document);
    }

    public final void testGetId() throws Exception {
        String names[] = this.vcoll.listResources();
        assertTrue("Expected more than 0 resources", names.length > 0);
        
        VirtualResource vres = (VirtualResource) this.vcoll.getResource(names[0]);
        
        PCVResource pres = vres.getPreCompiledResource();
        
        assertEquals(vres.getId(), pres.getId());
    }

    public final void testGetReferenceCollections() throws Exception {
        
        String names[] = this.vcoll.listResources();
        assertTrue("Expected more than 0 resources", names.length > 0);
        
        VirtualResource vres = (VirtualResource) this.vcoll.getResource(names[0]);
        
        PCVResource pres = vres.getPreCompiledResource();
        
        String refs[] = pres.getReferenceCollections();
        assertTrue("Expected result to be an array of Strings", refs instanceof String[]);
    }

    public final void testGetReferenceResources() throws Exception {
        String names[] = this.vcoll.listResources();
        assertTrue("Expected more than 0 resources", names.length > 0);
        
        VirtualResource vres = (VirtualResource) this.vcoll.getResource(names[0]);
        
        PCVResource pres = vres.getPreCompiledResource();
        
        String refs[] = pres.getReferenceResources();
        assertTrue("Expected result to be an array of Strings", refs instanceof String[]);
    }

    public final void testGetContentAsSAX() throws Exception {
        String names[] = this.vcoll.listResources();
        assertTrue("Expected more than 0 resources", names.length > 0);
        
        VirtualResource vres = (VirtualResource) this.vcoll.getResource(names[0]);
        
        PCVResource pres = vres.getPreCompiledResource();
        
        pres.getContentAsSAX(new DefaultHandler());
    }

    public final void testGetDocumentId() throws Exception {
        String names[] = this.vcoll.listResources();
        assertTrue("Expected more than 0 resources", names.length > 0);
        
        VirtualResource vres = (VirtualResource) this.vcoll.getResource(names[0]);
        
        PCVResource pres = vres.getPreCompiledResource();
        
        assertEquals(vres.getDocumentId(), pres.getDocumentId());
    }

    public final void testSetContentAsDOM() throws Exception {
        String names[] = this.vcoll.listResources();
        assertTrue("Expected more than 0 resources", names.length > 0);
        
        VirtualResource vres = (VirtualResource) this.vcoll.getResource(names[0]);
        
        PCVResource pres = vres.getPreCompiledResource();
        
        try {
            pres.setContentAsDOM(vres.getContentAsDOM());
            assertTrue(false);
        } catch (XMLDBException e) {
            assertTrue(true);
        }        
    }

    public final void testSetContentAsSAX() throws Exception {
        String names[] = this.vcoll.listResources();
        assertTrue("Expected more than 0 resources", names.length > 0);
        
        VirtualResource vres = (VirtualResource) this.vcoll.getResource(names[0]);
        
        PCVResource pres = vres.getPreCompiledResource();
        
        try {
            pres.setContentAsSAX();
            assertTrue(false);
        } catch (XMLDBException e) {
            assertTrue(true);
        }
    }

    public final void testGetContent() throws Exception {
        String names[] = this.vcoll.listResources();
        assertTrue("Expected more than 0 resources", names.length > 0);
        
        VirtualResource vres = (VirtualResource) this.vcoll.getResource(names[0]);
        
        PCVResource pres = vres.getPreCompiledResource();
        
        assertTrue("Expected to be a String", pres.getContent() instanceof String);
        assertTrue("Expected an xml declaration", ((String) pres.getContent()).indexOf("<?xml") != -1);
        
    }

    public final void testGetParentCollection() throws Exception {
        String names[] = this.vcoll.listResources();
        assertTrue("Expected more than 0 resources", names.length > 0);
        
        VirtualResource vres = (VirtualResource) this.vcoll.getResource(names[0]);
        
        PCVResource pres = vres.getPreCompiledResource();
        
        assertSame(this.vcoll, pres.getParentCollection());
    }

    public final void testGetResourceType() throws Exception {
        String names[] = this.vcoll.listResources();
        assertTrue("Expected more than 0 resources", names.length > 0);
        
        VirtualResource vres = (VirtualResource) this.vcoll.getResource(names[0]);
        
        PCVResource pres = vres.getPreCompiledResource();
        
        assertEquals(XMLResource.RESOURCE_TYPE, pres.getResourceType());
    }

    public final void testSetContent() throws Exception {
        String names[] = this.vcoll.listResources();
        assertTrue("Expected more than 0 resources", names.length > 0);
        
        VirtualResource vres = (VirtualResource) this.vcoll.getResource(names[0]);
        
        PCVResource pres = vres.getPreCompiledResource();
        
        try {
            pres.setContent("");
            assertTrue(false);
        } catch (XMLDBException e) {
            assertTrue(true);
        }
    }

}
