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
 * $Log: XSValidator.java,v $
 * Revision 1.2  2005/05/11 17:31:40  nexd
 * Refactoring and extended test cases
 *
 * Revision 1.1  2005/05/08 11:59:33  nexd
 * restructuring
 *
 * Revision 1.3  2005/03/31 12:08:40  nexd
 * Advanced VCL support
 *
 * Revision 1.2  2005/03/14 12:22:49  nexd
 * Heavy javadoc, checkstyle and eclipse todo-Task session.
 *
 */
package de.xplib.nexd.engine.xml.util;

import org.w3c.dom.Document;

/**
 * Class that validates a {@link org.w3c.dom.Document} against a XML-Schema.
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.2 $
 */
public class XSValidator implements ValidatorI {

    /**
     * Validates the <code>{@link Document} against a XML-Schema or a DTD.
     * 
     * @param docIn The <code>Document</code> to be valid.
     * @throws NonValidDocumentException If the <code>Document</code> is not 
     *                                   valid.
     * @see de.xplib.nexd.engine.xml.util.ValidatorI#validate(
     *      org.w3c.dom.Document)
     */
    public void validate(final Document docIn) 
            throws NonValidDocumentException {
        // TODO Auto-generated method stub
    }

}
