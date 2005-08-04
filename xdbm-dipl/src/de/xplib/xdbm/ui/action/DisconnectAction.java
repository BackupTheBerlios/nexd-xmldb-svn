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
 * $Log: DisconnectAction.java,v $
 * Revision 1.1  2005/04/12 08:34:21  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.ui.action;

import java.awt.event.ActionEvent;

import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.XMLDBException;

import de.xplib.xdbm.ui.Application;
import de.xplib.xdbm.util.XmldbObserver;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class DisconnectAction extends I18NAction implements XmldbObserver {
    
    /**
     * GoF Flyweigth instance of DisconnectAction.
     */
    public static final DisconnectAction INSTANCE = new DisconnectAction();

    /**
     * 
     */
    public DisconnectAction() {
        super("action.app.disconnect");
        
        Application.getInstance().addObserver(this);
        this.setEnabled(false);
    }

    /**
     * <Some description here>
     * 
     * @param ae
     * @see java.awt.event.ActionListener#actionPerformed(
     *      java.awt.event.ActionEvent)
     */
    public void actionPerformed(final ActionEvent ae) {
        
        try {
            Database db = Application.getInstance().getXmldb();
            if (db != null) {
                Collection coll = db.getCollection("/db", "sa", "");
                if (coll == null) {
                    coll = db.getCollection(
                            Application.getInstance().getConfig()
                                       .getConnection().getUri(), "sa", "");
                }
                
                if (coll != null) {
                    coll.close();
                }
            }
        } catch (XMLDBException e) {
            e.printStackTrace(Application.err);
        }
        
        Application.getInstance().setXmldb(null);
    }
    
    

    /**
     * <Some description here>
     * 
     * @param xmldbIn
     * @see de.xplib.xdbm.util.XmldbObserver#update(org.xmldb.api.base.Database)
     */
    public void update(final Database xmldbIn) {
        this.setEnabled(xmldbIn != null);

    }
}
