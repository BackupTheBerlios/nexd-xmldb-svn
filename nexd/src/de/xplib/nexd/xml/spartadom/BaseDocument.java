/* <blockquote><small> Copyright (C) 2002 Hewlett-Packard Company.
 * This file is part of Sparta, an XML Parser, DOM, and XPath library.
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the <a href="doc-files/LGPL.txt">GNU
 * Lesser General Public License</a> as published by the Free Software
 * Foundation; either version 2.1 of the License, or (at your option)
 * any later version.  This library is distributed in the hope that it
 * will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. </small></blockquote>
 * 
 * @author Eamonn O'Brien-Strain
 */
package de.xplib.nexd.xml.spartadom;

import java.io.IOException;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.NoSuchElementException;
import java.util.Vector;

import de.xplib.nexd.xml.Sparta;
import de.xplib.nexd.xml.xpath.Step;
import de.xplib.nexd.xml.xpath.XPath;
import de.xplib.nexd.xml.xpath.XPathException;

/**
 * An XML Document.
 * 
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 * @link http://sparta.sourceforge.net
 * @see org.w3c.dom.Document
 */
public class BaseDocument extends BaseNode {

    /** @see BaseDocument#xpathGetIndex(String) */
    public class Index implements Observer {

        /**
         * Comment for <code>attrName</code>
         */
        private final String attrName;

        /**
         * Comment for <code>dict</code>
         */
        private transient Sparta.Cache dict = null;

        /**
         * Comment for <code>xpath</code>
         */
        private final XPath xpath;

        /**
         * @param xpathIn ..
         * @throws XPathException ..
         */
        Index(final XPath xpathIn) throws XPathException {
            attrName = xpathIn.getIndexingAttrName();
            xpath = xpathIn;
            addObserver(this);
        }

        /**
         * @param attrValue
         *            value of the indexing attribute
         * @return enumeration of Elements
         * @throws ParseException
         *             when XPath that created this Index is malformed.
         */
        public final synchronized Enumeration get(final String attrValue)
                throws ParseException {
            if (dict == null) {
                regenerate();
            }
            Vector elemList = (Vector) dict.get(attrValue);
            return elemList == null ? EMPTY : elemList.elements();
        }

        /**
         * @throws ParseException ..
         */
        private void regenerate() throws ParseException {
            try {

                dict = Sparta.newCache();
                for (Enumeration i = visitor(xpath, false)
                        .getResultEnumeration(); i.hasMoreElements();) {
                    BaseElement elem = (BaseElement) i.nextElement();
                    String attrValue = elem.getAttribute(attrName);
                    Vector elemList = (Vector) dict.get(attrValue);
                    if (elemList == null) {
                        elemList = new Vector(1);
                        dict.put(attrValue, elemList);
                    }
                    elemList.addElement(elem);
                }

            } catch (XPathException e) {
                throw new ParseException("XPath problem", e);
            }
        }

        /**
         * @return number of elements returned by {@link #get(String) get}
         * @throws ParseException ..
         */
        public final synchronized int size() throws ParseException {
            if (dict == null) {
                regenerate();
            }
            return dict.size();
        }

        /**
         * <Some description here>
         * 
         * @param doc ..
         * @see de.xplib.nexd.xml.spartadom.BaseDocument.Observer#update(
         * 		de.xplib.nexd.xml.spartadom.BaseDocument)
         */
        public final synchronized void update(final BaseDocument doc) {
            dict = null; //force index to be regenerated on next get()
        }
    }

    /*
     * public void removeIndices() { indices.clear(); }
     */

    /** Something that is informed whenever the document changes. */
    public interface Observer {
        /** 
         * Called when the document changes.
         * 
         * @param doc .. 
         */
        void update(BaseDocument doc);
    }

    /**
     * Comment for <code>DEBUG</code>
     */
    private static final boolean DEBUG = false;

    /**
     * Comment for <code>EMPTY</code>
     */
    static final Enumeration EMPTY = new EmptyEnumeration();
    
    /**
     * Comment for <code>MAX_COUNT</code>
     */
    private static final short MAX_COUNT = 100;

    /**
     * Comment for <code>ONE</code>
     */
    private static final Integer ONE = new Integer(1);
    
