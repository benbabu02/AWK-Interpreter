package csi404.assignment7;

public class Word {

    Bit[] bitArray;

    public Word() {
        bitArray = new Bit[32];
        for (int i = 0; i < bitArray.length; i++) {
            bitArray[i] = new Bit();
        }
    }

    public Word(Bit[] bArray) {
        bitArray = new Bit[32];
        for (int i = 0; i < bitArray.length; i++) {
            bitArray[i] = new Bit(bArray[i].getValue());
        }
    }

    public Word(String binaryRep) {
        bitArray = new Bit[32];
        for (int i = 0; i < bitArray.length; i++) {
            if (binaryRep.charAt(i) == '0' || binaryRep.charAt(i) == 'f') {
                bitArray[i] = new Bit(false);
            } else {
                bitArray[i] = new Bit(true);
            }
        }
    }

    int getBitArrayLength() {
        return bitArray.length;
    }

    Bit[] getBitArray() { return bitArray; }

    // get a new Bit that has the same value as bit i
    Bit getBit(int i) {
        return new Bit(bitArray[i].getValue());
    }

    // set bit i's value
    void setBit(int i, Bit value) {
        bitArray[i] = new Bit(value.getValue());
    }

    // and two words, returning a new Word
    Word and(Word other) {
        Word newWord = new Word();
        newWord.setBit(1, new Bit(true));
        for (int i = 0; i < bitArray.length; i++) { // loops through bitArray and compares (and) each bit with the corresponding bit in the same index from the other word
            newWord.setBit(i, bitArray[i].and(other.getBit(i)));
        }
        return newWord;
    }

    // or two words, returning a new Word
    Word or(Word other) {
        Word newWord = new Word();
        newWord.setBit(1, new Bit(true));
        for (int i = 0; i < bitArray.length; i++) { // loops through bitArray and compares (or) each bit with the corresponding bit in the same index from the other word
            newWord.setBit(i, bitArray[i].or(other.getBit(i)));
        }
        return newWord;
    }

    // xor two words, returning a new Word
    Word xor(Word other) {
        Word newWord = new Word();
        newWord.setBit(1, new Bit(true));
        for (int i = 0; i < bitArray.length; i++) { // loops through bitArray and compares (xor) each bit with the corresponding bit in the same index from the other word
            newWord.setBit(i, bitArray[i].xor(other.getBit(i)));
        }
        return newWord;
    }

    // negate this word, creating a new Word
    Word not() {
        Word newWord = new Word();
        newWord.setBit(1, new Bit(true));
        for (int i = 0; i < bitArray.length; i++) { // loops through bitArray and applies not on each bit
            newWord.setBit(i, bitArray[i].not());
        }
        return newWord;
    }

    // right shift this word by amount bits, creating a new Word
    Word rightShift(int amount) {
        Word newWord = new Word();
        for (int i = amount; i < bitArray.length; i++) {
            newWord.setBit(i, bitArray[i-amount]);
        }
        return newWord;
    }

    // left shift this word by amount bits, creating a new Word
    Word leftShift(int amount) {
        Word newWord = new Word();
        for (int i = 0; i < bitArray.length-amount; i++) {
            newWord.setBit(i, bitArray[i+amount]);
        }
        return newWord;
    }

    // returns a comma separated string t's and f's
    @Override
    public String toString() {
        String returnStr = "";
        for (int i = 0; i < bitArray.length; i++) {
            if (bitArray[i].getValue()) {
                returnStr += "t";
            } else {
                returnStr += "f";
            }
            if (i != bitArray.length-1) {
                returnStr += ", ";
            }
        }
        return returnStr;
    }

    public String getBinaryRepresentation() {
        String returnStr = "";
        for (int i = 0; i < bitArray.length; i++) {
            if (bitArray[i].getValue()) {
                returnStr += "1";
            } else {
                returnStr += "0";
            }
        }
        return returnStr;
    }

