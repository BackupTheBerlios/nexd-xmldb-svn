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

/**
 * Circular character buffer used to store parsing history for debug purposes.
 * 
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 * @link http://sparta.sourceforge.net
 */
class CharCircBuffer {
    
    /**
     * Comment for <code>BUF_DIV</code>
     */
    private static final short BUF_DIV = 10;
    
    /**
     * Comment for <code>BUF_MUL</code>
     */
    private static final short BUF_MUL = 11;
    
    /**
     * Comment for <code>RAW_ADD</code>
     */
    private static final int RAW_ADD = 0x10000;

    /**
     * Comment for <code>buf</code>
     */
    private final int[] buf; //Stores either the chars or the integers+0x10000

    /**
     * Comment for <code>enabled</code>
     */
    private boolean enabled = true;

    /**
     * Comment for <code>next</code>
     */
    private int next = 0;

    /**
     * Comment for <code>total</code>
     */
    private int total = 0;

    /**
     * @param n ..
     */
    CharCircBuffer(final int n) {
        buf = new int[n];
    }

    /**
     * @param ch ..
     */
    void addChar(final char ch) {
        addRaw(ch);
    }

    /**
     * @param i ..
     */
    void addInt(final int i) {
        addRaw(i + RAW_ADD);
    }

    /**
     * @param v ..
     */
    private void addRaw(final int v) {
        if (enabled) {
            buf[next] = v;
            next = (next + 1) % buf.length;
            ++total;
        }
    }

    /**
     * @param s ..
     */
    void addString(final String s) {
        char[] chars = s.toCharArray();
        int slen = chars.length;
        for (int i = 0; i < slen; ++i) {
            addChar(chars[i]);
        }
    }

    /**
     * 
     */
    void disable() {
        enabled = false;
    }

    /**
     * 
     */
    void enable() {
        enabled = true;
    }

    /**
     * <Some description here>
     * 
     * @return ..
     * @see java.lang.Object#toString()
     */
    public String toString() {
        StringBuffer result = new StringBuffer(BUF_MUL * buf.length / BUF_DIV);
        int first = total < buf.length ? buf.length - total : 0;
        for (int i = first; i < buf.length; ++i) {
            int ii = (i + next) % buf.length;
            int v = buf[ii];
            if (v < RAW_ADD) {
                result.append((char) v);
        	} else {
                result.append(Integer.toString(v - RAW_ADD));
            }
        }
        return result.toString();
    }
}

// $Log: CharCircBuffer.java,v $
// Revision 1.2 2003/07/17 21:36:29 eobrain
// Use integer arithmetic instead of floating-point arithmetic which is
// not supported in the J2ME we were using on a Nokia phone.
//
// Revision 1.1.1.1 2002/08/19 05:04:02 eobrain
// import from HP Labs internal CVS
//
// Revision 1.2 2002/08/18 04:31:45 eob
// Add copyright and other formatting and commenting in preparation for
// release to SourceForge.
//
// Revision 1.1 2002/08/01 23:29:17 sermarti
// Much faster Sparta parsing.
// Has debug features enabled by default. Currently toggled
// in ParseCharStream.java and recompiled.
