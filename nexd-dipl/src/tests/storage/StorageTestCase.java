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
 * $Log: StorageTestCase.java,v $
 * Revision 1.1  2005/05/11 17:31:40  nexd
 * Refactoring and extended test cases
 *
 */
package tests.storage;

import java.util.HashMap;

import junit.framework.TestCase;
import de.xplib.nexd.engine.store.StorageFactoryImpl;
import de.xplib.nexd.store.AbstractStorageFactory;
import de.xplib.nexd.store.StorageException;
import de.xplib.nexd.store.StorageI;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class StorageTestCase extends TestCase {

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        
        System.setProperty("javax.xml.parsers.DocumentBuilderFactory",
            "de.xplib.nexd.engine.xml.jaxp.DocumentBuilderFactoryImpl");
        
        System.setProperty(AbstractStorageFactory.STORAGE_FACTORY_KEY, "de.xplib.nexd.engine.store.StorageFactoryImpl");
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    public void testStorageImplNoJDBCDriverFail() throws Exception {
        HashMap map = new HashMap();
        map.put("jdbc-driver", "this.is.no.jdbcDriver");
        map.put("jdbc-url", "jdbc:hsqldb:/home/manuel/tmp/nexd/nexd2.db");
        
        try {
            new StorageFactoryImpl().getUniqueStorage(map);
            assertTrue(false);
        } catch (StorageException e) {
        }
    }
    
    public void testOpenInvalidUserPassFail() throws Exception {
        HashMap map = new HashMap();
        map.put("jdbc-driver", "org.hsqldb.jdbcDriver");
        map.put("jdbc-url", "jdbc:hsqldb:/home/manuel/tmp/nexd/nexd2.db");
        
        StorageI store = new StorageFactoryImpl().getUniqueStorage(map);
        try {
            store.open("", "");
            assertTrue(false);
        } catch (StorageException e) {
        }
        
        try {
            store.open("sa", "---------");
            assertTrue(false);
        } catch (StorageException e) {
        }
    }

}
