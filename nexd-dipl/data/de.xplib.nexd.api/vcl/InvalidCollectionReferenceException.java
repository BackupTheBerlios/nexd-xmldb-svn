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
 * $Log: InvalidCollectionReferenceException.java,v $
 * Revision 1.5  2005/05/11 17:31:40  nexd
 * Refactoring and extended test cases
 *
 * Revision 1.4  2005/03/31 12:08:40  nexd
 * Advanced VCL support
 *
 * Revision 1.3  2005/03/14 12:22:48  nexd
 * Heavy javadoc, checkstyle and eclipse todo-Task session.
 *
 */
package de.xplib.nexd.api.vcl;

/**
 * <p>This <code>Exception</code> is thrown if the Virtual Collection Language
 * Schema references a not existing {@link org.xmldb.api.base.Collection} or it
 * is an instance of {@link _de.xplib.nexd.api.VirtualCollection}.</p>
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.5 $
 */
public class InvalidCollectionReferenceException extends Exception {
    
    /**
     * <p>Constant code that is used if a referenced 
     * {@link org.xmldb.api.base.Collection} doesn't exist.</p>
     */
    public static final short COLLECTION_NOT_EXIST = 0;
    
    /**
     * <p>Constant error code that is used if a referenced 
     * {@link org.xmldb.api.base.Collection} is an instance of 
     * {@link _de.xplib.nexd.api.VirtualCollection}.</p>
     */
    public static final short COLLECTION_IS_VIRTUAL = 1;
    
    /**
     * <p>Array that holds the error messages for the possible error
     * codes.</p>
     */
    public static final String[] MESSAGES = {
        "The referenced Collection doesn't exist.",
        "The referenced Collection is a VirtualCollection"
    };

    /**
     * <p>The error code, why this <code>Exception</code> was thrown.</p>
     */
    private final short code;
    
    /**
     * <p>Constructor.</p>
     * 
     * @param codeIn <p>The error code, why this <code>Exception</code> was
     *               thrown.</p>
     */
    public InvalidCollectionReferenceException(final short codeIn) {
        super(MESSAGES[codeIn]);
        
        this.code = codeIn;
    }
    
    /**
     * <p>Returns the error code, why this <code>Exception</code> was thrown
     * or <code>-1</code>.</p>
     * 
     * @return <p>The error code, why this <code>Exception</code> was thrown
     *         or <code>-1</code>.</p>
     */
    public short getCode() {
        return this.code;
    }
}
