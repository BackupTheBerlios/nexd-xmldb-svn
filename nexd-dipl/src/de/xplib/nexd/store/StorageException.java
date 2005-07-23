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
package de.xplib.nexd.store;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.4 $
 */
public class StorageException 
    extends Exception 
    implements StorageExceptionCodes {
    
    /**
     * Error messages for all codes.
     */
    private static final String[] MESSAGES = {
        "An unknown error occured.",                     // UNKNOWN
        "The requested Collection [?] doesn't exist.",   // NO_SUCH_COLLECTION
        "The Collection [?] already exists.",            // COLLECTION_EXISTS
        "The stored xml document is invalid.",           // INVALID_STORED_DOCUM
        "The nodeType [?] is unknown.",                  // UNKNOWN_NODE_TYPE
        "This storage doesn't support the nodeType[?].", // UNSUPPORTED_NODE_TYP
        "Cannot update a not existing node[?],",         // NOT_EXISTING_NODE
        "The submited schema cannot be handled.",        // INVALID_SCHEMA_TYPE
        "The used XPath expression is not correct or NEXD doesn't support " 
        + "the used syntax. ?"                           // BAD_XPATH_EXPRESSION
    };
    
    /**
     * The exception code.
     */
    private short code = UNKNOWN;
    
    /**
     * Constructor.
     * 
     * @param codeIn An exception code.
     */
    public StorageException(final short codeIn) {
        this(codeIn, new String[0]);
    }
    
    /**
     * @param codeIn An exception code.
     * @param tokens An array with tokens that will be replaced in the 
     *               exception message.
     */
    public StorageException(final short codeIn, final String[] tokens) {
        this(prepareMessage(MESSAGES[codeIn], tokens));
        
        this.code = codeIn;
    }

    /**
     * Constructor.
     * 
     * @param message Human readable message.
     */
    public StorageException(final String message) {
        super(message);
    }

    /**
     * Constructor.
     * 
     * @param message Human readable message.
     * @param cause A previous occured <code>Exception</code>.
     */
    public StorageException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Getter method for the exception code.
     * 
     * @return The exception code.
     */
    public short getCode() {
        return this.code;
    }
    
    /**
     * @param message The message string to prepare.
     * @param tokens The tokens that will replace parts of the message.
     * @return The prepared message string.
     */
    private static String prepareMessage(final String message, 
                                         final String[] tokens) {
        
        String prepared = message;
        for (int i = 0; i < tokens.length; i++) {
            prepared = prepared.replaceFirst("\\\\?", tokens[i]);
        }
        return prepared;
    }

}
