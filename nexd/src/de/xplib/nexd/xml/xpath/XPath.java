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
package de.xplib.nexd.xml.xpath;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Stack;

/**
 * The root of the parse tree for an XPath expression. This is a participant in
 * the Visitor Pattern [Gamma et al, #331]. You create an XPath object (which
 * parses an XPath expression into a parse tree), create a Vistor object, and
 * pass the visitor to the XPath.accept method.
 * 
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 * @link http://sparta.sourceforge.net
 */
public final class XPath {

    /**
     *  
     */
    private static final int ASSERTION = 0;

    /**
     * @associates XPath Warning grows without bound, no eviction. TODO : make
     *             LRU
     */
    private static Hashtable cache = new Hashtable();

    /**
     * Create an XPath from some steps. The value of steps.length must be >= 1
     * 
     * @param isAbsolute
     *            ---
     * @param steps
     *            ---
     * @return ---
     */
    public static XPath get(final boolean isAbsolute, final Step[] steps) {
        XPath created = new XPath(isAbsolute, steps);
        String xpathString = created.toString();
        synchronized (cache) {
            XPath inCache = (XPath) cache.get(xpathString);
            if (inCache == null) {
                cache.put(xpathString, created);
                return created;
            } else {
                return inCache;
            }
        }
    }

    ////////////////////////////////////////////////////////////////

    /**
     * Return the xpath parse-tree object for this expression. This may be
     * either a newly created object or a cached copy.
     * 
     * @param xpathString
     *            ---
     * @return ---
     * @throws XPathException
     *             ---
     */
    public static XPath get(final String xpathString) throws XPathException {
        //, IOException
        synchronized (cache) {
            XPath result = (XPath) cache.get(xpathString);
            if (result == null) {
                result = new XPath(xpathString);
                cache.put(xpathString, result);
            }
            return result;
        }
    }

    /**
     * Convenience function equivalent to get(xpathString).isStringValue()
     * 
     * @param xpathString
     *            ---
     * @return ---
     * @throws XPathException
     *             ---
     * @throws IOException
     *             ---
     */
    public static boolean isStringValue(final String xpathString)
            throws XPathException, IOException {
        return get(xpathString).isStringValue();
    }

    /**
     *  
     */
    private boolean absolute;

    /**
     * @link aggregation
     * @associates <{Step}>
     */
    private Stack steps = new Stack();

    /**
     *  
     */
    private String string;

    /**
     * Create an XPath from some steps. steps.lenght must be >= 1
     * 
     * @param isAbsolute
     *            ---
     * @param stepsIn
     *            ---
     */
    private XPath(final boolean isAbsolute, final Step[] stepsIn) {
        for (int i = 0; i < stepsIn.length; ++i) {
            steps.addElement(stepsIn[i]);
        }
        absolute = isAbsolute;
        string = null;
    }

    /**
     * Parse an XPath from a string.
     * 
     * @param s
     *            ---
     * @throws XPathException
     *             ---
     */
    private XPath(final String s) throws XPathException { //, IOException
        //StringReader not supported in J2ME
        this(s, new InputStreamReader(new ByteArrayInputStream(s.getBytes())));
    }

    /**
     * Parse an XPath from a character stream.
     * 
     * @param s
     *            ---
     * @param reader
     *            ---
     * @throws XPathException
     *             ---
     */
    private XPath(final String s, final Reader reader) throws XPathException {
        try {
            string = s;
            SimpleStreamTokenizer toks = new SimpleStreamTokenizer(reader);
            toks.ordinaryChar('/'); // '/' is not a comment
            toks.ordinaryChar('.'); // '.' is not a part of a number
            toks.wordChars(':', ':'); //Allow namespaces
            toks.wordChars('_', '_'); //Allow namespaces

            boolean multiLevel;
            if (toks.nextToken() == '/') {
                absolute = true;
                if (toks.nextToken() == '/') {
                    multiLevel = true;
                    toks.nextToken();
                } else {
                    multiLevel = false;
                }
            } else {
                absolute = false;
                multiLevel = false;
            }
            //current token is first token of node_test production
            steps.push(new Step(this, multiLevel, toks));
            //current token is token after last token of step production

            while (toks.ttype == '/') {
                if (toks.nextToken() == '/') {
                    multiLevel = true;
                    toks.nextToken();
                } else {
                    multiLevel = false;
                }
                //current token is first token of node_test production
                steps.push(new Step(this, multiLevel, toks));
                //current token is token after last token of step production
            }

            if (toks.ttype != SimpleStreamTokenizer.TT_EOF) {
                throw new XPathException(this, "at end of XPATH expression",
                        toks, "end of expression");
            }
        } catch (IOException e) {
            throw new XPathException(this, e);
        }
        if (ASSERTION >= 2) {
            if (!toString().equals(generateString())) {
                throw new Error("Postcondition failed");
            }
        }
    }

