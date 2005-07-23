package de.xplib.nexd.xml.xpath;

import java.util.Iterator;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.2 $
 */
public interface XPathI {
    
    /**
     * @link
     * @shapeType PatternLink
     * @pattern gof.AbstractFactory
     * @supplierRole Concrete Product
     */
    /*# private AbstractXPathImpl lnkAbstractXPathImpl;*/
    
    /**
     * Does this path begin with a '/' or '//' ?
     * 
     * @return ---
     */
    boolean isAbsolute();

    /**
     * 
     * @return ---
     */
    Iterator getSteps();

    /**
     * Does xpath evaluate to a string values (attribute values or text() nodes)
     * 
     * @return ---
     */
    boolean isStringValue();
}