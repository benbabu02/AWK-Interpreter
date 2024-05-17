package csi404.assignment7;

public class MainMemory {
    static Word[] memoryArr = new Word[1024];

    public static void fillInMemory() { // loops through memory, initializing the word at each index
        for (int i = 0; i < memoryArr.length; i++) {
            memoryArr[i] = new Word();
        }
    }

    public static Word read(Word address) {
        int index = (int) address.getUnsigned(); // index in memory to access
        if (memoryArr[index] == null) { // initialize a new word if index in memory does not exist
            memoryArr[index] = new Word();
        }
        return memoryArr[index];
    }

    public static void write(Word address, Word value) {
        int index = (int) address.getUnsigned();  // index in memory to access
        memoryArr[index] = new Word();  // initializing/clearing the index in memory to write to it
        for (int i = 0; i < value.getBitArrayLength(); i++) { // setting each bit from value to the word at index in memory
            memoryArr[index].setBit(i, value.getBit(i));
        }
    }

    public static void load(String[] data) {
        for (int i = 0; i < data.length; i++) {
            memoryArr[i] = new Word();
            if (data[i] == null) {
                continue;
            }
            String[] tokens = data[i].split(""); // splits the string into an array of 0s and 1s
            for (int j = 0; j < tokens.length; j++) {
                if (tokens[j].equalsIgnoreCase("0")) { // if the current character is a 0, then the bit is false
                    memoryArr[i].bitArray[j].set(false);
                } else { // if the current character is a 1, then the bit is true
                    memoryArr[i].bitArray[j].set(true);
                }
            }
        }
    }
}
