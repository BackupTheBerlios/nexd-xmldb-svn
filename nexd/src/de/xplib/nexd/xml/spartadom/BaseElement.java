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
import java.util.Vector;

import org.w3c.dom.DOMException;

import de.xplib.nexd.xml.Sparta;
import de.xplib.nexd.xml.xpath.Step;
import de.xplib.nexd.xml.xpath.XPath;
import de.xplib.nexd.xml.xpath.XPathException;

/**
 * A type of Node with a particular tagName that has a set of attributes and can
 * contain other nodes as children. An example of its form in XML in the form
 * 
 * <PRE>
 * 
 * &lt;tagName attr1="value1" attr2="value2"> text &lt;childTag...>
 * &lt;childTag...> text &lt;childTag...> text &lt;/tagName>
 * 
 * </PRE>
 * 
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 * @link http://sparta.sourceforge.net
 * @see org.w3c.dom.Element
 * @stereotype container
 */
public class BaseElement extends BaseNode {

    /**
     * Comment for <code>DEBUG</code>
     */
    private static final boolean DEBUG = false;
    
    /**
     * Comment for <code>HASH_VALUE</code>
     */
    private static final short HASH_VALUE = 32;

    /**
     * Comment for <code>attributeNames</code>
     */
    private Vector attributeNames = null; //create on first setAttribute

    //Profiling found that these hashtables were using a lot of memory, so
    // create them lazily

    /**
     * Comment for <code>attributes</code>
     */
    private Hashtable attributes = null; //create on first setAttribute

    /**
     * @link aggregation
     * @label firstChild
     */
    private BaseNode firstChild = null;

    /**
     * @link aggregation
     * @label lastChild
     */
    private BaseNode lastChild = null;

    /**
     * Comment for <code>tagName</code>
     */
    private String tagName = null;

    /**
     * 
     */
    public BaseElement() {
    }

    /**
     * @param tagNameIn --
     */
    public BaseElement(final String tagNameIn) {
        tagName = tagNameIn.intern();
    }

    /**
     * Add node as child of this element, cloning node if it is this element or
     * an ancestor.
     * 
     * @param addedChild --
     */
    public final void appendChild(final BaseNode addedChild) {
        BaseNode node = addedChild;
        if (DEBUG) {
            checkInvariant();
        }
        if (!canHaveAsDescendent(addedChild)) {
            node = (BaseElement) addedChild.clone();
        }
        appendChildNoChecking(node);
        notifyObservers();
        if (DEBUG) {
            checkInvariant();
        }
    }

    /**
     * @param addedChild --
     */
    public final void appendChildNoChecking(final BaseNode addedChild) {
        if (DEBUG) {
            checkInvariant();
        }
        BaseElement oldParent = addedChild.getParentNode();
        if (oldParent != null) {
            oldParent.removeChildNoChecking(addedChild);
        }
        addedChild.insertAtEndOfLinkedList(lastChild);
        if (firstChild == null) {
            firstChild = addedChild;
        }
        addedChild.setParentNode(this);
        //!children_.add/*Element*/( addedChild );
        lastChild = addedChild;
        addedChild.setOwnerDocument(getOwnerDocument());
        if (DEBUG) {
            checkInvariant();
        }
    }

    /**
     * @param node --
     * @return --
     */
    final boolean canHaveAsDescendent(final BaseNode node) {
        if (node == this) {
            return false;
        }
        BaseElement parent = getParentNode();
        if (parent == null) {
            return true;
        }
        return parent.canHaveAsDescendent(node);
    }

    /**
     * 
     */
    private void checkInvariant() {
        if (DEBUG) {
            if (tagName != Sparta.intern(tagName)) {
                throw new Error("tagname not interned");
            }
            if (attributeNames != null) {
                for (Enumeration i = attributeNames.elements(); i
                        .hasMoreElements();) {
                    String name = (String) i.nextElement();
                    if (name.indexOf(' ') != -1) {
                        throw new Error("Bad attribute " + name);
                    }
                }
            }
        }
    }

    /**
     * Create a deep clone of this Element. It will have the tagname and
     * attributes as this node. This method will be called recursively to copy
     * the while subtree of child Elements and Text nodes.
     * 
     * @return --
     */
    public final Object clone() {
        return cloneElement(true);
    }

