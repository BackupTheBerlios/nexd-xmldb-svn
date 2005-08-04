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
 * $Log: TestDriverDialog.java,v $
 * Revision 1.1  2005/04/12 08:34:22  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.ui.dialog;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class TestDriverDialog extends JDialog {
    
    /**
     * Comment for <code>jsp</code>
     */
    private JScrollPane jsp = new JScrollPane(); 

    /**
     * Comment for <code>jtPane</code>
     */
    private JTextPane jtPane = new JTextPane();
    
    /**
     * Comment for <code>doc</code>
     */
    private StyledDocument doc;
    
    /**
     * Comment for <code>test</code>
     */
    private SimpleAttributeSet test = new SimpleAttributeSet();
    
    /**
     * Comment for <code>status</code>
     */
    private SimpleAttributeSet success = new SimpleAttributeSet();
    
    /**
     * Comment for <code>status</code>
     */
    private SimpleAttributeSet fail = new SimpleAttributeSet();
    
    /**
     * @param ownerIn The parent Dialog.
     */
    public TestDriverDialog(final Dialog ownerIn) {
        super(ownerIn, "FooBar", true);
        this.initUI();
    }
    
    /**
     * @param ownerIn The parent Window.
     */
    public TestDriverDialog(final Frame ownerIn) {
        super(ownerIn, "FooBar", true);
        this.initUI();
    }
    
    /**
     * @param testIn ...
     */
    public void addTest(final String testIn) {
        try {
            this.doc.insertString(
                    this.doc.getLength(), testIn, this.test);
            this.jtPane.setCaretPosition(this.doc.getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * @param doneIn ..
     */
    public void addTestResult(final boolean doneIn) {
        
        AttributeSet as;
        String msg = "   ";
        if (doneIn) {
            as   = this.success;
            msg += "(+)";
        } else {
            as   = this.fail;
            msg += "(-)";
        }
        msg += "\n";
        
        try {
            this.doc.insertString(this.doc.getLength(), msg, as);
            this.jtPane.setCaretPosition(this.doc.getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * @param msgIn ...
     */
    public void addTestError(final String msgIn) {
        try {
            this.doc.insertString(
                    this.doc.getLength(), "  " + msgIn + "\n", this.fail);
            this.jtPane.setCaretPosition(this.doc.getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 
     */
    private void initUI() {
        
        this.test.addAttribute(StyleConstants.Foreground, Color.BLUE);
        this.success.addAttribute(StyleConstants.Foreground, Color.GREEN);
        this.success.addAttribute(StyleConstants.Bold, Boolean.TRUE);
        
        this.fail.addAttribute(StyleConstants.Foreground, Color.RED);
        this.fail.addAttribute(StyleConstants.Bold, Boolean.TRUE);
        
        this.getContentPane().add(this.jsp);
        
        this.jsp.getViewport().add(this.jtPane);
        
        this.doc = this.jtPane.getStyledDocument();
        
        //jtPane.getStyledDocument()
        
        this.setSize(400, 300);
        this.setResizable(false);
    }
}