    /**
     * Comment for <code>childNodes</code>
     */
    private static Vector childNodes = new Vector();

    /**
     * Comment for <code>indexible</code>
     */
    private final Hashtable indexible = DEBUG ? new Hashtable() : null;

    /**
     * Comment for <code>indices</code>
     */
    private Sparta.Cache indices = Sparta.newCache();

    /**
     * Comment for <code>observers</code>
     */
    private Vector observers = new Vector();

    /**
     * @link aggregation
     * @label documentElement
     */
    private BaseElement rootElement = null;

    /**
     * Comment for <code>systemId</code>
     */
    private String systemId;

    /**
     * Create new empty in-memory Document with a null documentElement.
     */
    public BaseDocument() {
        systemId = "MEMORY";
    }

    /**
     * @param systemIdIn ...
     */
    BaseDocument(final String systemIdIn) {
        systemId = systemIdIn;
    }

    /**
     * @param observer ..
     */
    public final void addObserver(final Observer observer) {
        observers.addElement(observer);
    }
    
    /**
     * @param newChild ..
     * @return ..
     */
    public final BaseNode appendChild(final BaseNode newChild) {
        
        return newChild;
    }

    /**
     * Deep copy of this document. Any annotation is not copied.
     * 
     * @return ..
     * @see java.lang.Object#clone()
     */
    public final Object clone() {
        BaseDocument copy = new BaseDocument(systemId);
        copy.rootElement = (BaseElement) rootElement.clone();
        return copy;
    }

    /**
     * Called whenever cached version of hashCode needs to be regenerated.
     * 
     * @return ..
     * @see de.xplib.nexd.xml.spartadom.BaseNode#computeHashCode()
     */
    protected final int computeHashCode() {
        return rootElement.hashCode();
    }

    /**
     * @param observer ..
     */
    public final void deleteObserver(final Observer observer) {
        observers.removeElement(observer);
    }

    /**
     * Two documents are equal IFF their document elements are equal.
     * 
     * @param thatO ..
     * @return ..
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public final boolean equals(final Object thatO) {

        //Do cheap tests first
        if (this == thatO) {
            return true;
        }
        if (!(thatO instanceof BaseDocument)) {
            return false;
        }
        BaseDocument that = (BaseDocument) thatO;
        return this.rootElement.equals(that.rootElement);
    }
    
    /**
     * <Some description here>
     * 
     * @return ..
     * @see java.lang.Object#hashCode()
     */
    public final int hashCode() {
        return super.hashCode();
    }

    /**
     * @return root element of this DOM tree.
     */
    public final BaseElement getDocumentElement() {
        return rootElement;
    }

    /**
     * @return the filename, URL, or other ID by which this document is known.
     *         Initialized to "MEMORY" for Document created with default
     *         constructor.
     */
    public final String getSystemId() {
        return systemId;
    }

    /**
     * @param parseTree ..
     * @throws XPathException ..
     */
    final void monitor(final XPath parseTree) throws XPathException {
        if (DEBUG) {
            String indexingAttr = parseTree.getIndexingAttrNameOfEquals();
            if (indexingAttr != null) {
                String xpath = parseTree.toString();
                String prefix = xpath.substring(0, xpath.lastIndexOf('='));
                Integer count = (Integer) indexible.get(prefix);
                if (count == null) {
                    count = ONE;
                } else {
                    count = new Integer(count.intValue() + 1);
                }
                indexible.put(prefix, count);
                if (count.intValue() > MAX_COUNT) {
                    System.out.println("COULD-BE-INDEXED: " + xpath + " used "
                            + count + " times in " + this);
                }
            }
        }
    }

    /**
     * <Some description here>
     * 
     * @see de.xplib.nexd.xml.spartadom.BaseNode#notifyObservers()
     */
    final void notifyObservers() {
        for (Enumeration i = observers.elements(); i.hasMoreElements();) {
            ((Observer) i.nextElement()).update(this);
        }
    }

    /**
     * Set the root element of this DOM tree.
     * 
     * @param rootElementIn ..
     */
    public final void setDocumentElement(final BaseElement rootElementIn) {
        rootElement = rootElementIn;
        rootElement.setOwnerDocument(this);
        notifyObservers();
    }

