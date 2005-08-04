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
 * $Log: CopyResourceAction.java,v $
 * Revision 1.1  2005/04/12 08:34:21  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.ui.action;

import java.awt.event.ActionEvent;

import de.xplib.xdbm.ui.Application;
import de.xplib.xdbm.ui.model.QueryResultObject;
import de.xplib.xdbm.ui.model.ResourceObject;
import de.xplib.xdbm.ui.model.UIObject;
import de.xplib.xdbm.util.UIObjectObserver;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class CopyResourceAction extends I18NAction implements UIObjectObserver {
    
    private ResourceObject resObj = null;
    
    /**
     * GoF Flyweight instance of CopyResourceAction
     */
    public static final CopyResourceAction INSTANCE =
        new CopyResourceAction();

    /**
     * @param keyIn
     */
    private CopyResourceAction() {
        super("action.object.resource.copy");
        
        this.setEnabled(false);
        
        this.putValue(SMALL_ICON, Application.createIcon(
                "icon/action.copy.generic.png"));
        
        Application.getInstance().addObserver(this);
    }

    /**
     * <Some description here>
     * 
     * @param e
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        if (this.resObj != null) {
            PasteResourceAction.INSTANCE.setCopyObject(this.resObj);
        }
    }

    /**
     * <Some description here>
     * 
     * @param objectIn
     * @see de.xplib.xdbm.util.UIObjectObserver#update(de.xplib.xdbm.ui.model.UIObject)
     */
    public void update(UIObject objectIn) {
        if (objectIn instanceof ResourceObject 
                && !(objectIn instanceof QueryResultObject)) {
            
            this.setEnabled(true);
            this.resObj = (ResourceObject) objectIn;
        } else {
            this.setEnabled(false);
            this.resObj = null;
        }
    }
}
