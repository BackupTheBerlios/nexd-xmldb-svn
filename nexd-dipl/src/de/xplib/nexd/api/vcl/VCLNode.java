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
 * $Log: VCLNode.java,v $
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

import org.sixdml.exceptions.InvalidQueryException;
import org.xmldb.api.base.XMLDBException;


/**
 * <p>This interface is the base for all types of a 
 * {@link de.xplib.nexd.api.vcl.VCLSchema} tree.</p>
 * <p>This interface implements a part of the GoF Composite pattern (163) and 
 * the Visitor pattern (331). The visitor is used for multiple tasks, it can 
 * perform syntax checks, transaction locks, compiling the schema and may more.
 * </p> 
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.5 $
 */
public interface VCLNode {

    /**
     * <p>Accept method for each <code>VCLNode</code>. This method is called 
     * from the {@link VCLSchemaVisitor} and then the visitors 
     * <code>visit()</code> method is called from the concrete 
     * <code>VCLNode</code> implementation.</p> 
     *  
     * @param visitorIn <p>The {@link VCLSchemaVisitor} that wants to visit the
     *                  concrete <code>VCLNode</code> implementation.</p>
     * @throws InvalidCollectionReferenceException <p>This is thrown, if a 
     *                       referenced {@link org.xmldb.api.base.Collection} 
     *                       doesn't exist or it is an instance of 
     *                       {@link _de.xplib.nexd.api.VirtualCollection}.</p>
     * @throws InvalidQueryException <p>This <code>Exception</code> is thrown,if
     *                               a query is not supported or it uses an 
     *                               invalid syntax.</p> 
     * @throws InvalidVCLSchemaException <p>This is thrown, if the xml Document
     *                                   doesn't match the required structure or
     *                                   it doesn't define all necessary 
     *                                   attributes.</p>
     * @throws UndeclaredVariableException <p>This <code>Exception</code> is 
     *                                     thrown, if a variable is accessed 
     *                                     that was not declared before.</p>
     * @throws VariableExistsException <p>This <code>Exception</code> is thrown,
     *                                 if a variable with the same name allready
     *                                 exists in the current context.</p> 
     * @throws XMLDBException <p>If any database specific error occures.</p>
     */
    void accept(VCLSchemaVisitor visitorIn) 
            throws InvalidCollectionReferenceException, 
                   InvalidQueryException, 
                   InvalidVCLSchemaException, 
                   UndeclaredVariableException, 
                   VariableExistsException, 
                   XMLDBException;
    
}
