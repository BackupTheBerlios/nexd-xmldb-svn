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
package de.xplib.nexd.engine.xml.xpath;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

import org.apache.commons.collections.FastHashMap;

import de.xplib.nexd.xml.xpath.XPathException;
import de.xplib.nexd.xml.xpath.XPathI;

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
public final class XPath implements XPathI {

    /**
     * @label uses
     */
    /*#de.xplib.nexd.engine.xml.xpath.XPathTokenizer Dependency_Link*/

    /**
     *
     */
    private static final int ASSERTION = 0;

    /**
     * @associates XPath Warning grows without bound, no eviction.
     */
    private static Map cache = new FastHashMap();

    static {
        ((FastHashMap) cache).setFast(true);
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
    protected static XPath get(final String xpathString) throws XPathException {
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
     * @associates <{Step}>
     * @clientCardinality 1
     * @clientRole steps
     * @directed true
     * @label creates
     * @link aggregation
     * @supplierCardinality 0..*
     */
    private final Stack steps = new Stack();

    /**
     *
     */
    private String string = null;

    /**
     * Parse an XPath from a string.
     *
     * @param xpathIn
     *            ---
     * @throws XPathExceptionExt
     *             ---
     */
    private XPath(final String xpathIn) throws XPathException {
        this(xpathIn, new InputStreamReader(new ByteArrayInputStream(xpathIn
                .getBytes())));
    }

    /**
     * Parse an XPath from a character stream.
     *
     * @param xpathIn
     *            ---
     * @param reader
     *            ---
     * @throws XPathExceptionExt
     *             ---
     */
    private XPath(final String xpathIn, final Reader reader)
            throws XPathException {

        super();

        try {
            string = xpathIn;
            XPathTokenizer toks = new XPathTokenizer(reader);
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
            Step step = (Step) steps.push(new Step(this, multiLevel, toks));
            //current token is token after last token of step production

            boolean block = (step.getPredicate() instanceof AbstractBinExpr);

            while (block || toks.ttype == '/') {

                if (!block && toks.nextToken() == '/') {
                    multiLevel = true;
                    toks.nextToken();
                } else {
                    multiLevel = false;
                }

                //current token is first token of node_test production
                step = (Step) steps.push(
                //new Step(this, multiLevel, toks, block));
                        Step.createStep(this, multiLevel, toks, block));
                //current token is token after last token of step production

                while (toks.sval.equals("and") || toks.sval.equals("or")) {
                    //steps.push(new Step(this, false, toks, true));
                    steps.push(Step.createStep(this, false, toks, true));
                }
                block = false;
            }
            if (toks.ttype != XPathTokenizer.TT_EOF) {
                throw new XPathExceptionExt(this, "at end of XPATH expression",
                        toks, "end of expression");
            }
        } catch (IOException e) {
            throw new XPathExceptionExt(this, e);
        }
        if (ASSERTION >= 2) {
            if (!toString().equals(generateString())) {
                throw new XPathExceptionExt(this, "Postcondition failed");
            }
        }
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
     * @throws XPathExceptionExt
     *             ---
     */
    public String getIndexingAttrName() throws XPathExceptionExt {
        Step step = (Step) steps.peek();
        AbstractBooleanExpr predicate = step.getPredicate();
        if (!(predicate instanceof AttrExistsExpr)) {
            throw new XPathExceptionExt(this,
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
     * @throws XPathExceptionExt
     *             ----
     */
    public String getIndexingAttrNameOfEquals() throws XPathExceptionExt {
        Step step = (Step) steps.peek();
        AbstractBooleanExpr predicate = step.getPredicate();

        String result = null;
        if (predicate instanceof AttrEqualsExpr) {
            result = ((AttrEqualsExpr) predicate).getAttrName();
        }
        return result;
    }

    /**
     *
     * @return ---
     */
    public Iterator getSteps() {
        return steps.iterator();
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