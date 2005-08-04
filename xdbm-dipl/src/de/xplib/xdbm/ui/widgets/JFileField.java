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
 * $Log: JFileField.java,v $
 * Revision 1.1  2005/04/12 08:34:22  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.ui.widgets;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class JFileField extends JPanel {

    /**
     * Comment for <code>JAR_FILE_FILTER</code>
     */
    public static final FileFilter JAR_FILE_FILTER = new JarFileFilter();
    
    /**
     * Comment for <code>XML_FILE_FILTER</code>
     */
    public static final FileFilter XML_FILE_FILTER = new XMLFileFilter();
    
    /**
     * Comment for <code>XSL_FILE_FILTER</code>
     */
    public static final FileFilter XSL_FILE_FILTER = new XSLFileFilter();
    
    /**
     * Comment for <code>DTD_FILE_FILTER</code>
     */
    public static final FileFilter DTD_FILE_FILTER = new DTDFileFilter();
    
    /**
     * Comment for <code>XSD_FILE_FILTER</code>
     */
    public static final FileFilter XSD_FILE_FILTER = new XSFileFilter();
    
    /**
     * Comment for <code>VCS_FILE_FILTER</code>
     */
    public static final FileFilter VCS_FILE_FILTER = new VCSFileFilter();
    
    
    /**
     * Comment for <code>text</code>
     */
    private JTextField text = new JTextField();
    
    /**
     * Comment for <code>button</code>
     */
    private JButton button = new JButton();
    
    /**
     * Comment for <code>mode</code>
     */
    private int mode = JFileChooser.FILES_AND_DIRECTORIES;
    
    /**
     * Comment for <code>directory</code>
     */
    private String directory = null;
    
    /**
     * Comment for <code>filter</code>
     */
    private FileFilter filter = null;
    
    /**
     * Comment for <code>filters</code>
     */
    private ArrayList filters = new ArrayList();
    
    /**
     * Comment for <code>selectListeners</code>
     */
    private ArrayList selectListeners = new ArrayList();
    
    /**
     * 
     */
    public JFileField() {
        
        this.button.setText("...");
        
        Dimension dim = new Dimension(20, 20);
        this.button.setSize(dim);
        this.button.setPreferredSize(dim);
        this.button.setMaximumSize(dim);
        this.button.setMinimumSize(dim);
        
        this.setLayout(new BorderLayout());
        
        this.add(text, BorderLayout.CENTER);
        this.add(button, BorderLayout.EAST);
        
        this.button.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent ae) {
                performActionOpenFileChoose(ae);
            }
        });
    }
    
    /**
     * @param textIn The text for the field.
     */
    public void setText(final String textIn) {
        this.text.setText(textIn.trim());
    }
    
    /**
     * @return The text of the text field.
     */
    public String getText() {
        return this.text.getText().trim();
    }
    
    /**
     * <Some description here>
     * 
     * @param ttIn
     * @see javax.swing.JComponent#setToolTipText(java.lang.String)
     */
    public void setToolTipText(final String ttIn) {
        super.setToolTipText(ttIn);
        
        this.text.setToolTipText(ttIn);
        this.button.setToolTipText(ttIn);
    }
    
    /**
     * @param editableIn Is the text field editable or not?
     */
    public void setEditable(final boolean editableIn) {
        this.text.setEditable(editableIn);
    }
    
    /**
     * <Some description here>
     * 
     * @param enabledIn
     * @see java.awt.Component#setEnabled(boolean)
     */
    public void setEnabled(final boolean enabledIn) {
        super.setEnabled(enabledIn);
        
        this.text.setEnabled(enabledIn);
        this.button.setEnabled(enabledIn);
    }
    
    /**
     * @param modeIn The file selection mode.
     */
    public void setFileSelectionMode(final int modeIn) {
        this.mode = modeIn;
    }
    
    /**
     * @return The file selection mode.
     */
    public int getFileSelectionMode() {
        return this.mode;
    }
    
    /**
     * @param directoryIn The current working directory.
     */
    public void setWorkingDirectory(final String directoryIn) {
        this.directory = directoryIn;
    }
    
    /**
     * @return The current working directory.
     */
    public String getWorkingDirectory() {
        return this.directory;
    }
    
    /**
     * @param filterIn FileFilter to use.
     */
    public void addFileFilter(final FileFilter filterIn) {
        if (!this.filters.contains(filterIn)) {
            this.filters.add(filterIn);
        }
    }
    
    /**
     * @param filterIn The filter to remove.
     */
    public void removeFileFilter(final FileFilter filterIn) {
        if (this.filters.contains(filterIn)) {
            this.filters.remove(filterIn);
        }
    }
    
    /**
     * @param slIn A JFileFieldSelectListener to add.
     */
    public void addSelectListener(final JFileFieldSelectListener slIn) {
        if (!this.selectListeners.contains(slIn)) {
            this.selectListeners.add(slIn);
        }
    }
    
    /**
     * @param slIn A JFileFieldSelectListener to remove.
     */
    public void removeSelectListener(final JFileFieldSelectListener slIn) {
        if (this.selectListeners.contains(slIn)) {
            this.selectListeners.remove(slIn);
        }
    }
    
    /**
     * @param deIn ...
     */
    public void addDocumentListener(final DocumentListener deIn) {
        this.text.getDocument().addDocumentListener(deIn);
    }
    
    /**
     * @param deIn ..
     */
    public void removeDocumentListener(final DocumentListener deIn) {
        this.text.getDocument().removeDocumentListener(deIn);
    }
    
    /**
     * @param aeIn The action event.
     */
    protected void performActionOpenFileChoose(final ActionEvent aeIn) {
        
        String txt = this.getText();
        
        JFileChooser jfc;
        if (this.directory != null || !txt.equals("")) {
            if (txt.equals("")) {
                txt = this.directory;
            }
            File f = new File(txt);
            if (!f.isDirectory()) {
                txt = f.getParent();
            }
            jfc = new JFileChooser(new File(txt));
        } else if (System.getProperty("jfilefield.last.selected.dir") != null) {
            jfc = new JFileChooser(new File(System.getProperty("jfilefield.last.selected.dir")));
        } else {
            jfc = new JFileChooser();
        }
        
        Iterator it = this.filters.iterator();
        while (it.hasNext()) {
            jfc.addChoosableFileFilter((FileFilter) it.next());
        }
        
        jfc.setFileSelectionMode(this.mode);
        
        jfc.showOpenDialog(this);
        
        File f = jfc.getSelectedFile();
        if (f != null) {
            
            String last = f.getAbsolutePath();
            if (f.isFile()) {
                last = f.getParentFile().getAbsolutePath();
            }
            
            System.setProperty("jfilefield.last.selected.dir", last);
                        
            this.text.setText(f.getAbsolutePath());
            
            ActionEvent ae = new ActionEvent(this, 0, f.getAbsolutePath());
            
            it = this.selectListeners.iterator();
            while (it.hasNext()) {
                ((JFileFieldSelectListener) it.next()).actionPerformed(ae);
            }
        }
    }
    
    
    /**
     *  
     * @author Manuel Pichler <manuel.pichler@xplib.de>
     * @version $Revision: 1.1 $
     */
    static class JarFileFilter extends FileFilter {
        
        /**
         * <Some description here>
         * 
         * @param fileIn
         * @return
         * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
         */
        public boolean accept(final File fileIn) {
            return fileIn.isDirectory() || fileIn.getName().endsWith(".jar");
        }
        
        /**
         * <Some description here>
         * 
         * @return
         * @see javax.swing.filechooser.FileFilter#getDescription()
         */
        public String getDescription() {
            return "*.jar (Java Jar Archives)";
        }
    }
    
    /**
     *  
     * @author Manuel Pichler <manuel.pichler@xplib.de>
     * @version $Revision: 1.1 $
     */
    static class XMLFileFilter extends FileFilter {

        /**
         * <Some description here>
         * 
         * @param fileIn
         * @return
         * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
         */
        public boolean accept(final File fileIn) {
            return fileIn.isDirectory() || fileIn.getName().endsWith(".xml");
        }
        
        /**
         * <Some description here>
         * 
         * @return
         * @see javax.swing.filechooser.FileFilter#getDescription()
         */
        public String getDescription() {
            return "*.xml (XML-Document)";
        }
    }
    
    /**
     *  
     * @author Manuel Pichler <manuel.pichler@xplib.de>
     * @version $Revision: 1.1 $
     */
    static class XSLFileFilter extends FileFilter {

        /**
         * <Some description here>
         * 
         * @param fileIn
         * @return
         * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
         */
        public boolean accept(final File fileIn) {
            return fileIn.isDirectory() || fileIn.getName().endsWith(".xsl");
        }
        
        /**
         * <Some description here>
         * 
         * @return
         * @see javax.swing.filechooser.FileFilter#getDescription()
         */
        public String getDescription() {
            return "*.xsl (XSL-Stylesheet)";
        }
    }
    
    /**
     *  
     * @author Manuel Pichler <manuel.pichler@xplib.de>
     * @version $Revision: 1.1 $
     */
    static class DTDFileFilter extends FileFilter {

        /**
         * <Some description here>
         * 
         * @param fileIn
         * @return
         * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
         */
        public boolean accept(final File fileIn) {
            return fileIn.isDirectory() || fileIn.getName().endsWith(".dtd");
        }
        
        /**
         * <Some description here>
         * 
         * @return
         * @see javax.swing.filechooser.FileFilter#getDescription()
         */
        public String getDescription() {
            return "*.dtd (Document Type Definition)";
        }
    }
    
    /**
     *  
     * @author Manuel Pichler <manuel.pichler@xplib.de>
     * @version $Revision: 1.1 $
     */
    static class XSFileFilter extends FileFilter {

        /**
         * <Some description here>
         * 
         * @param fileIn
         * @return
         * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
         */
        public boolean accept(final File fileIn) {
            return fileIn.isDirectory() 
                   || fileIn.getName().endsWith(".xs")
                   || fileIn.getName().endsWith(".xsd");
        }
        
        /**
         * <Some description here>
         * 
         * @return
         * @see javax.swing.filechooser.FileFilter#getDescription()
         */
        public String getDescription() {
            return "*.xs, *.xsd (XML-Schema)";
        }
    }
    
    /**
     *  
     * @author Manuel Pichler <manuel.pichler@xplib.de>
     * @version $Revision: 1.1 $
     */
    static class VCSFileFilter extends FileFilter {

        /**
         * <Some description here>
         * 
         * @param fileIn
         * @return
         * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
         */
        public boolean accept(final File fileIn) {
            return fileIn.isDirectory() || fileIn.getName().endsWith(".vcs");
        }
        
        /**
         * <Some description here>
         * 
         * @return
         * @see javax.swing.filechooser.FileFilter#getDescription()
         */
        public String getDescription() {
            return "*.vcs (Virtual-Collection Schema)";
        }
    }
    
}
