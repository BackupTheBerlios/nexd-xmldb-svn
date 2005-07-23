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
 * $Log: VCLSchemaTestCase.java,v $
 * Revision 1.5  2005/05/08 11:59:33  nexd
 * restructuring
 *
 * Revision 1.4  2005/04/22 14:59:42  nexd
 * SOAP support and performance update.
 *
 * Revision 1.3  2005/04/13 19:06:32  nexd
 * Minor API changes and a documentation update.
 *
 * Revision 1.2  2005/04/10 13:32:19  nexd
 * New JUnit test cases and some smaller bug fixes.
 *
 */
package tests.xapi.vc;

import junit.framework.TestCase;

import org.sixdml.SixdmlDatabase;
import org.sixdml.dbmanagement.SixdmlCollection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xmldb.api.DatabaseManager;

import de.xplib.nexd.api.vcl.VCLCollectionReference;
import de.xplib.nexd.api.vcl.VCLSchema;
import de.xplib.nexd.engine.xapi.DatabaseImpl;
import de.xplib.nexd.engine.xapi.VirtualCollectionImpl;
import de.xplib.nexd.engine.xapi.vcl.VCLSchemaImpl;
import de.xplib.nexd.engine.xml.dom.DocumentImpl;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.5 $
 */
public class VCLSchemaTestCase extends TestCase {

    /**
     * Comment for <code>db</code>
     */
    private SixdmlDatabase db = null;
    
    /**
     * Comment for <code>dbColl</code>
     */
    private SixdmlCollection dbColl = null;
    
    /**
     * Comment for <code>vcoll</code>
     */
    private VirtualCollectionImpl vcoll = null;
    
    /**
     * Comment for <code>schema</code>
     */
    private VCLSchema schema;

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        
        String name = "de.xplib.nexd.engine.xapi.DatabaseImpl";
        Class clazz = Class.forName(name);
        
        this.db = (SixdmlDatabase) clazz.newInstance();
        DatabaseManager.registerDatabase(this.db);
        
        this.dbColl = (SixdmlCollection) this.db.getCollection(
                "nexd://localhost./db", "sa", "");
        
        this.vcoll = (VirtualCollectionImpl) this.dbColl.getChildCollection(
                "vcl-data/myvc");
        
        this.schema = this.vcoll.getVCLSchema();
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        
        ((DatabaseImpl) this.db).getEngine().close();
    }

    public final void testGetPrefix() throws Exception {
        assertEquals("virtual", this.schema.getPrefix());
    }

    public final void testSetPrefix() {
        assertEquals("virtual", this.schema.getPrefix());
        ((VCLSchemaImpl) this.schema).setPrefix("manuel");
        assertEquals("manuel", this.schema.getPrefix());
    }

    public final void testGetPostfix() {
        assertEquals("vxml", this.schema.getPostfix());
    }

    public final void testSetPostfix() {
        assertEquals("vxml", this.schema.getPostfix());
        ((VCLSchemaImpl) this.schema).setPostfix("virtualxml");
        assertEquals("virtualxml", this.schema.getPostfix());
    }

    public final void testIsEnumerate() {
        assertTrue(this.schema.isEnumerate());
    }

    public final void testSetEnumerate() {
        assertTrue(this.schema.isEnumerate());
        ((VCLSchemaImpl) this.schema).setEnumerate(false);
        assertTrue("Expected true, because name is set.", this.schema.isEnumerate());
    }

    public final void testGetName() {
        assertEquals("autoren.xml", this.schema.getName());
    }

    public final void testSetName() {
        assertEquals("autoren.xml", this.schema.getName());
        ((VCLSchemaImpl) this.schema).setName("manuel");
        assertEquals("manuel", this.schema.getName());
        
        //((VCLSchemaImpl) this.schema).setName("");
        //assertNull(this.schema.getName());
        ((VCLSchemaImpl) this.schema).setEnumerate(true);
        assertTrue(this.schema.isEnumerate());
        ((VCLSchemaImpl) this.schema).setEnumerate(false);
        assertTrue(this.schema.isEnumerate());
        
        ((VCLSchemaImpl) this.schema).setName(null);
        assertTrue(this.schema.getName().equals(""));
        assertFalse(this.schema.isEnumerate());
    }

    public final void testGetCollectionReference() {
        
        VCLCollectionReference cRef = this.schema.getCollectionReference();
        assertNotNull("Expected collection reference and not null", cRef);

        assertEquals("/db/vcl-data/author", cRef.getMatch());
        assertEquals("", cRef.getSelect());
    }

    public final void testGetContentAsDOM() throws Exception {
        Document doc = (Document) this.schema.getContentAsDOM();
        assertNotNull("Expected Document and not null", doc);
        assertTrue("Expected DocumentImpl instance.", doc instanceof DocumentImpl);
        
        Element elem = doc.getDocumentElement();
        assertEquals("vcl:schema", elem.getTagName());
    }

}
