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
import org.sixdml.query.SixdmlQueryResultsMap;
import org.sixdml.query.SixdmlQueryService;
import org.sixdml.query.SixdmlXpathObject;
import org.xmldb.api.DatabaseManager;

import de.xplib.nexd.engine.xapi.DatabaseImpl;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.3 $
 */
public class QueryServiceTestCase extends TestCase {
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
    
    private SixdmlQueryService service = null;
    
    public QueryServiceTestCase() throws Exception {
        super("QueryServiceTestCase");
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
        
        this.res     = (SixdmlResource) this.docs.getResource("binarycloud-entity.xml");
        this.service = (SixdmlQueryService) this.docs.getService(
                "SixdmlQueryService", "1.0");
    }
    
    protected void tearDown() throws Exception {
        super.tearDown();
        
        ((DatabaseImpl) this.db).getEngine().close();
    }
    
    
    public final void testExecuteQuery_StringResourceBoolean() throws Exception {
        
        SixdmlXpathObject obj = this.service.executeQuery(
                "//@not-null", this.res);
        System.out.println(obj.getNodeSetAsXML());
        assertEquals(SixdmlXpathObject.BOOLEAN, obj.getType());
        assertTrue(obj.getObjectAsBoolean());
    }
    
    public final void testExecuteQuery_StringResourceNumber() throws Exception {
        
        SixdmlXpathObject obj = this.service.executeQuery(
                "//@version", this.res);
        
        assertEquals(SixdmlXpathObject.NUMBER, obj.getType());
        assertEquals(0.99999999, 1.0000000001 , obj.getObjectAsNumber());
    }
    
    public final void testExecuteQuery_StringResourceString() throws Exception {
        
        SixdmlXpathObject obj = this.service.executeQuery(
                "//id/@type", this.res);
        
        assertEquals(SixdmlXpathObject.STRING, obj.getType());
        assertEquals("BIGINT", obj.getObjectAsString());
    }
   
    public final void testExecuteQuery_StringResourceNodeList() throws Exception {
        
        SixdmlXpathObject obj = this.service.executeQuery(
                "//*[@type]", this.res);
        
        assertEquals(SixdmlXpathObject.NODESET, obj.getType());
        assertEquals(2, obj.getObjectAsNodeSet().getLength());
    }
    
    public final void testExecuteQuery_StringResourceTreeFrag() throws Exception {
        SixdmlXpathObject obj = this.service.executeQuery(
                "//*[@type and @type='BIGINT']", this.res);
        
        assertEquals(SixdmlXpathObject.TREE_FRAGMENT, obj.getType());
    }
    
    public final void testExecuteQuery_StringCollectionTreeFrag() throws Exception {
        
        SixdmlQueryResultsMap map = this.service.executeQuery(
                "//id[@type]", this.docs);
        
        assertEquals(2, map.size());
        
        //System.out.println(map.getXML());
    }

}
