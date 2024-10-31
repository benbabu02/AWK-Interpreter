/*
 * A StringHandler class which implements methods
 * that can be used to work through a string input.
 *
 * @author Benjamin Babu (bbabu@albany.edu)
 */
public class StringHandler {
    private String awkFile;
    private int index;

    public StringHandler(String awkFile) {
        this.awkFile = awkFile;
        this.index = 0;
    }

    /*
     * This method is used to look "i" characters ahead
     * and returns that character without moving the index.
     *
     * @return char
     */
    char Peek(int i) { return awkFile.charAt(index + i); }

    /*
     * This method is used to return a string of the next
     * "i" characters without moving the index.
     *
     * @return String
     */
    String PeekString(int i) { return awkFile.substring(index, index + i); }

    /*
     * This method is used to return the next character
     * in the string while moving the index.
     *
     * @return char
     */
    char GetChar() {
        int oldIndex = index;
        index++;
        return awkFile.charAt(oldIndex);
    }

    /*
     * This method is used to return the length of the input file.
     *
     * @return int
     */
    int GetFileLength() { return awkFile.length(); }

    /*
     * This method is used to move the index ahead by
     * "i" positions.
     */
    void Swallow(int i) { index += i; }

    /*
     * This method is used to check if we are at
     * the end of the document.
     *
     * @return boolean
     */
    boolean IsDone() { return index >= awkFile.length(); }

    /*
     * This method is used to check if peeking "i"
     * characters ahead will cause an index out of bounds.
     *
     * @return boolean
     */
    boolean IsPeekDone(int i) { return index + i >= awkFile.length(); }

    /*
     * This method is used to return the rest of the
     * document as a string.
     *
     * @return String
     */
    String Remainder() { return awkFile.substring(index); }
}
