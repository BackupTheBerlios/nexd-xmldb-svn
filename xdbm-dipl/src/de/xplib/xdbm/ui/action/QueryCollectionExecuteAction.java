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
 * $Log: QueryCollectionExecuteAction.java,v $
 * Revision 1.1  2005/04/12 08:34:21  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.ui.action;

import java.awt.event.ActionEvent;

import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Service;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XPathQueryService;

import de.xplib.xdbm.ui.Application;
import de.xplib.xdbm.ui.dialog.StandardDialogFactory;
import de.xplib.xdbm.ui.model.QueryResultSetObject;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class QueryCollectionExecuteAction extends I18NErrorAction {
    
    /**
     * GoF Flyweight instance of QueryCollectionExecuteAction.
     */
    public static final QueryCollectionExecuteAction INSTANCE
        = new QueryCollectionExecuteAction();
    
    private Collection coll = null;
    
    private String xpath = null;

    /**
     * 
     */
    public QueryCollectionExecuteAction() {
        super("action.object.collection.query.exec");
        
        this.setEnabled(false);
    }
    
    /**
     * @param collIn
     * @param xpathIn
     */
    public void setContext(final Collection collIn, final String xpathIn) {
        this.error = false;
        this.coll  = collIn;
        this.xpath = xpathIn;
    }

    /**
     * <Some description here>
     * 
     * @param aeIn
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent aeIn) {
        
        if (this.coll == null) {
            StandardDialogFactory.showNoCollectionDialog();
            this.error = true;
            return;
        }
        
        if (this.xpath == null) {
            StandardDialogFactory.showNoXPathQueryDialog();
            this.error = true;
            return;
        }
        
        try {
            Service s = this.coll.getService("XPathQueryService", "1.0");
            if (s == null || !(s instanceof XPathQueryService)) {
                StandardDialogFactory.showNoSuchServiceDialog("XPathQueryService");
                this.error = true;
                return;
            }
            
            XPathQueryService xqs = (XPathQueryService) s;
            
            QueryResultSetObject qrso = new QueryResultSetObject(
                    this.coll, xqs.query(this.xpath));
            
            Application.getInstance().setUIObject(qrso);
        } catch (XMLDBException e) {
            StandardDialogFactory.showErrorMessageDialog(e);
            this.error = true;
            return;
        }
        
        // reset
        this.setContext(null, null);
    }

}
