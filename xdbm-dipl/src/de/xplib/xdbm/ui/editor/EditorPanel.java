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
 * $Log: EditorPanel.java,v $
 * Revision 1.1  2005/04/12 08:34:24  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.ui.editor;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JToolBar;

import de.xplib.xdbm.ui.Application;
import de.xplib.xdbm.ui.action.NewXMLDocument;
import de.xplib.xdbm.ui.model.UIObject;
import de.xplib.xdbm.ui.model.VCLSchemaObject;
import de.xplib.xdbm.ui.model.XMLResourceObject;
import de.xplib.xdbm.ui.widgets.InternalFramePanel;
import de.xplib.xdbm.ui.widgets.JToolBarButton;
import de.xplib.xdbm.ui.widgets.syntax.JEditTextArea;
import de.xplib.xdbm.ui.widgets.syntax.XMLTokenMarker;
import de.xplib.xdbm.util.I18N;
import de.xplib.xdbm.util.I18NObserver;
import de.xplib.xdbm.util.UIObjectObserver;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class EditorPanel 
    extends InternalFramePanel
    implements I18NObserver, UIObjectObserver {
    
    private JEditTextArea textArea = new JEditTextArea();

    /**
     * @param titleIn
     */
    public EditorPanel(Application appIn) {
        super("");
        
        I18N i18n = I18N.getInstance();
        i18n.addObserver(this);
        this.updateI18N(i18n);
        
        this.setEnabled(false);
        
        appIn.addObserver(this);
        
        this.initUI();
    }
    
    private void initUI() {
        this.setLayout(new GridLayout(1, 1));
        
        this.textArea.setTokenMarker(new XMLTokenMarker());
        this.textArea.setFont(new Font("Courier", Font.PLAIN, 10));
        
        this.add(this.textArea);
        
        JToolBarButton jtb = new JToolBarButton(new NewXMLDocument(this.textArea));
        Dimension dim = new Dimension(20, 20);
        
        jtb.setSize(dim);
        jtb.setPreferredSize(dim);
        jtb.setMaximumSize(dim);
        jtb.setMinimumSize(dim);
        
        this.toolBar = new JToolBar();
        this.toolBar.add(jtb);
    }
    
    public void setText(final String textIn) {
        this.textArea.setText(textIn);
        this.textArea.setCaretPosition(0);
        this.focus();
    }
    
    public void setEditable(final boolean editableIn) {
        this.textArea.setEditable(editableIn);
    }

    /**
     * <Some description here>
     * 
     * @param i18nIn
     * @see de.xplib.xdbm.util.I18NObserver#updateI18N(de.xplib.xdbm.util.I18N)
     */
    public void updateI18N(I18N i18nIn) {

        this.title = i18nIn.getTitle("editor.title");
    }
    
    /**
     * <Some description here>
     * 
     * @param objectIn
     * @see de.xplib.xdbm.util.UIObjectObserver#update(
     *      de.xplib.xdbm.ui.model.UIObject)
     */
    public void update(UIObject objectIn) {
        if (objectIn instanceof VCLSchemaObject) {
            this.textArea.setText(((VCLSchemaObject) objectIn).getContent());
            this.setEnabled(true);
            this.focus();
        } else if (objectIn instanceof XMLResourceObject) {
            this.textArea.setText(((XMLResourceObject) objectIn).getContent());
            this.setEnabled(true);
        } else {
            this.textArea.setText("");
            this.setEnabled(false);
        }
    }
}
