/* <blockquote><small> Copyright (C) 2002 Hewlett-Packard Company.
 * This file is part of Sparta, an XML Parser, DOM, and XPath library.
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the <a href="doc-files/LGPL.txt">GNU
 * Lesser General Public License</a> as published by the Free Software
 * Foundation; either version 2.1 of the License, or (at your option)
 * any later version.  This library is distributed in the hope that it
 * will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. </small></blockquote>
 * 
 * @author Eamonn O'Brien-Strain
 */
package de.xplib.nexd.xml.spartadom;


/**
 * Thrown when declared encoding does not match assumed encoding.
 * 
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 * @link http://sparta.sourceforge.net
 *  
 */
public class EncodingMismatchException extends ParseException {

    /**
     * Comment for <code>declaredEncoding</code>
     */
    private String declaredEncoding;

    /**
     * @param systemId ..
     * @param declaredEncodingIn ..
     * @param assumedEncoding ..
     */
    EncodingMismatchException(final String systemId, 
            				  final String declaredEncodingIn,
            				  final String assumedEncoding) {
        super(systemId, 0, declaredEncodingIn
                .charAt(declaredEncodingIn.length() - 1), declaredEncodingIn,
                "encoding \'" + declaredEncodingIn + "\' declared instead " 
                + "of of " + assumedEncoding + " as expected");
        declaredEncoding = declaredEncodingIn;
    }

    /**
     * @return ..
     */
    final String getDeclaredEncoding() {
        return declaredEncoding;
    }

}

// $Log: EncodingMismatchException.java,v $
// Revision 1.1.1.1 2002/08/19 05:04:01 eobrain
// import from HP Labs internal CVS
//
// Revision 1.4 2002/08/18 04:35:44 eob
// Add copyright and other formatting and commenting in preparation for
// release to SourceForge.
//
// Revision 1.3 2002/08/05 20:04:32 sermarti
//
// Revision 1.2 2002/05/09 16:49:09 eob
// Add history arg.
//
// Revision 1.1 2002/01/08 19:25:38 eob
// initial