    /**
     * Create a clone of this node. It will have the tagname and attributes as
     * this node. If deep is true, this method will be called recursively to
     * copy the while subtree of child Elements and text nodes.
     * 
     * @param deep --
     * @return --
     */
    public final BaseElement cloneElement(final boolean deep) {
        BaseElement result = new BaseElement(tagName);
        if (attributeNames != null) {
            for (Enumeration i = attributeNames.elements(); i
                    .hasMoreElements();) {
                String name = (String) i.nextElement();
                result.setAttribute(name, (String) attributes.get(name));
            }
        }
        if (deep) {
            for (BaseNode n = firstChild; n != null; n = n.getNextSibling()) {
                result.appendChild((BaseNode) n.clone());
            }
        }
        return result;

    }

    /**
     * Create a shallow clone of this Element. It will have the tagname and
     * attributes as this Element but will not have child Elements or Nodes.
     * 
     * @return --
     */
    public final BaseElement cloneShallow() {
        return cloneElement(false);
    }

    /** 
     * Called whenever cached version of hashCode needs to be regenerated.
     * 
     * @return --
     * @see BaseNode#computeHashCode() 
     */
    protected final int computeHashCode() {
        int hash = tagName.hashCode();

        if (attributes != null) {
            for (Enumeration i = attributes.keys(); i.hasMoreElements();) {
                String key = (String) i.nextElement();
                hash = HASH_VALUE * hash + key.hashCode();
                String value = (String) attributes.get(key);
                hash = HASH_VALUE * hash + value.hashCode();
            }
        }

        for (BaseNode i = firstChild; i != null; i = i.getNextSibling()) {
            hash = HASH_VALUE * hash + i.hashCode();
        }
        return hash;
    }

    /**
     * To be equal elements must have the same tagname, they must have the same
     * children (applying equals recursivly) in the same order and they must
     * have the same attributes in any order. Elements can be equal even if they
     * are in different documents, have different parents, have different
     * siblings, or have different annotations.
     * 
     * @param thatO --
     * @return --
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public final boolean equals(final Object thatO) {

        //Do cheap tests first
        if (this == thatO) {
            return true;
        }
        if (!(thatO instanceof BaseElement)) {
            return false;
        }
        BaseElement that = (BaseElement) thatO;
        if (!this.tagName.equals(that.tagName)) {
            return false;
        }
        //!if( this.children_.size() != that.children_.size() )
        //! return false;
        int thisAttrCount = this.attributes == null ? 0 : this.attributes
                .size();
        int thatAttrCount = that.attributes == null ? 0 : that.attributes
                .size();
        if (thisAttrCount != thatAttrCount) {
            return false;
        }

        //Compare attributes ignoring order (we already know the
        //number is the same)
        if (attributes != null) {
            for (Enumeration i = attributes.keys(); i.hasMoreElements();) {
                String key = (String) i.nextElement();
                String thisValue = (String) this.attributes.get(key);
                //non-null
                String thatValue = (String) that.attributes.get(key);
                //maybe null
                if (!thisValue.equals(thatValue)) {
                    return false;
                }
            }
        }

        //Compare children in order (we already know the number is the same)
        BaseNode thisChild = this.firstChild;
        BaseNode thatChild = that.firstChild;
        while (thisChild != null) {
            if (!thisChild.equals(thatChild)) {
                return false;
            }
            thisChild = thisChild.getNextSibling();
            thatChild = thatChild.getNextSibling();
        }

        return true;
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
     * @param name --
     * @return value of attribute that has this name or null if no such
     *         attribute. WARNING! Unlike in a previous version of this API,
     *         this string is not interned therefore you must do a.equals(b)
     *         instead of a==b when comparing attribute values.
     */
    public final String getAttribute(final String name) {
        return attributes == null ? null : (String) attributes.get(name);
    }

    /** 
     * Return enumeration of Strings
     * 
     * @return -- 
     */
    public final Enumeration getAttributeNames() {
        if (attributeNames == null) {
            return BaseDocument.EMPTY;
        }
        return attributeNames.elements();
            
    }

    /**
     * @return either an Element or a Text node
     */
    public final BaseNode getFirstChild() {
        return firstChild;
    }

    /**
     * @return either an Element or a Text node
     */
    public final BaseNode getLastChild() {
        return lastChild;
    }

    /** 
     * @return tag as interned string. 
     */
    public final String getTagName() {
        return tagName;
    }

