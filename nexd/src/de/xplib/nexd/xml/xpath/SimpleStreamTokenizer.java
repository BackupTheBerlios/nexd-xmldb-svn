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
import java.io.Reader;

/**
 * Simplified replacement for java.util.StreamTokenizer which is not avilable in
 * J2ME. 
 * 
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 * @link http://sparta.sourceforge.net
 */
public class SimpleStreamTokenizer {
    
    /**
     * Comment for <code>DIM</code>
     */
    private static final short DIM = 256;
    
    /**
     * Comment for <code>QUOTE</code>
     */
    private static final int QUOTE = -6;
    
    /**
     * Comment for <code>TT_EOF</code>
     */
    public static final int TT_EOF = -1;
    
    /**
     * Comment for <code>TT_NUMBER</code>
     */
    public static final int TT_NUMBER = -2;
    
    /**
     * Comment for <code>TT_WORD</code>
     */
    public static final int TT_WORD = -3;
    
    /**
     * Comment for <code>WHITESPACE</code>
     */
    private static final int WHITESPACE = -5;
    
    /**
     * Comment for <code>buffer</code>
     */
    private final StringBuffer buffer = new StringBuffer();
    
    /**
     * Comment for <code>charType</code>
     */
    private final int[] charType = new int[SimpleStreamTokenizer.DIM];
    
    /**
     * Comment for <code>inQuote</code>
     */
    private char inQuote = 0;
    
    /**
     * Comment for <code>nextType</code>
     */
    private int nextType;
    
    /**
     * Comment for <code>nval</code>
     */
    public int nval = Integer.MIN_VALUE;
    
    /**
     * Comment for <code>pushedBack</code>
     */
    private boolean pushedBack = false;
    
    /**
     * Comment for <code>reader</code>
     */
    private final Reader reader;
    
    /**
     * Comment for <code>sval</code>
     */
    public String sval = "";
    
    /**
     * Comment for <code>ttype</code>
     */
    public int ttype = Integer.MIN_VALUE;
    
    /**
     * @param readerIn ---
     * @throws IOException ---
     */
    public SimpleStreamTokenizer(final Reader readerIn) throws IOException {
        reader = readerIn;
        for (char ch = 0; ch < charType.length; ++ch) {
            if ('A' <= ch && ch <= 'Z' || 'a' <= ch && ch <= 'z' || ch == '-') {
                charType[ch] = TT_WORD;
            } else if ('0' <= ch && ch <= '9') {
                charType[ch] = TT_NUMBER;
            } else if ('\u0000' <= ch && ch <= '\u0020') {
                charType[ch] = WHITESPACE;
            } else {
                charType[ch] = ch;
            }
        }
        nextToken();
    }
    
    /**
     * @return ---
     * @throws IOException ---
     */
    public final int nextToken() throws IOException {
        if (pushedBack) {
            pushedBack = false;
            return ttype;
        }
        ttype = nextType;
        while (true) {
            int ch;
            int currentType;
            boolean transition = false;
            boolean whitespace;
            do {
                ch = reader.read();
                if (ch == -1) {
                    if (inQuote != 0) {
                        throw new IOException("Unterminated quote");
                    }
                    currentType = TT_EOF;
                } else {
                    currentType = charType[ch];
                }
                whitespace = inQuote == 0 && currentType == WHITESPACE;
                transition = transition || whitespace;
            } while (whitespace);
            
            if (currentType == '\'' || currentType == '\"') {
                if (inQuote == 0) {
                    inQuote = (char) currentType;
                } else {
                    if (inQuote == currentType) {
                        inQuote = 0;
                    }
                }
            }
            if (inQuote != 0) {
                currentType = inQuote;
            }
            transition = transition
            || (ttype >= TT_EOF && ttype != '\'' && ttype != '\"')
            || ttype != currentType;
            if (transition) {
                //transition: we have a token to emit
                switch (ttype) {
                case TT_WORD:
                    sval = buffer.toString();
                    buffer.setLength(0);
                    break;
                case '\'':
                case '\"':
                    sval = buffer.toString().substring(1, buffer.length() - 1);
                    buffer.setLength(0);
                    break;
                case TT_NUMBER:
                    nval = Integer.parseInt(buffer.toString());
                    buffer.setLength(0);
                    break;
                default:
                    break;
                }
                if (currentType != WHITESPACE) {
                    nextType = currentType == QUOTE ? ch : currentType;
                }
            }
            
            switch (currentType) {
            case TT_WORD:
            case TT_NUMBER:
            case '\'':
            case '\"':
                buffer.append((char) ch);
                break;
            default:
                break;
            }
            if (transition) {
                return ttype;
            }
        }
    }
    
    /**
     * precondition 0 <=ch && ch <128
     * 
     * @param ch ---
     */
    public final void ordinaryChar(final char ch) {
        charType[ch] = ch;
    }
    
    /**
     * 
     */
    public final void pushBack() {
        pushedBack = true;
    }
    
    /**
     * <Some description here>
     * 
     * @return ---
     * @see java.lang.Object#toString()
     */
    public final String toString() {
        switch (ttype) {
        case TT_NUMBER:
            return Integer.toString(nval);
        case TT_WORD:
        case '\"':
            return "\"" + sval + "\"";
        case '\'':
            return "\'" + sval + "\'";
        case TT_EOF:
            return "(EOF)";
        default:
            return "\'" + (char) ttype + "\'";
        }
    }
    
    /**
     * precondition 0 <=ch && ch <128
     * 
     * @param from ---
     * @param to ---
     */
    public final void wordChars(final char from, final char to) {
        for (char ch = from; ch <= to; ++ch) {
            charType[ch] = TT_WORD;
        }
    }
}