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
 * $Log: ExecuteQueryXMLResourceAction.java,v $
 * Revision 1.1  2005/04/12 08:34:21  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.ui.action;

import java.awt.event.ActionEvent;

import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Service;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;
import org.xmldb.api.modules.XPathQueryService;

import de.xplib.xdbm.ui.Application;
import de.xplib.xdbm.ui.dialog.StandardDialogFactory;
import de.xplib.xdbm.ui.model.QueryResultSetObject;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class ExecuteQueryXMLResourceAction extends I18NErrorAction {
    
    /**
     * GoF Flyweight instance of ExecuteQueryXMLResource.
     */
    public static final ExecuteQueryXMLResourceAction INSTANCE
        = new ExecuteQueryXMLResourceAction();
    
    private XMLResource xres = null;
    
    private String xpath = null;

    /**
     */
    public ExecuteQueryXMLResourceAction() {
        super("action.object.resource.query.exec");
        
        this.setEnabled(false);
    }
    
    public void setContext(XMLResource xresIn, String xpathIn) {
        this.error = false;
        this.xres = xresIn;
        this.xpath = xpathIn;
    }

    /**
     * <Some description here>
     * 
     * @param e
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {

        if (this.xres == null) {
            StandardDialogFactory.showNoResourceDialog();
            this.error = true;
            return;
        }
        
        if (this.xpath == null) {
            StandardDialogFactory.showNoXPathQueryDialog();
            this.error = true;
            return;
        }
        
        try {
            Collection c = this.xres.getParentCollection();
            
            Service s = c.getService("XPathQueryService", "1.0");
            if (s == null || !(s instanceof XPathQueryService)) {
                StandardDialogFactory.showNoSuchServiceDialog("XPathQueryService");
                this.error = true;
                return;
            }
            
            XPathQueryService xqs = (XPathQueryService) s;
            
            QueryResultSetObject qrso = new QueryResultSetObject(
                    c, xqs.queryResource(this.xres.getId(), this.xpath));
            
            Application.getInstance().setUIObject(qrso);
        } catch (XMLDBException e1) {
            StandardDialogFactory.showErrorMessageDialog(e1);
            this.error = true;
            return;
        }
        
        // reset
        this.setContext(null, null);
    }

}
