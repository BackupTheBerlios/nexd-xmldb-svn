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
 * $Log: DOMImplementationTestCase.java,v $
 * Revision 1.1  2005/05/11 17:31:39  nexd
 * Refactoring and extended test cases
 *
 */
package tests.xml;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;

import de.xplib.nexd.engine.xml.dom.DOMImplementationImpl;
import de.xplib.nexd.engine.xml.jaxp.DocumentBuilderFactoryImpl;

import junit.framework.TestCase;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class DOMImplementationTestCase extends TestCase {
    
    Document doc;
    
    DOMImplementation impl;
    
    public void setUp() throws Exception {
        super.setUp();
        
        System.setProperty("javax.xml.parsers.DocumentBuilderFactory",
        "de.xplib.nexd.engine.xml.jaxp.DocumentBuilderFactoryImpl");
        
        DocumentBuilderFactory dbf = DocumentBuilderFactoryImpl.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        
        impl = db.getDOMImplementation();
        
        doc = db.parse("file:///home/devel/workspace/nexd/data/test/package.xml");
    }

    public final void testHasFeature() {
        assertSame(DOMImplementationImpl.class, impl.getClass());
        
        assertFalse(impl.hasFeature("XML", "1.0"));
    }

    public final void testCreateDocumentType() {
        DocumentType dtd = impl.createDocumentType("package", "FOO", "file:///home/devel/workspace/nexd/data/test/dtd/package.dtd");
        
        assertNotNull(dtd);
        assertEquals("FOO", dtd.getPublicId());
        assertEquals("file:///home/devel/workspace/nexd/data/test/dtd/package.dtd", dtd.getSystemId());
    }

    public final void testCreateDocument() {
        DocumentType dtd = impl.createDocumentType("package", "FOO", "file:///home/devel/workspace/nexd/data/test/dtd/package.dtd");
        
        Document d = impl.createDocument(null, "package", dtd);
        assertSame(d.getDoctype(), dtd);
    }
    
    public final void testCreateDocumentDTDInUseFail() {
        
        try {
            impl.createDocument(null, "package", doc.getDoctype());
            assertTrue(false);
        } catch (DOMException e) {
            assertEquals(DOMException.WRONG_DOCUMENT_ERR, e.code);
        }
    }
    
    public final void testCreateDocumentDTDWrongImplFail() {
        
        try {
            
            DocumentType dtd = new org.apache.xerces.dom.DOMImplementationImpl().createDocumentType("package", "FOO", "file:///home/devel/workspace/nexd/data/test/dtd/package.dtd");
            
            impl.createDocument(null, "package", dtd);
            assertTrue(false);
        } catch (DOMException e) {
            assertEquals(DOMException.WRONG_DOCUMENT_ERR, e.code);
        }
    }

    public final void testGetInterface() {
        assertNull(((DOMImplementationImpl) impl).getInterface("XML"));
    }

}
