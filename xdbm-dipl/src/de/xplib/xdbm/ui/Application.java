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
 * $Log: Application.java,v $
 * Revision 1.1  2005/04/12 08:34:21  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.ui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.net.URL;
import java.util.LinkedList;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JRadioButton;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;

import org.apache.commons.collections.FastArrayList;
import org.apache.commons.i18n.XMLMessageProvider;
import org.tigris.toolbar.layouts.DockLayout;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.XMLDBException;

import com.jgoodies.looks.Options;
import com.jgoodies.looks.plastic.PlasticLookAndFeel;

import de.xplib.xdbm.ui.dialog.FirstStartSetup;
import de.xplib.xdbm.ui.model.PluginFile;
import de.xplib.xdbm.ui.model.UIObject;
import de.xplib.xdbm.ui.widgets.InternalFrame;
import de.xplib.xdbm.ui.widgets.InternalFramePanel;
import de.xplib.xdbm.ui.widgets.UIFSplitPane;
import de.xplib.xdbm.util.Config;
import de.xplib.xdbm.util.I18N;
import de.xplib.xdbm.util.UIObjectObserver;
import de.xplib.xdbm.util.XmldbObserver;

/**
 * Represents a singleton.
 * 
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public final class Application extends JFrame {

    /**
     * Holds singleton instance
     */
    private static Application instance;
    
    /**
     * Comment for <code>out</code>
     */
    public static PrintStream out = System.out;
    
    /**
     * Comment for <code>err</code>
     */
    public static PrintStream err = System.err;
    
    /**
     * Global GUI settings.
     */
    private Settings settings = Settings.createDefault();
    
    /**
     * Comment for <code>config</code>
     */
    private Config config;
    
    /**
     * Comment for <code>xmldb</code>
     */
    private Database xmldb;
    
    /**
     * Comment for <code>xmldbObservers</code>
     */
    private FastArrayList xmldbObservers = new FastArrayList();
    
    /**
     * The last selected object in the user interface.
     */
    private UIObject uiObject = null;
    
    /**
     * The observers that want to know about changes in the selected object.
     */
    private FastArrayList uiObjectObservers = new FastArrayList();
    
    /**
     * Comment for <code>uiObjectStack</code>
     */
    private LinkedList uiObjectQueue = new LinkedList();
    
    /**
     * Comment for <code>uiObjectRunning</code>
     */
    private boolean uiObjectRunning = false;
    
    /**
     * The main menu bar.
     */
    private ApplicationMenuBar menuBar;
    
    /**
     * The main container with all toolbars.
     */
    private ApplicationToolBars toolBars;
    
    /**
     * The console panel.
     */
    private BottomFrame consolePanel;
    
    /**
     * The collection tree panel.
     */
    private LeftFrame treePanel;
    
    /**
     * The frame that shows the resources and query results.
     */
    private CenterFrame resourceFrame;
    
    /**
     * Comment for <code>jspContentConsole</code>
     */
    private JSplitPane jspContentConsole = new JSplitPane();
    
    /**
     * Comment for <code>jspTreeResource</code>
     */
    private JSplitPane jspTreeResource = new JSplitPane();

    /**
     * prevents instantiation
     */
    private Application() {
        super("XML:DB - Manager");
        
        this.uiObjectObservers.setFast(true);
        this.xmldbObservers.setFast(true);
        
        this.configureUI();
                
        XMLMessageProvider.install("i18n", this.getClass().getResourceAsStream(
                "/de/xplib/xdbm/ui/i18n.xml"));
        
        this.initConfig();
        
        I18N.getInstance().setLocale(this.config.getLocale());
                
        this.initListener();
        
        
    }

    /**
     * Returns the singleton instance.
     * 
     * @return	the singleton instance
     */
    public static Application getInstance() {
        if (instance == null) {
            instance = new Application();
            instance.initUI();
        }
        return instance;
    }
    
    /**
     * Creates an <code>Icon</code> instance for the given <code>pathIn</code>.
     * This method uses <code>{@link Application#readResource(String)}</code>
     * to locate the icon.
     * 
     * @param pathIn The relative path of the icon.
     * @return The <code>Icon</code> instance.
     */
    public static Icon createIcon(final String pathIn) {
        return new ImageIcon(readResource(pathIn));
    }
    
    /**
     * Reads a resource in the classpath of this client instance, this can be 
     * the local filesystem or a jar archive.
     *  
     * @param pathIn The relative resource path.
     * @return The <code>URL</code> where the resource can be found.
     */
    public static URL readResource(final String pathIn) {
        String path = "de/xplib/xdbm/ui/res/" + pathIn;
        return Application.class.getClassLoader().getResource(path);
    }
    
    public static InternalFramePanel openPanel(final String nameIn) {
        
        Application app = Application.getInstance();
        
        PluginFile p = app.getConfig().getPluginFile(nameIn);
        InternalFramePanel ifp = null;
        try {
            Plugin plugin = p.createPlugin();
            if (plugin.hasPanel()) {
                ifp = plugin.getPanelInstance(app);
            }
        } catch (NullPointerException e) {
            e.printStackTrace(Application.err);
        } catch (FileNotFoundException e) {
            e.printStackTrace(Application.err);
        } catch (ClassCastException e) {
            e.printStackTrace(Application.err);
        } catch (ClassNotFoundException e) {
            e.printStackTrace(Application.err);
        }
        
        if (ifp != null && ifp.getParent() == null) {
            InternalFrame[] frames = Application.getInstance().getInternalFrames();
            for (int i = 0; i < frames.length; i++) {
                if (frames[i].getPosition().equals(p.getPosition())) {
                    ((JTabbedPane) frames[i].getContent()).addTab("", ifp);
                }
            }
        }
        
        return ifp;
    }
    
    public static InternalFramePanel openPanel(Plugin pluginIn) {
        
        Application app = Application.getInstance();
        
        InternalFramePanel ifp = pluginIn.getPanelInstance(app);
        
        if (ifp != null && ifp.getParent() == null) {
            InternalFrame[] frames = Application.getInstance().getInternalFrames();
            for (int i = 0; i < frames.length; i++) {
                if (frames[i].getPosition().equals(pluginIn.getPosition())) {
                    ((JTabbedPane) frames[i].getContent()).addTab("", ifp);
                }
            }
        }
        
        return ifp;
    }
    
    /**
     * <Some description here>
     * 
     * @return
     * @see java.awt.Frame#getFrames()
     */
    public InternalFrame[] getInternalFrames() {
        return new InternalFrame[] {
                this.consolePanel,
                this.treePanel,
                this.resourceFrame};
    }
    
    /**
     * @return The ui settings.
     */
    public Settings getSettings() {
        return this.settings;
    }
    
    /**
     * @return The application configuration.
     */
    public Config getConfig() {
        return this.config;
    }
    
    
    
    /**
     * @return Returns the xmldb.
     */
    public final Database getXmldb() {
        return xmldb;
    }
    
    /**
     * @param xmldbIn The xmldb to set.
     */
    public final void setXmldb(final Database xmldbIn) {
        this.xmldb = xmldbIn;
        this.notifyXmldbObservers();
        
        if (this.xmldb == null) {
            this.setUIObject(null);
        }
    }
    
    /**
     * @param observerIn The xmldbObserver instance.
     */
    public void addObserver(final XmldbObserver observerIn) {
        if (!this.xmldbObservers.contains(observerIn)) {
            this.xmldbObservers.add(observerIn);
        }
    }
    
    /**
     * @param observerIn The xmldbObserver instance.
     */
    public void removeObserver(final XmldbObserver observerIn) {
        if (this.xmldbObservers.contains(observerIn)) {
            this.xmldbObservers.remove(observerIn);
        }
    }
    
    //private UpdateThread ut = new UpdateThread();
    
    /**
     * @return The selected object in the user interface of null.
     */
    public UIObject getUIObject() {
        return this.uiObject;
    }
    
    /**
     * @param uiObjectIn The last selected object in the user interface of null.
     */
    public void setUIObject(final UIObject uiObjectIn) {

        if (this.uiObjectQueue.contains(uiObjectIn)) {
            this.uiObjectQueue.remove(uiObjectIn);
        }
        this.uiObjectQueue.addLast(uiObjectIn);
        this.notifyUIObjectObservers();
    }
    
    /**
     * @param observerIn The UIObjectObserver to add.
     */
    public void addObserver(final UIObjectObserver observerIn) {
        if (!this.uiObjectObservers.contains(observerIn)) {
            this.uiObjectObservers.add(observerIn);
        }
    }
    
    /**
     * @param observerIn The UIObjectObserver to remove.
     */
    public void removeObserver(final UIObjectObserver observerIn) {
        if (this.uiObjectObservers.contains(observerIn)) {
            this.uiObjectObservers.remove(observerIn);
        }
    }
    
    /**
     * 
     */
    protected void notifyXmldbObservers() {
        for (int i = 0, s = this.xmldbObservers.size(); i < s; i++) {
            ((XmldbObserver) this.xmldbObservers.get(i)).update(this.xmldb);
        }
    }
    
    /**
     * 
     */
    protected void notifyUIObjectObservers() {
        
        if (this.uiObjectRunning) {
            return;
        }
        this.uiObjectRunning = true;
        this.uiObject = (UIObject) this.uiObjectQueue.removeFirst();
                
        for (int i = this.uiObjectObservers.size() - 1; i >= 0; i--) {
            if (uiObject == null && !uiObjectQueue.isEmpty()) {
                if (uiObjectQueue.getFirst() != null) {
                    break;
                } else {
                    uiObjectQueue.removeFirst();
                }
            }
            ((UIObjectObserver) this.uiObjectObservers.get(i)).update(uiObject);
        }
        
        if (this.uiObject != null) {
            this.uiObject.setChanged(false);
        	this.uiObject.setNew(false);
        }
        
        this.uiObjectRunning = false;
        while (!this.uiObjectQueue.isEmpty()) {
            this.notifyUIObjectObservers();
        }
    }
    
    /**
     * 
     */
    private void checkConfig() {
        
        File f = new File("/etc/xdbm");
        
        String home = System.getenv("HOME");
    }
    
    /**
     * Sets global user interface options.
     */
    private void configureUI() {
                
        Options.setDefaultIconSize(new Dimension(18, 18));

        // Set font options		
        UIManager.put(
            Options.USE_SYSTEM_FONTS_APP_KEY,
            settings.isUseSystemFonts());
        Options.setGlobalFontSizeHints(settings.getFontSizeHints());
        Options.setUseNarrowButtons(settings.isUseNarrowButtons());
        Options.setPopupDropShadowEnabled(
                settings.isPopupDropShadowEnabled().booleanValue());
        
        // Global options
        Options.setTabIconsEnabled(settings.isTabIconsEnabled());
        UIManager.put(Options.POPUP_DROP_SHADOW_ENABLED_KEY, 
                settings.isPopupDropShadowEnabled());

        // Swing Settings
        LookAndFeel selectedLaf = settings.getSelectedLookAndFeel();
        if (selectedLaf instanceof PlasticLookAndFeel) {
            PlasticLookAndFeel.setMyCurrentTheme(settings.getSelectedTheme());
            PlasticLookAndFeel.setTabStyle(settings.getPlasticTabStyle());
            PlasticLookAndFeel.setHighContrastFocusColorsEnabled(
                settings.isPlasticHighContrastFocusEnabled());
        } else if (selectedLaf.getClass() == MetalLookAndFeel.class) {
            MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());
        }
        
        // Work around caching in MetalRadioButtonUI
        JRadioButton radio = new JRadioButton();
        radio.getUI().uninstallUI(radio);
        JCheckBox checkBox = new JCheckBox();
        checkBox.getUI().uninstallUI(checkBox);

        try {
            UIManager.setLookAndFeel(selectedLaf);
        } catch (Exception e) {
            System.out.println("Can't change L&F: " + e);
        }
    }
    
    /**
     * 
     */
    private void initUI() {
                
        this.getContentPane().setLayout(
                new DockLayout(this, DockLayout.STACKING_STYLE));
        
        this.menuBar       = new ApplicationMenuBar(this);
        this.toolBars      = new ApplicationToolBars(this);
        this.consolePanel  = new BottomFrame(this);
        this.treePanel     = new LeftFrame(this);
        this.resourceFrame = new CenterFrame(this);
        
        this.jspContentConsole = new UIFSplitPane();
        this.jspTreeResource = new UIFSplitPane();
        
        this.add(this.jspContentConsole, DockLayout.center);
        
        this.jspContentConsole.setBorder(BorderFactory.createEmptyBorder());
        this.jspContentConsole.setOrientation(JSplitPane.VERTICAL_SPLIT);
        this.jspContentConsole.setDividerSize(5);
        this.jspContentConsole.add(this.jspTreeResource, JSplitPane.TOP);
        this.jspContentConsole.add(this.consolePanel, JSplitPane.BOTTOM);
        
        this.jspTreeResource.setBorder(BorderFactory.createEmptyBorder());
        this.jspTreeResource.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        this.jspTreeResource.setDividerSize(5);
        this.jspTreeResource.setDividerLocation(200);
        this.jspTreeResource.add(this.treePanel, JSplitPane.TOP);
        this.jspTreeResource.add(this.resourceFrame, JSplitPane.BOTTOM);
        
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        
        this.setSize((int) dim.getWidth(), (int) dim.getHeight() - 30);        
        this.setVisible(true);
    }
    
    /**
     * 
     */
    private void initListener() {
        
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(final WindowEvent we) {
                config.save();
                if (xmldb != null) {
                    try {
                        xmldb.getCollection("/db", "sa", "").close();
                    } catch (XMLDBException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        
        
        this.jspContentConsole.addComponentListener(new ComponentAdapter() {
            
            public void componentResized(final ComponentEvent ce) {
                jspContentConsole.setDividerLocation(
                        jspContentConsole.getHeight() - 150);
            }
        });
    }
    
    /**
     * 
     */
    private void initConfig() {
        Config cfg = Config.getInstance("ui");
        
        if (!cfg.exists()) {
            
            String lang = Locale.getDefault().getLanguage();
            if (!lang.equals("en") && !lang.equals("de")) {
                lang = "en";
            }
            cfg.setLocale(new Locale(lang));
            
            FirstStartSetup sd = new FirstStartSetup();
            sd.setVisible(true);
            
            cfg.save();
        }
        
        this.config = cfg;
    }

    
    class UpdateThread extends Thread {
        
        private LinkedList list = new LinkedList();
        
        public synchronized void push(final UIObject objIn) {
            this.list.addLast(objIn);
        }
        
        public void run() {
            while (!list.isEmpty()) {
                UIObject obj = (UIObject) list.removeFirst();
                for (int i = 0, s = uiObjectObservers.size(); i < s; i++) {
                    ((UIObjectObserver) uiObjectObservers.get(i)).update(obj);
                }
            }
        }
    }
}
