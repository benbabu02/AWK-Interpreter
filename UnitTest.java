package csi404.assignment7;

import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;

import static org.junit.Assert.*;

public class UnitTest {

    // ----- TESTS FOR BIT CLASS -----
    @Test
    public void test_bit_set_parameter() {
        Bit currentBit1 = new Bit();
        assertEquals(false, currentBit1.getValue());
        currentBit1.set();
        assertEquals(true, currentBit1.getValue());

        Bit currentBit2 = new Bit(true);
        assertEquals(true, currentBit2.getValue());
        currentBit2.set();
        assertEquals(true, currentBit2.getValue());
    }

    @Test
    public void test_bit_toggle() {
        Bit currentBit1 = new Bit();
        assertEquals(false, currentBit1.getValue());
        currentBit1.toggle();
        assertEquals(true, currentBit1.getValue());
        currentBit1.toggle();
        assertEquals(false, currentBit1.getValue());

        Bit currentBit2 = new Bit(true);
        assertEquals(true, currentBit2.getValue());
        currentBit2.toggle();
        assertEquals(false, currentBit2.getValue());
        currentBit2.toggle();
        assertEquals(true, currentBit2.getValue());
    }

    @Test
    public void test_bit_set_no_parameter() {
        Bit currentBit1 = new Bit();
        assertEquals(false, currentBit1.getValue());
        currentBit1.set();
        assertEquals(true, currentBit1.getValue());

        Bit currentBit2 = new Bit(true);
        assertEquals(true, currentBit2.getValue());
        currentBit2.set();
        assertEquals(true, currentBit2.getValue());
    }

    @Test
    public void test_bit_clear() {
        Bit currentBit1 = new Bit();
        assertEquals(false, currentBit1.getValue());
        currentBit1.clear();
        assertEquals(false, currentBit1.getValue());

        Bit currentBit2 = new Bit(true);
        assertEquals(true, currentBit2.getValue());
        currentBit2.clear();
        assertEquals(false, currentBit2.getValue());
    }

    @Test
    public void test_bit_getValue() {
        Bit currentBit1 = new Bit();
        assertEquals(false, currentBit1.getValue());

        Bit currentBit2 = new Bit(true);
        assertEquals(true, currentBit2.getValue());
    }

    @Test
    public void test_bit_and() {
        // 0 0 0
        // 0 1 0
        // 1 0 0
        // 1 1 1

        Bit otherFalse = new Bit();
        Bit currentFalse = new Bit();
        Bit otherTrue = new Bit(true);
        Bit currentTrue = new Bit(true);

        assertEquals(false, currentFalse.and(otherFalse).getValue());
        assertEquals(false, currentFalse.and(otherTrue).getValue());
        assertEquals(false, currentTrue.and(otherFalse).getValue());
        assertEquals(true, currentTrue.and(otherTrue).getValue());
    }

    @Test
    public void test_bit_or() {
        // 0 0 0
        // 0 1 1
        // 1 0 1
        // 1 1 1

        Bit otherFalse = new Bit();
        Bit currentFalse = new Bit();
        Bit otherTrue = new Bit(true);
        Bit currentTrue = new Bit(true);

        assertEquals(false, currentFalse.or(otherFalse).getValue());
        assertEquals(true, currentFalse.or(otherTrue).getValue());
        assertEquals(true, currentTrue.or(otherFalse).getValue());
        assertEquals(true, currentTrue.or(otherTrue).getValue());
    }

    @Test
    public void test_bit_xor() {
        // 0 0 0
        // 0 1 1
        // 1 0 1
        // 1 1 0

        Bit otherFalse = new Bit();
        Bit currentFalse = new Bit();
        Bit otherTrue = new Bit(true);
        Bit currentTrue = new Bit(true);

        assertEquals(false, currentFalse.xor(otherFalse).getValue());
        assertEquals(true, currentFalse.xor(otherTrue).getValue());
        assertEquals(true, currentTrue.xor(otherFalse).getValue());
        assertEquals(false, currentTrue.xor(otherTrue).getValue());
    }

    @Test
    public void test_bit_not() {
        // 0 1
        // 1 0

        Bit currentFalse = new Bit();
        Bit currentTrue = new Bit(true);

        assertEquals(true, currentFalse.not().getValue());
        assertEquals(true, currentFalse.not().getValue());
        assertEquals(false, currentTrue.not().getValue());
        assertEquals(false, currentTrue.not().getValue());
    }

    // ----- TESTS FOR WORD CLASS -----
    @Test
    public void test_word_getBit() {
        Word word = new Word();
        word.set(2);
        for (int i = 0; i < 32; i++) {
            if (i == 30) {
                assertEquals(true, word.getBit(i).getValue());
            } else {
                assertEquals(false, word.getBit(i).getValue());
            }
        }

        word = new Word();
        word.set(214748364);
        for (int i = 0; i < 32; i++) {
            if (i == 4 || i == 5 || i == 8 || i == 9 || i == 12 || i == 13 || i == 16 || i == 17 || i == 20 || i == 21 || i == 24 || i == 25 || i == 28 || i == 29) {
                assertEquals(true, word.getBit(i).getValue());
            } else {
                assertEquals(false, word.getBit(i).getValue());
            }
        }
    }

    @Test
    public void test_word_and() {
        Word word1 = new Word();
        word1.set(2);
        Word word2 = new Word();
        word2.set(5);
        Word resultWord = word1.and(word2);
        for (int i = 0; i < 32; i++) {
            assertEquals(false, resultWord.getBit(i).getValue());
        }

        word1 = new Word();
        word1.set(5);
        word2 = new Word();
        word2.set(5);
        resultWord = word1.and(word2);
        for (int i = 0; i < 32; i++) {
            if (i == 29 || i == 31) {
                assertEquals(true, resultWord.getBit(i).getValue());
            } else {
                assertEquals(false, resultWord.getBit(i).getValue());
            }
        }

        word1 = new Word();
        word1.set(25);
        word2 = new Word();
        word2.set(84);
        resultWord = word1.and(word2);
        for (int i = 0; i < 32; i++) {
            if (i == 27) {
                assertEquals(true, resultWord.getBit(i).getValue());
            } else {
                assertEquals(false, resultWord.getBit(i).getValue());
            }
        }
    }

    @Test
    public void test_word_or() {
        Word word1 = new Word();
        word1.set(2);
        Word word2 = new Word();
        word2.set(5);
        Word resultWord = word1.or(word2);
        for (int i = 0; i < 32; i++) {
            if (i == 29 || i == 30 || i == 31) {
                assertEquals(true, resultWord.getBit(i).getValue());
            } else {
                assertEquals(false, resultWord.getBit(i).getValue());
            }
        }

        word1 = new Word();
        word1.set(5);
        word2 = new Word();
        word2.set(5);
        resultWord = word1.or(word2);
        for (int i = 0; i < 32; i++) {
            if (i == 29 || i == 31) {
                assertEquals(true, resultWord.getBit(i).getValue());
            } else {
                assertEquals(false, resultWord.getBit(i).getValue());
            }
        }

        word1 = new Word();
        word1.set(25);
        word2 = new Word();
        word2.set(84);
        resultWord = word1.or(word2);
        for (int i = 0; i < 32; i++) {
            if (i == 25 || i == 27 || i == 28 || i == 29 ||  i == 31) {
                assertEquals(true, resultWord.getBit(i).getValue());
            } else {
                assertEquals(false, resultWord.getBit(i).getValue());
            }
        }
    }

    @Test
    public void test_word_xor() {
        Word word1 = new Word();
        word1.set(2);
        Word word2 = new Word();
        word2.set(5);
        Word resultWord = word1.xor(word2);
        for (int i = 0; i < 32; i++) {
            if (i == 29 || i == 30 || i == 31) {
                assertEquals(true, resultWord.getBit(i).getValue());
            } else {
                assertEquals(false, resultWord.getBit(i).getValue());
            }
        }

        word1 = new Word();
        word1.set(5);
        word2 = new Word();
        word2.set(5);
        resultWord = word1.xor(word2);
        for (int i = 0; i < 32; i++) {
            assertEquals(false, resultWord.getBit(i).getValue());
        }

        word1 = new Word();
        word1.set(25);
        word2 = new Word();
        word2.set(84);
        resultWord = word1.xor(word2);
        for (int i = 0; i < 32; i++) {
            if (i == 25 || i == 28 || i == 29 || i == 31) {
                assertEquals(true, resultWord.getBit(i).getValue());
            } else {
                assertEquals(false, resultWord.getBit(i).getValue());
            }
        }
    }

    @Test
    public void test_word_not() {
        Word word1 = new Word();
        word1.set(3);
        for (int i = 0; i < 32; i++) {
            if (i == 30 || i == 31) {
                assertEquals(true, word1.getBit(i).getValue());
            } else {
                assertEquals(false, word1.getBit(i).getValue());
            }
        }
        Word notWord1 = word1.not();
        for (int i = 0; i < 32; i++) {
            if (i == 30 || i == 31) {
                assertEquals(false, notWord1.getBit(i).getValue());
            } else {
                assertEquals(true, notWord1.getBit(i).getValue());
            }
        }

        Word word2 = new Word();
        word2.set(48);
        for (int i = 0; i < 32; i++) {
            if (i == 26 || i == 27) {
                assertEquals(true, word2.getBit(i).getValue());
            } else {
                assertEquals(false, word2.getBit(i).getValue());
            }
        }
        Word notWord2 = word2.not();
        for (int i = 0; i < 32; i++) {
            if (i == 26 || i == 27) {
                assertEquals(false, notWord2.getBit(i).getValue());
            } else {
                assertEquals(true, notWord2.getBit(i).getValue());
            }
        }
    }

    @Test
    public void test_word_rightShift() {
        Word word1 = new Word();
        word1.set(3);
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, t", word1.toString());
        Word rightWord1 = word1.rightShift(1);
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t", rightWord1.toString());
        rightWord1 = word1.rightShift(3);
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", rightWord1.toString());

        Word word2 = new Word();
        word2.set(8);
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, f, f", word2.toString());
        Word rightWord2 = word2.rightShift(0);
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, f, f", rightWord2.toString());
        rightWord2 = word2.rightShift(1);
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, f", rightWord2.toString());
        rightWord2 = word2.rightShift(2);
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f", rightWord2.toString());
        rightWord2 = word2.rightShift(3);
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t", rightWord2.toString());
    }

    @Test
    public void test_word_leftShift() {
        Word word1 = new Word();
        word1.set(3);
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, t", word1.toString());
        Word leftWord1 = word1.leftShift(1);
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, t, f", leftWord1.toString());
        leftWord1 = word1.leftShift(3);
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, t, f, f, f", leftWord1.toString());

        Word word2 = new Word();
        word2.set(8);
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, f, f", word2.toString());
        Word leftWord2 = word2.leftShift(0);
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, f, f", leftWord2.toString());
        leftWord2 = word2.leftShift(1);
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, f, f, f", leftWord2.toString());
        leftWord2 = word2.leftShift(2);
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, f, f, f, f", leftWord2.toString());
        leftWord2 = word2.leftShift(3);
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, f, f, f, f, f", leftWord2.toString());
    }

    @Test
    public void test_word_toString() {
        Word word = new Word();
        word.set(8);
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, f, f", word.toString());

        Word word2 = new Word();
        word2.set(488234);
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, t, t, t, f, t, t, t, f, f, t, t, f, f, t, f, t, f, t, f", word2.toString());

        Word word3 = new Word();
        word3.set(-488234);
        assertEquals("t, t, t, t, t, t, t, t, t, t, t, t, t, f, f, f, t, f, f, f, t, t, f, f, t, t, f, t, f, t, t, f", word3.toString());
    }

    @Test
    public void test_word_getUnsigned() {
        Word newWord = new Word();
        newWord.set(12);
        assertEquals(12, newWord.getUnsigned());

        newWord.set(184);
        assertEquals(184, newWord.getUnsigned());
    }

    @Test
    public void test_word_getSigned() {
        Word newWord = new Word();
        newWord.set(12);
        assertEquals(12, newWord.getSigned());

        newWord.set(-12);
        assertEquals(-12, newWord.getSigned());

        newWord.set(123984);
        assertEquals(123984, newWord.getSigned());

        newWord.set(-123984);
        assertEquals(-123984, newWord.getSigned());

    }

    @Test
    public void test_word_copy() {
        Word newWord1 = new Word();
        Word newWord2 = new Word();
        assertEquals(newWord1.toString(), newWord2.toString());

        newWord1.set(2);
        assertNotEquals(newWord1.toString(), newWord2.toString());
        newWord2.copy(newWord1);
        assertEquals(newWord1.toString(), newWord2.toString());

        newWord1.set(1203984);
        newWord2.copy(newWord1);
        assertEquals(newWord1.toString(), newWord2.toString());

        newWord1.set(0);
        newWord2.copy(newWord1);
        assertEquals(newWord1.toString(), newWord2.toString());

        newWord1.set(-0);
        newWord2.copy(newWord1);
        assertEquals(newWord1.toString(), newWord2.toString());
    }

    @Test
    public void test_word_set() {
        Word newWord1 = new Word();
        newWord1.set(7);
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, t, t", newWord1.toString());
        newWord1.set(-7);
        assertEquals("t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, f, f, t", newWord1.toString());

        Word newWord2 = new Word();
        newWord2.set(8);
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, f, f", newWord2.toString());
        newWord2.set(-8);
        assertEquals("t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, f, f, f", newWord2.toString());

        Word newWord3 = new Word();
        newWord3.set(184);
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, t, t, t, f, f, f", newWord3.toString());
        newWord3.set(-184);
        assertEquals("t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, f, t, f, f, t, f, f, f", newWord3.toString());

        Word newWord4 = new Word();
        newWord4.set(-550);
        assertEquals("t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, f, t, t, t, f, t, t, f, t, f", newWord4.toString());
    }

    // ----- TESTS FOR ALU CLASS -----

