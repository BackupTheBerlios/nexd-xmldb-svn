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
 * $Log: StatementTestCase.java,v $
 * Revision 1.1  2005/05/11 17:31:40  nexd
 * Refactoring and extended test cases
 *
 */
package tests.xapi;

import java.io.StringWriter;

import junit.framework.TestCase;

import org.sixdml.SixdmlDatabase;
import org.sixdml.command.SixdmlPreparedStatement;
import org.sixdml.command.SixdmlStatement;
import org.sixdml.command.SixdmlStatementService;
import org.sixdml.dbmanagement.SixdmlCollection;
import org.w3c.dom.Document;
import org.xml.sax.helpers.DefaultHandler;
import org.xmldb.api.DatabaseManager;

import de.xplib.nexd.api.VirtualCollection;
import de.xplib.nexd.engine.xapi.DatabaseImpl;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class StatementTestCase extends TestCase {
    
    
    /**
     * Comment for <code>db</code>
     */
    private SixdmlDatabase db = null;
    
    /**
     * Comment for <code>coll</code>
     */
    private SixdmlCollection coll =null;
    
    private VirtualCollection vcoll = null;
    
    /**
     * Comment for <code>dbColl</code>
     */
    private SixdmlCollection dbColl = null;
    
    private SixdmlStatement stmt;
    
    private SixdmlStatementService sss;
    
    /**
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        
        String name = "de.xplib.nexd.engine.xapi.DatabaseImpl";
        Class clazz = Class.forName(name);
        
        this.db = (SixdmlDatabase) clazz.newInstance();
        DatabaseManager.registerDatabase(this.db);
        
        this.coll = (SixdmlCollection) this.db.getCollection(
                "nexd://localhost./db/docs", "sa", "");
        
        this.vcoll = (VirtualCollection) this.db.getCollection(
                "nexd://localhost./db/vcl-data/myvc", "sa", "");
        
        this.dbColl = (SixdmlCollection) this.db.getCollection(
                "nexd://localhost./db", "sa", "");
        
        
        sss = (SixdmlStatementService) this.db.getService("SixdmlStatementService", "1.0");
        
        stmt = sss.createStatement();
        
        
    }
    
    protected void tearDown() throws Exception {
        super.tearDown();
        
        ((DatabaseImpl) this.db).getEngine().close();
    }
    
    /*
     * Class under test for Document execute()
     */
    public final void testExecute() throws Exception {
        Document doc = stmt.execute("SELECT /* FROM /db/docs/document.xml");
        assertNotNull(doc);
        assertNotNull(doc.getDocumentElement());
    }
    
    /*
     * Class under test for void execute(DefaultHandler)
     */
    public final void testExecuteDefaultHandler() throws Exception {
        stmt.execute("SELECT /* FROM /db/docs/document.xml", new DefaultHandler());
    }
    
    /*
     * Class under test for void execute(OutputStream)
     */
    public final void testExecuteOutputStream() throws Exception {
        stmt.execute("SELECT /* FROM /db/docs/document.xml", System.out);
    }
    
    /*
     * Class under test for void execute(Writer)
     */
    public final void testExecuteWriter() throws Exception {
        StringWriter sw = new StringWriter();
        stmt.execute("SELECT /* FROM /db/docs/document.xml", sw);
        
        assertTrue(sw.getBuffer().length() > 0);
    }
    
    /*
     * Class under test for Document execute(String)
     */
    public final void testExecuteString() throws Exception {
        SixdmlPreparedStatement sps = sss.prepareStatement("SELECT ? FROM /db/docs/document.xml");
        sps.setObject(1, "/*");
        
        Document doc = sps.execute();
        assertNotNull(doc);
        assertNotNull(doc.getDocumentElement());
    }
    
    /*
     * Class under test for void execute(String, DefaultHandler)
     */
    public final void testExecuteStringDefaultHandler() throws Exception {
        SixdmlPreparedStatement sps = sss.prepareStatement("SELECT ? FROM /db/docs/document.xml");
        sps.setObject(1, "/*");
        
        sps.execute(new DefaultHandler());
    }
    
    /*
     * Class under test for void execute(String, OutputStream)
     */
    public final void testExecuteStringOutputStream() throws Exception {
        
        SixdmlPreparedStatement sps = sss.prepareStatement("SELECT ? FROM /db/docs/document.xml");
        sps.setObject(1, "/*");
        
        sps.execute(System.out);
    }
    
    /*
     * Class under test for void execute(String, Writer)
     */
    public final void testExecuteStringWriter() throws Exception {
        
        SixdmlPreparedStatement sps = sss.prepareStatement("SELECT ? FROM /db/docs/document.xml");
        sps.setObject(1, "/*");
        
        StringWriter sw = new StringWriter();
        
        sps.execute(sw);
        
        assertTrue(sw.getBuffer().length() > 0);
    }
    
}
