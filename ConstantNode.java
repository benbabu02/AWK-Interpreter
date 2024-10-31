/*
 * The Constant Node class which implements a Node.
 *
 * @author Benjamin Babu (bbabu@albany.edu)
 */
public class ConstantNode extends Node {
    String stringValue;
    public ConstantNode(String inputString) {
        this.stringValue = inputString;
    }

    String getStr() {
        return stringValue;
    }

    @Override
    public String toString() {
        return "ConstantNode: " + stringValue + " ";
    }
}
