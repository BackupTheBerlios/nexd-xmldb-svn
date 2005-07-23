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
package de.xplib.nexd.engine.xml.dom;

/**
 * This class provides w3c name standards, you can find at the following 
 * locations:
 * <ul>
 *   <li><a href="http://www.w3.org/TR/1999/REC-xml-names-19990114/"
 *   >http://www.w3.org/TR/1999/REC-xml-names-19990114/</a></li>
 *   <li><a href="http://www.w3.org/TR/REC-xml/#CharClasses"
 *   >http://www.w3.org/TR/REC-xml/#CharClasses</a></li>
 * </ul>
 * 
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 */
public class QName {
    
    /**
     * The default namespace declaration prefix.
     */
    private static String nsPrefix = "xmlns";
    
    /**
     * Comment for <code>defaultNS</code>
     */
    private static String defaultNS = "http://www.w3.org/XML/1998/namespace";
   
    /**
     * Comment for <code>xmlnsNS</code>
     */
    private static String xmlnsNS = "http://www.w3.org/2000/xmlns/";
   
    /**
     * Comment for <code>prefix</code>
     */
    private String prefix = "";
    
    /**
     * Comment for <code>localName</code>
     */
    private String localName = "";
    
    /**
     * Comment for <code>namespaceURI</code>
     */
    private String namespaceURI = null;
    
    /**
     * Comment for <code>qualifiedName</code>
     */
    private String qualifiedName = "";
    

    /**
     * @param qName ..
     * @param nsURI ..
     */
    public QName(final String qName, final String nsURI) {
        super();

        this.qualifiedName = qName;
        this.namespaceURI  = nsURI;
        
        int col = qName.indexOf(':');
        if (col == -1) {
            this.localName = qName;
        } else {
            this.prefix    = qName.substring(0, qName.indexOf(':'));
            this.localName = qName.substring(qName.indexOf(':') + 1);
        }
    }
    
    
    /**
     * Checks if this is a valid qualified name.
     * 
     * @return .. 
     */
    public boolean isValidQName() {
        return QName.isQName(this.qualifiedName);
    }
    
    /**
     * Checks that prefix and namespace uri are valid.
     * 
     * @return ..
     */
    public boolean isValidNamespace() {
        boolean valid = true;
        if (!prefix.equals("") && namespaceURI == null) {
            valid = false;
        }
        if (prefix.equals("xml") && !namespaceURI.equals(defaultNS)) {
            valid = false;
        }
        if ((prefix.equals(nsPrefix) || qualifiedName.equals(nsPrefix))
                && (namespaceURI == null || !namespaceURI.equals(xmlnsNS))) {
            
            valid = false;
        }
        if (namespaceURI != null && namespaceURI.equals(xmlnsNS) 
                && !prefix.endsWith(nsPrefix) 
                && !qualifiedName.equals(nsPrefix)) {
            
            valid = false;
        }
        return valid;
    }
    
    /**
     * @return Returns the localName.
     */
    public String getLocalName() {
        return localName;
    }
    
    /**
     * @param localNameIn The localName to set.
     */
    public void setLocalName(final String localNameIn) {
        this.localName = localNameIn;
    }
    
    /**
     * @return Returns the namespaceURI.
     */
    public String getNamespaceURI() {
        return namespaceURI;
    }
    
    /**
     * @param namespaceURIIn The namespaceURI to set.
     */
    public void setNamespaceURI(final String namespaceURIIn) {
        this.namespaceURI = namespaceURIIn;
    }
    
    /**
     * @return Returns the prefix.
     */
    public String getPrefix() {
        return prefix;
    }
    
    /**
     * @param prefixIn The prefix to set.
     */
    public void setPrefix(final String prefixIn) {
        this.prefix = prefixIn;
        if (this.prefix == null || this.prefix.equals("")) {
            this.qualifiedName = this.localName;
        } else {
            this.qualifiedName = this.prefix + ":" + this.localName;
        }
    }
    
    /**
     * @return Returns the qualifiedName.
     */
    public String getQualifiedName() {
        return qualifiedName;
    }
    
    /**
     * @param qualifiedNameIn The qualifiedName to set.
     */
    public void setQualifiedName(final String qualifiedNameIn) {
        this.qualifiedName = qualifiedNameIn;
    }
    
