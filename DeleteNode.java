/*
 * The Delete Node class which extends a Statement Node.
 *
 * @author Benjamin Babu (bbabu@albany.edu)
 */
public class DeleteNode extends StatementNode {
    Node condition;

    public DeleteNode(Node condition) {
        this.condition = condition;
    }

    Node getCondition() {
        return condition;
    }

    @Override
    public String toString() {
        return "delete(" + condition + ")";
    }
}
