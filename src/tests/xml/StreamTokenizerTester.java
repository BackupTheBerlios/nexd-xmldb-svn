/*
 * Created on 02-Jul-2003 by eob
 */
package tests.xml;

import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.io.StringReader;

import junit.framework.TestCase;
import de.xplib.nexd.xml.xpath.SimpleStreamTokenizer;

/**
 * @version $Revision: 1.1 $
 * @author eob
 */
public class StreamTokenizerTester extends TestCase {

    /**
     * @param arg0 ..
     */
    public StreamTokenizerTester(final String arg0) {
        super(arg0);
    }

    /**
     * @throws IOException ..
     */
    public final void testWords1() throws IOException {
        Reader r = new StringReader("one two");
        SimpleStreamTokenizer t = new SimpleStreamTokenizer(r);
        assertEquals(SimpleStreamTokenizer.TT_WORD, t.nextToken());
        assertEquals("one", t.sval);
        assertEquals(SimpleStreamTokenizer.TT_WORD, t.nextToken());
        assertEquals("two", t.sval);
        assertEquals(SimpleStreamTokenizer.TT_EOF, t.nextToken());

    }

    /**
     * @throws IOException ,,
     */
    public final void testWords2() throws IOException {
        Reader r = new StringReader("one two");
        StreamTokenizer t = new StreamTokenizer(r);
        assertEquals(StreamTokenizer.TT_WORD, t.nextToken());
        assertEquals("one", t.sval);
        assertEquals(StreamTokenizer.TT_WORD, t.nextToken());
        assertEquals("two", t.sval);
        assertEquals(StreamTokenizer.TT_EOF, t.nextToken());

    }

    /**
     * @throws IOException ..
     */
    public final void testNums1() throws IOException {
        Reader r = new StringReader("111 222");
        SimpleStreamTokenizer t = new SimpleStreamTokenizer(r);
        assertEquals(SimpleStreamTokenizer.TT_NUMBER, t.nextToken());
        assertEquals(111, t.nval);
        assertEquals(SimpleStreamTokenizer.TT_NUMBER, t.nextToken());
        assertEquals(222, t.nval);
        assertEquals(SimpleStreamTokenizer.TT_EOF, t.nextToken());

    }

    /**
     * @throws IOException ..
     */
    public final void testNums2() throws IOException {
        Reader r = new StringReader("111 222");
        StreamTokenizer t = new StreamTokenizer(r);
        assertEquals(StreamTokenizer.TT_NUMBER, t.nextToken());
        assertEquals(111, (int) t.nval);
        assertEquals(StreamTokenizer.TT_NUMBER, t.nextToken());
        assertEquals(222, (int) t.nval);
        assertEquals(SimpleStreamTokenizer.TT_EOF, t.nextToken());

    }

    /**
     * @throws IOException ..
     */
    public final void testChars1() throws IOException {
        Reader r = new StringReader("%=");
        SimpleStreamTokenizer t = new SimpleStreamTokenizer(r);
        assertEquals('%', t.nextToken());
        assertEquals('=', t.nextToken());
        assertEquals(SimpleStreamTokenizer.TT_EOF, t.nextToken());
    }

    /**
     * @throws IOException ..
     */
    public final void testChars2() throws IOException { 
        Reader r = new StringReader("%=");
        StreamTokenizer t = new StreamTokenizer(r);
        assertEquals('%', t.nextToken());
        assertEquals('=', t.nextToken());
        assertEquals(StreamTokenizer.TT_EOF, t.nextToken());
    }

    /**
     * @throws IOException ..
     */
    public final void testSpacedChars1() throws IOException {
        Reader r = new StringReader("% =");
        SimpleStreamTokenizer t = new SimpleStreamTokenizer(r);
        assertEquals('%', t.nextToken());
        assertEquals('=', t.nextToken());
        assertEquals(SimpleStreamTokenizer.TT_EOF, t.nextToken());
    }

