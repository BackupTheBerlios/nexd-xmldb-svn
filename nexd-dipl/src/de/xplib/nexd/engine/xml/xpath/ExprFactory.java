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

/*
 * $Log: ExprFactory.java,v $
 * Revision 1.3  2005/05/30 19:17:08  nexd
 * UML documentation update....
 *
 * Revision 1.2  2005/05/11 17:31:39  nexd
 * Refactoring and extended test cases
 *
 * Revision 1.1  2005/05/08 11:59:31  nexd
 * restructuring
 *
 * Revision 1.6  2005/04/24 15:00:26  nexd
 * Bugfixes and many performance and coding improvements.
 *
 * Revision 1.5  2005/03/31 12:08:39  nexd
 * Advanced VCL support
 *
 * Revision 1.4  2005/03/14 12:22:48  nexd
 * Heavy javadoc, checkstyle and eclipse todo-Task session.
 *
 */
package de.xplib.nexd.engine.xml.xpath;

import java.io.IOException;


/**
 * A utility that parses a stream of tokens and creates the appropriate
 * sub-class of AbstractBooleanExpr.
 *
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 * @link http://sparta.sourceforge.net
 * @stereotype Factory
 */
public final class ExprFactory {

    /**
     * @label uses
     */

    /*#de.xplib.nexd.engine.xml.xpath.XPathTokenizer Dependency_Link*/

    /**
     * Comment for <code>Q_STRING</code>
     */
    private static final String Q_STRING = "quoted string";

    /**
     * @param xpath ---
     * @param toks ---
     * @return ---
     * @throws XPathExceptionExt ---
     * @throws IOException ---
     * @precondition current token is first token in expr production.
     * @postcondition current token is first token after expr production
     */
    static AbstractBooleanExpr createExpr(final XPath xpath,
            final XPathTokenizer toks) throws XPathExceptionExt, IOException {

        AbstractBooleanExpr expr;
        switch (toks.ttype) {

        case XPathTokenizer.TT_NUMBER:
            int position = (int) toks.nval;
            toks.nextToken();
            expr = new PositionEqualsExpr(position);
            break;

        case '@':
            expr = getAttrExpr(xpath, toks);
            break;

        case XPathTokenizer.TT_WORD:
            if (!toks.sval.equals("text")) {
                throw new XPathExceptionExt(xpath,
                        "at beginning of expression", toks, "text()");
            }

            if (toks.nextToken() != '(') {
                throw new XPathExceptionExt(xpath, "after text", toks, "(");
            }
            if (toks.nextToken() != ')') {
                throw new XPathExceptionExt(xpath, "after text(", toks, ")");
            }
            expr = getTextExpr(xpath, toks);
            break;

        default:
            throw new XPathExceptionExt(xpath, "at beginning of expression",
                    toks, "@, number, or text()");
        }
        return expr;
    }

    /**
     * Returns an expression for text.
     *
     * @param xpath The xpath instance.
     * @param toks The xpath tokenizer
     * @return a matching boolean expression.
     * @throws IOException If the tokenizer stoks.
     * @throws XPathExceptionExt If it is a not supported or wrong expression.
     */
    private static AbstractBooleanExpr getTextExpr(final XPath xpath,
            final XPathTokenizer toks) throws IOException, XPathExceptionExt {

        AbstractBooleanExpr expr;

        String tValue;
        switch (toks.nextToken()) {
        case '=':
            toks.nextToken();
            if (toks.ttype != '\"' && toks.ttype != '\'') {
                throw new XPathExceptionExt(xpath, "right hand side of equals",
                        toks, Q_STRING);
            }
            tValue = toks.sval;
            toks.nextToken();
            expr = new TextEqualsExpr(tValue);
            break;

        case '!':
            toks.nextToken();
            if (toks.ttype != '=') {
                throw new XPathExceptionExt(xpath, "after !", toks, "=");
            }
            toks.nextToken();
            if (toks.ttype != '\"' && toks.ttype != '\'') {
                throw new XPathExceptionExt(xpath, "right hand side of !=",
                        toks, Q_STRING);
            }
            tValue = toks.sval;
            toks.nextToken();
            expr = new TextNotEqualsExpr(tValue);
            break;

        default:
            expr = TextExistsExpr.INSTANCE;
            break;
        }
        return expr;
    }

    /**
     * Returns an expression for an attribute.
     *
     * @param xpath The xpath instance.
     * @param toks The xpath tokenizer
     * @return a matching boolean expression.
     * @throws IOException If the tokenizer stoks.
     * @throws XPathExceptionExt If it is a not supported or wrong expression.
     */
    private static AbstractBooleanExpr getAttrExpr(final XPath xpath,
            final XPathTokenizer toks) throws IOException, XPathExceptionExt {

        if (toks.nextToken() != XPathTokenizer.TT_WORD) {
            throw new XPathExceptionExt(xpath, "after @", toks, "name");
        }

        String name = toks.sval;

        AbstractBooleanExpr expr;
        switch (toks.nextToken()) {
        case '=':
            expr = getEqExpr(xpath, toks, name);
            break;
        case '<':
            expr = getLtExpr(xpath, toks, name);
            break;
        case '>':
            expr = getGtExpr(xpath, toks, name);
            break;
        case '!':
            expr = getNotExpr(xpath, toks, name);
            break;
        default:
            expr = new AttrExistsExpr(name);
        }
        return expr;
    }

