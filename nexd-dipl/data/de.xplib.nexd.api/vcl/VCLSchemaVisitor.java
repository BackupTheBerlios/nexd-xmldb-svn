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
 * $Log: VCLSchemaVisitor.java,v $
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
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.5 $
 */
public interface VCLSchemaVisitor {

    /**
     * <p>This method visits the document element of a Virtual Collection 
     * Language Schema, the {@link VCLSchema}.</p>
     * 
     * @param schemaIn <p>The {@link VCLSchema} node to visit.</p>
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
    void visit(VCLSchema schemaIn) throws InvalidCollectionReferenceException, 
                                          InvalidQueryException, 
                                          InvalidVCLSchemaException, 
                                          UndeclaredVariableException, 
                                          VariableExistsException, 
                                          XMLDBException;
    
    /**
     * <p>This method visits a collection <code>Element</code> in a Virtual 
     * Collection Language Schema, the {@link VCLCollectionReference}.</p>
     * 
     * @param collRefIn <p>The {@link VCLCollectionReference} node to visit.</p>
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
    void visit(VCLCollectionReference collRefIn) 
            throws InvalidCollectionReferenceException, 
                   InvalidQueryException, 
                   InvalidVCLSchemaException, 
                   UndeclaredVariableException, 
                   VariableExistsException, 
                   XMLDBException;
    
    /**
     * <p>This method visits an attribute in a Virtual 
     * Collection Language Schema, the {@link VCLAttr}.</p>
     * 
     * @param attrIn <p>The {@link VCLAttr} node to visit.</p>
     * @throws InvalidQueryException <p>This <code>Exception</code> is thrown,if
     *                               a query is not supported or it uses an 
     *                               invalid syntax.</p> 
     * @throws UndeclaredVariableException <p>This <code>Exception</code> is 
     *                                     thrown, if a variable is accessed 
     *                                     that was not declared before.</p>
     * @throws XMLDBException <p>If any database specific error occures.</p>
     */
    void visit(VCLAttr attrIn) throws InvalidQueryException, 
                                      UndeclaredVariableException, 
                                      XMLDBException;
    
    /**
     * <p>This method visits a value-of <code>Element</code> in a Virtual 
     * Collection Language Schema, the {@link VCLValueOf}.</p>
     * 
     * @param valueOfIn <p>The {@link VCLValueOf} node to visit.</p>
     * @throws InvalidQueryException <p>This <code>Exception</code> is thrown,if
     *                               a query is not supported or it uses an 
     *                               invalid syntax.</p> 
     * @throws UndeclaredVariableException <p>This <code>Exception</code> is 
     *                                     thrown, if a variable is accessed 
     *                                     that was not declared before.</p>
     * @throws XMLDBException <p>If any database specific error occures.</p>
     */
    void visit(VCLValueOf valueOfIn) throws InvalidQueryException, 
                                            UndeclaredVariableException, 
                                            XMLDBException;
    
    /**
     * <p>This method visits a variable <code>Element</code> in a Virtual 
     * Collection Language Schema, the {@link VCLVariable}.</p>
     * 
     * @param variableIn <p>The {@link VCLVariable} node to visit.</p>
     * @throws InvalidQueryException <p>This <code>Exception</code> is thrown,if
     *                               a query is not supported or it uses an 
     *                               invalid syntax.</p> 
     * @throws VariableExistsException <p>This <code>Exception</code> is thrown,
     *                                 if a variable with the same name allready
     *                                 exists in the current context.</p> 
     * @throws XMLDBException <p>If any database specific error occures.</p>
     */
    void visit(VCLVariable variableIn) throws InvalidQueryException, 
                                              VariableExistsException, 
                                              XMLDBException;
}
