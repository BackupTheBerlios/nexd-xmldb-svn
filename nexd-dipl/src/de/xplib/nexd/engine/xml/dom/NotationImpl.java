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
 * $Log: NotationImpl.java,v $
 * Revision 1.1  2005/05/08 11:59:31  nexd
 * restructuring
 *
 * Revision 1.3  2005/03/31 12:08:39  nexd
 * Advanced VCL support
 *
 * Revision 1.2  2005/03/14 12:22:48  nexd
 * Heavy javadoc, checkstyle and eclipse todo-Task session.
 *
 */
package de.xplib.nexd.engine.xml.dom;

import org.w3c.dom.Node;
import org.w3c.dom.Notation;

/**
 * This interface represents a notation declared in the DTD. A notation either 
 * declares, by name, the format of an unparsed entity (see <a 
 * href='http://www.w3.org/TR/2004/REC-xml-20040204#Notations'>section 4.7</a> 
 * of the XML 1.0 specification [<a 
 * href='http://www.w3.org/TR/2004/REC-xml-20040204'>XML 1.0</a>]), or is 
 * used for formal declaration of processing instruction targets (see <a 
 * href='http://www.w3.org/TR/2004/REC-xml-20040204#sec-pi'>section 2.6</a> of 
 * the XML 1.0 specification [<a 
 * href='http://www.w3.org/TR/2004/REC-xml-20040204'>XML 1.0</a>]). The 
 * <code>nodeName</code> attribute inherited from <code>Node</code> is set 
 * to the declared name of the notation.
 * <p>The DOM Core does not support editing <code>Notation</code> nodes; they 
 * are therefore readonly.
 * <p>A <code>Notation</code> node does not have any parent.
 * <p>See also the <a 
 * href='http://www.w3.org/TR/2004/REC-DOM-Level-3-Core-20040407'
 * >Document Object Model (DOM) Level 3 Core Specification</a>.
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 */
public class NotationImpl extends AbstractNode implements Notation {

    /**
     * Constructor 
     * 
     * @param ownerIn ..
     * @param nodeNameIn ..
     */
    protected NotationImpl(final DocumentImpl ownerIn, 
                           final String nodeNameIn) {
        
        super(ownerIn, nodeNameIn, Node.NOTATION_NODE);
    }

    /**
     * The public identifier of this notation. If the public identifier was 
     * not specified, this is <code>null</code>.
     * 
     * @return ..
     * @see org.w3c.dom.Notation#getPublicId()
     */
    public String getPublicId() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * The system identifier of this notation. If the system identifier was 
     * not specified, this is <code>null</code>. This may be an absolute URI 
     * or not.
     * 
     * @return ..
     * @see org.w3c.dom.Notation#getSystemId()
     */
    public String getSystemId() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Returns a duplicate of this node, i.e., serves as a generic copy 
     * constructor for nodes. The duplicate node has no parent; (
     * <code>parentNode</code> is <code>null</code>.) and no user data. User 
     * data associated to the imported node is not carried over. However, if 
     * any <code>UserDataHandlers</code> has been specified along with the 
     * associated data these handlers will be called with the appropriate 
     * parameters before this method returns.
     * <br>Cloning an <code>Element</code> copies all attributes and their 
     * values, including those generated by the XML processor to represent 
     * defaulted attributes, but this method does not copy any children it 
     * contains unless it is a deep clone. This includes text contained in 
     * an the <code>Element</code> since the text is contained in a child 
     * <code>Text</code> node. Cloning an <code>Attribute</code> directly, 
     * as opposed to be cloned as part of an <code>Element</code> cloning 
     * operation, returns a specified attribute (<code>specified</code> is 
     * <code>true</code>). Cloning an <code>Attribute</code> always clones 
     * its children, since they represent its value, no matter whether this 
     * is a deep clone or not. Cloning an <code>EntityReference</code> 
     * automatically constructs its subtree if a corresponding 
     * <code>Entity</code> is available, no matter whether this is a deep 
     * clone or not. Cloning any other type of node simply returns a copy of 
     * this node.
     * <br>Note that cloning an immutable subtree results in a mutable copy, 
     * but the children of an <code>EntityReference</code> clone are readonly
     * . In addition, clones of unspecified <code>Attr</code> nodes are 
     * specified. And, cloning <code>Document</code>, 
     * <code>DocumentType</code>, <code>Entity</code>, and 
     * <code>Notation</code> nodes is implementation dependent.
     * 
     * @param deep If <code>true</code>, recursively clone the subtree under 
     *   the specified node; if <code>false</code>, clone only the node 
     *   itself (and its attributes, if it is an <code>Element</code>).
     * @return The duplicate node.
     * @see org.w3c.dom.Node#cloneNode(boolean)
     */
    public Node cloneNode(final boolean deep) {
        // TODO Auto-generated method stub
        return null;
    }

}
