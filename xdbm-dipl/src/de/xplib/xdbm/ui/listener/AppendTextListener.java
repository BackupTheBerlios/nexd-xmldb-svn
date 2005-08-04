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
 * $Log: AppendTextListener.java,v $
 * Revision 1.1  2005/04/12 08:34:23  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.ui.listener;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.text.JTextComponent;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class AppendTextListener extends KeyAdapter {
    
    private JTextComponent jtcInput;
    
    private JTextComponent jtcOutput;
    
    private String baseInput;
    
    private Thread errThread;
    
    private String txt;
    
    

    /**
     * 
     */
    public AppendTextListener(final JTextComponent jtcInputIn,
                              final JTextComponent jtcOutputIn) {
        this(jtcInputIn, jtcOutputIn, "", null);
    }

    /**
     * 
     */
    public AppendTextListener(final JTextComponent jtcInputIn,
                              final JTextComponent jtcOutputIn,
                              final String baseInputIn) {
        this(jtcInputIn, jtcOutputIn, baseInputIn, null);
    }

    /**
     * 
     */
    public AppendTextListener(final JTextComponent jtcInputIn,
                              final JTextComponent jtcOutputIn,
                              final String baseInputIn,
                              final Thread errThreadIn) {
        super();

        this.jtcInput  = jtcInputIn;
        this.jtcOutput = jtcOutputIn;
        this.baseInput = baseInputIn;
        this.errThread = errThreadIn;
    }

    public void keyReleased(final KeyEvent ke) {
        this.update();
    }
    
    public void keyPressed(final KeyEvent ke) {
        this.update();
    }

    public void keyTyped(final KeyEvent ke) {
        this.update();
    }
    
    private void update() {
        String s = this.jtcInput.getText().trim();
        if (s.equals("")) {
            this.jtcOutput.setText(this.baseInput);
        } else {
            this.jtcOutput.setText(this.baseInput + s);
            
            if (this.errThread != null) {
                this.errThread.interrupt();
                this.errThread.start();
            }
        }
    }

}
