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
 * $Log: TextTestCase.java,v $
 * Revision 1.1  2005/05/11 17:31:39  nexd
 * Refactoring and extended test cases
 *
 */
package tests.xml;

import junit.framework.TestCase;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import de.xplib.nexd.engine.xml.dom.DOMImplementationImpl;
import de.xplib.nexd.engine.xml.dom.TextImpl;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class TextTestCase extends TestCase {
    
    private Document doc;
    
    public void setUp() throws Exception {
        super.setUp();
        
        doc = new DOMImplementationImpl().createDocument(null, null, null);
        Node n = doc.appendChild(doc.createElement("root"));
        n.appendChild(doc.createTextNode("Manuel "));
        n.appendChild(doc.createTextNode("idea "));
        n.appendChild(doc.createTextNode("of a new "));
        n.appendChild(doc.createTextNode("XML Database "));
        n.appendChild(doc.createTextNode("is great."));
    }

    public final void testCloneNode() {
        //TODO Implement cloneNode().
        
        
    }

    public final void testSplitText() {
        
        doc.getDocumentElement().normalize();
        
        Text txt = ((TextImpl) doc.getDocumentElement().getFirstChild()).splitText(6);
        assertEquals("Manuel", doc.getDocumentElement().getFirstChild().getNodeValue());
        
        assertEquals(" idea ", txt.getNodeValue().substring(0, 6));
        assertSame(doc.getDocumentElement().getFirstChild().getParentNode(), txt.getParentNode());
    }

    public final void testGetIsWhitespaceInElementContent() {
        assertTrue(((TextImpl) doc.getDocumentElement().getFirstChild()).getIsWhitespaceInElementContent());
    }

    public final void testGetWholeText() {
        String s = ((TextImpl) this.doc.getDocumentElement().getFirstChild()).getWholeText();
        
        assertEquals("Manuel idea of a new XML Database is great.", s);
        
        s = ((TextImpl) this.doc.getDocumentElement().getFirstChild().getNextSibling()).getWholeText();
        assertEquals("Manuel idea of a new XML Database is great.", s);
    }

    public final void testIsElementContentWhitespace() {
        assertFalse(((TextImpl) doc.getDocumentElement().getFirstChild()).isElementContentWhitespace());
    }

}
