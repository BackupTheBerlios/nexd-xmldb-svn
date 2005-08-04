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
 * $Log: ApplicationExitAction.java,v $
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
import de.xplib.xdbm.util.Config;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class ApplicationExitAction extends I18NAction {
    
    /**
     * GoF Flyweight instance of ApplicationExistAction.
     */
    public static final ApplicationExitAction INSTANCE = 
        new ApplicationExitAction();
    
    /**
     * 
     */
    public ApplicationExitAction() {
        super("action.app.exit");
    }

    /**
     * <Some description here>
     * 
     * @param e
     * @see java.awt.event.ActionListener#actionPerformed(
     *      java.awt.event.ActionEvent)
     */
    public void actionPerformed(final ActionEvent e) {
        Config cfg = Application.getInstance().getConfig();
        cfg.save();
        
        Database db = Application.getInstance().getXmldb();
        if (db != null) {
            try {
                Collection coll = db.getCollection(
                        db.getName() + ":///db", "sa", "");
                if (coll != null) {
                    coll.close();
                }
            } catch (XMLDBException e1) {
            }
        }
        
        System.exit(0);
    }
}
