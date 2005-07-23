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
import java.io.Reader;

/**
 * Simplified replacement for java.util.StreamTokenizer which is not avilable in
 * J2ME. 
 * 
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 * @link http://sparta.sourceforge.net
 */
public class XPathTokenizer implements XPathTokensI {
    
    /**
     * Comment for <code>DIM</code>
     */
    private static final short DIM = 256;
    
    /**
     * Comment for <code>builder</code>
     */
    private final StringBuilder builder = new StringBuilder();
    
    /**
     * Comment for <code>charType</code>
     */
    private final int[] charType = new int[XPathTokenizer.DIM];
    
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
    public XPathTokenizer(final Reader readerIn) throws IOException {
        super();
        
        reader = readerIn;
        for (char ch = 0; ch < charType.length; ++ch) {
            if ('A' <= ch && ch <= 'Z' || 'a' <= ch && ch <= 'z' || ch == '-') {
                charType[ch] = TT_WORD;
            } else if ('0' <= ch && ch <= '9') {
                charType[ch] = TT_NUMBER;
            } else if ('\u0000' <= ch && ch <= '\u0020') {
                charType[ch] = TT_WHITESPACE;
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
        } else {
            ttype = nextType;
            while (true) {
                int chr;
                int currentType;
                boolean transition = false;
                boolean whitespace;
                do {
                    chr = reader.read();
                    currentType = this.whitespace(chr);
                    whitespace = inQuote == 0 && currentType == TT_WHITESPACE;
                    transition = transition || whitespace;
                } while (whitespace);
                
                if (currentType == '\'' || currentType == '\"') {
                    this.quote(currentType);
                }
                if (inQuote != 0) {
                    currentType = inQuote;
                }
                
                transition = transition
                || (ttype >= TT_EOF && ttype != '\'' && ttype != '\"')
                || ttype != currentType;
                if (transition) {
                    this.setTokenValue();
                    if (currentType != TT_WHITESPACE) {
                        nextType = currentType == TT_QUOTE ? chr : currentType;
                    }
                }
                this.appendTokenValue(currentType, chr);
                if (transition) {
                    break;
                }
            }
        }
        return ttype;
    }
    
    /**
     * precondition 0 <=chIn && chIn <128
     * 
     * @param chIn ---
     */
    public final void ordinaryChar(final char chIn) {
        charType[chIn] = chIn;
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
        
        String str;
        switch (ttype) {
        	case TT_NUMBER:
        	    str = Integer.toString(nval);
        	    break;
        	    
        	case TT_WORD:
        	case '\"':
        	    str = "\"" + sval + "\"";
        	    break;
        	    
        	case '\'':
        	    str = "\'" + sval + "\'";
        	    break;
        	    
        	case TT_EOF:
        	    str = "(EOF)";
        	    break;
        	    
        	default:
        	    str = "\'" + (char) ttype + "\'";
        	    break;
        }
        return str;
    }
    
    /**
     * precondition 0 <=ch && ch <128
     * 
     * @param start ---
     * @param end ---
     */
    public final void wordChars(final char start, final char end) {
        for (char ch = start; ch <= end; ++ch) {
            charType[ch] = TT_WORD;
        }
    }
    
    /**
     * Is it a whitespace character.
     * 
     * @param chIn The current char.
     * @return The type of the char.
     * @throws IOException If we have a unterminated string.
     */
    private int whitespace(final int chIn) throws IOException {
        int type;
        if (chIn == -1) {
            if (inQuote != 0) {
                throw new IOException("Unterminated quote");
            }
            type = TT_EOF;
        } else {
            type = charType[chIn];
        }
        return type;
    }
    
    /**
     * Start or end quoted string.
     * 
     * @param typeIn The current character type.
     */
    private void quote(final int typeIn) {
        if (inQuote == 0) {
            inQuote = (char) typeIn;
        } else {
            if (inQuote == typeIn) {
                inQuote = 0;
            }
        }
    }
    
    /**
     * Sets the internal token value.
     */
    private void setTokenValue() {
        switch (ttype) {
        case TT_WORD:
            sval = builder.toString();
            builder.setLength(0);
            break;
        case '\'':
        case '\"':
            sval = builder.toString().substring(1, builder.length() - 1);
            builder.setLength(0);
            break;
        case TT_NUMBER:
            nval = Integer.parseInt(builder.toString());
            builder.setLength(0);
            break;
        default:
            break;
        }
    }
    
    /**
     * Adds a single char to the internal buffer.
     * 
     * @param typeIn The current token type.
     * @param chIn The single char.
     */
    private void appendTokenValue(final int typeIn, final int chIn) {
        switch (typeIn) {
        case TT_WORD:
        case TT_NUMBER:
        case '\'':
        case '\"':
            builder.append((char) chIn);
            break;
        default:
            break;
        }
    }
}