    @Test
    public void test_alu_multiply() {
        // 3 * 5 = 15
        Word w1 = new Word();
        w1.set(3);
        Word w2 = new Word();
        w2.set(5);
        ALU alu = new ALU(w1, w2);
        alu.doOperation(new Bit[]{new Bit(false), new Bit(true), new Bit(true), new Bit(true)});
        assertEquals(15, alu.getResult().getSigned());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, t, t, t", alu.getResult().toString());

        // 12 * 9 = 108
        w1 = new Word();
        w1.set(12);
        w2 = new Word();
        w2.set(9);
        alu = new ALU(w1, w2);
        alu.doOperation(new Bit[]{new Bit(false), new Bit(true), new Bit(true), new Bit(true)});
        assertEquals(108, alu.getResult().getSigned());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, t, f, t, t, f, f", alu.getResult().toString());

        // 7 * 2 = 14
        w1 = new Word();
        w1.set(7);
        w2 = new Word();
        w2.set(2);
        alu = new ALU(w1, w2);
        alu.doOperation(new Bit[]{new Bit(false), new Bit(true), new Bit(true), new Bit(true)});
        assertEquals(14, alu.getResult().getSigned());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, t, t, f", alu.getResult().toString());

        // 12 * 0 = 0
        w1 = new Word();
        w1.set(12);
        w2 = new Word();
        w2.set(0);
        alu = new ALU(w1, w2);
        alu.doOperation(new Bit[]{new Bit(false), new Bit(true), new Bit(true), new Bit(true)});
        assertEquals(0, alu.getResult().getSigned());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", alu.getResult().toString());
    }

    @Test
    public void test_alu_and() {
        // 3 5
        Word w1 = new Word();
        w1.set(3);
        Word w2 = new Word();
        w2.set(5);
        ALU alu = new ALU(w1, w2);
        alu.doOperation(new Bit[]{new Bit(true), new Bit(false), new Bit(false), new Bit(false)});
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t", alu.getResult().toString());

        // 12 9
        w1 = new Word();
        w1.set(12);
        w2 = new Word();
        w2.set(9);
        alu = new ALU(w1, w2);
        alu.doOperation(new Bit[]{new Bit(true), new Bit(false), new Bit(false), new Bit(false)});
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, f, f", alu.getResult().toString());

        // 7 2
        w1 = new Word();
        w1.set(7);
        w2 = new Word();
        w2.set(2);
        alu = new ALU(w1, w2);
        alu.doOperation(new Bit[]{new Bit(true), new Bit(false), new Bit(false), new Bit(false)});
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f", alu.getResult().toString());

        // 18 6
        w1 = new Word();
        w1.set(18);
        w2 = new Word();
        w2.set(6);
        alu = new ALU(w1, w2);
        alu.doOperation(new Bit[]{new Bit(true), new Bit(false), new Bit(false), new Bit(false)});
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f", alu.getResult().toString());
    }

    @Test
    public void test_alu_or() {
        // 3 5
        Word w1 = new Word();
        w1.set(3);
        Word w2 = new Word();
        w2.set(5);
        ALU alu = new ALU(w1, w2);
        alu.doOperation(new Bit[]{new Bit(true), new Bit(false), new Bit(false), new Bit(true)});
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, t, t", alu.getResult().toString());

        // 12 9
        w1 = new Word();
        w1.set(12);
        w2 = new Word();
        w2.set(9);
        alu = new ALU(w1, w2);
        alu.doOperation(new Bit[]{new Bit(true), new Bit(false), new Bit(false), new Bit(true)});
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, t, f, t", alu.getResult().toString());

        // 7 2
        w1 = new Word();
        w1.set(7);
        w2 = new Word();
        w2.set(2);
        alu = new ALU(w1, w2);
        alu.doOperation(new Bit[]{new Bit(true), new Bit(false), new Bit(false), new Bit(true)});
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, t, t", alu.getResult().toString());

        // 18 6
        w1 = new Word();
        w1.set(18);
        w2 = new Word();
        w2.set(6);
        alu = new ALU(w1, w2);
        alu.doOperation(new Bit[]{new Bit(true), new Bit(false), new Bit(false), new Bit(true)});
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, t, t, f", alu.getResult().toString());
    }

    @Test
    public void test_alu_xor() {
        // 3 5
        Word w1 = new Word();
        w1.set(3);
        Word w2 = new Word();
        w2.set(5);
        ALU alu = new ALU(w1, w2);
        alu.doOperation(new Bit[]{new Bit(true), new Bit(false), new Bit(true), new Bit(false)});
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, t, f", alu.getResult().toString());

        // 12 9
        w1 = new Word();
        w1.set(12);
        w2 = new Word();
        w2.set(9);
        alu = new ALU(w1, w2);
        alu.doOperation(new Bit[]{new Bit(true), new Bit(false), new Bit(true), new Bit(false)});
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, t", alu.getResult().toString());

        // 7 2
        w1 = new Word();
        w1.set(7);
        w2 = new Word();
        w2.set(2);
        alu = new ALU(w1, w2);
        alu.doOperation(new Bit[]{new Bit(true), new Bit(false), new Bit(true), new Bit(false)});
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, t", alu.getResult().toString());

        // 18 6
        w1 = new Word();
        w1.set(18);
        w2 = new Word();
        w2.set(6);
        alu = new ALU(w1, w2);
        alu.doOperation(new Bit[]{new Bit(true), new Bit(false), new Bit(true), new Bit(false)});
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, t, f, f", alu.getResult().toString());
    }

    @Test
    public void test_alu_not() {
        // 3
        Word w1 = new Word();
        w1.set(3);
        Word w2 = new Word();
        ALU alu = new ALU(w1, w2);
        alu.doOperation(new Bit[]{new Bit(true), new Bit(false), new Bit(true), new Bit(true)});
        assertEquals("t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, f, f", alu.getResult().toString());

        // 12
        w1 = new Word();
        w1.set(12);
        alu = new ALU(w1, w2);
        alu.doOperation(new Bit[]{new Bit(true), new Bit(false), new Bit(true), new Bit(true)});
        assertEquals("t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, f, f, t, t", alu.getResult().toString());

        // 7
        w1 = new Word();
        w1.set(7);
        alu = new ALU(w1, w2);
        alu.doOperation(new Bit[]{new Bit(true), new Bit(false), new Bit(true), new Bit(true)});
        assertEquals("t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, f, f, f", alu.getResult().toString());

        // 18
        w1 = new Word();
        w1.set(18);
        alu = new ALU(w1, w2);
        alu.doOperation(new Bit[]{new Bit(true), new Bit(false), new Bit(true), new Bit(true)});
        assertEquals("t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, f, t, t, f, t", alu.getResult().toString());
    }

    @Test
    public void test_alu_leftShift() {
        // 1 shift by 1
        Word w1 = new Word();
        w1.set(1);
        Word w2 = new Word();
        w2.set(1);
        ALU alu = new ALU(w1, w2);
        alu.doOperation(new Bit[]{new Bit(true), new Bit(true), new Bit(false), new Bit(false)});
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f", alu.getResult().toString());

        // 8 shift by 3
        w1 = new Word();
        w1.set(8);
        w2 = new Word();
        w2.set(3);
        alu = new ALU(w1, w2);
        alu.doOperation(new Bit[]{new Bit(true), new Bit(true), new Bit(false), new Bit(false)});
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, f, f, f, f, f", alu.getResult().toString());

        // 187 shift by 2
        w1 = new Word();
        w1.set(187);
        w2 = new Word();
        w2.set(2);
        alu = new ALU(w1, w2);
        alu.doOperation(new Bit[]{new Bit(true), new Bit(true), new Bit(false), new Bit(false)});
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, t, t, t, f, t, t, f, f", alu.getResult().toString());

        // 187 shift by 0
        w1 = new Word();
        w1.set(187);
        w2 = new Word();
        w2.set(0);
        alu = new ALU(w1, w2);
        alu.doOperation(new Bit[]{new Bit(true), new Bit(true), new Bit(false), new Bit(false)});
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, t, t, t, f, t, t", alu.getResult().toString());
    }

    @Test
    public void test_alu_rightShift() {
        // 1 shift by 1
        Word w1 = new Word();
        w1.set(1);
        Word w2 = new Word();
        w2.set(1);
        ALU alu = new ALU(w1, w2);
        alu.doOperation(new Bit[]{new Bit(true), new Bit(true), new Bit(false), new Bit(true)});
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", alu.getResult().toString());

        // 8 shift by 3
        w1 = new Word();
        w1.set(8);
        w2 = new Word();
        w2.set(3);
        alu = new ALU(w1, w2);
        alu.doOperation(new Bit[]{new Bit(true), new Bit(true), new Bit(false), new Bit(true)});
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t", alu.getResult().toString());

        // 187 shift by 2
        w1 = new Word();
        w1.set(187);
        w2 = new Word();
        w2.set(2);
        alu = new ALU(w1, w2);
        alu.doOperation(new Bit[]{new Bit(true), new Bit(true), new Bit(false), new Bit(true)});
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, t, t, t, f", alu.getResult().toString());

        // 187 shift by 0
        w1 = new Word();
        w1.set(187);
        w2 = new Word();
        w2.set(0);
        alu = new ALU(w1, w2);
        alu.doOperation(new Bit[]{new Bit(true), new Bit(true), new Bit(false), new Bit(true)});
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, t, t, t, f, t, t", alu.getResult().toString());
    }

    private String addBinary3(String a, String b) {
        return "00000000000000000000000000010110";
    }

    private String subtractBinary3(String a, String b) {
        return "11111111111111111111111111111010";
    }

    @Test
    public void test_alu_add() {
        // 3 + 5 = 8
        Word w1 = new Word();
        w1.set(3);
        Word w2 = new Word();
        w2.set(5);
        ALU alu = new ALU(w1, w2);
        alu.doOperation(new Bit[]{new Bit(true), new Bit(true), new Bit(true), new Bit(false)});
        assertEquals(8, alu.getResult().getSigned());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, f, f", alu.getResult().toString());

        // 12 + 9 = 21
        w1.set(12);
        w2.set(9);
        alu = new ALU(w1, w2);
        alu.doOperation(new Bit[]{new Bit(true), new Bit(true), new Bit(true), new Bit(false)});
        assertEquals(21, alu.getResult().getSigned());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, t, f, t", alu.getResult().toString());

        // 7 + 2 = 9
        w1.set(7);
        w2.set(2);
        alu = new ALU(w1, w2);
        alu.doOperation(new Bit[]{new Bit(true), new Bit(true), new Bit(true), new Bit(false)});
        assertEquals(9, alu.getResult().getSigned());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, f, t", alu.getResult().toString());

        // 18 + 6 = 24
        w1.set(18);
        w2.set(6);
        alu = new ALU(w1, w2);
        alu.doOperation(new Bit[]{new Bit(true), new Bit(true), new Bit(true), new Bit(false)});
        assertEquals(24, alu.getResult().getSigned());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, t, f, f, f", alu.getResult().toString());

        // 11 + 4 = 15
        w1.set(11);
        w2.set(4);
        alu = new ALU(w1, w2);
        alu.doOperation(new Bit[]{new Bit(true), new Bit(true), new Bit(true), new Bit(false)});
        assertEquals(15, alu.getResult().getSigned());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, t, t, t", alu.getResult().toString());

        // 20 + 1 = 21
        w1.set(20);
        w2.set(1);
        alu = new ALU(w1, w2);
        alu.doOperation(new Bit[]{new Bit(true), new Bit(true), new Bit(true), new Bit(false)});
        assertEquals(21, alu.getResult().getSigned());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, t, f, t", alu.getResult().toString());

        // 8 + 13 = 21
        w1.set(8);
        w2.set(13);
        alu = new ALU(w1, w2);
        alu.doOperation(new Bit[]{new Bit(true), new Bit(true), new Bit(true), new Bit(false)});
        assertEquals(21, alu.getResult().getSigned());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, t, f, t", alu.getResult().toString());

        // 2958 + 4621 = 7579
        w1.set(2958);
        w2.set(4621);
        alu = new ALU(w1, w2);
        alu.doOperation(new Bit[]{new Bit(true), new Bit(true), new Bit(true), new Bit(false)});
        assertEquals(7579, alu.getResult().getSigned());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, t, t, f, t, t, f, f, t, t, f, t, t", alu.getResult().toString());

        // 1786 + 8764 = 10550
        w1.set(1786);
        w2.set(8764);
        alu = new ALU(w1, w2);
        alu.doOperation(new Bit[]{new Bit(true), new Bit(true), new Bit(true), new Bit(false)});
        assertEquals(10550, alu.getResult().getSigned());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, t, f, f, t, f, f, t, t, f, t, t, f", alu.getResult().toString());

        // 2307 + 4623 = 6930
        w1.set(2307);
        w2.set(4623);
        alu = new ALU(w1, w2);
        alu.doOperation(new Bit[]{new Bit(true), new Bit(true), new Bit(true), new Bit(false)});
        assertEquals(6930, alu.getResult().getSigned());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, t, f, t, t, f, f, f, t, f, f, t, f", alu.getResult().toString());

        // -7 + 2 = -5
        w1.set(-7);
        w2.set(2);
        alu = new ALU(w1, w2);
        alu.doOperation(new Bit[]{new Bit(true), new Bit(true), new Bit(true), new Bit(false)});
        assertEquals(-5, alu.getResult().getSigned());
        assertEquals("t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, f, t, t", alu.getResult().toString());

        // -8 + 13 = 5
        w1.set(-8);
        w2.set(13);
        alu = new ALU(w1, w2);
        alu.doOperation(new Bit[]{new Bit(true), new Bit(true), new Bit(true), new Bit(false)});
        assertEquals(5, alu.getResult().getSigned());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, t", alu.getResult().toString());

        // 8 + -13 = -5
        w1.set(8);
        w2.set(-13);
        alu = new ALU(w1, w2);
        alu.doOperation(new Bit[]{new Bit(true), new Bit(true), new Bit(true), new Bit(false)});
        assertEquals(-5, alu.getResult().getSigned());
        assertEquals("t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, f, t, t", alu.getResult().toString());

        // -0 + 13 = 13
        w1.set(0);
        w2.set(13);
        alu = new ALU(w1, w2);
        alu.doOperation(new Bit[]{new Bit(true), new Bit(true), new Bit(true), new Bit(false)});
        assertEquals(13, alu.getResult().getSigned());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, t, f, t", alu.getResult().toString());

        // 0 + -13 = -13
        w1.set(0);
        w2.set(-13);
        alu = new ALU(w1, w2);
        alu.doOperation(new Bit[]{new Bit(true), new Bit(true), new Bit(true), new Bit(false)});
        assertEquals(-13, alu.getResult().getSigned());
        assertEquals("t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, f, f, t, t", alu.getResult().toString());

        // 13 + 0 = 13
        w1.set(13);
        w2.set(0);
        alu = new ALU(w1, w2);
        alu.doOperation(new Bit[]{new Bit(true), new Bit(true), new Bit(true), new Bit(false)});
        assertEquals(13, alu.getResult().getSigned());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, t, f, t", alu.getResult().toString());

        // 13 + -0 = 13
        w1.set(13);
        w2.set(-0);
        alu = new ALU(w1, w2);
        alu.doOperation(new Bit[]{new Bit(true), new Bit(true), new Bit(true), new Bit(false)});
        assertEquals(13, alu.getResult().getSigned());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, t, f, t", alu.getResult().toString());
    }

