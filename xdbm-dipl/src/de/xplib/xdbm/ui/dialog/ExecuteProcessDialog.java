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
 * $Log: ExecuteProcessDialog.java,v $
 * Revision 1.1  2005/04/12 08:34:22  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.ui.dialog;

import javax.swing.JLabel;
import javax.swing.JProgressBar;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import de.xplib.xdbm.util.I18N;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class ExecuteProcessDialog extends AbstractDialog {
    
    private JLabel message = new JLabel();
    
    private JProgressBar jpBar = new JProgressBar();
    
    public ExecuteProcessDialog(final String prefixIn) {
        super("", true);
        
        this.initI18N(prefixIn);
        this.initUI();
        
        this.pack();
        this.setResizable(false);
    }
    
    /**
     * 
     */
    private void initI18N(final String prefixIn) {
        I18N i18n = I18N.getInstance();
        
        this.setTitle(i18n.getTitle(prefixIn + ".title"));
        this.message.setText(i18n.getText(prefixIn + ".message"));
    }
    
    /**
     * 
     */
    private void initUI() {
        this.jpBar.setIndeterminate(true);
        
        FormLayout layout = new FormLayout(
                "90px, 3dlu, 90px, 3dlu, 90px", 
                "20dlu, 3dlu, p, 3dlu, p, 9dlu, p");
        
        layout.setColumnGroups(new int[][]{{1, 3, 5}});
        
        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();
        
        CellConstraints cc = new CellConstraints();
        
        builder.add(this.message, cc.xyw(1, 1, 5));
        builder.add(this.jpBar, cc.xyw(3, 3, 3));
        
        this.setContentPane(builder.getPanel());
    }
}
