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
 * $Log: NonDefaultConstructorFactory.java,v $
 * Revision 1.1  2005/05/11 17:31:40  nexd
 * Refactoring and extended test cases
 *
 */
package tests.pcvr;

import org.w3c.dom.Document;
import org.xmldb.api.base.XMLDBException;

import de.xplib.nexd.api.VirtualCollection;
import de.xplib.nexd.api.pcvr.AbstractPCVRFactory;
import de.xplib.nexd.api.pcvr.PCVRCompiler;
import de.xplib.nexd.api.pcvr.PCVResource;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class NonDefaultConstructorFactory extends AbstractPCVRFactory {

    /**
     * 
     */
    public NonDefaultConstructorFactory(final String dummy) {
        super();
        // TODO Auto-generated constructor stub
    }

    public PCVRCompiler newPCVRCompiler(VirtualCollection collIn)
            throws XMLDBException {
        // TODO Auto-generated method stub
        return null;
    }
    public PCVResource newPCVResource(VirtualCollection collIn, String nameIn,
            Document pcvDocIn) throws XMLDBException {
        // TODO Auto-generated method stub
        return null;
    }
}
