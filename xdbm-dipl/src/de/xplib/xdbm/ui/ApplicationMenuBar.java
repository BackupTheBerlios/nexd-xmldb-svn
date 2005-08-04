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
 * $Log: ApplicationMenuBar.java,v $
 * Revision 1.1  2005/04/12 08:34:21  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.ui;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.jgoodies.looks.HeaderStyle;
import com.jgoodies.looks.Options;
import com.jgoodies.looks.plastic.PlasticLookAndFeel;

import de.xplib.xdbm.ui.action.ApplicationExitAction;
import de.xplib.xdbm.ui.action.CopyResourceAction;
import de.xplib.xdbm.ui.action.CutResourceAction;
import de.xplib.xdbm.ui.action.DeleteCollectionAction;
import de.xplib.xdbm.ui.action.DeleteResourceAction;
import de.xplib.xdbm.ui.action.DisconnectAction;
import de.xplib.xdbm.ui.action.FlippingAction;
import de.xplib.xdbm.ui.action.NewCollectionAction;
import de.xplib.xdbm.ui.action.NewVirtualCollectionAction;
import de.xplib.xdbm.ui.action.NewXMLResourceAction;
import de.xplib.xdbm.ui.action.PasteResourceAction;
import de.xplib.xdbm.ui.action.QueryCollectionAction;
import de.xplib.xdbm.ui.action.QueryXMLResourceAction;
import de.xplib.xdbm.ui.action.ReconnectAction;
import de.xplib.xdbm.ui.action.SelectConnectionAction;
import de.xplib.xdbm.ui.action.ShowPCVResourceAction;
import de.xplib.xdbm.ui.action.ShowXMLAction;
import de.xplib.xdbm.ui.action.StartPluginAction;
import de.xplib.xdbm.ui.model.Connection;
import de.xplib.xdbm.ui.model.PluginFile;
import de.xplib.xdbm.util.Config;
import de.xplib.xdbm.util.ConfigObserver;
import de.xplib.xdbm.util.I18N;
import de.xplib.xdbm.util.I18NObserver;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class ApplicationMenuBar 
    extends JMenuBar 
    implements ConfigObserver, I18NObserver {

    /**
     * Comment for <code>app</code>
     */
    private final Application app;
    
    /**
     * Comment for <code>jmFile</code>
     */
    private JMenu jmFile = new JMenu("File");
    
    /**
     * Comment for <code>jmFileNew</code>
     */
    private JMenu jmFileNew = new JMenu("New");
    
    private JMenu jmEdit = new JMenu("Edit");
    
    private JMenu jmView = new JMenu("View");
    
    private JMenu jmViewPlugins = new JMenu("Plugins");
    
    private JMenu jmQuery = new JMenu("Query");
    
    /**
     * Comment for <code>jmiFileConnect</code>
     */
    private JMenuItem jmiFileConnect = new JMenuItem(
            SelectConnectionAction.INSTANCE);
    
    /**
     * Comment for <code>jmiFileDisconnect</code>
     */
    private JMenuItem jmiFileDisconnect = new JMenuItem(
            DisconnectAction.INSTANCE);
    
    private JMenuItem[] jmiFileReconnect = new JMenuItem[0];
    
    /**
     * Comment for <code>jmiExit</code>
     */
    private JMenuItem jmiExit = new JMenuItem(ApplicationExitAction.INSTANCE);
    
    private Plugin[] plugins = new Plugin[0];
    
    /**
     * @param appIn The main frame.
     */
    public ApplicationMenuBar(final Application appIn) {
        this.app = appIn;
        
        this.putClientProperty(Options.HEADER_STYLE_KEY, HeaderStyle.SINGLE);
        this.putClientProperty(PlasticLookAndFeel.IS_3D_KEY, Boolean.TRUE);
        
        this.app.setJMenuBar(this);
        
        this.add(this.jmFile);
        this.jmFile.add(this.jmFileNew);
        this.jmFileNew.setIcon(Application.createIcon("icon/menu.new.png"));
        this.jmFileNew.add(new JMenuItem(NewCollectionAction.INSTANCE));
        this.jmFileNew.add(new JMenuItem(NewVirtualCollectionAction.INSTANCE));
        this.jmFileNew.add(new JMenuItem(NewXMLResourceAction.INSTANCE));
        
        this.jmFile.addSeparator();
        this.jmFile.add(this.jmiFileConnect);
        this.jmFile.add(this.jmiFileDisconnect);
        this.jmFile.addSeparator();
        this.jmFile.addSeparator();
        this.jmFile.add(this.jmiExit);
        
        this.add(this.jmEdit);
        this.jmEdit.add(new JMenuItem(CutResourceAction.INSTANCE));
        this.jmEdit.add(new JMenuItem(CopyResourceAction.INSTANCE));
        this.jmEdit.add(new JMenuItem(PasteResourceAction.INSTANCE));
        this.jmEdit.add(new JMenuItem(new FlippingAction(new Action[] {
                DeleteResourceAction.INSTANCE, DeleteCollectionAction.INSTANCE
        })));
                
        this.add(this.jmView);
        this.jmView.add(this.jmViewPlugins);
        
        this.initPlugins();
        /*
        Map map  = this.app.getConfig().getPluginFiles();
        Iterator it = map.keySet().iterator();
        while (it.hasNext()) {
            try {
                Plugin p = ((PluginFile) map.get(it.next())).createPlugin();
                this.jmViewPlugins.add(new JMenuItem(p.getTitle()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        */
        this.jmView.addSeparator();
        
        this.jmView.add(new JMenuItem(ShowXMLAction.INSTANCE));
        this.jmView.add(new JMenuItem(ShowPCVResourceAction.INSTANCE));
        
        
        this.add(this.jmQuery);
        this.jmQuery.add(new JMenuItem(QueryCollectionAction.INSTANCE));
        this.jmQuery.add(new JMenuItem(QueryXMLResourceAction.INSTANCE));
        
        // we have build the system menu, now we setup the plugin menus.
        for (int i = 0; i < this.plugins.length; i++) {
            this.plugins[i].setUpMenu(this);
        }
        
        I18N i18n = I18N.getInstance();
        i18n.addObserver(this);
        this.updateI18N(i18n);
        
        Config cfg = this.app.getConfig();
        cfg.addObserver(this);
        this.update(cfg);
    }
    
    public JMenu getFileMenu() {
        return this.jmFile;
    }
    
    public JMenu getEditMenu() {
        return this.jmEdit;
    }
    
    public JMenu getViewMenu() {
        return this.jmView;
    }
    
    public JMenu getQueryMenu() {
        return this.jmQuery;
    }
    
    private void initPlugins() {
        
        List plugs = new ArrayList();
        
        Map map  = this.app.getConfig().getPluginFiles();
        Iterator it = map.keySet().iterator();
        while (it.hasNext()) {
            try {
                plugs.add(((PluginFile) map.get(it.next())).createPlugin());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        Collections.sort(plugs, new Comparator() {
            public int compare(final Object o1, final Object o2) {
                String t1 = ((Plugin) o1).getTitle();
                String t2 = ((Plugin) o2).getTitle();
                
                int l = t1.length() < t2.length() ? t1.length() : t2.length();
                for (int i = 0; i < l; i++) {
                    if (t1.charAt(i) < t2.charAt(i)) {
                        return -1;
                    } else if (t1.charAt(i) > t2.charAt(i)) {
                        return 1;
                    }
                }
                return 0;
            }
        });
        
        this.plugins = (Plugin[]) plugs.toArray(new Plugin[plugs.size()]);
        
        for (int i = 0; i < this.plugins.length; i++) {
            Plugin plugin = this.plugins[i];
            this.jmViewPlugins.add(new JMenuItem(new StartPluginAction(plugin)));
        }
    }
    
    /**
     * <Some description here>
     * 
     * @param i18nIn
     * @see de.xplib.xdbm.util.I18NObserver#updateI18N(I18N)
     */
    public void updateI18N(final I18N i18nIn) {
        
        this.jmFile.setText(i18nIn.getTitle("menu.file"));
        this.jmFile.setMnemonic(i18nIn.getMnemonic("menu.file").intValue());
        
        this.jmFileNew.setText(i18nIn.getTitle("menu.file.new"));
        this.jmFileNew.setMnemonic(
                i18nIn.getMnemonic("menu.file.new").intValue());
        
        this.jmEdit.setText(i18nIn.getTitle("menu.edit"));
        this.jmEdit.setMnemonic(i18nIn.getMnemonic("menu.edit").intValue());
        
        this.jmView.setText(i18nIn.getTitle("menu.view"));
        this.jmView.setMnemonic(i18nIn.getMnemonic("menu.view").intValue());
        
        this.jmViewPlugins.setText(i18nIn.getTitle("menu.view.plugins"));
        this.jmViewPlugins.setMnemonic(i18nIn.getMnemonic("menu.view.plugins").intValue());
        
        this.jmQuery.setText(i18nIn.getTitle("menu.query"));
        this.jmQuery.setMnemonic(i18nIn.getMnemonic("menu.query").intValue());
    }
    
    
    /**
     * <Some description here>
     * 
     * @param configIn
     * @see de.xplib.xdbm.util.ConfigObserver#update(de.xplib.xdbm.util.Config)
     */
    public void update(final Config configIn) {
        
        for (int i = 0; i < this.jmiFileReconnect.length; i++) {
            this.jmFile.remove(this.jmiFileReconnect[i]);
        }
        
        int pos = this.jmFile.getItemCount() - 1;
        if (jmFile.getItem(pos) == this.jmiExit) {
            
            pos       -= 1;
            int index  = 0;
            
            List list   = configIn.getConnections();
            Iterator it = list.iterator();
            this.jmiFileReconnect = new JMenuItem[list.size()];
            
            while (it.hasNext()) {
                Connection c  = (Connection) it.next();
                JMenuItem jmi = new JMenuItem(new ReconnectAction(c));
                jmFile.add(jmi, pos++);

                this.jmiFileReconnect[index++] = jmi;
            }
        }
        
        
        
    }
}
