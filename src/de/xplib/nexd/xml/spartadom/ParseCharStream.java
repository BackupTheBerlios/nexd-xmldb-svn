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

import java.io.IOException;
import java.io.Reader;
import java.util.Hashtable;


/**
 * An XML character stream that has been parsed into a DOM tree. This class
 * encapsulates the Sparta XML parsing.
 * 
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 * @link http://sparta.sourceforge.net
 */
class ParseCharStream implements ParseSource {

    /**
     * Comment for <code>BEGIN_CDATA</code>
     */
    private static final char[] BEGIN_CDATA = "<![CDATA[".toCharArray();

    /**
     * Comment for <code>BEGIN_ETAG</code>
     */
    private static final char[] BEGIN_ETAG = "</".toCharArray();

    //private static final int LOOKAHEAD = 9;

    /**
     * Replaced PeekReader with character array. 10X speed improvement.
     * Comment for <code>CBUF_SIZE</code>
     */
    private static final int CBUF_SIZE = 1024;

    /**
     * Comment for <code>CHARREF_BEGIN</code>
     */
    private static final char[] CHARREF_BEGIN = "&#".toCharArray();

    /**
     * Comment for <code>COMMENT_BEGIN</code>
     */
    private static final char[] COMMENT_BEGIN = "<!--".toCharArray();

    /**
     * Comment for <code>COMMENT_END</code>
     */
    private static final char[] COMMENT_END = "-->".toCharArray();

    /**
     * Comment for <code>DEBUG</code>
     */
    private static final boolean DEBUG = true;

    /**
     * Comment for <code>DOCTYPE_BEGIN</code>
     */
    private static final char[] DOCTYPE_BEGIN = "<!DOCTYPE".toCharArray();

    /**
     * Comment for <code>ENCODING</code>
     */
    private static final char[] ENCODING = "encoding".toCharArray();

    /**
     * Comment for <code>END_CDATA</code>
     */
    private static final char[] END_CDATA = "]]>".toCharArray();

    /**
     * Comment for <code>END_EMPTYTAG</code>
     */
    private static final char[] END_EMPTYTAG = "/>".toCharArray();

    /**
     * Comment for <code>ENTITY_BEGIN</code>
     */
    private static final char[] ENTITY_BEGIN = "<!ENTITY".toCharArray();

    /**
     * Comment for <code>H_DEBUG</code>
     */
    private static final boolean H_DEBUG = false;

    /**
     * Comment for <code>HISTORY_LENGTH</code>
     */
    public static final int HISTORY_LENGTH = 100;
    
    /**
     * Comment for <code>MAX_COMMON_CHAR</code>
     */
    private static final int MAX_COMMON_CHAR = 128;

    /**
     * Avoid calculations for most common characters.
     * Comment for <code>IS_NAME_CHAR</code>
     */
    private static final boolean[] IS_NAME_CHAR = new boolean[MAX_COMMON_CHAR];

    /**
     * Comment for <code>MARKUPDECL_BEGIN</code>
     */
    private static final char[] MARKUPDECL_BEGIN = "<!".toCharArray();

    /**
     * Comment for <code>NAME_PUNCT_CHARS</code>
     */
    private static final char[] NAME_PUNCT_CHARS = {'.', '-', '_', ':'};

    /**
     * Comment for <code>NDATA</code>
     */
    private static final char[] NDATA = "NDATA".toCharArray();

    /**
     * Comment for <code>PI_BEGIN</code>
     */
    private static final char[] PI_BEGIN = "<?".toCharArray();

    /**
     * Comment for <code>PUBLIC</code>
     */
    private static final char[] PUBLIC = "PUBLIC".toCharArray();

    /**
     * Comment for <code>QU_END</code>
     */
    private static final char[] QU_END = "?>".toCharArray();

    /**
     * Comment for <code>SYSTEM</code>
     */
    private static final char[] SYSTEM = "SYSTEM".toCharArray();

    /**
     * Empty char buffer used to fill with char data
     * Comment for <code>TMP_BUF_SIZE</code>
     */
    private static final int TMP_BUF_SIZE = 255;

    /**
     * Comment for <code>VERSION</code>
     */
    private static final char[] VERSION = "version".toCharArray();

    /**
     * Comment for <code>VERSIONNUM_PUNC_CHARS</code>
     */
    private static final char[] VERSIONNUM_PUNC_CHARS = {'_', '.', ':', '-'};

    /**
     * Comment for <code>XML_BEGIN</code>
     */
    private static final char[] XML_BEGIN = "<?xml".toCharArray();
    
    static {
        for (char ch = 0; ch < MAX_COMMON_CHAR; ++ch) {
            IS_NAME_CHAR[ch] = isNameChar(ch);
        }
    }

    /**
     * [89] Extender ::= #x00B7 | #x02D0 | #x02D1 | #x0387 | #x0640 | #x0E46 |
     * #x0EC6 | #x3005 | [#x3031-#x3035] | [#x309D-#x309E] | [#x30FC-#x30FE]
     * 
     * @param ch ..
     * @return ..
     */
    private static boolean isExtender(final char ch) {
        //verbose but efficient
        switch (ch) {
        case '\u00B7':
        case '\u02D0':
        case '\u02D1':
        case '\u0387':
        case '\u0640':
        case '\u0E46':
        case '\u0EC6':
        case '\u3005':
        case '\u3031':
        case '\u3032':
        case '\u3033':
        case '\u3034':
        case '\u3035':
        case '\u309D':
        case '\u309E':
        case '\u30FC':
        case '\u30FD':
        case '\u30FE':
            return true;
        default:
            return false;
        }
    }