    /**
     * @param qName The qualified name to check.
     * @return Is it a valid qName.
     */
    public static boolean isQName(final String qName) {
        
        int col1 = qName.indexOf(':');
        int col2 = qName.lastIndexOf(':');
        
        boolean isQName;
        if (col1 == -1) {
            isQName = isNCName(qName);
        } else if (col1 == col2) {
            isQName = isNCName(qName.substring(0, col1))
                              && isNCName(qName.substring(col1 + 1));
        } else {
            isQName = false;
        }
        return isQName;
    }
    
    
    /**
     * @param name The name part to check.
     * @return Is it a valid name.
     */
    protected static boolean isNCName(final String name) {
        boolean isNCName = true;
        
        if (name.length() < 1) {
            isNCName = false;
        }
        
        char chr = name.charAt(0);
        if (!isLetter(chr) && chr != '_') {
            isNCName = false;
        }
        
        for (int i = 1, length = name.length(); i < length; i++) {
            if (!isNCNameChar(name.charAt(i))) {
                isNCName = false;
            }
        }
        
        return isNCName;
    }
    
    /**
     * @param cIn The character to check.
     * @return Is this a valid XML NameChar.
     */
    protected static boolean isNCNameChar(final char cIn) {
        return isLetter(cIn) || isDigit(cIn) || cIn == '.' || cIn == '-' 
            || cIn == '_' || isCombiningChar(cIn) || isExtender(cIn);
    }
    
    
    /**
     * @param cIn The character to check.
     * @return Is this a valid XML letter.
     */
    protected static boolean isLetter(final char cIn) {
        return (isBaseChar(cIn) || isIdeographic(cIn));
    }
    
