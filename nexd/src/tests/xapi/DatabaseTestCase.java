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

import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Database;

import junit.framework.TestCase;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 */
public class DatabaseTestCase extends TestCase {
    
    /**
     * Comment for <code>db</code>
     */
    private Database db = null;
    
    /**
     * 
     */
    public DatabaseTestCase() throws Exception {
        
        String name = "de.xplib.nexd.xapi.DatabaseImpl";
        Class clazz = Class.forName(name);
        
        this.db = (Database) clazz.newInstance();
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
        
        //DatabaseManager.getCollection("xmldb:nexd://localhost:8080/axis/");
        DatabaseManager.getCollection("xmldb:nexd://localhost./axis");
    }

}
