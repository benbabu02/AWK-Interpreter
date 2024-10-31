import java.util.Optional;

/*
 * The Operation Node class which implements a Node.
 *
 * @author Benjamin Babu (bbabu@albany.edu)
 */
public class OperationNode extends Node {
    Node left;
    Optional<Node> right;
    OperationList thisEnum;

    enum OperationList{
        EQ, NE, LT, LE, GT, GE, AND, OR, NOT, MATCH, NOTMATCH, DOLLAR, PREINC, POSTINC, PREDEC, POSTDEC, UNARYPOS,
        UNARYNEG, IN, EXPONENT, ADD, SUBTRACT, MULTIPLY, DIVIDE, MODULO, CONCATENATION, ASSIGN
    }

    public OperationNode(Node inputLeft, OperationList inputEnum) {
        this.left = inputLeft;
        this.thisEnum = inputEnum;
    }

    public OperationNode(Node inputLeft, Optional<Node> inputRight, OperationList inputEnum) {
        this.left = inputLeft;
        this.right = inputRight;
        this.thisEnum = inputEnum;
    }

    Node getLeft() {
        return left;
    }

    Optional<Node> getRight() {
        return right;
    }

    @Override
    public String toString() {
        if (right == null || right.isEmpty()) { //checks for null since right is optional
            return "OperationNode: " + left + ", Enum: " + thisEnum + " ";
        }
        return "OperationNode: " + left + ", Enum: " + thisEnum + " , " + right;
    }
}
