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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;



/**
 * An XML byte stream that has been parsed into a DOM tree. Just like
 * ParseCharStream except handle Unicode encoding of byte stream. Use rules in
 * http://www.w3.org/TR/2000/REC-xml-20001006#sec-guessing to guess encoding --
 * if encoding declaration is different, restart parsing.
 * 
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 * @link http://sparta.sourceforge.net
 */

class ParseByteStream implements ParseSource {
    
    /**
     * Comment for <code>BIT_SHIFT_24</code>
     */
    private static final short BIT_SHIFT_24 = 24;
    
    /**
     * Comment for <code>BIT_SHIFT_16</code>
     */
    private static final short BIT_SHIFT_16 = 16;
    
    /**
     * Comment for <code>BIT_SHIFT_8</code>
     */
    private static final short BIT_SHIFT_8 = 8;
    
    /**
     * Comment for <code>BIT_SHIFT_FF</code>
     */
    private static final short BIT_SHIFT_FF = 0xFF;
    
    

    /**
     * @param bytes ..
     * @param integer ..
     * @return ..
     */
    private static boolean equals(final byte[] bytes, final int integer) {
        return bytes[0] == (byte) ((integer >>> BIT_SHIFT_24))
               && bytes[1] == (byte) ((integer >>> BIT_SHIFT_16) & BIT_SHIFT_FF)
               && bytes[2] == (byte) ((integer >>> BIT_SHIFT_8) & BIT_SHIFT_FF)
               && bytes[3] == (byte) ((integer) & BIT_SHIFT_FF);
    }

    /**
     * @param bytes ..
     * @param integer ..
     * @return ..
     */
    private static boolean equals(final byte[] bytes, final short integer) {
        return bytes[0] == (byte) ((integer >>> BIT_SHIFT_8))
                && bytes[1] == (byte) ((integer) & BIT_SHIFT_FF);
    }

    /**
     * @param encoding ..
     * @return ..
     */
    private static String fixEncoding(final String encoding) {
        return encoding.toLowerCase().equals("utf8") ? "UTF-8" : encoding;
    }

    /////////////////////////////////////////////////////////////////////

    /**
     * Convert byte stream to Unicode character stream according to
     * http://www.w3.org/TR/2000/REC-xml-20001006#sec-guessing .
     * 
     * @param systemId ..
     * @param start ..
     * @param n ..
     * @param log ..
     * @return ..
     * @throws IOException ..
     */
    private static String guessEncoding(final String systemId, 
                                        final byte[] start, 
                                        final int n,
                                        final ParseLog log) 
                                                throws IOException {
        //Test for UTF-16 byte-order mark
        String encoding;
        if (n != 4) {
            String msg = n <= 0 ? "no characters in input"
                    : "less than 4 characters in input: \""
                            + new String(start, 0, n) + "\"";
            log.error(msg, systemId, 1);
            encoding = "UTF-8";
        } else if (equals(start, 0x0000FEFF) || equals(start, 0xFFFE0000)
                || equals(start, 0x0000FFFE) || equals(start, 0xFEFF0000)
                || equals(start, 0x0000003C) || equals(start, 0x3C000000)
                || equals(start, 0x00003C00) || equals(start, 0x003C0000)) {
            encoding = "UCS-4";
        } else if (equals(start, 0x003C003F)) {
            encoding = "UTF-16BE"; //or ISO-10646-UCS-2
        } else if (equals(start, 0x3C003F00)) {
            encoding = "UTF-16LE"; //or ISO-10646-UCS-2
        } else if (equals(start, 0x3C3F786D)) {
            encoding = "UTF-8"; //or ISO 646, ASCII, ISO 8859, Shift-JIS, EUC
        } else if (equals(start, 0x4C6FA794)) {
            encoding = "EBCDIC";
        } else if (equals(start, (short) 0xFFFE)
                || equals(start, (short) 0xFEFF)) {
            encoding = "UTF-16";
        } else {
            encoding = "UTF-8";
        }
        if (!encoding.equals("UTF-8")) {
            log.note("From start " + hex(start[0]) + " " + hex(start[1]) + " "
                    + hex(start[2]) + " " + hex(start[3])
                    + " deduced encoding = " + encoding, systemId, 1);
        }
        return encoding;
    }

    /**
     * @param b ..
     * @return ..
     */
    private static String hex(final byte b) {
        String s = Integer.toHexString(b);
        switch (s.length()) {
        case 1:
            return "0" + s;
        case 2:
            return s;
        default:
            return s.substring(s.length() - 2);
        }
    }

    /**
     * Comment for <code>parseSource</code>
     */
    private ParseCharStream parseSource;

