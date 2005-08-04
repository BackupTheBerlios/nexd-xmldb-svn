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
 * $Log: NewXMLResourceDialog.java,v $
 * Revision 1.1  2005/04/12 08:34:22  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.ui.dialog;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JTextField;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import de.xplib.xdbm.ui.Application;
import de.xplib.xdbm.ui.action.CancelDialogAction;
import de.xplib.xdbm.ui.action.CloseDialogWrapperAction;
import de.xplib.xdbm.ui.action.NewXMLResourceExecuteAction;
import de.xplib.xdbm.ui.listener.AppendTextListener;
import de.xplib.xdbm.ui.widgets.JFileField;
import de.xplib.xdbm.ui.widgets.JFileFieldSelectListener;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class NewXMLResourceDialog extends AbstractDialog {
    
    private JTextField jtfPath = new JTextField(20);
    
    private JTextField jtfName = new JTextField(20);
    
    private JFileField jffXML = new JFileField();

    /**
     * 
     */
    public NewXMLResourceDialog() {
        super("", true);
        
        this.setTitle(i18n.getTitle("dialog.resource.xml.new.title"));
        
        this.loadCollection();
        
        this.initListener();
        this.initUI();
        this.pack();
        this.setResizable(false);
    }
    
    private void initListener() {
        
        this.jtfName.addKeyListener(
                new AppendTextListener(
                        this.jtfName, this.jtfPath, this.ctxPath + "/"));
        
        Thread t = new Thread() {
            public void run() {
                String value = jtfName.getText();
                do {
                    
                    if (jtfName.getText().equals(value)) {
                        try {
                            Thread.sleep(450);
                        } catch (InterruptedException e1) {
                        }
                    }
                    
                    value = jtfName.getText().trim();
                    try {
                        if (ctxColl.getResource(value) != null) {
                            jtfName.setBackground(Color.RED);
                            jtfName.setForeground(Color.WHITE);
                            NewXMLResourceExecuteAction.INSTANCE.setEnabled(false);
                        } else {
                            jtfName.setBackground(Color.WHITE);
                            jtfName.setForeground(Color.BLACK);
                            
                            if (!jffXML.getText().trim().equals("")) {
                                NewXMLResourceExecuteAction.INSTANCE.setColl(ctxColl);
                                NewXMLResourceExecuteAction.INSTANCE.setFile(jffXML.getText());
                                NewXMLResourceExecuteAction.INSTANCE.setId(jtfName.getText());
                                NewXMLResourceExecuteAction.INSTANCE.setEnabled(true);
                            }
                        }
                    } catch (Exception e) {
                        NewXMLResourceExecuteAction.INSTANCE.setEnabled(false);
                    }
                } while (NewXMLResourceDialog.this.isShowing());
                
                Application.out.println("Thread " + this + "stoppen.");
            }
        };
        t.start();
              
        
        this.jffXML.addSelectListener(new JFileFieldSelectListener() {
            public void actionPerformed(ActionEvent ae) {
                String name = jtfName.getText().trim();
                if (name.equals("")) {
                    jtfName.setText(new File(ae.getActionCommand()).getName());
                }
            }
        });
    }
    
    
    /**
     * 
     */
    private void initUI() {
        
        NewXMLResourceExecuteAction.INSTANCE.setEnabled(false);
        
        this.jtfPath.setEditable(false);
        this.jtfPath.setText(this.ctxPath);
        
        FormLayout layout = new FormLayout(
                "right:pref, 3dlu, pref, 3dlu, pref", 
                "p, 3dlu, p, 3dlu, p, 3dlu, p, 9dlu, p, 3dlu, p");
        
        layout.setColumnGroups(new int[][]{{3, 5}});
        
        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();
        
        CellConstraints cc = new CellConstraints();
        
        builder.addLabel("Pfad:", cc.xy(1, 1));
        builder.add(this.jtfPath, cc.xyw(3, 1, 3));
        
        builder.addLabel("Name:", cc.xy(1, 3));
        builder.add(this.jtfName, cc.xyw(3, 3, 3));
        
        builder.addLabel("Datei:", cc.xy(1, 5));
        builder.add(this.jffXML, cc.xyw(3, 5, 3));
        
        builder.addSeparator("", cc.xyw(1, 7, 5));
        
        builder.add(new JButton(new CancelDialogAction(this)), cc.xy(3, 9));
        builder.add(new JButton(new CloseDialogWrapperAction(
                NewXMLResourceExecuteAction.INSTANCE, this)), cc.xy(5, 9));
        
        this.getContentPane().setLayout(new GridLayout(1, 1));
        this.getContentPane().add(builder.getPanel());
    }

}
