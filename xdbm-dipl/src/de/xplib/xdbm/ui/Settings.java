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
 * $Log: Settings.java,v $
 * Revision 1.1  2005/04/12 08:34:21  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.ui;

import javax.swing.LookAndFeel;

import com.jgoodies.looks.BorderStyle;
import com.jgoodies.looks.FontSizeHints;
import com.jgoodies.looks.HeaderStyle;
import com.jgoodies.looks.LookUtils;
import com.jgoodies.looks.Options;
import com.jgoodies.looks.plastic.Plastic3DLookAndFeel;
import com.jgoodies.looks.plastic.PlasticLookAndFeel;
import com.jgoodies.looks.plastic.PlasticTheme;
import com.jgoodies.looks.plastic.theme.SkyBluerTahoma;

/** 
 * Describes most of the optional settings of the JGoodies Looks.
 * Used by the <code>DemoFrame</code> to configure the UI.
 * 
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 * 
 * @see     com.jgoodies.looks.BorderStyle
 * @see     com.jgoodies.looks.FontSizeHints
 * @see     com.jgoodies.looks.HeaderStyle
 * @see     com.jgoodies.looks.Options
 */
public final class Settings {
    
    /**
     * Comment for <code>selectedLookAndFeel</code>
     */
    private LookAndFeel selectedLookAndFeel;
    
    /**
     * Comment for <code>selectedTheme</code>
     */
    private PlasticTheme selectedTheme;
    
    /**
     * Comment for <code>useSystemFonts</code>
     */
    private Boolean useSystemFonts;
    
    /**
     * Comment for <code>fontSizeHints</code>
     */
    private FontSizeHints fontSizeHints;
    
    /**
     * Comment for <code>useNarrowButtons</code>
     */
    private boolean useNarrowButtons;
    
    /**
     * Comment for <code>tabIconsEnabled</code>
     */
    private boolean tabIconsEnabled;
    
    /**
     * Comment for <code>popupDropShadowEnabled</code>
     */
    private Boolean popupDropShadowEnabled;
    
    /**
     * Comment for <code>plasticTabStyle</code>
     */
    private String plasticTabStyle;
    
    /**
     * Comment for <code>plasticHighContrastFocusEnabled</code>
     */
    private boolean plasticHighContrastFocusEnabled;
    
    /**
     * Comment for <code>menuBarHeaderStyle</code>
     */
    private HeaderStyle menuBarHeaderStyle;
    
    /**
     * Comment for <code>menuBarPlasticBorderStyle</code>
     */
    private BorderStyle menuBarPlasticBorderStyle;
    
    /**
     * Comment for <code>menuBarWindowsBorderStyle</code>
     */
    private BorderStyle menuBarWindowsBorderStyle;
    
    /**
     * Comment for <code>menuBar3DHint</code>
     */
    private Boolean menuBar3DHint;
    
    /**
     * Comment for <code>toolBarHeaderStyle</code>
     */
    private HeaderStyle toolBarHeaderStyle;
    
    /**
     * Comment for <code>toolBarPlasticBorderStyle</code>
     */
    private BorderStyle toolBarPlasticBorderStyle;
    
    /**
     * Comment for <code>toolBarWindowsBorderStyle</code>
     */
    private BorderStyle toolBarWindowsBorderStyle;
    
    /**
     * Comment for <code>toolBar3DHint</code>
     */
    private Boolean toolBar3DHint;
    
    
    // Instance Creation ******************************************************
    
    /**
     * Consatrutor.
     */
    private Settings() {
        // Override default constructor; prevents instantiability.
    }
    