    /**
     * @param systemIdIn
     *            the filename, URL, or other ID by which this document is
     *            known.
     */
    public final void setSystemId(final String systemIdIn) {
        systemId = systemIdIn;
        notifyObservers();
    }

    /** 
     * Same as {@link #getSystemId  getSystemId} 
     * 
     * @return ..
     * @see java.lang.Object#toString()
     */
    public final String toString() {
        return systemId;
    }

    /** 
     * Accumulate text nodes hierarchically.
     * 
     * @param writer ..
     * @throws IOException ..
     * @see BaseNode#toString(Writer) 
     */
    public final void toString(final Writer writer) throws IOException {
        rootElement.toString(writer);
    }

    /**
     * Write DOM to XML.
     * 
     * @param writer ..
     * @throws IOException ..
     * @see BaseNode#toXml(Writer)
     */
    public final void toXml(final Writer writer) throws IOException {
        writer.write("<?xml version=\"1.0\" ?>\n");
        rootElement.toXml(writer);
    }

    /**
     * @param xpathIn ..
     * @param expectStringValue ..
     * @return ..
     * @throws XPathException ..
     */
    private XPathVisitor visitor(final String xpathIn, 
                                 final boolean expectStringValue)
                                         throws XPathException { //, IOException
        String xpath = xpathIn; 
        if (xpath.charAt(0) != '/') {
            xpath = "/" + xpath;
        }
        return visitor(XPath.get(xpath), expectStringValue);
    }

    /**
     * @param parseTree ..
     * @param expectStringValue ..
     * @return ..
     * @throws XPathException ..
     */
    final XPathVisitor visitor(final XPath parseTree, 
            				   final boolean expectStringValue)
                                       throws XPathException {
        
        if (parseTree.isStringValue() != expectStringValue) {
            String msg = expectStringValue ? "evaluates to element not string"
                    : "evaluates to string not element";
            throw new XPathException(parseTree, "\"" + parseTree
                    + "\" evaluates to " + msg);
        }
        return new XPathVisitor(this, parseTree);
    }

    /**
     * Just like Element.xpathEnsure, but also handles case of no
     * documentElement.
     * 
     * @param xpath ..
     * @return ..
     * @throws ParseException ..
     */
    public final boolean xpathEnsure(final String xpath) throws ParseException {
        try {

            //Quick exit for common case
            if (xpathSelectElement(xpath) != null) {
                return false;
            }

            //Split XPath into dirst step and bit relative to rootElement
            final XPath parseTree = XPath.get(xpath);
            int stepCount = 0;
            for (Enumeration i = parseTree.getSteps(); i.hasMoreElements();) {
                i.nextElement();
                ++stepCount;
            }
            Enumeration i = parseTree.getSteps();
            Step firstStep = (Step) i.nextElement();
            Step[] rootElemSteps = new Step[stepCount - 1];
            for (int j = 0; j < rootElemSteps.length; ++j) { 
                rootElemSteps[j] = (Step) i.nextElement();
        	}

            //Create root element if necessary
            if (rootElement == null) {
                BaseElement newRoot = makeMatching(null, firstStep, xpath);
                setDocumentElement(newRoot);
            } else {
                BaseElement expectedRoot = xpathSelectElement("/" + firstStep);
                if (expectedRoot == null) {
                    throw new ParseException("Existing root element <"
                            + rootElement.getTagName()
                            + "...> does not match first step \"" + firstStep
                            + "\" of \"" + xpath);
                }
            }

            if (rootElemSteps.length == 0) {
                return true;
            }
            return rootElement.xpathEnsure(XPath.get(false, rootElemSteps)
                    .toString());
        } catch (XPathException e) {
            throw new ParseException(xpath, e);
        }
    }

