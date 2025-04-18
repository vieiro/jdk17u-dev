/*
 * Copyright (c) 1997, 2024, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

/*
(C) Copyright Taligent, Inc. 1996 - All Rights Reserved
(C) Copyright IBM Corp. 1996 - All Rights Reserved

  The original version of this source code and documentation is copyrighted and
owned by Taligent, Inc., a wholly-owned subsidiary of IBM. These materials are
provided under terms of a License Agreement between Taligent and Sun. This
technology is protected by multiple US and International patents. This notice and
attribution to Taligent may not be removed.
  Taligent is a registered trademark of Taligent, Inc.
*/

/*
 * @test
 * @library /java/text/testlib
 * @summary test Collation API
 * @modules jdk.localedata
 * @run junit APITest
 */

import java.util.Locale;
import java.text.Collator;
import java.text.RuleBasedCollator;
import java.text.CollationKey;
import java.text.CollationElementIterator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

public class APITest {

    final void doAssert(boolean condition, String message)
    {
        if (!condition) {
            fail("ERROR: " + message);
        }
    }

    @Test
    public final void TestProperty( )
    {
        Collator col = null;
        try {
            col = Collator.getInstance(Locale.ROOT);
            System.out.println("The property tests begin : ");
            System.out.println("Test ctors : ");
            doAssert(col.compare("ab", "abc") < 0, "ab < abc comparison failed");
            doAssert(col.compare("ab", "AB") < 0, "ab < AB comparison failed");
            doAssert(col.compare("black-bird", "blackbird") > 0, "black-bird > blackbird comparison failed");
            doAssert(col.compare("black bird", "black-bird") < 0, "black bird < black-bird comparison failed");
            doAssert(col.compare("Hello", "hello") > 0, "Hello > hello comparison failed");

            System.out.println("Test ctors ends.");
            System.out.println("testing Collator.getStrength() method ...");
            doAssert(col.getStrength() == Collator.TERTIARY, "collation object has the wrong strength");
            doAssert(col.getStrength() != Collator.PRIMARY, "collation object's strength is primary difference");

            System.out.println("testing Collator.setStrength() method ...");
            col.setStrength(Collator.SECONDARY);
            doAssert(col.getStrength() != Collator.TERTIARY, "collation object's strength is secondary difference");
            doAssert(col.getStrength() != Collator.PRIMARY, "collation object's strength is primary difference");
            doAssert(col.getStrength() == Collator.SECONDARY, "collation object has the wrong strength");

            System.out.println("testing Collator.setDecomposition() method ...");
            col.setDecomposition(Collator.NO_DECOMPOSITION);
            doAssert(col.getDecomposition() != Collator.FULL_DECOMPOSITION, "collation object's strength is secondary difference");
            doAssert(col.getDecomposition() != Collator.CANONICAL_DECOMPOSITION, "collation object's strength is primary difference");
            doAssert(col.getDecomposition() == Collator.NO_DECOMPOSITION, "collation object has the wrong strength");
        } catch (Exception foo) {
            fail("Error : " + foo.getMessage()
            + "\n Default Collator creation failed.");
        }
        System.out.println("Default collation property test ended.");
        System.out.println("Collator.getRules() testing ...");
        doAssert(((RuleBasedCollator)col).getRules().length() != 0, "getRules() result incorrect" );
        System.out.println("getRules tests end.");
        try {
            col = Collator.getInstance(Locale.FRENCH);
            col.setStrength(Collator.PRIMARY);
            System.out.println("testing Collator.getStrength() method again ...");
            doAssert(col.getStrength() != Collator.TERTIARY, "collation object has the wrong strength");
            doAssert(col.getStrength() == Collator.PRIMARY, "collation object's strength is not primary difference");

            System.out.println("testing French Collator.setStrength() method ...");
            col.setStrength(Collator.TERTIARY);
            doAssert(col.getStrength() == Collator.TERTIARY, "collation object's strength is not tertiary difference");
            doAssert(col.getStrength() != Collator.PRIMARY, "collation object's strength is primary difference");
            doAssert(col.getStrength() != Collator.SECONDARY, "collation object's strength is secondary difference");

        } catch (Exception bar) {
            fail("Error :  " + bar.getMessage()
            + "\n Creating French collation failed.");
        }

        System.out.println("Create junk collation: ");
        Locale abcd = new Locale("ab", "CD", "");
        Collator junk = null;
        try {
            junk = Collator.getInstance(abcd);
        } catch (Exception err) {
            fail("Error : " + err.getMessage()
            + "\n Junk collation creation failed, should at least return the collator for the base bundle.");
        }
        try {
            col = Collator.getInstance(Locale.ROOT);
            doAssert(col.equals(junk), "The base bundle's collation should be returned.");
        } catch (Exception exc) {
            fail("Error : " + exc.getMessage()
            + "\n Default collation comparison, caching not working.");
        }

        System.out.println("Collator property test ended.");
    }