    /**
     * @param cIn The character to check.
     * @return It this as valid XML BaseChar.
     */
    protected static boolean isBaseChar(final char cIn) {
    	return (cIn >= 0x0041 && cIn <= 0x005A) 
    	|| (cIn >= 0x0061 && cIn <= 0x007A) || (cIn >= 0x00C0 && cIn <= 0x00D6) 
        || (cIn >= 0x00D8 && cIn <= 0x00F6) || (cIn >= 0x00F8 && cIn <= 0x00FF)
        || (cIn >= 0x0100 && cIn <= 0x0131) || (cIn >= 0x0134 && cIn <= 0x013E)
        || (cIn >= 0x0141 && cIn <= 0x0148) || (cIn >= 0x014A && cIn <= 0x017E)
        || (cIn >= 0x0180 && cIn <= 0x01C3) || (cIn >= 0x01CD && cIn <= 0x01F0)
        || (cIn >= 0x01F4 && cIn <= 0x01F5) || (cIn >= 0x01FA && cIn <= 0x0217)
        || (cIn >= 0x0250 && cIn <= 0x02A8) || (cIn >= 0x0490 && cIn <= 0x04C4)
        || (cIn >= 0x02BB && cIn <= 0x02C1) || (cIn >= 0x0388 && cIn <= 0x038A)
        || (cIn >= 0x038E && cIn <= 0x03A1) || (cIn >= 0x03A3 && cIn <= 0x03CE)
        || (cIn >= 0x03D0 && cIn <= 0x03D6) || (cIn >= 0x09AA && cIn <= 0x09B0)
        || (cIn >= 0x04CB && cIn <= 0x04CC) || (cIn >= 0x04D0 && cIn <= 0x04EB)
        || (cIn >= 0x04EE && cIn <= 0x04F5) || (cIn >= 0x04F8 && cIn <= 0x04F9)
        || (cIn >= 0x0531 && cIn <= 0x0556) || (cIn >= 0x0561 && cIn <= 0x0586)
        || (cIn >= 0x05D0 && cIn <= 0x05EA) || (cIn >= 0x05F0 && cIn <= 0x05F2)
        || (cIn >= 0x0621 && cIn <= 0x063A) || (cIn >= 0x0641 && cIn <= 0x064A)
        || (cIn >= 0x0671 && cIn <= 0x06B7) || (cIn >= 0x06BA && cIn <= 0x06BE)
        || (cIn >= 0x06C0 && cIn <= 0x06CE) || (cIn >= 0x06D0 && cIn <= 0x06D3)
        || (cIn >= 0x06E5 && cIn <= 0x06E6) || (cIn >= 0x09B6 && cIn <= 0x09B9)
        || (cIn >= 0x0958 && cIn <= 0x0961) || (cIn >= 0x0985 && cIn <= 0x098C)
        || (cIn >= 0x098F && cIn <= 0x0990) || (cIn >= 0x0993 && cIn <= 0x09A8)
        || (cIn >= 0x09DC && cIn <= 0x09DD) || (cIn >= 0x09DF && cIn <= 0x09E1)
        || (cIn >= 0x09F0 && cIn <= 0x09F1) || (cIn >= 0x0A05 && cIn <= 0x0A0A)
        || (cIn >= 0x0A0F && cIn <= 0x0A10) || (cIn >= 0x0A13 && cIn <= 0x0A28)
        || (cIn >= 0x0A2A && cIn <= 0x0A30) || (cIn >= 0x0A32 && cIn <= 0x0A33)
        || (cIn >= 0x0A35 && cIn <= 0x0A36) || (cIn >= 0x0A38 && cIn <= 0x0A39)
        || (cIn >= 0x0A59 && cIn <= 0x0A5C) || (cIn >= 0x0A72 && cIn <= 0x0A74)
        || (cIn >= 0x0A8F && cIn <= 0x0A91) || (cIn >= 0x0A93 && cIn <= 0x0AA8)
        || (cIn >= 0x0AAA && cIn <= 0x0AB0) || (cIn >= 0x0AB2 && cIn <= 0x0AB3)
        || (cIn >= 0x0AB5 && cIn <= 0x0AB9) || (cIn >= 0x0B05 && cIn <= 0x0B0C)
        || (cIn >= 0x0B0F && cIn <= 0x0B10) || (cIn >= 0x0B13 && cIn <= 0x0B28)
        || (cIn >= 0x0B2A && cIn <= 0x0B30) || (cIn >= 0x0B32 && cIn <= 0x0B33)
        || (cIn >= 0x0B36 && cIn <= 0x0B39) || (cIn >= 0x0B5C && cIn <= 0x0B5D)
        || (cIn >= 0x0B5F && cIn <= 0x0B61) || (cIn >= 0x0B85 && cIn <= 0x0B8A)
        || (cIn >= 0x0B8E && cIn <= 0x0B90) || (cIn >= 0x0B92 && cIn <= 0x0B95)
        || (cIn >= 0x0B99 && cIn <= 0x0B9A)
        || (cIn >= 0x0B9E && cIn <= 0x0B9F) || (cIn >= 0x0BA3 && cIn <= 0x0BA4)
        || (cIn >= 0x0BA8 && cIn <= 0x0BAA) || (cIn >= 0x0BAE && cIn <= 0x0BB5)
        || (cIn >= 0x0BB7 && cIn <= 0x0BB9) || (cIn >= 0x0C05 && cIn <= 0x0C0C)
        || (cIn >= 0x0C0E && cIn <= 0x0C10) || (cIn >= 0x0C12 && cIn <= 0x0C28)
        || (cIn >= 0x0C2A && cIn <= 0x0C33) || (cIn >= 0x0C35 && cIn <= 0x0C39)
        || (cIn >= 0x0C60 && cIn <= 0x0C61) || (cIn >= 0x0C85 && cIn <= 0x0C8C)
        || (cIn >= 0x0C8E && cIn <= 0x0C90) || (cIn >= 0x0C92 && cIn <= 0x0CA8)
        || (cIn >= 0x0CAA && cIn <= 0x0CB3) || (cIn >= 0x0CB5 && cIn <= 0x0CB9)
        || (cIn >= 0x0CE0 && cIn <= 0x0CE1) || (cIn >= 0x0D05 && cIn <= 0x0D0C)
        || (cIn >= 0x0D0E && cIn <= 0x0D10) || (cIn >= 0x0D12 && cIn <= 0x0D28)
        || (cIn >= 0x0D2A && cIn <= 0x0D39) || (cIn >= 0x0D60 && cIn <= 0x0D61)
        || (cIn >= 0x0E01 && cIn <= 0x0E2E) || (cIn >= 0x0E32 && cIn <= 0x0E33)
        || (cIn >= 0x0E40 && cIn <= 0x0E45) || (cIn >= 0x0E81 && cIn <= 0x0E82)
        || (cIn >= 0x0E87 && cIn <= 0x0E88) || (cIn >= 0x0EC0 && cIn <= 0x0EC4)
        || (cIn >= 0x0F49 && cIn <= 0x0F69) || (cIn >= 0x10D0 && cIn <= 0x10F6)
        || (cIn >= 0x1105 && cIn <= 0x1107) || (cIn >= 0x110E && cIn <= 0x1112) 
        || (cIn >= 0x116D && cIn <= 0x116E) || (cIn >= 0x1154 && cIn <= 0x1155)
        || (cIn >= 0x11AE && cIn <= 0x11AF) || (cIn >= 0x1E00 && cIn <= 0x1E9B)
        || (cIn >= 0x1F18 && cIn <= 0x1F1D) || (cIn >= 0x1EA0 && cIn <= 0x1EF9) 
        || (cIn >= 0x1F50 && cIn <= 0x1F57) || (cIn >= 0x1F5F && cIn <= 0x1F7D)
        || (cIn >= 0x1F80 && cIn <= 0x1FB4) || (cIn >= 0x1FB6 && cIn <= 0x1FBC)
        || (cIn >= 0x1FC2 && cIn <= 0x1FC4) || (cIn >= 0x1FC6 && cIn <= 0x1FCC)
        || (cIn >= 0x1FD0 && cIn <= 0x1FD3) || (cIn >= 0x1FD6 && cIn <= 0x1FDB) 
        || (cIn >= 0x1FE0 && cIn <= 0x1FEC) || (cIn >= 0x1FF2 && cIn <= 0x1FF4) 
        || (cIn >= 0x212A && cIn <= 0x212B) || (cIn >= 0x2180 && cIn <= 0x2182)
        || (cIn >= 0x3041 && cIn <= 0x3094) || (cIn >= 0x30A1 && cIn <= 0x30FA)
        || (cIn >= 0x3105 && cIn <= 0x312C) || (cIn >= 0xAC00 && cIn <= 0xD7A3) 
        || (cIn >= 0x0E94 && cIn <= 0x0E97) || cIn == 0x0E8D || cIn == 0x0E8A
        || (cIn >= 0x0E99 && cIn <= 0x0E9F) || cIn == 0x114E || cIn == 0x0E84
        || (cIn >= 0x0EA1 && cIn <= 0x0EA3) || cIn == 0x0EA5 || cIn == 0x1163
        || (cIn >= 0x0EAA && cIn <= 0x0EAB) || cIn == 0x0EA7 || cIn == 0x0E30
        || (cIn >= 0x0EAD && cIn <= 0x0EAE) || cIn == 0x0EB0 || cIn == 0x0CDE 
        || (cIn >= 0x0EB2 && cIn <= 0x0EB3) || cIn == 0x0EBD || cIn == 0x1150 
        || (cIn >= 0x0F40 && cIn <= 0x0F47) || cIn == 0x1140 || cIn == 0x114C   
        || (cIn >= 0x10A0 && cIn <= 0x10C5) || cIn == 0x113C || cIn == 0x1100 
        || (cIn >= 0x1102 && cIn <= 0x1103) || cIn == 0x113E || cIn == 0x1165 
        || (cIn >= 0x03E2 && cIn <= 0x03F3) || cIn == 0x03DE || cIn == 0x03DA
        || (cIn >= 0x0401 && cIn <= 0x040C) || cIn == 0x0559 || cIn == 0x03E0
        || (cIn >= 0x040E && cIn <= 0x044F) || cIn == 0x06D5 || cIn == 0x03DC
        || (cIn >= 0x0451 && cIn <= 0x045C) || cIn == 0x0A5E || cIn == 0x038C
        || (cIn >= 0x045E && cIn <= 0x0481) || cIn == 0x0ABD || cIn == 0x0386
        || (cIn >= 0x04C7 && cIn <= 0x04C8) || cIn == 0x0B3D || cIn == 0x0AE0
        || (cIn >= 0x0A85 && cIn <= 0x0A8B) || cIn == 0x0A8D || cIn == 0x0B9C
        || (cIn >= 0x110B && cIn <= 0x110C) || cIn == 0x1109 || cIn == 0x1167
        || (cIn >= 0x115F && cIn <= 0x1161) || cIn == 0x1169 || cIn == 0x1159
        || (cIn >= 0x1172 && cIn <= 0x1173) || cIn == 0x11EB || cIn == 0x119E
        || (cIn >= 0x11B7 && cIn <= 0x11B8) || cIn == 0x11BA || cIn == 0x11F0 
        || (cIn >= 0x11BC && cIn <= 0x11C2) || cIn == 0x1175 || cIn == 0x11F9
        || (cIn >= 0x1F00 && cIn <= 0x1F15) || cIn == 0x11A8 || cIn == 0x11AB   
        || (cIn >= 0x1F20 && cIn <= 0x1F45) || cIn == 0x1F5B || cIn == 0x1F5D
        || (cIn >= 0x1F48 && cIn <= 0x1F4D) || cIn == 0x1F59 || cIn == 0x1FBE
        || (cIn >= 0x1FF6 && cIn <= 0x1FFC) || cIn == 0x2126 || cIn == 0x212E
        || (cIn >= 0x0905 && cIn <= 0x0939) || cIn == 0x093D || cIn == 0x09B2;
    }
    
