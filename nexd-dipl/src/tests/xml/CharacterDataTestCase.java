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
 * $Log: CharacterDataTestCase.java,v $
 * Revision 1.1  2005/05/11 17:31:39  nexd
 * Refactoring and extended test cases
 *
 */
package tests.xml;

import junit.framework.TestCase;

import org.sixdml.dbmanagement.SixdmlCollection;
import org.sixdml.dbmanagement.SixdmlResource;
import org.w3c.dom.CharacterData;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;

import de.xplib.nexd.comm.NEXDEngineI;
import de.xplib.nexd.engine.NEXDEnginePool;
import de.xplib.nexd.engine.xml.dom.AbstractCharacterData;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class CharacterDataTestCase extends TestCase {
    
    private NEXDEngineI engine;
    
    private Document doc;
    
    private CharacterData charData;

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        
        engine = NEXDEnginePool.getInstance().getEngine();
        engine.open("sa", "");
        
        SixdmlCollection coll = engine.queryCollection("/db/docs");
        
        doc = (Document) ((SixdmlResource) engine.queryResource(coll, "document.xml")).getContentAsDOM();
        
        charData = (CharacterData) doc.getDocumentElement().getFirstChild().getNextSibling().getNextSibling().getNextSibling().getFirstChild().getNextSibling().getFirstChild();
        
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        
        engine.close();
        
        super.tearDown();
    }

    public final void testGetNodeValue() {
        
        String nv = charData.getNodeValue();
        assertEquals("MySQL", nv);
    }

    public final void testSetNodeValue() {
        
        charData.setNodeValue("HsqlDB");
        String nv = charData.getNodeValue();
        assertEquals("HsqlDB", nv);
    }

    public final void testGetTextContent() {
        String tc = ((AbstractCharacterData) charData).getTextContent();
        assertEquals("MySQL", tc);
        
        assertEquals(tc, charData.getNodeValue());
    }

    public final void testGetData() {
        String d = charData.getData();
        assertEquals("MySQL", d);
    }

    public final void testSetData() {
        charData.setData("Postgres");
        String d = charData.getData();
        
        assertEquals("Postgres", d);
        assertEquals(d, charData.getNodeValue());
    }

    public final void testGetLength() {
        assertEquals(5, charData.getLength());
        
        charData.setNodeValue("Manuel");
        assertEquals(6, charData.getLength());
        
        charData.setData("Postgres");
        assertEquals(8, charData.getLength());
    }

    public final void testSubstringData() {
        String sd = charData.substringData(0, 10);
        assertEquals("MySQL", sd);
        
        sd = charData.substringData(2, 3);
        assertEquals("SQL", sd);
        
        charData.setData("Manuel Pichler");
        sd = charData.substringData(7, 7);
        assertEquals("Pichler", sd);
    }
    
    public final void testSubstringDataNegativeFail() {
        
        try {
            String sd = charData.substringData(-1, 5);
            assertTrue(false);
        } catch (DOMException e) {
            assertEquals(DOMException.INDEX_SIZE_ERR, e.code);
        }
        
        try {
            String sd = charData.substringData(0, -1);
            assertTrue(false);
        } catch (DOMException e) {
            assertEquals(DOMException.INDEX_SIZE_ERR, e.code);
        }
        
        try {
            charData.substringData(-1, -5);
            assertTrue(false);
        } catch (DOMException e) {
            assertEquals(DOMException.INDEX_SIZE_ERR, e.code);
        }
        
    }

    public final void testAppendData() {
        
        String equal = "MySQL Database";
        
        charData.appendData(" Database");
        
        assertEquals(equal, charData.getData());
        assertEquals(equal, charData.getNodeValue());
        assertEquals(equal, ((AbstractCharacterData) charData).getTextContent());
    }

    public final void testInsertData() {
        
        charData.insertData(0, "The ");
        
        assertEquals("The MySQL", charData.getData());
        assertEquals("The MySQL", charData.getNodeValue());
        assertEquals("The MySQL", ((AbstractCharacterData) charData).getTextContent());
        
        charData.insertData(9, " Database!");
        
        assertEquals("The MySQL Database!", charData.getData());
        assertEquals("The MySQL Database!", charData.getNodeValue());
        assertEquals("The MySQL Database!", ((AbstractCharacterData) charData).getTextContent());
    }
    
    public final void testInsertDataNegativeOffsetFail() {
        
        try {
            charData.insertData(-1, "Foo");
            assertTrue(false);
        } catch (DOMException e) {
            assertEquals(DOMException.INDEX_SIZE_ERR, e.code);
        }
        
    }

    public final void testDeleteData() {
        
        charData.setData("The Hsql Database");
        
        charData.deleteData(0, 4);
        assertEquals("Hsql Database", charData.getData());
        assertEquals("Hsql Database", charData.getNodeValue());
        
        charData.deleteData(4, 9);
        assertEquals("Hsql", charData.getData());
        assertEquals("Hsql", charData.getNodeValue());
        
        charData.deleteData(0, 6);
        assertEquals("", charData.getData());
        assertEquals("", charData.getNodeValue());
    }
    
    public final void testDeleteDataNegativeParamsFail() {
        try {
            charData.deleteData(0, -1);
            assertTrue(false);
        } catch (DOMException e) {
            assertEquals(DOMException.INDEX_SIZE_ERR, e.code);
        }
        
        try {
            charData.deleteData(-1, 1);
            assertTrue(false);
        } catch (DOMException e) {
            assertEquals(DOMException.INDEX_SIZE_ERR, e.code);
        }
        
        try {
            charData.deleteData(-8, -1);
            assertTrue(false);
        } catch (DOMException e) {
            assertEquals(DOMException.INDEX_SIZE_ERR, e.code);
        }
    }

    public final void testReplaceData() {
        
        charData.replaceData(0, 2, "H");
        assertEquals("HSQL", charData.getData());
        assertEquals("HSQL", charData.getNodeValue());
        
        charData.replaceData(1, 3, "sqlDB");
        assertEquals("HsqlDB", charData.getData());
        assertEquals("HsqlDB", charData.getNodeValue());
        
        charData.replaceData(4, 5, "");
        assertEquals("Hsql", charData.getData());
        assertEquals("Hsql", charData.getNodeValue());
    }
    
    public final void testReplaceDataNegativeParamsFail() {
        
        try {
            charData.replaceData(-1, 4, "Foo");
            assertTrue(false);
        } catch (DOMException e) {
            assertEquals(DOMException.INDEX_SIZE_ERR, e.code);
        }
        
        try {
            charData.replaceData(3, -1, "Foo");
            assertTrue(false);
        } catch (DOMException e) {
            assertEquals(DOMException.INDEX_SIZE_ERR, e.code);
        }
        
        try {
            charData.replaceData(-1, -1, "Foo");
            assertTrue(false);
        } catch (DOMException e) {
            assertEquals(DOMException.INDEX_SIZE_ERR, e.code);
        }
    }

}
