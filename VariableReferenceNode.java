import java.util.Optional;

/*
 * The Variable Reference Node class which implements a Node.
 *
 * @author Benjamin Babu (bbabu@albany.edu)
 */
public class VariableReferenceNode extends Node {
    String name;
    Optional<Node> expression;

    public VariableReferenceNode(String inputName) {
        this.name = inputName;
    }
    public VariableReferenceNode(String inputName, Optional<Node> inputExpression) {
        this.name = inputName;
        this.expression = inputExpression;
    }

    String getName() {
        return name;
    }

    Optional<Node> getExpression() {
        return expression;
    }

    @Override
    public String toString() {
        if (expression == null) { // checks if null since expression is optional
            return "VariableReferenceNode: " + name;
        }
        return "VariableReferenceNode: " + name + ", Expression: "+ expression.get();
    }
}
