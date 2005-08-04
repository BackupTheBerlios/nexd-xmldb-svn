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
 * $Log: QueryXMLResourceAction.java,v $
 * Revision 1.1  2005/04/12 08:34:21  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.ui.action;

import java.awt.event.ActionEvent;

import org.xmldb.api.modules.XMLResource;

import de.xplib.xdbm.ui.Application;
import de.xplib.xdbm.ui.dialog.QueryXMLResourceDialog;
import de.xplib.xdbm.ui.dialog.StandardDialogFactory;
import de.xplib.xdbm.ui.model.QueryResultObject;
import de.xplib.xdbm.ui.model.UIObject;
import de.xplib.xdbm.ui.model.XMLResourceObject;
import de.xplib.xdbm.util.UIObjectObserver;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class QueryXMLResourceAction extends I18NAction implements UIObjectObserver {
    
    private XMLResource res = null;
    
    /**
     * GoF Flyweight instance of QueryXMLResourceAction
     */
    public static final QueryXMLResourceAction INSTANCE 
        = new QueryXMLResourceAction();

    /**
     */
    public QueryXMLResourceAction() {
        super("action.object.resource.query");

        this.setEnabled(false);
        this.putValue(SMALL_ICON, Application.createIcon(
                "icon/action.query.generic.png"));
        
        Application.getInstance().addObserver(this);
    }

    /**
     * <Some description here>
     * 
     * @param e
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        
        if (this.res == null) {
            StandardDialogFactory.showNoResourceDialog();
            return;
        }
        
        QueryXMLResourceDialog dialog = new QueryXMLResourceDialog(this.res);
        dialog.setVisible(true);
    }

    /**
     * <Some description here>
     * 
     * @param objectIn
     * @see de.xplib.xdbm.util.UIObjectObserver#update(de.xplib.xdbm.ui.model.UIObject)
     */
    public void update(UIObject objectIn) {

        if (objectIn instanceof XMLResourceObject 
                && !(objectIn instanceof QueryResultObject)) {
            
            this.res = (XMLResource) objectIn.getUserObject();
            this.setEnabled(true);
        } else {
            this.res = null;
            this.setEnabled(false);
        }
    }
}
