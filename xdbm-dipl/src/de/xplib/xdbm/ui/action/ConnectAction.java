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
 * $Log: ConnectAction.java,v $
 * Revision 1.1  2005/04/12 08:34:21  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.ui.action;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;

import javax.swing.JOptionPane;

import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.ErrorCodes;
import org.xmldb.api.base.XMLDBException;

import de.xplib.xdbm.ui.Application;
import de.xplib.xdbm.ui.model.Connection;
import de.xplib.xdbm.util.I18N;
import de.xplib.xdbm.util.XmldbObserver;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class ConnectAction extends I18NAction implements XmldbObserver {
    
    /**
     * GoF Flyweight instance of ConnectionAction.
     */
    public static final ConnectAction INSTANCE = new ConnectAction();
    
    /**
     * Used external ClassLoader instances.
     */
    private HashMap classLoader = new HashMap();
    
    /**
     * 
     */
    public ConnectAction() {
        super("action.app.connect");
        
        Application.getInstance().addObserver(this);
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
        I18N i18n       = I18N.getInstance();
        Connection conn = app.getConfig().getConnection();
        
        if (conn == null) {
            
            JOptionPane.showMessageDialog(
                    app, 
                    i18n.getTitle(
                            "dialog.error.null", new String[] {"Connection"}), 
                    i18n.getTitle("dialog.error.title"), 
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        URLClassLoader ucl = null;
        try {
            
            String jarFile = conn.getJarFile();
            if (!this.classLoader.containsKey(jarFile)) {
                ucl = new URLClassLoader(
                        new URL[] {new URL(jarFile)},
                        getClass().getClassLoader());
                this.classLoader.put(jarFile, ucl);
            } else {
                ucl = (URLClassLoader) this.classLoader.get(jarFile);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    app, 
                    i18n.getTitle(
                            "dialog.error.nofile", 
                            new String[] {conn.getJarFile()}), 
                    i18n.getTitle("dialog.error.title"), 
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        Class clazz = null;
        try {
            clazz = ucl.loadClass(conn.getClassName());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    app, 
                    i18n.getTitle(
                            "dialog.error.noclass", 
                            new String[] {conn.getClassName()}), 
                    i18n.getTitle("dialog.error.title"), 
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Object o = null;
        try {
            o = clazz.newInstance();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    app, 
                    i18n.getTitle(
                            "dialog.error.nodriver", 
                            new String[] {conn.getClassName()}), 
                    i18n.getTitle("dialog.error.title"), 
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!(o instanceof Database)) {
            JOptionPane.showMessageDialog(
                    app, 
                    i18n.getTitle(
                            "dialog.error.noxapidb", 
                            new String[] {conn.getClassName()}), 
                    i18n.getTitle("dialog.error.title"), 
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            DatabaseManager.registerDatabase((Database) o);
            
            Collection coll = DatabaseManager.getCollection(
                    conn.getUri(), "sa", "");
        } catch (XMLDBException e) {
            
            if (conn.getUri().startsWith("xmldb:")) {
                String uri = conn.getUri().substring(6);
                System.out.println(uri);
                
                try {
                    DatabaseManager.registerDatabase((Database) o);
                    
                    Collection coll = ((Database) o).getCollection(
                            uri, "sa", "");
                    if (coll == null) {
                        throw new XMLDBException(
                                ErrorCodes.NO_SUCH_COLLECTION,
                                "Cannot find collection " + uri);
                    }
                    
                    conn.setUri(uri);
                    
                    
                } catch (XMLDBException e1) {
                    JOptionPane.showMessageDialog(
                            app, 
                            i18n.getTitle(
                                    "dialog.error.nodbconn", 
                                    new String[] {uri}), 
                                    i18n.getTitle("dialog.error.title"), 
                                    JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else {
            
                JOptionPane.showMessageDialog(
                        app, 
                        i18n.getTitle(
                                "dialog.error.nodbconn", 
                                new String[] {conn.getUri()}), 
                                i18n.getTitle("dialog.error.title"), 
                                JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        app.getConfig().addConnection(conn);
        
        app.setXmldb((Database) o);
    }
    
    

    /**
     * <Some description here>
     * 
     * @param xmldbIn
     * @see de.xplib.xdbm.util.XmldbObserver#update(org.xmldb.api.base.Database)
     */
    public void update(final Database xmldbIn) {
        this.setEnabled(xmldbIn == null);
    }
}
