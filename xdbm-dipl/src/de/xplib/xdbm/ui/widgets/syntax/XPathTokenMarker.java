/*
 * Project: xmldb-manager 
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
 * $Log: XPathTokenMarker.java,v $
 * Revision 1.1  2005/04/12 08:34:20  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.ui.widgets.syntax;

import javax.swing.text.Segment;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class XPathTokenMarker extends TokenMarker {
    private static final String OPS = "<>=/()[]@*";
    private static final String QUOTES = "\"\'";
    
    public XPathTokenMarker() {
    }
    
    public byte markTokensImpl(byte token, Segment line, int lineIndex) {
       char[] array = line.array;
       int offset = line.offset;
       int lastOffset = offset;
       int length = line.count + offset;
       
       for ( int i = offset; i < length; i++ ) {
          int ip1 = i+1;
          char c = array[i];
          switch ( token ) {
             case Token.NULL: // text
                if ( OPS.indexOf(c) != -1 ) {
                   addToken(i-lastOffset, token);
                   lastOffset = i;
                   token = Token.KEYWORD1;
                }
                else if ( QUOTES.indexOf(c) != -1 ) {
                   addToken(i-lastOffset, token);
                   lastOffset = i;
                   if ( c == '\"' )
                      token = Token.LITERAL2;
                   else
                      token = Token.LITERAL1;
                }
                break;

             case Token.KEYWORD1: // operator
                if ( OPS.indexOf(c) == -1 ) {
                   addToken(i-lastOffset, token);
                   lastOffset = i;
                   if ( QUOTES.indexOf(c) != -1 ) {
                      if ( c == '\"' )
                         token = Token.LITERAL2;
                      else
                         token = Token.LITERAL1;
                   }
                   else
                      token = Token.NULL;
                }
                break;
                
             case Token.LITERAL1:
             case Token.LITERAL2: // strings
                if ( ( token == Token.LITERAL2 && c == '\"' )
                  || ( token == Token.LITERAL1 && c == '\'' ) ) {
                   addToken(ip1-lastOffset, token);
                   lastOffset = ip1;
                   token = Token.NULL;
                }
                break;
                
             default:
                throw new InternalError("Invalid state: " + token);
          }
       }
       
       addToken(length-lastOffset, token);
       return token;
    }
 }
