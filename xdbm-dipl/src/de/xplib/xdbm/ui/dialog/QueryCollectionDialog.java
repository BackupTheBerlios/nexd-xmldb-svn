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
 * $Log: QueryCollectionDialog.java,v $
 * Revision 1.1  2005/04/12 08:34:22  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.ui.dialog;

import javax.swing.Action;

import org.xmldb.api.base.Collection;
import org.xmldb.api.base.XMLDBException;

import de.xplib.xdbm.ui.action.QueryCollectionExecuteAction;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class QueryCollectionDialog extends AbstractXPathQueryDialog {

    /**
     * 
     */
    public QueryCollectionDialog(final Collection collIn) {
        super("dialog.collection.query", QueryCollectionExecuteAction.INSTANCE, collIn);
    }

    protected String getPath(final Object targetIn) {
        String path = "";
        
        try {
            Collection c = (Collection) targetIn;
        	while (c != null) {
                path = "/" + c.getName() + path;
                c    = c.getParentCollection();
        	}
        } catch (XMLDBException e) {
            e.printStackTrace();
        }
        return path;
    }
    
    protected void setupAction(Action actionIn, final Object targetIn, String xpathIn) {
        ((QueryCollectionExecuteAction) actionIn).setContext((Collection) targetIn, xpathIn);
    }

}