    // returns the value of this word as a long
    long getUnsigned() {
        long returnNum = 0;
        int power = 32;
        for (int i = 0; i < 32; i++) {
            if (bitArray[i].getValue()) {
                if (power == 0) {
                    returnNum++;
                } else {
                    returnNum += (long) Math.pow(2, power - 1);
                }
            }
            power--;
        }
        return returnNum;
    }

    // returns the value of this word as an int
    int getSigned() {
        // copying bitArray into a new array to undo twos complement
        Bit[] bArray = new Bit[32];
        for (int i = 0; i < bitArray.length; i++) {
            bArray[i] = new Bit(bitArray[i].getValue());
        }

        // setting up for changing a number between positive and negative
        int multiplier = 1;
        if (bitArray[0].getValue()) { // if bitArray[0] is true, number is negative
            multiplier *= -1;

            // twos complement to take care of negative numbers
            for (int j = 0; j < bArray.length; j++) {
                bArray[j] = bArray[j].not();
            }
            int k = bArray.length-1;
            while (k >= 0 && bArray[k].getValue()) {
                bArray[k] = bArray[k].not();
                k--;
            }
            bArray[k] = bArray[k].not(); // adding one
        }

        // getting the unsigned number
        int returnNum = 0;
        int power = 32;
        for (int i = 0; i < 32; i++) {
            if (bArray[i].getValue()) {
                if (power == 0) {
                    returnNum++;
                } else {
                    returnNum += (int) Math.pow(2, power - 1);
                }
            }
            power--;
        }
        return returnNum * multiplier;
    }

    // copies the values of the bits from another Word into this one
    void copy(Word other) {
        for (int i = 0; i < other.bitArray.length; i++) {
            bitArray[i] = new Bit(other.getBit(i).getValue());
        }
    }

    // set the value of the bits of this Word (used for tests)
    void set(int value) {
        for (Bit bit : bitArray) {      // make sure bitArray is cleared and empty
            bit.clear();
        } 

        int tempValue = Math.abs(value);
        int i = bitArray.length-1;
        while (tempValue > 0) { // converting the number to binary using mod
            if (tempValue % 2 == 0) {
                bitArray[i] = new Bit();
            } else {
                bitArray[i] = new Bit(true);
            }
            tempValue /= 2;
            i--;
        }

        if (value < 0) {
            for (int j = 0; j < bitArray.length; j++) { // twos complement to take care of negative numbers
                bitArray[j] = bitArray[j].not();
            }
            int k = bitArray.length-1;
            while (bitArray[k].getValue()) {
                bitArray[k] = bitArray[k].not();
                k--;
            }
            bitArray[k] = bitArray[k].not(); // adding one
        }
    }

    /*
     * Checks if the value of the word is zero
     * returns true is value is zero, else false
     */
    boolean checkIfZero() {
        for (int i = 0; i < getBitArrayLength(); i++) {
            if (getBit(i).getValue()) {
                return false;
            }
        }
        return true;
    }

    Word increment() { // adds one to word (+1)
        Word resultWord = new Word();
        Word one = new Word();
        one.set(1);
        Bit carryInSub = new Bit();

        for (int i = getBitArrayLength() - 1; i >= 0; i--) {
            Bit sum = bitArray[i].xor(one.getBit(i)).xor(carryInSub);
            resultWord.setBit(i, sum);
            Bit carryOut = bitArray[i].and(one.getBit(i)).or((bitArray[i].xor(one.getBit(i))).and(carryInSub));
            carryInSub = new Bit(carryOut.getValue());
        }
        return resultWord;
    }

    Word decrement() {
        Word resultWord = new Word();
        Word one = new Word();
        one.set(-1);
        Bit carryInSub = new Bit();

        for (int i = getBitArrayLength() - 1; i >= 0; i--) {
            Bit sum = bitArray[i].xor(one.getBit(i)).xor(carryInSub);
            resultWord.setBit(i, sum);
            Bit carryOut = bitArray[i].and(one.getBit(i)).or((bitArray[i].xor(one.getBit(i))).and(carryInSub));
            carryInSub = new Bit(carryOut.getValue());
        }
        return resultWord;
    }
}
