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
package tests.xml;

import junit.framework.TestCase;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import de.xplib.nexd.xml.dom.DOMImplementationImpl;
import de.xplib.nexd.xml.dom.ElementImpl;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 */
public class DOMBaseTestCase extends TestCase {
    
    /**
     * Comment for <code>doc1</code>
     */
    protected Document doc1 = null;
    
    /**
     * Comment for <code>domImpl</code>
     */
    protected DOMImplementation domImpl = null;

    /**
     * <Some description here>
     * 
     * @throws java.lang.Exception
     * @see junit.framework.TestCase#setUp()
     */
    protected final void setUp() throws Exception {
        super.setUp();
        
        domImpl = new DOMImplementationImpl();
        
    }
    
    
    
    /**
     * 
     */
    public void testCreateDocument() {
        
        Document doc = this.domImpl.createDocument(null, null, null);
        
        assertFalse(doc.hasChildNodes());
        //assertEquals(doc.)
    }
    
    /**
     * 
     */
    public void testCreateDocumentRoot() {
        
        Document doc = this.domImpl.createDocument(null, "root", null);
        
        assertEquals(doc.getDocumentElement().getClass(), ElementImpl.class);
    }
}