    /**
     * For faster lookup by XPath return (creating if necessary) an index. The
     * xpath should be of the form "xp[@attrName]" where xp is an xpath, not
     * ending in a "[...]" predicate, that returns a list of elements. Doing a
     * get("foo") on the index is equivalent to doing an
     * xpathSelectElement("xp[@attrName='foo']") on the document except that it
     * is faster ( O(1) as apposed to O(n) ). EXAMPLE:
     * 
     * <PRE>
     * 
     * Enumeration leaders; if(doc.xpathHasIndex("/Team/Members[@firstName]"){
     * //fast version Document.Index index = doc.xpathGetIndex(
     * "/Team/Members[@role]"); leaders = index.get("leader"); }else //slow
     * version leaders = doc.xpathSelectElement(
     *   "/Team/Members[@role='leader']");
     * </PRE>
     *
     * @param xpath ..
     * @return ..
     * @throws ParseException ..  
     */
    public final Index xpathGetIndex(final String xpath) throws ParseException {
        try {

            Index index = (Index) indices.get(xpath);
            //TODO : cacnonicalize key (use XPath object as key)
            if (index == null) {
                XPath xp = XPath.get(xpath);
                index = new Index(xp);
                indices.put(xpath, index);
            }
            return index;

        } catch (XPathException e) {
            throw new ParseException("XPath problem", e);
        }
    }

    /**
     * @param xpath ..
     * @return whether an index existst for this xpath
     * @see BaseNode#xpathGetIndex
     */
    public final boolean xpathHasIndex(final String xpath) {
        return indices.get(xpath) != null;
    }

    /**
     * Select the first element that matches the absolute XPath expression in
     * this document, or null if there is no match.
     * 
     * @param xpathIn ..
     * @return ..
     * @throws ParseException ..
     * @see BaseNode#xpathSelectElement(String)
     */
    public final BaseElement xpathSelectElement(final String xpathIn) 
    		throws ParseException {
        
        String xpath = xpathIn;
        try {
            if (xpath.charAt(0) != '/') {
                xpath = "/" + xpath;
            }
            XPath parseTree = XPath.get(xpath);
            monitor(parseTree);
            return visitor(parseTree, false).getFirstResultElement();
        } catch (XPathException e) {
            throw new ParseException("XPath problem", e);
        }
    }

    /**
     * Select all the elements that match the absolute XPath expression in this
     * document.
     * 
     * @param xpathIn ..
     * @return ..
     * @throws ParseException ..
     * @see BaseNode#xpathSelectElements(String)
     */
    public final Enumeration xpathSelectElements(final String xpathIn) 
    		throws ParseException { //, IOException
        
        String xpath = xpathIn;
        try {
            if (xpath.charAt(0) != '/') {
                xpath = "/" + xpath;
            }
            XPath parseTree = XPath.get(xpath);
            monitor(parseTree);
            return visitor(parseTree, false).getResultEnumeration();
        } catch (XPathException e) {
            throw new ParseException("XPath problem", e);
        }
    }

    /**
     * Select the first element that matches the absolute XPath expression in
     * this document, or null if there is no match.
     * 
     * @param xpath ..
     * @return ...
     * @throws ParseException ..
     * @see BaseNode#xpathSelectString(String)
     */
    public final String xpathSelectString(final String xpath) 
    		throws ParseException {
        
        try {
            return visitor(xpath, true).getFirstResultString();
        } catch (XPathException e) {
            throw new ParseException("XPath problem", e);
        }
    }

    /**
     * Select all the strings that match the absolute XPath expression in this
     * document.
     * 
     * @param xpath ..
     * @return ..
     * @throws ParseException ..
     * @see BaseNode#xpathSelectStrings(String)
     */
    public final Enumeration xpathSelectStrings(final String xpath) 
    		throws ParseException {
        
        try {
            return visitor(xpath, true).getResultEnumeration();
        } catch (XPathException e) {
            throw new ParseException("XPath problem", e);
        }
    }
}

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 */
class EmptyEnumeration implements Enumeration {
    
    /**
     * <Some description here>
     * 
     * @return ..
     * @see java.util.Enumeration#hasMoreElements()
     */
    public boolean hasMoreElements() {
        return false;
    }

    /**
     * <Some description here>
     * 
     * @return ..
     * @see java.util.Enumeration#nextElement()
     */
    public Object nextElement() {
        throw new NoSuchElementException();
    }
}

