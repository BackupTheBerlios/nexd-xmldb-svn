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
 * $Log: AbstractXPathQueryDialog.java,v $
 * Revision 1.1  2005/04/12 08:34:22  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.ui.dialog;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import de.xplib.xdbm.ui.action.CancelDialogAction;
import de.xplib.xdbm.ui.action.CloseDialogWrapperAction;
import de.xplib.xdbm.ui.widgets.syntax.JEditTextArea;
import de.xplib.xdbm.ui.widgets.syntax.SyntaxDocument;
import de.xplib.xdbm.ui.widgets.syntax.XPathTokenMarker;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public abstract class AbstractXPathQueryDialog extends AbstractDialog {
    
    private JLabel contextLabel = new JLabel();
    
    private JTextField contextPath = new JTextField(20);
    
    private JLabel xpathLabel = new JLabel();
    
    private JEditTextArea xpathArea = new JEditTextArea();
    
    private Action action = null;
    
    private Object target = null;
    
    private String prefix = "";
        
    /**
     * @param titleIn
     * @param modalIn
     */
    public AbstractXPathQueryDialog(final String prefixIn,
                                    final Action actionIn,
                                    final Object targetIn) {
        super("", true);
        
        this.prefix = prefixIn;
        this.action = actionIn;
        this.target = targetIn;
        
        this.initI18N();
        this.initUI();
        this.initListener();
        this.pack();
        this.setResizable(false);
    }
    
    private void initI18N() {
        
        this.setTitle(this.i18n.getTitle(this.prefix + ".title"));
        
        this.contextLabel.setText(i18n.getTitle(this.prefix + ".target"));
        this.contextPath.setToolTipText(i18n.getToolTip(this.prefix + ".target"));
        
        this.xpathLabel.setText(i18n.getTitle(this.prefix + ".xpath"));
        this.xpathArea.setToolTipText(i18n.getToolTip(this.prefix + ".xpath"));
    }
    
    /**
     * 
     */
    private void initListener() {
        
        this.addWindowListener(new WindowAdapter() {
            public void windowOpened(WindowEvent we) {
                xpathArea.requestFocusInWindow();
                xpathArea.setCaretPosition(0);
            }
        });
        
        this.xpathArea.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent de) {
                handleChange(de);
            }
            public void changedUpdate(DocumentEvent de) {
                handleChange(de);
            }
            public void removeUpdate(DocumentEvent de) {
                handleChange(de);
            }
            void handleChange(DocumentEvent de) {
                if (de.getDocument().getLength() == 0) {
                    action.setEnabled(false);
                } else {
                    setupAction(action, target, xpathArea.getText().trim());
                    action.setEnabled(true);
                }
            }
        });
    }
    
    /**
     * 
     */
    private void initUI() {
        
        this.contextPath.setEditable(false);
        this.contextPath.setText(this.getPath(this.target));
        
        this.xpathArea.setTokenMarker(new XPathTokenMarker());
        this.xpathArea.setBorder(BorderFactory.createEtchedBorder());
        this.xpathArea.setDocument(new SyntaxDocument());
        
        FormLayout layout = new FormLayout(
                "right:pref, 3dlu, pref, 3dlu, pref", 
        "p, 3dlu, top:80dlu, 3dlu, p, 9dlu, p");
        
        layout.setColumnGroups(new int[][]{{3, 5}});
        
        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();
        
        CellConstraints cc = new CellConstraints();
        
        builder.add(this.contextLabel, cc.xy(1, 1));
        builder.add(this.contextPath, cc.xyw(3, 1, 3));
        
        builder.add(this.xpathLabel, cc.xy(1, 3));
        builder.add(this.xpathArea, cc.xyw(3, 3, 3));
        
        builder.addSeparator("", cc.xyw(1, 5, 5));
        
        builder.add(new JButton(new CancelDialogAction(this)), cc.xy(3, 7));
        builder.add(new JButton(new CloseDialogWrapperAction(
                action, this)), cc.xy(5, 7));
        
        this.setContentPane(builder.getPanel());
    }
    
    protected abstract String getPath(Object target);
    
    protected abstract void setupAction(Action actionIn, Object target, String xpath);
    
}