    /**
     * @throws IOException ..
     */
    public final void testSpacedChars2() throws IOException {
        Reader r = new StringReader("% =");
        StreamTokenizer t = new StreamTokenizer(r);
        assertEquals('%', t.nextToken());
        assertEquals('=', t.nextToken());
        assertEquals(StreamTokenizer.TT_EOF, t.nextToken());
    }

    /**
     * @throws IOException ..
     */
    public final void testXPath1() throws IOException {
        Reader r = new StringReader(
                "/project/target[@name='junit']/junit/test/@name");
        SimpleStreamTokenizer t = new SimpleStreamTokenizer(r);
        t.ordinaryChar('/'); // '/' is not a comment
        t.ordinaryChar('.'); // '.' is not a part of a number
        t.wordChars(':', ':'); //Allow namespaces
        t.wordChars('_', '_'); //Allow namespaces

        assertEquals('/', t.nextToken());
        assertEquals(SimpleStreamTokenizer.TT_WORD, t.nextToken());
        assertEquals("project", t.sval);
        assertEquals('/', t.nextToken());
        assertEquals(SimpleStreamTokenizer.TT_WORD, t.nextToken());
        assertEquals("target", t.sval);
        assertEquals('[', t.nextToken());
        assertEquals('@', t.nextToken());
        assertEquals(SimpleStreamTokenizer.TT_WORD, t.nextToken());
        assertEquals("name", t.sval);
        assertEquals('=', t.nextToken());
        assertEquals('\'', t.nextToken());
        assertEquals("junit", t.sval);
        assertEquals(']', t.nextToken());
        assertEquals('/', t.nextToken());
        assertEquals(SimpleStreamTokenizer.TT_WORD, t.nextToken());
        assertEquals("junit", t.sval);
        assertEquals('/', t.nextToken());
        assertEquals(SimpleStreamTokenizer.TT_WORD, t.nextToken());
        assertEquals("test", t.sval);
        assertEquals('/', t.nextToken());
        assertEquals('@', t.nextToken());
        assertEquals(SimpleStreamTokenizer.TT_WORD, t.nextToken());
        assertEquals("name", t.sval);
        assertEquals(SimpleStreamTokenizer.TT_EOF, t.nextToken());
    }

    /**
     * @throws IOException ..
     */
    public final void testXPath2() throws IOException {
        Reader r = new StringReader(
                "/project/target[@name='junit']/junit/test/@name");
        StreamTokenizer t = new StreamTokenizer(r);
        t.ordinaryChar('/'); // '/' is not a comment
        t.ordinaryChar('.'); // '.' is not a part of a number
        t.wordChars(':', ':'); //Allow namespaces
        t.wordChars('_', '_'); //Allow namespaces

        assertEquals('/', t.nextToken());
        assertEquals(StreamTokenizer.TT_WORD, t.nextToken());
        assertEquals("project", t.sval);
        assertEquals('/', t.nextToken());
        assertEquals(StreamTokenizer.TT_WORD, t.nextToken());
        assertEquals("target", t.sval);
        assertEquals('[', t.nextToken());
        assertEquals('@', t.nextToken());
        assertEquals(StreamTokenizer.TT_WORD, t.nextToken());
        assertEquals("name", t.sval);
        assertEquals('=', t.nextToken());
        assertEquals('\'', t.nextToken());
        assertEquals("junit", t.sval);
        assertEquals(']', t.nextToken());
        assertEquals('/', t.nextToken());
        assertEquals(StreamTokenizer.TT_WORD, t.nextToken());
        assertEquals("junit", t.sval);
        assertEquals('/', t.nextToken());
        assertEquals(StreamTokenizer.TT_WORD, t.nextToken());
        assertEquals("test", t.sval);
        assertEquals('/', t.nextToken());
        assertEquals('@', t.nextToken());
        assertEquals(StreamTokenizer.TT_WORD, t.nextToken());
        assertEquals("name", t.sval);
        assertEquals(StreamTokenizer.TT_EOF, t.nextToken());
    }

}

// $Log: StreamTokenizerTester.java,v $
// Revision 1.1 2003/07/18 00:02:37 eobrain
// Make compatiblie with J2ME. For example do not use "new"
// java.util classes.
//