    /**
     * Returns an expression for an attribute not expression.
     *
     * @param xpath The xpath instance.
     * @param toks The xpath tokenizer
     * @param name The name of the attribute.
     * @return a matching boolean expression.
     * @throws IOException If the tokenizer stoks.
     * @throws XPathExceptionExt If it is a not supported or wrong expression.
     */
    private static AbstractBooleanExpr getNotExpr(final XPath xpath,
            final XPathTokenizer toks, final String name) throws IOException,
            XPathExceptionExt {

        toks.nextToken();
        if (toks.ttype != '=') {
            throw new XPathExceptionExt(xpath, "after !", toks, "=");
        }
        toks.nextToken();
        if (toks.ttype != '\"' && toks.ttype != '\'') {
            throw new XPathExceptionExt(xpath, "right hand side of !=", toks,
                    Q_STRING);
        }
        String value = toks.sval;
        toks.nextToken();
        return new AttrNotEqualsExpr(name, value);
    }

    /**
     * Returns an expression for an attribute equals expression.
     *
     * @param xpath The xpath instance.
     * @param toks The xpath tokenizer
     * @param name The name of the attribute.
     * @return a matching boolean expression.
     * @throws IOException If the tokenizer stoks.
     * @throws XPathExceptionExt If it is a not supported or wrong expression.
     */
    private static AbstractBooleanExpr getEqExpr(final XPath xpath,
            final XPathTokenizer toks, final String name) throws IOException,
            XPathExceptionExt {

        toks.nextToken();

        if (toks.ttype == XPathTokenizer.TT_NUMBER) {
            toks.sval = String.valueOf((char) toks.ttype);
            toks.ttype = '\'';
        }
        if (toks.ttype != '\"' && toks.ttype != '\'') {
            throw new XPathExceptionExt(xpath, "right hand side of equals",
                    toks, Q_STRING);
        }
        String value = toks.sval;
        toks.nextToken();
        return new AttrEqualsExpr(name, value);
    }

    /**
     * Returns an expression for an attribute greater expression.
     *
     * @param xpath The xpath instance.
     * @param toks The xpath tokenizer
     * @param name The name of the attribute.
     * @return a matching boolean expression.
     * @throws IOException If the tokenizer stoks.
     * @throws XPathExceptionExt If it is a not supported or wrong expression.
     */
    private static AbstractBooleanExpr getGtExpr(final XPath xpath,
            final XPathTokenizer toks, final String name) throws IOException,
            XPathExceptionExt {

        toks.nextToken();

        double valueN;
        boolean equals = false;
        if (toks.ttype == '=') {
            equals = true;
            toks.nextToken();
        }

        if (toks.ttype == '\"' || toks.ttype == '\'') {
            valueN = Double.parseDouble(toks.sval);
        } else if (toks.ttype == XPathTokenizer.TT_NUMBER) {
            valueN = toks.nval;
        } else {
            throw new XPathExceptionExt(xpath,
                    "right hand side of greater-than", toks,
                    "quoted string or number");
        }
        toks.nextToken();
        if (toks.ttype == '.') {
            valueN = getDoubleValue(toks, valueN, xpath);
        }

        AbstractBooleanExpr expr;
        if (equals) {
            expr = new AttrGreaterEqualsExpr(name, valueN);
        } else {
            expr = new AttrGreaterExpr(name, valueN);
        }
        return expr;
    }

    /**
     * Returns an expression for an attribute lower expression.
     *
     * @param xpath The xpath instance.
     * @param toks The xpath tokenizer
     * @param name The name of the attribute.
     * @return a matching boolean expression.
     * @throws IOException If the tokenizer stoks.
     * @throws XPathExceptionExt If it is a not supported or wrong expression.
     */
    private static AbstractBooleanExpr getLtExpr(final XPath xpath,
            final XPathTokenizer toks, final String name) throws IOException,
            XPathExceptionExt {

        toks.nextToken();

        double valueN;
        boolean equals = false;
        if (toks.ttype == '=') {
            equals = true;
            toks.nextToken();
        }

        if (toks.ttype == '\"' || toks.ttype == '\'') {
            valueN = Double.parseDouble(toks.sval);
        } else if (toks.ttype == XPathTokenizer.TT_NUMBER) {
            valueN = toks.nval;
        } else {
            throw new XPathExceptionExt(xpath, "right hand side of less-than",
                    toks, "quoted string or number");
        }
        toks.nextToken();
        if (toks.ttype == '.') {
            valueN = getDoubleValue(toks, valueN, xpath);
        }

        AbstractBooleanExpr expr;
        if (equals) {
            expr = new AttrLessEqualsExpr(name, valueN);
        } else {
            expr = new AttrLessExpr(name, valueN);
        }
        return expr;
    }

    /**
     * Returns double value of the xpath string.
     *
     * @param toks The xpath tokenizer
     * @param valueN The double value.
     * @param xpath The xpath instance.
     * @return a matching boolean expression.
     * @throws IOException If the tokenizer stoks.
     * @throws XPathExceptionExt If it is a not supported or wrong expression.
     */
    private static double getDoubleValue(final XPathTokenizer toks,
            final double valueN, final XPath xpath) throws IOException,
            XPathExceptionExt {

        toks.nextToken();
        try {
            if (toks.ttype == XPathTokenizer.TT_NUMBER) {
                return valueN + toks.nval / (toks.sval.length() * 10);
            } else {
                throw new XPathExceptionExt(xpath,
                        "right hand side of less-than", toks,
                        "quoted string or number");
            }
        } finally {
            toks.nextToken();
        }
    }

    /**
     *
     */
    private ExprFactory() {
        super();
    }

}