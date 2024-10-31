import java.util.Optional;

/*
 * The Ternary Node class which extends a Statement Node.
 *
 * @author Benjamin Babu (bbabu@albany.edu)
 */
public class TernaryNode extends Node {

    Node checkCase;
    Optional<Node> trueCase;
    Optional<Node> falseCase;


    public TernaryNode(Node checkCase, Optional<Node> trueCase, Optional<Node> falseCase) {
        this.checkCase = checkCase;
        this.trueCase = trueCase;
        this.falseCase = falseCase;
    }

    @Override
    public String toString() {
        return "TernaryNode: " + checkCase + " , " + trueCase + " , " + falseCase + " ";
    }
}
