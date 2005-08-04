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
 * $Log: OutputPanelStream.java,v $
 * Revision 1.1  2005/04/12 08:34:23  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.ui.console;

import java.io.OutputStream;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class OutputPanelStream extends OutputStream {
    
    /**
     * Comment for <code>panel</code>
     */
    private OutputPanel panel;
    
    /**
     * Comment for <code>doc</code>
     */
    private StyledDocument doc;
    
    /**
     * Comment for <code>set</code>
     */
    private AttributeSet set;
    
    /**
     * @param panelIn ..
     * @param docIn ..
     * @param setIn ..
     */
    public OutputPanelStream(final OutputPanel panelIn,
                             final StyledDocument docIn,
                             final AttributeSet setIn) {
        
        this.panel = panelIn;
        this.doc = docIn;
        this.set = setIn;
    }

    /**
     * <Some description here>
     * 
     * @param byteIn
     * @see java.io.OutputStream#write(int)
     */
    public void write(final int byteIn) {
        
        try {
            this.doc.insertString(
                    this.doc.getLength(), 
                    String.valueOf((char) byteIn),
                    this.set);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
}
