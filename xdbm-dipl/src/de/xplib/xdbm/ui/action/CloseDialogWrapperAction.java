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
 * $Log: CloseDialogWrapperAction.java,v $
 * Revision 1.1  2005/04/12 08:34:21  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.ui.action;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import de.xplib.xdbm.ui.Application;
import de.xplib.xdbm.util.I18N;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class CloseDialogWrapperAction extends AbstractAction {
    
    private Action delegate;
    
    private JDialog dialog;
    
    private I18N i18n = I18N.getInstance();

    /**
     * 
     */
    public CloseDialogWrapperAction(final Action delegateIn, final JDialog dialogIn) {
        super();

        this.delegate = delegateIn;
        this.dialog   = dialogIn;
    }
    
    /**
     * @param e
     */
    public void actionPerformed(ActionEvent e) {
        delegate.actionPerformed(e);
        if (delegate instanceof I18NErrorAction) {
            if (!((I18NErrorAction) delegate).hasError()) {
                dialog.dispose();
            } else if (((I18NErrorAction) delegate).getMessage() != null) {
                JOptionPane.showMessageDialog(
                        Application.getInstance(), 
                        ((I18NErrorAction) delegate).getMessage(), 
                        this.i18n.getTitle("app.err.title"), 
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            dialog.dispose();
        }
    }
    
    /**
     * <Some description here>
     * 
     * @param listener
     * @see javax.swing.AbstractAction#addPropertyChangeListener(java.beans.PropertyChangeListener)
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        delegate.addPropertyChangeListener(listener);
    }
    /**
     * <Some description here>
     * 
     * @param obj
     * @return
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        return delegate.equals(obj);
    }
    /**
     * <Some description here>
     * 
     * @param key
     * @return
     * @see javax.swing.AbstractAction#getValue(java.lang.String)
     */
    public Object getValue(String key) {
        return delegate.getValue(key);
    }
    
    /**
     * <Some description here>
     * 
     * @return
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return delegate.hashCode();
    }
    
    /**
     * <Some description here>
     * 
     * @return
     * @see javax.swing.AbstractAction#isEnabled()
     */
    public boolean isEnabled() {
        return delegate.isEnabled();
    }
    /**
     * <Some description here>
     * 
     * @param key
     * @param value
     * @see javax.swing.AbstractAction#putValue(java.lang.String, java.lang.Object)
     */
    public void putValue(String key, Object value) {
        delegate.putValue(key, value);
    }
    /**
     * <Some description here>
     * 
     * @param listener
     * @see javax.swing.AbstractAction#removePropertyChangeListener(java.beans.PropertyChangeListener)
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        delegate.removePropertyChangeListener(listener);
    }
    /**
     * <Some description here>
     * 
     * @param b
     * @see javax.swing.AbstractAction#setEnabled(boolean)
     */
    public void setEnabled(boolean b) {
        delegate.setEnabled(b);
    }
    /**
     * <Some description here>
     * 
     * @return
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return delegate.toString();
    }
}
