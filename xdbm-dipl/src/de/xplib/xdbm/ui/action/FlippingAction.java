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
 * $Log: FlippingAction.java,v $
 * Revision 1.1  2005/04/12 08:34:21  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.ui.action;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;
import javax.swing.Action;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class FlippingAction extends AbstractAction implements PropertyChangeListener {
    
    private Action delegate;
    
    private Action[] delegates;
    
    public FlippingAction(final Action[] delegatesIn) {
        this.delegates = delegatesIn;
        this.delegate  = this.delegates[0];
        
        for (int i = 0; i < this.delegates.length; i++) {
            this.delegates[i].addPropertyChangeListener(this);
        }
    }
    
    /**
     * @param e
     */
    public void actionPerformed(ActionEvent e) {
        delegate.actionPerformed(e);
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
     * @param b
     * @see javax.swing.AbstractAction#setEnabled(boolean)
     */
    public void setEnabled(boolean b) {
        delegate.setEnabled(b);
    }
    
    /**
     * <Some description here>
     * 
     * @param evt
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent evt) {

        if (evt.getPropertyName().equals("enabled")) {
            
            Action tmp = this.delegate;
            
            if (evt.getNewValue().equals(Boolean.FALSE)) {
                for (int i = 0; i < this.delegates.length; i++) {
                    if (this.delegates[i].isEnabled()) {
                        this.delegate = this.delegates[i];
                        break;
                    }
                }
            } else {
                this.delegate = (Action) evt.getSource();
            }
            
            if (tmp != this.delegate) {
                this.firePropertyChange(
                        NAME, tmp.getValue(NAME), this.delegate.getValue(NAME));
                this.firePropertyChange(
                        SMALL_ICON, 
                        tmp.getValue(SMALL_ICON), 
                        this.delegate.getValue(SMALL_ICON));
                this.firePropertyChange(
                        SHORT_DESCRIPTION, 
                        tmp.getValue(SHORT_DESCRIPTION), 
                        this.delegate.getValue(SHORT_DESCRIPTION));
            }

        }
        
        this.firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
    }
}
