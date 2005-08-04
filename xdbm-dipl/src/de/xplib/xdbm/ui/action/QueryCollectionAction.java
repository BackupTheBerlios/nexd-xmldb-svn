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
 * $Log: QueryCollectionAction.java,v $
 * Revision 1.1  2005/04/12 08:34:21  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.ui.action;

import java.awt.event.ActionEvent;

import org.xmldb.api.base.Collection;

import de.xplib.xdbm.ui.Application;
import de.xplib.xdbm.ui.dialog.QueryCollectionDialog;
import de.xplib.xdbm.ui.dialog.StandardDialogFactory;
import de.xplib.xdbm.ui.model.CollectionObject;
import de.xplib.xdbm.ui.model.UIObject;
import de.xplib.xdbm.util.UIObjectObserver;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class QueryCollectionAction 
    extends I18NAction
    implements UIObjectObserver {
    
    private Collection coll = null;
    
    /**
     * GoF Flyweight instance of QueryCollectionAction
     */
    public static final QueryCollectionAction INSTANCE 
        = new QueryCollectionAction();

    /**
     * 
     */
    public QueryCollectionAction() {
        super("action.object.collection.query");
        // TODO Auto-generated constructor stub
        
        Application.getInstance().addObserver(this);
        
        this.setEnabled(false);
        this.putValue(SMALL_ICON, Application.createIcon(
                "icon/action.query.generic.png"));
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
            return;
        }

        QueryCollectionDialog dialog = new QueryCollectionDialog(this.coll);
        dialog.setVisible(true);
    }
    
    

    /**
     * <Some description here>
     * 
     * @param objectIn
     * @see de.xplib.xdbm.util.UIObjectObserver#update(de.xplib.xdbm.ui.model.UIObject)
     */
    public void update(final UIObject objectIn) {
        if (objectIn instanceof CollectionObject) {
            this.coll = ((CollectionObject) objectIn).getCollection();
            this.setEnabled(true);
        } else {
            this.coll = null;
            this.setEnabled(false);
        }

    }
}
