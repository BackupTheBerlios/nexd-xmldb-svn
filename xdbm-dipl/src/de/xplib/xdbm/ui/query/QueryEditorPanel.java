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
 * $Log: QueryEditorPanel.java,v $
 * Revision 1.1  2005/04/12 08:34:22  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.ui.query;

import de.xplib.xdbm.ui.Application;
import de.xplib.xdbm.ui.model.UIObject;
import de.xplib.xdbm.ui.widgets.InternalFramePanel;
import de.xplib.xdbm.util.I18N;
import de.xplib.xdbm.util.I18NObserver;
import de.xplib.xdbm.util.UIObjectObserver;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class QueryEditorPanel 
    extends InternalFramePanel 
    implements I18NObserver, UIObjectObserver {

    /**
     * @param titleIn
     */
    public QueryEditorPanel(Application appIn) {
        super("");

        I18N i18n = I18N.getInstance();
        i18n.addObserver(this);
        this.updateI18N(i18n);
        
        appIn.addObserver(this);
    }

    /**
     * <Some description here>
     * 
     * @param i18nIn
     * @see de.xplib.xdbm.util.I18NObserver#updateI18N(de.xplib.xdbm.util.I18N)
     */
    public void updateI18N(I18N i18nIn) {

        this.title = i18nIn.getTitle("plugin.query.editor.title");
    }
    
    /**
     * <Some description here>
     * 
     * @param objectIn
     * @see de.xplib.xdbm.util.UIObjectObserver#update(de.xplib.xdbm.ui.model.UIObject)
     */
    public void update(UIObject objectIn) {
        // TODO Auto-generated method stub

    }
}
