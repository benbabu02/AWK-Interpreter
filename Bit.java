package csi404.assignment7;

public class Bit {

    private Boolean bitValue;

    Bit() {
        bitValue = false;
    }

    Bit(Boolean value) {
        bitValue = value;
    }

    // sets the value of the bit
    void set(Boolean value) {
        bitValue = value;
    }

    // changes the value from true to false or false to true
    void toggle() {
        if (bitValue) {
            bitValue = false;
        } else {
            bitValue = true;
        }
    }

    // sets the bit to true
    void set() {
        bitValue = true;
    }

    // sets the bit to false
    void clear() {
        bitValue = false;
    }

    // returns the current value
    Boolean getValue() {
        return bitValue;
    }

    // performs and on two bits and returns a new bit set to the result
    Bit and(Bit other) {
        // true iff true and true
        // else false

        // check if current bit is true
            // check if other bit is true
                // set new bit to true
                // return new bit
        // set new bit to false
        // return new bit

        Bit newBit = new Bit();
        if (bitValue) {
            if (other.getValue()) {
                newBit.set();
            }
        }
        return newBit;
    }

    // performs or on two bits and returns a new bit set to the result
    Bit or(Bit other) {
        // false iff false and false
        // else true

        // check if current bit is false
        // check if other bit is false
        // set new bit to true
        // return new bit
        // set new bit to false
        // return new bit

        Bit newBit = new Bit();
        if (!bitValue) {
            if (!other.getValue()) {
                return newBit;
            }
        }
        newBit.set();
        return newBit;
    }

    // performs xor on two bits and returns a new bit set to the result
    Bit xor(Bit other) {
        // true if false and true
        // true if true and false
        // else false

        // check if current bit is true
        // check if other bit is true
        // set new bit to true
        // return new bit
        // set new bit to false
        // return new bit

        Bit newBit = new Bit();
        if (bitValue) {
            if (!other.getValue()) {
                newBit.set();
            }
        } else {
            if (other.getValue()) {
                newBit.set();
            }
        }
        return newBit;
    }

    // performs not on the existing bit, returning the result as a new bit
    Bit not() {
        // if currentBit is false, set new bit to true
        // if currentBit is true, set new bit to false

        Bit newBit = new Bit();
        if (!bitValue) {
            newBit.set();
        }
        return newBit;
    }

    // returns "t" or "f"
    @Override
    public String toString() {
        if (bitValue) {
            return "t";
        } else {
            return "f";
        }
    }
}