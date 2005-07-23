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
package tests.xapi;

import junit.framework.TestCase;

import org.sixdml.SixdmlDatabase;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.ErrorCodes;
import org.xmldb.api.base.Service;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.CollectionManagementService;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.4 $
 */
public class DatabaseTestCase extends TestCase {
    
    /**
     * Comment for <code>db</code>
     */
    private SixdmlDatabase db = null;
    
    /**
     * 
     */
    public DatabaseTestCase() throws Exception {
        
        String name = "de.xplib.nexd.engine.xapi.DatabaseImpl";
        Class clazz = Class.forName(name);
        
        this.db = (SixdmlDatabase) clazz.newInstance();
        DatabaseManager.registerDatabase(this.db);
    }

    /**
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    
    /**
     * @throws Exception ..
     */
    public void testGetName() throws Exception {
        assertEquals(this.db.getName(), "nexd");
    }
    
    /**
     * @throws Exception
     */
    public void testGetCollection() throws Exception {
        
        Collection coll = DatabaseManager.getCollection(
                "xmldb:nexd://localhost./db/docs/foo");
        
        assertEquals(coll.getName(), "foo");
    }
    
    /**
     * @throws Exception
     */
    public void testCollectionManagementServiceCreate() throws Exception {
        Collection coll = DatabaseManager.getCollection(
                "xmldb:nexd://localhost./db", "sa", "");
        
        CollectionManagementService cms = (CollectionManagementService)
        		coll.getService("CollectionManagementService", "1.0");
        
        
        int code = -1;
        try {
            cms.createCollection("docs/foo");
        } catch (XMLDBException e) {
            code = e.errorCode;
        }
        assertEquals(code, ErrorCodes.VENDOR_ERROR);
        
        cms.createCollection("/foo/");
    }
    
    /**
     * @throws Exception
     */
    public void testCollectionManagementServiceDelete() throws Exception {
        Collection coll = DatabaseManager.getCollection(
                "xmldb:nexd://localhost./db", "sa", "");
        
        CollectionManagementService cms = (CollectionManagementService)
        		coll.getService("CollectionManagementService", "1.0");
        
        int code = -1;
        try {
            cms.removeCollection("/foobar");
        } catch (XMLDBException e) {
            code = e.errorCode;
        }
        
        assertEquals(code, ErrorCodes.NO_SUCH_COLLECTION);
        
        cms.removeCollection("foo");
        
    }
    
    
    public void testGetServices() throws Exception {
        Service[] all = db.getServices();
        assertEquals(3, all.length);
    }
    
    public void testGetSixdmlTransactionService() throws Exception {
        assertNotNull(db.getService("SixdmlTransactionService", "1.0"));
    }
    
    public void testAcceptUri() throws Exception {
        assertTrue(db.acceptsURI("nexd://localhost./db"));
        assertTrue(db.acceptsURI("nexd://192.1689.0.1:9999/db"));
        assertTrue(db.acceptsURI("nexd://192.1689.0.1/db"));
        assertFalse(db.acceptsURI("nexd://192.1689.0.1:9999"));
        assertFalse(db.acceptsURI("nexd://192.1689.0.1"));
    }
    
    public void testGetConformanceLevel() throws Exception {
        assertEquals("1.0", db.getConformanceLevel());
    }

}
