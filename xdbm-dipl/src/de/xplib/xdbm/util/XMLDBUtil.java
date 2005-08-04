/*
 * Project: xmldb-manager 
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
 * $Log: XMLDBUtil.java,v $
 * Revision 1.1  2005/04/12 08:34:20  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.util;

import org.xmldb.api.base.Collection;
import org.xmldb.api.base.XMLDBException;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public final class XMLDBUtil {

    /**
     * 
     */
    private XMLDBUtil() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    
    public static String getPath(final Collection collIn) {
        
        String path = "";
        Collection tmp = collIn;
        
        try {
            while (tmp != null) {
                path = "/" + tmp.getName() + path;
                tmp  = tmp.getParentCollection(); 
            }
        } catch (XMLDBException e) {
            e.printStackTrace();
        }
        
        return path;
        
    }

}
