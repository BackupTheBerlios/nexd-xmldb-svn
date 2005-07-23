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
 * $Log: CollectionManagementServiceTestCase.java,v $
 * Revision 1.1  2005/05/11 17:31:40  nexd
 * Refactoring and extended test cases
 *
 */
package tests.xapi;

import java.io.File;
import java.net.URL;
import java.util.HashMap;

import junit.framework.TestCase;

import org.sixdml.SixdmlDatabase;
import org.sixdml.dbmanagement.SixdmlCollection;
import org.sixdml.dbmanagement.SixdmlCollectionManagementService;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.ErrorCodes;
import org.xmldb.api.base.XMLDBException;

import de.xplib.nexd.engine.xapi.DatabaseImpl;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class CollectionManagementServiceTestCase extends TestCase {
    
    /**
     * Comment for <code>db</code>
     */
    private SixdmlDatabase db = null;
    
    private SixdmlCollection docs = null;
    
    private SixdmlCollectionManagementService service = null;

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        
        String name = "de.xplib.nexd.engine.xapi.DatabaseImpl";
        Class clazz = Class.forName(name);
        
        this.db = (SixdmlDatabase) clazz.newInstance();
        DatabaseManager.registerDatabase(this.db);
        
        
        this.docs = (SixdmlCollection) this.db.getCollection(
                "nexd://localhost./db/docs", "sa", "");
        
        this.service = (SixdmlCollectionManagementService) this.docs.getService(
                "SixdmlCollectionManagementService", "1.0");
    }
    
    protected void tearDown() throws Exception {
        super.tearDown();
        
        ((DatabaseImpl) this.db).getEngine().close();
    }
    

    /*
     * Class under test for Collection createCollection(String, URL)
     */
    public final void testCreateCollectionStringURL() throws Exception {
        URL schema = new File("data/test/dtd/package.dtd").toURL();
        
        assertNotNull(service.createCollection("bar", schema));
    }

    public final void testRemoveCollectionWithSchema() throws Exception {
        service.removeCollection("bar");
    }

    public final void testCreateIndex() throws Exception {
        service.createIndex("FOO", new HashMap());
    }

    public final void testGetSupportedIndexTypes() throws Exception {
        assertEquals(0, service.getSupportedIndexTypes().length);
    }

    /*
     * Class under test for Collection createCollection(String)
     */
    public final void testCreateCollectionString() {
        //TODO Implement createCollection().
    }

    public final void testRemoveCollectionRootFail() throws Exception {
        try {
            service.removeCollection("/db");
            assertTrue(false);
        } catch (XMLDBException e) {
            assertEquals(ErrorCodes.INVALID_COLLECTION, e.errorCode);
        }
    }

}
