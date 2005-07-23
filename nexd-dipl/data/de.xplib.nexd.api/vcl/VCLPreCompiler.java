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
 * $Log: VCLPreCompiler.java,v $
 * Revision 1.6  2005/05/30 19:17:08  nexd
 * UML documentation update....
 *
 * Revision 1.5  2005/05/11 17:31:40  nexd
 * Refactoring and extended test cases
 *
 * Revision 1.4  2005/05/08 11:59:33  nexd
 * restructuring
 *
 * Revision 1.3  2005/03/31 12:08:40  nexd
 * Advanced VCL support
 *
 * Revision 1.2  2005/03/14 12:22:48  nexd
 * Heavy javadoc, checkstyle and eclipse todo-Task session.
 *
 */
package de.xplib.nexd.api.vcl;

import java.io.IOException;
import java.net.URL;

import org.sixdml.exceptions.InvalidQueryException;
import org.xml.sax.SAXException;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;


/**
 * <p>This is the default pre compiler for a Virtual Collection Language Schema.
 * It creates pre compiled resources from the given {@link VCLSchema}, the so 
 * called {@link _de.xplib.nexd.api.pcvr.PCVResource} instances. These pre
 * compiled resources contain both the structure of the 
 * {@link de.xplib.nexd.api.vcl.VCLSchema} and the content of the resulting
 * {@link _de.xplib.nexd.api.VirtualResource}.</p>
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.6 $
 */
public class VCLPreCompiler {
    
    /**
     * <p>The parser that is used to create a {@link VCLSchema} from the
     * given xml Document.</p>
     * @clientRole parser
     * @directed true
     * @supplierCardinality 1
     */
    private final VCLParserI parser;
    
    /**
     * <p>The compiling visitor (331) that builds the
     * {@link _de.xplib.nexd.api.pcvr.PCVResource} instances from the
     * {@link VCLSchema}.</p>
     * @clientRole visitor
     * @directed true
     * @supplierCardinality 1
     */
    private final VCLSchemaCompileVisitor visitor;
    
    /**
     * <p>Constructor.</p>
     * 
     * @param visitorIn The visitor that creates the 
     *                  {@link _de.xplib.nexd.api.pcvr.PCVResource} instances.
     */
    public VCLPreCompiler(final VCLSchemaCompileVisitor visitorIn) {
        this(null, visitorIn);
    }

    /**
     * <p>Constructor.</p>
     * 
     * @param parserIn The parser that produces the {@link VCLSchema}.
     * @param visitorIn The visitor that creates the 
     *                  {@link _de.xplib.nexd.api.pcvr.PCVResource} instances.
     */
    public VCLPreCompiler(final VCLParserI parserIn,
                          final VCLSchemaCompileVisitor visitorIn) {
        super();
        
        this.parser = parserIn;
        this.visitor = visitorIn;
    }
    
    /**
     * <p>This method compiles the {@link VCLSchema} that is located with the 
     * given <code>URL</code>. For this task this method uses the 
     * {@link AbstractVCLParser} and the {@link VCLSchemaCompileVisitor}.</p>
     * <p>An <code>IllegalArgumentException</code> is thrown if the URI is 
     * <code>null</code>.</p>
     *  
     * @param schemaIn The <code>URL</code> where the {@link VCLSchema} can 
     *                 be found.
     * @return The new {@link _de.xplib.nexd.api.pcvr.PCVResource} objects.
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
     * @throws IOException <p>If any IO errors occur.</p>
     * @throws SAXException <p>If any parse errors occur.</p>
     * @throws UndeclaredVariableException <p>This <code>Exception</code> is 
     *                                     thrown, if a variable is accessed 
     *                                     that was not declared before.</p>
     * @throws VariableExistsException <p>This <code>Exception</code> is thrown,
     *                                 if a variable with the same name allready
     *                                 exists in the current context.</p> 
     * @throws XMLDBException <p>If any database specific error occures.</p>
     */
    public ResourceSet compile(final URL schemaIn) 
            throws InvalidCollectionReferenceException, 
                   InvalidQueryException,
                   InvalidVCLSchemaException,
                   IOException,
                   SAXException,
                   UndeclaredVariableException,
                   VariableExistsException,
                   XMLDBException {
        
        if (schemaIn == null) {
            throw new IllegalArgumentException("The URL cannot be null.");
        }
        
        VCLSchema schema = this.parser.parse(schemaIn.openStream());
        
        return this.compile(schema);
    }
    
    /**
     * <p>This method compiles the {@link VCLSchema}. For this task this method 
     * uses the {@link VCLSchemaCompileVisitor}.</p>
     * <p>An <code>IllegalArgumentException</code> is thrown if the 
     * {@link VCLSchema} is <code>null</code>.</p>
     *  
     * @param schemaIn The <code>VCLSchema</code> instance.
     * @return The new {@link _de.xplib.nexd.api.pcvr.PCVResource} objects.
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
    public ResourceSet compile(final VCLSchema schemaIn)
	        throws InvalidCollectionReferenceException, 
			       InvalidQueryException,
			       InvalidVCLSchemaException,
			       UndeclaredVariableException,
			       VariableExistsException,
			       XMLDBException {
        
        if (schemaIn == null) {
            throw new IllegalArgumentException("The VCLSchema cannot be null.");
        }
        
        this.visitor.visit(schemaIn);
        
        return this.visitor.getResources();
    }
}
