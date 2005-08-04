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
 * $Log: FirstStartSetup.java,v $
 * Revision 1.1  2005/04/12 08:34:22  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.ui.dialog;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.commons.i18n.MessageManager;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.XMLDBException;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import de.xplib.xdbm.ui.widgets.JFileField;
import de.xplib.xdbm.ui.widgets.JFileFieldSelectListener;
import de.xplib.xdbm.util.Config;
import de.xplib.xdbm.util.JarClassFinder;
import de.xplib.xdbm.util.JarClassFinderCallback;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class FirstStartSetup extends JDialog implements JarClassFinderCallback {
    
    /**
     * Comment for <code>config</code>
     */
    private Config config;
    
    /**
     * Comment for <code>bLayout</code>
     */
    private BorderLayout mainLayout = new BorderLayout();
    
    /**
     * Comment for <code>statusPanel</code>
     */
    private JPanel statusPanel = new JPanel();
    
    /**
     * Comment for <code>statusLayout</code>
     */
    private GridLayout statusLayout = new GridLayout(1, 2);
    
    /**
     * Comment for <code>statusLabel</code>
     */
    private JLabel statusLabel = new JLabel();

    /**
     * Comment for <code>jpBar</code>
     */
    private JProgressBar statusBar = new JProgressBar();
    
    /**
     * Comment for <code>jfField</code>
     */
    private JFileField jfField = new JFileField();
    
    /**
     * Comment for <code>jtfClass</code>
     */
    private JTextField jtfClass = new JTextField(20);
    
    /**
     * Comment for <code>jtfDbURI</code>
     */
    private JTextField jtfDbURI = new JTextField(20);
    
    /**
     * Comment for <code>jbTest</code>
     */
    private JButton jbTest = new JButton();
    
    /**
     * Comment for <code>jbCancel</code>
     */
    private JButton jbCancel = new JButton();
    
    /**
     * Comment for <code>jbOk</code>
     */
    private JButton jbOk = new JButton();
    
    /**
     * 
     */
    public FirstStartSetup() {
        
        this.config = Config.getInstance("ui");
        Locale l = this.config.getLocale();
                
        this.setModal(true);
        this.setTitle(MessageManager.getText(
                "setup.dialog.title", "text", new Object[0], l));
        
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        
        this.initUI();
        
        this.pack();
        this.setResizable(false);
        
    }
    
    /**
     * <Some description here>
     * 
     * @param posIn ..
     * @param nameIn ..
     * @see de.xplib.xdbm.util.JarClassFinderCallback#call(
     *      int, java.lang.String)
     */
    public void call(final int posIn, final String nameIn) {
        this.statusBar.setValue(posIn);
        double d = 0;
        if (posIn != 0) {
            d = Math.round(100 * ((float) posIn) / this.statusBar.getMaximum());
        }
        this.statusBar.setString(d + "%");
    }
    
    /**
     * @return The file path of the database driver jar archive.
     */
    public String getDriverJar() {
        return this.jfField.getText().trim();
    }
    
    /**
     * @return The class name of the database driver.
     */
    public String getDriverClass() {
        return this.jtfClass.getText().trim();
    }
    
    /**
     * @return The database uri or an empty String.
     */
    public String getDatabaseURI() {
        return this.jtfDbURI.getText().trim();
    }
    
    /**
     * @param aeIn ...
     * @return Does the test fail or run?
     */
    private boolean actionPerformedTest(final ActionEvent aeIn) {
        
        Locale l = this.config.getLocale();
        
        try {
            
            String jar = jfField.getText();
            if (!jar.startsWith("file://")) {
                jar = "file://" + jar;
            }
            
            URLClassLoader ucl = new URLClassLoader(
                    new URL[] {new URL(jar)});
            
            Class clazz = ucl.loadClass(jtfClass.getText());
            Database db = (Database) clazz.newInstance();
            
            DatabaseManager.registerDatabase(db);
            
            String uri = this.jtfDbURI.getText().trim();
            if (uri != null && !uri.equals("")) {
                try {
                    Collection coll = DatabaseManager.getCollection(uri);
                    
                    coll.close();
                } catch (XMLDBException e1) {
                    throw new Exception(MessageManager.getText(
                            "setup.dialog.test.error.uri", "text", 
                            new Object[] {uri}, l));
                }
            }
            
            return true;
        } catch (Exception e) {
            
            String msg = "<html>" + MessageManager.getText(
                    "setup.dialog.test.error.label", "text", 
                    new Object[0], l) + "<br>" + e.getMessage() + "</html>";
            
            String title = MessageManager.getText(
                    "setup.dialog.test.error.title", "text", new Object[0], l);
            
            JOptionPane.showMessageDialog(
                    this, msg, title, JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    /**
     * 
     */
    private void initUI() {
        
        this.getContentPane().setLayout(this.mainLayout);
        
        Locale l = this.config.getLocale();
        Object[] args = new Object[0];
        
        this.jfField.addFileFilter(JFileField.JAR_FILE_FILTER);
        this.jfField.setFileSelectionMode(JFileChooser.FILES_ONLY);
        this.jfField.addSelectListener(new FileSelectListener());
        this.jfField.addDocumentListener(new EnableButtonListener());
        this.jfField.setToolTipText(MessageManager.getText(
                "setup.dialog.jarfile.tooltip", "text", args, l));
        
        this.jtfClass.getDocument().addDocumentListener(
                new EnableButtonListener());
        this.jtfClass.setToolTipText(MessageManager.getText(
                "setup.dialog.class.tooltip", "text", args, l));
        
        this.jtfDbURI.setToolTipText(MessageManager.getText(
                "setup.dialog.dburi.tooltip", "text", args, l));
        
        this.jbTest.setEnabled(false);
        this.jbTest.setText(MessageManager.getText(
                "setup.dialog.test.label", "text", args, l));
        this.jbTest.setToolTipText(MessageManager.getText(
                "setup.dialog.test.tooltip", "text", args, l));
        this.jbTest.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent ae) {
                if (actionPerformedTest(ae)) {
                    JOptionPane.showMessageDialog(
                            FirstStartSetup.this, 
                            MessageManager.getText(
                                    "setup.dialog.test.success.message",
                                    "text", new Object[0], config.getLocale()),
                            MessageManager.getText(
                                    "setup.dialog.test.success.title",
                                    "text", new Object[0], config.getLocale()),
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        
        this.jbCancel.setText(MessageManager.getText(
                "setup.dialog.cancel.label", "text", args, l));
        this.jbCancel.setToolTipText(MessageManager.getText(
                "setup.dialog.cancel.tooltip", "text", args, l));
        this.jbCancel.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent ae) {
                System.exit(0);
            }
        });
        
        this.jbOk.setEnabled(false);
        this.jbOk.setText(MessageManager.getText(
                "setup.dialog.ok.label", "text", args, l));
        this.jbOk.setToolTipText(MessageManager.getText(
                "setup.dialog.ok.tooltip", "text", args, l));
        this.jbOk.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent ae) {
                if (actionPerformedTest(ae)) {
                    
                    String jar = jfField.getText().trim(); 
                    
                    config.putDriver(jar, jtfClass.getText().trim());
                    
                    String uri = jtfDbURI.getText().trim();
                    if (!uri.equals("")) {
                        config.putDatabaseURI(jar, uri);
                    }
                    
                    dispose();
                }
            }
        });
        
        
        FormLayout layout = new FormLayout(
                "right:pref, 3dlu, pref, 3dlu, pref, 3dlu, pref", 
                "p, 3dlu, p, 3dlu, p, 3dlu, p, 9dlu, p, 3dlu, p"); 
        
        layout.setColumnGroups(new int[][]{{3 , 5, 7}});
        
        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();
        
        
//      Obtain a reusable constraints object to place components in the grid.
        CellConstraints cc = new CellConstraints();

//         Fill the grid with components; the builder can create
//         frequently used components, e.g. separators and labels.

//         Add a titled separator to cell (1, 1) that spans 7 columns.
        builder.addSeparator(MessageManager.getText(
                "setup.dialog.header", "text", args, l), cc.xyw(1,  1, 7));
        builder.addLabel(MessageManager.getText(
                "setup.dialog.jarfile.label", "text", args, l), cc.xy (1,  3));
        builder.add(this.jfField, cc.xyw(3,  3, 5));
        builder.addLabel(MessageManager.getText(
                "setup.dialog.class.label", "text", args, l), cc.xy (1,  5));
        builder.add(this.jtfClass, cc.xyw(3,  5, 5));
        builder.addLabel(MessageManager.getText(
                "setup.dialog.dburi.label", "text", args, l), cc.xy (1,  7));
        builder.add(this.jtfDbURI, cc.xyw(3,  7, 5));
        
        builder.addSeparator("", cc.xyw(1, 9, 7));
        
        builder.add(this.jbTest, cc.xy(3, 11));
        builder.add(this.jbCancel, cc.xy(5, 11));
        builder.add(this.jbOk, cc.xy(7, 11));
        
        this.getContentPane().add(builder.getPanel(), BorderLayout.CENTER);
                
        this.statusPanel.setLayout(this.statusLayout);
        this.statusPanel.add(this.statusLabel);
        this.statusPanel.add(this.statusBar);
        
        this.statusLabel.setBorder(BorderFactory.createLoweredBevelBorder());
        
        this.statusBar.setBorder(BorderFactory.createLoweredBevelBorder());
        this.statusBar.setStringPainted(true);
        
        this.getContentPane().add(this.statusPanel, BorderLayout.SOUTH);
    }
    
    
    /**
     *  
     * @author Manuel Pichler <manuel.pichler@xplib.de>
     * @version $Revision: 1.1 $
     */
    class FileSelectListener implements JFileFieldSelectListener {
        
        /**
         * <Some description here>
         * 
         * @param aeIn
         * @see java.awt.event.ActionListener#actionPerformed(
         *      java.awt.event.ActionEvent)
         */
        public void actionPerformed(final ActionEvent aeIn) {
            System.out.println(aeIn.getActionCommand());
            
            String path = "file://" + aeIn.getActionCommand();
            
            try {
                ProgressThread pt = new ProgressThread(path);
                pt.start();
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    
    /**
     *  
     * @author Manuel Pichler <manuel.pichler@xplib.de>
     * @version $Revision: 1.1 $
     */
    class EnableButtonListener implements DocumentListener {
        
        /**
         * <Some description here>
         * 
         * @param e
         * @see javax.swing.event.DocumentListener#changedUpdate(
         *      javax.swing.event.DocumentEvent)
         */
        public void changedUpdate(final DocumentEvent e) {
            this.update();
        }
        
        /**
         * <Some description here>
         * 
         * @param e
         * @see javax.swing.event.DocumentListener#insertUpdate(
         *      javax.swing.event.DocumentEvent)
         */
        public void insertUpdate(final DocumentEvent e) {
            this.update();
        }
        
        /**
         * <Some description here>
         * 
         * @param e
         * @see javax.swing.event.DocumentListener#removeUpdate(
         *      javax.swing.event.DocumentEvent)
         */
        public void removeUpdate(final DocumentEvent e) {
            this.update();
        }
        
        /**
         * 
         */
        private void update() {
            String t1 = jfField.getText().trim();
            String t2 = jtfClass.getText().trim();
            
            boolean b = (t1 != null && !t1.equals("") && t2 != null 
                    && !t2.equals(""));
            
            jbTest.setEnabled(b);
            jbOk.setEnabled(b);
        }
    }
    
    /**
     *  
     * @author Manuel Pichler <manuel.pichler@xplib.de>
     * @version $Revision: 1.1 $
     */
    class ProgressThread extends Thread {
        
        /**
         * Comment for <code>jcf</code>
         */
        private JarClassFinder jcf;
        
        /**
         * @param pathIn ..
         * @throws MalformedURLException ..
         * @throws IOException ..
         */
        public ProgressThread(final String pathIn) throws MalformedURLException,
                                                          IOException {
            this.jcf = new JarClassFinder(pathIn);
        }
        
        /**
         * <Some description here>
         * 
         * 
         * @see java.lang.Runnable#run()
         */
        public void run() {
            statusLabel.setText(MessageManager.getText(
                    "setup.dialog.status.search", "text", 
                    new Object[0], config.getLocale()));
            jcf.setCallback(FirstStartSetup.this);
            statusBar.setMaximum(jcf.getSize());
            Class clazz = jcf.findClassByInterface(Database.class);
            if (clazz == null) {
                statusLabel.setText("");
            } else {
                String name = clazz.getName();
                jtfClass.setText(name);
                
                statusLabel.setText(MessageManager.getText(
                        "setup.dialog.status.found", "text",
                        new Object[] {name}, config.getLocale()));
            }
        }
    }
}
