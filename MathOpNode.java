import java.util.Optional;

/*
 * The Math Operation Node class which implements Node.
 *
 * @author Benjamin Babu (bbabu@albany.edu)
 */
public class MathOpNode extends Node {
    Node left;
    Optional<Node> right;
    Optional<Token> opType;

    public MathOpNode(Node left, Optional<Token> opType, Optional<Node> right) {
        this.left = left;
        this.opType = opType;
        this.right = right;
    }

    @Override
    public String toString() {
        return "MathOpNode: " + left.toString() + opType.get() + right.toString();
    }
}
