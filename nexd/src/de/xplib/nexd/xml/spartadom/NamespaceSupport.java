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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;


/**
 * Streamlined thermopylae specific implementation of the
 * org.xml.sax.helpers.NamespaceSupport class.
 * 
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 * @link http://sparta.sourceforge.net
 */

public class NamespaceSupport {

    /**
     * Comment for <code>XMLNS</code>
     */
    static final String XMLNS = "http://www.w3.org/XML/1998/namespace";

    /**
     * Comment for <code>XMLPREF</code>
     */
    static final String XMLPREF = "xml";

    /**
     * Comment for <code>context</code>
     */
    private final Stack context = new Stack();

    /**
     * Comment for <code>currContext</code>
     */
    private Map currContext = null;

    /**
     * Comment for <code>currFullPrefix</code>
     */
    private String currFullPrefix = null;

    /**
     * Comment for <code>currNS</code>
     */
    private String currNS = null;

    /**
     * Cache last prefix for speed
     * Comment for <code>currPrefix</code>
     */
    private String currPrefix = null;

    /**
     * Comment for <code>defaultNS</code>
     */
    private String defaultNS = "";

    /**
     * Comment for <code>depth</code>
     */
    private int depth = 0;

    /**
     * Comment for <code>oldContext</code>
     */
    private final Stack oldContext = new Stack();

    /**
     * 
     */
    public NamespaceSupport() {
    }

    /**
     * @param prefix ..
     * @param uri ..
     */
    final void declarePrefix(final String prefix, final String uri) {
        if (prefix.equals("xml") || prefix.equals("xmlns")) {
            return;
        }
        if (prefix.equals("")) {
            defaultNS = uri;
            return;
        }
        currContext.put(prefix, uri);
    }

    /**
     * @return ..
     */
    final Iterator popContext() {
        depth--;
        if (depth < 0) {
            currPrefix = null;
        }

        oldContext.push(currContext);
        Iterator keySetIterator = currContext.keySet().iterator();
        if (!context.isEmpty()) {
            currContext = (Map) context.pop();
        } else {
            currContext = null;
        }
        return keySetIterator;
    }

    /**
     * @param fullName ..
     * @param result ..
     * @param attribute ..
     * @return ..
     * @throws ParseException ..
     */
    final String[] processName(final String fullName, 
                         final String[] result, 
                         final boolean attribute)
            throws ParseException {

        if (currPrefix != null && fullName.startsWith(currFullPrefix)) {
            result[0] = currNS;
            result[1] = fullName.substring(currFullPrefix.length());
            result[2] = fullName;
            return result;
        }

        int a = fullName.indexOf(':');
        if (a > 0) {
            String prefix = fullName.substring(0, a);
            if (prefix.equals("xml") || prefix.equals("xmlns")) {
                return null;
            }

            result[1] = fullName.substring(a + 1);
            result[2] = fullName;

            result[0] = (String) currContext.get(prefix);
            if (result[0] == null) {
                for (int i = context.size() - 1; i >= 0; i--) {
                    Map ht = (Map) (context.elementAt(i));
                    result[0] = (String) (ht.get(prefix));
                    if (result[0] != null) {
                        break;
                    }
                }
            }

            if (result[0] == null) {
                throw new ParseException("Error processing tag " + fullName
                        + ". No namespace mapping found.");
            }

            depth = 0;
            currNS = result[0];
            currPrefix = prefix;
            currFullPrefix = prefix + ":";
        } else {
            if (attribute) {
                result[0] = "";
            } else {
                result[0] = defaultNS;
            }
            result[1] = fullName;
            result[2] = fullName;
        }

        return result;
    }

    /**
     * 
     */
    public final void pushContext() {
        if (currContext != null) {
            context.push(currContext);
        }
        if (!oldContext.isEmpty()) {
            currContext = (Map) oldContext.pop();
            currContext.clear();
        } else {
            currContext = new HashMap();
        }

        depth++;
    }
}

// $Log: NamespaceSupport.java,v $
// Revision 1.2 2003/01/27 23:30:59 yuhongx
// Replaced Hashtable with HashMap.
//
// Revision 1.1.1.1 2002/08/19 05:04:16 eobrain
// import from HP Labs internal CVS
//
// Revision 1.5 2002/08/19 00:39:45 eob
// Tweak javadoc comment.
//
// Revision 1.4 2002/08/18 05:45:59 eob
// Add copyright and other formatting and commenting in preparation for
// release to SourceForge.
//
// Revision 1.3 2002/08/17 00:54:14 sermarti
//
// Revision 1.2 2002/08/15 23:40:23 sermarti
//
// Revision 1.1 2002/08/09 22:36:49 sermarti
