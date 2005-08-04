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
 * $Log: ApplicationToolBars.java,v $
 * Revision 1.1  2005/04/12 08:34:21  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.ui;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JToolBar;

import org.tigris.swidgets.DockLayout;
import org.tigris.toolbar.ToolBarFactory;

import com.jgoodies.looks.Options;
import com.jgoodies.looks.plastic.PlasticLookAndFeel;
import com.jgoodies.looks.windows.WindowsLookAndFeel;

import de.xplib.xdbm.ui.action.CopyResourceAction;
import de.xplib.xdbm.ui.action.CutResourceAction;
import de.xplib.xdbm.ui.action.DeleteCollectionAction;
import de.xplib.xdbm.ui.action.DeleteResourceAction;
import de.xplib.xdbm.ui.action.FlippingAction;
import de.xplib.xdbm.ui.action.NewCollectionAction;
import de.xplib.xdbm.ui.action.NewVirtualCollectionAction;
import de.xplib.xdbm.ui.action.NewXMLResourceAction;
import de.xplib.xdbm.ui.action.PasteResourceAction;
import de.xplib.xdbm.ui.action.SaveResourceAction;
import de.xplib.xdbm.ui.widgets.JToolBarButton;
import de.xplib.xdbm.ui.widgets.PopupBoxButton;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class ApplicationToolBars {

    /**
     * Comment for <code>app</code>
     */
    private final Application app;
    
    private final Settings settings;
    
    /**
     * @param appIn The main frame.
     */
    public ApplicationToolBars(final Application appIn) {
        this.app = appIn;
        
        this.settings = appIn.getSettings();
        
        this.initUI();
    }
    
    private void initUI() {
        
        PopupBoxButton newButton = new PopupBoxButton(
                NewCollectionAction.INSTANCE, 3, 1);
        newButton.setFocusable(false);
        newButton.add(NewVirtualCollectionAction.INSTANCE);
        newButton.add(NewXMLResourceAction.INSTANCE);
        
        JToolBarButton saveButton = new JToolBarButton(SaveResourceAction.INSTANCE);
        
        this.app.getContentPane().add(
                DockLayout.NORTH, createToolBar(
                        new JButton[] {newButton, saveButton}));
        
        Action[] editActions = new Action[] {
                CutResourceAction.INSTANCE,
                CopyResourceAction.INSTANCE,
                PasteResourceAction.INSTANCE,
                new FlippingAction(new Action[] {
                        DeleteResourceAction.INSTANCE, 
                        DeleteCollectionAction.INSTANCE})
        };
        
        this.app.getContentPane().add(
                DockLayout.NORTH, createToolBar(editActions));
    }
    
    public JToolBar createToolBar(final Action[] actions) {
        
        JToolBarButton[] buttons =  new JToolBarButton[actions.length];
        for (int i = 0; i < actions.length; i++) {
            buttons[i] = new JToolBarButton(actions[i]);
        }
        
        return this.createToolBar(buttons);
    }
    
    private JToolBar createToolBar(JButton button) {
        
        return this.createToolBar(new JButton[] {button});
    }
    
    private JToolBar createToolBar(JButton[] buttons) {
        JToolBar bar = ToolBarFactory.createToolBar(false, buttons, true);
        
        bar.putClientProperty("JToolBar.isRollover", Boolean.TRUE);
        bar.putClientProperty(
                Options.HEADER_STYLE_KEY,
                settings.getToolBarHeaderStyle());
        bar.putClientProperty(
                PlasticLookAndFeel.BORDER_STYLE_KEY,
                settings.getToolBarPlasticBorderStyle());
        bar.putClientProperty(
                WindowsLookAndFeel.BORDER_STYLE_KEY,
                settings.getToolBarWindowsBorderStyle());
        bar.putClientProperty(
                PlasticLookAndFeel.IS_3D_KEY,
                settings.getToolBar3DHint());
        bar.setFloatable(true);
        
        return bar;
    }
}