    @Test
    public void test_alu_subtract() {
        // 3 - 5 = -2
        Word w1 = new Word();
        w1.set(3);
        Word w2 = new Word();
        w2.set(5);
        ALU alu = new ALU(w1, w2);
        alu.doOperation(new Bit[]{new Bit(true), new Bit(true), new Bit(true), new Bit(true)});
        assertEquals(-2, alu.getResult().getSigned());
        assertEquals("t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, f", alu.getResult().toString());

        // 12 - 9 = 3
        w1.set(12);
        w2.set(9);
        alu = new ALU(w1, w2);
        alu.doOperation(new Bit[]{new Bit(true), new Bit(true), new Bit(true), new Bit(true)});
        assertEquals(3, alu.getResult().getSigned());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, t", alu.getResult().toString());

        // 7 - 2 = 5
        w1.set(7);
        w2.set(2);
        alu = new ALU(w1, w2);
        alu.doOperation(new Bit[]{new Bit(true), new Bit(true), new Bit(true), new Bit(true)});
        assertEquals(5, alu.getResult().getSigned());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, t", alu.getResult().toString());

        // 18 - 6 = 12
        w1.set(18);
        w2.set(6);
        alu = new ALU(w1, w2);
        alu.doOperation(new Bit[]{new Bit(true), new Bit(true), new Bit(true), new Bit(true)});
        assertEquals(12, alu.getResult().getSigned());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, t, f, f", alu.getResult().toString());

        // 11 - 4 = 7
        w1.set(11);
        w2.set(4);
        alu = new ALU(w1, w2);
        alu.doOperation(new Bit[]{new Bit(true), new Bit(true), new Bit(true), new Bit(true)});
        assertEquals(7, alu.getResult().getSigned());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, t, t", alu.getResult().toString());

        // 20 - 1 = 19
        w1.set(20);
        w2.set(1);
        alu = new ALU(w1, w2);
        alu.doOperation(new Bit[]{new Bit(true), new Bit(true), new Bit(true), new Bit(true)});
        assertEquals(19, alu.getResult().getSigned());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, f, t, t", alu.getResult().toString());

        // 8 - 13 = -5
        w1.set(8);
        w2.set(13);
        alu = new ALU(w1, w2);
        alu.doOperation(new Bit[]{new Bit(true), new Bit(true), new Bit(true), new Bit(true)});
        assertEquals(-5, alu.getResult().getSigned());
        assertEquals("t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, f, t, t", alu.getResult().toString());

        // 2958 - 4621 = -1663
        w1.set(2958);
        w2.set(4621);
        alu = new ALU(w1, w2);
        alu.doOperation(new Bit[]{new Bit(true), new Bit(true), new Bit(true), new Bit(true)});
        assertEquals(-1663, alu.getResult().getSigned());
        assertEquals("t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, f, f, t, t, f, f, f, f, f, f, t", alu.getResult().toString());

        // 1786 - 8764 = -6978
        w1.set(1786);
        w2.set(8764);
        alu = new ALU(w1, w2);
        alu.doOperation(new Bit[]{new Bit(true), new Bit(true), new Bit(true), new Bit(true)});
        assertEquals(-6978, alu.getResult().getSigned());
        assertEquals("t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, f, f, t, f, f, t, f, t, t, t, t, t, f", alu.getResult().toString());

        // 2307 - 4623 = -2316
        w1.set(2307);
        w2.set(4623);
        alu = new ALU(w1, w2);
        alu.doOperation(new Bit[]{new Bit(true), new Bit(true), new Bit(true), new Bit(true)});
        assertEquals(-2316, alu.getResult().getSigned());
        assertEquals("t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, f, t, t, f, t, t, t, t, f, t, f, f", alu.getResult().toString());

        // -7 - 2 = -9
        w1.set(-7);
        w2.set(2);
        alu = new ALU(w1, w2);
        alu.doOperation(new Bit[]{new Bit(true), new Bit(true), new Bit(true), new Bit(true)});
        assertEquals(-9, alu.getResult().getSigned());
        assertEquals("t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, f, t, t, t", alu.getResult().toString());

        // -8 - 13 = -21
        w1.set(-8);
        w2.set(13);
        alu = new ALU(w1, w2);
        alu.doOperation(new Bit[]{new Bit(true), new Bit(true), new Bit(true), new Bit(true)});
        assertEquals(-21, alu.getResult().getSigned());
        assertEquals("t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, f, t, f, t, t", alu.getResult().toString());

        // 8 - -13 = 21
        w1.set(8);
        w2.set(-13);
        alu = new ALU(w1, w2);
        alu.doOperation(new Bit[]{new Bit(true), new Bit(true), new Bit(true), new Bit(true)});
        assertEquals(21, alu.getResult().getSigned());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, t, f, t", alu.getResult().toString());

        // -0 - 13 = -13
        w1.set(-0);
        w2.set(13);
        alu = new ALU(w1, w2);
        alu.doOperation(new Bit[]{new Bit(true), new Bit(true), new Bit(true), new Bit(true)});
        assertEquals(-13, alu.getResult().getSigned());
        assertEquals("t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, f, f, t, t", alu.getResult().toString());

        // 0 - -13 = 13
        w1.set(0);
        w2.set(-13);
        alu = new ALU(w1, w2);
        alu.doOperation(new Bit[]{new Bit(true), new Bit(true), new Bit(true), new Bit(true)});
        assertEquals(13, alu.getResult().getSigned());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, t, f, t", alu.getResult().toString());

        // 13 - 0 = 13
        w1.set(13);
        w2.set(0);
        alu = new ALU(w1, w2);
        alu.doOperation(new Bit[]{new Bit(true), new Bit(true), new Bit(true), new Bit(true)});
        assertEquals(13, alu.getResult().getSigned());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, t, f, t", alu.getResult().toString());

        // 13 - -0 = 13
        w1.set(13);
        w2.set(-0);
        alu = new ALU(w1, w2);
        alu.doOperation(new Bit[]{new Bit(true), new Bit(true), new Bit(true), new Bit(true)});
        assertEquals(13, alu.getResult().getSigned());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, t, f, t", alu.getResult().toString());
    }

    @Test
    public void test_alu_add2() {
        // 3 + 5 = 8
        Word w1 = new Word();
        w1.set(3);
        Word w2 = new Word();
        w2.set(5);
        ALU alu = new ALU(w1, w2);
        Word add2Result = alu.add2(w1, w2);
        assertEquals(8, add2Result.getSigned());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, f, f", add2Result.toString());

        // 12 + 9 = 21
        w1.set(12);
        w2.set(9);
        alu = new ALU(w1, w2);
        add2Result = alu.add2(w1, w2);
        assertEquals(21, add2Result.getSigned());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, t, f, t", add2Result.toString());

        // 7 + 2 = 9
        w1.set(7);
        w2.set(2);
        alu = new ALU(w1, w2);
        add2Result = alu.add2(w1, w2);
        assertEquals(9, add2Result.getSigned());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, f, t", add2Result.toString());

        // 18 + 6 = 24
        w1.set(18);
        w2.set(6);
        alu = new ALU(w1, w2);
        add2Result = alu.add2(w1, w2);
        assertEquals(24, add2Result.getSigned());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, t, f, f, f", add2Result.toString());

        // 11 + 4 = 15
        w1.set(11);
        w2.set(4);
        alu = new ALU(w1, w2);
        add2Result = alu.add2(w1, w2);
        assertEquals(15, add2Result.getSigned());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, t, t, t", add2Result.toString());

        // 20 + 1 = 21
        w1.set(20);
        w2.set(1);
        alu = new ALU(w1, w2);
        add2Result = alu.add2(w1, w2);
        assertEquals(21, add2Result.getSigned());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, t, f, t", add2Result.toString());

        // 8 + 13 = 21
        w1.set(8);
        w2.set(13);
        alu = new ALU(w1, w2);
        add2Result = alu.add2(w1, w2);
        assertEquals(21, add2Result.getSigned());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, t, f, t", add2Result.toString());

        // 2958 + 4621 = 7579
        w1.set(2958);
        w2.set(4621);
        alu = new ALU(w1, w2);
        add2Result = alu.add2(w1, w2);
        assertEquals(7579, add2Result.getSigned());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, t, t, f, t, t, f, f, t, t, f, t, t", add2Result.toString());

        // 1786 + 8764 = 10550
        w1.set(1786);
        w2.set(8764);
        alu = new ALU(w1, w2);
        add2Result = alu.add2(w1, w2);
        assertEquals(10550, add2Result.getSigned());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, t, f, f, t, f, f, t, t, f, t, t, f", add2Result.toString());

        // 2307 + 4623 = 6930
        w1.set(2307);
        w2.set(4623);
        alu = new ALU(w1, w2);
        add2Result = alu.add2(w1, w2);
        assertEquals(6930, add2Result.getSigned());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, t, f, t, t, f, f, f, t, f, f, t, f", add2Result.toString());

        // -7 + 2 = -5
        w1.set(-7);
        w2.set(2);
        alu = new ALU(w1, w2);
        add2Result = alu.add2(w1, w2);
        assertEquals(-5, add2Result.getSigned());
        assertEquals("t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, f, t, t", add2Result.toString());

        // -8 + 13 = 5
        w1.set(-8);
        w2.set(13);
        alu = new ALU(w1, w2);
        add2Result = alu.add2(w1, w2);
        assertEquals(5, add2Result.getSigned());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, t", add2Result.toString());

        // 8 + -13 = -5
        w1.set(8);
        w2.set(-13);
        alu = new ALU(w1, w2);
        add2Result = alu.add2(w1, w2);
        assertEquals(-5, add2Result.getSigned());
        assertEquals("t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, f, t, t", add2Result.toString());

        // -0 + 13 = 13
        w1.set(0);
        w2.set(13);
        alu = new ALU(w1, w2);
        add2Result = alu.add2(w1, w2);
        assertEquals(13, add2Result.getSigned());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, t, f, t", add2Result.toString());

        // 0 + -13 = -13
        w1.set(0);
        w2.set(-13);
        alu = new ALU(w1, w2);
        add2Result = alu.add2(w1, w2);
        assertEquals(-13, add2Result.getSigned());
        assertEquals("t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, f, f, t, t", add2Result.toString());

        // 13 + 0 = 13
        w1.set(13);
        w2.set(0);
        alu = new ALU(w1, w2);
        add2Result = alu.add2(w1, w2);
        assertEquals(13, add2Result.getSigned());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, t, f, t", add2Result.toString());

        // 13 + -0 = 13
        w1.set(13);
        w2.set(-0);
        alu = new ALU(w1, w2);
        add2Result = alu.add2(w1, w2);
        assertEquals(13, add2Result.getSigned());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, t, f, t", add2Result.toString());
    }

    @Test
    public void test_alu_add4() {
        // 3 + 5 + 4 + 1 = 13
        Word w1 = new Word();
        w1.set(3);
        Word w2 = new Word();
        w2.set(5);
        Word w3 = new Word();
        w3.set(4);
        Word w4 = new Word();
        w4.set(1);
        ALU alu = new ALU(w1, w2);
        Word add4Result = alu.add4(w1, w2, w3, w4);
        assertEquals(15, add4Result.getSigned());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, t, t, t", add4Result.toString());

        // 20 + 1 + 8 + 13 = 42
        w1 = new Word();
        w1.set(20);
        w2 = new Word();
        w2.set(1);
        w3 = new Word();
        w3.set(8);
        w4 = new Word();
        w4.set(13);
        alu = new ALU(w1, w2);
        add4Result = alu.add4(w1, w2, w3, w4);
        assertEquals(58, add4Result.getSigned());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, t, t, f, t, f", add4Result.toString());

        // 2958 + 4621 + 1786 + 8764 = 18129
        w1 = new Word();
        w1.set(2958);
        w2 = new Word();
        w2.set(4621);
        w3 = new Word();
        w3.set(1786);
        w4 = new Word();
        w4.set(8764);
        alu = new ALU(w1, w2);
        add4Result = alu.add4(w1, w2, w3, w4);
        assertEquals(12497, add4Result.getSigned());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, t, f, f, f, f, t, t, f, t, f, f, f, t", add4Result.toString());

        // 7 + 2 + 8 + 13 = 30
        w1 = new Word();
        w1.set(7);
        w2 = new Word();
        w2.set(2);
        w3 = new Word();
        w3.set(8);
        w4 = new Word();
        w4.set(13);
        alu = new ALU(w1, w2);
        add4Result = alu.add4(w1, w2, w3, w4);
        assertEquals(2, add4Result.getSigned());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f", add4Result.toString());

        // 8 + 13 + 0 + 13 = 34
        w1 = new Word();
        w1.set(8);
        w2 = new Word();
        w2.set(13);
        w3 = new Word();
        w3.set(0);
        w4 = new Word();
        w4.set(13);
        alu = new ALU(w1, w2);
        add4Result = alu.add4(w1, w2, w3, w4);
        assertEquals(2, add4Result.getSigned());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f", add4Result.toString());

        // 13 + -0 + 0 + -13 = 26
        w1 = new Word();
        w1.set(13);
        w2 = new Word();
        w2.set(0);
        w3 = new Word();
        w3.set(0);
        w4 = new Word();
        w4.set(13);
        alu = new ALU(w1, w2);
        add4Result = alu.add4(w1, w2, w3, w4);
        assertEquals(26, add4Result.getSigned());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, t, f, t, f", add4Result.toString());
    }