    /**
     * @param cIn The character to check.
     * @return Is this a valid ideographic.
     */
    protected static boolean isIdeographic(final char cIn) {
        return ((cIn >= 0x4E00 && cIn <= 0x9FA5) 
        		|| (cIn == 0x3007) 
        		|| (cIn >= 0x3021 && cIn <= 0x3029));
    }
    
    
    /**
     * @param cIn The character to check,
     * @return Is this a valid XML CombiningChar.
     */
    protected static boolean isCombiningChar(final char cIn) {
        return (cIn >= 0x0300 && cIn <= 0x0345) 
                || (cIn >= 0x0360 && cIn <= 0x0361) 
                || (cIn >= 0x0483 && cIn <= 0x0486) 
                || (cIn >= 0x0591 && cIn <= 0x05A1) 
                || (cIn >= 0x05A3 && cIn <= 0x05B9) 
                || (cIn >= 0x05BB && cIn <= 0x05BD) 
                || cIn == 0x05BF 
                || (cIn >= 0x05C1 && cIn <= 0x05C2) 
                || cIn == 0x05C4 
                || (cIn >= 0x064B && cIn <= 0x0652) 
                || cIn == 0x0670 
                || (cIn >= 0x06D6 && cIn <= 0x06DC) 
                || (cIn >= 0x06DD && cIn <= 0x06DF) 
                || (cIn >= 0x06E0 && cIn <= 0x06E4) 
                || (cIn >= 0x06E7 && cIn <= 0x06E8) 
                || (cIn >= 0x06EA && cIn <= 0x06ED) 
                || (cIn >= 0x0901 && cIn <= 0x0903) 
                || cIn == 0x093C 
                || (cIn >= 0x093E && cIn <= 0x094C) 
                || cIn == 0x094D 
                || (cIn >= 0x0951 && cIn <= 0x0954) 
                || (cIn >= 0x0962 && cIn <= 0x0963) 
                || (cIn >= 0x0981 && cIn <= 0x0983) 
                || cIn == 0x09BC || cIn == 0x09BE || cIn == 0x09BF 
                || (cIn >= 0x09C0 && cIn <= 0x09C4) 
                || (cIn >= 0x09C7 && cIn <= 0x09C8) 
                || (cIn >= 0x09CB && cIn <= 0x09CD) 
                || cIn == 0x09D7 
                || (cIn >= 0x09E2 && cIn <= 0x09E3)
                || cIn == 0x0A02 || cIn == 0x0A3C 
                || cIn == 0x0A3E || cIn == 0x0A3F 
                || (cIn >= 0x0A40 && cIn <= 0x0A42) 
                || (cIn >= 0x0A47 && cIn <= 0x0A48) 
                || (cIn >= 0x0A4B && cIn <= 0x0A4D) 
                || (cIn >= 0x0A70 && cIn <= 0x0A71) 
                || (cIn >= 0x0A81 && cIn <= 0x0A83) 
                || cIn == 0x0ABC 
                || (cIn >= 0x0ABE && cIn <= 0x0AC5) 
                || (cIn >= 0x0AC7 && cIn <= 0x0AC9) 
                || (cIn >= 0x0ACB && cIn <= 0x0ACD) 
                || (cIn >= 0x0B01 && cIn <= 0x0B03) 
                || cIn == 0x0B3C 
                || (cIn >= 0x0B3E && cIn <= 0x0B43) 
                || (cIn >= 0x0B47 && cIn <= 0x0B48) 
                || (cIn >= 0x0B4B && cIn <= 0x0B4D) 
                || (cIn >= 0x0B56 && cIn <= 0x0B57) 
                || (cIn >= 0x0B82 && cIn <= 0x0B83) 
                || (cIn >= 0x0BBE && cIn <= 0x0BC2) 
                || (cIn >= 0x0BC6 && cIn <= 0x0BC8) 
                || (cIn >= 0x0BCA && cIn <= 0x0BCD) 
                || cIn == 0x0BD7 
                || (cIn >= 0x0C01 && cIn <= 0x0C03) 
                || (cIn >= 0x0C3E && cIn <= 0x0C44) 
                || (cIn >= 0x0C46 && cIn <= 0x0C48) 
                || (cIn >= 0x0C4A && cIn <= 0x0C4D) 
                || (cIn >= 0x0C55 && cIn <= 0x0C56) 
                || (cIn >= 0x0C82 && cIn <= 0x0C83) 
                || (cIn >= 0x0CBE && cIn <= 0x0CC4) 
                || (cIn >= 0x0CC6 && cIn <= 0x0CC8) 
                || (cIn >= 0x0CCA && cIn <= 0x0CCD) 
                || (cIn >= 0x0CD5 && cIn <= 0x0CD6) 
                || (cIn >= 0x0D02 && cIn <= 0x0D03) 
                || (cIn >= 0x0D3E && cIn <= 0x0D43) 
                || (cIn >= 0x0D46 && cIn <= 0x0D48) 
                || (cIn >= 0x0D4A && cIn <= 0x0D4D) 
                || cIn == 0x0D57 || cIn == 0x0E31 
                || (cIn >= 0x0E34 && cIn <= 0x0E3A) 
                || (cIn >= 0x0E47 && cIn <= 0x0E4E) 
                || cIn == 0x0EB1 
                || (cIn >= 0x0EB4 && cIn <= 0x0EB9) 
                || (cIn >= 0x0EBB && cIn <= 0x0EBC) 
                || (cIn >= 0x0EC8 && cIn <= 0x0ECD) 
                || (cIn >= 0x0F18 && cIn <= 0x0F19) 
                || cIn == 0x0F35 || cIn == 0x0F37 || cIn == 0x0F39 
                || cIn == 0x0F3E || cIn == 0x0F3F 
                || (cIn >= 0x0F71 && cIn <= 0x0F84) 
                || (cIn >= 0x0F86 && cIn <= 0x0F8B) 
                || (cIn >= 0x0F90 && cIn <= 0x0F95) 
                || cIn == 0x0F97 
                || (cIn >= 0x0F99 && cIn <= 0x0FAD) 
                || (cIn >= 0x0FB1 && cIn <= 0x0FB7) 
                || cIn == 0x0FB9 
                || (cIn >= 0x20D0 && cIn <= 0x20DC) 
                || cIn == 0x20E1 
                || (cIn >= 0x302A && cIn <= 0x302F) 
                || cIn == 0x3099 || cIn == 0x309A;
    }
    
