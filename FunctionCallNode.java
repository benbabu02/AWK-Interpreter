import java.util.LinkedList;

/*
 * The Function Call Node class which extends a Statement Node.
 *
 * @author Benjamin Babu (bbabu@albany.edu)
 */
public class FunctionCallNode extends StatementNode {
    Token functionName;
    LinkedList<Node> parameters;

    public FunctionCallNode(Token functionName, LinkedList<Node> parameters) {
        this.functionName = functionName;
        this.parameters = parameters;
    }

    String getName() {
        return functionName.getTokenValue();
    }

    @Override
    public String toString() {
        return "FunctionCallNode: " + functionName + " " + parameters.toString();
    }
}
