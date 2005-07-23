/*
 * Project: nexd 
 * Copyright (C) 2005  Manuel Pichler <manuel.pichler@xplib.de>
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

/*
 * $Log: XPathTokensI.java,v $
 * Revision 1.1  2005/05/08 11:59:31  nexd
 * restructuring
 *
 */
package de.xplib.nexd.engine.xml.xpath;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public interface XPathTokensI {
    
    /**
     * Comment for <code>TT_EOF</code>
     */
    int TT_EOF = -1;

    /**
     * Comment for <code>TT_NUMBER</code>
     */
    int TT_NUMBER = -2;

    /**
     * Comment for <code>TT_WORD</code>
     */
    int TT_WORD = -3;

    /**
     * Comment for <code>TT_QUOTE</code>
     */
    int TT_QUOTE = -6;
    
    /**
     * Comment for <code>TT_WHITESPACE</code>
     */
    int TT_WHITESPACE = -5;

}