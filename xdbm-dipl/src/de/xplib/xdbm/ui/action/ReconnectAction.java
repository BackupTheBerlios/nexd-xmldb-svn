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
 * $Log: ReconnectAction.java,v $
 * Revision 1.1  2005/04/12 08:34:21  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.ui.action;

import java.awt.event.ActionEvent;

import de.xplib.xdbm.ui.Application;
import de.xplib.xdbm.ui.model.Connection;
import de.xplib.xdbm.util.I18N;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class ReconnectAction extends I18NAction {
    
    /**
     * Comment for <code>conn</code>
     */
    private Connection conn;

    /**
     * @param connIn Connection object.
     */
    public ReconnectAction(final Connection connIn) {
        super("");
        
        this.conn = connIn;
        
        this.putValue(NAME, connIn.getUri());
        this.putValue(SHORT_DESCRIPTION, I18N.getInstance().getToolTip(
                "action.app.reconnect", new String[] {connIn.getUri()}));
    }

    /**
     * <Some description here>
     * 
     * @param ae
     * @see java.awt.event.ActionListener#actionPerformed(
     *      java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent ae) {
        
        Application app = Application.getInstance();
        
        if (app.getXmldb() != null) {
            DisconnectAction.INSTANCE.actionPerformed(ae);
        }
        app.getConfig().setConnection(this.conn);
        
        ConnectAction.INSTANCE.actionPerformed(ae);
    }

}
