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
import org.sixdml.command.SixdmlPreparedStatement;
import org.sixdml.command.SixdmlStatement;
import org.sixdml.command.SixdmlStatementService;
import org.sixdml.dbmanagement.SixdmlCollection;
import org.sixdml.dbmanagement.SixdmlResource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xmldb.api.DatabaseManager;

import de.xplib.nexd.engine.xapi.DatabaseImpl;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.3 $
 */
public class StatementServiceTestCase extends TestCase {
    
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
    
    private SixdmlStatementService service = null;
    
    public StatementServiceTestCase() throws Exception {
        super("StatementServiceTestCase");
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
        this.service = (SixdmlStatementService) this.db.getService(
                "SixdmlStatementService", "1.0");
    }
    
    protected void tearDown() throws Exception {
        super.tearDown();
        
        ((DatabaseImpl) this.db).getEngine().close();
    }
    
    public final void testCreateStatement() throws Exception {
        SixdmlStatement stmt = this.service.createStatement(); 
        assertNotNull(stmt);
        assertTrue(stmt instanceof SixdmlStatement);
    }
    
    public final void testCreatePreparedStatement() throws Exception {
        SixdmlPreparedStatement stmt = this.service.prepareStatement("");
        assertNotNull(stmt);
    }
    
    public final void testShowCollectionStmt() throws Exception {
        SixdmlStatement stmt = this.service.createStatement();
        stmt.execute("SHOW COLLECTION /db/docs ;", System.out);
    }
    
    public final void testShowCollectionPreStmt() throws Exception {
        SixdmlPreparedStatement stmt = this.service.prepareStatement(
                "SHOW COLLECTION ? ;");
        
        stmt.setObject(1, "/db/docs");
        stmt.execute(System.out);
    }
    
    public final void testSelectResourceStmt() throws Exception {
        SixdmlStatement stmt = this.service.createStatement();
        
        assertTrue(stmt.execute("SELECT /* FROM /db/docs/document.xml") instanceof Document);
    }
    
    public final void testSelectResourcePreStmt() throws Exception {
        SixdmlPreparedStatement stmt = this.service.prepareStatement(
                "SELECT ? FROM ?");
        
        stmt.setObject(2, "/db/docs/document.xml");
        stmt.setObject(1, "/*");
        
        Document doc = stmt.execute();
        assertNotNull(doc);
        
        stmt.setObject(2, "/db/docs/foo.xml");
        try {
            assertNull(stmt.execute());
            assertTrue(false);
        } catch (Exception e) {
            assertTrue(true);
        }
    }
    
    
    public final void testSelectResourceXPathStmt() throws Exception {
        SixdmlStatement stmt = this.service.createStatement();
        
        Document doc = stmt.execute("SELECT //id[@type='SMALL'] FROM COLLECTION /db/docs;");
        assertNotNull(doc);
        
        Element elem = (Element) doc.getElementsByTagName("id").item(0);
        assertNotNull(elem);
        
        assertEquals("binarycloud-entity-modified.xml", elem.getParentNode().getAttributes().getNamedItem("resource-name").getNodeValue());
    }

}
