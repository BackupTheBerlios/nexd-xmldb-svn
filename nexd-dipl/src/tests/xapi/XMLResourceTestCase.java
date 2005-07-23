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
 * $Log: XMLResourceTestCase.java,v $
 * Revision 1.1  2005/05/11 17:31:40  nexd
 * Refactoring and extended test cases
 *
 */
package tests.xapi;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import junit.framework.TestCase;

import org.sixdml.dbmanagement.SixdmlCollection;
import org.w3c.dom.Document;
import org.xmldb.api.base.ErrorCodes;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;

import de.xplib.nexd.comm.NEXDEngineI;
import de.xplib.nexd.engine.NEXDEnginePool;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class XMLResourceTestCase extends TestCase {
    
    NEXDEngineI engine;
    
    SixdmlCollection coll;

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        
        engine = NEXDEnginePool.getInstance().getEngine();
        engine.open("sa", "");
        coll = engine.queryCollection("/db/docs");
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        
        engine.close();
    }

    public final void testGetDocumentId() {
        //TODO Implement getDocumentId().
    }

    public final void testGetContentAsDOM() {
        //TODO Implement getContentAsDOM().
    }

    public final void testSetContentAsDOM() {
        //TODO Implement setContentAsDOM().
    }

    public final void testGetContentAsSAX() {
        //TODO Implement getContentAsSAX().
    }

    public final void testSetContentAsSAX() throws Exception {
        //TODO Implement setContentAsSAX().
        
        XMLResource res = (XMLResource) coll.createResource("mydocId", XMLResource.RESOURCE_TYPE);
        
        SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
        parser.getXMLReader().setContentHandler(res.setContentAsSAX());
        parser.getXMLReader().parse("data/test/conf.xml");
        
        assertNotNull(res.getContent());
        assertEquals(String.class, res.getContent().getClass());
        
        assertNotNull(res.getContentAsDOM());
        assertTrue(res.getContentAsDOM() instanceof Document);
    }

    public final void testGetContent() {
        //TODO Implement getContent().
    }

    public final void testSetContentFile() throws Exception {
        
        XMLResource res = (XMLResource) coll.createResource("mydocId", XMLResource.RESOURCE_TYPE);
        
        res.setContent("data/test/conf.xml");
        
        assertNotNull(res.getContent());
        assertEquals(String.class, res.getContent().getClass());
        
        assertNotNull(res.getContentAsDOM());
        assertTrue(res.getContentAsDOM() instanceof Document);
        //TODO Implement setContent().
    }
    
    public final void testSetContentFail() throws Exception {
        XMLResource res = (XMLResource) coll.createResource("mydocId", XMLResource.RESOURCE_TYPE);
        
        try {
            res.setContent("---");
            assertTrue(false);
        } catch (XMLDBException e) {
            assertEquals(ErrorCodes.VENDOR_ERROR, e.errorCode);
        }
    }
    
    public final void testSetContentWrongXMLFail() throws Exception {
        XMLResource res = (XMLResource) coll.createResource("mydocId", XMLResource.RESOURCE_TYPE);
        
        try {
            res.setContent("<?xml version='<xml />' ?></doo>");
            assertTrue(false);
        } catch (XMLDBException e) {
            assertEquals(ErrorCodes.VENDOR_ERROR, e.errorCode);
        }
    }
    
    public final void testSetContentInvalidTypeFail() throws Exception {
        XMLResource res = (XMLResource) coll.createResource("mydocId", XMLResource.RESOURCE_TYPE);
        
        try {
            res.setContent(XMLResource.class);
            assertTrue(false);
        } catch (XMLDBException e) {
            assertEquals(ErrorCodes.VENDOR_ERROR, e.errorCode);
        }
    }

    public final void testGetName() {
        //TODO Implement getName().
    }

}