    @Test
    public final void TestHashCode( )
    {
        System.out.println("hashCode tests begin.");
        Collator col1 = null;
        try {
            col1 = Collator.getInstance(Locale.ROOT);
        } catch (Exception foo) {
            fail("Error : " + foo.getMessage()
            + "\n Default collation creation failed.");
        }
        Collator col2 = null;
        Locale dk = new Locale("da", "DK", "");
        try {
            col2 = Collator.getInstance(dk);
        } catch (Exception bar) {
            fail("Error : " + bar.getMessage()
            + "\n Danish collation creation failed.");
            return;
        }
        Collator col3 = null;
        try {
            col3 = Collator.getInstance(Locale.ROOT);
        } catch (Exception err) {
            fail("Error : " + err.getMessage()
            + "\n 2nd default collation creation failed.");
        }
        System.out.println("Collator.hashCode() testing ...");

        if (col1 != null) {
            doAssert(col1.hashCode() != col2.hashCode(), "Hash test1 result incorrect");
            if (col3 != null) {
                doAssert(col1.hashCode() == col3.hashCode(), "Hash result not equal");
            }
        }

        System.out.println("hashCode tests end.");
    }

    //----------------------------------------------------------------------------
    // ctor -- Tests the constructor methods
    //
    @Test
    public final void TestCollationKey( )
    {
        System.out.println("testing CollationKey begins...");
        Collator col = null;
        try {
            col = Collator.getInstance(Locale.ROOT);
        } catch (Exception foo) {
            fail("Error : " + foo.getMessage()
            + "\n Default collation creation failed.");
        }
        if (col == null) {
            return;
        }

        String test1 = "Abcda", test2 = "abcda";
        System.out.println("Use tertiary comparison level testing ....");
        CollationKey sortk1 = col.getCollationKey(test1);
        CollationKey sortk2 = col.getCollationKey(test2);
        doAssert(sortk1.compareTo(sortk2) > 0,
                    "Result should be \"Abcda\" >>> \"abcda\"");
        CollationKey sortk3 = sortk2;
        CollationKey sortkNew = sortk1;
        doAssert(sortk1 != sortk2, "The sort keys should be different");
        doAssert(sortk1.hashCode() != sortk2.hashCode(), "sort key hashCode() failed");
        doAssert(sortk2.compareTo(sortk3) == 0, "The sort keys should be the same");
        doAssert(sortk1 == sortkNew, "The sort keys assignment failed");
        doAssert(sortk1.hashCode() == sortkNew.hashCode(), "sort key hashCode() failed");
        doAssert(sortkNew != sortk3, "The sort keys should be different");
        doAssert(sortk1.compareTo(sortk3) > 0, "Result should be \"Abcda\" >>> \"abcda\"");
        doAssert(sortk2.compareTo(sortk3) == 0, "Result should be \"abcda\" == \"abcda\"");
        long    cnt1, cnt2;
        byte byteArray1[] = sortk1.toByteArray();
        byte byteArray2[] = sortk2.toByteArray();
        doAssert(byteArray1 != null && byteArray2 != null, "CollationKey.toByteArray failed.");
        System.out.println("testing sortkey ends...");
    }
    //----------------------------------------------------------------------------
    // ctor -- Tests the constructor methods
    //
    @Test
    public final void TestElemIter( )
    {
        System.out.println("testing sortkey begins...");
        Collator col = null;
        try {
            col = Collator.getInstance();
        } catch (Exception foo) {
            fail("Error : " + foo.getMessage()
            + "\n Default collation creation failed.");
        }
        RuleBasedCollator rbCol;
        if (col instanceof RuleBasedCollator) {
            rbCol = (RuleBasedCollator) col;
        } else {
            return;
        }
        String testString1 = "XFILE What subset of all possible test cases has the highest probability of detecting the most errors?";
        String testString2 = "Xf ile What subset of all possible test cases has the lowest probability of detecting the least errors?";
        System.out.println("Constructors and comparison testing....");
        CollationElementIterator iterator1 = rbCol.getCollationElementIterator(testString1);
        CollationElementIterator iterator2 = rbCol.getCollationElementIterator(testString1);
        CollationElementIterator iterator3 = rbCol.getCollationElementIterator(testString2);
        int order1, order2, order3;
        order1 = iterator1.next();
        order2 = iterator2.next();
        doAssert(order1 == order2, "The order result should be the same");

        order3 = iterator3.next();
        doAssert(CollationElementIterator.primaryOrder(order1)
                     == CollationElementIterator.primaryOrder(order3),
                 "The primary orders should be the same");
        doAssert(CollationElementIterator.secondaryOrder(order1)
                     == CollationElementIterator.secondaryOrder(order3),
                 "The secondary orders should be the same");
        doAssert(CollationElementIterator.tertiaryOrder(order1)
                     == CollationElementIterator.tertiaryOrder(order3),
                 "The tertiary orders should be the same");

        order1 = iterator1.next();
        order3 = iterator3.next();
        doAssert(CollationElementIterator.primaryOrder(order1)
                     == CollationElementIterator.primaryOrder(order3),
                 "The primary orders should be identical");
        doAssert(CollationElementIterator.tertiaryOrder(order1)
                     != CollationElementIterator.tertiaryOrder(order3),
                 "The tertiary orders should be different");

        order1 = iterator1.next();
        order3 = iterator3.next();
        doAssert(CollationElementIterator.secondaryOrder(order1)
                     != CollationElementIterator.secondaryOrder(order3),
                 "The secondary orders should be different");
        doAssert(order1 != CollationElementIterator.NULLORDER,
                 "Unexpected end of iterator reached");

        iterator1.reset();
        iterator2.reset();
        iterator3.reset();
        order1 = iterator1.next();
        order2 = iterator2.next();
        doAssert(order1 == order2, "The order result should be the same");

        order3 = iterator3.next();
        doAssert(CollationElementIterator.primaryOrder(order1)
                     == CollationElementIterator.primaryOrder(order3),
                 "The orders should be the same");
        doAssert(CollationElementIterator.secondaryOrder(order1)
                     == CollationElementIterator.secondaryOrder(order3),
                 "The orders should be the same");
        doAssert(CollationElementIterator.tertiaryOrder(order1)
                     == CollationElementIterator.tertiaryOrder(order3),
                 "The orders should be the same");

        order1 = iterator1.next();
        order2 = iterator2.next();
        order3 = iterator3.next();
        doAssert(CollationElementIterator.primaryOrder(order1)
                     == CollationElementIterator.primaryOrder(order3),
                 "The primary orders should be identical");
        doAssert(CollationElementIterator.tertiaryOrder(order1)
                     != CollationElementIterator.tertiaryOrder(order3),
                 "The tertiary orders should be different");

        order1 = iterator1.next();
        order3 = iterator3.next();
        doAssert(CollationElementIterator.secondaryOrder(order1)
                     != CollationElementIterator.secondaryOrder(order3),
                 "The secondary orders should be different");
        doAssert(order1 != CollationElementIterator.NULLORDER, "Unexpected end of iterator reached");
        System.out.println("testing CollationElementIterator ends...");
    }

    @Test
    public final void TestGetAll()
    {
        Locale[] list = Collator.getAvailableLocales();
        for (int i = 0; i < list.length; ++i) {
            System.out.println("Locale name: ");
            System.out.println(list[i].toString());
            System.out.println(" , the display name is : ");
            System.out.println(list[i].getDisplayName());
        }
    }
}
