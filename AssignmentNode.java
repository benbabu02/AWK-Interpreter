/*
 * The Assignment Node class which extends a StatementNode.
 *
 * @author Benjamin Babu (bbabu@albany.edu)
 */
public class AssignmentNode extends StatementNode {
    Node target;
    Node expression;

    public AssignmentNode(Node target, Node expression) {
        this.target = target;
        this.expression = expression;
    }

    Node getTarget() {
        return target;
    }

    Node getExpression() {
        return expression;
    }

    @Override
    public String toString() {
        return "AssignmentNode: " + target + " " + expression + " ";
    }
}
