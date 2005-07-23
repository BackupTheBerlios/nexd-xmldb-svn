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
 * $Log: VResourceTestCase.java,v $
 * Revision 1.5  2005/05/11 17:31:40  nexd
 * Refactoring and extended test cases
 *
 * Revision 1.4  2005/05/08 11:59:32  nexd
 * restructuring
 *
 * Revision 1.3  2005/04/22 14:59:41  nexd
 * SOAP support and performance update.
 *
 * Revision 1.2  2005/04/13 19:06:32  nexd
 * Minor API changes and a documentation update.
 *
 * Revision 1.1  2005/04/10 13:18:46  nexd
 * New JUnit test cases and minor bug fixes.
 *
 */
package tests.xapi;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import junit.framework.TestCase;

import org.sixdml.SixdmlDatabase;
import org.sixdml.dbmanagement.SixdmlCollection;
import org.sixdml.dbmanagement.SixdmlResource;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XPathQueryService;

import de.xplib.nexd.api.VirtualResource;
import de.xplib.nexd.api.pcvr.PCVResource;
import de.xplib.nexd.engine.xapi.DatabaseImpl;
import de.xplib.nexd.engine.xapi.VirtualCollectionImpl;
import de.xplib.nexd.engine.xapi.VirtualResourceImpl;
import de.xplib.nexd.engine.xml.dom.DOMImplementationImpl;
import de.xplib.nexd.engine.xml.dom.DocumentImpl;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.5 $
 */
