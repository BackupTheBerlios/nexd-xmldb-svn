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
 * $Log: ValidatorErrorHandler.java,v $
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

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Simple error handling class.
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class ValidatorErrorHandler implements ErrorHandler {

    /**
     * <Some description here>
     * 
     * @param arg0
     * @throws org.xml.sax.SAXException
     * @see org.xml.sax.ErrorHandler#warning(org.xml.sax.SAXParseException)
     */
    public void warning(final SAXParseException arg0) throws SAXException {
        throw arg0;
    }

    /**
     * <Some description here>
     * 
     * @param arg0
     * @throws org.xml.sax.SAXException
     * @see org.xml.sax.ErrorHandler#error(org.xml.sax.SAXParseException)
     */
    public void error(final SAXParseException arg0) throws SAXException {
        throw arg0;
    }

    /**
     * <Some description here>
     * 
     * @param arg0
     * @throws org.xml.sax.SAXException
     * @see org.xml.sax.ErrorHandler#fatalError(org.xml.sax.SAXParseException)
     */
    public void fatalError(final SAXParseException arg0) throws SAXException {
        throw arg0;
    }

}
