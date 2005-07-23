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
 * $Log: VirtualResourceTestCase.java,v $
 * Revision 1.1  2005/05/11 17:31:40  nexd
 * Refactoring and extended test cases
 *
 */
package tests.xapi.vc;

import org.sixdml.SixdmlDatabase;
import org.sixdml.dbmanagement.SixdmlCollection;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
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
public class VirtualResourceTestCase extends TestCase {
    
    /**
     * Comment for <code>db</code>
     */
    private SixdmlDatabase db = null;
    
    /**
     * Comment for <code>vcoll</code>
     */
    private VirtualCollection vcoll =null;
    
    private VirtualResource vres = null;
    
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
        
        
        String[] names = this.vcoll.listResources();
        
        this.vres = (VirtualResource) this.vcoll.getResource(names[0]);
    }
    
    protected void tearDown() throws Exception {
        super.tearDown();
        
        ((DatabaseImpl) this.db).getEngine().close();
    }

    public final void testGetPreCompiledResource() throws Exception {

        PCVResource pres = this.vres.getPreCompiledResource();
        
        assertNotNull(pres);
        assertTrue(pres instanceof PCVResource);
    }

    public final void testGetDocumentId() throws Exception {
        assertEquals(this.vres.getId(), this.vres.getDocumentId());
    }

    public final void testGetContentAsDOM() throws Exception {
        Node cnt = this.vres.getContentAsDOM();
        
        assertNotNull(cnt);
        assertTrue(cnt instanceof Document);
    }

    public final void testSetContentAsDOM() throws Exception {
        
        try {
            vres.setContentAsDOM(vres.getContentAsDOM());
            assertTrue(false);
        } catch (XMLDBException e) {
            assertTrue(true);
        }
    }

    public final void testGetContentAsSAX() throws Exception {
        vres.getContentAsSAX(new DefaultHandler());
    }

    public final void testSetContentAsSAX() {
        try {
            vres.setContentAsSAX();
            assertTrue(false);
        } catch (XMLDBException e) {
            assertTrue(true);
        }
    }

    public final void testGetContent() throws Exception {
        
        Object cnt = vres.getContent();
        assertNotNull(cnt);
        assertTrue(cnt instanceof String);
        assertTrue(((String) cnt).indexOf("<?xml") != -1);
    }

    public final void testSetContent() {
        try {
            vres.setContent("");
            assertTrue(false);
        } catch (XMLDBException e) {
            assertTrue(true);
        }
    }

    public final void testGetParentCollection() throws Exception {
        assertSame(vcoll, vres.getParentCollection());
    }

    public final void testGetId() throws Exception {
        assertEquals(vres.getDocumentId(), vres.getId());
    }

    public final void testGetResourceType() throws Exception{
        assertEquals(XMLResource.RESOURCE_TYPE, vres.getResourceType());
    }

}
