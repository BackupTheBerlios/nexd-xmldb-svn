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

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 */
public class EchoErrorHandler implements ErrorHandler {

    /**
     * <Some description here>
     * 
     * @param e
     * @see org.xml.sax.ErrorHandler#error(org.xml.sax.SAXParseException)
     */
    public final void error(final SAXParseException e) {
        message(e, "ERROR");
    }

    /**
     * <Some description here>
     * 
     * @param e ..
     * @throws SAXParseException ..
     * @see org.xml.sax.ErrorHandler#fatalError(org.xml.sax.SAXParseException)
     */
    public final void fatalError(final SAXParseException e) 
    throws SAXParseException {
        message(e, "ERROR");
        throw e;
    }

    /**
     * <Some description here>
     * 
     * @param e ..
     * @see org.xml.sax.ErrorHandler#warning(org.xml.sax.SAXParseException)
     */
    public final void warning(final SAXParseException e) {
        message(e, "WARNING");
    }

    /**
     * @param e ..
     * @param level ..
     */
    private void message(final SAXParseException e, final String level) {
        System.out.println(e.getSystemId() + " (" + e.getLineNumber() + "): "
                + e.getMessage() + " (" + level + ")");
    }

}