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
 * $Log: VCLSchemaCompileVisitor.java,v $
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

import org.xmldb.api.base.ResourceSet;


/**
 * <p>This represents a visitor (GOF Visitor Pattern [331]) that creates 
 * pre compiled resources from a given {@link de.xplib.nexd.api.vcl.VCLSchema}.
 * </p> 
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.5 $
 */
public interface VCLSchemaCompileVisitor extends VCLSchemaVisitor {

    /**
     * <p>Returns the creates pre compiled resources.</p>
     * 
     * @return The new created {@link _de.xplib.nexd.api.pcvr.PCVResource} 
     *         objects.
     */
    ResourceSet getResources();
}
