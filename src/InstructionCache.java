package csi404.assignment7;

public class InstructionCache {
    private static Word[] cacheArr = new Word[8]; // 8 words
    private static Word givenAddress; // additional word

    InstructionCache() {
        for (int i = 0; i < cacheArr.length; i++) {
            cacheArr[i] = new Word();
        }
    }

    public static void fillInCache(Word address) { // fills the cache with 8 words starting from the given address
        for (int i = 0; i < cacheArr.length; i++) {
            cacheArr[i] = MainMemory.read(new Word((address.increment()).getBinaryRepresentation()));
        }
    }

    public static Word read(Word address) {
        if (address.equals(givenAddress)) {
            return cacheArr[(int) (address.getUnsigned() - givenAddress.getUnsigned())];
        } else {
            fillInCache(address);
            return cacheArr[0]; // Returning the first word from the cache after filling it
        }
    }
}
