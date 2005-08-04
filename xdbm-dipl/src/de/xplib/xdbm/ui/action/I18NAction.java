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
 * $Log: I18NAction.java,v $
 * Revision 1.1  2005/04/12 08:34:21  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.ui.action;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import de.xplib.xdbm.util.I18N;
import de.xplib.xdbm.util.I18NObserver;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public abstract class I18NAction 
    extends AbstractAction 
    implements I18NObserver {
    
    protected final I18N i18n;
    
    /**
     * Comment for <code>key</code>
     */
    private final String key;

    /**
     * 
     */
    public I18NAction(final String keyIn) {
        
        this.key = keyIn;
        
        this.i18n = I18N.getInstance();
        i18n.addObserver(this);
        this.updateI18N(this.i18n);
    }
    
    /**
     * <Some description here>
     * 
     * @param i18nIn
     * @see de.xplib.xdbm.util.I18NObserver#updateI18N(I18N)
     */
    public void updateI18N(final I18N i18nIn) {
        this.putValue(Action.NAME, i18nIn.getTitle(this.key));
        this.putValue(Action.SHORT_DESCRIPTION, i18nIn.getToolTip(this.key));
        this.putValue(Action.MNEMONIC_KEY, i18nIn.getMnemonic(this.key));
        
        KeyStroke ks = i18nIn.getAccelerator(this.key);
        if (ks != null) {
            
            
            
            this.putValue(Action.ACCELERATOR_KEY, ks);
        }
        //String 
    }

}
