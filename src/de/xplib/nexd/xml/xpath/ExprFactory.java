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

import java.io.IOException;

/**
 * A utility that parses a stream of tokens and creates the appropriate
 * sub-class of BooleanExpr.
 * 
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 * @link http://sparta.sourceforge.net
 * @stereotype factory
 */
public class ExprFactory {

    /**
     * @param xpath ---
     * @param toks ---
     * @return ---
     * @throws XPathException ---
     * @throws IOException ---
     * @precondition current token is first token in expr production.
     * @postcondition current token is first token after expr production
     */
    static BooleanExpr createExpr(final XPath xpath, 
                                  final SimpleStreamTokenizer toks)
                                          throws XPathException, IOException {
        switch (toks.ttype) {

        default:
            throw new XPathException(xpath, "at beginning of expression", toks,
                    "@, number, or text()");

        case SimpleStreamTokenizer.TT_NUMBER:
            int position = (int) toks.nval;
            toks.nextToken();
            return new PositionEqualsExpr(position);

        case '@':

            if (toks.nextToken() != SimpleStreamTokenizer.TT_WORD) {
                throw new XPathException(xpath, "after @", toks, "name");
            }
            String name = toks.sval;
            String value;
            int valueN;
            switch (toks.nextToken()) {
            case '=':
                toks.nextToken();
                if (toks.ttype != '\"' && toks.ttype != '\'') {
                    throw new XPathException(xpath,
                            "right hand side of equals", toks, "quoted string");
                }
                value = toks.sval;
                toks.nextToken();
                return new AttrEqualsExpr(name, value);
            case '<':
                toks.nextToken();
                if (toks.ttype == '\"' || toks.ttype == '\'') {
                    // use jdk1.1 API to make the code work with PersonalJava
                    // valueN = Double.parseDouble(toks.sval);
                    valueN = Integer.parseInt(toks.sval);
                } else if (toks.ttype == SimpleStreamTokenizer.TT_NUMBER) {
                    valueN = toks.nval;
                } else {
                    throw new XPathException(xpath,
                            "right hand side of less-than", toks,
                            "quoted string or number");
                }
                toks.nextToken();
                return new AttrLessExpr(name, valueN);
            case '>':
                toks.nextToken();
                if (toks.ttype == '\"' || toks.ttype == '\'') {
                    // use jdk1.1 API to make the code work with PersonalJava
                    // valueN = Double.parseDouble(toks.sval);
                    valueN = Integer.parseInt(toks.sval);
                } else if (toks.ttype == SimpleStreamTokenizer.TT_NUMBER) {
                    valueN = toks.nval;
                } else {
                    throw new XPathException(xpath,
                            "right hand side of greater-than", toks,
                            "quoted string or number");
                }
                toks.nextToken();
                return new AttrGreaterExpr(name, valueN);
            case '!':
                toks.nextToken();
                if (toks.ttype != '=') {
                    throw new XPathException(xpath, "after !", toks, "=");
                }
                toks.nextToken();
                if (toks.ttype != '\"' && toks.ttype != '\'') {
                    throw new XPathException(xpath, "right hand side of !=",
                            toks, "quoted string");
                }
                value = toks.sval;
                toks.nextToken();
                return new AttrNotEqualsExpr(name, value);
            default:
                return new AttrExistsExpr(name);
            }
        case SimpleStreamTokenizer.TT_WORD:
            if (!toks.sval.equals("text")) {
                throw new XPathException(xpath, "at beginning of expression",
                        toks, "text()");
            }

            if (toks.nextToken() != '(') {
                throw new XPathException(xpath, "after text", toks, "(");
            }
            if (toks.nextToken() != ')') {
                throw new XPathException(xpath, "after text(", toks, ")");
            }
            String tValue;
            switch (toks.nextToken()) {
            case '=':
                toks.nextToken();
                if (toks.ttype != '\"' && toks.ttype != '\'') {
                    throw new XPathException(xpath,
                            "right hand side of equals", toks, "quoted string");
                }
                tValue = toks.sval;
                toks.nextToken();
                return new TextEqualsExpr(tValue);
            case '!':
                toks.nextToken();
                if (toks.ttype != '=') {
                    throw new XPathException(xpath, "after !", toks, "=");
                }
                toks.nextToken();
                if (toks.ttype != '\"' && toks.ttype != '\'') {
                    throw new XPathException(xpath, "right hand side of !=",
                            toks, "quoted string");
                }
                tValue = toks.sval;
                toks.nextToken();
                return new TextNotEqualsExpr(tValue);
            default:
                return TextExistsExpr.INSTANCE;
            }
        }
    }
    
    /**
     * 
     */
    protected ExprFactory() {
        
    }

}

// $Log: ExprFactory.java,v $
// Revision 1.8 2003/07/18 00:01:42 eobrain
// Make compatiblie with J2ME. For example do not use "new"
// java.util classes.
//
// Revision 1.7 2003/05/12 20:07:59 eobrain
// Inconsequential code change to avoid eclipse warning.
//
// Revision 1.6 2003/01/09 01:12:37 yuhongx
// Use JDK1.1 API to make the code work with PersonalJava.
//
// Revision 1.5 2002/12/06 23:37:37 eobrain
// Make objects that are always the same follow the Flyweight Pattern.
//
// Revision 1.4 2002/12/05 04:34:38 eobrain
// Add support for greater than and less than relational expressions in
// predicates.
//
// Revision 1.3 2002/10/30 16:30:16 eobrain
// Feature request [ 630127 ] Support /a/b[text()='foo']
// http://sourceforge.net/projects/sparta-xml/
//
// Revision 1.2 2002/09/18 05:30:21 eobrain
// Support xpath predicates of the form [1], [2], ...
//
// Revision 1.1.1.1 2002/08/19 05:04:04 eobrain
// import from HP Labs internal CVS
//
// Revision 1.3 2002/08/18 23:38:41 eob
// Add copyright and other formatting and commenting in preparation for
// release to SourceForge.
//
// Revision 1.2 2002/05/23 21:12:10 eob
// Better error reporting.
//
// Revision 1.1 2002/02/01 02:01:08 eob
// initial
