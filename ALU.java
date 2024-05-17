package csi404.assignment7;

public class ALU {
    Word op1;
    Word op2;
    Word result;

    public ALU() {
        op1 = new Word();
        op2 = new Word();
        result = new Word();
    }

    public ALU(Word iop1, Word iop2) {
        op1 = iop1;
        op2 = iop2;
        result = new Word();
    }

    public Word getResult() {
        return result;
    }

    public void doOperation(Bit[] operation) {
        if (!operation[0].getValue()) { // bit[0] == 0
            if (operation[1].getValue()) { // bit[1] == 1
                if (operation[2].getValue()) { // bit[2] == 1
                    if (operation[3].getValue()) { // bit[3] == 1
                        // 0111
                        Word[] arr = new Word[32];
                        int shiftBy = 0;
                        for (int i = op2.getBitArrayLength() - 1; i >= 0; i--) {
                            if (op2.getBit(i).getValue()) { // op2 value is 1 so copy op1
                                Word tempWord = op1.leftShift(shiftBy); // left shift number by specific amount in order to achieve as required in multiplication
                                arr[i] = tempWord;
                            } else {
                                arr[i] = new Word();
                            }
                            shiftBy++;
                        }

                        Word[] eightadd4Result = new Word[8];
                        int count = 31;
                        for (int i = 7; i >= 0; i--) { // using add4 8 times gives us 8 new numbers to work with
                            eightadd4Result[i] = add4(arr[count], arr[count-1], arr[count-2], arr[count-3]);
                            count -= 4;
                        }

                        Word[] fouradd4Result = new Word[2];
                        count = 7;
                        for (int i = 1; i >= 0; i--) { // using add4 4 times gives us 2 new numbers to work with
                            fouradd4Result[i] = add4(eightadd4Result[count], eightadd4Result[count-1], eightadd4Result[count-2], eightadd4Result[count-3]);
                            count -=4;
                        }

                        Word resultWord = new Word();
                        resultWord.copy(add2(fouradd4Result[1], fouradd4Result[0])); // with the two new numbers, we can use add2 to get the result of this multiplication process

                        for (int i = resultWord.getBitArrayLength() - 1; i >= 0; i--) { // setting the bits in result
                            result.setBit(i, resultWord.getBit(i));
                        }
                    }
                }
            }
        } else { // bit[0] == 1
            if (!operation[1].getValue()) { // bit[1] == 0  10
                if (!operation[2].getValue()) { // bit[2] == 0  100
                    if (!operation[3].getValue()) { // bit[3] == 0  1000
                        // 1000
                        result = op1.and(op2);
                    } else { // bit[3] == 1 1001
                        // 1001
                        result = op1.or(op2);
                    }
                } else { // bit[2] == 1 101
                    if (!operation[3].getValue()) { // bit[3] == 0  1010
                        // 1010
                        result = op1.xor(op2);
                    } else { // bit[3] == 1 1011
                        // 1011
                        result = op1.not();
                    }
                }
            } else { // bit[1] == 1
                if (!operation[2].getValue()) { // bit[2] == 0
                    if (!operation[3].getValue()) { // bit[3] == 0
                        // 1100
                        int lShiftNum = 0;
                        int power = 1;
                        for (int i = op2.getBitArrayLength() - 1; i >= op2.getBitArrayLength() - 6; i--) {
                            if (op2.getBit(i).getValue()) {
                                lShiftNum += (int) Math.pow(2, power - 1);
                            }
                            power++;
                        }
                        Word resultWord = op1.leftShift(lShiftNum);
                        for (int j = 0; j < result.getBitArrayLength(); j++) {
                            result.setBit(j, resultWord.getBit(j));
                        }
                    } else { // bit[3] == 1
                        // 1101
                        int rShiftNum = 0;
                        int power = 1;
                        for (int i = op2.getBitArrayLength() - 1; i >= op2.getBitArrayLength() - 5; i--) {
                            if (op2.getBit(i).getValue()) {
                                rShiftNum += (int) Math.pow(2, power - 1);
                            }
                            power++;
                        }
                        Word resultWord = op1.rightShift(rShiftNum);
                        for (int j = 0; j < result.getBitArrayLength(); j++) {
                            result.setBit(j, resultWord.getBit(j));
                        }
                    }
                } else { // bit[2] == 1
                    if (!operation[3].getValue()) { // bit[3] == 0
                        // 1110

                        // s = x xor y xor cin
                        // cout = x and y or ((x xor y) and cin)
                        Word resultWord = add2(op1, op2);
                        for (int i = 0; i < result.getBitArrayLength(); i++) {
                            result.setBit(i, new Bit(resultWord.getBit(i).getValue()));
                        }
                    } else { // bit[3] == 1
                        // 1111

                        // copying op2 bitArray into a new array to use twos complement
                        Bit[] bArray = new Bit[32];
                        for (int i = 0; i < op2.getBitArrayLength(); i++) {
                            bArray[i] = new Bit(op2.getBit(i).getValue());
                        }

                        if (!op2.checkIfZero()) { // if op2 is zero, there is no need to negate the number to subtract
                            // using twos complement on op2 to negate the number
                            for (int j = 0; j < bArray.length; j++) {
                                bArray[j] = bArray[j].not();
                            }
                            int k = bArray.length - 1;
                            while (k >= 0 && bArray[k].getValue()) {
                                bArray[k] = bArray[k].not();
                                k--;
                            }
                            bArray[k] = bArray[k].not(); // adding one
                        }

                        // adding the numbers -> a + -b => op1 + -op2 => op1 + (not(op2) + 1)
                        Word nWord = new Word(bArray);
                        Word resultWord = add2(op1, nWord);
                        for (int i = 0; i < result.getBitArrayLength(); i++) {
                            result.setBit(i, new Bit(resultWord.getBit(i).getValue()));
                        }
                    }
                }
            }
        }
    }

//    public void doOperation(Bit[] operation) {
//        if (!operation[0].getValue()) { // bit[0] == 0
//            if (!operation[1].getValue()) { // bit[1] == 0
//                if (!operation[2].getValue()) { // bit[2] == 0
//                    if (!operation[3].getValue()) { // bit[3] == 0
//                        // 0000
//                    } else { // bit[3] == 1
//                        // 0001
//                    }
//                } else { // bit[2] == 1
//                    if (!operation[3].getValue()) { // bit[3] == 0
//                        // 0010
//                    } else { // bit[3] == 1
//                        // 0011
//                    }
//                }
//            } else { // bit[1] == 1
//                if (!operation[2].getValue()) { // bit[2] == 0
//                    if (!operation[3].getValue()) { // bit[3] == 0
//                        // 0100
//                    } else { // bit[3] == 1
//                        // 0101
//                    }
//                } else { // bit[2] == 1
//                    if (operation[3].getValue()) { // bit[3] == 1
//                        // 0111
//                        Word[] arr = new Word[32];
//                        int shiftBy = 0;
//                        for (int i = op2.getBitArrayLength() - 1; i >= 0; i--) {
//                            if (op2.getBit(i).getValue()) { // op2 value is 1 so copy op1
//                                Word tempWord = op1.leftShift(shiftBy); // left shift number by specific amount in order to achieve as required in multiplication
//                                arr[i] = tempWord;
//                            } else {
//                                arr[i] = new Word();
//                            }
//                            shiftBy++;
//                        }
//
//                        Word[] eightadd4Result = new Word[8];
//                        int count = 31;
//                        for (int i = 7; i >= 0; i--) { // using add4 8 times gives us 8 new numbers to work with
//                            eightadd4Result[i] = add4(arr[count], arr[count-1], arr[count-2], arr[count-3]);
//                            count -= 4;
//                        }
//
//                        Word[] fouradd4Result = new Word[2];
//                        count = 7;
//                        for (int i = 1; i >= 0; i--) { // using add4 4 times gives us 2 new numbers to work with
//                            fouradd4Result[i] = add4(eightadd4Result[count], eightadd4Result[count-1], eightadd4Result[count-2], eightadd4Result[count-3]);
//                            count -=4;
//                        }
//
//                        Word resultWord = new Word();
//                        resultWord.copy(add2(fouradd4Result[1], fouradd4Result[0])); // with the two new numbers, we can use add2 to get the result of this multiplication process
//
//                        for (int i = resultWord.getBitArrayLength() - 1; i >= 0; i--) { // setting the bits in result
//                            result.setBit(i, resultWord.getBit(i));
//                        }
//                    }
//                }
//            }
//        } else if (operation[0].getValue()) { // bit[0] == 1
//            if (!operation[1].getValue()) { // bit[1] == 0  10
//                if (!operation[2].getValue()) { // bit[2] == 0  100
//                    if (!operation[3].getValue()) { // bit[3] == 0  1000
//                        // 1000
//                        result = op1.and(op2);
//                    } else { // bit[3] == 1 1001
//                        // 1001
//                        result = op1.or(op2);
//                    }
//                } else { // bit[2] == 1 101
//                    if (!operation[3].getValue()) { // bit[3] == 0  1010
//                        // 1010
//                        result = op1.xor(op2);
//                    } else { // bit[3] == 1 1011
//                        // 1011
//                        result = op1.not();
//                    }
//                }
//            } else { // bit[1] == 1
//                if (!operation[2].getValue()) { // bit[2] == 0
//                    if (!operation[3].getValue()) { // bit[3] == 0
//                        // 1100
//                        int lShiftNum = 0;
//                        int power = 1;
//                        for (int i = op2.getBitArrayLength() - 1; i >= op2.getBitArrayLength() - 6; i--) {
//                            if (op2.getBit(i).getValue()) {
//                                lShiftNum += (int) Math.pow(2, power - 1);
//                            }
//                            power++;
//                        }
//                        Word resultWord = op1.leftShift(lShiftNum);
//                        for (int j = 0; j < result.getBitArrayLength(); j++) {
//                            result.setBit(j, resultWord.getBit(j));
//                        }
//                    } else { // bit[3] == 1
//                        // 1101
//                        int rShiftNum = 0;
//                        int power = 1;
//                        for (int i = op2.getBitArrayLength() - 1; i >= op2.getBitArrayLength() - 5; i--) {
//                            if (op2.getBit(i).getValue()) {
//                                rShiftNum += (int) Math.pow(2, power - 1);
//                            }
//                            power++;
//                        }
//                        Word resultWord = op1.rightShift(rShiftNum);
//                        for (int j = 0; j < result.getBitArrayLength(); j++) {
//                            result.setBit(j, resultWord.getBit(j));
//                        }
//                    }
//                } else { // bit[2] == 1
//                    if (!operation[3].getValue()) { // bit[3] == 0
//                        // 1110
//
//                        // s = x xor y xor cin
//                        // cout = x and y or ((x xor y) and cin)
//                        Word resultWord = add2(op1, op2);
//                        for (int i = 0; i < result.getBitArrayLength(); i++) {
//                            result.setBit(i, new Bit(resultWord.getBit(i).getValue()));
//                        }
//                    } else { // bit[3] == 1
//                        // 1111
//
//                        // copying op2 bitArray into a new array to use twos complement
//                        Bit[] bArray = new Bit[32];
//                        for (int i = 0; i < op2.getBitArrayLength(); i++) {
//                            bArray[i] = new Bit(op2.getBit(i).getValue());
//                        }
//
//                        if (!op2.checkIfZero()) { // if op2 is zero, there is no need to negate the number to subtract
//                            // using twos complement on op2 to negate the number
//                            for (int j = 0; j < bArray.length; j++) {
//                                bArray[j] = bArray[j].not();
//                            }
//                            int k = bArray.length - 1;
//                            while (k >= 0 && bArray[k].getValue()) {
//                                bArray[k] = bArray[k].not();
//                                k--;
//                            }
//                            bArray[k] = bArray[k].not(); // adding one
//                        }
//
//                        // adding the numbers -> a + -b => op1 + -op2 => op1 + (not(op2) + 1)
//                        Word nWord = new Word(bArray);
//                        Word resultWord = add2(op1, nWord);
//                        for (int i = 0; i < result.getBitArrayLength(); i++) {
//                            result.setBit(i, new Bit(resultWord.getBit(i).getValue()));
//                        }
//                    }
//                }
//            }
//        } else {
//            // TODO throw exception because function does not exist
//        }
//    }

