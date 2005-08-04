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
 * $Log: NewCollectionDialog.java,v $
 * Revision 1.1  2005/04/12 08:34:22  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.ui.dialog;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Service;
import org.xmldb.api.base.XMLDBException;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import de.xplib.xdbm.ui.Application;
import de.xplib.xdbm.ui.action.CancelDialogAction;
import de.xplib.xdbm.ui.action.CloseDialogWrapperAction;
import de.xplib.xdbm.ui.action.NewCollectionExecuteAction;
import de.xplib.xdbm.ui.model.ResourceObject;
import de.xplib.xdbm.ui.model.UIObject;
import de.xplib.xdbm.ui.model.VirtualCollectionObject;
import de.xplib.xdbm.ui.model.VirtualResourceObject;
import de.xplib.xdbm.ui.model.XapiCollectionObject;
import de.xplib.xdbm.ui.widgets.JFileField;
import de.xplib.xdbm.util.I18N;


/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class NewCollectionDialog extends AbstractDialog {
    
    /**
     * Comment for <code>ctxColl</code>
     */
    private Collection ctxColl = null;
    
    /**
     * Comment for <code>ctxPath</code>
     */
    private String ctxPath = "";
    
    /**
     * Comment for <code>i18n</code>
     */
    private I18N i18n = I18N.getInstance();
    
    private String msgErrTitle = i18n.getTitle("app.err.title");
    
    /**
     * Comment for <code>jtfParentColl</code>
     */
    private JTextField jtfParentColl = new JTextField(20);
    
    /**
     * Comment for <code>jtfCollName</code>
     */
    private JTextField jtfCollName = new JTextField(20);
    
    /**
     * Comment for <code>jffSchema</code>
     */
    private JFileField jffSchema = new JFileField();

    /**
     * 
     */
    public NewCollectionDialog() {
        super("", true);
        
        Application app = Application.getInstance(); 
        UIObject o = app.getUIObject();
        if (o == null) {
            JOptionPane.showMessageDialog(
                    app, i18n.getText("dialog.collection.new.err.noparent"), 
                    msgErrTitle, JOptionPane.ERROR_MESSAGE);
        } else if (o instanceof VirtualCollectionObject 
                || o instanceof VirtualResourceObject) {
            JOptionPane.showMessageDialog(
                    app, i18n.getText("dialog.collection.new.err.wrongparent"),
                    msgErrTitle, JOptionPane.ERROR_MESSAGE);
        } else {
            if (o instanceof XapiCollectionObject) {
                this.ctxColl = (Collection) o.getUserObject(); 
            } else if (o instanceof ResourceObject) {
                this.ctxColl = ((ResourceObject) o).getCollection();
            } else {
                JOptionPane.showMessageDialog(
                        app, i18n.getText("app.err.msg.unknown"), 
                        msgErrTitle, JOptionPane.ERROR_MESSAGE);
            }
            
            try {
                String path = "/" + this.ctxColl.getName();
                Collection c = this.ctxColl.getParentCollection();
                while (c != null) {
                    path = "/" + c.getName() + path;
                    c = c.getParentCollection();
                }
                this.ctxPath = path;
                
            } catch (XMLDBException e) {
                e.printStackTrace(Application.err);
            }
        }
        
        this.initListener();
        this.initUI();
        
        this.pack();
        this.setResizable(false);
    }
    
    /**
     * 
     */
    private void initListener() {
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(final WindowEvent we) {
                NewCollectionExecuteAction.INSTANCE.setEnabled(true);
            }
        });
    }
    
    /**
     * 
     */
    private void initUI() {
        
        boolean sixdml = false;
        Service s = null;
        try {
            s = this.ctxColl.getService(
                    "SixdmlCollectionManagementService", "1.0");
            if (s == null) {
                s = this.ctxColl.getService(
                        "CollectionManagementService", "1.0");
            } else {
                sixdml = true;
            }
            if (s == null) {
                JOptionPane.showMessageDialog(
                        Application.getInstance(), 
                        i18n.getText("dialog.collection.new.err.noservice"),
                        msgErrTitle, JOptionPane.ERROR_MESSAGE);
            }
        } catch (XMLDBException e) {
            e.printStackTrace(Application.err);
        }

        
        this.setTitle(i18n.getTitle(
                "dialog.collection.new.title." + (sixdml ? "sixdml" : "xapi")));
        
        this.jtfParentColl.setEditable(false);
        this.jtfParentColl.setText(this.ctxPath);
        
        this.jtfCollName.addKeyListener(new KeyAdapter() {
            public void keyPressed(final KeyEvent ke) {
                String s = jtfCollName.getText();
                if (!s.equals("")) {
                    jtfParentColl.setText(ctxPath + "/" + s);
                    
                    Thread t = new Thread() {
                        public void run() {
                            try {
                                if (ctxColl.getChildCollection(
                                        jtfCollName.getText()) != null) {
                                    
                                    String err = i18n.getText(
                                            "dialog.collection.new.err.exit");

                                    jtfCollName.setForeground(Color.WHITE);
                                    jtfCollName.setBackground(Color.RED);
                                    jtfCollName.setToolTipText(err);
                                                                        
                                    Application.err.println(err);
                                    NewCollectionExecuteAction.INSTANCE.setEnabled(false);
                                } else {
                                    jtfCollName.setForeground(Color.BLACK);
                                    jtfCollName.setBackground(Color.WHITE);
                                    jtfCollName.setToolTipText("");
                                    NewCollectionExecuteAction.INSTANCE.setEnabled(true);
                                }
                            } catch (XMLDBException e) {
                            }
                        }
                    };
                    t.start();
                } else {
                    jtfParentColl.setText(ctxPath);
                    NewCollectionExecuteAction.INSTANCE.setEnabled(false);
                }
            }
        });
        
        NewCollectionExecuteAction.INSTANCE.setEnabled(false);
        NewCollectionExecuteAction.INSTANCE.setCollection(this.ctxColl);
        NewCollectionExecuteAction.INSTANCE.setCollectionNameComponent(this.jtfCollName);
        if (sixdml) {
            NewCollectionExecuteAction.INSTANCE.setSchemaComponent(this.jffSchema);
        }
        
        FormLayout layout = new FormLayout(
                "right:pref, 3dlu, pref, 3dlu, pref", 
                "p, 3dlu, p, 3dlu, p, 3dlu, p, 9dlu, p, 3dlu, p");
        
        layout.setColumnGroups(new int[][]{{3, 5}});
        
        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();
        
        CellConstraints cc = new CellConstraints();
        
        builder.addLabel(
                i18n.getTitle("dialog.collection.new.parent"), cc.xy(1, 1));
        builder.add(this.jtfParentColl, cc.xyw(3, 1, 3));
        this.jtfParentColl.setToolTipText(
                i18n.getToolTip("dialog.collection.new.parent"));
        
        builder.addLabel(
                i18n.getTitle("dialog.collection.new.name"), cc.xy(1, 3));
        builder.add(this.jtfCollName, cc.xyw(3, 3, 3));
        this.jtfCollName.setToolTipText(
                i18n.getToolTip("dialog.collection.new.name"));
        
        if (sixdml) {
            builder.addLabel(
                    i18n.getTitle("dialog.collection.new.schema"), cc.xy(1, 5));
            builder.add(this.jffSchema, cc.xyw(3, 5, 3));
            this.jffSchema.setToolTipText(
                    i18n.getToolTip("dialog.collection.new.schema"));
        }
        
        builder.addSeparator("", cc.xyw(1, 7, 5));
        
        builder.add(new JButton(new CancelDialogAction(this)), cc.xy(3, 9));
        builder.add(new JButton(new CloseDialogWrapperAction(
                NewCollectionExecuteAction.INSTANCE, this)), cc.xy(5, 9));
        
        this.getContentPane().setLayout(new GridLayout(1, 1));
        this.getContentPane().add(builder.getPanel());
    }

}