// $Log: Document.java,v $
// Revision 1.12 2003/11/01 05:42:18 eobrain
// Add synchronized on some methods to make thread-safe.
//
// Revision 1.11 2003/07/17 23:52:05 eobrain
// Make compatiblie with J2ME. For example do not use "new"
// java.util classes.
//
// Revision 1.10 2003/06/19 20:28:20 eobrain
// Hash code optimization.
// Add monitoring (in debug mode) to detect when indexing could optimize.
//
// Revision 1.9 2003/05/12 19:56:03 eobrain
// Performance improvements.
//
// Revision 1.8 2003/01/27 23:30:58 yuhongx
// Replaced Hashtable with HashMap.
//
// Revision 1.7 2003/01/09 00:55:26 yuhongx
// Use jdk1.1 API (replaced add() with addElement()).
//
// Revision 1.6 2002/12/13 22:44:36 eobrain
// Remove redundant get/set annotation that is already in superclass.
//
// Revision 1.5 2002/12/13 18:12:16 eobrain
// Fix xpathEnsure to handle case when the XPath given specifies a root node
// tagname that conflicts with the existing root node. Extend xpathEnsure to
// work with any type of predicate. Replace hacky string manipulation code with
// code that works on the XPath parse tree.
//
// Revision 1.4 2002/11/06 02:57:59 eobrain
// Organize imputs to removed unused imports. Remove some unused local
// variables.
//
// Revision 1.3 2002/10/30 16:39:02 eobrain
// Fixed bug [ 627024 ] doc.xpathEnsure("/top") throws exception
// http://sourceforge.net/projects/sparta-xml/
//
// Revision 1.2 2002/09/12 23:00:57 eobrain
// Allow Document.xpathEnsure to work when there is no root element set.
//
// Revision 1.1.1.1 2002/08/19 05:03:55 eobrain
// import from HP Labs internal CVS
//
// Revision 1.24 2002/08/18 04:19:18 eob
// Sparta no longer throws XPathException -- it throws ParseException
// instead.
//
// Revision 1.23 2002/08/17 22:41:41 eob
// Add copyright and other formatting and commenting in preparation for
// release to SourceForge.
//
// Revision 1.22 2002/08/15 22:39:53 eob
// Fix bug in which index was not getting put into index hash.
//
// Revision 1.21 2002/08/15 21:25:18 eob
// Constructor no longer needs documenent.
//
// Revision 1.20 2002/08/15 05:07:48 eob
// Add indexing for fast XPath lookup.
//
// Revision 1.19 2002/08/13 22:54:39 eob
// Added xpath indexing for faster lookup.
//
// Revision 1.18 2002/07/25 21:10:15 sermarti
// Adding files that mysteriously weren't added from Sparta before.
//
// Revision 1.17 2002/06/14 19:36:42 eob
// Make toString of Node do the same as in XSLT -- recursive
// concatenation of all text in text nodes.
//
// Revision 1.16 2002/05/23 21:04:35 eob
// Add better error reporting.
//
// Revision 1.15 2002/05/10 21:37:42 eob
// equals added
//
// Revision 1.14 2002/05/09 16:47:50 eob
// Add cloneDocument
//
// Revision 1.13 2002/03/26 01:41:11 eob
// Deprecate XPathAPI
//
// Revision 1.12 2002/02/23 01:43:26 eob
// Add clone method. Tweak toXml API.
//
// Revision 1.11 2002/02/15 21:20:35 eob
// Rename xpath* methods to xpathSelect* to make more obvious.
//
// Revision 1.10 2002/02/15 21:05:28 eob
// Add convenient xpath* methods, allowing a more object-oriented use than
// XPathAPI.
//
// Revision 1.9 2002/02/04 22:09:04 eob
// Add defualt constructer.
//
// Revision 1.8 2002/02/01 21:49:45 eob
// Make Document inherit from Node. Needed for XPath.
//
// Revision 1.7 2002/01/04 00:36:52 eob
// add annotation
//
// Revision 1.6 2002/01/04 16:48:44 eob
// Comment changes only.
//
// Revision 1.5 2002/01/04 14:48:56 eob
// Remove Log
//
// Revision 1.4 2002/01/04 14:46:21 eob
// comment change only
//
// Revision 1.3 2002/01/04 14:39:19 eob
// Move parse functionality functionality to ParseSource.
//
// Revision 1.2 2001/12/20 20:06:28 eob
// Fix some entity bugs. Use UTD-8 or UTF-16 encoding as appropriate.
//
// Revision 1.1 2001/12/19 05:52:38 eob
// initial
