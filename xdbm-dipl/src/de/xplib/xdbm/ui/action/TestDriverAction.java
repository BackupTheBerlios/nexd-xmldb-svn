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
 * $Log: TestDriverAction.java,v $
 * Revision 1.1  2005/04/12 08:34:21  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.ui.action;

import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import javax.swing.AbstractAction;

import org.sixdml.SixdmlDatabase;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.ErrorCodes;
import org.xmldb.api.base.Service;
import org.xmldb.api.base.XMLDBException;

import de.xplib.xdbm.ui.Application;
import de.xplib.xdbm.ui.dialog.TestDriverDialog;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class TestDriverAction extends AbstractAction {
    
    /**
     * Comment for <code>owner</code>
     */
    private Dialog owner = null;
    
    /**
     * Comment for <code>jarFile</code>
     */
    private String jar = null;
    
    /**
     * Comment for <code>className</code>
     */
    private String className = null;
    
    /**
     * Comment for <code>dialog</code>
     */
    private TestDriverDialog dialog;
    
    /**
     * 
     */
    public TestDriverAction() {
        
    }
    
    /**
     * @param jarIn ..
     * @param classNameIn ..
     */
    public TestDriverAction(final String jarIn, final String classNameIn) {
        this(null, jarIn, classNameIn);
    }
    
    /**
     * @param ownerIn ...
     * @param jarIn ..
     * @param classNameIn ..
     */
    public TestDriverAction(final Dialog ownerIn, 
                            final String jarIn, 
                            final String classNameIn) {
        
        this.owner     = ownerIn;
        this.jar       = jarIn;
        this.className = classNameIn;
    }

    /**
     * <Some description here>
     * 
     * @param aeIn
     * @see java.awt.event.ActionListener#actionPerformed(
     *      java.awt.event.ActionEvent)
     */
    public void actionPerformed(final ActionEvent aeIn) {
        
        if (this.owner == null) {
            dialog = new TestDriverDialog(Application.getInstance());
        } else {
            dialog = new TestDriverDialog(this.owner);
        }
        
        Thread t = new Thread() {
            public void run() {
                dialog.setVisible(true);
            }
        };
        t.start();
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e3) {
            // TODO Auto-generated catch block
            e3.printStackTrace();
        }
        
        Class clazz = null;
        try {
            
            dialog.addTest("Load class " + this.className);
            
            if (!this.jar.startsWith("file://")) {
                this.jar = "file://" + this.jar;
            }
            
            URLClassLoader ucl = new URLClassLoader(
                    new URL[] {new URL(this.jar)});
            
            clazz = ucl.loadClass(this.className);
            
            dialog.addTestResult(true);
            
        } catch (ClassNotFoundException e) {
            dialog.addTestResult(false);
            dialog.addTestError(e.getMessage());
            dialog.addTestResult(false);
        } catch (MalformedURLException e) {
            dialog.addTestError(e.getMessage());
        }
        
        Object o = null;
        if (clazz != null) {
            
            try {
                dialog.addTest("Create instance of " + this.className);
                o = clazz.newInstance();
                dialog.addTestResult(true);
            } catch (InstantiationException e) {
                dialog.addTestResult(false);
                dialog.addTestError(e.getMessage());
            } catch (IllegalAccessException e) {
                dialog.addTestResult(false);
                dialog.addTestError(e.getMessage());
            }
        }
        
        if (o == null) {
            return;
        }
        
        dialog.addTest("Test instance of org.xmldb.api.Database");
        if (o instanceof Database) {
            dialog.addTestResult(true);
        } else {
            dialog.addTestResult(false);
        }
        
        dialog.addTest("Test instance of org.sixdml.SixdmlDatabase");
        if (o instanceof SixdmlDatabase) {
            dialog.addTestResult(true);
            
            
            SixdmlDatabase sdb = (SixdmlDatabase) o;
            dialog.addTest("Test SixdmlDatabase.getServices");
            try {
                Service[] services = sdb.getServices();
                dialog.addTestResult(true);
                
                for (int i = 0; i < services.length; i++) {
                    String name    = services[i].getName();
                    String version = services[i].getVersion();
                    
                    dialog.addTest("Test SixdmlDatabase.getService('" + name + "', '" + version + "')");
                    try {
                        Service s = sdb.getService(name, version);
                        if (s == null) {
                            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, "Service is null");
                        }
                        dialog.addTestResult(true);
                    } catch (Exception e2) {
                        dialog.addTestResult(false);
                        dialog.addTestError(e2.getMessage());
                    }
                }
            } catch (Exception e1) {
                dialog.addTestResult(false);
                dialog.addTestError(e1.getMessage());
            }
        } else {
            dialog.addTestResult(false);
        }
        
    }

}
