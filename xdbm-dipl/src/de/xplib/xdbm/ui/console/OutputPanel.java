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
 * $Log: OutputPanel.java,v $
 * Revision 1.1  2005/04/12 08:34:23  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.ui.console;

import java.awt.Color;
import java.awt.GridLayout;
import java.io.PrintStream;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import org.xmldb.api.base.Database;
import org.xmldb.api.base.XMLDBException;

import de.xplib.xdbm.ui.Application;
import de.xplib.xdbm.ui.widgets.InternalFrame;
import de.xplib.xdbm.ui.widgets.InternalFramePanel;
import de.xplib.xdbm.util.I18N;
import de.xplib.xdbm.util.I18NObserver;
import de.xplib.xdbm.util.XmldbObserver;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class OutputPanel 
    extends InternalFramePanel
    implements I18NObserver, XmldbObserver {
    
    /**
     * Comment for <code>app</code>
     */
    private final Application app = Application.getInstance();
    
    /**
     * Comment for <code>i18n</code>
     */
    private final I18N i18n = I18N.getInstance();
    
    /**
     * Comment for <code>textPane</code>
     */
    private JTextPane textPane = new JTextPane();
    
    
    //private 
    
    public OutputPanel() {
        super("");
        
        this.setLayout(new GridLayout(1, 1));
        
        JScrollPane jsp = new JScrollPane();
        jsp.getViewport().add(this.textPane);
        
        this.add(jsp);
        
        this.icon = Application.createIcon("icon/icon.output.png");
        
        StyledDocument doc = this.textPane.getStyledDocument();
        
        SimpleAttributeSet out = new SimpleAttributeSet();
        out.addAttribute(StyleConstants.Foreground, Color.BLUE);
        Application.out = new PrintStream(new OutputPanelStream(
                this, doc, out));
        
        SimpleAttributeSet err = new SimpleAttributeSet();
        err.addAttribute(StyleConstants.Foreground, Color.RED);
        Application.err = new PrintStream(new OutputPanelStream(
                this, doc, err));
        
        i18n.addObserver(this);
        this.updateI18N(i18n);
        
        this.app.addObserver(this);
    }    
    
    /**
     * <Some description here>
     * 
     * @param iFrameIn
     * @see de.xplib.xdbm.ui.InternalPanel#update(
     *      de.xplib.xdbm.ui.widgets.InternalFrame)
     */
    public void update(final InternalFrame iFrameIn) {
        iFrameIn.setTitle(this.title);
    }
    
    /**
     * <Some description here>
     * 
     * @param i18nIn
     * @see de.xplib.xdbm.util.I18NObserver#updateI18N(de.xplib.xdbm.util.I18N)
     */
    public void updateI18N(final I18N i18nIn) {
        this.title = i18nIn.getTitle("app.output.title"); 
    }
    
    
    /**
     * <Some description here>
     * 
     * @param xmldbIn
     * @see de.xplib.xdbm.util.XmldbObserver#update(org.xmldb.api.base.Database)
     */
    public void update(final Database xmldbIn) {
        if (xmldbIn == null) {
            Application.out.println(
                    this.i18n.getText("app.output.msg.disconnect"));
        } else {
            try {
                Application.out.println(
                        this.i18n.getText("app.output.msg.connect", 
                             new String[] {
                                xmldbIn.getClass().getName(),
                                xmldbIn.getName(),
                                xmldbIn.getConformanceLevel()}));
            } catch (XMLDBException e) {
                e.printStackTrace(Application.err);
            }
        }
    }
}
