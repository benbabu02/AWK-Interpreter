/*
 * The Pattern Node class which implements a Node.
 *
 * @author Benjamin Babu (bbabu@albany.edu)
 */
public class PatternNode extends Node {
    String stringValue;
    public PatternNode(String inputString) {
        this.stringValue = inputString;
    }

    String getPattern() {
        return stringValue;
    }

    @Override
    public String toString() {
        return "PatternNode: " + stringValue;
    }
}
