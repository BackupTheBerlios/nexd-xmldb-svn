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
 * $Log: ExecuteProgressThread.java,v $
 * Revision 1.1  2005/04/12 08:34:20  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.util;

import javax.swing.JDialog;

import de.xplib.xdbm.ui.dialog.StandardDialogFactory;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public abstract class ExecuteProgressThread extends Thread {
    
    private final JDialog dialog;

    /**
     * 
     */
    public ExecuteProgressThread(JDialog dialogIn) {
        super();

        this.dialog = dialogIn;
    }

    /**
     * <Some description here>
     * 
     * @see java.lang.Runnable#run()
     */
    public final void run() {

        Thread t = new Thread() {
            public void run() {
                dialog.setVisible(true);
            }
        };
        t.start();
        
        try {
            while (!dialog.isVisible()) {
                Thread.sleep(50);
            }
            
            long start = System.currentTimeMillis();
            
            this.doRun();
            
            long end = System.currentTimeMillis();
            
            dialog.dispose();
            t.interrupt();
            
            StandardDialogFactory.showTimeNeededDialog(end - start);
            
            this.doPostRun();
        } catch (Exception e) {
            
            e.printStackTrace();
            
            dialog.dispose();
            t.interrupt();
            StandardDialogFactory.showErrorMessageDialog(e);
        }
    }
    
    
    /**
     * 
     */
    protected abstract void doRun() throws Exception;
    
    /**
     * 
     */
    protected abstract void doPostRun() throws Exception;

}
