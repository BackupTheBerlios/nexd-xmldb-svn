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
 * $Log: QueryXMLResourceDialog.java,v $
 * Revision 1.1  2005/04/12 08:34:22  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.ui.dialog;

import javax.swing.Action;

import org.xmldb.api.base.Collection;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;

import de.xplib.xdbm.ui.Application;
import de.xplib.xdbm.ui.action.ExecuteQueryXMLResourceAction;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class QueryXMLResourceDialog extends AbstractXPathQueryDialog {

    /**
     * @param resIn
     */
    public QueryXMLResourceDialog(XMLResource resIn) {
        super("dialog.collection.query", ExecuteQueryXMLResourceAction.INSTANCE, resIn);
    }

    /**
     * <Some description here>
     * 
     * @param target
     * @return
     * @see de.xplib.xdbm.ui.dialog.AbstractXPathQueryDialog#getPath(java.lang.Object)
     */
    protected String getPath(Object target) {
        String path = "";
        try {
            XMLResource res = (XMLResource) target;
            
            path = "/" + res.getId();
            
            Collection coll = res.getParentCollection();
            while (coll != null) {
                path = "/" + coll.getName() + path;
                coll = coll.getParentCollection();
            }
        } catch (XMLDBException e) {
            e.printStackTrace();
        }
        return path;
    }

    /**
     * <Some description here>
     * 
     * @param actionIn
     * @param target
     * @param xpath
     * @see de.xplib.xdbm.ui.dialog.AbstractXPathQueryDialog#setupAction(javax.swing.Action, java.lang.Object, java.lang.String)
     */
    protected void setupAction(Action actionIn, Object target, String xpath) {
        
Application.out.println(target);
        ((ExecuteQueryXMLResourceAction) actionIn).setContext(
                (XMLResource) target, xpath);
    }

}
