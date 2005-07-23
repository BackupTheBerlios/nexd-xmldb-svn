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
 * $Log: IntStackTestCase.java,v $
 * Revision 1.1  2005/05/11 17:31:41  nexd
 * Refactoring and extended test cases
 *
 */
package tests;

import de.xplib.nexd.util.IntStack;
import junit.framework.TestCase;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class IntStackTestCase extends TestCase {

    /*
     * Class under test for void IntStack()
     */
    public final void testIntStack() {
        IntStack is = new IntStack();
        for (int i = 0; i < 10000; i++) {
            is.push(i);
        }
        
        for (int i = 0; i < 10000; i++) {
            is.pop();
        }
    }

    /*
     * Class under test for void IntStack(int)
     */
    public final void testIntStackint() {
        IntStack is = new IntStack(100);
        for (int i = 0; i < 10000; i++) {
            is.push(i);
        }
        
        for (int i = 0; i < 10000; i++) {
            is.pop();
        }
    }

    /*
     * Class under test for void IntStack(int, float)
     */
    public final void testIntStackintfloat() {
        IntStack is = new IntStack(100, 4.0f);
        for (int i = 0; i < 10000; i++) {
            is.push(i);
        }
        
        for (int i = 0; i < 10000; i++) {
            is.pop();
        }
    }

    public final void testPop() {
        IntStack is = new IntStack();
        try {
            is.pop();
            assertTrue(false);
        } catch (ArrayIndexOutOfBoundsException e) {
            assertTrue(true);
        }
    }

    public final void testPeek() {
        IntStack is = new IntStack();
        try {
            is.peek();
            assertTrue(false);
        } catch (ArrayIndexOutOfBoundsException e) {
            assertTrue(true);
        }
    }

    public final void testFirst() {
        IntStack is = new IntStack(100, 4.0f);
        for (int i = 0; i < 10000; i++) {
            is.push(i);
        }
        assertEquals(0, is.first());
    }

    public final void testSize() {
        IntStack is = new IntStack(100, 4.0f);
        for (int i = 0; i < 10000; i++) {
            is.push(i);
        }
        assertEquals(10000, is.size());
    }

    public final void testReset() {
        IntStack is = new IntStack(100, 4.0f);
        for (int i = 0; i < 10000; i++) {
            is.push(i);
        }
        is.reset();
        assertEquals(0, is.size());
    }

}
