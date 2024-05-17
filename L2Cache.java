package csi404.assignment7;

public class L2Cache {
    private static Word[] cacheArr = new Word[32]; // cache size is 32 words
    private static Word[] givenAddresses = new Word[4]; // used to store each group of 8 words

    public static void fillInCache(Word address) { // fills the cache with 32 words starting from the given address
        int groupIndex = (int) (address.getUnsigned() / 8);
        givenAddresses[groupIndex] = address; // store the address tag for this group
        for (int i = 0; i < 8; i++) {
            cacheArr[groupIndex * 8 + i] = MainMemory.read(new Word((address.increment()).getBinaryRepresentation()));
        }
    }

    public static Word read(Word address) {
        int groupIndex = (int) (address.getUnsigned() / 8);
        Word addressTag = givenAddresses[groupIndex];
        if (address.equals(addressTag)) {
            return cacheArr[(int) (address.getUnsigned() - addressTag.getUnsigned())];
        } else {
            fillInCache(address);
            return cacheArr[groupIndex * 8]; // returning the first word from the cache group after filling it
        }
    }
}