    /** 
     * remove this attribute if it exists, otherwise silently do nothing.
     * 
     * @param name -- 
     */
    public final void removeAttribute(final String name) {
        if (attributes == null) {
            return;
        }
        attributes.remove(name);
        attributeNames.removeElement(name);
        notifyObservers();
    }

    /**
     * @param childToRemove --
     * @throws DOMException --
     */
    public final void removeChild(final BaseNode childToRemove)
    		throws DOMException {
        
        boolean found = removeChildNoChecking(childToRemove);
        if (!found) {
            throw new DOMException(DOMException.NOT_FOUND_ERR, "Cannot find "
                    + childToRemove + " in " + this);
        }
        notifyObservers();
        if (DEBUG) {
            checkInvariant();
        }
    }

    /**
     * @param childToRemove --
     * @return --
     */
    private boolean removeChildNoChecking(final BaseNode childToRemove) {
        if (DEBUG) {
            checkInvariant();
        }
        int i = 0;
        for (BaseNode child = firstChild; child != null; child = child
                .getNextSibling()) {
            if (child.equals(childToRemove)) {

                //Fix up list endpoints if necessary
                if (firstChild == child) {
                    firstChild = child.getNextSibling();
                }
                if (lastChild == child) {
                    lastChild = child.getPreviousSibling();
                }

                child.removeFromLinkedList();
                child.setParentNode(null);

                child.setOwnerDocument(null);

                if (DEBUG) {
                    checkInvariant();
                }
                return true;
            }
            ++i;
        }
        if (DEBUG) {
            checkInvariant();
        }
        return false;
    }

    /**
     * Replace oldChild with newChild.
     * 
     * @param newChild --
     * @param oldChild --
     * @throws DOMException
     *             if oldChild object is not a child.
     */
    public final void replaceChild(final BaseElement newChild, 
            					   final BaseNode oldChild)
            throws DOMException {
        
        internalReplace(newChild, oldChild);
        notifyObservers();
    }

    /**
     * Replace oldChild with newChild.
     * 
     * @param newChild --
     * @param oldChild --
     * @throws DOMException
     *             if oldChild object is not a child.
     */
    public final void replaceChild(final BaseText newChild, 
                                   final BaseNode oldChild) 
    	                                   throws DOMException {
        
        internalReplace(newChild, oldChild);
        notifyObservers();
    }

    /**
     * @param newChild --
     * @param oldChild --
     * @throws DOMException --
     */
    private void internalReplace(final BaseNode newChild, 
                                 final BaseNode oldChild) throws DOMException {
        int i = 0;
        for (BaseNode child = firstChild; child != null; child = child
                .getNextSibling()) {
            if (child == oldChild) {

                //Fix up list endpoints if necessary
                if (firstChild == oldChild) {
                    firstChild = newChild;
                }
                if (lastChild == oldChild) {
                    lastChild = newChild;
                }

                //Make oldChild's neighbouring siblings point to newChild
                oldChild.replaceInLinkedList(newChild);

                //Fix parent pointers
                newChild.setParentNode(this);
                oldChild.setParentNode(null);

                return;
            }
            ++i;
        }
        throw new DOMException(DOMException.NOT_FOUND_ERR, "Cannot find "
                + oldChild + " in " + this);
    }

    /**
     * @param name
     *            attribute name which must be non-null, non empty
     * @param value
     *            attribue value.
     * @precondition non zero-length name
     */
    public final void setAttribute(final String name, final String value) {
        if (attributes == null) {
            attributes = new Hashtable();
            attributeNames = new Vector();
        }
        if (attributes.get(name) == null) {
            attributeNames.addElement(name);
        }
        attributes.put(name, value);
        notifyObservers();
        if (DEBUG) {
            checkInvariant();
        }
    }

    /**
     * @param tagNameIn --
     */
    public final void setTagName(final String tagNameIn) {
        tagName = Sparta.intern(tagNameIn);
        notifyObservers();
    }

    /** 
     * Accumlate text nodes hierarchically.
     * 
     * @param writer --
     * @throws IOException ..
     * @see BaseNode#toString(Writer) 
     */
    final void toString(final Writer writer) throws IOException {
        for (BaseNode i = firstChild; i != null; i = i.getNextSibling()) {
            i.toString(writer);
        }
    }

