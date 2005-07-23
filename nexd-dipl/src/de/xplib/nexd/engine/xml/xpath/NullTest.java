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
package de.xplib.nexd.engine.xml.xpath;


/**
 * Empty <code>AbstractNodeTest</code> implementation. 
 * This class is used allways when only a <code>AbstractBooleanExpr</code> is 
 * needed and no <code>AbstractNodeTest</code>. This simple helper prevents 
 * developers from writing many unnessary:
 * 
 * <pre>
 *   if (step.getNodeTest() != null) {
 *       ...
 *   } 
 * </pre> 
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.2 $
 */
public class NullTest extends AbstractNodeTest {
    
    /**
     * We only need one instance of this class, so we can implement it with the 
     * GoF Flyweight pattern (195).
     */
    public static final NullTest INSTANCE = new NullTest();

    /**
     * Empty accept method for the GoF Visitor pattern (331). 
     * 
     * @param visitorIn The visitor instance.
     * @throws XPathExceptionExt Never.
     * @see de.xplib.nexd.engine.xml.xpath.AbstractNodeTest#accept(
     *      de.xplib.nexd.engine.xml.xpath.Visitor)
     */
    public void accept(final Visitor visitorIn) throws XPathExceptionExt {
    }

    /**
     * Does this nodetest evaluate to a string values (attribute values or
     * text() nodes)
     * 
     * @return Allways <code>false</code>.
     * @see de.xplib.nexd.engine.xml.xpath.AbstractNodeTest#isStringValue()
     */
    public boolean isStringValue() {
        return false;
    }

}
