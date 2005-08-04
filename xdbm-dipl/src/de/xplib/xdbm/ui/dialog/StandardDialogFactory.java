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
 * $Log: StandardDialogFactory.java,v $
 * Revision 1.1  2005/04/12 08:34:22  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.ui.dialog;

import javax.swing.JOptionPane;

import de.xplib.xdbm.ui.Application;
import de.xplib.xdbm.util.I18N;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public final class StandardDialogFactory {
    
    
    private static Application app = Application.getInstance();
    
    private static I18N i18n = I18N.getInstance();
    
    /**
     * 
     */
    private StandardDialogFactory() {
        super();
    }
    
    public static void showNoCollectionDialog() {
        JOptionPane.showMessageDialog(
                app, 
                i18n.getText("dialog.error.nocoll"), 
                i18n.getTitle("app.err.title"), 
                JOptionPane.ERROR_MESSAGE);
    }
    
    public static void showNoResourceDialog() {
        JOptionPane.showMessageDialog(
                app, 
                i18n.getText("dialog.error.nores"), 
                i18n.getTitle("app.err.title"), 
                JOptionPane.ERROR_MESSAGE);
    }
    
    public static void showNoXPathQueryDialog() {
        JOptionPane.showMessageDialog(
                app, 
                i18n.getText("dialog.error.noxpath"), 
                i18n.getTitle("app.err.title"), 
                JOptionPane.ERROR_MESSAGE);
    }

    public static void showNoNameDialog() {
        JOptionPane.showMessageDialog(
                app, 
                i18n.getText("dialog.error.noname"), 
                i18n.getTitle("app.err.title"), 
                JOptionPane.ERROR_MESSAGE);
    }
    
    public static void showSameNameDialog(final String nameIn) {
        JOptionPane.showMessageDialog(
                app, 
                i18n.getText("dialog.error.samename", new String[] {nameIn}), 
                i18n.getTitle("app.err.title"), 
                JOptionPane.ERROR_MESSAGE);
    }
    
    public static void showNoReadDialog(final String nameIn) {
        JOptionPane.showMessageDialog(
                app, 
                i18n.getText("dialog.error.noread", new String[] {nameIn}), 
                i18n.getTitle("app.err.title"), 
                JOptionPane.ERROR_MESSAGE);
    }
    
    public static void showNoVCLSchemaDialog() {
        JOptionPane.showMessageDialog(
                app, 
                i18n.getText("dialog.error.novcs"), 
                i18n.getTitle("app.err.title"), 
                JOptionPane.ERROR_MESSAGE);
    }
    
    public static void showNoXSLStylesheetDialog() {
        JOptionPane.showMessageDialog(
                app, 
                i18n.getText("dialog.error.noxsl"), 
                i18n.getTitle("app.err.title"), 
                JOptionPane.ERROR_MESSAGE);
    }
    
    public static void showNoSuchServiceDialog(final String nameIn) {
        JOptionPane.showMessageDialog(
                app, 
                i18n.getText("dialog.error.noservice", new String[] {nameIn}), 
                i18n.getTitle("app.err.title"), 
                JOptionPane.ERROR_MESSAGE);
    }
    
    public static void showErrorMessageDialog(final Exception exIn) {
        
        String msg = exIn.getMessage();
        if (msg == null || msg.trim().equals("")) {
            exIn.printStackTrace(Application.err);
            return;
        }
        
        JOptionPane.showMessageDialog(
                app, 
                i18n.getText("dialog.error.message", new String[] {exIn.getMessage()}), 
                i18n.getTitle("app.err.title"), 
                JOptionPane.ERROR_MESSAGE);
    }
    
    
    public static void showTimeNeededDialog(final long time) {
        
        float sec = time / 1000f;
        
        JOptionPane.showMessageDialog(
                app, 
                i18n.getText("dialog.message.time", new String[] {String.valueOf(sec)}), 
                i18n.getTitle("dialog.message.time"), 
                JOptionPane.INFORMATION_MESSAGE);
    }

}
