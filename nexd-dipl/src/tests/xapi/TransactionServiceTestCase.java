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

import junit.framework.TestCase;

import org.sixdml.SixdmlDatabase;
import org.sixdml.dbmanagement.SixdmlCollection;
import org.sixdml.dbmanagement.SixdmlResource;
import org.sixdml.dbmanagement.SixdmlTransactionService;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.ErrorCodes;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.CollectionManagementService;

import de.xplib.nexd.comm.NEXDEngineI;
import de.xplib.nexd.engine.xapi.DatabaseImpl;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.4 $
 */
public class TransactionServiceTestCase extends TestCase {
    
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
    
    private SixdmlTransactionService service = null;
    
    public TransactionServiceTestCase() throws Exception {
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
        this.service = (SixdmlTransactionService) this.docs.getService(
                "SixdmlTransactionService", "1.0");
    }
    
    protected void tearDown() throws Exception {
        super.tearDown();
        
        ((DatabaseImpl) this.db).getEngine().close();
    }
    
    
    public final void testBeginTransaction() throws Exception {
        
        Collection c = this.docs.getChildCollection("foo");
        if (c == null) {
            ((CollectionManagementService) docs.getService("CollectionManagementService", "1.0")).createCollection("foo");
        }
        
        
        assertNull(this.docs.getProperty(NEXDEngineI.TRANSACTION_ID_KEY));
        
        this.service.begin();
        String id1 = this.docs.getProperty(NEXDEngineI.TRANSACTION_ID_KEY);
        assertNotNull(id1);
        
        Collection child = this.docs.getChildCollection("foo");
        String id2 = child.getProperty(NEXDEngineI.TRANSACTION_ID_KEY);
        assertNotNull(id2);
        assertEquals(id1, id2);
        
        Collection parent = this.docs.getParentCollection();
        String id3 = parent.getProperty(NEXDEngineI.TRANSACTION_ID_KEY);
        assertNotNull(parent.getProperty(NEXDEngineI.TRANSACTION_ID_KEY));
        assertEquals(id1, id3);
        
        assertTrue(service.isActive());
        
        this.service.commit();
        
        assertFalse(service.isActive());
     
    }
    
    public final void testCommitTransaction() throws Exception {
        
        assertNull(this.docs.getProperty(NEXDEngineI.TRANSACTION_ID_KEY));
        
        this.service.begin();
        String id1 = this.docs.getProperty(NEXDEngineI.TRANSACTION_ID_KEY);
        assertNotNull(id1);
        
        this.service.commit();
        
        assertNull(this.docs.getProperty(NEXDEngineI.TRANSACTION_ID_KEY));
    }
    
    
    public final void testConflict() throws Exception {
        
        this.service.begin();
        
        // Coll is a child collection of docs, so a transaction on this part of
        // the database is already blocked
        SixdmlTransactionService tService = (SixdmlTransactionService) 
				this.coll.getService("SixdmlTransactionService", "1.0");
        
        boolean ok = false;
        try {
            tService.begin();
        } catch (XMLDBException e) {
            ok = true;
        }
        assertTrue("Expected an exception.", ok);
        
        this.service.commit();
        
        ok = true;
        try {
            tService.begin();
        } catch (XMLDBException e) {
            ok = false;
        }
        assertTrue("Expected no exception.", ok);
        
        tService.commit();
    }
    
    
    public void testRollbackNotImplementedFail() throws Exception {
        try {
            service.rollback();
            assertTrue(false);
        } catch (XMLDBException e) {
            assertEquals(ErrorCodes.NOT_IMPLEMENTED, e.errorCode);
        }
    }

}
