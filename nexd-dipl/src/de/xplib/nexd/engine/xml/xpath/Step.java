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

import java.io.IOException;


/**
 * One of the steps which, separated by slashes, make up an XPath expression.
 *
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 * @link http://sparta.sourceforge.net
 */
/**
 *
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 */
public class Step {

    /**
     * @label uses
     */
    /*#de.xplib.nexd.engine.xml.xpath.ExprFactory Dependency_Link*/

    /**
     * @label uses
     */

    /*#de.xplib.nexd.engine.xml.xpath.XPathTokenizer Dependency_Link1*/

    /**
     * Comment for <code>DOT</code>
     */
    /*
     public static final Step DOT = new Step(
     ThisNodeTest.INSTANCE, TrueExpr.INSTANCE);*/

    /**
     * Comment for <code>multiLevel</code>
     */
    private final boolean multiLevel;

    /**
     * @clientCardinality 1
     * @clientRole nodeTest
     * @directed
     * @link aggregationByValue
     * @supplierCardinality 1
     */
    private final AbstractNodeTest nodeTest;

    /**
     * @clientCardinality 1
     * @clientRole predicate
     * @directed
     * @link aggregationByValue
     * @supplierCardinality 0..1
     */
    private final AbstractBooleanExpr predicate;

    /**
     * Factory method that creates a step.
     *
     * @param xpath ..
     * @param multiLevelIn ..
     * @param toks ..
     * @param blockIn ..
     * @return ..
     * @throws XPathExceptionExt ..
     * @throws IOException ..
     */
    protected static Step createStep(final XPath xpath,
            final boolean multiLevelIn, final XPathTokenizer toks,
            final boolean blockIn) throws XPathExceptionExt, IOException {

        return new Step(xpath, multiLevelIn, toks, blockIn);
    }

    /**
     *
     * @param nodeTestIn ---
     * @param predicateIn ---
     */
    Step(final AbstractNodeTest nodeTestIn,
            final AbstractBooleanExpr predicateIn) {
        super();

        nodeTest = nodeTestIn;
        predicate = predicateIn;
        multiLevel = false;
    }

    /**
     * @param xpath ---
     * @param multiLevelIn --
     * @param toks ---
     * @throws XPathExceptionExt ---
     * @throws IOException ---
     * @precondition current token is 1st token in the node_test production.
     * @postcondition current tok is tok after last tok of step production
     */
    Step(final XPath xpath, final boolean multiLevelIn,
            final XPathTokenizer toks) throws XPathExceptionExt, IOException {

        this(xpath, multiLevelIn, toks, false);
    }

    /**
     * @param xpath ---
     * @param multiLevelIn --
     * @param toks ---
     * @param blockIn ---
     * @throws XPathExceptionExt ---
     * @throws IOException ---
     * @precondition current token is 1st token in the node_test production.
     * @postcondition current tok is tok after last tok of step production
     */
    Step(final XPath xpath, final boolean multiLevelIn,
            final XPathTokenizer toks, final boolean blockIn)
            throws XPathExceptionExt, IOException {

        super();

        multiLevel = multiLevelIn;
        nodeTest = this.getNodeTest(xpath, toks, blockIn);
        predicate = this.getPredicate(xpath, toks, blockIn);

        //current token is token after step production
    }

    /**
     * @return ---
     */
    public final AbstractNodeTest getNodeTest() {
        return nodeTest;
    }

    /**
     * @return ---
     */
    public final AbstractBooleanExpr getPredicate() {
        return predicate;
    }

    /**
     * Is this step preceeded by a '//' ?
     *
     * @return ---
     */
    public final boolean isMultiLevel() {
        return multiLevel;
    }

    /**
     * Does this step evaluate to a string values (attribute values or text()
     * nodes)
     *
     * @return ---
     */
    public final boolean isStringValue() {
        return nodeTest.isStringValue();
    }

    /**
     * <Some description here>
     *
     * @return ---
     * @see java.lang.Object#toString()
     */
    public final String toString() {
        return nodeTest.toString() + predicate.toString();
    }

    /**
     * @param xpath ..
     * @param toks ..
     * @param blockIn ..
     * @return ..
     * @throws IOException ..
     * @throws XPathExceptionExt ..
     */
    private AbstractNodeTest getNodeTest(final XPath xpath,
            final XPathTokenizer toks, final boolean blockIn)
            throws IOException, XPathExceptionExt {

        AbstractNodeTest test;
        if (blockIn) {
            test = NullTest.INSTANCE;
        } else {
            switch (toks.ttype) {
            case '.':
                if (toks.nextToken() == '.') {
                    test = ParentNodeTest.INSTANCE;
                } else {
                    toks.pushBack();
                    test = ThisNodeTest.INSTANCE;
                }
                break;
            case '*':
                test = AllElementTest.INSTANCE;
                break;
            case '@':
                if (toks.nextToken() != XPathTokenizer.TT_WORD) {
                    throw new XPathExceptionExt(xpath, "after @ in node test",
                            toks, "name");
                }
                test = new AttrTest(toks.sval);
                break;
            case XPathTokenizer.TT_WORD:
                test = this.getWordNodeTest(xpath, toks);
                break;
            default:
                throw new XPathExceptionExt(xpath, "at begininning of step",
                        toks, "'.' or '*' or name");
            }
        }
        return test;
    }

    /**
     * @param xpath ..
     * @param toks ..
     * @return ..
     * @throws IOException ,.,
     * @throws XPathExceptionExt ..
     */
    private AbstractNodeTest getWordNodeTest(final XPath xpath,
            final XPathTokenizer toks) throws IOException, XPathExceptionExt {

        AbstractNodeTest test;
        if (toks.sval.equals("text")) {
            if (toks.nextToken() != '(' || toks.nextToken() != ')') {
                throw new XPathExceptionExt(xpath, "after text", toks, "()");
            }
            test = TextTest.INSTANCE;
        } else {
            test = new ElementTest(toks.sval);
        }
        return test;
    }

    /**
     * @param xpath ..
     * @param toks ..
     * @param blockIn ..
     * @return ..
     * @throws IOException ..
     * @throws XPathExceptionExt ..
     */
    private AbstractBooleanExpr getPredicate(final XPath xpath,
            final XPathTokenizer toks, final boolean blockIn)
            throws IOException, XPathExceptionExt {

        AbstractBooleanExpr predic;
        if (blockIn || toks.nextToken() == '[') {
            if (!blockIn) {
                toks.nextToken();
            }
            //current token is first token in expr production
            AbstractBooleanExpr pred = ExprFactory.createExpr(xpath, toks);
            //current token is 1st token after expr production

            boolean skip = false;

            if (toks.sval.equals("and")) {
                toks.nextToken();
                if (toks.sval.equals("not")) {
                    toks.nextToken();
                    predic = new AndExpr(new NotExpr(pred));
                } else {
                    predic = new AndExpr(pred);
                }
                skip = true;
            } else if (toks.sval.equals("or")) {
                toks.nextToken();
                if (toks.sval.equals("not")) {
                    toks.nextToken();
                    predic = new OrExpr(new NotExpr(pred));
                } else {
                    predic = new OrExpr(pred);
                }
                skip = true;
            } else {
                predic = pred;
            }

            if (!skip) {
                if (toks.ttype != ']') {
                    throw new XPathExceptionExt(xpath,
                            "after predicate expression", toks, "]");
                }
                toks.nextToken();
            }
        } else {
            predic = TrueExpr.INSTANCE;
        }
        return predic;
    }
}