    /**
     * private final boolean isChar(char[] expected) throws ParseException,
     * IOException{ if (curPosition >= endPosition) if (fillBuf() == -1) 
     *    return false;
     *    return isIn(cbuffer[curPosition],expected); }
     * 
     * @param ch ..
     * @param expected ..
     * @return ..
     */
    private static boolean isIn(final char ch, final char[] expected) {
        for (int i = 0; i < expected.length; ++i) {
            if (ch == expected[i]) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param ch ..
     * @return ..
     */
    private static boolean isLetter(final char ch) {
        return "abcdefghijklmnopqrstuvwxyz".indexOf(
                Character.toLowerCase(ch)) != -1;
    }

    /**
     * @param ch ..
     * @return ..
     */
    private static boolean isNameChar(final char ch) {
        //return Unicode.isUnicodeIdentifierPart(ch)
        //    || isIn(ch,NAME_PUNCT_CHARS)
        //    || Unicode.getType(ch)==Unicode.COMBINING_SPACING_MARK
        //    || isExtender(ch);
        return Character.isDigit(ch) || isLetter(ch)
                || isIn(ch, NAME_PUNCT_CHARS) || isExtender(ch);
    }

    /**
     * Comment for <code>cbuffer</code>
     */
    private final char[] cbuffer;

    /**
     * Comment for <code>lastChar</code>
     */
    private int lastChar = -2; //last char read

    /**
     * Comment for <code>curPosition</code>
     */
    private int curPosition = 0;

    /**
     * Comment for <code>docTypeName</code>
     */
    private String docTypeName = null;

    /**
     * Comment for <code>encoding</code>
     */
    private final String encoding;

    /**
     * Comment for <code>endPosition</code>
     */
    private int endPosition = 0;

    //private final char[] buf_ = new char[LOOKAHEAD];
    
    /**
     * Comment for <code>entities</code>
     */
    private final Hashtable entities = new Hashtable();

    /**
     * Comment for <code>endOfStream</code>
     */
    private boolean endOfStream = false; // End of stream identifier

    /**
     * Comment for <code>handler</code>
     */
    private final ParseHandler handler;

    /**
     * Comment for <code>history</code>
     */
    private final CharCircBuffer history;

    /**
     * Comment for <code>isExternalDtd</code>
     */
    private boolean isExternalDtd = false;

    // Debug information
    /**
     * Comment for <code>lineNumber</code>
     */
    private int lineNumber = -1;

    /**
     * Comment for <code>log</code>
     */
    private final ParseLog log;

    /**
     * Comment for <code>entityRefs</code>
     */
    private final Hashtable entityRefs = new Hashtable();

    /**
     * Comment for <code>reader</code>
     */
    private final Reader reader;

    //////////////////////////////////////////////////////////////

    /**
     * Comment for <code>systemId</code>
     */
    private String systemId; // Temp not final

    /**
     * Comment for <code>tmpBuffer</code>
     */
    private final char[] tmpBuffer = new char[TMP_BUF_SIZE];

    /** 
     * Constructor used when passing in XML stored in a string 
     *
     * @param systemIdIn ..
     * @param xmlDataIn ..
     * @param logIn ..
     * @param encodingIn ..
     * @param handlerIn ..
     * @throws ParseException ..
     * @throws EncodingMismatchException ..
     * @throws IOException ..
     */
    public ParseCharStream(final String systemIdIn, 
                           final char[] xmlDataIn, 
                           final ParseLog logIn,
                           final String encodingIn, 
                           final ParseHandler handlerIn) 
                                   throws ParseException,
                                          EncodingMismatchException, 
                                          IOException {
        
        this(systemIdIn, null, xmlDataIn, logIn, encodingIn, handlerIn);
    }

    /**
     * Parse XML document from characters stream according to W3C grammar. [1]
     * document ::= prolog element Misc*
     * 
     * @param systemIdIn ..
     * @param readerIn .. 
     * @param xmlDataIn ..
     * @param logIn ..
     * @param encodingIn ..
     * @param handlerIn ..
     * @throws ParseException ..
     * @throws EncodingMismatchException ..
     * @throws IOException ..
     *  @see <a href="http://www.w3.org/TR/2000/REC-xml-20001006">
     *         http://www.w3.org/TR/2000/REC-xml-20001006 </a>
     */
    public ParseCharStream(final String systemIdIn, 
                           final Reader readerIn, 
                           final char[] xmlDataIn,
                           final ParseLog logIn, 
                           final String encodingIn, 
                           final ParseHandler handlerIn)
                                   throws ParseException, 
                                          EncodingMismatchException, 
                                          IOException {
        if (DEBUG) {
            lineNumber = 1;
        }
        if (H_DEBUG) {
            history = new CharCircBuffer(HISTORY_LENGTH);
            history.addString("1:");
        } else {
            history = null;
        }

        log = (logIn == null) ? DEFAULT_LOG : logIn;
        encoding = encodingIn == null ? null : encodingIn.toLowerCase();

        //http://www.w3.org/TR/2000/REC-xml-20001006#sec-predefined-ent
        entities.put("lt", "<");
        entities.put("gt", ">");
        entities.put("amp", "&");
        entities.put("apos", "\'");
        entities.put("quot", "\"");

        // Set input stream buffer. Either use string char array or
        // fill from character reader
        if (xmlDataIn != null) {
            cbuffer = xmlDataIn;
            curPosition = 0;
            endPosition = cbuffer.length;
            endOfStream = true;
            reader = null;
        } else {
            reader = readerIn;
            cbuffer = new char[CBUF_SIZE];
            fillBuf();
        }

        systemId = systemIdIn;

        // Set the ParseHandler for parsing
        handler = handlerIn;
        handler.setParseSource(this);

        readProlog();

        handler.startDocument();

        BaseElement rootElement = readElement(/* null */);

        if (docTypeName != null
                && !docTypeName.equals(rootElement.getTagName())) {
            
            log.warning("DOCTYPE name \"" + docTypeName
                    + "\" not same as tag name, \"" + rootElement.getTagName()
                    + "\" of root element", systemId, getLineNumber());
        }
        while (isMisc()) {
            readMisc();
        }

        if (reader != null) {
            reader.close();
        }
        handler.endDocument();
    }

    /** 
     * Constructor used when passing in XML from a character stream 
     * 
     * @param systemIdIn ..
     * @param readerIn ..
     * @param logIn ..
     * @param encodingIn ..
     * @param handlerIn ..
     * @throws ParseException ..
     * @throws EncodingMismatchException ..
     * @throws IOException ..
     */
    public ParseCharStream(final String systemIdIn, 
                           final Reader readerIn, 
                           final ParseLog logIn,
                           final String encodingIn, 
                           final ParseHandler handlerIn) 
                                   throws ParseException,
                                          EncodingMismatchException, 
                                          IOException {
        this(systemIdIn, readerIn, null, logIn, encodingIn, handlerIn);
    }

    /**
     * @return ..
     * @throws IOException ..
     */
    private int fillBuf() throws IOException {
        if (endOfStream) {
            return -1;
        }

        if (endPosition == cbuffer.length) {
            //if (curPosition != endPosition)
            //    throw new Error("Assertion failed in Sparta: 
            // curPosition != (endPosition
            // == cbuffer.length)");
            curPosition = 0;
            endPosition = 0;
        }

        int count = reader.read(
                cbuffer, endPosition, cbuffer.length - endPosition);
        if (count <= 0) {
            endOfStream = true;
            return -1;
        }
        endPosition += count;
        return count;
    }

    /**
     * @param min ..
     * @return ..
     * @throws IOException ..
     */
    private int fillBuf(final int min) throws IOException {
        if (endOfStream) {
            return -1;
        }

        int count = 0;
        if (cbuffer.length - curPosition < min) {
            for (int i = 0; curPosition + i < endPosition; i++) {
                cbuffer[i] = cbuffer[curPosition + i];
            }
            count = endPosition - curPosition;
            endPosition = count;
            curPosition = 0;
        }
        int res = fillBuf();
        if (res == -1) {
            if (count == 0) {
                return -1;
            } else {
                return count;
            }
        } else {
            return count + res;
        }

    }

    /**
     * @return ..
     */
    final String getHistory() {
        if (H_DEBUG) {
            return history.toString();
        } else {
            return "";
        }
    }

    /**
     * @return ..
     */
    int getLastCharRead() {
        return lastChar;
    }

    /**
     * Last line number read by parser.
     * 
     * @return ..
     * @see de.xplib.nexd.xml.spartadom.ParseSource#getLineNumber()
     */
    public int getLineNumber() {
        return lineNumber;
    }

    /**
     * @return ..
     */
    ParseLog getLog() {
        return log;
    }

    /**
     * <Some description here>
     * 
     * @return ...
     * @see de.xplib.nexd.xml.spartadom.ParseSource#getSystemId()
     */
    public String getSystemId() {
        return systemId;
    }

    /**
     * @return ..
     * @throws ParseException ..
     * @throws IOException ..
     */
    private boolean isCdSect() throws ParseException, IOException {
        return isSymbol(BEGIN_CDATA);
    }

    /**
     * @param expected ..
     * @return ..
     * @throws ParseException ..
     * @throws IOException ..
     */
    private boolean isChar(final char expected) 
    		throws ParseException, IOException {
        
        if (curPosition >= endPosition) {
            if (fillBuf() == -1) {
                throw new ParseException(this, "unexpected end of expression.");
            }
        }
        return (cbuffer[curPosition] == expected);
    }

    /**
     * @param expect0 ..
     * @param expect1 ..
     * @return ..
     * @throws ParseException ..
     * @throws IOException ..
     */
    private boolean isChar(final char expect0, final char expect1)
            throws ParseException, IOException {
        
        if (curPosition >= endPosition) {
            if (fillBuf() == -1) {
                return false;
            }
        }
        final char ch = cbuffer[curPosition];
        return ch == expect0 || ch == expect1;
    }

    /**
     * @param expect0 ..
     * @param expect1 ..
     * @param expect2 ..
     * @param expect3 ..
     * @return ..
     * @throws ParseException ..
     * @throws IOException ..
     */
    private boolean isChar(final char expect0, 
            	           final char expect1, 
            	           final char expect2,
            	           final char expect3) 
                                   throws ParseException, IOException {
        
        if (curPosition >= endPosition) {
            if (fillBuf() == -1) {
                return false;
            }
        }
        final char ch = cbuffer[curPosition];
        return ch == expect0 || ch == expect1 || ch == expect2 || ch == expect3;
    }

    /**
     * @return ..
     * @throws ParseException ..
     * @throws IOException ..
     */
    private boolean isComment() throws ParseException, IOException {
        return isSymbol(COMMENT_BEGIN);
    }

    /**
     * @return ..
     * @throws ParseException ..
     * @throws IOException ..
     */
    private boolean isDeclSep() throws ParseException, IOException {
        return isPeReference() || isS();
    }

    /**
     * @return ..
     * @throws ParseException ..
     * @throws IOException ..
     */
    private boolean isDocTypeDecl() throws ParseException, IOException {
        return isSymbol(DOCTYPE_BEGIN);
    }

    /**
     * @return ..
     * @throws ParseException ..
     * @throws IOException ..
     */
    private boolean isEncodingDecl() throws ParseException, IOException {
        return isSymbol(ENCODING);
    }

    /**
     * @return ..
     * @throws ParseException ..
     * @throws IOException ..
     */
    private boolean isEntityDecl() throws ParseException, IOException {
        return isSymbol(ENTITY_BEGIN);
    }

    /**
     * @return ..
     * @throws ParseException ..
     * @throws IOException ..
     */
    private boolean isEntityValue() throws ParseException, IOException {
        return isChar('\'', '\"');
    }

    /**
     * @return ..
     * @throws ParseException ..
     * @throws IOException ..
     */
    private boolean isETag() throws ParseException, IOException {
        return isSymbol(BEGIN_ETAG);
    }

    /**
     * @return ..
     * @throws ParseException ..
     * @throws IOException ..
     */
    private boolean isExternalId() throws ParseException, IOException {
        return isSymbol(SYSTEM) || isSymbol(PUBLIC);
    }

    /**
     * @return ..
     * @throws ParseException ..
     * @throws IOException ..
     */
    private boolean isMisc() throws ParseException, IOException {
        return isComment() || isPi() || isS();
    }

    /**
     * [4] NameChar ::= Letter | Digit | '.' | '-' | '_' | ':' | CombiningChar |
     * Extender
     *
     * @return ..
     * @throws ParseException ..
     * @throws IOException ..
     */
    private boolean isNameChar() throws ParseException, IOException {
        char ch = peekChar();
        return (ch < MAX_COMMON_CHAR) ? IS_NAME_CHAR[ch] : isNameChar(ch);
    }

    /**
     * @return ..
     * @throws ParseException ..
     * @throws IOException ..
     */
    private boolean isPeReference() throws ParseException, IOException {
        return isChar('%');
    }

    /**
     * @return ..
     * @throws ParseException ..
     * @throws IOException ..
     */
    private boolean isPi() throws ParseException, IOException {
        return isSymbol(PI_BEGIN);
    }

    /**
     * @return ..
     * @throws ParseException ..
     * @throws IOException ..
     */
    private boolean isReference() throws ParseException, IOException {
        return isChar('&');
    }

    /**
     * @return ..
     * @throws ParseException ..
     * @throws IOException ..
     */
    private boolean isS() throws ParseException, IOException {
        return isChar(' ', '\t', '\r', '\n');
    }

    /**
     * @param expected ..
     * @return ..
     * @throws ParseException ..
     * @throws IOException ..
     */
    private boolean isSymbol(final char[] expected) 
    		throws ParseException, IOException {
        
        final int n = expected.length;
        if (endPosition - curPosition < n) {
            if (fillBuf(n) <= 0) {
                lastChar = -1;
                return false;
            }
        }
        lastChar = cbuffer[endPosition - 1];

        if (endPosition - curPosition < n) {
            return false;
        }

        //compare actual with expected
        //int startPos = curPosition;
        for (int i = 0; i < n; ++i) {
            if (cbuffer[curPosition + i] != expected[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return ..
     * @throws ParseException ..
     * @throws IOException ..
     */
    private boolean isVersionNumChar() throws ParseException, IOException {
        char ch = peekChar();
        return Character.isDigit(ch) || 'a' <= ch && ch <= 'z' || 'Z' <= ch
                && ch <= 'Z' || isIn(ch, VERSIONNUM_PUNC_CHARS);
    }

    /**
     * @return ..
     * @throws ParseException ..
     * @throws IOException ..
     */
    private boolean isXmlDecl() throws ParseException, IOException {
        return isSymbol(XML_BEGIN);
    }

    /**
     * @return ..
     * @throws ParseException ..
     * @throws IOException ..
     */
    private char peekChar() throws ParseException, IOException {
        //APPROXIMATION
        if (curPosition >= endPosition) {
            if (fillBuf() == -1) {
                throw new ParseException(this, "unexpected end of expression.");
            }
        }
        return cbuffer[curPosition];
    }

    /** [41] Attribute ::= Name Eq AttValue 
     *
     * @param element ..
     * @throws ParseException ..
     * @throws IOException ..
     */
    private void readAttribute(final BaseElement element)
    		throws ParseException, IOException {
        
        String name = readName();
        readEq();
        String value = readAttValue();
        //http://www.w3.org/TR/2000/REC-xml-20001006#uniqattspec
        if (element.getAttribute(name) != null) {
            log.warning("Element " + this + " contains attribute " + name
                    + "more than once", systemId, getLineNumber());
        }
        element.setAttribute(name, value);
    }

    //////////////////////////////////////////////////////////////
    /**
     * [10] AttValue ::= '"' ([^ <&"] | Reference)* '"' | "'" ([^ <&'] |
     * Reference)* "'"
     *
     * @return ..
     * @throws ParseException ..
     * @throws IOException ..
     */
    private String readAttValue() throws ParseException, IOException {
        char quote = readChar('\'', '\"');
        StringBuffer result = new StringBuffer();
        while (!isChar(quote)) {
            if (isReference()) {
                result.append(readReference());
            } else {
                result.append(readChar());
            }
        }
        readChar(quote);
        return result.toString();
    }

    /**
     * [18] CDSect ::= CDStart CData CDEnd [19] CDStart ::= ' <![CDATA[' [20]
     * CData ::= (Char* - (Char* ']]>' Char*)) [21] CDEnd ::= ']]>'
     *
     * @throws ParseException ..
     * @throws IOException ..
     */
    private void readCdSect(/* Element element */) 
            throws ParseException, IOException {
        
        StringBuffer result = null;
        readSymbol(BEGIN_CDATA);
        int i = 0;
        while (!isSymbol(END_CDATA)) {
            if (i >= TMP_BUF_SIZE) {
                if (result == null) {
                    result = new StringBuffer(i);
                    result.append(tmpBuffer, 0, i);
                } else {
                    result.append(tmpBuffer, 0, i);
                }
                i = 0;
            }
            tmpBuffer[i++] = readChar();
        }
        readSymbol(END_CDATA);

        if (result != null) {
            result.append(tmpBuffer, 0, i);
            char[] cdSect = result.toString().toCharArray();
            handler.characters(cdSect, 0, cdSect.length);
        } else {
            handler.characters(tmpBuffer, 0, i);
        }

        /*
         * Old style StringBuffer buf = new StringBuffer();
         * readSymbol(BEGIN_CDATA); while( !isSymbol(END_CDATA) ) buf.append(
         * readChar() ); readSymbol(END_CDATA); if( buf.length() > 0 ) { char
         * cdSect[] = buf.toString().toCharArray(); handler.characters(cdSect,
         * 0, cdSect.length); }
         */
    }

    /**
     * [2] Char ::= #x9 | #xA | #xD | [#x20-#xD7FF] | [#xE000-#xFFFD] |
     * [#x10000-#x10FFFF]
     *
     * @return ..
     * @throws ParseException ..
     * @throws IOException ..
     */
    private char readChar() throws ParseException, IOException {
        //APPROXIMATION
        if (curPosition >= endPosition) {
            if (fillBuf() == -1) {
                throw new ParseException(this, "unexpected end of expression.");
            }
        }
        if (DEBUG) {
            if (cbuffer[curPosition] == '\n') {
                lineNumber++;
            }
        }
        if (H_DEBUG) {
            history.addChar(cbuffer[curPosition]);
            if (cbuffer[curPosition] == '\n') {
                history.addInt(lineNumber);
                history.addChar(':');
            }
        }

        return cbuffer[curPosition++];
    }

    /**
     * @param expected ..
     * @throws ParseException ..
     * @throws IOException ..
     */
    private void readChar(final char expected) 
    		throws ParseException, IOException {
        
        final char ch = readChar();
        if (ch != expected) {
            throw new ParseException(this, ch, expected);
        }
    }

    /*
     * private final char readChar(char[] expected) throws ParseException,
     * IOException{ char ch = readChar(); if( !isIn(ch,expected) ) throw new
     * ParseException(this,ch,expected); return ch; }
     */

    /**
     * @param expect0 ..
     * @param expect1 ..
     * @return ..
     * @throws ParseException ..
     * @throws IOException ..
     */
    private char readChar(final char expect0, final char expect1)
            throws ParseException, IOException {
        
        final char ch = readChar();
        if (ch != expect0 && ch != expect1) {
            throw new ParseException(this, ch, new char[] {expect0, expect1});
        }
        return ch;
    }

    /**
     * @param expect0 ..
     * @param expect1 ..
     * @param expect2 ..
     * @param expect3 ..
     * @return ..
     * @throws ParseException ..
     * @throws IOException ..
     */
    private char readChar(final char expect0, 
                          final char expect1,
                          final char expect2,
                          final char expect3) 
                                  throws ParseException, IOException {
        
        final char ch = readChar();
        if (ch != expect0 && ch != expect1 && ch != expect2 && ch != expect3) {
            throw new ParseException(this, ch, new char[] {expect0, expect1,
                    expect2, expect3});
        }
        return ch;
    }

    /**
     * [66] CharRef ::= '&#' [0-9]+ ';'
     *
     * @return ..
     * @throws ParseException ..
     * @throws IOException ..
     */
    private char readCharRef() throws ParseException, IOException {
        readSymbol(CHARREF_BEGIN);
        int radix = 10;
        if (isChar('x')) {
            readChar();
            radix = 16;
        }
        int i = 0;
        while (!isChar(';')) {
            tmpBuffer[i++] = readChar();
            if (i >= TMP_BUF_SIZE) {
                log.warning("Tmp buffer overflow on readCharRef", systemId,
                        getLineNumber());
                return ' ';
            }
        }
        readChar(';');
        String num = new String(tmpBuffer, 0, i);
        try {
            return (char) Integer.parseInt(num, radix);
        } catch (NumberFormatException e) {
            log.warning("\"" + num + "\" is not a valid "
                    + (radix == 16 ? "hexadecimal" : "decimal") + " number",
                    systemId, getLineNumber());
            return ' ';
        }
    }

    /**
     * [15] Comment ::= ' <!--' ((Char - '-') | ('-' (Char - '-')))* '-->'
     *
     * @throws ParseException ..
     * @throws IOException ..
     */
    private void readComment() throws ParseException, IOException {
        //This is actually less strict than the spec because it allows
        //embedded -- and comments ending with --->
        
        readSymbol(COMMENT_BEGIN);
        int start = this.curPosition;
        while (!isSymbol(COMMENT_END)) {
            readChar();
        }
        int end = this.curPosition;
        readSymbol(COMMENT_END);
        
        
        StringBuilder sb = new StringBuilder();
        for (int i = start; i < end; i++) {
            sb.append(this.cbuffer[i]);
        }
        System.out.println("[" + sb.toString() + "]");
    }

    /**
     * [43] content ::= CharData? ( (element | Reference | CDSect | PI |
     * Comment) CharData? )*
     *
     * @throws ParseException ..
     * @throws IOException ..
     */
    private void readContent(/* Element element */) 
    		throws ParseException, IOException {
        
        readPossibleCharData(/* element */);
        boolean keepGoing = true;
        while (keepGoing) {
            if (isETag()) {
                keepGoing = false;
            } else if (isReference()) {
                // appendText( element, readReference() );
                char[] ref = readReference();
                handler.characters(ref, 0, ref.length);
            } else if (isCdSect()) {
                readCdSect(/* element */);
            } else if (isPi()) {
                readPi();
            } else if (isComment()) {
                readComment();
            } else if (isChar('<')) {
                readElement(/* element */);
            } else {
                keepGoing = false;
            }
            readPossibleCharData(/* element */);
        }

    }

    /** 
     * [28a] DeclSep ::= PEReference | S 
     *
     * @throws ParseException ..
     * @throws IOException ..
     */
    private void readDeclSep() throws ParseException, IOException {
        if (isPeReference()) {
            readPeReference();
        } else {
            readS();
        }
    }

    /**
     * [28] doctypedecl ::= '<!DOCTYPE' S Name (1) ( S ExternalID )? (2) S? (3) 
     * ('[' (markupdecl|DeclSep)* ']' S? )? '>'
     *
     * @throws ParseException ..
     * @throws IOException ..
     */
    private void readDocTypeDecl() throws ParseException, IOException {
        readSymbol(DOCTYPE_BEGIN);
        readS();
        docTypeName = readName();
        if (isS()) {
            //either at (1) or (2)
            readS();
            if (!isChar('>') && !isChar('[')) {
                //was at (1)
                isExternalDtd = true; //less checking of entity references
                readExternalId();
                //now at (2)
                if (isS()) {
                    readS();
                }
            }
        }
        //now at (3)
        if (isChar('[')) {
            readChar();
            while (!isChar(']')) {
                if (isDeclSep()) {
                    readDeclSep();
                } else {
                    readMarkupDecl();
                }
            }
            readChar(']');
            if (isS()) {
                readS();
            }
        }
        readChar('>');
    }

    /**
     * Parse element using stream in document. [39] element ::= EmptyElemTag |
     * STag content ETag
     *
     * @return ..
     * @throws ParseException ..
     * @throws IOException ..
     */
    private BaseElement readElement(/* Element parentElement */) 
    		throws ParseException, IOException {
        
        final BaseElement element = new BaseElement();

        final boolean isSTag = readEmptyElementTagOrSTag(element);

        handler.startElement(element);

        if (isSTag) {
            readContent(/* element */
            );
            readETag(element);
        }

        handler.endElement(element);

        //element.normalize();
        return element;
    }

    /**
     * Return if this is a STag [40] STag ::= ' <' Name (S Attribute)* S? '>'
     * [44] EmptyElemTag ::= ' <' Name (S Attribute)* S? '/>'
     *
     * @param element ..
     * @return ..
     * @throws ParseException ..
     * @throws IOException ..
     */
    private boolean readEmptyElementTagOrSTag(final BaseElement element)
            throws ParseException, IOException {
        
        readChar('<');
        element.setTagName(readName());
        while (isS()) {
            readS();
            if (!isChar('/', '>')) {
                readAttribute(element);
            }
        }
        if (isS()) {
            readS();
        }
        boolean isSTag = isChar('>');
        if (isSTag) {
            readChar('>');
        } else {
            readSymbol(END_EMPTYTAG);
        }
        return isSTag;

    }

    /**
     * [80] EncodingDecl ::= S 'encoding' Eq ('"'EncName'"' | "'"EncName"'" )
     * [81] EncName ::= [A-Za-z] ([A-Za-z0-9._] | '-')*
     *
     * @return ..
     * @throws ParseException ..
     * @throws IOException ..
     */
    private String readEncodingDecl() throws ParseException, IOException {
        readSymbol(ENCODING);
        readEq();
        char quote = readChar('\'', '\"');
        StringBuffer result = new StringBuffer();
        while (!isChar(quote)) {
            result.append(readChar());
        }
        readChar(quote);
        return result.toString();
    }

    /**
     * [70] EntityDecl ::= GEDecl | PEDecl [71] GEDecl ::= ' <!ENTITY' S Name S
     * EntityDef S? '>' [72] PEDecl ::= ' <!ENTITY' S '%' S Name S PEDef S? '>'
     * [73] EntityDef ::= EntityValue | (ExternalID NDataDecl?) [74] PEDef ::=
     * EntityValue | ExternalID [76] NDataDecl ::= S 'NDATA' S Name
     *
     * @throws ParseException ..
     * @throws IOException ..
     */
    private void readEntityDecl() throws ParseException, IOException {
        readSymbol(ENTITY_BEGIN);
        readS();
        if (isChar('%')) {
            readChar('%');
            readS();
            String name = readName();
            readS();
            String value;
            if (isEntityValue()) {
                value = readEntityValue();
            } else {
                value = readExternalId();
            }
            entityRefs.put(name, value);
        } else {
            String name = readName();
            readS();
            String value;
            if (isEntityValue()) {
                value = readEntityValue();
            } else if (isExternalId()) {
                value = readExternalId();
                if (isS()) {
                    readS();
                }
                if (isSymbol(NDATA)) {
                    readSymbol(NDATA);
                    readS();
                    readName();
                }
            } else {
                throw new ParseException(
                        this, "expecting double-quote, \"PUBLIC\" or "
                        + "\"SYSTEM\" while reading entity declaration");
            }
            entities.put(name, value);
        }
        if (isS()) {
            readS();
        }
        readChar('>');
    }

    /**
     * [68] EntityRef ::= '&' Name ';'
     *
     * @return ..
     * @throws ParseException ..
     * @throws IOException ..
     */
    private String readEntityRef() throws ParseException, IOException {
        readChar('&');
        String name = readName();
        String result = (String) entities.get(name);
        //http://www.w3.org/TR/2000/REC-xml-20001006#vc-entdeclared
        if (result == null) {
            result = "";
            if (isExternalDtd) {
                log.warning("&" + name
                        + "; not found -- possibly defined in external DTD)",
                        systemId, getLineNumber());
            } else {
                log.warning("No declaration of &" + name + ";", systemId,
                        getLineNumber());
            }
        }
        readChar(';');
        return result;
    }

    /**
     * [9] EntityValue ::= '"' ( [^%&"] | PEReference | Reference )* '"'
     *
     * @return ..
     * @throws ParseException ..
     * @throws IOException ..
     */
    private String readEntityValue() throws ParseException, IOException {
        //grammar allows only double quote, but many xmlconf examples
        //use single quotes
        char quote = readChar('\'', '\"');
        StringBuffer result = new StringBuffer();
        while (!isChar(quote)) {
            if (isPeReference()) {
                result.append(readPeReference());
            } else if (isReference()) {
                result.append(readReference());
            } else {
                result.append(readChar());
            }
        }
        readChar(quote);
        return result.toString();
    }

    /** 
     * [25] Eq ::= S? '=' S? 
     *
     * @throws ParseException ..
     * @throws IOException ..
     */
    private void readEq() throws ParseException, IOException {
        if (isS()) {
            readS();
        }
        readChar('=');
        if (isS()) {
            readS();
        }
    }

    /** 
     * [42] ETag ::= ' </' Name S? '>' 
     *
     * @param element ..
     * @throws ParseException ..
     * @throws IOException ..
     */
    private void readETag(final BaseElement element) 
    		throws ParseException, IOException {
        
        readSymbol(BEGIN_ETAG);
        String name = readName();
        //http://www.w3.org/TR/2000/REC-xml-20001006#GIMatch
        if (!name.equals(element.getTagName())) {
            log.warning("end tag (" + name + ") does not match begin tag ("
                    + element.getTagName() + ")", systemId, getLineNumber());
        }
        if (isS()) {
            readS();
        }
        readChar('>');
    }

    /**
     * [75] ExternalID ::= 'SYSTEM' S SystemLiteral | 'PUBLIC' S PubidLiteral S
     * SystemLiteral
     *
     * @return ..
     * @throws ParseException ..
     * @throws IOException ..
     */
    private String readExternalId() throws ParseException, IOException {
        if (isSymbol(SYSTEM)) {
            readSymbol(SYSTEM);
        } else if (isSymbol(PUBLIC)) {
            readSymbol(PUBLIC);
            readS();
            readPubidLiteral();
        } else {
            throw new ParseException(this,
                    "expecting \"SYSTEM\" or \"PUBLIC\" "
                    + "while reading external ID");
        }
        readS();
        readSystemLiteral();
        return "(WARNING: external ID not read)"; //not implemented
    }

    /**
     * [29] markupdecl ::=
     * elementdecl|AttlistDecl|EntityDecl|NotationDecl|PI|Comment
     *
     * @throws ParseException ..
     * @throws IOException ..
     */
    private void readMarkupDecl() throws ParseException, IOException {
        if (isPi()) {
            readPi();
        } else if (isComment()) {
            readComment();
        } else if (isEntityDecl()) {
            readEntityDecl();
    	} else if (isSymbol(MARKUPDECL_BEGIN)) { 
        // (element-|Attlist-|Entity-|Notation-)Decl
            while (!isChar('>')) {
                if (isChar('\'', '\"')) {
                    char quote = readChar();
                    while (!isChar(quote)) {
                        readChar();
                    }
                    readChar(quote);
                } else {
                    readChar();
                }
            }
            readChar('>');
        } else {
            throw new ParseException(this,
                    "expecting processing instruction, comment, or \"<!\"");
        }
    }

    /**
     * @throws ParseException ..
     * @throws IOException ..
     */
    private void readMisc() throws ParseException, IOException {
        if (isComment()) {
            readComment();
        } else if (isPi()) {
            readPi();
        } else if (isS()) {
            readS();
        } else {
            throw new ParseException(this,
                    "expecting comment or processing instruction or space");
        }
    }

    /**
     * [5] Name ::= (Letter | '_' | ':') (NameChar) [84] Letter ::= BaseChar |
     * Ideographic
     *
     * @return ..
     * @throws ParseException ..
     * @throws IOException ..
     */
    private String readName() throws ParseException, IOException {
        StringBuffer result = null;
        int i = 0;
        tmpBuffer[i++] = readNameStartChar();
        while (isNameChar()) {
            if (i >= TMP_BUF_SIZE) {
                if (result == null) {
                    result = new StringBuffer(i);
                    result.append(tmpBuffer, 0, i);
                } else {
                    result.append(tmpBuffer, 0, i);
                }
                i = 0;
            }
            tmpBuffer[i++] = readChar();
        }
        if (result == null) {
            return new String(tmpBuffer, 0, i).intern();
        } else {
            // Interning because lots of repeats. Slower but less memory.
            result.append(tmpBuffer, 0, i);
            return result.toString();
        }
    }

    /**
     * @return ..
     * @throws ParseException ..
     * @throws IOException ..
     */
    private char readNameStartChar() throws ParseException, IOException {
        char ch = readChar();
        //if (!Unicode.isUnicodeIdentifierStart(ch) && ch != '_' && ch != ':')
        if (!isLetter(ch) && ch != '_' && ch != ':') {
            throw new ParseException(this, ch, "letter, underscore, colon");
        }
        return ch;
    }

    /*
     * Old methods private void appendText(Element element, String string) {
     * handler.characters(string); }
     * 
     * private void appendText(Element element, char ch){
     * handler.character(ch); }
     */

    /** 
     * [69] PEReference ::= '%' Name ';'
     *
     * @return ..
     * @throws ParseException ..
     * @throws IOException ..
     */
    private String readPeReference() throws ParseException, IOException {
        readChar('%');
        String name = readName();
        String result = (String) entityRefs.get(name);
        //http://www.w3.org/TR/2000/REC-xml-20001006#vc-entdeclared
        if (result == null) {
            result = "";
            log.warning("No declaration of %" + name + ";", systemId,
                    getLineNumber());
        }
        readChar(';');
        return result;
    }

    /** 
     * [16] PI ::= ' <?' PITarget (S (Char* - (Char* '?>' Char*)))? '?>'
     *
     * @throws ParseException ..
     * @throws IOException ..
     */
    private void readPi() throws ParseException, IOException {
        //APPROXIMATION -- treat as comment
        readSymbol(PI_BEGIN);
        while (!isSymbol(QU_END)) {
            readChar();
        }
        readSymbol(QU_END);
    }

    /**
     * [14] CharData ::= [^ <&]* - ( [^ <&]* ']]>' [^ <&]* )
     *
     * @throws ParseException ..
     * @throws IOException ..
     */
    private void readPossibleCharData(/* Element element */) 
    		throws ParseException, IOException {
        
        int i = 0;
        while (!isChar('<') && !isChar('&') && !isSymbol(END_CDATA)) {

            tmpBuffer[i] = readChar();

            //convert DOS line endings to UNIX
            if (tmpBuffer[i] == '\r' && peekChar() == '\n') {
                tmpBuffer[i] = readChar();
            }

            i++;
            if (i == TMP_BUF_SIZE) {
                handler.characters(tmpBuffer, 0, TMP_BUF_SIZE);
                i = 0;
            }
        }
        if (i > 0) {
            handler.characters(tmpBuffer, 0, i);
        }
    }

    /** 
     * www.w3.org/TR/2000/REC-xml-20001006#NT-prolog
     *
     * @throws ParseException ..
     * @throws EncodingMismatchException ..
     * @throws IOException ..
     */
    private void readProlog() 
    		throws ParseException, EncodingMismatchException, IOException {
        
        if (isXmlDecl()) {
            readXmlDecl();
        }
        while (isMisc()) {
            readMisc();
        }
        if (isDocTypeDecl()) {
            readDocTypeDecl();
            while (isMisc()) {
                readMisc();
            }
        }
    }

    /**
     * [12] PubidLiteral ::= '"' PubidChar* '"' | "'" (PubidChar - "'")* "'"
     *
     * @throws ParseException ..
     * @throws IOException ..
     */
    private void readPubidLiteral() throws ParseException, IOException {
        //APPROXIMATION
        readSystemLiteral();
    }

    /**
     * [67] Reference ::= EntityRef | CharRef
     *
     * @return ..
     * @throws ParseException ..
     * @throws IOException ..
     */
    private char[] readReference() throws ParseException, IOException {
        if (isSymbol(CHARREF_BEGIN)) {
            return new char[] {readCharRef()};
        }
        return readEntityRef().toCharArray();
    }

    /**
     * [3] S ::= (#x20 | #x9 | #xD | #xA)+
     * 
     * @throws ParseException ..
     * @throws IOException .. 
     */
    private void readS() throws ParseException, IOException {
        readChar(' ', '\t', '\r', '\n');
        while (isChar(' ', '\t', '\r', '\n')) {
            readChar();
        }
    }

    /**
     * @param expected ..
     * @throws ParseException ..
     * @throws IOException ..
     */
    private void readSymbol(final char[] expected) 
    		throws ParseException, IOException {
        
        int n = expected.length;
        if (endPosition - curPosition < n) {
            if (fillBuf(n) <= 0) {
                lastChar = -1;
                throw new ParseException(this, "end of XML file", expected);
            }
        }
        lastChar = cbuffer[endPosition - 1];

        if (endPosition - curPosition < n) {
            throw new ParseException(this, "end of XML file", expected);
        }
        //compare actual with expected
        for (int i = 0; i < n; ++i) {
            if (H_DEBUG) {
                history.addChar(cbuffer[curPosition + i]);
            }

            if (cbuffer[curPosition + i] != expected[i]) {
                throw new ParseException(this, 
                        new String(cbuffer, curPosition, n), expected);
            }
        }

        curPosition += n;
    }

    /**
     * [11] SystemLiteral ::= ('"' [^"]* '"') | ("'" [^']* "'")
     * 
     * @throws ParseException ..
     * @throws IOException ..
     */
    private void readSystemLiteral() throws ParseException, IOException {
        char quote = readChar();
        while (peekChar() != quote) {
            readChar();
        }
        readChar(quote);
    }

    /**
     * [24] VersionInfo::=S 'version' Eq ("'"VersionNum"'" | '"'VersionNum* '"')
     * 
     * @throws ParseException ..
     * @throws IOException ..
     */
    private void readVersionInfo() throws ParseException, IOException {
        readS();
        readSymbol(VERSION);
        readEq();
        //char quote = readChar(QUOTE_CHARS);
        char quote = readChar('\'', '\"');
        readVersionNum();
        readChar(quote);
    }

    /**
     * [26] VersionNum ::= ([a-zA-Z0-9_.:] | '-')+
     * 
     * @throws ParseException ..
     * @throws IOException ..
     */
    private void readVersionNum() throws ParseException, IOException {
        readChar();
        while (isVersionNumChar()) {
            readChar();
        }
    }

    /**
     * [23] XMLDecl ::= ' <?xml' VersionInfo EncodingDecl? SDDecl? S? '?>'
     * 
     * @throws ParseException ..
     * @throws EncodingMismatchException ..
     * @throws IOException ..
     */
    private void readXmlDecl() 
    		throws ParseException, EncodingMismatchException, IOException {
        
        readSymbol(XML_BEGIN);
        readVersionInfo();
        if (isS()) {
            readS();
        }
        if (isEncodingDecl()) {
            String encodingDeclared = readEncodingDecl();
            if (encoding != null
                    && !encodingDeclared.toLowerCase().equals(encoding)) {
                
                throw new EncodingMismatchException(systemId,
                        encodingDeclared, encoding);
            }
        }
        //APPROXIMATION:
        while (!isSymbol(QU_END)) {
            readChar();
        }
        readSymbol(QU_END);
    }

    /**
     * <Some description here>
     * 
     * @return
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return systemId;
    }

}

// $Log: ParseCharStream.java,v $
// Revision 1.7 2003/07/17 23:58:40 eobrain
// Make compatiblie with J2ME. For example do not use "new"
// java.util classes.
//
// Revision 1.6 2003/05/12 20:04:47 eobrain
// Performance improvements including some interning.
//
// Revision 1.5 2003/03/21 00:22:23 eobrain
// Lots of little performance optimizations.
//
// Revision 1.4 2003/01/27 23:30:58 yuhongx
// Replaced Hashtable with HashMap.
//
// Revision 1.3 2002/11/06 02:57:59 eobrain
// Organize imputs to removed unused imports. Remove some unused local
// variables.
//
// Revision 1.2 2002/08/21 20:18:12 eobrain
// Ignore case when comparing encodings.
//
// Revision 1.1.1.1 2002/08/19 05:04:00 eobrain
// import from HP Labs internal CVS
//
// Revision 1.17 2002/08/18 04:36:59 eob
// Make interface package-private so as not to clutter up the javadoc.
//
// Revision 1.16 2002/08/17 00:54:14 sermarti
//
// Revision 1.15 2002/08/15 23:40:22 sermarti
//
// Revision 1.14 2002/08/05 20:04:32 sermarti
//
// Revision 1.13 2002/08/01 23:36:52 sermarti
// Sparta minor update: Now with debug really enabled.
//
// Revision 1.12 2002/08/01 23:29:17 sermarti
// Much faster Sparta parsing.
// Has debug features enabled by default. Currently toggled
// in ParseCharStream.java and recompiled.
//
// Revision 1.11 2002/07/25 21:10:15 sermarti
// Adding files that mysteriously weren't added from Sparta before.
//
// Revision 1.10 2002/05/23 21:28:25 eob
// Make misc optimizations because performance profiling showed that this
// class is heavily used. Avoid use char arrays instead of strings in
// symbol comparison. Remove deprecated methods.
//
// Revision 1.9 2002/05/09 16:50:06 eob
// Add history for better error reporting.
//
// Revision 1.8 2002/03/21 23:52:21 eob
// Deprecate functionality moved to Parser facade class.
//
// Revision 1.7 2002/02/23 02:06:51 eob
// Add constructor that takes a File.
//
// Revision 1.6 2002/02/06 23:32:40 eob
// Better error message.
//
// Revision 1.5 2002/02/01 21:56:23 eob
// Tweak error messages. Add no-log constructor.
//
// Revision 1.4 2002/01/09 00:53:02 eob
// Formatting changes only.
//
// Revision 1.3 2002/01/09 00:48:24 eob
// Replace well-formed errors with warnings.
//
// Revision 1.2 2002/01/08 19:56:51 eob
// Comment change only.
//
// Revision 1.1 2002/01/08 19:29:31 eob
// Factored out ParseSource functionality into ParseCharStream and
// ParseByteStream.
