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
 * $Log: NewVirtualCollectionDialog.java,v $
 * Revision 1.1  2005/04/12 08:34:22  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.ui.dialog;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.xmldb.api.base.Collection;
import org.xmldb.api.base.XMLDBException;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import de.xplib.xdbm.ui.action.CancelDialogAction;
import de.xplib.xdbm.ui.action.CloseDialogWrapperAction;
import de.xplib.xdbm.ui.action.ExecuteNewVirtualCollectionAction;
import de.xplib.xdbm.ui.listener.AppendTextListener;
import de.xplib.xdbm.ui.widgets.JFileField;
import de.xplib.xdbm.util.I18N;
import de.xplib.xdbm.util.XMLDBUtil;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class NewVirtualCollectionDialog extends AbstractDialog {
    
    private Collection collection = null;
    
    private String collPath = "";
    
    private JLabel jlCollection = new JLabel();
    
    private JTextField jtfCollection = new JTextField(20);
    
    private JLabel jlName = new JLabel();
    
    private JTextField jtfName = new JTextField(20);
    
    private JLabel jlSchema = new JLabel();
    
    private JFileField jffSchema = new JFileField();
    
    private JLabel jlStylesheet = new JLabel();
    
    private JFileField jffStylesheet = new JFileField();
    

    /**
     * @param titleIn
     * @param modalIn
     */
    public NewVirtualCollectionDialog(final Collection collIn) {
        super("", true);
        
        this.collection = collIn;
        this.collPath   = XMLDBUtil.getPath(collIn);
        
        this.initI18N();
        this.initListener();
        this.initUI();
        this.pack();
        this.setResizable(false);
    }
    
    private void initI18N() {
        
        I18N i18n = I18N.getInstance();
        
        this.setTitle(i18n.getTitle("dialog.vcollection.new.title"));
        
        this.jlCollection.setText(i18n.getTitle("dialog.vcollection.new.parent"));
        this.jtfCollection.setToolTipText(i18n.getToolTip("dialog.vcollection.new.parent"));
        
        this.jlName.setText(i18n.getTitle("dialog.vcollection.new.name"));
        this.jtfName.setToolTipText(i18n.getToolTip("dialog.vcollection.new.name"));
        
        this.jlSchema.setText(i18n.getTitle("dialog.vcollection.new.vcl"));
        this.jffSchema.setToolTipText(i18n.getToolTip("dialog.vcollection.new.vcl"));
        
        this.jlStylesheet.setText(i18n.getTitle("dialog.vcollection.new.xsl"));
        this.jffStylesheet.setToolTipText(i18n.getToolTip("dialog.vcollection.new.xsl"));
    }
    
    private void initListener() {
        
        this.jtfName.addKeyListener(new AppendTextListener(
                this.jtfName, this.jtfCollection, this.collPath + "/"));
        
        
        Thread t = new Thread() {
            public void run() {
                
                ExecuteNewVirtualCollectionAction a = ExecuteNewVirtualCollectionAction.INSTANCE;
                
                String value = jtfName.getText();
                do {
                    
                    if (value.equals(jtfName.getText())) {
                        try {
                            Thread.sleep(400);
                        } catch (InterruptedException e) {
                        }
                    }
                    value = jtfName.getText().trim();
                    
                    try {
                        if (value.equals("")) {
                            a.setEnabled(false);
                        } else if (collection.getChildCollection(value) != null) {
                            a.setEnabled(false);
                            jtfName.setBackground(Color.RED);
                            jtfName.setForeground(Color.WHITE);
                            continue;
                        } else if (jffSchema.getText().equals("")) {
                            a.setEnabled(false);
                        } else {
                            a.setEnabled(true);
                            a.setContext(
                                    collection, value, 
                                    jffSchema.getText(), 
                                    jffStylesheet.getText());
                        }
                        jtfName.setBackground(Color.WHITE);
                        jtfName.setForeground(Color.BLACK);
                    } catch (XMLDBException e) {
                        a.setEnabled(false);
                    }
                    
                } while (NewVirtualCollectionDialog.this.isShowing());
            }
        };
        t.start();
    }
    
    /**
     * 
     */
    private void initUI() {
        
        this.jtfCollection.setText(this.collPath);
        this.jtfCollection.setEditable(false);
        
        this.jffSchema.addFileFilter(JFileField.VCS_FILE_FILTER);
        
        this.jffStylesheet.addFileFilter(JFileField.XSL_FILE_FILTER);
        
        FormLayout layout = new FormLayout(
                "right:pref, 3dlu, pref, 3dlu, pref", 
                "p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 9dlu, p, 3dlu, p");
        
        layout.setColumnGroups(new int[][]{{3, 5}});
        
        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();
        
        CellConstraints cc = new CellConstraints();
        
        
        builder.add(this.jlCollection, cc.xy(1, 1));
        builder.add(this.jtfCollection, cc.xyw(3, 1, 3));
        
        builder.add(this.jlName, cc.xy(1, 3));
        builder.add(this.jtfName, cc.xyw(3, 3, 3));
        
        builder.add(this.jlSchema, cc.xy(1, 5));
        builder.add(this.jffSchema, cc.xyw(3, 5, 3));
        
        builder.add(this.jlStylesheet, cc.xy(1, 7));
        builder.add(this.jffStylesheet, cc.xyw(3, 7, 3));
        
        builder.addSeparator("", cc.xyw(1, 9, 5));
        
        builder.add(new JButton(new CancelDialogAction(this)), cc.xy(3, 11));
        builder.add(new JButton(new CloseDialogWrapperAction(
                ExecuteNewVirtualCollectionAction.INSTANCE, this)), cc.xy(5, 11));
        
        this.setContentPane(builder.getPanel());
    }

}
