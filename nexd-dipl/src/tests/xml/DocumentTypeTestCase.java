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
 * $Log: DocumentTypeTestCase.java,v $
 * Revision 1.1  2005/05/11 17:31:39  nexd
 * Refactoring and extended test cases
 *
 */
package tests.xml;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.TestCase;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.NamedNodeMap;

import de.xplib.nexd.engine.xml.dom.DocumentImpl;
import de.xplib.nexd.engine.xml.jaxp.DocumentBuilderFactoryImpl;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class DocumentTypeTestCase extends TestCase {
    
    Document doc;
    
    public void setUp() throws Exception {
        super.setUp();
        
        System.setProperty("javax.xml.parsers.DocumentBuilderFactory",
        "de.xplib.nexd.engine.xml.jaxp.DocumentBuilderFactoryImpl");
        
        DocumentBuilderFactory dbf = DocumentBuilderFactoryImpl.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        
        doc = db.parse("file:///home/devel/workspace/nexd/data/test/package.xml");
    }

    public final void testCloneNode() throws Exception {
        assertSame(DocumentImpl.class, doc.getClass());
        
        DocumentType n = (DocumentType) doc.getDoctype().cloneNode(true);
        assertEquals(n.getInternalSubset(), doc.getDoctype().getInternalSubset());
        assertEquals(n.getPublicId(), doc.getDoctype().getPublicId());
        assertEquals(n.getSystemId(), doc.getDoctype().getSystemId());
    }

    public final void testGetName() {
        assertEquals("package", doc.getDoctype().getName());
    }

    public final void testGetEntities() {
        NamedNodeMap nnm = doc.getDoctype().getEntities();
        assertNotNull(nnm);
        assertTrue(nnm instanceof NamedNodeMap);
        assertEquals(0, nnm.getLength());
    }

    public final void testGetNotations() {
        NamedNodeMap nnm = doc.getDoctype().getNotations();
        assertNotNull(nnm);
        assertTrue(nnm instanceof NamedNodeMap);
        assertEquals(0, nnm.getLength());
    }

    public final void testGetPublicId() {
        String pid = doc.getDoctype().getPublicId();
        assertEquals("FOO", pid);
    }

    public final void testGetSystemId() {
        String sid = doc.getDoctype().getSystemId();
        assertEquals("file:///home/manuel/workspace/nexd/data/test/dtd/package.dtd", sid);
    }

    public final void testGetInternalSubset() {
        String subset = doc.getDoctype().getInternalSubset();
        assertNotNull(subset);
    }

}