    /** 
     * Write XML representation to character stream.
     * 
     * @param writer --
     * @throws IOException --
     * @see BaseNode#toXml(Writer) 
     */
    public final void toXml(final Writer writer) throws IOException {
        writer.write("<" + tagName);
        if (attributeNames != null) {
            for (Enumeration i = attributeNames.elements(); i
                    .hasMoreElements();) {
                String name = (String) i.nextElement();
                String value = (String) attributes.get(name);
                writer.write(" " + name + "=\"");
                htmlEncode(writer, value);
                writer.write("\"");
            }
        }
        if (firstChild == null) {
            writer.write("/>");
        } else {
            writer.write(">");
            for (BaseNode i = firstChild; i != null; i = i.getNextSibling()) {
                i.toXml(writer);
            }
            writer.write("</" + tagName + ">");
        }
    }

    /**
     * @param xpath --
     * @param expectStringValue --
     * @return --
     * @throws XPathException --
     */
    private XPathVisitor visitor(final String xpath, 
                                 final boolean expectStringValue)
            throws XPathException {
        XPath parseTree = XPath.get(xpath);
        if (parseTree.isStringValue() != expectStringValue) {
            String msg = expectStringValue ? "evaluates to element not string"
                    : "evaluates to string not element";
            throw new XPathException(parseTree, "\"" + parseTree
                    + "\" evaluates to " + msg);
        }
        return new XPathVisitor(this, parseTree);
    }

    /**
     * Make sure this XPath exists, creating nodes if necessary, returning true
     * if any nodes created. Xpath must of the type that returns an element (not
     * a string).
     * 
     * @param xpath --
     * @return --
     * @throws ParseException --
     */
    public final boolean xpathEnsure(final String xpath) throws ParseException {
        try {

            //Quick exit for common case
            if (xpathSelectElement(xpath) != null) {
                return false;
            }

            //Split XPath into parent steps and last step
            final XPath parseTree = XPath.get(xpath);
            int stepCount = 0;
            for (Enumeration i = parseTree.getSteps(); i.hasMoreElements();) {
                i.nextElement();
                ++stepCount;
            }
            Step[] parentSteps = new Step[stepCount - 1];
            Enumeration i = parseTree.getSteps();
            for (int j = 0; j < parentSteps.length; ++j) {
                parentSteps[j] = (Step) i.nextElement();
            }
            Step lastStep = (Step) i.nextElement();

            BaseElement parent;
            if (parentSteps.length == 0) {
                parent = this;
            } else {
                String parentXPath = XPath.get(parseTree.isAbsolute(),
                        parentSteps).toString();
                xpathEnsure(parentXPath.toString()); //recursion
                parent = xpathSelectElement(parentXPath);
            }

            BaseElement newChild = makeMatching(parent, lastStep, xpath);
            parent.appendChildNoChecking(newChild);
            return true;

        } catch (XPathException e) {
            throw new ParseException(xpath, e);
        }
    }

    /**
     * Select the first element that matches the relative XPath expression with
     * respect to this element, or null if there is no match.
     * 
     * @todo make more efficient by short-circuiting the search.
     * @param xpath --
     * @return ..
     * @throws ParseException ..
     */
    public final BaseElement xpathSelectElement(final String xpath) 
    		throws ParseException {
        try {

            if (DEBUG) {
                BaseDocument doc = getOwnerDocument();
                if (doc != null && this == doc.getDocumentElement()) {
                    XPath parseTree = XPath.get(xpath);
                    doc.monitor(parseTree);
                }
            }
            return visitor(xpath, false).getFirstResultElement();

        } catch (XPathException e) {
            throw new ParseException("XPath problem", e);
        }
    }

    /**
     * Select all the elements that match the relative XPath expression with
     * respect to this element.
     * 
     * @param xpath ..
     * @return ..
     * @throws ParseException ..
     */
    public final Enumeration xpathSelectElements(final String xpath) 
    		throws ParseException {
        try {

            if (DEBUG) {
                BaseDocument doc = getOwnerDocument();
                if (doc != null && this == doc.getDocumentElement()) {
                    XPath parseTree = XPath.get(xpath);
                    doc.monitor(parseTree);
                }
            }

            return visitor(xpath, false).getResultEnumeration();

        } catch (XPathException e) {
            throw new ParseException("XPath problem", e);
        }
    }