    /**
     * Parse XML document from byte stream, converting to Unicode characters as
     * specifed by the initial byte-order-mark.
     * 
     * @param systemId ..
     * @param istream
     *            is the source of bytes and must support mark so that we can
     *            peek ahead at its first two bytes
     * @param logIn ..
     * @param guessedEncodingIn ..
     * @param handler ..
     * @throws ParseException ..
     * @throws IOException ..
     */
    public ParseByteStream(final String systemId, 
            			   final InputStream istream, 
            			   final ParseLog logIn,
            			   final String guessedEncodingIn, 
            			   final ParseHandler handler)
                                   throws ParseException, IOException {
        ParseLog log = logIn;
        if (log == null) {
            log = DEFAULT_LOG;
        }

        //We need to be able to restart the stream if the declared encoding
        //is different than our guess, os buffer if necessary. We also need
        //to be able to peek ahead at the first 4 bytes
        if (!istream.markSupported()) {
            throw new Error(
                    "Precondition violation: the InputStream "
                    + "passed to ParseByteStream must support mark");
        }
        istream.mark(MAXLOOKAHEAD); //mark at begining

        byte[] start = new byte[4];
        int n = istream.read(start);

        String guessedEncoding = guessedEncodingIn;
        if (guessedEncoding == null) {
            guessedEncoding = guessEncoding(systemId, start, n, log);
        }
        
        try {

            //First try with guessed encoding
            istream.reset();
            InputStreamReader reader = new InputStreamReader(istream,
                    fixEncoding(guessedEncoding));
            try {

                parseSource = new ParseCharStream(systemId, reader, logIn,
                        guessedEncoding, handler);
                //}catch( CharConversionException e ){
            } catch (IOException e) {

                //This exception seems to be caused by reading euc-jp as utf-8
                String secondGuessEncoding = "euc-jp";
                logIn.note("Problem reading with assumed encoding of "
                        + guessedEncoding + " so restarting with "
                        + secondGuessEncoding, systemId, 1);
                istream.reset();
                try {
                    reader = new InputStreamReader(istream,
                            fixEncoding(secondGuessEncoding));
                } catch (UnsupportedEncodingException ee) {
                    throw new ParseException(logIn, systemId, 1, '\0',
                            secondGuessEncoding, "\"" + secondGuessEncoding
                                    + "\" is not a supported encoding");
                }

                parseSource = new ParseCharStream(
                        systemId, reader, logIn, null, handler);
            }
        } catch (EncodingMismatchException e) {
            //if that didn't work try declared encoding
            String declaredEncoding = e.getDeclaredEncoding();
            logIn.note("Encoding declaration of " + declaredEncoding
                    + " is different that assumed " + guessedEncoding
                    + " so restarting the parsing with the new encoding",
                    systemId, 1);
            istream.reset();
            InputStreamReader reader;
            try {
                reader = new InputStreamReader(istream,
                        fixEncoding(declaredEncoding));
            } catch (UnsupportedEncodingException ee) {
                throw new ParseException(logIn, systemId, 1, '\0',
                        declaredEncoding, "\"" + declaredEncoding
                                + "\" is not a supported encoding");
            }
            parseSource = new ParseCharStream(
                    systemId, reader, logIn, null, handler);
        }
    }

    /**
     * Last line number read by parser.
     * 
     * @return
     * @see de.xplib.nexd.xml.spartadom.ParseSource#getLineNumber()
     */
    public int getLineNumber() {
        return parseSource.getLineNumber();
    }

    /**
     * <Some description here>
     * 
     * @return
     * @see de.xplib.nexd.xml.spartadom.ParseSource#getSystemId()
     */
    public String getSystemId() {
        return parseSource.getSystemId();
    }

    /**
     * <Some description here>
     * 
     * @return
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return parseSource.toString();
    }

}

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 */
interface ParseHandler {
    /**
     * @param pcs ..
     */
    void setParseSource(ParseCharStream pcs);
    /**
     * 
     */
    void startDocument();
    /**
     * 
     */
    void endDocument();
    /**
     * @param ch ..
     * @param s ..
     * @param l ..
     */
    void characters(char[] ch, int s, int l);

    /**
     * @param eb .l.
     */
    void startElement(BaseElement eb);
    /**
     * @param eb ,.,
     */
    void endElement(BaseElement eb);
}

// $Log: ParseByteStream.java,v $
// Revision 1.5 2003/07/28 04:33:04 eobrain
// Fix bug that was removing dashes from unicode encoding names. We
// should do this only for UTF-8.
//
// Revision 1.4 2003/07/17 23:55:28 eobrain
// Make compatiblie with J2ME. For example do not use "new"
// java.util classes.
//
// Revision 1.3 2003/01/09 01:05:38 yuhongx
// added FixEncoding().
//
// Revision 1.2 2002/11/06 02:57:59 eobrain
// Organize imputs to removed unused imports. Remove some unused local
// variables.
//
// Revision 1.1.1.1 2002/08/19 05:04:00 eobrain
// import from HP Labs internal CVS
//
// Revision 1.14 2002/08/18 04:36:25 eob
// Make interface package-private so as not to clutter up the javadoc.
//
// Revision 1.13 2002/08/17 00:54:14 sermarti
//
// Revision 1.12 2002/08/05 20:04:32 sermarti
//
// Revision 1.11 2002/07/25 21:10:15 sermarti
// Adding files that mysteriously weren't added from Sparta before.
//
// Revision 1.10 2002/05/23 22:00:19 eob
// Add better error handling.
//
// Revision 1.9 2002/05/09 17:02:26 eob
// Fix NullPointerException in error reporting.
//
// Revision 1.8 2002/05/09 16:49:52 eob
// Add history for better error reporting.
//
// Revision 1.7 2002/03/21 23:50:49 eob
// Deprecate functionality moved to Parser facade class.
//
// Revision 1.6 2002/02/15 21:30:38 eob
// Comment changes only.
//
// Revision 1.5 2002/02/01 21:55:15 eob
// Comment change only.
//
// Revision 1.4 2002/01/09 00:45:58 eob
// Formatting change only.
//
// Revision 1.3 2002/01/09 00:44:57 eob
// Handle CharConversionException caused by reading euc-jp characters
// before encoding has been established. Restart parsing.
//
// Revision 1.2 2002/01/08 19:53:43 eob
// Comment change only.
//
// Revision 1.1 2002/01/08 19:31:33 eob
// Factored out ParseSource functionality into ParseCharStream and
// ParseByteStream.