    /**
     * GoF Factory method.
     * 
     * @return A <code>Settings</code> instance.
     */
    public static Settings createDefault() {
        Settings settings = new Settings();
        //settings.setSelectedLookAndFeel(new PlasticXPLookAndFeel());
        settings.setSelectedLookAndFeel(new Plastic3DLookAndFeel());
        //settings.setSelectedTheme(PlasticLookAndFeel.createMyDefaultTheme());
        settings.setSelectedTheme(new SkyBluerTahoma());
        settings.setUseSystemFonts(Boolean.TRUE);
        settings.setFontSizeHints(FontSizeHints.MIXED);
        settings.setUseNarrowButtons(false);
        settings.setTabIconsEnabled(true);
        settings.setPlasticTabStyle(PlasticLookAndFeel.TAB_STYLE_DEFAULT_VALUE);
        settings.setPlasticHighContrastFocusEnabled(false);
        settings.setMenuBarHeaderStyle(null);
        settings.setMenuBarPlasticBorderStyle(null);
        settings.setMenuBarWindowsBorderStyle(null);
        settings.setMenuBar3DHint(Boolean.TRUE);
        settings.setToolBarHeaderStyle(null);
        settings.setToolBarPlasticBorderStyle(null);
        settings.setToolBarWindowsBorderStyle(null);
        settings.setToolBar3DHint(Boolean.TRUE);
        settings.setPopupDropShadowEnabled(Boolean.TRUE);
        
        String name = LookUtils.IS_OS_WINDOWS_XP
        	? Options.getCrossPlatformLookAndFeelClassName()
        	: Options.getSystemLookAndFeelClassName();
        
        try {
            settings.setSelectedLookAndFeel(
                    (LookAndFeel) Class.forName(name).newInstance());
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        
        return settings;
    }
    
    
    // Accessors **************************************************************
    
    /**
     * @return ..
     */
    public FontSizeHints getFontSizeHints() {
        return fontSizeHints;
    }
    
    /**
     * @param fontSizeHintsIn ..
     */
    public void setFontSizeHints(final FontSizeHints fontSizeHintsIn) {
        this.fontSizeHints = fontSizeHintsIn;
    }
    
    /**
     * @return ..
     */
    public Boolean getMenuBar3DHint() {
        return menuBar3DHint;
    }
    
    /**
     * @param menuBar3DHintIn ..
     */
    public void setMenuBar3DHint(final Boolean menuBar3DHintIn) {
        this.menuBar3DHint = menuBar3DHintIn;
    }
    
    /**
     * @return ..
     */
    public HeaderStyle getMenuBarHeaderStyle() {
        return menuBarHeaderStyle;
    }
    
    /**
     * @param menuBarHeaderStyleIn ..
     */
    public void setMenuBarHeaderStyle(final HeaderStyle menuBarHeaderStyleIn) {
        this.menuBarHeaderStyle = menuBarHeaderStyleIn;
    }
    
    /**
     * @return ..
     */
    public BorderStyle getMenuBarPlasticBorderStyle() {
        return menuBarPlasticBorderStyle;
    }
    
    /**
     * @param menuBarPlasticBorderStyleIn ..
     */
    public void setMenuBarPlasticBorderStyle(
            final BorderStyle menuBarPlasticBorderStyleIn) {
        
        this.menuBarPlasticBorderStyle = menuBarPlasticBorderStyleIn;
    }
    
    /**
     * @return ..
     */
    public BorderStyle getMenuBarWindowsBorderStyle() {
        return menuBarWindowsBorderStyle;
    }
    
    /**
     * @param menuBarWindowsBorderStyleIn ..
     */
    public void setMenuBarWindowsBorderStyle(
            final BorderStyle menuBarWindowsBorderStyleIn) {
        
        this.menuBarWindowsBorderStyle = menuBarWindowsBorderStyleIn;
    }
    
    /**
     * @return ..
     */
    public Boolean isPopupDropShadowEnabled() {
        return popupDropShadowEnabled;
    }
    
    /**
     * @param popupDropShadowEnabledIn ..
     */
    public void setPopupDropShadowEnabled(
            final Boolean popupDropShadowEnabledIn) {
        this.popupDropShadowEnabled = popupDropShadowEnabledIn;
    }
    
    /**
     * @return ..
     */
    public boolean isPlasticHighContrastFocusEnabled() {
        return plasticHighContrastFocusEnabled;
    }
    
    /**
     * @param plasticHighContrastFocusEnabledIn ..
     */
    public void setPlasticHighContrastFocusEnabled(
            final boolean plasticHighContrastFocusEnabledIn) {
        
        this.plasticHighContrastFocusEnabled = 
            plasticHighContrastFocusEnabledIn;
    }
    
    /**
     * @return ..
     */
    public String getPlasticTabStyle() {
        return plasticTabStyle;
    }
    
    /**
     * @param plasticTabStyleIn ..
     */
    public void setPlasticTabStyle(final String plasticTabStyleIn) {
        this.plasticTabStyle = plasticTabStyleIn;
    }
    
    /**
     * @return ..
     */
    public LookAndFeel getSelectedLookAndFeel() {
        return selectedLookAndFeel;
    }
    
    /**
     * @param selectedLookAndFeelIn ..
     */
    public void setSelectedLookAndFeel(
            final LookAndFeel selectedLookAndFeelIn) {
        
        this.selectedLookAndFeel = selectedLookAndFeelIn;
    }
    
    /**
     * @return ..
     */
    public PlasticTheme getSelectedTheme() {
        return selectedTheme;
    }
    
    /**
     * @param selectedThemeIn ..
     */
    public void setSelectedTheme(final PlasticTheme selectedThemeIn) {
        this.selectedTheme = selectedThemeIn;
    }
    
    /**
     * @return ..
     */
    public boolean isTabIconsEnabled() {
        return tabIconsEnabled;
    }
    
    /**
     * @param tabIconsEnabledIn ..
     */
    public void setTabIconsEnabled(final boolean tabIconsEnabledIn) {
        this.tabIconsEnabled = tabIconsEnabledIn;
    }
    
    /**
     * @return ..
     */
    public Boolean getToolBar3DHint() {
        return toolBar3DHint;
    }
    
    /**
     * @param toolBar3DHintIn ..
     */
    public void setToolBar3DHint(final Boolean toolBar3DHintIn) {
        this.toolBar3DHint = toolBar3DHintIn;
    }
    
    /**
     * @return ..
     */
    public HeaderStyle getToolBarHeaderStyle() {
        return toolBarHeaderStyle;
    }
    
    /**
     * @param toolBarHeaderStyleIn ..
     */
    public void setToolBarHeaderStyle(final HeaderStyle toolBarHeaderStyleIn) {
        this.toolBarHeaderStyle = toolBarHeaderStyleIn;
    }
    
    /**
     * @return ..
     */
    public BorderStyle getToolBarPlasticBorderStyle() {
        return toolBarPlasticBorderStyle;
    }
    
    /**
     * @param toolBarPlasticBorderStyleIn ..
     */
    public void setToolBarPlasticBorderStyle(
            final BorderStyle toolBarPlasticBorderStyleIn) {
        
        this.toolBarPlasticBorderStyle = toolBarPlasticBorderStyleIn;
    }
    
    /**
     * @return ..
     */
    public BorderStyle getToolBarWindowsBorderStyle() {
        return toolBarWindowsBorderStyle;
    }
    
    /**
     * @param toolBarWindowsBorderStyleIn ..
     */
    public void setToolBarWindowsBorderStyle(
            final BorderStyle toolBarWindowsBorderStyleIn) {
        
        this.toolBarWindowsBorderStyle = toolBarWindowsBorderStyleIn;
    }
    
    /**
     * @return ..
     */
    public boolean isUseNarrowButtons() {
        return useNarrowButtons;
    }
    
    /**
     * @param useNarrowButtonsIn ..
     */
    public void setUseNarrowButtons(final boolean useNarrowButtonsIn) {
        this.useNarrowButtons = useNarrowButtonsIn;
    }
    
    /**
     * @return ..
     */
    public Boolean isUseSystemFonts() {
        return useSystemFonts;
    }
    
    /**
     * @param useSystemFontsIn ..
     */
    public void setUseSystemFonts(final Boolean useSystemFontsIn) {
        this.useSystemFonts = useSystemFontsIn;
    }
    
}