    /**
     * Select the first element that matches the relative XPath expression with
     * respect to this element, or null if there is no match.
     * 
     * @param xpath ..
     * @return ..
     * @throws ParseException ..
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
     * Select all the strings that match the relative XPath expression with
     * respect to this element.
     * 
     * @param xpath ..
     * @return ..
     * @throws ParseException ..
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

// $Log: Element.java,v $
// Revision 1.10 2003/10/28 00:25:11 eobrain
// Whitespace change only.
//
// Revision 1.9 2003/07/17 23:53:19 eobrain
// Make compatiblie with J2ME. For example do not use "new"
// java.util classes.
//
// Revision 1.8 2003/06/24 23:54:32 eobrain
// Fix bug that was causing duplicate identical text-node children to be
// deleted.
// Remove unnecessary hashset of children.
//
// Revision 1.7 2003/06/19 20:28:20 eobrain
// Hash code optimization.
// Add monitoring (in debug mode) to detect when indexing could optimize.
//
// Revision 1.6 2003/05/12 19:56:36 eobrain
// Performance improvements including interning of tagname and attribute values.
//
// Revision 1.5 2003/01/27 23:30:58 yuhongx
// Replaced Hashtable with HashMap.
//
// Revision 1.4 2002/12/13 23:09:24 eobrain
// Fix javadoc.
//
// Revision 1.3 2002/12/13 18:12:17 eobrain
// Fix xpathEnsure to handle case when the XPath given specifies a root node
// tagname that conflicts with the existing root node. Extend xpathEnsure to
// work with any type of predicate. Replace hacky string manipulation code with
// code that works on the XPath parse tree.
//
// Revision 1.2 2002/10/30 16:37:25 eobrain
// Fix Element.appendChild so that it cannot create invalid loop by
// adding ancestor. No longer throw DOMException. (WARNING: This breaks
// backwards compatibility. Client code that catches or propagates
// DOMException because of this call will now give a compile error.)
//
// Revision 1.1.1.1 2002/08/19 05:03:55 eobrain
// import from HP Labs internal CVS
//
// Revision 1.23 2002/08/18 04:20:52 eob
// Sparta no longer throws XPathException -- it throws ParseException
// instead.
//
// Revision 1.22 2002/08/15 23:40:22 sermarti
//
// Revision 1.21 2002/08/15 21:29:00 eob
// Constructor no longer needs documenent.
//
// Revision 1.20 2002/08/15 05:08:21 eob
// Notify observers.
//
// Revision 1.19 2002/07/25 21:10:15 sermarti
// Adding files that mysteriously weren't added from Sparta before.
//
// Revision 1.18 2002/06/21 00:25:47 eob
// Make work with old JDK 1.1.*
//
// Revision 1.17 2002/06/15 22:16:49 eob
// Comment change only. Fix javadoc problem.
//
// Revision 1.16 2002/06/14 19:37:15 eob
// Make toString of Node do the same as in XSLT -- recursive
// concatenation of all text in text nodes.
//
// Revision 1.15 2002/05/23 21:22:44 eob
// Better error reporting.
//
// Revision 1.14 2002/05/11 00:10:18 eob
// Remove stub method that is now implemented with a slightly different
// name in Node.
//
// Revision 1.13 2002/05/10 21:03:19 eob
// Add equals method.
//
// Revision 1.12 2002/05/09 16:48:40 eob
// Add replaceChild. Fix cloneChild.
//
// Revision 1.11 2002/03/28 01:23:18 jrowson
// fixed bugs related to client side caching
//
// Revision 1.10 2002/03/26 01:45:39 eob
// Deprecate XPathAPI
//
// Revision 1.9 2002/02/23 02:04:44 eob
// Add clone method. Tweak toXml API.
//
// Revision 1.8 2002/02/15 21:20:10 eob
// Rename xpath* methods to xpathSelect* to make more obvious.
//
// Revision 1.7 2002/02/15 21:05:55 eob
// Add convenient xpath* methods, allowing a more object-oriented use than
// XPathAPI.
//
// Revision 1.6 2002/02/01 21:50:27 eob
// Move toXml up to Node
//
// Revision 1.5 2002/01/05 07:53:28 eob
// Factor out some functionality into Node.
//
// Revision 1.4 2002/01/04 00:37:50 eob
// add annotation
//
// Revision 1.3 2002/01/04 16:49:17 eob
// Fix indentation.
//
// Revision 1.2 2002/01/04 16:47:59 eob
// Move parse functionality functionality to ParseSource.
//
// Revision 1.1 2001/12/19 05:52:38 eob
// initial
