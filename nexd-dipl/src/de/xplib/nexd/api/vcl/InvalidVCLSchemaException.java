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
 * $Log: InvalidVCLSchemaException.java,v $
 * Revision 1.2  2005/03/31 12:08:40  nexd
 * Advanced VCL support
 *
 * Revision 1.1  2005/03/14 12:22:48  nexd
 * Heavy javadoc, checkstyle and eclipse todo-Task session.
 *
 */
package de.xplib.nexd.api.vcl;

/**
 * <p>This <code>Exception</code> is thrown if a Virtual Collection Language
 * Schema hasn't the correct structure, or required attributes aren't defined.
 * </p>
 * 
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.2 $
 */
public class InvalidVCLSchemaException extends Exception {
    
    /**
     * Error code that says the <code>&lt;schema&gt;</code> element doesn't
     * has a child <code>&lt;collection&gt;</code>.
     */
    public static final int NO_ROOT_REFERENCE = 0;
    
    /**
     * Error code that says the <code>&lt;schema&gt;</code> element has allready
     * a <code>&lt;collection&gt;</code> element.
     */
    public static final int DOUBLE_ROOT_REFERENCE = 1;
    
    /**
     * Error code that says a <code>&lt;collection&gt;</code> element doesn't
     * has an attribute <code>match</code>.
     */
    public static final int ATTR_MATCH_REQUIRED = 2;
    
    /**
     * Array holding error messages for all defined error codes. 
     */
    public static final String[] MESSAGES = {
        "The VCL-Schema doesn't contain a root <collection> element.",
        "The <schema> element has allready a <collection> element.",
        "There is no 'match' attribute defined for the <collection> element."
    };
    
    /**
     * The error code.
     */
    private final int code;

    /**
     * Constructor.
     */
    public InvalidVCLSchemaException() {
        this("");
    }
    
    /**
     * Constructor.
     * 
     * @param messageIn Custom error message.
     */
    public InvalidVCLSchemaException(final String messageIn) {
        super(messageIn);
        this.code = -1;
    }
    
    /**
     * Constructor.
     * 
     * @param codeIn The error code.
     */
    public InvalidVCLSchemaException(final int codeIn) {
        super(MESSAGES[codeIn]);
        
        this.code = codeIn;
    }

    /**
     * Getter method for the error code.
     * 
     * @return The error code.
     */
    public int getCode() {
        return this.code;
    }
}