public class VResourceTestCase extends TestCase {
    
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
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        
        ((DatabaseImpl) this.db).getEngine().close();
    }

    public final void testGetPreCompiledResource() throws Exception {
        
        String[] names = this.vcoll.listChildCollections();
        for (int i = 0; i < names.length; i++) {
            
            VirtualResource vres = (VirtualResource) this.vcoll.getResource(names[i]);
            PCVResource pcvres   = vres.getPreCompiledResource();
            
            assertNotNull("Expected PCVResource and not null.", pcvres);
            assertTrue("Expected a PCVResource and not " + pcvres.getClass(), pcvres instanceof PCVResource);
            
            String name = ((Document) pcvres.getContentAsDOM())
                                .getDocumentElement()
                                .getAttribute(PCVResource.ATTR_SCHEMA_RESNAME);
            
            assertEquals(names[i], name);
        }
    }

    public final void testGetDocumentId() throws Exception {
        
        XPathQueryService xqs = (XPathQueryService) this.vcoll.getService(
                "XPathQueryService", "1.0");
        
        String[] names = this.vcoll.listResources();
        for (int i = 0; i < names.length; i++) {

            VirtualResource vres = (VirtualResource) this.vcoll.getResource(names[i]);
            assertEquals(names[i], vres.getDocumentId());
            
            assertEquals(vres.getDocumentId(), vres.getId());
            
            ResourceSet rs = xqs.queryResource(names[i], "//author");
            
            for (int j = 0; j < rs.getSize(); j++) {
                VirtualResource qvres = (VirtualResource) rs.getResource(j);
                
                assertEquals(vres.getId(), qvres.getDocumentId());
                assertTrue(
                        "Expected getId() != getDocumentId()", 
                        !qvres.getId().equals(qvres.getDocumentId()));
            }
        }
        
    }

    public final void testGetContentAsDOM() throws Exception {
        
        String[] names = this.vcoll.listResources();
        for (int i = 0; i < names.length; i++) {
            VirtualResource vres = (VirtualResource) this.vcoll.getResource(names[i]);
            
            Node n = vres.getContentAsDOM();
            assertTrue(
                    "Expected content to be an instance of DocumentImpl and not " 
                    + n.getClass(), n instanceof DocumentImpl);
        }
    }

    public final void testSetContentAsDOM() throws Exception {
        
        DocumentBuilder dob = DocumentBuilderFactory.newInstance()
                                                   .newDocumentBuilder();
        Document doc = dob.parse(new InputSource(new StringReader(
                "<foo><br/></foo>")));
        
        String[] names = this.vcoll.listResources();
        for (int i = 0; i < names.length; i++) {
            VirtualResource vres = (VirtualResource) this.vcoll.getResource(names[i]);
            
            try {
                vres.setContentAsDOM(doc);
                assertTrue(false);
            } catch (XMLDBException e) {
                assertTrue(true);
            }
            
            assertNotSame(doc, vres.getContentAsDOM());
        }
    }

    public final void testGetContentAsSAX() throws Exception {
        
        String[] names = this.vcoll.listResources();
        for (int i = 0; i < names.length; i++) {
            VirtualResource vres = (VirtualResource) this.vcoll.getResource(names[i]);
        }
        //TODO Implement getContentAsSAX().
    }

    public final void testSetContentAsSAX() throws Exception {
        
        SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
        
        String[] names = this.vcoll.listResources();
        for (int i = 0; i < names.length; i++) {
            VirtualResource vres = (VirtualResource) this.vcoll.getResource(names[i]);
            
            ContentHandler ch;
            try {
                ch = vres.setContentAsSAX();
                assertTrue(false);
            } catch (XMLDBException e) {
                assertTrue(true);
            }
            
        }
    }

    public final void testGetContent() throws Exception {
        
        String[] names = this.vcoll.listResources();
        for (int i = 0; i < names.length; i++) {
            VirtualResource vres = (VirtualResource) this.vcoll.getResource(names[i]);
            
            String s = (String) vres.getContent();
            assertEquals("<?xml", s.substring(0, 5));
            
            s = s.substring(s.indexOf("?>") + 2).trim();
            assertEquals("<author", s.substring(0, 7));
        }
        
        
    }

    public final void testSetContent() throws Exception {
        
        Document d = new DOMImplementationImpl().createDocument(null, null, null);
        Node foo = d.appendChild(d.createElement("foo"));
        foo.appendChild(d.createElement("bar"));
        
        String[] names = this.vcoll.listResources();
        for (int i = 0; i < names.length; i++) {
            VirtualResource vres = (VirtualResource) this.vcoll.getResource(names[i]);
            
            try {
                vres.setContent(d);
                assertTrue(false);
            } catch (XMLDBException e) {
                assertTrue(true);
            }
        }
    }

    public final void testGetName() throws Exception {
        
        XPathQueryService xqs = (XPathQueryService) this.vcoll.getService(
                "XPathQueryService", "1.0");
        
        String[] names = this.vcoll.listResources();
        for (int i = 0; i < names.length; i++) {
            VirtualResourceImpl vres = (VirtualResourceImpl) this.vcoll.getResource(names[i]);
            
            assertEquals(vres.getId(), vres.getName());
            assertEquals(vres.getDocumentId(), vres.getName());
            
            ResourceSet rs = xqs.queryResource(vres.getId(), "//author");
            
            for (int j = 0; j < rs.getSize(); j++) {
                VirtualResourceImpl qvres = (VirtualResourceImpl) rs.getResource(j);
                assertEquals(vres.getName(), qvres.getName());
            }
        }
    }

    public final void testGetParentCollection() throws Exception {
        
        String[] names = this.vcoll.listResources();
        for (int i = 0; i < names.length; i++) {
            VirtualResourceImpl vres = (VirtualResourceImpl) this.vcoll.getResource(names[i]);
            
            assertSame(this.vcoll, vres.getParentCollection());
        }
    }

    public final void testGetResourceType() throws Exception {
        
        String[] names = this.vcoll.listResources();
        for (int i = 0; i < names.length; i++) {
            VirtualResourceImpl vres = (VirtualResourceImpl) this.vcoll.getResource(names[i]);
            
            assertEquals(SixdmlResource.RESOURCE_TYPE, vres.getResourceType());
        }
    }

}