    Word add2(Word a, Word b) {
        Word resultWord = new Word();
        Bit carryInSub = new Bit();
        for (int i = a.getBitArrayLength() - 1; i >= 0; i--) {
            Bit sum = a.getBit(i).xor(b.getBit(i)).xor(carryInSub);
            resultWord.setBit(i, sum);
            Bit carryOut = a.getBit(i).and(b.getBit(i)).or((a.getBit(i).xor(b.getBit(i))).and(carryInSub));
            carryInSub = new Bit(carryOut.getValue());
        }
        return resultWord;
    }

    Word add4(Word a, Word b, Word c, Word d) { // TODO fix this
        Word resultWord = new Word();
        //        Bit carryInSub = new Bit();

        // ==== version 3 ====
//        Word resultWord1 = new Word();
//        for (int i = a.getBitArrayLength() - 1; i >= 0; i--) {
//            Bit sum = a.getBit(i).xor(b.getBit(i)).xor(carryInSub);
//            resultWord1.setBit(i, sum);
//            Bit carryOut = a.getBit(i).and(b.getBit(i)).or((a.getBit(i).xor(b.getBit(i))).and(carryInSub));
//            carryInSub = new Bit(carryOut.getValue());
//        }

//        Word resultWord2 = new Word();
//        carryInSub = new Bit();
//        for (int j = c.getBitArrayLength() - 1; j >= 0; j--) {
//            Bit sum = c.getBit(j).xor(d.getBit(j)).xor(carryInSub);
//            resultWord2.setBit(j, sum);
//            Bit carryOut = c.getBit(j).and(d.getBit(j)).or((c.getBit(j).xor(d.getBit(j))).and(carryInSub));
//            carryInSub = new Bit(carryOut.getValue());
//        }

//        carryInSub = new Bit();
//        for (int k = resultWord1.getBitArrayLength() - 1; k >= 0; k--) {
//            Bit sum = resultWord1.getBit(k).xor(resultWord2.getBit(k)).xor(carryInSub);
//            resultWord.setBit(k, sum);
//            Bit carryOut = resultWord1.getBit(k).and(resultWord2.getBit(k)).or((resultWord1.getBit(k).xor(resultWord2.getBit(k))).and(carryInSub));
//            carryInSub = new Bit(carryOut.getValue());
//        }

//        Word resultWord1 = new Word();
//        Word resultWord2 = new Word();
//        Bit carryInSub1 = new Bit();
//        for (int i = a.getBitArrayLength() - 1; i >= 0; i--) {
//            Bit sum1 = a.getBit(i).xor(b.getBit(i)).xor(carryInSub1);
//        }


        // ==== version 4 ====
        // (r1).and(r2).or(((r1).xor(r2)).and(c3))

        Bit carryInSub1 = new Bit();
        Bit carryInSub2 = new Bit();
        Bit carryInSub3 = new Bit();
        for (int i = a.getBitArrayLength() - 1; i >= 0; i--) {
            Bit sum = a.getBit(i).xor(b.getBit(i)).xor(c.getBit(i)).xor(d.getBit(i)).xor(carryInSub1).xor(carryInSub2).xor(carryInSub3);
            resultWord.setBit(i, sum);

            Bit carryOut1 = a.getBit(i).and(b.getBit(i)).or((a.getBit(i).xor(b.getBit(i))).and(carryInSub1));
            carryInSub1 = new Bit(carryOut1.getValue());

            Bit carryOut2 = c.getBit(i).and(d.getBit(i)).or((c.getBit(i).xor(d.getBit(i))).and(carryInSub2));
            carryInSub2 = new Bit(carryOut2.getValue());

            Bit carryOut3 = (a.getBit(i).xor(b.getBit(i)).xor(carryInSub1)).and(c.getBit(i).xor(d.getBit(i)).xor(carryInSub2)).or(((a.getBit(i).xor(b.getBit(i)).xor(carryInSub1)).xor(c.getBit(i).xor(d.getBit(i)).xor(carryInSub2))).and(carryInSub3));
            carryInSub3 = new Bit(carryOut3.getValue());
        }

        return resultWord;
    }
}