    // ----- TESTS FOR MAINMEMORY CLASS -----
    @Test
    public void test_mainmemory_read() {
        Word address1 = new Word();
        address1.set(0);
        Word address2 = new Word();
        address2.set(1023);
        Word address3 = new Word();
        address3.set(4);
        Word address4 = new Word();
        address4.set(8);
        Word address5 = new Word();
        address5.set(123);
        Word address6 = new Word();
        address6.set(984);
        Word address7 = new Word();
        address7.set(84);
        Word address8 = new Word();
        address8.set(48);

        MainMemory.fillInMemory();
        MainMemory.memoryArr[(int)address1.getUnsigned()].set(12);
        MainMemory.memoryArr[(int)address2.getUnsigned()].set(-12341);
        MainMemory.memoryArr[(int)address3.getUnsigned()].set(-32188);
        MainMemory.memoryArr[(int)address4.getUnsigned()].set(948745);
        MainMemory.memoryArr[(int)address5.getUnsigned()].set(892869);
        MainMemory.memoryArr[(int)address6.getUnsigned()].set(834551);
        MainMemory.memoryArr[(int)address7.getUnsigned()].set(367094);

        assertEquals(12, MainMemory.read(address1).getSigned());
        assertEquals(-12341, MainMemory.read(address2).getSigned());
        assertEquals(-32188, MainMemory.read(address3).getSigned());
        assertEquals(948745, MainMemory.read(address4).getSigned());
        assertEquals(892869, MainMemory.read(address5).getSigned());
        assertEquals(834551, MainMemory.read(address6).getSigned());
        assertEquals(367094, MainMemory.read(address7).getSigned());
        assertEquals(0, MainMemory.read(address8).getSigned());



    }

    @Test
    public void test_mainmemory_write() {
        Word address = new Word();
        Word value = new Word();

        MainMemory.fillInMemory();

        address.set(0);
        assertEquals(0, MainMemory.read(address).getSigned());
        value.set(18);
        MainMemory.write(address, value);
        assertEquals(18, MainMemory.read(address).getSigned());

        address.set(1023);
        assertEquals(0, MainMemory.read(address).getSigned());
        value.set(90);
        MainMemory.write(address, value);
        assertEquals(90, MainMemory.read(address).getSigned());

        address.set(213);
        assertEquals(0, MainMemory.read(address).getSigned());
        value.set(18);
        MainMemory.write(address, value);
        assertEquals(18, MainMemory.read(address).getSigned());

        address.set(1);
        assertEquals(0, MainMemory.read(address).getSigned());
        value.set(12);
        MainMemory.write(address, value);
        assertEquals(12, MainMemory.read(address).getSigned());

        address.set(0);
        assertEquals(18, MainMemory.read(address).getSigned());
        value.set(1239);
        MainMemory.write(address, value);
        assertEquals(1239, MainMemory.read(address).getSigned());
    }

    @Test
    public void test_mainmemory_load() {
        String[] data = {"00000000000000000000000000000000",  // 0
                         "00000000000000000000000000000001",  // 1
                         "00000000001000000100010001000000",  // 2114624
                         "11111111111011010010100111110100",  // -1234444
                         "11111111111111111111111111111111"}; // -1

        MainMemory.load(data);
        assertEquals(0, MainMemory.memoryArr[0].getSigned());
        assertEquals(1, MainMemory.memoryArr[1].getSigned());
        assertEquals(2114624, MainMemory.memoryArr[2].getSigned());
        assertEquals(-1234444, MainMemory.memoryArr[3].getSigned());
        assertEquals(-1, MainMemory.memoryArr[4].getSigned());
    }

    // ----- TESTS FOR PROCESSOR (ASSIGNMENT 3: COMPUTER 1) CLASS -----
    @Test
    public void test_word_increment() {
        Word num = new Word();
        num.set(1);
        assertEquals(1, num.getUnsigned());
        Word newNum = num.increment();
        assertEquals(2, newNum.getUnsigned());

        newNum = newNum.increment();
        assertEquals(3, newNum.getUnsigned());

        num = new Word();
        num.set(198344);
        assertEquals(198344, num.getSigned());
        newNum = num.increment();
        assertEquals(198345, newNum.getSigned());

        num = new Word();
        num.set(-12);
        assertEquals(-12, num.getSigned());
        newNum = num.increment();
        assertEquals(-11, newNum.getSigned());

        num = new Word();
        num.set(-198344);
        assertEquals(-198344, num.getSigned());
        newNum = num.increment();
        assertEquals(-198343, newNum.getSigned());

    }

    // ----- TESTS FOR PROCESSOR (ASSIGNMENT 4: COMPUTER 2) CLASS -----

