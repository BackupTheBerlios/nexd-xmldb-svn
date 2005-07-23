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
 * $Log: InvalidParentCollectionException.java,v $
 * Revision 1.2  2005/03/31 12:08:39  nexd
 * Advanced VCL support
 *
 * Revision 1.1  2005/03/14 12:22:48  nexd
 * Heavy javadoc, checkstyle and eclipse todo-Task session.
 *
 */
package de.xplib.nexd.api;

/**
 * This <code>Exception</code> is thrown by the NEXD-VC-API if a user/developer
 * tries to create an instance of <code>VirtualCollection</code> in an other
 * <code>VirtualCollection</code> instead of a <code>Collection</code>.
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.2 $
 */
public class InvalidParentCollectionException extends Exception {

    /**
     * <p>Constructor.</p>
     */
    public InvalidParentCollectionException() {
        super();
    }

    /**
     * <p>Constructor.</p>
     * 
     * @param messageIn <p>The exception message.</p>
     */
    public InvalidParentCollectionException(final String messageIn) {
        super(messageIn);
    }

}
