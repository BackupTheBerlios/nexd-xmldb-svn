/*
 * Project: nexd 
 * Copyright (C) 2004  Manuel Pichler <manuel.pichler@xplib.de>
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
 * $Log: XMLResourceImpl.java,v $
 * Revision 1.2  2005/05/11 17:31:39  nexd
 * Refactoring and extended test cases
 *
 * Revision 1.1  2005/05/08 11:59:31  nexd
 * restructuring
 *
 * Revision 1.8  2005/04/24 15:00:26  nexd
 * Bugfixes and many performance and coding improvements.
 *
 * Revision 1.7  2005/04/10 13:18:46  nexd
 * New JUnit test cases and minor bug fixes.
 *
 * Revision 1.6  2005/03/31 12:08:39  nexd
 * Advanced VCL support
 *
 * Revision 1.5  2005/03/14 12:22:48  nexd
 * Heavy javadoc, checkstyle and eclipse todo-Task session.
 *
 */
package de.xplib.nexd.engine.xapi;


/**
 * Provides access to XML resources stored in the database. An XMLResource can
 * be accessed either as text XML or via the DOM or SAX APIs.<p />
 *
 * The default behavior for getContent and setContent is to work with XML data
 * as text so these methods work on <code>String</code> content.
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.2 $
 */
public class XMLResourceImpl extends AbstractXMLResource {
    
    /**
     * <p>Empty constructor used by 
     * {@link org.xmldb.api.base.ResourceSet#getMembersAsResource()}.</p>
     * <p>In every other case this constructor is evil.</p>
     */
    public XMLResourceImpl() {
        super(null, "");
    }

    /**
     * @param parentIn The parent collection of this <code>Resource</code>.
     * @param idIn The resource id.
     */
    public XMLResourceImpl(final CollectionImpl parentIn, final String idIn) {
        super(parentIn, idIn);
    }

}