    /**
     * @param cIn The character to check.
     * @return Is this a valid XML digit.
     */
    protected static boolean isDigit(final char cIn) {
        return (cIn >= 0x0030 && cIn <= 0x0039) 
               || (cIn >= 0x0660 && cIn <= 0x0669) 
               || (cIn >= 0x06F0 && cIn <= 0x06F9) 
               || (cIn >= 0x0966 && cIn <= 0x096F) 
               || (cIn >= 0x09E6 && cIn <= 0x09EF) 
               || (cIn >= 0x0A66 && cIn <= 0x0A6F) 
               || (cIn >= 0x0AE6 && cIn <= 0x0AEF) 
               || (cIn >= 0x0B66 && cIn <= 0x0B6F) 
               || (cIn >= 0x0BE7 && cIn <= 0x0BEF) 
               || (cIn >= 0x0C66 && cIn <= 0x0C6F) 
               || (cIn >= 0x0CE6 && cIn <= 0x0CEF) 
               || (cIn >= 0x0D66 && cIn <= 0x0D6F) 
               || (cIn >= 0x0E50 && cIn <= 0x0E59) 
               || (cIn >= 0x0ED0 && cIn <= 0x0ED9) 
               || (cIn >= 0x0F20 && cIn <= 0x0F29);
    }
    
    /**
     * @param cIn The character to check.
     * @return Is this a valid XML Extender.
     */
    protected static boolean isExtender(final char cIn) {
        return cIn == 0x00B7 || cIn == 0x02D0 
               || cIn == 0x02D1 || cIn == 0x0387 
               || cIn == 0x0640 || cIn == 0x0E46 
               || cIn == 0x0EC6 || cIn == 0x3005 
               || (cIn >= 0x3031 && cIn <= 0x3035) 
               || (cIn >= 0x309D && cIn <= 0x309E) 
               || (cIn >= 0x30FC && cIn <= 0x30FE);
    }

}