    @Test
    public void test_processor_decode() {
        Processor p = new Processor();
        // 3 Register (3R) - 11
        p.currentInstruction = new Word("00101001"+"00100"+"00010"+"0100"+"11000"+"00011");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, t, f, f, t", p.instructions.get("immediate").toString());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, f", p.instructions.get("rs1").toString());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f", p.instructions.get("rs2").toString());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, f", p.instructions.get("function").toString());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, t, f, f, f", p.instructions.get("rd").toString());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, t", p.instructions.get("opcode").toString());
        // 2 Register (2R) - 10
        p.currentInstruction = new Word("0010001001101"+"00111"+"1100"+"01000"+"01010");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, f, f, t, f, f, t, t, f, t", p.instructions.get("immediate").toString());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, t, t", p.instructions.get("rs").toString());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, t, f, f", p.instructions.get("function").toString());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, f, f", p.instructions.get("rd").toString());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, t, f", p.instructions.get("opcode").toString());
        // Dest Only (1R) - 01
        p.currentInstruction = new Word("011100001000000010"+"1111"+"01010"+"10001");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, t, t, f, f, f, f, t, f, f, f, f, f, f, f, t, f", p.instructions.get("immediate").toString());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, t, t, t", p.instructions.get("function").toString());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, t, f", p.instructions.get("rd").toString());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, f, f, t", p.instructions.get("opcode").toString());
        // No Register (0R) - 00
        p.currentInstruction = new Word("010010010111000000011010010"+"01100");
        p.decode();
        assertEquals("f, f, f, f, f, f, t, f, f, t, f, f, t, f, t, t, t, f, f, f, f, f, f, f, t, t, f, t, f, f, t, f", p.instructions.get("immediate").toString());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, t, f, f", p.instructions.get("opcode").toString());
    }

    @Test
    public void test_processor_execute_single() throws Exception {
        // these are tested one instruction at a time by creating a new processor for each instruction

        // test 3R (11)
        Processor p = new Processor();
        p.currentInstruction = new Word("00101001"+"00100"+"00010"+"1110"+"11000"+"00011");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, t, f, f, t", p.instructions.get("immediate").toString());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, f", p.instructions.get("rs1").toString());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f", p.instructions.get("rs2").toString());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, t, t, f", p.instructions.get("function").toString());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, t, f, f, f", p.instructions.get("rd").toString());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, t", p.instructions.get("opcode").toString());
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.instructions.get("result").toString());
        for (int i = 0; i < 32; i++) {
            assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.registers[i].toString());
        }

        // test 2R (10)
        p = new Processor();
        p.currentInstruction = new Word("0010001001101"+"00111"+"1110"+"01000"+"00010");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, f, f, t, f, f, t, t, f, t", p.instructions.get("immediate").toString());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, t, t", p.instructions.get("rs").toString());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, t, t, f", p.instructions.get("function").toString());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, f, f", p.instructions.get("rd").toString());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f", p.instructions.get("opcode").toString());
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.instructions.get("result").toString());
        for (int i = 0; i < 32; i++) {
            assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.registers[i].toString());
        }

        // test Dest only (01)
        p = new Processor();
        p.currentInstruction = new Word("011100001000000010"+"1111"+"01010"+"00001");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, t, t, f, f, f, f, t, f, f, f, f, f, f, f, t, f", p.instructions.get("immediate").toString());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, t, t, t", p.instructions.get("function").toString());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, t, f", p.instructions.get("rd").toString());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t", p.instructions.get("opcode").toString());
        assertFalse(p.halted.getValue());
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, t, t, f, f, f, f, t, f, f, f, f, f, f, f, t, f", p.instructions.get("result").toString());
        for (int i = 0; i < 32; i++) {
            assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.registers[i].toString());
        }

        // test halted (00)
        p = new Processor();
        p.currentInstruction = new Word("010010010111000000011010010"+"00000");
        p.decode();
        assertEquals("f, f, f, f, f, f, t, f, f, t, f, f, t, f, t, t, t, f, f, f, f, f, f, f, t, t, f, t, f, f, t, f", p.instructions.get("immediate").toString());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.instructions.get("opcode").toString());
        assertFalse(p.halted.getValue());
        p.execute();
        assertTrue(p.halted.getValue());
        for (int i = 0; i < 32; i++) {
            assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.registers[i].toString());
        }
    }

    @Test
    public void test_a4_process_store() throws Exception {
        // these are tested one instruction at a time by creating a new processor for each instruction

        // test 3R (11)
        Processor p = new Processor();
        p.currentInstruction = new Word("00101001"+"00100"+"00010"+"1110"+"11000"+"00011");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, t, f, f, t", p.instructions.get("immediate").toString());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, f", p.instructions.get("rs1").toString());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f", p.instructions.get("rs2").toString());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, t, t, f", p.instructions.get("function").toString());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, t, f, f, f", p.instructions.get("rd").toString());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, t", p.instructions.get("opcode").toString());
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.instructions.get("result").toString());
        p.store();
        for (int i = 0; i < 32; i++) {
            assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.registers[i].toString());
        }

        // test 2R (10)
        p = new Processor();
        p.currentInstruction = new Word("0010001001101"+"00111"+"1110"+"01000"+"00010");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, f, f, t, f, f, t, t, f, t", p.instructions.get("immediate").toString());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, t, t", p.instructions.get("rs").toString());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, t, t, f", p.instructions.get("function").toString());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, f, f", p.instructions.get("rd").toString());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f", p.instructions.get("opcode").toString());
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.instructions.get("result").toString());
        p.store();
        for (int i = 0; i < 32; i++) {
            assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.registers[i].toString());
        }

        // test Dest only (01)
        p = new Processor();
        p.currentInstruction = new Word("011100001000000010"+"1111"+"01010"+"00001");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, t, t, f, f, f, f, t, f, f, f, f, f, f, f, t, f", p.instructions.get("immediate").toString());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, t, t, t", p.instructions.get("function").toString());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, t, f", p.instructions.get("rd").toString());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t", p.instructions.get("opcode").toString());
        assertFalse(p.halted.getValue());
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, t, t, f, f, f, f, t, f, f, f, f, f, f, f, t, f", p.instructions.get("result").toString());
        p.store();
        for (int i = 0; i < 32; i++) {
            if (i == 10) {
                assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, t, t, f, f, f, f, t, f, f, f, f, f, f, f, t, f", p.registers[i].toString());
            } else {
                assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.registers[i].toString());
            }
        }

        // test halted (00)
        p = new Processor();
        p.currentInstruction = new Word("010010010111000000011010010"+"00000");
        p.decode();
        assertEquals("f, f, f, f, f, f, t, f, f, t, f, f, t, f, t, t, t, f, f, f, f, f, f, f, t, t, f, t, f, f, t, f", p.instructions.get("immediate").toString());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.instructions.get("opcode").toString());
        assertFalse(p.halted.getValue());
        p.execute();
        assertTrue(p.halted.getValue());
        p.store();
        for (int i = 0; i < 32; i++) {
            assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.registers[i].toString());
        }
    }

    @Test
    public void test_a4_processor_run() throws Exception {
        String[] data = {"000000000000000101"+"1010"+"00001"+"00001",
                         "10001110"+"00010"+"00001"+"1110"+"00001"+"00011",
                         "0000000000000"+"00010"+"1110"+"00010"+"00010",
                         "01001010"+"00011"+"00001"+"1110"+"00010"+"00011",
                         "000000000000000000000000000"+"00000"};
        MainMemory.load(data);
        Processor p = new Processor();
        p.run();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.registers[0].toString());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, t", p.registers[1].toString());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, t", p.registers[2].toString());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.registers[3].toString());
    }

    // ----- TESTS FOR PROCESSOR (ASSIGNMENT 5: COMPUTER 3) CLASS -----

    @Test
    public void test_processor_execute_branchNoR() throws Exception {
        Processor p = new Processor();
        p.currentInstruction = new Word("010010010111000000011010010"+"00100");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        p.execute();
        assertEquals("f, f, f, f, f, f, t, f, f, t, f, f, t, f, t, t, t, f, f, f, f, f, f, f, t, t, f, t, f, f, t, f", p.getPC().toString());
    }

    @Test
    public void test_processor_execute_branchDestOnly() throws Exception {
        Processor p = new Processor();
        p.currentInstruction = new Word("011100001000000010"+"1111"+"01010"+"00101");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, t, t, f, f, f, f, t, f, f, f, f, f, f, f, t, f", p.getPC().toString());
    }

    @Test
    public void test_processor_execute_branch2R() throws Exception {

        // test equals (0000) true
        Processor p = new Processor();
        p.registers[1].set(32);
        p.registers[2].set(32);
        p.currentInstruction = new Word("0010001001101"+"00001"+"0000"+"00010"+"00110");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, f, f, t, f, f, t, t, f, t", p.getPC().toString());

        //test equals (0000) false
        p = new Processor();
        p.registers[1].set(32);
        p.registers[2].set(31);
        p.currentInstruction = new Word("0010001001101"+"00001"+"0000"+"00010"+"00110");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());

        // test not equal (0001) true
        p = new Processor();
        p.registers[1].set(32);
        p.registers[2].set(31);
        p.currentInstruction = new Word("0010001001101"+"00001"+"0001"+"00010"+"00110");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, f, f, t, f, f, t, t, f, t", p.getPC().toString());
        // test not equal (0001) false
        p = new Processor();
        p.registers[1].set(32);
        p.registers[2].set(32);
        p.currentInstruction = new Word("0010001001101"+"00001"+"0001"+"00010"+"00110");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());


        // test less than (0010) true
        p = new Processor();
        p.registers[1].set(30);
        p.registers[2].set(31);
        p.currentInstruction = new Word("0010001001101"+"00001"+"0010"+"00010"+"00110");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, f, f, t, f, f, t, t, f, t", p.getPC().toString());
        // test less than (0010) false
        p = new Processor();
        p.registers[1].set(32);
        p.registers[2].set(32);
        p.currentInstruction = new Word("0010001001101"+"00001"+"0010"+"00010"+"00110");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        p = new Processor();
        p.registers[1].set(35);
        p.registers[2].set(32);
        p.currentInstruction = new Word("0010001001101"+"00001"+"0010"+"00010"+"00110");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());

        // test greater than or equal (0011) true
        p = new Processor();
        p.registers[1].set(32);
        p.registers[2].set(31);
        p.currentInstruction = new Word("0010001001101"+"00001"+"0011"+"00010"+"00110");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, f, f, t, f, f, t, t, f, t", p.getPC().toString());
        p = new Processor();
        p.registers[1].set(31);
        p.registers[2].set(31);
        p.currentInstruction = new Word("0010001001101"+"00001"+"0011"+"00010"+"00110");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, f, f, t, f, f, t, t, f, t", p.getPC().toString());
        // test greater than or equal (0011) false
        p = new Processor();
        p.registers[1].set(30);
        p.registers[2].set(31);
        p.currentInstruction = new Word("0010001001101"+"00001"+"0011"+"00010"+"00110");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());

        // test greater than (0100) true
        p = new Processor();
        p.registers[1].set(32);
        p.registers[2].set(31);
        p.currentInstruction = new Word("0010001001101"+"00001"+"0100"+"00010"+"00110");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, f, f, t, f, f, t, t, f, t", p.getPC().toString());
        // test greater than (0100) false
        p = new Processor();
        p.registers[1].set(31);
        p.registers[2].set(31);
        p.currentInstruction = new Word("0010001001101"+"00001"+"0100"+"00010"+"00110");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        p = new Processor();
        p.registers[1].set(30);
        p.registers[2].set(31);
        p.currentInstruction = new Word("0010001001101"+"00001"+"0100"+"00010"+"00110");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());

        // test less than or equal (0101) true
        p = new Processor();
        p.registers[1].set(30);
        p.registers[2].set(31);
        p.currentInstruction = new Word("0010001001101"+"00001"+"0101"+"00010"+"00110");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, f, f, t, f, f, t, t, f, t", p.getPC().toString());
        p = new Processor();
        p.registers[1].set(31);
        p.registers[2].set(31);
        p.currentInstruction = new Word("0010001001101"+"00001"+"0101"+"00010"+"00110");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, f, f, t, f, f, t, t, f, t", p.getPC().toString());
        // test less than or equal (0101) false
        p = new Processor();
        p.registers[1].set(32);
        p.registers[2].set(31);
        p.currentInstruction = new Word("0010001001101"+"00001"+"0101"+"00010"+"00110");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
    }

    @Test
    public void test_processor_execute_branch3R() throws Exception {
        // test equals (0000) true
        Processor p = new Processor();
        p.registers[1].set(32);
        p.registers[2].set(32);
        p.currentInstruction = new Word("00101001"+"00001"+"00010"+"0000"+"11000"+"00111");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, t, f, f, t", p.getPC().toString());

        //test equals (0000) false
        p = new Processor();
        p.registers[1].set(32);
        p.registers[2].set(31);
        p.currentInstruction = new Word("00101001"+"00010"+"00100"+"0000"+"11000"+"00111");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());

        // test not equal (0001) true
        p = new Processor();
        p.registers[1].set(31);
        p.registers[2].set(32);
        p.currentInstruction = new Word("00101001"+"00001"+"00010"+"0001"+"11000"+"00111");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, t, f, f, t", p.getPC().toString());
        p = new Processor();
        p.registers[1].set(33);
        p.registers[2].set(31);
        p.currentInstruction = new Word("00101001"+"00001"+"00010"+"0001"+"11000"+"00111");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, t, f, f, t", p.getPC().toString());
        // test not equal (0001) false
        p = new Processor();
        p.registers[1].set(31);
        p.registers[2].set(31);
        p.currentInstruction = new Word("00101001"+"00001"+"00010"+"0001"+"11000"+"00111");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());

        // test less than (0010) true
        p = new Processor();
        p.registers[1].set(31);
        p.registers[2].set(32);
        p.currentInstruction = new Word("00101001"+"00001"+"00010"+"0010"+"11000"+"00111");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, t, f, f, t", p.getPC().toString());
        // test less than (0010) false
        p = new Processor();
        p.registers[1].set(32);
        p.registers[2].set(32);
        p.currentInstruction = new Word("00101001"+"00001"+"00010"+"0010"+"11000"+"00111");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        p = new Processor();
        p.registers[1].set(33);
        p.registers[2].set(32);
        p.currentInstruction = new Word("00101001"+"00001"+"00010"+"0010"+"11000"+"00111");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());

        // test greater than or equal (0011) true
        p = new Processor();
        p.registers[1].set(32);
        p.registers[2].set(31);
        p.currentInstruction = new Word("00101001"+"00001"+"00010"+"0011"+"11000"+"00111");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, t, f, f, t", p.getPC().toString());
        p = new Processor();
        p.registers[1].set(31);
        p.registers[2].set(31);
        p.currentInstruction = new Word("00101001"+"00001"+"00010"+"0011"+"11000"+"00111");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, t, f, f, t", p.getPC().toString());
        // test greater than or equal (0011) false
        p = new Processor();
        p.registers[1].set(31);
        p.registers[2].set(32);
        p.currentInstruction = new Word("00101001"+"00001"+"00010"+"0011"+"11000"+"00111");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());

        // test greater than (0100) true
        p = new Processor();
        p.registers[1].set(32);
        p.registers[2].set(31);
        p.currentInstruction = new Word("00101001"+"00001"+"00010"+"0100"+"11000"+"00111");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, t, f, f, t", p.getPC().toString());
        // test greater than (0100) false
        p = new Processor();
        p.registers[1].set(31);
        p.registers[2].set(31);
        p.currentInstruction = new Word("00101001"+"00001"+"00010"+"0100"+"11000"+"00111");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        p = new Processor();
        p.registers[1].set(30);
        p.registers[2].set(31);
        p.currentInstruction = new Word("00101001"+"00001"+"00010"+"0100"+"11000"+"00111");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());

        // test less than or equal (0101) true
        p = new Processor();
        p.registers[1].set(30);
        p.registers[2].set(31);
        p.currentInstruction = new Word("00101001"+"00001"+"00010"+"0101"+"11000"+"00111");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, t, f, f, t", p.getPC().toString());
        p = new Processor();
        p.registers[1].set(31);
        p.registers[2].set(31);
        p.currentInstruction = new Word("00101001"+"00001"+"00010"+"0101"+"11000"+"00111");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, t, f, f, t", p.getPC().toString());
        // test less than or equal (0101) false
        p = new Processor();
        p.registers[1].set(32);
        p.registers[2].set(31);
        p.currentInstruction = new Word("00101001"+"00001"+"00010"+"0101"+"11000"+"00111");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
    }

    @Test
    public void test_processor_execute_callNoR() throws Exception {
        Processor p = new Processor();
        p.currentInstruction = new Word("010010010111000000011010010"+"01000");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        Word tempSP = new Word();
        tempSP.set(1023);
        p.execute();
        assertEquals("f, f, f, f, f, f, t, f, f, t, f, f, t, f, t, t, t, f, f, f, f, f, f, f, t, t, f, t, f, f, t, f", p.getPC().toString());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", MainMemory.read(tempSP).toString());
        p.execute();
        tempSP.set(1022);
        assertEquals("f, f, f, f, f, f, t, f, f, t, f, f, t, f, t, t, t, f, f, f, f, f, f, f, t, t, f, t, f, f, t, f", MainMemory.read(tempSP).toString());
    }

    @Test
    public void test_processor_execute_callDestOnly() throws Exception {
        Processor p = new Processor();
        p.registers[1].set(32);
        p.currentInstruction = new Word("000000000000000010"+"1111"+"00001"+"01001");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        Word tempSP = new Word();
        tempSP.set(1023);
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", MainMemory.read(tempSP).toString());
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, f, f, t, f", p.getPC().toString());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", MainMemory.read(tempSP).toString());
        p.execute();
        tempSP.set(1022);
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, f, f, t, f", MainMemory.read(tempSP).toString());
    }

    private String addBinary(String a, String b) {
        return "10000";
    }

    private String subtractBinary(String a, String b) {
        return "100";
    }

    @Test
    public void test_processor_execute_call2R() throws Exception {
        // test equals (0000) true
        Processor p = new Processor();
        p.registers[1].set(32);
        p.registers[2].set(32);
        p.currentInstruction = new Word("0010001001101"+"00001"+"0000"+"00010"+"01010");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, f, f, t, f, f, t, t, f, t", p.getPC().toString());
        //test equals (0000) false
        p = new Processor();
        p.registers[1].set(32);
        p.registers[2].set(31);
        p.currentInstruction = new Word("0010001001101"+"00001"+"0000"+"00010"+"01010");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());

        // test not equal (0001) true
        p = new Processor();
        p.registers[1].set(31);
        p.registers[2].set(32);
        p.currentInstruction = new Word("0010001001101"+"00001"+"0001"+"00010"+"01010");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, f, f, t, f, f, t, t, f, t", p.getPC().toString());
        // test not equal (0001) false
        p = new Processor();
        p.registers[1].set(32);
        p.registers[2].set(32);
        p.currentInstruction = new Word("0010001001101"+"00001"+"0001"+"00010"+"01010");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());

        // test less than (0010) true
        p = new Processor();
        p.registers[1].set(31);
        p.registers[2].set(32);
        p.currentInstruction = new Word("0010001001101"+"00001"+"0010"+"00010"+"01010");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, f, f, t, f, f, t, t, f, t", p.getPC().toString());
        // test less than (0010) false
        p = new Processor();
        p.registers[1].set(32);
        p.registers[2].set(32);
        p.currentInstruction = new Word("0010001001101"+"00001"+"0010"+"00010"+"01010");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        p = new Processor();
        p.registers[1].set(33);
        p.registers[2].set(32);
        p.currentInstruction = new Word("0010001001101"+"00001"+"0010"+"00010"+"01010");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());

        // test greater than or equal (0011) true
        p = new Processor();
        p.registers[1].set(33);
        p.registers[2].set(32);
        p.currentInstruction = new Word("0010001001101"+"00001"+"0011"+"00010"+"01010");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, f, f, t, f, f, t, t, f, t", p.getPC().toString());
        p = new Processor();
        p.registers[1].set(32);
        p.registers[2].set(32);
        p.currentInstruction = new Word("0010001001101"+"00001"+"0011"+"00010"+"01010");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, f, f, t, f, f, t, t, f, t", p.getPC().toString());
        // test greater than or equal (0011) false
        p = new Processor();
        p.registers[1].set(31);
        p.registers[2].set(32);
        p.currentInstruction = new Word("0010001001101"+"00001"+"0011"+"00010"+"01010");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());

        // test greater than (0100) true
        p = new Processor();
        p.registers[1].set(33);
        p.registers[2].set(32);
        p.currentInstruction = new Word("0010001001101"+"00001"+"0100"+"00010"+"01010");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, f, f, t, f, f, t, t, f, t", p.getPC().toString());
        // test greater than (0100) false
        p = new Processor();
        p.registers[1].set(32);
        p.registers[2].set(32);
        p.currentInstruction = new Word("0010001001101"+"00001"+"0100"+"00010"+"01010");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        p = new Processor();
        p.registers[1].set(31);
        p.registers[2].set(32);
        p.currentInstruction = new Word("0010001001101"+"00001"+"0100"+"00010"+"01010");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());

        // test less than or equal (0101) true
        p = new Processor();
        p.registers[1].set(31);
        p.registers[2].set(32);
        p.currentInstruction = new Word("0010001001101"+"00001"+"0101"+"00010"+"01010");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, f, f, t, f, f, t, t, f, t", p.getPC().toString());
        p = new Processor();
        p.registers[1].set(32);
        p.registers[2].set(32);
        p.currentInstruction = new Word("0010001001101"+"00001"+"0101"+"00010"+"01010");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, f, f, t, f, f, t, t, f, t", p.getPC().toString());
        // test less than or equal (0101) false
        p = new Processor();
        p.registers[1].set(33);
        p.registers[2].set(32);
        p.currentInstruction = new Word("0010001001101"+"00001"+"0101"+"00010"+"01010");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
    }

    @Test
    public void test_processor_execute_call3R() throws Exception {
        // test equals (0000) true
        Processor p = new Processor();
        p.registers[1].set(32);
        p.registers[2].set(32);
        p.currentInstruction = new Word("00101001"+"00001"+"00010"+"0000"+"11000"+"01011");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, t, f, f, t", p.getPC().toString());
        // test equals (0000) false
        p = new Processor();
        p.registers[1].set(32);
        p.registers[2].set(31);
        p.currentInstruction = new Word("00101001"+"00001"+"00010"+"0000"+"11000"+"01011");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());

        // test not equal (0001) true
        p = new Processor();
        p.registers[1].set(31);
        p.registers[2].set(32);
        p.currentInstruction = new Word("00101001"+"00001"+"00010"+"0001"+"11000"+"01011");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, t, f, f, t", p.getPC().toString());
        // test not equal (0001) false
        p = new Processor();
        p.registers[1].set(32);
        p.registers[2].set(32);
        p.currentInstruction = new Word("00101001"+"00001"+"00010"+"0001"+"11000"+"01011");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());

        // test less than (0010) true
        p = new Processor();
        p.registers[1].set(31);
        p.registers[2].set(32);
        p.currentInstruction = new Word("00101001"+"00001"+"00010"+"0010"+"11000"+"01011");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, t, f, f, t", p.getPC().toString());
        // test less than (0010) false
        p = new Processor();
        p.registers[1].set(32);
        p.registers[2].set(32);
        p.currentInstruction = new Word("00101001"+"00001"+"00010"+"0010"+"11000"+"01011");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        p = new Processor();
        p.registers[1].set(33);
        p.registers[2].set(32);
        p.currentInstruction = new Word("00101001"+"00001"+"00010"+"0010"+"11000"+"01011");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());

        // test greater than or equal (0011) true
        p = new Processor();
        p.registers[1].set(32);
        p.registers[2].set(32);
        p.currentInstruction = new Word("00101001"+"00001"+"00010"+"0011"+"11000"+"01011");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, t, f, f, t", p.getPC().toString());
        p = new Processor();
        p.registers[1].set(33);
        p.registers[2].set(32);
        p.currentInstruction = new Word("00101001"+"00001"+"00010"+"0011"+"11000"+"01011");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, t, f, f, t", p.getPC().toString());
        // test greater than or equal (0011) false
        p = new Processor();
        p.registers[1].set(31);
        p.registers[2].set(32);
        p.currentInstruction = new Word("00101001"+"00001"+"00010"+"0011"+"11000"+"01011");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());

        // test greater than (0100) true
        p = new Processor();
        p.registers[1].set(33);
        p.registers[2].set(32);
        p.currentInstruction = new Word("00101001"+"00001"+"00010"+"0100"+"11000"+"01011");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, t, f, f, t", p.getPC().toString());
        // test greater than (0100) false
        p = new Processor();
        p.registers[1].set(32);
        p.registers[2].set(32);
        p.currentInstruction = new Word("00101001"+"00001"+"00010"+"0100"+"11000"+"01011");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        p = new Processor();
        p.registers[1].set(31);
        p.registers[2].set(32);
        p.currentInstruction = new Word("00101001"+"00001"+"00010"+"0100"+"11000"+"01011");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());

        // test less than or equal (0101) true
        p = new Processor();
        p.registers[1].set(31);
        p.registers[2].set(32);
        p.currentInstruction = new Word("00101001"+"00001"+"00010"+"0101"+"11000"+"01011");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, t, f, f, t", p.getPC().toString());
        p = new Processor();
        p.registers[1].set(32);
        p.registers[2].set(32);
        p.currentInstruction = new Word("00101001"+"00001"+"00010"+"0101"+"11000"+"01011");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, t, f, f, t", p.getPC().toString());
        // test less than or equal (0101) false
        p = new Processor();
        p.registers[1].set(33);
        p.registers[2].set(32);
        p.currentInstruction = new Word("00101001"+"00001"+"00010"+"0101"+"11000"+"01011");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
    }

    @Test
    public void test_word_decrement() {
        Word word = new Word();
        word.set(12);
        word.copy(word.decrement());
        assertEquals(word.getSigned(), 11);

        word = new Word();
        word.set(0);
        word.copy(word.decrement());
        assertEquals(word.getSigned(), -1);

        word = new Word();
        word.set(-12);
        word.copy(word.decrement());
        assertEquals(word.getSigned(), -13);
    }

    @Test
    public void test_processor_execute_pushDestOnly() throws Exception {
        Processor p = new Processor();
        p.registers[1].set(32);
        p.currentInstruction = new Word("000000000000000010"+"1110"+"00001"+"01101");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, f, f, f, t", MainMemory.read(p.getSP().decrement()).toString());
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, f, f, t, f", MainMemory.read(p.getSP()).toString());
    }

    @Test
    public void test_processor_execute_push2R() throws Exception {
        Processor p = new Processor();
        p.registers[1].set(1);
        p.registers[2].set(32);
        p.currentInstruction = new Word("0000000000010"+"00001"+"1110"+"00010"+"01110");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, f, f, f, t", MainMemory.read(p.getSP().decrement()).toString());
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, f, f, f, t", MainMemory.read(p.getSP()).toString());
    }

    @Test
    public void test_processor_execute_push3R() throws Exception {
        Processor p = new Processor();
        p.registers[1].set(1);
        p.registers[2].set(32);
        p.currentInstruction = new Word("00000010"+"00001"+"00010"+"1110"+"11000"+"01111");
        p.decode();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, f, f, f, t", MainMemory.read(p.getSP().decrement()).toString());
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, f, f, f, t", MainMemory.read(p.getSP()).toString());
    }

    @Test
    public void test_processor_execute_loadNoR() throws Exception {
        Processor p = new Processor();
        p.registers[1].set(1);
        p.registers[2].set(32);
        p.currentInstruction = new Word("00000010"+"00001"+"00010"+"1110"+"11000"+"01111");
        p.decode();
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, f, f, f, t", MainMemory.read(p.getSP()).toString());
        p.currentInstruction = new Word("010010010111000000011010010"+"10000");
        p.decode();
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, f, f, f, t", p.getPC().toString());
    }

    private String addBinary2(String a, String b) {
        return "00000000000000000000000000010110";
    }

    private String subtractBinary2(String a, String b) {
        return "11111111111111111111111111111010";
    }

    @Test
    public void test_processor_execute_loadDestOnly() throws Exception {
        Processor p = new Processor();
        p.registers[1].set(1);
        p.registers[2].set(32);
        p.currentInstruction = new Word("00000010"+"00001"+"00010"+"1110"+"11000"+"01111");
        p.decode();
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, f, f, f, t", MainMemory.read(p.getSP()).toString());
        p.registers[3].set(1022);
        p.currentInstruction = new Word("000000000000000001"+"1110"+"00011"+"10001");
        p.decode();
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, f, f, f, t", p.instructions.get("rd").toString());
    }

    @Test
    public void test_processor_execute_load2R() throws Exception {
        Processor p = new Processor();
        p.registers[1].set(1);
        p.registers[2].set(32);
        p.currentInstruction = new Word("00000010"+"00001"+"00010"+"1110"+"11000"+"01111");
        p.decode();
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, f, f, f, t", MainMemory.read(p.getSP()).toString());
        p.registers[3].set(1022);
        p.currentInstruction = new Word("0000000000001"+"00011"+"1110"+"00010"+"10010");
        p.decode();
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, f, f, f, t", p.instructions.get("rd").toString());
    }

    @Test
    public void test_processor_execute_load3R() throws Exception {
        Processor p = new Processor();
        p.registers[1].set(1);
        p.registers[2].set(32);
        p.currentInstruction = new Word("00000010"+"00001"+"00010"+"1110"+"11000"+"01111");
        p.decode();
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f", p.getPC().toString());
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, f, f, f, t", MainMemory.read(p.getSP()).toString());
        p.registers[3].set(1022);
        p.registers[4].set(1);
        p.currentInstruction = new Word("00000010"+"00011"+"00100"+"1110"+"11000"+"10011");
        p.decode();
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, f, f, f, t", p.instructions.get("rd").toString());
    }

    @Test
    public void test_processor_execute_store_DestOnly() throws Exception {
        Processor p = new Processor();
        p.registers[3].set(1022);
        p.currentInstruction = new Word("000000000000100001"+"1110"+"00011"+"10101");
        p.decode();
        p.execute();
        Word tempSP = new Word();
        tempSP.set(1022);
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, f, f, f, t", MainMemory.read(tempSP).toString());
    }

    @Test
    public void test_processor_execute_store_2R() throws Exception {
        Processor p = new Processor();
        p.registers[3].set(33);
        p.registers[4].set(1021);
        p.currentInstruction = new Word("0000000000001"+"00011"+"1110"+"00100"+"10110");
        p.decode();
        p.execute();
        Word tempSP = new Word();
        tempSP.set(1022);
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, f, f, f, t", MainMemory.read(tempSP).toString());
    }

    @Test
    public void test_processor_execute_store_3R() throws Exception {
        Processor p = new Processor();
        p.registers[2].set(33);
        p.registers[3].set(1022);
        p.currentInstruction = new Word("00000010"+"00011"+"00010"+"1110"+"00100"+"10111");
        p.decode();
        p.execute();
        Word tempSP = new Word();
        tempSP.set(1022);
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, f, f, f, t", MainMemory.read(tempSP).toString());
    }

    @Test
    public void test_processor_execute_popDestOnly() throws Exception {
        Processor p = new Processor();
        p.registers[3].set(1023);
        p.currentInstruction = new Word("000000000000100001"+"1110"+"00011"+"10101");
        p.decode();
        p.execute();
        p.currentInstruction = new Word("000000000000100001"+"1110"+"00011"+"11001");
        p.decode();
        p.SP.copy(p.SP.decrement());
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, f, f, f, t", p.instructions.get("rd").toString());
    }

    @Test
    public void test_processor_execute_pop2R() throws Exception {
        Processor p = new Processor();
        p.registers[3].set(1023);
        p.currentInstruction = new Word("000000000000100001"+"1110"+"00011"+"10101");
        p.decode();
        p.execute();
        p.registers[3].set(1);
        p.currentInstruction = new Word("0000000010001"+"00011"+"1110"+"00100"+"11010");
        p.decode();
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, t, t, t, t, f, t, t, t, f", p.instructions.get("rd").toString());
        p.registers[3].set(2);
        p.currentInstruction = new Word("0000000000001"+"00011"+"1110"+"00100"+"11010");
        p.decode();
        p.execute();
        assertEquals("f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, t, t, t, t, t, t, t, f, t", p.instructions.get("rd").toString());
    }

    @Test
    public void test_processor_execute_pop3R() throws Exception {
        Processor p = new Processor();
        p.registers[3].set(1023);
        p.currentInstruction = new Word("000000000000100001"+"1110"+"00011"+"10101");
        p.decode();
        p.execute();
        p.registers[2].set(33);
        p.registers[3].set(1022);
        p.currentInstruction = new Word("00000010"+"00011"+"00010"+"1110"+"00100"+"11011");
        p.decode();
        p.execute();
        assertEquals("t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, f, f, f, f, t", p.instructions.get("rd").toString());
    }


    // ----- TESTS FOR LEXER AND PARSER (ASSIGNMENT 6: Assembler) CLASSES -----
    @Test
    public void test_lexer() throws Exception {
        String str = "math add R1 R2 R3";
        Lexer lexer = new Lexer(str);
        LinkedList<Token> lex = lexer.Lex();
        assertEquals("[MATH , ADD , REGISTER(1) , REGISTER(2) , REGISTER(3) ]", lex.toString());

        str = "math";
        lexer = new Lexer(str);
        lex = lexer.Lex();
        assertEquals("[MATH ]", lex.toString());

        str = "add";
        lexer = new Lexer(str);
        lex = lexer.Lex();
        assertEquals("[ADD ]", lex.toString());

        str = "subtract";
        lexer = new Lexer(str);
        lex = lexer.Lex();
        assertEquals("[SUBTRACT ]", lex.toString());

        str = "multiply";
        lexer = new Lexer(str);
        lex = lexer.Lex();
        assertEquals("[MULTIPLY ]", lex.toString());

        str = "and";
        lexer = new Lexer(str);
        lex = lexer.Lex();
        assertEquals("[AND ]", lex.toString());

        str = "or";
        lexer = new Lexer(str);
        lex = lexer.Lex();
        assertEquals("[OR ]", lex.toString());

        str = "not";
        lexer = new Lexer(str);
        lex = lexer.Lex();
        assertEquals("[NOT ]", lex.toString());

        str = "xor";
        lexer = new Lexer(str);
        lex = lexer.Lex();
        assertEquals("[XOR ]", lex.toString());

        str = "copy";
        lexer = new Lexer(str);
        lex = lexer.Lex();
        assertEquals("[COPY ]", lex.toString());

        str = "halt";
        lexer = new Lexer(str);
        lex = lexer.Lex();
        assertEquals("[HALT ]", lex.toString());

        str = "branch";
        lexer = new Lexer(str);
        lex = lexer.Lex();
        assertEquals("[BRANCH ]", lex.toString());

        str = "jump";
        lexer = new Lexer(str);
        lex = lexer.Lex();
        assertEquals("[JUMP ]", lex.toString());

        str = "call";
        lexer = new Lexer(str);
        lex = lexer.Lex();
        assertEquals("[CALL ]", lex.toString());

        str = "push";
        lexer = new Lexer(str);
        lex = lexer.Lex();
        assertEquals("[PUSH ]", lex.toString());

        str = "load";
        lexer = new Lexer(str);
        lex = lexer.Lex();
        assertEquals("[LOAD ]", lex.toString());

        str = "return";
        lexer = new Lexer(str);
        lex = lexer.Lex();
        assertEquals("[RETURN ]", lex.toString());

        str = "store";
        lexer = new Lexer(str);
        lex = lexer.Lex();
        assertEquals("[STORE ]", lex.toString());

        str = "peek";
        lexer = new Lexer(str);
        lex = lexer.Lex();
        assertEquals("[PEEK ]", lex.toString());

        str = "pop";
        lexer = new Lexer(str);
        lex = lexer.Lex();
        assertEquals("[POP ]", lex.toString());

        str = "interrupt";
        lexer = new Lexer(str);
        lex = lexer.Lex();
        assertEquals("[INTERRUPT ]", lex.toString());

        str = "equal";
        lexer = new Lexer(str);
        lex = lexer.Lex();
        assertEquals("[EQUAL ]", lex.toString());

        str = "unequal";
        lexer = new Lexer(str);
        lex = lexer.Lex();
        assertEquals("[UNEQUAL ]", lex.toString());

        str = "greater";
        lexer = new Lexer(str);
        lex = lexer.Lex();
        assertEquals("[GREATER ]", lex.toString());

        str = "less";
        lexer = new Lexer(str);
        lex = lexer.Lex();
        assertEquals("[LESS ]", lex.toString());

        str = "greaterOrEqual";
        lexer = new Lexer(str);
        lex = lexer.Lex();
        assertEquals("[GREATEROREQUAL ]", lex.toString());

        str = "lessOrEqual";
        lexer = new Lexer(str);
        lex = lexer.Lex();
        assertEquals("[LESSOREQUAL ]", lex.toString());

        str = "shift";
        lexer = new Lexer(str);
        lex = lexer.Lex();
        assertEquals("[SHIFT ]", lex.toString());

        str = "left";
        lexer = new Lexer(str);
        lex = lexer.Lex();
        assertEquals("[LEFT ]", lex.toString());

        str = "right";
        lexer = new Lexer(str);
        lex = lexer.Lex();
        assertEquals("[RIGHT ]", lex.toString());

        str = "1";
        lexer = new Lexer(str);
        lex = lexer.Lex();
        assertEquals("[NUMBER(1) ]", lex.toString());

        str = "R125";
        lexer = new Lexer(str);
        lex = lexer.Lex();
        assertEquals("[REGISTER(125) ]", lex.toString());

        str = "\n";
        lexer = new Lexer(str);
        lex = lexer.Lex();
        assertEquals("[NEWLINE ]", lex.toString());

        str = "\r";
        lexer = new Lexer(str);
        lex = lexer.Lex();
        assertEquals("[SLASHR ]", lex.toString());

        str = "R1 \n";
        lexer = new Lexer(str);
        lex = lexer.Lex();
        assertEquals("[REGISTER(1) , NEWLINE ]", lex.toString());

        str = "R1 1002 \n";
        lexer = new Lexer(str);
        lex = lexer.Lex();
        assertEquals("[REGISTER(1) , NUMBER(1002) , NEWLINE ]", lex.toString());
    }

    @Test
    public void test_parser_math() throws Exception {
        String str = "add R1 R2";
        Lexer lexer = new Lexer(str);
        LinkedList<Token> lex = lexer.Lex();
        assertEquals("[ADD , REGISTER(1) , REGISTER(2) ]", lex.toString());
        Parser par = new Parser(lex);
        assertEquals("0000000000000"+"00001"+"1110"+"00010"+"00010", par.ParseMath());

        str = "add R1 R2 R3";
        lexer = new Lexer(str);
        lex = lexer.Lex();
        assertEquals("[ADD , REGISTER(1) , REGISTER(2) , REGISTER(3) ]", lex.toString());
        par = new Parser(lex);
        assertEquals("00000000"+"00001"+"00010"+"1110"+"00011"+"00011", par.ParseMath());
    }

    @Test
    public void test_parser_branch() throws Exception {
        String str = "1";
        Lexer lexer = new Lexer(str);
        LinkedList<Token> lex = lexer.Lex();
        assertEquals("[NUMBER(1) ]", lex.toString());
        Parser par = new Parser(lex);
        assertEquals("000000000000000000000000001"+"00100", par.ParseBranch());

        str = "1";
        lexer = new Lexer(str);
        lex = lexer.Lex();
        assertEquals("[NUMBER(1) ]", lex.toString());
        par = new Parser(lex);
        assertEquals("000000000000000000"+"0000"+"00001"+"00100", par.ParseBranch());

        str = "equal R1 R2 3";
        lexer = new Lexer(str);
        lex = lexer.Lex();
        assertEquals("[EQUAL , REGISTER(1) , REGISTER(2) , NUMBER(3) ]", lex.toString());
        par = new Parser(lex);
        assertEquals("0000000000011"+"00001"+"0000"+"00010"+"00110", par.ParseBranch());

        str = "branch 1";
        lexer = new Lexer(str);
        lex = lexer.Lex();
        assertEquals("[BRANCH , NUMBER(1) ]", lex.toString());
        par = new Parser(lex);
        assertEquals("000000000000000000000000001"+"00100", par.ParseStatement());

        str = "branch 1";
        lexer = new Lexer(str);
        lex = lexer.Lex();
        assertEquals("[BRANCH , NUMBER(1) ]", lex.toString());
        par = new Parser(lex);
        assertEquals("000000000000000000"+"0000"+"00001"+"00100", par.ParseStatement());

        str = "branch equal R1 R2 3";
        lexer = new Lexer(str);
        lex = lexer.Lex();
        assertEquals("[BRANCH , EQUAL , REGISTER(1) , REGISTER(2) , NUMBER(3) ]", lex.toString());
        par = new Parser(lex);
        assertEquals("0000000000011"+"00001"+"0000"+"00010"+"00110", par.ParseStatement());
    }

    @Test
    public void test_parser_halt() throws Exception {
        String str = "halt";
        Lexer lexer = new Lexer(str);
        LinkedList<Token> lex = lexer.Lex();
        assertEquals("[HALT ]", lex.toString());
        Parser par = new Parser(lex);
        assertEquals("00000", par.ParseHalt());

        str = "halt";
        lexer = new Lexer(str);
        lex = lexer.Lex();
        assertEquals("[HALT ]", lex.toString());
        par = new Parser(lex);
        assertEquals("000000000000000000000000000"+"00000", par.ParseStatement());
    }

    @Test
    public void test_parser_copy() throws Exception {
        String str = "R1 3";
        Lexer lexer = new Lexer(str);
        LinkedList<Token> lex = lexer.Lex();
        assertEquals("[REGISTER(1) , NUMBER(3) ]", lex.toString());
        Parser par = new Parser(lex);
        assertEquals("[00001, 000000000000000011]", Arrays.toString(par.ParseCopy()));

        str = "R1 3";
        lexer = new Lexer(str);
        lex = lexer.Lex();
        assertEquals("[REGISTER(1) , NUMBER(3) ]", lex.toString());
        par = new Parser(lex);
        assertEquals("000000000000000011"+"0000"+"00001"+"00001", par.ParseStatementCopy());

        str = "copy R1 3";
        lexer = new Lexer(str);
        lex = lexer.Lex();
        assertEquals("[COPY , REGISTER(1) , NUMBER(3) ]", lex.toString());
        par = new Parser(lex);
        assertEquals("000000000000000011"+"0000"+"00001"+"00001", par.ParseStatement());
    }

    @Test
    public void test_parser_jump() throws Exception {
        String str = "1";
        Lexer lexer = new Lexer(str);
        LinkedList<Token> lex = lexer.Lex();
        assertEquals("[NUMBER(1) ]", lex.toString());
        Parser par = new Parser(lex);
        assertEquals("000000000000000000000000001"+"00100", par.ParseJump());

        str = "1";
        lexer = new Lexer(str);
        lex = lexer.Lex();
        assertEquals("[NUMBER(1) ]", lex.toString());
        par = new Parser(lex);
        assertEquals("000000000000000000"+"0000"+"00001"+"00100", par.ParseJump());

        str = "jump 1";
        lexer = new Lexer(str);
        lex = lexer.Lex();
        assertEquals("[JUMP , NUMBER(1) ]", lex.toString());
        par = new Parser(lex);
        assertEquals("000000000000000000000000001"+"00100", par.ParseStatement());

        str = "jump 1";
        lexer = new Lexer(str);
        lex = lexer.Lex();
        assertEquals("[JUMP , NUMBER(1) ]", lex.toString());
        par = new Parser(lex);
        assertEquals("000000000000000000"+"0000"+"00001"+"00100", par.ParseStatement());
    }

    @Test
    public void test_parser_call() throws Exception {
        String str = "1";
        Lexer lexer = new Lexer(str);
        LinkedList<Token> lex = lexer.Lex();
        assertEquals("[NUMBER(1) ]", lex.toString());
        Parser par = new Parser(lex);
        assertEquals("000000000000000000000000001"+"01000", par.ParseCall());

        str = "R1 1";
        lexer = new Lexer(str);
        lex = lexer.Lex();
        assertEquals("[REGISTER(1) , NUMBER(1) ]", lex.toString());
        par = new Parser(lex);
        assertEquals("000000000000000001"+"0000"+"00001"+"01001", par.ParseCall());

        str = "unequal R1 R2 1";
        lexer = new Lexer(str);
        lex = lexer.Lex();
        assertEquals("[UNEQUAL , REGISTER(1) , REGISTER(2) , NUMBER(1) ]", lex.toString());
        par = new Parser(lex);
        assertEquals("0000000000001"+"00001"+"0001"+"00010"+"01010", par.ParseCall());

        str = "unequal R1 R2 R3 1";
        lexer = new Lexer(str);
        lex = lexer.Lex();
        assertEquals("[UNEQUAL , REGISTER(1) , REGISTER(2) , REGISTER(3) , NUMBER(1) ]", lex.toString());
        par = new Parser(lex);
        assertEquals("00000001"+"00001"+"00010"+"0001"+"00011"+"01011", par.ParseCall());

        str = "call 1";
        lexer = new Lexer(str);
        lex = lexer.Lex();
        assertEquals("[CALL , NUMBER(1) ]", lex.toString());
        par = new Parser(lex);
        assertEquals("000000000000000000000000001"+"01000", par.ParseStatement());

        str = "call R1 1";
        lexer = new Lexer(str);
        lex = lexer.Lex();
        assertEquals("[CALL , REGISTER(1) , NUMBER(1) ]", lex.toString());
        par = new Parser(lex);
        assertEquals("000000000000000001"+"0000"+"00001"+"01001", par.ParseStatement());

        str = "call unequal R1 R2 1";
        lexer = new Lexer(str);
        lex = lexer.Lex();
        assertEquals("[CALL , UNEQUAL , REGISTER(1) , REGISTER(2) , NUMBER(1) ]", lex.toString());
        par = new Parser(lex);
        assertEquals("0000000000001"+"00001"+"0001"+"00010"+"01010", par.ParseStatement());

        str = "call unequal R1 R2 R3 1";
        lexer = new Lexer(str);
        lex = lexer.Lex();
        assertEquals("[CALL , UNEQUAL , REGISTER(1) , REGISTER(2) , REGISTER(3) , NUMBER(1) ]", lex.toString());
        par = new Parser(lex);
        assertEquals("00000001"+"00001"+"00010"+"0001"+"00011"+"01011", par.ParseStatement());
    }

    @Test
    public void test_parser_push() throws Exception {
        String str = "add R1 1";
        Lexer lexer = new Lexer(str);
        LinkedList<Token> lex = lexer.Lex();
        assertEquals("[ADD , REGISTER(1) , NUMBER(1) ]", lex.toString());
        Parser par = new Parser(lex);
        assertEquals("000000000000000001"+"1110"+"00001"+"01101", par.ParsePush());
    }

    @Test
    public void test_parser_pop() throws Exception {
        // Rd  mem[sp++]
        String str = "R1";
        Lexer lexer = new Lexer(str);
        LinkedList<Token> lex = lexer.Lex();
        assertEquals("[REGISTER(1) ]", lex.toString());
        Parser par = new Parser(lex);
        assertEquals("000000000000000000"+"0000"+"00001"+"11001", par.ParsePop());

        str = "pop R1";
        lexer = new Lexer(str);
        lex = lexer.Lex();
        assertEquals("[POP , REGISTER(1) ]", lex.toString());
        par = new Parser(lex);
        assertEquals("000000000000000000"+"0000"+"00001"+"11001", par.ParseStatement());
    }

    @Test
    public void test_parser_load() throws Exception {
        String str = "R1";
        Lexer lexer = new Lexer(str);
        LinkedList<Token> lex = lexer.Lex();
        assertEquals("[REGISTER(1) ]", lex.toString());
        Parser par = new Parser(lex);
        assertEquals("0000000000000000000000"+"00001"+"10000", par.ParseLoad());

        str = "R1 1";
        lexer = new Lexer(str);
        lex = lexer.Lex();
        assertEquals("[REGISTER(1) , NUMBER(1) ]", lex.toString());
        par = new Parser(lex);
        assertEquals("000000000000000001"+"0000"+"00001"+"10001", par.ParseLoad());

        str = "R1 R2 3";
        lexer = new Lexer(str);
        lex = lexer.Lex();
        assertEquals("[REGISTER(1) , REGISTER(2) , NUMBER(3) ]", lex.toString());
        par = new Parser(lex);
        assertEquals("0000000000011"+"00001"+"0000"+"00010"+"10010", par.ParseLoad());
    }

    @Test
    public void test_parser_store() throws Exception {
        String str = "R1 1";
        Lexer lexer = new Lexer(str);
        LinkedList<Token> lex = lexer.Lex();
        assertEquals("[REGISTER(1) , NUMBER(1) ]", lex.toString());
        Parser par = new Parser(lex);
        assertEquals("000000000000000001"+"0000"+"00001"+"10101", par.ParseStore());

        str = "R1 R2 1";
        lexer = new Lexer(str);
        lex = lexer.Lex();
        assertEquals("[REGISTER(1) , REGISTER(2) , NUMBER(1) ]", lex.toString());
        par = new Parser(lex);
        assertEquals("0000000000001"+"00001"+"0000"+"00010"+"10110", par.ParseStore());

        str = "store R1 1";
        lexer = new Lexer(str);
        lex = lexer.Lex();
        assertEquals("[STORE , REGISTER(1) , NUMBER(1) ]", lex.toString());
        par = new Parser(lex);
        assertEquals("000000000000000001"+"0000"+"00001"+"10101", par.ParseStatement());

        str = "store R1 R2 1";
        lexer = new Lexer(str);
        lex = lexer.Lex();
        assertEquals("[STORE , REGISTER(1) , REGISTER(2) , NUMBER(1) ]", lex.toString());
        par = new Parser(lex);
        assertEquals("0000000000001"+"00001"+"0000"+"00010"+"10110", par.ParseStatement());
    }

    @Test
    public void test_parser_return() throws Exception {
        String str = "R1";
        Lexer lexer = new Lexer(str);
        LinkedList<Token> lex = lexer.Lex();
        assertEquals("[REGISTER(1) ]", lex.toString());
        Parser par = new Parser(lex);
        assertEquals("000000000000000000"+"0000"+"00001"+"11001", par.ParseReturn());

        str = "return R1";
        lexer = new Lexer(str);
        lex = lexer.Lex();
        assertEquals("[RETURN , REGISTER(1) ]", lex.toString());
        par = new Parser(lex);
        assertEquals("000000000000000000"+"0000"+"00001"+"11001", par.ParseStatement());
    }

    @Test
    public void test_parser_peek() throws Exception {
        String str = "R1 R2 3";
        Lexer lexer = new Lexer(str);
        LinkedList<Token> lex = lexer.Lex();
        assertEquals("[REGISTER(1) , REGISTER(2) , NUMBER(3) ]", lex.toString());
        Parser par = new Parser(lex);
        assertEquals("0000000000011"+"00001"+"0000"+"00010"+"11010", par.ParsePeek());

        str = "R1 R2 R3 1";
        lexer = new Lexer(str);
        lex = lexer.Lex();
        assertEquals("[REGISTER(1) , REGISTER(2) , REGISTER(3) , NUMBER(1) ]", lex.toString());
        par = new Parser(lex);
        assertEquals("00000001"+"00001"+"00010"+"0000"+"00011"+"11011", par.ParsePeek());

        str = "peek R1 R2 3";
        lexer = new Lexer(str);
        lex = lexer.Lex();
        assertEquals("[PEEK , REGISTER(1) , REGISTER(2) , NUMBER(3) ]", lex.toString());
        par = new Parser(lex);
        assertEquals("0000000000011"+"00001"+"0000"+"00010"+"11010", par.ParseStatement());

        str = "peek R1 R2 R3 1";
        lexer = new Lexer(str);
        lex = lexer.Lex();
        assertEquals("[PEEK , REGISTER(1) , REGISTER(2) , REGISTER(3) , NUMBER(1) ]", lex.toString());
        par = new Parser(lex);
        assertEquals("00000001"+"00001"+"00010"+"0000"+"00011"+"11011", par.ParseStatement());
    }

    @Test
    public void test_parser_interrupt() throws Exception {
        String str = "interrupt";
        Lexer lexer = new Lexer(str);
        LinkedList<Token> lex = lexer.Lex();
        assertEquals("[INTERRUPT ]", lex.toString());
        Parser par = new Parser(lex);
        assertThrows(Exception.class, par::ParseInterrupt);
    }

    @Test
    public void test_parser_shift() throws Exception {
        String str = "R1 R2";
        Lexer lexer = new Lexer(str);
        LinkedList<Token> lex = lexer.Lex();
        assertEquals("[REGISTER(1) , REGISTER(2) ]", lex.toString());
        Parser par = new Parser(lex);
        assertEquals("[00001, 00010]", Arrays.toString(par.ParseTwoR()));
    }

    @Test
    public void test_parser_twoR() throws Exception {
        String str = "R1 R2";
        Lexer lexer = new Lexer(str);
        LinkedList<Token> lex = lexer.Lex();
        assertEquals("[REGISTER(1) , REGISTER(2) ]", lex.toString());
        Parser par = new Parser(lex);
        assertEquals("[00001, 00010]", Arrays.toString(par.ParseTwoR()));
    }

    @Test
    public void test_parser_threeR() throws Exception {
        String str = "R1 R2 R3";
        Lexer lexer = new Lexer(str);
        LinkedList<Token> lex = lexer.Lex();
        assertEquals("[REGISTER(1) , REGISTER(2) , REGISTER(3) ]", lex.toString());
        Parser par = new Parser(lex);
        assertEquals("[00001, 00010, 00011]", Arrays.toString(par.ParseThreeR()));
    }

    @Test
    public void test_parser_destOnly() throws Exception {
        String str = "R1 3";
        Lexer lexer = new Lexer(str);
        LinkedList<Token> lex = lexer.Lex();
        assertEquals("[REGISTER(1) , NUMBER(3) ]", lex.toString());
        Parser par = new Parser(lex);
        assertEquals("[00001, 000000000000000011]", Arrays.toString(par.ParseDestOnly()));
    }

    @Test
    public void test_parser_noR() throws Exception {
        String str = "1";
        Lexer lexer = new Lexer(str);
        LinkedList<Token> lex = lexer.Lex();
        assertEquals("[NUMBER(1) ]", lex.toString());
        Parser par = new Parser(lex);
        assertEquals("000000000000000000000000001"+"00000", par.ParseNoR());
    }


    // ----- TESTS FOR CACHE (ASSIGNMENT 7: Cache) CLASSES -----
    @Test
    public void test_cache_test1() throws Exception {
        String str = "store R1 1\n" +
                "store R2 2\n" +
                "store R3 3\n" +
                "store R4 4\n" +
                "store R5 5\n" +
                "store R6 1\n" +
                "store R7 2\n" +
                "store R8 3\n" +
                "store R9 4\n" +
                "store R10 35\n" +
                "store R11 45\n" +
                "store R12 34\n" +
                "store R13 4\n" +
                "store R14 234\n" +
                "store R15 66\n" +
                "store R16 234\n" +
                "store R17 1112\n" +
                "store R18 909\n" +
                "store R19 324\n" +
                "store R20 677\n" +
                "load R0 R2 200\n" +
                "math add R2 R1\n" +
                "load R0 R2 201\n" +
                "math add R2 R1\n" +
                "load R0 R2 202\n" +
                "math add R2 R1\n" +
                "load R0 R2 203\n" +
                "math add R2 R1\n" +
                "load R0 R2 204\n" +
                "math add R2 R1\n" +
                "load R0 R2 205\n" +
                "math add R2 R1\n" +
                "load R0 R2 206\n" +
                "math add R2 R1\n" +
                "load R0 R2 207\n" +
                "math add R2 R1\n" +
                "load R0 R2 208\n" +
                "math add R2 R1\n" +
                "load R0 R2 209\n" +
                "math add R2 R1\n" +
                "load R0 R2 210\n" +
                "math add R2 R1\n" +
                "load R0 R2 211\n" +
                "math add R2 R1\n" +
                "load R0 R2 212\n" +
                "math add R2 R1\n" +
                "load R0 R2 213\n" +
                "math add R2 R1\n" +
                "load R0 R2 214\n" +
                "math add R2 R1\n" +
                "load R0 R2 215\n" +
                "math add R2 R1\n" +
                "load R0 R2 216\n" +
                "math add R2 R1\n" +
                "load R0 R2 217\n" +
                "math add R2 R1\n" +
                "load R0 R2 218\n" +
                "math add R2 R1\n" +
                "load R0 R2 219\n" +
                "math add R2 R1\n" +
                "halt\n";
        Lexer lexer = new Lexer(str);
        LinkedList<Token> lex = lexer.Lex();
        Parser par = new Parser(lex);
        MainMemory.load(par.Parse());
        Processor p = new Processor();
        p.run();
        System.out.println("process cycle: " + p.getCurrentClockCycle());
    }

    @Test
    public void test_cache_test2() throws Exception {
        String str = "store R1 1\n" +
                "store R2 2\n" +
                "store R3 3\n" +
                "store R4 4\n" +
                "store R5 5\n" +
                "store R6 1\n" +
                "store R7 2\n" +
                "store R8 3\n" +
                "store R9 4\n" +
                "store R10 35\n" +
                "store R11 45\n" +
                "store R12 34\n" +
                "store R13 4\n" +
                "store R14 234\n" +
                "store R15 66\n" +
                "store R16 234\n" +
                "store R17 1112\n" +
                "store R18 909\n" +
                "store R19 324\n" +
                "store R20 677\n" +
                "copy R21 R1\n" +
                "copy R22 R2\n" +
                "copy R23 R3\n" +
                "copy R24 R4\n" +
                "copy R25 R5\n" +
                "copy R26 R6\n" +
                "copy R27 R7\n" +
                "copy R28 R8\n" +
                "copy R29 R9\n" +
                "copy R30 R10\n" +
                "copy R31 R11\n" +
                "copy R32 R12\n" +
                "copy R33 R13\n" +
                "copy R34 R14\n" +
                "copy R35 R15\n" +
                "copy R36 R16\n" +
                "copy R37 R17\n" +
                "copy R38 R18\n" +
                "copy R39 R19\n" +
                "copy R30 R20\n" +
                "store R21 R1\n" +
                "store R22 R2\n" +
                "store R23 R3\n" +
                "store R24 R4\n" +
                "store R25 R5\n" +
                "store R26 R6\n" +
                "store R27 R7\n" +
                "store R28 R8\n" +
                "store R29 R9\n" +
                "store R30 R10\n" +
                "store R31 R11\n" +
                "store R32 R12\n" +
                "store R33 R13\n" +
                "store R34 R14\n" +
                "store R35 R15\n" +
                "store R36 R16\n" +
                "store R37 R17\n" +
                "store R38 R18\n" +
                "store R39 R19\n" +
                "store R30 R20\n" +
                "load R0 R2 200\n" +
                "math add R2 R1\n" +
                "load R0 R2 201\n" +
                "math add R2 R1\n" +
                "load R0 R2 202\n" +
                "math add R2 R1\n" +
                "load R0 R2 203\n" +
                "math add R2 R1\n" +
                "load R0 R2 204\n" +
                "math add R2 R1\n" +
                "load R0 R2 205\n" +
                "math add R2 R1\n" +
                "load R0 R2 206\n" +
                "math add R2 R1\n" +
                "load R0 R2 207\n" +
                "math add R2 R1\n" +
                "load R0 R2 208\n" +
                "math add R2 R1\n" +
                "load R0 R2 209\n" +
                "math add R2 R1\n" +
                "load R0 R2 210\n" +
                "math add R2 R1\n" +
                "load R0 R2 211\n" +
                "math add R2 R1\n" +
                "load R0 R2 212\n" +
                "math add R2 R1\n" +
                "load R0 R2 213\n" +
                "math add R2 R1\n" +
                "load R0 R2 214\n" +
                "math add R2 R1\n" +
                "load R0 R2 215\n" +
                "math add R2 R1\n" +
                "load R0 R2 216\n" +
                "math add R2 R1\n" +
                "load R0 R2 217\n" +
                "math add R2 R1\n" +
                "load R0 R2 218\n" +
                "math add R2 R1\n" +
                "load R0 R2 219\n" +
                "math add R2 R1\n" +
                "halt\n";
        Lexer lexer = new Lexer(str);
        LinkedList<Token> lex = lexer.Lex();
        Parser par = new Parser(lex);
        MainMemory.load(par.Parse());
        Processor p = new Processor();
        p.run();
        System.out.println("process cycle: " + p.getCurrentClockCycle());
    }

    @Test
    public void test_cache_test3() throws Exception {
        String str = "store R1 1\n" +
                "store R2 2\n" +
                "store R3 3\n" +
                "store R4 4\n" +
                "store R5 5\n" +
                "store R6 1\n" +
                "store R7 2\n" +
                "store R8 3\n" +
                "store R9 4\n" +
                "store R10 35\n" +
                "store R11 45\n" +
                "store R12 34\n" +
                "store R13 4\n" +
                "store R14 234\n" +
                "store R15 66\n" +
                "store R16 234\n" +
                "store R17 1112\n" +
                "store R18 909\n" +
                "store R19 324\n" +
                "store R20 677\n" +
                "math add R2 R1\n" +
                "load R0 R2 912\n" +
                "math add R2 R1\n" +
                "load R0 R2 912\n" +
                "math add R2 R1\n" +
                "load R0 R2 912\n" +
                "math add R2 R1\n" +
                "load R0 R2 912\n" +
                "math add R2 R1\n" +
                "load R0 R2 912\n" +
                "math add R2 R1\n" +
                "load R0 R2 912\n" +
                "math add R2 R1\n" +
                "load R0 R2 912\n" +
                "math add R2 R1\n" +
                "load R0 R2 912\n" +
                "math add R2 R1\n" +
                "load R0 R2 912\n" +
                "math add R2 R1\n" +
                "load R0 R2 912\n" +
                "math add R2 R1\n" +
                "load R0 R2 912\n" +
                "math add R2 R1\n" +
                "load R0 R2 912\n" +
                "math add R2 R1\n" +
                "load R0 R2 912\n" +
                "math add R2 R1\n" +
                "load R0 R2 912\n" +
                "math add R2 R1\n" +
                "load R0 R2 912\n" +
                "math add R2 R1\n" +
                "load R0 R2 912\n" +
                "math add R2 R1\n" +
                "load R0 R2 912\n" +
                "math add R2 R1\n" +
                "load R0 R2 912\n" +
                "halt\n";
        Lexer lexer = new Lexer(str);
        LinkedList<Token> lex = lexer.Lex();
        Parser par = new Parser(lex);
        MainMemory.load(par.Parse());
        Processor p = new Processor();
        p.run();
        System.out.println("process cycle: " + p.getCurrentClockCycle());
    }
}
