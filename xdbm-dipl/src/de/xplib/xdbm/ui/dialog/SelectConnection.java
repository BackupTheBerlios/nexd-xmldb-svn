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
 * $Log: SelectConnection.java,v $
 * Revision 1.1  2005/04/12 08:34:22  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.ui.dialog;

import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.xmldb.api.base.Database;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import de.xplib.xdbm.ui.Application;
import de.xplib.xdbm.ui.action.CancelDialogAction;
import de.xplib.xdbm.ui.action.ConnectAction;
import de.xplib.xdbm.ui.model.Connection;
import de.xplib.xdbm.ui.widgets.SelectDriverField;
import de.xplib.xdbm.util.I18N;
import de.xplib.xdbm.util.XmldbObserver;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class SelectConnection extends AbstractDialog implements XmldbObserver {
    
    /**
     * Comment for <code>app</code>
     */
    private Application app;
    
    /**
     * Comment for <code>sdField</code>
     */
    private SelectDriverField sdfJar;
    
    /**
     * Comment for <code>jtfClass</code>
     */
    private JTextField jtfClass = new JTextField(20);
    
    /**
     * Comment for <code>jlURIs</code>
     */
    private JList jlURIs = new JList(new String[] {"   ", "   ", "  "});
    
    /**
     * Comment for <code>jbConnect</code>
     */
    private JButton jbConnect = new JButton(ConnectAction.INSTANCE);

    /**
     * @throws java.awt.HeadlessException
     */
    public SelectConnection() throws HeadlessException {
        super("", true);
        
        this.app = Application.getInstance();
        this.app.addObserver(this);
        
        
        this.initUI();

        this.pack();
        this.setResizable(false);
    }

    
    
    /**
     * <Some description here>
     * 
     * @param xmldbIn
     * @see de.xplib.xdbm.util.XmldbObserver#update(org.xmldb.api.base.Database)
     */
    public void update(final Database xmldbIn) {
        if (xmldbIn != null) {
            this.app.removeObserver(this);
            this.dispose();
        }
    }
    
    /**
     * 
     */
    private void initUI() {
        
        I18N i18n = I18N.getInstance();
        
        this.setTitle(i18n.getTitle("dialog.select.conn.title"));
        
        this.jtfClass.setEditable(false);
        this.jtfClass.setToolTipText(
                i18n.getToolTip("dialog.select.conn.class"));
        
        this.sdfJar = new SelectDriverField(this.app.getConfig());
        this.sdfJar.setToolTipText(
                i18n.getToolTip("dialog.select.conn.jarfile"));
        this.sdfJar.addItemListener(new ItemListener() {
            public void itemStateChanged(final ItemEvent ie) {

                String name = (String) ie.getItem();
                jtfClass.setText(app.getConfig().getDriverClass(name));
                
                jlURIs.removeAll();
                
                List list = app.getConfig().getDatabaseURIs(name);
                jlURIs.setListData(list.toArray(new String[list.size()]));
            }
        });
        this.sdfJar.update(this.app.getConfig());
        
        this.jlURIs.setToolTipText(
                i18n.getToolTip("dialog.select.conn.uri"));
        this.jlURIs.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(final ListSelectionEvent lse) {
                
                String jar = sdfJar.getSelectedItem();
                String cls = jtfClass.getText().trim();
                
                String uri = (String) jlURIs.getSelectedValue();
                uri = (uri == null ? "" : uri.trim());
                
                if (jar.equals("") || cls.equals("") || uri.equals("")) {
                    jbConnect.setEnabled(false);
                }
                
                app.getConfig().setConnection(new Connection(jar, cls, uri));
                
                jbConnect.setEnabled(true);
            }
        });
        
        this.jbConnect.setEnabled(false);
        
        
        FormLayout layout = new FormLayout(
                "right:pref, 3dlu, pref, 3dlu, pref, 3dlu, pref", 
                "p, 3dlu, p, 3dlu, p, 3dlu, p, 9dlu, 30dlu, 3dlu, p"); 
        
        layout.setColumnGroups(new int[][]{{3 , 5, 7}});
        
        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();
        
//      Obtain a reusable constraints object to place components in the grid.
        CellConstraints cc = new CellConstraints();

//         Fill the grid with components; the builder can create
//         frequently used components, e.g. separators and labels.

//         Add a titled separator to cell (1, 1) that spans 7 columns.
        builder.addLabel(
                i18n.getTitle("dialog.select.conn.jarfile"), cc.xy(1, 3));
        builder.add(this.sdfJar, cc.xyw(3, 3, 5));
        
        builder.addLabel("Klasse", cc.xy(1, 5));
        builder.add(this.jtfClass, cc.xyw(3, 5, 5));
        
        //builder.addSeparator("", cc.xyw(1, 9, 7));
        
        JScrollPane jsp = new JScrollPane();
        jsp.getViewport().add(this.jlURIs);
                
        builder.add(jsp, cc.xyw(1, 9, 7));
        
        builder.add(new JButton(new CancelDialogAction(this)), cc.xy(5, 11));
        builder.add(this.jbConnect, cc.xy(7, 11));
        
        this.getContentPane().setLayout(new GridLayout(1, 1));
        this.getContentPane().add(builder.getPanel());
        
        this.setSize(400, 300);
    }
}