    /**
     * A one-level clone in which the steps list is cloned but not the steps
     * objects themselves. It is OK for different XPaths to share Steps because
     * they are immutable.
     * 
     * @return A copy
     */
    public Object clone() {
        Step[] iSteps = new Step[steps.size()];
        Enumeration step = steps.elements();
        for (int i = 0; i < iSteps.length; ++i) {
            iSteps[i] = (Step) step.nextElement();
        }
        return new XPath(absolute, iSteps);
    }

    /**
     * 
     * @return ---
     */
    private String generateString() {
        StringBuffer result = new StringBuffer();
        boolean first = true;
        for (Enumeration i = steps.elements(); i.hasMoreElements();) {
            Step step = (Step) i.nextElement();
            if (!first || absolute) {
                result.append('/');
                if (step.isMultiLevel()) {
                    result.append('/');
                }
            }
            result.append(step.toString());
            first = false;
        }
        return result.toString();
    }

    /**
     * Return the attribute name in a trailing [@attrName] predicate. For
     * example if the Xpath expression was "/a/b[@p='pp']/c[@q]" then the
     * indexing attribute name would be "q"
     * 
     * @return ---
     * @throws XPathException
     *             ---
     */
    public String getIndexingAttrName() throws XPathException {
        Step step = (Step) steps.peek();
        BooleanExpr predicate = step.getPredicate();
        if (!(predicate instanceof AttrExistsExpr)) {
            throw new XPathException(this,
                    "has no indexing attribute name (must end with "
                            + "predicate of the form [@attrName]");
        }
        return ((AttrExistsExpr) predicate).getAttrName();
    }

    /**
     * Return the attribute name in a trailing [@attrName='attrValue'] predicate
     * or null if it someother type of predicate.. For example if the Xpath
     * expression was "/a/b[@p='pp']/c[@q='qqq']" then the indexing attribute
     * name would be "q"
     * 
     * @return ---
     * @throws XPathException
     *             ----
     */
    public String getIndexingAttrNameOfEquals() throws XPathException {
        Step step = (Step) steps.peek();
        BooleanExpr predicate = step.getPredicate();
        if (predicate instanceof AttrEqualsExpr) {
            return ((AttrEqualsExpr) predicate).getAttrName();
        }
        return null;
    }

    /**
     * 
     * @return ---
     */
    public Enumeration getSteps() {
        return steps.elements();
    }

    /**
     * Does this path begin with a '/' or '//' ?
     * 
     * @return ---
     */
    public boolean isAbsolute() {
        return absolute;
    }

    /**
     * Does xpath evaluate to a string values (attribute values or text() nodes)
     * 
     * @return ---
     */
    public boolean isStringValue() {
        Step lastStep = (Step) steps.peek();
        return lastStep.isStringValue();
    }

    /**
     * <Some description here>
     * 
     * @return ---
     * @see java.lang.Object#toString()
     */
    public String toString() {
        if (string == null) {
            string = generateString();
        }
        return string;
    }

}

// $Log: XPath.java,v $
// Revision 1.10 2003/07/18 00:01:42 eobrain
// Make compatiblie with J2ME. For example do not use "new"
// java.util classes.
//
// Revision 1.9 2003/06/19 20:29:04 eobrain
// Add monitoring (in debug mode) to detect when indexing could optimize.
//
// Revision 1.8 2003/05/12 20:08:10 eobrain
// Inconsequential code change to avoid eclipse warning.
//
// Revision 1.7 2003/03/21 00:21:25 eobrain
// Allow underscores in attribute names.
//
// Revision 1.6 2003/01/27 23:30:58 yuhongx
// Replaced Hashtable with HashMap.
//
// Revision 1.5 2003/01/09 01:17:14 yuhongx
// Use JDK1.1 API to make code work with PersonalJava (use addElement()).
//
// Revision 1.4 2002/12/13 22:42:22 eobrain
// Fix javadoc.
//
// Revision 1.3 2002/12/13 18:09:34 eobrain
// Create XPath from a sequence of steps.
//
// Revision 1.2 2002/12/06 23:41:49 eobrain
// Add toString() which returns the original XPath.
//
// Revision 1.1.1.1 2002/08/19 05:04:02 eobrain
// import from HP Labs internal CVS
//
// Revision 1.9 2002/08/18 23:39:27 eob
// Add copyright and other formatting and commenting in preparation for
// release to SourceForge.
//
// Revision 1.8 2002/08/15 05:11:35 eob
// getIndexingAttrName
//
// Revision 1.7 2002/06/21 00:35:19 eob
// Make work with old JDK 1.1.*
//
// Revision 1.6 2002/06/04 05:27:08 eob
// Simplify use of visitor pattern to make code easier to understand.
//
// Revision 1.5 2002/05/23 21:14:51 eob
// Better error reporting.
//
// Revision 1.4 2002/05/10 19:42:48 eob
// Add static isStringValue
//
// Revision 1.3 2002/03/26 05:31:49 eob
// Making contructor private. Adding static factory method to manage
// cache of XPath objects.
//
// Revision 1.2 2002/02/04 22:12:59 eob
// Add boolean property to test whether this xpath returns a string.
//
// Revision 1.1 2002/02/01 01:59:09 eob
// initial
